package com.pmzhongguo.otc.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.scheduler.UsdtCnyPriceScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.service.MarketService;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.entity.dto.AccountDTO;
import com.pmzhongguo.otc.entity.dto.AccountInfoDTO;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.otcenum.AccountOperateTypeEnum;
import com.pmzhongguo.otc.otcenum.OTCOrderOperateTypeEnum;
import com.pmzhongguo.otc.otcenum.OrderStatusEnum;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.PriceChangeTypeEnum;
import com.pmzhongguo.otc.otcenum.PriceTypeEnum;
import com.pmzhongguo.otc.otcenum.TakerEnum;
import com.pmzhongguo.otc.otcenum.TradeStatusEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;
import com.pmzhongguo.otc.utils.OTCOrderUtil;
import com.pmzhongguo.otc.utils.OTCRedisLockConstants;

@Service
@Transactional
public class OTCTradeManager {

	protected Logger TradeLogger = LoggerFactory.getLogger("tradeInfo");

	@Autowired
	private OTCAccountManager oTCAccountManager;

	@Autowired
	private OTCOrderManager oTCOrderService;

	@Autowired
	private OTCTradeService oTCTradeService;

	@Autowired
	private OTCAccountInfoManager oTCAccountInfoManager;

	@Autowired
	public DaoUtil daoUtil;

	public List<OTCTradeDTO> findByConditionPage(Map<String, Object> param) {
		List<OTCTradeDTO> list = oTCTradeService.findByConditionPage(param);
		return list;
	}

	/**
	 * 与findByConditionPage不同在与status条件写死为status in (0, 1)
	 * 
	 * @param param
	 * @return
	 */
	public List<OTCTradeDTO> findBytradingPage(Map<String, Object> param) {
		List<OTCTradeDTO> list = oTCTradeService.findBytradingPage(param);
		return list;
	}

	/**
	 * 查询申诉中的交易单子
	 * @param param
	 * @return
	 */
	public List<OTCTradeDTO> findByComplainTradePage(Map<String, Object> param) {
		List<OTCTradeDTO> list = oTCTradeService.findByComplainTradePage(param);
		return list;
	}
	/**
	 * 条件 memberId|startDate|endDate|status 数据库时间字段 dbDate
	 * 
	 * @param param
	 * @return
	 */
	public int countDone(Map<String, Object> param) {
		int count = oTCTradeService.countDone(param);
		return count;
	}
	/**
	 * 	根据 memberId 查询申诉记录数
	 * @param param
	 * @return
	 */
	public int countComplain(Map<String, Object> param) {
		int count = oTCTradeService.countComplain(param);
		return count;
	}

	/**
	 * 获取订单
	 * 
	 * @param id
	 * @return
	 */
	public ObjResp getOrderAccountInfo(OTCTradeDTO oTCTradeDTO) {
		Integer id = oTCTradeDTO.getId();
		ObjResp resp = new ObjResp();
		// 返回的结果
		Map<String, Object> result = new HashMap<String, Object>();
		// 查询订单里的账户id
		Map<String, Object> accountId = new HashMap<String, Object>();
		
		accountId = daoUtil.queryForMap(
				"SELECT o.type, o.acount_id as acountId, o.member_id memberId FROM o_trade t LEFT JOIN  o_order o ON t.o_id = o.id WHERE t.opposite_t_id = ? ",
				id);
		List<AccountInfoDTO> oppoDto = new ArrayList<AccountInfoDTO>();
		if (!HelpUtils.isMapNullValue(accountId, "acountId")) {
			oppoDto = getOrderAccountInfo(String.valueOf(accountId.get("acountId")));
		}else {
			oppoDto = oTCAccountInfoManager.findByConditionPage(HelpUtils.newHashMap("isDelete", WhetherEnum.NO, "memberId", Integer.valueOf(String.valueOf(accountId.get("memberId")))));
		}
		if (oTCTradeDTO.gettType().getType() == OrderTypeEnum.SELL.getType()) {
			result.put("buyAccount", oppoDto);
		} else {
			result.put("sellAccount", oppoDto);
		}
		
		accountId = daoUtil.queryForMap(
				"SELECT o.type, o.acount_id as acountId FROM o_trade t LEFT JOIN  o_order o ON t.o_id = o.id WHERE t.id = ? ",
				id);
		List<AccountInfoDTO> selfDto = new ArrayList<AccountInfoDTO>();
		if (!HelpUtils.isMapNullValue(accountId, "acountId")) {
			selfDto = getOrderAccountInfo(String.valueOf(accountId.get("acountId")));
		}else {
			selfDto = oTCAccountInfoManager.findByConditionPage(HelpUtils.newHashMap("memberId", oTCTradeDTO.getMemberId(), "isDelete", WhetherEnum.NO));
		}
		if (oTCTradeDTO.gettType().getType() == OrderTypeEnum.BUY.getType()) {
			result.put("buyAccount", selfDto);
		} else {
			result.put("sellAccount", selfDto);
		}
		resp.setData(result);
		return resp;
	}

	private List<AccountInfoDTO> getOrderAccountInfo(String accountId) {
		String[] acountArr = accountId.split(",");
		List<AccountInfoDTO> list = new ArrayList<AccountInfoDTO>();
		for (String str : acountArr) {
			AccountInfoDTO accountInfo = oTCAccountInfoManager.findByIdAll(Integer.valueOf(str.trim()));
			list.add(accountInfo);
		}
		return list;
	}

	/**
	 * 申诉审核
	 * 
	 * @param id
	 * @param opera
	 * @param memo_result
	 * @return
	 */
	public ObjResp complainAudit(Integer id, String opera, String memo_result) {
		ObjResp resp = new ObjResp();
		// 先将状态恢复
		// opera = 0 撤销申诉
		daoUtil.update(
				"UPDATE o_trade t SET t.status = (SELECT o.status FROM (SELECT tt.status FROM o_trade tt WHERE tt.opposite_t_id = ?) o) WHERE t.id = ?",
				id, id);
		if (!HelpUtils.nullOrBlank(memo_result)) {
			OTCTradeDTO record = new OTCTradeDTO();
			record.setId(id);
			record.setMemo(memo_result);
			oTCTradeService.updateByPrimaryKeySelective(record);
		}

		// 交易撤销 TRADE-COMPLAIN-AUDIT
		if ("1".equals(opera)) {
			resp = cancelTrade(id, null);
		}

		// 交易完成
		if ("2".equals(opera)) {
			resp = confirmedTrade(id);
		}
		failThrowException(resp);
		return resp;
	}

	/**
	 * 查询会员基本交易信息
	 * 
	 * @param memberId
	 * @return
	 */
	public ObjResp queryMerchantTradeInfo(Integer memberId) {
		ObjResp resp = new ObjResp();
		String start = HelpUtils.dateAddDay(-30) + " 00:00:00";
		String end = HelpUtils.dateToString(new Date());
		int totalDoneTrade = countDone(HelpUtils.newHashMap("memberId", memberId, "status", TradeStatusEnum.DONE));
		int done_30 = countDone(HelpUtils.newHashMap("memberId", memberId, "startDate", start,
				"endDate", end, "status", TradeStatusEnum.DONE));
		int totalComplainTrade = countComplain(
				HelpUtils.newHashMap("memberId", memberId));
		int consumingTime = getConsumingTime(
				HelpUtils.newHashMap("memberId", memberId, "status", TradeStatusEnum.DONE));
		resp.setData(HelpUtils.newHashMap("totalDoneTrade", totalDoneTrade, "done_30", done_30, "totalComplainTrade", totalComplainTrade,
				"consumingTime", consumingTime));
		return resp;
	}

	/**
	 * memberId and status
	 * 
	 * @param param
	 * @return 放行时间
	 */
	public int getConsumingTime(Map<String, Object> param) {
		int consumingTime = oTCTradeService.getConsumingTime(param);
		return consumingTime;
	};

	public OTCTradeDTO findById(int id) {
		return oTCTradeService.findById(id);
	}

	/**
	 * 付费确认
	 * 
	 * @param tradeId
	 * @return
	 */
	public ObjResp pay(int tradeId, Integer acountId) {
		// 付款方暂不记录付款账号
		pay0(tradeId, null);
		OTCTradeDTO tradeDTO1 = oTCTradeService.findById(tradeId);
		// 修改状态时记入收款方(卖方)账号以备查
		pay0(tradeDTO1.getOppositeTId(), acountId);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}

	private void pay0(int tradeId, Integer acountId) {
		int count = modifyStatus(tradeId, TradeStatusEnum.UNCONFIRMED, null);
		failThrowException(new ObjResp(count, "tradeId:" + tradeId + " pay Trade fail!", null));
		if (acountId != null) {
			OTCTradeDTO payTrade = new OTCTradeDTO();
			payTrade.setId(tradeId);
			payTrade.setAcountId(acountId);
			count = oTCTradeService.updateByPrimaryKeySelective(payTrade);
			failThrowException(new ObjResp(count, "tradeId:" + tradeId + " pay Trade fail!", null));
		}
	}

	/**
	 * 撤销订单
	 * 
	 * @param order
	 * @return
	 */
	public ObjResp cancelOrder(OTCOrderDTO order) {
		ObjResp resp = oTCOrderService.orderChange(order.getId(), order.getRemainVolume(),
				OTCOrderOperateTypeEnum.CANCELORDER);
		failThrowException(resp);
		if (order.getType().getType() == OrderTypeEnum.SELL.getType()) {
			resp = oTCAccountManager.assetChange(order.getMemberId(), order.getBaseCurrency(), order.getRemainVolume(),
					AccountOperateTypeEnum.RETURNFROZEN);
			failThrowException(resp);
		}
		return resp;
	}

	/**
	 * 取消交易
	 * 
	 * @param tradeId
	 * @return
	 */
	public ObjResp cancelTrade(int tradeId, String memo) {
		OTCTradeDTO tradeDTO1 = oTCTradeService.findById(tradeId);
		OTCTradeDTO tradeDTO2 = oTCTradeService.findById(tradeDTO1.getOppositeTId());
		OTCOrderDTO orderDTO1 = oTCOrderService.findById(tradeDTO1.getoId());
		OTCOrderDTO orderDTO2 = oTCOrderService.findById(tradeDTO2.getoId());
		cancelTrade(tradeDTO1, orderDTO1, memo);
		cancelTrade(tradeDTO2, orderDTO2, memo);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}

	private void cancelTrade(OTCTradeDTO tradeDTO, OTCOrderDTO orderDTO, String memo) {
		// 操作交易记录
		int count = modifyStatus(tradeDTO.getId(), TradeStatusEnum.CANCELED, memo);
		failThrowException(new ObjResp(count, "tradeId:" + tradeDTO.getId() + " cancelTrade fail!", tradeDTO));

		// 市价买单要换算出计价货币数
		if (OTCOrderUtil.isMarketBuy(orderDTO)) {
			orderDTO.setCurVolume(tradeDTO.getVolume().multiply(tradeDTO.getPrice()));
		} else {
			orderDTO.setCurVolume(tradeDTO.getVolume());
		}

		// 操作订单记录
		ObjResp resp = oTCOrderService.orderChange(orderDTO.getId(), orderDTO.getCurVolume(),
				OTCOrderOperateTypeEnum.CANCELTRADE);
		failThrowException(resp);

		// 挂单方判断是否是部分撤单
		// 判断是否是部分撤单的订单，如果是还要把撤销的交易也撤单
		if (orderDTO.getStatus().getType() == OrderStatusEnum.PC.getType()) {
			resp = oTCOrderService.orderChange(orderDTO.getId(), orderDTO.getCurVolume(),
					OTCOrderOperateTypeEnum.CANCELORDER);
			failThrowException(resp);
			if (orderDTO.getType().getType() == OrderTypeEnum.SELL.getType()) {
				oTCAccountManager.assetChange(orderDTO.getMemberId(), orderDTO.getBaseCurrency(), tradeDTO.getVolume(),
						AccountOperateTypeEnum.RETURNFROZEN);
				failThrowException(resp);
			}
		}

		// 如果是吃单方，撤销交易就意味着撤单
		if (tradeDTO.getTaker().getType() == TakerEnum.SELF.getType()) {
			orderDTO.setRemainVolume(tradeDTO.getVolume());
			cancelOrder(orderDTO);
		}
	}

	/**
	 * 交易确认
	 * 
	 * @param tradeId
	 * @return
	 */
	public ObjResp confirmedTrade(int tradeId) {
		OTCTradeDTO tradeDTO1 = oTCTradeService.findById(tradeId);
		OTCTradeDTO tradeDTO2 = oTCTradeService.findById(tradeDTO1.getOppositeTId());
		confirmedTrade(tradeDTO1);
		confirmedTrade(tradeDTO2);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}

	private void confirmedTrade(OTCTradeDTO tradeDTO) {
		int count = modifyStatus(tradeDTO.getId(), TradeStatusEnum.DONE, null);
		failThrowException(new ObjResp(count, "tradeId:" + tradeDTO.getId() + " confirmedTrade fail!", tradeDTO));
		OTCOrderDTO orderDTO = oTCOrderService.findById(tradeDTO.getoId());
		// 市价买单要换算出计价货币数
		if (OTCOrderUtil.isMarketBuy(orderDTO)) {
			orderDTO.setCurVolume(tradeDTO.getVolume().multiply(tradeDTO.getPrice()));
		} else {
			orderDTO.setCurVolume(tradeDTO.getVolume());
		}
		ObjResp resp = oTCOrderService.orderChange(tradeDTO.getoId(), orderDTO.getCurVolume(),
				OTCOrderOperateTypeEnum.DONETRADE);
		failThrowException(resp);
		if (tradeDTO.gettType().getType() == OrderTypeEnum.BUY.getType()) {
			resp = oTCAccountManager.assetChange(tradeDTO.getMemberId(), tradeDTO.getBaseCurrency(),
					tradeDTO.getVolume(), AccountOperateTypeEnum.ADDTOTAL);
		} else {
			resp = oTCAccountManager.assetChange(tradeDTO.getMemberId(), tradeDTO.getBaseCurrency(),
					tradeDTO.getVolume(), AccountOperateTypeEnum.REDUCEFROZEN);
		}
		failThrowException(resp);
	}

	public int modifyStatus(Integer tradeId, TradeStatusEnum status, String memo) {
		return oTCTradeService.modifyStatus(tradeId, status, memo);
	}

	public int complain(OTCTradeDTO complainDto) {
		OTCTradeDTO cancelTrade = new OTCTradeDTO();
		cancelTrade.setId(complainDto.getId());
		cancelTrade.setStatus(complainDto.getStatus());
		cancelTrade.setMemo(complainDto.getMemo());
		cancelTrade.setComplainType(complainDto.getComplainType());
		int count = updateByPrimaryKeySelective(cancelTrade);
		return count;
	}

	/**
	 * 创建订单
	 * 
	 * @param entry     挂单方初始数据 baseCurrency quoteCurrency type price priceType
	 *                  merchantId operIp volume minQuote 吃单方直接用挂单方记录
	 * @param volume
	 * @param orderType
	 * @return
	 */
	public ObjResp createOrder(OTCOrderDTO entry, OTCOrderDTO opposite) {
		ObjResp resp = new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
//		OTCOrderDTO record = new OTCOrderDTO();

		oTCOrderService.initOrder(entry);
		//设置价格基础汇率
		entry.setBaseRate(BigDecimal.ZERO);
		//如果是吃单操作就要从挂单方获取价格类型
		if(opposite != null) {
			entry.setPriceChangeType(opposite.getPriceChangeType());
			entry.setBaseRate(opposite.getBaseRate());
		}

		//浮动价格需要设置
		if (opposite == null && entry.getPriceChangeType().getType() == PriceChangeTypeEnum.FLOAT.getType()) {
			Map<String, TickerResp> tickerMap = MarketService.getTicker();
			if (tickerMap != null) {
				if (entry.getBaseCurrency().equals(HelpUtils.getOfficialCurrencyToUpper())) {
					entry.setBaseRate(UsdtCnyPriceScheduler.PRICE);
				} else {
					String mapKey = (entry.getBaseCurrency() + HelpUtils.getOfficialCurrencyToUpper() + "_ticker");
					TickerResp ticker = tickerMap.get(mapKey.toLowerCase());
					entry.setBaseRate(ticker.getClose().multiply(UsdtCnyPriceScheduler.PRICE));
				}
			}
		}
		
		// 如果是卖单先要将基础币冻结
		if (entry.getType().getType() == OrderTypeEnum.SELL.getType()) {
			resp = oTCAccountManager.assetChange(entry.getMemberId(), entry.getBaseCurrency(), entry.getFrozen(),
					AccountOperateTypeEnum.FROZEN);
			if (resp.getState().intValue() == Resp.FAIL.intValue()) {
				throw new BusinessException(Resp.FAIL, resp.getMsg());
			}
			/**
			 *	防止冻结时锁不住的情况 	
			 * 
			 */
			AccountDTO accountDTO = ((List<AccountDTO>) oTCAccountManager.getAccount(HelpUtils.newHashMap("currency", entry.getBaseCurrency().toUpperCase(), "memberId", entry.getMemberId())).getData()).get(0);
			if(accountDTO.getTotalBalance().compareTo(accountDTO.getFrozenBalance()) == -1) {
				String err = "订单冻结后数据异常。" + accountDTO.toString();
				TradeLogger.warn(err);
				throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LACK_OF_EFFECTIVE_ASSETS.getErrorENMsg());
			}
		}
		// 冻结完成，创建订单
		int orderId = oTCOrderService.insert(entry);
		entry.setId(orderId);

		// 如果只是挂单就直接返回
		if (entry.getPriceType().getType() == PriceTypeEnum.LIMIT.getType() && opposite == null) {
			return resp;
		}

		// 交易操作
//		marketCreateTrade(entry, opposite, 1);

		String lockKey = OTCRedisLockConstants.OTC_ORDER_TRADE_REDIS_LOCK_PRE + String.valueOf(opposite.getId());
		boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
				OTCRedisLockConstants.OTC_ORDER_TRADE_REDIS_LOCK_EXPIRE_TIME, OTCRedisLockConstants.REDIS_LOCK_RETRY_10,
				OTCRedisLockConstants.REDIS_LOCK_INTERVAL_100);
		if (isLock) {
			try {
				resp = simpleCreateTrade(opposite, entry);
			} finally {
				JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
			}
		} else {
			TradeLogger.warn(" Order:" + opposite.getId() + " Don`t get lock!" + lockKey);
			resp = new ObjResp(Resp.FAIL, ErrorInfoEnum.GET_LOCK_FAIL.getErrorENMsg(), lockKey);
			failThrowException(resp);
		}

		// 判断市价单是否还有剩余未成交的数量，有就撤单
//		if (entry.getRemainVolume().compareTo(BigDecimal.ZERO) > 0) {
//			resp = oTCOrderService.orderChange(entry.getId(), entry.getRemainVolume(),
//					OTCOrderOperateTypeEnum.CANCELORDER);
//		}
		return resp;
	}

	/**
	 * 
	 * @param resp
	 */
	private void failThrowException(ObjResp resp) {
		if (resp.getState().intValue() != Resp.SUCCESS.intValue()) {
			TradeLogger.warn(resp.getMsg());
			throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg());
		}
	}

	/**
	 * 根据两张订单生成交易信息
	 * 
	 * @param sell
	 * @param buy
	 */
	private ObjResp create(OTCOrderDTO opposite, OTCOrderDTO selfOrder, BigDecimal curvolume) {
		ObjResp resp = new ObjResp();
		try {
			String tNumber = HelpUtils.getNumber("T");
			OTCTradeDTO selfDTO = oTCTradeService.init(selfOrder, opposite.getId(), 0);
			selfDTO.setPrice(opposite.getPrice());
			selfDTO.setVolume(curvolume);
			selfDTO.setPaymentTime(opposite.getPaymentTime());
			selfDTO.settNumber(tNumber);
			// 生成吃单方的交易
			int selfID = oTCTradeService.insert(selfDTO);

			OTCTradeDTO oppoDTO = oTCTradeService.init(opposite, selfOrder.getId(), selfID);
			oppoDTO.setPaymentTime(opposite.getPaymentTime());
			oppoDTO.settNumber(tNumber);
			// 生成挂单方的交易
			int oppoId = oTCTradeService.insert(oppoDTO);
			OTCTradeDTO selfUpdateDTO = new OTCTradeDTO();
			selfUpdateDTO.setId(selfID);
			selfUpdateDTO.setOppositeTId(oppoId);
			// 将挂单方的交易ID回写到吃单方的交易记录里
			oTCTradeService.updateByPrimaryKeySelective(selfUpdateDTO);
			TradeLogger.info("sell OrderId:" + selfOrder.getId() + " tradeId:" + selfOrder + " buy OrderId:"
					+ opposite.getId() + " tradeId:" + oppoId);
			// 生成返回信息
			selfDTO.setId(selfID);
			selfDTO.setOppositeTId(oppoId);
			resp.setData(selfDTO);
		} catch (Exception e) {
			TradeLogger.warn("cause:" + e.getCause(), e);
			return new ObjResp(Resp.FAIL, e.getCause().toString(), null);
		}
		return resp;
	}

	/**
	 * 处理一张订单交易
	 * 
	 * @param opposite  挂单方订单
	 * @param selfOrder 吃单方订单
	 */
	private ObjResp simpleCreateTrade(OTCOrderDTO opposite, OTCOrderDTO selfOrder) {

		// 当前交易量
		BigDecimal curvolume = BigDecimal.ZERO;
		if (OTCOrderUtil.isMarketBuy(selfOrder)) {
			curvolume = selfOrder.getRemainVolume().divide(opposite.getPrice());
		} else {
			curvolume = selfOrder.getRemainVolume();
		}
		// 找出最小的交易量
//		curvolume = opposite.getRemainVolume().compareTo(curvolume) >= 0 ? curvolume : opposite.getRemainVolume();
		// 1、先修改订单信息
		opposite.setCurVolume(curvolume);
		ObjResp resp = oTCOrderService.orderChange(opposite.getId(), opposite.getCurVolume(),
				OTCOrderOperateTypeEnum.LOCKTRADE);
		failThrowException(resp);

		if (OTCOrderUtil.isMarketBuy(selfOrder)) {
			selfOrder.setCurVolume(curvolume.multiply(opposite.getPrice()));
		} else {
			selfOrder.setCurVolume(curvolume);
		}

		resp = oTCOrderService.orderChange(selfOrder.getId(), selfOrder.getCurVolume(),
				OTCOrderOperateTypeEnum.LOCKTRADE);
		failThrowException(resp);

		// 2、创建交易信息
		resp = create(opposite, selfOrder, curvolume);
		failThrowException(resp);
		
		//修改自己订单数据是因为市价单要进行多笔操作，如果只是限价单单笔操作就不需求这几条代码
		selfOrder.setRemain(selfOrder.getRemain().subtract(selfOrder.getCurAmount()));
		selfOrder.setRemainVolume(selfOrder.getRemainVolume().subtract(selfOrder.getCurVolume()));
		selfOrder.setLockVolume(selfOrder.getLockVolume().add(selfOrder.getCurVolume()));
		selfOrder.setLockAmount(selfOrder.getLockAmount().add(selfOrder.getCurAmount()));
		// 创建交易后挂单方剩余额如果小于最小交易额就撤单
		opposite.setRemainVolume(opposite.getRemainVolume().subtract(curvolume));
		if (opposite.getRemainVolume().multiply(opposite.getPrice()).compareTo(opposite.getMinQuote()) < 0) {
			resp = cancelOrder(opposite);
		}
		return resp;
	}

	/**
	 * 吃单交易操作 市价交易才需要，暂时留着
	 * 
	 * @param selfOrder 吃单方订单
	 * @param opposite  市价单交易这个订单为null
	 * @param page      市价单查询挂单用 第一次调用为1
	 */
	private void marketCreateTrade(OTCOrderDTO selfOrder, OTCOrderDTO opposite, int page) {
		List<OTCOrderDTO> list = new ArrayList<OTCOrderDTO>();
		if (selfOrder.getPriceType().getType() == PriceTypeEnum.LIMIT.getType()) {
			list.add(opposite);
		} else {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("sortname", "price, create_time");
			param.put("pagesize", "10");
			param.put("page", page);
			param.put("status", "WATTING");
			param.put("baseCurrency", selfOrder.getBaseCurrency());
			param.put("quoteCurrency", selfOrder.getQuoteCurrency());
			if (OrderTypeEnum.BUY.getType() == selfOrder.getType().getType()) {
				param.put("type", OrderTypeEnum.SELL);
				param.put("sortorder", "desc, desc");
			} else {
				param.put("type", OrderTypeEnum.BUY);
				param.put("sortorder", "asc, desc");
			}
			list = oTCOrderService.findByConditionPage(param);
		}

		if (list == null || list.size() < 1) {
			return;
		}
		for (OTCOrderDTO dto : list) {
			// 吃单最大交易额小于挂单最小交易额直接退出
			if (selfOrder.getMaxQuote().compareTo(dto.getMinQuote()) < 0) {
				return;
			}
			String lockKey = OTCRedisLockConstants.OTC_ORDER_TRADE_REDIS_LOCK_PRE + String.valueOf(dto.getId());
			boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
					OTCRedisLockConstants.OTC_ORDER_TRADE_REDIS_LOCK_EXPIRE_TIME, OTCRedisLockConstants.REDIS_LOCK_RETRY_10,
					OTCRedisLockConstants.REDIS_LOCK_INTERVAL_100);
			if (isLock) {
				try {
					simpleCreateTrade(dto, selfOrder);
				} finally {
					JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
				}
			} else {
				TradeLogger.warn(" Order:" + selfOrder.getId() + " Don`t get lock!" + lockKey);
			}
		}
		if (selfOrder.getRemainVolume().compareTo(BigDecimal.ZERO) > 0
				&& selfOrder.getPriceType().getType() == PriceTypeEnum.MARKET.getType()) {
			marketCreateTrade(selfOrder, null, ++page);
		}
	}

	public int updateByPrimaryKeySelective(OTCTradeDTO record) {
		int i = 0;
		i = oTCTradeService.updateByPrimaryKeySelective(record);
		return i;
	}
}
