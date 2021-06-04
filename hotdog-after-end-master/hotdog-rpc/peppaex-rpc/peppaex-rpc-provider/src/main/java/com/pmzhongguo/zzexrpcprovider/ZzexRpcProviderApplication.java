package com.pmzhongguo.zzexrpcprovider;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.consts.SystemConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan("com.pmzhongguo")
@Configuration
@EnableDubbo
@EnableScheduling
@Slf4j
public class ZzexRpcProviderApplication {

    /**
     * tomcat 端口
     */
    @Value("${server.port}")
    private String server_port;
    /**
     * zk 地址
     */
    @Value("${dubbo.registry.address}")
    private String ZKAddress;
    /**
     * dubbo 服务端口
     */
    @Value("${dubbo.protocol.port}")
    private String ZKPort;
    /**
     * dubbo 服务连接超时时间
     */
    @Value("${dubbo.provider.timeout}")
    private Integer dubboTimeout;
    /**
     * redis 地址
     */
    @Value("${spring.redis.host}")
    private String redisHost;
    /**
     * redis端口
     */
    @Value("${spring.redis.port}")
    private String redisPort;
    /**
     * redis 密码
     */
    @Value("${spring.redis.password}")
    private String redisPassword;
    @Value("${group.path}")
    private String groupPath;
    @Value("${sub.path}")
    private String subPath;
    // 线程池数量
    @Value("${zzex.pool.static}")
    private String staticPool;
    @Value("${zzex.pool.market}")
    private String marketPool;
    @Value("${zzex.pool.kline}")
    private String klinePool;
    // 日志打印
    @Value("${zzex.log.static}")
    private String staticLog;
    // 设置锁超时时间
    @Value("${zzex.timeout.static}")
    private String staticTimeout;

    public static void main(String[] args) {
        SpringApplication.run(ZzexRpcProviderApplication.class, args);
    }

    /**
     * 初始化系统变量
     */
    @PostConstruct
    public void init() {
        // 初始化系统配置信息
        SystemConst systemConst = new SystemConst(server_port, ZKAddress, ZKPort, dubboTimeout, redisHost, redisPort, redisPassword, groupPath, subPath, 30, 10, 10);
        try {
            systemConst.setStaticPool(Integer.valueOf(staticPool));
            systemConst.setMarketPool(Integer.valueOf(marketPool));
            systemConst.setKlinePool(Integer.valueOf(klinePool));

            // 配置超时时间
            StaticConst.TIMEOUT_STATIC = Integer.valueOf(staticTimeout);
        } catch (Exception e) {
            log.warn("请配置线程参数: static: {}, market: {}, kline: {}, staticTimeout: {}", staticPool, marketPool, klinePool, staticTimeout);
        }
        StaticConst.setStaticConst(systemConst);
        // 配置日志参数
        if (StringUtils.isEmpty(staticLog)) {
            StaticConst.LOG_STATIC = 0;
        } else {
            if (staticLog.equals("1")) {
                StaticConst.LOG_STATIC = 1;
            } else {
                StaticConst.LOG_STATIC = 0;
            }
        }
    }

}
