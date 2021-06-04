package com.pmzhongguo.ex.business.entity;

public class OTCOwner
{
    private Integer id;//广告主ID
    private String o_name;//广告主姓名
    private String bank_info;//银行卡信息
    private String alipay_info;//支付宝信息
    private String wxpay_info;//微信信息
    private String last_time;//最后在线时间
    private Integer avg_time;//平均放行时间


    public String getBank_info()
    {
        return bank_info;
    }

    public void setBank_info(String bank_info)
    {
        this.bank_info = bank_info;
    }

    public String getAlipay_info()
    {
        return alipay_info;
    }

    public void setAlipay_info(String alipay_info)
    {
        this.alipay_info = alipay_info;
    }

    public String getWxpay_info()
    {
        return wxpay_info;
    }

    public void setWxpay_info(String wxpay_info)
    {
        this.wxpay_info = wxpay_info;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getO_name()
    {
        return o_name;
    }

    public void setO_name(String o_name)
    {
        this.o_name = o_name;
    }

    public String getLast_time()
    {
        return last_time;
    }

    public void setLast_time(String last_time)
    {
        this.last_time = last_time;
    }

    public Integer getAvg_time()
    {
        return avg_time;
    }

    public void setAvg_time(Integer avg_time)
    {
        this.avg_time = avg_time;
    }
}
