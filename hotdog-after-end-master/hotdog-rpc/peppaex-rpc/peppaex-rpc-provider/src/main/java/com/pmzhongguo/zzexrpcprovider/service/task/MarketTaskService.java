/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/22 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.service.task;

import com.pmzhongguo.ex.business.resp.DepthResp;
import com.pmzhongguo.ex.business.resp.KLineResp;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.resp.TradeResp;

import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/22 17:36
 * @description：market interface
 * @version: $
 */
public interface MarketTaskService {
    /**
     * 执行market调度
     */
    void executeMarket();

    /**
     * K线生成
     *
     * @param symbol
     */
    void generateKLineResp(String symbol);

    /**
     * 获取ticker
     *
     * @param symbol
     * @return
     */
    TickerResp getTicker(String symbol);

    /**
     * 检测心跳
     *
     * @return
     */
    String heartBeat();

    /**
     * 获取ticker Map
     *
     * @return
     */
    Map<String, TickerResp> getTicker();

    /**
     * 获取depth Map
     *
     * @return
     */
    Map<String, DepthResp> getDepthResp();

    /**
     * 获取trade Map
     *
     * @return
     */
    Map<String, TradeResp> getTradeResp();

    /**
     * 获取深度数据
     *
     * @param symbol
     * @param step
     * @return
     */
    DepthResp getDepth(String symbol, Integer step);

    /**
     * 获取Trade数据
     *
     * @param symbol
     * @param size
     * @return
     */
    TradeResp getTrades(String symbol, Integer size);

    /**
     * 获取K线数据
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
