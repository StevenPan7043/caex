package com.contract.dao;

import com.contract.entity.CPayChannel;
import com.contract.entity.CPayChannelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CPayChannelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    int countByExample(CPayChannelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    int deleteByExample(CPayChannelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    int insert(CPayChannel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    int insertSelective(CPayChannel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    List<CPayChannel> selectByExample(CPayChannelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    CPayChannel selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CPayChannel record, @Param("example") CPayChannelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CPayChannel record, @Param("example") CPayChannelExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CPayChannel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_pay_channel
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CPayChannel record);
}