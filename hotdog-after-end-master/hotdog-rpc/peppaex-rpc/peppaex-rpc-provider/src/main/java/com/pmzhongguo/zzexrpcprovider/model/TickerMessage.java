/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/6 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/6 16:56
 * @description：ticker数据封装
 * @version: $
 */
public class TickerMessage extends BaseMessage {
    private Long timestamp;

    private TickerResp data;

    public TickerMessage() {
        super(QuotationKeyConst.TOPIC_TICKER);
    }

    public String getMsgInfo(TickerResp tickerResp) {
        this.data = tickerResp;
        tickerResp.timestamp = timestamp;
        return JSON.toJSONString(this, SerializerFeature.WriteNonStringValueAsString, SerializerFeature.WriteNullListAsEmpty);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public TickerResp getData() {
        return data;
    }

    public void setData(TickerResp data) {
        this.data = data;
    }
}
