package com.pmzhongguo.ex.business.service;


import com.google.common.collect.ImmutableMap;
import com.pmzhongguo.ex.business.dto.CurrencyLockReleaseDto;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.mapper.LockAccountMapper;
import com.pmzhongguo.ex.business.mapper.MemberMapper;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.core.rediskey.CurrencyLockBuyTransferKey;
import com.pmzhongguo.ex.core.service.BaseService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.*;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.zzextool.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *	买入释放一定比例锁仓账号资产到待释放资产，卖则把待释放资产全部还原到锁仓账号
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Service
@Transactional
public class CurrencyLockAccountService extends BaseService<LockAccount> {

	@Autowired
	private LockAccountMapper lockAccountMapper;

	@Autowired
	private MemberService memberService;

	@Autowired
	private ExService exService;


	@Autowired
	private CurrencyLockReleaseService currencyLockReleaseService;

	@Autowired
	private CurrencyLockAccountDetailService currencyLockAccountDetailService;

	@Autowired
	private RuleServiceManage ruleServiceManage;


	private static Logger logger = LoggerFactory.getLogger(CurrencyLockAccountService.class);

	@Override
	public SuperMapper<LockAccount> getMapper() {
		return lockAccountMapper;
	}

	/**
	 * 查询
	 * @param params member_id,currency
	 */
	public LockAccount findByMemberIdAndCurrency(Map<String,Object> params) {
		return lockAccountMapper.findByMemberIdAndCurrency(params);
	}

	/**
	 * 释放锁仓币种数量，用户买就从锁账号释放对应的量，用户卖就从释放锁对应的量
	 * @param orderId 订单id
	 * @param webOrder 网页订单详情
	 */

	public void releaseLockCurrency(Long orderId,Order webOrder) {
		if (orderId < 1) {
			return;
		}
		// 1. 判断是否锁仓币种
		boolean isLockCurrency = HelpUtils.isLockCurrency(webOrder.getBase_currency());
		if (!isLockCurrency) {
			return;
		}
		// todo 暂时写死gbt,后面要通过规则判断
		Rule rule = RuleServiceManage.getCurrencyRuleByCache(webOrder.getBase_currency());
		if (null != rule && (!rule.getRuleType().equals(LockRuleTypeEnum.TRADE.getCodeCn()) && !rule.getRuleType().equals(LockRuleTypeEnum.TRADE.getType() + ""))) {
			return;
		}
		// 是否有规则
//		Rule rule = RuleServiceManage.getCurrencyRuleByCache(webOrder.getBase_currency());
//		if (null == rule) {
//			return;
//		}
		Currency currency = HelpUtils.getCurrencyMap().get(webOrder.getBase_currency());
		// 根据 table_name,o_on,order_id,member_id 找到订单
		Order dbOrder = exService.getOrder(webOrder);

		// 2. 买就直接处理我方，卖要处理反方
		if (OrderTypeEnum.BUY.getCode().equals(webOrder.getO_type())) {
			handlerBuyOperation(dbOrder,currency);

		} else if (OrderTypeEnum.SELL.getCode().equals(webOrder.getO_type())) {
			handlerSellOperation(dbOrder,currency);
		}

	}

	/**
	 * 处理买
	 * @param dbOrder
	 * @param currency
	 */
	private void handlerBuyOperation(Order dbOrder,Currency currency) {
		BigDecimal totalVolume = dbOrder.getDone_volume();
		//	交易为0，不处理
		if (totalVolume.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		CurrencyLockBuyTransferKey lockKey = new CurrencyLockBuyTransferKey();
		String redisKey = lockKey.getRedisKey(currency.getCurrency(),dbOrder.getMember_id());
		boolean isLock = JedisUtil.getInstance().getLockRetry(redisKey, IPAddressPortUtil.IP_ADDRESS
				, lockKey.getExpireSeconds(), 200, 30);
		// 资产操作
		if (isLock) {
			try {
				// 判断锁仓是否有该币种账号
				LockAccount lockAccount = findByMemberIdAndCurrency(ImmutableMap.of(
						"currency", currency.getCurrency()
						, "member_id", dbOrder.getMember_id()));
				// 有锁仓账号或锁仓资产大于0 划转买家锁仓资产到待释放账号
				if (lockAccount != null && lockAccount.getLock_num().compareTo(BigDecimal.ZERO) > 0) {
					releaseLockCurrencyWithMemberAssets(totalVolume, lockAccount, currency);
				}

				// 卖家待释放划转到锁仓
				handlerWaitRelease(dbOrder, currency);
			} finally {
				JedisUtil.getInstance().releaseLock(redisKey, IPAddressPortUtil.IP_ADDRESS);
			}

		}else {
			logger.error("<=== 【{}】购买释放锁仓，处理买，未获得锁",currency.getCurrency());
		}

	}

	/**
	 * 处理卖
	 * @param dbOrder
	 * @param currency
	 */
	private void handlerSellOperation(Order dbOrder,Currency currency) {
		// 找到买家的所有交易数量和member_id
		ImmutableMap<String, Object> paramsMap = ImmutableMap.<String, Object>builder()
				.put("o_id", dbOrder.getId())
				.put("table_name", (dbOrder.getBase_currency() + dbOrder.getQuote_currency()).toLowerCase())
				.put("taker", "opposite")
				.build();
		List<Trade> oppositeTradeDetailList = exService.findOppositeTrades(paramsMap);

		if (CollectionUtils.isEmpty(oppositeTradeDetailList)) {
			return;
		}
		for (Trade oppositeTradeDetail : oppositeTradeDetailList) {

			BigDecimal totalVolume = oppositeTradeDetail.getVolume();
			CurrencyLockBuyTransferKey lockKey = new CurrencyLockBuyTransferKey();
			String redisKey = lockKey.getRedisKey(currency.getCurrency(),dbOrder.getMember_id());
			boolean isLock = JedisUtil.getInstance().getLockRetry(redisKey, IPAddressPortUtil.IP_ADDRESS
					, lockKey.getExpireSeconds(), 200, 30);
			// 资产操作
			if (isLock) {
				try {
					LockAccount lockAccount = findByMemberIdAndCurrency(ImmutableMap.of(
							"currency", currency.getCurrency()
							, "member_id", oppositeTradeDetail.getMember_id()));

					// 没有锁仓账号或锁仓资产小于0  不执行
					if (lockAccount == null || lockAccount.getLock_num().compareTo(BigDecimal.ZERO) < 1) {
						continue;
					}
					// 划转买家锁仓资产到待释放账号
					releaseLockCurrencyWithMemberAssets(totalVolume,lockAccount,currency);
				}finally {
					JedisUtil.getInstance().releaseLock(redisKey, IPAddressPortUtil.IP_ADDRESS);
				}
			}
		}


		ImmutableMap<String,Object> map = ImmutableMap.of(
				"currency", currency.getCurrency()
				, "member_id", dbOrder.getMember_id());
		CurrencyLockBuyTransferKey lockKey = new CurrencyLockBuyTransferKey();
		String redisKey = lockKey.getRedisKey(currency.getCurrency(),dbOrder.getMember_id());
		boolean isLock = JedisUtil.getInstance().getLockRetry(redisKey, IPAddressPortUtil.IP_ADDRESS
				, lockKey.getExpireSeconds(), 200, 30);
		// 资产操作
		if(isLock) {
			try {
				// 将自己的待释放划转到锁仓
				List<CurrencyLockReleaseDto> waitList = currencyLockReleaseService.findWaitReleaseByMemberIdAndCurrency(map);
				if (CollectionUtils.isEmpty(waitList)) {
					return;
				}
				LockAccount lockAccount = findByMemberIdAndCurrency(map);
				addLockCurrencyWithMemberAssets(lockAccount,waitList,currency);
			}finally {
				JedisUtil.getInstance().releaseLock(redisKey, IPAddressPortUtil.IP_ADDRESS);
			}
		}
	}

	/**
	 * 查找该笔订单的反方交易用户
	 * @param dbOrder
	 * @param currency
	 */
	private void handlerWaitRelease(Order dbOrder, Currency currency) {
		// 找到反方的所有交易数量和member_id
		ImmutableMap<String, Object> paramsMap = ImmutableMap.<String, Object>builder()
				.put("o_id", dbOrder.getId())
				.put("table_name", (dbOrder.getBase_currency() + dbOrder.getQuote_currency()).toLowerCase())
				.put("taker", "opposite")
				.build();
		List<Trade> oppositeTradeDetailList = exService.findOppositeTrades(paramsMap);

		if (CollectionUtils.isEmpty(oppositeTradeDetailList)) {
			return;
		}
		for (Trade oppositeTradeDetail : oppositeTradeDetailList) {

			ImmutableMap<String,Object> immutableMap = ImmutableMap.of("member_id", oppositeTradeDetail.getMember_id()
					, "currency", oppositeTradeDetail.getBase_currency());
			List<CurrencyLockReleaseDto> waitList = currencyLockReleaseService.findWaitReleaseByMemberIdAndCurrency(immutableMap);

			// 没有锁仓账号或锁仓资产小于0  不执行
			if (CollectionUtils.isEmpty(waitList)) {
				continue;
			}
			LockAccount lockAccount = findByMemberIdAndCurrency(immutableMap);
			addLockCurrencyWithMemberAssets(lockAccount,waitList,currency);
		}

	}

	/**
	 * 从待释放转到添加锁仓资产,更改待释放记录标记为2
	 * @param lockAccount 用户锁仓账号
	 * @param waitReleaseList 待释放的记录
	 */
	private void addLockCurrencyWithMemberAssets(LockAccount lockAccount
			,List<CurrencyLockReleaseDto> waitReleaseList,Currency currency) {
		BigDecimal tradeVolume = BigDecimal.ZERO;
		for (CurrencyLockReleaseDto lockReleaseDto : waitReleaseList) {
			LockRelease lockRelease = new LockRelease();
			lockRelease.setUpdate_time(DateUtils.formatDate8(new Date()));
			lockRelease.setId(lockReleaseDto.getId());
			lockRelease.setIs_release(LockOperTypeEnum.RETURN.getCode());
			int result = currencyLockReleaseService.updateByIdAndIsRelease(lockRelease);
			if (result > 0) {
				tradeVolume = tradeVolume.add(lockReleaseDto.getReleaseVolume());
			}else {
				logger.warn("<=== 【{}】购买释放，待释放还原到锁仓账号更新失败：【{}】",lockReleaseDto.getCurrency(),lockRelease.toString());
			}

		}

		// 添加 锁仓资产明细
		LockAccountDetail lockAccountDetail = new LockAccountDetail();
		lockAccountDetail.setMember_id(lockAccount.getMember_id());
		lockAccountDetail.setCurrency(currency.getCurrency());
		lockAccountDetail.setOper_num_before(lockAccount.getLock_num());
		lockAccountDetail.setOper_num(lockAccount.getLock_num().add(tradeVolume));
		lockAccountDetail.setCreate_time(HelpUtils.formatDate8(new Date()));
		lockAccountDetail.setType(LockOperTypeEnum.RETURN.getCode());
		currencyLockAccountDetailService.insert(lockAccountDetail);

		lockAccount.setLock_num(lockAccount.getLock_num().add(tradeVolume));
		lockAccount.setUpdate_time(DateUtils.formatDate8(new Date()));
		// 更新用户锁仓资产
		updateById(lockAccount);
	}


	/**
	 * 释放用户锁仓资产
	 * @param tradeVolume 交易的数量
	 * @param currency 释放的币种详情
	 */
	private void releaseLockCurrencyWithMemberAssets(BigDecimal tradeVolume,LockAccount lockAccount,Currency currency) {
		Rule rule = RuleServiceManage.getCurrencyRuleByCache(currency.getCurrency());
		BigDecimal releasePercent = new BigDecimal(rule.getRuleDetail()).divide(new BigDecimal(100));
		BigDecimal releaseVolume = tradeVolume.multiply(releasePercent).setScale(currency.getC_precision(), BigDecimal.ROUND_HALF_DOWN);
		BigDecimal originVolume = lockAccount.getLock_num();
		// 判断锁仓资产是否小于买的数量，防止出现负数
		if(releaseVolume.compareTo(originVolume) > 0) {
			releaseVolume = lockAccount.getLock_num();
		}
		lockAccount.setLock_num(originVolume.subtract(releaseVolume));
		lockAccount.setUpdate_time(HelpUtils.formatDate8(new Date()));
		// 更新锁仓资产
		updateById(lockAccount);
		// 添加 锁仓资产明细
		LockAccountDetail lockAccountDetail = new LockAccountDetail();
		lockAccountDetail.setMember_id(lockAccount.getMember_id());
		lockAccountDetail.setCurrency(currency.getCurrency());
		lockAccountDetail.setOper_num_before(originVolume);
		lockAccountDetail.setOper_num(releaseVolume);
		lockAccountDetail.setCreate_time(HelpUtils.formatDate8(new Date()));
		lockAccountDetail.setType(LockOperTypeEnum.RELEASE.getCode());
		currencyLockAccountDetailService.insert(lockAccountDetail);
		int isRelease = CommonEnum.NO.getCode();
		//  用户添加资产
		boolean immediatlyRelease = rule.getLockReleaseTime() == 0;
		if (immediatlyRelease) {
			// 直接释放
			memberService.accountProc(releaseVolume,currency.getCurrency(),lockAccount.getMember_id(), AccountOperTypeEnum.ADD_BALANCE.getCode(), OptSourceEnum.CURRENCYLOCK);
			isRelease = CommonEnum.YES.getCode();
		}

		//  添加释放记录
        // 释放时间=当前时间+N分钟

		String releaseTime = HelpUtils.dateAddMinFormat8(rule.getLockReleaseTime());
		LockRelease lockRelease = new LockRelease();
		lockRelease.setMember_id(lockAccount.getMember_id());
		lockRelease.setCurrency(currency.getCurrency());
		lockRelease.setRelease_volume(releaseVolume);
		lockRelease.setIs_release(isRelease);
		lockRelease.setCreate_time(HelpUtils.formatDate8(new Date()));
		lockRelease.setUpdate_time(HelpUtils.formatDate8(new Date()));
		lockRelease.setRelease_time(releaseTime);
		lockRelease.setLock_account_detail_id(lockAccountDetail.getId());
		currencyLockReleaseService.insert(lockRelease);

	}

	/**
	 * 添加锁仓账号资产
	 * @param r_amount 添加数量
	 * @param currency 币种
	 * @param member_id 用户id
	 */
	public void addLockAccount(BigDecimal r_amount, String currency, Integer member_id) {
		LockAccount account = findByMemberIdAndCurrency(ImmutableMap.of("member_id", member_id, "currency", currency));
		if(account == null) {
			// 插入明细
			LockAccountDetail lockAccountDetail = new LockAccountDetail(member_id,currency,BigDecimal.ZERO,r_amount
					,HelpUtils.formatDate8(new Date()), LockOperTypeEnum.LOCK.getCode());
			currencyLockAccountDetailService.insert(lockAccountDetail);
			// 直接新增资产
			account = new LockAccount(member_id,currency,r_amount
					,HelpUtils.formatDate8(new Date()),HelpUtils.formatDate8(new Date()));
			lockAccountMapper.insert(account);
		}else {
			// 插入明细
			LockAccountDetail lockAccountDetail = new LockAccountDetail(member_id,currency,account.getLock_num()
					,r_amount,HelpUtils.formatDate8(new Date()),LockOperTypeEnum.LOCK.getCode());
			currencyLockAccountDetailService.insert(lockAccountDetail);
			// 更新资产
			account.setUpdate_time(HelpUtils.formatDate8(new Date()));
			account.setLock_num(account.getLock_num().add(r_amount));
			lockAccountMapper.updateById(account);
		}
		// 用户没有币币账户，需要创建一个，否则资产看不到锁住的数量
		Account bbAccount = memberService.getAccount(ImmutableMap.of("member_id", member_id, "currency", currency));
		if(bbAccount == null) {
			bbAccount = new Account();
			bbAccount.setFrozen_balance(BigDecimal.ZERO);
			bbAccount.setTotal_balance(BigDecimal.ZERO);
			bbAccount.setCurrency(currency);
			bbAccount.setMember_id(member_id);
			memberService.addAccount(bbAccount);
		}

	}


	public List<LockAccount> findByMemberId(Integer member_id) {
		return lockAccountMapper.findByMemberId(member_id);
	}
}
