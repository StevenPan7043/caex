package com.pmzhongguo.ex.business.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.mapper.SuperMapper;

/**
 * 投票模块
 * 
 * @author zengweixiong
 *
 */
@SuppressWarnings("rawtypes")
public interface VoteMapper extends SuperMapper {

	/**
	 * 查询币种信息
	 * 
	 * @return
	 */
	List<Map<Integer, String>> coins();

	/**
	 * 查询设置信息
	 * 
	 * @return
	 */
	Map<String, Object> getSet();

	/**
	 * 编辑投票信息
	 * 
	 * @param param
	 */
	void editSet(Map<String, Object> param);

	/**
	 * 查询投票列表
	 * 
	 * @return
	 */
	List<Map<String, Object>> list();

	/**
	 * 投票情况 赞成反对百分比
	 * 
	 * @return
	 */
	Map<String, Object> info();

	/**
	 * 统计条数
	 * 
	 * @param param
	 * @return
	 */
	Integer count(Map<String, Object> param);

	/**
	 * api 列表分页
	 * 
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> apiList(Map<String, Object> param);

	/**
	 * 添加一条投票记录
	 * 
	 * @param map
	 */
	void add(Map<String, Object> map);

	/**
	 * 获取用户对应币种的资产信息
	 * 
	 * @param map
	 * @return
	 */
	Map<String, Object> findMemberInfo(Map<String, Object> map);

	/**
	 * 用户资产扣除币种数量
	 * 
	 * @param asset
	 */
	void updateMemberInfo(Map<String, Object> asset);

}
