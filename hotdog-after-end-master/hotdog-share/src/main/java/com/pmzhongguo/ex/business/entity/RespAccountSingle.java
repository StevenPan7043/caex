package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;


public class RespAccountSingle extends Resp
{
    @ApiModelProperty(value = "资产信息")
    public RespAccount account;

    public RespAccountSingle(Integer _state, String _msg, RespAccount _account)
    {
        super(_state, _msg);
        account = _account;
    }
}
