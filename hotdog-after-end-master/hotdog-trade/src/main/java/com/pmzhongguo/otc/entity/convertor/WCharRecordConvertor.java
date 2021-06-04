package com.pmzhongguo.otc.entity.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.entity.dataobject.WCharRecordDO;
import com.pmzhongguo.otc.entity.dto.WCharRecordDTO;
import com.pmzhongguo.otc.otcenum.TakerEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;

public class WCharRecordConvertor {

	public static WCharRecordDO DTO2DO(WCharRecordDTO wCharRecordDTO) {
		WCharRecordDO wCharRecordDO = new WCharRecordDO();
		wCharRecordDO.setCharContent(wCharRecordDTO.getCharContent());
		wCharRecordDO.setCreateTime(wCharRecordDTO.getCreateTime());
		wCharRecordDO.setId(wCharRecordDTO.getId());
		if (wCharRecordDTO.getIsRead() != null) {
			wCharRecordDO.setIsRead(wCharRecordDTO.getIsRead().getType());
		}
		wCharRecordDO.setMemberId(wCharRecordDTO.getMemberId());
		wCharRecordDO.setModifyTime(wCharRecordDTO.getModifyTime());
		wCharRecordDO.setOppositeId(wCharRecordDTO.getOppositeId());
		if (wCharRecordDTO.getTaker() != null) {
			wCharRecordDO.setTaker(wCharRecordDTO.getTaker().getType());
		}
		wCharRecordDO.setTradeId(wCharRecordDTO.getTradeId());
		wCharRecordDO.setOppositeTId(wCharRecordDTO.getOppositeTId());
		return wCharRecordDO;
	}

	public static WCharRecordDTO DO2DTO(WCharRecordDO wCharRecordDO) {
		WCharRecordDTO wCharRecordDTO = new WCharRecordDTO();
		wCharRecordDTO.setCharContent(wCharRecordDO.getCharContent());
		wCharRecordDTO.setCreateTime(wCharRecordDO.getCreateTime());
		wCharRecordDTO.setId(wCharRecordDO.getId());
		wCharRecordDTO.setIsRead(WhetherEnum.getEnumByType(wCharRecordDO.getIsRead()));
		wCharRecordDTO.setMemberId(wCharRecordDO.getMemberId());
		wCharRecordDTO.setModifyTime(wCharRecordDO.getModifyTime());
		wCharRecordDTO.setOppositeId(wCharRecordDO.getOppositeId());
		wCharRecordDTO.setTaker(TakerEnum.getEnumByType(wCharRecordDO.getTaker()));
		wCharRecordDTO.setTradeId(wCharRecordDO.getTradeId());
		wCharRecordDTO.setOppositeTId(wCharRecordDO.getOppositeTId());
		wCharRecordDTO.setNick_name(wCharRecordDO.getNick_name());
		wCharRecordDTO.setOppo_nick_name(wCharRecordDO.getOppo_nick_name());
		return wCharRecordDTO;
	}

	public static List<WCharRecordDO> DTO2DO(List<WCharRecordDTO> dtoList) {
		List<WCharRecordDO> doList = new ArrayList<WCharRecordDO>();
		for (WCharRecordDTO a : dtoList) {
			doList.add(DTO2DO(a));
		}
		return doList;
	}

	public static List<WCharRecordDTO> DO2DTO(List<WCharRecordDO> doList) {
		List<WCharRecordDTO> dotList = new ArrayList<WCharRecordDTO>();
		for (WCharRecordDO a : doList) {
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
		if (!HelpUtils.isMapNullValue(param, "taker")) {
			TakerEnum takerEnum = TakerEnum.getTakerEnum(String.valueOf(param.get("taker")));
			if (takerEnum != null) {
				param.put("taker", takerEnum.getType());
			}
		}
		if (!HelpUtils.isMapNullValue(param, "isRead")) {
			WhetherEnum whetherEnum = WhetherEnum.getWhetherEnum(String.valueOf(param.get("isRead")));
			if (whetherEnum != null) {
				param.put("isRead", whetherEnum.getType());
			}
		}
	}
}
