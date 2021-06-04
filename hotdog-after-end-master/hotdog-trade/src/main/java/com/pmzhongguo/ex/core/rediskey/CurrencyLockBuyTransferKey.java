package com.pmzhongguo.ex.core.rediskey;

/**
 * @description: 购买锁仓划转redis key
 * @date: 2019-03-15 16:34
 * @author: 十一
 */
public class CurrencyLockBuyTransferKey extends RedisBaseKey{


    private static final int EXPIRE = 3 * 1000;
    private static final String FUNC_NAME = "currencyLockBuyTransferKey";


    public CurrencyLockBuyTransferKey() {
        super(EXPIRE,FUNC_NAME);
    }


    public String getRedisKey(String currency,int memberId) {
        return getKey() + ":" + currency + ":" + memberId;
    }
}
