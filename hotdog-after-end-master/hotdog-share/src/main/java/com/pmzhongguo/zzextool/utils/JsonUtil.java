/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/8 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/8 19:05
 * @description：json util
 * @version: $
 */
public class JsonUtil
{
    /**
     * 私有构造器，防止类的实例化
     */
    private JsonUtil()
    {
        super();
    }

    /**
     * 将Object对象转化为json字符串
     *
     * @param object 要转化的java对象
     * @return String 转化后的json字符串
     */
    public static String beanToJson(Object object)
    {
        return JSON.toJSONString(object);
    }

    /**
     * 将json字符串转化为java对象
     *
     * @param json    json字符串
     * @param classes java对象类型
     * @return T 转化后的java对象
     */
    public static <T> T jsonToBean(String json, Class<T> classes)
    {
        return JSON.parseObject(json, classes);
    }

    /**
     * 将Object对象转化为json字符串并指定日期格式
     *
     * @param object     要转化的java对象
     * @param dateFormat 日期格式
     * @return String 转化后的json字符串
     */
    public static String beanToJson(Object object, String dateFormat)
    {
        return JSON.toJSONStringWithDateFormat(object, dateFormat,
                SerializerFeature.PrettyFormat);
    }


    /**
     * 将Map转换为对象
     *
     * @param paramMap
     * @param cls
     * @return
     */
    public static <T> T parseMap2Object(Map<String, Object> paramMap, Class<T> cls)
    {
        return JSONObject.parseObject(JSONObject.toJSONString(paramMap), cls);
    }

    /**
     * 将对象转Map
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T extends Object> Map<String, Object> parseObject2Map(T t)
    {
        Map<String, Object> params = new HashMap<String, Object>();

        Class<?> clazz = t.getClass();
        for (; clazz != Object.class; clazz = clazz.getSuperclass())
        {
            try
            {
                Field[] fields = clazz.getDeclaredFields();

                for (int j = 0; j < fields.length; j++)
                { // 遍历所有属性
                    String name = fields[j].getName(); // 获取属性的名字
                    Object value = null;

                    Method method = t.getClass()
                            .getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                    value = method.invoke(t);

                    if (value != null)
                    {
                        params.put(name, value);
                    }
                }
            } catch (Exception e)
            {
            }
        }
        return params;
    }

    /**
     * 判断字符串是否是json格式
     *
     * @param test
     * @return
     */
    public static boolean isJSONValid(String test)
    {
        try
        {
            JSONObject.parseObject(test);
        } catch (JSONException ex)
        {
            try
            {
                JSONObject.parseArray(test);
            } catch (JSONException ex1)
            {
                return false;
            }
        }
        return true;
    }
}
