package com.zytx.websocket.handler;

import com.alibaba.fastjson.JSON;
import com.zytx.common.cache.SocketCache;
import com.zytx.common.dto.ChatMessageDto;
import com.zytx.common.util.StringUtil;
import com.zytx.common.constant.ChatConstant;
import com.zytx.websocket.constant.LogConstant;
import com.zytx.websocket.service.SocketMessageService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 *
 */
@Component
@ChannelHandler.Sharable
public class DefaultAbstractHandler extends AbstractHandler {

    private final Logger log = LoggerFactory.getLogger(DefaultAbstractHandler.class);

    @Autowired
    private SocketMessageService socketMessageService;
    private static DefaultAbstractHandler defaultAbstractHandler;

    @PostConstruct
    public void init() {
        //初始化当前类，以便能够使用注解注入
        defaultAbstractHandler = this;
    }

    public void getService() {
        if (socketMessageService == null) {
            synchronized (DefaultAbstractHandler.class) {
                if (socketMessageService == null) {
                    socketMessageService = defaultAbstractHandler.socketMessageService;
                }
            }
        }
    }

    @Override
    protected void webdoMessage(ChannelHandlerContext ctx, WebSocketFrame msg) {
        getService();
        /**
         * 目前只有文本消息类型
         * 后面如果加其他类型时在进行处理
         */
        if (msg instanceof TextWebSocketFrame) {
            //处理文本信息
            TextWebSocketFrame text = (TextWebSocketFrame) msg;
            String message = text.text();
            if (StringUtil.isNullOrBank(message)) {
                return;
            }
            ChatMessageDto chat = JSON.parseObject(message, ChatMessageDto.class);
            switch (chat.getType()) {
                case ChatConstant.LOGIN:
                    //保存到缓存
                    socketMessageService.login(ctx, chat);
                    break;
                case ChatConstant.TEXT:
                    socketMessageService.doMessage(ctx, chat);
                    break;
                case ChatConstant.HEART_BEAT:
                    log.warn(SocketCache.getToken(ctx.channel()) + "用户发送心跳");
                    break;
                case ChatConstant.WITHDRAW:
                    socketMessageService.withdraw(ctx, chat);
                    break;
            }

        }

    }

    @Override
    protected void httpdoMessage(ChannelHandlerContext ctx, FullHttpRequest msg) {
        //前端连接socket时http请求，可以在此获取一些缓存数据
        ctx.channel().writeAndFlush(new TextWebSocketFrame("获取一些缓存数据"));
    }

    //异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info(LogConstant.EXCEPTIONCAUGHT + ctx.channel().remoteAddress().toString() + LogConstant.DISCONNECT);
        ctx.close();
    }

    //掉线
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info(LogConstant.CHANNELINACTIVE + ctx.channel().localAddress().toString() + LogConstant.CLOSE_SUCCESS);
        try {
            getService();
            try {
                socketMessageService.logout(ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ctx.close();
        } catch (Exception e) {
            log.error(LogConstant.NOTFINDLOGINCHANNLEXCEPTION);
        }
    }

    //超时
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("IdleStateEvent:超时处理");
        try {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.isFirst()) return;
                getService();
                try {
                    socketMessageService.logout(ctx);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ctx.close();
            }
        } catch (Exception e) {
            log.error(LogConstant.USEREVENTTRIGGERED);
        }
    }
}
