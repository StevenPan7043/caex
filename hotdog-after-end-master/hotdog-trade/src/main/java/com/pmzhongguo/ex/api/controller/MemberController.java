package com.pmzhongguo.ex.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.bean.SessionInfo;
import com.pmzhongguo.ex.business.dto.*;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.model.NoticeContentBuilder;
import com.pmzhongguo.ex.business.service.manager.MemberServiceManager;
import com.pmzhongguo.ex.business.service.*;
import com.pmzhongguo.ex.business.vo.MemberVo;
import com.pmzhongguo.ex.core.gt.GeetestConfig;
import com.pmzhongguo.ex.core.gt.GeetestLib;
import com.pmzhongguo.ex.core.sms.ISmsService;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.*;
import com.pmzhongguo.ex.core.web.resp.LstResp;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.entity.FrmConfig;
import com.pmzhongguo.ex.framework.service.FrmUserService;
import com.pmzhongguo.otc.faceid.FaceIdUtil;
import com.pmzhongguo.otc.otcenum.MobiInfoTemplateEnum;
import com.pmzhongguo.otc.sms.JuheInternationalSend;
import com.pmzhongguo.otc.sms.JuheSend;
import com.pmzhongguo.otc.sms.JuheUtil;
import com.pmzhongguo.otc.sms.SmsSendPool;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.zzextool.utils.StringUtil;
import com.qiniu.conts.CacheConst;
import com.qiniu.util.BeanUtil;
import com.qiniu.util.StringUtils;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(value = "????????????", description = "????????????????????????????????????", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class MemberController extends TopController {
    private static Logger logger = LoggerFactory.getLogger(MemberController.class);

    /**
     * ?????????????????????????????????????????????
     */
    private static final int FREQUENCY_TIME = 60 * 5;

    /**
     * ???????????????????????????
     */
    private static final int FREQUENCY_COUNT = 10;

    /**
     * ?????????????????????????????????
     */
    private static final String SMS_FREQUENCY_LIMIT_ = "sms_frequency_limit_";

    public static final String FACEID_CACHE_KEY = "faceid_cache_key";

    private static Map<String, String> freeMnameMap = new HashMap<String, String>();

    private static final String LOGIN_FREE_VERIFICATION = "login_free_verification";

    static {
        String freeMname = PropertiesUtil.getPropValByKey(LOGIN_FREE_VERIFICATION);
        if (!HelpUtils.nullOrBlank(freeMname)) {
            String[] arr = freeMname.split("\\|");
            for (String mname : arr) {
                logger.info("????????????????????????{}", mname);
                freeMnameMap.put(mname, mname);
            }
        }

    }

    @Autowired
    private ReturnCommiService returnCommiService;


    @Autowired
    CurrencyLockAccountService currencyLockAccountService;

    @Autowired
    CurrencyLockReleaseService currencyLockReleaseService;

    @Autowired
    protected ApiAccessLimitService apiAccessLimitService;

    @Resource
    private WarehouseAccountService warehouseAccountService;

    @Autowired
    private FrmUserService frmUserService;

    @Autowired
    private MemberServiceManager memberServiceManager;

    @ApiOperation(value = "????????????", notes = "???????????????m_name??????????????????m_pwd???????????????sms_code?????????/?????????????????????introduce_m_id????????????ID???", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/member", method = RequestMethod.POST)
    @ResponseBody
    public Resp addMember(HttpServletRequest request,
                          HttpServletResponse response, @RequestBody Member member) throws Exception {
        // ??????????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getM_name())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_MAIL_NULL_TIP.getErrorENMsg());
        }

        // ????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getM_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_NULL_TIP.getErrorENMsg());
        }

        // ?????????????????????????????????????????????86
        if (HelpUtils.nullOrBlank(member.getArea_code())) {
            member.setArea_code(AreaCodeEnum.CH.getAreaCode());
        }
        member.setM_name(member.getM_name().toLowerCase()); // ???????????????

        if (member.getCheckeds() == 1) {
            ObjResp resp = fromReg(null, request, member.getM_name());
            if (resp.getState() != 1) {
                return resp;
            }
            member.setSms_code(String.valueOf(resp.getData()));
        }
        // ???????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }

        return memberServiceManager.handlerRegister(member);


    }

//	@ApiOperation(value = "????????????", notes = "???????????????m_name??????????????????m_pwd???????????????sms_code?????????/?????????????????????introduce_m_id????????????ID???", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
//	@RequestMapping(value = "m/member", method = RequestMethod.POST)
//	@ResponseBody
//	public Resp addMember(HttpServletRequest request,
//						  HttpServletResponse response, @RequestBody Member member) throws Exception {
//		// ??????????????????????????????????????????
//		if (HelpUtils.nullOrBlank(member.getM_name())) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_MAIL_NULL_TIP.getErrorENMsg());
//		}
//		// ???????????????????????????????????????
//		if (HelpUtils.nullOrBlank(member.getSms_code())) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
//		}
//		// ????????????????????????????????????
//		if (HelpUtils.nullOrBlank(member.getM_pwd())) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_NULL_TIP.getErrorENMsg());
//		}
//
//		// ?????????????????????????????????????????????86
//		if (HelpUtils.nullOrBlank(member.getArea_code())){
//			member.setArea_code(AreaCodeEnum.CH.getAreaCode());
//		}
//		member.setM_name(member.getM_name().toLowerCase()); // ???????????????
//
//		Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("m_name", member.getM_name())); //????????????????????????
//		if (null != oldMember && oldMember.getM_status() == 1) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ALREADY_REG_FINDPWD.getErrorENMsg());
//		} else if (null != oldMember && oldMember.getM_status() == 2) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg());
//		}
//		// ????????????????????????????????????
//		if (null == oldMember || !member.getSms_code().equals(oldMember.getSms_code())) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
//		}
//
//		// ????????????
//		member.setM_pwd(MacMD5.CalcMD5Member(member.getM_pwd()));
//		member.setId(oldMember.getId());
//		member.setM_status(1);
//		//???????????????????????????????????????????????????????????????????????????
//		member.setSms_code(HelpUtils.randomNumNo0(6));
//
//		// ?????????????????????????????????
//		if (!HelpUtils.nullOrBlank(member.getIntroduce_m_id()) && !member.getIntroduce_m_id().equals(String.valueOf(oldMember.getId()))) {
//			//??????
//			String introMId = member.getIntroduce_m_id().trim().replace(HelpUtils.PRE_INTRODUCE_ID, "");
//			//??????
//			introMId = introMId.replace(HelpUtils.PRE_INTRODUCE_ID.toUpperCase(), "");
//			Integer iIntroMId = 0;
//			try {
//				iIntroMId = Integer.parseInt(introMId);
//				// ????????????id?????????????????????????????????
//                Member introMember = memberService.getMemberById(iIntroMId);
//                iIntroMId = introMember == null ? 0 : introMember.getId();
//            } catch (Exception e) {
//                logger.warn("< ==== ???????????????????????? {} ???" ,e.fillInStackTrace());
//				iIntroMId = 0;
//			}
//			member.setIntroduce_m_id(iIntroMId+"");
//
//		} else {
//			member.setIntroduce_m_id("0");
//		}
//
//		// ???????????????????????????????????????????????????
//		if (!"0".equals(member.getIntroduce_m_id())) {
//			// ??????????????????
//
//			boolean result = returnCommiService.isExistByMemberIdAndIntroduceId(member);
//			if (!result) {
//				returnCommiService.add(member);
//			}
//		}
//
//		memberService.updateMember(member, true, false);
//
//		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
//	}


    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "m/getCountrylist", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getCountrylist(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, request.getServletContext().getAttribute(HelpUtils.COUNTRYLIST));
    }

    @ApiOperation(value = "????????????", notes = "???????????????m_name???????????????, m_pwd???????????????sms_code?????????/???????????????)", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/forgotV2", method = RequestMethod.POST)
    @ResponseBody
    public Resp forgotV2(HttpServletRequest request,
                         HttpServletResponse response, @RequestBody Member member) throws Exception {

        // ??????????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getM_name())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_MAIL_NULL_TIP.getErrorENMsg());
        }
        // ???????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }
        // ????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getM_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_NULL_TIP.getErrorENMsg());
        }

        member.setM_name(member.getM_name().toLowerCase()); // ???????????????

        Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("m_name", member.getM_name()));
        if (null != oldMember && oldMember.getM_status() == 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PLEASE_LOGIN_AFTER_REGISTRATION.getErrorENMsg());
        } else if (null != oldMember && oldMember.getM_status() == 2) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg());
        }
        // ????????????????????????????????????
        if (null == oldMember || HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !member.getSms_code().equals(oldMember.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        // ??????????????????
        member.setM_pwd(MacMD5.CalcMD5Member(member.getM_pwd()));
        member.setId(oldMember.getId());
        //member.setLast_mod_pwd_time(HelpUtils.formatDate8(new Date()));
        member.setSms_code(HelpUtils.randomNumNo0(6)); //?????????????????????????????????????????????
        memberService.updateMember(member, false, false);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "????????????", notes = "???????????????m_security_pwd???????????????,m_pwd????????????,sms_code?????????/??????????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/resetPwd", method = RequestMethod.POST)
    @ResponseBody
    public Resp resetPwd(HttpServletRequest request,
                         HttpServletResponse response, @RequestBody Member member) throws Exception {

        Member m = JedisUtilMember.getInstance().getMember(request, member.getToken());
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // ???????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }
        // ????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getM_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_NULL_TIP.getErrorENMsg());
        }

        // ????????????????????????????????????
        Member oldMember = memberService.getMemberById(m.getId());
        if (null == oldMember) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }
        if (HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !member.getSms_code().equals(oldMember.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        //????????????????????????????????????????????????m_security_pwd???????????????
        if (HelpUtils.nullOrBlank(member.getM_security_pwd()) ||
                !MacMD5.CalcMD5Member(member.getM_security_pwd()).equals(oldMember.getM_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_OLD_PWD_ERR_TIP.getErrorENMsg());
        }

        // ??????????????????
        member.setM_pwd(MacMD5.CalcMD5Member(member.getM_pwd()));

        if (!HelpUtils.nullOrBlank(oldMember.getM_security_pwd()) && oldMember.getM_security_pwd().equals(member.getM_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_SAME_TIP.getErrorENMsg());
        }

        member.setId(oldMember.getId());
        //member.setLast_mod_pwd_time(HelpUtils.formatDate8(new Date()));
        member.setSms_code(HelpUtils.randomNumNo0(6)); //?????????????????????????????????????????????
        memberService.updateMember(member, false, false);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "??????????????????", notes = "???????????????m_security_pwd?????????????????????sms_code?????????/??????????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/setSecPwd", method = RequestMethod.POST)
    @ResponseBody
    public Resp setSecPwd(HttpServletRequest request,
                          HttpServletResponse response, @RequestBody Member member) throws Exception {

        Member m = JedisUtilMember.getInstance().getMember(request, member.getToken());
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // ???????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }
        // ????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getM_security_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_NULL_TIP.getErrorENMsg());
        }

        // ????????????????????????????????????
        Member oldMember = memberService.getMemberById(m.getId());
        if (null == oldMember || HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !member.getSms_code().equals(oldMember.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        // ??????????????????
        String unEncryptionSecurityPwd = member.getM_security_pwd();
        member.setM_security_pwd(MacMD5.CalcMD5Member(member.getM_security_pwd()));

        if (oldMember.getM_pwd().equals(member.getM_security_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_SAME_TIP.getErrorENMsg());
        }


        member.setId(oldMember.getId());
        if (!HelpUtils.nullOrBlank(oldMember.getM_security_pwd())) {
            member.setLast_mod_pwd_time(HelpUtils.formatDate8(new Date()));
        }
        member.setSms_code(HelpUtils.randomNumNo0(6)); //?????????????????????????????????????????????
        memberServiceManager.updateContractMember(member, false, false, unEncryptionSecurityPwd);

        // ?????????????????????????????????session
        m.setM_security_pwd("1");
        m.setLast_mod_pwd_time(HelpUtils.formatDate8(new Date()));
        JedisUtilMember.getInstance().setMember(request, m);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "??????????????????", notes = "??????????????????", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/modifyNickName/{id}/{m_nick_name}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getMember(HttpServletRequest request,
                             HttpServletResponse response, @PathVariable Integer id, @PathVariable String m_nick_name) {

        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        if (id.intValue() != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ID.getErrorENMsg(), null);
        }
        Member updateDto = new Member();
        updateDto.setId(id);
        updateDto.setM_nick_name(m_nick_name);
        memberService.updateMember(updateDto, false, false);
        m.setM_nick_name(m_nick_name);
        JedisUtilMember.getInstance().setMember(request, m);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    /**
     * ????????????/???????????????
     * ???????????????????????????????????????????????????????????????????????????
     *
     * @param request
     * @param response
     * @param m_name
     * @param from
     * @param check_code_token
     * @param check_code
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "????????????/???????????????", notes = "????????????/???????????????,from??????login,forgot???reg,check??????????????????????????????????????????,????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/mail/{check_code_token}/{check_code}/{m_name}/{from}", method = RequestMethod.GET)
    @ResponseBody
    public Resp mail(HttpServletRequest request,
                     HttpServletResponse response, @PathVariable String m_name, @PathVariable String from, @PathVariable String check_code_token, @PathVariable String check_code) throws Exception {

        // ???????????????????????????
        boolean frequencyLimit = frequencyLimit(request, from);
        if (frequencyLimit) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_LIMIT_FREQUENCY_OPERTION.getErrorENMsg());
        }
        // ??????????????????????????????????????????
        if (HelpUtils.nullOrBlank(m_name)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_MAIL_NULL_TIP.getErrorENMsg());
        }
        // ???????????????????????????from
        if (HelpUtils.nullOrBlank(from)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_FROM.getErrorENMsg());
        }

        //??????????????????
        m_name = m_name.toLowerCase();
        Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("m_name", m_name));
        if (BeanUtil.isEmpty(oldMember) && SMSTypeEnum.FORGOT.getMsg().equals(from)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg());
        }
        //???????????????????????????????????????????????????member?????????????????????????????????86
        Integer areaCode = null;
        if (BeanUtil.isEmpty(oldMember)) {
            areaCode = BeanUtil.isEmpty($("area_code")) ? AreaCodeEnum.CH.getAreaCode() : Integer.valueOf($("area_code"));
        } else {
            areaCode = oldMember.getArea_code();
        }

        if (HelpUtils.isEmail(m_name)) {

        } else if (HelpUtils.isMobile(areaCode + "" + HelpUtils.removeZero(m_name))) {

        } else {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_M_NAME_ERROR_TIP.getErrorENMsg());
        }

        //app???????????????????????????
        String type = request.getParameter("type");
        if (HelpUtils.nullOrBlank(type) || !"app".equals(type)) {
            // ???????????????
            if (HelpUtils.nullOrBlank(check_code_token) || HelpUtils.nullOrBlank(check_code)) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CHECK_CODE.getErrorENMsg());
            }
            if (!check_code.equalsIgnoreCase(JedisUtil.getInstance().getCheckCode(check_code_token))) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CHECK_CODE.getErrorENMsg());
            } else {
                JedisUtil.getInstance().removeCheckCode(check_code_token);
            }
        }

        // ???????????????????????????
        String smsCode = "";
        ObjResp resp = null;

        if (SMSTypeEnum.REG.getMsg().equals(from)) {
            resp = fromReg(oldMember, request, m_name);
            if (resp.getState() != 1) {
                return resp;
            }
        } else if (SMSTypeEnum.FORGOT.getMsg().equals(from)) {

            resp = fromForgot(oldMember, smsCode);
            if (resp.getState() != 1) {
                return resp;
            }
        } else if (SMSTypeEnum.LOGIN.getMsg().equals(from)) {
            resp = fromLogin(oldMember, smsCode);
            if (resp.getState() != 1) {
                return resp;
            }
        } else if (SMSTypeEnum.CHECK.getMsg().equals(from)) {
            Member member = JedisUtilMember.getInstance().getMember(request, null);
            if (member == null) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
            }
            resp = fromCheck(member, smsCode);
            if (resp.getState() != 1) {
                return resp;
            }
        }
        smsCode = (String) resp.getData();

        boolean ret = false;
        if (HelpUtils.isEmail(m_name)) {
            NoticeContentBuilder parse
                    = new NoticeContentBuilder(MobiInfoTemplateEnum.JH_SECURITY_CODE.getType()
                    , smsCode
                    , 5);
            if (StringUtil.isNullOrBank(parse.getContent())) {
                return Resp.failMsg(ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg());
            }
            SmsSendPool.getInstance().send(new TencentMailUtil(m_name, parse.getContent()));
            ret = true;
        } else if (HelpUtils.isMobile(areaCode + "" + HelpUtils.removeZero(m_name))) {
            // ?????????
            ISmsService smsService = null;
            if (BeanUtil.isEmpty(areaCode) || areaCode.equals(AreaCodeEnum.CH.getAreaCode())) {
                // ???????????????
//                smsService = new ChuangLanSmsServiceImpl()
//                        .builder(m_name, Joiner.on(",").join(Arrays.asList(smsCode, 5)), AbstractChuangLanSmsService.CLT_CODE_TIME);

                //???????????????
                String format = String.format(MobiInfoTemplateEnum.JH_SECURITY_CODE.getCode(), smsCode, 5);
                SmsSendPool.getInstance().send(new JuheSend(m_name, MobiInfoTemplateEnum.JH_SECURITY_CODE.getType(), format));
            } else {
                String format = "";
                Integer tpl_id = -1;
                switch (areaCode) {
                    case 86:
                        format = String.format(InternationalSmsCNEnum.JUHE_CODE_TIME.getFormatCode(), smsCode, 5);
                        tpl_id = InternationalSmsCNEnum.JUHE_CODE_TIME.getType();
                        break;
                    case 1:
                    case 44:
                        format = String.format(InternationalSmsENEnum.JUHE_CODE_TIME.getFormatCode(), smsCode, 5);
                        tpl_id = InternationalSmsENEnum.JUHE_CODE_TIME.getType();
                        break;
                    case 81:
                        format = String.format(InternationalSmsJAEnum.JUHE_CODE_TIME.getFormatCode(), smsCode, 5);
                        tpl_id = InternationalSmsJAEnum.JUHE_CODE_TIME.getType();
                        break;
                    case 82:
                        format = String.format(InternationalSmsKOEnum.JUHE_CODE_TIME.getFormatCode(), smsCode, 5);
                        tpl_id = InternationalSmsKOEnum.JUHE_CODE_TIME.getType();
                        break;
                    case 7:
                        format = String.format(InternationalSmsRUEnum.JUHE_CODE_TIME.getFormatCode(), smsCode, 5);
                        tpl_id = InternationalSmsRUEnum.JUHE_CODE_TIME.getType();
                        break;
                    case 852:
                    case 886:
                        format = String.format(InternationalSmsHKEnum.JUHE_CODE_TIME.getFormatCode(), smsCode, 5);
                        tpl_id = InternationalSmsHKEnum.JUHE_CODE_TIME.getType();
                        break;
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(format) && tpl_id > 0) {
                    SmsSendPool.getInstance().send(new JuheInternationalSend(areaCode + "", m_name, tpl_id, format));
                }
            }

            ret = true;
        }

        if (!ret) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_MAIL_FAIL.getErrorENMsg());
        }
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * ??????
     *
     * @param oldMember
     * @param request
     * @param m_name
     * @return
     */
    private ObjResp fromReg(Member oldMember, HttpServletRequest request, String m_name) {
        String smsCode = "";
        if (null != oldMember && oldMember.getM_status() == 1) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ALREADY_REG_FINDPWD.getErrorENMsg(), null);
        } else if (null != oldMember && oldMember.getM_status() == 2) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg(), null);
        } else if (null == oldMember) {
            smsCode = HelpUtils.randomNumNo0(6);
            Member member = new Member();
            member.setM_name(m_name);
            // ??????????????????????????????
            member.setSms_code(smsCode);
            member.setArea_code(BeanUtil.isEmpty($("area_code")) ? AreaCodeEnum.CH.getAreaCode() : Integer.valueOf($("area_code")));
            member.setReg_time(HelpUtils.formatDate8(new Date()));
            member.setLast_login_ip(HelpUtils.getIpAddr(request));
            memberService.addMember(member);
        } else {
            smsCode = oldMember.getSms_code();
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, smsCode);
    }

    /**
     * ????????????
     *
     * @param oldMember
     * @param smsCode
     * @return
     */
    private ObjResp fromForgot(Member oldMember, String smsCode) {
        //??????
        if (null != oldMember && oldMember.getM_status() == 1) {
            smsCode = HelpUtils.randomNumNo0(6);
            Member member = new Member();
            member.setId(oldMember.getId());
            member.setSms_code(smsCode);
            member.setSms_code_time(HelpUtils.formatDate8(new Date()));
            memberService.updateMember(member, false, false);
        } else if (null != oldMember && oldMember.getM_status() == 2) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg(), null);
        } else if (null != oldMember && oldMember.getM_status() == 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg(), null);
        } else if (null == oldMember) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg(), null);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, smsCode);
    }

    /**
     * ???????????????????????????,???????????????????????????????????????????????????????????????????????????
     *
     * @param smsCode
     * @return
     */
    private ObjResp fromCheck(Member oldMember, String smsCode) {

        smsCode = HelpUtils.randomNumNo0(6);
        Member member = new Member();
        member.setId(oldMember.getId());
        member.setSms_code(smsCode);
        member.setSms_code_time(HelpUtils.formatDate8(new Date()));
        memberService.updateMember(member, false, false);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, smsCode);
    }

    /**
     * ??????
     *
     * @param oldMember
     * @param smsCode
     * @return
     */
    private ObjResp fromLogin(Member oldMember, String smsCode) {
        return fromForgot(oldMember, smsCode);
    }

    /**
     *
     * @param from login/reg/forgot
     * @param key m_name/id
     * @return true ????????????
     */
    /**
     * ??????ip?????????????????????????????????????????????redis???
     *
     * @param request
     * @return true ????????????
     */
    private boolean frequencyLimit(HttpServletRequest request, String from) {
        String ip = HelpUtils.getIpAddr(request);
        String val = (String) JedisUtil.getInstance().get(SMS_FREQUENCY_LIMIT_ + ip, true);
        if (HelpUtils.nullOrBlank(val)) {
            JedisUtil.getInstance().set(SMS_FREQUENCY_LIMIT_ + ip, String.valueOf(1), true);
            JedisUtil.getInstance().expire(SMS_FREQUENCY_LIMIT_ + ip, FREQUENCY_TIME);
            return false;
        }
        Integer count = Integer.valueOf(val);
        if (count < FREQUENCY_COUNT) {
            count++;
            JedisUtil.getInstance().set(SMS_FREQUENCY_LIMIT_ + ip, String.valueOf(count), true);
            JedisUtil.getInstance().expire(SMS_FREQUENCY_LIMIT_ + ip, FREQUENCY_TIME);
            return false;
        }

        return true;
    }


    @ApiOperation(value = "??????????????????????????????", notes = "???????????????????????????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/forgotV/{m_name}/{sms_code}", method = RequestMethod.GET)
    @ResponseBody
    public Resp forgotV(HttpServletRequest request,
                        HttpServletResponse response, @PathVariable String m_name, @PathVariable String sms_code) throws Exception {

        // ??????????????????????????????????????????
        if (HelpUtils.nullOrBlank(m_name)) {
            return new Resp(Resp.FAIL, "LANG_MAIL_NULL_TIP");
        }
        // ???????????????????????????from
        if (HelpUtils.nullOrBlank(sms_code)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }

        Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("m_name", m_name));
        if (null == oldMember) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg());
        } else if (null != oldMember && oldMember.getM_status() == 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PLEASE_LOGIN_AFTER_REGISTRATION.getErrorENMsg());
        } else if (null != oldMember && oldMember.getM_status() == 2) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg());
        } else if (HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !oldMember.getSms_code().equals(sms_code)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    private boolean sendMail(String mName, String smsCode) {
        try {
            StringBuffer mailContent = new StringBuffer();
            mailContent.append("<table align='left' border='0' cellpadding='0' cellspacing='0' style='border-collapse:collapse;margin:0;padding:0;'>");
            mailContent.append("<tbody>");
            mailContent.append("<tr>");
            mailContent.append("<td valign='top' style='margin:0;padding:0;border-top:0;height:100%!important;width:100%!important'>");
            mailContent.append("<div style='padding: 20px; margin: 20px; width: 500px; border: 1px solid #D4D4D4;'>");
            mailContent.append("?????????????????? ?????????????????????" + smsCode + "????????????????????????????????????????????? ????????????ZZEX?????????,????????????????????? ???ZZEX???");
            mailContent.append("</div></td></tr></tbody></table>");

            return AliMailUtil.sendMail(mName, "???ZZEX????????????", mailContent.toString());
        } catch (Exception e) {
            return false;
        }
    }


    @ApiOperation(value = "???????????????", notes = "???????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/login/verification/{m_name}/{code}", method = RequestMethod.GET)
    @ResponseBody
    public Resp VerificationCode(HttpServletRequest request, @PathVariable String m_name, @PathVariable String code, @RequestParam String token, String last_login_device) {

        // token ??????
        String localToken = (String) JedisUtil.getInstance().get(Constants.LOGIN_TOKEN_VERIFICATION + m_name, true);
        if (null == localToken || !localToken.equals(token)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_LOGIN_TOKEN_EXPIRE.getErrorENMsg());
        }


        Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("m_name", m_name));
        if (null == oldMember) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg());
        }
        if (HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), Constants.SMSCODE_TIME_OUT)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_EXPIRE_TIP.getErrorENMsg());
        }
        //  ???????????????????????????????????????
        if (!freeMnameMap.containsKey(m_name)) {
            if (!code.equals(oldMember.getSms_code())) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
            }
        }
        oldMember.setLast_login_device(last_login_device);
        return memberService.hideMemberInfo(request, oldMember, true);
    }

    /**
     * ??????token?????????
     *
     * @return
     */
    @RequestMapping(value = "m/checkAuthToken", method = RequestMethod.GET)
    @ResponseBody
    public Resp checkAuthToken(String authToken, String last_login_device, HttpServletRequest request) {
        if (StringUtils.isNullOrEmpty(authToken)) {
            return new Resp(Resp.FAIL, "token can not be empty");
        }
        SessionInfo sessionInfo = JSON.parseObject(EncryptUtils.desDecrypt(authToken), SessionInfo.class);
        if (sessionInfo == null) return new Resp(Resp.FAIL, "token check failed");
        String key = new StringBuffer(CacheConst.LOGIN_PREFIX).append("app_authtoken").append(sessionInfo.getId()).toString();
        String rAuthToken = (String) JedisUtil.getInstance().get(key, true);
        if (StringUtils.isNullOrEmpty(rAuthToken)) return new Resp(Resp.FAIL, "token expired");
        if (!authToken.equals(rAuthToken)) {
            return new Resp(Resp.FAIL, "token check failed");
        }
        if (HelpUtils.nullOrBlank(last_login_device)) {
            last_login_device = "0";
        }
        // ??????
//		JedisUtil.getInstance().expire(key, 7 * 86400);
        return memberService.createSession(sessionInfo.getM_name(), authToken, last_login_device, request);
    }

    @ApiOperation(value = "APP??????(??????????????????????????????Token)", notes = "APP??????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiIgnore
    @RequestMapping(value = "m/app/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp loginApp(HttpServletRequest request, @RequestBody Member member) {
        if (HelpUtils.nullOrBlank(member.getM_name())) {
            logger.info(HelpUtils.getRequestInfo(request) + "  ???????????????????????????null!");
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_NAME.getErrorENMsg(), "????????????????????????????????????");

        }

        member.setM_name(member.getM_name().toLowerCase()); // ???????????????

        Integer googleAuthCode = member.getGoogle_auth_code();

        String m_pwd = MacMD5.CalcMD5Member(member.getM_pwd());
        member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", member.getM_name(), "m_pwd", m_pwd));
        return memberService.loginCommon(request, member, true, googleAuthCode);
    }

    @ApiOperation(value = "PC??????", notes = "???????????????m_name??????????????????m_pwd????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/memberLogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp login(HttpServletRequest request,
                         HttpServletResponse response, @RequestBody MemberVo membervo) {

        Member member = new Member();
        BeanUtils.copyProperties(membervo, member);
        if (!"1".equals(membervo.getGee_test())) {
            int gtResult = gtVerifyCode(request, membervo);
            if (gtResult < 1) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_GT_CHECK_CODE.getErrorENMsg(), null);
            }
        }

        if (HelpUtils.nullOrBlank(member.getM_name())) {
            logger.warn(HelpUtils.getRequestInfo(request) + "  ???????????????????????????null!");
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_NAME.getErrorENMsg(), "????????????????????????????????????");

        }

        member.setM_name(member.getM_name().toLowerCase()); // ???????????????

        Integer googleAuthCode = member.getGoogle_auth_code();

        String m_pwd = MacMD5.CalcMD5Member(member.getM_pwd());
        member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", member.getM_name(), "m_pwd", m_pwd));
        if (BeanUtil.isEmpty(member)) {
            logger.warn("?????????????????????{},??????????????????{}", JsonUtil.toJson(member), m_pwd);
        }
        if (HelpUtils.nullOrBlank(membervo.getLast_login_device())) {
            member.setLast_login_device("0");
        } else {
            member.setLast_login_device(membervo.getLast_login_device());
        }
        // ????????????
        return memberService.loginCommon(request, member, true, googleAuthCode);
    }


    //??????gt??????
    private int gtVerifyCode(HttpServletRequest request, MemberVo memberVo) {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String validate = memberVo.getGeetest_validate();
        String seccode = memberVo.getGeetest_seccode();
        String challenge = memberVo.getGeetest_challenge();
        String ipAddr = HelpUtils.getIpAddr(request);
        String gtServerStatusCode = JedisUtil.getInstance().getCheckCode(gtSdk.gtServerStatusSessionKey + ipAddr);
        //???????????????,???????????????
        HashMap<String, String> param = new HashMap<String, String>();
        //???????????????????????????????????????IP
        param.put("ip_address", ipAddr);

        int gtResult = 0;
        if ("1".equals(gtServerStatusCode)) {
            //gt-server????????????gt-server??????????????????
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            logger.error("???????????????1?????????0?????????{}", gtResult);
        } else {
            // gt-server???????????????????????????failback????????????
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            logger.error("???????????????1?????????0?????????{}", gtResult);
        }
        return gtResult;
    }

    @ApiOperation(value = "????????????????????????", notes = "???????????????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/googleAuthReq", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp googleAuthInfo(HttpServletRequest request,
                                  HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        // ?????????????????????
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();

        Map<String, String> retMap = new HashMap<String, String>();
        String private_key = key.getKey();
        retMap.put("google_auth_key", private_key);
        // ??????????????????????????????????????????????????????????????????
        retMap.put("google_auth_qrcode_text", "otpauth://totp/" + HelpUtils.getMgrConfig().getComp_en_name() + "-" + m.getM_name() + "?secret=" + private_key);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, retMap);
    }

    @ApiOperation(value = "????????????????????????", notes = "???????????????google_auth_key???????????????????????????google_auth_code????????????????????????sms_code????????????????????????old_google_auth_code????????????????????????????????????????????????????????????????????????????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/googleAuthBind", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp googleAuthBind(HttpServletRequest request,
                               HttpServletResponse response, @RequestBody Member member) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        GoogleAuthenticator gAuth = new GoogleAuthenticator();

        Member oldMember = memberService.getMemberById(m.getId());

        // ?????????????????????
        if (null == oldMember || HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !oldMember.getSms_code().equals(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        // ???????????????????????????????????????????????????
        if (!HelpUtils.nullOrBlank(oldMember.getGoogle_auth_key())) {
            if (member.getOld_google_auth_code() == null) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_OLD_GOOGLE_CODE_ERROR.getErrorENMsg());
            }
            if (!gAuth.authorize(oldMember.getGoogle_auth_key(), member.getOld_google_auth_code())) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_OLD_GOOGLE_CODE_ERROR.getErrorENMsg());
            }
        }

        if (!gAuth.authorize(member.getGoogle_auth_key(), member.getGoogle_auth_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_GOOGLE_CODE_ERROR.getErrorENMsg());
        }

        // ???????????????GoogleAuthKey
        Member newMember = new Member();
        newMember.setId(m.getId());
        newMember.setGoogle_auth_key(member.getGoogle_auth_key());
        if (!HelpUtils.nullOrBlank(oldMember.getM_security_pwd())) {
            newMember.setLast_mod_pwd_time(HelpUtils.formatDate8(new Date()));
        }
        newMember.setSms_code(HelpUtils.randomNumNo0(6)); //?????????????????????????????????????????????
        memberService.updateMember(newMember, false, false);

        // ??????Session??????authKey??????????????????session??????????????????key???????????????1??????????????????
        m.setGoogle_auth_key("1");
        JedisUtilMember.getInstance().setMember(request, m);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    @ApiOperation(value = "????????????????????????????????????", notes = "0??????????????????1????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/isEnableGoogleAuth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp isEnableGoogleAuth(HttpServletRequest request,
                                      HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, m.getGoogle_auth_key());
    }

    @ApiOperation(value = "??????????????????", notes = "?????????????????????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/googleAuthVerify/{google_auth_code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp googleAuthVerify(HttpServletRequest request,
                                 HttpServletResponse response, @PathVariable Integer google_auth_code) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }
        // ??????????????????key???????????????????????????????????????session????????????
        Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("id", m.getId())); //????????????????????????
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        if (HelpUtils.nullOrBlank(oldMember.getGoogle_auth_key()) || !gAuth.authorize(oldMember.getGoogle_auth_key(), google_auth_code)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_GOOGLE_CODE_ERROR.getErrorENMsg());
        }
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "????????????OSS??????Policy", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/aliOssPolicy", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getAliOssPolicy(HttpServletRequest request,
                                   HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        Map<String, String> respMap = getAliPolicy();

        if (null == respMap) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_UNDEFINED_ERROR.getErrorENMsg(), null);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, respMap);
    }


    @ApiOperation(value = "????????????????????????", notes = "????????????10???????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/loginlog", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp loginlog(HttpServletRequest request,
                            HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        List logLst = memberService.getAllMemberOperLog(HelpUtils.newHashMap("member_id", m.getId(), "page", 1, "pagesize", 10, "sortname", "id", "sortorder", "desc"));

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, logLst);
    }


    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/friendlog/{page}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp friendlog(HttpServletRequest request,
                             HttpServletResponse response, @PathVariable Integer page) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        Map param = HelpUtils.newHashMap("member_id", m.getId(), "page", page, "pagesize", 10, "sortname", "id", "sortorder", "desc");
        List logLst = memberService.getAllIntroMember(param);

        Map ret = new HashMap();
        ret.put("data", logLst);
        ret.put("total", param.get("total"));

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, ret);
    }

    @ApiOperation(value = "????????????????????????", notes = "page?????????????????????1??????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/demand/{page}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp demand(HttpServletRequest request,
                          HttpServletResponse response, @PathVariable Integer page, @PathVariable Integer pageSize) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        Map param = new HashMap();
        param.put("member_id", m.getId());
        param.put("page", (null == page ? "1" : page));
        param.put("pagesize", (null == pageSize ? "10" : pageSize));
        param.put("sortname", "id");
        param.put("sortorder", "desc");
        List demandLst = memberService.getAllDemand(param);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("demandLst", demandLst, "total", param.get("total")));
    }


    @ApiOperation(value = "??????", notes = "??????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/member/logout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp logout(HttpServletRequest request,
                       HttpServletResponse response) {

        try {
            Member member = JedisUtilMember.getInstance().getMember(request, null);
            JedisUtilMember.getInstance().removeMember(request, null);
            memberServiceManager.handlerContractLogout(member);
        } catch (Exception e) {
            logger.error("???????????????????????????{}", e);
        }

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "????????????URL", notes = "???????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/friendUrl", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getFriendUrl(HttpServletRequest request,
                                HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);

        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, request.getScheme() + "://" + request.getServerName() + "/m/reg/" + HelpUtils.PRE_INTRODUCE_ID + m.getId());
    }

    @ApiOperation(value = "??????????????????/??????????????????24??????", notes = "?????????24????????????????????????data???1??????????????????0???????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/isCoinPwdMoreThan24", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getIsCoinPwdMoreThan24(HttpServletRequest request,
                                          HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);

        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        if ("0".equals(m.getM_security_pwd())) {
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, 0);
        }

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.differentDaysByMillisecond(m.getLast_mod_pwd_time(), HelpUtils.formatDate8(new Date())) < 24 ? "0" : "1");
    }

    @ApiOperation(value = "??????????????????????????????", notes = "?????????????????????????????????data???1??????????????????0???????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/isSetCoinPwd", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getIsSetCoinPwd(HttpServletRequest request,
                                   HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);

        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, m.getM_security_pwd());


    }

    @ApiOperation(value = "????????????????????????", notes = "????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/authindentity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getAuthIdentity(HttpServletRequest request,
                                   HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        AuthIdentity authIdentity = memberService.getAuthIdentityById(m.getId());

        if (authIdentity == null) {
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("authIdentity", null));
        }
        //??????????????????????????????????????????????????????
        authIdentity.setId_front_img(null);
        authIdentity.setId_back_img(null);
        authIdentity.setId_handheld_img(null);
        Map<String, Object> datas = new HashMap();
        datas.put("url", FaceIdUtil.DO_URL);
        datas.put("authIdentity", authIdentity);
        datas.put("expire_time", FaceIdUtil.EXPIRE_TIME);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, datas);
    }

    //?????????????????????token????????????
    public boolean isBizTokenExpired(String bizToken) {
        if (bizToken == null) {
            return true;
        }
        try {
            long time = Long.parseLong(bizToken.substring(0, 10));
            long current = System.currentTimeMillis() / 1000;
            return current - time > 60 * 2;
        } catch (Exception e) {
            return true;
        }

    }

//	@ApiOperation(value = "??????????????????Token", notes = "??????????????????Token", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
//	@RequestMapping(value = "m/qiniuToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public ObjResp getQiNiuToken(HttpServletRequest request,
//			HttpServletResponse response) {
//		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, Auth.getToken());
//	}

    @ApiOperation(value = "????????????????????????", notes = "?????????????????????????????????????????????????????????currencyLst?????????????????????????????????accountsLst????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/assetsLst/{ts}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp assetsReload(HttpServletRequest request,
                                HttpServletResponse response) throws IOException, ClassNotFoundException {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map accountsCurrencyMap = new HashMap(HelpUtils.newHashMap("accountAuth", "1"));
        FrmConfig frmConfig = frmUserService.findConfig();
        // ????????????-??????????????????
        if (frmConfig.getWithdraw_need_identity() == 1) {
            // ?????????????????????????????????
            AuthIdentity authIdentity = memberService.getAuthIdentityById(m.getId());
            if (null == authIdentity || authIdentity.getId_status() != 1) {
                // ?????????????????????
                accountsCurrencyMap.put("accountAuth", 0);
            }
        }

        List<Currency> currencyList = HelpUtils.getCurrencyLst();

        List<LockAccount> lockAccountList = currencyLockAccountService.findByMemberId(m.getId());
        List<CurrencyLockReleaseDto> waitLockReleaseDtoList = currencyLockReleaseService
                .findWaitReleaseByMemberId(m.getId());
        // ????????????????????????
        accountsCurrencyMap.put("currencyLst", currencyList);
        List<AccountDto> accountDtoList = memberService.genMemberAccountsLst(m.getId());
        for (AccountDto accountDto : accountDtoList) {
            BigDecimal sumWarehouseAccount = warehouseAccountService.getSumWarehouseAccount(HelpUtils.newHashMap("memberId", m.getId(), "currency", accountDto.getCurrency()));
            if (BeanUtil.isEmpty(sumWarehouseAccount)) {
                sumWarehouseAccount = BigDecimal.ZERO;
            }
            BigDecimal lockNum = BigDecimal.ZERO;
            for (LockAccount lockAccount : lockAccountList) {
                if (accountDto.getCurrency().equals(lockAccount.getCurrency())) {
                    lockNum = lockAccount.getLock_num();
                }
            }
            accountDto.setLock_num(lockNum.add(sumWarehouseAccount));
            // ???list????????????????????????????????????????????????
            CurrencyLockReleaseDto waitLockReleaseDto = waitLockReleaseDtoList.stream()
                    .filter(e -> accountDto.getCurrency().equals(e.getCurrency()))
                    .findAny().orElse(null);
            BigDecimal waitVolume = waitLockReleaseDto == null ? BigDecimal.ZERO : waitLockReleaseDto.getReleaseVolume();
            accountDto.setWait_release_num(waitVolume);

            // ??????????????????
            accountDto.setTotal_balance(accountDto.getTotal_balance().add(accountDto.getLock_num()).add(waitVolume));
        }
        // ??????????????????
        accountsCurrencyMap.put("accountsLst", accountDtoList);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, accountsCurrencyMap);
    }

    @ApiOperation(value = "????????????????????????", notes = "????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/member", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getMember(HttpServletRequest request,
                             HttpServletResponse response) {
//		String cookie = request.getHeader("cookie");
//		if(StringUtils.isNullOrEmpty(cookie))
//		{
//			logger.warn("?????????????????????, cookie??????.??????IP???: " + HelpUtils.getIpAddr(request));
//		}
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }


        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, m);
    }


    @ApiOperation(value = "????????????????????????", notes = "???????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/showDemand/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Demand getDemand(HttpServletRequest request,
                            HttpServletResponse response, @PathVariable Integer id) {

        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return null;
        }

        // ??????????????????
        Demand demand = memberService.getDemandById(id);

        if (null == demand || demand.getMember_id().intValue() != m.getId().intValue()) {
            return null;
        }

        // ????????????????????????
        List<DemandLog> demandLogList = memberService.getDemandLogByDemandId(id);
        demand.setDemandLogList(demandLogList);

        return demand;
    }


    @ApiOperation(value = "??????????????????", notes = "???????????????given_name????????????middle_name???????????????????????????family_name?????????, sex???????????????birthday???????????????id_number??????????????????id_begin_date???????????????????????????id_end_date???????????????????????????province????????????city????????????nationality???????????????id_front_img????????????????????????????????????id_back_img????????????????????????????????????id_handheld_img???????????????????????????idType??????????????????passport???idcard??????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/authIdentity", method = RequestMethod.POST)
    @ResponseBody
    public Resp addAuthIdentity(HttpServletRequest request,
                                HttpServletResponse response, @RequestBody AuthIdentity authIdentity) throws Exception {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }
//
        authIdentity.setId(member.getId());

        // ???????????????????????????
        Integer alreadyIdNumCount = daoUtil.queryForInt("SELECT COUNT(1) FROM m_auth_identity WHERE id <> ? AND id_number = ?", member.getId(), authIdentity.getId_number());
        if (alreadyIdNumCount > 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ID_NUM_ALREADY_EXIST.getErrorENMsg(), null);
        }

        authIdentity.setGiven_name(HelpUtils.escapeHTMLTags(authIdentity.getGiven_name()));
        authIdentity.setMiddle_name(HelpUtils.escapeHTMLTags(authIdentity.getMiddle_name()));
        authIdentity.setFamily_name(HelpUtils.escapeHTMLTags(authIdentity.getFamily_name()));
        authIdentity.setNationality(HelpUtils.escapeHTMLTags(authIdentity.getNationality()));
        authIdentity.setId_type(HelpUtils.escapeHTMLTags(authIdentity.getId_type()));
        authIdentity.setNationality(HelpUtils.escapeHTMLTags(authIdentity.getNationality()));
        authIdentity.setProvince(HelpUtils.escapeHTMLTags(authIdentity.getProvince()));
        authIdentity.setCity(HelpUtils.escapeHTMLTags(authIdentity.getCity()));
        authIdentity.setId_number(HelpUtils.escapeHTMLTags(authIdentity.getId_number()));
        authIdentity.setId_status(0);
        //???????????????????????????????????????????????????????????????token??????
        authIdentity.setBiz_token(null);

        memberService.addAuthIdentity(authIdentity);

        return new Resp(Resp.SUCCESS, ErrorInfoEnum.LANG_AUTH_IDENTITY_SUB_SUCCESS.getErrorENMsg());
    }


    @ApiOperation(value = "????????????", notes = "??????????????????????????????d_title?????????????????????d_memo??????????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/addDemand", method = RequestMethod.POST)
    @ResponseBody
    public Resp addDemand(HttpServletRequest request,
                          HttpServletResponse response, @RequestBody Demand demand) throws Exception {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // ??????< " ?????????
        demand.setD_title(HelpUtils.escapeHTMLTags(demand.getD_title()));
        demand.setD_memo(HelpUtils.escapeHTMLTags(demand.getD_memo()));

        demand.setMember_id(member.getId());
        demand.setM_name(member.getM_name());
        demand.setCreate_time(HelpUtils.formatDate8(new Date()));

        memberService.addDemand(demand);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "?????????????????????", notes = "???????????????????????????????????????demand_id?????????ID?????? l_memo?????????????????????l_memo_type??????????????????1???????????????2????????????????????????????????????????????????1???", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/addDemandLog", method = RequestMethod.POST)
    @ResponseBody
    public Resp addDemandLog(HttpServletRequest request,
                             HttpServletResponse response, @RequestBody DemandLog demandLog) throws Exception {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // ??????< " ?????????
        demandLog.setL_memo(HelpUtils.escapeHTMLTags(demandLog.getL_memo()));

        demandLog.setLog_type(0);
        demandLog.setCreate_time(HelpUtils.formatDate8(new Date()));
        demandLog.setD_status(0);
        //??????????????????????????????????????????
        if (null == demandLog.getL_memo_type() || demandLog.getL_memo_type() > 2 || demandLog.getL_memo_type() < 1) {
            demandLog.setL_memo_type(1);
        }

        memberService.addDemandLog(demandLog);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "??????api_key???api_secret", notes = "???????????????api_key???api_secret", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/apitoken", method = RequestMethod.POST)
    @ResponseBody
    public RespObjApiKeySecret genApiToken(HttpServletRequest request, HttpServletResponse response,
                                           @RequestBody ReqGenApiKey reqGenApiKey) {
        Member oldMember = null;
        try {
            oldMember = validateGenAndModifyApiToken(request, reqGenApiKey);
            return memberService.genApiToken(reqGenApiKey, oldMember);
        } catch (Exception e) {
            logger.warn("apiToken??????try-catch??????????????????????????????{}", e.getMessage());
            return new RespObjApiKeySecret(Resp.FAIL, e.getMessage(), null, null);
        }
    }

    @ApiOperation(value = "??????apiToken", notes = "????????????apiToken(?????????????????????IP????????????????????????)", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/apitoken", method = RequestMethod.PUT)
    @ResponseBody
    public Resp modApiToken(HttpServletRequest request, HttpServletResponse response,
                            @RequestBody ReqModifyApiKey reqModifyApiKey) throws Exception {
        Member oldMember = null;
        try {
            if (null == reqModifyApiKey.getId() || reqModifyApiKey.getId() <= 0) {
                return new Resp(Resp.FAIL, "ILLEGAL_ID");
            }

            oldMember = validateGenAndModifyApiToken(request, reqModifyApiKey);
            return memberService.modifyApiToken(reqModifyApiKey, oldMember);
        } catch (Exception e) {
            logger.warn("apiToken??????try-catch??????????????????????????????{}", e.getMessage());
            return new Resp(Resp.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "??????api token", notes = "??????api token", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/apitoken", method = RequestMethod.DELETE)
    @ResponseBody
    public Resp delApiToken(HttpServletRequest request, HttpServletResponse response,
                            @RequestBody ReqDelApiKey reqDelApiKey) throws Exception {

        if (null == reqDelApiKey.getId() || reqDelApiKey.getId() <= 0) {
            return new Resp(Resp.FAIL, "ILLEGAL_ID");
        }

        if (HelpUtils.nullOrBlank(reqDelApiKey.getSecurity_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg());
        }

        Member redisMember = JedisUtilMember.getInstance().getMember(request, null);
        Member oldMember = memberService.getMemberById(redisMember.getId());
        if (!MacMD5.CalcMD5Member(reqDelApiKey.getSecurity_pwd()).equals(oldMember.getM_security_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg());
        }
        // ?????????????????????????????????????????????????????????????????????????????????????????????Redis?????????????????????????????????Redis????????????????????????
        if (!HelpUtils.nullOrBlank(oldMember.getGoogle_auth_key())) {
            if (reqDelApiKey.getGoogle_auth_code() == null) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_GOOGLE_CODE_ERROR.getErrorENMsg());
            }
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            if (!gAuth.authorize(oldMember.getGoogle_auth_key(), reqDelApiKey.getGoogle_auth_code())) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_GOOGLE_CODE_ERROR.getErrorENMsg());
            }
        }

        RespApiToken token = memberService.getApiToken(redisMember.getId(), reqDelApiKey.getId());
        if (null == token) {
            return new Resp(Resp.FAIL, "ILLEGAL_ID");
        }

        return memberService.delApiToken(token, redisMember.getId());
    }

    @ApiOperation(value = "????????????Api Token??????", notes = "????????????Api Token??????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/apitokens", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public RespApiTokens getApiTokens(HttpServletRequest request) {
        Member redisMember = JedisUtilMember.getInstance().getMember(request, null);

        return memberService.getApiTokens(redisMember.getId());
    }


    private Member validateGenAndModifyApiToken(HttpServletRequest request, ReqGenApiKey reqGenApiKey) {
        if (HelpUtils.nullOrBlank(reqGenApiKey.getSecurity_pwd())) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg());
        }

        if (HelpUtils.nullOrBlank(reqGenApiKey.getSms_code())) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        if (HelpUtils.nullOrBlank(reqGenApiKey.getLabel())) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_API_LABEL_ERR.getErrorENMsg());
        }

        if (HelpUtils.nullOrBlank(reqGenApiKey.getApi_privilege())) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_API_PRIVILEGE_ERR.getErrorENMsg());
        }

        Member redisMember = JedisUtilMember.getInstance().getMember(request, null);
        if (null == redisMember) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        Member oldMember = memberService.getMemberById(redisMember.getId());

        // ?????????????????????????????????????????????????????????????????????????????????????????????Redis?????????????????????????????????Redis????????????????????????
        if (!HelpUtils.nullOrBlank(oldMember.getGoogle_auth_key())) {
            if (reqGenApiKey.getGoogle_auth_code() == null) {
                throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_GOOGLE_CODE_ERROR.getErrorENMsg());
            }
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            if (!gAuth.authorize(oldMember.getGoogle_auth_key(), reqGenApiKey.getGoogle_auth_code())) {
                throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_GOOGLE_CODE_ERROR.getErrorENMsg());
            }
        }

        if (!MacMD5.CalcMD5Member(reqGenApiKey.getSecurity_pwd()).equals(oldMember.getM_security_pwd())) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg());
        }

        if (oldMember.getAuth_grade().intValue() < 1) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_AUTH_IDENTITY_FIRST.getErrorENMsg());
        }

        if (HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !reqGenApiKey.getSms_code().equals(oldMember.getSms_code())) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        return oldMember;
    }

    @ApiOperation(value = "???????????????????????????", notes = "???????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/gtValidateCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getCaptcha(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String resStr = "{}";
        //???????????????,???????????????
        HashMap<String, String> param = new HashMap<String, String>();
        //web:????????????????????????h5:????????????????????????????????????????????????????????????web_view???native???????????????SDK??????APP???????????????
        param.put("client_type", "web");
        //??????ip??????
        String ipAddr = HelpUtils.getIpAddr(request);
        param.put("ip_address", ipAddr);
        //?????????????????????
        int gtServerStatus = gtSdk.preProcess(param);
        JedisUtil.getInstance().setCheckCode(gtSdk.gtServerStatusSessionKey + ipAddr, gtServerStatus + "", Constants.CHECK_CODE_TIME_OUT);
        resStr = gtSdk.getResponseStr();

        PrintWriter out = response.getWriter();
        out.println(resStr);
    }

    //	@ApiOperation(value = "??????JSSESSION", notes = "??????JSSESSION", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiIgnore
    @RequestMapping(value = "m/getJsSession", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getJsSession(HttpServletRequest request,
                                HttpServletResponse response) throws IOException {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, request.getSession().getId());
    }

    @ApiOperation(value = "??????????????????????????????", notes = "??????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/reutrncommi/{page}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp getReturnCommi(HttpServletRequest request, @PathVariable Integer page, @PathVariable Integer pageSize) throws IOException {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("member_id", m.getId());
        param.put("page", page);
        param.put("pagesize", pageSize);
        List<Map<String, Object>> list = returnCommiService.findByPage(param);
        return new LstResp(Resp.SUCCESS, Resp.SUCCESS_MSG, Integer.parseInt(param.get("total") + ""), list);
    }

    @ApiOperation(value = "????????????????????????", notes = "????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/reutrncommi/intro/{page}/{pageSize}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp findIntroByPage(HttpServletRequest request, @PathVariable Integer page, @PathVariable Integer pageSize) throws IOException {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("member_id", m.getId());
        param.put("page", page);
        param.put("pagesize", pageSize);
        List<Map<String, Object>> list = memberService.findIntroByPage(param);
        return new LstResp(Resp.SUCCESS, Resp.SUCCESS_MSG, Integer.parseInt(param.get("total") + ""), list);
    }

    @ApiOperation(value = "?????????????????????????????????", notes = "?????????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/reutrncommi/intro/total", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp findReturnCommiTotalAndIntroByMemberId(HttpServletRequest request) throws IOException {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> map = returnCommiService.findReturnCommiTotalAndIntroByMemberId(m.getId());
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, map);
    }

    @ApiOperation(value = "????????????????????????", notes = "????????????????????????", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/phone", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp updateMemberPhone(HttpServletRequest request, @RequestBody Member member) throws IOException {
        Member m = JedisUtilMember.getInstance().getMember(request, member.getToken());
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // ???????????????????????????????????????
        if (HelpUtils.nullOrBlank(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }

        // ????????????????????????????????????
        Member oldMember = memberService.getMemberById(m.getId());
        if (null == oldMember || HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !member.getSms_code().equals(oldMember.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }
        Integer count = daoUtil.queryForInt("select count(*) from m_member where phone = ?", member.getPhone());
        if (count > 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.PHONE_HAS_BINDING.getErrorENMsg());
        }
        // ??????????????????
        member.setPhone(member.getPhone());
        //?????????????????????????????????????????????
        member.setSms_code(HelpUtils.randomNumNo0(6));
        member.setId(m.getId());
        memberService.updateMember(member, false, false);

        m.setPhone(member.getPhone());

        JedisUtilMember.getInstance().setMember(request, m);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "??????????????????", notes = "??????????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/m/authorize", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp faceIdAuthorize(HttpServletRequest request) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        ///
        //???redis???????????????biztoken
        String bizToken = getFaceIdTokenCache(m.getId());
        Map<String, Object> datas = new HashMap(4);
        datas.put("url", FaceIdUtil.DO_URL);
        datas.put("expire_time", FaceIdUtil.EXPIRE_TIME);
        //??????????????????????????????????????????
        if (bizToken != null) {
            datas.put("biz_token", bizToken);
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, datas);
        }
        AuthIdentity authIdentity = new AuthIdentity();
        authIdentity.setId(m.getId());
        authIdentity.setGiven_name("/");
        authIdentity.setFamily_name("/");
        authIdentity.setNationality("??????Chinese");
        authIdentity.setId_handheld_img("/");
        authIdentity.setLast_submit_time(HelpUtils.getCurrTime());
        authIdentity.setId_status(3);
        bizToken = FaceIdUtil.getBizToken(m.getId() + "");
        if (StringUtils.isNullOrEmpty(bizToken)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
        }
        authIdentity.setBiz_token(bizToken);
        memberService.addAuthIdentity(authIdentity);
        faceIdTokenCache(m.getId(), bizToken);
        datas.put("biz_token", bizToken);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, datas);

    }

    /**
     * notify_url ?????? JavaSpring MVC ????????????post?????????????????????
     */
    @ApiIgnore
    @RequestMapping(method = RequestMethod.POST, value = "/m/notify")
    @ResponseBody
    public String toNotify(HttpServletRequest request) {
        String data = request.getParameter("data");
        if (StringUtils.isNullOrEmpty(data)) {
            return "";
        }
        JSONObject json = JSONObject.parseObject(data);
        String bizToken = json.getString("biz_token");
        if (StringUtils.isNullOrEmpty(bizToken)) {
            return "";
        }
        String result = FaceIdUtil.getResult(bizToken, "https://openapi.faceid.com/lite/v1/get_result");
        if (StringUtils.isNullOrEmpty(result)) {
            return "";
        }
        String memberId = null;
        try {
            memberId = memberService.authMemberByFaceId(result);
        } catch (Exception e) {
            logger.warn("faceId notify?????????token???" + bizToken + e.getMessage());
        } finally {
            JedisUtil.getInstance().del(FACEID_CACHE_KEY + memberId);
        }
        return "";
    }

    //???????????????biztoken??????
    protected String getFaceIdTokenCache(Integer MemberId) {
        return (String) JedisUtil.getInstance().get(FACEID_CACHE_KEY + MemberId, true);
    }

    //??????biztoken
    protected void faceIdTokenCache(Integer MemberId, String BizToken) {
        JedisUtil.getInstance().set(FACEID_CACHE_KEY + MemberId, BizToken, null);
        JedisUtil.getInstance().expire(FACEID_CACHE_KEY + MemberId, (int) FaceIdUtil.EXPIRE_TIME);
    }

}
