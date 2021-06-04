package com.pmzhongguo.udun.util;

import com.alibaba.fastjson.JSON;
import com.pmzhongguo.ex.core.utils.PropertiesUtil;
import com.pmzhongguo.udun.constant.API;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UdunUtil {
    public static Logger logger = LoggerFactory.getLogger(UdunUtil.class);

    public static Map<String, Object> requestMap(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        params.put("callUrl", PropertiesUtil.getPropValByKey("udun_back_url"));//回调地址
        String time = "" + new Date().getTime();
        result.put("timestamp", time);
        result.put("nonce", "123456");
        String body = JSON.toJSONString(Arrays.asList(params));
        result.put("body", body);
        try {
            String sign = SignUtil.sign(API.API_KEY, time, "123456", body);
            result.put("sign", sign);
        } catch (Exception e) {
            logger.warn("签名失败");
            e.printStackTrace();
        }
        return result;
    }
}
