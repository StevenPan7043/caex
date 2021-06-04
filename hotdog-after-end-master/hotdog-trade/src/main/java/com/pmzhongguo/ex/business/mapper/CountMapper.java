package com.pmzhongguo.ex.business.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;

/**
 * 统计模块
 * 
 * @author zengweixiong
 *
 */
public interface CountMapper extends SuperMapper<Object> {

	/**
	 * 根据日期统计当天的提现
	 * 
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> countWithdraw(Map<String, Object> map);

	/**
	 * 根据日期统计当天的充值
	 * 
	 * @param date
	 * @return
	 */
	List<Map<String, Object>> countRecharge(Map<String, Object> map);

}
