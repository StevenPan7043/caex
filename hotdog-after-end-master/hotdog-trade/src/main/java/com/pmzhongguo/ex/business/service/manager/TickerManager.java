package com.pmzhongguo.ex.business.service.manager;

import com.alibaba.fastjson.JSONObject;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.resp.TickerIncluded;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.scheduler.SymbolTickerScheduler;
import com.pmzhongguo.ex.business.service.MarketService;
import com.pmzhongguo.ex.core.utils.*;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2020/5/14 11:12 AM
 */
@Component
public class TickerManager {
    private static Logger logger = LoggerFactory.getLogger(TickerManager.class);
    public static Map<String, Object> SYMBOL_TICKER_MAP = new HashedMap();
//    public Map<String, Object> symbolTicker() {
//        String tickerStr = HttpUtils.get("http://ppex.info/m/allticker/312312321");
//        JSONObject jsonObject = JSONObject.parseObject(tickerStr);
//        List<TickerIncluded> tickerIncludedList = new ArrayList<>();
//        if ((jsonObject.get("state") + "").equals("1")) {
//            List<CurrencyPair> currencyPairLst = HelpUtils.getCurrencyPairLst();
//            for (CurrencyPair currencyPair : currencyPairLst) {
//                String currencyPariName = currencyPair.getKey_name().toLowerCase() + "_ticker";
//                JSONObject data = jsonObject.getJSONObject("data");
//                if (data.get(currencyPariName) != null) {
//                    String symbolTicker = data.getString(currencyPariName);
//                    TickerResp tickerResp = JsonUtil.fromJson(symbolTicker, TickerResp.class);
//                    TickerIncluded tickerIncluded = new TickerIncluded();
//                    tickerIncluded.setSymbol(currencyPair.getBase_currency().toLowerCase() + "_" + currencyPair.getQuote_currency().toLowerCase());
//                    tickerIncluded.setBuy((tickerResp.bis1 ==null || tickerResp.bis1.length == 0) ? BigDecimal.ZERO : tickerResp.bis1[0]);
//                    tickerIncluded.setHigh(tickerResp.high);
//                    tickerIncluded.setLast(tickerResp.close);
//                    tickerIncluded.setLow(tickerResp.low);
//                    tickerIncluded.setSell((tickerResp.ask1 ==null || tickerResp.ask1.length == 0) ? BigDecimal.ZERO : tickerResp.ask1[0]);
//                    if (tickerResp.close.compareTo(BigDecimal.ZERO) == 0) {
//                        tickerIncluded.setChange(BigDecimal.ZERO);
//                    } else {
//                        tickerIncluded.setChange((tickerResp.close.subtract(tickerResp.open)).divide(tickerResp.close, 4, BigDecimal.ROUND_HALF_UP));
//                    }
//                    tickerIncluded.setVol(tickerResp.volume);
//                    tickerIncludedList.add(tickerIncluded);
//                }
//            }
//            SYMBOL_TICKER_MAP.put("data", System.currentTimeMillis() / 1000);
//            SYMBOL_TICKER_MAP.put("ticker", tickerIncludedList);
//        }
//        return SYMBOL_TICKER_MAP;
//    }

    public Map<String, Object> symbolTicker() {
        Map<String, TickerResp> ticker = MarketService.getTicker();
        if (ticker == null || ticker.size() <= 0) {
//            logger.warn("交易对行情收录值allticker为空");
            return SYMBOL_TICKER_MAP;
        }
        List<TickerIncluded> tickerIncludedList = new ArrayList<>();

        List<CurrencyPair> currencyPairLst = HelpUtils.getCurrencyPairLst();
        for (CurrencyPair currencyPair : currencyPairLst) {
            String currencyPariName = currencyPair.getKey_name().toLowerCase() + "_ticker";
            TickerResp tickerResp = ticker.get(currencyPariName);
            if (tickerResp == null || tickerResp.getpStatus() != 1) {
                continue;
            }
            TickerIncluded tickerIncluded = new TickerIncluded();
            tickerIncluded.setSymbol(currencyPair.getBase_currency().toLowerCase() + "_" + currencyPair.getQuote_currency().toLowerCase());
            tickerIncluded.setBuy((tickerResp.bis1 ==null || tickerResp.bis1.length == 0) ? BigDecimal.ZERO : tickerResp.bis1[0]);
            tickerIncluded.setHigh(tickerResp.high);
            tickerIncluded.setLast(tickerResp.close);
            tickerIncluded.setLow(tickerResp.low);
            tickerIncluded.setSell((tickerResp.ask1 ==null || tickerResp.ask1.length == 0) ? BigDecimal.ZERO : tickerResp.ask1[0]);
            if (tickerResp.close.compareTo(BigDecimal.ZERO) == 0) {
                tickerIncluded.setChange(BigDecimal.ZERO);
            } else {
                tickerIncluded.setChange((tickerResp.close.subtract(tickerResp.open)).divide(tickerResp.close, 4, BigDecimal.ROUND_HALF_UP));
            }
            tickerIncluded.setVol(tickerResp.volume);
            tickerIncludedList.add(tickerIncluded);
        }

        SYMBOL_TICKER_MAP.put("data", System.currentTimeMillis() / 1000);
        SYMBOL_TICKER_MAP.put("ticker", tickerIncludedList);
        return SYMBOL_TICKER_MAP;
    }
}
