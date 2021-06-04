package com.contract.config;

import com.contract.common.DESEncode;
import com.contract.common.StringNetherSwap;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import javax.validation.constraints.NotNull;

/**
 * @author jary
 * @creatTime 2020/5/15 2:44 PM
 */
public class DescPropertyResourceConfigure extends PropertyPlaceholderConfigurer {
    @NotNull
    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isDescStr(propertyName)) {
            propertyValue = DESEncode.decrypt(propertyValue);
            propertyValue = StringNetherSwap.swap(propertyValue);
        }
        return propertyValue;
    }

    /**
     * 判断获取到的是不是指定的字段
     *
     * @param propertyName
     * @return
     */
    public static boolean isDescStr(String propertyName){
        boolean b = false;
        String user = "jdbc.user";
        String pwd = "jdbc.password";
        if (propertyName.indexOf(user) >= 0 ||
                propertyName.indexOf(pwd) >= 0){
            b =true;
        }
        return b;
    }


}
