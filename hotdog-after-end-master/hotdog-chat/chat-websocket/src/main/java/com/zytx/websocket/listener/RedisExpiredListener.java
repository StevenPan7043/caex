package com.zytx.websocket.listener;

import com.zytx.common.cache.SocketCache;
import com.zytx.common.constant.RedisKeyConstant;
import com.zytx.common.constant.RespEnum;
import com.zytx.common.dto.ChatMessageDto;
import com.zytx.common.util.RespUtil;
import com.zytx.common.util.SendUtil;
import com.zytx.common.util.StringUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * redis过期事件监听
 */
@Slf4j
@Component
public class RedisExpiredListener extends KeyExpirationEventMessageListener {

    public RedisExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        log.warn("expiredKey:" + expiredKey);
        if (expiredKey.contains(RedisKeyConstant.CHAT_BAN)) {
            //对解禁用户发消息，告知解禁
            String id = expiredKey.split(RedisKeyConstant.CHAT_BAN)[1].substring(1);
            ChatMessageDto dto = new ChatMessageDto();
            dto.setId(Integer.parseInt(id));
            dto.setIsBan(0);

            String web_token = SocketCache.getToken("WEB_" + id);
            String app_token = SocketCache.getToken("APP_" + id);
            if (!StringUtil.isNullOrBank(web_token)) {
                SendUtil.send(SocketCache.getChannel(web_token), RespUtil.getSuccess(RespEnum.OUT_BAN, dto));
            }
            if (!StringUtil.isNullOrBank(app_token)) {
                SendUtil.send(SocketCache.getChannel(app_token), RespUtil.getSuccess(RespEnum.OUT_BAN, dto));
            }
        }
    }
}
