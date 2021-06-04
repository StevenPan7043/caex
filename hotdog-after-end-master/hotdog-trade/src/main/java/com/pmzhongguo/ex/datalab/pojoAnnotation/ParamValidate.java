package com.pmzhongguo.ex.datalab.pojoAnnotation;

import java.lang.annotation.*;

/**
 * @author jary
 * @creatTime 2018/12/20 10:20 AM
 */
@Documented
@Target(ElementType.FIELD)   //常量
@Inherited
@Retention(RetentionPolicy.RUNTIME )
public @interface ParamValidate {
//    /**
//     * 是否能为null
//     * @return
//     */
//    boolean isCanNull() default true;
//
//    /**
//     * 是否能为空字符串
//     * @return
//     */
//    boolean isCanEmpty() default true;
//
//    /**
//     * 是否能为0
//     * @return
//     */
//    boolean isCanZero() default true;

    /**
     * 必须参数
     * @return
     */
    boolean requrie() default true;
    /**
     * 字段含义
     * @return
     */
    String value();

    /**
     * 错误提示码
     * @return
     */
    String errorCode();
}
