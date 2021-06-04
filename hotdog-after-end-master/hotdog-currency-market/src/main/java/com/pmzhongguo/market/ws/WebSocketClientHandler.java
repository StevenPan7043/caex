package com.pmzhongguo.market.ws;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pmzhongguo.market.constants.InitConstant;
import com.pmzhongguo.market.utils.DateUtil;
import com.pmzhongguo.market.utils.GZipUtils;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Date;

import static com.pmzhongguo.market.base.AbstractEosMarket.contractMarket;
import static com.pmzhongguo.market.base.AbstractEosMarket.marketResp;
import static com.pmzhongguo.market.service.impl.EOSMarketServiceImpl._BTC;
import static com.pmzhongguo.market.service.impl.EOSMarketServiceImpl._ETH;
import static com.pmzhongguo.market.service.impl.EOSMarketServiceImpl._USDT;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private Logger log = Logger.getLogger(WebSocketClientHandler.class);

    public static boolean RECONN_FLAG = false;

    private final WebSocketClientHandshaker handshaker;
    private ChannelPromise handshakeFuture;

    public WebSocketClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.warn("链接上啦。。。");
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("掉线了!" + DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        log.warn("检测到火币行情接口掉线。。。");
        try {
            WebSocketClient.HBSocket(InitConstant.MARKET_SYMBOL_LIST);
            log.warn("火币行情接口重连成功。。。");
        } catch (Exception e) {
            RECONN_FLAG = true;
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                log.info("WebSocket Client connected!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                log.info("WebSocket Client failed to connect");
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException("Unexpected FullHttpResponse (getStatus=" + response.status() + ", content="
                    + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");
            ch.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing" + DateUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            ch.close();
            return;
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
            byte[] temp = new byte[binaryFrame.content().readableBytes()];
            binaryFrame.content().readBytes(temp);
            temp = GZipUtils.decompress(temp);
            String str = new String(temp, "UTF-8");
            if (str.contains("ping")) {
                ch.writeAndFlush(new TextWebSocketFrame(str.replace("ping", "pong")));
            }
            JSONObject json = JSON.parseObject(str);
            try {
                if (json.containsKey("tick")) {
                    String chStr = json.getString("ch");
                    String b = chStr.replace(".", ",");
                    String[] chArr = b.split(",");
                    String symbol = chArr[1];// 交易对 格式为btcusdt
                    String event = chArr[2];// 事件 kline depth
                    switch (event) {
                        case "kline":
                            String marketKey = "";
                            if (symbol.endsWith("usdt")) {
                                String kline_coin = symbol.replace("usdt", "");
                                marketKey = (kline_coin + _USDT).toUpperCase();
                            } else if (symbol.endsWith("eth")) {
                                String kline_coin = symbol.replace("eth", "");
                                marketKey = (kline_coin + _ETH).toUpperCase();
                            } else if (symbol.endsWith("btc")) {
                                String kline_coin = symbol.replace("btc", "");
                                marketKey = (kline_coin + _BTC).toUpperCase();
                            } else {
                                return;
                            }
                            JSONObject tick = json.getJSONObject("tick");
                            String close = tick.getString("close");
                            contractMarket.put(marketKey, new BigDecimal(close));

                            BigDecimal[] bigDecimals = new BigDecimal[2];
                            BigDecimal bigDecimal = new BigDecimal(close);
                            bigDecimals[0] = bigDecimal;
                            bigDecimals[1] = bigDecimal;
                            marketResp.put(marketKey, bigDecimals);
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println("socke火币异常：" + e.getMessage());
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}
