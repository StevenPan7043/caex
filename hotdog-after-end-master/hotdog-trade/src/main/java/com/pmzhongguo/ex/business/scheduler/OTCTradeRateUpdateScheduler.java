package com.pmzhongguo.ex.business.scheduler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pmzhongguo.ex.business.resp.TickerResp;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.service.MarketService;
import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.otcenum.OrderStatusEnum;
import com.pmzhongguo.otc.otcenum.PriceChangeTypeEnum;
import com.pmzhongguo.otc.service.OTCOrderManager;
import com.pmzhongguo.otc.service.OTCTradeManager;
import com.pmzhongguo.otc.utils.OTCRedisLockConstants;

import static com.pmzhongguo.ex.business.scheduler.UsdtCnyPriceScheduler.PRICE;

@Component
public class OTCTradeRateUpdateScheduler {
	
	private Logger logger = LoggerFactory.getLogger(OTCTradeRateUpdateScheduler.class);

	final int SCALE = 2;

	final int PAGE_SIZE = 100;
	
	final String quote = "CNY";

	@Autowired
	private OTCOrderManager oTCOrderManager;
	
	@Autowired
	private OTCTradeManager oTCTradeManager;
	
	ExecutorService tradeRateUpdatePool = Executors.newFixedThreadPool(8, new ZzexThreadFactory("tradeRateUpdatePool"));

	@Autowired
	public DaoUtil daoUtil;

	/**
     * 浮动价格 挂单状态
	 */
    @Scheduled(cron = "0 */1 * * * ?")
    public void tradeRateUpdate() {
		// 当前汇兑率
		BigDecimal close = null;
        List<Currency> list = HelpUtils.getCurrencyIsOtcLst();
        for (Currency c : list) {
			if (c.getCurrency().toLowerCase().equals(HelpUtils.getOfficialCurrencyToLower())) {
				close = PRICE;
			} else {
				TickerResp ticker = null;
				Map<String, TickerResp> tickerMap = MarketService.getTicker();
				String mapKey = c.getCurrency().toLowerCase() + HelpUtils.getOfficialCurrencyToLower() + "_ticker";
				ticker = tickerMap.get(mapKey.toLowerCase());
				if (ticker == null) {
					logger.warn("Current price from mapKey get value is null!  " + mapKey);
					continue;
				} else {
					close = ticker.getClose().multiply(PRICE);
				}
			}
			List<Map<String, Integer>> result = getOrder(c.getCurrency(), quote);
			if (CollectionUtils.isEmpty(result)) {
				break;
			}
			for (Map<String, Integer> item : result) {
				OTCOrderDTO updateDto = null;
				// 查询出来，再次做判断，防止被交易
				int id = Integer.valueOf(String.valueOf(item.get("id"))).intValue();
				OTCOrderDTO dto = oTCOrderManager.findById(id);
				if (isUpdate(dto)) {
					updateDto = updateFloatPrice(dto, close);
					updateDto.setId(dto.getId());
					updateDto.setRemainVolume(dto.getRemainVolume());
					updateDto.setMemberId(dto.getMemberId());
					updateDto.setBaseCurrency(dto.getBaseCurrency());
					updateDto.setMinQuote(dto.getMinQuote());
					updateDto.setType(dto.getType());
				}
				if (updateDto != null && updateDto.getMaxQuote().compareTo(updateDto.getMinQuote()) < 0) {
					oTCTradeManager.cancelOrder(updateDto);
				}
			}
		}

	}

	/**
	 * 	获取要更新的订单列表
	 * @param baseCurrency
	 * @param quoteCurrency
	 * @return
	 */
	private List<Map<String, Integer>> getOrder(String baseCurrency, String quoteCurrency) {
		List<Map<String, Integer>> result = daoUtil.queryForList(
				"SELECT id FROM o_order o WHERE o.base_currency = ? AND o.quote_currency = ? AND o.status = ? AND o.price_change_type = ? ORDER BY o.`create_time` DESC ",
				baseCurrency, quoteCurrency, OrderStatusEnum.WATTING.getType(), PriceChangeTypeEnum.FLOAT.getType());
		return result;
	}

	class OTCTradeRateUpdateJob implements Runnable {

		List<Map<String, Integer>> list;

		BigDecimal currRate;

		public OTCTradeRateUpdateJob(List<Map<String, Integer>> list, BigDecimal currRate) {
			super();
			this.list = list;
			this.currRate = currRate;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			OTCOrderDTO updateDto = null;
			for (Map<String, Integer> idMap : list) {
				int id = Integer.valueOf(String.valueOf(idMap.get("id"))).intValue();
				String lockKey = OTCRedisLockConstants.OTC_ORDER_TRADE_REDIS_LOCK_PRE + String.valueOf(id);
				boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
						OTCRedisLockConstants.OTC_ORDER_TRADE_REDIS_LOCK_EXPIRE_TIME,
						OTCRedisLockConstants.REDIS_LOCK_RETRY_10, OTCRedisLockConstants.REDIS_LOCK_INTERVAL_100);
				if (isLock) {
					try {
						// 查询出来，再次做判断，防止被交易
						OTCOrderDTO dto = oTCOrderManager.findById(id);
						if (isUpdate(dto)) {
							updateDto = updateFloatPrice(dto, currRate);
							updateDto.setId(dto.getId());
							updateDto.setRemainVolume(dto.getRemainVolume());
							updateDto.setMemberId(dto.getMemberId());
							updateDto.setBaseCurrency(dto.getBaseCurrency());
							updateDto.setMinQuote(dto.getMinQuote());
							updateDto.setType(dto.getType());
						}
					}catch(Exception e) {
						logger.warn(lockKey + "  exception! cause:" + e.getCause(), e );
					} finally {
						JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
					}
				}
				if(updateDto != null && updateDto.getMaxQuote().compareTo(updateDto.getMinQuote()) < 0) {
					oTCTradeManager.cancelOrder(updateDto);
				}
			}
		}
	}

	/**
	 * 获取到锁后再次判断是否要更新
	 * 
	 * @param dto
	 * @return
	 */
	private boolean isUpdate(OTCOrderDTO dto) {
		return dto != null && dto.getPriceChangeType().getType() == PriceChangeTypeEnum.FLOAT.getType()
				&& dto.getStatus().getType() == OrderStatusEnum.WATTING.getType();
	}

	/**
	 * 更新数据
	 * 
	 * @param dto
	 * @param currRate
	 */
	private OTCOrderDTO updateFloatPrice(OTCOrderDTO dto, BigDecimal currRate) {
		OTCOrderDTO updateDto = new OTCOrderDTO();
		updateDto.setId(dto.getId());
		BigDecimal pre = currRate.subtract(dto.getBaseRate());
		BigDecimal realPrice = pre.add(dto.getOrigPrice());
		updateDto.setPrice(realPrice.setScale(SCALE, BigDecimal.ROUND_DOWN));
		updateDto.setBaseRate(currRate);
		updateDto.setRemain(updateDto.getPrice().multiply(dto.getRemainVolume()));
		updateDto.setMaxQuote(updateDto.getRemain());
		oTCOrderManager.update(updateDto);
		return updateDto;
	}

	/**
	 * 获取最新的汇率价格
	 * 
	 * @param base
	 * @param quote
	 * @return
	 */
	private TickerResp getTicker(String base, String quote) {
		TickerResp ticker = null;
		Map<String, TickerResp> tickerMap = MarketService.getTicker();
		if (tickerMap != null) {
			String mapKey = base + quote + "_ticker";
			ticker = tickerMap.get(mapKey.toLowerCase());
		}
		return ticker;
	}
}
