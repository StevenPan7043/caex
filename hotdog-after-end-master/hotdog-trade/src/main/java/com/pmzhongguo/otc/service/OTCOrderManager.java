package com.pmzhongguo.otc.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.base.Joiner;
import com.pmzhongguo.ex.core.rediskey.RedisKeyTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.entity.dto.AccountInfoDTO;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.otcenum.OTCOrderOperateTypeEnum;
import com.pmzhongguo.otc.otcenum.OrderStatusEnum;
import com.pmzhongguo.otc.otcenum.PriceTypeEnum;
import com.pmzhongguo.otc.otcenum.TradeStatusEnum;
import com.pmzhongguo.otc.utils.OTCOrderUtil;
import com.pmzhongguo.otc.utils.OTCRedisLockConstants;

@Service
@Transactional
public class OTCOrderManager {

	protected static Logger log = LoggerFactory.getLogger(OTCOrderManager.class);

	ExecutorService pool = Executors.newFixedThreadPool(10, new ZzexThreadFactory("TradeOTCOrder"));

	@Autowired
	private OTCOrderService oTCOrderService;
	
	
	@Autowired
	private OTCTradeManager oTCTradeManager;
	
	@Autowired
	private OTCAccountInfoManager oTCAccountInfoManager;
	
	@Autowired
	private com.pmzhongguo.ex.business.service.MemberService MemberService;
	
	public ObjResp validateCreateOrder(OTCOrderDTO oTCOrderDTO) {
		ObjResp resp = new ObjResp();
		
		
		
		return resp;
	}
	
	public int update(OTCOrderDTO record) {
		int i = oTCOrderService.updateByPrimaryKeySelective(record);
		return i;
	}
	
	/**
	 * ??????????????????
	 * 
	 * @param record
	 * 
	 * 
	 * @return
	 */
	public int insert(OTCOrderDTO record) {
		return oTCOrderService.insert(record);
	}

	/**
	 * ??????id????????????
	 * 
	 * @param id
	 * @return
	 */
	public OTCOrderDTO findById(int id) {
		OTCOrderDTO record = oTCOrderService.findById(id);
		return record;
	}
	
	/**
	 * ????????????????????????
	 * 
	 * @param param
	 * @return
	 */
	public List<OTCOrderDTO> findByConditionPage(Map<String, Object> param) {
		List<OTCOrderDTO> list = oTCOrderService.findByConditionPage(param);
		return list;
	}
	
	/**
	 * 	????????????????????????
	 * @param param effectiveTime ????????????  createTime ??????????????????????????????
	 * @return
	 */
	public List<OTCOrderDTO> getCancelOrderList(Map<String, Object> param) {
		List<OTCOrderDTO> list = oTCOrderService.getCancelOrderList(param);
		return list;
	}
	
	/**
	 * 	???????????????????????????
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> getTradeOTCOrder(Map<String, Object> param) {
		List<OTCOrderDTO> list = oTCOrderService.findByConditionPage(param);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		List<Future> futurelist = new ArrayList<Future>();
		for(OTCOrderDTO dto : list) {
			 Callable c = new TradeOTCOrderJob(dto);
	            Future f = pool.submit(c);
	            futurelist.add(f);
		}
		//??????????????????????????????
		Map<String, Object> jobResult = new HashMap<String, Object>();
		for (Future f : futurelist) {
            // ???Future?????????????????????????????????????????????????????????
            try {
				Map<String, Object> r = (Map<String, Object>) f.get();
				jobResult.put(r.get("memberId") + "_" + r.get("orderId"), r);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
        }
		
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for(OTCOrderDTO dto : list) {
			results.add((Map<String, Object>) jobResult.get(dto.getMemberId() + "_" + dto.getId()));
		}
		return results;
	}
	
	private class TradeOTCOrderJob implements Callable<Map<String, Object>>{

		OTCOrderDTO dto;
		
		public TradeOTCOrderJob(OTCOrderDTO dto) {
			this.dto = dto;
		}

		@Override
		public Map<String, Object> call() throws Exception {
			// TODO Auto-generated method stub
			
			Integer memberId = dto.getMemberId();
			
			//?????????
			Map<String, Object> values = new HashMap<String, Object>();
			
			//??????????????????
			Member m = MemberService.getMemberById(memberId);
			values.put("m_nick_name", m.getM_nick_name());
			
			//??????????????????
			String start = HelpUtils.dateAddDay(-30) + " 00:00:00";
			String end = HelpUtils.dateToString(new Date());

			int totalDoneTrade = getTotalCountDoneWithCache(memberId, TradeStatusEnum.DONE);
			//30?????????
			int done_30 = get30DoneWithCache(memberId,start,end, TradeStatusEnum.DONE);
			int totalComplainTrade = getComplainWithCache(memberId);

//			int totalDoneTrade = oTCTradeManager.countDone(HelpUtils.newHashMap("memberId", memberId, "status", TradeStatusEnum.DONE));
//			//30?????????
//			int done_30 = oTCTradeManager.countDone(HelpUtils.newHashMap("memberId", memberId, "dbDate", "done_time", "startDate", start, "endDate", end, "status", TradeStatusEnum.DONE));
//			int totalComplainTrade = oTCTradeManager.countComplain(HelpUtils.newHashMap("memberId", memberId));
     		values.put("done_30", done_30);
			values.put("totalComplainTrade", totalComplainTrade);
			values.put("totalDoneTrade", totalDoneTrade);
			
			//??????????????????
			if(!HelpUtils.nullOrBlank(dto.getAcountId())) {
				List<AccountInfoDTO> list = oTCAccountInfoManager.findByConditionPage(HelpUtils.newHashMap("memberId", memberId));
				String[] accountIdStrs = dto.getAcountId().split(",");
				Map<String, Object> accountMap = new HashMap<String, Object>();
				for(String idStr : accountIdStrs) {
					for(AccountInfoDTO dto : list) {
						if(idStr.equals(dto.getId().toString())) {
							accountMap.put(idStr, dto.getType());
							break;
						}
					}
				}
				values.put("account", accountMap);
			}
			
			//???????????? 
			values.put("orderId", dto.getId());
			values.put("memberId", memberId);
			values.put("baseCurrency", dto.getBaseCurrency());
			values.put("quoteCurrency", dto.getQuoteCurrency());
			values.put("type", dto.getType());
			values.put("remainVolume", dto.getRemainVolume());
			values.put("volume", dto.getVolume());
			values.put("price", dto.getPrice());
			values.put("remark", dto.getRemark());
			values.put("minQuote", dto.getMinQuote());
			values.put("maxQuote", dto.getMaxQuote());
			values.put("paymentTime", dto.getPaymentTime());
			values.put("createTime", dto.getCreateTime());
			values.put("lockVolume", dto.getLockVolume());
			values.put("doneVolume", dto.getDoneVolume());
			values.put("status", dto.getStatus());
			values.put("number", dto.getNumber());
			values.put("mName", dto.getmName());
			return values;
		}
	}




	/**
	 * ???????????????
	 * @param memberId
	 * @return
	 */
	private int getComplainWithCache(Integer memberId) {
		String key = Joiner.on(":").join(RedisKeyTemplate.OTC_ORDER_TOTAL_COMPLAIN_TRADE_KEY, memberId);
//		log.error("==============> otcOrderTotalComplainTradeKey redis???key???{},???????????????{}"
//				,key,RedisKeyTemplate.REDIS_KEY_60_SEC);


		Object totalComplainTradeObj = JedisUtil.getInstance().get(key, true);
		if (HelpUtils.nullOrBlank(totalComplainTradeObj)) {
			int totalComplainTrade = oTCTradeManager.countComplain(HelpUtils.newHashMap("memberId", memberId));
			// ??????????????????
			JedisUtil.getInstance().set(key,String.valueOf(totalComplainTrade),true);
			JedisUtil.getInstance().expire(key,RedisKeyTemplate.REDIS_KEY_60_SEC);
			return totalComplainTrade;
		}
		return Integer.valueOf(totalComplainTradeObj.toString());
	}

	/**
	 * 30????????????
	 * @param memberId
	 * @param start
	 * @param end
	 * @param done
	 * @return
	 */
	private int get30DoneWithCache(Integer memberId, String start, String end, TradeStatusEnum done) {
		start = HelpUtils.trim(start);
		end = HelpUtils.trim(end);
		String key = Joiner.on(":").join(RedisKeyTemplate.OTC_ORDER_30_DONE_TRADE_KEY, memberId,start,end, done);
//		log.error("==============> otcOrderTotalDoneTradeKey redis???key???{},???????????????{}"
//				,key,RedisKeyTemplate.REDIS_KEY_60_SEC);


		Object done30TradeObj = JedisUtil.getInstance().get(key, true);
		if (HelpUtils.nullOrBlank(done30TradeObj)) {
			int done30 = oTCTradeManager.countDone(HelpUtils.newHashMap("memberId", memberId, "dbDate", "done_time", "startDate", start
					, "endDate", end, "status", done));
			// ??????????????????
			JedisUtil.getInstance().set(key,String.valueOf(done30),true);
			JedisUtil.getInstance().expire(key,RedisKeyTemplate.REDIS_KEY_60_SEC);
			return done30;
		}
		return Integer.valueOf(done30TradeObj.toString());
	}

	/**
	 * ?????????????????????????????????????????????????????????????????????
	 * @param memberId
	 * @param done
	 * @return
	 */
	private int getTotalCountDoneWithCache(Integer memberId, TradeStatusEnum done) {
		String key = Joiner.on(":").join(RedisKeyTemplate.OTC_ORDER_TOTAL_DONE_TRADE_KEY, memberId, done);
//		log.error("==============> otcOrderTotalDoneTradeKey redis???key???{},???????????????{}"
//				,key,RedisKeyTemplate.REDIS_KEY_60_SEC);

		Object totalDoneTradeObj = JedisUtil.getInstance().get(key, true);
		if (HelpUtils.nullOrBlank(totalDoneTradeObj)) {
			int totalDoneTrade = oTCTradeManager.countDone(HelpUtils.newHashMap("memberId", memberId, "status", TradeStatusEnum.DONE));
			// ??????????????????
			JedisUtil.getInstance().set(key,String.valueOf(totalDoneTrade),true);
			JedisUtil.getInstance().expire(key,RedisKeyTemplate.REDIS_KEY_60_SEC);
			return totalDoneTrade;
		}
		return Integer.valueOf(totalDoneTradeObj.toString());
	}

	/**
	 * ?????????????????????
	 * 
	 * @param record baseCurrency quoteCurrency type price priceType volume minQuote
	 */
	public void initOrder(OTCOrderDTO record) {
		if (HelpUtils.nullOrBlank(record.getNumber())) {
			record.setNumber(HelpUtils.getNumber("O"));
		}
		if (record.getPriceType().getType() == PriceTypeEnum.MARKET.getType()) {
			record.setPrice(BigDecimal.ZERO);
			record.setMinQuote(BigDecimal.ZERO);
			record.setMaxQuote(BigDecimal.ZERO);
			record.setPaymentTime(0);
		} 
		
		record.setRemainVolume(record.getVolume());
		// ???????????????????????? ??????*??????
		if (OTCOrderUtil.isLimitBuy(record)) {
			record.setFrozen(record.getPrice().multiply(record.getVolume()));
		} else {
			record.setFrozen(record.getVolume());
		}

		record.setRemain(record.getFrozen());
		record.setLockVolume(BigDecimal.ZERO);
		record.setLockAmount(BigDecimal.ZERO);
		record.setDoneVolume(BigDecimal.ZERO);
		record.setCancelVolume(BigDecimal.ZERO);
		record.setUnfrozen(BigDecimal.ZERO);
		record.setOrigPrice(record.getPrice());
		// ??????????????????WATTING??? ???????????????????????????????????? TRADING
//		if (record.getPriceType().getType() == PriceTypeEnum.LIMIT.getType() && (record.getOppositeId() == null || record.getOppositeId().intValue() == 0)) {
//			record.setStatus(OrderStatusEnum.WATTING);
//		} else {
//			record.setStatus(OrderStatusEnum.TRADING);
//		}
		//??????????????????????????????0
		if(record.getEffectiveTime() == null) {
			record.setEffectiveTime(0);
		}
	}


	/**
	 * 	??????????????????
	 * 	??????????????? WATTING ???????????????????????????
	 * 	??????????????? WATTING TRADING ???????????????????????????
	 * 	??????????????? TRADING ???????????????????????????
	 * 	????????????????????????????????????????????????
	 * 	???????????????????????????curVolume?????????????????????remainvolume 
	 * @param orderId
	 * @param curVolume
	 * @param procType
	 * @return
	 */
	public ObjResp orderChange(Integer orderId, BigDecimal curVolume, OTCOrderOperateTypeEnum procType) {
		String lockKey = OTCRedisLockConstants.OTC_ORDER_CHANGE_REDIS_LOCK_PRE + String.valueOf(orderId);
		boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
				OTCRedisLockConstants.OTC_ORDER_CHANGE_REDIS_LOCK_EXPIRE_TIME, OTCRedisLockConstants.REDIS_LOCK_RETRY_10, OTCRedisLockConstants.REDIS_LOCK_INTERVAL_100);
		ObjResp result = null;
		try {
			if (isLock) {
				OTCOrderDTO record = oTCOrderService.findById(orderId);
				if (record == null) {
					return new ObjResp(Resp.FAIL, orderId + " The order was not found???", null);
				}
				record.setCurVolume(curVolume);
				result = orderChange(record,procType);
			} else {
				result = new ObjResp(Resp.FAIL, lockKey + " Don`t get lock!", null);
			}

		} finally {
			JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
		}
		return result;
	}
	
	private ObjResp orderChange(OTCOrderDTO dto, OTCOrderOperateTypeEnum procType) {
		
		if (dto.getStatus().getType() == OrderStatusEnum.DONE.getType()
				|| dto.getStatus().getType() == OrderStatusEnum.CANCELED.getType()) {
			return new ObjResp(Resp.FAIL, "The order has been completed???", null);
		}
		ObjResp result = null;
		switch (procType.getType()) {
		case 1:
			result = oTCOrderService.lockTrade(dto);
			break;
		case 2:
			result = oTCOrderService.doneTrade(dto);
			break;
		case 3:
			result = oTCOrderService.cancelTrade(dto);
			break;
		case 4:
			result = oTCOrderService.cancelOrder(dto);
			break;
		}
		return result;
	}
}
