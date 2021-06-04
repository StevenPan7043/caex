package com.pmzhongguo.otc.controller;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.base.Joiner;
import com.pmzhongguo.ex.business.entity.AuthIdentity;
import com.pmzhongguo.ex.core.sms.ISmsService;
import com.pmzhongguo.ex.core.sms.InterSmsServiceImpl;
import com.pmzhongguo.ex.core.web.AreaCodeEnum;
import com.pmzhongguo.ex.core.web.InternationalSmsCNEnum;
import com.pmzhongguo.otc.entity.dto.AccountDTO;
import com.pmzhongguo.otc.entity.dto.AccountInfoDTO;
import com.pmzhongguo.otc.service.*;
import com.qiniu.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.entity.req.OTCOrderReq;
import com.pmzhongguo.otc.otcenum.MobiInfoTemplateEnum;
import com.pmzhongguo.otc.otcenum.OrderStatusEnum;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.PriceTypeEnum;
import com.pmzhongguo.otc.otcenum.TradeStatusEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;
import com.pmzhongguo.otc.sms.JuheSend;
import com.pmzhongguo.otc.sms.SmsSendPool;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "NEW OTC订单", description = "订单相关", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("otc/order")
public class  OTCOrderController extends TopController {

	@Autowired
	private OTCOrderManager oTCOrderManager;

	@Autowired
	private OTCTradeManager oTCTradeManager;

	@Autowired
	private MerchantManager merchantManager;

	@Autowired
	private OTCAccountInfoManager oTCAccountInfoManager;

	@Autowired
	private OTCAccountService otcAccountService;

	@ApiOperation(value = "创建订单", notes = "输入字段：<br/> 挂单方：<br/>基础货币：baseCurrency <br/>计价货币:quoteCurrency <br/>订单类型:type(BUY|SELL) <br/>单价:price <br/>数量:volume <br/>最小交易额:minQuote(计价货币)   <br/>最大交易额:maxQuote(计价货币)  <br/>付款限时:paymentTime<br/> 订单有效时间:effectiveTime<br/>备注:remark<br/>收付款账号:acountId(多个id用逗号分隔)<br/> 资金密码:securityPwd <br/> 价格变动类型:priceChangeType(UNCHANGE|FLOAT) <br/>", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "publishOTCOrder", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp publishOTCOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OTCOrderReq req) {

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		// 是否设置手机号码
		if (m.getM_name().contains("@")) {
			if (HelpUtils.nullOrBlank(m.getPhone())) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.PSL_SET_PHONE.getErrorENMsg(), null);
			}
		}
		if (m.getM_status() == 2) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.ACCOUNT_HAS_LOCKED.getErrorENMsg(), null);
		}
		if (!"1".equals(m.getM_security_pwd()) || !validateSecurityPwd(m.getId(), req.getSecurityPwd())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg(), null);
		}

		if (merchantManager.isMerchant(m.getId()).getState().intValue() != Resp.SUCCESS.intValue()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.INVALID_MERCHANT.getErrorENMsg(), null);
		}
		ObjResp resp = validate(req);
		if (resp.getState().intValue() == resp.FAIL.intValue()) {
			return resp;
		}
		// merchantId的处理
		Integer memberId = m.getId();
		String IpAddr = HelpUtils.getIpAddr(request);
		
		//判断已挂订单数
		ObjResp objResp = validateTrade(req, memberId, 3);
		if(objResp.getState().intValue() == Resp.FAIL.intValue()) {
			return objResp;
		}

		OTCOrderDTO oTCOrderDTO = req.toOTCOrderDTO();
		oTCOrderDTO.setPriceType(PriceTypeEnum.LIMIT);
		oTCOrderDTO.setMemberId(memberId);
		oTCOrderDTO.setOperIp(IpAddr);

		oTCOrderDTO.setStatus(OrderStatusEnum.WATTING);
		oTCOrderDTO.setIsAds(WhetherEnum.YES);
		// 挂单方
		return oTCTradeManager.createOrder(oTCOrderDTO, null);
	}

	private ObjResp validateTrade(OTCOrderReq req, Integer memberId, int limit) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("memberId", memberId);
		queryMap.put("status", OrderStatusEnum.WATTING);
		queryMap.put("type", req.getType());
		queryMap.put("baseCurrency", req.getBaseCurrency());
		queryMap.put("isAds", WhetherEnum.YES);
		List<OTCOrderDTO> list = oTCOrderManager.findByConditionPage(queryMap);
		
		int wating = (list == null ? 0 : list.size());
		
		queryMap = new HashMap<String, Object>();
		queryMap.put("memberId", memberId);
		queryMap.put("status", OrderStatusEnum.TRADING);
		queryMap.put("type", req.getType());
		queryMap.put("baseCurrency", req.getBaseCurrency());
		queryMap.put("isAds", WhetherEnum.YES);
		list = oTCOrderManager.findByConditionPage(queryMap);

		wating = wating + (list == null ? 0 : list.size());
		
		if(wating >= limit) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.ORDER_TYPE_ONLY_TWO.getErrorENMsg(), null);
		}
		return new ObjResp();
	}
	
	private ObjResp validate(OTCOrderReq req) {
		// 验证基础货币
		if (HelpUtils.nullOrBlank(req.getBaseCurrency())
				|| !HelpUtils.getCurrencyIsOtcMap().containsKey(req.getBaseCurrency())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.BASE_CURRENCY_NOT_EXISTS.getErrorENMsg(), null);
		}

		if (HelpUtils.nullOrBlank(req.getQuoteCurrency())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.QUOTE_CURRENCY_NOT_EXISTS.getErrorENMsg(), null);
		}

		if (req.getPrice() == null || req.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_PRICE.getErrorENMsg(), null);
		}

		if (req.getVolume() == null || req.getVolume().compareTo(BigDecimal.ZERO) <= 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
		}
		if (req.getMinQuote() == null) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_DATA.getErrorENMsg(), "MinQuote");
		}
		if (req.getMaxQuote() == null) {
			return new ObjResp(Resp.FAIL,ErrorInfoEnum.LANG_ERROR_DATA.getErrorENMsg(), "MaxQuote");
		}
		if (req.getMaxQuote().compareTo(req.getMinQuote()) < 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_DATA.getErrorENMsg(), "MinQuote>MaxQuote");
		}
		BigDecimal realQuote = req.getVolume().multiply(req.getPrice());
		
		if (realQuote.compareTo(req.getMinQuote()) < 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_DATA.getErrorENMsg(), "MinQuote>Price * Volume");
		}
		
		if (req.getPaymentTime() == null || (req.getPaymentTime().intValue() != 15
				&& req.getPaymentTime().intValue() != 30 && req.getPaymentTime().intValue() != 45)) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_DATA.getErrorENMsg(), null);
		}
		if (req.getEffectiveTime() == null
				|| (req.getEffectiveTime().intValue() != 3 && req.getEffectiveTime().intValue() != 5
						&& req.getEffectiveTime().intValue() != 10 && req.getEffectiveTime().intValue() != 15)) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_DATA.getErrorENMsg(), null);
		}
		if (req.getType().getType() == OrderTypeEnum.SELL.getType() && HelpUtils.nullOrBlank(req.getAcountId())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_DATA.getErrorENMsg(), "AcountId");
		}
		return new ObjResp();
	}

	@ApiOperation(value = "吃限价单,买卖单", notes = "输入字段：<br/> 对方订单ID：oppositeId 法币数量:volume <br/> 资金密码:securityPwd <br/> 单价:price <br/>", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "addOTCOrder", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp addOTCOrder(HttpServletRequest request, HttpServletResponse response, @RequestBody OTCOrderReq req) {

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}

		// 是否设置手机号码
		if (m.getM_name().contains("@")) {
			if (HelpUtils.nullOrBlank(m.getPhone())) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.PSL_SET_PHONE.getErrorENMsg(), null);
			}
		}
		//交易量检查
		if (req.getVolume() == null || req.getVolume().compareTo(BigDecimal.ZERO) <= 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
		}
		// 判断是否进行过身份认证
		AuthIdentity authIdentity = memberService.getAuthIdentityById(m.getId());
		if (null == authIdentity || authIdentity.getId_status() != 1) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_AUTH_IDENTITY_FIRST.getErrorENMsg(), null);
		}

		//用户昵称校验
		if (StringUtils.isBlank(m.getM_nick_name())){
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ISNOTEMPTY_NICKNAME.getErrorENMsg(), null);
		}

		if (!"1".equals(m.getM_security_pwd()) || !validateSecurityPwd(m.getId(), req.getSecurityPwd())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg(), null);
		}
		if(req.getOppositeId() == null || req.getOppositeId().intValue() == 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ORDER_ID.getErrorENMsg(), null);
		}
		// merchantId的处理
		Integer memberId = m.getId();
		String IpAddr = HelpUtils.getIpAddr(request);

		OTCOrderDTO oTCOrderDTO = new OTCOrderDTO();
		oTCOrderDTO.setMemberId(memberId);
		oTCOrderDTO.setOperIp(IpAddr);

		OTCOrderDTO opposite = oTCOrderManager.findById(req.getOppositeId());
		
		if(opposite == null) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ORDER_ID.getErrorENMsg(), null);
		}


		if (opposite.getMemberId().intValue() == memberId.intValue()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.SELF_TRADE_ERROR.getErrorENMsg(), null);
		}

		//查询委托人是否有收款方式
		Map<String, Object> condition = HelpUtils.newHashMap("id_list", opposite.getAcountId());
		List<AccountInfoDTO> clientAccountInfoDTOS = oTCAccountInfoManager.findByConditionPage(condition);
		if (null == clientAccountInfoDTOS || clientAccountInfoDTOS.size() <=0){
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_UNBOUND_COLLECTION.getErrorENMsg(), null);
		}

		//查吃单人是否有收款方式
		condition = HelpUtils.newHashMap("memberId", m.getId(),"isDelete",0);
		List<AccountInfoDTO> singleAccountInfoDTOS = oTCAccountInfoManager.findByConditionPage(condition);
		if (null == singleAccountInfoDTOS || singleAccountInfoDTOS.size() <=0 ){
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_UNBOUND_COLLECTION.getErrorENMsg(), null);
		}

		//委托人和吃单人账户类型做匹配，若无交集，返回错误信息。
		boolean flag = false;
		for (AccountInfoDTO aifItem : clientAccountInfoDTOS){
			for (AccountInfoDTO aifParam : singleAccountInfoDTOS){
				if (aifItem.getType() == aifParam.getType()){
					flag=true;
					break;
				}
			}
		}
		if (!flag) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_MISMATCH_COLLECTION.getErrorENMsg(), null);
		}

		if(req.getPrice().compareTo(opposite.getPrice()) != 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_DATA.getErrorENMsg(), "价格已经过期失效，请重新下单");
		}

		//  卖需要判断用户资产账号是否存在,对方是买，我方就是卖
		if (opposite.getType().getType() == OrderTypeEnum.BUY.getType()) {
			AccountDTO userAsset = otcAccountService
					.findBymerchantIdAndCurrency(HelpUtils.newHashMap("memberId", m.getId()
							, "currency", opposite.getBaseCurrency()));
			if(userAsset == null) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorENMsg(), ErrorInfoEnum.LANG_ACCOUNT_NOT_EXIST.getErrorCNMsg());
			}
		}

		// 如果是买方 有两个限制：一、存在未付款交易 二、当天存在三笔撤销记录不能交易
		if (opposite != null && opposite.getType().getType() == OrderTypeEnum.SELL.getType()) {
			// 如果有未付款的交易就不能吃单
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("memberId", memberId);
			queryMap.put("status", TradeStatusEnum.NP);
			queryMap.put("tType", OrderTypeEnum.BUY);
			List<OTCTradeDTO> list = oTCTradeManager.findByConditionPage(queryMap);
			if (!CollectionUtils.isEmpty(list)) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.BUY_HAS_ONE_TRADING.getErrorENMsg(), list.get(0));
			}
			// 当天存在三笔撤销记录不能吃单
			Date nowDate = new Date();
			String startDate = HelpUtils.formatDate(nowDate) + " 00:00:00";
			String endDate = HelpUtils.formatDate(nowDate) + " 23:59:59";
			Integer count = daoUtil.queryForInt(
					"SELECT COUNT(id) FROM `o_trade` t WHERE t.`member_id` = ? AND t.status = ? AND t.`t_type` = ? AND t.`modify_time` >= ? AND t.`modify_time` <= ?",
					memberId, TradeStatusEnum.CANCELED.getType(), OrderTypeEnum.BUY.getType(), startDate, endDate);
			if(count > 2) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.CANCEL_TRADE_OVERMUCH.getErrorENMsg(), null);
			}
		}
		
		//卖方未放行申诉，二十四小时内不能出售
		if (opposite != null && opposite.getType().getType() == OrderTypeEnum.BUY.getType()) {
			Date date = HelpUtils.dateAddInt(new Date(), -1);
			String startDate = HelpUtils.formatDate8(date);
			Integer count = daoUtil.queryForInt(
					"SELECT COUNT(1) FROM `o_trade` t WHERE t.`t_type` = ? AND t.`member_id` = ? AND t.complain_type = ? AND t.`done_time` >= ?",
					OrderTypeEnum.SELL.getType(), memberId, TradeStatusEnum.UNCONFIRMED.getType(), startDate);
			if(count > 0) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.COMPLAIN_BY_UNCONFIRMED.getErrorENMsg(), null);
			}
		}

		// 不是商户，判断是否存在一笔交易未完成
		ObjResp merchant = merchantManager.isMerchant(m.getId());
		if (!Resp.SUCCESS.equals(merchant.getState())) {
			int selfType = opposite.getType().getType() == OrderTypeEnum.BUY.getType() ? OrderTypeEnum.SELL.getType() :  OrderTypeEnum.BUY.getType();
			Integer count = daoUtil.queryForInt(
					"SELECT COUNT(1) FROM `o_trade` t WHERE  t.`member_id` = ? and t.`t_type` = ? AND t.`status` not in (3,4)"
					,m.getId(), selfType);
			if(count > 0) {
                if (selfType == OrderTypeEnum.BUY.getType()) {

                    return new ObjResp(Resp.FAIL, ErrorInfoEnum.BUY_HAS_ONE_TRADING.getErrorENMsg(), null);
                }else {
                    return new ObjResp(Resp.FAIL, ErrorInfoEnum.SELL_HAS_ONE_TRADING.getErrorENMsg(), null);
                }
			}

		}


		BigDecimal tradeAmount = req.getVolume().multiply(opposite.getPrice());

		//
		if (opposite.getMinQuote().compareTo(tradeAmount) > 0) {
			String err = "order MinQuote:" + opposite.getMinQuote().stripTrailingZeros().toPlainString() + " trade MinQuote:"
					+ tradeAmount.stripTrailingZeros().toPlainString();
			return new ObjResp(Resp.FAIL,
					ErrorInfoEnum.LANG_ERROR_DATA.getErrorENMsg(),
					err);
		}
		if (opposite.getMaxQuote().compareTo(tradeAmount) < 0) {
			String err = "order MaxQuote:" + opposite.getMaxQuote().stripTrailingZeros().toPlainString() + " trade MaxQuote:"
					+ tradeAmount.stripTrailingZeros().toPlainString();
			return new ObjResp(Resp.FAIL,
					ErrorInfoEnum.LANG_ERROR_DATA.getErrorENMsg(),
					err);
		}
		oTCOrderDTO.setVolume(req.getVolume());
		oTCOrderDTO.setPriceType(PriceTypeEnum.LIMIT);
		// 从对方订单拿的数据
		oTCOrderDTO.setBaseCurrency(opposite.getBaseCurrency());
		oTCOrderDTO.setQuoteCurrency(opposite.getQuoteCurrency());
		OrderTypeEnum orderType = opposite.getType().getType() == OrderTypeEnum.BUY.getType() ? OrderTypeEnum.SELL
				: OrderTypeEnum.BUY;
		oTCOrderDTO.setType(orderType);
		oTCOrderDTO.setPrice(opposite.getPrice());
		oTCOrderDTO.setStatus(OrderStatusEnum.TRADING);
		oTCOrderDTO.setIsAds(WhetherEnum.NO);
		// 吃单
		ObjResp resp = oTCTradeManager.createOrder(oTCOrderDTO, opposite);
		try {
			if (resp.getState().intValue() == Resp.SUCCESS.intValue()) {
				OTCTradeDTO selfDTO = (OTCTradeDTO) resp.getData();
				Member opposite_m = memberService.getMemberById(opposite.getMemberId());
				if (BeanUtil.isEmpty(opposite_m.getArea_code()) || opposite_m.getArea_code().equals(AreaCodeEnum.CH.getAreaCode())) {
					// 自己是买方
					if (selfDTO.gettType().getType() == OrderTypeEnum.BUY.getType()) {
						// 通知自己
//						String buySmsParams = Joiner.on(",").join(Arrays.asList(opposite_m.getM_nick_name()
//								, selfDTO.getVolume().stripTrailingZeros().toPlainString(), selfDTO.getBaseCurrency()
//								, selfDTO.getPrice().stripTrailingZeros().toPlainString(), selfDTO.getQuoteCurrency()
//								, selfDTO.gettNumber(), opposite.getPaymentTime()));
//						ISmsService buySmsService = new ChuangLanSmsServiceImpl().builder(m.getM_name(), buySmsParams
//								, AbstractChuangLanSmsService.CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME);
//						SmsSendPool.getInstance().send(buySmsService);

						//聚合验证码
						String format = String.format(MobiInfoTemplateEnum.JH_TRADE_BOUGHT.getCode(),
								opposite_m.getM_nick_name(),
								selfDTO.getVolume().stripTrailingZeros().toPlainString(),
								selfDTO.getBaseCurrency(),
								selfDTO.getPrice().stripTrailingZeros().toPlainString(),
								selfDTO.getQuoteCurrency(),
								selfDTO.gettNumber(),
								opposite.getPaymentTime());
						SmsSendPool.getInstance().send(new JuheSend(m.getM_name(), MobiInfoTemplateEnum.JH_TRADE_BOUGHT.getType(), format));

						// 通知对方
//						String sellSmsParams = Joiner.on(",").join(Arrays.asList(m.getM_nick_name()
//								, selfDTO.getVolume().stripTrailingZeros().toPlainString(), selfDTO.getBaseCurrency()
//								, selfDTO.getPrice().stripTrailingZeros().toPlainString(), selfDTO.getQuoteCurrency()
//								, selfDTO.gettNumber(), opposite.getPaymentTime()));
//						ISmsService sellSmsService = new ChuangLanSmsServiceImpl().builder(opposite_m.getM_name(), sellSmsParams
//								, AbstractChuangLanSmsService.CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER);
//						SmsSendPool.getInstance().send(sellSmsService);

						//聚合验证码
						format = String.format(MobiInfoTemplateEnum.JH_TRADE_SOLD.getCode(),
								m.getM_nick_name(),
								selfDTO.getVolume().stripTrailingZeros().toPlainString(),
								selfDTO.getBaseCurrency(),
								selfDTO.getPrice().stripTrailingZeros().toPlainString(),
								selfDTO.getQuoteCurrency(),
								selfDTO.gettNumber(),
								opposite.getPaymentTime());
						SmsSendPool.getInstance().send(new JuheSend(opposite_m.getM_name(), MobiInfoTemplateEnum.JH_TRADE_SOLD.getType(), format));
					} else {
						// 自己是卖方
						// 通知对方
//						String buySmsParams = Joiner.on(",").join(Arrays.asList(m.getM_nick_name(),
//								selfDTO.getVolume().stripTrailingZeros().toPlainString(), selfDTO.getBaseCurrency(),
//								selfDTO.getPrice().stripTrailingZeros().toPlainString(), selfDTO.getQuoteCurrency(),
//								selfDTO.gettNumber(), opposite.getPaymentTime()));
//						ISmsService buySmsService = new ChuangLanSmsServiceImpl().builder(opposite_m.getM_name(), buySmsParams
//								, AbstractChuangLanSmsService.CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME);
//						SmsSendPool.getInstance().send(buySmsService);

						//聚合验证码
						String format = String.format(MobiInfoTemplateEnum.JH_TRADE_BOUGHT.getCode(),
								m.getM_nick_name(),
								selfDTO.getVolume().stripTrailingZeros().toPlainString(),
								selfDTO.getBaseCurrency(),
								selfDTO.getPrice().stripTrailingZeros().toPlainString(),
								selfDTO.getQuoteCurrency(),
								selfDTO.gettNumber(),
								opposite.getPaymentTime());
						SmsSendPool.getInstance().send(new JuheSend(opposite_m.getM_name(), MobiInfoTemplateEnum.JH_TRADE_BOUGHT.getType(), format));

						// 通知自己
//						String sellSmsParams = Joiner.on(",").join(Arrays.asList(opposite_m.getM_nick_name(),
//								selfDTO.getVolume().stripTrailingZeros().toPlainString(), selfDTO.getBaseCurrency(),
//								selfDTO.getPrice().stripTrailingZeros().toPlainString(), selfDTO.getQuoteCurrency(),
//								selfDTO.gettNumber(), opposite.getPaymentTime()));
//						ISmsService sellSmsService = new ChuangLanSmsServiceImpl().builder(m.getM_name(), sellSmsParams
//								, AbstractChuangLanSmsService.CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER);
//						SmsSendPool.getInstance().send(sellSmsService);

						//聚合验证码
						format = String.format(MobiInfoTemplateEnum.JH_TRADE_SOLD.getCode(),
								opposite_m.getM_nick_name(),
								selfDTO.getVolume().stripTrailingZeros().toPlainString(),
								selfDTO.getBaseCurrency(),
								selfDTO.getPrice().stripTrailingZeros().toPlainString(),
								selfDTO.getQuoteCurrency(),
								selfDTO.gettNumber(),
								opposite.getPaymentTime());
						SmsSendPool.getInstance().send(new JuheSend(m.getM_name(), MobiInfoTemplateEnum.JH_TRADE_SOLD.getType(), format));
					}
				}
//				else {
//					// 自己是买方
//					if (selfDTO.gettType().getType() == OrderTypeEnum.BUY.getType()) {
//						// 通知自己
//						ISmsService buySmsService = new InterSmsServiceImpl().builder(opposite_m.getM_nick_name()
//								, selfDTO.getVolume().stripTrailingZeros().toPlainString(), selfDTO.getBaseCurrency()
//								, selfDTO.getPrice().stripTrailingZeros().toPlainString(), selfDTO.getQuoteCurrency()
//								, selfDTO.gettNumber(), opposite.getPaymentTime(),
//								m.getM_name(), InternationalSmsCNEnum.CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME.getType());
//						SmsSendPool.getInstance().send(buySmsService);
//						// 通知对方
//						String sellSmsParams = Joiner.on(",").join(Arrays.asList(m.getM_nick_name()
//								, selfDTO.getVolume().stripTrailingZeros().toPlainString(), selfDTO.getBaseCurrency()
//								, selfDTO.getPrice().stripTrailingZeros().toPlainString(), selfDTO.getQuoteCurrency()
//								, selfDTO.gettNumber(), opposite.getPaymentTime()));
//						ISmsService sellSmsService = new InterSmsServiceImpl().builder(m.getM_nick_name()
//								, selfDTO.getVolume().stripTrailingZeros().toPlainString(), selfDTO.getBaseCurrency()
//								, selfDTO.getPrice().stripTrailingZeros().toPlainString(), selfDTO.getQuoteCurrency()
//								, selfDTO.gettNumber(), opposite.getPaymentTime(),
//								opposite_m.getM_name(), InternationalSmsCNEnum.CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER.getType());
//						SmsSendPool.getInstance().send(sellSmsService);
//					} else {
//						// 自己是卖方
//
//						ISmsService buySmsService = new InterSmsServiceImpl().builder(
//								m.getM_nick_name(),
//								selfDTO.getVolume().stripTrailingZeros().toPlainString(), selfDTO.getBaseCurrency(),
//								selfDTO.getPrice().stripTrailingZeros().toPlainString(), selfDTO.getQuoteCurrency(),
//								selfDTO.gettNumber(), opposite.getPaymentTime()
//								, opposite_m.getM_name(), InternationalSmsCNEnum.CLT_BUY_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER_TIME.getType());
//						SmsSendPool.getInstance().send(buySmsService);
//						// 通知自己
//						ISmsService sellSmsService = new InterSmsServiceImpl().builder(opposite_m.getM_nick_name(),
//								selfDTO.getVolume().stripTrailingZeros().toPlainString(), selfDTO.getBaseCurrency(),
//								selfDTO.getPrice().stripTrailingZeros().toPlainString(), selfDTO.getQuoteCurrency(),
//								selfDTO.gettNumber(), opposite.getPaymentTime()
//								, m.getM_name()
//								, InternationalSmsCNEnum.CLT_SELL_MEMBER_NUM_CURRENCY_PRICE_NUM_ORDER.getType());
//						SmsSendPool.getInstance().send(sellSmsService);
//					}
//				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("发送短信异常， memberId:" + m.getId() + " \r\n" + e.toString());
		}

		return resp;
	}

	@ApiOperation(value = "查询可交易订单", notes = "输入字段：<br/>  订单类型:type(BUY/SELL) 基础货币:baseCurrency 页数:  page(从第1页开始)   每页条数:pagesize<br/> ", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "getTradeOTCOrder", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> tradeOTCOrder(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = $params(request);
		param.put("status", OrderStatusEnum.WATTING);
//		String sortname = String.valueOf(param.get("sortname"));
//		if (HelpUtils.nullOrBlank(sortname)) {
//			param.put("sortname", "create_time");
//			param.put("sortorder", "desc");
//		}
		
		String type = String.valueOf(param.get("type"));
		if(OrderTypeEnum.BUY.getCode().toUpperCase().equals(type)) {
			param.put("sortname", "vip,price,create_time");
			param.put("sortorder", "desc,desc,asc");
		}else {
			param.put("sortname", "vip,price,create_time");
			param.put("sortorder", "desc,asc,asc");
		}
		
		List<Map<String, Object>> list = oTCOrderManager.getTradeOTCOrder(param);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

//	WATTING(0, "watting"), TRADING(1, "trading"), DONE(2, "done"), PC(3, "partial-canceled"), CANCELED(4, "canceled");	
	@ApiOperation(value = "商家订单", notes = "输入字段：<br/>  商家ID:memberId 订单类型:type(BUY/SELL) 订单状态:status(WATTING|TRADING|DONE|PC|CANCELED) 基础货币:baseCurrency  页数:page(从第1页开始)   每页条数:pagesize<br/> ", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "getOTCOrder", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getOTCOrder(HttpServletRequest request, HttpServletResponse response) {

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}

		Map<String, Object> param = $params(request);
		if (HelpUtils.isMapNullValue(param, "memberId")) {
			param.put("memberId", m.getId());
		}
		String sortname = String.valueOf(param.get("sortname"));
		if (HelpUtils.nullOrBlank(sortname)) {
			param.put("sortname", "create_time");
			param.put("sortorder", "desc");
		}
		List<Map<String, Object>> list = oTCOrderManager.getTradeOTCOrder(param);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,
				HelpUtils.newHashMap("rows", list, "total", param.get("total")));
	}
	
//	WATTING(0, "watting"), TRADING(1, "trading"), DONE(2, "done"), PC(3, "partial-canceled"), CANCELED(4, "canceled");	
	@ApiOperation(value = "商家广告", notes = "输入字段：<br/>  商家ID:memberId 订单类型:type(BUY/SELL) 订单状态:status(WATTING|TRADING|DONE|PC|CANCELED) 基础货币:baseCurrency  页数:page(从第1页开始)   每页条数:pagesize<br/> ", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "getOTCAds", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getOTCAds(HttpServletRequest request, HttpServletResponse response) {

//		Member m = JedisUtilMember.getInstance().getMember(request, null);
//		if (null == m) {
//			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
//		}

		Map<String, Object> param = $params(request);
		if (HelpUtils.isMapNullValue(param, "memberId")) {
			return new ObjResp(Resp.FAIL, "LANG_ERROR_DATA", "memberId");
		}
		String sortname = String.valueOf(param.get("sortname"));
		if (HelpUtils.nullOrBlank(sortname)) {
			param.put("sortname", "create_time");
			param.put("sortorder", "desc");
		}
		param.put("isAds", WhetherEnum.YES);
		List<Map<String, Object>> list = oTCOrderManager.getTradeOTCOrder(param);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,
				HelpUtils.newHashMap("rows", list, "total", param.get("total")));
	}

	@ApiOperation(value = "订单撤销", notes = "输入字段：订单ID： id", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "cancel/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjResp cancelOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}

		OTCOrderDTO oTCOrderDTO = oTCOrderManager.findById(id);
		if (oTCOrderDTO == null) {
			return new ObjResp(Resp.FAIL, "LANG_ORDER_DOES_NOT_EXIST", null);
		}

		if (oTCOrderDTO.getMemberId().intValue() != m.getId().intValue()) {
			return new ObjResp(Resp.FAIL, "LANG_OPERTION_FAIL", null);
		}
		ObjResp resp = oTCTradeManager.cancelOrder(oTCOrderDTO);
		return resp;
	}

//	@ApiOperation(value = "一键交易", notes = "输入字段：<br/> 市价订单：基础货币：baseCurrency 计价货币:quoteCurrency 订单类型:type(BUY/SELL) 数量:volume(买方计价货币数|卖方基础货币数)", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "marketOTCOrder", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp marketOTCOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OTCOrderReq req) {

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}

		// merchantId的处理
		Integer memberId = m.getId();
		String IpAddr = HelpUtils.getIpAddr(request);

		OTCOrderDTO oTCOrderDTO = req.toOTCOrderDTO();
		oTCOrderDTO.setMemberId(memberId);
		oTCOrderDTO.setOperIp(IpAddr);
		oTCOrderDTO.setPriceType(PriceTypeEnum.MARKET);
		OTCOrderDTO opposite = null;

		// 挂单方
//		return oTCTradeManager.createOrder(oTCOrderDTO, opposite);
		return null;
	}

//	@ApiOperation(value = "test", notes = "", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "test", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp test(HttpServletRequest request, HttpServletResponse response) {
		return new ObjResp();
	}

	private String getDate(int day) {
		String date = HelpUtils.dateAddDay(-day);
		return date + " 23:59:59";
	}

}
