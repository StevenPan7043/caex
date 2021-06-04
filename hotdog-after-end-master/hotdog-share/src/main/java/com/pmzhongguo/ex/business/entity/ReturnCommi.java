package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.zzextool.utils.JsonUtil;

import java.math.BigDecimal;

/**
 * 用户返佣比例和有效天数
 */
public class ReturnCommi
{
    private Long id;

    /**
     * 会员ID
     */
    private Integer member_id;

    /**
     * 邀请码
     */
    private String introduce_id;

    /**
     * 返佣有效时间 yyyy-MM-dd hh:mm:ss
     */
    private String return_commi_time;

    /**
     * 返佣比例
     */
    private BigDecimal return_commi_rate;

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

    public String getIntroduce_id()
    {
        return introduce_id;
    }

    public void setIntroduce_id(String introduce_id)
    {
        this.introduce_id = introduce_id;
    }

    public String getReturn_commi_time()
    {
        return return_commi_time;
    }

    public void setReturn_commi_time(String return_commi_time)
    {
        this.return_commi_time = return_commi_time;
    }

    public BigDecimal getReturn_commi_rate()
    {
        return return_commi_rate;
    }

    public void setReturn_commi_rate(BigDecimal return_commi_rate)
    {
        this.return_commi_rate = return_commi_rate;
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


    @Override
    public String toString()
    {
        String result = "";
        try
        {
            result = JsonUtil.beanToJson(this);
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }
}
