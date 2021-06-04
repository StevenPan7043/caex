package com.pmzhongguo.ex.core.sms;


/**
 * @description: 短信接口
 * @date: 2019-05-30 15:36
 * @author: 十一
 */
public interface ISmsService extends Runnable{




    /**
     * 发送短信
     */
    void send();

    /**
     * 聚合发送国际短信
     */
    void juHeSend();

    /**
     * 发送短信参数构建
     * @param args 可变参数
     * @return
     */
    ISmsService builder(Object ...args);
}
