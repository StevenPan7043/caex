package com.pmzhongguo.ex.business.scheduler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.TradeStatusEnum;
import com.pmzhongguo.otc.service.OTCTradeManager;
import com.pmzhongguo.otc.utils.OTCRedisLockConstants;

@Component
public class OTCExpireTradeScheduler {

	protected Logger logger = LoggerFactory.getLogger(OTCExpireTradeScheduler.class);
	
//	ExecutorService cancelTradepool_15 = Executors.newFixedThreadPool(4, new ZzexThreadFactory("cancelTradepool_15"));
//	ExecutorService cancelTradepool_30 = Executors.newFixedThreadPool(4, new ZzexThreadFactory("cancelTradepool_30"));
//	ExecutorService cancelTradepool_45 = Executors.newFixedThreadPool(4, new ZzexThreadFactory("cancelTradepool_45"));
	
	@Autowired
	private DaoUtil daoUtil;
	
	@Autowired
	private OTCTradeManager oTCTradeManager;
	
	private static String format = "SELECT id FROM `o_trade` t WHERE t.`payment_time` = ? AND t.`t_type` = ? AND t.`create_time` < ? and t.status = ?";
	
//	@Scheduled(cron = "0 */1 * * * ?")
//	public void cancelTrade15() {
//		cancelTrade(15, cancelTradepool_15);
//	}
//	
//	@Scheduled(cron = "0 */1 * * * ?")
//	public void cancelTrade30() {
//		cancelTrade(30, cancelTradepool_30);
//	}
//	
//	@Scheduled(cron = "0 */1 * * * ?")
//	public void cancelTrade45() {
//		cancelTrade(45, cancelTradepool_45);
//	}
	
	private void cancelTrade(int payment_time, ExecutorService cancelTradepool) {
		
		String lockKey = OTCRedisLockConstants.OTC_CANCEL_TRADE_JOB_LOCK_PRE + payment_time;
		//任务1m执行一次，锁的有效时间设为50s
		boolean isLock = JedisUtil.getInstance().getLock(lockKey, IPAddressPortUtil.IP_ADDRESS,
				OTCRedisLockConstants.OTC_CANCEL_TRADE_JOB_LOCK_EXPIRE_TIME);
		if (isLock) {
			try {
				String endDate = getDate(payment_time);
				List<Map<String, Object>> list = daoUtil.queryForList(format, payment_time, OrderTypeEnum.BUY.getType(), endDate, TradeStatusEnum.NP.getType());
				if(!CollectionUtils.isEmpty(list)) {
					logger.info("cancelTrade_" + payment_time + "======================>" + list.size());
					for(Map<String, Object> map : list) {
						cancelTradepool.execute(new CancelTradeJob(Integer.valueOf(String.valueOf(map.get("id")))));
					}
				}
			} finally {
				JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
			}
		} else {
			logger.info(lockKey + " Don`t get lock!");
		}
	}
	
	private String getDate(int time) {
		String date = HelpUtils.dateAddMin(-time);
		return date + ":00";
	}
	
	class CancelTradeJob implements Runnable{

		Integer tradeId;
		
		public CancelTradeJob(Integer tradeId) {
			super();
			this.tradeId = tradeId;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			oTCTradeManager.cancelTrade(tradeId, "超时自动撤销");
		}
		
	}
}
