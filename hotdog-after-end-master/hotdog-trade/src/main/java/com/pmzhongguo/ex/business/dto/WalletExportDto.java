package com.pmzhongguo.ex.business.dto;

import com.pmzhongguo.zzextool.annotation.ExcelField;

import java.math.BigDecimal;

public class WalletExportDto {
    //用户ID
    @ExcelField(title = "UID", align = 2)
    private Integer member_id;
    //用户用户账号
    @ExcelField(title = "会员账号", align = 2)
    private String m_name;
    //货币类型
    @ExcelField(title = "货币种类", align = 2)
    private String currency;
    //全仓余额
    @ExcelField(title = "全仓余额", align = 2)
    private BigDecimal balance;
    //逐仓余额
    @ExcelField(title = "逐仓余额", align = 2)
    private BigDecimal zcbalance;

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

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
}
