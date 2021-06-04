/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/8 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.model;

import java.io.Serializable;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/8 9:32
 * @description：基础消息model
 * @version: $
 */
public class BaseMessage implements Serializable {

    /**
     * 消息类型
     */
    private String msgType;

    /**
     *  0 表示第一次推送对象, 1 表示是第一次推送
     */
    private int isFirst;

    public BaseMessage(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }
}
