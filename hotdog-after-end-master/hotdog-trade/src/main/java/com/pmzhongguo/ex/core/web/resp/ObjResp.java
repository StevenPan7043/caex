package com.pmzhongguo.ex.core.web.resp;


import com.wordnik.swagger.annotations.ApiModelProperty;

public class ObjResp extends Resp {
	@ApiModelProperty(value = "返回数据")
	private Object data;

	public ObjResp() {
		this(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}
	
	public ObjResp(Integer _state, String _msg, Object _data) {
		super(_state, _msg);
		this.data = _data;
	}



    public static ObjResp failMsg (String _msg) {
        return new ObjResp(FAIL, _msg,null);
    }

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
