/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/11 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.controller;

import com.pmzhongguo.ex.business.resp.DepthResp;
import com.pmzhongguo.ex.business.resp.KLineResp;
import com.pmzhongguo.ex.business.resp.TradeResp;
import com.pmzhongguo.zzexrpcprovider.service.task.MarketTaskServiceImpl;
import com.pmzhongguo.zzexrpcquartz.service.market.MarketApiService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/11 16:12
 * @description：测试行情数据
 * @version: $
 */
@Controller
@RequestMapping(value = "/market")
public class MarketController {

    @Autowired(required = false)
    private MarketApiService marketService;

    @RequestMapping(value = "/ticker")
    @ResponseBody
    public Object getTicker(String symbol) {
        if (StringUtils.isNotBlank(symbol)) {
            return marketService.getTicker(symbol);
        }
        return marketService.getTickerResp();
    }

    @RequestMapping(value = "depth")
    @ResponseBody
    public DepthResp getDepth(String symbol, Integer step) {
        return marketService.getDepth(symbol, step);
    }

    @RequestMapping(value = "trade")
    @ResponseBody
    public TradeResp getTrades(String symbol, Integer size) {
        return marketService.getTrades(symbol, size);
    }

    @RequestMapping(value = "kline")
    @ResponseBody
    public KLineResp getKLine(String symbol, String type, Integer size) {
        return marketService.getKLine(symbol, type, size);
    }

    @RequestMapping(value = "klineAsc")
    public KLineResp getKLineAsc(String symbol, String type, Integer size) {
        return marketService.getKLineAsc(symbol, type, size);
    }

    @RequestMapping(value = "getAllKline")
    @ResponseBody
    public Map<String, KLineResp> getAllKline() {
        return MarketTaskServiceImpl.getKlineResp();
    }

}
