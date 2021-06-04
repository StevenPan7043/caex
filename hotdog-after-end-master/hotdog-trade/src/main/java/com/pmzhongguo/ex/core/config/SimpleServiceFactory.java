package com.pmzhongguo.ex.core.config;

import com.pmzhongguo.ex.business.service.IDataProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author jary
 * @creatTime 2019/11/20 10:09 AM
 */
public abstract class SimpleServiceFactory   {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 上下文
     */
    protected ServletContext servletContext;

    protected WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
    /**
     * 获取同步service
     * @param jediChannel
     * @return
     */
    public abstract IDataProcess getSimpleService(String jediChannel);
}
