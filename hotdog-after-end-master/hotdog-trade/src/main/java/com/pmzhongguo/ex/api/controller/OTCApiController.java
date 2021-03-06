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

@Api(value = "OTC??????", description = "OTC??????", produces = MediaType.APPLICATION_JSON_VALUE)
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

	@ApiOperation(value = "??????24?????????????????????", notes = "??????????????????????????????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/ticker/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getOTCTicker(HttpServletRequest request, HttpServletResponse response) {

		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, otcService.tickersMap);
	}

	@ApiOperation(value = "??????OTC?????????", notes = "??????OTC?????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/symbol/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getOTCSymbol(HttpServletRequest request, HttpServletResponse response) {

		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, otcService.OTCPairNameLst);
	}

	@ApiOperation(value = "????????????????????????", notes = "???????????????????????????ad_type???1?????????2?????????0??????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

	@ApiOperation(value = "????????????????????????", notes = "????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/ads/{id}", method = RequestMethod.GET)
	@ResponseBody
	public AdResp getAds(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {

		OTCAds ad = otcService.findOTCAdsById(id);

		return new AdResp(Resp.SUCCESS, Resp.SUCCESS_MSG, ad);
	}

	@ApiOperation(value = "??????OTC??????", notes = "base_currency:?????????????????????????????????+??????(_ALL)??????BTC???quote_currency?????????????????????????????????+??????(_ALL)??????CNY <br>page:????????????1???????????????????????????????????? <br>ad_type 1?????????????????????2?????????????????? <br>status 0?????????(??????????????????????????????,?????????)???1????????????2????????????9??????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

	@ApiOperation(value = "??????????????????????????????", notes = "?????????????????????????????????data??????1?????????????????????0???????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/is_auth", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getIsAuth(HttpServletRequest request, HttpServletResponse response) {
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}
		// ??????????????????????????????
		AuthIdentity authIdentity = memberService.getAuthIdentityById(member.getId());
		if (null != authIdentity && authIdentity.getId_status() == 1) {
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, 1);
		}

		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, 0);
	}

	@ApiOperation(value = "??????????????????(??????????????????????????????)??????", notes = "????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
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

		// ??????????????????????????????????????????????????????????????????
		/*
		 * Integer alreadyCount = daoUtil.
		 * queryForInt("select count(1) from otc_account_info where member_id = ? and a_type = ?"
		 * , member.getId(), otcAccountInfoReq.getA_type()); if (alreadyCount > 0) {
		 * return new Resp(Resp.FAIL, "LANG_A_TYPE_ONLY_ONE_RECORD"); }
		 */

		// ??????????????????????????????
		AuthIdentity authIdentity = memberService.getAuthIdentityById(member.getId());
		if (null == authIdentity || authIdentity.getId_status() != 1) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_AUTH_IDENTITY_FIRST.getErrorENMsg());
		}

		// ??????????????????
		Member oldMember = memberService.getMemberById(member.getId());
		if (HelpUtils.nullOrBlank(otcAccountInfoReq.getSec_pwd())
				|| !MacMD5.CalcMD5Member(otcAccountInfoReq.getSec_pwd()).equals(oldMember.getM_security_pwd())) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg());
		}

		// ???????????????????????????????????????
		daoUtil.update("delete from otc_account_info where member_id = ? and a_type = ?", member.getId(),
				otcAccountInfoReq.getA_type());

		daoUtil.update(
				"INSERT INTO otc_account_info(member_id,a_type,a_name,a_account,a_bank_or_img,a_create_time) VALUES(?, ?, ?, ?, ?, ?)",
				member.getId(), otcAccountInfoReq.getA_type(), otcAccountInfoReq.getA_name(),
				otcAccountInfoReq.getA_account(), otcAccountInfoReq.getA_bank_or_img(),
				HelpUtils.formatDate8(new Date()));

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	@ApiOperation(value = "??????????????????(??????????????????????????????)??????", notes = "????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
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

	@ApiOperation(value = "??????OTC??????", notes = "??????OTC??????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/addorder", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp createOTCOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OTCOrderReq otcOrderReq) throws Exception {
		// ????????????
		if (1 != otcOrderReq.getAd_type() && 2 != otcOrderReq.getAd_type()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADS_TYPE.getErrorENMsg(), null);
		}
		//????????????id????????????????????????????????????
		OTCAds ads = otcService.findOTCAdsById(otcOrderReq.getAds_id());
		// XXX ??????????????????????????? 1:?????? , 2:??????
		if (otcOrderReq.getAd_type() == 2) {
			// ??????????????????
			if (null == otcOrderReq.getVolume()
					|| otcOrderReq.getVolume().multiply(ads.getPrice()).compareTo(ads.getMin_quote()) < 0) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
			}
			// ????????????????????? 100 ?????????
//			BigDecimal bd = otcOrderReq.getVolume();
//			int volume = bd.intValue();
//			if (volume % 100 != 0) {
//				return new ObjResp(Resp.FAIL, "SALES_NUMBER_MULTIPLE_100", null);
//			}
		}else if(otcOrderReq.getAd_type() == 1) {
			// ????????????
			if (null == otcOrderReq.getVolume()
					|| otcOrderReq.getVolume().multiply(ads.getPrice()).compareTo(ads.getMin_quote()) < 0
					|| otcOrderReq.getVolume().multiply(ads.getPrice()).compareTo(ads.getMax_quote()) > 0) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
			}
		}
		
		if (null == ads || ads.getId() <= 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADS_ID.getErrorENMsg(), null);
		}
		// ????????????
		BigDecimal minQuote = ads.getMin_quote();
		BigDecimal maxQuote = ads.getMax_quote();
		BigDecimal totalVolume = ads.getTotal_volume();
		BigDecimal volume = otcOrderReq.getVolume();
		// (???????????? - ????????????) < 0 ==> ????????????
		// System.err.println(maxQuote.subtract(totalVolume).subtract(volume).compareTo(BigDecimal.ZERO));
		// if
		// (maxQuote.subtract(totalVolume).subtract(volume).compareTo(BigDecimal.ZERO)
		// == -1) {
		// return new ObjResp(Resp.FAIL, "FULL_AMOUNT", null);
		// }

		// ????????????
		if (otcOrderReq.getAd_type() != ads.getAd_type()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADS_TYPE.getErrorENMsg(), null);
		}


		// ??????????????????
		if (HelpUtils.nullOrBlank(otcOrderReq.getAccount_types())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ACCOUNT_TYPE.getErrorENMsg(), null);
		}
		
		String[] accountTypeArr = otcOrderReq.getAccount_types().split(",");
		String accountTypeWhere = "";
		if (otcOrderReq.getAd_type() == 1) { // ???????????????????????????????????????
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

		// ????????????????????????
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}

		// ???????????????????????????
		List<Map> dbAccountInfoLst = daoUtil.queryForList(
				"select id, a_type, a_name, a_account, a_bank_or_img from otc_account_info where member_id = ? and a_type "
						+ accountTypeWhere,
				member.getId());
		if (null == dbAccountInfoLst || dbAccountInfoLst.size() == 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ACCOUNT_TYPE.getErrorENMsg(), null);
		}
		String accountInfo = "";
		for (Map object : dbAccountInfoLst) {
			accountInfo += "???" + object.get("a_type") + "???" + object.get("a_name") + "???" + object.get("a_account") + "???"
					+ object.get("a_bank_or_img");
		}

		// ??????????????????????????????
		AuthIdentity authIdentity = memberService.getAuthIdentityById(member.getId());
		if (null == authIdentity || authIdentity.getId_status() != 1) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_AUTH_IDENTITY_FIRST.getErrorENMsg(), null);
		}

		// ??????????????????
		Member oldMember = memberService.getMemberById(member.getId());
		if (HelpUtils.nullOrBlank(otcOrderReq.getSec_pwd())
				|| !MacMD5.CalcMD5Member(otcOrderReq.getSec_pwd()).equals(oldMember.getM_security_pwd())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg(), null);
		}

		// ???????????????????????????????????????
		if (otcOrderReq.getAd_type() == 1) {
			Integer waitNum = daoUtil.queryForInt(
					"select count(1) from m_coin_recharge where member_id = ? and currency = ? and r_status = -1"
					, member.getId(),ads.getBase_currency());
			// ???????????????????????????
			if (waitNum > 1) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_WAITING_ORDER_MAX_THAN_2.getErrorENMsg(), null);
			}
		}

		// ???????????????2??????????????????
		if (otcOrderReq.getAd_type() == 1) {
			Integer waitConfirm = daoUtil.queryForInt(
					"select count(1) from m_coin_recharge where member_id = ? and currency = ? and r_status = 0"
					, member.getId(),ads.getBase_currency());
			// ???????????????????????????
			if (waitConfirm > 1) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_WAITING_ORDER_CONFIRM_MAX_THAN_2.getErrorENMsg(), null);
			}
		}

		// ????????????????????????????????????
		/*
		 * Integer num = daoUtil.
		 * queryForInt("select count(1) from otc_account_info where member_id = ? and a_type = ? and a_info = ?"
		 * , member.getId(), otcOrderReq.getAccount_type(),
		 * otcOrderReq.getAccount_info()); if (num == 0) { daoUtil.
		 * update("insert into otc_account_info(member_id, a_type, a_info) values(?, ?, ?)"
		 * , member.getId(), otcOrderReq.getAccount_type(),
		 * otcOrderReq.getAccount_info()); }
		 */

		// ??????????????????
		if (otcOrderReq.getAd_type() == 1) {// ????????????????????????
			//????????????id???????????????????????????
//			daoUtil.update(
//					"update otc_ads set total_volume = total_volume + ?, total_amount = total_amount + ? where id = ? ",
//					otcOrderReq.getVolume(), ads.getPrice().multiply(otcOrderReq.getVolume()), ads.getId());
//			//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
			daoUtil.update(
					"update otc_ads set max_quote=max_quote-? where owner_id = ? and base_currency=?",
					otcOrderReq.getVolume(), ads.getOwner_id(),ads.getBase_currency());
		} else {// ???????????????????????? , ????????????????????????????????????
//			daoUtil.update(
//					"update otc_ads set total_volume = total_volume + ?, total_amount = total_amount + ? where id = ? ",
//					 otcOrderReq.getVolume(), ads.getPrice().multiply(otcOrderReq.getVolume()),
//					ads.getId());
//			daoUtil.update(
//					"update otc_ads set max_quote=max_quote+? where owner_id = ? and base_currency=?",
//					otcOrderReq.getVolume(), ads.getOwner_id(),ads.getBase_currency());
		}

		String jedisKey = ads.getBase_currency().toLowerCase() + ads.getQuote_currency().toLowerCase();
		// ?????????, ??????, ??????
		BigDecimal[] datas = { BigDecimal.valueOf(HelpUtils.getNowTimeStampMillisecond()), ads.getPrice(),
				otcOrderReq.getVolume() };
		JedisUtil.getInstance().lpush(jedisKey + "_OTC_trade", datas);
		JedisUtil.getInstance().ltrim(jedisKey + "_OTC_trade", 0, 10000);

		if (otcOrderReq.getAd_type() == 1) { // ?????????????????????
			CoinRecharge coinRecharge = new CoinRecharge();
			coinRecharge.setMember_id(member.getId());
			coinRecharge.setR_amount(otcOrderReq.getVolume());
			coinRecharge.setR_address(accountInfo);
			coinRecharge.setR_txid(member.getId() + "-" + System.currentTimeMillis() + HelpUtils.randomString(10));
			coinRecharge.setCurrency(ads.getBase_currency());
			coinRecharge.setCurrency_id(2); // ZC???d_currency?????????id???2????????????2
			coinRecharge.setR_confirmations("1");
			coinRecharge.setR_create_time(HelpUtils.formatDate8(new Date()));
			coinRecharge.setR_status(-1); // ???????????????
			coinRecharge.setOtc_ads_id(ads.getId());
			coinRecharge.setOtc_money(ads.getPrice().multiply(otcOrderReq.getVolume()));
			coinRecharge.setOtc_oppsite_currency(ads.getQuote_currency());
			coinRecharge.setOtc_owner_name(ads.getO_name());
			coinRecharge.setOtc_price(ads.getPrice());

			memberService.addCoinRechargeByMember(coinRecharge);

			// TODO ??????????????????????????????????????????, ???????????????????????????
			System.err.println(maxQuote.subtract(totalVolume).subtract(volume).compareTo(minQuote));
			if (maxQuote.subtract(volume).compareTo(minQuote) == -1) {
				System.err.println("??????????????????!");
				Map<String, Object> paramMap = new HashMap<String, Object>(2);
				paramMap.put("status", 0);
				paramMap.put("id", ads.getId());
				otcMapper.updateStatus4Ads(paramMap);
			}
		} else { // ?????????????????????
			WithdrawCreateDtoInner withdrawCreateDto = new WithdrawCreateDtoInner();
			withdrawCreateDto.setCurrency(ads.getQuote_currency());
			withdrawCreateDto.setAmount(otcOrderReq.getVolume().multiply(ads.getPrice()));
			withdrawCreateDto.setAddr(accountInfo);

			addWithdraw(ads, withdrawCreateDto, 0, withdrawCreateDto.getAddr(),
					withdrawCreateDto.getMember_coin_addr_label(), HelpUtils.getIpAddr(request), member.getId());
		}
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}

	@ApiOperation(value = "OTC???????????????????????????????????????", notes = "??????OTC??????????????????????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/confirmorder", method = RequestMethod.POST)
	@ResponseBody
	public Resp confirmOTCOrder(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OTCOrderConfirmReq otcOrderConfirmReq) throws Exception {
		// ????????????????????????
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

		// ?????????????????????????????????????????????????????????1????????????????????????
		daoUtil.update("update m_coin_recharge set r_status = 0 where id = ?", id);

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}
	
	
	@ApiOperation(value = "OTC??????????????????????????????????????????", notes = "??????OTC??????????????????????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "otc/confirmGathering", method = RequestMethod.POST)
	@ResponseBody
	public Resp confirmOTCGathering(HttpServletRequest request, HttpServletResponse response,
			@RequestBody OTCOrderConfirmReq otcOrderConfirmReq) throws Exception {
		// ????????????????????????
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
		} 

		Long id = otcOrderConfirmReq.getId();
		CoinWithdraw coinWithdraw = memberService.getCoinWithdraw(id);
		if (null == coinWithdraw || coinWithdraw.getMember_id().intValue() != member.getId().intValue()) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ORDER_NOT_EXISTS.getErrorENMsg());
		}
		
		// ???????????????????????????
		daoUtil.update("update m_coin_withdraw set w_status = 1 where id = ?", id);
		//??????????????????
		memberService.confirmGathering(id);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

}
