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


@Api(value = "会员接口", description = "会员注册、账号、密码相关", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class MemberController extends TopController {
    private static Logger logger = LoggerFactory.getLogger(MemberController.class);

    /**
     * 短信验证码频率时间限制，单位秒
     */
    private static final int FREQUENCY_TIME = 60 * 5;

    /**
     * 短信验证码频率限制
     */
    private static final int FREQUENCY_COUNT = 10;

    /**
     * 短信验证码频率限制前缀
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
                logger.info("登录免验证账号：{}", mname);
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

    @ApiOperation(value = "会员注册", notes = "输入字段：m_name（用户名），m_pwd（密码），sms_code（邮箱/短信验证码），introduce_m_id（介绍人ID）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/member", method = RequestMethod.POST)
    @ResponseBody
    public Resp addMember(HttpServletRequest request,
                          HttpServletResponse response, @RequestBody Member member) throws Exception {
        // 后台再校验是否输入了手机号码
        if (HelpUtils.nullOrBlank(member.getM_name())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_MAIL_NULL_TIP.getErrorENMsg());
        }

        // 后台再校验是否输入了密码
        if (HelpUtils.nullOrBlank(member.getM_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_NULL_TIP.getErrorENMsg());
        }

        // 区号是否为空，为空默认是中国是86
        if (HelpUtils.nullOrBlank(member.getArea_code())) {
            member.setArea_code(AreaCodeEnum.CH.getAreaCode());
        }
        member.setM_name(member.getM_name().toLowerCase()); // 用户名小写

        if (member.getCheckeds() == 1) {
            ObjResp resp = fromReg(null, request, member.getM_name());
            if (resp.getState() != 1) {
                return resp;
            }
            member.setSms_code(String.valueOf(resp.getData()));
        }
        // 后台再校验是否输入了校验码
        if (HelpUtils.nullOrBlank(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }

        return memberServiceManager.handlerRegister(member);


    }

//	@ApiOperation(value = "会员注册", notes = "输入字段：m_name（用户名），m_pwd（密码），sms_code（邮箱/短信验证码），introduce_m_id（介绍人ID）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
//	@RequestMapping(value = "m/member", method = RequestMethod.POST)
//	@ResponseBody
//	public Resp addMember(HttpServletRequest request,
//						  HttpServletResponse response, @RequestBody Member member) throws Exception {
//		// 后台再校验是否输入了手机号码
//		if (HelpUtils.nullOrBlank(member.getM_name())) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_MAIL_NULL_TIP.getErrorENMsg());
//		}
//		// 后台再校验是否输入了校验码
//		if (HelpUtils.nullOrBlank(member.getSms_code())) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
//		}
//		// 后台再校验是否输入了密码
//		if (HelpUtils.nullOrBlank(member.getM_pwd())) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_NULL_TIP.getErrorENMsg());
//		}
//
//		// 区号是否为空，为空默认是中国是86
//		if (HelpUtils.nullOrBlank(member.getArea_code())){
//			member.setArea_code(AreaCodeEnum.CH.getAreaCode());
//		}
//		member.setM_name(member.getM_name().toLowerCase()); // 用户名小写
//
//		Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("m_name", member.getM_name())); //用户名全部用小写
//		if (null != oldMember && oldMember.getM_status() == 1) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ALREADY_REG_FINDPWD.getErrorENMsg());
//		} else if (null != oldMember && oldMember.getM_status() == 2) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg());
//		}
//		// 后台再校验校验码是否正确
//		if (null == oldMember || !member.getSms_code().equals(oldMember.getSms_code())) {
//			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
//		}
//
//		// 完成注册
//		member.setM_pwd(MacMD5.CalcMD5Member(member.getM_pwd()));
//		member.setId(oldMember.getId());
//		member.setM_status(1);
//		//刷新验证码，防止用户可以用原来的验证码处理其他事物
//		member.setSms_code(HelpUtils.randomNumNo0(6));
//
//		// 推荐人是空是自己则跳过
//		if (!HelpUtils.nullOrBlank(member.getIntroduce_m_id()) && !member.getIntroduce_m_id().equals(String.valueOf(oldMember.getId()))) {
//			//小写
//			String introMId = member.getIntroduce_m_id().trim().replace(HelpUtils.PRE_INTRODUCE_ID, "");
//			//大写
//			introMId = introMId.replace(HelpUtils.PRE_INTRODUCE_ID.toUpperCase(), "");
//			Integer iIntroMId = 0;
//			try {
//				iIntroMId = Integer.parseInt(introMId);
//				// 判断用户id是否存在，防止用户乱填
//                Member introMember = memberService.getMemberById(iIntroMId);
//                iIntroMId = introMember == null ? 0 : introMember.getId();
//            } catch (Exception e) {
//                logger.warn("< ==== 用户邀请异常：【 {} 】" ,e.fillInStackTrace());
//				iIntroMId = 0;
//			}
//			member.setIntroduce_m_id(iIntroMId+"");
//
//		} else {
//			member.setIntroduce_m_id("0");
//		}
//
//		// 只有当用户是被邀请注册的才可以返佣
//		if (!"0".equals(member.getIntroduce_m_id())) {
//			// 添加返佣记录
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
     * 获取国际语言区号
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

    @ApiOperation(value = "找回密码", notes = "输入字段：m_name（用户名）, m_pwd（密码），sms_code（邮箱/短信验证码)", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/forgotV2", method = RequestMethod.POST)
    @ResponseBody
    public Resp forgotV2(HttpServletRequest request,
                         HttpServletResponse response, @RequestBody Member member) throws Exception {

        // 后台再校验是否输入了手机号码
        if (HelpUtils.nullOrBlank(member.getM_name())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_MAIL_NULL_TIP.getErrorENMsg());
        }
        // 后台再校验是否输入了校验码
        if (HelpUtils.nullOrBlank(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }
        // 后台再校验是否输入了密码
        if (HelpUtils.nullOrBlank(member.getM_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_NULL_TIP.getErrorENMsg());
        }

        member.setM_name(member.getM_name().toLowerCase()); // 用户名小写

        Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("m_name", member.getM_name()));
        if (null != oldMember && oldMember.getM_status() == 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PLEASE_LOGIN_AFTER_REGISTRATION.getErrorENMsg());
        } else if (null != oldMember && oldMember.getM_status() == 2) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg());
        }
        // 后台再校验校验码是否正确
        if (null == oldMember || HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !member.getSms_code().equals(oldMember.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        // 完成密码修改
        member.setM_pwd(MacMD5.CalcMD5Member(member.getM_pwd()));
        member.setId(oldMember.getId());
        //member.setLast_mod_pwd_time(HelpUtils.formatDate8(new Date()));
        member.setSms_code(HelpUtils.randomNumNo0(6)); //将校验码换成新的，以免邮件泄露
        memberService.updateMember(member, false, false);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "修改密码", notes = "输入字段：m_security_pwd（原密码）,m_pwd（密码）,sms_code（邮箱/短信验证码）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/resetPwd", method = RequestMethod.POST)
    @ResponseBody
    public Resp resetPwd(HttpServletRequest request,
                         HttpServletResponse response, @RequestBody Member member) throws Exception {

        Member m = JedisUtilMember.getInstance().getMember(request, member.getToken());
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // 后台再校验是否输入了校验码
        if (HelpUtils.nullOrBlank(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }
        // 后台再校验是否输入了密码
        if (HelpUtils.nullOrBlank(member.getM_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_NULL_TIP.getErrorENMsg());
        }

        // 后台再校验校验码是否正确
        Member oldMember = memberService.getMemberById(m.getId());
        if (null == oldMember) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }
        if (HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !member.getSms_code().equals(oldMember.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        //再校验原密码是否正确（原密码借用m_security_pwd字段存放）
        if (HelpUtils.nullOrBlank(member.getM_security_pwd()) ||
                !MacMD5.CalcMD5Member(member.getM_security_pwd()).equals(oldMember.getM_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_OLD_PWD_ERR_TIP.getErrorENMsg());
        }

        // 完成密码修改
        member.setM_pwd(MacMD5.CalcMD5Member(member.getM_pwd()));

        if (!HelpUtils.nullOrBlank(oldMember.getM_security_pwd()) && oldMember.getM_security_pwd().equals(member.getM_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_SAME_TIP.getErrorENMsg());
        }

        member.setId(oldMember.getId());
        //member.setLast_mod_pwd_time(HelpUtils.formatDate8(new Date()));
        member.setSms_code(HelpUtils.randomNumNo0(6)); //将校验码换成新的，以免邮件泄露
        memberService.updateMember(member, false, false);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "设置资金密码", notes = "输入字段：m_security_pwd（资金密码），sms_code（邮箱/短信验证码）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/setSecPwd", method = RequestMethod.POST)
    @ResponseBody
    public Resp setSecPwd(HttpServletRequest request,
                          HttpServletResponse response, @RequestBody Member member) throws Exception {

        Member m = JedisUtilMember.getInstance().getMember(request, member.getToken());
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // 后台再校验是否输入了校验码
        if (HelpUtils.nullOrBlank(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }
        // 后台再校验是否输入了密码
        if (HelpUtils.nullOrBlank(member.getM_security_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_NULL_TIP.getErrorENMsg());
        }

        // 后台再校验校验码是否正确
        Member oldMember = memberService.getMemberById(m.getId());
        if (null == oldMember || HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !member.getSms_code().equals(oldMember.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        // 完成密码修改
        String unEncryptionSecurityPwd = member.getM_security_pwd();
        member.setM_security_pwd(MacMD5.CalcMD5Member(member.getM_security_pwd()));

        if (oldMember.getM_pwd().equals(member.getM_security_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PWD_SAME_TIP.getErrorENMsg());
        }


        member.setId(oldMember.getId());
        if (!HelpUtils.nullOrBlank(oldMember.getM_security_pwd())) {
            member.setLast_mod_pwd_time(HelpUtils.formatDate8(new Date()));
        }
        member.setSms_code(HelpUtils.randomNumNo0(6)); //将校验码换成新的，以免邮件泄露
        memberServiceManager.updateContractMember(member, false, false, unEncryptionSecurityPwd);

        // 重新设置安全密码后再存session
        m.setM_security_pwd("1");
        m.setLast_mod_pwd_time(HelpUtils.formatDate8(new Date()));
        JedisUtilMember.getInstance().setMember(request, m);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "修改用户昵称", notes = "修改用户昵称", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
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
     * 发送邮件/手机验证码
     * 要进行频率的控制，登录和其它使用的验证码要分开判断
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
    @ApiOperation(value = "发送邮件/手机验证码", notes = "发送邮件/手机验证码,from取值login,forgot或reg,check，分别为登录，忘记密码和注册,短信校验", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/mail/{check_code_token}/{check_code}/{m_name}/{from}", method = RequestMethod.GET)
    @ResponseBody
    public Resp mail(HttpServletRequest request,
                     HttpServletResponse response, @PathVariable String m_name, @PathVariable String from, @PathVariable String check_code_token, @PathVariable String check_code) throws Exception {

        // 短信类型，频率判断
        boolean frequencyLimit = frequencyLimit(request, from);
        if (frequencyLimit) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_LIMIT_FREQUENCY_OPERTION.getErrorENMsg());
        }
        // 后台再校验是否输入了手机号码
        if (HelpUtils.nullOrBlank(m_name)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_MAIL_NULL_TIP.getErrorENMsg());
        }
        // 后台再校验是否输入from
        if (HelpUtils.nullOrBlank(from)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_FROM.getErrorENMsg());
        }

        //用户名全小写
        m_name = m_name.toLowerCase();
        Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("m_name", m_name));
        if (BeanUtil.isEmpty(oldMember) && SMSTypeEnum.FORGOT.getMsg().equals(from)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg());
        }
        //注册是需要加国际区号，注册成功后从member表获取区号。默认区号为86
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

        //app就不需要验证数字码
        String type = request.getParameter("type");
        if (HelpUtils.nullOrBlank(type) || !"app".equals(type)) {
            // 验证校验码
            if (HelpUtils.nullOrBlank(check_code_token) || HelpUtils.nullOrBlank(check_code)) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CHECK_CODE.getErrorENMsg());
            }
            if (!check_code.equalsIgnoreCase(JedisUtil.getInstance().getCheckCode(check_code_token))) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CHECK_CODE.getErrorENMsg());
            } else {
                JedisUtil.getInstance().removeCheckCode(check_code_token);
            }
        }

        // 验证码请求类型判断
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
            // 验证码
            ISmsService smsService = null;
            if (BeanUtil.isEmpty(areaCode) || areaCode.equals(AreaCodeEnum.CH.getAreaCode())) {
                // 创蓝验证码
//                smsService = new ChuangLanSmsServiceImpl()
//                        .builder(m_name, Joiner.on(",").join(Arrays.asList(smsCode, 5)), AbstractChuangLanSmsService.CLT_CODE_TIME);

                //聚合验证码
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
     * 注册
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
            // 将这个号码插入用户表
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
     * 忘记密码
     *
     * @param oldMember
     * @param smsCode
     * @return
     */
    private ObjResp fromForgot(Member oldMember, String smsCode) {
        //正常
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
     * 用于短信验证码校验,只更新短信验证码和验证码发送时间，不做任何逻辑判断
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
     * 登录
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
     * @return true 操作频繁
     */
    /**
     * 通过ip地址判断请求是否太过频繁，放到redis中
     *
     * @param request
     * @return true 操作频繁
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


    @ApiOperation(value = "验证忘记密码时验证码", notes = "验证忘记密码时输入的验证码是否正确", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/forgotV/{m_name}/{sms_code}", method = RequestMethod.GET)
    @ResponseBody
    public Resp forgotV(HttpServletRequest request,
                        HttpServletResponse response, @PathVariable String m_name, @PathVariable String sms_code) throws Exception {

        // 后台再校验是否输入了手机号码
        if (HelpUtils.nullOrBlank(m_name)) {
            return new Resp(Resp.FAIL, "LANG_MAIL_NULL_TIP");
        }
        // 后台再校验是否输入from
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
            mailContent.append("尊敬的用户： 您的验证码是：" + smsCode + "。请不要把验证码泄露给其他人。 感谢您对ZZEX的支持,祝您生活愉快！ 【ZZEX】");
            mailContent.append("</div></td></tr></tbody></table>");

            return AliMailUtil.sendMail(mName, "【ZZEX】验证码", mailContent.toString());
        } catch (Exception e) {
            return false;
        }
    }


    @ApiOperation(value = "验证码校验", notes = "验证码校验", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/login/verification/{m_name}/{code}", method = RequestMethod.GET)
    @ResponseBody
    public Resp VerificationCode(HttpServletRequest request, @PathVariable String m_name, @PathVariable String code, @RequestParam String token, String last_login_device) {

        // token 校验
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
        //  配置文件中的账号跳过验证码
        if (!freeMnameMap.containsKey(m_name)) {
            if (!code.equals(oldMember.getSms_code())) {
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
            }
        }
        oldMember.setLast_login_device(last_login_device);
        return memberService.hideMemberInfo(request, oldMember, true);
    }

    /**
     * 校验token合法性
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
        // 延时
//		JedisUtil.getInstance().expire(key, 7 * 86400);
        return memberService.createSession(sessionInfo.getM_name(), authToken, last_login_device, request);
    }

    @ApiOperation(value = "APP登录(不同之处是返回值多了Token)", notes = "APP登录", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiIgnore
    @RequestMapping(value = "m/app/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp loginApp(HttpServletRequest request, @RequestBody Member member) {
        if (HelpUtils.nullOrBlank(member.getM_name())) {
            logger.info(HelpUtils.getRequestInfo(request) + "  用户登录时用户名为null!");
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_NAME.getErrorENMsg(), "用户登录时用户名不能为空");

        }

        member.setM_name(member.getM_name().toLowerCase()); // 用户名小写

        Integer googleAuthCode = member.getGoogle_auth_code();

        String m_pwd = MacMD5.CalcMD5Member(member.getM_pwd());
        member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", member.getM_name(), "m_pwd", m_pwd));
        return memberService.loginCommon(request, member, true, googleAuthCode);
    }

    @ApiOperation(value = "PC登录", notes = "输入字段：m_name（用户名），m_pwd（密码）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
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
            logger.warn(HelpUtils.getRequestInfo(request) + "  用户登录时用户名为null!");
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_NAME.getErrorENMsg(), "用户登录时用户名不能为空");

        }

        member.setM_name(member.getM_name().toLowerCase()); // 用户名小写

        Integer googleAuthCode = member.getGoogle_auth_code();

        String m_pwd = MacMD5.CalcMD5Member(member.getM_pwd());
        member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", member.getM_name(), "m_pwd", m_pwd));
        if (BeanUtil.isEmpty(member)) {
            logger.warn("登录请求参数：{},加密字符串：{}", JsonUtil.toJson(member), m_pwd);
        }
        if (HelpUtils.nullOrBlank(membervo.getLast_login_device())) {
            member.setLast_login_device("0");
        } else {
            member.setLast_login_device(membervo.getLast_login_device());
        }
        // 登录校验
        return memberService.loginCommon(request, member, true, googleAuthCode);
    }


    //进行gt验证
    private int gtVerifyCode(HttpServletRequest request, MemberVo memberVo) {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String validate = memberVo.getGeetest_validate();
        String seccode = memberVo.getGeetest_seccode();
        String challenge = memberVo.getGeetest_challenge();
        String ipAddr = HelpUtils.getIpAddr(request);
        String gtServerStatusCode = JedisUtil.getInstance().getCheckCode(gtSdk.gtServerStatusSessionKey + ipAddr);
        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
        //传输用户请求验证时所携带的IP
        param.put("ip_address", ipAddr);

        int gtResult = 0;
        if ("1".equals(gtServerStatusCode)) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, param);
            logger.error("极验证结果1成功，0失败：{}", gtResult);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            System.out.println("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
            logger.error("极验证结果1成功，0失败：{}", gtResult);
        }
        return gtResult;
    }

    @ApiOperation(value = "谷歌验证私钥获取", notes = "返回私钥、二维码的内容信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/googleAuthReq", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp googleAuthInfo(HttpServletRequest request,
                                  HttpServletResponse response) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        // 获得一个新私钥
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();

        Map<String, String> retMap = new HashMap<String, String>();
        String private_key = key.getKey();
        retMap.put("google_auth_key", private_key);
        // 返回二维码内容，二维码内容图片由前台自行生成
        retMap.put("google_auth_qrcode_text", "otpauth://totp/" + HelpUtils.getMgrConfig().getComp_en_name() + "-" + m.getM_name() + "?secret=" + private_key);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, retMap);
    }

    @ApiOperation(value = "绑定谷歌验证私钥", notes = "输入字段：google_auth_key（用户谷歌私钥），google_auth_code（谷歌验证码），sms_code（邮箱验证码），old_google_auth_code（原谷歌验证码，如果已经设置过，需带原验证码才可以重置）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
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

        // 校验邮箱验证码
        if (null == oldMember || HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !oldMember.getSms_code().equals(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }

        // 如果原来设置过，需校验原谷歌验证码
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

        // 更新用户的GoogleAuthKey
        Member newMember = new Member();
        newMember.setId(m.getId());
        newMember.setGoogle_auth_key(member.getGoogle_auth_key());
        if (!HelpUtils.nullOrBlank(oldMember.getM_security_pwd())) {
            newMember.setLast_mod_pwd_time(HelpUtils.formatDate8(new Date()));
        }
        newMember.setSms_code(HelpUtils.randomNumNo0(6)); //将校验码换成新的，以免邮件泄露
        memberService.updateMember(newMember, false, false);

        // 刷新Session中的authKey，安全考虑，session不存储具体的key，只存一个1表示已经验证
        m.setGoogle_auth_key("1");
        JedisUtilMember.getInstance().setMember(request, m);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    @ApiOperation(value = "用户是否启用谷歌验证校验", notes = "0表示未启用，1表示启用", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "谷歌验证校验", notes = "返回用户输入的谷歌验证是否正确", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/googleAuthVerify/{google_auth_code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp googleAuthVerify(HttpServletRequest request,
                                 HttpServletResponse response, @PathVariable Integer google_auth_code) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }
        // 谷歌验证码的key从数据库读取，因为安全考虑session中未存储
        Member oldMember = memberService.getMemberBy(HelpUtils.newHashMap("id", m.getId())); //用户名全部用小写
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        if (HelpUtils.nullOrBlank(oldMember.getGoogle_auth_key()) || !gAuth.authorize(oldMember.getGoogle_auth_key(), google_auth_code)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_GOOGLE_CODE_ERROR.getErrorENMsg());
        }
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "获得阿里OSS上传Policy", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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


    @ApiOperation(value = "获取用户登录日志", notes = "返回最近10条登录记录", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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


    @ApiOperation(value = "获取用户推荐的用户", notes = "返回用户推荐的用户", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "获取用户工单列表", notes = "page参数为页数，从1开始", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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


    @ApiOperation(value = "注销", notes = "注销", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/member/logout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp logout(HttpServletRequest request,
                       HttpServletResponse response) {

        try {
            Member member = JedisUtilMember.getInstance().getMember(request, null);
            JedisUtilMember.getInstance().removeMember(request, null);
            memberServiceManager.handlerContractLogout(member);
        } catch (Exception e) {
            logger.error("用户注销系统报错！{}", e);
        }

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "获得推荐URL", notes = "获得用户的推广地址", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "资金密码设置/修改是否超过24小时", notes = "未超过24小时不允许提现，data为1表示超过了，0表示未超过或未设置", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "用户是否设置资金密码", notes = "用户是否设置资金密码，data为1表示设置了，0表示未设置", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "获得用户认证信息", notes = "获得用户认证信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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
        //将传给前端的用户验证信息中的照片删除
        authIdentity.setId_front_img(null);
        authIdentity.setId_back_img(null);
        authIdentity.setId_handheld_img(null);
        Map<String, Object> datas = new HashMap();
        datas.put("url", FaceIdUtil.DO_URL);
        datas.put("authIdentity", authIdentity);
        datas.put("expire_time", FaceIdUtil.EXPIRE_TIME);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, datas);
    }

    //根据时间戳判定token是否过期
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

//	@ApiOperation(value = "获得七牛存储Token", notes = "获得七牛存储Token", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
//	@RequestMapping(value = "m/qiniuToken", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	public ObjResp getQiNiuToken(HttpServletRequest request,
//			HttpServletResponse response) {
//		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, Auth.getToken());
//	}

    @ApiOperation(value = "获得用户资产列表", notes = "即充值界面的所有代币和用户的资产列表，currencyLst表示所有代币信息列表，accountsLst表示用户资产列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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
        // 全局配置-需要身份认证
        if (frmConfig.getWithdraw_need_identity() == 1) {
            // 判断是否进行过身份认证
            AuthIdentity authIdentity = memberService.getAuthIdentityById(m.getId());
            if (null == authIdentity || authIdentity.getId_status() != 1) {
                // 用户认证不通过
                accountsCurrencyMap.put("accountAuth", 0);
            }
        }

        List<Currency> currencyList = HelpUtils.getCurrencyLst();

        List<LockAccount> lockAccountList = currencyLockAccountService.findByMemberId(m.getId());
        List<CurrencyLockReleaseDto> waitLockReleaseDtoList = currencyLockReleaseService
                .findWaitReleaseByMemberId(m.getId());
        // 所有代币信息列表
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
            // 从list中找到币种名称相同的任意一个即可
            CurrencyLockReleaseDto waitLockReleaseDto = waitLockReleaseDtoList.stream()
                    .filter(e -> accountDto.getCurrency().equals(e.getCurrency()))
                    .findAny().orElse(null);
            BigDecimal waitVolume = waitLockReleaseDto == null ? BigDecimal.ZERO : waitLockReleaseDto.getReleaseVolume();
            accountDto.setWait_release_num(waitVolume);

            // 添加到总额中
            accountDto.setTotal_balance(accountDto.getTotal_balance().add(accountDto.getLock_num()).add(waitVolume));
        }
        // 用户资产列表
        accountsCurrencyMap.put("accountsLst", accountDtoList);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, accountsCurrencyMap);
    }

    @ApiOperation(value = "获得当前用户信息", notes = "获得当前用户信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/member", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getMember(HttpServletRequest request,
                             HttpServletResponse response) {
//		String cookie = request.getHeader("cookie");
//		if(StringUtils.isNullOrEmpty(cookie))
//		{
//			logger.warn("获取用户信息时, cookie为空.请求IP为: " + HelpUtils.getIpAddr(request));
//		}
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }


        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, m);
    }


    @ApiOperation(value = "获得工单详细信息", notes = "工单标题、沟通记录", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/showDemand/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Demand getDemand(HttpServletRequest request,
                            HttpServletResponse response, @PathVariable Integer id) {

        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return null;
        }

        // 工单详细信息
        Demand demand = memberService.getDemandById(id);

        if (null == demand || demand.getMember_id().intValue() != m.getId().intValue()) {
            return null;
        }

        // 工单回复信息列表
        List<DemandLog> demandLogList = memberService.getDemandLogByDemandId(id);
        demand.setDemandLogList(demandLogList);

        return demand;
    }


    @ApiOperation(value = "提交身份证明", notes = "输入字段：given_name（名），middle_name（中间名，选填），family_name（姓）, sex（性别），birthday（生日），id_number（证件号），id_begin_date（证件有效期起），id_end_date（证件有效期止），province（省），city（市），nationality（国家），id_front_img（证件正面照图片地址），id_back_img（证件反面照图片地址），id_handheld_img（手持证件地址），idType（证件类型，passport、idcard等）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
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

        // 验证身份证是否重复
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
        //如果原来是中国验证，后来改成外国验证，要把token删除
        authIdentity.setBiz_token(null);

        memberService.addAuthIdentity(authIdentity);

        return new Resp(Resp.SUCCESS, ErrorInfoEnum.LANG_AUTH_IDENTITY_SUB_SUCCESS.getErrorENMsg());
    }


    @ApiOperation(value = "提交工单", notes = "提交工单，输入字段：d_title（工单标题），d_memo（工单内容）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/addDemand", method = RequestMethod.POST)
    @ResponseBody
    public Resp addDemand(HttpServletRequest request,
                          HttpServletResponse response, @RequestBody Demand demand) throws Exception {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // 替换< " 等字符
        demand.setD_title(HelpUtils.escapeHTMLTags(demand.getD_title()));
        demand.setD_memo(HelpUtils.escapeHTMLTags(demand.getD_memo()));

        demand.setMember_id(member.getId());
        demand.setM_name(member.getM_name());
        demand.setCreate_time(HelpUtils.formatDate8(new Date()));

        memberService.addDemand(demand);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "工单下继续留言", notes = "工单下继续留言，输入字段：demand_id（工单ID）， l_memo（留言内容），l_memo_type（内容类型，1表示文本，2表示图片等附件，不传时后端自动取1）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/addDemandLog", method = RequestMethod.POST)
    @ResponseBody
    public Resp addDemandLog(HttpServletRequest request,
                             HttpServletResponse response, @RequestBody DemandLog demandLog) throws Exception {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // 替换< " 等字符
        demandLog.setL_memo(HelpUtils.escapeHTMLTags(demandLog.getL_memo()));

        demandLog.setLog_type(0);
        demandLog.setCreate_time(HelpUtils.formatDate8(new Date()));
        demandLog.setD_status(0);
        //前端未设置时，默认为文本类型
        if (null == demandLog.getL_memo_type() || demandLog.getL_memo_type() > 2 || demandLog.getL_memo_type() < 1) {
            demandLog.setL_memo_type(1);
        }

        memberService.addDemandLog(demandLog);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "生成api_key和api_secret", notes = "给会员分配api_key和api_secret", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/apitoken", method = RequestMethod.POST)
    @ResponseBody
    public RespObjApiKeySecret genApiToken(HttpServletRequest request, HttpServletResponse response,
                                           @RequestBody ReqGenApiKey reqGenApiKey) {
        Member oldMember = null;
        try {
            oldMember = validateGenAndModifyApiToken(request, reqGenApiKey);
            return memberService.genApiToken(reqGenApiKey, oldMember);
        } catch (Exception e) {
            logger.warn("apiToken使用try-catch捕获异常，异常信息：{}", e.getMessage());
            return new RespObjApiKeySecret(Resp.FAIL, e.getMessage(), null, null);
        }
    }

    @ApiOperation(value = "修改apiToken", notes = "会员修改apiToken(只能修改标签和IP地址白名单和权限)", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
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
            logger.warn("apiToken使用try-catch捕获异常，异常信息：{}", e.getMessage());
            return new Resp(Resp.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "删除api token", notes = "删除api token", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
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
        // 判断谷歌验证码，只有用户启用了才判断，设置谷歌验证码时，会刷新Redis中数据，因此这里直接取Redis中的数据没有问题
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

    @ApiOperation(value = "获取用户Api Token列表", notes = "获取用户Api Token列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

        // 判断谷歌验证码，只有用户启用了才判断，设置谷歌验证码时，会刷新Redis中数据，因此这里直接取Redis中的数据没有问题
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

    @ApiOperation(value = "获取极验证校验参数", notes = "获取极验证校验参数", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/gtValidateCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getCaptcha(HttpServletRequest request,
                           HttpServletResponse response) throws IOException {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());

        String resStr = "{}";
        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
        //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        param.put("client_type", "web");
        //加入ip地址
        String ipAddr = HelpUtils.getIpAddr(request);
        param.put("ip_address", ipAddr);
        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);
        JedisUtil.getInstance().setCheckCode(gtSdk.gtServerStatusSessionKey + ipAddr, gtServerStatus + "", Constants.CHECK_CODE_TIME_OUT);
        resStr = gtSdk.getResponseStr();

        PrintWriter out = response.getWriter();
        out.println(resStr);
    }

    //	@ApiOperation(value = "获取JSSESSION", notes = "获取JSSESSION", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "分页获取每天返现记录", notes = "分页获取每天返现记录", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "分页获取邀请人数", notes = "分页获取邀请人数", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "获取邀请人数和返佣总数", notes = "获取邀请人数和返佣总数", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @ApiOperation(value = "修改用户手机号码", notes = "修改用户手机号码", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "m/phone", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp updateMemberPhone(HttpServletRequest request, @RequestBody Member member) throws IOException {
        Member m = JedisUtilMember.getInstance().getMember(request, member.getToken());
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // 后台再校验是否输入了校验码
        if (HelpUtils.nullOrBlank(member.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }

        // 后台再校验校验码是否正确
        Member oldMember = memberService.getMemberById(m.getId());
        if (null == oldMember || HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !member.getSms_code().equals(oldMember.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }
        Integer count = daoUtil.queryForInt("select count(*) from m_member where phone = ?", member.getPhone());
        if (count > 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.PHONE_HAS_BINDING.getErrorENMsg());
        }
        // 完成手机修改
        member.setPhone(member.getPhone());
        //将校验码换成新的，以免邮件泄露
        member.setSms_code(HelpUtils.randomNumNo0(6));
        member.setId(m.getId());
        memberService.updateMember(member, false, false);

        m.setPhone(member.getPhone());

        JedisUtilMember.getInstance().setMember(request, m);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "获取验证地址", notes = "获取验证地址", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/m/authorize", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp faceIdAuthorize(HttpServletRequest request) {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        ///
        //从redis读取缓存的biztoken
        String bizToken = getFaceIdTokenCache(m.getId());
        Map<String, Object> datas = new HashMap(4);
        datas.put("url", FaceIdUtil.DO_URL);
        datas.put("expire_time", FaceIdUtil.EXPIRE_TIME);
        //如果缓存存在则直接返回给前端
        if (bizToken != null) {
            datas.put("biz_token", bizToken);
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, datas);
        }
        AuthIdentity authIdentity = new AuthIdentity();
        authIdentity.setId(m.getId());
        authIdentity.setGiven_name("/");
        authIdentity.setFamily_name("/");
        authIdentity.setNationality("中国Chinese");
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
     * notify_url 使用 JavaSpring MVC 框架接受post过来的表单参数
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
            logger.warn("faceId notify失败，token：" + bizToken + e.getMessage());
        } finally {
            JedisUtil.getInstance().del(FACEID_CACHE_KEY + memberId);
        }
        return "";
    }

    //获取缓存的biztoken信息
    protected String getFaceIdTokenCache(Integer MemberId) {
        return (String) JedisUtil.getInstance().get(FACEID_CACHE_KEY + MemberId, true);
    }

    //缓存biztoken
    protected void faceIdTokenCache(Integer MemberId, String BizToken) {
        JedisUtil.getInstance().set(FACEID_CACHE_KEY + MemberId, BizToken, null);
        JedisUtil.getInstance().expire(FACEID_CACHE_KEY + MemberId, (int) FaceIdUtil.EXPIRE_TIME);
    }

}
