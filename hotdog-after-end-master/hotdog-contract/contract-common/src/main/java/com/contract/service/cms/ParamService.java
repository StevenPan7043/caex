package com.contract.service.cms;

import java.math.BigDecimal;
import java.util.List;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.contract.dao.CoinsMapper;
import com.contract.dao.SysInfosMapper;
import com.contract.entity.Coins;
import com.contract.entity.CoinsExample;
import com.contract.entity.SysInfos;
import com.contract.service.redis.RedisUtilsService;

@Service
public class ParamService {

	@Autowired
	private SysInfosMapper sysInfosMapper;
	@Autowired
	private RedisUtilsService redisUtilsService;
	@Autowired
	private CoinsMapper coinsMapper;
	
	public List<SysInfos> queryParams() {
		return sysInfosMapper.selectByExample(null);
	}

	public RestResponse editParam(String key, String val, String remark) {
		SysInfos infos= sysInfosMapper.selectByPrimaryKey(key);
		if(infos==null) {
			return GetRest.getFail("参数不存在");
		}
		if(StringUtils.isEmpty(remark)) {
			return GetRest.getFail("请填写备注");
		}
		if(StringUtils.isEmpty(val)) {
			return GetRest.getFail("请填写参数");
		}
		infos.setVals(val);
		infos.setRemark(remark);
		infos.setKeyval(key);
		sysInfosMapper.updateByPrimaryKeySelective(infos);
		redisUtilsService.setKey(infos.getKeyval(), val);
		return GetRest.getSuccess("修改成功");
	}

	public SysInfos getParams(String key) {
		return sysInfosMapper.selectByPrimaryKey(key);
	}

	public List<Coins> queryCoins() {
		CoinsExample coinsExample=new CoinsExample();
		coinsExample.setOrderByClause("sort asc");
		return coinsMapper.selectByExample(coinsExample);
	}

	public Coins getCoin(String coin) {
		return coinsMapper.selectByPrimaryKey(coin);
	}

	public RestResponse editCoin(Coins coins) {
		if(coins.getBeginspread()==null || coins.getBeginspread().compareTo(BigDecimal.ZERO)<0) {
			return GetRest.getFail("点差范围/开始不能为空");
		}
		if(coins.getStopspread()==null || coins.getStopspread().compareTo(BigDecimal.ZERO)<0) {
			return GetRest.getFail("点差范围/结束不能为空");
		}
		if(coins.getStopspread().compareTo(coins.getBeginspread())<0) {
			return GetRest.getFail("结束值不得低于开始值");
		}
		if(coins.getZcstopscale()==null) {
			return GetRest.getFail("自动逐仓USDT不能为空");
		}
		if(coins.getZcscale()==null) {
			return GetRest.getFail("逐仓奖励倍数不能为空");
		}
		if(coins.getZctax()==null) {
			return GetRest.getFail("逐仓手续费不能为空");
		}
		coinsMapper.updateByPrimaryKeySelective(coins);
		
		//设置系统行情货币
		CoinsExample coinsExample=new CoinsExample();
		coinsExample.setOrderByClause("updatetime asc");
		List<Coins> list=coinsMapper.selectByExample(coinsExample);
		String coinarr=JSONArray.toJSONString(list);
		redisUtilsService.setKey("coins_key", coinarr);
		return GetRest.getSuccess("成功");
	}
}
