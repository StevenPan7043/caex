package com.pmzhongguo.ex.business.mapper;

import com.pmzhongguo.ex.business.dto.LockListDto;
import com.pmzhongguo.ex.business.dto.LockRechargeDto;
import com.pmzhongguo.ex.business.entity.LockList;
import com.pmzhongguo.ex.business.entity.LockRecharge;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.util.List;
import java.util.Map;

/**
 * 锁仓
 * 
 *
 */
public interface LockListMapper extends SuperMapper<Object> {



	public int insert(LockList lockList);

	/**
	 * 分页查找某个锁仓的释放明細
	 * @param map
	 * @return
	 */
	public List<Map> findByPage(Map map);


	/**
	 * 查找笔锁仓某天释放明细
	 * @param id		LockRecharge id
	 * @param undata 释放日期  20181011
	 * @return
	 */
	LockList findByLockRechargeIdAndUndata(Map map);



	List<LockList> findAllLockAfterundataAndStatus(int undate);

	List<Map<String,Object>> findTopLockByMemberAndDate(Map<String,Object> params);

	List<LockList> findLockRecordByPage(Map<String, Object> params);

	Map<String,Object> findLockTotalAmountByMemberIdAndCurrency(Map<String, Object> params);


	/**===============================后台管理端==========================================**/
	List<Map> findLocklistByPage(Map<String, Object> params);
}
