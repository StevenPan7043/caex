package com.pmzhongguo.otc.entity.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.entity.dataobject.AccountDO;
import com.pmzhongguo.otc.entity.dto.AccountDTO;

public class AccountConvertor {
	
	public static AccountDO DTO2DO(AccountDTO accountDTO) {
		AccountDO accountDO = new AccountDO();
		accountDO.setId(accountDTO.getId());
		if(!HelpUtils.nullOrBlank(accountDTO.getCurrency())) {
			accountDO.setCurrency(accountDTO.getCurrency().toUpperCase());
		}
		accountDO.setMemberId(accountDTO.getMemberId());
		accountDO.setTotalBalance(accountDTO.getTotalBalance());
		accountDO.setFrozenBalance(accountDTO.getFrozenBalance());
		accountDO.setCreateTime(accountDTO.getCreateTime());
		accountDO.setModifyTime(accountDTO.getModifyTime());
		return accountDO;
	}
	
	public static AccountDTO DO2DTO(AccountDO accountDO){
		AccountDTO accountDTO = new AccountDTO();
		accountDTO.setId(accountDO.getId());
		accountDTO.setCurrency(accountDO.getCurrency());
		accountDTO.setMemberId(accountDO.getMemberId());
		accountDTO.setTotalBalance(accountDO.getTotalBalance());
		accountDTO.setFrozenBalance(accountDO.getFrozenBalance());
		accountDTO.setCreateTime(accountDO.getCreateTime());
		accountDTO.setModifyTime(accountDO.getModifyTime());
		return accountDTO;
	}
	
	public static List<AccountDO> DTO2DO(List<AccountDTO> dtoList){
		List<AccountDO> doList = new ArrayList<AccountDO>();
		for(AccountDTO a : dtoList) {
			doList.add(DTO2DO(a));
		}
		return doList;
	}
	
	public static List<AccountDTO> DO2DTO(List<AccountDO> doList){
		List<AccountDTO> dotList = new ArrayList<AccountDTO>();
		for(AccountDO a : doList) {
			dotList.add(DO2DTO(a));
		}
		return dotList;
	}
	
	/**
	 * 将查询条件map里的枚举转换为枚举type
	 * 
	 * @param param
	 */
	public static void initMap(Map<String, Object> param) {
		if (!HelpUtils.isMapNullValue(param, "currency")) {
			param.put("currency", param.get("currency").toString().toUpperCase());
		}
	}
}
