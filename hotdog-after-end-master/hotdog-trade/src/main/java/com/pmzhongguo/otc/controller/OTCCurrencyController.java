package com.pmzhongguo.otc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "NEW OTC币种相关", description = "OTC币种相关", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("otc/currency")
public class OTCCurrencyController extends TopController {

	@ApiOperation(value = "获得系统支持的OTC币种", notes = "获得系统支持的OTC币种", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "getList", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getCurrency(HttpServletRequest request,
							   HttpServletResponse response) {
		List<Currency> list = HelpUtils.getCurrencyIsOtcLst();
		List<String> result = new ArrayList<String>();
		for(Currency c : list) {
			result.add(c.getCurrency());
		}
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, result);
	}
}
