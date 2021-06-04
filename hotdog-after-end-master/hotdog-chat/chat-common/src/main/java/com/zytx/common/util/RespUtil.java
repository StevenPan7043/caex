package com.zytx.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zytx.common.constant.RespEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 返回数据
 */
@Data
public class RespUtil implements Serializable {
    private static final long serialVersionUID = -6454501924900451109L;
    private Integer status;//返回状态
    private Object data;//数据
    private String desc;//描述


    public RespUtil(Integer status, Object data, String desc) {
        this.status = status;
        this.data = data;
        this.desc = desc;
    }


    public static String getSuccess(RespEnum resp, Object data, String desc) {
        RespUtil respUtil = new RespUtil(resp.getCode(), data, desc);
        return JSON.toJSONString(respUtil, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String getSuccess(RespEnum resp, Object data) {
        RespUtil respUtil = new RespUtil(resp.getCode(), data, resp.getDesc());
        return JSON.toJSONString(respUtil, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String getFail(RespEnum resp, Object data, String desc) {
        RespUtil respUtil = new RespUtil(resp.getCode(), data, desc);
        return JSON.toJSONString(respUtil, SerializerFeature.DisableCircularReferenceDetect);
    }

    public static String getFail(RespEnum resp, Object data) {
        RespUtil respUtil = new RespUtil(resp.getCode(), data, resp.getDesc());
        return JSON.toJSONString(respUtil, SerializerFeature.DisableCircularReferenceDetect);
    }

}
