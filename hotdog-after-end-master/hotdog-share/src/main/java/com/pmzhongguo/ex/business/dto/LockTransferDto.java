package com.pmzhongguo.ex.business.dto;

import java.math.BigDecimal;

public class LockTransferDto {

    /**
     * 货币代码
     */
    private String currency;

    /**
     * 来源账户
     */
    private String sourceAccount;

    /**
     * 目标账户
     */
    private String targetAccount;


    /**
     * 划转货币数量
     */
    private BigDecimal transferNum;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public BigDecimal getTransferNum() {
        return transferNum;
    }

    public void setTransferNum(BigDecimal transferNum) {
        this.transferNum = transferNum;
    }
}
