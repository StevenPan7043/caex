package com.pmzhongguo.market.ws;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an example of a WebSocket client.
 * <p>
 * In order to run this example you need a compatible WebSocket server.
 * Therefore you can either start the WebSocket server from the examples by
 * running {@link } or connect to an existing WebSocket server such as
 * <a href="http://www.websocket.org/echo.html">ws://echo.websocket.org</a>.
 * <p>
 * The client will attempt to connect to the URI passed to it as the first
 * argument. You don't have to specify any arguments if you want to connect to
 * the example WebSocket server, as this is the default.
 */
public final class WebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketClient.class);
    static final String URL = System.getProperty("url", "wss://api.huobi.br.com/ws");
	//备用地址
//    static final String URL = System.getProperty("url", "wss://api.huobi.pro/ws");

    public static void HBSocket(List<String> marketSymbolList) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            URI uri = new URI(URL);
            String scheme = uri.getScheme() == null ? "ws" : uri.getScheme();
            final String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
            final int port;
            if (uri.getPort() == -1) {
                if ("ws".equalsIgnoreCase(scheme)) {
                    port = 80;
                } else if ("wss".equalsIgnoreCase(scheme)) {
                    port = 443;
                } else {
                    port = -1;
                }
            } else {
                port = uri.getPort();
            }
            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                System.err.println("Only WS(S) is supported.");
                return;
            }

            final boolean ssl = "wss".equalsIgnoreCase(scheme);
            final SslContext sslCtx;
            if (ssl) {
                sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            } else {
                sslCtx = null;
            }
            final WebSocketClientHandler handler = new WebSocketClientHandler(WebSocketClientHandshakerFactory
                    .newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));

            Bootstrap b = new Bootstrap();
            b.group(group).option(ChannelOption.TCP_NODELAY, true).channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            if (sslCtx != null) {
                                p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                            }
                            p.addLast(new HttpClientCodec(), new HttpObjectAggregator(8192),
                                    WebSocketClientCompressionHandler.INSTANCE, handler);
                        }
                    });

            Channel ch = b.connect(uri.getHost(), port).sync().channel();
            handler.handshakeFuture().sync();
            List<String> hbApiParamMap = new ArrayList<String>();
            // 推送
            for (String symbol : marketSymbolList) {
                logger.info("推送火币深度交易对,ip:{},port:{},coin:{}", host, uri.getPort(), symbol);
                String market = symbol.replace("_", "").toLowerCase();
                //推送K线图
                JSONObject obj = new JSONObject();
                obj.put("sub", "market." + market + ".kline.1min");
                obj.put("id", "id1");
                hbApiParamMap.add(obj.toJSONString());
            }
            for (String hbApiParam : hbApiParamMap) {
                WebSocketFrame frame = new TextWebSocketFrame(hbApiParam);
                ch.writeAndFlush(frame);
            }
        } catch (Exception e) {
            logger.warn("链接火币ws失败，url：{}",URL);
            group.shutdownGracefully();
            return;
        }
        logger.warn("-------火币ws初始化连接成功-------");
    }
}
