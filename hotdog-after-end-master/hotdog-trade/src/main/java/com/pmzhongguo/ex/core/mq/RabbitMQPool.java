package com.pmzhongguo.ex.core.mq;

import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.otc.sms.SmsSendPool;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jary
 * @creatTime 2019/11/11 3:56 PM
 * RabbitMQ消息发送
 */
public class RabbitMQPool {

    private ExecutorService pool = Executors.newFixedThreadPool(4, new ZzexThreadFactory("RabbitMQSend"));

    private RabbitMQPool() {
    }

    private static class RabbitMQInstance {
        private static final RabbitMQPool instance = new RabbitMQPool();
    }

    public static RabbitMQPool getInstance() {
        return RabbitMQPool.RabbitMQInstance.instance;
    }

    public void send(Runnable runnable) {
        pool.execute(runnable);
    }
}


