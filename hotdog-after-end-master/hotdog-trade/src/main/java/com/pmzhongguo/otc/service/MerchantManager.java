package com.pmzhongguo.otc.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.entity.dto.MerchantDTO;
import com.pmzhongguo.otc.otcenum.AccountOperateTypeEnum;
import com.pmzhongguo.otc.otcenum.AuditStatusEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class MerchantManager {
	
	protected Logger TradeLogger = LoggerFactory.getLogger("tradeInfo");
	
	private static final String OTC_MERCHANT_REDIS_LOCK_PRE = "otc_merchant_redis_lock_pre_"; // EXPIRE_TIME
	private static final int OTC_MERCHANT_REDIS_LOCK_EXPIRE_TIME = 1 * 1000;
	private static final int OTC_MERCHANT_REDIS_LOCK_RETRY = 10;
	private static final int OTC_MERCHANT_REDIS_LOCK_INTERVAL = 100;
	
	@Autowired
	private MerchantService merchantService;
	
	@Autowired
	private OTCAccountManager oTCAccountManager;
	@Autowired
	private OTCOrderService oTCOrderService;
	@Autowired
	private OTCTradeManager oTCTradeManager;
	public ObjResp auditMerchant(Integer memberId, String currency, BigDecimal num, AuditStatusEnum operate, String memo, WhetherEnum isDeposit) {
		ObjResp result = new ObjResp();
		String lockKey = OTC_MERCHANT_REDIS_LOCK_PRE + String.valueOf(memberId) + currency;
		boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
				OTC_MERCHANT_REDIS_LOCK_EXPIRE_TIME, OTC_MERCHANT_REDIS_LOCK_RETRY, OTC_MERCHANT_REDIS_LOCK_INTERVAL);
		if (isLock) {
			try {
				MerchantDTO record = new MerchantDTO();
				switch (operate.getType()) {
				case 1:
					//申请商家时需要将押金冻结
					result = oTCAccountManager.assetChange(memberId, currency, num, AccountOperateTypeEnum.APPLY);
					failThrowException(result);
					record.setDepositCurrency(currency);
					record.setDepositVolume(num);
					record.setMemberId(memberId);
					record.setStatus(AuditStatusEnum.APPLY_AUDITING);
					record.setIsDeposit(isDeposit);
					int id = insert(record);
					result.setData(id);
					break;
				case 2:
					//商家申请通过需要解冻押金并修改商家信息状态
					result = oTCAccountManager.assetChange(memberId, currency, num, AccountOperateTypeEnum.APPLY_PASSED);
					failThrowException(result);
					record.setMemberId(memberId);
					record.setStatus(AuditStatusEnum.APPLY_PASSED);
					merchantService.updateStatus(record);
					break;
				case 3:
					//商家申请不通过需要还原冻结
					result = oTCAccountManager.assetChange(memberId, currency, num, AccountOperateTypeEnum.APPLY_REJECT);
					failThrowException(result);
					record.setMemberId(memberId);
					record.setMemo(memo);
					record.setStatus(AuditStatusEnum.APPLY_REJECT);
					merchantService.updateStatus(record);
					break;
				case 5:
					//放弃商家资格需要将押金退还会员资金账户
					this.cancelOTCOrder(memberId,1,5);
					result = oTCAccountManager.assetChange(memberId, currency, num, AccountOperateTypeEnum.SECEDE);
					failThrowException(result);
					merchantService.deleteById(HelpUtils.newHashMap("memberId", memberId, "delRemark", StringUtils.isEmpty(memo) ? "" : memo));
					break;
				}
				
			} finally {
				JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
			}
		} else {
			result = new ObjResp(Resp.FAIL, lockKey + " Don`t get lock!", null);
		}
		failThrowException(result);
		return result;
	}

	/**
	 * 订单下架
	 * @param memberId
	 * @param isAds
	 * @param cacelStatus
	 */
	private void cancelOTCOrder(Integer memberId, Integer isAds, Integer cacelStatus) {
		List<OTCOrderDTO> orderDTOS = oTCOrderService.findByConditionPage(HelpUtils.newHashMap("memberId", memberId, "isAds", isAds, "cacelStatus", cacelStatus));
		if (!CollectionUtils.isEmpty(orderDTOS)) {
			for (OTCOrderDTO otcOrderDTOItem : orderDTOS) {
				oTCTradeManager.cancelOrder(otcOrderDTOItem);
			}
		}
	}
	public ObjResp isMerchant(Integer memberId) {
		MerchantDTO dto = getMerchant(memberId);
		if(dto == null || dto.getStatus().getType() != AuditStatusEnum.APPLY_PASSED.getType()) {
			return new ObjResp(Resp.FAIL, "", null);
		}
		return new ObjResp(Resp.SUCCESS, "", null);
	}
	
	public MerchantDTO getMerchant(Integer memberId) {
		List<MerchantDTO> list = merchantService.findByConditionPage(HelpUtils.newHashMap("memberId", memberId));
		if(list == null || list.size() < 1) {
			return null;
		}
		return list.get(0);
	}
	
	/**
	 *  
	 * @param param
	 * @return
	 */
	public List<MerchantDTO> findByConditionPage(Map<String, Object> param){
		List<MerchantDTO> list = merchantService.findByConditionPage(param);
		return list;
	}

	public int insert(MerchantDTO record) {
		return merchantService.insert(record);
	}
	
	public MerchantDTO findById(int id) {
		return merchantService.findById(id);
	}
	
	public int update(MerchantDTO record) {
		return merchantService.updateStatus(record);
	}
	
	public int deleteById(Map<String, Object> params) {
		return merchantService.deleteById(params);
	}
	
	private void failThrowException(ObjResp resp) {
		if (resp.getState().intValue() != Resp.SUCCESS.intValue()) {
			TradeLogger.warn(resp.getMsg());
			throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg());
		}
	}
}
