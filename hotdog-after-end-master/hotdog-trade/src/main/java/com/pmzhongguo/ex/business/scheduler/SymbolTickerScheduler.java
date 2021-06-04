package com.pmzhongguo.ex.business.scheduler;

import com.pmzhongguo.ex.business.service.manager.TickerManager;
import com.pmzhongguo.ex.core.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * 交易对行情收录
 *
 * @author jary
 * @creatTime 2020/5/14 9:29 AM
 */
@Component
public class SymbolTickerScheduler {
    private static Logger logger = LoggerFactory.getLogger(SymbolTickerScheduler.class);

    @Autowired
    private TickerManager tickerManager;

    @Scheduled(cron = "*/1 * * * * ?")
    public void symbolTicker() {
        String lockKey = "symbol_ticker_lockKey";
        boolean isLock = JedisUtil.getInstance().getLock(lockKey, IPAddressPortUtil.IP_ADDRESS, 2000);
        // 未获取到分布式锁，不执行
        if (!isLock) {
            logger.info("<===================== 生成交易对行情收录未获取锁 =================> ");
            return;
        }
        try {
            tickerManager.symbolTicker();
        } catch (Exception e) {
            logger.error("<===================== 生成交易对行情收录异常 =================> ");
        } finally {
            JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
        }
    }
}
