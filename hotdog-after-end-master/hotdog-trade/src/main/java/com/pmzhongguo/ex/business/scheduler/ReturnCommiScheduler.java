package com.pmzhongguo.ex.business.scheduler;


import com.pmzhongguo.ex.business.dto.AccountDto;
import com.pmzhongguo.ex.business.dto.ReturnCommiCurrAmountDto;
import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.business.entity.CurrencyPair;

import com.pmzhongguo.ex.business.service.CurrencyService;
import com.pmzhongguo.ex.business.service.ExService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.business.service.ReturnCommiService;
import com.pmzhongguo.ex.core.rediskey.returncommi.ReturnCommiKey;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.*;


/**
 * 每天统计每个币种返佣总数
 * 从每个交易对的交易表中查找每个天的交易数据，插入到另return_commi_curr_amount表中，
 * 再根据汇率换算统计到return_commi_amount表中
 *
 */
@Component
public class ReturnCommiScheduler {

    private static Logger returnCommiLogger = LoggerFactory.getLogger("returnCommi");


    @Autowired
    private ReturnCommiService returnCommiService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ExService exService;

    @Autowired
    private CurrencyService currencyService;
    /**
     * 返佣任务
     * 每天凌晨一点启动一次
     */
    @Scheduled(cron = "0 0 1 * * ?")
    // 测试，每1分钟一次
//    @Scheduled(cron = " * */5 * * * ?")
    public void countReturnCommiJob() {
        StopWatch stopWatch = new StopWatch("邀请交易返佣");
        stopWatch.start();
        String lockKey = ReturnCommiKey.returnCommiKey.getKey();
        boolean isLock = JedisUtil.getInstance().getLock(lockKey, IPAddressPortUtil.IP_ADDRESS,
                ReturnCommiKey.returnCommiKey.getExpireSeconds());
        // 未获取到分布式锁，不执行
        if (!isLock) {
            Object o = JedisUtil.getInstance().get(lockKey, true);
            returnCommiLogger.error("<===================== 返佣未获取锁，执行任务的主机：【{}】 =================> ",o);
            return;
        }
        try {
            // 执行返佣任务
            executeJob();
        } catch (Exception e) {
            returnCommiLogger.error("< ================= 返佣金异常信息：{}", e.fillInStackTrace());
        } finally {
            JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
        }
        stopWatch.stop();
        returnCommiLogger.error("<===== 返佣时间：\n {}", stopWatch.prettyPrint());
    }

    /**
     * 执行返佣任务
     */
    private void executeJob() {
        // 1. 先判断return_commi表中是否有有效的返佣记录
        List<Integer> memberIdReturnCommiList = hasValidateReturnCommi();
        if (CollectionUtils.isEmpty(memberIdReturnCommiList)) {
            return;
        }

        // 2. 查找所有所有交易对，根据交易对查找交易表
        // 3. 将明细插入到return_commi_curr_amout表中
        List<CurrencyPair> currencyPairLst = currencyService.findAllCurrencyPair();
        for (Integer memberId : memberIdReturnCommiList) {
            List<AccountDto> accountDtoList = memberService.findAccountListByMemberId(memberId);
            if (CollectionUtils.isEmpty(accountDtoList)) {
                continue;
            }
            // 查询每个用户交易t_trade_{pair}表的中的数据，并插入到return_commi_curr_amount表中
            handlerTradeAndReturnCommi(currencyPairLst,accountDtoList,memberId);
        }

        // 4. 统计return_commi_curr_amoun表的数量插入到return_commi_curr_amount表和用户资产m_account表中
        saveReturnCommiAmountAndMemberAccount();
    }


    /**
     * step 1
     * 先判断return_commi表中是否有有效的返佣记录
     * @return 有效记录的用户id
     */
    private List<Integer> hasValidateReturnCommi() {
        // 查找返佣有效时间内的member_id
        return returnCommiService.findReturnCommiByTimeAndGroupByMemberId(HelpUtils.formatDate8(new Date()));
    }

    /**
     * step 2,3
     * 查询交易t_trade_{pair}表的中的数据，并插入到return_commi_curr_amount表中
     * @param currencyPairLst
     * @param accountDtoList
     * @param memberId
     */
    private void handlerTradeAndReturnCommi(List<CurrencyPair> currencyPairLst, List<AccountDto> accountDtoList,Integer memberId) {
        // 查询时间
        Map<String, Object> param = new HashMap<String, Object>();
        long yesterday = HelpUtils.getAfterSomeDayLongTime(-1);
        String yyyyMMdd = HelpUtils.TimeStamp2StringDate(yesterday, "yyyy-MM-dd");
        param.put("start_time", yyyyMMdd + " 00:00:00");
        param.put("end_time", yyyyMMdd + " 23:59:59");

        for (CurrencyPair pair : currencyPairLst) {
            boolean match = accountDtoList.stream().anyMatch(e -> pair.getKey_name().contains(e.getCurrency()));
            // 如果用户没有这个币的资产自然就没有交易记录，也就不用查了
            if (!match) {
                continue;
            }
            param.put("table_name", "t_trade_" + pair.getKey_name().toLowerCase());
            param.put("fee_currency", pair.getBase_currency().toUpperCase());
            // 用来过滤机器人下单
            param.put("member_id", memberId);
            // 从交易表中找到某个用户每天要返现的手续费总和,交易表中买是基础货币
            try {
                List<ReturnCommiCurrAmountDto> baseDtoList = exService.findUnCountReturnCommiRecordOfDay(param);

                if (!CollectionUtils.isEmpty(baseDtoList)) {
                    returnCommiService.handleCountCurrReturnCommi(baseDtoList, pair.getBase_currency());
                }
            }catch (Exception e) {
                returnCommiLogger.error("< ================= 返佣金,交易表查找记录异常，信息：{}", e.fillInStackTrace());
                returnCommiLogger.error("< ================= 参数：{}", param.toString());
            }
            /// 交易表中卖是计价货币
            param.put("fee_currency", pair.getQuote_currency().toUpperCase());
            try {
                List<ReturnCommiCurrAmountDto> quoteDtoList = exService.findUnCountReturnCommiRecordOfDay(param);
                if (!CollectionUtils.isEmpty(quoteDtoList)) {
                    returnCommiService.handleCountCurrReturnCommi(quoteDtoList, pair.getQuote_currency());
                }
            }catch (Exception e) {
                returnCommiLogger.error("< ================= 返佣金,交易表查找记录异常，信息：{}", e.fillInStackTrace());
                returnCommiLogger.error("< ================= 参数：{}", param.toString());
            }
        }
    }

    /**
     * step 3
     * 统计return_commi_curr_amoun表的数量插入到return_commi_curr_amount表和用户资产m_account表中
     */
    private void saveReturnCommiAmountAndMemberAccount() {
        Map<String, Object> paramAmount = new HashMap<String, Object>();
        String currTime = HelpUtils.TimeStamp2StringDate(System.currentTimeMillis(), "yyyy-MM-dd");
        paramAmount.put("start_time", currTime + " 00:00:00");
        paramAmount.put("end_time", currTime + " 23:59:59");
        List<Map<String, Object>> list = returnCommiService.findCurrReturnCommiOfDay(paramAmount);

        if (!CollectionUtils.isEmpty(list)) {
            // 批量添加 + 更新用户账上余额
            List<CoinRecharge> coinRechargeList = returnCommiService.batchInsertAmountReturnCommiOfDay(list);
            // 更新用户资产
            for (CoinRecharge coinRecharge : coinRechargeList) {
                try {
                    memberService.manAddCoinRecharge(coinRecharge);
                } catch (Exception e) {
                    returnCommiLogger.error("<================ 返佣更新用户资产异常,更新信息： " + coinRecharge.toString());
                    returnCommiLogger.error("<================ 返佣更新用户资产异常信息： ", e.fillInStackTrace());
                }
            }
        }
    }

}