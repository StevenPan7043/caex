package com.pmzhongguo.ex.core.mq;

/**
 * @author jary
 * @creatTime 2019/11/11 4:05 PM
 */
public class RabbitMQServiceImpl extends AbstractRabbitMQService {

    @Override
    public RabbitMQService builder(Object... args) {
        if (args == null) {
            return this;
        }
        routingKey = args[0].toString();
        msg = args[1];
        return this;
    }
}
