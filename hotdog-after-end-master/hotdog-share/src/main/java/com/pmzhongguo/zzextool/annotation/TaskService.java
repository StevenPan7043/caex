package com.pmzhongguo.zzextool.annotation;


import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/20 22:45
 * @description：task 注解定义，用于开启全部调度服务
 * @version: $
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface TaskService
{
    /**
     * task bean 名称
     * @return
     */
    String value();
}
