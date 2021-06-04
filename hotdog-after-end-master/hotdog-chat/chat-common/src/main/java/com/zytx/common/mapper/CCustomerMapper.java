package com.zytx.common.mapper;

import com.zytx.common.entity.CCustomer;
import com.zytx.common.entity.CCustomerExample;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CCustomerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int countByExample(CCustomerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int deleteByExample(CCustomerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int insert(CCustomer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int insertSelective(CCustomer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    List<CCustomer> selectByExampleWithBLOBs(CCustomerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    List<CCustomer> selectByExample(CCustomerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    CCustomer selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int updateByExampleSelective(@Param("record") CCustomer record, @Param("example") CCustomerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int updateByExampleWithBLOBs(@Param("record") CCustomer record, @Param("example") CCustomerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int updateByExample(@Param("record") CCustomer record, @Param("example") CCustomerExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(CCustomer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int updateByPrimaryKeyWithBLOBs(CCustomer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table c_customer
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(CCustomer record);
}