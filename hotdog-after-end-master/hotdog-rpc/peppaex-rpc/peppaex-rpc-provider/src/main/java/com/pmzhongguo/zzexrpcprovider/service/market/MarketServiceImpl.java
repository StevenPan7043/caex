/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/11 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.service.market;

import com.alibaba.dubbo.config.annotation.Service;
import com.pmzhongguo.ex.business.resp.DepthResp;
import com.pmzhongguo.ex.business.resp.KLineResp;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.resp.TradeResp;
import com.pmzhongguo.zzexrpcprovider.service.task.MarketTaskService;
import com.pmzhongguo.zzexrpcquartz.service.market.MarketApiService;
import com.pmzhongguo.zzextool.consts.StaticConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/11 14:51
 * @description：获取行情信息
 * @version: $
 */
@Service(version = StaticConst.DUBBO_VERSION)
@Component
public class MarketServiceImpl implements MarketApiService {

    @Autowired
    private MarketTaskService marketTaskService;

    @Override
    public Map<String, TickerResp> getTickerResp() {
        return marketTaskService.getTicker();
    }

    @Override
    public Map<String, DepthResp> getDepthResp() {
        return marketTaskService.getDepthResp();
    }

    @Override
    public Map<String, TradeResp> getTradeResp() {
        return marketTaskService.getTradeResp();
    }

//    @Override
//    public Map<String, KLineResp> getKlineResp()
//    {
//        return marketTaskService.getKlineResp();
//    }


    @Override
    public String heartBeat() {
        return marketTaskService.heartBeat();
    }

    @Override
    public TickerResp getTicker(String symbol) {
        return marketTaskService.getTicker(symbol);
    }

    @Override
    public DepthResp getDepth(String symbol, Integer step) {
        return marketTaskService.getDepth(symbol.toLowerCase(), step);
    }

    @Override
    public TradeResp getTrades(String symbol, Integer size) {
        return marketTaskService.getTrades(symbol.toLowerCase(), size);
    }

    @Override
    public KLineResp getKLine(String symbol, String type, Integer size) {
        return marketTaskService.getKLine(symbol.toLowerCase(), type, size);
    }

    @Override
    public KLineResp getKLineAsc(String symbol, String type, Integer size) {
        return marketTaskService.getKLineAsc(symbol.toLowerCase(), type, size);
    }
}
