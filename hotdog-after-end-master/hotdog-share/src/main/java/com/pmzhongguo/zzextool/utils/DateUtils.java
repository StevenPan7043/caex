/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/8 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/8 17:36
 * @description：日期工具类
 * @version: $
 */
public class DateUtils {
    private static final String YYYY_MM_DD_HHMM_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String YYYY_MM_DD_HHMMSS_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * @param dateStr
     * @param strFormat 字符串日期格式和当前时间对比判断相差多少秒,为空，默认是yyyy-MM-dd HH:mm
     * @return 返回相差的时间，单位秒
     */
    public static long differentDaysBySecond(String dateStr, String strFormat) {
        SimpleDateFormat yyyy_MM_dd_HHmm = new SimpleDateFormat(YYYY_MM_DD_HHMM_FORMAT);
        SimpleDateFormat yyyy_MM_dd_HHmmss = new SimpleDateFormat(YYYY_MM_DD_HHMMSS_FORMAT);
        Date date = null;
        try {
            if (null == strFormat) {
                strFormat = "yyyy-MM-dd HH:mm";
            }
            if (YYYY_MM_DD_HHMM_FORMAT.equals(strFormat)) {
                date = yyyy_MM_dd_HHmm.parse(dateStr);
            } else if (YYYY_MM_DD_HHMMSS_FORMAT.equals(strFormat)) {
                date = yyyy_MM_dd_HHmmss.parse(dateStr);
            }
            long time = System.currentTimeMillis() - date.getTime();
            return time / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getAfterSomeDayLongTime(int day) {
        long time = getNowTimeStampMillisecond() + (1000 * 60 * 60 * 24 * day);
        return time;

    }

    /**
     * 取得当前时间戳（精确到毫秒）
     *
     * @return nowTimeStamp
     */
    public static long getNowTimeStampMillisecond() {
        return System.currentTimeMillis();
    }

    /**
     * 取得当前时间戳（精确到秒）
     *
     * @return nowTimeStamp
     */
    public static String getNowTimeStamp() {
        long time = System.currentTimeMillis();
        String nowTimeStamp = String.valueOf(time / 1000);
        return nowTimeStamp;
    }

    /**
     * 从long型时间中获得小时
     *
     * @param t
     * @return int
     */
    public static int getHour(long t) {
        Calendar cld = Calendar.getInstance();
        if (t > 0) {
            cld.setTime(new Date(t));
        }
        return cld.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 从Date中获得小时
     *
     * @param date
     * @return int
     */
    public static int getHour(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 通过格式化字段来格式化日期
     *
     * @param myDate    输入的日期
     * @param formatStr 需要格式化的样式例如yyyy-M-D
     * @return 格式化后的日期类型
     */
    public static String formatDateByFormatStr(Date myDate, String formatStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        return formatter.format(myDate);
    }

    /**
     * 格式化日期 yyyy-MM-dd HH:mm:ss
     *
     * @param myDate
     * @return
     */
    public static String formatDate8(Date myDate) {
        return formatDateByFormatStr(myDate, YYYY_MM_DD_HHMMSS_FORMAT);
    }

}
