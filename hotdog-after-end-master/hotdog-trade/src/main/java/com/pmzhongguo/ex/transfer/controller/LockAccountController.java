package com.pmzhongguo.ex.transfer.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.ex.transfer.service.ThirdPartyService;
import com.qiniu.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 充值锁仓释放
 * @author jary
 * @creatTime 2019/8/2 9:28 AM
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/" + LockAccountController.c_name)
@ApiIgnore
public class LockAccountController extends TopController {

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private MemberService memberService;



    public static final String c_name = "lockAccount";

    /**
     * 释放入账
     * 判断充值id是否是已确认入账，
     * 校验签名
     * 入账
     * @param map
     * @param request
     * @return
     */
    @PostMapping(value = "/release",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp releaseAccount(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        if (BeanUtil.isEmpty(map.get("coinRechargeId"))) {
            return new Resp(Resp.FAIL, "充值id为空");
        }
        Member member = null;
        try {
            RespObj respObj = commonTransfer(map, c_name, request);
            if (respObj.getState() != 1) {
                logger.warn("<=================== 释放入账【/" + c_name + "/release】校验失败: " + map);
                return respObj;
            }
            member = (Member) respObj.getData();
            // 转入
            memberService.accountProc(new BigDecimal(map.get("transferNum").toString()), map.get("currency").toString().toUpperCase(), member.getId(), 3, OptSourceEnum.LOCKACCOUNT);

        } catch (Exception e) {
            // 异常
            logger.warn("<=============== 释放入账异常：{}", e.getMessage());
            e.printStackTrace();
            return new Resp(Resp.FAIL, e.getMessage());
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }


    /**
     * 校验
     *
     * @param map
     * @param c_name
     * @param request
     * @return
     */
    public RespObj commonTransfer(Map map, String c_name, HttpServletRequest request) {
        //校验项目方信息
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
        if (respObj.getState() != 1) {
            return respObj;
        }
        ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        ApiToken apiToken = getApiToken(thirdPartyInfo);
        if (BeanUtil.isEmpty(map.get("currency"))) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorCNMsg(), null);
        }
        // 判断币种是否支持
        Currency currency = HelpUtils.getCurrencyMap().get(map.get("currency").toString().toUpperCase());
        if (currency == null) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorCNMsg(), null);
        }
        if (thirdPartyInfo.getWithdraw_currency_list().contains(map.get("currency").toString().toUpperCase())) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorCNMsg(), null);
        }

        // 校验签名
        String validateRet = HelpUtils.preValidateBaseSecret(map);
        if (!"".equals(validateRet)) {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new RespObj(Resp.FAIL, validateRet, null);
        }
        // 校验合法性，非法情况直接抛出异常
        RespObj tokenValidate = validateProApiToken(map, apiToken, HelpUtils.getIpAddr(request), "Withdraw");
        if (tokenValidate.getState() != 1) {
            return tokenValidate;
        }
        // 校验用户信息
        RespObj resp = volidateMember(Integer.valueOf(map.get("memberId").toString()));
        if (resp.getState() != 1) {
            return resp;
        }
        Member member = (Member) resp.getData();

        // 判断划转金额是否大于0
        BigDecimal bigDecimal = new BigDecimal(map.get("transferNum").toString());
        if (bigDecimal.compareTo(BigDecimal.ZERO) <= 0) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_TRANSFERNUM.getErrorCNMsg(), null);
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
    }
    /**
     * 校验账户合法性
     *
     * @param memberId
     * @return
     */
    public RespObj volidateMember(Integer memberId) {
        if (BeanUtil.isEmpty(memberId)) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorCNMsg(), null);
        }
        // 查询用户
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorCNMsg(), null);
        }
        // 判断用户状态
        if (member.getM_status() == 0) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorCNMsg(), null);
        } else if (member.getM_status() == 2) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorCNMsg(), null);
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
    }

    /**
     * 获取ApiTokena
     *
     * @param thirdPartyInfo
     * @return
     */
    public ApiToken getApiToken(ThirdPartyInfo thirdPartyInfo)
    {
        ApiToken apiToken = new ApiToken();
        apiToken.setApi_key(thirdPartyInfo.getS_apiKey());
        apiToken.setApi_secret(thirdPartyInfo.getS_secretKey());
        apiToken.setCreate_time(HelpUtils.formatDate8(new Date()));
        apiToken.setLabel(thirdPartyInfo.getC_name());
        apiToken.setApi_privilege("Withdraw,Accounts,Order");
        apiToken.setApi_limit(0);
        return apiToken;
    }
}
