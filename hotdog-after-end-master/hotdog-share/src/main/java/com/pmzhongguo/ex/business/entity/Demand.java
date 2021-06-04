package com.pmzhongguo.ex.business.entity;

import java.util.List;


public class Demand
{
    private Integer id;//
    private Integer member_id;//提交人ID
    private String m_name;//提交人账号
    private String create_time;//提交时间
    private String d_title;//需求标题
    private String d_memo;//需求内容
    private String d_attach_url; //附件URL
    private Integer d_status;//处理状态，0待处理，1处理中，2已关闭
    private Integer is_has_unread; //是否有未读消息
    private Integer is_has_unread_plat; //平台下面是否有未读消息
    private List<DemandLog> demandLogList;

    public String getD_attach_url()
    {
        return d_attach_url;
    }

    public void setD_attach_url(String d_attach_url)
    {
        this.d_attach_url = d_attach_url;
    }

    public Integer getIs_has_unread_plat()
    {
        return is_has_unread_plat;
    }

    public void setIs_has_unread_plat(Integer is_has_unread_plat)
    {
        this.is_has_unread_plat = is_has_unread_plat;
    }

    public List<DemandLog> getDemandLogList()
    {
        return demandLogList;
    }

    public void setDemandLogList(List<DemandLog> demandLogList)
    {
        this.demandLogList = demandLogList;
    }

    public Integer getIs_has_unread()
    {
        return is_has_unread;
    }

    public void setIs_has_unread(Integer is_has_unread)
    {
        this.is_has_unread = is_has_unread;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public String getM_name()
    {
        return m_name;
    }

    public void setM_name(String m_name)
    {
        this.m_name = m_name;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getD_title()
    {
        return d_title;
    }

    public void setD_title(String d_title)
    {
        this.d_title = d_title;
    }

    public String getD_memo()
    {
        return d_memo;
    }

    public void setD_memo(String d_memo)
    {
        this.d_memo = d_memo;
    }

    public Integer getD_status()
    {
        return d_status;
    }

    public void setD_status(Integer d_status)
    {
        this.d_status = d_status;
    }
}
