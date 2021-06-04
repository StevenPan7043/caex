package com.pmzhongguo.zzextool.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/20 22:45
 * @description：task 方法注解定义，用于管理类中定义的调度
 * @version: $
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface MyScheduled
{
    /**
     * 调度规则
     *
     * @return
     */
    String cron() default "";

    /**
     * 币种名称 大写
     * 默认为空, 为空的时候采用cron
     * 不为空的时候格式为    currency-field ： 根据币种名字查询币种信息, 根据字段名字获取对应的cron
     *
     * @return
     */
    String name() default "";
}
