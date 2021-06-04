package com.pmzhongguo.zzexrpcprovider.utils;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.framework.entity.FrmConfig;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;
import com.pmzhongguo.zzexrpcprovider.service.currency.CurrencyService;
import com.pmzhongguo.zzexrpcprovider.service.framework.FrmUserService;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import com.pmzhongguo.zzextool.utils.DaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;

/**
 * Created by denglinjie on 2016/6/29.
 */

@Slf4j
@Component
public class RedisMsgPubSubListener extends JedisPubSub {

    @Autowired
    public DaoUtil daoUtil;
    @Autowired
    protected CurrencyService currencyService;
    @Autowired
    protected FrmUserService frmUserService;


    @Override
    public void unsubscribe() {
        super.unsubscribe();
    }

    @Override
    public void unsubscribe(String... channels) {
        super.unsubscribe(channels);
    }

    @Override
    public void subscribe(String... channels) {
        super.subscribe(channels);
    }

    @Override
    public void psubscribe(String... patterns) {
        super.psubscribe(patterns);
    }

    @Override
    public void punsubscribe() {
        super.punsubscribe();
    }

    @Override
    public void punsubscribe(String... patterns) {
        super.punsubscribe(patterns);
    }

    @Override
    public void onMessage(String channel, String message) {
        log.warn("<======频道:" + channel + "receives message :" + message);
        try {
            if (JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_PAIR.equals(channel)) {
                // 交易对
                QuotationKeyConst.currencyPairs = currencyService.getCurrencyPairLst();
                QuotationKeyConst.PAIRS.clear();
                for (CurrencyPair currencyPair : QuotationKeyConst.currencyPairs) {
                    QuotationKeyConst.PAIRS.add(currencyPair.getKey_name().toLowerCase());
                }
                log.warn("交易对数据已同步" + QuotationKeyConst.PAIRS.size());
            } else if (JedisChannelConst.JEDIS_CHANNEL_SYNC_CONFIG.equals(channel)) {
                //系统配置
                FrmConfig mgrConfig = frmUserService.findConfig();
                QuotationKeyConst.frmConfig = mgrConfig;
            } else if (JedisChannelConst.JEDIS_CHANNEL_CURRENCY.equals(channel)) {
                QuotationKeyConst.currencysMap = (Map<String, Currency>) currencyService.getCurrencyMap().get("retMap");
            }
            log.warn("<======redis数据同步成功");
        } catch (Exception e) {
            log.error("同步失败,channel = " + channel + "错误:" + e.getMessage());
            this.unsubscribe();
        }
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {

    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        log.warn("频道:" + channel + "已被监听:" + subscribedChannels);
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println("频道:" + channel + "已关闭:" + subscribedChannels);
    }
}
