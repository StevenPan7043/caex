package com.pmzhongguo.ex.api.controller;


import java.util.*;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.dto.TradeRankDto;
import com.pmzhongguo.ex.business.scheduler.FrmScheduler;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.SerializeUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.LstResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pmzhongguo.ex.business.service.TradeRankingService;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 统计交易排名模块
 * 
 * @author Administrator
 *
 */
@Api(value = "交易排名", description = "交易对的交易排名", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/tr")
//@ApiIgnore
public class TradeRankingController extends TopController {

	@Autowired
	private TradeRankingService tradeRankingService;

	/**
	 * 交易排名列表接口
	 * 
	 * @param type
	 *            交易市场类型
	 * @param size
	 *            页码 [1, 总页数]
	 * @return
	 */
	@ApiOperation(value = "交易排名列表接口", notes = "交易排名列表，type：交易对名称，例如btczc; size: 前几名", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/list/{type}/{pagesize}")
	@ResponseBody
	public ObjResp list(@PathVariable String type, @PathVariable Integer pagesize) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", type.toLowerCase());
		param.put("size", pagesize);
		// 暂时写死，交易类型：tradeType值是buy或sell，
		param.put("trade_type", "buy");
		return tradeRankingService.list(param);
	}

	/**
	 * 查询接口
	 * 
	 * @param type
	 * @return
	 */
	@ApiOperation(value = "查询接口", notes = "查询接口交易排名", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/search/{type}")
	@ResponseBody
	public ObjResp search(@PathVariable String type, String uid, String account) {
		if (uid == "" && account == "") {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_FIND_CONDITION_NOT_NULL.getErrorCNMsg(), null);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", type);
		param.put("uid", uid);
		param.put("account", account);
		return tradeRankingService.search(param);
	}

	/**
	 * 查询接口
	 * 
	 * @param type
	 * @return 1 开启; 0关闭
	 */
	@ApiOperation(value = "交易排名是否开启", notes = "type：交易对名称，例如ETHZC，查询某个交易对是否开启，并返回开启的交易对", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/isopen/{type}")
	@ResponseBody
	public ObjResp isopen(@PathVariable String type) {
		if (type == "") {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_FIND_CONDITION_NOT_NULL.getErrorCNMsg(), null);
		}
		return tradeRankingService.isopen(type);
	}

	/**
	 * 交易排名列表接口,包含了24小时成交量
	 *
	 * @param type
	 *            交易市场类型
	 * @param page
	 *            页码 [1, 总页数]
	 * @return
	 */
	@ApiOperation(value = "交易排名列表接口,包含了24小时成交量", notes = "交易排名列表，包含24h成交量，去除了自己刷自己，type：货币名称，例如BTC，EBANK，size:获取的排名数量", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/list/day/{type}/{page}/{size}")
	@ResponseBody
	public LstResp listTradeRankingAndDayTrade(@PathVariable String type,@PathVariable Integer page, @PathVariable Integer size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("count", size);
		param.put("type","EBANK");

		// 从缓存中读取如果就直接返回
		byte[] bytes = JedisUtil.getInstance().lpop(FrmScheduler.EBANK_TRANK_DATA_KEY, 0, -1);
		List<Map<String, Object>> sortList = new ArrayList<Map<String,Object>>();
		if(bytes != null && bytes.length > 0) {
			sortList = (List<Map<String, Object>>)SerializeUtil.deserialize(bytes);
			return new LstResp(Resp.SUCCESS,Resp.SUCCESS_MSG,sortList.size(),sortList);
		}

		sortList = tradeRankingService.getEncryptionAccountTradeRankingByZcAndUsdtList(param);
		int total = sortList == null ? 0 : sortList.size();
        return new LstResp(Resp.SUCCESS,Resp.SUCCESS_MSG,total,sortList);
	}


	@ApiOperation(value = "货币持仓排名", notes = "type：币的名称，如：EBANK，size：数量，如：10", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/list/currency/{type}/{size}")
	@ResponseBody
	public LstResp listTradeRankingByCurrency(@PathVariable String type, @PathVariable Integer size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("size", size);
		param.put("type",type.toUpperCase());

		List<Map<String, Object>>sortList = tradeRankingService.findMemberAccountRankByCurrency(param);
		return new LstResp(Resp.SUCCESS,Resp.SUCCESS_MSG,sortList.size(),sortList);
	}

	@ApiOperation(value = "新的交易排名列表接口,包含了24小时成交量，可以统计zc,usdt,eth", notes = "交易排名列表，包含24h成交量，去除了自己刷自己，type：货币名称，例如BTC，EBANK，size:获取的排名数量", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/new/list/day/{type}/{size}")
	@ResponseBody
	public LstResp listNewTradeRankingAndDayTrade(@PathVariable String type, @PathVariable Integer size) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("count", size);
		param.put("type",type);

		// 从缓存中读取如果就直接返回
		byte[] bytes = JedisUtil.getInstance().lpop(TradeRankingService.REDIS_TRADE_RANK_KEY_+type.toUpperCase(), 0, -1);
		List<TradeRankDto> sortList = new ArrayList<TradeRankDto>();
		if(bytes != null && bytes.length > 0) {
			sortList = (List<TradeRankDto>)SerializeUtil.deserialize(bytes);
			return new LstResp(Resp.SUCCESS,Resp.SUCCESS_MSG,sortList.size(),sortList);
		}

		sortList = tradeRankingService.findTradeRankData(type,size);
		return new LstResp(Resp.SUCCESS,Resp.SUCCESS_MSG,sortList.size(),sortList);
	}

}
