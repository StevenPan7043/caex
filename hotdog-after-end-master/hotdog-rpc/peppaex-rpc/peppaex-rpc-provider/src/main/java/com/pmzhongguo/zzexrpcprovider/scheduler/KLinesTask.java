/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/22 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.scheduler;

import com.pmzhongguo.zzexrpcprovider.service.task.KlineTaskService;
import com.pmzhongguo.zzextool.annotation.MyScheduled;
import com.pmzhongguo.zzextool.basic.AbstractBaseTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/22 11:43
 * @description：kline 调度
 * @version: $
 */
@Slf4j
@Component
public class KLinesTask extends AbstractBaseTask {
    @Autowired
    private KlineTaskService klineTaskService;

    @Override
    protected void initTaskData() {
        this.poolSize = 1;
        this.threadNamePrefix = "KLinesTask-TaskExecutor-";
    }

    @MyScheduled(cron = "*/10 * * * * ?")
    protected void executeTask() {
        // 打印测试日志
        klineTaskService.executeKLine();
    }

}
