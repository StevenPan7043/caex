package com.contract.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring 工具类
 * @author  FAGNLIAI
 */
public class SpringBeanFactoryUtils implements ApplicationContextAware {
    
    private static ApplicationContext appCtx;


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }

    /**
     * TODO: 获取ApplicationContext
     * @Auhor: RICK
     * @Date : 2016年11月1日
     */
    public static ApplicationContext getApplicationContext() {
        return appCtx;
    }

    /**
     * TODO: 这是一个便利的方法，帮助我们快速得到一个BEAN
     * @Auhor: RICK
     * @Date : 2016年11月1日
     */
    public static Object getBean(String beanName) {
        return appCtx.getBean(beanName);
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object getBean(Class classs){
        return  appCtx.getBean(classs);
    }
}
