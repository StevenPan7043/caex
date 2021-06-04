/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/22 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.service.task;

import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.zzexrpcprovider.bean.GenericServiceImpl;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;
import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.threadpool.ZzexThreadFactory;
import com.pmzhongguo.zzextool.utils.DateUtils;
import com.pmzhongguo.zzextool.utils.IPAddressPortUtil;
import com.pmzhongguo.zzextool.utils.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/22 17:24
 * @description：k线调度service
 * @version: $
 */
@Slf4j
@Service
public class KLineTaskServiceImpl extends GenericServiceImpl implements KlineTaskService {
    String ip = IPAddressPortUtil.IP_ADDRESS;
    String lockKey = "kLinesScheduled_" + ip;
    ExecutorService pool = Executors.newFixedThreadPool(StaticConst.POOL_KLINE, new ZzexThreadFactory("KlineTaskService"));
    @Autowired
    private MarketTaskService marketTaskService;

    @Override
    public void executeKLine() {
        boolean isLock = JedisUtil.getInstance().getLock(lockKey, ip, 10 * 1000);
        if (isLock) {
            long start = System.currentTimeMillis();
            try {
                List<CurrencyPair> cpLst = QuotationKeyConst.currencyPairs;
                CountDownLatch countDownLatch = new CountDownLatch(cpLst.size());
                for (CurrencyPair currencyPair : cpLst) {
                    // 该交易对的开启时间在当前系统时间
                    long time = DateUtils.differentDaysBySecond(currencyPair.getOpen_time(), null);
                    if (time > StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME) {
                        String symbol = currencyPair.getKey_name().toLowerCase();
                        pool.execute(new RunnableJob(countDownLatch, symbol));
                    } else {
                        countDownLatch.countDown();
                    }
                }
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } finally {
                boolean isRelease = JedisUtil.getInstance().releaseLock(lockKey, ip);
                if (!isRelease) {
                    long end = System.currentTimeMillis();
                    log.info(" release kLinesScheduled Lock fail。key:{}  耗时 : {}  ms", lockKey, end - start);
                }
            }
        }
    }

    class RunnableJob implements Runnable {

        CountDownLatch countDownLatch;
        String symbol;

        public RunnableJob(CountDownLatch countDownLatch, String symbol) {
            super();
            this.countDownLatch = countDownLatch;
            this.symbol = symbol;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                marketTaskService.generateKLineResp(symbol);
            } catch (Exception e) {
                log.warn(symbol + "k线生成异常");
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }
    }
}
