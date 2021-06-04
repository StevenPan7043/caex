/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/14 All Rights Reserved.
 */
package com.pmzhongguo.ex.core.config;

import com.pmzhongguo.zzexrpcquartz.service.market.MarketApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/14 16:26
 * @description：spring 启动后调用
 * @version: $
 */
@Service
public class StartCallConfig implements ApplicationListener<ContextRefreshedEvent>
{

    private static final Logger LOGGER = LoggerFactory.getLogger(StartCallConfig.class);

    //@Autowired
   // private MarketApiService marketApiService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event)
    {
        if (event.getApplicationContext().getParent() == null)
        {
            //检测调度状态
            //LOGGER.warn("<==================== 检测调度状态： " + marketApiService.heartBeat());
        }
    }
}
