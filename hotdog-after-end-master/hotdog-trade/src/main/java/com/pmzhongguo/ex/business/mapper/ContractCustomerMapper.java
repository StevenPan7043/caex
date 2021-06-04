package com.pmzhongguo.ex.business.mapper;

import com.contract.dto.TeamVo;
import com.contract.entity.CCustomer;
import com.contract.entity.CCustomerExample;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ContractCustomerMapper extends SuperMapper {
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

	CCustomer getByInvitationcode(String parentcode);

	CCustomer getByPhone(String phone);

	List<CCustomer> queryList(CCustomer customer);

	BigDecimal getTotalmoney(CCustomer customer);

	List<Map<String, Object>> queryUnder(CCustomer cus);

	List<TeamVo> queryTeam(Integer id);

}