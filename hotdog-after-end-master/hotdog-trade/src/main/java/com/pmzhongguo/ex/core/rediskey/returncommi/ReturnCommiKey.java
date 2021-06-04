package com.pmzhongguo.ex.core.rediskey.returncommi;

import com.pmzhongguo.ex.core.rediskey.RedisBaseKey;

/**
 * @description: 返佣redis key
 * @date: 2019-03-15 16:34
 * @author: 十一
 */
public class ReturnCommiKey extends RedisBaseKey{


    /**
     * 一个小时的时长
     */
    private static final int EXPIRE = 60 * 60 * 1000;

    private static final String FUNC_NAME = "lock";

    public ReturnCommiKey(int expireTime, String funcName) {
        super(expireTime, funcName);
    }

    public ReturnCommiKey(String funcName) {
        super(funcName);
    }

    public static ReturnCommiKey returnCommiKey = new ReturnCommiKey(EXPIRE,FUNC_NAME);
}