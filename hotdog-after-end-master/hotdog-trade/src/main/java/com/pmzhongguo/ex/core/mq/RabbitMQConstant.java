package com.pmzhongguo.ex.core.mq;

/**
 * @author jary
 * @creatTime 2019/11/11 4:56 PM
 */
public class RabbitMQConstant {
    /**
     * 保存资产详情交换机名称
     */
    public static final String USER_ACCOUNT_EXCHANGE_NAME = "user_account_exchange";
    /**
     * 保存kline和trade的交换机名称交换机名称
     */
    public static final String KLINE_AND_TRADE_EXCHANGE_NAME = "kline_exchange";
    /**
     * 用户资产明细路由键
     */
    public static final String ACCOUNT_KEY = "account_routing_details_key";
    /**
     * otc资产路由键
     */
    public static final String OTC_ACCOUNT_KEY = "otc_account_details_routing_key";
    /**
     * 消息发送失败时将消息存入redis的key
     */
    public static final String KLINE_AND_TRADE_ERROR_REDIS_KEY = "kline_and_trade_message_error";
    /**
     * 消息发送失败时将消息存入redis的key
     */
    public static final String ACCOUNT_ERROR_REDIS_KEY = "account_message_error";
    /**
     * 消息发送失败时将消息存入redis的key
     */
    public static final String OTC_ACCOUNT_ERROR_REDIS_KEY = "otc_message_error";
}
