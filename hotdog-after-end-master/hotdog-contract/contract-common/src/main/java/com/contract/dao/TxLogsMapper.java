package com.contract.dao;

import com.contract.dto.CposDto;
import com.contract.entity.TxLogs;
import com.contract.entity.TxLogsExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TxLogsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    int countByExample(TxLogsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    int deleteByExample(TxLogsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(String hash);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    int insert(TxLogs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    int insertSelective(TxLogs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    List<TxLogs> selectByExample(TxLogsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    TxLogs selectByPrimaryKey(String hash);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") TxLogs record, @Param("example") TxLogsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") TxLogs record, @Param("example") TxLogsExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(TxLogs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tx_logs
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(TxLogs record);

	List<TxLogs> queryLogs(TxLogs logs);

	Map<String, Object> getTotal(TxLogs logs);
	
	List<CposDto> queryGj();
	
	void updateStatus(CposDto l);
}