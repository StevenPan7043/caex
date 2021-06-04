package com.pmzhongguo.ex.core.rediskey;

/**
 * @description: 购买定时释放redis key
 * @date: 2019-03-15 16:34
 * @author: 十一
 */
public class CurrencyLockReleaseKey extends RedisBaseKey{


    private static final int EXPIRE = 8 * 60 * 1000;
    private static final String FUNC_NAME = "currencylockrelease";

    public CurrencyLockReleaseKey(int expireTime, String funcName) {
        super(expireTime, funcName);
    }

    public CurrencyLockReleaseKey(String funcName) {
        super(funcName);
    }

    public static CurrencyLockReleaseKey currencyLockReleaseKey = new CurrencyLockReleaseKey(EXPIRE,FUNC_NAME);
}
