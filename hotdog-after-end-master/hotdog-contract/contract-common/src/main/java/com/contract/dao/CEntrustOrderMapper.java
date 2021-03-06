package com.contract.dao;

import com.contract.entity.CEntrustOrder;
import com.contract.entity.CEntrustOrderExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CEntrustOrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    int countByExample(CEntrustOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    int deleteByExample(CEntrustOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    int insert(CEntrustOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    int insertSelective(CEntrustOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    List<CEntrustOrder> selectByExample(CEntrustOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    CEntrustOrder selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CEntrustOrder record, @Param("example") CEntrustOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CEntrustOrder record, @Param("example") CEntrustOrderExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CEntrustOrder record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_entrust_order
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CEntrustOrder record);

	List<CEntrustOrder> queryList(CEntrustOrder entrustOrder);

	CEntrustOrder getByOrdercode(String ordercode);
}