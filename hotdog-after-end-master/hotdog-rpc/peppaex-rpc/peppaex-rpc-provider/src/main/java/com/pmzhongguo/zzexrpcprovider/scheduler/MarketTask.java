/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/22 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.scheduler;

import com.pmzhongguo.zzexrpcprovider.service.task.MarketTaskService;
import com.pmzhongguo.zzextool.annotation.MyScheduled;
import com.pmzhongguo.zzextool.basic.AbstractBaseTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/22 11:20
 * @description：market调度
 * @version: $
 */
@Slf4j
@Component
public class MarketTask extends AbstractBaseTask {

    @Autowired
    private MarketTaskService marketTaskService;

    @Override
    protected void initTaskData() {
        this.poolSize = 1;
        this.threadNamePrefix = "MarketTask-TaskExecutor-";
    }

    @MyScheduled(cron = "0/1 * * * * ?")
    protected void executeTask() {
        marketTaskService.executeMarket();
    }

}
