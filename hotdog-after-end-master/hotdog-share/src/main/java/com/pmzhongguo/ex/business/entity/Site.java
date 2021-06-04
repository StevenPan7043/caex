package com.pmzhongguo.ex.business.entity;

public class Site
{
    private Integer id;//站点ID
    private String c_name;//站点名称
    private String is_use;//是否启用，1：是，0：否

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getC_name()
    {
        return c_name;
    }

    public void setC_name(String c_name)
    {
        this.c_name = c_name;
    }

    public String getIs_use()
    {
        return is_use;
    }

    public void setIs_use(String is_use)
    {
        this.is_use = is_use;
    }
}
