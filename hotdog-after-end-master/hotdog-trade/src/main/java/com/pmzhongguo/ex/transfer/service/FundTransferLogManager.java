/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/18 All Rights Reserved.
 */
package com.pmzhongguo.ex.transfer.service;

import com.alibaba.fastjson.JSON;
import com.pmzhongguo.ex.business.dto.WithdrawCreateDtoInner;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ex.core.web.RechargeTypeEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyFlow;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.ex.transfer.enums.ThirdPartyFlowEnum;
import com.pmzhongguo.ex.transfer.mapper.ThirdPartyFlowMapper;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.transfer.entity.FundOperationLog;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.qiniu.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/18 14:08
 * @description：资金划转操作日志
 * @version: $
 */
@Service
@Transactional
public class FundTransferLogManager extends BaseServiceSupport {

    @Autowired
    private MemberService memberService;

    @Autowired
    private FundOperationLogService fundOperationLogService;

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    AccountManager accountManager;

    @Autowired
    ThirdPartyFlowMapper thirdPartyFlowMapper;
    /**
     * 划转操作日志记录
     *
     * @param apiFundTransfer
     * @param member
     * @return
     */
    public Resp addFundTransferLog(ApiFundTransfer apiFundTransfer, Member member, String tableName, String ipAddr, OptSourceEnum optSourceEnum) {
        if (StringUtils.isEmpty(tableName)) {
            return new Resp(Resp.FAIL, "项目方信息不存在");
        }
        // 判断币种是否支持
        String currencyKey = apiFundTransfer.getCurrency().toUpperCase();
        if (StringUtils.isBlank(currencyKey)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg());
        }
        Map<String, Currency> currencyMap = HelpUtils.getCurrencyMap();
        if (!currencyMap.containsKey(currencyKey) && "gstt".equalsIgnoreCase(currencyKey)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg());
        }
        Currency currency = currencyMap.get(currencyKey);
        String createDate = HelpUtils.formatDate8(new Date());
        Long id = -1L;
        String coin_addr = "";
        if (StringUtils.isEmpty(apiFundTransfer.getAddr())) {
            coin_addr = "0x00000000000000000000000000000000";
        } else {
            coin_addr = apiFundTransfer.getAddr();
        }
        CoinWithdraw coinWithdraw = new CoinWithdraw();
        if ("withdraw".equals(apiFundTransfer.getTransferType())) {
            coinWithdraw.setCurrency(currency.getCurrency());
            coinWithdraw.setCurrency_id(currency.getId());
            coinWithdraw.setMember_coin_addr(coin_addr);
            coinWithdraw.setMember_coin_addr_label("-");
            coinWithdraw.setMember_coin_addr_id(0);

            coinWithdraw.setMember_id(member.getId());
            coinWithdraw.setOper_ip(ipAddr);
            coinWithdraw.setW_amount(apiFundTransfer.getTransferNum().abs());
            coinWithdraw.setW_create_time(HelpUtils.formatDate8(new Date()));
            // 手续费，取系统设置的手续费
            // 划转暂时不收手续费
            if (false) {
                handlerFee(coinWithdraw, apiFundTransfer);
            } else {
                coinWithdraw.setW_fee(BigDecimal.ZERO);
            }
            if (coinWithdraw.getW_fee().compareTo(coinWithdraw.getW_amount()) >= 0) {
                throw new BusinessException(-1, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg());
            }
            coinWithdraw.setW_txid(apiFundTransfer.getTradeID());
            coinWithdraw.setW_status(1);
            coinWithdraw.setOtc_ads_id(0);
            coinWithdraw.setOtc_oppsite_currency("");
            coinWithdraw.setOtc_owner_name(tableName);
            coinWithdraw.setOtc_price(BigDecimal.ZERO);
            coinWithdraw.setOtc_volume(BigDecimal.ZERO);
            memberService.addCoinWithdraw(coinWithdraw);
            id = coinWithdraw.getId();
        } else if ("deposit".equals(apiFundTransfer.getTransferType())) {
            // 更新充值记录表
            CoinRecharge coinRecharge = new CoinRecharge();
            coinRecharge.setMember_id(member.getId());
            coinRecharge.setR_guiji(1);  //不归集
            coinRecharge.setCurrency_id(currency.getId());
            coinRecharge.setCurrency(currencyKey);
            coinRecharge.setR_amount(apiFundTransfer.getTransferNum().abs());
            coinRecharge.setR_create_time(createDate);
            if (currencyKey.equalsIgnoreCase("USDT")) {
                coinRecharge.setR_address(RechargeTypeEnum.TRANSFER.getType());
            } else {
                coinRecharge.setR_address(coin_addr);
            }
            coinRecharge.setR_txid(apiFundTransfer.getTradeID());
            coinRecharge.setR_confirmations("0");
            coinRecharge.setR_status(1);
            coinRecharge.setR_gas(0);
            coinRecharge.setR_guiji(0);
            coinRecharge.setR_source(tableName + "划转");
            memberService.addCoinRechargeByMember(coinRecharge);
            id = coinRecharge.getId();
        } else if ("release".equals(apiFundTransfer.getTransferType())) {
        } else {
            return new Resp(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg());
        }
        RespObj respObj = thirdPartyService.getThirdPartyInfoRespObj(tableName);
        ThirdPartyInfo  thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
        // 更新账户资产表
        if (tableName.equalsIgnoreCase(OptSourceEnum.NRS.getCode()) && "deposit".equals(apiFundTransfer.getTransferType())) {
            //有项目方账号的项目方，先冻结项目方资产，再新增用户资产，最后解冻项目方资产
            memberService.accountProc(apiFundTransfer.getTransferNum(), apiFundTransfer.getCurrency().toUpperCase(),Integer.valueOf(thirdPartyInfo.getExt()) , 1, optSourceEnum);
            memberService.accountProc(apiFundTransfer.getTransferNum(), apiFundTransfer.getCurrency().toUpperCase(), member.getId(), 3, optSourceEnum);
            memberService.accountProc(apiFundTransfer.getTransferNum(), apiFundTransfer.getCurrency().toUpperCase(), Integer.valueOf(thirdPartyInfo.getExt()), 2, optSourceEnum);
        } else {
            memberService.accountProc(apiFundTransfer.getTransferNum(), apiFundTransfer.getCurrency().toUpperCase(), member.getId(), 3, optSourceEnum);
            if (tableName.equalsIgnoreCase(OptSourceEnum.NRS.getCode())
                    && "withdraw".equals(apiFundTransfer.getTransferType()) && !member.getId().equals(Integer.valueOf(thirdPartyInfo.getExt()))) {
                memberService.accountProc(apiFundTransfer.getTransferNum().abs(), apiFundTransfer.getCurrency().toUpperCase(), Integer.valueOf(thirdPartyInfo.getExt()), 3, optSourceEnum);
            }
        }
        if (tableName.equalsIgnoreCase(OptSourceEnum.NRS.getCode())
                && !member.getId().equals(Integer.valueOf(thirdPartyInfo.getExt()))) {
            updateCnameAccountLog(Integer.valueOf(thirdPartyInfo.getExt()), id, apiFundTransfer, tableName, ipAddr, currency);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,id);
    }


    /**
     * 项目方账户充提币记录
     * @param proId 项目方账户id
     * @param userFlowId  用户流水id
     * @param apiFundTransfer
     * @param tableName
     * @param ipAddr
     * @param currency
     */
    public void updateCnameAccountLog(Integer proId, Long userFlowId, ApiFundTransfer apiFundTransfer, String tableName, String ipAddr, Currency currency) {
        String createDate = HelpUtils.formatDate8(new Date());
        Long proFlowId = -1L;
        ThirdPartyFlow thirdPartyFlow = null;
        if ("withdraw".equals(apiFundTransfer.getTransferType())) {
            //用户提现，项目方回写
            CoinRecharge coinRecharge = new CoinRecharge();
            coinRecharge.setMember_id(proId);
            coinRecharge.setCurrency_id(currency.getId());
            coinRecharge.setCurrency(currency.getCurrency());
            coinRecharge.setR_amount(apiFundTransfer.getTransferNum().abs());
            coinRecharge.setR_create_time(createDate);
            coinRecharge.setR_address(RechargeTypeEnum.TRANSFER.getType());
            coinRecharge.setR_txid(apiFundTransfer.getTradeID());
            coinRecharge.setR_confirmations("0");
            coinRecharge.setR_guiji(1);  //不归集
            coinRecharge.setR_status(1);
            coinRecharge.setR_gas(0);
            coinRecharge.setR_guiji(0);
            coinRecharge.setR_source(tableName + "划转");
            memberService.addCoinRechargeByMember(coinRecharge);
            proFlowId = coinRecharge.getId();
            thirdPartyFlow = new ThirdPartyFlow(ThirdPartyFlowEnum.PRO_WRITE_BACK.getType(), proFlowId, userFlowId, createDate, ThirdPartyFlowEnum.PRO_WRITE_BACK.getCode());
        } else if ("deposit".equals(apiFundTransfer.getTransferType())) {
            //用户充值，项目方扣款
            CoinWithdraw coinWithdraw = new CoinWithdraw();
            coinWithdraw.setCurrency(currency.getCurrency());
            coinWithdraw.setCurrency_id(currency.getId());
            coinWithdraw.setMember_coin_addr(RechargeTypeEnum.TRANSFER.getType());
            coinWithdraw.setMember_coin_addr_label("-");
            coinWithdraw.setMember_coin_addr_id(0);
            coinWithdraw.setMember_id(proId);
            coinWithdraw.setOper_ip(ipAddr);
            coinWithdraw.setW_amount(apiFundTransfer.getTransferNum().abs());
            coinWithdraw.setW_create_time(HelpUtils.formatDate8(new Date()));
            // 手续费，取系统设置的手续费
            // 划转暂时不收手续费
            if (false) {
                handlerFee(coinWithdraw, apiFundTransfer);
            } else {
                coinWithdraw.setW_fee(BigDecimal.ZERO);
            }
            if (coinWithdraw.getW_fee().compareTo(coinWithdraw.getW_amount()) >= 0) {
                throw new BusinessException(-1, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg());
            }
            coinWithdraw.setW_txid(apiFundTransfer.getTradeID());
            coinWithdraw.setW_status(1);
            coinWithdraw.setOtc_ads_id(0);
            coinWithdraw.setOtc_oppsite_currency("");
            coinWithdraw.setOtc_owner_name(tableName);
            coinWithdraw.setOtc_price(BigDecimal.ZERO);
            coinWithdraw.setOtc_volume(BigDecimal.ZERO);
            memberService.addCoinWithdraw(coinWithdraw);
            proFlowId = coinWithdraw.getId();
            thirdPartyFlow = new ThirdPartyFlow(ThirdPartyFlowEnum.PRO_DEDUCTION.getType(), proFlowId, userFlowId, createDate, ThirdPartyFlowEnum.PRO_DEDUCTION.getCode());
        }
        if (!BeanUtil.isEmpty(thirdPartyFlow)) {
            thirdPartyFlowMapper.insert(thirdPartyFlow);
        }
    }

    /**
     * @param member
     * @param apiFundTransfer
     * @param tableName
     * @return
     */
    public FundOperationLog createFundOperationLog(Member member, ApiFundTransfer apiFundTransfer, String tableName) {
        FundOperationLog fundOperationLog = new FundOperationLog();
        fundOperationLog.setMemberId(member.getId());
        fundOperationLog.setmName(member.getM_name());
        fundOperationLog.setTradeId(apiFundTransfer.getTradeID());
        fundOperationLog.setCurrency(apiFundTransfer.getCurrency());
        fundOperationLog.setTransferTime(HelpUtils.formatDate8(new Date()));
        BigDecimal frozenToBalance = daoUtil.queryForObject("SELECT total_balance - frozen_balance FROM m_account WHERE member_id = ? AND currency = ?", BigDecimal.class, member.getId(), apiFundTransfer.getCurrency().toUpperCase());
        fundOperationLog.setCurrentNum(frozenToBalance == null ? BigDecimal.ZERO : frozenToBalance);
        fundOperationLog.setTransferNum(apiFundTransfer.getTransferNum().abs());
        fundOperationLog.setTransferStatus(Resp.SUCCESS_MSG);
        fundOperationLog.setBody(JSON.toJSONString(apiFundTransfer));
        fundOperationLog.setTableName(tableName);
        fundOperationLog.setTransferType(apiFundTransfer.getTransferType());
        return fundOperationLog;
    }
    /**
     *  第三方通道提币异常日志
     * @param member
     * @param withdrawCreateDto
     * @param tableName
     * @return
     */
    public FundOperationLog createFundOperationLog(Member member, WithdrawCreateDtoInner withdrawCreateDto, String tableName, String errMsg) {
        FundOperationLog fundOperationLog = new FundOperationLog();
        fundOperationLog.setMemberId(member.getId());
        fundOperationLog.setmName(member.getM_name());
        fundOperationLog.setCurrency(withdrawCreateDto.getCurrency());
        fundOperationLog.setTransferTime(HelpUtils.formatDate8(new Date()));
        BigDecimal frozenToBalance = daoUtil.queryForObject("SELECT total_balance - frozen_balance FROM m_account WHERE member_id = ? AND currency = ?", BigDecimal.class, member.getId(), withdrawCreateDto.getCurrency().toUpperCase());
        fundOperationLog.setCurrentNum(frozenToBalance == null ? BigDecimal.ZERO : frozenToBalance);
        fundOperationLog.setTransferNum(withdrawCreateDto.getAmount().abs());
        fundOperationLog.setTransferStatus("fail");
        fundOperationLog.setBody(JSON.toJSONString(withdrawCreateDto));
        fundOperationLog.setTableName(tableName);
        fundOperationLog.setRemark(errMsg);
        fundOperationLog.setTransferType("withdraw");
        return fundOperationLog;
    }
    /**
     * 手续费处理
     *
     * @param coinWithdraw
     * @param apiFundTransfer
     */
    private void handlerFee(CoinWithdraw coinWithdraw, ApiFundTransfer apiFundTransfer) {
        Currency tmpCurrency = HelpUtils.getCurrencyMap().get(coinWithdraw.getCurrency());
        // 按百分比收取手续费
        if (tmpCurrency.getWithdraw_fee_percent() == 1) {
            BigDecimal fee = apiFundTransfer.getTransferNum().abs().multiply(tmpCurrency.getWithdraw_fee())
                    .divide(BigDecimal.valueOf(100));
            // 小于最小手续费，则按最小手续费计算
            if (fee.compareTo(tmpCurrency.getWithdraw_fee_min()) < 0) {
                fee = tmpCurrency.getWithdraw_fee_min();
            }
            // 大于最大手续费，则按最大的收取
            if (tmpCurrency.getWithdraw_fee_max().compareTo(BigDecimal.ZERO) > 0
                    && fee.compareTo(tmpCurrency.getWithdraw_fee_max()) > 0) {
                fee = tmpCurrency.getWithdraw_fee_max();
            }
            fee = fee.setScale(tmpCurrency.getC_precision(), BigDecimal.ROUND_DOWN);

            coinWithdraw.setW_fee(fee);
        } else {
            // 收取固定手续费
            coinWithdraw.setW_fee(tmpCurrency.getWithdraw_fee());
        }
    }


    public List<FundOperationLog> getFundOperationLogList(Map param) {
        return fundOperationLogService.getFundOperationLogList(param);
    }
    public List<FundOperationLog> getFundOperationLogListByPage(Map param) {
        return fundOperationLogService.getFundOperationLogListByPage(param);
    }
    /**
     * 将英文转换为中文
     * @param errorMsg 如果是中文，直接返回
     * @return
     */
    public String errorTip(String errorMsg) {
        String msg = HelpUtils.isChinese(errorMsg) ? errorMsg : ErrorInfoEnum.errorCNMsgOf(errorMsg);
        // 如果是未定义的异常，返回默认的中文提示
        return HelpUtils.nullOrBlank(msg) ? "系统异常，请联系管理员" : msg;
    }
}
