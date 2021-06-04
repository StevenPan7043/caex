package com.pmzhongguo.otc.entity.convertor;

import java.util.ArrayList;
import java.util.List;

import com.pmzhongguo.otc.entity.dataobject.SSOTokenDO;
import com.pmzhongguo.otc.entity.dto.SSOTokenDTO;

public class SSOTokenConvertor {
	
	public static SSOTokenDO DTO2DO(SSOTokenDTO sSOTokenDTO) {
		SSOTokenDO sSOTokenDO = new SSOTokenDO();
		sSOTokenDO.setApiKey(sSOTokenDTO.getApiKey());
		sSOTokenDO.setApiSecret(sSOTokenDTO.getApiSecret());
		sSOTokenDO.setCreateTime(sSOTokenDTO.getCreateTime());
		sSOTokenDO.setId(sSOTokenDTO.getId());
		sSOTokenDO.setMemberId(sSOTokenDTO.getMemberId());
		sSOTokenDO.setModifyTime(sSOTokenDTO.getModifyTime());
		return sSOTokenDO;
	}
	
	public static SSOTokenDTO DO2DTO(SSOTokenDO sSOTokenDO){
		SSOTokenDTO sSOTokenDTO = new SSOTokenDTO();
		sSOTokenDTO.setApiKey(sSOTokenDO.getApiKey());
		sSOTokenDTO.setApiSecret(sSOTokenDO.getApiSecret());
		sSOTokenDTO.setCreateTime(sSOTokenDO.getCreateTime());
		sSOTokenDTO.setId(sSOTokenDO.getId());
		sSOTokenDTO.setMemberId(sSOTokenDO.getMemberId());
		sSOTokenDTO.setModifyTime(sSOTokenDO.getModifyTime());
		return sSOTokenDTO;
	}
	
	public static List<SSOTokenDO> DTO2DO(List<SSOTokenDTO> dtoList){
		List<SSOTokenDO> doList = new ArrayList<SSOTokenDO>();
		for(SSOTokenDTO a : dtoList) {
			doList.add(DTO2DO(a));
		}
		return doList;
	}
	
	public static List<SSOTokenDTO> DO2DTO(List<SSOTokenDO> doList){
		List<SSOTokenDTO> dotList = new ArrayList<SSOTokenDTO>();
		for(SSOTokenDO a : doList) {
			dotList.add(DO2DTO(a));
		}
		return dotList;
	}

}
