package com.pmzhongguo.ex.api.controller;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.gson.Gson;
import com.pmzhongguo.ex.business.dto.*;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.resp.OrdersResp;
import com.pmzhongguo.ex.business.resp.TradesResp;
import com.pmzhongguo.ex.business.service.ApiAccessLimitService;
import com.pmzhongguo.ex.business.service.CurrencyLockAccountService;
import com.pmzhongguo.ex.business.service.ExService;
import com.pmzhongguo.ex.business.service.TradeService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.utils.JsonUtil;
import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.utils.DateUtils;
import com.qiniu.util.BeanUtil;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "订单接口", description = "下单、取消订单，需登录认证", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("o")
public class OrderController extends TopController {
	private Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Resource
	private ExService exService;
	@Resource
	private TradeService tradeService;
	@Autowired
	private ApiAccessLimitService apiAccessLimitService;
	@Autowired
	private CurrencyLockAccountService currencyLockAccountService;
	
	//对同一个用户，一个时刻只能一个线程访问，因此利用Guava进行变量锁
	private Interner<String> memberIdPool = Interners.newWeakInterner();
	
	//对同一个用户，一个时刻只能一个线程访问，因此利用Guava进行变量锁
	private Interner<String> orderMemberIdPool = Interners.newWeakInterner();
	
	
	/**
	 * API创建订单
	 * 
	 * @param request
	 * @param response
	 * @param apiOrderCreate
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "创建交易订单", notes = "创建交易订单，成功时，返回订单id，其他情况返回null或错误信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "api/order", method = RequestMethod.POST)
	@ResponseBody
	public Resp createOrder(HttpServletRequest request, HttpServletResponse response,
							@RequestBody ApiOrderCreate apiOrderCreate) throws Exception {
		try {
//			logger.warn("api下单,服务器时间戳：{},apiKey时间戳：{}", System.currentTimeMillis(), apiOrderCreate.getTimestamp());

			apiAccessLimitService.check("createOrder", apiOrderCreate.getApi_key());
			String validateRet = HelpUtils.preValidateBaseSecret(apiOrderCreate);
			if (!"".equals(validateRet)) {
				if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
					return new RespObj(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
				}
				return new ObjResp(Resp.FAIL, validateRet, null);
			}

			// 统一走网页版API
			OrderCreateDto reqOrderCreate = new OrderCreateDto();
			reqOrderCreate.setO_price_type(apiOrderCreate.getO_price_type());
			reqOrderCreate.setO_type(apiOrderCreate.getO_type());
			// todo 这里的o_no 可能需要由服务端生成，待测试
			reqOrderCreate.setPrice(apiOrderCreate.getPrice());
			reqOrderCreate.setSource("Api");
			reqOrderCreate.setSymbol(apiOrderCreate.getSymbol());
			reqOrderCreate.setVolume(apiOrderCreate.getVolume());

			String ip = HelpUtils.getIpAddr(request);

			// 校验合法性，非法情况直接抛出异常
			ApiToken apiToken = memberService.getApiToken(apiOrderCreate.getApi_key());
			if (BeanUtil.isEmpty(apiToken)){
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_API_TOKEN_NOT_EXIST.getErrorENMsg(), null);
			}
			//当订单号为空时，通过apiKey拿到用户id，通过规则生成订单号。
			if (BeanUtil.isEmpty(apiOrderCreate.getO_no())) {
				reqOrderCreate.setO_no(apiToken.getMember_id() + "-" + System.currentTimeMillis() + HelpUtils.randomString(10));
//			logger.warn("用户id---Symbol---O_no：{},用户api_key:{}",apiToken.getMember_id()+"---"+apiOrderCreate.getSymbol()+"---"+reqOrderCreate.getO_no(),apiOrderCreate.getApi_key());
			} else {
				reqOrderCreate.setO_no(apiOrderCreate.getO_no());
			}

			validateApiToken(HelpUtils.objToMap(apiOrderCreate), apiToken, ip, "Order");

			// 校验订单合法性
			AuthDto authDto = validateCreateOrder(reqOrderCreate,apiToken.getMember_id());
			if (!authDto.isSuccess()) {
				return new ObjResp(Resp.FAIL, authDto.getMsg(), null);
			}
			authDto.setMemberId(apiToken.getMember_id());
			authDto.setTokenId(apiToken.getId());
			// 判断可用余额是否大于交易金额
			CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(apiOrderCreate.getSymbol());
			if (null == currencyPair) {
				return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
			}
			String currency = "buy".equals(apiOrderCreate.getO_type()) ? currencyPair.getQuote_currency() : currencyPair.getBase_currency();
			Account account = memberService.getAccount(HelpUtils.newHashMap("member_id", apiToken.getMember_id(), "currency", currency));
			if (null == account || account.getId() <= 0) {
				return new Resp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
			}
			if ("buy".equals(apiOrderCreate.getO_type())) {
				BigDecimal totalPrice = apiOrderCreate.getVolume().multiply(apiOrderCreate.getPrice())
						.setScale(currencyPair.getPrice_precision(), BigDecimal.ROUND_HALF_DOWN);
				if (account.getAvailable_balance().compareTo(totalPrice) < 0) {
					return new Resp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
				}
			} else {
				if (account.getAvailable_balance().compareTo(apiOrderCreate.getVolume()) < 0) {
					return new Resp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
				}
			}

			//校验O_no和memberId的唯一性
			if (!existDataUni(authDto.getMemberId(), reqOrderCreate.getO_no(), authDto.getCurrencyPair().getKey_name().toLowerCase())) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ORDER_DUPLICATE_ERROR.getErrorENMsg(), null);
			}
			ObjResp orderCommon = this.createOrderCommon(authDto, reqOrderCreate, request, apiToken.getTrade_commission());

			if (!StringUtils.isBlank(apiOrderCreate.getO_no()) || orderCommon.getState() != 1) {
				return orderCommon;
			}

			Object idResp = orderCommon.getData();
			orderCommon.setData(HelpUtils.newHashMap("id", idResp, "o_no", reqOrderCreate.getO_no()));
			return orderCommon;
		} catch (Exception e) {
			logger.warn("创建交易订单,异常信息：{},请求参数：{}", e.getMessage(), JsonUtil.bean2JsonString(apiOrderCreate));
			e.printStackTrace();
			return new ObjResp(Resp.FAIL, e.getMessage(), null);
		}
	}

	

	/**
	 * 创建订单
	 * @param request
	 * @param response
	 * @param orderCreateDto
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "创建订单", notes = "创建一个订单，o_no不用输入<br>source一般PC版填web，手机版填mobile，APP填APP", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "order/createI", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp createOrderI(HttpServletRequest request,
			HttpServletResponse response, @RequestBody OrderCreateDto orderCreateDto) throws Exception {
		
		// 为了防止跨域攻击，前台提交的订单，需进行sign校验，这里干脆搞成验证资金密码，也可达到同样效果
		/*
		String sign = ((String) session.getAttribute("sign"));
		if (HelpUtils.nullOrBlank(orderCreateDto.getSign()) || !orderCreateDto.getSign().equals(sign)) {
			return new ObjResp(Resp.FAIL, "LANG_ILLEGAL_SIGN", null);
		}
		*/
		
		
		Member member = JedisUtilMember.getInstance().getMember(request, orderCreateDto.getToken());
		if (null == member) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}
		
		// 资金密码需要从后台取
		// Member oldMember = memberService.getMemberById(member.getId());
		
		// 校验资金密码
		//if (HelpUtils.nullOrBlank(orderCreateDto.getSecurity_pwd()) || !MacMD5.CalcMD5Member(orderCreateDto.getSecurity_pwd()).equals(oldMember.getM_security_pwd())) {
		//	return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg(), null);
		//}
		
		// 设置订单ID
		orderCreateDto.setO_no(member.getId() + "-" + System.currentTimeMillis() + HelpUtils.randomString(10));

		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(orderCreateDto.getSymbol());
		if (BeanUtil.isEmpty(currencyPair)){
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_TRADE_PAIR.getErrorENMsg(), null);
		}
		String open_time = currencyPair.getOpen_time();
		long symbolTime = DateUtil.stringToDate(open_time, DateStyleEnum.YYYY_MM_DD_HH_MM).getTime();
		long currentTimeMilli = System.currentTimeMillis();
		if (symbolTime - currentTimeMilli > 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_STOP_EX.getErrorENMsg(), null);
		}
		String symbolClosePriceKey = currencyPair.getKey_name().toUpperCase() + "_close_price";
		Object symbolClosePrice = JedisUtil.getInstance().get(symbolClosePriceKey, true);
		if (symbolClosePrice != null && currencyPair.getIs_ups_downs_limit().equals(1) ) {
			BigDecimal close_price = new BigDecimal(symbolClosePrice + "");
			BigDecimal tempPrice = close_price.multiply(currencyPair.getUps_downs_limit()).divide(new BigDecimal(100)).setScale(currencyPair.getPrice_precision(), BigDecimal.ROUND_DOWN);
			BigDecimal highPrice = close_price.add(tempPrice);
			BigDecimal lowPrice = close_price.subtract(tempPrice);
			if (orderCreateDto.getO_type().equals("buy")) {
				if (orderCreateDto.getPrice().subtract(highPrice).compareTo(BigDecimal.ZERO) > 0) {
					return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_PRICE_UPS_DOWN_HIGH.getErrorENMsg(), null);
				}
			} else if (orderCreateDto.getO_type().equals("sell")) {
				if (orderCreateDto.getPrice().subtract(lowPrice).compareTo(BigDecimal.ZERO) < 0) {
					return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_PRICE_UPS_DOWN_LOW.getErrorENMsg(), null);
				}
			}
		}
		// 校验订单合法性
		AuthDto authDto = validateCreateOrder(orderCreateDto,member.getId());
		if(!authDto.isSuccess()) {
			return new ObjResp(Resp.FAIL, authDto.getMsg(), null);
		}
		authDto.setMemberId(member.getId());
		
		
		return this.createOrderCommon(authDto, orderCreateDto, request, member.getTrade_commission());
	}
	
	
	private ObjResp  createOrderCommon(AuthDto authDto, OrderCreateDto orderCreateDto, HttpServletRequest request, Double tradeCommission) throws Exception {
		synchronized (orderMemberIdPool.intern(authDto.getMemberId() + "")) {
			//Long id = exService.addOrder(orderCreateDto, authDto.getCurrencyPair(), HelpUtils.getIpAddr(request), authDto.getTokenId(), authDto.getMemberId(), tradeCommission);
			Integer memberId = authDto.getMemberId();
			
			// 冻结
			// 判断余额、冻结金额
			BigDecimal frozen = BigDecimal.ZERO; // 需冻结金额
			String frozenCurrency = ""; // 需冻结的币（含人民币）种

			// 需要冻结的金额或数量，对限价卖单，同数量一致，对限价买单，等于价格*数量，对市价单，等于数量
			if ("limit".equals(orderCreateDto.getO_price_type())) { // 限价单
				if ("buy".equals(orderCreateDto.getO_type())) { // 限价买单
					frozen = orderCreateDto.getVolume().multiply(orderCreateDto.getPrice()); // 限价买单，所以是价格乘以数量
					frozenCurrency = authDto.getCurrencyPair().getQuote_currency(); // 计价货币
				} else { // 限价卖单
					frozen = orderCreateDto.getVolume(); // 限价卖单，所以是数量
					frozenCurrency = authDto.getCurrencyPair().getBase_currency(); // 基础货币
				}
			} else { // 市价单
				frozen = orderCreateDto.getVolume(); // 市价单，全部等于数量，因为对买单来说，是计价货币的数量，对卖单来说，是基础货币的数量
				if ("buy".equals(orderCreateDto.getO_type())) {
					frozenCurrency = authDto.getCurrencyPair().getQuote_currency(); // 计价货币
				} else {
					frozenCurrency = authDto.getCurrencyPair().getBase_currency(); // 基础货币
				}
			}

			Order order = new Order();
			order.setPair_id(authDto.getCurrencyPair().getId());
			order.setBase_currency(authDto.getCurrencyPair().getBase_currency());
			order.setQuote_currency(authDto.getCurrencyPair().getQuote_currency());
			order.setO_no(orderCreateDto.getO_no());
			order.setO_type(orderCreateDto.getO_type());
			order.setO_price_type(orderCreateDto.getO_price_type());
			order.setPrice(orderCreateDto.getPrice());
			order.setVolume(orderCreateDto.getVolume());
			order.setDone_volume(BigDecimal.ZERO);
			order.setToken_id(authDto.getTokenId());
			order.setMember_id(memberId);
			order.setSource(orderCreateDto.getSource());
			order.setOper_ip(HelpUtils.getIpAddr(request));
			order.setCreate_time(HelpUtils.formatDate8(new Date()));
			order.setCreate_time_unix(HelpUtils.getNowTimeStampInt()); // 下单时间戳，挂单队列的二级队列排序需要按照这个时间来
			order.setTable_name(authDto.getCurrencyPair().getKey_name().toLowerCase());
			order.setFrozen(frozen);
			order.setUnfrozen(BigDecimal.ZERO);
			order.setTradeCommission(tradeCommission);

			// TODO 使用Disruptor处理交易，不阻塞用户下单，在现在用户量不大的情况下，先不使用
			// ringBuffer.publishEvent(publisher, order);

			order.setDone_volume(BigDecimal.ZERO);
			order.setCur_done_volume(BigDecimal.ZERO);
			order.setUnfrozen(BigDecimal.ZERO);
			// 返回订单ID
			Long id = allMemberFrozenAndProcSyn(frozen, frozenCurrency, memberId, "order", order, null);
//			//  释放锁仓币种数量
//            try {
//				currencyLockAccountService.releaseLockCurrency(id,order);
//			} catch (Exception e) {
//            	logger.error("=======> 释放锁仓币种数量：{}",e.getCause() + "\t 订单：" + order.toString());
//			}
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, id);
		}
	}




	/**
	 * 取消订单公共方法
	 * @param order
	 * @param authDto
	 * @param orderCancelDto
	 * @return
	 */
	private Resp cancelOrder(Order order, AuthDto authDto, OrderCancelDto orderCancelDto) {
		synchronized (memberIdPool.intern(authDto.getMemberId() + "")) {
			order = exService.getOrder(order);
			
			if (null == order) {
				return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ORDER_DOES_NOT_EXIST.getErrorENMsg());
			}
			
			if (!order.getO_no().equals(orderCancelDto.getO_no())) {
				return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ORDER_DOES_NOT_EXIST.getErrorENMsg());
			}
			
			if ("market".equals(order.getO_price_type())) {
				return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_MARKET_ORDER_CANNOT_BE_CANCELLED.getErrorENMsg());
			}
			
			if (!"watting".equals(order.getO_status()) && !"partial-done".equals(order.getO_status())) {
				return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ORDER_IS_DONE_OR_CANCELED.getErrorENMsg());
			}
			
			order.setTable_name(authDto.getCurrencyPair().getKey_name().toLowerCase());
			
			// true表示取消订单
			// tradeService.trade(order, true);
			allMemberFrozenAndProcSyn(BigDecimal.ZERO, null, order.getMember_id(), "cancel", order, null);
			
			return new Resp(Resp.SUCCESS, Resp.SUBMIT_SUCCESS_MSG);
		}
	}
	
	
	@ApiOperation(value = "取消交易订单", notes = "取消一个交易订单", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "api/order", method = RequestMethod.DELETE)
	@ResponseBody
	public Resp cancelOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ApiOrderCancel apiOrderCancel) throws Exception {
		String validateRet = HelpUtils.preValidateBaseSecret(apiOrderCancel);
		if (!"".equals(validateRet)) {
			if("ILLEGAL_TIMESTAMP".equals(validateRet)) {
				return new RespObj(Resp.FAIL,validateRet,HelpUtils.getNowTimeStampInt());
			}
			return new RespObj(Resp.FAIL, validateRet, null);
		}
		
		// 统一走网页版API
		OrderCancelDto reqOrderCancel = new OrderCancelDto();
		reqOrderCancel.setId(apiOrderCancel.getId());
		reqOrderCancel.setO_no(apiOrderCancel.getO_no());
		reqOrderCancel.setSymbol(apiOrderCancel.getSymbol());
		
		
		String ip = HelpUtils.getIpAddr(request);		
		
		// 校验合法性，非法情况直接抛出异常
		ApiToken apiToken = memberService.getApiToken(apiOrderCancel.getApi_key());
		validateApiToken(HelpUtils.objToMap(apiOrderCancel), apiToken, ip, "Order");

		Member member = new Member();
		member.setId(apiToken.getMember_id());

		// 校验合法性，非法情况直接抛出异常
		AuthDto authDto = validateCancelOrder(reqOrderCancel);
		if(!authDto.isSuccess()) {
			return new Resp(Resp.FAIL, authDto.getMsg());
		}
		authDto.setMemberId(member.getId());
		
		Order order = new Order();
		order.setId(reqOrderCancel.getId());
		order.setMember_id(authDto.getMemberId());
		order.setTable_name(authDto.getCurrencyPair().getKey_name().toLowerCase());
		
		return cancelOrder(order, authDto, reqOrderCancel);

	}
	
	@ApiOperation(value = "取消订单", notes = "取消一个订单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "order/cancelI", method = RequestMethod.POST)
	@ResponseBody
	public Resp cancelOrderI(HttpServletRequest request,
			HttpServletResponse response, @RequestBody OrderCancelDto orderCancelDto) throws Exception {
		
		// TODO 为了防止跨域攻击，前台提交的订单，需进行sign校验，此处要求订单ID和订单号，基本可以防止
		/*
		String sign = ((String) session.getAttribute("sign"));
		if (HelpUtils.nullOrBlank(orderCreateDto.getSign()) || !orderCreateDto.getSign().equals(sign)) {
			return new ObjResp(Resp.FAIL, "LANG_ILLEGAL_SIGN", null);
		}
		*/
		
		Member member = JedisUtilMember.getInstance().getMember(request, orderCancelDto.getToken());
		if (null == member) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
		}
		
		AuthDto authDto = validateCancelOrder(orderCancelDto);
		if(!authDto.isSuccess()) {
			return new Resp(Resp.FAIL, authDto.getMsg());
		}
		authDto.setMemberId(member.getId());
		
		Order order = new Order();
		order.setId(orderCancelDto.getId());
		order.setMember_id(authDto.getMemberId());
		order.setTable_name(authDto.getCurrencyPair().getKey_name().toLowerCase());
		
		return cancelOrder(order, authDto, orderCancelDto);
		
	}
	
	@ApiOperation(value = "获得订单详细信息『单个』", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY <br>o_no:订单号", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "orderI/{symbol}/{o_no}", method = RequestMethod.GET)
	@ResponseBody
	public OrdersResp getOrderI(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String symbol, @PathVariable String o_no) {

		if (null == symbol) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol.toUpperCase());
		if (null == currencyPair) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null, 0);
		}
		
		Map<String, Object> orderMap = new HashMap<String, Object>();
		orderMap.put("symbol", symbol);
		orderMap.put("o_no", o_no);
		orderMap.put("member_id", member.getId());
		orderMap.put("table_name", symbol.toLowerCase());
		orderMap.put("price_precision", currencyPair.getPrice_precision());
		orderMap.put("volume_precision", currencyPair.getVolume_precision());
		List<OrderRespDto> jsonList = exService.getOrders(orderMap);

		return new OrdersResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList, Integer.parseInt(orderMap.get("total") + ""));
	}
	
	
	@ApiOperation(value = "获得订单交易明细（一个订单的一个或多个成交）", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY <br>o_id:订单ID", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "order_tradesI/{symbol}/{o_id}/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public TradesResp getOrderTradesI(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String symbol, @PathVariable String o_id) {

		Map<String, Object> orderMap = new HashMap<String, Object>();
		
		if (null == symbol) {
			return new TradesResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol.toUpperCase());
		if (null == currencyPair) {
			return new TradesResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new TradesResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null, 0);
		}
		
		orderMap.put("member_id", member.getId());
		orderMap.put("table_name", symbol.toLowerCase());
		orderMap.put("price_precision", currencyPair.getPrice_precision());
		orderMap.put("volume_precision", currencyPair.getVolume_precision());
		orderMap.put("symbol", symbol);
		orderMap.put("o_id", o_id);
		String page = $("page");
		String pagesize = $("pagesize");
		orderMap.put("page", page == null ? 1 : page);
		orderMap.put("pagesize", pagesize == null ? 50 : pagesize);
		List<TradeRespDto> jsonList = exService.getTrades(orderMap);

		return new TradesResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList, Integer.parseInt(orderMap.get("total") + ""));
	}
	
	
	@ApiOperation(value = "获得订单详细信息『批量』", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY <br>page:页数，从1开始，每页20条记录（挂单会显示所有记录），默认按时间倒叙排列 <br>status 0表示未完成，1表示已完成", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "ordersI/{symbol}/{page}/{status}/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public OrdersResp getOrdersI(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String symbol, @PathVariable Integer page, @PathVariable Integer status, @PathVariable Long ts) {

		Map<String, Object> orderMap = new HashMap<String, Object>();
		
		if (null == symbol) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol.toUpperCase());
		if (null == currencyPair) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new OrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null, 0);
		}
		
		orderMap.put("member_id", member.getId());
		orderMap.put("table_name", symbol.toLowerCase());
		orderMap.put("page", page);
		orderMap.put("pagesize", 20);
		orderMap.put("sortname", "o.id");
		orderMap.put("sortorder", "desc");
		orderMap.put("price_precision", currencyPair.getPrice_precision());
		orderMap.put("volume_precision", currencyPair.getVolume_precision());
		if (status == 0) {
			orderMap.put("o_status_in", "'watting', 'partial-done'");
//			orderMap.put("pagesize", 100000);
		} else {
			orderMap.put("o_status_in", "'done', 'partial-canceled', 'canceled'");
		}
		List<OrderRespDto> jsonList = exService.getOrders(orderMap);

		return new OrdersResp(Resp.SUCCESS, symbol, jsonList, Integer.parseInt(orderMap.get("total") + ""));
	}
	
	
	@ApiOperation(value = "获得已成交记录", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY,page:页数，每页20条", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "tradesI/{symbol}/{page}/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public TradesResp getTradesI(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String symbol, @PathVariable Integer page) {

		Map<String, Object> orderMap = new HashMap<String, Object>();
		
		if (null == symbol) {
			return new TradesResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol.toUpperCase());
		if (null == currencyPair) {
			return new TradesResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg(), null, 0);
		}
		
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new TradesResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null, 0);
		}
		
		orderMap.put("member_id", member.getId());
		orderMap.put("table_name", symbol.toLowerCase());
		orderMap.put("price_precision", currencyPair.getPrice_precision());
		orderMap.put("volume_precision", currencyPair.getVolume_precision());
		orderMap.put("symbol", symbol);
		orderMap.put("page", page);
		orderMap.put("pagesize", 20);
		orderMap.put("sortname", "t.id");
		orderMap.put("sortorder", "desc");
		List<TradeRespDto> jsonList = exService.getTrades(orderMap);

		return new TradesResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList, Integer.parseInt(orderMap.get("total") + ""));
	}
	

	
	

	public AuthDto validateCancelOrder(OrderCancelDto orderCancelDto) {
		// 返回对象
		AuthDto authDto = new AuthDto();
		authDto.setSuccess(false);
		
		if (HelpUtils.getMgrConfig().getIs_stop_ex() == 1) {
			authDto.setMsg(ErrorInfoEnum.LANG_STOP_EX.getErrorENMsg());
			return authDto;
		}

		if (null == orderCancelDto.getId() || 0 == orderCancelDto.getId()) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_ORDER_ID.getErrorENMsg());
			return authDto;
		}
		
		CurrencyPair cp = HelpUtils.getCurrencyPairMap().get(
				orderCancelDto.getSymbol());
		if (null == cp) {
			authDto.setMsg(ErrorInfoEnum.LANG_ILLEGAL_SYMBOL.getErrorENMsg());
			return authDto;
		}

		authDto.setSuccess(true);
		authDto.setMsg("success");
		authDto.setCurrencyPair(cp);

		return authDto;
	}

	/**
	 * 校验订单中memberId和O_no的唯一性
	 *
	 * @param memberId
	 * @param O_no
	 * @return
	 */
	public boolean existDataUni(Integer memberId, String O_no, String tableName)
	{
		String lockKey = "order_exist_prefix" + O_no + memberId;
		boolean isLock = JedisUtil.getInstance().getLock(lockKey, IPAddressPortUtil.IP_ADDRESS, 3 * 1000);
		if (isLock) {
			// 查询数据库是否已经存在O_no
			String sql = "select 1 from t_order_" + tableName + " where o_no = ? and member_id = ? limit 1";
			Object num = daoUtil.queryForMap(sql, O_no, memberId);
			if (num == null)
			{
				return true;
			}
		}
		return false;
	}

	protected AuthDto validateFlashSale(OrderCreateDto orderCreateDto,Integer memberId) {
		AuthDto authDto = new AuthDto();
		authDto.setSuccess(false);
		CurrencyPair cp = HelpUtils.getCurrencyPairMap().get(orderCreateDto.getSymbol());
		//判定是否满足抢购条件
		if (cp.getIs_flash_sale_open() == 1){
			//判定是否设置了抢购时间
			if (cp.getFlash_sale_close_time() == null || cp.getIs_flash_sale_open() == null){
				authDto.setMsg(ErrorInfoEnum.FLASH_SALE_NOT_START.getErrorENMsg());
				return authDto;
			}
			//判定是否在抢购时间内
			long opentime = DateUtils.differentDaysBySecond(cp.getFlash_sale_open_time(), null);
			long closetime = DateUtils.differentDaysBySecond(cp.getFlash_sale_close_time(), null);
			if (!(opentime > StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME && closetime < StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME)){
				authDto.setMsg(ErrorInfoEnum.FLASH_SALE_NOT_START.getErrorENMsg());
				return authDto;
			}
			//判定是否超过最大购买量
			if (cp.getMax_buy_volume().compareTo(BigDecimal.ZERO) != 0){
				String table = "t_order_"+orderCreateDto.getSymbol().toLowerCase();
				String sql = "SELECT volume from " + table + " WHERE 1 = 1 and o_type=buy member_id = ? and create_time > ? and create_time < ?;";
				List<Map> list = daoUtil.queryForList(sql,memberId,cp.getFlash_sale_open_time(),cp.getFlash_sale_close_time());
				BigDecimal totalVolume = orderCreateDto.getVolume();
				for (Map m:list) {
					totalVolume = totalVolume.add((BigDecimal) m.get("volume")) ;
				}
				if (cp.getMax_buy_volume().compareTo(totalVolume) == -1) {
					authDto.setMsg(ErrorInfoEnum.LANG_BIGGER_THAN_MAX_BUYVOLUME_TIP.getErrorENMsg());
					return authDto;
				}
			}
		}
		authDto.setSuccess(true);
		authDto.setMsg("success");
		authDto.setTokenId(0);
		authDto.setCurrencyPair(cp);

		return authDto;
	}

	/**
	 * 获取某个币对应ZC的汇率
	 * @param currency
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/zcRate/{currency}", method = RequestMethod.GET)
	public ObjResp getZC(@PathVariable String currency) {
		return new ObjResp(Resp.SUCCESS,Resp.SUCCESS_MSG,HelpUtils.getSomeCurrencyWithZcRate(currency));
	}
}
