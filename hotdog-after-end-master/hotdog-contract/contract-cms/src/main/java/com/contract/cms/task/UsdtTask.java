package com.contract.cms.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.contract.service.wallet.WalletService;

import common.Logger;

@Component
public class UsdtTask {

	private static Logger log = Logger.getLogger(UsdtTask.class);
	@Autowired
	private WalletService walletService;
	/**
	 * CRON表达式    含义 
	 * "0/5 * *  * * ? "		每5秒执行一次  
	 * "0 0 12 * * ?"    		每天中午十二点触发 
	 * "0 15 10 ? * *"    		每天早上10：15触发 
	 * "0 15 10 * * ?"    		每天早上10：15触发 
	 * "0 15 10 * * ? *"    	每天早上10：15触发 
	 * "0 15 10 * * ? 2005"    	2005年的每天早上10：15触发 
	 * "0 * 14 * * ?"    		每天从下午2点开始到2点59分每分钟一次触发 
	 * "0 0/5 14 * * ?"    		每天从下午2点开始到2：55分结束每5分钟一次触发 
	 * "0 0/5 14,18 * * ?"    	每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发 
	 * "0 0-5 14 * * ?"    		每天14:00至14:05每分钟一次触发 
	 * "0 10,44 14 ? 3 WED"    	三月的每周三的14：10和14：44触发 
	 * "0 15 10 ? * MON-FRI"    每个周一、周二、周三、周四、周五的10：15触发 
	 */
	

//	/**
//	 * usdt充值 OMNI
//	 */
//	@Scheduled(cron="0 0/5 * * * ?")
//	public void autoUsdtRecharge() {
//		walletService.autoUsdtRecharge();
//	}
//
//	/**
//	 * usdt充值 ECR20
//	 */
//	@Scheduled(cron="0 0/20 * * * ?")
//	public void autoECR20UsdtRecharge() {
//		walletService.autoECR20UsdtRecharge();
//	}
//
//	/*
//	 * 核查提币状态
//	 */
//	@Scheduled(cron="0 0/60 * * * ?")
//	public void autoUsdtAccount() {
//		walletService.autoUsdtAccount();
//	}
//
//	@Scheduled(cron="0 0/60 * * * ?")
//	public void autoUsd() {
//		walletService.autoUsd();
//	}
//
//	/**
//	 * 代币定时归集
//	 */
//	@Scheduled(cron="0 0 0/3 * * ?")
//	public void autoECR20GJ() {
//		log.info("定时同步ECR20代币归集开始");
//		walletService.autoECR20GJ();
//		log.info("定时同步ECR20代币归集结束");
//	}
}
