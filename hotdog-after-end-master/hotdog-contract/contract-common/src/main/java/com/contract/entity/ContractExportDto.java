package com.contract.entity;


import com.contract.config.ExcelField;

import java.math.BigDecimal;

/**
 *  @description: 导出excel
 *  @date: 2029-08-20 11:07
 *  @author: 十一
 */

public class ContractExportDto {


    private Integer userid;
    @ExcelField(title = "账号", align = 2 )
    private String phone;//电话号
    @ExcelField(title = "实名", align = 2 )
    private String realname;
    @ExcelField(title = "订单编号", align = 2)
    private String ordercode;//电话号
    @ExcelField(title = "保证金", align = 2)
    private BigDecimal realmoney;//电话号
    @ExcelField(title = "持仓量", align = 2)
    private String coinnum;//电话号
    @ExcelField(title = "杠杆倍数", align = 2)
    private Integer gearing;//电话号
    @ExcelField(title = "持仓币价", align = 2)
    private BigDecimal buyprice;//持仓价
    @ExcelField(title = "平仓币价", align = 2)
    private BigDecimal stopprice;//平仓价
    @ExcelField(title = "利息", align = 2)
    private BigDecimal rates;//利息
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public BigDecimal getRealmoney() {
        return realmoney;
    }

    public void setRealmoney(BigDecimal realmoney) {
        this.realmoney = realmoney;
    }

    public String getCoinnum() {
        return coinnum;
    }

    public void setCoinnum(String coinnum) {
        this.coinnum = coinnum;
    }

    public Integer getGearing() {
        return gearing;
    }

    public void setGearing(Integer gearing) {
        this.gearing = gearing;
    }

    public BigDecimal getBuyprice() {
        return buyprice;
    }

    public void setBuyprice(BigDecimal buyprice) {
        this.buyprice = buyprice;
    }

    public BigDecimal getStopprice() {
        return stopprice;
    }

    public void setStopprice(BigDecimal stopprice) {
        this.stopprice = stopprice;
    }

    public BigDecimal getRates() {
        return rates;
    }

    public void setRates(BigDecimal rates) {
        this.rates = rates;
    }

    public BigDecimal getSpreadmoney() {
        return spreadmoney;
    }

    public void setSpreadmoney(BigDecimal spreadmoney) {
        this.spreadmoney = spreadmoney;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getStopwin() {
        return stopwin;
    }

    public void setStopwin(BigDecimal stopwin) {
        this.stopwin = stopwin;
    }

    public BigDecimal getStopdonat() {
        return stopdonat;
    }

    public void setStopdonat(BigDecimal stopdonat) {
        this.stopdonat = stopdonat;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSettleflag() {
        return settleflag;
    }

    public void setSettleflag(String settleflag) {
        this.settleflag = settleflag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getStoptime() {
        return stoptime;
    }

    public void setStoptime(String stoptime) {
        this.stoptime = stoptime;
    }

    @ExcelField(title = "点差金额", align = 2)
    private BigDecimal spreadmoney;//点差金额
    @ExcelField(title = "手续费", align = 2)
    private BigDecimal tax;//手续费
    @ExcelField(title = "止盈", align = 2)
    private BigDecimal stopwin;//止盈
    @ExcelField(title = "止损", align = 2)
    private BigDecimal stopdonat;//止损
    @ExcelField(title = "描述", align = 2)
    private String  remark;//描述
    @ExcelField(title = "货币类型", align = 2)
    private String  coin;//货币类型
    @ExcelField(title = "订单类型", align = 2)
    private String type;//订单类型 1：开多  2：开空
    @ExcelField(title = "订单状态", align = 2)
    private String status;//订单状态 1:持仓中 2：已完成
    @ExcelField(title = "是否结算", align = 2)
    private String settleflag;//是否结算 -1:未结算 0：已结算
    @ExcelField(title = "子公司", align = 2)
    private String username;//子公司
    @ExcelField(title = "持仓时间", align = 2)
    private String createtime;//持仓时间
    @ExcelField(title = "平仓时间", align = 2)
    private String stoptime;//持仓时间




}
