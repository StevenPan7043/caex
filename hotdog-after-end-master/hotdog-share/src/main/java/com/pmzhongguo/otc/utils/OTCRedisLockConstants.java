package com.pmzhongguo.otc.utils;

public class OTCRedisLockConstants {
	
	//OTC用户资产操作
	public static final String OTC_ACCOUNT_REDIS_LOCK_PRE = "otc_account_redis_lock_pre_"; 
	public static final int OTC_ACCOUNT_REDIS_LOCK_EXPIRE_TIME = 2 * 1000;	// EXPIRE_TIME
	
	//OTC用户订单交易
	public static final String OTC_ORDER_TRADE_REDIS_LOCK_PRE = "otc_order_trade_redis_lock_pre_";
	public static final int OTC_ORDER_TRADE_REDIS_LOCK_EXPIRE_TIME = 1 * 1000;	// EXPIRE_TIME
	
	//OTC用户订单操作
	public static final String OTC_ORDER_CHANGE_REDIS_LOCK_PRE = "otc_order_change_redis_lock_pre_";
	public static final int OTC_ORDER_CHANGE_REDIS_LOCK_EXPIRE_TIME = 2 * 1000;	// EXPIRE_TIME
	
	//OTC用户撤销交易操作
	public static final String OTC_CANCEL_TRADE_JOB_LOCK_PRE = "otc_cancel_trade_job_lock_pre_"; 
	public static final int OTC_CANCEL_TRADE_JOB_LOCK_EXPIRE_TIME = 50 * 1000;	// EXPIRE_TIME
	
	//OTC用户撤销交易操作
	public static final String OTC_CANCEL_ORDER_JOB_LOCK_PRE = "otc_cancel_order_job_lock_pre"; 
	public static final int OTC_CANCEL_ORDER_JOB_LOCK_EXPIRE_TIME = 50 * 60 * 1000;	// EXPIRE_TIME
	
	//重试次数
	public static final int REDIS_LOCK_RETRY_10 = 10;
	public static final int REDIS_LOCK_RETRY_20 = 20;
	public static final int REDIS_LOCK_RETRY_30 = 30;
	
	//重试间隔时间
	public static final int REDIS_LOCK_INTERVAL_50 = 50;
	public static final int REDIS_LOCK_INTERVAL_100 = 100;
	public static final int REDIS_LOCK_INTERVAL_200 = 200;
}
