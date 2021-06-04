package com.pmzhongguo.ex.business.entity;

import java.math.BigDecimal;

/**
 * 用户返佣每条总数量
 */
public class ReturnCommiAmount {

    private Long id;

    /**
     * 会员ID
     */
    private Integer member_id;

    /**
     * 返佣总数
     */
    private String currency;

    /**
     * 返佣总数
     */
    private BigDecimal return_commi_num;

    /**
     * create_time
     */
    private String create_time;

    /**
     * update_time
     */
    private String update_time;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getReturn_commi_num() {
        return return_commi_num;
    }

    public void setReturn_commi_num(BigDecimal return_commi_num) {
        this.return_commi_num = return_commi_num;
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
}
