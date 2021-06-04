package com.pmzhongguo.ex.core.rediskey;

/**
 * @description: 一定要写注释啊
 * @date: 2019-06-13 20:58
 * @author: 十一
 */
public interface RedisKeyTemplate {

    int REDIS_KEY_60_SEC = 60;

    int REDIS_KEY_300_SEC = 60*5;


    String OTC_ORDER_30_DONE_TRADE_KEY = "otcorder30donetrade";

    String OTC_ORDER_TOTAL_COMPLAIN_TRADE_KEY = "otcordertotalcomplaintrade";

    String OTC_ORDER_TOTAL_DONE_TRADE_KEY = "otcordertotaldonetrade";
}
