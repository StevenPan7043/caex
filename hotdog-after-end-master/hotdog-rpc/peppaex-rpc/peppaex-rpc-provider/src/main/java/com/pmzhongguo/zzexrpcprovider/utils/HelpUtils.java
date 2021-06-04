/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/8 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.utils;

import com.pmzhongguo.ex.business.entity.Currency;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/8 18:44
 * @description：公用工具类
 * @version: $
 */
public class HelpUtils {

    /**
     * CurrencyPair表存放到Session中的Key值
     */
    public final static String CURRENCYPAIRMAP = "CURRENCYPAIRMAP";
    public final static String CURRENCYPAIRLST = "CURRENCYPAIRLST";
    public final static String CURRENCYPAIRSYMBOL = "CURRENCYPAIRSYMBOL";
    public final static String CURRENCYPAIRSYMBOLSTR = "CURRENCYPAIRSYMBOLSTR";

    public final static String CURRENCYMAP = "CURRENCYMAP";
    public final static String CURRENCYLST = "CURRENCYLST";
    public final static String CURRENCY_IS_OTC_MAP = "CURRENCY_IS_OTC_MAP";
    public final static String CURRENCY_IS_OTC_LST = "CURRENCY_IS_OTC_LST";

    /**
     * MgrConfig表存放到Session中的Key值
     */
    public final static String MGRCONFIG = "MGRCONFIG";

    public static Map<String, Currency> getCurrencyMap() {
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        return (Map<String, Currency>) wac.getServletContext().getAttribute(CURRENCYMAP);
    }

    public static Map newHashMap(Object... args) {
        return toMap(args);
    }

    public static Map toMap(Object[] args) {
        Map map = new HashMap();
        for (int i = 1; i < args.length; i += 2) {
            map.put(args[i - 1], args[i]);
        }
        return map;
    }

    public static String formatDate8(Date myDate) {
        return formatDateByFormatStr(myDate, "yyyy-MM-dd HH:mm:ss");
    }

    public static String formatDateByFormatStr(Date myDate, String formatStr) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
        return formatter.format(myDate);
    }
}
