package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.dto.LockRechargeDto;
import com.pmzhongguo.ex.business.entity.LockRecharge;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * 锁仓
 * 
 *
 */
public interface LockRechargeMapper extends SuperMapper<Object> {




	/**
	 * 直接修改 只能修改运行的时间，参数、同异步等无法修改
	 *
	 * @param map
	 */
	public void update(Map map);





	/**
	 * 查询释放任务列表
	 *
	 * @return
	 */
	public List<LockRechargeDto> findAll();

	public List<LockRecharge> findCurrencyByPage(Map map);


	List<LockRecharge> findLockListByPage(Map map);

	/**===============后台管理===========================*/
	public List<Map> findRechargeByPage(Map map);

	LockRecharge findOne(Integer id);
}
