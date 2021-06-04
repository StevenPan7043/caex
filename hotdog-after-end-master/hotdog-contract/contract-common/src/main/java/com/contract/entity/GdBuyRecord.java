package com.contract.entity;

import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户购买跟单记录
 *
 */
public class GdBuyRecord {
    /**
     * 
     */
    private Integer id;

    /**
     * 跟单id
     */
    private Integer projectId;

    /**
     * 用户id
     */
    private Integer memberId;

    /**
     * 购买的数量
     */
    private Integer num;

    /**
     * 产出币种
     */
    private String outputCurrency;

    /**
     * 购买单价
     */
    private BigDecimal price;

    /**
     * 购买总价
     */
    private BigDecimal total;

    /**
     * 1有效2无效
     */
    private String status;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_buy_record.id
     *
     * @return the value of documentary_buy_record.id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_buy_record.id
     *
     * @param id the value for documentary_buy_record.id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_buy_record.project_id
     *
     * @return the value of documentary_buy_record.project_id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_buy_record.project_id
     *
     * @param projectId the value for documentary_buy_record.project_id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_buy_record.member_id
     *
     * @return the value of documentary_buy_record.member_id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_buy_record.member_id
     *
     * @param memberId the value for documentary_buy_record.member_id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_buy_record.num
     *
     * @return the value of documentary_buy_record.num
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Integer getNum() {
        return num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_buy_record.num
     *
     * @param num the value for documentary_buy_record.num
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setNum(Integer num) {
        this.num = num;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_buy_record.output_currency
     *
     * @return the value of documentary_buy_record.output_currency
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public String getOutputCurrency() {
        return outputCurrency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_buy_record.output_currency
     *
     * @param outputCurrency the value for documentary_buy_record.output_currency
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setOutputCurrency(String outputCurrency) {
        this.outputCurrency = outputCurrency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_buy_record.price
     *
     * @return the value of documentary_buy_record.price
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_buy_record.price
     *
     * @param price the value for documentary_buy_record.price
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_buy_record.total
     *
     * @return the value of documentary_buy_record.total
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_buy_record.total
     *
     * @param total the value for documentary_buy_record.total
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_buy_record.status
     *
     * @return the value of documentary_buy_record.status
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public String getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_buy_record.status
     *
     * @param status the value for documentary_buy_record.status
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_buy_record.create_time
     *
     * @return the value of documentary_buy_record.create_time
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_buy_record.create_time
     *
     * @param createTime the value for documentary_buy_record.create_time
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_buy_record.modify_time
     *
     * @return the value of documentary_buy_record.modify_time
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_buy_record.modify_time
     *
     * @param modifyTime the value for documentary_buy_record.modify_time
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}