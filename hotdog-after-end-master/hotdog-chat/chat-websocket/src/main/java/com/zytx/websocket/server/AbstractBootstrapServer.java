package com.zytx.websocket.server;


import com.zytx.websocket.config.InitNetty;
import com.zytx.websocket.constant.BootstrapConstant;
import com.zytx.websocket.filter.IpFilterRuleHandler;
import com.zytx.websocket.handler.DefaultAbstractHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 *
 **/
public abstract class AbstractBootstrapServer implements BootstrapServer {

    /**
     * @param channelPipeline channelPipeline
     * @param serverBean      服务配置参数
     */
    protected void initHandler(ChannelPipeline channelPipeline, InitNetty serverBean) {
        intProtocolHandler(channelPipeline, serverBean);
        channelPipeline.addLast(new IdleStateHandler(serverBean.getHeart(), 0, 0));
        channelPipeline.addLast(new DefaultAbstractHandler());
    }

    private void intProtocolHandler(ChannelPipeline channelPipeline, InitNetty serverBean) {
        channelPipeline.addLast(BootstrapConstant.HTTP_CODE, new HttpServerCodec());
        channelPipeline.addLast(BootstrapConstant.AGGREGATOR, new HttpObjectAggregator(serverBean.getMaxContext()));
        channelPipeline.addLast(BootstrapConstant.CHUNKED_WRITE, new ChunkedWriteHandler());
        channelPipeline.addLast(BootstrapConstant.WEB_SOCKET_HANDLER, new WebSocketServerProtocolHandler(serverBean.getWebSocketPath()));
        channelPipeline.addLast(BootstrapConstant.IP_FILTER_RULE, new RuleBasedIpFilter(new IpFilterRuleHandler()));
    }

}
