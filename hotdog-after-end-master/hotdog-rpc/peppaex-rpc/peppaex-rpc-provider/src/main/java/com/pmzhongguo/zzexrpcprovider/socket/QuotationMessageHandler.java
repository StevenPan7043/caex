/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/4 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.socket;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;
import com.pmzhongguo.zzexrpcprovider.model.APISocketMessage;
import com.pmzhongguo.zzexrpcprovider.service.task.SyncMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/4 10:05
 * @description：行情websocket消息接收器
 * @version: $
 */
@Slf4j
@Component
public class QuotationMessageHandler extends BinaryWebSocketHandler {

    static String[] CHANNELS = {
            // 深度
            QuotationKeyConst.TOPIC_DEPTH,
            // 行情
            QuotationKeyConst.TOPIC_TICKER,
            // 交易
            QuotationKeyConst.TOPIC_TRADE,
            // 1min kline
            QuotationKeyConst.TOPIC_K1M,
            // 5min kline
            QuotationKeyConst.TOPIC_K5M,
            // 15min kline
            QuotationKeyConst.TOPIC_K15M,
            // 30min kline
            QuotationKeyConst.TOPIC_K30M,
            // 1hour kline
            QuotationKeyConst.TOPIC_KHOUR,
            // day kline
            QuotationKeyConst.TOPIC_KDAY,
            // week kline
            QuotationKeyConst.TOPIC_KWEEK,
            // month kline
            QuotationKeyConst.TOPIC_KMONTH
    };

    /**
     * 消息订阅
     *
     * @param session
     * @param message
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) {
        APISocketMessage socketMessage = getAPIMessage(message.getPayload().toString());
        if (null == socketMessage) {
            return;
        }
        String channel = socketMessage.getChannel();

        // 消息订阅
        Boolean flag = Subscibe(session, channel);
        if (!flag) {
            return;
        }
        // 发送初始化消息
        SyncMessageService.sendMessageToOneSession(session, channel);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("sessionId: " + session.getId() + " 已连接");
        super.afterConnectionEstablished(session);
    }

    /**
     * 连接断开
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        cleanSession(session, status);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        cleanSession(session, CloseStatus.SESSION_NOT_RELIABLE);
    }

    // -------------------------------------------------------------------------------------------------------------------------

    /**
     * 清除session
     *
     * @param session
     * @param status
     * @throws IOException
     */
    void cleanSession(WebSocketSession session, CloseStatus status) throws IOException {
        String sessionId = session.getId();
        List<String> topics = QuotationKeyConst.WSID_MAP.get(sessionId);
        if (CollectionUtils.isEmpty(topics)) {
            return;
        }
        WebSocketSession obj = getWebSocketSessionFromCache(session.getId());
        if (obj == null) {
            QuotationKeyConst.WSID_MAP.remove(sessionId);
            return;
        }
        WebSocketSession webSocketSession = (ConcurrentWebSocketSessionDecorator) obj;
        for (String channelTopic : topics) {
            if (!QuotationKeyConst.sessionConstMap.containsKey(channelTopic)) {
                continue;
            }
            Set<WebSocketSession> sessionSet = QuotationKeyConst.sessionConstMap.get(channelTopic);
            sessionSet.remove(webSocketSession);
            QuotationKeyConst.sessionConstMap.put(channelTopic, sessionSet);
            log.info("session:" + webSocketSession.getId() + "取消消息:" + channelTopic + "订阅！！");
        }
        QuotationKeyConst.WSID_MAP.remove(webSocketSession.getId());

        if (session.isOpen()) {
            webSocketSession.close(status);
        }
    }

    // 消息订阅
    protected Boolean Subscibe(WebSocketSession session, String channelTopic) {
        //校验channel规则
        if (!checkChannel(channelTopic)) {
            return false;
        }
        //根据当前订阅的主题删除同类型主题session


        Set<WebSocketSession> webSocketSessionSet;
        // 1. 取出订阅主题的Session
        if (!QuotationKeyConst.sessionConstMap.containsKey(channelTopic)) {
            webSocketSessionSet = Sets.newConcurrentHashSet();
        } else {
            webSocketSessionSet = QuotationKeyConst.sessionConstMap.get(channelTopic);
        }

        if (QuotationKeyConst.WSID_MAP.containsKey(session.getId())) {
            String sessionId = session.getId();
            session = getWebSocketSessionFromCache(sessionId);
            if (session == null) {
                QuotationKeyConst.WSID_MAP.remove(sessionId);
                return false;
            }
            webSocketSessionSet.add(session);
        } else {
            webSocketSessionSet.add(new ConcurrentWebSocketSessionDecorator(session, QuotationKeyConst.SENDTIMELIMIT, QuotationKeyConst.BUFFERSIZELIMIT));
        }

        // 判断session是否订阅过其他主题
        List<String> topics = QuotationKeyConst.WSID_MAP.get(session.getId());
        if (CollectionUtils.isEmpty(topics)) {
            topics = Lists.newArrayList();
        }

        String tmpTopic = null;
        for (String topic : topics) {
            if (topic.contains(channelTopic.substring(0, channelTopic.indexOf(".")))) {
                tmpTopic = topic;
                QuotationKeyConst.sessionConstMap.get(topic).remove(session);
            }
        }

        // 添加session
        QuotationKeyConst.sessionConstMap.put(channelTopic, webSocketSessionSet);
        if (!topics.contains(channelTopic)) {
            topics.add(channelTopic);
            topics.remove(tmpTopic);
        }
        QuotationKeyConst.WSID_MAP.put(session.getId(), topics);
        log.info("session: " + session.getId() + "【" + channelTopic + "】订阅成功！！");
        return true;
    }


    /**
     * 将文件消息转换成对象消息
     *
     * @param content
     * @return
     */
    protected APISocketMessage getAPIMessage(String content) {
        if (StringUtils.isBlank(content)) {
            return null;
        }
        APISocketMessage message = null;
        try {
            message = JSON.parseObject(content, APISocketMessage.class);
        } catch (RuntimeException e) {
            log.info("message:" + content + " convent fail!");
        }
        return message;
    }

    /**
     * 校验channel规则
     *
     * @param channel
     * @return
     */
    Boolean checkChannel(String channel) {
        if (StringUtils.isEmpty(channel)) {
            return false;
        }
        Integer index = channel.lastIndexOf(".");
        //前缀 depth. ticker. kline.1min. trade.
        String aboveItem = channel.substring(0, index);
        //币对
        String behindItem = channel.substring(index + 1);
        //判断当前币对是否开放行情
        if (!QuotationKeyConst.PAIRS.contains(behindItem)) {
            return false;
        }
        //判断channel是否开放
        if (!Arrays.asList(CHANNELS).contains(aboveItem)) {
            return false;
        }
        return true;
    }

    /**
     * 为了解决高并发WebSocketSession 而引入 ConcurrentWebSocketSessionDecorator
     * 所有缓存session和关闭session时都调用该方法
     *
     * @param sessionId
     * @return
     */
    private WebSocketSession getWebSocketSessionFromCache(String sessionId) {
        WebSocketSession ws = null;
        //取出该缓存订阅的主题
        if (QuotationKeyConst.WSID_MAP.containsKey(sessionId)) {
            List<String> topics = QuotationKeyConst.WSID_MAP.get(sessionId);
            for (String topic : topics) {
                // 根据当前订阅的主题消息刷选已经订阅的主题并清除
                Set<WebSocketSession> wsSessionSets = QuotationKeyConst.sessionConstMap.get(topic);
                for (WebSocketSession session2 : wsSessionSets) {
                    if (sessionId.equals(session2.getId())) {
                        ws = session2;
                        break;
                    }
                }
                if (ws != null) {
                    break;
                }
            }
        }

        return ws;
    }

}
