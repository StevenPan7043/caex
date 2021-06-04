package com.pmzhongguo.ex.business.dto;

import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

public class RespObjApiKeySecret extends Resp
{

    @ApiModelProperty(value = "api_key")
    private String api_key;
    @ApiModelProperty(value = "api_secret，此值只会在此次返回，以后无法获得，所以需要提醒用户保存好")
    private String api_secret;

    public RespObjApiKeySecret(Integer _status, String _message, String _api_key, String _api_secret)
    {
        super(_status, _message);
        this.api_key = _api_key;
        this.api_secret = _api_secret;
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
}
