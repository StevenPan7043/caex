/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/6 All Rights Reserved.
 */
package com.pmzhongguo.ex.transfer.controller;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.mapper.MemberMapper;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.MacMD5;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.entity.AccountIntroReqVo;
import com.pmzhongguo.ex.transfer.entity.FundOperationLog;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.ex.transfer.service.FundOperationLogService;
import com.pmzhongguo.ex.transfer.service.FundTransferLogManager;
import com.pmzhongguo.ex.transfer.service.ThirdPartyService;
import com.qiniu.util.BeanUtil;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/9/10 16:01
 * @description：划转controller
 * @version: $
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/" + NRSTransferController.c_name)
@ApiIgnore
public class NRSTransferController extends TopController
{

    @Autowired
    private FundTransferLogManager fundTransferLogManager;

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private MemberService memberService;


    @Autowired
    private MemberMapper memberMapper;

    public static final String c_name = "nrs";

    private ThirdPartyInfo thirdPartyInfo = null;

    @Autowired
    private FundOperationLogService fundOperationLogService;

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
        if (StringUtils.isEmpty(apiFundTransfer.getTradeID())){
            return new Resp(Resp.FAIL, "交易id为空");
        }
        if (StringUtils.isEmpty(apiFundTransfer.getAddr())){
            return new Resp(Resp.FAIL, "充币地址为空");
        }
        List<FundOperationLog> fundOperationLogList = fundTransferLogManager.getFundOperationLogList(HelpUtils.newHashMap("tradeID", apiFundTransfer.getTradeID(),"tableName",c_name.toLowerCase()));
        if (!CollectionUtils.isEmpty(fundOperationLogList)){
            return new Resp(Resp.FAIL, "该交易信息已存在");
        }

        Resp resp = null;
        Member member = null;
        FundOperationLog fundOperationLog = null;
        try {
            RespObj respObj = commonTransfer(apiFundTransfer, c_name, request);
            if (respObj.getState() != 1) {
                logger.warn("<=================== 划转【充值】【/" + c_name + "/deposit】校验失败,原因：{} ",respObj.getMsg());
                logger.warn("<=================== 划转【充值】【/" + c_name + "/deposit】校验失败: " + JSON.toJSONString(apiFundTransfer));
                return respObj;
            }
            if (!thirdPartyInfo.getDeposit_currency_list().contains(apiFundTransfer.getCurrency().toLowerCase())){
                return new Resp(Resp.FAIL,"无该币种充币权限");
            }
            Currency currency = HelpUtils.getCurrencyMap().get(apiFundTransfer.getCurrency().toUpperCase());
            if (apiFundTransfer.getTransferNum().subtract(currency.getC_min_recharge()).compareTo(BigDecimal.ZERO) < 0 ){
                return new Resp(Resp.FAIL,ErrorInfoEnum.LANG_LITTLE_THAN_MIN_BUYVOLUME_TIP.getErrorCNMsg());
            }
            member = (Member) respObj.getData();
            // 转入
            apiFundTransfer.setTransferType("deposit");
            String ipAddr = HelpUtils.getIpAddr(request);
            resp = fundTransferLogManager.addFundTransferLog(apiFundTransfer, member, c_name, ipAddr, OptSourceEnum.NRS);
        } catch (Exception e) {
            // 异常
            logger.warn("<=============== " + c_name + "划转充值异常：{}", e.getMessage());
            e.printStackTrace();

//            fundOperationLog = fundTransferLogManager.createFundOperationLog(member, apiFundTransfer, c_name);
//            fundOperationLog.setTransferStatus("failed");
//            fundOperationLogService.insert(fundOperationLog);
            return new Resp(Resp.FAIL,"充值失败");
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
        if (StringUtils.isEmpty(apiFundTransfer.getTradeID())){
            return new Resp(Resp.FAIL, "交易id为空");
        }
        List<FundOperationLog> fundOperationLogList = fundTransferLogManager.getFundOperationLogList(HelpUtils.newHashMap("tradeID", apiFundTransfer.getTradeID(),"tableName",c_name.toLowerCase()));
        if (!CollectionUtils.isEmpty(fundOperationLogList)){
            return new Resp(Resp.FAIL, "该交易信息已存在");
        }
        RespObj respObj = commonTransfer(apiFundTransfer, c_name, request);
        if (respObj.getState() != 1) {
            logger.warn("<=================== 划转【提币】【/" + c_name + "/withdraw】校验失败: " + JSON.toJSONString(apiFundTransfer));
            return respObj;
        }
        Member member = (Member) respObj.getData();
        // 判断资金密码是否正确
        if (StringUtils.isEmpty(apiFundTransfer.getFundPwd())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorCNMsg());
        }
        // 是否设置资金密码
        if (member.getM_security_pwd() == null) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PLS_SET_SEC_PWD.getErrorCNMsg());
        }
        if (!MacMD5.CalcMD5Member(apiFundTransfer.getFundPwd()).equals(member.getM_security_pwd())) {
            logger.warn("划转【提币】【" + c_name + "/withdraw】入参:{}",MacMD5.CalcMD5Member(apiFundTransfer.getFundPwd()));
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorCNMsg());
        }


        Resp resp = null;
        FundOperationLog fundOperationLog = null;
        try {

            // 获取相反数, 减少资产
            apiFundTransfer.setTransferNum(apiFundTransfer.getTransferNum().negate());
            // 转出
            apiFundTransfer.setTransferType("withdraw");
            String ipAddr = HelpUtils.getIpAddr(request);
            resp = fundTransferLogManager.addFundTransferLog(apiFundTransfer, member, c_name, ipAddr,OptSourceEnum.NRS);
        } catch (Exception e) {
            // 异常
            logger.warn("<==============="+ c_name +"划转提现 异常：【{}】",e.getMessage());
            e.printStackTrace();

            fundOperationLog = fundTransferLogManager.createFundOperationLog(member, apiFundTransfer, c_name);
            fundOperationLog.setTransferStatus("failed");
            fundOperationLogService.insert(fundOperationLog);
            return new Resp(Resp.FAIL,e.getMessage());
        }
        return resp;
    }




    /**
     * 校验用户签名合法性
     * @return
     */
    public RespObj validateUserToken(HttpServletRequest request, Map param, ReqBaseSecret reqBaseSecret) {
        //校验项目方信息
        try {
            RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
            if (respObj.getState() != 1) {
                return respObj;
            }
            ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
            logger.warn("数据库token日志：[{}]",new Gson().toJson(thirdPartyInfo));
            ApiToken apiToken = getApiToken(thirdPartyInfo);
            logger.warn("转换成token后的日志：[{}]",new Gson().toJson(apiToken));
            String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
            if (!"".equals(validateRet)) {
                if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                    return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
                }
                return new RespObj(Resp.FAIL, validateRet, null);
            }
            Map req = HelpUtils.objToMap(reqBaseSecret);
            // 校验合法性，非法情况直接抛出异常
            RespObj tokenValidate = validateProApiToken(req, apiToken, HelpUtils.getIpAddr(request), "Withdraw");
            if (tokenValidate.getState() != 1) {
                return tokenValidate;
            }
        } catch (Exception e) {
            return new RespObj(Resp.FAIL, e.getMessage(), null);
        }
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
    public RespObj commonTransfer(ApiFundTransfer apiFundTransfer, String c_name, HttpServletRequest request)
    {
        //校验项目方信息
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
        if (respObj.getState() != 1)
        {
            return respObj;
        }
         thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        ApiToken apiToken = getApiToken(thirdPartyInfo);
        if (StringUtils.isEmpty(apiFundTransfer.getCurrency()))
        {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorCNMsg(), null);
        }
        if (BeanUtil.isEmpty(thirdPartyInfo.getExt())){
            return new RespObj(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorCNMsg(), null);
        }

        // 判断币种是否支持
        Currency currency = HelpUtils.getCurrencyMap().get(apiFundTransfer.getCurrency().toUpperCase());
        if (currency == null)
        {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorCNMsg(), null);
        }
        if (thirdPartyInfo.getWithdraw_currency_list().contains(apiFundTransfer.getCurrency().toUpperCase()))
        {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorCNMsg(), null);
        }
        // 校验签名
        String validateRet = HelpUtils.preValidateBaseSecret(apiFundTransfer);
        if (!"".equals(validateRet))
        {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet))
            {
                return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new RespObj(Resp.FAIL, validateRet, null);
        }
        // 校验合法性，非法情况直接抛出异常
        RespObj tokenValidate = validateProApiToken(HelpUtils.objToMap(apiFundTransfer), apiToken, HelpUtils.getIpAddr(request), "Withdraw");
        if (tokenValidate.getState() != 1) {
            return tokenValidate;
        }
        // 校验用户信息
        RespObj resp = volidateMember(apiFundTransfer.getAddr());
        if (resp.getState() != 1)
        {
            return resp;
        }
        Member member = (Member) resp.getData();
        // 判断划转金额是否大于0
        if (apiFundTransfer.getTransferNum().compareTo(BigDecimal.ZERO) <= 0)
        {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_TRANSFERNUM.getErrorCNMsg(), null);
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

    /**
     * 校验账户合法性
     *
     * @param addr
     * @return
     */
    public RespObj volidateMember(String addr)
    {

        // 查询用户

        CoinRechargeAddr coinParam = new CoinRechargeAddr();
        coinParam.setAddress(addr);
        List<CoinRechargeAddr> coinRechargeAddrList = memberMapper.getCoinRechargeAddrList(coinParam);
        if (CollectionUtils.isEmpty(coinRechargeAddrList))
        {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADDRESS.getErrorCNMsg(), null);
        }
        Member member = memberService.getMemberById(coinRechargeAddrList.get(0).getMember_id());
        if (member == null)
        {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorCNMsg(), null);
        }
        // 判断用户状态
        if (member.getM_status() == 0)
        {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorCNMsg(), null);
        } else if (member.getM_status() == 2)
        {
            return new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorCNMsg(), null);
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
    }


    /**
     * 查询充值记录
     *
     * @param reqBaseSecret
     * @param request
     * @return
     */
    @RequestMapping(value = "/coinRecharge")
    @ResponseBody
    public Resp coinRechargeBytxid(@RequestBody AccountIntroReqVo reqBaseSecret, HttpServletRequest request) {
        try {
//            logger.warn("<=================== 划转【查询充值记录】【/" + c_name + "/coinRecharge】签名参数= " + JSON.toJSONString(reqBaseSecret) + "交易ID= " + reqBaseSecret.getTradeID());
//            校验项目方信息
            RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
            if (respObj.getState() != 1) {
                logger.warn("<=================== 划转【查询充值记录】【/" + c_name + "/coinRecharge】校验失败: 签名参数= " + JSON.toJSONString(reqBaseSecret) + "交易ID= " + reqBaseSecret.getTradeID());
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
            req.put("tradeID", reqBaseSecret.getTradeID());
            // 校验合法性，非法情况直接抛出异常
            RespObj tokenValidate = validateProApiToken(req, apiToken, HelpUtils.getIpAddr(request), "Withdraw");
            if (tokenValidate.getState() != 1) {
                return tokenValidate;
            }
            // 校验用户信息
            RespObj resp = volidateMember(reqBaseSecret.getAddr());
            if (resp.getState() != 1)
            {
                return resp;
            }
            String create_time_end  = DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS);
            String create_time_start = DateUtil.dateToString(DateUtil.addMonth(new Date(),-3), DateStyleEnum.YYYY_MM_DD_HH_MM_SS);

            List<CoinRecharge> list = memberService.getAllCoinRecharge(
                    HelpUtils.newHashMap("r_txid", reqBaseSecret.getTradeID(), "r_address", reqBaseSecret.getAddr(), "create_time_start", create_time_start, "create_time_end", create_time_end));
            if (list != null && list.size() > 0) {
                return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
            return new Resp(Resp.FAIL, "系统异常，请联系管理员");
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    /**
     * 查询提币记录
     *
     * @param reqBaseSecret
     * @param request
     * @return
     */
    @RequestMapping(value = "/coinWithdraw")
    @ResponseBody
    public Resp coinWithdrawBytxid(@RequestBody AccountIntroReqVo reqBaseSecret, HttpServletRequest request) {
        try {
//            logger.warn("<=================== 划转【查询提币记录】【/" + c_name + "/coinWithdraw】 签名参数= " + JSON.toJSONString(reqBaseSecret) + "交易ID= " + reqBaseSecret.getTradeID());
            //校验项目方信息
            RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
            if (respObj.getState() != 1) {
                logger.warn("<=================== 划转【查询提币记录】【/" + c_name + "/coinWithdraw】校验失败: 签名参数= " + JSON.toJSONString(reqBaseSecret) + "交易ID= " + reqBaseSecret.getTradeID());
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
            req.put("tradeID", reqBaseSecret.getTradeID());
            // 校验合法性，非法情况直接抛出异常
            RespObj tokenValidate = validateProApiToken(req, apiToken, HelpUtils.getIpAddr(request), "Withdraw");
            if (tokenValidate.getState() != 1){
                return tokenValidate;
            }

            String create_time_end  = DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS);
            String create_time_start = DateUtil.dateToString(DateUtil.addMonth(new Date(),-3), DateStyleEnum.YYYY_MM_DD_HH_MM_SS);
            List<CoinWithdraw> list = memberService.getAllCoinWithdraw(HelpUtils.newHashMap("w_txid", reqBaseSecret.getTradeID(),"member_coin_addr",reqBaseSecret.getAddr(), "create_time_start", create_time_start, "create_time_end", create_time_end));
            if (list != null && list.size() > 0) {
                return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
            return new Resp(Resp.FAIL, "系统异常，请联系管理员");
        }
        return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

}
