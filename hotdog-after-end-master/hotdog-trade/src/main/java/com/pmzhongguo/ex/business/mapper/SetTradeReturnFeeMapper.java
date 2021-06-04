package com.pmzhongguo.ex.business.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.entity.SetTradeReturnFee;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

/**
 * 交易返手续费模块
 * 
 * @author zengweixiong
 *
 */
@SuppressWarnings("rawtypes")
public interface SetTradeReturnFeeMapper extends SuperMapper {

	/**
	 * 查询设置信息
	 * 
	 * @return
	 */
	List<Map<String, Object>> getSet();

	/**
	 * 编辑设置信息
	 * 
	 * @param list
	 */
	void editSet(List<SetTradeReturnFee> list);

	/**
	 * 获取父级3代关系数据
	 * @param memberId
	 * @return
	 */
	List<Map<String, Object>> getAll(Integer memberId);

}
