package com.pmzhongguo.ex.business.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.business.entity.Order;
import com.pmzhongguo.ex.business.entity.Trade;
import com.pmzhongguo.ex.business.mapper.DataMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;


@Service
@Transactional
public class DataService extends BaseServiceSupport {
	@Autowired
	private DataMapper dataMapper;


	public List<Order> getAllOrder(Map param) {
		return dataMapper.listOrderPage(param);
	}


	public List<Trade> getAllTrade(Map param) {
		return dataMapper.listTradePage(param);
	}


	public List<Map> getAllAddrPoolBalance(Map param) {
		return dataMapper.listAddrPoolBalancePage(param);
	}

	public long getAllMemberTrade(Map<String,Object> param) {
		return dataMapper.listMemberTrade(param);
	}
}
