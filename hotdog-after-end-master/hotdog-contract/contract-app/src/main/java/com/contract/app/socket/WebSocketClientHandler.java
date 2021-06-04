package com.contract.app.socket;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.contract.dto.KlineDto;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.contract.common.DateUtil;
import com.contract.common.FunctionUtils;
import com.contract.dto.Depth;
import com.contract.dto.SymbolDto;
import com.contract.entity.Coins;
import com.contract.service.SpringBeanFactoryUtils;
import com.contract.service.redis.RedisUtilsService;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

public class WebSocketClientHandler extends SimpleChannelInboundHandler<Object> {

    private Logger log = Logger.getLogger(WebSocketClientHandler.class);
    public static boolean RECONN_FLAG = false;
    ConcurrentHashMap<String, ChannelGroup> rooms = new ConcurrentHashMap();

    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup("ChannelGroups",
            GlobalEventExecutor.INSTANCE);

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


    /**
     * 用newSingleThreadExecutor保证数据的顺序执行
     */
    Executor detailExecutor= Executors.newSingleThreadExecutor();
    Executor depthExecutor= Executors.newSingleThreadExecutor();
    Executor klineExecutor= Executors.newSingleThreadExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.warn("链接上啦。。。");
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("掉线了!" + DateUtil.toDateString(DateUtil.currentDate(), "yyyy-MM-dd HH:mm:ss"));
        log.warn("检测到火币行情接口掉线。。。");
        List<Coins> coins = getRedisUtilsService().queryCoins();
        try {
            WebSocketClient.HBSocket(coins);
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
            System.out.println("WebSocket Client received closing" + DateUtil.toDateString(DateUtil.currentDate(), "yyyy-MM-dd HH:mm:ss"));
            ch.close();
            return;
        } else if (frame instanceof BinaryWebSocketFrame) {
            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame) frame;
            byte[] temp = new byte[binaryFrame.content().readableBytes()];
            binaryFrame.content().readBytes(temp);
            temp = GZipUtils.decompress(temp);
            String str = new String(temp, "UTF-8");
            if (str.contains("ping")) {
                // log.info("send:" + str.replace("ping", "pong"));
                ch.writeAndFlush(new TextWebSocketFrame(str.replace("ping", "pong")));
            }
            JSONObject json = JSON.parseObject(str);
            try {
//				System.out.println(json);
                if (json.containsKey("tick")) {
                    // String ts= json.getString("ts");
                    String chStr = json.getString("ch");
                    String b = chStr.replace(".", ",");
                    String[] chArr = b.split(",");
                    String symbol = chArr[1];// 交易对 格式为btcusdt
                    String event = chArr[2];// 事件 kline depth
                    switch (event) {
                        case "detail":
                            depthExecutor.execute(new DetailRunable(symbol, json));
                            break;
                        case "depth":
                            detailExecutor.execute(new DepthRunable(symbol, json));
                            break;
                        case "kline":
                            klineExecutor.execute(new KlineRunable(symbol, json, chArr[3]));
                            break;
                        default:
                            break;
                    }
                } else {
                    // log.info("receive:" + json.toJSONString());
                }
            } catch (Exception e) {
                System.out.println("socke火币异常：" + e.getMessage());
            }
        }
    }

    private class DetailRunable implements Runnable{

        private String symbol;

        JSONObject json;

        public DetailRunable(String symbol, JSONObject json) {
            this.symbol = symbol;
            this.json = json;
        }

        @Override
        public void run() {
            // 表示查询 市场概要
            String coin = symbol.replace("usdt", "");
            String tick = json.getString("tick");
            JSONObject json2 = JSONObject.parseObject(tick);
            SymbolDto dto = new SymbolDto();
            dto.setCoin(coin.toUpperCase());
            dto.setSymbol(coin + "_usdt");
            dto.setName(coin.toUpperCase() + "/USDT");
            dto.setUnit("USDT");
            Coins e = getRedisUtilsService().getCoinBySymbol(dto.getSymbol());
            dto.setType(e.getType());
            dto.setZcscale(e.getZcscale());
            dto.setZcstopscale(e.getZcstopscale());
            dto.setZctax(e.getZctax());
            dto.setSort(e.getSort());
            dto.setOpenVal(e.getOpenval());
            dto.setHigh(json2.getBigDecimal("high"));
            dto.setLow(json2.getBigDecimal("low"));
            dto.setVol(json2.getBigDecimal("vol"));
            BigDecimal usdtPrice = json2.getBigDecimal("close");// 实时加
            dto.setUsdtPrice(usdtPrice);

            BigDecimal openPrice = e.getOpenval();// 开盘价
            BigDecimal money = BigDecimal.ZERO;// 计算权重
            // 保存涨跌比例
            BigDecimal scale = BigDecimal.ZERO;
            if (usdtPrice.compareTo(openPrice) == 0) {
                dto.setIsout(2);
            } else if (usdtPrice.compareTo(openPrice) > 0) {
                dto.setIsout(1);
                money = FunctionUtils.sub(usdtPrice, openPrice, 8);
            } else if (usdtPrice.compareTo(openPrice) < 0) {
                dto.setIsout(0);
                money = FunctionUtils.sub(openPrice, usdtPrice, 8);
            }
            scale = FunctionUtils.div(money, openPrice, 5);
            // 保存涨跌比例
            dto.setScale(scale);
            BigDecimal usdtTocny = getRedisUtilsService().getUsdtToCny();
            BigDecimal cny = FunctionUtils.mul(usdtPrice, usdtTocny, 2);
            dto.setCny(cny);

            String key = "maketdetail_" + coin;
            getRedisUtilsService().setKey(key, JSONObject.toJSONString(dto));
        }
    }

    private class DepthRunable implements Runnable{

        private String symbol;

        JSONObject json;

        public DepthRunable(String symbol, JSONObject json) {
            this.symbol = symbol;
            this.json = json;
        }

        @Override
        public void run() {
            String depth_coin = symbol.replace("usdt", "");
            String depth_key = depth_coin.toUpperCase() + "/USDT_depth";
            String depth_tick = json.getString("tick");
            Depth depth = JSONObject.parseObject(depth_tick, Depth.class);
            List<List<BigDecimal>> asks = depth.getAsks();
            asks =asks.subList(0,40);
            depth.setAsks(asks);
            List<List<BigDecimal>> bids = depth.getBids();
            bids = bids.subList(0,40);
            depth.setBids(bids);
            getRedisUtilsService().setKey(depth_key, JSONObject.toJSONString(depth));
        }
    }

    private class KlineRunable implements Runnable{

        private String symbol;

        JSONObject json;

        String times;

        public KlineRunable(String symbol, JSONObject json, String times) {
            this.symbol = symbol;
            this.json = json;
            this.times = times;
        }

        @Override
        public void run() {
            String kline_coin = symbol.replace("usdt", "");
            String kline_key = "kline_" + kline_coin.toUpperCase() + "/USDT_" + times;
            String kline_tick = json.getString("tick");
            KlineDto kline = JSONObject.parseObject(kline_tick, KlineDto.class);
            getRedisUtilsService().setKey(kline_key+"_current",JSONObject.toJSONString(kline));
            createKline(kline_key,kline);
        }
    }

    private void createKline(String kline_key, KlineDto kline) {
        String klineListStr = getRedisUtilsService().getKey(kline_key);
        if (klineListStr == null) {
            List<KlineDto> klines = new ArrayList<>();
            klines.add(kline);
            getRedisUtilsService().setKey(kline_key, JSONObject.toJSONString(klines));
        } else {
            boolean flag = false;
            List<KlineDto> klineList = JSONObject.parseArray(klineListStr, KlineDto.class);
            for (int i = 0; i < klineList.size(); i++) {
                KlineDto klineItem = klineList.get(i);
                if (klineItem.getId() == kline.getId()) {
                    klineList.set(i, kline);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                klineList.add(kline);
            }
            if (klineList.size() > 1500) {
//                klineList.sort(new Comparator<Kline>() {
//                    @Override
//                    public int compare(Kline o1, Kline o2) {
//                        return (int) (o1.getId() - o2.getId());
//                    }
//                });
                klineList.remove(klineList.get(0));
            }
            getRedisUtilsService().setKey(kline_key, JSONObject.toJSONString(klineList));
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

    /**
     * 注入redis
     *
     * @return
     */
    private RedisUtilsService getRedisUtilsService() {
        return SpringBeanFactoryUtils.getApplicationContext().getBean(RedisUtilsService.class);
    }

    public static void main(String[] args) {
        String a = "market.ethusdt.detail";
        String b = a.replace(".", ",");
        System.out.println(b);
        String[] chArr = b.split(",");
        System.out.println(chArr.length);
        String symbol = chArr[1];// 交易对 格式为btcusdt
        System.out.println(symbol);

    }
}
