package com.pmzhongguo.ex.core.rediskey;

/**
 * @description: redis key的基类
 *               防止key重复和方便命名统一管理，key命名格式： 类名+冒号分隔+业务功能名称，如：RedisBaseKey:lock
 * @date: 2019-03-15 16:27
 * @author: 十一
 */
public class RedisBaseKey {

    /**
     * key过期时间，单位ms
     */
    private int expireTime;

    /**
     * 功能名称
     */
    private String funcName;

    public RedisBaseKey(String funcName) {
        this(0,funcName);
    }

    public RedisBaseKey(int expireTime, String funcName) {
        this.expireTime = expireTime;
        this.funcName = funcName;
    }

    public int getExpireSeconds() {
        return expireTime;
    }

    public String getKey() {
        String className = getClass().getSimpleName();
        return className + ":" + funcName;
    }
}
