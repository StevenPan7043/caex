package com.pmzhongguo.market.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/7/13 8:48 AM
 */
public class AbstractEosMarket  {

    protected static Logger logger = LoggerFactory.getLogger(AbstractEosMarket.class);

    /**
     * usdt与cny的汇率
     */
    protected static BigDecimal usdt_price = BigDecimal.ZERO;

    /**
     * 设置价格key
     */
    protected static final String SYMBOL_PRICE = "SYMBOL_PRICE_MAP";

    public static Map<String, BigDecimal[]> marketResp = new HashMap<>();
    public static Map<String, BigDecimal> contractMarket = new HashMap<>();

}
