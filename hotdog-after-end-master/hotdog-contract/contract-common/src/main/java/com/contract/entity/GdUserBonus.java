package com.contract.entity;

import com.contract.service.Page;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户跟单日收益
 *
 */
public class GdUserBonus {
    /**
     * 
     */
    private Integer id;

    /**
     * 购买表id
     */
    private Integer recordId;

    /**
     * 跟单id
     */
    private Integer projectId;

    /**
     * 用户id
     */
    private Integer memberId;

    /**
     * 产出币种
     */
    private String outputCurrency;

    /**
     * 分红数量
     */
    private BigDecimal bonusNum;

    /**
     * 分红日期yyyy-MM-dd
     */
    private String bonusDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_user_bonus.id
     *
     * @return the value of documentary_user_bonus.id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_user_bonus.id
     *
     * @param id the value for documentary_user_bonus.id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_user_bonus.record_id
     *
     * @return the value of documentary_user_bonus.record_id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Integer getRecordId() {
        return recordId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_user_bonus.record_id
     *
     * @param recordId the value for documentary_user_bonus.record_id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_user_bonus.project_id
     *
     * @return the value of documentary_user_bonus.project_id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Integer getProjectId() {
        return projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_user_bonus.project_id
     *
     * @param projectId the value for documentary_user_bonus.project_id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_user_bonus.member_id
     *
     * @return the value of documentary_user_bonus.member_id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_user_bonus.member_id
     *
     * @param memberId the value for documentary_user_bonus.member_id
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_user_bonus.output_currency
     *
     * @return the value of documentary_user_bonus.output_currency
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public String getOutputCurrency() {
        return outputCurrency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_user_bonus.output_currency
     *
     * @param outputCurrency the value for documentary_user_bonus.output_currency
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setOutputCurrency(String outputCurrency) {
        this.outputCurrency = outputCurrency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_user_bonus.bonus_num
     *
     * @return the value of documentary_user_bonus.bonus_num
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public BigDecimal getBonusNum() {
        return bonusNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_user_bonus.bonus_num
     *
     * @param bonusNum the value for documentary_user_bonus.bonus_num
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setBonusNum(BigDecimal bonusNum) {
        this.bonusNum = bonusNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_user_bonus.bonus_date
     *
     * @return the value of documentary_user_bonus.bonus_date
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public String getBonusDate() {
        return bonusDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_user_bonus.bonus_date
     *
     * @param bonusDate the value for documentary_user_bonus.bonus_date
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setBonusDate(String bonusDate) {
        this.bonusDate = bonusDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_user_bonus.create_time
     *
     * @return the value of documentary_user_bonus.create_time
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_user_bonus.create_time
     *
     * @param createTime the value for documentary_user_bonus.create_time
     *
     * @mbg.generated Tue Nov 10 11:00:52 CST 2020
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}