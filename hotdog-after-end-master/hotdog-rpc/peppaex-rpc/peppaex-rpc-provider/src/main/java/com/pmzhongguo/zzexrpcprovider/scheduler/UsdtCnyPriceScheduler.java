package com.pmzhongguo.zzexrpcprovider.scheduler;

import com.alibaba.fastjson.JSONObject;
import com.pmzhongguo.zzextool.annotation.MyScheduled;
import com.pmzhongguo.zzextool.basic.AbstractBaseTask;
import com.pmzhongguo.zzextool.utils.HttpUtil;
import com.pmzhongguo.zzextool.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @description: 获取价格
 * @date: 2019-12-25 21:35
 * @author: 十一
 */
@Component
public class UsdtCnyPriceScheduler extends AbstractBaseTask {

    private static Logger logger = LoggerFactory.getLogger(UsdtCnyPriceScheduler.class);

    @Value("${zzex.currencyMarketPricetUrl}")
    private String url;

    public static BigDecimal PRICE = new BigDecimal("6.96");


//    @MyScheduled(cron = "* */5 * * * ?")
    @MyScheduled(cron = "*/1 * * * * ?")
    public void executeTask() {
        JSONObject jsonObject = getCurrencyMarketPrice();
        if (jsonObject == null) {
            return;
        }
        try {
            PRICE = jsonObject.getBigDecimal("data");
        } catch (Exception e) {
            logger.error("获取 usdtcny 价格异常，请求URL：{}", url);
            e.printStackTrace();
        }
    }

    /**
     * 请求数据
     * @return
     */
    public  JSONObject getCurrencyMarketPrice() {
        String result = HttpUtil.get(url+ "?currencyPair=USDT_CNY");
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

    @Override
    protected void initTaskData() {
        this.poolSize = 1;
        this.threadNamePrefix = "UDSTPRICETask-TaskExecutor-";
    }
}
