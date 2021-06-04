package com.pmzhongguo.crowd.config.rediskey;

/**
 * @description: key 基类
 * @date: 2019-03-04 09:37
 * @author: 十一
 */
public abstract class BaseKey {

    private int expireSeconds;

    private String prefix;

    public BaseKey(String prefix) {
        this(0,prefix);
    }

    public BaseKey(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className + ":" + prefix;
    }
}
