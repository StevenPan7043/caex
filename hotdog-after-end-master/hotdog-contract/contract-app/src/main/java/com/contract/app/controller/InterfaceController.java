package com.contract.app.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.contract.app.common.MappingUtil;
import com.contract.common.DateUtil;
import com.contract.common.FunctionUtils;
import com.contract.dto.Depth;
import com.contract.dto.SymbolDto;
import com.contract.entity.Coins;
import com.contract.service.redis.RedisUtilsService;

@Controller
public class InterfaceController {

	@Autowired
	private RedisUtilsService redisUtilsService;
	
	@RequestMapping(value = MappingUtil.allticker)
	@ResponseBody
	public JSONObject allticker() {
		List<Coins> coins=redisUtilsService.queryCoins();
		long date=DateUtil.currentTimeMillis();
		JSONObject json=new JSONObject();
		json.put("date", date);
		JSONArray array=new JSONArray();
		for(Coins c:coins) {
			String key = "maketdetail_" + c.getSymbol().replace("_usdt", "");
			String result=redisUtilsService.getKey(key);
			if(!StringUtils.isEmpty(result)) {
				SymbolDto dto=JSONObject.parseObject(result, SymbolDto.class);
				
				String depthKey=c.getName()+"_depth";
				Depth depthMsg=redisUtilsService.getDepth(depthKey);
				
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("symbol", c.getSymbol());
				jsonObject.put("buy",depthMsg.getBids().get(0).get(0));
				jsonObject.put("high", dto.getHigh());
				jsonObject.put("last", dto.getUsdtPrice());
				jsonObject.put("low", dto.getLow());
				jsonObject.put("sell", depthMsg.getAsks().get(0).get(0));
				if(FunctionUtils.isEquals(dto.getIsout(), 0)) {
					jsonObject.put("change", "-"+dto.getScale());
				}else {
					jsonObject.put("change", dto.getScale());
				}
				jsonObject.put("vol", dto.getVol());
				array.add(jsonObject);
			}
		}
		json.put("ticker", array);
		return json;
	}
	
}
