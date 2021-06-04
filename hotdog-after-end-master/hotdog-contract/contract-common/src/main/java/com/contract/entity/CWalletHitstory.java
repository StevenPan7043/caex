package com.contract.entity;

import java.math.BigDecimal;
import java.util.Date;

public class CWalletHitstory {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_wallet_hitstory.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_wallet_hitstory.cid
     *
     * @mbggenerated
     */
    private Integer cid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_wallet_hitstory.type
     *
     * @mbggenerated
     */
    private String type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_wallet_hitstory.balance
     *
     * @mbggenerated
     */
    private BigDecimal balance;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_wallet_hitstory.zcbalance
     *
     * @mbggenerated
     */
    private BigDecimal zcbalance;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_wallet_hitstory.record_date
     *
     * @mbggenerated
     */
    private String recordDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column c_wallet_hitstory.create_date
     *
     * @mbggenerated
     */
    private Date createDate;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_wallet_hitstory.id
     *
     * @return the value of c_wallet_hitstory.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_wallet_hitstory.id
     *
     * @param id the value for c_wallet_hitstory.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_wallet_hitstory.cid
     *
     * @return the value of c_wallet_hitstory.cid
     *
     * @mbggenerated
     */
    public Integer getCid() {
        return cid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_wallet_hitstory.cid
     *
     * @param cid the value for c_wallet_hitstory.cid
     *
     * @mbggenerated
     */
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_wallet_hitstory.type
     *
     * @return the value of c_wallet_hitstory.type
     *
     * @mbggenerated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_wallet_hitstory.type
     *
     * @param type the value for c_wallet_hitstory.type
     *
     * @mbggenerated
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_wallet_hitstory.balance
     *
     * @return the value of c_wallet_hitstory.balance
     *
     * @mbggenerated
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_wallet_hitstory.balance
     *
     * @param balance the value for c_wallet_hitstory.balance
     *
     * @mbggenerated
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_wallet_hitstory.zcbalance
     *
     * @return the value of c_wallet_hitstory.zcbalance
     *
     * @mbggenerated
     */
    public BigDecimal getZcbalance() {
        return zcbalance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_wallet_hitstory.zcbalance
     *
     * @param zcbalance the value for c_wallet_hitstory.zcbalance
     *
     * @mbggenerated
     */
    public void setZcbalance(BigDecimal zcbalance) {
        this.zcbalance = zcbalance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_wallet_hitstory.record_date
     *
     * @return the value of c_wallet_hitstory.record_date
     *
     * @mbggenerated
     */
    public String getRecordDate() {
        return recordDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_wallet_hitstory.record_date
     *
     * @param recordDate the value for c_wallet_hitstory.record_date
     *
     * @mbggenerated
     */
    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_wallet_hitstory.create_date
     *
     * @return the value of c_wallet_hitstory.create_date
     *
     * @mbggenerated
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_wallet_hitstory.create_date
     *
     * @param createDate the value for c_wallet_hitstory.create_date
     *
     * @mbggenerated
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}