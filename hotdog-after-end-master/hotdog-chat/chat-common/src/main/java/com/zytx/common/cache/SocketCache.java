package com.zytx.common.cache;

import com.zytx.common.dto.UserDto;
import com.zytx.common.util.JWTUtil;
import com.zytx.common.util.StringUtil;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存数据
 */
public class SocketCache {
    /**
     * 存储通道标识。key:用户标识,value:通道
     */
    private final static Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 存储用户地址标识 key:地址,value:用户标识
     */
    private final static Map<String, String> addressMap = new ConcurrentHashMap<>();

    /**
     * 存储用户地址标识 key:地址,value:用户标识
     */
    private final static Map<String, UserDto> userMap = new ConcurrentHashMap<>();

    /**
     * 存储用户token和ID key:登录设备_id,value:token
     */
    private final static Map<String, String> tokenMap = new ConcurrentHashMap<>();

    /**
     * 保存用户标识到缓存
     *
     * @param channel
     * @param token
     */
    public static void saveCache(Channel channel, String token) {
        channelMap.put(token, channel);
        addressMap.put(channel.remoteAddress().toString(), token);
        Map<String, String> map = JWTUtil.parseToken(token);
        tokenMap.put(map.get("client") + "_" + map.get("id"), token);
    }

    /**
     * 保存在线用户信息
     *
     * @param token
     * @param userDto
     */
    public static void saveCache(String token, UserDto userDto) {
        userMap.put(token, userDto);
    }

    /**
     * 删除缓存
     *
     * @param channel
     */
    public static void delCache(Channel channel) {
        String remove = addressMap.remove(channel.remoteAddress().toString());
        channelMap.remove(remove);
        userMap.remove(remove);
        Map<String, String> map = JWTUtil.parseToken(remove);
        tokenMap.remove(map.get("client") + "_" + map.get("id"));
    }

    /**
     * 删除缓存
     *
     * @param client_id
     */
    public static void delCache(String client_id) {
        String token = tokenMap.remove(client_id);
        if (!StringUtil.isNullOrBank(token)) {
            Channel channel = channelMap.remove(token);
            userMap.remove(token);
            addressMap.remove(channel.remoteAddress().toString());
        }
    }

    /**
     * 获取通道
     *
     * @param token
     * @return
     */
    public static Channel getChannel(String token) {
        return channelMap.get(token);
    }

    /**
     * 获取用户标识
     *
     * @param channel
     * @return
     */
    public static String getToken(Channel channel) {
        return addressMap.get(channel.remoteAddress().toString());
    }

    /**
     * 获取用户标识
     *
     * @param client_id
     * @return
     */
    public static String getToken(String client_id) {
        return tokenMap.get(client_id);
    }

    public static UserDto getUser(String token) {
        return userMap.get(token);
    }

    public static Map<String, Channel> getChannelMap() {
        return channelMap;
    }

    public static Map<String, String> getAddressMap() {
        return addressMap;
    }

    public static Map<String, UserDto> getUserMap() {
        return userMap;
    }
}
