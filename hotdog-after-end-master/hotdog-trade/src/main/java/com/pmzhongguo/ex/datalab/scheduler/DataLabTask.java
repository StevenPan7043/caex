package com.pmzhongguo.ex.datalab.scheduler;

import com.pmzhongguo.ex.datalab.manager.CurrencyAuthManager;
import com.pmzhongguo.ex.datalab.service.CurrencyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jary
 * @creatTime 2019/12/3 10:17 AM
 */
@Component
public class DataLabTask implements SimpleTask {

    @Autowired
    private CurrencyAuthManager currencyAuthManager;

    /**
     * 获取有交易对权限的实验室基础数据
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 */5 * * * ?")
//    @Scheduled(cron = "*/30 * * * * ?")
    @Override
    public void execute() {
        currencyAuthManager.execute();
    }


    /**
     * 手续费分账
     * 每天凌晨1点执行一次
     */
//    @Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0 0 1 * * ?")
    public void executeSeparateAccountTask() {
        /**
         * 获取当前交易对T-1日交易手续费总量
         * 按手续费比例分账，入账
         * 插入明细记录
         */
        currencyAuthManager.executeSeparateAccountTask();
    }
}
