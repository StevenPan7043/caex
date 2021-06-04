package com.pmzhongguo.ex.core.mq;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * @author jary
 * @creatTime 2019/11/11 4:02 PM
 */
public abstract class AbstractRabbitMQService implements RabbitMQService {


    /**
     * 交换机名称
     */
    public static final String KLINE_NAME = "user_account_exchange";

    /**
     * 路由键
     */
    protected String routingKey;

    /**
     * 消息体
     */
    protected Object msg;

    @Override
    public void convertAndSend() {

    }


    @Override
    public void run() {
        convertAndSend();
    }
}
