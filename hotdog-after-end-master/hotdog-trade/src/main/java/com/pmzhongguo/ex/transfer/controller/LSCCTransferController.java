/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/6 All Rights Reserved.
 */
package com.pmzhongguo.ex.transfer.controller;
import java.math.BigDecimal;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.service.ExService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.business.service.TradeService;
import com.pmzhongguo.ex.business.vo.ApiFundVo;
import com.pmzhongguo.ex.business.vo.OrderVo;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.ex.transfer.service.ThirdPartyService;
import com.pmzhongguo.otc.otcenum.OrderStatusEnum;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.qiniu.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：Jary
 * @date ：Created in 2019/11/12 16:01
 *
 * @version: $
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/" + LSCCTransferController.c_name)
@ApiIgnore
public class LSCCTransferController extends TopController {

    @Autowired
    private MemberService memberService;

    @Resource
    private ExService exService;


    public static final String c_name = "lscc";
    @Autowired
    private ThirdPartyService thirdPartyService;

    private ThirdPartyInfo thirdPartyInfo = null;
    /**
     *
     * 根据用户名查询用户UID
     * @param request
     * @param apiFundVo
     * @return
     */
    @RequestMapping(value = "getMemberUid", method = RequestMethod.POST)
    @ResponseBody
    public RespObj getMemberUid(HttpServletRequest request, @RequestBody ApiFundVo apiFundVo) {
        RespObj respObj = null;
        try {
            if (BeanUtil.isEmpty(apiFundVo.getM_name())) {
                return new RespObj(Resp.FAIL, "用户名为空", null);
            }
            respObj = commonTransfer(apiFundVo, c_name);
            if (respObj.getState().equals(Resp.FAIL)) {
                return respObj;
            }
            Member member = (Member) respObj.getData();
            Account account = memberService.getAccount(HelpUtils.newHashMap("member_id", member.getId(), "currency", c_name.toUpperCase()));
            Map<String, Object> resultData = new HashMap<>();
            resultData.put("UID", member.getId());
            resultData.put("m_name", member.getM_name());
            resultData.put("auth_grade", member.getAuth_grade()>0?"已认证":"未认证");
            Map<String, Object> currencyBalanceMap = new HashMap<>();
            currencyBalanceMap.put("LSCC", BeanUtil.isEmpty(account) ? null : account.getAvailable_balance());
            resultData.put("currencyBalance", currencyBalanceMap);
            respObj.setData(resultData);
        } catch (Exception e) {
            e.printStackTrace();
            return new RespObj(Resp.FAIL, "系统繁忙", null);
        }
        return respObj;
    }

    /**
     * 校验订单号
     * @param request
     * @param apiFundVo
     * @return
     */
    @RequestMapping(value = "checkOrderNo", method = RequestMethod.POST)
    @ResponseBody
    public RespObj checkOrderNo(HttpServletRequest request, @RequestBody ApiFundVo apiFundVo) {
        try {
            if (BeanUtil.isEmpty(apiFundVo.getM_name())) {
                return new RespObj(Resp.FAIL, "用户名为空", null);
            }
            if (BeanUtil.isEmpty(apiFundVo.getO_no())) {
                return new RespObj(Resp.FAIL, "订单号为空", null);
            }
            if (BeanUtil.isEmpty(apiFundVo.getSymbol())) {
                return new RespObj(Resp.FAIL, "交易对为空", null);
            }
            RespObj respObj = commonTransfer(apiFundVo, c_name);
            if (respObj.getState().equals(Resp.FAIL)) {
                return respObj;
            }
            Member member = (Member) respObj.getData();
            Order order = getOrder(member, apiFundVo);
            if (BeanUtil.isEmpty(order)) {
                return new RespObj(Resp.FAIL, "订单不存在", null);
            }
            if (order.getO_status().equals(OrderStatusEnum.DONE.getCode())
                    || order.getO_status().equals(OrderStatusEnum.PC.getCode())
                    || order.getO_status().equals(OrderStatusEnum.CANCELED.getCode())
            ) {
                OrderVo orderVo = new OrderVo();
                orderVo.setM_name(apiFundVo.getM_name());
                orderVo.setO_no(apiFundVo.getO_no());
                orderVo.setSymbol(apiFundVo.getSymbol());
                orderVo.setO_state(order.getO_status().equals(OrderStatusEnum.DONE.getCode()) ? "交易完成" : order.getO_status().equals(OrderStatusEnum.PC.getCode()) ? "部分撤销" : order.getO_status().equals(OrderStatusEnum.CANCELED.getCode()) ? "已撤销" : "未交易完成");
                orderVo.setO_type(order.getO_type().equals(OrderTypeEnum.SELL.getCode()) ? OrderTypeEnum.SELL.getCode() : OrderTypeEnum.BUY.getCode());
                orderVo.setDone_volume(order.getDone_volume());
                orderVo.setTotal_amount(order.getPrice().multiply(order.getDone_volume()).setScale(8, BigDecimal.ROUND_HALF_UP));
                orderVo.setQuote_currency(order.getQuote_currency());
                orderVo.setPrice(order.getPrice());
                orderVo.setCreate_time(order.getCreate_time());
                orderVo.setVolume(order.getVolume());
                if (!BeanUtil.isEmpty(order.getCancel_time())) {
                    orderVo.setComplet_time(order.getCancel_time());
                } else {
                    List<Trade> trades4Lock = exService.getTrades4Lock(HelpUtils.newHashMap("o_id", order.getId(), "member_id", order.getMember_id(), "table_name", apiFundVo.getSymbol().toLowerCase()));
                    if (!CollectionUtils.isEmpty(trades4Lock)) {
                        orderVo.setComplet_time(trades4Lock.get(0).getDone_time());
                    }
                }
                return new RespObj(Resp.SUCCESS, Resp.SUCCESS_MSG, orderVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new RespObj(Resp.FAIL, "系统繁忙", null);
        }
        return new RespObj(Resp.SUCCESS, "订单【" + apiFundVo.getO_no() + "】未交易完成", null);
    }

    /**
     * 获取订单
     * @param member
     * @param apiFundVo
     * @return
     */
    public Order getOrder(Member member,ApiFundVo apiFundVo){
        Order order = new Order();
        order.setO_no(apiFundVo.getO_no());
        order.setTable_name(apiFundVo.getSymbol().toLowerCase());
        order.setMember_id(member.getId());
        return exService.getOrder(order);
    }
    /**
     * 签名校验
     *
     * @param apiFundTransfer
     * @param c_name
     * @return
     */
    public RespObj commonTransfer(ApiFundTransfer apiFundTransfer, String c_name) {
        //校验项目方信息
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(c_name);
        if (respObj.getState() != 1) {
            return respObj;
        }
        thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        ApiToken apiToken = getApiToken(thirdPartyInfo);

        // 校验签名
        String validateRet = HelpUtils.preValidateBaseSecret(apiFundTransfer);
        if (!"".equals(validateRet)) {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new RespObj(Resp.FAIL, validateRet, null);
        }
        String sign = HelpUtils.createSign(HelpUtils.objToMap(apiFundTransfer), apiToken.getApi_secret());
        if (!sign.equals(apiFundTransfer.getSign())) {
            logger.warn("c_name:{},生成签名后：「{}」", c_name, sign);
            return new RespObj(Resp.FAIL, "签名错误", HelpUtils.getNowTimeStampInt());
        }
        Member member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", apiFundTransfer.getM_name()));
        if (BeanUtil.isEmpty(member)) {
            return new RespObj(Resp.FAIL, "该用户信息不存在", 0);
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
