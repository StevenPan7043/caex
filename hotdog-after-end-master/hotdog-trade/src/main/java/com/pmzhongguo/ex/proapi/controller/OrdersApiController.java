package com.pmzhongguo.ex.proapi.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pmzhongguo.ex.business.dto.OrderRespDto;
import com.pmzhongguo.ex.business.dto.TradeRespDto;
import com.pmzhongguo.ex.business.entity.ApiToken;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.entity.ReqBaseSecret;
import com.pmzhongguo.ex.business.entity.RespObj;
import com.pmzhongguo.ex.business.resp.OrdersResp;
import com.pmzhongguo.ex.business.resp.TradesResp;
import com.pmzhongguo.ex.business.service.ApiAccessLimitService;
import com.pmzhongguo.ex.business.service.ExService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "程序交易API|交易查询接口", description = "程序交易API|交易查询接口")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("o/api")
public class OrdersApiController extends TopController {

	@Autowired
	private ExService exService;
	@Autowired
	private ApiAccessLimitService apiAccessLimitService;

	@ApiOperation(value = "根据订单id获得订单详细信息『单个』（仅返回当前apikey对应数据）", notes = "<h5>symbol: </h5>交易对名称，如BTCCNY、LTCCNY、ETHCNY <h5>o_id:</h5>订单ID<h5>签名所需参数: </h5>api_key、timestamp、sign_type和sign详见签名方法", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "orderI/{symbol}/{id}/{api_key}/{timestamp}/{sign_type}/{sign}", method = RequestMethod.GET)
	@ResponseBody
	public Resp getOrderI(HttpServletRequest request,
			HttpServletResponse response, @PathVariable("symbol") String symbol, @PathVariable("id") String id, @PathVariable("api_key") String apikey,
			@PathVariable("timestamp") Long timestamp, @PathVariable("sign_type") String signType, @PathVariable("sign") String sign) {
		
		apiAccessLimitService.check("getOrders", apikey);
		
		if (null == symbol) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol.toUpperCase());
		if (null == currencyPair) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		
		if (HelpUtils.nullOrBlank(id)) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ORDER_ID.getErrorENMsg(), null, 0);
		}
		
		ReqBaseSecret reqBaseSecret = new ReqBaseSecret();
		reqBaseSecret.setApi_key(apikey);
		reqBaseSecret.setSign_type(signType);
		reqBaseSecret.setTimestamp(timestamp);
		reqBaseSecret.setSign(sign);
		String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
		if (!"".equals(validateRet)) {
			if("ILLEGAL_TIMESTAMP".equals(validateRet)) {
				return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
			}
			return new OrdersResp(Resp.FAIL, validateRet, null, 0);
		}
		
		String ip = HelpUtils.getIpAddr(request);		
		
		// 校验合法性，非法情况直接抛出异常
		ApiToken apiToken = memberService.getApiToken(apikey);
		validateApiToken(HelpUtils.objToMap(reqBaseSecret), apiToken, ip, "Order");
		
		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("token_id", apiToken.getId());
		orderMap.put("symbol", symbol);
		orderMap.put("o_id", id);
		orderMap.put("table_name", symbol.toLowerCase());
		orderMap.put("price_precision", currencyPair.getPrice_precision());
		orderMap.put("volume_precision", currencyPair.getVolume_precision());
		List<OrderRespDto> jsonList = exService.getOrderByIdAndTokenid(orderMap);
		return new OrdersResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList, jsonList == null ? 0 : jsonList.size());
	}
	
	@ApiOperation(value = "获得订单列表（仅返回当前apikey对应数据）", notes = "<h5>symbol: </h5>交易对名称，如BTCCNY、LTCCNY、ETHCNY <h5>page：</h5>页数，从1开始，<h5>pageSize：</h5>每页记录数，最大20，默认按时间倒叙排列 <h5>status: </h5>0表示未完成（挂单中），1表示已完成（含已取消），2表示所有<h5>签名所需参数: </h5>api_key、timestamp、sign_type和sign详见签名方法", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "orders/{symbol}/{page}/{pageSize}/{status}/{api_key}/{timestamp}/{sign_type}/{sign}", method = RequestMethod.GET)
	@ResponseBody
	public Resp getOrders(HttpServletRequest request, @PathVariable("symbol") String symbol, @PathVariable("page") Integer page,
			@PathVariable("pageSize") Integer pageSize, @PathVariable("status") Integer status, @PathVariable("api_key") String apikey,
			@PathVariable("timestamp") Long timestamp, @PathVariable("sign_type") String signType, @PathVariable("sign") String sign) {
		apiAccessLimitService.check("getOrders", apikey);
		if (null == symbol) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol.toUpperCase());
		if (null == currencyPair) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		
		ReqBaseSecret reqBaseSecret = new ReqBaseSecret();
		reqBaseSecret.setApi_key(apikey);
		reqBaseSecret.setSign_type(signType);
		reqBaseSecret.setTimestamp(timestamp);
		reqBaseSecret.setSign(sign);
		String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
		if (!"".equals(validateRet)) {
			if("ILLEGAL_TIMESTAMP".equals(validateRet)) {
				return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
			}
			return new OrdersResp(Resp.FAIL, validateRet, null, 0);
		}
		
		String ip = HelpUtils.getIpAddr(request);		
		
		// 校验合法性，非法情况直接抛出异常
		ApiToken apiToken = memberService.getApiToken(apikey);
		validateApiToken(HelpUtils.objToMap(reqBaseSecret), apiToken, ip, "Order");


		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("token_id", apiToken.getId());
		orderMap.put("table_name", symbol.toLowerCase());
		orderMap.put("page", page);
		orderMap.put("pagesize", pageSize > 20 ? 20 : pageSize);
		orderMap.put("sortname", "o.id");
		orderMap.put("sortorder", "desc");
		orderMap.put("price_precision", currencyPair.getPrice_precision());
		orderMap.put("volume_precision", currencyPair.getVolume_precision());
		if (status == 0) {
			orderMap.put("o_status_in", "'watting', 'partial-done'");
		} else if (status == 1) {
			orderMap.put("o_status_in", "'done', 'partial-canceled', 'canceled'");
		}
		List<OrderRespDto> jsonList = exService.getOrders(orderMap);

		return new OrdersResp(Resp.SUCCESS, symbol, jsonList, Integer.parseInt(orderMap.get("total") + ""));
	}

	/**
	 * 获得订单的交易明细
	 * 
	 * @param request
	 * @param symbol
	 * @param o_id
	 * @return
	 */
	@ApiOperation(value = "获得单个订单的交易明细（仅返回当前apikey对应数据）", notes = "<h5>symbol: </h5> 交易对名称，如BTCCNY、LTCCNY、ETHCNY <h5>id: </h5> 订单ID<h5>签名所需参数: </h5>api_key、timestamp、sign_type和sign详见签名方法", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "order/trades/{symbol}/{id}/{api_key}/{timestamp}/{sign_type}/{sign}", method = RequestMethod.GET)
	@ResponseBody
	public Resp getTrade(HttpServletRequest request, @PathVariable("symbol") String symbol,
			@PathVariable("id") String id, @PathVariable("api_key") String apikey,
			@PathVariable("timestamp") Long timestamp, @PathVariable("sign_type") String signType, @PathVariable("sign") String sign) {
		
		apiAccessLimitService.check("getTrade", apikey);
		
		if (null == symbol) {
			return new TradesResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol.toUpperCase());
		if (null == currencyPair) {
			return new TradesResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}

		ReqBaseSecret reqBaseSecret = new ReqBaseSecret();
		reqBaseSecret.setApi_key(apikey);
		reqBaseSecret.setSign_type(signType);
		reqBaseSecret.setTimestamp(timestamp);
		reqBaseSecret.setSign(sign);
		String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
		if (!"".equals(validateRet)) {
			if("ILLEGAL_TIMESTAMP".equals(validateRet)) {
				return new RespObj(Resp.FAIL,validateRet,HelpUtils.getNowTimeStampInt());
			}
			return new TradesResp(Resp.FAIL, validateRet, null, 0);
		}
		
		String ip = HelpUtils.getIpAddr(request);		
		
		// 校验合法性，非法情况直接抛出异常
		ApiToken apiToken = memberService.getApiToken(apikey);
		validateApiToken(HelpUtils.objToMap(reqBaseSecret), apiToken, ip, "Order");

		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("token_id", apiToken.getId());
		orderMap.put("table_name", symbol.toLowerCase());
		orderMap.put("price_precision", currencyPair.getPrice_precision());
		orderMap.put("volume_precision", currencyPair.getVolume_precision());
		orderMap.put("symbol", symbol);
		orderMap.put("o_id", id);
		orderMap.put("page", 1);
		orderMap.put("pagesize", 10000);
		List<TradeRespDto> jsonList = exService.getTrades(orderMap);

		return new TradesResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList, Integer.parseInt(orderMap.get("total") + ""));
	}

	/**
	 * token方式
	 * 
	 * @param request
	 * @param symbol
	 * @param o_id
	 * @return
	 */
	@ApiOperation(value = "获得已成交记录（仅返回当前apikey对应数据）", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY，默认按时间倒叙排序<br>page：页数，从1开始，pageSize最大值20<br><br>api_key、timestamp、sign_type和sign详见签名方法", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "trades/{symbol}/{page}/{pageSize}/{api_key}/{timestamp}/{sign_type}/{sign}", method = RequestMethod.GET)
	@ResponseBody
	public Resp getTrades(HttpServletRequest request, @PathVariable("symbol") String symbol,
			@PathVariable("page") Integer page, @PathVariable("pageSize") Integer pageSize, @PathVariable("api_key") String apikey,
			@PathVariable("timestamp") Long timestamp, @PathVariable("sign_type") String signType, @PathVariable("sign") String sign) {
		apiAccessLimitService.check("getTrades", apikey);
		if (null == symbol) {
			return new TradesResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol.toUpperCase());
		if (null == currencyPair) {
			return new TradesResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}

		ReqBaseSecret reqBaseSecret = new ReqBaseSecret();
		reqBaseSecret.setApi_key(apikey);
		reqBaseSecret.setSign_type(signType);
		reqBaseSecret.setTimestamp(timestamp);
		reqBaseSecret.setSign(sign);
		String validateRet = HelpUtils.preValidateBaseSecret(reqBaseSecret);
		if (!"".equals(validateRet)) {
			if("ILLEGAL_TIMESTAMP".equals(validateRet)) {
				return new RespObj(Resp.FAIL,validateRet,HelpUtils.getNowTimeStampInt());
			}
			return new TradesResp(Resp.FAIL, validateRet, null, 0);
		}
		
		String ip = HelpUtils.getIpAddr(request);		
		
		// 校验合法性，非法情况直接抛出异常
		ApiToken apiToken = memberService.getApiToken(apikey);
		validateApiToken(HelpUtils.objToMap(reqBaseSecret), apiToken, ip, "Order");

		Map<String, Object> tradeMap = new HashMap<String, Object>();
		tradeMap.put("token_id", apiToken.getId());
		tradeMap.put("table_name", symbol.toLowerCase());
		tradeMap.put("price_precision", currencyPair.getPrice_precision());
		tradeMap.put("volume_precision", currencyPair.getVolume_precision());
		tradeMap.put("symbol", symbol);
		tradeMap.put("page", page);
		tradeMap.put("pagesize", pageSize > 20 ? 20 : pageSize);
		tradeMap.put("sortname", "id");
		tradeMap.put("sortorder", "desc");
		List<TradeRespDto> jsonList = exService.getTrades(tradeMap);

		return new TradesResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList, Integer.parseInt(tradeMap.get("total") + ""));
	}
	
	/*ApiToken apiToken = new ApiToken();
	apiToken.setApi_key("42d31e13-5ad9-4b03-ac4e-09f9912ad752");
	apiToken.setApi_privilege("Withdraw,Accounts,Order");
	apiToken.setApi_secret("0320657DBE6B9CCF85B64663A3641AD0");
	apiToken.setMember_id(1037);
	apiToken.setT_status(1);
	apiToken.setId(6);*/
}
