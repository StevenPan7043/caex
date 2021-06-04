/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/22 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.scheduler;

import com.pmzhongguo.zzexrpcprovider.service.task.StaticTaskService;
import com.pmzhongguo.zzextool.annotation.MyScheduled;
import com.pmzhongguo.zzextool.basic.AbstractBaseTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/22 11:40
 * @description：static 调度
 * @version: $
 */
@Slf4j
@Component
//@TaskService(value = "StaticTask")
public class StaticTask extends AbstractBaseTask {

    @Autowired
    private StaticTaskService staticTaskService;

    @Override
    protected void initTaskData() {
        this.poolSize = 1;
        this.threadNamePrefix = "StaticTask-TaskExecutor-";
    }

    @MyScheduled(cron  = "0/1 * * * * ?")
    protected void executeTask() {
        staticTaskService.executeStatic();
    }

}
