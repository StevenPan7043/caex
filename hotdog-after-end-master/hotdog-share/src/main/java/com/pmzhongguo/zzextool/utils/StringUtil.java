/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/22 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.utils;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/22 14:47
 * @description：string util
 * @version: $
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils
{
    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s)
    {
        if (Character.isLowerCase(s.charAt(0)))
        {
            return s;
        } else
        {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转大写
     *
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s)
    {
        if (Character.isUpperCase(s.charAt(0)))
        {
            return s;
        } else
        {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 检查一个字符串是null还是空的
     *
     * @param param
     * @return boolean
     */
    public static boolean isNullOrBank(Object param)
    {
        return (param == null || param.toString().length() == 0 || param.toString().trim().equals("") || param.toString().trim().equalsIgnoreCase("null") || param
                .toString().trim().equals("undefined")) ? true : false;
    }
}
