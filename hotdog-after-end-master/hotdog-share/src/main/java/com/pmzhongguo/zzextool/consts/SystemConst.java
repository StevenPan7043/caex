/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/20 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.consts;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/20 22:45
 * @description：系统配置项
 * @version: $
 */
@Data
@Component
public class SystemConst
{

    public SystemConst()
    {
    }

    public SystemConst(String server_port, String ZKAddress, String ZKPort, Integer dubboTimeout,
                       String redisHost, String redisPort, String redisPassword, String groupPath,
                       String subPath, int staticPool, int marketPool, int klinePool)
    {
        this.server_port = server_port;
        this.ZKAddress = ZKAddress;
        this.ZKPort = ZKPort;
        this.dubboTimeout = dubboTimeout;
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.redisPassword = redisPassword;
        this.groupPath = groupPath;
        this.subPath = subPath;
        this.staticPool = staticPool;
        this.marketPool = marketPool;
        this.klinePool = klinePool;
    }

    /**
     * tomcat 端口
     */
    private String server_port;

    /**
     * zk 地址
     */
    private String ZKAddress;

    /**
     * dubbo 服务端口
     */
    private String ZKPort;

    /**
     * dubbo 服务连接超时时间
     */
    private Integer dubboTimeout;

    /**
     * redis 地址
     */
    private String redisHost;

    /**
     * redis端口
     */
    private String redisPort;

    /**
     * redis 密码
     */
    private String redisPassword;

    private String groupPath;

    private String subPath;


    // 线程池参数
    private int staticPool;

    private int marketPool;

    private int klinePool;
}
