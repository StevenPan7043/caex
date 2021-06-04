package com.pmzhongguo.crowd.config.support;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;



/**
 * @description: 异步或线程中注入无法获得Bean，手动获取Bean
 * @date: 2019-04-17 11:27
 * @author: 十一
 */
public class BeanContext implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        BeanContext.context = context;
    }
    //根据bean name 获取实例
    public static Object getBeanByName(String beanName) {
        if (beanName == null || context == null) {
            return null;
        }
        return context.getBean(beanName);
    }
    //只适合一个class只被定义一次的bean（也就是说，根据class不能匹配出多个该class的实例）
    public static Object getBeanByType(Class clazz) {
        if (clazz == null || context == null) {
            return null;
        }
        return context.getBean(clazz);
    }
    public static String[] getBeanDefinitionNames() {
        return context.getBeanDefinitionNames();
    }

}
