package com.pmzhongguo.ex.business.dto;

import com.pmzhongguo.zzextool.utils.JsonUtil;

import java.math.BigDecimal;

/**
 * @description: 用户币种每天返佣数量
 * @date: 2019-03-19 09:45
 * @author: 十一
 */
public class ReturnCommiCurrAmountDto {

    /**
     * id
     */
    private Long id;

    /**
     * 会员ID,当前交易的用户
     */
    private Integer member_id;


    /**
     *手续费数量
     */
    private BigDecimal fee_amount;

    /**
     *返佣数量
     */
    private BigDecimal amount;

    /**
     * 汇率
     */
    private BigDecimal rate;


    /**
     * 币种
     */
    private String currency;

    /**
     * 手续费货币
     */
    private String fee_currency;

    /**
     * create_time
     */
    private String create_time;

    /**
     * update_time
     */
    private String update_time;

    private String searchDate;

    private String tType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public BigDecimal getFee_amount() {
        return fee_amount;
    }

    public void setFee_amount(BigDecimal fee_amount) {
        this.fee_amount = fee_amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getFee_currency() {
        return fee_currency;
    }

    public void setFee_currency(String fee_currency) {
        this.fee_currency = fee_currency;
    }

    public String gettType() {
        return tType;
    }

    public void settType(String tType) {
        this.tType = tType;
    }

    public String getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(String searchDate) {
        this.searchDate = searchDate;
    }

    @Override
    public String toString() {
        String result = "";
        try {
            result = JsonUtil.beanToJson(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
