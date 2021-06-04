/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/6 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pmzhongguo.ex.business.resp.DepthResp;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/6 16:55
 * @description：深度数据封装
 * @version: $
 */
public class DepthMessage extends BaseMessage {
    private Long timestamp;

    private DepthResp data;

    public DepthMessage() {
        super(QuotationKeyConst.TOPIC_DEPTH);
    }

    public String getMsgInfo(DepthResp depthResp) {
        this.data = depthResp;
        depthResp.timestamp = timestamp;
        return JSON.toJSONString(this, SerializerFeature.WriteNonStringValueAsString, SerializerFeature.WriteNullListAsEmpty);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public DepthResp getData() {
        return data;
    }

    public void setData(DepthResp data) {
        this.data = data;
    }
}
