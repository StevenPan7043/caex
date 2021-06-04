package com.pmzhongguo.ex.business.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pmzhongguo.ex.api.controller.LockController;
import com.pmzhongguo.ex.business.resp.DepthResp;
import com.pmzhongguo.ex.business.resp.KLineResp;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.resp.TradeResp;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzexrpcquartz.service.market.MarketApiService;
import com.pmzhongguo.zzextool.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;

@Service
//@Transactional
public class MarketService extends BaseServiceSupport {

    private static Logger logger = LoggerFactory.getLogger(LockController.class);

    public static MarketApiService staticMarketApiService;

    @Autowired(required = false)
    public MarketApiService marketApiService;

    @PostConstruct
    private void init() {
        staticMarketApiService = this.marketApiService;
    }

    /**
     * 获得深度数据
     *
     * @param symbol
     * @param step
     * @return
     */
    public DepthResp getDepth(String symbol, Integer step) {
        DepthResp depthResp = staticMarketApiService.getDepthResp().get(symbol + "_" + step);
        if (depthResp == null) {
            logger.info("<=======================未获取到 Depth数据 {symbol: " + symbol + ", step: " + step + "}===============================>");
            depthResp = new DepthResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbol, DateUtils.getNowTimeStampMillisecond(), new BigDecimal[0][0], new BigDecimal[0][0]);
        }
        return depthResp;
    }

    /**
     * 获得交易数据
     *
     * @param symbol
     * @param size
     * @return
     */
    public TradeResp getTrades(String symbol, Integer size) {
        TradeResp tradeResp = staticMarketApiService.getTradeResp().get(symbol + "_" + size);
        if (tradeResp == null) {
            logger.info("<=======================未获取到 Trades数据 {symbol:" + symbol + ", size: " + size + "}===============================>");
            tradeResp = new TradeResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbol, DateUtils.getNowTimeStampMillisecond(), Lists.newArrayList());
        }
        return tradeResp;
    }

    /**
     * 获得K线数据
     *
     * @param symbol
     * @param type
     * @param size
     * @return
     */
    public KLineResp getKLine(String symbol, String type, Integer size) {
        KLineResp kLineResp = staticMarketApiService.getKLine(symbol, type, size);
        if (kLineResp == null) {
            logger.info("<=======================未获取到 KLine数据 {symbol: " + symbol + ", type: " + type + ", size: " + size + "}===============================>");
            kLineResp = new KLineResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbol, type, DateUtils.getNowTimeStampMillisecond(), Lists.newArrayList());
        }
        return kLineResp;
    }

    /**
     * 获得K线数据 升序
     *
     * @param symbol
     * @param type
     * @param size
     * @return
     */
    public KLineResp getKLineAsc(String symbol, String type, Integer size) {
        return staticMarketApiService.getKLineAsc(symbol, type, size);
    }

    /**
     * 获得行情汇总信息
     *
     * @param symbol
     * @return
     */
    public TickerResp   getTicker(String symbol) {
        TickerResp tickerResp = staticMarketApiService.getTicker(symbol);
        if (tickerResp == null) {
            logger.info("<=======================未获取到 Ticker数据 {symbol: " + symbol + "}===============================>");
            tickerResp = new TickerResp();
        }
        return tickerResp;
    }

    /**
     * 获得所有行情汇总信息
     *
     * @return
     */
    public static Map<String, TickerResp> getTicker() {
        Map<String, TickerResp> tickerRespMap = staticMarketApiService.getTickerResp();
        if (tickerRespMap == null) {
            logger.info("<=======================未获取到全部Ticker数据===============================>");
            tickerRespMap = Maps.newHashMap();
        }
        return tickerRespMap;
    }

}
