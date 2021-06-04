package com.pmzhongguo.ex.core.mq;

/**
 * RabbitMQ接口
 *
 * @author jary
 * @creatTime 2019/11/11 3:58 PM
 */
public interface RabbitMQService extends Runnable {
    /**
     * 消息发送
     */
    void convertAndSend();

    /**
     * 实例化
     *
     * @param args
     * @return
     */
    RabbitMQService builder(Object... args);
}
