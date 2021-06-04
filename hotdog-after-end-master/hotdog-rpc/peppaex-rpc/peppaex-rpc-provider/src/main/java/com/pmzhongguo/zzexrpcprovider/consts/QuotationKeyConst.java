/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/4 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.consts;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.resp.DepthResp;
import com.pmzhongguo.ex.business.resp.KLineResp;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.resp.TradeResp;
import com.pmzhongguo.ex.framework.entity.FrmConfig;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/4 10:29
 * @description：行情服务键值定义
 * @version: $
 */
public class QuotationKeyConst {

    // 单条消息发送最大时间限制 (单位:milliseconds),超过最大延迟限制,断开重连,获取最新数据
    // 单条消息发送时间超过限定时间就不会再往队列添加新消息,避免网络堵塞或系统资源占用
    public static final Integer SENDTIMELIMIT = 5 * 1000;
    // 消息体最大限制: 超过限制不发送 (单位:bytes)
    public static final Integer BUFFERSIZELIMIT = 500 * 1024;
    // 全行情
    public static String TOPIC_MARKET = "market";
    // 实时行情主题
    public static String TOPIC_TICKER = "ticker";
    // 盘口深度主题
    public static String TOPIC_DEPTH = "depth";
    // 交易流水主题
    public static String TOPIC_TRADE = "trade";
    // 交易流水主题
    public static String TOPIC_KLINE = "kline";
    // 1分钟K线主题
    public static String TOPIC_K1M = "kline.1min";
    // 5分钟K线主题
    public static String TOPIC_K5M = "kline.5min";
    // 15分钟K线主题
    public static String TOPIC_K15M = "kline.15min";
    // 30分钟K线主题
    public static String TOPIC_K30M = "kline.30min";
    // 小时K线主题
    public static String TOPIC_KHOUR = "kline.60min";
    // 日K线主题
    public static String TOPIC_KDAY = "kline.1day";
    // 周K线主题
    public static String TOPIC_KWEEK = "kline.1week";
    // 月K线主题
    public static String TOPIC_KMONTH = "kline.1mon";
    // 深度单边显示数量
    public static int DEPTH_ONESIDE_NUM = 36;
    public static Map<String, Long> LAST_SEND_ID_TRADE_50 = Maps.newHashMap();
    // 本地计划任务用变量，任务执行完后保存到redis
    // 因只是计划任务用，所以要设为private不能直接取值
    // 需要时从redis里获取
    public static Map<String, DepthResp> _depthsMap = Maps.newHashMap();
    public static Map<String, TradeResp> _tradesMap = Maps.newHashMap();
    public static Map<String, KLineResp> _kLinesMap = Maps.newHashMap();
    public static Map<String, TickerResp> _tickersMap = Maps.newLinkedHashMap();
    //存放websocket对象
    public static Map<String, Set<WebSocketSession>> sessionConstMap = Maps.newConcurrentMap();

    //缓存sessionid
    public static Map<String, List<String>> WSID_MAP = Maps.newConcurrentMap();

    // 交易对名称集合
    public static List<String> PAIRS = Lists.newArrayList();

    // 缓存交易对
    public static List<CurrencyPair> currencyPairs = Lists.newArrayList();

    public static FrmConfig frmConfig = new FrmConfig();

    public static Map<String, Currency> currencysMap = Maps.newHashMap();

}
