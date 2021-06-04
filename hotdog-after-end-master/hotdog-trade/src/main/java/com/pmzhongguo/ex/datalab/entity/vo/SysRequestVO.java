package com.pmzhongguo.ex.datalab.entity.vo;

import java.util.Map;

public class SysRequestVO {
	
	private String param;

	private Map<String, Object> paramMap;
	
	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

	public void setParam(String param) throws Exception {
		this.param = param;
	}

	public String getParam() {
		return param;
	}
}
