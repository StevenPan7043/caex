package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.business.dto.AccountDto;
import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


public class AccountsResp extends Resp
{
    @ApiModelProperty(value = "毫秒级生成时间")
    public Long timestamp;
    @ApiModelProperty(value = "账户余额信息")
    public List<AccountDto> datas;


    public AccountsResp(Integer _state, String _msg, Long _timestamp, List<AccountDto> _datas)
    {
        super(_state, _msg);
        datas = _datas;
        timestamp = _timestamp;
    }
}
