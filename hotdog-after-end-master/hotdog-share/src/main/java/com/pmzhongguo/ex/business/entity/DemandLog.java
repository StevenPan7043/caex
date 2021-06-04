package com.pmzhongguo.ex.business.entity;


public class DemandLog
{
    private Long id;//
    private Integer demand_id;//需求ID
    private Integer log_type;//0表示会员发布，1表示平台客服回复
    private String create_time;//提交时间
    private String l_memo;//需求内容
    private Integer l_memo_type;//内容类型，1表示文本，2表示图片等附件
    private String plat_user_name;//平台答复人账号，会员发布时可填空，或者填会员账号


    // 附加字段
    private Integer d_status; //需求的状态


    public Integer getL_memo_type()
    {
        return l_memo_type;
    }

    public void setL_memo_type(Integer l_memo_type)
    {
        this.l_memo_type = l_memo_type;
    }

    public Integer getD_status()
    {
        return d_status;
    }

    public void setD_status(Integer d_status)
    {
        this.d_status = d_status;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getDemand_id()
    {
        return demand_id;
    }

    public void setDemand_id(Integer demand_id)
    {
        this.demand_id = demand_id;
    }

    public Integer getLog_type()
    {
        return log_type;
    }

    public void setLog_type(Integer log_type)
    {
        this.log_type = log_type;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getL_memo()
    {
        return l_memo;
    }

    public void setL_memo(String l_memo)
    {
        this.l_memo = l_memo;
    }

    public String getPlat_user_name()
    {
        return plat_user_name;
    }

    public void setPlat_user_name(String plat_user_name)
    {
        this.plat_user_name = plat_user_name;
    }
}
