package com.zytx.common.util;

public class StringUtil {

    /**
     * 检查一个字符串是null还是空的
     *
     * @param param
     * @return boolean
     */
    public static boolean isNullOrBank(Object param) {
        return (param == null || param.toString().length() == 0 || param.toString().trim().equals("") || param.toString().trim().equalsIgnoreCase("null") || param
                .toString().trim().equals("undefined")) ? true : false;
    }
}
