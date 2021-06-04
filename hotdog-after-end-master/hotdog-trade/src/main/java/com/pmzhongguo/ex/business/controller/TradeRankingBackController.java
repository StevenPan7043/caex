package com.pmzhongguo.ex.business.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.TradeRankingSet;
import com.pmzhongguo.ex.business.service.TradeRankingService;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;

/**
 * 后台管理 页面- 交易排名模块
 * 
 * @author Administrator
 *
 */
@ApiIgnore
@Controller
@RequestMapping("backstage/trade")
public class TradeRankingBackController extends TopController {

	@Resource
	private TradeRankingService tradeRankingService;

	/**
	 * 交易排名页面列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/trade_ranking_list_query", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listTrade(HttpServletRequest request, HttpServletResponse response) {
		if ("".equals($("type"))) {
			Map map = new HashMap();
			map.put("Rows", null);
			map.put("Total", 0);
			return map;
		}
		Map param = $params(request);
		param.put("type", $("type").toLowerCase());
		param.put("count", 20);
		if (! "ebank".equalsIgnoreCase($("type").toLowerCase())) {
			return null;
		}

		List list = tradeRankingService.getTradeRankingByZcAndUsdtList(param);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Rows", list);
		return map;
	}

	/**
	 * 交易排名 页面列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/trade_ranking_list", method = RequestMethod.GET)
	public String tradeRankingList(HttpServletRequest request, HttpServletResponse response) {
		return "business/data/trade_ranking_list";
	}

	/**
	 * 交易排名 页面列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/trade_ranking_edit", method = RequestMethod.GET)
	public String tradeRankingEdit(HttpServletRequest request, HttpServletResponse response) {
		Integer dcpid = $int("dcpid");
		TradeRankingSet tradeRankingSet = tradeRankingService.fineTradeRankingOne(dcpid);
		$attr("info", tradeRankingSet);
		return "business/data/trade_ranking_edit";
	}

	/**
	 * 设置交易排名启动管理 页面数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/set_trade_ranking_data", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> setTradeRankingData(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = $params(request);
		List<TradeRankingSet> list = tradeRankingService.getCurrencyPairList();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 设置交易排名启动管理 页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/trade_ranking_set", method = RequestMethod.GET)
	public String setTradeRanking(HttpServletRequest request, HttpServletResponse response) {
		return "business/data/trade_ranking_set";
	}

	/**
	 * 设置交易排名启动管理 页面数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/set_trade_ranking_isopen", method = RequestMethod.POST)
	@ResponseBody
	public Resp setTradeRankingIsopen(HttpServletRequest request, HttpServletResponse response) {
		$params(request);
		TradeRankingSet tradeRankingSet = new TradeRankingSet();
		tradeRankingSet.setDcpid($int("dcpid"));
		tradeRankingSet.setKeyname($("keyname"));
		tradeRankingSet.setTtrsid($int("ttrsid"));
		tradeRankingSet.setOpentime($("opentime"));
		tradeRankingSet.setStartdate($("startdate"));
		tradeRankingSet.setEnddate($("enddate"));
		tradeRankingSet.setIsopen($int("isopen"));
		tradeRankingService.setTradeRankingSet(tradeRankingSet);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 后台发送排名奖励
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/send_reward", method = RequestMethod.GET)
	@ResponseBody
	public Resp sendReward(HttpServletRequest request, HttpServletResponse response) {
		$params(request);
		String type = $("type");
		if (null == type) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.PLEASE_SELECT_SYMBOL.getErrorCNMsg());
		}
		return tradeRankingService.sendReward(type);
	}

	/**
	 * 币种页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/currency_trade_ranking_list", method = RequestMethod.GET)
	public String currencyTradeRankingList(HttpServletRequest request, HttpServletResponse response) {
		return "business/data/currency_trade_ranking_list";
	}

	/**
	 * 获取币种排名
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/get_currency_trade_ranking_data", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> setCurrencyTradeRankingData(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = tradeRankingService.getCurrencyList();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Rows", list);
		return map;
	}

	/**
	 * 设置币种排名
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/currency_trade_ranking_set", method = RequestMethod.GET)
	public String setCurrencyTradeRanking(HttpServletRequest request, HttpServletResponse response) {
		Integer id = $int("id");
		Map<String,Object> info = tradeRankingService.fineCurrencyTradeRankingOne(id);
		$attr("info", info);
		return "business/data/currency_trade_ranking_edit";
	}

	/**
	 * 设置交易排名启动管理 页面数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/set_currency_trade_ranking_isopen", method = RequestMethod.POST)
	@ResponseBody
	public Resp setCurrencyTradeRankingIsopen(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = $params(request);
		tradeRankingService.setCurrencyTradeRankingSet(params);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

}
