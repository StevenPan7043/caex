package com.pmzhongguo.ex.datalab.entity;

import java.math.BigDecimal;

/**
 * @author jary
 * @creatTime 2019/11/29 3:53 PM
 */
public class AccountFee {

    private Integer id;

    private Integer memberId;

    /**
     * 手续费币种
     */
    private String feeCurrency;

    /**
     * 总资产
     */
    private BigDecimal totalAmount;

    /**
     * 冻结资产
     */
    private BigDecimal forzenAmount;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getForzenAmount() {
        return forzenAmount;
    }

    public void setForzenAmount(BigDecimal forzenAmount) {
        this.forzenAmount = forzenAmount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}

