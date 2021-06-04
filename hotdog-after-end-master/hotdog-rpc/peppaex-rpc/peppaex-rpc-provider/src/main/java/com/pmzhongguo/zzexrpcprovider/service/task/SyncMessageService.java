/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/4 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.service.task;

import com.alibaba.fastjson.JSON;
import com.pmzhongguo.ex.business.resp.DepthResp;
import com.pmzhongguo.ex.business.resp.KLineResp;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.resp.TradeResp;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;
import com.pmzhongguo.zzexrpcprovider.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/4 10:53
 * @description：异步消息发送服务
 * @version: $
 */
@Slf4j
@Component
public class SyncMessageService {

    /**
     * 发送深度数据
     *
     * @param symbol
     * @param dResp
     */
    public static void syncDepthMessage(String symbol, DepthResp dResp) {
        try {
            if (dResp == null) {
                return;
            }
            // 深度主题
            String topic = getDepthTopic(symbol);
            if (StringUtils.isEmpty(topic)) {
                return;
            }
            DepthMessage newMessage = new DepthMessage();

            // 根据币对进行depth推送
            DepthResp oldDresp = QuotationKeyConst._depthsMap.get(symbol + "_0");

            // 将旧数据和新数据时间戳去除再对比
            if (oldDresp == null) {
                newMessage.setData(depthFilter(dResp));
                sendMessage(topic, newMessage);
            } else {
                String newJson = newMessage.getMsgInfo(dResp);
                DepthMessage oldMessage = new DepthMessage();
                String oldJson = oldMessage.getMsgInfo(oldDresp);
                if (checkMessage(newJson, oldJson)) {
                    newMessage.setData(depthFilter(dResp));
                    sendMessage(topic, newMessage);
                }
            }
        } catch (Exception e) {
            log.error("depth数据发送异常: error " + e.getLocalizedMessage());
        }
    }

    /**
     * 发送ticker数据
     *
     * @param symbol
     * @param tResp
     */
    public static void syncTickerMessage(String symbol, TickerResp tResp) {
        try {
            if (tResp == null) {
                return;
            }
            // ticker主题
            String topic = getTickerTopic(symbol);
            if (StringUtils.isEmpty(topic)) {
                return;
            }
            TickerMessage newMessage = new TickerMessage();

            // 根据币对进行ticker推送
            TickerResp oldTresp = QuotationKeyConst._tickersMap.get(symbol + "_ticker");

            // 将旧数据和新数据时间戳去除再对比
            if (oldTresp == null) {
                newMessage.setData(tResp);
                sendMessage(topic, newMessage);
            } else {
                String newJson = newMessage.getMsgInfo(tResp);
                TickerMessage oldMessage = new TickerMessage();
                String oldJson = oldMessage.getMsgInfo(oldTresp);
                if (checkMessage(newJson, oldJson)) {
                    newMessage.setData(tResp);
                    sendMessage(topic, newMessage);
                }
            }
        } catch (Exception e) {
            log.error("ticker数据发送异常: error " + e.getLocalizedMessage());
        }
    }

    /**
     * 发送trade数据
     *
     * @param symbol
     */
    public static void syncTradeMessage(String symbol, int num) {
        try {
            // 成交主题
            String topic = getTradeTopic(symbol);
            if (StringUtils.isEmpty(topic)) {
                return;
            }
            if (num != 50) {
                return;
            }

            TradeMessage newMessage = new TradeMessage();

            TradeResp resp = QuotationKeyConst._tradesMap.get(symbol + "_" + num);
            if (resp.trades.size() == 0) {
                return;
            }
            Long tradeId = resp.trades.get(0).id;
            Long oldId = QuotationKeyConst.LAST_SEND_ID_TRADE_50.get(symbol);
            if (oldId == null) {
                // 发送保存Id
                newMessage.setData(resp);
                sendMessage(topic, newMessage);
                QuotationKeyConst.LAST_SEND_ID_TRADE_50.put(symbol, tradeId);
            } else {
                if (tradeId.longValue() != oldId.longValue()) {
                    // 发送保存最新Id
                    newMessage.setData(resp);
                    sendMessage(topic, newMessage);
                    QuotationKeyConst.LAST_SEND_ID_TRADE_50.put(symbol, tradeId);
                }
            }
        } catch (Exception e) {
            log.error("trade数据发送异常: error " + e.getLocalizedMessage());
        }
    }

    /**
     * 发送Kline数据
     *
     * @param symbol
     * @param type
     * @param num
     * @param kResp
     */
    public static void syncKlineMessage(String symbol, String type, int num, KLineResp kResp) {
        try {
            if (kResp == null) {
                return;
            }
            // k线主题
            String topic = getKlineTopic(symbol, type);
            if (StringUtils.isEmpty(topic)) {
                return;
            }
            // K线连上的时候推1000条数据，之后每次推送一条数据
            if (num != 1) {
                return;
            }

            KlineMessage newMessage = new KlineMessage();

            // 根据币对进行ticker推送
            KLineResp oldKresp = QuotationKeyConst._kLinesMap.get(symbol + "_" + type + "_" + num);

            // 将旧数据和新数据时间戳去除再对比
            if (oldKresp == null) {
                newMessage.setData(kResp);
                sendMessage(topic, newMessage);
            } else {
                String newJson = newMessage.getMsgInfo(kResp);
                KlineMessage oldMessage = new KlineMessage();
                String oldJson = oldMessage.getMsgInfo(oldKresp);
                if (checkMessage(newJson, oldJson)) {
                    newMessage.setData(kResp);
                    sendMessage(topic, newMessage);
                }
            }
        } catch (Exception e) {
            log.error("kline数据发送异常: error " + e.getLocalizedMessage());
        }
    }

    /**
     * 比较旧消息是否相等
     *
     * @param newMessage
     * @param oldMessage
     * @return
     */
    public static boolean checkMessage(String newMessage, String oldMessage) {
        if (StringUtils.isEmpty(newMessage)) {
            return false;
        }
        if (StringUtils.isEmpty(oldMessage)) {
            return true;
        }
        if (!oldMessage.equals(newMessage)) {
            return true;
        }
        return false;
    }

    /**
     * 比较旧消息是否相等
     *
     * @param newMessage
     * @param oldMessage
     * @return
     */
    public static boolean checkMessageObject(TradeMessage newMessage, TradeMessage oldMessage) {
        if (newMessage == null) {
            return false;
        }
        if (oldMessage == null) {
            return true;
        }
        if (!oldMessage.equals(newMessage)) {
            return true;
        }
        return false;
    }


    /**
     * 消息过滤，深度数据只显示买卖固定数量
     *
     * @param depthResp
     * @return
     */
    public static DepthResp depthFilter(DepthResp depthResp) {
        int saveLen = QuotationKeyConst.DEPTH_ONESIDE_NUM;
        DepthResp newResp = new DepthResp(depthResp.getState(), depthResp.getMsg(), depthResp.symbol, depthResp.timestamp, depthResp.bids, depthResp.asks);
        if (depthResp.bids != null && depthResp.bids.length > saveLen) {
            newResp.bids = new BigDecimal[saveLen][];
            // 截取二维数组的前36位
            for (int i = 0; i < saveLen; i++) {
                if (depthResp.bids[i] != null) {
                    newResp.bids[i] = depthResp.bids[i];
                }
            }
        }
        if (depthResp.asks != null && depthResp.asks.length > saveLen) {
            newResp.asks = new BigDecimal[saveLen][];
            // 截取二维数组的前36位
            for (int i = 0; i < saveLen; i++) {
                if (depthResp.asks[i] != null) {
                    newResp.asks[i] = depthResp.asks[i];
                }
            }
        }
        return depthResp;
    }


    // 发送消息

    /**
     * 单session发送消息
     *
     * @param session
     * @param channel
     */
    public static void sendMessageToOneSession(WebSocketSession session, String channel) {
        Integer index = channel.lastIndexOf(".");
        //前缀 depth. ticker. kline.1min. trade.
        String topic = channel.substring(0, index);
        //币对
        String symbol = channel.substring(index + 1);
        if (StringUtils.isAnyEmpty(topic, symbol)) {
            log.warn("初始化消息发送 channel 错误: " + channel);
            return;
        }
        Object obj = null;
        String msgType = topic;
        // ticker 数据
        if (topic.equals(QuotationKeyConst.TOPIC_TICKER)) {
            obj = QuotationKeyConst._tickersMap.get(symbol + "_ticker");
        }
        if (topic.equals(QuotationKeyConst.TOPIC_DEPTH)) {
            // 深度数据过滤只返回固定数量
            obj = depthFilter(QuotationKeyConst._depthsMap.get(symbol + "_0"));
        }
        if (topic.equals(QuotationKeyConst.TOPIC_TRADE)) {
            obj = QuotationKeyConst._tradesMap.get(symbol + "_" + 50);
        }
        if (topic.contains(QuotationKeyConst.TOPIC_KLINE)) {
            msgType = QuotationKeyConst.TOPIC_KLINE;
            String type = topic.substring(topic.indexOf(".") + 1);
            if (StringUtils.isEmpty(type)) {
                log.warn("接受的K线主题不正确: channel: " + channel);
                return;
            }
            obj = QuotationKeyConst._kLinesMap.get(symbol + "_" + type + "_" + 1000);
        }
        if (obj == null) {
            log.warn("初始化消息为空, channel: " + channel);
            return;
        }
        QuotationMessage newMessage = new QuotationMessage(msgType);
        newMessage.setIsFirst(1);
        newMessage.setData(obj);
        TextMessage textMessage = new TextMessage(JSON.toJSONBytes(newMessage));
        try {
            if (session.isOpen()) {
                session.sendMessage(textMessage);
            }
        } catch (IOException e) {
            log.error("[ 主题: " + channel + " sessionId: " + session.getId() + "] 消息发送失败！{}", e.getLocalizedMessage());
        }
    }

    /**
     * 向订阅过的session推送
     *
     * @param topic
     * @param obj
     */
    private static void sendMessage(String topic, Object obj) {
        if (obj == null || !QuotationKeyConst.sessionConstMap.containsKey(topic)) {
            return;
        }

        TextMessage textMessage = new TextMessage(JSON.toJSONBytes(obj));
        Set<WebSocketSession> sessionSet = QuotationKeyConst.sessionConstMap.get(topic);
        if (sessionSet != null && sessionSet.size() > 0) {
            // 推送数据
            Iterator<WebSocketSession> it = sessionSet.iterator();
            while (it.hasNext()) {
                WebSocketSession session = it.next();
                try {
                    if (session.isOpen()) {
                        log.info("发送消息 =====> 主题【" + topic + "】数据");
                        session.sendMessage(textMessage);
                    } else {
                        // 使用迭代器删除
                        it.remove();
                    }
                } catch (IOException e) {
                    log.error("[ 主题: " + topic + " sessionId: " + session.getId() + "] 消息发送失败！{}", e.getLocalizedMessage());
                }
            }
        }
    }

    // 获取主题
    public static String getDepthTopic(String symbol) {
        String topic = QuotationKeyConst.TOPIC_DEPTH + "." + symbol;
        return checkTopicExist(topic) ? topic : null;
    }

    public static String getTickerTopic(String symbol) {
        String topic = QuotationKeyConst.TOPIC_TICKER + "." + symbol;
        return checkTopicExist(topic) ? topic : null;
    }

    public static String getTradeTopic(String symbol) {
        String topic = QuotationKeyConst.TOPIC_TRADE + "." + symbol;
        return checkTopicExist(topic) ? topic : null;
    }

    public static String getKlineTopic(String symbol, String type) {
        String topic = QuotationKeyConst.TOPIC_KLINE + "." + type + "." + symbol;
        return checkTopicExist(topic) ? topic : null;
    }

    /**
     * 验证topic是否存在
     *
     * @param topic
     * @return
     */
    public static boolean checkTopicExist(String topic) {
        if (!QuotationKeyConst.sessionConstMap.containsKey(topic)) {
            return false;
        }
        return true;
    }


}
