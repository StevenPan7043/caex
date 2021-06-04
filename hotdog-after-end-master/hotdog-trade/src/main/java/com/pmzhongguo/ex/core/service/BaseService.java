package com.pmzhongguo.ex.core.service;

import com.pmzhongguo.ex.core.mapper.SuperMapper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @description: service 基类，基本的CRUD
 * @date: 2019-06-03 11:08
 * @author: 十一
 */
public abstract class BaseService<T> {

    /**
     * 该类的mapper
     * @return
     */
    public abstract SuperMapper<T> getMapper();

    /**
     * 根据主键ID得到详细信息
     *
     * @param id
     * @return
     */
    public T findById(Serializable id) {
        return getMapper().findById(id);
    }


    /**
     * 新增
     * @param entity
     * @return
     */
    public int insert(T entity) {
        return getMapper().insert(entity);
    }

    /**
     * 更新
     * @param id
     * @return
     */
    public int updateById(T id) {
        return getMapper().updateById(id);
    }


    /**
     * 删除
     * @param id
     * @return
     */
    public int deleteById(Serializable id) {
        return getMapper().deleteById(id);
    }

    /**
     * 后台管理系统条件分页
     * @param param
     * @return
     */
    public List<Map<String,Object>> findMgrByPage(Map<String, Object> param) {
        return getMapper().findMgrByPage(param);
    }
}
