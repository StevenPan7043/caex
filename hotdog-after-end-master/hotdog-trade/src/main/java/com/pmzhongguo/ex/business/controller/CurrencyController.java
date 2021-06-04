package com.pmzhongguo.ex.business.controller;

import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.business.dto.CurrencyVerticalDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyIntroduce;
import com.pmzhongguo.ex.business.service.*;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.*;

import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.resp.Resp;

@ApiIgnore
@Controller
@RequestMapping("backstage/currency")
public class CurrencyController extends TopController {
	@Resource
	private CurrencyService currencyService;

	@Resource
	private CurrencyVerticalService currencyVerticalService;

	@Autowired
	private CurrencyIntroduceService introduceService;

	@Autowired
	private TradeRankingService rankingService;

	/**
	 * 交易对列表界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "currency_pair_list", method = RequestMethod.GET)
	public String toListCurrencyPair(HttpServletRequest request,
			HttpServletResponse response) {

		return "business/currency/currency_pair_list";
	}
	
	/**
	 * 交易对增加界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "currency_pair_add", method = RequestMethod.GET)
	public String toAddCurrencyPair(HttpServletRequest request,
			HttpServletResponse response) {
		return "business/currency/currency_pair_edit";
	}
	
	/**
	 * 交易对编辑界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "currency_pair_edit", method = RequestMethod.GET)
	public String toEditCurrencyPair(HttpServletRequest request,
			HttpServletResponse response) {
		CurrencyPair info = currencyService.getCurrencyPair($int("id"));
		$attr("info", info);
		return "business/currency/currency_pair_edit";
	}

	
	/**
	 * 交易对列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "currency_pair", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listCurrencyPair(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		if (!HelpUtils.nullOrBlank(param.get("keyword"))) {
			param.put("keyword", param.get("keyword").toString().toUpperCase());
		}
		param.put("sortorder", "asc,desc");
		param.put("sortname", "area_id,p_order");
		List<CurrencyPair> list = currencyService.getAllCurrencyPair(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 交易对编辑
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping(value = "currency_pair_edit_do", method = RequestMethod.POST)
	@ResponseBody
	public Resp editCurrencyPair(HttpServletRequest request, CurrencyPair currencyPair) {
		ObjResp objResp = currencyService.editCurrencyPair(currencyPair);
		if (!objResp.getState().equals(Resp.SUCCESS)) {
			return objResp;
		}
		// 刷新缓存
		currencyService.cacheCurrencyPair(request.getServletContext());
		// zk同步
		// 同步
		return syncCurrencyPair(request);
	}

	/**
	 * 交易对新增
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping(value = "currency_pair_add_do", method = RequestMethod.POST)
	@ResponseBody
	public Resp addCurrencyPair(HttpServletRequest request, CurrencyPair currencyPair) {
		if (currencyPair.getBase_currency().equals(currencyPair.getQuote_currency())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.BASE_AND_QUOTE_CURRENCY_NOT_CONSISTENT.getErrorCNMsg(), null);
		}

		ObjResp objResp = currencyService.addCurrencyPair(currencyPair);
		if (!objResp.equals(Resp.SUCCESS)) {
			return objResp;
		}
		// 刷新缓存
		currencyService.cacheCurrencyPair(request.getServletContext());
		//刷新redis
//		currencyService.cacheCurrencyPair();
		// 同步
		return syncCurrencyPair(request);
	}
	
	
	/**
	 * 虚拟币数据列表界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "currency_list", method = RequestMethod.GET)
	public String toListCurrency(HttpServletRequest request,
			HttpServletResponse response) {

		return "business/currency/currency_list";
	}
	
	/**
	 * 虚拟币新增界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "currency_add", method = RequestMethod.GET)
	public String toAddCurrency(HttpServletRequest request,
			HttpServletResponse response) {
		return "business/currency/currency_edit";
	}
	
	/**
	 * 虚拟币编辑界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "currency_edit", method = RequestMethod.GET)
	public String toEditCurrency(HttpServletRequest request,
								 HttpServletResponse response) {
		Currency info = currencyService.getCurrency($int("id"));
		CurrencyVerticalDto currencyVerticalDto = new CurrencyVerticalDto();
		currencyVerticalDto.setCurrencyId($int("id"));
		if (info.getCurrency().equals("USDT".toUpperCase())) {
			List<CurrencyVerticalDto> currencyVerticalList = currencyVerticalService.getCurrencyVerticalList(currencyVerticalDto);
			if (!CollectionUtils.isEmpty(currencyVerticalList)) {
				for (CurrencyVerticalDto c : currencyVerticalList) {
					$attr(info.getCurrency().toLowerCase() + "_" +c.getColumn()+"_" + c.getCategoryKey(), c.getValue());
				}
			}
		}
		$attr("info", info);
		return "business/currency/currency_edit";
	}

	/**
	 * 虚拟币列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "currency", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listCurrency(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		if (!HelpUtils.nullOrBlank(param.get("keyword"))) {
			param.put("keyword", param.get("keyword").toString().toUpperCase());
		}
		List<Currency> list = currencyService.getAllCurrency(param);
		List<Map> currencylist = new ArrayList<>();
		for (Currency currency:list) {
			Map currencyMap = new HashMap();
			Integer isLock = RuleServiceManage.getCurrencyRuleByCache(currency.getCurrency()) == null ? 0 : 1;
			currencyMap.put("currency",currency.getCurrency());
			currencyMap.put("currency_name",currency.getCurrency_name());
			currencyMap.put("is_otc",currency.getIs_otc());
			currencyMap.put("is_coin",currency.getIs_coin());
			currencyMap.put("can_recharge",currency.getCan_recharge());
			currencyMap.put("can_withdraw",currency.getCan_withdraw());
			currencyMap.put("is_show",currency.getIs_show());
			currencyMap.put("is_lock",isLock);
			currencyMap.put("c_precision",currency.getC_precision());
			currencyMap.put("c_order",currency.getC_order());
			currencyMap.put("num",currency.getNum());
			currencyMap.put("createNum",currency.getCreateNum());
			currencyMap.put("guijiCron",currency.getGuijiCron());
			currencyMap.put("rechargeCron",currency.getRechargeCron());
			currencyMap.put("status",currency.getStatus());
			currencyMap.put("id",currency.getId());
			currencyMap.put("can_internal_transfer",currency.getCan_internal_transfer());
			currencylist.add(currencyMap);
		}
		Map map = new HashMap();
		map.put("Rows", currencylist);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 虚拟币编辑
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping(value = "currency_edit_do", method = RequestMethod.POST)
	@ResponseBody
	public Resp editCurrency(HttpServletRequest request, Currency currency) {
		// currency_name 始终等于 currency
		try {
			currency.setCurrency_name(currency.getCurrency());
			if (currency.getCurrency().equalsIgnoreCase("USDT")) {
				List<CurrencyVerticalDto> cvdlist = new ArrayList<>();
				//新增 recharge——ERC20
				addCvd(currency.getId(), "recharge", ChainTypeEnum.ERC20.getType(), cvdlist,currency.getUsdtRechargeERC20()+"");

				//新增 withdraw——ERC20
				addCvd(currency.getId(), "withdraw", ChainTypeEnum.ERC20.getType(), cvdlist,currency.getUsdtWithdrawERC20()+"");

				//新增 recharge——OMNI
				addCvd(currency.getId(), "recharge", ChainTypeEnum.OMNI.getType(), cvdlist,currency.getUsdtRechargeOMNI()+"");

				//新增 withdraw——OMNI
				addCvd(currency.getId(), "withdraw", ChainTypeEnum.OMNI.getType(), cvdlist,currency.getUsdtWithdrawOMNI()+"");
				currencyVerticalService.editCurrencyVertical(cvdlist, currency);
				if (currency.getUsdtRechargeERC20() == 0 && currency.getUsdtRechargeOMNI() == 0) {
					currency.setCan_recharge(0);
				}
				if (currency.getUsdtRechargeERC20() == 1 || currency.getUsdtRechargeOMNI() == 1) {
					currency.setCan_recharge(1);
				}
				if (currency.getUsdtWithdrawERC20() == 0 && currency.getUsdtWithdrawOMNI() == 0) {
					currency.setCan_withdraw(0);
				}
				if (currency.getUsdtWithdrawERC20() == 1 || currency.getUsdtWithdrawOMNI() == 1) {
					currency.setCan_withdraw(1);
				}
			}
			currencyService.editCurrency(currency);
			JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_CURRENCY, JedisChannelConst.SYNC_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
			return new Resp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg());
		}
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	private void addCvd(Integer currencyId, String type, String categoryKey, List<CurrencyVerticalDto> cvdlist,String value) {
		CurrencyVerticalDto cvd = new CurrencyVerticalDto();
		cvd.setCurrencyId(currencyId);
		cvd.setColumn(type);
		cvd.setValue(value);
		cvd.setCategoryKey(categoryKey);
		cvdlist.add(cvd);
	}
	/**
	 * 虚拟币新增
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping(value = "currency_add_do", method = RequestMethod.POST)
	@ResponseBody
	public Resp addCurrency(HttpServletRequest request, Currency currency) {
		// currency_name 始终等于 currency
		currency.setCurrency_name(currency.getCurrency().toUpperCase());
		currency.setCurrency(currency.getCurrency().toUpperCase());
		currencyService.addCurrency(currency);
		// 刷新缓存
		currencyService.cacheCurrency(request.getServletContext());
		// 插入币的排名记录
		rankingService.insertCurrencyRank(currency);
		// 同步
		syncCurrency(request);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 去币种资料页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "currency_introduce", method = RequestMethod.GET)
	public String toCurrencyIntroduce(HttpServletRequest request,
								 HttpServletResponse response) {

		return "business/currency/currency_introduce";
	}

	/**
	 * 获取数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "currency_introduce/list", method = RequestMethod.GET)
	@ResponseBody
	public Map getCurrencyIntroduceList(HttpServletRequest request) {
		Map param = $params(request);
		List<CurrencyIntroduce> list = introduceService.getAllCurrencyIntroduce(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 编辑页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "currency_introduce/edit", method = RequestMethod.GET)
	public String toCurrencyIntroduceEdit(HttpServletRequest request) {
		String id = request.getParameter("id");
		if(!StringUtils.isBlank(id)) {
			CurrencyIntroduce introduce = introduceService.getCurrencyIntroduce(Integer.parseInt(id));
			$attr("info", introduce);
		}
		return "business/currency/currency_introduce_edit";
	}

	/**
	 * 添加/更新
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "currency_introduce/add", method = RequestMethod.POST)
	@ResponseBody
	public Resp currencyIntroduceAdd(HttpServletRequest request) {
		Map<String, Object> params = $params(request);
		String id = request.getParameter("id");
		if(StringUtils.isBlank(id)) {
			introduceService.addCurrencyIntroduce(params);
		}else {
			introduceService.updateCurrencyIntroduce(params);
		}
		return new Resp(Resp.SUCCESS,Resp.SUCCESS_MSG);
	}

	/**
	 * zookeeper 同步交易币种
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sync/currency", method = RequestMethod.POST)
	@ResponseBody
	public Resp syncCurrency(HttpServletRequest request) {
		JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_CURRENCY,JedisChannelConst.SYNC_MESSAGE);
		return new Resp(Resp.SUCCESS,Resp.SUCCESS_MSG);
	}

	/**
	 * zookeeper 同步交易对
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/sync/currency-pair", method = RequestMethod.POST)
	@ResponseBody
	public Resp syncCurrencyPair(HttpServletRequest request) {
		JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_PAIR,"sync");
		return new Resp(Resp.SUCCESS,Resp.SUCCESS_MSG);


	}

	//规则同步
	@RequestMapping(value = "/sync/currency-rule",method = RequestMethod.POST)
	@ResponseBody
	public Resp syncRule(){
		JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_RULE,JedisChannelConst.SYNC_MESSAGE);
		return new Resp(Resp.SUCCESS,Resp.SUCCESS_MSG);
	}



}
