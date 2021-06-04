package com.zytx.common.entity;

import java.util.Date;

public class CChatRecord {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_chat_record.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_chat_record.member_id
     *
     * @mbggenerated
     */
    private Integer memberId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_chat_record.to_id
     *
     * @mbggenerated
     */
    private Integer toId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_chat_record.to_type
     *
     * @mbggenerated
     */
    private Integer toType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_chat_record.message
     *
     * @mbggenerated
     */
    private String message;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_chat_record.type
     *
     * @mbggenerated
     */
    private Integer type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_chat_record.is_success
     *
     * @mbggenerated
     */
    private Integer isSuccess;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_chat_record.create_time
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_chat_record.id
     *
     * @return the value of c_chat_record.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_chat_record.id
     *
     * @param id the value for c_chat_record.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_chat_record.member_id
     *
     * @return the value of c_chat_record.member_id
     *
     * @mbggenerated
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_chat_record.member_id
     *
     * @param memberId the value for c_chat_record.member_id
     *
     * @mbggenerated
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_chat_record.to_id
     *
     * @return the value of c_chat_record.to_id
     *
     * @mbggenerated
     */
    public Integer getToId() {
        return toId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_chat_record.to_id
     *
     * @param toId the value for c_chat_record.to_id
     *
     * @mbggenerated
     */
    public void setToId(Integer toId) {
        this.toId = toId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_chat_record.to_type
     *
     * @return the value of c_chat_record.to_type
     *
     * @mbggenerated
     */
    public Integer getToType() {
        return toType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_chat_record.to_type
     *
     * @param toType the value for c_chat_record.to_type
     *
     * @mbggenerated
     */
    public void setToType(Integer toType) {
        this.toType = toType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_chat_record.message
     *
     * @return the value of c_chat_record.message
     *
     * @mbggenerated
     */
    public String getMessage() {
        return message;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_chat_record.message
     *
     * @param message the value for c_chat_record.message
     *
     * @mbggenerated
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_chat_record.type
     *
     * @return the value of c_chat_record.type
     *
     * @mbggenerated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_chat_record.type
     *
     * @param type the value for c_chat_record.type
     *
     * @mbggenerated
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_chat_record.is_success
     *
     * @return the value of c_chat_record.is_success
     *
     * @mbggenerated
     */
    public Integer getIsSuccess() {
        return isSuccess;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_chat_record.is_success
     *
     * @param isSuccess the value for c_chat_record.is_success
     *
     * @mbggenerated
     */
    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_chat_record.create_time
     *
     * @return the value of c_chat_record.create_time
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_chat_record.create_time
     *
     * @param createTime the value for c_chat_record.create_time
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}