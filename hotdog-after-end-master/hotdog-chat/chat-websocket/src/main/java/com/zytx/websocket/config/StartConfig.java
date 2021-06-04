package com.zytx.websocket.config;


import com.zytx.common.util.RedisUtil;
import com.zytx.websocket.server.BootstrapServer;
import com.zytx.websocket.server.NettyBootstrapServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StartConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(StartConfig.class);

    private static BootstrapServer bootstrapServer = null;

    /**
     * 启动服务
     **/
    public static void open() {
        bootstrapServer = new NettyBootstrapServer();
        bootstrapServer.start();
    }

    /**
     * 关闭服务
     */
    public static void close() {
        if (bootstrapServer != null) {
            bootstrapServer.shutdown();
        }
    }

}
