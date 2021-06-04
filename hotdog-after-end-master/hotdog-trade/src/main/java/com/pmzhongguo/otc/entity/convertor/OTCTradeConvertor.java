package com.pmzhongguo.otc.entity.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.entity.dataobject.OTCTradeDO;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.otcenum.ComplainTypeEnum;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.TakerEnum;
import com.pmzhongguo.otc.otcenum.TradeStatusEnum;
import com.qiniu.util.BeanUtil;

public class OTCTradeConvertor {

	public static OTCTradeDO DTO2DO(OTCTradeDTO oTCTradeDTO) {
		OTCTradeDO oTCTradeDO = new OTCTradeDO();
		if(!HelpUtils.nullOrBlank(oTCTradeDTO.getBaseCurrency())) {
			oTCTradeDO.setBaseCurrency(oTCTradeDTO.getBaseCurrency().toUpperCase());
		}
		oTCTradeDO.setCreateTime(oTCTradeDTO.getCreateTime());
		oTCTradeDO.setModifyTime(oTCTradeDTO.getModifyTime());
		oTCTradeDO.setFee(oTCTradeDTO.getFee());
		oTCTradeDO.setFeeCurrency(oTCTradeDTO.getFeeCurrency());
		oTCTradeDO.setId(oTCTradeDTO.getId());
		oTCTradeDO.setMemberId(oTCTradeDTO.getMemberId());
		oTCTradeDO.setoId(oTCTradeDTO.getoId());
		oTCTradeDO.setOppositeOId(oTCTradeDTO.getOppositeOId());
		oTCTradeDO.setOppositeTId(oTCTradeDTO.getOppositeTId());
		oTCTradeDO.setPrice(oTCTradeDTO.getPrice());
		if(!HelpUtils.nullOrBlank(oTCTradeDTO.getQuoteCurrency())) {
			oTCTradeDO.setQuoteCurrency(oTCTradeDTO.getQuoteCurrency().toUpperCase());
		}
		if(oTCTradeDTO.getStatus() != null) {
			oTCTradeDO.setStatus(oTCTradeDTO.getStatus().getType());
		}
		if(oTCTradeDTO.getTaker() != null) {
			oTCTradeDO.setTaker(oTCTradeDTO.getTaker().getType());
		}
		if(oTCTradeDTO.gettType() != null) {
			oTCTradeDO.settType(oTCTradeDTO.gettType().getType());
		}
		oTCTradeDO.setVolume(oTCTradeDTO.getVolume());
		oTCTradeDO.setPayTime(oTCTradeDTO.getPayTime());
		oTCTradeDO.setPaymentTime(oTCTradeDTO.getPaymentTime());
		oTCTradeDO.setDoneTime(oTCTradeDTO.getDoneTime());
		oTCTradeDO.setConsumingTime(oTCTradeDTO.getConsumingTime());
		oTCTradeDO.setAcountId(oTCTradeDTO.getAcountId());
		oTCTradeDO.setMemo(oTCTradeDTO.getMemo());
		oTCTradeDO.setOpposite_nick_name(oTCTradeDTO.getOpposite_nick_name());
		oTCTradeDO.setOpposite_member_id(oTCTradeDTO.getOpposite_member_id());
		if(oTCTradeDTO.getComplainType() != null) {
			oTCTradeDO.setComplainType(oTCTradeDTO.getComplainType().getType());
		}
		oTCTradeDO.settNumber(oTCTradeDTO.gettNumber());
		return oTCTradeDO;
	}

	public static OTCTradeDTO DO2DTO(OTCTradeDO oTCTradeDO) {
		OTCTradeDTO oTCTradeDTO = new OTCTradeDTO();
		oTCTradeDTO.setBaseCurrency(oTCTradeDO.getBaseCurrency());
		oTCTradeDTO.setCreateTime(oTCTradeDO.getCreateTime());
		oTCTradeDTO.setModifyTime(oTCTradeDO.getModifyTime());
		oTCTradeDTO.setFee(oTCTradeDO.getFee());
		oTCTradeDTO.setFeeCurrency(oTCTradeDO.getFeeCurrency());
		oTCTradeDTO.setId(oTCTradeDO.getId());
		oTCTradeDTO.setMemberId(oTCTradeDO.getMemberId());
		oTCTradeDTO.setoId(oTCTradeDO.getoId());
		oTCTradeDTO.setOppositeOId(oTCTradeDO.getOppositeOId());
		oTCTradeDTO.setOppositeTId(oTCTradeDO.getOppositeTId());
		oTCTradeDTO.setPrice(oTCTradeDO.getPrice());
		oTCTradeDTO.setQuoteCurrency(oTCTradeDO.getQuoteCurrency());
		oTCTradeDTO.setStatus(TradeStatusEnum.getEnumByType(oTCTradeDO.getStatus()));
		oTCTradeDTO.setTaker(TakerEnum.getEnumByType(oTCTradeDO.getTaker()));
		oTCTradeDTO.settType(OrderTypeEnum.getEnumByType(oTCTradeDO.gettType()));
		oTCTradeDTO.setVolume(oTCTradeDO.getVolume());
		oTCTradeDTO.setPayTime(oTCTradeDO.getPayTime());
		oTCTradeDTO.setPaymentTime(oTCTradeDO.getPaymentTime());
		oTCTradeDTO.setDoneTime(oTCTradeDO.getDoneTime());
		oTCTradeDTO.setConsumingTime(oTCTradeDO.getConsumingTime());
		oTCTradeDTO.setAcountId(oTCTradeDO.getAcountId());
		oTCTradeDTO.setMemo(oTCTradeDO.getMemo());
		oTCTradeDTO.setOpposite_nick_name(oTCTradeDO.getOpposite_nick_name());
		oTCTradeDTO.setOpposite_member_id(oTCTradeDO.getOpposite_member_id());
		oTCTradeDTO.setMemoStr(oTCTradeDO.getMemoStr());
		oTCTradeDTO.setOppositeStatus(oTCTradeDO.getOppositeStatus() == 0 ? "" : TradeStatusEnum.getEnumByType(oTCTradeDO.getOppositeStatus()).getCode());
		if(oTCTradeDO.getComplainType() != null) {
			oTCTradeDTO.setComplainType(ComplainTypeEnum.getEnumByType(oTCTradeDO.getComplainType()));
		}
		oTCTradeDTO.settNumber(oTCTradeDO.gettNumber());
		oTCTradeDTO.setmName(oTCTradeDO.getmName());;
		return oTCTradeDTO;
	}

	public static List<OTCTradeDO> DTO2DO(List<OTCTradeDTO> dtoList) {
		List<OTCTradeDO> doList = new ArrayList<OTCTradeDO>();
		for (OTCTradeDTO a : dtoList) {
			doList.add(DTO2DO(a));
		}
		return doList;
	}

	public static List<OTCTradeDTO> DO2DTO(List<OTCTradeDO> doList) {
		List<OTCTradeDTO> dotList = new ArrayList<OTCTradeDTO>();
		for (OTCTradeDO a : doList) {
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
		if (!HelpUtils.isMapNullValue(param, "tType")) {
			OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(String.valueOf(param.get("tType")));
			if (orderTypeEnum != null) {
				param.put("tType", orderTypeEnum.getType());
			}
		}
		if (!HelpUtils.isMapNullValue(param, "status")) {
			TradeStatusEnum tradeStatusEnum = TradeStatusEnum.getTradeStatusEnum(String.valueOf(param.get("status")));
			if (tradeStatusEnum != null) {
				param.put("status", tradeStatusEnum.getType());
			}
		}
		if (!HelpUtils.isMapNullValue(param, "complainType")) {
			ComplainTypeEnum complainTypeEnum = ComplainTypeEnum.getComplainTypeEnum(String.valueOf(param.get("complainType")));
			if (complainTypeEnum != null) {
				param.put("complainType", complainTypeEnum.getType());
			}
		}
	}
}
