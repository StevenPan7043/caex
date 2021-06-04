package com.pmzhongguo.ex.business.service;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


import com.lmax.disruptor.EventHandler;
import com.pmzhongguo.ex.business.entity.Order;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;


/**
 * 此类暂时不用
 * @author Administrator
 *
 */
public class TradeConsumer extends BaseServiceSupport implements
		EventHandler<Order> {
	
	private static TradeService tradeService = null;
	
	private static TradeService getTradeService() {		
		if (null == tradeService) {
			WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
			tradeService = (TradeService) wac.getBean("tradeService");
		}
		
		return tradeService;
	}
	
	@Override
	public void onEvent(Order event, long sequence, boolean endOfBatch)
			throws Exception {
		getTradeService().trade(event, false);
	}
}
