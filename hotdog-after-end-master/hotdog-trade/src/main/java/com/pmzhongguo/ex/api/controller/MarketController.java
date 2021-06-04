package com.pmzhongguo.ex.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.pmzhongguo.ex.business.dto.MemberPairDto;
import com.pmzhongguo.ex.business.dto.SymbolDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyIntroduce;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.resp.*;
import com.pmzhongguo.ex.business.scheduler.UsdtCnyPriceScheduler;
import com.pmzhongguo.ex.business.service.CurrencyIntroduceService;
import com.pmzhongguo.ex.business.service.MarketService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.LstResp;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzextool.utils.StringUtil;
import com.qiniu.util.BeanUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "行情接口", description = "无需签名认证", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("m")
public class MarketController extends TopController {

	@Resource
	private MarketService marketService;
	@Resource
	private MemberService memberService;
	@Autowired
	private CurrencyIntroduceService currencyIntroduceService;
	@Autowired
	private UsdtCnyPriceScheduler usdtCnyPriceScheduler;

	
	@ApiOperation(value = "获得用户自选", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "favorite/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getMemberFavorite(HttpServletRequest request,
			HttpServletResponse response, @PathVariable Long ts) {
		
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}
		
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, daoUtil.queryForList("select pair_dsp_name from m_member_pair where member_id = ?", member.getId()));
	}
	
	@ApiOperation(value = "新增用户自选", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "favorite", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp addMemberFavorite(HttpServletRequest request,
			HttpServletResponse response, @RequestBody MemberPairDto memberPairDto) {
		
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}
		
		if (HelpUtils.nullOrBlank(memberPairDto.getPair_dsp_name()) || null == HelpUtils.getCurrencyPairMap().get(memberPairDto.getPair_dsp_name())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_TRADE_PAIR.getErrorENMsg(), null);
		}
		
		daoUtil.update("delete from m_member_pair where member_id = ? and pair_dsp_name = ?", member.getId(), memberPairDto.getPair_dsp_name().toUpperCase());
		daoUtil.update("insert into m_member_pair(member_id, pair_dsp_name) values(?,?)", member.getId(), memberPairDto.getPair_dsp_name().toUpperCase());
		
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, daoUtil.queryForList("select pair_dsp_name from m_member_pair where member_id = ?", member.getId()));
	}
	
	@ApiOperation(value = "删除用户自选", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "favorite/del", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp delMemberFavorite(HttpServletRequest request,
			HttpServletResponse response, @RequestBody MemberPairDto memberPairDto) {
		
		Member member = JedisUtilMember.getInstance().getMember(request, null);
		if (null == member) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
		}
		
		if (HelpUtils.nullOrBlank(memberPairDto.getPair_dsp_name()) || null == HelpUtils.getCurrencyPairMap().get(memberPairDto.getPair_dsp_name())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_TRADE_PAIR.getErrorENMsg(), null);
		}
		
		daoUtil.update("delete from m_member_pair where member_id = ? and pair_dsp_name = ?", member.getId(), memberPairDto.getPair_dsp_name().toUpperCase());
		
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, daoUtil.queryForList("select pair_dsp_name from m_member_pair where member_id = ?", member.getId()));
	}

	@Deprecated
	@ApiOperation(value = "获得行情汇总信息", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "ticker/{symbol}", method = RequestMethod.GET)
	@ResponseBody
	public TickerResp getTicker(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String symbol) {

		TickerResp tickerResp = marketService.getTicker(symbol.toLowerCase());

		return tickerResp;
	}


    @ApiOperation(value = "新获得行情汇总信息", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "symbolTicker/{symbol}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getSymbolTicker(HttpServletRequest request, HttpServletResponse response, @PathVariable String symbol) {
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, marketService.getTicker(symbol.toLowerCase()));
    }

	@ApiOperation(value = "获得所有行情汇总", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "allticker/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getAllTicker(HttpServletRequest request,
			HttpServletResponse response, @PathVariable Long ts) {
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, MarketService.getTicker());
	}
	
	//@ApiIgnore
	@ApiOperation(value = "获得综合数据", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY <br>type: K线类型，取值1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week <br>size: 数据条数，取值1、10、100、1000 <br>allTicker: 是否需要所有交易对的Ticker，1表示是，0表示否，<br>from：取值为market时，将返回用户的资产信息，取值为其他任何值，不反回资产信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "summary/{symbol}/{type}/{size}/{allTicker}/{from}/{ts}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getSummary(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String symbol, @PathVariable String type, @PathVariable Integer size, @PathVariable Integer allTicker, @PathVariable String from, @PathVariable Long ts) {
		if (size != 1 && size != 10 && size != 100 && size != 1000) {
			size = 1;
		}
		
		// k线
//		KLineResp kLineResp = marketService.getKLine(symbol.toLowerCase(), type, size);
		// 交易对基本信息（开高低收、买一、卖一等信息）
		TickerResp tickerResp = marketService.getTicker(symbol.toLowerCase());
		// 市场深度（买卖盘）
		DepthResp depthResp = marketService.getDepth(symbol.toLowerCase(), 0); //目前不合并深度，因此step填0
		// 最近成交记录
		TradeResp tradeResp = marketService.getTrades(symbol.toLowerCase(), 50);
		
		Map<String, Object> data = new HashMap<String, Object>();
//		data.put("kLine", kLineResp.data.get("lines"));
		data.put("ticker", tickerResp);
		data.put("depth", depthResp);
		data.put("trade", tradeResp == null ? null : tradeResp.trades);
//		data.put("tickersMap", marketService.getTicker());
		data.put("type", type);
		data.put("symbol", symbol);
		data.put("is_stop_ex", HelpUtils.getMgrConfig().getIs_stop_ex());
		
		if (allTicker == 1) { //需要所有交易对的Ticker
			data.put("tickersMap", marketService.getTicker());
		}
		
		if ("market".equals(from)) { //market需要资产信息等
			Member m = JedisUtilMember.getInstance().getMember(request, null);
			if (null != m) {
				// 个人资产
				data.put("currencyLst", memberService.genMemberAccountsLst(m.getId()));
			}		
			
		}
		
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, data);
	}
	
	
	@ApiOperation(value = "获得K线数据", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY <br>type: K线类型，取值1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week <br>size: 数据条数，取值1、10、100、1000", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "kline/{symbol}/{type}/{size}", method = RequestMethod.GET)
	@ResponseBody
	public KLineResp getKLine(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String symbol, @PathVariable String type, @PathVariable Integer size) {
		if (size != 1 && size != 10 && size != 100 && size != 1000) {
			size = 1;
		}
		
		KLineResp kLineResp = marketService.getKLine(symbol.toLowerCase(), type, size);

		return kLineResp;
	}

	@ApiOperation(value = "获得K线数据,时间升序", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY <br>type: K线类型，取值1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week <br>size: 数据条数，取值1、10、100、1000", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "kline/asc/{symbol}/{type}/{size}", method = RequestMethod.GET)
	@ResponseBody
	public KLineResp getKLineAscTime(HttpServletRequest request,
									 HttpServletResponse response, @PathVariable String symbol, @PathVariable String type, @PathVariable Integer size) {
		if (size != 1 && size != 10 && size != 100 && size != 1000) {
			size = 1;
		}

		KLineResp kLineResp = marketService.getKLineAsc(symbol.toLowerCase(), type, size);
		return kLineResp;
	}


    @Deprecated
	@ApiOperation(value = "获得市场深度", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "depth/{symbol}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public DepthResp getDepth(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String symbol) {
		
		/*
		if (step < 0 || step > 5) {
			step = 0;
		}
		*/
		DepthResp depthResp = marketService.getDepth(symbol.toLowerCase(), 0); //目前不合并深度，因此step填0

		return depthResp;
	}

    @ApiOperation(value = "新获得市场深度", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "symbolDepth/{symbol}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getSymbolDepth(HttpServletRequest request,
                                  HttpServletResponse response, @PathVariable String symbol) {
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, marketService.getDepth(symbol.toLowerCase(), 0));
    }
	
	@ApiOperation(value = "获得交易数据", notes = "symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY <br>size: 数据条数，取值1、50", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "trade/{symbol}/{size}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public TradeResp getTrades(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String symbol, @PathVariable Integer size) {
		if (size != 1 && size != 50) {
			size = 1;
		}
		TradeResp tradeResp = marketService.getTrades(symbol.toLowerCase(), size);

		return tradeResp;
	}
	
	@ApiOperation(value = "获得系统支持的交易对", notes = "获得系统支持的交易对,symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "symbol", method = RequestMethod.GET)
	@ResponseBody
	public SymbolsResp getSymbol(HttpServletRequest request,
								 HttpServletResponse response) {
		Map reqMap = $params(request);
		List<SymbolDto> symbolDtoList = HelpUtils.getCurrencyPairSymbol();
		if (!BeanUtil.isEmpty(reqMap) && !BeanUtil.isEmpty(reqMap.get("symbol"))) {
			String symbolReq = reqMap.get("symbol") + "";
			for (SymbolDto symbolDto : symbolDtoList) {
				if (symbolDto.getSymbol().equalsIgnoreCase(symbolReq)) {
					symbolDtoList = new ArrayList<>();
					symbolDtoList.add(symbolDto);
					break;
				}
			}
		}
		return new SymbolsResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbolDtoList);
	}

	@ApiOperation(value = "获得系统支持的币种", notes = "获得系统支持的币种，currencyPair:币种名称，如BTC、LTC、ETH", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "/currency", method = RequestMethod.GET)
	@ResponseBody
	public LstResp getCurrency(HttpServletRequest request,
							   HttpServletResponse response) {
		Map reqMap = $params(request);
		List<Currency> currencyLst = HelpUtils.getCurrencyLst();
		if (!BeanUtil.isEmpty(reqMap) && !BeanUtil.isEmpty(reqMap.get("currencyPair"))) {
			String currencyReq = reqMap.get("currencyPair") + "";
			for (Currency c :
					currencyLst) {
				if (c.getCurrency().equalsIgnoreCase(currencyReq)) {
					currencyLst = new ArrayList<>();
					currencyLst.add(c);
					break;
				}
			}
		}
		return new LstResp(Resp.SUCCESS, Resp.SUCCESS_MSG, currencyLst.size(), currencyLst);
	}
	
	@ApiOperation(value = "获得系统当前时间戳", notes = "获得系统当前时间戳", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "timestamp", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getCurTimestamp(HttpServletRequest request,
			HttpServletResponse response) {
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.getNowTimeStamp());
	}
	
	@ApiOperation(value = "获得系统当前时间戳(String)", notes = "获得系统当前时间戳字符串", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "time", method = RequestMethod.GET)
	@ResponseBody
	public String getTimestamp(HttpServletRequest request,
			HttpServletResponse response) {
		return HelpUtils.getNowTimeStamp();
	}
	
	@ApiOperation(value = "获得K线配置", notes = "获得K线配置", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "klineconf", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp getKXData(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("supports_search", "true");
		map.put("supports_group_request","false");
		map.put("supports_marks","false");
		map.put("supports_timescale_marks","true");
		map.put("supports_time","true");
		String [] str= {"1","5","15","30","60","D",};
		map.put("supported_resolutions",str);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,map);
	}
	
	@ApiOperation(value = "TV配置", notes = "获取TV配置 symbol:交易对名称，如BTCCNY、LTCCNY、ETHCNY", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "tvconf/{symbol}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ObjResp getsymbol(HttpServletRequest request,
			HttpServletResponse response, @PathVariable String symbol) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("name", symbol);
		map.put("timezone", "UTC");
		map.put("minmov", "1");
		map.put("minmov2", "0");
		map.put("pointvalue", "1");
		map.put("session", "24x7");
		map.put("has_intraday", "false");
		map.put("has_no_volume", "false");
		map.put("description", "Apple Inc.");
		map.put("type", "stock");
		String [] str= {"1","5","15","30","60","D",};
		map.put("supported_resolutions",str);
		map.put("pricescale", "10000000");
		map.put("ticker",symbol);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,map);
	}

	@ApiOperation(value = "币种资料", notes = "获取币种资料，如：ETH，BTC", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "currency/info/{currency}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ObjResp getCurrencyIntroduce(HttpServletRequest request,
							 HttpServletResponse response, @PathVariable String currency) {
		CurrencyIntroduce currencyIntroduce = null;
		if(StringUtils.isNotBlank(currency)){
			currencyIntroduce = currencyIntroduceService.getCurrencyIntroduceByCurrency(currency.toUpperCase());
		}
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,currencyIntroduce);
	}

    @ApiOperation(value = "获取币种行情价格", notes = "获取币种行情价格,currencyPair可以指定交易对，例如currencyPair=USDT_CNY则返回该交易对的价格", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "currency/price", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getCurrencyMarketPrice(@RequestParam(required = false,defaultValue = "") String currencyPair) {

	    // 直接返回，因为有定时任务刷新
	    if (currencyPair.equalsIgnoreCase("USDT_CNY")) {
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,UsdtCnyPriceScheduler.PRICE);
        }
        JSONObject marketPrice = usdtCnyPriceScheduler.getCurrencyMarketPrice();
        if (marketPrice == null){
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,null);
        }

	    if (StringUtil.isNullOrBank(currencyPair)) {
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,marketPrice);
        }
	    // 获取指定的交易对价格
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,marketPrice.get(currencyPair.toUpperCase()));


    }

	@ApiOperation(value = "获取交易对涨幅限制", notes = "获取交易对涨幅限制", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "currency-pair/upsDown", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ObjResp getSymbolUpsDown(String symbol) {
        if (symbol == null) {
            //不传交易对时查询所有
            Map<String, UpsDownResp> upsDowns = new HashMap<>();
            Map<String, CurrencyPair> currencyPairMap = HelpUtils.getCurrencyPairMap();
            currencyPairMap.forEach((k, v) -> {
                if (v.getIs_ups_downs_limit().equals(1)) {
                    UpsDownResp upsDownResp = null;
					upsDownResp = setSymbolUpsDown(v.getKey_name(), v, upsDownResp);
                    upsDowns.put(k, upsDownResp);
                }
            });
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, upsDowns);
		} else {
			CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(symbol.toUpperCase());
			UpsDownResp upsDownResp = null;
			if (currencyPair != null && currencyPair.getIs_ups_downs_limit().equals(1)) {
				upsDownResp = setSymbolUpsDown(symbol, currencyPair, upsDownResp);
			}
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap(symbol.toUpperCase(), upsDownResp));
		}
	}

    private UpsDownResp setSymbolUpsDown(String symbol, CurrencyPair currencyPair, UpsDownResp upsDownResp) {
        Object _close_price = JedisUtil.getInstance().get(symbol.toUpperCase() + "_close_price", true);
        if (JedisUtil.getInstance().get(symbol.toUpperCase() + "_close_price", true) != null) {
            BigDecimal close_price = new BigDecimal(_close_price + "");
            BigDecimal tempPrice = close_price.multiply(currencyPair.getUps_downs_limit()).divide(new BigDecimal(100)).setScale(currencyPair.getPrice_precision(), BigDecimal.ROUND_DOWN);
            BigDecimal highPrice = close_price.add(tempPrice);
            BigDecimal lowPrice = close_price.subtract(tempPrice);
            upsDownResp = new UpsDownResp(currencyPair.getIs_ups_downs_limit(), highPrice, lowPrice);
            currencyPair.setLowPrice(lowPrice);
            currencyPair.setHighPrice(highPrice);
            HelpUtils.getCurrencyPairMap().put(symbol.toUpperCase(), currencyPair);
        }
		return upsDownResp;
	}
}
