package com.pmzhongguo.ipfs.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 获取SimpleDateFormat
     *
     * @param parttern 日期格式
     * @return SimpleDateFormat对象
     * @throws RuntimeException 异常：非法日期格式
     */
    private static SimpleDateFormat getDateFormat(String parttern) {
        SimpleDateFormat sdf = new SimpleDateFormat(parttern);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sdf;
    }

    /**
     * 将日期字符串转化为日期。失败返回null。
     *
     * @param date     日期字符串
     * @param parttern 日期格式
     * @return 日期
     */
    public static Date stringToDate(String date, String parttern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(parttern).parse(date);
            } catch (Exception e) {
            }
        }
        return myDate;
    }

    /**
     * 获取两个日期相差的天数
     *
     * @param date      日期字符串
     * @param otherDate 另一个日期字符串
     * @return 相差天数
     */
    public static int getIntervalDays(String date, String otherDate) {
        return getIntervalDays(stringToDate(date, YYYY_MM_DD), stringToDate(otherDate, YYYY_MM_DD));
    }

    /**
     * 获取两个日期相差的天数
     *
     * @param date      日期
     * @param otherDate 另一个日期
     * @return 相差天数
     */
    public static int getIntervalDays(Date date, Date otherDate) {
        long time = dateToDate(date, YYYY_MM_DD).getTime() - dateToDate(otherDate, YYYY_MM_DD).getTime();
        return (int) (time / (24 * 60 * 60 * 1000));
    }

    /**
     * Date +/- int = 新的Date
     *
     * @param inDate     Date 原日期
     * @param AddDateInt int 要加减的天数
     * @return ReturnDate Date 新的Date
     */
    public static Date dateAddInt(Date inDate, int AddDateInt) {
        Calendar currentC = Calendar.getInstance();
        currentC.setTime(inDate);
        currentC.add(Calendar.DAY_OF_YEAR, AddDateInt);
        return currentC.getTime();
    }

    /**
     * 把日期型转换成字符型
     *
     * @param inDate Date 需要转换的日期时间
     * @return outDateStr String
     */
    public static String dateToSimpleStr(Date inDate, String parttern) {
        String outDateStr = "";
        if (inDate != null) {
            outDateStr = getDateFormat(parttern).format(inDate);
        }
        return outDateStr;
    }

    /**
     * 把日期型转换成另一种日期格式
     *
     * @param inDate Date 需要转换的日期时间
     * @return outDateStr String
     */
    public static Date dateToDate(Date inDate, String parttern) {
        try {
            inDate = getDateFormat(parttern).parse(inDate.toString());
        } catch (Exception e) {
        }
        return inDate;
    }
}
