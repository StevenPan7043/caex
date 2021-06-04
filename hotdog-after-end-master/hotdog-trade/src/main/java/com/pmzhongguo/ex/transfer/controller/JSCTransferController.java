/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/6 All Rights Reserved.
 */
package com.pmzhongguo.ex.transfer.controller;

import com.alibaba.fastjson.JSON;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.HttpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.utils.MacMD5;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.core.web.resp.ResponseModel;
import com.pmzhongguo.ex.transfer.entity.FundOperationLog;
import com.pmzhongguo.ex.transfer.entity.RespUserSecret;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.ex.transfer.service.FundOperationLogService;
import com.pmzhongguo.ex.transfer.service.FundTransferLogManager;
import com.pmzhongguo.ex.transfer.service.SSOUsersTokenManager;
import com.pmzhongguo.ex.transfer.service.ThirdPartyService;
import com.qiniu.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/6 12:01
 * @description：划转controller
 * @version: $
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/" + JSCTransferController.c_name)
@ApiIgnore
public class JSCTransferController extends TopController {

    @Autowired
    private FundTransferLogManager fundTransferLogManager;
    @Autowired
    private SSOUsersTokenManager sSOTokenManager;
    @Autowired
    private ThirdPartyService thirdPartyService;

    public static final String c_name = "JSC";
    @Autowired
    private FundOperationLogService fundOperationLogService;

    /**
     * 用户token信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getUserSSOToken", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getUserSSOToken(HttpServletRequest request) {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
        }
        // 登录校验
        ObjResp objResp = memberService.loginCommon(request, member, false);
        if (objResp.getState() != 1) {
            return objResp;
        }
        RespUserSecret reqBaseSecret = sSOTokenManager.getUserSSOToken(member, c_name);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, reqBaseSecret);
    }

    /**
     * 划转充值
     *
     * @param apiFundTransfer
     * @param request
     * @return
     */
    @PostMapping(value = "/deposit")
    @ResponseBody
    public Resp fundTransferDeposit(@RequestBody ApiFundTransfer apiFundTransfer, HttpServletRequest request) {
        RespObj respObj = commonTransfer(apiFundTransfer, c_name, request);
        if (respObj.getState() != 1) {
            logger.warn("<=================== 划转【充值】【/" + c_name + "/deposit】校验失败,原因：{} ",respObj.getMsg());
            logger.warn("<=================== 划转【充值】【/" + c_name + "/deposit】校验失败: " + JSON.toJSONString(apiFundTransfer));
            return respObj;
        }
        Member member = (Member) respObj.getData();
        // 转入
        apiFundTransfer.setTransferType("deposit");
        String ipAddr = HelpUtils.getIpAddr(request);
        Resp resp = null;
        FundOperationLog fundOperationLog = null;
        try {

            resp = fundTransferLogManager.addFundTransferLog(apiFundTransfer, member, c_name, ipAddr, OptSourceEnum.JSC);
        } catch (Exception e) {
            // 异常
            logger.warn("<=============== jsc 划转充值异常：{}", e.getMessage());
            e.printStackTrace();

            fundOperationLog = fundTransferLogManager.createFundOperationLog(member, apiFundTransfer, c_name);
            fundOperationLog.setTransferStatus("failed");
            fundOperationLogService.insert(fundOperationLog);
            return new Resp(Resp.FAIL,e.getMessage());
        }
        // 记录操作日志表
        fundOperationLog = fundTransferLogManager.createFundOperationLog(member, apiFundTransfer, c_name);
        fundOperationLogService.insert(fundOperationLog);
        return resp;
    }

    /**
     * 划转提现
     *
     * @param apiFundTransfer
     * @param request
     * @return
     */
    @PostMapping(value = "/withdraw")
    @ResponseBody
    public Resp fundTransferWithdraw(@RequestBody ApiFundTransfer apiFundTransfer, HttpServletRequest request) {
        RespObj respObj = commonTransfer(apiFundTransfer, c_name, request);
        if (respObj.getState() != 1) {
            logger.warn("<=================== 划转【提币】【/" + c_name + "/withdraw】校验失败: " + JSON.toJSONString(apiFundTransfer));
            return respObj;
        }
        Member member = (Member) respObj.getData();
        // 判断资金密码是否正确
        if (StringUtils.isEmpty(apiFundTransfer.getFundPwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg());
        }
        // 是否设置资金密码
        if (member.getM_security_pwd() == null) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PLS_SET_SEC_PWD.getErrorENMsg());
        }
        if (!MacMD5.CalcMD5Member(apiFundTransfer.getFundPwd()).equals(member.getM_security_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg());
        }
        // 获取相反数, 减少资产
        apiFundTransfer.setTransferNum(apiFundTransfer.getTransferNum().negate());
        // 转出
        apiFundTransfer.setTransferType("withdraw");
        String ipAddr = HelpUtils.getIpAddr(request);
        Resp resp = null;
        FundOperationLog fundOperationLog = null;
        try {
            resp = fundTransferLogManager.addFundTransferLog(apiFundTransfer, member, c_name, ipAddr,OptSourceEnum.JSC);
        } catch (Exception e) {
            // 异常
            logger.warn("<=============== jsc 划转提现 异常：【{}】",e.getMessage());
            e.printStackTrace();

            fundOperationLog = fundTransferLogManager.createFundOperationLog(member, apiFundTransfer, c_name);
            fundOperationLog.setTransferStatus("failed");
            fundOperationLogService.insert(fundOperationLog);
            return new Resp(Resp.FAIL,e.getMessage());
        }
        return resp;
    }

    /**
     * 校验订单合法性
     *
     * @param account
     * @param reqBaseSecret
     * @param request
     * @return
     */
    @RequestMapping(value = "/checkDeposit")
    @ResponseBody
    public Resp checkDeposit(String account, ReqBaseSecret reqBaseSecret, HttpServletRequest request) {
        //校验项目方信息
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
        if (respObj.getState() != 1) {
            logger.warn("<=================== 划转【校验订单合法性】【/" + c_name + "/checkDeposit】校验失败: 签名参数= " + JSON.toJSONString(reqBaseSecret) + "用户名= " + account);
            return respObj;
        }
        ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        ApiToken apiToken = getApiToken(thirdPartyInfo);

        String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
        if (!"".equals(validateRet)) {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new RespObj(Resp.FAIL, validateRet, null);
        }
        Map req = HelpUtils.objToMap(reqBaseSecret);
        req.put("account", account);
        // 校验合法性，非法情况直接抛出异常
        validateApiToken(req, apiToken, HelpUtils.getIpAddr(request), "Withdraw");
        Resp resp = volidateMember(account);
        if (resp.getState() != 1) {
            return resp;
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, true);
    }

    /**
     * 查询充值记录
     *
     * @param tradeID
     * @param reqBaseSecret
     * @param request
     * @return
     */
    @RequestMapping(value = "/coinRecharge")
    @ResponseBody
    public Resp coinRechargeBytxid(String tradeID, ReqBaseSecret reqBaseSecret, HttpServletRequest request) {
        //校验项目方信息
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
        if (respObj.getState() != 1) {
            logger.warn("<=================== 划转【查询充值记录】【/" + c_name + "/coinRecharge】校验失败: 签名参数= " + JSON.toJSONString(reqBaseSecret) + "交易ID= " + tradeID);
            return respObj;
        }
        ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        ApiToken apiToken = getApiToken(thirdPartyInfo);

        String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
        if (!"".equals(validateRet)) {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new RespObj(Resp.FAIL, validateRet, null);
        }
        Map req = HelpUtils.objToMap(reqBaseSecret);
        req.put("tradeID", tradeID);
        // 校验合法性，非法情况直接抛出异常
        validateApiToken(req, apiToken, HelpUtils.getIpAddr(request), "Withdraw");

        List<CoinRecharge> list = memberService.getAllCoinRecharge(HelpUtils.newHashMap("r_txid", tradeID));
        if (list != null && list.size() > 0) {
            return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, true);
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, false);
    }

    /**
     * 查询提币记录
     *
     * @param tradeID
     * @param reqBaseSecret
     * @param request
     * @return
     */
    @RequestMapping(value = "/coinWithdraw")
    @ResponseBody
    public Resp coinWithdrawBytxid(String tradeID, ReqBaseSecret reqBaseSecret, HttpServletRequest request) {
        //校验项目方信息
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
        if (respObj.getState() != 1) {
            logger.warn("<=================== 划转【查询提币记录】【/" + c_name + "/coinWithdraw】校验失败: 签名参数= " + JSON.toJSONString(reqBaseSecret) + "交易ID= " + tradeID);
            return respObj;
        }
        ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        ApiToken apiToken = getApiToken(thirdPartyInfo);

        String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
        if (!"".equals(validateRet)) {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new RespObj(Resp.FAIL, validateRet, null);
        }
        Map req = HelpUtils.objToMap(reqBaseSecret);
        req.put("tradeID", tradeID);
        // 校验合法性，非法情况直接抛出异常
        validateApiToken(req, apiToken, HelpUtils.getIpAddr(request), "Withdraw");

        List<CoinWithdraw> list = memberService.getAllCoinWithdraw(HelpUtils.newHashMap("w_txid", tradeID));
        if (list != null && list.size() > 0) {
            return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, true);
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, false);
    }

    /**
     * 查询账户上下级用户接口
     *
     * @param request
     * @param m_name    用户名
     * @param direction 方向  up或down
     * @param level     等级
     * @return
     */
    @RequestMapping(value = "/accountIntroduce")
    @ResponseBody
    public ObjResp accountIntroduce(HttpServletRequest request, String m_name, String direction, String level, ReqBaseSecret reqBaseSecret) {

        // 查询用户信息
        Map param = $params(request);
        Member member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", param.get("m_name")));
        if (member == null) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg(), null);
        }
        //校验apiToken
        RespObj respObj = this.validateUserToken(request, param, reqBaseSecret);
        if (respObj.getState() != 1) {
            logger.warn("<=================== 划转【查询账户邀请人】【/" + c_name + "/accountIntroduce】校验失败: 请求参数= " + JSON.toJSONString(param));
            return new ObjResp(respObj.getState(), respObj.getMsg(), respObj.getData());
        }
        //todo 先判断查询方向，再判断查询级别
        if (StringUtils.isBlank(direction) && StringUtils.isBlank(level)) {
            level = 3 + "";
        }
        if (!BeanUtil.isNumber(level)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LEVEL_MAST_BE_NUMBER.getErrorENMsg(), null);
        }
        return memberService.getLeMemberMap(direction, level, member);
    }

    /**
     * 校验用户签名合法性
     *
     * @return
     */
    public RespObj validateUserToken(HttpServletRequest request, Map param, ReqBaseSecret reqBaseSecret) {
        //校验项目方信息
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
        if (respObj.getState() != 1) {
            return respObj;
        }
        ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        ApiToken apiToken = getApiToken(thirdPartyInfo);
        String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
        if (!"".equals(validateRet)) {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new RespObj(Resp.FAIL, validateRet, null);
        }
        Map req = HelpUtils.objToMap(reqBaseSecret);
        // 校验合法性，非法情况直接抛出异常
        validateApiToken(req, apiToken, HelpUtils.getIpAddr(request), "Withdraw");
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, true);
    }

    /**
     * 划转校验
     *
     * @param apiFundTransfer
     * @param c_name
     * @param request
     * @return
     */
    public RespObj commonTransfer(ApiFundTransfer apiFundTransfer, String c_name, HttpServletRequest request) {
        //校验项目方信息
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
        if (respObj.getState() != 1) {
            return respObj;
        }
        ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        ApiToken apiToken = getApiToken(thirdPartyInfo);
        if (StringUtils.isEmpty(apiFundTransfer.getCurrency())) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg(), null);
        }
        // 判断币种是否支持
        Currency currency = HelpUtils.getCurrencyMap().get(apiFundTransfer.getCurrency().toUpperCase());
        if (currency == null) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorENMsg(), null);
        }
        if (thirdPartyInfo.getWithdraw_currency_list().contains(apiFundTransfer.getCurrency().toUpperCase())) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
        }

        // 校验签名
        String validateRet = HelpUtils.preValidateBaseSecret(apiFundTransfer);
        if (!"".equals(validateRet)) {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new RespObj(Resp.FAIL, validateRet, null);
        }
        // 校验合法性，非法情况直接抛出异常
        validateApiToken(HelpUtils.objToMap(apiFundTransfer), apiToken, HelpUtils.getIpAddr(request), "Withdraw");
        // 校验用户信息
        RespObj resp = volidateMember(apiFundTransfer.getM_name());
        if (resp.getState() != 1) {
            return resp;
        }
        Member member = (Member) resp.getData();
        // 调用客户接口判断请求是否合法
        String url = thirdPartyInfo.getC_ip() + apiFundTransfer.getTradeID();
        try {
            String result = HttpUtils.get(url, HelpUtils.newHashMap("app_key", thirdPartyInfo.getC_appKey()), "UTF-8");
            if (StringUtils.isBlank(result)) {
                return new RespObj(Resp.FAIL, "connection timed out", null);
            }
            ResponseModel responseModel = JSON.parseObject(result, ResponseModel.class);
            if (responseModel == null) {
                return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CLIENT_STATUS.getErrorENMsg(), null);
            }
            if (StringUtils.isEmpty(thirdPartyInfo.getC_code())) {
                return new RespObj(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg(), null);
            }
            // 校验是否返回的正确码
            if (responseModel.getCode().intValue() != Integer.valueOf(thirdPartyInfo.getC_code()).intValue()) {
                return new RespObj(Resp.FAIL, responseModel.getMessage(), responseModel.getData());
            }
        } catch (Exception e) {
            logger.error("<================== 调用客户端服务异常: " + e.getLocalizedMessage());
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CLIENT_STATUS.getErrorENMsg(), null);
        }
        // 判断划转金额是否大于0
        if (apiFundTransfer.getTransferNum().compareTo(BigDecimal.ZERO) <= 0) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_TRANSFERNUM.getErrorENMsg(), null);
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
    }

    /**
     * 查询账户可用资产
     *
     * @param apiFundTransfer
     * @param request
     * @return
     */
    @RequestMapping(value = "/fundAssetsInfo")
    @ResponseBody
    public Resp fundAssetsInfo(ApiFundTransfer apiFundTransfer, HttpServletRequest request) {
        if (StringUtils.isEmpty(apiFundTransfer.getCurrency())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg());
        }
        //校验项目方信息
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
        if (respObj.getState() != 1) {
            logger.warn("<=================== 划转【查询账户可用资产】【/" + c_name + "/fundAssetsInfo】校验失败: 请求参数= " + JSON.toJSONString(apiFundTransfer));
            return respObj;
        }
        ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        ApiToken apiToken = getApiToken(thirdPartyInfo);

        // 校验签名
        String validateRet = HelpUtils.preValidateBaseSecret(apiFundTransfer);
        if (!"".equals(validateRet)) {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new RespObj(Resp.FAIL, validateRet, null);
        }
        // 校验合法性，非法情况直接抛出异常
        validateApiToken(HelpUtils.objToMap(apiFundTransfer), apiToken, HelpUtils.getIpAddr(request), "Withdraw");
        // 校验用户信息
        RespObj resp = volidateMember(apiFundTransfer.getM_name());
        if (resp.getState() != 1) {
            return resp;
        }
        Member member = (Member) resp.getData();
        Currency currency = HelpUtils.getCurrencyMap().get(apiFundTransfer.getCurrency().toUpperCase());
        if (currency == null) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorENMsg());
        }
        // 判断可提币币种配置是否包含
        if (!thirdPartyInfo.getWithdraw_currency_list().contains(apiFundTransfer.getCurrency().toLowerCase())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg());
        }

        BigDecimal frozenToBalance = BigDecimal.ZERO;
        try {
            // 查询资产余额
            frozenToBalance = daoUtil.queryForObject(
                    "SELECT total_balance - frozen_balance FROM m_account WHERE member_id = ? AND currency = ?",
                    BigDecimal.class, member.getId(), apiFundTransfer.getCurrency().toUpperCase());
            if (frozenToBalance == null) {
                frozenToBalance = BigDecimal.ZERO;
            } else {
                frozenToBalance = frozenToBalance.setScale(currency.getC_precision(), BigDecimal.ROUND_HALF_UP);
            }
        } catch (Exception e) {
            logger.error("请求参数为：" + JSON.toJSONString(apiFundTransfer));
            logger.error("获取账户资产余额异常: " + e.getLocalizedMessage());
        }

        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, frozenToBalance.toPlainString());
    }

    /**
     * 资金密码校验绑定
     *
     * @param apiFundTransfer
     * @param request
     * @return
     */
    @RequestMapping(value = "/binding", method = RequestMethod.POST)
    @ResponseBody
    public Resp jscBindingAccount(@RequestBody ApiFundTransfer apiFundTransfer, HttpServletRequest request) {
        //校验项目方信息
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
        if (respObj.getState() != 1) {
            logger.warn("<=================== 划转【资金密码校验绑定】【/" + c_name + "/binding】校验失败: 请求参数= " + JSON.toJSONString(apiFundTransfer));
            return respObj;
        }
        ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        ApiToken apiToken = getApiToken(thirdPartyInfo);

        // 校验签名
        String validateRet = HelpUtils.preValidateBaseSecret(apiFundTransfer);
        if (!"".equals(validateRet)) {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new RespObj(Resp.FAIL, validateRet, null);
        }
        // 校验合法性，非法情况直接抛出异常
        validateApiToken(HelpUtils.objToMap(apiFundTransfer), apiToken, HelpUtils.getIpAddr(request), "Withdraw");
        // 校验用户信息
        RespObj resp = volidateMember(apiFundTransfer.getM_name());
        if (resp.getState() != 1) {
            return resp;
        }
        Member member = (Member) resp.getData();

        String fundPwd = member.getM_security_pwd();
        // 是否设置资金密码
        if (fundPwd == null) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PLS_SET_SEC_PWD.getErrorCNMsg());
        }
        // 验证资金密码
        if (!MacMD5.CalcMD5Member(apiFundTransfer.getFundPwd()).equals(member.getM_security_pwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg());
        }
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * 获取ApiToken
     *
     * @param thirdPartyInfo
     * @return
     */
    public ApiToken getApiToken(ThirdPartyInfo thirdPartyInfo) {
        ApiToken apiToken = new ApiToken();
        apiToken.setApi_key(thirdPartyInfo.getS_apiKey());
        apiToken.setApi_secret(thirdPartyInfo.getS_secretKey());
        apiToken.setCreate_time(HelpUtils.formatDate8(new Date()));
        apiToken.setLabel(thirdPartyInfo.getC_name());
        apiToken.setApi_privilege("Withdraw,Accounts,Order");
        apiToken.setApi_limit(0);
        return apiToken;
    }

    /**
     * 校验账户合法性
     *
     * @param account
     * @return
     */
    public RespObj volidateMember(String account) {
        if (StringUtils.isBlank(account)) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg(), null);
        }
        // 查询用户
        Map params = new HashMap();
        params.put("m_name", account);
        Member member = memberService.getMemberBy(params);
        if (member == null) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg(), null);
        }
        // 判断用户状态
        if (member.getM_status() == 0) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg(), null);
        } else if (member.getM_status() == 2) {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg(), null);
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
    }
}
