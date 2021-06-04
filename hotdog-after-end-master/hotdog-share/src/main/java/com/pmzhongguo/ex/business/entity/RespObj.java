package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

public class RespObj extends Resp
{

    @ApiModelProperty(value = "返回数据")
    private Object data;

    public RespObj(Integer _status, String _message, Object _data)
    {
        super(_status, _message);
        this.data = _data;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        this.data = data;
    }
}
