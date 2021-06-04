package com.pmzhongguo.ex.core.websocket;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import javax.websocket.server.ServerEndpointConfig.Configurator;

public class WebsocketConfig extends Configurator {
	/* 修改握手,就是在握手协议建立之前修改其中携带的内容 */
	@Override
	public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
		/* 如果没有监听器,那么这里获取到的HttpSession是null */
		HttpSession httpSession = (HttpSession) request.getHttpSession();
		sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
		super.modifyHandshake(sec, request, response);
	}

}
