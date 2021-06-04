package com.pmzhongguo.ex.business.resp;

import com.pmzhongguo.ex.business.dto.SymbolDto;
import com.pmzhongguo.ex.core.web.resp.Resp;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


public class SymbolsResp extends Resp
{

    @ApiModelProperty(value = "交易对")
    public List<SymbolDto> symbols;

    public SymbolsResp(Integer _state, String _msg, List<SymbolDto> _symbols)
    {
        super(_state, _msg);
        symbols = _symbols;
    }
}
