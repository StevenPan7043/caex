package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


public class CoinRechargeResp extends Resp
{
    @ApiModelProperty(value = "提现信息")
    public List<CoinRecharge> recharges;
    @ApiModelProperty(value = "总记录数")
    public Integer total;

    public CoinRechargeResp(Integer _state, String _msg, List<CoinRecharge> _recharges, Integer _total)
    {
        super(_state, _msg);
        recharges = _recharges;
        total = _total;
    }
}
