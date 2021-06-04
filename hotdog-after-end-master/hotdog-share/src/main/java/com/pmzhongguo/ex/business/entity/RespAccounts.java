package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.ex.business.dto.AccountDto;
import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class RespAccounts extends Resp
{
    @ApiModelProperty(value = "资产信息")
    public List<AccountDto> accounts;
    @ApiModelProperty(value = "虚拟币列表")
    public List<RespCurrency> currencys;


    public RespAccounts(Integer _state, String _msg, List<AccountDto> _accounts, List<RespCurrency> _currencys)
    {
        super(_state, _msg);
        accounts = _accounts;
        currencys = _currencys;
    }
}
