package com.pmzhongguo.ex.datalab.manager;

import com.pmzhongguo.ex.business.dto.AccountDto;
import com.pmzhongguo.ex.business.dto.ReturnCommiCurrAmountDto;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.business.service.TradeService;
import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.datalab.contants.BaseModel;
import com.pmzhongguo.ex.datalab.contants.CurrencyAuthConstant;
import com.pmzhongguo.ex.datalab.contants.PairFreeDetailConstant;
import com.pmzhongguo.ex.datalab.entity.AccountFee;
import com.pmzhongguo.ex.datalab.entity.CurrencyAuth;
import com.pmzhongguo.ex.datalab.entity.dto.CurrencyAuthDto;
import com.pmzhongguo.ex.datalab.entity.dto.DataLabDto;
import com.pmzhongguo.ex.datalab.entity.dto.TradeDto;
import com.pmzhongguo.ex.datalab.entity.vo.CurrencyAuthVo;
import com.pmzhongguo.ex.datalab.scheduler.SimpleTask;
import com.pmzhongguo.ex.datalab.service.CurrencyAuthService;
import com.pmzhongguo.ex.datalab.service.PairFreeDetailService;
import com.pmzhongguo.ex.datalab.utils.BinarySystemUtil;
import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.zzextool.utils.StringUtil;
import com.qiniu.util.BeanUtil;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jary
 * @creatTime 2019/11/30 10:48 AM
 */
@Component
public class CurrencyAuthManager extends BaseModel implements IFaceManager, SimpleTask {
    private Logger logger = LoggerFactory.getLogger(CurrencyAuthManager.class);
    private Map<String, DataLabDto> dataLabMap = new HashMap<>();

    @Autowired private CurrencyAuthService currencyAuthService;
    @Autowired private TradeService tradeService;
    @Autowired private MemberService memberService;
    @Autowired private PairFreeDetailManager pairFreeDetailManager;
    @Autowired private PairFreeDetailService pairFreeDetailService;

    @Override
    public ObjResp doSaveProcess(Map<String, Object> reqMapVo) throws Exception {
        CurrencyAuthVo currencyAuthVo = JsonUtil.parseMap2Object(reqMapVo, CurrencyAuthVo.class);
        ObjResp validate = currencyAuthVo.validate();
        if (validate.getState().equals(Resp.FAIL)) {
            return validate;
        }
        if (currencyAuthVo.getFeeScale().compareTo(new BigDecimal(CurrencyAuthConstant.feeScale)) > 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.SYMBOL_FEE_TOTAL_NOT_MORE_THAN_100.getErrorENMsg(), null);
        }
        String symbol = (currencyAuthVo.getBaseCurrencye() + currencyAuthVo.getValuationCurrency()).toUpperCase();
        CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol);
        if (BeanUtil.isEmpty(currencyPair)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null);
        }
        Member member = memberService.getMemberById(currencyAuthVo.getMemberId());
        if (BeanUtil.isEmpty(member)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg(), null);
        }
        List<CurrencyAuthDto> currencyAuthDtoList = currencyAuthService.getAllCurrencyAuth(HelpUtils.newHashMap(CurrencyAuthConstant.memberId, member.getId(), CurrencyAuthConstant.currencyPairId, currencyPair.getId()));
        if (!CollectionUtils.isEmpty(currencyAuthDtoList)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_USER_IS_EXIST.getErrorENMsg(), null);
        }

        currencyAuthDtoList = currencyAuthService.getAllCurrencyAuth(HelpUtils.newHashMap("notInId", currencyAuthVo.getId(),CurrencyAuthConstant.currencyPairId, currencyPair.getId()));
        if (!CollectionUtils.isEmpty(currencyAuthDtoList)) {
            BigDecimal feeTotal = BigDecimal.ZERO;
            for (CurrencyAuthDto cad : currencyAuthDtoList) {
                feeTotal = cad.getFeeScale().add(feeTotal);
            }
            if (feeTotal.add(currencyAuthVo.getFeeScale()).compareTo(new BigDecimal(CurrencyAuthConstant.feeScale)) > 0) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.SYMBOL_FEE_TOTAL_NOT_MORE_THAN_100.getErrorENMsg(), null);
            }
        }
        CurrencyAuth currencyAuth = new CurrencyAuth();
        currencyAuth.setMemberId(currencyAuthVo.getMemberId());
        currencyAuth.setCurrencyPairId(currencyPair.getId());
        currencyAuth.setIsFree(currencyAuthVo.getIsFree());
        currencyAuth.setBaseWQuota(currencyAuthVo.getBaseWQuota());
        currencyAuth.setValuationWQuota(currencyAuthVo.getValuationWQuota());
        currencyAuth.setmName(member.getM_name());
        currencyAuth.setFeeScale(currencyAuthVo.getFeeScale());
        //设置权限
        if (StringUtils.isNotEmpty(currencyAuthVo.getDataCollection())) {
            currencyAuth.setAuthority(getAuthority(currencyAuthVo.getDataCollection(), currencyAuth.getAuthority()));
        }
        if (StringUtils.isNotEmpty(currencyAuthVo.getDataLab())) {
            currencyAuth.setAuthority(getAuthority(currencyAuthVo.getDataLab(), currencyAuth.getAuthority()));
        }
        if (StringUtils.isNotEmpty(currencyAuthVo.getDataTrade())) {
            currencyAuth.setAuthority(getAuthority(currencyAuthVo.getDataTrade(), currencyAuth.getAuthority()));
        }
        if (StringUtils.isNotEmpty(currencyAuthVo.getDataWallet())) {
            currencyAuth.setAuthority(getAuthority(currencyAuthVo.getDataWallet(), currencyAuth.getAuthority()));
        }
        currencyAuth.setCreateTime(DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
        currencyAuth.setUpdateTime(currencyAuth.getCreateTime());
        currencyAuth.setRemark(currencyAuthVo.getRemark());
        currencyAuthService.save(currencyAuth);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    public ObjResp edit(Map<String, Object> reqMapVo) throws Exception {
        CurrencyAuthVo currencyAuthVo = JsonUtil.parseMap2Object(reqMapVo, CurrencyAuthVo.class);
        ObjResp validate = currencyAuthVo.validate();
        if (validate.getState().equals(Resp.FAIL)) {
            return validate;
        }
        List<CurrencyAuthDto> currencyAuthDtoList = currencyAuthService.getAllCurrencyAuth(HelpUtils.newHashMap("id", currencyAuthVo.getId()));
        if (!CollectionUtils.isEmpty(currencyAuthDtoList)) {
            CurrencyAuthDto currencyAuthDto = currencyAuthDtoList.get(0);
            if (!currencyAuthDto.getFeeScale().subtract(currencyAuthVo.getFeeScale()).equals(BigDecimal.ZERO)) {
                currencyAuthDtoList = currencyAuthService.getAllCurrencyAuth(HelpUtils.newHashMap("notInId", currencyAuthVo.getId(),CurrencyAuthConstant.currencyPairId, currencyAuthDto.getCurrencyPairId()));
                if (!CollectionUtils.isEmpty(currencyAuthDtoList)) {
                    BigDecimal feeTotal = BigDecimal.ZERO;
                    for (CurrencyAuthDto cad : currencyAuthDtoList) {
                        feeTotal = cad.getFeeScale().add(feeTotal);
                    }
                    if (feeTotal.add(currencyAuthVo.getFeeScale()).compareTo(new BigDecimal(CurrencyAuthConstant.feeScale)) > 0) {
                        return new ObjResp(Resp.FAIL, ErrorInfoEnum.SYMBOL_FEE_TOTAL_NOT_MORE_THAN_100.getErrorENMsg(), null);
                    }
                }
                currencyAuthDto.setFeeScale(currencyAuthVo.getFeeScale());
            }
            if (!currencyAuthDto.getIsFree().equals(currencyAuthVo.getIsFree())) {
                currencyAuthDto.setIsFree(currencyAuthVo.getIsFree());
            }
            currencyAuthDto.setRemark(StringUtil.isNullOrBank(currencyAuthVo.getRemark()) ? "" : currencyAuthVo.getRemark());
            if (!currencyAuthDto.getBaseWQuota().subtract(currencyAuthVo.getBaseWQuota()).equals(BigDecimal.ZERO)) {
                currencyAuthDto.setBaseWQuota(currencyAuthVo.getBaseWQuota());
            }
            if (!currencyAuthDto.getValuationWQuota().subtract(currencyAuthVo.getValuationWQuota()).equals(BigDecimal.ZERO)) {
                currencyAuthDto.setValuationWQuota(currencyAuthVo.getValuationWQuota());
            }
            //设置权限
            if (StringUtils.isNotEmpty(currencyAuthVo.getDataCollection())) {
                currencyAuthVo.setAuthority(getAuthority(currencyAuthVo.getDataCollection(), currencyAuthVo.getAuthority()));
            }
            if (StringUtils.isNotEmpty(currencyAuthVo.getDataLab())) {
                currencyAuthVo.setAuthority(getAuthority(currencyAuthVo.getDataLab(), currencyAuthVo.getAuthority()));
            }
            if (StringUtils.isNotEmpty(currencyAuthVo.getDataTrade())) {
                currencyAuthVo.setAuthority(getAuthority(currencyAuthVo.getDataTrade(), currencyAuthVo.getAuthority()));
            }
            if (StringUtils.isNotEmpty(currencyAuthVo.getDataWallet())) {
                currencyAuthVo.setAuthority(getAuthority(currencyAuthVo.getDataWallet(), currencyAuthVo.getAuthority()));
            }
            if (currencyAuthDto.getAuthority() != currencyAuthVo.getAuthority()) {
                currencyAuthDto.setAuthority(currencyAuthVo.getAuthority());
            }
            CurrencyAuth currencyAuth = dao2bean(currencyAuthDto);
            currencyAuthService.update(currencyAuth);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    private CurrencyAuth dao2bean(CurrencyAuthDto currencyAuthDto){
        CurrencyAuth currencyAuth= new CurrencyAuth();
        currencyAuth.setId(currencyAuthDto.getId());
        currencyAuth.setMemberId(currencyAuthDto.getMemberId());
        currencyAuth.setCurrencyPairId(currencyAuthDto.getCurrencyPairId());
        currencyAuth.setmName(currencyAuthDto.getmName());
        currencyAuth.setFeeScale(currencyAuthDto.getFeeScale());
        currencyAuth.setAuthority(currencyAuthDto.getAuthority());
        currencyAuth.setBaseWQuota(currencyAuthDto.getBaseWQuota());
        currencyAuth.setValuationWQuota(currencyAuthDto.getValuationWQuota());
        currencyAuth.setIsFree(currencyAuthDto.getIsFree());
        currencyAuth.setRemark(currencyAuthDto.getRemark());
        currencyAuth.setCreateTime(currencyAuthDto.getCreateTime());
        currencyAuth.setUpdateTime(currencyAuthDto.getUpdateTime());
        currencyAuth.setExtend1(currencyAuthDto.getExtend1());
        currencyAuth.setExtend2(currencyAuthDto.getExtend2());
        return currencyAuth;
    }
    private Integer getAuthority(String binary, Integer authorityNo) {
        if (authorityNo == null || authorityNo < 0) {
            authorityNo = 0;
        }
        return BinarySystemUtil.binaryToDecimal(binary) + authorityNo;
    }

    @Override
    public ObjResp doUpdateProcess(Map<String, Object> reqMapVo) throws Exception {
        return null;
    }

    @Override
    public Map<String, Object> getParamMap(Map<String, Object> reqMapVo) throws Exception {
        List<CurrencyAuthDto> allCurrencyAuth = currencyAuthService.getAllCurrencyAuth(reqMapVo);
        return HelpUtils.newHashMap(CurrencyAuthConstant.web_Resp_Rows, allCurrencyAuth, CurrencyAuthConstant.web_Resp_Total, reqMapVo.get(CurrencyAuthConstant.total));
    }

    public CurrencyAuthDto getCurrencyAuthDtoById(Map<String, Object> reqMapVo) {
        if (!BeanUtil.isEmpty(reqMapVo.get("id"))) {
            List<CurrencyAuthDto> allCurrencyAuth = currencyAuthService.getAllCurrencyAuth(reqMapVo);
            return allCurrencyAuth.get(0);
        }
        return null;
    }

    public ObjResp getCurrencyAuthMap(Map<String, Object> reqMapVo) {
        List<CurrencyAuthDto> currencyAuthMap = currencyAuthService.getCurrencyAuthMap(reqMapVo);
        if (CollectionUtils.isEmpty(currencyAuthMap)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.SYMBOL_FEE_ACCOUNT_NOT_AUTH.getErrorENMsg(), null);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, currencyAuthMap);
    }

    /**
     * 获得实验室基础数据
     *
     * @param reqMap
     * @return
     */
    public ObjResp getDataLabFirst(Map<String, Object> reqMap) throws Exception {
        String symbol = reqMap.get(CurrencyAuthConstant.symbol) + "";
        ObjResp objResp = this.validateSymbol(symbol);
        if (objResp.getState().equals(Resp.FAIL)){
            return objResp;
        }
        if (BeanUtil.isEmpty(dataLabMap.get(symbol))) {
            executeDataLab(HelpUtils.newHashMap(CurrencyAuthConstant.symbol, symbol));
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, dataLabMap.get(symbol));
    }

    public ObjResp getPositionAcountList(Map<String, Object> reqMap) {
        String symbol = reqMap.get(CurrencyAuthConstant.symbol) + "";
        ObjResp objResp = this.validateSymbol(symbol);
        if (objResp.getState().equals(Resp.FAIL)) {
            return objResp;
        }
        CurrencyPair currencyPair = (CurrencyPair) objResp.getData();
        reqMap.put(CurrencyAuthConstant.currency, currencyPair.getBase_currency().toUpperCase());
        this.getPageReqMap(reqMap,"ma.id","desc");
        List<AccountDto> accountDtos = memberService.listAccountsByPage(reqMap);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap(CurrencyAuthConstant.web_Resp_Rows, accountDtos, CurrencyAuthConstant.web_Resp_Total, reqMap.get(CurrencyAuthConstant.total)));
    }



    @Override
    public void execute() {
        try {
//            System.out.println("******开始执行：" + System.currentTimeMillis() / 1000);
            executeDataLab(null);
        } catch (Exception e) {
            logger.warn("定时任务获取数据实验室基础数据异常。");
            e.printStackTrace();
        }
    }
    private static final int DATALAB_LOCK_EXPIRE_TIME = 60 * 1000 * 60;
    private static final String DATALAB_LOCK = "DATALAB_LOCK";

    /**
     *执行手续费分账
     */
    public void executeSeparateAccountTask() {
        try {
            boolean isLock = JedisUtil.getInstance().getLock(DATALAB_LOCK, IPAddressPortUtil.IP_ADDRESS, DATALAB_LOCK_EXPIRE_TIME);
            if (isLock) {
                long yesterday = HelpUtils.getAfterSomeDayLongTime(-1);
                String searchDate = HelpUtils.TimeStamp2StringDate(yesterday, DateStyleEnum.YYYY_MM_DD.getValue());
                executeSeparateAccountTask(HelpUtils.newHashMap(PairFreeDetailConstant.searchDate, searchDate));
            } else {
                Object o = JedisUtil.getInstance().get(DATALAB_LOCK, true);
                logger.warn("执行手续费分账定时任务时获取分布式锁失败，执行任务主机是：{}。", o);
            }
        } catch (Exception e) {
            logger.warn("定时任务执行手续费分账异常。");
            e.printStackTrace();
        }
    }

    /**
     * 拿到锁后，执行手续费分账
     * @param reqMap        要查找数据的日期
     * @throws Exception
     */
    private void executeSeparateAccountTask(Map<String, Object> reqMap) throws Exception {
        // 找到哪些币支持分账
        List<String> symbolList = currencyAuthService.getAllSymbol(reqMap);
        for (String symbol : symbolList) {
            ObjResp objResp = this.validateSymbol(symbol);
            if (objResp.getState().equals(Resp.FAIL)) {
                logger.warn("[{}]生成交易手续费时交易对异常：{}", symbol, objResp.getData());
                continue;
            }
            Object searchDate = reqMap.get(PairFreeDetailConstant.searchDate);
            if (BeanUtil.isEmpty(searchDate)) {
                logger.warn("{}生成交易手续费时交易对异常,【原因】：{}", symbol, "统计时间为空");
                continue;
            }
            List pairFreeDetailList = pairFreeDetailService
                    .getPairFreeDetailList(HelpUtils.newHashMap(PairFreeDetailConstant.searchDate, searchDate
                            , PairFreeDetailConstant.currencyPair, symbol.toUpperCase()));
            if (!CollectionUtils.isEmpty(pairFreeDetailList)) {
                logger.warn("{}生成交易手续费时交易对异常,【原因】：{}", symbol, searchDate + "已做过统计");
                continue;
            }
            searchDate = searchDate + "%";
            reqMap.put(CurrencyAuthConstant.symbol, symbol.toUpperCase());
            reqMap.put(CurrencyAuthConstant.table_name, symbol.toLowerCase());
            reqMap.put(CurrencyAuthConstant.createTime, searchDate);
            // 从交易表中找到手续费记录
            List<ReturnCommiCurrAmountDto> tradeFeeList = getTradeFeeList(reqMap);
            if (!CollectionUtils.isEmpty(tradeFeeList)) {
                pairFreeDetailManager.addTradeFee(reqMap, tradeFeeList);
            } else {
                logger.warn("{}生成交易手续费时交易对异常,【原因】：{}", symbol, searchDate + "没有交易记录。");
            }
        }
    }

    private List<ReturnCommiCurrAmountDto> getTradeFeeList(Map<String, Object> param) {
        return currencyAuthService.getTradeFeeList(param);
    }

    public void executeDataLab(Map<String, Object> reqMap) throws Exception{
        List<String> symbolList = currencyAuthService.getAllSymbol(reqMap);
        Properties properties = PropertiesLoaderUtils.loadAllProperties("../threadPool.properties");
        Integer fee_list_pool_num = Integer.valueOf(properties.getProperty("pair_auth_pool_num"));
        ExecutorService fee_list_pool = Executors.newFixedThreadPool(fee_list_pool_num, new ZzexThreadFactory("pair_auth_pool"));
        Map<String, CurrencyPair> currencyPairMap = HelpUtils.getCurrencyPairMap();
        for (String symbol : symbolList) {
            fee_list_pool.execute(new PairAuth(symbol, currencyPairMap));
        }
    }
    class PairAuth implements Runnable {
        private String symbol;
        private Map<String, CurrencyPair> currencyPairMap;

        public PairAuth(String symbol, Map<String, CurrencyPair> currencyPairMap) {
            this.symbol = symbol;
            this.currencyPairMap = currencyPairMap;
        }

        @Override
        public void run() {
            CurrencyPair currencyPair = currencyPairMap.get(symbol);
            if (BeanUtil.isEmpty(currencyPair)) {
                logger.warn("交易对「{}」权限不存在", symbol);
                return;
            }
            DataLabDto dataLabDcto = new DataLabDto();
            Map<String, Object> etcTotal = memberService.getEtcTotal(currencyPair.getBase_currency());
            dataLabDcto.setUidList(Integer.valueOf(etcTotal.get("idTotal")+""));
            dataLabDcto.setCurrencyFlow(new BigDecimal((etcTotal.get("balanceTotal")+"")).stripTrailingZeros());
            dataLabDcto.setRechargeNo(getTotalRecharge(currencyPair) == null ? new BigDecimal("0") : getTotalRecharge(currencyPair).setScale(4, BigDecimal.ROUND_HALF_UP));
            dataLabDcto.setWithdrawNo(getTotalWithdrawal(currencyPair) == null ? new BigDecimal("0") : getTotalWithdrawal(currencyPair).setScale(4, BigDecimal.ROUND_HALF_UP));
            dataLabMap.put(currencyPair.getKey_name().toUpperCase(), dataLabDcto);
        }
    }



    private BigDecimal getPlatformFlows(CurrencyPair currencyPair) {
        return tradeService.getPlatformFlows(currencyPair);
    }

    private BigDecimal getTotalRecharge(CurrencyPair currencyPair) {
        return memberService.getTotalRecharge(HelpUtils.newHashMap(CurrencyAuthConstant.currency, currencyPair.getBase_currency(), CurrencyAuthConstant.createTime, DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD)+"%"));
    }

    private BigDecimal getTotalWithdrawal(CurrencyPair currencyPair) {
        return memberService.getTotalWithdrawal(HelpUtils.newHashMap(CurrencyAuthConstant.currency, currencyPair.getBase_currency(), CurrencyAuthConstant.createTime, DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD)+"%"));

    }

    public ObjResp getTradeListByPage(Map<String, Object> reqMap) {
        String symbol = reqMap.get(CurrencyAuthConstant.symbol) + "";
        ObjResp objResp = this.validateSymbol(symbol);
        if (objResp.getState().equals(Resp.FAIL)) {
            return objResp;
        }
        CurrencyPair currencyPair = (CurrencyPair) objResp.getData();
        reqMap.put(CurrencyAuthConstant.table_name, symbol.toLowerCase());
        reqMap.put(CurrencyAuthConstant.baseCurrency, currencyPair.getBase_currency());
        reqMap.put(CurrencyAuthConstant.quoteCurrency, currencyPair.getQuote_currency());
        this.getPageReqMap(reqMap,"t.done_time","desc");
        List<TradeDto> tradeList = currencyAuthService.getTradeListByPage(reqMap);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap(CurrencyAuthConstant.web_Resp_Rows, tradeList, CurrencyAuthConstant.web_Resp_Total, reqMap.get(CurrencyAuthConstant.total)));
    }


}
