package com.contract.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.contract.common.HttpUtils;
import com.contract.dto.SymbolDto;
import com.contract.dto.Ticker;
import com.contract.entity.Coins;
import com.contract.service.redis.RedisUtilsService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

@Component
public class HuobiUtils {

    @Autowired
    private RedisUtilsService redisUtilsService;

    /**
     * 获取火币实时价格
     * @param coinname
     * @return
     */
    public SymbolDto getSymbolDto(String coinname) {
        //FIL
//        if (coinname.equals("FIL/USDT")) {
        SymbolDto   symbolDto = redisUtilsService.getSymbolDto(coinname);
//        }
//        else {
//            symbolDto = currencyScale(coinname);
//        }
        return symbolDto;
    }

    /**
     * 用于调用自己家币种行情接口
     * @param coinname
     * @return
     */
    public SymbolDto currencyScale(String coinname) {
        SymbolDto symbolDto = new SymbolDto();
        try {
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            InputStream in = HuobiUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(in);
            String usdt_price1 = properties.getProperty("usdt_price");
            String tickerStr = HttpUtils.get(usdt_price1 + timeInMillis);
            JSONObject jsonObject = JSON.parseObject(tickerStr).getJSONObject("data");
            if (coinname != null) {
                for (String key : jsonObject.keySet()) {
                    if (jsonObject.getJSONObject(key).getString("dspName").equals(coinname)) {
                        symbolDto.setCoin(jsonObject.getJSONObject(key).getString("baseCurrencyName"));
                        symbolDto.setName(jsonObject.getJSONObject(key).getString("dspName"));
                        symbolDto.setUnit(jsonObject.getJSONObject(key).getString("quoteCurrencyName"));
                        symbolDto.setSymbol(jsonObject.getJSONObject(key).getString("symbol").replace("usdt","_usdt"));
                        symbolDto.setUsdtPrice(jsonObject.getJSONObject(key).getBigDecimal("close"));
                        symbolDto.setCny(jsonObject.getJSONObject(key).getBigDecimal("cnyClose"));
                        symbolDto.setOpenVal(jsonObject.getJSONObject(key).getBigDecimal("open"));
                        symbolDto.setHigh(jsonObject.getJSONObject(key).getBigDecimal("high"));
                        symbolDto.setLow(jsonObject.getJSONObject(key).getBigDecimal("low"));
                        symbolDto.setVol(jsonObject.getJSONObject(key).getBigDecimal("volume"));
                        symbolDto.setSort(jsonObject.getJSONObject(key).getInteger("order"));
                        symbolDto.setType(0);
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return symbolDto;
    }
}


