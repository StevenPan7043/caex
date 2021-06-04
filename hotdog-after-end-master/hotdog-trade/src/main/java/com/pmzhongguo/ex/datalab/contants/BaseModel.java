package com.pmzhongguo.ex.datalab.contants;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.datalab.entity.vo.SysRequestVO;
import com.pmzhongguo.ex.datalab.manager.AccountFeeManager;
import com.qiniu.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class BaseModel {
	protected Logger logger = LoggerFactory.getLogger(BaseModel.class);

	public static final Gson GSON = new Gson();
	
	public Map<String, Object> getParam(SysRequestVO reqVO) throws Exception {
		Map<String, Object> param = null;
		if(StringUtils.isNotEmpty(reqVO.getParam())) {//from invoke rest
			param = GSON.fromJson(reqVO.getParam(), new TypeToken<Map<String, Object>>(){}.getType());
		} else if (reqVO.getParamMap() != null) {//from web
			param = reqVO.getParamMap();
		}
		return param;
	}

	public Map<String, Object> getParam(String ocrParamter) throws Exception {
		Map<String, Object> param = null;
		if (StringUtils.isNotEmpty(ocrParamter)) {//from invoke rest
			param = GSON.fromJson(ocrParamter, new TypeToken<Map<String, Object>>() {}.getType());
		}
		return param;
	}

	protected void getPageReqMap(Map<String, Object> reqMap, String sortName, String sortOrder) {
		reqMap.put(CurrencyAuthConstant.page, BeanUtil.isEmpty(reqMap.get(CurrencyAuthConstant.page)) ? 1 : reqMap.get(CurrencyAuthConstant.page));
		reqMap.put(CurrencyAuthConstant.pagesize, BeanUtil.isEmpty(reqMap.get(CurrencyAuthConstant.pagesize)) ? 20 : reqMap.get(CurrencyAuthConstant.pagesize));
		if (StringUtils.isNotEmpty(sortName)) {
			reqMap.put(CurrencyAuthConstant.sortname, sortName);
		}
		if (StringUtils.isNotEmpty(sortOrder)) {
			reqMap.put(CurrencyAuthConstant.sortorder, sortOrder);
		}
	}
	protected ObjResp validateSymbol(String symbol) {
		if (BeanUtil.isEmpty(symbol)) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}
		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol);
		if (BeanUtil.isEmpty(currencyPair)) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null);
		}
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, currencyPair);
	}
}
