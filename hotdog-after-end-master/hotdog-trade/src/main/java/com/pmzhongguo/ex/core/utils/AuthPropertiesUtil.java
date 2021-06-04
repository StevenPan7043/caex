package com.pmzhongguo.ex.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Properties;

/**
 * @Created by 张众
 * @Date 2019/12/5 9:37
 * @Description
 */
public class AuthPropertiesUtil {

    private static Logger logger = LoggerFactory.getLogger(MqPropertiesUtil.class);

    private static ResourceLoader resourceLoader = new DefaultResourceLoader();
    /**
     * 这个properties是专门存放server.properties中的变量的
     */
    private static Properties properties = null;

    private static Properties tempProperties = null;

    public AuthPropertiesUtil(String ... resourcesPaths)
    {
        tempProperties = loadProperties(resourcesPaths);
    }

    static {
        // 默认加载服务器ip配置文件
        properties = MqPropertiesUtil.loadProperties("../auth.properties");
    }
    /**
     * 打印所有的key-value
     *
     * @param props
     */
    private static void loadAllProperty(Properties props) {
        Enumeration en = props.propertyNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String value = props.getProperty(key);
            logger.info("加载配置文件的值======> " + key + " : " + value);
        }
    }

    /**
     * 加载指定路径配置文件
     *
     * @param filePath
     */
    public static Properties loadProperties(String filePath) {
        Properties prop = null;
        try {
            prop = PropertiesLoaderUtils.loadAllProperties(filePath);
            loadAllProperty(prop);
        }catch (IOException e) {
            logger.error("读取配置文件失败：" + e.getMessage());
        }
        return prop;
    }

    /**
     * 载入多个文件, 文件路径使用Spring Resource格式.
     */
    private Properties loadProperties(String ... resourcesPaths)
    {
        Properties props = new Properties();
        for (String location : resourcesPaths)
        {
            InputStream is = null;
            try
            {
                Resource resource = resourceLoader.getResource(location);
                is = resource.getInputStream();
                props.load(is);
                is.close();
            }
            catch (IOException ex)
            {
                logger.warn( "Could not load properties from path:" + location + ", " + ex.getMessage());
            }
        }
        return props;
    }

    /**
     * 取出Property，但以System的Property优先,取不到返回空字符串.
     */
    private String getValue(String key)
    {
        String systemProperty = System.getProperty(key);
        if (systemProperty != null) { return systemProperty; }
        if (tempProperties.containsKey(key)) { return tempProperties.getProperty(key); }
        return "";
    }

    /**
     * 取出String类型的Property，但以System的Property优先,如果都为Null则抛出异常.
     */
    public String getProperty(String key)
    {
        String value = getValue(key);
        if (value == null) { throw new NoSuchElementException(); }
        return value;
    }

    public static String getPropValByKey(String key) {
        return properties.getProperty(key);
    }
    public static void reloadProp() {
        properties = MqPropertiesUtil.loadProperties("../auth.properties");
    }


}
