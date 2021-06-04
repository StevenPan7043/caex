package com.contract.app.task;

import com.contract.common.FunctionUtils;
import com.contract.dao.CContractOrderMapper;
import com.contract.entity.CContractOrder;
import com.contract.entity.Coins;
import com.contract.service.redis.RedisUtilsService;
import com.contract.service.wallet.btc.UsdtPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @author Daily
 * @date 2020/7/3 12:15
 */


@Component
public class OdreCleanTask {

    Logger logger = LoggerFactory.getLogger(OdreCleanTask.class);

    @Autowired
    private CContractOrderMapper cContractOrderMapper;

//    ExecutorService pool = new ThreadPoolExecutor(8, 10,
//            60 * 1000L, TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<Runnable>());

    ExecutorService pool = Executors.newFixedThreadPool(8);

    String LOCKKEY_PRE = "AutoCleanRedisOrder";

    @Autowired
    private RedisUtilsService redisUtilsService;

    /**
     * 订单已平仓，redis未清理
     * 清理redis里的全仓订单
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
            pool.execute(new clearRedisOrderRunable(c.getName()));
        }
    }

    class clearRedisOrderRunable implements Runnable{

        private String coin;

        public clearRedisOrderRunable(String coin){
            this.coin = coin;
        }
        @Override
        public void run() {
            String lockKey = LOCKKEY_PRE + coin;
            boolean lock = redisUtilsService.lock(lockKey);
            if (lock) {
                try {
                    String orderKey = "order_*_" + coin + "_*";
                    Set<String> keySetAll = redisUtilsService.getKeys(orderKey);
                    for (String k : keySetAll) {
                        String ordercode = k.split("_")[4];
                        CContractOrder cContractOrder = cContractOrderMapper.getByOrdercode(ordercode);
                        //订单不为空并且状态为2（已完成），将redis缓存订单删除
                        if (cContractOrder != null && FunctionUtils.isEquals(2, cContractOrder.getStatus())) {
                            String orderkey = "order_" + cContractOrder.getCid() + "_" + coin + "_" + cContractOrder.getOrdercode();
                            logger.warn("OdreCleanTask completed QC order: " + cContractOrder.getOrdercode() + " deleted from redis!");
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
