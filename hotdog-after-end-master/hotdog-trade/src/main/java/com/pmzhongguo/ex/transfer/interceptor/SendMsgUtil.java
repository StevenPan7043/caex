/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/17 All Rights Reserved.
 */
package com.pmzhongguo.ex.transfer.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/17 14:57
 * @description：发送消息
 * @version: $
 */
public class SendMsgUtil
{

    protected static Logger logger = LoggerFactory.getLogger(SendMsgUtil.class);

    /**
     * 将某个对象转换成json格式并发送到客户端
     *
     * @param response
     * @param obj
     * @throws Exception
     */
    public static void sendJsonMessage(HttpServletResponse response, Object obj)
    {
        try
        {
            response.reset();
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.print(JSONObject.toJSONString(obj, SerializerFeature.WriteMapNullValue,
                    SerializerFeature.WriteDateUseDateFormat));
            writer.close();
            response.flushBuffer();
            return;
        } catch (IOException e)
        {
            logger.error(String.format("SendMsgUtil = cause: {%s} , message: {%s}, localizedMessage: {%s}", e.getCause(), e.getMessage(), e.getLocalizedMessage()));
        }
    }
}
