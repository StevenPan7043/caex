package com.contract.dao;

import com.contract.entity.GdProject;
import com.contract.entity.GdProjectExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GdProjectMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    long countByExample(GdProjectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    int deleteByExample(GdProjectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    int insert(GdProject record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    int insertSelective(GdProject record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    List<GdProject> selectByExample(GdProjectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    GdProject selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    int updateByExampleSelective(@Param("record") GdProject record, @Param("example") GdProjectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    int updateByExample(@Param("record") GdProject record, @Param("example") GdProjectExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    int updateByPrimaryKeySelective(GdProject record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table documentary_project
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    int updateByPrimaryKey(GdProject record);
}