package com.contract.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 逐仓明细表
 *
 */
public class GdDetail {
    /**
     * 
     */
    private Integer id;

    /**
     * 流水号
     */
    private String paycode;

    /**
     * 会员编号
     */
    private Integer cid;

    /**
     * 操作类型见枚举类
     */
    private Integer typeid;

    /**
     * 1收入 0支出
     */
    private Integer isout;

    /**
     * 上一次值
     */
    private BigDecimal original;

    /**
     * 本次操作值
     */
    private BigDecimal cost;

    /**
     * 最后值
     */
    private BigDecimal last;

    /**
     * 创建时间
     */
    private Date createtime;

    /**
     * 修改时间 用来做乐观锁
     */
    private Date updatetime;

    /**
     * 目标用户id
     */
    private Integer targetid;

    /**
     * 备注
     */
    private String remark;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.id
     *
     * @return the value of documentary_detail.id
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.id
     *
     * @param id the value for documentary_detail.id
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.paycode
     *
     * @return the value of documentary_detail.paycode
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public String getPaycode() {
        return paycode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.paycode
     *
     * @param paycode the value for documentary_detail.paycode
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setPaycode(String paycode) {
        this.paycode = paycode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.cid
     *
     * @return the value of documentary_detail.cid
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public Integer getCid() {
        return cid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.cid
     *
     * @param cid the value for documentary_detail.cid
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.typeid
     *
     * @return the value of documentary_detail.typeid
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public Integer getTypeid() {
        return typeid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.typeid
     *
     * @param typeid the value for documentary_detail.typeid
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setTypeid(Integer typeid) {
        this.typeid = typeid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.isout
     *
     * @return the value of documentary_detail.isout
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public Integer getIsout() {
        return isout;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.isout
     *
     * @param isout the value for documentary_detail.isout
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setIsout(Integer isout) {
        this.isout = isout;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.original
     *
     * @return the value of documentary_detail.original
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public BigDecimal getOriginal() {
        return original;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.original
     *
     * @param original the value for documentary_detail.original
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setOriginal(BigDecimal original) {
        this.original = original;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.cost
     *
     * @return the value of documentary_detail.cost
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.cost
     *
     * @param cost the value for documentary_detail.cost
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.last
     *
     * @return the value of documentary_detail.last
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public BigDecimal getLast() {
        return last;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.last
     *
     * @param last the value for documentary_detail.last
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setLast(BigDecimal last) {
        this.last = last;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.createtime
     *
     * @return the value of documentary_detail.createtime
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.createtime
     *
     * @param createtime the value for documentary_detail.createtime
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.updatetime
     *
     * @return the value of documentary_detail.updatetime
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public Date getUpdatetime() {
        return updatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.updatetime
     *
     * @param updatetime the value for documentary_detail.updatetime
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.targetid
     *
     * @return the value of documentary_detail.targetid
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public Integer getTargetid() {
        return targetid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.targetid
     *
     * @param targetid the value for documentary_detail.targetid
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setTargetid(Integer targetid) {
        this.targetid = targetid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column documentary_detail.remark
     *
     * @return the value of documentary_detail.remark
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column documentary_detail.remark
     *
     * @param remark the value for documentary_detail.remark
     *
     * @mbg.generated Tue Nov 10 16:17:49 CST 2020
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}