package com.contract.dto;

import com.contract.service.Page;

import java.math.BigDecimal;

/**
 * 业务员团队数据统计
 * @author jary
 * @creatTime 2020/2/22 4:28 PM
 */
public class SalesTeamDto extends Page {

    /**
     * 账号
     */
    private String phone;

    /**
     * 账户Id
     */
    private int cid;

    /**
     * 真是姓名
     */
    private String realName;

    /**
     * 总转入
     */
    private BigDecimal transInSum;

    /**
     * 总转出
     */
    private BigDecimal transOutSum;

    /**
     * 总盈亏
     */
    private BigDecimal rates;
    /**
     * 总手续费
     */
    private BigDecimal taxes;

    /**
     * 总余额
     */
    private BigDecimal totalBalance;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public BigDecimal getTransInSum() {
        return transInSum;
    }

    public void setTransInSum(BigDecimal transInSum) {
        this.transInSum = transInSum;
    }

    public BigDecimal getTransOutSum() {
        return transOutSum;
    }

    public void setTransOutSum(BigDecimal transOutSum) {
        this.transOutSum = transOutSum;
    }

    public BigDecimal getRates() {
        return rates;
    }

    public void setRates(BigDecimal rates) {
        this.rates = rates;
    }

    public BigDecimal getTaxes() {
        return taxes;
    }

    public void setTaxes(BigDecimal taxes) {
        this.taxes = taxes;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }
}
