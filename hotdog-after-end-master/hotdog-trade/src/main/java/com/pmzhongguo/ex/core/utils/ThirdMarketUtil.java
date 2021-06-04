package com.pmzhongguo.ex.core.utils;

import com.pmzhongguo.ex.core.web.ThirdMarketEnum;
import com.qiniu.util.BeanUtil;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/6/20 10:40 AM
 */
public class ThirdMarketUtil {
    /**
     * 设置基础币种CNY价格
     *
     * @param baseCurrency
     * @param close
     * @param usdtPrice
     * @param resultMap
     */
    public static void setCNY(String baseCurrency, BigDecimal close, BigDecimal usdtPrice, Map<String, BigDecimal> resultMap) {
        resultMap.put(baseCurrency + "_CNY", close.multiply(usdtPrice));
    }

    public static BigDecimal setCNY(String baseCurrency, BigDecimal close, BigDecimal usdtPrice) {
        return close.multiply(usdtPrice);
    }
    /**
     * 设置基础币种USDT价格
     *
     * @param baseCurrency
     * @param usdtPrice
     * @param resultMap
     */
    public static void setUSDT(String baseCurrency, BigDecimal usdtPrice, Map<String, BigDecimal> resultMap) {
        if (String.valueOf(usdtPrice).equals("0")) {
            resultMap.put(baseCurrency + "_USDT", BigDecimal.ZERO);
        } else {
            resultMap.put(baseCurrency + "_USDT", resultMap.get(baseCurrency + "_CNY").divide(usdtPrice, 8, BigDecimal.ROUND_HALF_UP));
        }
    }

    /**
     * 设置基础币种ETH价格
     *
     * @param baseCurrency
     * @param resultMap
     */
    public static void setETH(String baseCurrency, Map<String, BigDecimal> resultMap) {
        if (BeanUtil.isEmpty(resultMap.get(ThirdMarketEnum.ETH_CNY.getSymbol())) || String.valueOf(resultMap.get(ThirdMarketEnum.ETH_CNY.getSymbol())).equals("0")) {
            resultMap.put(baseCurrency + "_ETH", BigDecimal.ZERO);
        } else {
            resultMap.put(baseCurrency + "_ETH", resultMap.get(baseCurrency + "_CNY").divide(resultMap.get(ThirdMarketEnum.ETH_CNY.getSymbol()), 8, BigDecimal.ROUND_HALF_UP));
        }


    }


    public static void setCNY(String baseCurrency, String symbolOne, String symbolTwo, Map<String, BigDecimal> resultMap) throws Exception {
        resultMap.put(baseCurrency + "_CNY", resultMap.get(symbolOne).multiply(resultMap.get(symbolTwo)));
    }

    public static void setUSDT(String baseCurrency, String quoteCurrency, BigDecimal usdtPrice, Map<String, BigDecimal> resultMap) {

    }
}
