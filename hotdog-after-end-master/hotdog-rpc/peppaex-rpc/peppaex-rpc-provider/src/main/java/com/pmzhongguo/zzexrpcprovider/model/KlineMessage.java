/**
 * zzex.com Inc.
 * Copyright (c) 2019/7/6 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.pmzhongguo.ex.business.resp.KLineResp;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;

/**
 * @author ：yukai
 * @date ：Created in 2019/7/6 16:56
 * @description：kline data
 * @version: $
 */
public class KlineMessage extends BaseMessage {

    private KLineResp data;

    public KlineMessage() {
        super(QuotationKeyConst.TOPIC_KLINE);
    }

    public String getMsgInfo(KLineResp kLineResp) {
        this.data = kLineResp;
        return JSON.toJSONString(this, SerializerFeature.WriteNonStringValueAsString, SerializerFeature.WriteNullListAsEmpty);
    }

    public KLineResp getData() {
        return data;
    }

    public void setData(KLineResp data) {
        this.data = data;
    }
}
