/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/11 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcquartz.service.market;


import com.pmzhongguo.ex.business.resp.DepthResp;
import com.pmzhongguo.ex.business.resp.KLineResp;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.resp.TradeResp;

import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/11 14:52
 * @description：获取行情信息
 * @version: $
 */
public interface MarketApiService {

    /**
     * 检测服务是否启动
     *
     * @return
     */
    String heartBeat();

    /**
     * 获得行情汇总信息
     *
     * @param symbol
     * @return
     */
    TickerResp getTicker(String symbol);

    /**
     * 获得所有行情汇总信息
     *
     * @return
     */
    Map<String, TickerResp> getTickerResp();

    /**
     * 获取depth Resp
     *
     * @return
     */
    Map<String, DepthResp> getDepthResp();

    /**
     * 获取 trade Resp
     *
     * @return
     */
    Map<String, TradeResp> getTradeResp();

    /**
     * 获取Kline Resp
     *
     * @return
     */
//    Map<String, KLineResp> getKlineResp();

    /**
     * 获得深度数据
     *
     * @param symbol
     * @param step
     * @return
     */
    DepthResp getDepth(String symbol, Integer step);

    /**
     * 获得交易数据
     *
     * @param symbol
     * @param size
     * @return
     */
    TradeResp getTrades(String symbol, Integer size);

    /**
     * 获得K线数据
     *
     * @param symbol
     * @param type
     * @param size
     * @return
     */
    KLineResp getKLine(String symbol, String type, Integer size);

    /**
     * 获得K线数据 升序
     *
     * @param symbol
     * @param type
     * @param size
     * @return
     */
    KLineResp getKLineAsc(String symbol, String type, Integer size);
}
