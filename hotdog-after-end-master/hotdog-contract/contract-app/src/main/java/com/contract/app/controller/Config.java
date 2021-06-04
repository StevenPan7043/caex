package com.contract.app.controller;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.contract.common.ListUtils;
import com.contract.dao.CContractOrderMapper;
import com.contract.entity.*;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.contract.app.socket.WebSocketClient;
import com.contract.dao.CWalletMapper;
import com.contract.dao.CoinsMapper;
import com.contract.dao.SysInfosMapper;
import com.contract.enums.CoinEnums;
import com.contract.service.api.ApiTradeService;
import com.contract.service.redis.RedisUtilsService;
import com.contract.service.thead.ThreadPoolConfig;
import com.contract.service.wallet.btc.UsdtPrice;

/**
 * 初始化加载
 * 
 * @author arno
 *
 */
@Component
public class Config implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private SysInfosMapper infosMapper;
	@Autowired
	private RedisUtilsService redisUtilsService;
	@Autowired
	private CoinsMapper coinsMapper;
	@Autowired
	private ApiTradeService apiTradeService;
	@Autowired
	private ThreadPoolConfig threadPoolConfig;
	@Autowired
	private CWalletMapper cWalletMapper;
	@Autowired
	private CContractOrderMapper cContractOrderMapper;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 基础配置 放入redis
		List<SysInfos> list = infosMapper.selectByExample(null);
		for (SysInfos l : list) {
			redisUtilsService.setKey(l.getKeyval(), l.getVals());// 设置系统参数
		}
		// 设置系统行情货币
		CoinsExample coinsExample = new CoinsExample();
		coinsExample.setOrderByClause("sort asc");
		List<Coins> coins = coinsMapper.selectByExample(coinsExample);
		String coinarr = JSONArray.toJSONString(coins);
		redisUtilsService.setKey("coins_key", coinarr);

		// 启动生成交易行情
		// 1.获取到usdt等值人民币价格
		BigDecimal usdtTocny = UsdtPrice.getUsdtToCny();
		redisUtilsService.setKey("usdt_cny", usdtTocny.toPlainString());
	
		// 启动开始监听
		WebSocketClient.HBSocket(coins);
				
		// 启动加入线程池
		String allKey = "timeout_*";
		Set<String> keySetAll = redisUtilsService.getKeys(allKey);
		for (String k : keySetAll) {
			try {
				String string = redisUtilsService.getKey(k);
				if (!StringUtils.isEmpty(string)) {
					List<CContractOrder> cContractOrders = JSONArray.parseArray(string, CContractOrder.class);
					if (cContractOrders != null && cContractOrders.size() > 0) {
						// 加入线程池去处理
						apiTradeService.handleorderTimeout(cContractOrders,k); 
//						threadPoolConfig.commitTask(new HandleTimeoutTask(cContractOrders, k, apiTradeService));
					}
				}
			} catch (Exception e) {
				System.out.println("解析失败");
			}
		}
		CContractOrderExample cContractOrderExample = new CContractOrderExample();
		cContractOrderExample.createCriteria().andRatesGreaterThan(BigDecimal.ZERO).andRunTimeGreaterThan(0);
		cContractOrderExample.setOrderByClause("stoptime DESC");
		List<CContractOrder> list1 = cContractOrderMapper.selectByExample(cContractOrderExample);
		if(list1.size()>20){
			list1 = list1.subList(0,20);
		}
		ListUtils.winList.addAll(list1);


		System.out.println("基础配置加载完毕");

	}


}
