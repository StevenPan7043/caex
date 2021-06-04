package com.pmzhongguo.ex.business.scheduler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pmzhongguo.zzextool.utils.HttpUtil;
import com.pmzhongguo.zzextool.utils.PropertiesUtil;
import com.pmzhongguo.zzextool.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @description: 获取价格
 * @date: 2019-12-25 21:35
 * @author: 十一
 */
@Component
@PropertySource("classpath:spring/rebate.properties")
public class UsdtCnyPriceScheduler {

    private static Logger logger = LoggerFactory.getLogger(UsdtCnyPriceScheduler.class);

    @Value("${usdt_cny_url}")
    private  String url;

    public static BigDecimal PRICE = new BigDecimal("6.96");


    @Scheduled(cron = "0/30 * * * * ?")
    public void refreshPrice() {
        JSONObject jsonObject = getCurrencyMarketPrice();
        if (jsonObject == null) {
            return;
        }
        JSONArray bigdecimas = jsonObject.getJSONArray("USDT_CNY");

        BigDecimal usdtCny = bigdecimas.getBigDecimal(0);
        if(StringUtil.isNullOrBank(usdtCny)) {
            return;
        }
        PRICE = usdtCny;
    }

    /**
     * 请求数据
     * @return
     */
    public  JSONObject getCurrencyMarketPrice() {
        String result = HttpUtil.get(url);
        if (StringUtil.isNullOrBank(result)) {
            logger.error("获取usdtcny价格失败，请求 url: {}",url);
            return null;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(result);
            return jsonObject;
        }catch (Exception e) {
            logger.error("行情价格转换失败");
            e.printStackTrace();
        }
        return null;
    }

}
