/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/29 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.basic;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pmzhongguo.ex.business.entity.Currency;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/29 19:05
 * @description：钱包系统参数
 * @version: $
 */
public class WalletConst {


    public static final String guijiCron = "-guijiCron";

    public static final String autoConfirmCron = "-autoConfirmCron";

    public static final String rechargeCron = "-rechargeCron";

    public static final String addressCron = "-addressCron";

    public static final String withdrawCron = "-withdrawCron";

    public static final String num = "-num";

    public static final String createNum = "-createNum";

    public static final String status = "-status";

    /**
     * 自动到账salt
     */
    public static final String SALT = "zzex_auto_confirm";

    /**
     * 地址预存少于
     */
    public static final ConcurrentMap<String, Integer> numMap = Maps.newConcurrentMap();

    /**
     * 一次性创建地址数量
     */
    public static final ConcurrentMap<String, Integer> createNumMap = Maps.newConcurrentMap();

    /**
     * 币种执行状态
     */
    public static final ConcurrentMap<String, Integer> statusMap = Maps.newConcurrentMap();
    public static String CONTROLLERPASSWORD = "123456";
    public static String USDTFEE = "0.0008";

    /**
     * 保存cron
     */
    public static ConcurrentMap<String, String> cronPool = Maps.newConcurrentMap();

    /**
     * 保存币种Map
     * key : 合约地址
     * value : 币种信息
     */
    public static ConcurrentMap<String, Currency> currencyMap = Maps.newConcurrentMap();
    /**
     * 保存币种Map
     * key : 币种
     * value : 币种信息
     */
    public static ConcurrentMap<String, Currency> currencyMapTwo = Maps.newConcurrentMap();

    /**
     * 获取币种集合
     */
    public static List<Currency> currencyList = Lists.newArrayList();

    /**
     * ETH 手续费地址
     */
    public static final String eth_feeAddress = "--";

    public static void modifyCurrencyInfo(List<Currency> currencyListObj) {
        // 赋值全局 currencyList
        WalletConst.currencyList = currencyListObj;
        // 赋值全局 currencyMap
        for (Currency currency : currencyListObj) {
            if (StringUtils.isNotEmpty(currency.getTokenaddr()) && currency.getIs_in_eth() == 1) {
                WalletConst.currencyMap.put(currency.getTokenaddr().toLowerCase(), currency);
            }
            WalletConst.currencyMapTwo.put(currency.getCurrency(), currency);
        }
    }

}
