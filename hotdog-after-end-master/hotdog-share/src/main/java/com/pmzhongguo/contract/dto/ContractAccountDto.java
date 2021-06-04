package com.pmzhongguo.contract.dto;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * 合约资产
 * @author zn
 */
public class ContractAccountDto {

    @ApiModelProperty(value = "货币代码，如BTC")
    private String currency;
    @ApiModelProperty(value = "合约余额")
    private BigDecimal balance;
    @ApiModelProperty(value = "逐仓余额")
    private BigDecimal zcbalance;
    @ApiModelProperty(value = "合约折合余额")
    private BigDecimal cnyBalance;
    @ApiModelProperty(value = "锁仓冻结的资产")
    private BigDecimal cnyZcbalance;

    private Integer cid;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getZcbalance() {
        return zcbalance;
    }

    public void setZcbalance(BigDecimal zcbalance) {
        this.zcbalance = zcbalance;
    }

    public BigDecimal getCnyBalance() {
        return cnyBalance;
    }

    public void setCnyBalance(BigDecimal cnyBalance) {
        this.cnyBalance = cnyBalance;
    }

    public BigDecimal getCnyZcbalance() {
        return cnyZcbalance;
    }

    public void setCnyZcbalance(BigDecimal cnyZcbalance) {
        this.cnyZcbalance = cnyZcbalance;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }
}
