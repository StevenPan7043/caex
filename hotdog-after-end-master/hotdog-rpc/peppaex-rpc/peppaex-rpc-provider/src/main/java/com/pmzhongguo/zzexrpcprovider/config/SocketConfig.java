/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/4 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.config;

import com.pmzhongguo.zzexrpcprovider.interceptor.HandshakeInterceptor;
import com.pmzhongguo.zzexrpcprovider.socket.QuotationMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/4 17:10
 * @description：socket config
 * @version: $
 */
@Configuration
@EnableWebSocket
public class SocketConfig implements WebSocketConfigurer {

    @Autowired
    private QuotationMessageHandler quotationHandler;

    @Autowired
    private HandshakeInterceptor handshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        String wsUrl = "/ws/v1";
        registry.addHandler(quotationHandler, wsUrl).setAllowedOrigins("*").addInterceptors(handshakeInterceptor);
    }
}
