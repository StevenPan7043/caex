package com.pmzhongguo.market.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/8/15 5:01 PM
 */
public interface EOSMarketService {
    /**
     * 获取usdt基础价格
     */
    BigDecimal getEosTickers();

    /**
     * 获取火币行情
     */
    Map<String, BigDecimal[]> getEosMarket();

    void setShanZhaiCoin();
}
