package com.pmzhongguo.ex.datalab.manager;

import com.pmzhongguo.ex.business.dto.ReturnCommiCurrAmountDto;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.datalab.contants.BaseModel;
import com.pmzhongguo.ex.datalab.contants.CurrencyAuthConstant;
import com.pmzhongguo.ex.datalab.contants.PairFreeDetailConstant;
import com.pmzhongguo.ex.datalab.entity.AccountFee;
import com.pmzhongguo.ex.datalab.entity.PairFreeDetail;
import com.pmzhongguo.ex.datalab.entity.dto.CurrencyAuthDto;
import com.pmzhongguo.ex.datalab.entity.dto.PairFreeDetailDto;
import com.pmzhongguo.ex.datalab.service.AccountFeeService;
import com.pmzhongguo.ex.datalab.service.CurrencyAuthService;
import com.pmzhongguo.ex.datalab.service.PairFreeDetailService;
import com.pmzhongguo.otc.utils.JsonUtil;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/30 10:49 AM
 */
@Component
public class PairFreeDetailManager extends BaseModel implements IFaceManager {
    private Logger logger = LoggerFactory.getLogger(PairFreeDetailManager.class);

    @Autowired private CurrencyAuthService currencyAuthService;
    @Autowired private AccountFeeService accountFeeService;
    @Autowired private PairFreeDetailService pairFreeDetailService;
    @Autowired private AccountFeeIncreaseManager accountFeeIncreaseManager;
    @Autowired private AccountFeeManager accountFeeManager;

    @Override
    public ObjResp doSaveProcess(Map<String, Object> reqMapVo) throws Exception {
        return null;
    }

    @Override
    public ObjResp doUpdateProcess(Map<String, Object> reqMapVo) throws Exception {
        return null;
    }

    @Override
    public Map<String, Object> getParamMap(Map<String, Object> reqMapVo) throws Exception {
        this.getPageReqMap(reqMapVo, "id", "desc");
        List<PairFreeDetailDto> pairFreeDetailList = pairFreeDetailService.getPairFreeDetailList(reqMapVo);
        return HelpUtils.newHashMap(CurrencyAuthConstant.web_Resp_Rows, pairFreeDetailList, CurrencyAuthConstant.web_Resp_Total, reqMapVo.get(CurrencyAuthConstant.total));
    }

    public ObjResp getPairFreeDetailListByPage(Map<String, Object> reqMap) {
        String symbol = reqMap.get(CurrencyAuthConstant.symbol) + "";
        ObjResp objResp = this.validateSymbol(symbol);
        if (objResp.getState().equals(Resp.FAIL)) {
            return objResp;
        }
        this.getPageReqMap(reqMap, "create_time", "desc");
        reqMap.put(PairFreeDetailConstant.currencyPair,symbol);
        List pairFreeDetailList = pairFreeDetailService.getPairFreeDetailList(reqMap);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap(CurrencyAuthConstant.web_Resp_Rows, pairFreeDetailList, CurrencyAuthConstant.web_Resp_Total, reqMap.get(CurrencyAuthConstant.total)));
    }

    /**
     * 添加交易费用
     * @param reqMapVo  查询参数
     * @param rccad     要返佣的交易手续费记录
     */
    public void addTradeFee(Map<String, Object> reqMapVo, List<ReturnCommiCurrAmountDto> rccad) {
        List<CurrencyAuthDto> currencyAuthMap = currencyAuthService.getCurrencyAuthMap(reqMapVo);
        for (CurrencyAuthDto currencyAuthDto : currencyAuthMap) {
            try {
                addTradeFee(currencyAuthDto, rccad);
            } catch (Exception e) {
                logger.warn("手续费资产统计失败，详细信息：{}", JsonUtil.bean2JsonString(currencyAuthDto));
                e.printStackTrace();
            }
        }

    }

    /**
     * 处理具体的交易费用
     * @param currencyAuthDto
     * @param rccad
     */
    private void addTradeFee(CurrencyAuthDto currencyAuthDto, List<ReturnCommiCurrAmountDto> rccad) {
        Iterator<ReturnCommiCurrAmountDto> iterator = rccad.iterator();
        while (iterator.hasNext()) {
            ReturnCommiCurrAmountDto next = iterator.next();
            PairFreeDetail pairFreeDetail = getPairFreeDetail(currencyAuthDto, next);

            List<AccountFee> accountFeeList = accountFeeService.getAccountFeeByMemberId(HelpUtils.newHashMap(CurrencyAuthConstant.memberId, pairFreeDetail.getMemberId(), CurrencyAuthConstant.feeCurrency, pairFreeDetail.getFeeCurrency().toUpperCase()));
            AccountFee accountFeeDB = new AccountFee();
            if (!CollectionUtils.isEmpty(accountFeeList)) {
                accountFeeDB = accountFeeList.get(0);
                accountFeeDB.setTotalAmount(accountFeeDB.getTotalAmount().add(pairFreeDetail.getRealAmount()));
                accountFeeDB.setUpdateTime(DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
            }
            ObjResp objResp = accountFeeManager.executeAccountFeeChange(accountFeeIncreaseManager
                    , getAccountFeeChange(pairFreeDetail));
            if (objResp.getState().equals(Resp.FAIL)) {
                logger.error("datalab,给用户手续费账户添加资产失败：{}",objResp.getMsg());
                throw new BusinessException(Resp.FAIL, objResp.getMsg(), null);
            }
            logger.warn("datalab，保存返佣明细：{}",JsonUtil.bean2JsonString(pairFreeDetail));
            pairFreeDetailService.save(pairFreeDetail);
        }
    }

    private PairFreeDetail getPairFreeDetail(CurrencyAuthDto currencyAuthDto, ReturnCommiCurrAmountDto next) {
        PairFreeDetail pairFreeDetail = new PairFreeDetail();
        pairFreeDetail.setMemberId(currencyAuthDto.getMemberId());
        pairFreeDetail.setCurrencyPairId(currencyAuthDto.getCurrencyPairId());
        pairFreeDetail.setCurrencyPair(currencyAuthDto.getSymbol());
        pairFreeDetail.settType(next.gettType());
        pairFreeDetail.setFeeScale(currencyAuthDto.getFeeScale());
        pairFreeDetail.setFeeCurrency(next.getFee_currency());
        pairFreeDetail.setFeeTotalAmount(next.getFee_amount());
        pairFreeDetail.setReaAmount(currencyAuthDto.getFeeScale().multiply(next.getFee_amount()).divide(new BigDecimal("100"), 4, BigDecimal.ROUND_HALF_UP));
        pairFreeDetail.setCreateTime(DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
        pairFreeDetail.setSearchDate(next.getSearchDate().substring(0, next.getSearchDate().length() - 1));
        return pairFreeDetail;
    }
    private AccountFee getAccountFeeChange(PairFreeDetail pairFreeDetail) {
        AccountFee accountFeeChange = new AccountFee();
        accountFeeChange.setMemberId(pairFreeDetail.getMemberId());
        accountFeeChange.setFeeCurrency(pairFreeDetail.getFeeCurrency());
        accountFeeChange.setTotalAmount(pairFreeDetail.getRealAmount());
        accountFeeChange.setForzenAmount(BigDecimal.ZERO);
        accountFeeChange.setCreateTime(DateUtil.dateToString(new Date(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
        accountFeeChange.setUpdateTime(accountFeeChange.getCreateTime());

        return accountFeeChange;
    }
}
