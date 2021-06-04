package com.pmzhongguo.market.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

import static net.sf.json.JSONObject.fromObject;
import static net.sf.json.JSONObject.toBean;

/**
 * @author jary
 * @creatTime 2018/11/30 10:22 AM
 */
public class JsonUtil {
    /**
     * List<T> 转 json 保存到数据库
     */
    public static <T> String listToJson(List<T> ts) {
        String jsons = JSON.toJSONString(ts);
        return jsons;
    }

    /**
     * bean 转Json字符串
     * @param ts
     * @return
     */
    public static String bean2JsonString(Object ts) {
        return new Gson().toJson(ts);
    }

    /**
     * json 转 List<T>
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        @SuppressWarnings("unchecked")
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }

    /**
     * 字符串转对象
     * @param jsonString
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonString, Class<T> tClass) {
        return (T)toBean(fromObject(jsonString), tClass);
    }

    /**
     * 字符串转对象
     * @param jsonString
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T json2Bean(String jsonString, Class<T> tClass, Map map) {
        return (T)toBean(fromObject(jsonString), tClass,map);
    }

//    //2、使用JSONArray
//    JSONArray jsonArray=JSONArray.fromObject(arrayStr);
//    //获得jsonArray的第一个元素
//    Object o=jsonArray.get(0);
//    JSONObject jsonObject2=JSONObject.fromObject(o);
//    Student stu2=(Student)JSONObject.toBean(jsonObject2, Student.class);
//        System.out.println("stu:"+stu);
//        System.out.println("stu2:"+stu2);

}
