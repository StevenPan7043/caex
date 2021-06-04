package com.pmzhongguo.ex.business.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;

/**
 * APP更新设置信息
 * 
 * @author zengweixiong
 *
 */
public interface AppUpdateMapper extends SuperMapper<Object> {

	/**
	 * 获取一条APP更新设置信息
	 * 
	 * @param type
	 *            APP类型;
	 */
	public Map<String, Object> findOne(Map<String, Object> map);

	/**
	 * 获取所有数据
	 * @return
	 */
	public List<Map<String, Object>> findAll();

	/**
	 * 修改一条数据
	 * @param param
	 */
	public void update(Map<String, Object> param);

}
