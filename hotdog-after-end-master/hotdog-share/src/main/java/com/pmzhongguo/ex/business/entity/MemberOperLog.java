package com.pmzhongguo.ex.business.entity;

public class MemberOperLog
{
    private Integer id;//
    private Integer member_id;// 会员id
    private String oper_time;// 操作时间
    private String oper_memo;// 操作内容
    private String province;// 省
    private String city;// 市
    private String address;// 地址
    private String baidu_lng;// 经纬度
    private String baidu_lat;// 经纬度

    // ---------------附加字段--------------
    private String login_token; // 登录的Token

    private String push_appid;// 推送appid
    private String push_appkey;// 推送appkey
    private String push_clientid;// 推送clientid
    private String push_token;// 推送token
    private String last_os;// 最后使用机型

    private String xcx_openid; // 小程序ID
    private String daily_xcx_openid; // 今日油价的OpenId

    public String getDaily_xcx_openid()
    {
        return daily_xcx_openid;
    }

    public void setDaily_xcx_openid(String daily_xcx_openid)
    {
        this.daily_xcx_openid = daily_xcx_openid;
    }

    public String getXcx_openid()
    {
        return xcx_openid;
    }

    public void setXcx_openid(String xcx_openid)
    {
        this.xcx_openid = xcx_openid;
    }

    public String getPush_appid()
    {
        return push_appid;
    }

    public void setPush_appid(String push_appid)
    {
        this.push_appid = push_appid;
    }

    public String getPush_appkey()
    {
        return push_appkey;
    }

    public void setPush_appkey(String push_appkey)
    {
        this.push_appkey = push_appkey;
    }

    public String getPush_clientid()
    {
        return push_clientid;
    }

    public void setPush_clientid(String push_clientid)
    {
        this.push_clientid = push_clientid;
    }

    public String getPush_token()
    {
        return push_token;
    }

    public void setPush_token(String push_token)
    {
        this.push_token = push_token;
    }

    public String getLast_os()
    {
        return last_os;
    }

    public void setLast_os(String last_os)
    {
        this.last_os = last_os;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getLogin_token()
    {
        return login_token;
    }

    public void setLogin_token(String login_token)
    {
        this.login_token = login_token;
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

    public String getOper_time()
    {
        return oper_time;
    }

    public void setOper_time(String oper_time)
    {
        this.oper_time = oper_time;
    }

    public String getOper_memo()
    {
        return oper_memo;
    }

    public void setOper_memo(String oper_memo)
    {
        this.oper_memo = oper_memo;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getBaidu_lng()
    {
        return baidu_lng;
    }

    public void setBaidu_lng(String baidu_lng)
    {
        this.baidu_lng = baidu_lng;
    }

    public String getBaidu_lat()
    {
        return baidu_lat;
    }

    public void setBaidu_lat(String baidu_lat)
    {
        this.baidu_lat = baidu_lat;
    }

}
