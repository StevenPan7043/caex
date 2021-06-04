package com.pmzhongguo.otc.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.web.OptSourceEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.business.dto.AccountDetailDto;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisLock;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.entity.dto.AccountDTO;
import com.pmzhongguo.otc.entity.dto.OTCAccountDetailDTO;
import com.pmzhongguo.otc.otcenum.AccountOperateTypeEnum;
import com.pmzhongguo.otc.utils.OTCRedisLockConstants;

@Service
@Transactional
public class OTCAccountManager {
	
	protected Logger TradeLogger = LoggerFactory.getLogger("tradeInfo");
	
	@Autowired
	private OTCAccountService oTCAccountService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private OTCAccountDetailManager oTCAccountDetailManager;
	
	/**
	 * 	会员资产划转
	 * @param memberId
	 * @param currency
	 * @param num
	 * @return
	 */
	public ObjResp accountTransfer(Integer memberId, String currency, BigDecimal num, AccountOperateTypeEnum operate) {
		ObjResp result = new ObjResp();
		String lockKey = OTCRedisLockConstants.OTC_ACCOUNT_REDIS_LOCK_PRE + String.valueOf(memberId) + currency;
		boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
				OTCRedisLockConstants.OTC_ACCOUNT_REDIS_LOCK_EXPIRE_TIME, OTCRedisLockConstants.REDIS_LOCK_RETRY_10, OTCRedisLockConstants.REDIS_LOCK_INTERVAL_100);
		if (isLock) {
			try {
				result = assetChange(memberId, currency, num, operate);
				memberService.accountProc(num.negate(), currency, memberId, 3, OptSourceEnum.OTC);
			} finally {
				JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
			}
		} else {
			result = new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg(), lockKey + " Don`t get lock!");
		}
		failThrowException(result);
		return result;
	}
	
	/**
	 * 	查询会员资产信息
	 * @param params
	 * @return
	 */
	public ObjResp getAccount(Map<String, Object> params) {
		List<AccountDTO> list =  oTCAccountService.findByConditionPage(params);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
	}

	/**
	 * 冻结或解冻、增加余额
	 * 
	 * @param memberId
	 * @param currency
	 * @param num
	 * @param procType
	 * @return
	 */
	public ObjResp assetChange(Integer memberId, String currency, BigDecimal num, AccountOperateTypeEnum procType) {
		ObjResp result = verifyAccount(memberId, currency);

		if (result.getState().intValue() != Resp.SUCCESS.intValue()
				&& procType.getType() != AccountOperateTypeEnum.ADDTOTAL.getType()
				&& procType.getType() != AccountOperateTypeEnum.INTO.getType()
				&& procType.getType() != AccountOperateTypeEnum.SECEDE.getType()) {
			return result;
		}
		String lockKey = OTCRedisLockConstants.OTC_ACCOUNT_REDIS_LOCK_PRE + String.valueOf(memberId) + "_" + currency;
		boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
				OTCRedisLockConstants.OTC_ACCOUNT_REDIS_LOCK_EXPIRE_TIME, OTCRedisLockConstants.REDIS_LOCK_RETRY_10, OTCRedisLockConstants.REDIS_LOCK_INTERVAL_100);
		ObjResp returnResult =new ObjResp();
		if (isLock) {
			try {
				switch (procType.getType()) {
					case 1:
					case 5:
					case 6:
					case 10:
						returnResult = oTCAccountService.addTotalBalance(memberId, currency, num, result);
						break;
					case 2:
					case 7:
						returnResult = oTCAccountService.addFrozenBalance(num, result);
						break;
					case 3:
					case 9:
						returnResult = oTCAccountService.returnFrozenBalance(num, result);
						break;
					case 4:
					case 8:
						returnResult = oTCAccountService.reduceFrozenBalance(num, result);
						break;
				}
			} finally {
				JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
			}
		} else {
			returnResult = new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg(), lockKey + " Don`t get lock!");
		}

		//记录账户操作
		if (returnResult.getState().equals(1)) {
			AccountDTO accountDTO = (AccountDTO) result.getData();
			oTCAccountDetailManager.insert(new OTCAccountDetailDTO(memberId, currency, num.compareTo(BigDecimal.ZERO) < 0 ? num.negate() : num, procType, accountDTO == null ? "" : accountDTO.toString()));
		}
		return returnResult;
	}
	
	/**
	 * 查询账户信息
	 * 
	 * @param memberId
	 * @param currency
	 * @return State Resp.SUCCESS 查询到账户信息 Data保存账户信息 State Resp.FAIL 未能查询到账户信息
	 */
	public ObjResp verifyAccount(Integer memberId, String currency) {
		boolean b = false;
		AccountDTO accountDTO = oTCAccountService.findBymerchantIdAndCurrency(HelpUtils.newHashMap("currency", currency.toUpperCase(), "memberId", memberId));
		ObjResp result = new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorENMsg(), null);
		b = accountDTO != null;
		if (b) {
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, accountDTO);
		}
		return result;
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
}
