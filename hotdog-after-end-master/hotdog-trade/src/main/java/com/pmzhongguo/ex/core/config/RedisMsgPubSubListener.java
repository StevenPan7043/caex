package com.pmzhongguo.ex.core.config;

import com.pmzhongguo.ex.core.web.JedisChannelEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import redis.clients.jedis.JedisPubSub;

import javax.servlet.ServletContext;

/**
 * Created by denglinjie on 2016/6/29.
 */

@Slf4j
@Component
public class RedisMsgPubSubListener extends JedisPubSub {


    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public void unsubscribe(String... channels) {
        super.unsubscribe(channels);
    }

    @Override
    public void subscribe(String... channels) {
        super.subscribe(channels);
    }

    @Override
    public void psubscribe(String... patterns) {
        super.psubscribe(patterns);
    }

    @Override
    public void punsubscribe() {
        super.punsubscribe();
    }

    @Override
    public void punsubscribe(String... patterns) {
        super.punsubscribe(patterns);
    }

    @Override
    public void onMessage(String channel, String message) {
        log.warn("<===================================================================>");
        log.warn("<======redis监听到频道: 「{}」，receives message: {}.", channel, message);
        try {
            ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
            SimpleServiceFactoryHandler handler = new SimpleServiceFactoryHandler(servletContext);
            handler.monitorExecute(channel);
            syncSuccess(channel);
        } catch (Exception e) {
            syncFail(channel);
            e.printStackTrace();
            this.unsubscribe();
        }
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.warn("<==========频道【{}】已被监听,频道channel:【{}】", subscribedChannels, channel + "-" + JedisChannelEnum.getEnumByCode(channel).getCodeCn());
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        log.warn("<==========频道:【{}】已关闭,频道channel:【{}】", subscribedChannels, channel + "-" + JedisChannelEnum.getEnumByCode(channel).getCodeCn());
    }

    private void syncSuccess(String channel) {
        log.warn("频道:【{}】-【{}】同步成功。", channel, JedisChannelEnum.getEnumByCode(channel).getCodeCn());
    }

    private void syncFail(String channel) {
        log.warn("频道:【{}】-【{}】同步失败。", channel, JedisChannelEnum.getEnumByCode(channel).getCodeCn());
    }
}
