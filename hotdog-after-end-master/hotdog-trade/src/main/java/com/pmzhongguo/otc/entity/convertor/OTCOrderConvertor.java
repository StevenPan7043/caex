package com.pmzhongguo.otc.entity.convertor;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.entity.dataobject.OTCOrderDO;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.otcenum.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OTCOrderConvertor {

	public static OTCOrderDO DTO2DO(OTCOrderDTO oTCOrderDTO) {
		OTCOrderDO oTCOrderDO = new OTCOrderDO();
		if(!HelpUtils.nullOrBlank(oTCOrderDTO.getBaseCurrency())) {
			oTCOrderDO.setBaseCurrency(oTCOrderDTO.getBaseCurrency().toUpperCase());
		}
		oTCOrderDO.setModifyTime(oTCOrderDTO.getModifyTime());
		oTCOrderDO.setCreateTime(oTCOrderDTO.getCreateTime());
		oTCOrderDO.setDoneVolume(oTCOrderDTO.getDoneVolume());
		oTCOrderDO.setFrozen(oTCOrderDTO.getFrozen());
		oTCOrderDO.setId(oTCOrderDTO.getId());
		oTCOrderDO.setLockAmount(oTCOrderDTO.getLockAmount());
		oTCOrderDO.setLockVolume(oTCOrderDTO.getLockVolume());
		oTCOrderDO.setRemain(oTCOrderDTO.getRemain());
		oTCOrderDO.setMemberId(oTCOrderDTO.getMemberId());
		oTCOrderDO.setMinQuote(oTCOrderDTO.getMinQuote());
		oTCOrderDO.setMaxQuote(oTCOrderDTO.getMaxQuote());
		oTCOrderDO.setPaymentTime(oTCOrderDTO.getPaymentTime());
		oTCOrderDO.setNumber(oTCOrderDTO.getNumber());
		oTCOrderDO.setOperIp(oTCOrderDTO.getOperIp());
		oTCOrderDO.setPrice(oTCOrderDTO.getPrice());
		if (oTCOrderDTO.getPriceType() != null) {
			oTCOrderDO.setPriceType(oTCOrderDTO.getPriceType().getType());
		}
		if(!HelpUtils.nullOrBlank(oTCOrderDTO.getQuoteCurrency())) {
			oTCOrderDO.setQuoteCurrency(oTCOrderDTO.getQuoteCurrency().toUpperCase());
		}
		oTCOrderDO.setRemainVolume(oTCOrderDTO.getRemainVolume());
		if (oTCOrderDTO.getStatus() != null ) {
			oTCOrderDO.setStatus(oTCOrderDTO.getStatus().getType());
		}
		if (oTCOrderDTO.getType() != null) {
			oTCOrderDO.setType(oTCOrderDTO.getType().getType());
		}
		oTCOrderDO.setUnfrozen(oTCOrderDTO.getUnfrozen());
		oTCOrderDO.setVolume(oTCOrderDTO.getVolume());
		oTCOrderDO.setCancelVolume(oTCOrderDTO.getCancelVolume());
		//这两个值只是sql需要用，所以DO转DTO不需要传
		oTCOrderDO.setCurAmount(oTCOrderDTO.getCurAmount());
		oTCOrderDO.setCurVolume(oTCOrderDTO.getCurVolume());
		oTCOrderDO.setAcountId(oTCOrderDTO.getAcountId());
		oTCOrderDO.setEffectiveTime(oTCOrderDTO.getEffectiveTime());
		oTCOrderDO.setRemark(oTCOrderDTO.getRemark());
		if(oTCOrderDTO.getIsAds() != null) {
			oTCOrderDO.setIsAds(oTCOrderDTO.getIsAds().getType());
		}
		oTCOrderDO.setOrigPrice(oTCOrderDTO.getOrigPrice());
		if(oTCOrderDTO.getPriceChangeType() != null) {
			oTCOrderDO.setPriceChangeType(oTCOrderDTO.getPriceChangeType().getType());
		}
		oTCOrderDO.setBaseRate(oTCOrderDTO.getBaseRate());
		
		return oTCOrderDO;
	}

	public static OTCOrderDTO DO2DTO(OTCOrderDO oTCOrderDO) {
		OTCOrderDTO oTCOrderDTO = new OTCOrderDTO();
		oTCOrderDTO.setBaseCurrency(oTCOrderDO.getBaseCurrency());
		oTCOrderDTO.setModifyTime(oTCOrderDO.getModifyTime());
		oTCOrderDTO.setCreateTime(oTCOrderDO.getCreateTime());
		oTCOrderDTO.setDoneVolume(oTCOrderDO.getDoneVolume());
		oTCOrderDTO.setFrozen(oTCOrderDO.getFrozen());
		oTCOrderDTO.setId(oTCOrderDO.getId());
		oTCOrderDTO.setLockAmount(oTCOrderDO.getLockAmount());
		oTCOrderDTO.setLockVolume(oTCOrderDO.getLockVolume());
		oTCOrderDTO.setRemain(oTCOrderDO.getRemain());
		oTCOrderDTO.setMemberId(oTCOrderDO.getMemberId());
		oTCOrderDTO.setMinQuote(oTCOrderDO.getMinQuote());
		oTCOrderDTO.setMaxQuote(oTCOrderDO.getMaxQuote());
		oTCOrderDTO.setPaymentTime(oTCOrderDO.getPaymentTime());
		oTCOrderDTO.setNumber(oTCOrderDO.getNumber());
		oTCOrderDTO.setOperIp(oTCOrderDO.getOperIp());
		oTCOrderDTO.setPrice(oTCOrderDO.getPrice());
		oTCOrderDTO.setPriceType(PriceTypeEnum.getEnumByType(oTCOrderDO.getPriceType()));
		oTCOrderDTO.setQuoteCurrency(oTCOrderDO.getQuoteCurrency());
		oTCOrderDTO.setRemainVolume(oTCOrderDO.getRemainVolume());
		oTCOrderDTO.setStatus(OrderStatusEnum.getEnumByType(oTCOrderDO.getStatus()));
		oTCOrderDTO.setType(OrderTypeEnum.getEnumByType(oTCOrderDO.getType()));
		oTCOrderDTO.setUnfrozen(oTCOrderDO.getUnfrozen());
		oTCOrderDTO.setVolume(oTCOrderDO.getVolume());
		oTCOrderDTO.setCancelVolume(oTCOrderDO.getCancelVolume());
		oTCOrderDTO.setAcountId(oTCOrderDO.getAcountId());
		oTCOrderDTO.setEffectiveTime(oTCOrderDO.getEffectiveTime());
		oTCOrderDTO.setRemark(oTCOrderDO.getRemark());
		oTCOrderDTO.setmName(oTCOrderDO.getmName());
		oTCOrderDTO.setIsAds(WhetherEnum.getEnumByType(oTCOrderDO.getIsAds()));
		oTCOrderDTO.setOrigPrice(oTCOrderDO.getOrigPrice());
		oTCOrderDTO.setPriceChangeType(PriceChangeTypeEnum.getEnumByType(oTCOrderDO.getPriceChangeType()));
		oTCOrderDTO.setBaseRate(oTCOrderDO.getBaseRate());
		
		return oTCOrderDTO;
	}

	public static List<OTCOrderDO> DTO2DO(List<OTCOrderDTO> dtoList) {
		List<OTCOrderDO> doList = new ArrayList<OTCOrderDO>();
		for (OTCOrderDTO a : dtoList) {
			doList.add(DTO2DO(a));
		}
		return doList;
	}

	public static List<OTCOrderDTO> DO2DTO(List<OTCOrderDO> doList) {
		List<OTCOrderDTO> dotList = new ArrayList<OTCOrderDTO>();
		for (OTCOrderDO a : doList) {
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
		if (!HelpUtils.isMapNullValue(param, "payType"))
		{
			PayTypeEnum payTypeEnum = PayTypeEnum.getEnumByCode(String.valueOf(param.get("payType")).toLowerCase());
			if (payTypeEnum != null) {
				param.put("payType", payTypeEnum.getType());
			}
		}
		if (!HelpUtils.isMapNullValue(param, "type")) {
			OrderTypeEnum orderTypeEnum = OrderTypeEnum.getOrderTypeEnum(String.valueOf(param.get("type")));
			if (orderTypeEnum != null) {
				param.put("type", orderTypeEnum.getType());
			}
		}
		if (!HelpUtils.isMapNullValue(param, "priceType")) {
			PriceTypeEnum priceTypeEnum = PriceTypeEnum.getPriceTypeEnum(String.valueOf(param.get("priceType")));
			if (priceTypeEnum != null) {
				param.put("priceType", priceTypeEnum.getType());
			}
		}
		if (!HelpUtils.isMapNullValue(param, "status")) {
			OrderStatusEnum orderStatusEnum = OrderStatusEnum.getOrderStatusEnum(String.valueOf(param.get("status")));
			if (orderStatusEnum != null) {
				param.put("status", orderStatusEnum.getType());
			}
		}
		if (!HelpUtils.isMapNullValue(param, "baseCurrency")) {
			param.put("baseCurrency", String.valueOf(param.get("baseCurrency")).toUpperCase());
		}
		if (!HelpUtils.isMapNullValue(param, "quoteCurrency")) {
			param.put("quoteCurrency", String.valueOf(param.get("quoteCurrency")).toUpperCase());
		}
		if (!HelpUtils.isMapNullValue(param, "isAds")) {
			WhetherEnum whetherEnum = WhetherEnum.getWhetherEnum(String.valueOf(param.get("isAds")));
			if (whetherEnum != null) {
				param.put("isAds", whetherEnum.getType());
			}
		}
		if (!HelpUtils.isMapNullValue(param, "priceChangeType")) {
			PriceChangeTypeEnum priceChangeTypeEnum = PriceChangeTypeEnum.getPriceTypeEnum(String.valueOf(param.get("priceChangeType")));
			if (priceChangeTypeEnum != null) {
				param.put("priceChangeType", priceChangeTypeEnum.getType());
			}
		}
	}
}
