package com.pmzhongguo.ipfs.constant;

import java.math.BigDecimal;

/**
 * @author Daily
 * @date 2020/9/1 14:21
 */
public class CONSTANT {

    //100
    public static final BigDecimal BIGDECIMAL_ONE_HUNDRED = new BigDecimal(100);

    //
    public static final BigDecimal INTRODUCER_HASHRATE = new BigDecimal(3000);

    //会员购买达到一定数量后折扣
    public static final BigDecimal BUY_DISCOUNT_NINE_HASHRATE = new BigDecimal(15);
    public static final BigDecimal BUY_DISCOUNT_NINE = new BigDecimal(0.9);
    public static final BigDecimal BUY_DISCOUNT_EIGHT_HASHRATE = new BigDecimal(50);
    public static final BigDecimal BUY_DISCOUNT_EIGHT = new BigDecimal(0.8);
    public static final BigDecimal BUY_DISCOUNT_SEVEN_HASHRATE = new BigDecimal(100);
    public static final BigDecimal BUY_DISCOUNT_SEVEN = new BigDecimal(0.7);

    public static final BigDecimal FROZEN_DAY = new BigDecimal(180);//分红冻结天数
    public static final BigDecimal LOCK_DAY = new BigDecimal(90);//算力分红封存天数
    public static final BigDecimal KJ_LOCK_DAY = new BigDecimal(180);//矿机分红封存天数
}
