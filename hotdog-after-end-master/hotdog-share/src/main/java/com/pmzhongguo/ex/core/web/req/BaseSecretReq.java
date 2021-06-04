package com.pmzhongguo.ex.core.web.req;

import io.swagger.annotations.ApiModelProperty;

public class BaseSecretReq
{

    @ApiModelProperty(value = "api_key可以在用户中心中获取", required = true)
    private String api_key;
    @ApiModelProperty(value = "时间戳，标准北京时间，时区为东八区，自1970年1月1日 0点0分0秒以来的秒数。注意：部分系统取到的值为毫秒级，需要转换成秒(10位数字)。", required = true)
    private Integer timestamp;
    @ApiModelProperty(value = "使用api_secret对请求参数进行签名的结果", required = true)
    private String sign;
    @ApiModelProperty(value = "使用api_secret对请求参数进行签名的方法，目前支持MD5，签名方法详见单独说明", required = true)
    private String sign_type;


    public String getSign_type()
    {
        return sign_type;
    }

    public void setSign_type(String sign_type)
    {
        this.sign_type = sign_type;
    }

    public String getApi_key()
    {
        return api_key;
    }

    public void setApi_key(String api_key)
    {
        this.api_key = api_key;
    }

    public Integer getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp)
    {
        this.timestamp = timestamp;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }
}
