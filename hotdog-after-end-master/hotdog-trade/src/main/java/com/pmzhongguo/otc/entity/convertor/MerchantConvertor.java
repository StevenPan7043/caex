package com.pmzhongguo.otc.entity.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.entity.dataobject.MerchantDO;
import com.pmzhongguo.otc.entity.dto.MerchantDTO;
import com.pmzhongguo.otc.otcenum.AuditStatusEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;

public class MerchantConvertor {
	
	public static MerchantDO DTO2DO(MerchantDTO merchantDTO) {
		MerchantDO merchantDO = new MerchantDO();
		merchantDO.setId(merchantDTO.getId());
		merchantDO.setMemberId(merchantDTO.getMemberId());
		if(!HelpUtils.nullOrBlank(merchantDTO.getDepositCurrency())) {
			merchantDO.setDepositCurrency(merchantDTO.getDepositCurrency().toUpperCase());
		}
		merchantDO.setDepositVolume(merchantDTO.getDepositVolume());
		if(merchantDTO.getStatus() != null) {
			merchantDO.setStatus(merchantDTO.getStatus().getType());
		}
		merchantDO.setCreateTime(merchantDTO.getCreateTime());
		merchantDO.setModifyTime(merchantDTO.getModifyTime());
		merchantDO.setMemo(merchantDTO.getMemo());
		merchantDO.setVip(merchantDTO.getVip());
		if(merchantDTO.getIsDeposit() != null) {
			merchantDO.setIsDeposit(merchantDTO.getIsDeposit().getType());
		}
		merchantDO.setLast_login_time(merchantDTO.getLast_login_time());
		return merchantDO;
	}
	
	public static MerchantDTO DO2DTO(MerchantDO merchantDO){
		MerchantDTO merchantDTO = new MerchantDTO();
		merchantDTO.setId(merchantDO.getId());
		merchantDTO.setMemberId(merchantDO.getMemberId());
		merchantDTO.setDepositCurrency(merchantDO.getDepositCurrency());
		merchantDTO.setDepositVolume(merchantDO.getDepositVolume());
		merchantDTO.setStatus(AuditStatusEnum.getEnumByType(merchantDO.getStatus()));
		merchantDTO.setCreateTime(merchantDO.getCreateTime());
		merchantDTO.setModifyTime(merchantDO.getModifyTime());
		merchantDTO.setMemo(merchantDO.getMemo());
		merchantDTO.setIsDeposit(WhetherEnum.getEnumByType(merchantDO.getIsDeposit()));
		merchantDTO.setmName(merchantDO.getmName());
		merchantDTO.setLast_login_time(merchantDO.getLast_login_time());
		merchantDTO.setVip(merchantDO.getVip());
		return merchantDTO;
	}
	
	public static List<MerchantDO> DTO2DO(List<MerchantDTO> dtoList){
		List<MerchantDO> doList = new ArrayList<MerchantDO>();
		for(MerchantDTO m : dtoList) {
			doList.add(DTO2DO(m));
		}
		return doList;
	}
	
	public static List<MerchantDTO> DO2DTO(List<MerchantDO> doList){
		List<MerchantDTO> dotList = new ArrayList<MerchantDTO>();
		for(MerchantDO m : doList) {
			dotList.add(DO2DTO(m));
		}
		return dotList;
	}

	/**
	 * 将查询条件map里的枚举转换为枚举type
	 * 
	 * @param param
	 */
	public static void initMap(Map<String, Object> param) {
		
		if (!HelpUtils.isMapNullValue(param, "isDeposit")) {
			WhetherEnum whetherEnum = WhetherEnum.getWhetherEnum(String.valueOf(param.get("isDeposit")));
			if (whetherEnum != null) {
				param.put("isDeposit", whetherEnum.getType());
			}
		}
		
		if (!HelpUtils.isMapNullValue(param, "status")) {
			AuditStatusEnum auditStatusEnum = AuditStatusEnum.getAuditStatusEnum(String.valueOf(param.get("status")));
			if (auditStatusEnum != null) {
				param.put("status", auditStatusEnum.getType());
			}
		}
		
		if (!HelpUtils.isMapNullValue(param, "depositCurrency")) {
			param.put("depositCurrency", param.get("depositCurrency").toString().toUpperCase());
		}
	}

}
