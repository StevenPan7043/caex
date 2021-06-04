/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/14 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.model;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/14 11:23
 * @description：message
 * @version: $
 */
public class QuotationMessage extends BaseMessage {

    private Object data;

    public QuotationMessage(String msgType) {
        super(msgType);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
