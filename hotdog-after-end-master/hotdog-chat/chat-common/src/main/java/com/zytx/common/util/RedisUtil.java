package com.zytx.common.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * redis封装类
 */
@Service
@Log4j2
public class RedisUtil {

    /**
     * 多套系统，共用一个redis时，防止key重复
     */
    private final static String KEY_PREFIX = "contract_";

    @Autowired
    private volatile RedisTemplate<String, String> redisTemplate;

    /**
     * 秒级内做并发处理
     *
     * @param key
     * @param cacheSeconds
     * @return true 表示可以继续下面逻辑没有并发  false表示并发数据
     */
    public boolean setIncrSecond(String key, long cacheSeconds) {
        key = KEY_PREFIX + key;
        try {
            long count = redisTemplate.opsForValue().increment(key, 1);
            //此段代码出现异常则会出现死锁问题，key一直都存在
            if (count == 1) {
                //设置有效期X秒
                redisTemplate.expire(key, cacheSeconds, TimeUnit.SECONDS);
                return true;
            }
            //如果存在表示重复
            return false;
        } catch (Exception e) {
            log.error("redis加锁异常", e);
            //出现异常删除锁
            redisTemplate.delete(key);
            return true;
        }
    }

    /**
     * 获取唯一Id
     *
     * @param key
     * @param hashKey
     * @param delta   增加量（不传采用1）
     * @return
     */
    public Long incrementHash(String key, String hashKey, Long delta) {
        key = KEY_PREFIX + key;
        try {
            if (null == delta) {
                delta = 1L;
            }
            return redisTemplate.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {//redis宕机时采用uuid的方式生成唯一id
            int first = new Random(10).nextInt(8) + 1;
            int randNo = UUID.randomUUID().toString().hashCode();
            if (randNo < 0) {
                randNo = -randNo;
            }
            return Long.valueOf(first + String.format("%16d", randNo));
        }
    }

    /**
     * 设置无限期 键值对信息
     *
     * @param key
     * @param value
     */
    public void setKey(String key, String value) {
        key = KEY_PREFIX + key;
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置有限期 键值对信息
     *
     * @param key
     * @param value
     * @param second 秒
     */
    public void setKey(String key, String value, long second) {
        key = KEY_PREFIX + key;
        redisTemplate.opsForValue().set(key, value, second, TimeUnit.SECONDS);
    }


    /**
     * 根据键获取值
     *
     * @param key
     * @return
     */
    public String getKey(String key) {
        key = KEY_PREFIX + key;
        String val = redisTemplate.opsForValue().get(key);
        return val;
    }

    /**
     * 根据键获取值
     *
     * @param key
     * @return
     */
    public Set<String> getKeys(String key) {
        key = KEY_PREFIX + key;
        Set<String> val = redisTemplate.keys(key);
        return val;
    }

    /**
     * 批量删除
     *
     * @param keys
     */
    public void deleteKey(Set<String> keys) {
        for (String k : keys) {
            k = KEY_PREFIX + k;
            redisTemplate.delete(k);
        }
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void deleteKey(String key) {
        key = KEY_PREFIX + key;
        redisTemplate.delete(key);
    }


    /**
     * 加入聊天室
     */
    public void joinGroup(String key, String token) {
        key = KEY_PREFIX + key;
        redisTemplate.opsForSet().add(key, token);
    }

    /**
     * 退出聊天室
     */
    public void exitGroup(String key, String token) {
        key = KEY_PREFIX + key;
        redisTemplate.opsForSet().remove(key, token);
    }

    /**
     * 是否在聊天室
     *
     * @return
     */
    public Boolean isInGroup(String key, String token) {
        key = KEY_PREFIX + key;
        return redisTemplate.opsForSet().isMember(key, token);
    }

    /**
     * 根据键获取值
     *
     * @param key
     * @return
     */
    public Set<String> getSetKey(String key) {
        key = KEY_PREFIX + key;
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 根据键获取值
     *
     * @param key
     * @return
     */
    public Map getHashKey(String key) {
        key = KEY_PREFIX + key;
        Map entries = redisTemplate.opsForHash().entries(key);
        return entries;
    }

    /**
     * 删除hash的值
     *
     * @param key1
     * @param key2
     */
    public Long deleteHashKey(String key1, Object... key2) {
        key1 = KEY_PREFIX + key1;
        return redisTemplate.opsForHash().delete(key1, key2);
    }
}
