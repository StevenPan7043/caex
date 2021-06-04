package com.contract.dao;

import com.contract.entity.GdTeamBonus;
import com.contract.entity.GdTeamBonusExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GdTeamBonusMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    long countByExample(GdTeamBonusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    int deleteByExample(GdTeamBonusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    int insert(GdTeamBonus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    int insertSelective(GdTeamBonus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    List<GdTeamBonus> selectByExample(GdTeamBonusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    GdTeamBonus selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    int updateByExampleSelective(@Param("record") GdTeamBonus record, @Param("example") GdTeamBonusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    int updateByExample(@Param("record") GdTeamBonus record, @Param("example") GdTeamBonusExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    int updateByPrimaryKeySelective(GdTeamBonus record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table gd_team_bonus
     *
     * @mbg.generated Tue Nov 10 22:15:18 CST 2020
     */
    int updateByPrimaryKey(GdTeamBonus record);

    public List<GdTeamBonus> gdTeamBonusList(GdTeamBonus gdTeamBonus);
}