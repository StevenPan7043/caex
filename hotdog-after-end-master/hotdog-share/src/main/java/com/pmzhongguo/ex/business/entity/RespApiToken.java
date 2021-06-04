package com.pmzhongguo.ex.business.entity;

import io.swagger.annotations.ApiModelProperty;

public class RespApiToken
{
    private Long id;//
    @ApiModelProperty(value = "标签")
    private String label;// 标签
    @ApiModelProperty(value = "APIKey")
    private String api_key;// APIKey
    @ApiModelProperty(value = "创建时间")
    private String create_time;// 创建时间
    @ApiModelProperty(value = "IP地址白名单，为空时不限制，多个IP以半角逗号分隔")
    private String trusted_ip;// IP地址白名单，为空时不限制，多个IP以半角逗号分隔
    @ApiModelProperty(value = "API权限，以逗号分割多个，目前支持 Accounts,Order,Withdraw，分别对应 查询账户资产/委托交易/充值提币")
    private String api_privilege;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
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

    public String getApi_key()
    {
        return api_key;
    }

    public void setApi_key(String api_key)
    {
        this.api_key = api_key;
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

    public String getApi_privilege()
    {
        return api_privilege;
    }

    public void setApi_privilege(String api_privilege)
    {
        this.api_privilege = api_privilege;
    }
}
