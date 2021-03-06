package com.contract.dao;

import com.contract.entity.MtCliqueMenus;
import com.contract.entity.MtCliqueMenusExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MtCliqueMenusMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    int countByExample(MtCliqueMenusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    int deleteByExample(MtCliqueMenusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    int insert(MtCliqueMenus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    int insertSelective(MtCliqueMenus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    List<MtCliqueMenus> selectByExample(MtCliqueMenusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    MtCliqueMenus selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") MtCliqueMenus record, @Param("example") MtCliqueMenusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") MtCliqueMenus record, @Param("example") MtCliqueMenusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(MtCliqueMenus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_menus
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(MtCliqueMenus record);
}