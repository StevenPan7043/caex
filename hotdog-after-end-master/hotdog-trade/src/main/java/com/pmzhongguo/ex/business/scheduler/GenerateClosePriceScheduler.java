package com.pmzhongguo.ex.business.scheduler;

import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.entity.Trade;
import com.pmzhongguo.ex.business.mapper.OrderMapper;
import com.pmzhongguo.ex.business.service.CurrencyService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 生成每个交易对的收盘价、最高最低价
 * @author jary
 * @creatTime 2020/4/20 9:39 AM
 */
@Component
public class GenerateClosePriceScheduler {
    private Logger logger = LoggerFactory.getLogger(GenerateClosePriceScheduler.class);

    @Autowired
    CurrencyService currencyService;

    @Autowired
    OrderMapper orderMapper;

    /**
     * 每天晚上12执行一次
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void generateClosePriceScheduler() {
        logger.debug("generateClosePrice 开始执行======================");
        List<CurrencyPair> currencyPairLst = currencyService.getCurrencyPairLst();
        for (CurrencyPair currencyPair : currencyPairLst) {
            String symbolClosePriceKey = currencyPair.getKey_name().toUpperCase() + "_close_price";
            Trade trade = orderMapper.getTradeOrderByTimeLimitOne(HelpUtils.newHashMap("table_name", currencyPair.getKey_name().toLowerCase(), "time", HelpUtils.dateAddDay(-1)));
            if (currencyPair.getIs_ups_downs_limit() == 1) {
                if (trade != null) {
                    JedisUtil.getInstance().set(symbolClosePriceKey, trade.getPrice() + "", true);
                }
            } else {
                Object symbolClosePrice = JedisUtil.getInstance().get(symbolClosePriceKey, true);
                if (symbolClosePrice != null) {
                    JedisUtil.getInstance().del(symbolClosePriceKey);
                }
            }

        }
    }

}
