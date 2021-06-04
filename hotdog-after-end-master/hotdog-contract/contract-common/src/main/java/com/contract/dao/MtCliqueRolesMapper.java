package com.contract.dao;

import com.contract.entity.MtCliqueRoles;
import com.contract.entity.MtCliqueRolesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MtCliqueRolesMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int countByExample(MtCliqueRolesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int deleteByExample(MtCliqueRolesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int insert(MtCliqueRoles record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int insertSelective(MtCliqueRoles record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    List<MtCliqueRoles> selectByExampleWithBLOBs(MtCliqueRolesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    List<MtCliqueRoles> selectByExample(MtCliqueRolesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    MtCliqueRoles selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") MtCliqueRoles record, @Param("example") MtCliqueRolesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") MtCliqueRoles record, @Param("example") MtCliqueRolesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") MtCliqueRoles record, @Param("example") MtCliqueRolesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(MtCliqueRoles record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(MtCliqueRoles record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_roles
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(MtCliqueRoles record);

	MtCliqueRoles getValidByid(Integer roleid);
}