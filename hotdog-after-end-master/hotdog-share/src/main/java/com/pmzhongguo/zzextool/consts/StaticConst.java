/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/24 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.consts;

import com.pmzhongguo.zzextool.utils.NetworkUtils;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/24 16:27
 * @description：静态变量
 * @version: $
 */
public class StaticConst {

    /**
     * tomcat 端口
     */
    public static String server_port;

    /**
     * zk 地址
     */
    public static String ZKAddress;

    /**
     * dubbo 服务端口
     */
    public static String ZKPort;

    // 本机对外IP
    public static final String hostIP = NetworkUtils.getNetIp();

    // 内网IP
    public static final String localIP = NetworkUtils.getLocalIp();

    // 本机名称
    public static final String hostName = NetworkUtils.getHostName();

    public static String SUB_DATA;

    /**
     * dubbo 服务连接超时时间
     */
    public static Integer DUBBO_TIMEOUT;

    /**
     * dubbo服务接口版本
     */
    public static final String DUBBO_VERSION = "1.0.0";

    /**
     * redis 地址
     */
    public static String REDIS_Host;

    /**
     * redis端口
     */
    public static String REDIS_Port;

    /**
     * redis端口
     */
    public static String REDIS_Password;

    /**
     * 根节点路径
     */
    public static String GROUP_PATH;

    /**
     * 子节点路径
     */
    public static String SUB_PATH;

    public static int POOL_STATIC;

    public static int POOL_MARKET;

    public static int POOL_KLINE;

    /**
     * 0 不执行打印日志, 1 执行打印日志
     */
    public static int LOG_STATIC;

    public static int TIMEOUT_STATIC;

    /**
     * zk连接超时时间, 单位为毫秒.
     */
    public static final int SESSION_TIMEOUT = 10000;

    /**
     * 调度执行状态   1-执行    0-未执行
     */
    public static int TASK_STATUS = 0;

    /**
     * no 调用，
     */
    public static int TASK_NO_STATUS = 1;

    // 交易对提前开启时间，因为系统原因会有延时，所以需要提前，单位：秒（s）
    public static int ADVANCE_CURRENCY_PAIR_OPEN_TIME = -5;

    public static void setStaticConst(SystemConst systemConst) {
        server_port = systemConst.getServer_port();
        ZKAddress = systemConst.getZKAddress();
        ZKPort = systemConst.getZKPort();
        DUBBO_TIMEOUT = systemConst.getDubboTimeout();
        REDIS_Host = systemConst.getRedisHost();
        REDIS_Port = systemConst.getRedisPort();
        REDIS_Password = systemConst.getRedisPassword();
        GROUP_PATH = systemConst.getGroupPath();
        SUB_PATH = systemConst.getSubPath();
        SUB_DATA = hostIP + ":" + StaticConst.ZKPort;

        // 配置线程池参数
        POOL_STATIC = systemConst.getStaticPool() == 0 ? 30 : systemConst.getStaticPool();
        POOL_MARKET = systemConst.getMarketPool() == 0 ? 10 : systemConst.getMarketPool();
        POOL_KLINE = systemConst.getKlinePool() == 0 ? 10 : systemConst.getKlinePool();
    }
}
