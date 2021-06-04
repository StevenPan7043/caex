package com.pmzhongguo.ex.business.scheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.service.OTCOrderManager;
import com.pmzhongguo.otc.service.OTCTradeManager;
import com.pmzhongguo.otc.utils.OTCRedisLockConstants;

/**
 * 到期订单撤单计划任务
 * 
 * @author Daily
 *
 */
@Component
public class OTCExpireOrderScheduler {

	@Autowired
	private OTCTradeManager oTCTradeManager;

	@Autowired
	private OTCOrderManager oTCOrderManager;

	// 3天 5天 10天 15天
	private static final int[] effectiveTimes = { 3, 5, 10, 15 };

	protected Logger logger = LoggerFactory.getLogger(OTCExpireOrderScheduler.class);

	@Scheduled(cron = "0 1 0 * * ?")
	public void cancelOrder() {
		String lockKey = OTCRedisLockConstants.OTC_CANCEL_ORDER_JOB_LOCK_PRE;
		// 任务1h执行一次，锁的有效时间设为50m
		boolean isLock = JedisUtil.getInstance().getLock(lockKey, IPAddressPortUtil.IP_ADDRESS,
				OTCRedisLockConstants.OTC_CANCEL_ORDER_JOB_LOCK_EXPIRE_TIME);
		if (isLock) {

			try {
				for (int t : effectiveTimes) {
					cancelOrder(t);
				}
			} finally {

				JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
			}
		} else {
			logger.info(lockKey + " Don`t get lock!");
		}

	}

	private void cancelOrder(int effectiveTime) {
		String date = getDate(effectiveTime);
		List<OTCOrderDTO> list = oTCOrderManager
				.getCancelOrderList(HelpUtils.newHashMap("effectiveTime", effectiveTime, "createTime", date));
		if (!CollectionUtils.isEmpty(list)) {
			ExecutorService cancelOrderpool = Executors.newFixedThreadPool(8, new ZzexThreadFactory("cancelOrder"));
			for (OTCOrderDTO dto : list) {
				logger.info("cancelOrder_" + effectiveTime + "======================>" + list.size());
				cancelOrderpool.execute(new CancelOrderJob(dto));
			}
			if (cancelOrderpool != null) {
				cancelOrderpool.shutdown();
			}
		}

	}

	private String getDate(int day) {
		String date = HelpUtils.dateAddDay(-day);
		return date + " 23:59:59";
	}

	class CancelOrderJob implements Runnable {

		OTCOrderDTO order;

		public CancelOrderJob(OTCOrderDTO order) {
			super();
			this.order = order;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			oTCTradeManager.cancelOrder(order);
		}
	}
}
