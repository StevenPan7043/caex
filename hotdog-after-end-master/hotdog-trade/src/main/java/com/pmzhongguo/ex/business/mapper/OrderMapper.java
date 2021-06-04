package com.pmzhongguo.ex.business.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.dto.OrderRespDto;
import com.pmzhongguo.ex.business.dto.ReturnCommiCurrAmountDto;
import com.pmzhongguo.ex.business.dto.TradeRespDto;
import com.pmzhongguo.ex.business.entity.Order;
import com.pmzhongguo.ex.business.entity.Trade;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.datalab.entity.dto.TradeDto;


public interface OrderMapper extends SuperMapper {

	public void addOrder(Order order);

	public void changeOrder(Order order);

	public void addTrade(Trade sellTrade);

	public Order getOrder(Order order);

	public List<OrderRespDto> getOrdersPage(Map<String, Object> orderMap);
	public int getOrdersPageTotal(Map<String, Object> orderMap);

	public List<TradeRespDto> getTradesPage(Map<String, Object> orderMap);

	public List<TradeDto> getTradeListByPage(Map<String, Object> orderMap);
	public List<Trade> getTrades(Map<String, Object> orderMap);
	public List<Trade> findOppositeTrades(Map<String, Object> orderMap);

	public List<OrderRespDto> getOrderByIdAndTokenid(Map<String, Object> orderMap);

	public long listMemberOrder(Map<String,Object> param);


	List<ReturnCommiCurrAmountDto> findUnCountReturnCommiRecordOfDay(Map<String, Object> param);

	List<ReturnCommiCurrAmountDto> getTradeFeeList(Map<String, Object> param);

	Trade getTradeDetail(Map<String, Object> params);

	BigDecimal getPlatformFlows(Map<String, Object> params);

	public Trade getTradeOrderByTimeLimitOne(Map<String, Object> map);
}
