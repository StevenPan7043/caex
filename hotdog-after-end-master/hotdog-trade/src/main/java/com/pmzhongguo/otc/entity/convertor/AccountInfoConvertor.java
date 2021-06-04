package com.pmzhongguo.otc.entity.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.entity.dataobject.AccountInfoDO;
import com.pmzhongguo.otc.entity.dto.AccountInfoDTO;
import com.pmzhongguo.otc.otcenum.PayTypeEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;

public class AccountInfoConvertor {

	public static AccountInfoDO DTO2DO(AccountInfoDTO accountInfoDTO) {
		AccountInfoDO accountInfoDO = new AccountInfoDO();
		accountInfoDO.setId(accountInfoDTO.getId());
		accountInfoDO.setMemberId(accountInfoDTO.getMemberId());
		accountInfoDO.setName(accountInfoDTO.getName());
		if (accountInfoDTO.getType() != null) {
			accountInfoDO.setType(accountInfoDTO.getType().getType());
		}
		accountInfoDO.setAccount(accountInfoDTO.getAccount());
		accountInfoDO.setBankOrImg(accountInfoDTO.getBankOrImg());
		accountInfoDO.setCreateTime(accountInfoDTO.getCreateTime());
		accountInfoDO.setModifyTime(accountInfoDTO.getModifyTime());
		if (accountInfoDTO.getIsDelete() != null) {
			accountInfoDO.setIsDelete(accountInfoDTO.getIsDelete().getType());
		}
		return accountInfoDO;
	}

	public static AccountInfoDTO DO2DTO(AccountInfoDO accountInfoDO) {
		AccountInfoDTO accountInfoDTO = new AccountInfoDTO();
		accountInfoDTO.setId(accountInfoDO.getId());
		accountInfoDTO.setMemberId(accountInfoDO.getMemberId());
		accountInfoDTO.setName(accountInfoDO.getName());
		accountInfoDTO.setType(PayTypeEnum.getEnumByType(accountInfoDO.getType()));
		accountInfoDTO.setAccount(accountInfoDO.getAccount());
		accountInfoDTO.setBankOrImg(accountInfoDO.getBankOrImg());
		accountInfoDTO.setCreateTime(accountInfoDO.getCreateTime());
		accountInfoDTO.setModifyTime(accountInfoDO.getModifyTime());
		accountInfoDTO.setIsDelete(WhetherEnum.getEnumByType(accountInfoDO.getIsDelete()));
		return accountInfoDTO;
	}

	public static List<AccountInfoDO> DTO2DO(List<AccountInfoDTO> dtoList) {
		List<AccountInfoDO> doList = new ArrayList<AccountInfoDO>();
		for (AccountInfoDTO a : dtoList) {
			doList.add(DTO2DO(a));
		}
		return doList;
	}

	public static List<AccountInfoDTO> DO2DTO(List<AccountInfoDO> doList) {
		List<AccountInfoDTO> dotList = new ArrayList<AccountInfoDTO>();
		for (AccountInfoDO a : doList) {
			dotList.add(DO2DTO(a));
		}
		return dotList;
	}

	
	/**
	 * 将查询条件map里的枚举转换为枚举type
	 * 
	 * @param param
	 */
	public static void mapEnumName2type(Map<String, Object> param) {
		if (!HelpUtils.isMapNullValue(param, "type")) {
			PayTypeEnum payTypeEnum = PayTypeEnum.getPayTypeEnum(String.valueOf(param.get("type")));
			if (payTypeEnum != null) {
				param.put("type", payTypeEnum.getType());
			}
		}
		if (!HelpUtils.isMapNullValue(param, "isDelete")) {
			WhetherEnum isDelete = WhetherEnum.getWhetherEnum(String.valueOf(param.get("isDelete")));
			if (isDelete != null) {
				param.put("isDelete", isDelete.getType());
			}
		}
	}
}
