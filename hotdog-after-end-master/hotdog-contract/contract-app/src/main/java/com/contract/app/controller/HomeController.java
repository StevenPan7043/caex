package com.contract.app.controller;

import java.util.List;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import com.contract.service.HuobiUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.contract.app.common.MappingUtil;
import com.contract.dto.Depth;
import com.contract.dto.SymbolDto;
import com.contract.entity.SysNotice;
import com.contract.exception.ThrowPageException;
import com.contract.service.api.ApiHomeService;
import com.contract.service.redis.RedisUtilsService;



@Controller
public class HomeController {

	@Autowired
	private ApiHomeService apiHomeService;
	@Autowired
	private RedisUtilsService redisUtilsService;
	@Autowired
	private HuobiUtils huobiUtils;
	
	/**
	 * 
	 * @param position 1.banner  2中层
	 * @return
	 */
	@RequestMapping(value = MappingUtil.queryBanner)
	@ResponseBody
	public RestResponse queryBanner(Integer position, Integer type) {
		RestResponse result=apiHomeService.queryBanner(position,type);
		return result;
	}
	
	/**
	 * 查询资讯
	 * @param notice
	 * @return
	 */
	@RequestMapping(value = MappingUtil.queryNotice)
	@ResponseBody
	public RestResponse queryNotice(SysNotice notice,Integer type ) {
		RestResponse result=apiHomeService.queryNotice(notice,type);
		return result;
	}
	
	/**
	 * 获取资讯详情
	 * @return
	 */
	@RequestMapping(value = MappingUtil.getNotice)
	@ResponseBody
	public RestResponse getNotice(Integer detailid) {
		RestResponse result=apiHomeService.getNotice(detailid);
		return result;
	}
	
	/**
	 * 获取行情
	 * @return
	 */
	@RequestMapping(value = MappingUtil.querySymbol)
	@ResponseBody
	public RestResponse querySymbol() {
		List<SymbolDto> list=redisUtilsService.querySymDto();
		return GetRest.getSuccess("成功",list);
	}
	
	/**
	 * K线图
	 * @param coin BTC/USDT
	 * @return
	 */
	@RequestMapping(value = MappingUtil.showKline)
	public ModelAndView showKline(String coin,String token) {
		if(StringUtils.isEmpty(coin)) {
			throw new ThrowPageException("市场错误");
		}
		coin=coin.toUpperCase();
		SymbolDto dto=huobiUtils.getSymbolDto(coin);
		if(dto==null) {
			throw new ThrowPageException("币种不存在");
		}
		//深度
		Depth depth=redisUtilsService.getDepth(coin+"_depth");
		ModelAndView view=new ModelAndView(MappingUtil.showKline);
		view.addObject("coin", coin);
		view.addObject("name", coin.split("/")[0]);
		view.addObject("klinecoin", coin.replace("/", ""));
		view.addObject("dto", dto);
		view.addObject("depth", depth);
		view.addObject("token", token);
		return view;
	}
}
