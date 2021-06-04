package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.business.entity.OTCAds;
import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;


public class AdResp extends Resp
{
    @ApiModelProperty(value = "提现信息")
    public OTCAds ad;

    public AdResp(Integer _state, String _msg, OTCAds _ad)
    {
        super(_state, _msg);
        ad = _ad;
    }
}
