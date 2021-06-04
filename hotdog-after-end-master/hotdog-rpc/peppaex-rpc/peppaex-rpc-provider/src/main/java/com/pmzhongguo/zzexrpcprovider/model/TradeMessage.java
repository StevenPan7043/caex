/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/6 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pmzhongguo.ex.business.resp.TradeResp;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/6 16:57
 * @description：trade data
 * @version: $
 */
public class TradeMessage extends BaseMessage {

    private Long timestamp;

    private TradeResp data;

    public TradeMessage() {
        super(QuotationKeyConst.TOPIC_TRADE);
    }

    public String getMsgInfo(TradeResp tradeResp) {
        this.data = tradeResp;
        tradeResp.timestamp = timestamp;
        return JSON.toJSONString(this, SerializerFeature.WriteNonStringValueAsString, SerializerFeature.WriteNullListAsEmpty);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public TradeResp getData() {
        return data;
    }

    public void setData(TradeResp data) {
        this.data = data;
    }

}
