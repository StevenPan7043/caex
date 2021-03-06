package com.pmzhongguo.otc.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.core.sms.InterSmsServiceImpl;
import com.pmzhongguo.ex.core.web.*;
import com.google.common.base.Joiner;
import com.pmzhongguo.ex.core.sms.ISmsService;
import com.pmzhongguo.otc.otcenum.*;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.otc.otcenum.*;
import com.pmzhongguo.otc.sms.JuheSend;
import com.qiniu.util.BeanUtil;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.utils.PropertiesUtil;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.entity.dto.AccountInfoDTO;
import com.pmzhongguo.otc.entity.dto.MerchantDTO;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.service.MerchantManager;
import com.pmzhongguo.otc.service.OTCAccountInfoManager;
import com.pmzhongguo.otc.service.OTCTradeManager;
import com.pmzhongguo.otc.sms.SmsSendPool;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@Api(value = "NEW OTC??????", description = "????????????", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("otc/trade")
public class OTCTradeController extends TopController {

	@Autowired
	private OTCTradeManager oTCTradeManager;

	@Autowired
	private OTCAccountInfoManager oTCAccountInfoManager;

	@Autowired
	private MerchantManager merchantManager;

	@Autowired
	private DaoUtil daoUtil;

//	@ApiOperation(value = "????????????", notes = "?????????????????????ID??? id   ???????????????Id:acountId", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "pay/{id}/{acountId}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjResp pay(HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int acountId) {

		ObjResp resp = validateTrade(request, id);

		if(resp.getState().intValue() == Resp.FAIL.intValue()) {
			return resp;
		}
		OTCTradeDTO oTCTradeDTO = (OTCTradeDTO) resp.getData();
		//??????????????????????????????????????????????????????
		if(oTCTradeDTO.gettType().getType() != OrderTypeEnum.BUY.getType() || oTCTradeDTO.getStatus().getType() != TradeStatusEnum.NP.getType()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERTION.getErrorENMsg(), null);
		}
		resp = oTCTradeManager.pay(id, acountId);
		try {
			if(resp.getState().intValue() == Resp.SUCCESS.intValue()) {
				String query = "SELECT t.`t_number`, m.`m_name` FROM `o_trade` t LEFT JOIN o_order o ON o.id = t.`opposite_o_id`  LEFT JOIN m_member m ON m.`id` = o.`member_id` WHERE t.id = ?";
				Map<String ,Object> map = daoUtil.queryForMap(query, id);
				Member m = JedisUtilMember.getInstance().getMember(request, null);
				String m_name = String.valueOf(map.get("m_name"));
				// ????????????????????????
				ISmsService smsService= null;
				if (BeanUtil.isEmpty(m.getArea_code()) || m.getArea_code().equals(AreaCodeEnum.CH.getAreaCode())) {
//					smsService = new ChuangLanSmsServiceImpl()
//							.builder(m_name, Joiner.on(",").join(Arrays.asList(m.getM_nick_name(), String.valueOf(map.get("t_number"))
//							)), AbstractChuangLanSmsService.CLT_PAY_MEMBER_ORDER);

					//???????????????
					String format = String.format(MobiInfoTemplateEnum.JH_TRADE_PAID.getCode(), m.getM_nick_name(), map.get("t_number"));
					SmsSendPool.getInstance().send(new JuheSend(m_name, MobiInfoTemplateEnum.JH_TRADE_PAID.getType(), format));
				}
//				else {
//					smsService = new InterSmsServiceImpl()
//							.builder(m.getM_nick_name(), String.valueOf(map.get("t_number")),m_name, InternationalSmsCNEnum.CLT_PAY_MEMBER_ORDER.getType());
//				}

//				SmsSendPool.getInstance().send(smsService);
			}
		}catch(Exception e) {
            logger.warn("?????????????????????{}",e.toString());
            e.printStackTrace();
		}

		return resp;
	}

//	@ApiOperation(value = "????????????", notes = "?????????????????????ID:id", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "confirmed/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjResp confirmedTrade(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {

		ObjResp resp = validateTrade(request, id);

		if(resp.getState().intValue() == Resp.FAIL.intValue()) {
			return resp;
		}
		OTCTradeDTO oTCTradeDTO = (OTCTradeDTO) resp.getData();

		//??????????????????????????????????????????????????????
		if(oTCTradeDTO.gettType().getType() != OrderTypeEnum.SELL.getType() || oTCTradeDTO.getStatus().getType() != TradeStatusEnum.UNCONFIRMED.getType()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg(), null);
		}
		resp = oTCTradeManager.confirmedTrade(id);
		try {
			if(resp.getState().intValue() == Resp.SUCCESS.intValue()) {
				String query = "SELECT t.`base_currency`, m.`m_name` FROM `o_trade` t LEFT JOIN o_order o ON o.id = t.`opposite_o_id`  LEFT JOIN m_member m ON m.`id` = o.`member_id` WHERE t.id = ?";
				Map<String ,Object> map = daoUtil.queryForMap(query, id);
				Member m = JedisUtilMember.getInstance().getMember(request, null);

				String m_name = String.valueOf(map.get("m_name"));
				// ???????????????????????????
				ISmsService smsService = null;
				if (BeanUtil.isEmpty(m.getArea_code()) || m.getArea_code().equals(AreaCodeEnum.CH.getAreaCode())) {
//					smsService = new ChuangLanSmsServiceImpl()
//							.builder(m_name, Joiner.on(",").join(Arrays.asList(m.getM_nick_name(), String.valueOf(map.get("base_currency")))
//							), AbstractChuangLanSmsService.CLT_MEMBER_CURRENCY);

					//???????????????
					String format = String.format(MobiInfoTemplateEnum.JH_TRADE_CONFIRMED.getCode(), m.getM_nick_name(), map.get("base_currency"));
					SmsSendPool.getInstance().send(new JuheSend(m_name, MobiInfoTemplateEnum.JH_TRADE_CONFIRMED.getType(), format));
				}
//				else {
//					smsService = new InterSmsServiceImpl()
//							.builder(m.getM_nick_name(), String.valueOf(map.get("base_currency")), m_name,InternationalSmsCNEnum.CLT_MEMBER_CURRENCY.getType());
//				}
//				SmsSendPool.getInstance().send(smsService);
			}
		}catch(Exception e) {
            logger.warn("?????????????????????{}",e.toString());
            e.printStackTrace();
		}

		return resp;
	}

	@ApiOperation(value = "????????????", notes = "?????????????????????ID??? memo:????????????", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "cancel/{id}/{memo}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjResp cancelTrade(HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable String memo) {

		ObjResp resp = validateTrade(request, id);

		if(resp.getState().intValue() == Resp.FAIL.intValue()) {
			return resp;
		}
		OTCTradeDTO oTCTradeDTO = (OTCTradeDTO) resp.getData();

		if(oTCTradeDTO.gettType().getType() == OrderTypeEnum.SELL.getType()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERTION.getErrorENMsg(), null);
		}
		if(oTCTradeDTO.getStatus().getType() == TradeStatusEnum.DONE.getType() || oTCTradeDTO.getStatus().getType() == TradeStatusEnum.CANCELED.getType()) {
		    logger.warn("The trade " + oTCTradeDTO.getStatus().getCode() + "  ??????id???" + id);
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ORDER_IS_DONE_OR_CANCELED.getErrorENMsg(), null);
		}
		resp = oTCTradeManager.cancelTrade(id, memo);
		try {
			if (resp.getState().intValue() == Resp.SUCCESS.intValue()) {
				String query = "SELECT t.`t_number`, m.`m_name` FROM `o_trade` t LEFT JOIN o_order o ON o.id = t.`opposite_o_id`  LEFT JOIN m_member m ON m.`id` = o.`member_id` WHERE t.id = ?";
				Map<String, Object> map = daoUtil.queryForMap(query, id);
				Member m = JedisUtilMember.getInstance().getMember(request, null);

				String m_name = String.valueOf(map.get("m_name"));
				// ???????????????????????????
				ISmsService smsService = null;

				if (BeanUtil.isEmpty(m.getArea_code()) || m.getArea_code().equals(AreaCodeEnum.CH.getAreaCode())) {
//					smsService = new ChuangLanSmsServiceImpl()
//							.builder(m_name, Joiner.on(",").join(Arrays.asList(m.getM_nick_name(), String.valueOf(map.get("t_number")))
//							), AbstractChuangLanSmsService.CLT_CANCEL_MEMBER_ORDER);
//
					//???????????????
					String format = String.format(MobiInfoTemplateEnum.JH_CANCELED_TRADE.getCode(), m.getM_nick_name(), map.get("t_number"));
					SmsSendPool.getInstance().send(new JuheSend(m_name, MobiInfoTemplateEnum.JH_CANCELED_TRADE.getType(), format));
				}
//				else {
//					smsService = new InterSmsServiceImpl()
//							.builder(m.getM_nick_name(), String.valueOf(map.get("t_number")),m_name
//							, InternationalSmsCNEnum.CLT_CANCEL_MEMBER_ORDER.getType());
//				}
//				SmsSendPool.getInstance().send(smsService);
			}
		}catch(Exception e) {
            logger.warn("?????????????????????{}",e.toString());
            e.printStackTrace();
		}
		return resp;
	}

//	@ApiOperation(value = "????????????", notes = "?????????????????????ID??? id  ????????????:memo", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "complain/{id}/{complainType}/{memo}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjResp complain(HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable String memo, @PathVariable ComplainTypeEnum complainType) {

		ObjResp resp = validateTrade(request, id);

		if(resp.getState().intValue() == Resp.FAIL.intValue()) {
			return resp;
		}
		OTCTradeDTO oTCTradeDTO = (OTCTradeDTO) resp.getData();

		if(oTCTradeDTO.getStatus().getType() == TradeStatusEnum.DONE.getType() || oTCTradeDTO.getStatus().getType() == TradeStatusEnum.CANCELED.getType()) {
			return new ObjResp(Resp.FAIL, "The trade " + oTCTradeDTO.getStatus().getCode(), null);
		}
		OTCTradeDTO complainDto = new OTCTradeDTO();
		complainDto.setId(id);
		complainDto.setStatus(TradeStatusEnum.COMPLAINING);
		complainDto.setMemo(memo);
		complainDto.setComplainType(complainType);
		int count = oTCTradeManager.complain(complainDto);
		try {
			if(count == 1) {
				String query = "SELECT t.`t_number`, m.`m_name` FROM `o_trade` t LEFT JOIN o_order o ON o.id = t.`opposite_o_id`  LEFT JOIN m_member m ON m.`id` = o.`member_id` WHERE t.id = ?";
				Map<String ,Object> map = daoUtil.queryForMap(query, id);

				Member m = JedisUtilMember.getInstance().getMember(request, null);

				String smsContent = Joiner.on(",").join(Arrays.asList(m.getM_nick_name(),String.valueOf(map.get("t_number"))));
				String m_name = String.valueOf(map.get("m_name"));

				// ???????????? ????????????
				if (BeanUtil.isEmpty(m.getArea_code()) || m.getArea_code().equals(AreaCodeEnum.CH.getAreaCode())){
//					ISmsService oppositeSms = new ChuangLanSmsServiceImpl().builder(m_name
//							, smsContent, AbstractChuangLanSmsService.CLT_ZZEXAPPEAL_MEMBER_ORDER);
//					SmsSendPool.getInstance().send(oppositeSms);

					//???????????????
					String format = String.format(MobiInfoTemplateEnum.JH_COMPLAINING_TRADE.getCode(), m.getM_nick_name(),map.get("t_number"));
					SmsSendPool.getInstance().send(new JuheSend(m_name, MobiInfoTemplateEnum.JH_COMPLAINING_TRADE.getType(), format));

					// ????????????????????????
//					ISmsService smsService = new ChuangLanSmsServiceImpl()
//							.builder(PropertiesUtil.getPropValByKey("otc_sms_warn").trim()
//									, smsContent, AbstractChuangLanSmsService.CLT_MEMBERAPPEAL_MEMBER_ORDER);
//					SmsSendPool.getInstance().send(smsService);

					//???????????????
					format = String.format(MobiInfoTemplateEnum.JH_COMPLAINING_TRADE_WARN.getCode(), m.getM_nick_name(), map.get("t_number"));
					SmsSendPool.getInstance().send(new JuheSend(PropertiesUtil.getPropValByKey("otc_sms_warn").trim(), MobiInfoTemplateEnum.JH_COMPLAINING_TRADE_WARN.getType(), format));
				}
//				else {
//					ISmsService oppositeSms = new InterSmsServiceImpl().builder(m.getM_nick_name(),String.valueOf(map.get("t_number")),m_name, InternationalSmsCNEnum.CLT_ZZEXAPPEAL_MEMBER_ORDER.getType());
//					SmsSendPool.getInstance().send(oppositeSms);
//
//					// ????????????????????????
//					ISmsService smsService = new InterSmsServiceImpl()
//							.builder(m.getM_nick_name(),String.valueOf(map.get("t_number")),PropertiesUtil.getPropValByKey("otc_sms_warn").trim(), InternationalSmsCNEnum.CLT_MEMBERAPPEAL_MEMBER_ORDER.getType());
//					SmsSendPool.getInstance().send(smsService);
//				}


			}
		}catch(Exception e) {
            logger.warn("?????????????????????{}",e.toString());
            e.printStackTrace();
		}
		return count == 1 ? new ObjResp() : new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorENMsg(), "");
	}



//	@ApiOperation(value = "????????????", notes = "?????????????????????ID:id", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiIgnore
	@RequestMapping(value = "cancelComplain/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public ObjResp cancelComplain(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		OTCTradeDTO oTCTradeDTO = oTCTradeManager.findById(id);
		if(oTCTradeDTO == null) {
			return new ObjResp(Resp.FAIL, "No trade was found!", null);
		}
		if(oTCTradeDTO.getMemberId().intValue() != m.getId().intValue()) {
			return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
		}
		if(oTCTradeDTO.getStatus().getType() != TradeStatusEnum.COMPLAINING.getType()) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERTION.getErrorENMsg(), null);
		}

		oTCTradeManager.complainAudit(id, "0", null);
		return new ObjResp();
	}

	@ApiOperation(value = "??????????????????", notes = "?????????????????????ID??? id  <br/> ????????????: ????????????????????????:selfAccount  ????????????????????????:oppositeAccount ", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "query/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp query(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}

		//??????????????????????????????
		OTCTradeDTO selfTrade = oTCTradeManager.findById(id);
		if(selfTrade == null || selfTrade.getMemberId().intValue() != m.getId().intValue()) {
			return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
		}
		//?????????????????????????????????????????????????????????????????????????????????
		Integer accountId = null;
		OTCTradeDTO oppositeTrade = oTCTradeManager.findById(selfTrade.getOppositeTId());
		if (selfTrade.getAcountId() != null) {
			accountId = selfTrade.getAcountId();
		} else if (oppositeTrade.getAcountId() != null) {
			accountId = oppositeTrade.getAcountId();
		}
		if (!StringUtils.isEmpty(oppositeTrade.getStatus().getCode())) {
			selfTrade.setOppositeStatus(oppositeTrade.getStatus().getCode());
			selfTrade.setMemoStr(StringUtils.isEmpty(selfTrade.getMemo()) ? oppositeTrade.getMemo() : selfTrade.getMemo());
		}
		validateAppeal(selfTrade);
		validateAppeal(oppositeTrade);
		if (accountId != null) {
			AccountInfoDTO selfAccountInfoDTO = oTCAccountInfoManager.findById(accountId);
			selfTrade.setSelfAccount(selfAccountInfoDTO);
			List<AccountInfoDTO> byConditionPage = oTCAccountInfoManager
					.findByConditionPage(HelpUtils.newHashMap("memberId", selfTrade.getMemberId(),
							"type", selfAccountInfoDTO.getType().getType(), "isDelete", 0));
			if (!CollectionUtils.isEmpty(byConditionPage)) {
				selfTrade.setSelfAccount(byConditionPage.get(0));
			}
			List<AccountInfoDTO> byCondition = oTCAccountInfoManager.findByConditionPage(HelpUtils.newHashMap("memberId", oppositeTrade.getMemberId(), "type", selfAccountInfoDTO.getType().getType(), "isDelete", 0));
			if (!CollectionUtils.isEmpty(byCondition)) {
				selfTrade.setOppositeAccount(byCondition.get(0));
			}
		}


		ObjResp resp = new ObjResp();

		//??????????????????
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("selfTrade", selfTrade);

		//??????????????????
		Member oppoMember = memberService.getMemberById(oppositeTrade.getMemberId());
		Map<String, Object> oppositeInfo = new HashMap<String, Object>();
		oppositeInfo.put("memberId", oppoMember.getId());
		oppositeInfo.put("m_nick_name", oppoMember.getM_nick_name());
		//????????????????????????
		MerchantDTO merchantDTO = merchantManager.getMerchant(oppositeTrade.getMemberId());
		if(merchantDTO != null) {
			oppositeInfo.put("depositCurrency", merchantDTO.getDepositCurrency());
			oppositeInfo.put("depositVolume", merchantDTO.getDepositVolume());
		}
		resultMap.put("oppositeInfo", oppositeInfo);
		// ????????????????????????
		Map<String,String> phoneObj = new HashMap<>();
		Member selfMember = memberService.getMemberById(selfTrade.getMemberId());
		phoneObj.put("selfPhone", selfMember.getM_name().contains("@") ? selfMember.getPhone() : selfMember.getM_name());
		phoneObj.put("oppositePhone",oppoMember.getM_name().contains("@") ? oppoMember.getPhone() : oppoMember.getM_name());
		resultMap.put("phones",phoneObj);
		resp.setData(resultMap);
		return resp;
	}

	//??????????????????
	private void validateAppeal(OTCTradeDTO otcTradeDTO) {
		if (StringUtils.isNotBlank(otcTradeDTO.getCreateTime()) && !BeanUtil.isEmpty(otcTradeDTO.getPaymentTime())) {
			long hms = DateUtil.getHMS(new Date(), DateUtil.stringToDate(otcTradeDTO.getCreateTime(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
			if (hms >= 0 && hms <= (otcTradeDTO.getPaymentTime() * 60 * 1000)) {
				otcTradeDTO.setAppealFlag(false);
			} else {
				otcTradeDTO.setAppealFlag(true);
			}
		}
	}


	@ApiOperation(value = "????????????????????????", notes = "??????????????? ????????????:tType(BUY/SELL) ????????????:status(NP|UNCONFIRMED|DONE|CANCELED|COMPLAINING) ???????????????:sortname   ????????????:sortorder asc|desc(??????|??????)  ?????????:page(???1??????)  ???????????????:pagesize", httpMethod = "GET")
	@RequestMapping(value = "queryTradePage", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp queryTradePage(HttpServletRequest request, HttpServletResponse response) {
		ObjResp resp = new ObjResp();

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}

		try {
			Map<String, Object> param = $params(request);
			param.put("memberId", m.getId());
			String sortname = String.valueOf(param.get("sortname"));
			if(HelpUtils.nullOrBlank(sortname)) {
				param.put("sortname", "create_time");
				param.put("sortorder", "desc");
			}
			List<OTCTradeDTO> list = oTCTradeManager.findByConditionPage(param);
			resp.setData(HelpUtils.newHashMap("rows", list, "total", param.get("total")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setState(Resp.FAIL);
			resp.setMsg("fail");
		}
		return resp;
	}

	/**
	 * 	???queryTradePage????????????status???????????????status in (0, 1)
	 * @param
	 * @return
	 */
	@ApiOperation(value = "???????????????????????????", notes = "??????????????? ????????????:tType(BUY/SELL) ???????????????:sortname   ????????????:sortorder asc|desc(??????|??????)  ?????????:page(???1??????)  ???????????????:pagesize", httpMethod = "GET")
	@RequestMapping(value = "queryTradingPage", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp queryTradingPage(HttpServletRequest request, HttpServletResponse response) {
		ObjResp resp = new ObjResp();

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}

		try {
			Map<String, Object> param = $params(request);
			param.put("memberId", m.getId());
			String sortname = String.valueOf(param.get("sortname"));
			if(HelpUtils.nullOrBlank(sortname)) {
				param.put("sortname", "create_time");
				param.put("sortorder", "desc");
			}
            param.put("status", 5);
			List<OTCTradeDTO> list = oTCTradeManager.findBytradingPage(param);
			resp.setData(HelpUtils.newHashMap("rows", list, "total", param.get("total")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setState(Resp.FAIL);
			resp.setMsg("fail");
		}
		return resp;
	}
	@ApiOperation(value = "???????????????????????????", notes = "??????????????? ????????????:tType(BUY/SELL) ???????????????:sortname   ????????????:sortorder asc|desc(??????|??????)  ?????????:page(???1??????)  ???????????????:pagesize", httpMethod = "GET")
	@RequestMapping(value = "queryComplainingPage", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp queryComplainingPage(HttpServletRequest request, HttpServletResponse response) {
		ObjResp resp = new ObjResp();

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}

		try {
			Map<String, Object> param = $params(request);
			param.put("memberId", m.getId());
			String sortname = String.valueOf(param.get("sortname"));
			if(HelpUtils.nullOrBlank(sortname)) {
				param.put("sortname", "create_time");
				param.put("sortorder", "desc");
			}
			param.put("status", 5);
			List<OTCTradeDTO> list = oTCTradeManager.findByComplainTradePage(param);
			resp.setData(HelpUtils.newHashMap("rows", list, "total", param.get("total")));
		} catch (Exception e) {
			// TODO Auto-generated catch block
            e.printStackTrace();
            resp.setState(Resp.FAIL);
			resp.setMsg("fail");
		}
		return resp;
	}
	@ApiOperation(value = "????????????????????????", notes = "?????????????????????????????????  ????????????<br/> ????????????:totalDoneTrade <br/> 30????????????:done_30 <br/> ??????????????????:totalComplainTrade <br/> ??????????????????:consumingTime(???????????????)", httpMethod = "GET")
	@RequestMapping(value = "queryMerchantTradeInfo/{memberId}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp queryMerchantTradeInfo(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer memberId) {
		return oTCTradeManager.queryMerchantTradeInfo(memberId);
	}

	@ApiIgnore
	@RequestMapping(value = "complainAudit", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp complainAudit(HttpServletRequest request, HttpServletResponse response) {
		int id = $int("id");
		String opera = $("opera");
		String memo_result = $("memo_result");
		ObjResp resp = oTCTradeManager.complainAudit(id, opera, memo_result);
		return resp;
	}


	@ApiOperation(value = "??????????????????????????????????????????", notes = "??????????????? ??????Id:id  ????????????????????????????????????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "getOrderAccountInfo/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp get(HttpServletRequest request, @PathVariable Integer id) {

		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		OTCTradeDTO oTCTradeDTO = oTCTradeManager.findById(id);

		if(oTCTradeDTO == null) {
			return new ObjResp(Resp.FAIL, "No trade was found!", null);
		}
		if(oTCTradeDTO.getMemberId().intValue() != m.getId().intValue()) {
			return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
		}
		ObjResp resp = oTCTradeManager.getOrderAccountInfo(oTCTradeDTO);
		return resp;
	}

	private ObjResp validateTrade(HttpServletRequest request, Integer tradeId) {
		Member m = JedisUtilMember.getInstance().getMember(request, null);
		if (null == m) {
			return new ObjResp(Resp.FAIL, "LANG_NO_LOGIN", null);
		}
		OTCTradeDTO oTCTradeDTO = oTCTradeManager.findById(tradeId);
		if(oTCTradeDTO == null) {
			return new ObjResp(Resp.FAIL, "No trade was found!", null);
		}
		if(oTCTradeDTO.getMemberId().intValue() != m.getId().intValue()) {
			return new ObjResp(Resp.FAIL, "LANG_ERROR_OPERATION_OBJECT", null);
		}
		if(oTCTradeDTO.getStatus().getType() == TradeStatusEnum.COMPLAINING.getType()) {
			return new ObjResp(Resp.FAIL, "TRADE_COMPLAINING", null);
		}
		OTCTradeDTO opposite = oTCTradeManager.findById(oTCTradeDTO.getOppositeTId());
		if(opposite.getStatus().getType() == TradeStatusEnum.COMPLAINING.getType()) {
			return new ObjResp(Resp.FAIL, "TRADE_COMPLAINING", null);
		}
		ObjResp resp = new ObjResp();
		resp.setData(oTCTradeDTO);
		return resp;
	}
}
