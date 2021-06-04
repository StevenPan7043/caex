package com.pmzhongguo.ex.core.utils;



import com.pmzhongguo.crowd.entity.CrowdJob;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * @description: 复制对象属性工具类,避免直接使用第三方工具类
 * @date: 2019-04-12 10:39
 * @author: 十一
 */
public class CopyObjAttributeUtil {


    /**
     *
     * @param map map
     * @param clazz 要转换的类
     * @return
     */
    public static Object map2Obj(Map<String, Object> map, Class<?> clazz) {
        if (map == null) {
            return null;
        }
        Object obj = null;
        try {
            obj = clazz.newInstance();

            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                String fieldName = field.getName();
                Object fieldObj = map.get(fieldName);
                if(fieldObj == null || HelpUtils.nullOrBlank(fieldObj)) {
                    continue;
                }
                field.setAccessible(true);
                Class<?> fieldClassType = field.getType();
                if (fieldClassType.equals(Integer.class)) {
                    field.set(obj, Integer.valueOf(String.valueOf(fieldObj).trim()));
                } else if(fieldClassType.equals(Long.class)) {
                    field.set(obj, Long.valueOf(String.valueOf(fieldObj).trim()));
                } else if(fieldClassType.equals(BigDecimal.class)) {
                    field.set(obj, new BigDecimal(fieldObj.toString()));
                }

                else {
                    field.set(obj, map.get(field.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("job_name","ceshi");
        map.put("id","1");
        CrowdJob crowdJob = new CrowdJob();
        crowdJob = (CrowdJob)map2Obj(map, CrowdJob.class);
        System.out.println(crowdJob.toString());
    }
}
