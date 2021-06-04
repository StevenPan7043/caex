package com.pmzhongguo.otc.entity.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.entity.dataobject.OTCAccountDetailDO;
import com.pmzhongguo.otc.entity.dto.OTCAccountDetailDTO;
import com.pmzhongguo.otc.otcenum.AccountOperateTypeEnum;

public class OTCAccountDetailConvertor {

	public static OTCAccountDetailDO DTO2DO(OTCAccountDetailDTO oTCAccountDetailDTO) {
		OTCAccountDetailDO oTCAccountDetailDO = new OTCAccountDetailDO();
		oTCAccountDetailDO.setId(oTCAccountDetailDTO.getId());
		if(!HelpUtils.nullOrBlank(oTCAccountDetailDTO.getCurrency())) {
			oTCAccountDetailDO.setCurrency(oTCAccountDetailDTO.getCurrency().toUpperCase());
		}
		oTCAccountDetailDO.setMemberId(oTCAccountDetailDTO.getMemberId());
		oTCAccountDetailDO.setNum(oTCAccountDetailDTO.getNum());
		oTCAccountDetailDO.setBeforeInfo(oTCAccountDetailDTO.getBeforeInfo());
		oTCAccountDetailDO.setCreateTime(oTCAccountDetailDTO.getCreateTime());
		if (oTCAccountDetailDTO.getProcType() != null) {
			oTCAccountDetailDO.setProcType(oTCAccountDetailDTO.getProcType().getType());
		}
		return oTCAccountDetailDO;
	}

	public static OTCAccountDetailDTO DO2DTO(OTCAccountDetailDO oTCAccountDetailDO) {
		OTCAccountDetailDTO oTCAccountDetailDTO = new OTCAccountDetailDTO();
		oTCAccountDetailDTO.setId(oTCAccountDetailDO.getId());
		oTCAccountDetailDTO.setCurrency(oTCAccountDetailDO.getCurrency());
		oTCAccountDetailDTO.setMemberId(oTCAccountDetailDO.getMemberId());
		oTCAccountDetailDTO.setNum(oTCAccountDetailDO.getNum());
		oTCAccountDetailDTO.setBeforeInfo(oTCAccountDetailDO.getBeforeInfo());
		oTCAccountDetailDTO.setCreateTime(oTCAccountDetailDO.getCreateTime());
		oTCAccountDetailDTO.setProcType(AccountOperateTypeEnum.getEnumByType(oTCAccountDetailDO.getProcType()));
		return oTCAccountDetailDTO;
	}

	public static List<OTCAccountDetailDO> DTO2DO(List<OTCAccountDetailDTO> dtoList) {
		List<OTCAccountDetailDO> doList = new ArrayList<OTCAccountDetailDO>();
		for (OTCAccountDetailDTO a : dtoList) {
			doList.add(DTO2DO(a));
		}
		return doList;
	}

	public static List<OTCAccountDetailDTO> DO2DTO(List<OTCAccountDetailDO> doList) {
		List<OTCAccountDetailDTO> dotList = new ArrayList<OTCAccountDetailDTO>();
		for (OTCAccountDetailDO a : doList) {
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
		if (!HelpUtils.isMapNullValue(param, "procType")) {
			AccountOperateTypeEnum accountOperateTypeEnum = AccountOperateTypeEnum.getAccountOperateTypeEnum(String.valueOf(param.get("procType")));
			if (accountOperateTypeEnum != null) {
				param.put("procType", accountOperateTypeEnum.getType());
			}
		}
	}
}
