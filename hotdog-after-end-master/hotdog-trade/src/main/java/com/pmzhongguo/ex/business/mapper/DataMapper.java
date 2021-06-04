package com.pmzhongguo.ex.business.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.entity.Order;
import com.pmzhongguo.ex.business.entity.Trade;
import com.pmzhongguo.ex.core.mapper.SuperMapper;


public interface DataMapper extends SuperMapper {
	
	public List<Order> listOrderPage(Map param);

	public List<Trade> listTradePage(Map param);

	public List<Map> listAddrPoolBalancePage(Map param);
	public long listMemberTrade(Map<String,Object> param);


}
