package com.pmzhongguo.ex.business.resp;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KLineResp implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = 4883539839205129791L;
    @ApiModelProperty(value = "是否成功")
    public boolean success;
    public Map<String, List<BigDecimal[]>> data;


    public KLineResp(Integer _state, String _msg, String _symbol, String _type, Long _timestamp, List<BigDecimal[]> _datas)
    {
        data = new HashMap<String, List<BigDecimal[]>>();
        data.put("lines", _datas);
        success = true;
    }
}
