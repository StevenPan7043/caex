package com.contract.common.mail;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author jary
 * @creatTime 2020/3/13 8:45 AM
 */
public class HelpUtils {

    /**
     * 获得当前时间，并转换成yyyyMMddHHmmss格式
     *
     * @return String
     */
    public static String getCurrTime() {
        return formatDateByFormatStr(new Date(), "yyyyMMddHHmmss");
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

}
