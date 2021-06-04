package com.pmzhongguo.ex.business.entity;

import java.math.BigDecimal;

/**
 * 用户交易返佣明细
 */
public class ReturnCommiRecord
{
    private Long id;

    /**
     * 会员ID,当前交易的用户
     */
    private Integer member_id;

    /**
     * 关联返佣表id，return_commi
     */
    private Long return_commi_id;

    /**
     * 返佣数量
     */
    private BigDecimal return_commi_num;

    /**
     * 交易数量
     */
    private BigDecimal tx_num;

    /**
     * 币种
     */
    private String currency;

    /**
     * create_time
     */
    private String create_time;

    /**
     * update_time
     */
    private String update_time;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getMember_id()
    {
        return member_id;
    }

    public void setMember_id(Integer member_id)
    {
        this.member_id = member_id;
    }

    public Long getReturn_commi_id()
    {
        return return_commi_id;
    }

    public void setReturn_commi_id(Long return_commi_id)
    {
        this.return_commi_id = return_commi_id;
    }

    public BigDecimal getReturn_commi_num()
    {
        return return_commi_num;
    }

    public void setReturn_commi_num(BigDecimal return_commi_num)
    {
        this.return_commi_num = return_commi_num;
    }

    public BigDecimal getTx_num()
    {
        return tx_num;
    }

    public void setTx_num(BigDecimal tx_num)
    {
        this.tx_num = tx_num;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getUpdate_time()
    {
        return update_time;
    }

    public void setUpdate_time(String update_time)
    {
        this.update_time = update_time;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

}
