package com.pmzhongguo.ex.transfer.controller;

import com.google.gson.Gson;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Account;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.business.vo.MemberVo;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.entity.CTransferReqVo;
import com.pmzhongguo.ex.transfer.service.AccountManager;
import com.pmzhongguo.otc.entity.dto.AccountDTO;
import com.qiniu.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author jary
 * @creatTime 2019/6/22 3:50 PM
 * 用户转入、转出
 */
@ApiIgnore
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/ct")
public class CommunityController  extends TopController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private AccountManager accountManager;

    /**
     * 交易所转入到社区 交易所转出，扣掉用户资产，对方平台转入
     * @param request
     * @return
     */
    @RequestMapping(value = "/communityTransfer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp communityTransfer(HttpServletRequest request, @RequestBody CTransferReqVo cTransferReqVo) {
        String ipAddr = HelpUtils.getIpAddr(request);
        logger.warn("交易所转入到社区入参参数:{},请求IP：{}",new Gson().toJson(cTransferReqVo),ipAddr);
        try {
            //ip校验
            String communityIpStr = PropertiesUtil.getPropValByKey("community_ip");
            List<String> communityIpArr = Arrays.asList(communityIpStr.split(","));
            if (!communityIpArr.contains(ipAddr)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_IP.getErrorCNMsg(), null);
            }
            cTransferReqVo.setIp(ipAddr);
            cTransferReqVo.setCurrency(cTransferReqVo.getCurrency().toUpperCase());
            //参数校验
            ObjResp objResp = this.validateReqVo(request, cTransferReqVo);
            if (objResp.getState() == -1) {
                return objResp;
            }
            Member member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", cTransferReqVo.getUsername()));
            if (BeanUtil.isEmpty(member)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorCNMsg(), null);
            }
            if (!member.getM_pwd().equals(MacMD5.CalcMD5Member(cTransferReqVo.getPassword()))) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.PASSWORD_ERROR.getErrorCNMsg(), null);
            }
            if (!member.getM_security_pwd().equals(MacMD5.CalcMD5Member(cTransferReqVo.getPaypassword()))) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorCNMsg(), null);
            }
            Currency currency = HelpUtils.getCurrencyMap().get(cTransferReqVo.getCurrency());
            if (BeanUtil.isEmpty(currency)){
                return new ObjResp(Resp.FAIL,ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorCNMsg(),null);
            }
            Account account = memberService.getAccount(HelpUtils.newHashMap("member_id", member.getId(), "currency", cTransferReqVo.getCurrency()));

            if (BeanUtil.isEmpty(account)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorCNMsg(), null);
            }
            if (cTransferReqVo.getNum().subtract(account.getTotal_balance().subtract(account.getFrozen_balance())).compareTo(BigDecimal.ZERO) > 0) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorCNMsg(), null);
            }
            accountManager.transfer(cTransferReqVo, member, account);
        } catch (Exception e) {
            logger.warn("转入异常,交易所转入到社区入参：{}", new Gson().toJson(cTransferReqVo));
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_DATA_ERROR.getErrorCNMsg(), null);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }


    /**
     * 校验入参
     * @param request
     * @param cTransferReqVo
     * @return
     */
    private ObjResp validateReqVo(HttpServletRequest request, CTransferReqVo cTransferReqVo) {

        if (BeanUtil.isEmpty(cTransferReqVo.getPassword())) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.PASSWORD_ERROR.getErrorCNMsg(), null);

        }
        if (BeanUtil.isEmpty(cTransferReqVo.getPaypassword())) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.PAYMENT_PASSWORD_CANNOT_BE_EMPTY.getErrorCNMsg(), null);
        }


        if (BeanUtil.isEmpty(cTransferReqVo.getToaddress())) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.TRANSFER_ACCOUNT_CANNOT_BE_EMPTY.getErrorCNMsg(), null);
        }
        if (BeanUtil.isEmpty(cTransferReqVo.getNum())) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorCNMsg(), null);
        }

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    /**
     * 对方平台转出，自己交易所   增加用户资产
     * @param request
     * @param cTransferReqVo
     * @return
     */
    @RequestMapping(value = "/rollOut", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp rollOut(HttpServletRequest request, @RequestBody CTransferReqVo cTransferReqVo) {
        String ipAddr = HelpUtils.getIpAddr(request);
        logger.warn("平台转出入参参数：{},请求IP：{}",new Gson().toJson(cTransferReqVo),ipAddr);
        try {
            //ip校验
            String communityIpStr = PropertiesUtil.getPropValByKey("community_ip");
            List<String> communityIpArr = Arrays.asList(communityIpStr.split(","));
            if (!communityIpArr.contains(ipAddr)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_IP.getErrorCNMsg(), null);
            }
            cTransferReqVo.setIp(ipAddr);
            //参数校验
            if (BeanUtil.isEmpty(cTransferReqVo)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorCNMsg(), null);
            }
            if (BeanUtil.isEmpty(cTransferReqVo.getUsername())) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ISNOTEMPTY_NICKNAME.getErrorCNMsg(), null);

            }
            if (BeanUtil.isEmpty(cTransferReqVo.getCurrency())) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorCNMsg(), null);
            }
            cTransferReqVo.setCurrency(cTransferReqVo.getCurrency().toUpperCase());
            if (BeanUtil.isEmpty(cTransferReqVo.getNum())) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorCNMsg(), null);
            }
            if (cTransferReqVo.getNum().compareTo(BigDecimal.ZERO) <0) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorCNMsg(), null);
            }

            Member member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", cTransferReqVo.getUsername()));
            if (BeanUtil.isEmpty(member)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorCNMsg(), null);
            }
            Currency currency = HelpUtils.getCurrencyMap().get(cTransferReqVo.getCurrency());
            if (BeanUtil.isEmpty(currency)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorCNMsg(), null);
            }
            Account account = memberService.getAccount(HelpUtils.newHashMap("member_id", member.getId(), "currency", cTransferReqVo.getCurrency()));

//            if (BeanUtil.isEmpty(account)) {
//                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorCNMsg(), null);
//            }
//
//            if (cTransferReqVo.getNum().subtract(account.getTotal_balance()).compareTo(BigDecimal.ZERO) > 0) {
//                return new ObjResp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorCNMsg(), null);
//            }
            accountManager.rollOut(cTransferReqVo, member, account);
        } catch (Exception e) {
            logger.warn("转出异常,转出到交易平台入参：{}", new Gson().toJson(cTransferReqVo));
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_DATA_ERROR.getErrorCNMsg(), null);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }


    /**
     * 校验用户资产
     * @param request
     * @param cTransferReqVo
     * @return
     */
    @RequestMapping(value = "/checklogin", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp checklogin(HttpServletRequest request, @RequestBody CTransferReqVo cTransferReqVo) {
        String ipAddr = HelpUtils.getIpAddr(request);
        logger.warn("校验用户资产入参参数：{},请求IP：{}", new Gson().toJson(cTransferReqVo), ipAddr);
        try {
            //ip校验
            String communityIpStr = PropertiesUtil.getPropValByKey("community_ip");
            List<String> communityIpArr = Arrays.asList(communityIpStr.split(","));
            if (!communityIpArr.contains(ipAddr)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_IP.getErrorCNMsg(), null);
            }
            cTransferReqVo.setIp(ipAddr);
            //参数校验
            if (BeanUtil.isEmpty(cTransferReqVo)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorCNMsg(), null);
            }
            if (BeanUtil.isEmpty(cTransferReqVo.getUsername())) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ISNOTEMPTY_NICKNAME.getErrorCNMsg(), null);
            }
            if (BeanUtil.isEmpty(cTransferReqVo.getPassword())) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.PASSWORD_ERROR.getErrorCNMsg(), null);
            }
            if (BeanUtil.isEmpty(cTransferReqVo.getCurrency())) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorCNMsg(), null);
            }
            cTransferReqVo.setCurrency(cTransferReqVo.getCurrency().toUpperCase());
            Member member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", cTransferReqVo.getUsername()));
            if (BeanUtil.isEmpty(member)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorCNMsg(), null);
            }
            if (!member.getM_pwd().equals(MacMD5.CalcMD5Member(cTransferReqVo.getPassword()))) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.PASSWORD_ERROR.getErrorCNMsg(), null);
            }
            Account account = memberService.getAccount(HelpUtils.newHashMap("member_id", member.getId(), "currency", cTransferReqVo.getCurrency()));
            if (BeanUtil.isEmpty(account)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorCNMsg(), null);
            }
            MemberVo memberVo = new MemberVo();
            BeanUtils.copyProperties(member, memberVo);
            memberVo.setAccount(account.getTotal_balance().subtract(account.getFrozen_balance()));
            memberVo.setM_pwd("");
            memberVo.setM_security_pwd("");
            memberVo.setSms_code("");
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, memberVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_DATA_ERROR.getErrorCNMsg(), null);
        }
    }
}
