package com.pmzhongguo.ex.business.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.business.dto.SymbolDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.mapper.CurrencyMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;



@Service
@Transactional
public class CurrencyService extends BaseServiceSupport implements IDataProcess {
	@Autowired
	private CurrencyMapper currencyMapper;

	public CurrencyPair getCurrencyPair(Integer id) {
		return currencyMapper.getCurrencyPair(id);
	}

	public List<CurrencyPair> getAllCurrencyPair(Map param) {
		return currencyMapper.listCurrencyPairPage(param);
	}

	public ObjResp editCurrencyPair(CurrencyPair currencyPair) {
		Integer alreadyFirst = -1;
		if (currencyPair.getIs_area_first() == 1) {
			alreadyFirst = daoUtil.queryForInt("select count(1) from d_currency_pair where area_id = ? and is_area_first = 1 and id <> ?", currencyPair.getArea_id(), currencyPair.getId());
			if (alreadyFirst >= 1) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.TRADE_AREA_HAS_NUMBER_ONE_SYMBOL.getErrorCNMsg(), null);
			}
		}
		alreadyFirst = daoUtil.queryForInt("select count(1) from d_currency_pair where area_id = ?  and id <> ? and p_order = ?", currencyPair.getArea_id(), currencyPair.getId(),currencyPair.getP_order());
		if (alreadyFirst >= 1) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.SYMBOL_IS_ALREADY_EXISTS.getErrorCNMsg(), null);
		}
		currencyMapper.editCurrencyPair(currencyPair);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}

	/**
	 * 新增交易对，需新建对应交易对的订单表和交易表
	 * @param currencyPair
	 */
	public ObjResp addCurrencyPair(CurrencyPair currencyPair) {
		if (null == HelpUtils.getCurrencyMap().get(currencyPair.getBase_currency())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.BASE_CURRENCY_NOT_EXISTS.getErrorCNMsg(), null);
		}
		if (null == HelpUtils.getCurrencyMap().get(currencyPair.getQuote_currency())) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.QUOTE_CURRENCY_NOT_EXISTS.getErrorCNMsg(), null);
		}
		Integer alreadyFirst = -1;
		if (currencyPair.getIs_area_first() == 1) {
			alreadyFirst = daoUtil.queryForInt("select count(1) from d_currency_pair where area_id = ? and is_area_first = 1", currencyPair.getArea_id());
			if (alreadyFirst >= 1) {
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.SYMBOL_IS_ALREADY_EXISTS.getErrorCNMsg(), null);
			}
		}
		alreadyFirst = daoUtil.queryForInt("select count(1) from d_currency_pair where area_id = ? and p_order = ?", currencyPair.getArea_id(), currencyPair.getP_order());
		if (alreadyFirst >= 1) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.SYMBOL_IS_ALREADY_EXISTS.getErrorCNMsg(), null);
		}
		currencyPair.setKey_name(currencyPair.getBase_currency() + currencyPair.getQuote_currency());
		currencyMapper.addCurrencyPair(currencyPair);

		//新建订单表和交易表
		daoUtil.update("CREATE TABLE t_order_" + currencyPair.getKey_name().toLowerCase() + " LIKE t_order_template");
		daoUtil.update("CREATE TABLE t_trade_" + currencyPair.getKey_name().toLowerCase() + " LIKE t_trade_template");
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
	}
	
	/**
	 * 以key_name为Key，存储CurrencyPairMap，并存储到内存中
	 * 后续对CurrencyPair的获取，都到内存中获取，修改了CurrencyPair重新刷新内存
	 * @return
	 */
	public Map<String, CurrencyPair> getCurrencyPairMap() {
		Map param = new HashMap();
		param.put("p_status", 1);
		param.put("sortorder", "asc,asc");
		param.put("sortname", "area_id,p_order");
		param.put("needPage", "1");
		List<CurrencyPair> cpLst = currencyMapper.listCurrencyPairPage(param);
		Map<String, CurrencyPair> retMap = new HashMap<String, CurrencyPair>();
		for (int i = 0; i < cpLst.size(); i++) {
			if (cpLst.get(i).getIs_ups_downs_limit() == 1) {
				Object _close_price = JedisUtil.getInstance().get(cpLst.get(i).getKey_name().toUpperCase() + "_close_price", true);
				if (_close_price != null) {
					BigDecimal close_price = new BigDecimal(_close_price + "");
					BigDecimal tempPrice = close_price.multiply(cpLst.get(i).getUps_downs_limit()).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN);
					BigDecimal highPrice = close_price.add(tempPrice);
					BigDecimal lowPrice = close_price.subtract(tempPrice);
					cpLst.get(i).setHighPrice(highPrice);
					cpLst.get(i).setLowPrice(lowPrice);
				}
			}
			retMap.put(cpLst.get(i).getKey_name(), cpLst.get(i));
		}
		
		return retMap;
	}
	
	/**
	 * 获取List并存储到内存中
	 * @return
	 */
	public List<CurrencyPair> getCurrencyPairLst() {
		Map param = new HashMap();
		param.put("p_status", 1);
		param.put("sortorder", "asc,asc");
		param.put("sortname", "area_id,p_order");
		param.put("needPage", "1");
		List<CurrencyPair> cpLst = currencyMapper.listCurrencyPairPage(param);
		
		return cpLst;
	}
	
	/**
	 * 缓存交易对数据，分别以map和list形式缓存，方便业务模块获取
	 * @param servletContext
	 */
	public void cacheCurrencyPair(ServletContext servletContext) {
		Map<String, CurrencyPair> currencyPairMap = getCurrencyPairMap();
		servletContext.setAttribute(HelpUtils.CURRENCYPAIRMAP, currencyPairMap);
		
		List<CurrencyPair> currencyPairLst = getCurrencyPairLst();
		servletContext.setAttribute(HelpUtils.CURRENCYPAIRLST, currencyPairLst);
		
		// 前台请求支持的交易对时，返回的格式
		List<SymbolDto> cpRespLst = new ArrayList<SymbolDto>();
		for (CurrencyPair cp : currencyPairLst) {
			SymbolDto cpResp = new SymbolDto(cp.getKey_name(),
					cp.getBase_currency(), cp.getQuote_currency(),
					cp.getPrice_precision(), cp.getVolume_precision(),
					cp.getTaker_fee(), cp.getMaker_fee());
			
			cpRespLst.add(cpResp);
			
			// 各交易区的首个交易对的名称（symbol）和显示名称（dspNmae）
			if (cp.getIs_area_first() == 1) {
				HelpUtils.firstSymbol.put(cp.getArea_id(), cp.getKey_name());
				HelpUtils.firstDspName.put(cp.getArea_id(), cp.getDsp_name());
			}
		}
		
		servletContext.setAttribute(HelpUtils.CURRENCYPAIRSYMBOL, cpRespLst);
	}
	
	/**
	 * 缓存交易对数据，分别以map和list形式缓存，方便业务模块获取
	 * @param id
	 */
/*	public void cacheCurrencyPair() {
		Map<String, CurrencyPair> currencyPairMap = getCurrencyPairMap();
		JedisUtil.getInstance().set(HelpUtils.CURRENCYPAIRMAP, currencyPairMap, false);
		
		List<CurrencyPair> currencyPairLst = getCurrencyPairLst();
		JedisUtil.getInstance().set(HelpUtils.CURRENCYPAIRLST, currencyPairLst, false);
		
		// 前台请求支持的交易对时，返回的格式
		List<SymbolDto> cpRespLst = new ArrayList<SymbolDto>();
		for (CurrencyPair cp : currencyPairLst) {
			SymbolDto cpResp = new SymbolDto(cp.getKey_name(),
					cp.getBase_currency(), cp.getQuote_currency(),
					cp.getPrice_precision(), cp.getVolume_precision(),
					cp.getTaker_fee(), cp.getMaker_fee());
			
			cpRespLst.add(cpResp);
			
			// 各交易区的首个交易对的名称（symbol）和显示名称（dspNmae）
			if (cp.getIs_area_first() == 1) {
				HelpUtils.firstSymbol.put(cp.getArea_id(), cp.getKey_name());
				HelpUtils.firstDspName.put(cp.getArea_id(), cp.getDsp_name());
			}
		}
		JedisUtil.getInstance().set(HelpUtils.CURRENCYPAIRSYMBOL, cpRespLst, false);
	}*/
	
	
	
	public Currency getCurrency(Integer id) {
		return currencyMapper.getCurrency(id);
	}

	public List<Currency> getAllCurrency(Map param) {
		return currencyMapper.listCurrencyPage(param);
	}

	public void editCurrency(Currency currency) {
		currencyMapper.editCurrency(currency);
	}

	public void addCurrency(Currency currency) {
		currencyMapper.addCurrency(currency);
	}
	
	/**
	 * 缓存虚拟币列表，以list和map分别缓存
	 * @param servletContext
	 */
	public void cacheCurrency(ServletContext servletContext) {
		Map param = new HashMap();
		param.put("sortname", "c_order");
		param.put("sortorder", "asc");
		
		// 存储List格式
		List<Currency> currencyLst = getAllCurrency(param);

		// 所有的币 map 格式
		Map<String, Currency> retAllMap = new HashMap<String, Currency>();

		// 存储 list 格式
		List<Currency> retList = new ArrayList<>(currencyLst.size());
		
		// 存储Map格式
		//以currency为Key，存储CurrencyMap，并存储到内存中
		//后续对Currency的获取，都到内存中获取，修改了Currency重新刷新内存
		Map<String, Currency> retMap = new HashMap<String, Currency>();

		// 存储可用于otc交易的币种,list
		List<Currency> currencyIsOtcLst = new ArrayList<Currency>();
		//List<Currency> currencyIsLocList = new ArrayList<Currency>();
		// 存储可用于otc交易的币种,map
		Map<String, Currency> currencyIsOtcMap = new HashMap<String, Currency>();
		Map<String, Currency> currencyIsLockMap = new HashMap<String, Currency>();

		for (int i = 0; i < currencyLst.size(); i++) {

			// 缓存 is_show 为 1 的币种
			if (currencyLst.get(i).getIs_show() == 1) {
				retList.add(currencyLst.get(i));
				retMap.put(currencyLst.get(i).getCurrency(), currencyLst.get(i));

				// 存储可用于otc交易的币种
				if (currencyLst.get(i).getIs_otc() == 1) {
					currencyIsOtcLst.add(currencyLst.get(i));
					currencyIsOtcMap.put(currencyLst.get(i).getCurrency(), currencyLst.get(i));
				}
				//	if (currencyLst.get(i).getIs_lock() == 1){
				//		currencyIsLocList.add(currencyLst.get(i));
				//		currencyIsLockMap.put(currencyLst.get(i).getCurrency(), currencyLst.get(i));
				//	}
			}
			retAllMap.put(currencyLst.get(i).getCurrency(), currencyLst.get(i));


		}
		servletContext.setAttribute(HelpUtils.CURRENCY_ALL_LST, currencyLst);
		servletContext.setAttribute(HelpUtils.CURRENCY_ALL_MAP, retAllMap);
		servletContext.setAttribute(HelpUtils.CURRENCYLST, retList);
		servletContext.setAttribute(HelpUtils.CURRENCYMAP, retMap);
		servletContext.setAttribute(HelpUtils.CURRENCY_IS_OTC_LST, currencyIsOtcLst);
		servletContext.setAttribute(HelpUtils.CURRENCY_IS_OTC_MAP, currencyIsOtcMap);
		//servletContext.setAttribute(HelpUtils.CURRENCY_IS_LOCK_LST, currencyIsLocList);
		servletContext.setAttribute(HelpUtils.CURRENCY_IS_LOCK_MAP, currencyIsLockMap);
	}

	public Currency findByName(String currency) {
		return currencyMapper.findByName(currency);
	}

	public List<CurrencyPair> findAllCurrencyPair() {
		return currencyMapper.findAllCurrencyPair();
	}


	@Override
	public void dataSync(ServletContext servletContext) {
		cacheCurrency(servletContext);
	}
}
