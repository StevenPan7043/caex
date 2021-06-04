package com.pmzhongguo.otc.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.dao.AccountMapper;
import com.pmzhongguo.otc.entity.convertor.AccountConvertor;
import com.pmzhongguo.otc.entity.dataobject.AccountDO;
import com.pmzhongguo.otc.entity.dto.AccountDTO;

@Service
@Transactional
public class OTCAccountService {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AccountMapper accountMapper;

	/**
	 * 为商家添加币种资产信息
	 * 
	 * @param record
	 * @return
	 */
	public int insert(AccountDTO record) {
		AccountDO accountDO = AccountConvertor.DTO2DO(record);
		accountMapper.insert(accountDO);
		return accountDO.getId() == null ? 0 : accountDO.getId();
	}

	/**
	 * 根据id查找商家币种账户信息
	 * 
	 * @param id
	 * @return
	 */
	public AccountDTO findById(Integer id) {
		AccountDO accountDo = accountMapper.findById(id);
		return accountDo == null ? null : AccountConvertor.DO2DTO(accountDo);
	}

	public List<AccountDTO> findByConditionPage(Map<String, Object> param) {
		AccountConvertor.initMap(param);
		List<AccountDO> list = accountMapper.findByConditionPage(param);
		
		return CollectionUtils.isEmpty(list) ? null : AccountConvertor.DO2DTO(list);
	}
	
	public AccountDTO findBymerchantIdAndCurrency(Map<String, Object> param) {
		List<AccountDTO> list = findByConditionPage(param);
		AccountDTO accountDto = CollectionUtils.isEmpty(list) ? null : list.get(0);
		return accountDto;
	}

	/**
	 * 根据id删除一条商家币种账户信息
	 * 
	 * @param id
	 * @return
	 */
	public int deleteByPrimaryKey(Integer id) {
		int i = 0;
		if (id == null) {
			return i;
		}
		i = accountMapper.deleteByPrimaryKey(id);
		return i;
	}

	/**
	 * 更新一条商家币种账户信息
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(AccountDTO record) {
		int i = 0;
		if (record.getId() == null) {
			return i;
		}
		AccountDO accountDO = AccountConvertor.DTO2DO(record);
		i = accountMapper.updateByPrimaryKeySelective(accountDO);
		return i;
	}

	/**
	 * 增加总额[+total_balance] total_balance 存需要增加的数据
	 * 
	 * @param merchantId
	 * @param currency
	 * @param totalBalance
	 * @param result
	 * @return
	 */
	public ObjResp addTotalBalance(Integer memberId, String currency, BigDecimal totalBalance, ObjResp result) {
		AccountDTO account = new AccountDTO();
		int i = 0;
		if (result.getState().intValue() == 1) {
			AccountDTO old = (AccountDTO) result.getData();
			if(old.getTotalBalance().subtract(old.getFrozenBalance()).add(totalBalance).compareTo(BigDecimal.ZERO) >= 0) {
				account.setId(old.getId());
				account.setTotalBalance(totalBalance);
				AccountDO accountDO = AccountConvertor.DTO2DO(account);
				i = accountMapper.addTotalBalance(accountDO);
			}
		} else {
			if(totalBalance.compareTo(BigDecimal.ZERO) >= 0) {
				account.setFrozenBalance(BigDecimal.ZERO);
				account.setCurrency(currency);
				account.setMemberId(memberId);
				account.setTotalBalance(totalBalance);
				i = insert(account);
			}
		}
		if (i > 0) {
			result = new  ObjResp();
		} else {
			result.setState(Resp.FAIL);
			result.setMsg("法币增加总额失败！");
			result.setData("memberId:" + memberId + " currency:" + currency + " num:" + totalBalance.stripTrailingZeros().toPlainString());
			logger.warn(result.getMsg() + result.getData());
		}
		return result;
	}

	/**
	 * 冻结[+frozen_balance] frozen_balance 存需要冻结的数
	 * 
	 * @param frozenBalance
	 * @param result
	 * @return
	 */
	public ObjResp addFrozenBalance(BigDecimal frozenBalance, ObjResp result) {
		AccountDTO current = (AccountDTO) result.getData();
		BigDecimal availableBalance = current.getTotalBalance().subtract(current.getFrozenBalance());
		// 可用余额不足
		if (availableBalance.subtract(frozenBalance).compareTo(BigDecimal.ZERO) < 0) {
			String errorMsg = "可用余额不足！memberId:" + current.getMemberId() + " " + current.getCurrency()
					+ " total:" + current.getTotalBalance().toPlainString() + " frozen:"
					+ current.getFrozenBalance().toPlainString() + ", Num:" + frozenBalance.toPlainString();
			logger.warn("OTCAccountService#addFrozenBalance: {}",errorMsg);
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg(), null);
		}
		AccountDTO account = new AccountDTO();
		account.setId(current.getId());
		account.setFrozenBalance(frozenBalance);
		AccountDO accountDO = AccountConvertor.DTO2DO(account);
		int i = accountMapper.addFrozenBalance(accountDO);
		if (i > 0) {
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
		}
		return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg(), null);
	}

	/**
	 * 还原冻结[-frozen_balance] frozen_balance 存需要还原冻结的数 也就是取消冻结
	 * 
	 * @param frozenBalance
	 * @param result
	 * @return
	 */
	public ObjResp returnFrozenBalance(BigDecimal frozenBalance, ObjResp result) {
		AccountDTO current = (AccountDTO) result.getData();
		if (current.getFrozenBalance().compareTo(frozenBalance) < 0) {
			String errorMsg = current.getCurrency() + "'s otc account(" + current.getMemberId() + ") error, frozen:"
					+ current.getFrozenBalance().toPlainString() + ", returnNum:" + frozenBalance.toPlainString();
            logger.warn("OTCAccountService#returnFrozenBalance: {}",errorMsg);
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg(), errorMsg);
		}
		AccountDTO account = new AccountDTO();
		account.setId(current.getId());
		account.setFrozenBalance(frozenBalance);
		AccountDO accountDO = AccountConvertor.DTO2DO(account);
		int i = accountMapper.returnFrozenBalance(accountDO);
		if (i > 0) {
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
		}
		return new ObjResp(Resp.FAIL,ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg() , null);
	}

	/**
	 * 解冻[-frozen_balance，同时-total_balance] 还原冻结和解冻的区别是：解冻是成交，还原冻结是取消 frozen_balance
	 * 和 total_balance 存解冻的数
	 * 
	 * @param account
	 * @return
	 */
	public ObjResp reduceFrozenBalance(BigDecimal frozenBalance, ObjResp result) {
		AccountDTO current = (AccountDTO) result.getData();
		if (current.getTotalBalance().compareTo(frozenBalance) == -1
				|| current.getFrozenBalance().compareTo(frozenBalance) == -1) {
			String errorMsg = current.getCurrency() + "'s otc account(" + current.getMemberId() + ") error, total:"
					+ current.getTotalBalance().toPlainString() + ", frozen:"
					+ current.getFrozenBalance().toPlainString() + ", reduceNum:" + frozenBalance.toPlainString();
			logger.warn(errorMsg);
			return new ObjResp(Resp.FAIL, errorMsg, null);
		}
		AccountDTO account = new AccountDTO();
		account.setId(current.getId());
		account.setTotalBalance(frozenBalance);
		account.setFrozenBalance(frozenBalance);
		AccountDO accountDO = AccountConvertor.DTO2DO(account);
		int i = accountMapper.reduceFrozenBalance(accountDO);
		if (i > 0) {
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
		}
		return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg(), null);
	}
}
