package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.business.entity.OTCAds;
import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


public class AdsResp extends Resp
{
    @ApiModelProperty(value = "广告信息")
    public List<OTCAds> ads;
    @ApiModelProperty(value = "总记录数")
    public Integer total;


    public AdsResp(Integer _state, String _msg, List<OTCAds> _ads, Integer _total)
    {
        super(_state, _msg);
        ads = _ads;
        total = _total;
    }
}
