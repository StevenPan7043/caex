package com.pmzhongguo.ex.framework.entity;

import java.io.Serializable;

public class FrmUser implements Serializable
{
    private static final long serialVersionUID = -458045724375300041L;

    private Integer id;//
    private String user_real_name;// 姓名，唯一约束，重名的，后面加1、2、3等
    private String user_name;// 用户名，唯一约束
    private String user_pwd;// 用户的密码
    private String mobile_phone;// 手机号码
    private Integer is_can_login;// 是否允许登录系统
    private String google_auth_key; //谷歌验证Key
    private Integer is_deleted;// 是否被删除，不允许物理删除
    private Integer otc_owner_id; //OTC广告主ID，为0表示所有


    public Integer getOtc_owner_id()
    {
        return otc_owner_id;
    }

    public void setOtc_owner_id(Integer otc_owner_id)
    {
        this.otc_owner_id = otc_owner_id;
    }

    public String getGoogle_auth_key()
    {
        return google_auth_key;
    }

    public void setGoogle_auth_key(String google_auth_key)
    {
        this.google_auth_key = google_auth_key;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUser_real_name()
    {
        return user_real_name;
    }

    public void setUser_real_name(String user_real_name)
    {
        this.user_real_name = user_real_name;
    }

    public String getUser_name()
    {
        return user_name;
    }

    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
    }

    public String getUser_pwd()
    {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd)
    {
        this.user_pwd = user_pwd;
    }

    public String getMobile_phone()
    {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone)
    {
        this.mobile_phone = mobile_phone;
    }

    public Integer getIs_can_login()
    {
        return is_can_login;
    }

    public void setIs_can_login(Integer is_can_login)
    {
        this.is_can_login = is_can_login;
    }

    public Integer getIs_deleted()
    {
        return is_deleted;
    }

    public void setIs_deleted(Integer is_deleted)
    {
        this.is_deleted = is_deleted;
    }
}
