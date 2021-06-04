/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/8 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/8 20:33
 * @description：mapper interface
 * @version: $
 */
public interface GenericMapper<T extends GenericEntity> {

    /**
     * 根据主键ID得到详细信息
     *
     * @param id
     * @return
     */
    public T findById(Serializable id);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    public int insert(T entity);

    /**
     * 更新
     *
     * @param entity
     * @return
     */
    public int updateById(T entity);

    public void batchInsert(List<T> entity);

    public int deleteById(Integer id);
}
