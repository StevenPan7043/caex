package com.pmzhongguo.ex.business.entity;

import java.io.Serializable;

public class ApiToken implements Serializable
{

    private static final long serialVersionUID = -2895354381265742L;

    private Integer id;//
    private String label;// 标签
    private Integer member_id;// 会员ID
    private String api_key;// APIKey
    private String api_secret;// APISecret
    private Double trade_commission; // 佣金比例，1为正常收取，0为不收取，0.5为打5折
    private String create_time;// 创建时间
    private String trusted_ip;// IP地址白名单，为空时不限制，多个IP以半角逗号分隔
    private String api_privilege; //API权限
    private Integer t_status;// 状态，0：无效，1：启用
    private Integer api_limit; //1不受限制，0限制

    public Integer getApi_limit()
    {
        return api_limit;
    }

    public void setApi_limit(Integer api_limit)
    {
        this.api_limit = api_limit;
    }

    public String getApi_privilege()
    {
        return api_privilege;
    }

    public void setApi_privilege(String api_privilege)
    {
        this.api_privilege = api_privilege;
    }

    public Double getTrade_commission()
    {
        return trade_commission;
    }

    public void setTrade_commission(Double trade_commission)
    {
        this.trade_commission = trade_commission;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public Integer getMember_id()
    {
        return member_id;
    }

    public void setMember_id(Integer member_id)
    {
        this.member_id = member_id;
    }

    public String getApi_key()
    {
        return api_key;
    }

    public void setApi_key(String api_key)
    {
        this.api_key = api_key;
    }

    public String getApi_secret()
    {
        return api_secret;
    }

    public void setApi_secret(String api_secret)
    {
        this.api_secret = api_secret;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getTrusted_ip()
    {
        return trusted_ip;
    }

    public void setTrusted_ip(String trusted_ip)
    {
        this.trusted_ip = trusted_ip;
    }

    public Integer getT_status()
    {
        return t_status;
    }

    public void setT_status(Integer t_status)
    {
        this.t_status = t_status;
    }
}
