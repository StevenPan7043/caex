package com.pmzhongguo.ex.core.mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description: mapper超类，基本的CRUD
 * @author: 十一
 */
public interface SuperMapper<T> {

	/**
	 * 根据主键ID得到详细信息
	 * 
	 * @param pk
	 * @return
	 */
	public T findById(Serializable id);

	/**
	 * 
	 * 新增
	 * 
	 * @param p
	 */
	public int insert(T entity);

	/**
	 * 根据id更新
	 * @param entity
	 * @return
	 */
	public int updateById(T entity);


	/**
	 * 批量删除
	 * @param entity
	 */
	public void batchInsert(List<T> entity);

	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	public int deleteById(Serializable id);

	/**
	 * 后台管理系统条件分页查询
	 * @param param
	 * @return
	 */
    List<Map<String,Object>> findMgrByPage(Map<String, Object> param);
}
