package com.pmzhongguo.ex.business.dto;

import com.pmzhongguo.zzextool.annotation.ExcelField;

import java.math.BigDecimal;

public class AccountHistoryExportDto {

    //用户ID
    @ExcelField(title = "UID", align = 2)
    private Integer member_id;
    //用户用户账号
    @ExcelField(title = "会员账号", align = 2)
    private String m_name;
    //用户姓名
    @ExcelField(title = "用户姓名", align = 2)
    private String real_name;
    //货币类型
    @ExcelField(title = "货币种类", align = 2)
    private String currency;
    //全仓余额
    @ExcelField(title = "全仓余额", align = 2)
    private BigDecimal balance;
    //逐仓余额
    @ExcelField(title = "逐仓余额", align = 2)
    private BigDecimal zcbalance;
    //总余额，可交易余额 = 总余额 - 冻结余额
    @ExcelField(title = "总余额", align = 2)
    private BigDecimal total_balance;
    //冻结余额
    @ExcelField(title = "冻结余额", align = 2)
    private BigDecimal frozen_balance;
    //资产日期
    @ExcelField(title = "日期", align = 2, sort = 1)
    private String record_date;
    //总金额,将所有不同货币金额转换成usdt的总资产
    private BigDecimal totalMoney;
    //用户遍历求和：member_id+_+record_date
    private String idtime;

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

    public BigDecimal getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(BigDecimal total_balance) {
        this.total_balance = total_balance;
    }

    public BigDecimal getFrozen_balance() {
        return frozen_balance;
    }

    public void setFrozen_balance(BigDecimal frozen_balance) {
        this.frozen_balance = frozen_balance;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getIdtime() {
        return idtime;
    }

    public void setIdtime(String idtime) {
        this.idtime = idtime;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }
}
