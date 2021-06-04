package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.entity.LockAmount;
import com.pmzhongguo.ex.business.entity.LockList;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * 锁仓
 * 
 *
 */
public interface LockAmountMapper extends SuperMapper<Object> {


	public void insert(Map map);

	public int update(Map map);

	public LockAmount findOne(Map map);

	public List<LockAmount> findByPage(Map map);
	public List<LockAmount> findAllCurrency(Map map);



}
