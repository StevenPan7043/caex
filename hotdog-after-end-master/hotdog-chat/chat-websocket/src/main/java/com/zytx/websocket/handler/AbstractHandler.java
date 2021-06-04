package com.zytx.websocket.handler;

import com.zytx.websocket.constant.LogConstant;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netty实现初始层
 */
public abstract class AbstractHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger log = LoggerFactory.getLogger(AbstractHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg instanceof WebSocketFrame) {
                System.out.println("WebSocketFrame" + msg);
                webdoMessage(ctx, (WebSocketFrame) msg);
            } else if (msg instanceof FullHttpRequest) {
                System.out.println("FullHttpRequest" + msg);
                httpdoMessage(ctx, (FullHttpRequest) msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void webdoMessage(ChannelHandlerContext ctx, WebSocketFrame msg);

    protected abstract void httpdoMessage(ChannelHandlerContext ctx, FullHttpRequest msg);

    //加入，仅表示连接成功无法做事情
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info(LogConstant.HANDLERADDED + ctx.channel().remoteAddress().toString() + LogConstant.JOIN_SUCCESS);
    }

    //在线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info(LogConstant.CHANNELACTIVE + ctx.channel().remoteAddress().toString() + LogConstant.CHANNEL_SUCCESS);
    }

    //超时
//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        super.userEventTriggered(ctx, evt);
//    }
}
