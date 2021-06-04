package com.pmzhongguo.market.scheduler;

import com.pmzhongguo.market.base.AbstractEosMarket;
import com.pmzhongguo.market.service.EOSMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author jary
 * @creatTime 2019/7/9 1:48 PM
 */
@EnableScheduling
@Component
public class EOSMarketScheduler extends AbstractEosMarket {
    @Autowired
    private EOSMarketService eosMarketService;

    /**
     * usdt与CNY汇率
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void tickers() {
        eosMarketService.getEosTickers();
    }


    /**
     * 火币行情数据
     * 1s执行一次
     */
    @Scheduled(cron = " */1 * * * * ?")
    public void quotationResult() {
        long start = System.currentTimeMillis();
        eosMarketService.setShanZhaiCoin();
        long end = System.currentTimeMillis();
        if (end - start > 1000) {
            logger.info("quotationResult定时任务执行消耗时间为，{}ms", end - start);
        }
    }

}
