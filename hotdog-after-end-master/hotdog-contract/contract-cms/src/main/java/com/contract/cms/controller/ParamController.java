package com.contract.cms.controller;

import java.util.List;

import com.contract.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.contract.cms.common.MappingUtils;
import com.contract.entity.Coins;
import com.contract.entity.SysInfos;
import com.contract.service.cms.ParamService;
import com.contract.service.redis.RedisUtilsService;

@Controller
public class ParamController {

	@Autowired
	private ParamService paramService;
	
	@Autowired
	private RedisUtilsService redisUtilsService;
	
	
	@RequestMapping(value = MappingUtils.showParams)
	public ModelAndView showParams() {
		List<SysInfos> list=paramService.queryParams();
		ModelAndView view=new ModelAndView(MappingUtils.showParams);
		view.addObject("list", list);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.editParam)
	@ResponseBody
	public RestResponse editParam(String key, String val, String remark) {
		RestResponse result=paramService.editParam(key,val,remark);
		return result;
	}
	
	@RequestMapping(value = MappingUtils.showEditparam)
	public ModelAndView showEditparam(String key) {
		SysInfos infos=paramService.getParams(key);
		ModelAndView view=new ModelAndView(MappingUtils.showEditparam);
		view.addObject("infos", infos);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.showCoins)
	public ModelAndView showCoins() {
		List<Coins> list=paramService.queryCoins();
		ModelAndView view=new ModelAndView(MappingUtils.showCoins);
		view.addObject("list", list);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.showEditCoin)
	public ModelAndView showEditCoin(String coin) {
		Coins coins=paramService.getCoin(coin);
		ModelAndView view=new ModelAndView(MappingUtils.showEditCoin);
		view.addObject("coins", coins);
		return view; 
	}
	
	@RequestMapping(value = MappingUtils.editCoin)
	@ResponseBody
	public RestResponse editCoin(Coins coins) {
		RestResponse result=paramService.editCoin(coins);
		return result;
	}
}
