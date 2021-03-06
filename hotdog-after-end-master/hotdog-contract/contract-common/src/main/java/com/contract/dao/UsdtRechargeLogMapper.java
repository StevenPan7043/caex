package com.contract.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.contract.dto.TxDto;
import com.contract.entity.UsdtRechargeLog;
import com.contract.entity.UsdtRechargeLogExample;

public interface UsdtRechargeLogMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    int countByExample(UsdtRechargeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    int deleteByExample(UsdtRechargeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    int insert(UsdtRechargeLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    int insertSelective(UsdtRechargeLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    List<UsdtRechargeLog> selectByExample(UsdtRechargeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    UsdtRechargeLog selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") UsdtRechargeLog record, @Param("example") UsdtRechargeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") UsdtRechargeLog record, @Param("example") UsdtRechargeLogExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(UsdtRechargeLog record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table usdt_recharge_log
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(UsdtRechargeLog record);

	List<UsdtRechargeLog> queryList(UsdtRechargeLog rechargeLog);

	List<TxDto> queryAddr();

	void updateStatus(TxDto l);

	BigDecimal getNodemoney(String nodeid);

	BigDecimal getWaitUsdt();

	BigDecimal getRecharge();

	Map<String, Object> getRechargeTotal(UsdtRechargeLog rechargeLog);
}