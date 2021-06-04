package com.pmzhongguo.ex.datalab.entity.dto;

import java.math.BigDecimal;

/**
 * @author jary
 * @creatTime 2019/12/3 1:53 PM
 */
public class DataLabDto {


    private Integer uidList;

    private BigDecimal currencyFlow;

    private BigDecimal rechargeNo;

    private BigDecimal withdrawNo;

    public Integer getUidList() {
        return uidList;
    }

    public void setUidList(Integer uidList) {
        this.uidList = uidList;
    }

    public BigDecimal getCurrencyFlow() {
        return currencyFlow;
    }

    public void setCurrencyFlow(BigDecimal currencyFlow) {
        this.currencyFlow = currencyFlow;
    }

    public BigDecimal getRechargeNo() {
        return rechargeNo;
    }

    public void setRechargeNo(BigDecimal rechargeNo) {
        this.rechargeNo = rechargeNo;
    }

    public BigDecimal getWithdrawNo() {
        return withdrawNo;
    }

    public void setWithdrawNo(BigDecimal withdrawNo) {
        this.withdrawNo = withdrawNo;
    }
}
