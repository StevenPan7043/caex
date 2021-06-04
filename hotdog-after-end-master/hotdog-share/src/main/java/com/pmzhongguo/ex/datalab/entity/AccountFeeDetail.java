package com.pmzhongguo.ex.datalab.entity;

import java.math.BigDecimal;

/**
 * 手续费资产明细表
 *
 * @author jary
 * @creatTime 2019/11/29 3:56 PM
 */
public class AccountFeeDetail {

    private Integer id;

    /**
     * 用户id
     */
    private Integer memberId;

    /**
     * 手续费币种
     */
    private String feeCurrency;

    /**
     * 资产变动类型
     */
    private Integer type;

    /**
     * 资产变动类型
     */
    private BigDecimal totalAmount;

    /**
     * 浮动数量
     */
    private BigDecimal flotAmount;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 备注
     */
    private BigDecimal remark;

    public AccountFeeDetail(Integer memberId, String feeCurrency, Integer type, BigDecimal totalAmount, BigDecimal flotAmount, String createTime, BigDecimal remark) {
        this.memberId = memberId;
        this.feeCurrency = feeCurrency;
        this.type = type;
        this.totalAmount = totalAmount;
        this.flotAmount = flotAmount;
        this.createTime = createTime;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getFeeCurrency() {
        return feeCurrency;
    }

    public void setFeeCurrency(String feeCurrency) {
        this.feeCurrency = feeCurrency;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getFlotAmount() {
        return flotAmount;
    }

    public void setFlotAmount(BigDecimal flotAmount) {
        this.flotAmount = flotAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public BigDecimal getRemark() {
        return remark;
    }

    public void setRemark(BigDecimal remark) {
        this.remark = remark;
    }
}
