package com.pmzhongguo.ex.business.entity;

public class CoinWithdrawAddr {
    private Integer id;//会员提现地址ID
    private Integer member_id;//会员ID
    private Integer currency_id;//d_currency的id，CNY不会出现在此
    private String currency;//d_currency的currency
    private String addr_label;//地址标签
    private String addr;//地址
    private Integer c_status;//状态：1：正常，2：已作废

    /**
     * 币种链类型
     */
    private String currency_chain_type;
    private String sms_code;
    private String token; //Token


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public Integer getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(Integer currency_id) {
        this.currency_id = currency_id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAddr_label() {
        return addr_label;
    }

    public void setAddr_label(String addr_label) {
        this.addr_label = addr_label;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getC_status() {
        return c_status;
    }

    public void setC_status(Integer c_status) {
        this.c_status = c_status;
    }

    public String getCurrency_chain_type() {
        return currency_chain_type;
    }

    public void setCurrency_chain_type(String currency_chain_type) {
        this.currency_chain_type = currency_chain_type;
    }
}
