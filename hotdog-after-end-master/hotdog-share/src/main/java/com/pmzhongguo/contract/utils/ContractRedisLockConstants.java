package com.pmzhongguo.contract.utils;

public class ContractRedisLockConstants {
	
	//划转资产用户资产操作
	public static final String CONTRACT_ACCOUNT_REDIS_LOCK_PRE = "contract_account_redis_lock_pre_";
	public static final int CONTRACT_ACCOUNT_REDIS_LOCK_EXPIRE_TIME = 2 * 1000;	// EXPIRE_TIME
	

	//重试次数
	public static final int REDIS_LOCK_RETRY_10 = 10;
	public static final int REDIS_LOCK_RETRY_20 = 20;
	public static final int REDIS_LOCK_RETRY_30 = 30;
	
	//重试间隔时间
	public static final int REDIS_LOCK_INTERVAL_50 = 50;
	public static final int REDIS_LOCK_INTERVAL_100 = 100;
	public static final int REDIS_LOCK_INTERVAL_200 = 200;
}
