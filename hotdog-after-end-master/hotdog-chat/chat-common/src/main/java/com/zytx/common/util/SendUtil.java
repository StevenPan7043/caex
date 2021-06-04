package com.zytx.common.util;

import com.zytx.common.cache.SocketCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SendUtil {
    private static final Logger log = LoggerFactory.getLogger(SendUtil.class);

    public static Boolean send(Channel channel, String message) {
        final boolean[] flag = {true};
        channel.writeAndFlush(new TextWebSocketFrame(message)).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    log.warn("发送失败：" + future.cause());
                    flag[0] = false;
                }
            }
        });
        return flag[0];
    }
}
