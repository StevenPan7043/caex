package com.pmzhongguo.ex.business.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pmzhongguo.ex.business.mapper.CountMapper;

/**
 * 统计模块
 * 
 * @author zengweixiong
 *
 */
@Service
public class CountService {

	@Resource
	private CountMapper countMapper;

	/**
	 * 查询该天的充值, 提现统计
	 * 
	 * @param date
	 * @return
	 */
	public Map<String, Object> countToday(Map<String, Object> maps) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		List<Map<String, Object>> wList = countMapper.countWithdraw(maps);
		List<Map<String, Object>> rList = countMapper.countRecharge(maps);
		map.put("wList", wList);
		map.put("rList", rList);
		return map;
	}

}
