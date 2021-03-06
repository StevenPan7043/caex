package com.contract.dao;

import com.contract.entity.MtCliqueUser;
import com.contract.entity.MtCliqueUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MtCliqueUserMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    int countByExample(MtCliqueUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    int deleteByExample(MtCliqueUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    int insert(MtCliqueUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    int insertSelective(MtCliqueUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    List<MtCliqueUser> selectByExample(MtCliqueUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    MtCliqueUser selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") MtCliqueUser record, @Param("example") MtCliqueUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") MtCliqueUser record, @Param("example") MtCliqueUserExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(MtCliqueUser record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table mt_clique_user
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(MtCliqueUser record);

	MtCliqueUser getByLogin(String login);

	List<String> queryUsername(Integer id);
}