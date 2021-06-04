package com.contract.app.socket;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.contract.entity.Coins;

import com.contract.enums.KlineTypeEnums;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
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
	static final String URL = System.getProperty("url", "wss://api.huobi.pro/ws");
//	旧的不能用了 static final String URL = System.getProperty("url", "wss://api.huobi.br.com/ws");
//	备用地址  static final String URL = System.getProperty("url", "wss://api-aws.huobi.pro/ws");

	public static void HBSocket(List<Coins> coins) {
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
			List<String> msgs = new ArrayList<String>();
			// 推送
			for(Coins c:coins) {
				logger.info("推送火币深度交易对,ip:{},port:{},coin:{}", host, uri.getPort(), JSONArray.toJSONString(c));
				String market=c.getSymbol().replace("_", "");
				//先推送 市场价格
				msgs.add("{\"sub\":\"market."+market+".detail\",\"id\":\"id1\"}");
				//然后获取深度
				msgs.add("{\"sub\":\"market."+market+".depth.step0\",\"id\":\"id1\"}");
				//1min, 5min, 15min, 30min, 60min, 1day,1week
				for (KlineTypeEnums klineTypeEnums2 : KlineTypeEnums.values()) {
					//推送K线图
					JSONObject obj=new JSONObject();
					obj.put("sub", "market."+market+".kline."+klineTypeEnums2.getName());
					obj.put("id", "id1");
//					obj.put("to", "2556115200");//截止时间
//					obj.put("from", "1563206400");//开始时间 到秒

					msgs.add(obj.toJSONString());
				}
			}
//			msgs.add("{\"sub\":\"market.btcusdt.kline.1min\",\"id\":\"id1\"}");
//			msgs.add("{\"sub\":\"market.btcusdt.depth.step0\",\"id\":\"id1\"}");
//			msgs.add("{\"sub\":\"market.btcusdt.detail\",\"id\":\"id1\"}");
			for (int i = 0; i < msgs.size(); i++) {
				WebSocketFrame frame = new TextWebSocketFrame(msgs.get(i));
				ch.writeAndFlush(frame);
			}
			/*
			 * BufferedReader console = new BufferedReader(new
			 * InputStreamReader(System.in)); while (true) { String msg =
			 * console.readLine(); if (msg == null) { break; } else if
			 * ("bye".equals(msg.toLowerCase())) { ch.writeAndFlush(new
			 * CloseWebSocketFrame()); ch.closeFuture().sync(); break; } else if
			 * ("ping".equals(msg.toLowerCase())) { WebSocketFrame frame = new
			 * PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
			 * ch.writeAndFlush(frame); } else { WebSocketFrame frame = new
			 * TextWebSocketFrame(msg); ch.writeAndFlush(frame); } }
			 */

		} catch (Exception e) {
			logger.info("链接火币ws失败，url：wss://api.huobi.br.com/ws");
			group.shutdownGracefully();
		}
	}
}
