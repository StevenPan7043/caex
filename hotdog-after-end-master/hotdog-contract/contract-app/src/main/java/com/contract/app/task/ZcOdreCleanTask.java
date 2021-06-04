package com.contract.app.task;

import com.contract.common.FunctionUtils;
import com.contract.dao.CZcOrderMapper;
import com.contract.entity.CZcOrder;
import com.contract.entity.Coins;
import com.contract.service.redis.RedisUtilsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Daily
 * @date 2020/7/4 20:43
 */


@Component
public class ZcOdreCleanTask {

    Logger logger = LoggerFactory.getLogger(ZcOdreCleanTask.class);

    @Autowired
    private CZcOrderMapper cZcOrderMapper;

//    ExecutorService pool = new ThreadPoolExecutor(8, 10,
//            60 * 1000L, TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<Runnable>());

    ExecutorService pool = Executors.newFixedThreadPool(8);

    String LOCKKEY_PRE = "AutoCleanRedisZcOrder";

    @Autowired
    private RedisUtilsService redisUtilsService;

    /**
     * 订单已平仓，redis未清理
     * 清理redis里的逐仓订单
     * 临时应急解决方案，后面还要优化
     */
    @Scheduled(cron="0 0/3 * * * ?")
    public void AutoCleanRedisOrder() {
        cleanRedisOrder();
    }

    public void cleanRedisOrder() {
        //拿到币种列表
        List<Coins> coins = redisUtilsService.queryCoins();
        for (Coins c : coins) {
            //order_*_EOS/USDT
            pool.execute(new clearRedisZcOrderRunable(c.getName()));
        }
    }

    class clearRedisZcOrderRunable implements Runnable{

        private String coin;

        public clearRedisZcOrderRunable(String coin){
            this.coin = coin;
        }
        @Override
        public void run() {
            String lockKey = LOCKKEY_PRE + coin;
            boolean lock = redisUtilsService.lock(lockKey);
            if (lock) {
                try {
                    String zcOrderKey = "zc_*_" + coin + "_*";
                    Set<String> keySetAll = redisUtilsService.getKeys(zcOrderKey);
                    for (String k : keySetAll) {
                        String ordercode = k.split("_")[4];
                        CZcOrder cZcOrder = cZcOrderMapper.getByOrdercode(ordercode);
                        //订单不为空并且状态为2（已完成），将redis缓存订单删除
                        if (cZcOrder != null && FunctionUtils.isEquals(2, cZcOrder.getStatus())) {
//                            String orderkey = "order_" + cContractOrder.getCid() + "_" + coin + "_" + cContractOrder.getOrdercode();
                            String orderkey = "zc_" + cZcOrder.getCid() + "_" + coin + "_" + cZcOrder.getOrdercode();
                            logger.warn("ZcOdreCleanTask completed ZC order: " + cZcOrder.getOrdercode() + " deleted from redis!");
                            redisUtilsService.deleteKey(orderkey);
                        }
                    }
                } finally {
                    redisUtilsService.deleteKey(lockKey);
                }
            }else{
                logger.warn("获取分布式锁 " + lockKey + " 失败！");
            }
        }
    }


}
