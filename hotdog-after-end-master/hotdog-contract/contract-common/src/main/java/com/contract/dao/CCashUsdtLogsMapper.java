package com.contract.dao;

import com.contract.entity.CCashUsdtLogs;
import com.contract.entity.CCashUsdtLogsExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface CCashUsdtLogsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    int countByExample(CCashUsdtLogsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    int deleteByExample(CCashUsdtLogsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    int insert(CCashUsdtLogs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    int insertSelective(CCashUsdtLogs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    List<CCashUsdtLogs> selectByExample(CCashUsdtLogsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    CCashUsdtLogs selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CCashUsdtLogs record, @Param("example") CCashUsdtLogsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CCashUsdtLogs record, @Param("example") CCashUsdtLogsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CCashUsdtLogs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_cash_usdt_logs
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CCashUsdtLogs record);

	List<CCashUsdtLogs> queryCashUsdlist(CCashUsdtLogs cashLogs);

	List<CCashUsdtLogs> queryDzlist();

	Map<String, Object> getTotalmoney(CCashUsdtLogs cashLogs);
}