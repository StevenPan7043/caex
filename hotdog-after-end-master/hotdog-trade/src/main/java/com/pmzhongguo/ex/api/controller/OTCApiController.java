package com.pmzhongguo.ex.api.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pmzhongguo.ex.business.dto.OTCAccountInfoReq;
import com.pmzhongguo.ex.business.dto.OTCOrderConfirmReq;
import com.pmzhongguo.ex.business.dto.OTCOrderReq;
import com.pmzhongguo.ex.business.dto.WithdrawCreateDtoInner;
import com.pmzhongguo.ex.business.entity.AuthIdentity;
import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.business.entity.CoinWithdraw;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.entity.OTCAds;
import com.pmzhongguo.ex.business.mapper.OTCMapper;
import com.pmzhongguo.ex.business.resp.AdResp;
import com.pmzhongguo.ex.business.resp.AdsResp;
import com.pmzhongguo.ex.business.resp.OTCOrder;
import com.pmzhongguo.ex.business.resp.OTCOrdersResp;
import com.pmzhongguo.ex.business.service.ExService;
import com.pmzhongguo.ex.business.service.OTCService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.utils.MacMD5;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "OTC接口", description = "OTC接口", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("o")
public class OTCApiController extends TopController {
	private Logger logger = LoggerFactory.getLogger(OTCApiController.class);

	@Resource
	private OTCService otcService;
	@Resource
	protected ExService exService;

	@Resource
	private OTCMapper otcMapper;

	@ApiOperation(value = "获得24小时内价格情况", notes = "返回值数组为：开盘，最高，最低，收盘", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/ticker/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getOTCTicker(HttpServletRequest request, HttpServletResponse response) {

		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, otcService.tickersMap);
	}

	@ApiOperation(value = "获得OTC交易对", notes = "获得OTC交易对", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/symbol/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getOTCSymbol(HttpServletRequest request, HttpServletResponse response) {

		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, otcService.OTCPairNameLst);
	}

	@ApiOperation(value = "获得买卖广告列表", notes = "获得买卖广告列表，ad_type：1买入，2卖出，0所有", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/ads/{base_currency}/{quote_currency}/{ad_type}/{page}/{pageSize}", method = RequestMethod.GET)
	@ResponseBody
	public AdsResp getAdsI(HttpServletRequest request, HttpServletResponse response, @PathVariable String base_currency,
			@PathVariable String quote_currency, @PathVariable Integer ad_type, @PathVariable Integer page,
			@PathVariable Integer pageSize) {

		Map<String, Object> param = new HashMap<String, Object>();

		if (ad_type == 1 || ad_type == 2) {
			param.put("ad_type", ad_type);
		}
		param.put("c_status", 1);
		param.put("base_currency", base_currency);
		param.put("quote_currency", quote_currency);
		param.put("page", page);
		param.put("pagesize", pageSize);
		param.put("sortname", "a_order");
		param.put("sortorder", "asc");

		List<OTCAds> jsonList = otcService.getAllOTCAds(param);

		return new AdsResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList, Integer.parseInt(param.get("total") + ""));
	}

	@ApiOperation(value = "获得买卖广告详情", notes = "获得买卖广告详情", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/ads/{id}", method = RequestMethod.GET)
	@ResponseBody
	public AdResp getAds(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {

		OTCAds ad = otcService.findOTCAdsById(id);

		return new AdResp(Resp.SUCCESS, Resp.SUCCESS_MSG, ad);
	}

	@ApiOperation(value = "获得OTC订单", notes = "base_currency:基础货币，所有传下划线+大写(_ALL)，如BTC，quote_currency计价货币，所有传下划线+大写(_ALL)，如CNY <br>page:页数，从1开始，默认按时间倒叙排列 <br>ad_type 1表示买入订单，2表示卖出订单 <br>status 0未完成(含待付款和待对方处理,处理中)，1已完成，2已取消，9全部", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/orders/{base_currency}/{quote_currency}/{page}/{pageSize}/{ad_type}/{status}/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public OTCOrdersResp getOrders(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String base_currency, @PathVariable String quote_currency, @PathVariable Integer page,
			@PathVariable Integer pageSize, @PathVariable Integer ad_type, @PathVariable Integer status,
			@PathVariable Long ts) {

		Map<String, Object> orderMap = new HashMap<String, Object>();

		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new OTCOrdersResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null, 0);
		}
		orderMap.put("member_id", member.getId());
		if (status == 0) {
			if(ad_type==1) {
				orderMap.put("status", " <= 0");
			}
			if(ad_type==2) {
				orderMap.put("status", "in (0,3,4)");
			}
		} else if (status == 1) {
			orderMap.put("status", " = 1 ");
		} else if (status == 2) {
			orderMap.put("status", " = 2 ");
		}

		orderMap.put("page", page);
		orderMap.put("pagesize", pageSize);
		orderMap.put("sortname", "id");
		orderMap.put("sortorder", "desc");
		if (!"_ALL".equals(base_currency)) {
			orderMap.put("base_currency", base_currency);
		}
		if (!"_ALL".equals(quote_currency)) {
			orderMap.put("quote_currency", quote_currency);
		}

		List<OTCOrder> jsonList = otcService.getOrders(orderMap, ad_type);

		return new OTCOrdersResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList,
				Integer.parseInt(orderMap.get("total") + ""));
	}

	@ApiOperation(value = "用户是否已经实名认证", notes = "用户是否已经实名认证，data值：1表示已经实名，0表示未实名", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/is_auth", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getIsAuth(HttpServletRequest request, HttpServletResponse response) {
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}
		// 校验用户是否实名认证
		AuthIdentity authIdentity = memberService.getAuthIdentityById(member.getId());
		if (null != authIdentity && authIdentity.getId_status() == 1) {
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, 1);
		}

		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, 0);
	}

	@ApiOperation(value = "绑定用户账户(银行卡、微信、支付宝)信息", notes = "账户信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/account_info", method = RequestMethod.POST)
	@ResponseBody
	public Resp addAccountInfoI(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OTCAccountInfoReq otcAccountInfoReq) {

		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
		}

		if (!"bank".equals(otcAccountInfoReq.getA_type()) && !"alipay".equals(otcAccountInfoReq.getA_type())
				&& !"wxpay".equals(otcAccountInfoReq.getA_type())) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_A_TYPE.getErrorENMsg());
		}

		// 校验该类型是否已经绑定，一个类型只能绑定一个
		/*
		 * Integer alreadyCount = daoUtil.
		 * queryForInt("select count(1) from otc_account_info where member_id = ? and a_type = ?"
		 * , member.getId(), otcAccountInfoReq.getA_type()); if (alreadyCount > 0) {
		 * return new Resp(Resp.FAIL, "LANG_A_TYPE_ONLY_ONE_RECORD"); }
		 */

		// 校验用户是否实名认证
		AuthIdentity authIdentity = memberService.getAuthIdentityById(member.getId());
		if (null == authIdentity || authIdentity.getId_status() != 1) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_AUTH_IDENTITY_FIRST.getErrorENMsg());
		}

		// 校验资金密码
		Member oldMember = memberService.getMemberById(member.getId());
		if (HelpUtils.nullOrBlank(otcAccountInfoReq.getSec_pwd())
				|| !MacMD5.CalcMD5Member(otcAccountInfoReq.getSec_pwd()).equals(oldMember.getM_security_pwd())) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg());
		}

		// 如果已经绑定，就先删原来的
		daoUtil.update("delete from otc_account_info where member_id = ? and a_type = ?", member.getId(),
				otcAccountInfoReq.getA_type());

		daoUtil.update(
				"INSERT INTO otc_account_info(member_id,a_type,a_name,a_account,a_bank_or_img,a_create_time) VALUES(?, ?, ?, ?, ?, ?)",
				member.getId(), otcAccountInfoReq.getA_type(), otcAccountInfoReq.getA_name(),
				otcAccountInfoReq.getA_account(), otcAccountInfoReq.getA_bank_or_img(),
				HelpUtils.formatDate8(new Date()));

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	@ApiOperation(value = "获得用户账户(银行卡、微信、支付宝)信息", notes = "账户信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/account_info", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getAccountInfoI(HttpServletRequest request, HttpServletResponse response) {

		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}

		List jsonList = daoUtil.queryForList(
				"select id, a_type,a_name,a_account,a_bank_or_img from otc_account_info where member_id = ?",
				member.getId());

		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList);
	}

	@ApiOperation(value = "提交OTC订单", notes = "提交OTC订单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/addorder", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp createOTCOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OTCOrderReq otcOrderReq) throws Exception {
		// 校验类型
		if (1 != otcOrderReq.getAd_type() && 2 != otcOrderReq.getAd_type()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADS_TYPE.getErrorENMsg(), null);
		}
		//根据广告id查询广告主信息和广告信息
		OTCAds ads = otcService.findOTCAdsById(otcOrderReq.getAds_id());
		// XXX 判断是卖出还是买入 1:买入 , 2:卖出
		if (otcOrderReq.getAd_type() == 2) {
			// 校验最小限额
			if (null == otcOrderReq.getVolume()
					|| otcOrderReq.getVolume().multiply(ads.getPrice()).compareTo(ads.getMin_quote()) < 0) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
			}
			// 判断金额是否是 100 的倍数
//			BigDecimal bd = otcOrderReq.getVolume();
//			int volume = bd.intValue();
//			if (volume % 100 != 0) {
//				return new ObjResp(Resp.FAIL, "SALES_NUMBER_MULTIPLE_100", null);
//			}
		}else if(otcOrderReq.getAd_type() == 1) {
			// 校验数量
			if (null == otcOrderReq.getVolume()
					|| otcOrderReq.getVolume().multiply(ads.getPrice()).compareTo(ads.getMin_quote()) < 0
					|| otcOrderReq.getVolume().multiply(ads.getPrice()).compareTo(ads.getMax_quote()) > 0) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
			}
		}
		
		if (null == ads || ads.getId() <= 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADS_ID.getErrorENMsg(), null);
		}
		// 接收参数
		BigDecimal minQuote = ads.getMin_quote();
		BigDecimal maxQuote = ads.getMax_quote();
		BigDecimal totalVolume = ads.getTotal_volume();
		BigDecimal volume = otcOrderReq.getVolume();
		// (可用额度 - 参数金额) < 0 ==> 满额条件
		// System.err.println(maxQuote.subtract(totalVolume).subtract(volume).compareTo(BigDecimal.ZERO));
		// if
		// (maxQuote.subtract(totalVolume).subtract(volume).compareTo(BigDecimal.ZERO)
		// == -1) {
		// return new ObjResp(Resp.FAIL, "FULL_AMOUNT", null);
		// }

		// 校验类型
		if (otcOrderReq.getAd_type() != ads.getAd_type()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADS_TYPE.getErrorENMsg(), null);
		}


		// 校验账户信息
		if (HelpUtils.nullOrBlank(otcOrderReq.getAccount_types())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ACCOUNT_TYPE.getErrorENMsg(), null);
		}
		
		String[] accountTypeArr = otcOrderReq.getAccount_types().split(",");
		String accountTypeWhere = "";
		if (otcOrderReq.getAd_type() == 1) { // 买入，对应充值，只能选一个
			if (!"bank".equals(otcOrderReq.getAccount_types()) && !"alipay".equals(otcOrderReq.getAccount_types())
					&& !"wxpay".equals(otcOrderReq.getAccount_types())) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ACCOUNT_TYPE.getErrorENMsg(), null);
			}
			accountTypeWhere = " = '" + otcOrderReq.getAccount_types() + "'";
		} else {
			accountTypeWhere = " in ('1'";
			for (int i = 0; i < accountTypeArr.length; i++) {
				if (!"bank".equals(accountTypeArr[i]) && !"alipay".equals(accountTypeArr[i])
						&& !"wxpay".equals(accountTypeArr[i])) {
					return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ACCOUNT_TYPE.getErrorENMsg(), null);
				}

				accountTypeWhere += ", '" + accountTypeArr[i] + "'";
			}
			accountTypeWhere += ")";
		}

		// 校验用户是否登录
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}

		// 获得用户的付款类型
		List<Map> dbAccountInfoLst = daoUtil.queryForList(
				"select id, a_type, a_name, a_account, a_bank_or_img from otc_account_info where member_id = ? and a_type "
						+ accountTypeWhere,
				member.getId());
		if (null == dbAccountInfoLst || dbAccountInfoLst.size() == 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ACCOUNT_TYPE.getErrorENMsg(), null);
		}
		String accountInfo = "";
		for (Map object : dbAccountInfoLst) {
			accountInfo += "→" + object.get("a_type") + "▲" + object.get("a_name") + "▲" + object.get("a_account") + "▲"
					+ object.get("a_bank_or_img");
		}

		// 校验用户是否实名认证
		AuthIdentity authIdentity = memberService.getAuthIdentityById(member.getId());
		if (null == authIdentity || authIdentity.getId_status() != 1) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_AUTH_IDENTITY_FIRST.getErrorENMsg(), null);
		}

		// 校验资金密码
		Member oldMember = memberService.getMemberById(member.getId());
		if (HelpUtils.nullOrBlank(otcOrderReq.getSec_pwd())
				|| !MacMD5.CalcMD5Member(otcOrderReq.getSec_pwd()).equals(oldMember.getM_security_pwd())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg(), null);
		}

		// 买入，判断是否有订单未完成
		if (otcOrderReq.getAd_type() == 1) {
			Integer waitNum = daoUtil.queryForInt(
					"select count(1) from m_coin_recharge where member_id = ? and currency = ? and r_status = -1"
					, member.getId(),ads.getBase_currency());
			// 超过两个订单未付款
			if (waitNum > 1) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_WAITING_ORDER_MAX_THAN_2.getErrorENMsg(), null);
			}
		}

		// 买入，超过2笔订单待确认
		if (otcOrderReq.getAd_type() == 1) {
			Integer waitConfirm = daoUtil.queryForInt(
					"select count(1) from m_coin_recharge where member_id = ? and currency = ? and r_status = 0"
					, member.getId(),ads.getBase_currency());
			// 超过两个订单未付款
			if (waitConfirm > 1) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_WAITING_ORDER_CONFIRM_MAX_THAN_2.getErrorENMsg(), null);
			}
		}

		// 先保存账户信息，搞成绑定
		/*
		 * Integer num = daoUtil.
		 * queryForInt("select count(1) from otc_account_info where member_id = ? and a_type = ? and a_info = ?"
		 * , member.getId(), otcOrderReq.getAccount_type(),
		 * otcOrderReq.getAccount_info()); if (num == 0) { daoUtil.
		 * update("insert into otc_account_info(member_id, a_type, a_info) values(?, ?, ?)"
		 * , member.getId(), otcOrderReq.getAccount_type(),
		 * otcOrderReq.getAccount_info()); }
		 */

		// 再刷新成交量
		if (otcOrderReq.getAd_type() == 1) {// 买入修改广告额度
			//根据广告id修改总数量和总金额
//			daoUtil.update(
//					"update otc_ads set total_volume = total_volume + ?, total_amount = total_amount + ? where id = ? ",
//					otcOrderReq.getVolume(), ads.getPrice().multiply(otcOrderReq.getVolume()), ads.getId());
//			//根据商户名称修改最大限额，用户买入则商户的可用额度应该减少，订单确认后总额度也应当减少
			daoUtil.update(
					"update otc_ads set max_quote=max_quote-? where owner_id = ? and base_currency=?",
					otcOrderReq.getVolume(), ads.getOwner_id(),ads.getBase_currency());
		} else {// 卖出修改广告限额 , 总数量总金额最大限额添加
//			daoUtil.update(
//					"update otc_ads set total_volume = total_volume + ?, total_amount = total_amount + ? where id = ? ",
//					 otcOrderReq.getVolume(), ads.getPrice().multiply(otcOrderReq.getVolume()),
//					ads.getId());
//			daoUtil.update(
//					"update otc_ads set max_quote=max_quote+? where owner_id = ? and base_currency=?",
//					otcOrderReq.getVolume(), ads.getOwner_id(),ads.getBase_currency());
		}

		String jedisKey = ads.getBase_currency().toLowerCase() + ads.getQuote_currency().toLowerCase();
		// 时间戳, 价格, 数量
		BigDecimal[] datas = { BigDecimal.valueOf(HelpUtils.getNowTimeStampMillisecond()), ads.getPrice(),
				otcOrderReq.getVolume() };
		JedisUtil.getInstance().lpush(jedisKey + "_OTC_trade", datas);
		JedisUtil.getInstance().ltrim(jedisKey + "_OTC_trade", 0, 10000);

		if (otcOrderReq.getAd_type() == 1) { // 买入，对应充值
			CoinRecharge coinRecharge = new CoinRecharge();
			coinRecharge.setMember_id(member.getId());
			coinRecharge.setR_amount(otcOrderReq.getVolume());
			coinRecharge.setR_address(accountInfo);
			coinRecharge.setR_txid(member.getId() + "-" + System.currentTimeMillis() + HelpUtils.randomString(10));
			coinRecharge.setCurrency(ads.getBase_currency());
			coinRecharge.setCurrency_id(2); // ZC在d_currency表中的id是2，默认写2
			coinRecharge.setR_confirmations("1");
			coinRecharge.setR_create_time(HelpUtils.formatDate8(new Date()));
			coinRecharge.setR_status(-1); // 状态待付款
			coinRecharge.setOtc_ads_id(ads.getId());
			coinRecharge.setOtc_money(ads.getPrice().multiply(otcOrderReq.getVolume()));
			coinRecharge.setOtc_oppsite_currency(ads.getQuote_currency());
			coinRecharge.setOtc_owner_name(ads.getO_name());
			coinRecharge.setOtc_price(ads.getPrice());

			memberService.addCoinRechargeByMember(coinRecharge);

			// TODO 判断可用余额是否大于最小限额, 小于则自动关闭广告
			System.err.println(maxQuote.subtract(totalVolume).subtract(volume).compareTo(minQuote));
			if (maxQuote.subtract(volume).compareTo(minQuote) == -1) {
				System.err.println("自动关闭广告!");
				Map<String, Object> paramMap = new HashMap<String, Object>(2);
				paramMap.put("status", 0);
				paramMap.put("id", ads.getId());
				otcMapper.updateStatus4Ads(paramMap);
			}
		} else { // 卖出，对应提现
			WithdrawCreateDtoInner withdrawCreateDto = new WithdrawCreateDtoInner();
			withdrawCreateDto.setCurrency(ads.getQuote_currency());
			withdrawCreateDto.setAmount(otcOrderReq.getVolume().multiply(ads.getPrice()));
			withdrawCreateDto.setAddr(accountInfo);

			addWithdraw(ads, withdrawCreateDto, 0, withdrawCreateDto.getAddr(),
					withdrawCreateDto.getMember_coin_addr_label(), HelpUtils.getIpAddr(request), member.getId());
		}
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}

	@ApiOperation(value = "OTC买单已付款（即点我已付款）", notes = "确认OTC买单（即点我已付款）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/confirmorder", method = RequestMethod.POST)
	@ResponseBody
	public Resp confirmOTCOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OTCOrderConfirmReq otcOrderConfirmReq) throws Exception {
		// 校验用户是否登录
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
		} 

		Long id = otcOrderConfirmReq.getId();
		CoinRecharge coinRecharge = memberService.getCoinRecharge(id);
		if (null == coinRecharge || coinRecharge.getMember_id().intValue() != member.getId().intValue()) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ORDER_NOT_EXISTS.getErrorENMsg());
		}

		if (coinRecharge.getR_status() != -1) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ORDER_ALREADY_CONFIRMED.getErrorENMsg());
		}

		// 可以更新，这里可以直接更新，因为状态为1时后台无法操作。
		daoUtil.update("update m_coin_recharge set r_status = 0 where id = ?", id);

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}
	
	
	@ApiOperation(value = "OTC卖单确认收款（即点确认收款）", notes = "确认OTC卖单（即点确认收款）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/confirmGathering", method = RequestMethod.POST)
	@ResponseBody
	public Resp confirmOTCGathering(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OTCOrderConfirmReq otcOrderConfirmReq) throws Exception {
		// 校验用户是否登录
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
		} 

		Long id = otcOrderConfirmReq.getId();
		CoinWithdraw coinWithdraw = memberService.getCoinWithdraw(id);
		if (null == coinWithdraw || coinWithdraw.getMember_id().intValue() != member.getId().intValue()) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ORDER_NOT_EXISTS.getErrorENMsg());
		}
		
		// 更新状态为已完成。
		daoUtil.update("update m_coin_withdraw set w_status = 1 where id = ?", id);
		//处理订单数据
		memberService.confirmGathering(id);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

}
