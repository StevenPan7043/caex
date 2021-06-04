package com.zytx.websocket.server;



/**
 * netty服务配置和启动
 */
public interface BootstrapServer {

    void shutdown();

    void start();

}
