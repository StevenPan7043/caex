/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/2 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.config;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.pmzhongguo.zzexrpcquartz.service.market.MarketApiService;
import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/2 14:28
 * @description：dubbo service
 * @version: $
 */
@Slf4j
@Component
public class DubboService {

    /**
     * 调度可用服务IP
     */
    public static String activeIp;
    private static boolean serviceStatus = false;
    /**
     * 保存dubbo服务接口
     * key 127.0.0.1:2181
     * value interface
     */
    private static ConcurrentHashMap<String, MarketApiService> facade = new ConcurrentHashMap<>();
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private TaskWatcher taskWatcher;
    /**
     * dubbo服务地址
     */
    private String url;
    @Autowired
    private MarketApiService marketApiService;

    public MarketApiService getMarketApiService(String ip) {
        ip = StringUtils.isEmpty(ip) ? activeIp : ip;
        // 当主机宕机时切换到本机服务
        if (StaticConst.SUB_DATA.equals(ip)) {
            StaticConst.TASK_STATUS = 1;
            return null;
        }
        // 判断服务是否启动完成
        if (!serviceStatus) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!facade.keySet().contains(ip)) {
                log.warn("服务未创建，获取不到服务接口。");
                return marketApiService;
            }
        }
        if (facade.keySet().contains(ip)) {
            return facade.get(StringUtils.isEmpty(ip) ? activeIp : ip);
        }
        log.error("未获取到运行中的dubbo服务");
        return marketApiService;
//        return createMarketService(MarketApiService.class, taskWatcher.getZooKeeper(), 0);
    }

    private void setMarketApiService(String ip, MarketApiService marketApiService) {
        facade.put(StringUtils.isEmpty(ip) ? activeIp : ip, marketApiService);
    }

    /**
     * 创建MarketApiService服务
     *
     * @param clazz
     * @param zooKeeper
     * @return
     */
    public MarketApiService createMarketService(Class<MarketApiService> clazz, ZooKeeper zooKeeper, int retries) {
        // 控制轮询次数
        ++retries;
        // 获取不到锁就创建服务对象
        MarketApiService marketService = null;
        try {
            marketService = this.createService(clazz, DubboService.activeIp, zooKeeper);
            serviceStatus = true;
            /*if (retries < 2)
            {
                marketService = this.createService(clazz, DubboService.activeIp, zooKeeper);
                if (marketService == null)
                {
                    log.error("未获取到运行中的dubbo服务，睡眠2秒后尝试重新获取，执行第" + (retries + 1) + "次获取");
                    Thread.sleep(2000);
                    this.createMarketService(MarketApiService.class, zooKeeper, retries);
                }
            } else
            {
                log.warn("当前运行机器IP : " + DubboService.activeIp + "。尝试获取 MarketApiService " + retries + "次之后未获取到！请重启本调度服务！");
                return null;
            }*/
        } catch (KeeperException e) {
            log.error("创建运行中MarketApiService 接口服务失败 : " + e.getLocalizedMessage());
        } catch (InterruptedException e) {
            log.error("创建运行中MarketApiService 接口服务失败 : " + e.getLocalizedMessage());
        }
        if (marketService != null) {
            this.setMarketApiService(DubboService.activeIp, marketService);
        }
        return marketService;
    }

    /**
     * 获取服务接口
     *
     * @param bean
     * @param ip
     * @param <T>
     * @return
     */
    private <T> T createService(Class<T> bean, String ip, ZooKeeper zooKeeper) throws KeeperException, InterruptedException {
        //获取dubbo服务和端口
        List<String> subNodes = zooKeeper.getChildren("/dubbo/" + bean.getName() + "/providers", false);
        boolean flag = true;
        // 尝试获取服务次数
        int count = 0;
        while (flag) {
            if (subNodes == null || subNodes.size() == 0) {
                if (count >= 5) {
                    taskWatcher.init();
                    return null;
                }
                Thread.sleep(2000);
                subNodes = zooKeeper.getChildren("/dubbo/" + bean.getName() + "/providers", false);
            } else {
                flag = false;
            }
            count++;
        }
        subNodes.forEach(item -> {
            try {
                item = URLDecoder.decode(item, "UTF-8");
                if (item.contains(ip)) {
                    url = item;
                }
            } catch (UnsupportedEncodingException e) {
                log.error("url 解码异常： " + e.getLocalizedMessage());
            }
        });
        try {
            Long startTime = DateUtils.getNowTimeStampMillisecond();
            ReferenceBean<T> referenceBean = new ReferenceBean<>();
            referenceBean.setApplicationContext(applicationContext);
            referenceBean.setInterface(bean);
            referenceBean.setUrl(url);
            referenceBean.setTimeout(StaticConst.DUBBO_TIMEOUT);
            referenceBean.setVersion(StaticConst.DUBBO_VERSION);
            referenceBean.setRetries(2);
            referenceBean.afterPropertiesSet();
            T resultService = referenceBean.get();
            Long endTime = DateUtils.getNowTimeStampMillisecond();
            log.warn("获取运行中的dubbo服务接口: " + bean.getName() + " ； 耗时: " + (endTime - startTime) + "毫秒； 当前运行机器IP: " + activeIp);
            return resultService;
        } catch (Exception e) {
            log.error("获取dubbo服务异常: " + e.getLocalizedMessage());
        }
        return null;
    }

}
