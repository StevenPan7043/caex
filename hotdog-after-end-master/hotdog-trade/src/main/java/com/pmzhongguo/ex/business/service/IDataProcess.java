package com.pmzhongguo.ex.business.service;


import javax.servlet.ServletContext;

/**
 * @author jary
 * @creatTime 2019/11/20 10:09 AM
 * 简单工厂
 */

public interface IDataProcess {

    /**
     * 数据同步
     */
    void dataSync(ServletContext servletContext);
}
