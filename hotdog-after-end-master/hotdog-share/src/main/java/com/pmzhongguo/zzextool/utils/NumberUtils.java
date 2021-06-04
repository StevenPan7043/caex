/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/9 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.utils;

import java.util.regex.Pattern;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/9 18:01
 * @description：数字工具类
 * @version: $
 */
public class NumberUtils
{
    private static char[] numbersNo0 = ("1235678912356789").toCharArray();

    private static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");


    public static final String randomNumNo0(int length)
    {
        if (length < 1)
        {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++)
        {
            randBuffer[i] = numbersNo0[getRandom(15)];
        }
        return new String(randBuffer);
    }

    /**
     * 获得一个随机数
     *
     * @param maxRandom 随机数的取值访问
     * @return int
     */
    public static final int getRandom(int maxRandom)
    {
        return (int) ((1 - Math.random()) * maxRandom);
    }

    /**
     * 判断是否是整数
     * @param str
     * @return
     */
    public static boolean isIntegerNumber(String str){
        if (StringUtil.isNullOrBank(str)) {
            return false;
        }
        return NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 判断一个数有多少个小数，例如：10.002  返回的是3，表示有3个小数
     * @param num
     * @return 返回 -1 表示没有小数位数，例如：10.00 就会返回-1
     */
    public static int hasRadixPoint(String num) {
        int index = num.indexOf(".");
        if (index== -1) {
            return -1;
        }
        Double aDouble = Double.valueOf(num);
        String substring = aDouble.toString().substring(index + 1);
        if ("0".equals(substring)){
            return -1;
        }
        return substring.length();

    }

}
