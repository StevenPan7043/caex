/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/20 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.basic;

import com.pmzhongguo.zzexrpcprovider.bean.GenericServiceImpl;
import com.pmzhongguo.zzexrpcprovider.config.FrmZkWatch;
import com.pmzhongguo.zzexrpcprovider.config.TaskWatcher;
import com.pmzhongguo.zzexrpcprovider.scheduler.KLinesTask;
import com.pmzhongguo.zzexrpcprovider.scheduler.MarketTask;
import com.pmzhongguo.zzexrpcprovider.scheduler.StaticTask;
import com.pmzhongguo.zzexrpcprovider.scheduler.UsdtCnyPriceScheduler;
import com.pmzhongguo.zzexrpcprovider.service.currency.CurrencyService;
import com.pmzhongguo.zzexrpcprovider.service.framework.FrmUserService;
import com.pmzhongguo.zzexrpcprovider.utils.RedisMsgPubSubListener;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import com.pmzhongguo.zzextool.utils.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/20 22:16
 * @description：定时任务启动类 实现CommandLineRunner接口，我们可以在项目启动的时候进行一些数据的加载、
 * 执行某个特定的动作等，如果存在多个加载动作，可以使用@Order注解来排序。 （数字越小，优先级越高）
 * 执行时机 ： 容器启动完成时
 * @version: $
 */
@Slf4j
@Component
public class TaskRunner extends GenericServiceImpl implements CommandLineRunner {
    @Autowired
    protected CurrencyService currencyService;
    @Autowired
    protected FrmUserService frmUserService;
    @Autowired
    private TaskWatcher zkWatcher;
    @Autowired
    private MarketTask marketTask;

    @Autowired
    private KLinesTask kLinesTask;

    @Autowired
    private StaticTask staticTask;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private RedisMsgPubSubListener redisMsgPubSubListener;

    @Autowired
    private UsdtCnyPriceScheduler usdtCnyPriceScheduler;

    @Override
    public void run(String... args) {
        // 初始化外部配置文件
        TaskManager.writeIOAsync(true);
        // 初始化系统配置数据
        initData();
        zkWatcher.init();
        // 开启行情调度
        marketTask.startTask(marketTask);
        kLinesTask.startTask(kLinesTask);
        staticTask.startTask(staticTask);
        usdtCnyPriceScheduler.startTask(usdtCnyPriceScheduler);
        // 创建frm监听
        //createFrmZkWatch(Constants.ZK_WATCH_PATH_CURRENCY_PAIR);
        //createFrmZkWatch(Constants.ZK_WATCH_PATH_SYS_CONFIG);
        //createFrmZkWatch(Constants.ZK_WATCH_PATH_CURRENCY);
        JedisListen();
    }

    private void createFrmZkWatch(String path) {
        try {
            FrmZkWatch frmZkWatch = (FrmZkWatch) applicationContext.getBean("frmZkWatch");
            frmZkWatch.init(path);
            log.warn("<--------- FrmZkWatch创建成功, path : " + path + " ------->");
        } catch (Exception e) {
            log.error("<--------- FrmZkWatch创建失败, Cause by : " + e.getLocalizedMessage() + " ------->");
        }
    }

    private void JedisListen() {
        new Thread(() -> {
            while (true) {
                JedisUtil.getInstance().subscribe(redisMsgPubSubListener, JedisChannelConst.JEDIS_CHANNEL_CURRENCY,
                        JedisChannelConst.JEDIS_CHANNEL_SYNC_CONFIG, JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_PAIR);
            }
        }).start();
    }

}


