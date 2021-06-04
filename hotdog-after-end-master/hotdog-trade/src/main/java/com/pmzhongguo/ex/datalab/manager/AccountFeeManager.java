package com.pmzhongguo.ex.datalab.manager;

import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.scheduler.UsdtCnyPriceScheduler;
import com.pmzhongguo.ex.business.service.MarketService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.MacMD5;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.datalab.contants.AccountFeeConstant;
import com.pmzhongguo.ex.datalab.contants.BaseModel;
import com.pmzhongguo.ex.datalab.contants.CurrencyAuthConstant;
import com.pmzhongguo.ex.datalab.entity.AccountFee;
import com.pmzhongguo.ex.datalab.entity.dto.AccountFeeDto;
import com.pmzhongguo.ex.datalab.entity.dto.CurrencyAuthDto;
import com.pmzhongguo.ex.datalab.entity.vo.AccountFeeWithDrawVo;
import com.pmzhongguo.ex.datalab.pojoAnnotation.AccountFeeChangeHandler;
import com.pmzhongguo.ex.datalab.service.AccountFeeService;
import com.pmzhongguo.ex.datalab.service.CurrencyAuthService;
import com.pmzhongguo.ex.datalab.service.PairFreeDetailService;
import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.qiniu.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/30 10:41 AM
 */
@Component
@Transactional
public class AccountFeeManager extends BaseModel implements IFaceManager {

    @Autowired private AccountFeeService accountFeeService;
    @Autowired private AccountFeeFrozenManager accountFeeFrozenManager;
    @Autowired private CurrencyAuthService currencyAuthService;

    @Override
    public ObjResp doSaveProcess(Map<String, Object> reqMapVo) throws Exception {
        return null;
    }

    @Override
    public ObjResp doUpdateProcess(Map<String, Object> reqMapVo) throws Exception {
        AccountFeeWithDrawVo accountFeeReq = JsonUtil.parseMap2Object(reqMapVo, AccountFeeWithDrawVo.class);
        ObjResp validate = accountFeeReq.validate();
        if (validate.getState().equals(Resp.FAIL)) {
            return validate;
        }
        // 判断用户短信验证码和资金密码
        Member oldMember = accountFeeReq.getMember();
        if (HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !accountFeeReq.getSendCode().equals(oldMember.getSms_code())) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg(), null);
        }
        if (!MacMD5.CalcMD5Member(accountFeeReq.getFundPassword() + "").equals(oldMember.getM_security_pwd())) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg(), null);
        }
        List<CurrencyAuthDto> currencyAuthMap = currencyAuthService.getCurrencyAuthMap(
                HelpUtils.newHashMap(CurrencyAuthConstant.memberId, oldMember.getId(), CurrencyAuthConstant.symbol, accountFeeReq.getSymbol(), "isFree", 0));
        if (CollectionUtils.isEmpty(currencyAuthMap)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.SYMBOL_FEE_ACCOUNT_NOT_AUTH.getErrorENMsg(), null);
        }
        CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(accountFeeReq.getSymbol());
        if (currencyPair.getBase_currency().equals(accountFeeReq.getCurrency())
                && accountFeeReq.getCurrencyNo().compareTo(currencyAuthMap.get(0).getBaseWQuota()) < 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_WITHDRAW_AMOUNT_MIN_THAN_MIN_TIP.getErrorENMsg(), null);
        }
        if (currencyPair.getQuote_currency().equals(accountFeeReq.getCurrency())
                && accountFeeReq.getCurrencyNo().compareTo(currencyAuthMap.get(0).getValuationWQuota()) < 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_WITHDRAW_AMOUNT_MIN_THAN_MIN_TIP.getErrorENMsg(), null);
        }
        AccountFee accountFeeChange = new AccountFee();
        accountFeeChange.setMemberId(oldMember.getId());
        accountFeeChange.setFeeCurrency(accountFeeReq.getCurrency());
        accountFeeChange.setTotalAmount(accountFeeReq.getCurrencyNo().abs().negate());
        return executeAccountFeeChange(accountFeeFrozenManager, accountFeeChange);
    }

    /**
     *  数据实验室用户所有手续费资产账户的资产变动必须得走这里
     * @param accountFeeChangeService
     * @param accountFeeChane
     */
    public ObjResp executeAccountFeeChange(AccountFeeChangeService accountFeeChangeService, AccountFee accountFeeChane) {
        String lockKey = AccountFeeConstant.ACCOUNT_FEE_UPDATE_LOCK_KEY + "—" + accountFeeChane.getFeeCurrency().toUpperCase();
        boolean isLock = JedisUtil.getInstance().getLock(lockKey, IPAddressPortUtil.IP_ADDRESS, AccountFeeConstant.ACCOUNT_FEE_UPDATE_LOCK_EXPIRE_TIME);
        if (!isLock) {
            logger.warn("分账手续费分布式锁获取失败，key：「{}」", lockKey);
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.GET_LOCK_FAIL.getErrorENMsg(), null);
        }
        try {
            List<AccountFee> accountFeeList =
                    accountFeeService.getAccountFeeByMemberId(
                            HelpUtils.newHashMap(CurrencyAuthConstant.memberId, accountFeeChane.getMemberId(),
                                    CurrencyAuthConstant.feeCurrency, accountFeeChane.getFeeCurrency()));
            AccountFee accountFeeDB = null;
            if (!CollectionUtils.isEmpty(accountFeeList)) {
                accountFeeDB = accountFeeList.get(0);
            }
            AccountFeeChangeHandler handler = new AccountFeeChangeHandler(accountFeeChangeService, accountFeeChane, accountFeeDB);
            handler.executeChange();
        } catch (Exception e) {
            e.printStackTrace();
            return new ObjResp(Resp.FAIL, e.getMessage(), null);
        } finally {
            boolean isRelease = JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
            if (!isRelease) {
                logger.warn("分账手续费分布式锁释放失败，key：「{}」", lockKey);
            }
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    @Override
    public Map<String, Object> getParamMap(Map<String, Object> reqMapVo) throws Exception {
        this.getPageReqMap(reqMapVo, "id", "desc");
        List<AccountFeeDto> accountFeeByPage = accountFeeService.getAccountFeeByPage(reqMapVo);
        return HelpUtils.newHashMap(CurrencyAuthConstant.web_Resp_Rows, accountFeeByPage, CurrencyAuthConstant.web_Resp_Total, reqMapVo.get(CurrencyAuthConstant.total));
    }

    public ObjResp getAccountFee(Map<String, Object> reqMapVo) throws Exception {
        if (BeanUtil.isEmpty(reqMapVo.get(CurrencyAuthConstant.symbol))) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_TRADE_PAIR.getErrorENMsg(), null);
        }
        CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(reqMapVo.get(CurrencyAuthConstant.symbol));
        if (BeanUtil.isEmpty(currencyPair)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_TRADE_PAIR.getErrorENMsg(), null);
        }
        reqMapVo.put(AccountFeeConstant.currencyPairStr,"('"+ currencyPair.getBase_currency() + "','" + currencyPair.getQuote_currency()+"')");
        List<AccountFeeDto> accountFeeList = accountFeeService.getAccountFeeByPage(reqMapVo);
        Map<String, AccountFeeDto> respMap = new HashMap<>();
        Map<String, TickerResp> ticker = MarketService.getTicker();
        if (!CollectionUtils.isEmpty(accountFeeList)) {
            for (AccountFeeDto accountFeeDto : accountFeeList) {
                if (HelpUtils.getOfficialCurrencyToUpper().equals(accountFeeDto.getFeeCurrency())) {
                    accountFeeDto.setAvailableFb(accountFeeDto.getAvailableBalance().multiply(UsdtCnyPriceScheduler.PRICE));
                } else {
                    BigDecimal close = getCurrentPairCloseForZC(accountFeeDto, ticker);
                    if (close == null || close.subtract(BigDecimal.ZERO).equals(BigDecimal.ZERO)) {
                        accountFeeDto.setAvailableFb(BigDecimal.ZERO);
                    }
                    close = close.multiply(UsdtCnyPriceScheduler.PRICE);
                    accountFeeDto.setAvailableFb(accountFeeDto.getAvailableBalance().multiply(close));
                }
                respMap.put(accountFeeDto.getFeeCurrency().toUpperCase(), accountFeeDto);
            }
        }
        // 如果没有的话，要造假数据给前端
        if (!respMap.containsKey(currencyPair.getBase_currency())) {
            Member member = (Member) reqMapVo.get(AccountFeeConstant.member);
            AccountFeeDto accountFeeDto = initAccountFeeForGetAccountFee(currencyPair.getBase_currency().toUpperCase(), member);
            respMap.put(currencyPair.getBase_currency().toUpperCase(),accountFeeDto);
        }
        if (!respMap.containsKey(currencyPair.getQuote_currency())) {
            Member member = (Member) reqMapVo.get(AccountFeeConstant.member);
            AccountFeeDto accountFeeDto = initAccountFeeForGetAccountFee(currencyPair.getQuote_currency().toUpperCase(), member);
            respMap.put(currencyPair.getQuote_currency().toUpperCase(),accountFeeDto);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, respMap);
    }

    /**
     * 获取手续费账号，如果没有的话，需要初始化返回前端
     * @param currency
     * @param member
     * @return
     */
    private AccountFeeDto  initAccountFeeForGetAccountFee(String currency,Member member) {
        AccountFeeDto accountFeeDto = new AccountFeeDto();
        accountFeeDto.setmName(member.getM_name());
        accountFeeDto.setMemberId(member.getId());
        accountFeeDto.setFeeCurrency(currency);
        accountFeeDto.setTotalAmount(BigDecimal.ZERO);
        accountFeeDto.setForzenAmount(BigDecimal.ZERO);
        accountFeeDto.setAvailableFb(BigDecimal.ZERO);
        accountFeeDto.setAvailableBalance(BigDecimal.ZERO);
        return accountFeeDto;
    }

    /**
     * 找到币的交易行情汇率
     *
     * @param accountFeeDto
     * @param ticker
     * @return
     */
    private BigDecimal getCurrentPairCloseForZC(AccountFeeDto accountFeeDto, Map<String, TickerResp> ticker) {
        TickerResp tickerResp = ticker.get(accountFeeDto.getFeeCurrency().toLowerCase()  + HelpUtils.getOfficialCurrencyToLower() + "_ticker");
        if (tickerResp != null) {
            return tickerResp.getClose();
        }
        tickerResp = ticker.get(accountFeeDto.getFeeCurrency().toLowerCase() + "eth_ticker");
        if (tickerResp != null) {
            TickerResp ethzc_ticker = ticker.get("eth"+ HelpUtils.getOfficialCurrencyToLower()+"_ticker");
            if (ethzc_ticker == null) {
                return null;
            }
            return ethzc_ticker.close.multiply(tickerResp.close).setScale(8, BigDecimal.ROUND_HALF_UP);
        }
        tickerResp = ticker.get(accountFeeDto.getFeeCurrency().toLowerCase() + "btc_ticker");
        if (tickerResp != null) {
            TickerResp usdtzc_ticker = ticker.get("btc"+ HelpUtils.getOfficialCurrencyToLower() + "_ticker");
            if (usdtzc_ticker == null) {
                return null;
            }

            return usdtzc_ticker.close.multiply(tickerResp.close).setScale(8, BigDecimal.ROUND_HALF_UP);
        }
        return null;
    }

}
