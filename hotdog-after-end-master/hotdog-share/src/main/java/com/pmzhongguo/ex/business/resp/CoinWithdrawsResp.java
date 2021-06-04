package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.business.entity.CoinWithdraw;
import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


public class CoinWithdrawsResp extends Resp
{
    @ApiModelProperty(value = "提现信息")
    public List<CoinWithdraw> withdraws;
    @ApiModelProperty(value = "总记录数")
    public Integer total;

    public CoinWithdrawsResp(Integer _state, String _msg, List<CoinWithdraw> _withdraws, Integer _total)
    {
        super(_state, _msg);
        withdraws = _withdraws;
        total = _total;
    }
}
