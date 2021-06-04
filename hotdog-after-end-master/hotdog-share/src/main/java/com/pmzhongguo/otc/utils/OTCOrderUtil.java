package com.pmzhongguo.otc.utils;

import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.PriceTypeEnum;

public class OTCOrderUtil {
	public static boolean isMarketBuy(OTCOrderDTO order) {
		return order.getPriceType().getType() == PriceTypeEnum.MARKET.getType()
				&& order.getType().getType() == OrderTypeEnum.BUY.getType();
	}

	public static boolean isLimitBuy(OTCOrderDTO order) {
		return order.getPriceType().getType() == PriceTypeEnum.LIMIT.getType()
				&& order.getType().getType() == OrderTypeEnum.BUY.getType();
	}
}
