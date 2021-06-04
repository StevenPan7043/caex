package com.pmzhongguo.ex.core.web.resp;

import com.wordnik.swagger.annotations.ApiModelProperty;

import java.util.List;



public class LstResp extends Resp {
	public static final Integer SUCCESS = 1;

	@ApiModelProperty(value = "数据记录条数")
	private Integer total;
	@ApiModelProperty(value = "列表数据")
	private List datas;

	public LstResp(Integer _state, String _msg, Integer _total,
			List _datas) {
		super(_state, _msg);
		total = _total;
		datas = _datas;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List getDatas() {
		return datas;
	}

	public void setDatas(List datas) {
		this.datas = datas;
	}
}
