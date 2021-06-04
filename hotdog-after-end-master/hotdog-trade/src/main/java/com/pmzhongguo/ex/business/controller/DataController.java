package com.pmzhongguo.ex.business.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import com.pmzhongguo.ex.api.controller.MemberController;
import com.pmzhongguo.ex.business.dto.OrderCancelDto;
import com.pmzhongguo.ex.business.dto.SymbolDto;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.service.ExService;
import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.SerializeUtil;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Order;
import com.pmzhongguo.ex.business.entity.Trade;
import com.pmzhongguo.ex.business.service.DataService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;

@ApiIgnore
@RequestMapping(value = "backstage/trade/")
@Controller
public class DataController extends TopController {
	private Logger logger = LoggerFactory.getLogger(MemberController.class);
	@Resource
	protected DataService dataService;

	@Autowired
	protected ExService exService;
	//被注释的变量改为局部变量，防止出现线程安全
//	private volatile CopyOnWriteArrayList<Map> totalLst;
//	private volatile CountDownLatch countDownLatch;
	//map的key为 http线程名 用于存储查询的手续费
	Map<String,List<Map>> totalLists = new HashMap<>();


	/**
	 * 订单数据列表界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "order_list", method = RequestMethod.GET)
	public String toListOrder(HttpServletRequest request,
							  HttpServletResponse response) {

		return "business/data/order_list";
	}

	@RequestMapping(value = "addr/addr_pool_list", method = RequestMethod.GET)
	public String toListAddrPool(HttpServletRequest request,
								 HttpServletResponse response) {

		return "business/data/addr_pool_list";
	}
	/**
	 * 会员订单汇总
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/order/member_order_list", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listMemberOrder(HttpServletRequest request) {


		Map<String,Object> param = $params(request);
		List<SymbolDto> currencyPairList = HelpUtils.getCurrencyPairSymbol();
		Map<String,Object> map = new HashMap();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(SymbolDto symbolDto : currencyPairList) {
			Map<String,Object> resultMap = new HashMap();
			param.put("table_name", symbolDto.symbol.toLowerCase());
			long count = exService.getAllMemberOrder(param);
			resultMap.put("table_name",symbolDto.symbol);
			resultMap.put("count",count);
			resultMap.put("m_name",param.get("m_name"));
			list.add(resultMap);
		}

		map.put("Rows", list);
		map.put("Total", map.size());
		return map;
	}

	@RequestMapping(value = "addr/addr_pool", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listAddrPool(HttpServletRequest request, HttpServletResponse response) {
		Map param = new HashMap();
		List<Map> list = dataService.getAllAddrPoolBalance(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}


	/**
	 * 撤单
	 * @param orderList
	 * @return
	 */
	@RequestMapping(value = "order/cancel", method = RequestMethod.POST)
	@ResponseBody
	public Resp cancelOrder(@RequestBody List<Order> orderList) {
		for (Order o : orderList) {
			// 只有未完成、未取消的才会处理
			if ("watting".equals(o.getO_status()) || "partial-done".equals(o.getO_status())) {
				OrderCancelDto dto = new OrderCancelDto();
				BeanUtils.copyProperties(o,dto);
				dto.setSymbol(o.getBase_currency()+o.getQuote_currency());
				// 校验合法性，非法情况直接抛出异常
				CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(dto.getSymbol());
				if(null == currencyPair) {
					logger.error("<====== 取消订单异常：交易对为空 =========>");
					continue;
				}
				o.setTable_name(currencyPair.getKey_name().toLowerCase());
				Order order = exService.getOrder(o);
				order.setTable_name(o.getTable_name());
				exService.allMemberFrozenAndProcOutter(BigDecimal.ZERO, null,order.getMember_id(),"cancel", order, null);
			}
		}
		return new Resp(Resp.SUCCESS,Resp.SUCCESS_MSG);
	}
	/**
	 * 订单数据列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "order", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listOrder(HttpServletRequest request, HttpServletResponse response) {

		if ("".equals($("table_name"))) {
			Map map = new HashMap();
			map.put("Rows", null);
			map.put("Total", 0);
			return map;
		}

		Map param = $params(request);
		param.put("table_name", $("table_name").toLowerCase());
		// 这里是处理挂单中的。
		if ("watting,partial-done".equals(param.get("o_status"))) {
			param.put("o_status_in", "'watting', 'partial-done'");
			param.remove("o_status");
		}

		List<Order> list = dataService.getAllOrder(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 成交数据列表界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "trade_list", method = RequestMethod.GET)
	public String toListTrade(HttpServletRequest request,
			HttpServletResponse response) {

		return "business/data/trade_list";
	}

	/**
	 * 会员交易汇总
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "member_trade_list", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listMemberTrade(HttpServletRequest request) {


		Map<String,Object> param = $params(request);
		List<SymbolDto> currencyPairList = HelpUtils.getCurrencyPairSymbol();
		Map<String,Object> map = new HashMap();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(SymbolDto symbolDto : currencyPairList) {
			Map<String,Object> resultMap = new HashMap();
			param.put("table_name", symbolDto.symbol.toLowerCase());
			long count = dataService.getAllMemberTrade(param);
			resultMap.put("table_name",symbolDto.symbol);
			resultMap.put("count",count);
			resultMap.put("m_name",param.get("m_name"));
			list.add(resultMap);
		}

		map.put("Rows", list);
		map.put("Total", map.size());
		return map;
	}



	/**
	 * 成交数据列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/trade", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listTrade(HttpServletRequest request, HttpServletResponse response) {

		if ("".equals($("table_name"))) {
			Map map = new HashMap();
			map.put("Rows", null);
			map.put("Total", 0);
			return map;
		}

		Map param = $params(request);
		param.put("table_name", $("table_name").toLowerCase());
		List<Trade> list = dataService.getAllTrade(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}


	/**
	 * 总资产列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/total_balance", method = RequestMethod.GET)
	public String toListTotalBalance(HttpServletRequest request,
									 HttpServletResponse response) {

		return "business/data/total_balance_list";
	}


	/**
	 * 总资产数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/total_balance_list", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listTotalBalance(HttpServletRequest request, HttpServletResponse response) {

		// 币币
		List<Map<String,Object>> list = daoUtil.queryForList("SELECT currency, SUM(total_balance)  total_balance, SUM(frozen_balance) frozen_balance " +
				" FROM m_account  WHERE currency != '' GROUP BY currency");
		// 法币
		List<Map<String,Object>> otcList = daoUtil.queryForList("SELECT currency, SUM(total_balance) total_balance, SUM(frozen_balance) frozen_balance " +
				" FROM o_account WHERE currency != '' GROUP BY currency");
		// 锁仓
		List<Map<String,Object>> lockList = daoUtil.queryForList("SELECT sum(lock_num) as total_balance,currency FROM m_lock_account GROUP BY currency");

		//新锁仓
		List<Map<String,Object>> wareHouseReleaseList = daoUtil.queryForList("SELECT sum(warehouse_amount) as total_balance,currency FROM m_warehouse_account WHERE is_release != 1 and is_deleted = 0 GROUP BY currency");


		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();

		for (Map<String,Object> map : list) {
			String currency = map.get("currency").toString();
			BigDecimal bbTotal = new BigDecimal(map.get("total_balance").toString());
			BigDecimal bbFrozenTotal = new BigDecimal(map.get("frozen_balance").toString());

			// 锁仓
			Object lockObj = lockList.stream().filter(e -> e.get("currency") != null && currency.equals(e.get("currency")))
					.findAny().orElse(null);
			BigDecimal lockTotal = lockObj != null
					? new BigDecimal(((Map<String, Object>) lockObj).get("total_balance").toString())
					: BigDecimal.ZERO;
			lockObj = wareHouseReleaseList.stream().filter(e -> e.get("currency") != null && currency.equals(e.get("currency")))
					.findAny().orElse(null);
			lockTotal = lockObj != null ? new BigDecimal(((Map<String, Object>) lockObj).get("total_balance").toString()).add(lockTotal) : lockTotal;
			// otc
			Object otcObj = otcList.stream().filter(e -> e.get("currency") != null && currency.equals(e.get("currency")))
					.findAny().orElse(null);
			Map<String,Object> otcMap = otcObj != null
					? (Map<String,Object>)otcObj
					: ImmutableMap.of("total_balance",BigDecimal.ZERO,"frozen_balance",BigDecimal.ZERO);
			BigDecimal otcTotal = new BigDecimal(otcMap.get("total_balance").toString());
			BigDecimal otcFrozenTotal = new BigDecimal(otcMap.get("frozen_balance").toString());

			Map<String,Object> obj = new HashMap<String,Object>();
			obj.put("currency",currency);
			// 总的
			BigDecimal canUseBalance = bbTotal.add(otcTotal).subtract(bbFrozenTotal).subtract(otcFrozenTotal).subtract(lockTotal);
			obj.put("total_balance",bbTotal.add(otcTotal).stripTrailingZeros().toPlainString());
			obj.put("frozen_balance",bbFrozenTotal.add(otcFrozenTotal).add(lockTotal).stripTrailingZeros().toPlainString());
			obj.put("can_use_balance",canUseBalance.stripTrailingZeros().toPlainString());

			// 币币
			obj.put("total",bbTotal);
			obj.put("frozen",bbFrozenTotal);
			obj.put("can_use",bbTotal.subtract(bbFrozenTotal).stripTrailingZeros().toPlainString());

			// otc
			BigDecimal otcCanUseBalance = new BigDecimal(otcMap.get("total_balance").toString())
					.subtract(new BigDecimal(otcMap.get("frozen_balance").toString()));
			obj.put("otc_total",new BigDecimal(otcMap.get("total_balance").toString()).stripTrailingZeros().toPlainString());
			obj.put("otc_frozen",new BigDecimal(otcMap.get("frozen_balance").toString()).stripTrailingZeros().toPlainString());
			obj.put("otc_can_use",otcCanUseBalance.stripTrailingZeros().toPlainString());

			// 锁仓
			obj.put("lock_total",lockTotal);
			resultList.add(obj);

		}

		Map map = new HashMap();
		map.put("Rows", resultList);
		map.put("Total", resultList.size());
		return map;
	}

	public static void main(String[] args) {
		String currency = "ETH";
		List<Map<String,Object>> otcList = new ArrayList<>();
		otcList.add(ImmutableMap.of("currency","ETH","num","229"));
		otcList.add(ImmutableMap.of("currency","ETH","num","22.33"));
		Object otcObj = otcList.stream().filter(e -> e.get("currency") != null && currency.equals(e.get("currency")))
				.findAny().orElse(null);
		System.out.println(otcObj.toString());
	}


	/**
	 * 手续费列表界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/fee_list", method = RequestMethod.GET)
	public String toListFee(HttpServletRequest request,
							HttpServletResponse response, Model model) {

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet("https://www.zzexvip.com/m/currency");
		CloseableHttpResponse httpResponse;
		try {
			httpResponse = httpClient.execute(httpGet);
			Map map = JSON.parseObject(EntityUtils.toString(httpResponse.getEntity()), Map.class);
			List<Map<String,String>> datas = JSON.parseObject(map.get("datas").toString(), List.class);
			List<String> currencys = new ArrayList<>();
			datas.forEach(data->currencys.add(data.get("currency")));
			Collections.sort(currencys);
			model.addAttribute("currencys",currencys);
		} catch (IOException e) {
			logger.warn("请求币种超时");
		}
		return "business/data/fee_list";
	}

	/**
	 * 手续费列表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/fee_list_list", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listFee(HttpServletRequest request, HttpServletResponse response) throws InterruptedException, IOException {

		Properties properties = PropertiesLoaderUtils.loadAllProperties("../threadPool.properties");
		Integer fee_list_pool_num = Integer.valueOf(properties.getProperty("fee_list_pool_num"));
		ExecutorService fee_list_pool = Executors.newFixedThreadPool(fee_list_pool_num, new ZzexThreadFactory("fee_list_pool"));
		String startDate = $("startDate");
		String endDate = $("endDate");
		String currency = $("currency");
		boolean isSymbolEmpty = StringUtils.isNotBlank(currency);
		String addWhere = "";
		String addWhere2 = "";
		if (!HelpUtils.nullOrBlank(startDate)) {
			addWhere += " And done_time >= '" + startDate + "'";
			addWhere2 += " And audit_time >= '" + startDate + "'";
		}
		if (!HelpUtils.nullOrBlank(endDate)) {
			addWhere += " And done_time <= '" + endDate + "'";
			addWhere2 += " And audit_time <= '" + endDate + "'";
		}
		CopyOnWriteArrayList<Map> totalLst = new CopyOnWriteArrayList<>();
		List<Map> list = daoUtil.queryForList("select key_name from d_currency_pair");
		CountDownLatch countDownLatch = new CountDownLatch(list.size());
		// 交易手续费从各个表获取
		String threadName = Thread.currentThread().getName();
		for (Map keyNameItem : list) {
			String symbolTemp = keyNameItem.get("key_name").toString();
			if(isSymbolEmpty || symbolTemp.startsWith(currency) || symbolTemp.endsWith(currency)){
				fee_list_pool.execute(new ExecuteListFee(keyNameItem, addWhere,currency,threadName,countDownLatch));
			}else {
				countDownLatch.countDown();
			}
		}
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<Map> maps = totalLists.get(threadName);
		if (maps!=null){
			totalLst.addAll(maps);
			totalLists.remove(threadName);
		}
		List list2 = null;
		if (isSymbolEmpty) {
			list2 = daoUtil.queryForList("SELECT '提现手续费' key_name, SUM(w_fee) fee, currency fee_currency FROM m_coin_withdraw WHERE 1 = 1 and w_status = 1 " + addWhere2 + " and currency='"+currency+"' GROUP BY currency");
			totalLst.addAll(list2);
		}else{
//			 提现手续费
			list2 = daoUtil.queryForList("SELECT '提现手续费' key_name, SUM(w_fee) fee, currency fee_currency FROM m_coin_withdraw WHERE 1 = 1 and w_status = 1 " + addWhere2 + " GROUP BY currency");
			totalLst.addAll(list2);
		}
		Map map = new HashMap();
		map.put("Total", totalLst.size());
		map.put("Rows", totalLst);
		fee_list_pool.shutdown();
		return map;
	}
	class ExecuteListFee implements Runnable {
		private Map keyNameMap;
		private String _addWhere;
		private String currency;
		private String threadName;
		private CountDownLatch countDownLatch;
		ExecuteListFee(Map keyNameMap, String _addWhere,String currency,String threadName,CountDownLatch countDownLatch) {
			this.keyNameMap = keyNameMap;
			this._addWhere = _addWhere;
			this.currency = currency;
			this.threadName = threadName;
			this.countDownLatch = countDownLatch;
		}

		@Override
		public void run() {
			long start = System.currentTimeMillis();
			List<Map> list1 = new ArrayList<>();
			try {
				if (StringUtils.isNotBlank(currency)){
					list1 = daoUtil.queryForList("SELECT '" + (keyNameMap.get("key_name") + "") + "' key_name, SUM(fee) fee, fee_currency FROM t_trade_" + (keyNameMap.get("key_name") + "").toLowerCase() + " WHERE 1 = 1 " + _addWhere + " and fee_currency = '" + currency + "' GROUP BY fee_currency");
				}else {
					list1 = daoUtil.queryForList("SELECT '" + (keyNameMap.get("key_name") + "") + "' key_name, SUM(fee) fee, fee_currency FROM t_trade_" + (keyNameMap.get("key_name") + "").toLowerCase() + " WHERE 1 = 1 " + _addWhere + " GROUP BY fee_currency");
				}
			}catch (Exception e){
				logger.warn("在进行查询手续费时sql查询报错:",e);
			}
			long end = System.currentTimeMillis();
			if (end - start > 3000) {
				logger.warn("多线程查询手续费，交易对：[{}],执行时长：{} ms。", keyNameMap.get("key_name"), end - start);
			}
			if (list1.size()>0) {
				List list = totalLists.get(threadName);
				if (list!=null){
					list1.addAll(list);
				}
				totalLists.put(threadName,list1);
			}
			countDownLatch.countDown();
		}
	}
}
