package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RespApiTokens extends Resp
{
    @ApiModelProperty(value = "API Token列表")
    public List<RespApiToken> apiTokens;

    public RespApiTokens(Integer _state, String _msg, List<RespApiToken> _apiTokens)
    {
        super(_state, _msg);
        apiTokens = _apiTokens;
    }
}
