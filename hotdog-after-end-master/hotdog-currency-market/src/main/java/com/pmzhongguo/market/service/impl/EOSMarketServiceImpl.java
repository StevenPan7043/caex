package com.pmzhongguo.market.service.impl;

import com.pmzhongguo.market.base.AbstractEosMarket;
import com.pmzhongguo.market.base.ZzexThreadFactory;
import com.pmzhongguo.market.constants.InitConstant;
import com.pmzhongguo.market.dto.EosMarketDetailDTO;
import com.pmzhongguo.market.dto.EosProTraderDTO;
import com.pmzhongguo.market.dto.RechargeMarketDTO;
import com.pmzhongguo.market.entity.FollowSymbol;
import com.pmzhongguo.market.entity.vo.BaseReqVo;
import com.pmzhongguo.market.service.EOSMarketService;
import com.pmzhongguo.market.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jary
 * @creatTime 2019/8/15 5:01 PM
 */
@Service
@Transactional
public class EOSMarketServiceImpl extends AbstractEosMarket implements EOSMarketService {
    ExecutorService createEOSMarketPool = Executors.newFixedThreadPool(8, new ZzexThreadFactory("createEOSMarket"));
    public static final String _USDT = "_USDT";
    public static final String _ETH = "_ETH";
    public static final String _BTC = "_BTC";
    public static final String _CNY = "_CNY";
    public static final Integer _jingdu = 6;


    @Override
    public BigDecimal getEosTickers() {
        String traderMarketResult = HttpUtils.get(InitConstant.EOS_USDT_CLOSE_URL, createMarketReq(), "UTF-8");
        if (BeanUtil.isEmpty(traderMarketResult)) {
            logger.warn("获取火币USDT汇率超时，eos_usdt_close_url:{}", InitConstant.EOS_USDT_CLOSE_URL);
            return usdt_price;
        }
        //获取基数价格
//        logger.warn("获取到火币网usdt价格结果是：{}", traderMarketResult);
        JSONObject jsonObject = JSONObject.fromObject(traderMarketResult);
        RechargeMarketDTO rechargeMarketDTO = JsonUtil.json2Bean(traderMarketResult, RechargeMarketDTO.class);
        if (!BeanUtil.isEmpty(rechargeMarketDTO.getCode()) && rechargeMarketDTO.getCode() == 200) {
            String dataVo = jsonObject.getString("data");
            List<EosProTraderDTO> eosProTraderDTOS = JsonUtil.jsonToList(dataVo, EosProTraderDTO.class);
            rechargeMarketDTO.setData(eosProTraderDTOS);
            usdt_price = new BigDecimal(eosProTraderDTOS.get(0).getPrice()).multiply(new BigDecimal(1));
            if (usdt_price.compareTo(BigDecimal.ZERO) <= 0) {
                usdt_price = new BigDecimal("6.851");
            }
        }
        contractMarket.put("USDT_CNY",usdt_price);
        BigDecimal[] bigDecimals = new BigDecimal[2];
        bigDecimals[0] = usdt_price;
        bigDecimals[1] = usdt_price;
        marketResp.put("USDT_CNY", bigDecimals);
        return usdt_price;
    }

    @Override
    public Map<String, BigDecimal[]> getEosMarket() {
        if (usdt_price.compareTo(BigDecimal.ZERO) <= 0) {
            this.getEosTickers();
        }
        BigDecimal[] bigDecimals = new BigDecimal[2];
        bigDecimals[0] = usdt_price;
        bigDecimals[1] = usdt_price;
        marketResp.put("USDT_CNY", bigDecimals);
        contractMarket.put("USDT_CNY",usdt_price);
//        String quotationResult = HttpUtils.get(InitConstant.EOS_SYMBOL_CLOSE_URL);
////              logger.warn("获取到火币网交易对汇率接口返回值是：{}", quotationResult);
//        if (StringUtils.isBlank(quotationResult)) {
//            logger.warn("获取火币网交易对汇率接口超时，eos_symbol_close_url:{}", InitConstant.EOS_SYMBOL_CLOSE_URL);
//            return marketResp;
//        }
//        Map<String, Object> map = new HashMap<>();
//        map.put("data", EosMarketDetailDTO.class);
//        BaseReqVo baseReqVo = JsonUtil.json2Bean(quotationResult, BaseReqVo.class,map);
//        if (BeanUtil.isEmpty(baseReqVo)) {
//            logger.warn("获取火币网交易对汇率接口超时，eos_symbol_close_url:{}", InitConstant.EOS_SYMBOL_CLOSE_URL);
//            return marketResp;
//        }
//        if (!baseReqVo.getStatus().equalsIgnoreCase("OK")) {
//            return marketResp;
//        }
//
//        List<EosMarketDetailDTO> eosMarketDetailDTOS = baseReqVo.getData();
//        if (!CollectionUtils.isEmpty(eosMarketDetailDTOS)) {
//            for (EosMarketDetailDTO eosItem : eosMarketDetailDTOS) {
//                createEOSMarketPool.execute(new CreateEOSMarketJob(eosItem));
//            }
//        }
//        //将山寨币缓存在内存中
//        setShanZhaiCoin();
        return marketResp;
    }


    /**
     * 山寨币设置
     */
    public void setShanZhaiCoin() {
        //将山寨币缓存在内存中
        Object symbolPriceObject = JedisUtil.getInstance().get(SYMBOL_PRICE, true);
        if (symbolPriceObject != null) {
            Map<String, Object> symbolPriceMap = com.alibaba.fastjson.JSONObject.parseObject(symbolPriceObject.toString());

            for (String symbolKey : symbolPriceMap.keySet()) {

                FollowSymbol followSymbol = JsonUtil.json2Bean(symbolPriceMap.get(symbolKey).toString(), FollowSymbol.class);
                if (contractMarket.get(followSymbol.getTargetSymbol()) == null) {
                    continue;
                }

                BigDecimal currTargetPrice = contractMarket.get(followSymbol.getTargetSymbol());
                if (followSymbol.getTargetPrePrice() == null || followSymbol.getTargetPrePrice().compareTo(BigDecimal.ZERO) == 0) {
                    followSymbol.setFollowPrePrice(followSymbol.getFollowStartPrice());
                    contractMarket.put(followSymbol.getFollowSymbol(), followSymbol.getFollowStartPrice());
                } else {
                    BigDecimal subtract = currTargetPrice.subtract(followSymbol.getTargetPrePrice()).divide(followSymbol.getTargetPrePrice(), _jingdu, BigDecimal.ROUND_HALF_UP);
                    BigDecimal upAndDownPrice = subtract.multiply(followSymbol.getMul()).setScale(_jingdu, BigDecimal.ROUND_HALF_UP);
                    BigDecimal followPrice = followSymbol.getFollowPrePrice() == null ?
                            followSymbol.getFollowStartPrice().add(followSymbol.getFollowStartPrice().multiply(upAndDownPrice).setScale(_jingdu, BigDecimal.ROUND_HALF_UP))
                            : followSymbol.getFollowPrePrice().add(followSymbol.getFollowPrePrice().multiply(upAndDownPrice).setScale(_jingdu, BigDecimal.ROUND_HALF_UP));
                    followSymbol.setFollowPrePrice(followPrice);
                    contractMarket.put(followSymbol.getFollowSymbol(), followPrice);
                }
                followSymbol.setTargetPrePrice(currTargetPrice);
                symbolPriceMap.put(symbolKey, followSymbol);
            }
            JedisUtil.getInstance().set(SYMBOL_PRICE, JsonUtil.bean2JsonString(symbolPriceMap), true);
        }
    }

    class CreateEOSMarketJob implements Runnable {
        private EosMarketDetailDTO emdd;

        public CreateEOSMarketJob(EosMarketDetailDTO emdd) {
            super();
            this.emdd = emdd;
        }

        @Override
        public void run() {
            createEosMarket(emdd);
        }

        private void createEosMarket(EosMarketDetailDTO emdd) {
            BigDecimal[] _cnyValue = new BigDecimal[2];
            BigDecimal[] _usdtValue = new BigDecimal[2];

            Map<String, BigDecimal> result = new HashMap<>();
            String symbol = emdd.getSymbol().toLowerCase();
            if (symbol.endsWith("cny")) {
                String baseCurrency = symbol.substring(0, symbol.indexOf("cny")).toUpperCase();
                BigDecimal bigDecimal = ThirdMarketUtil.setCNY(baseCurrency, emdd.getClose(), usdt_price);
                result.put(baseCurrency + _CNY, bigDecimal);
                _cnyValue[0] = bigDecimal;
                _cnyValue[1] = emdd.getOpen();
                marketResp.put(baseCurrency + _CNY, _cnyValue);

                //设置usdt开盘价、实时收盘价
                ThirdMarketUtil.setUSDT(baseCurrency, usdt_price, result);
                BigDecimal usdt_open = emdd.getOpen().divide(usdt_price, BigDecimal.ROUND_HALF_UP);
                _usdtValue[0] = result.get(baseCurrency + _USDT);
                _usdtValue[1] = usdt_open;
                marketResp.put(baseCurrency + _USDT, _usdtValue);
            } else if (symbol.endsWith("usdt")) {
                String baseCurrency = symbol.substring(0, symbol.indexOf("usdt")).toUpperCase();
                if (!result.containsKey(baseCurrency + _CNY)) {
                    //cny收盘价
                    result.put(baseCurrency + _CNY, ThirdMarketUtil.setCNY(baseCurrency, usdt_price, emdd.getClose()));
                    //cny开盘价
                    result.put(baseCurrency + "_CNY_OPEN", ThirdMarketUtil.setCNY(baseCurrency, usdt_price, emdd.getOpen()));
                    _cnyValue[0] = usdt_price.multiply(emdd.getClose());
                    _cnyValue[1] = usdt_price.multiply(emdd.getOpen());
                    marketResp.put(baseCurrency + _CNY, _cnyValue);
                }
                ThirdMarketUtil.setUSDT(baseCurrency, usdt_price, result);
                _usdtValue[0] = emdd.getClose();
                _usdtValue[1] = emdd.getOpen();
                marketResp.put(baseCurrency + _USDT, _usdtValue);
                contractMarket.put(baseCurrency + _USDT,emdd.getClose());
            } else if (symbol.endsWith("btc")) {
                if (symbol.startsWith("eth")) {
                    String baseCurrency = symbol.substring(0, symbol.indexOf("btc")).toUpperCase();
                    contractMarket.put(baseCurrency + _BTC, emdd.getClose());
                }
            }
        }
    }

    private Map<String, String> createMarketReq() {
        Map<String, String> mapReq = new HashMap<>();
        mapReq.put("country", 37 + "");
        mapReq.put("currency", 1 + "");
        mapReq.put("payMethod", 0 + "");
        mapReq.put("currPage", 1 + "");
        mapReq.put("coinId", 2 + "");
        mapReq.put("tradeType", "buy");
        mapReq.put("blockType", "general");
        mapReq.put("online", 1 + "");
        return mapReq;
    }
}
