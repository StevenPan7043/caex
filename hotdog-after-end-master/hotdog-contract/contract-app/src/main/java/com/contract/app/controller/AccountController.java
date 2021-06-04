package com.contract.app.controller;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import com.contract.dto.KlineDto;
import com.contract.service.redis.RedisUtilsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.contract.app.common.MappingUtil;
import com.contract.common.FunctionUtils;
import com.contract.dto.MoneyDto;
import com.contract.dto.TeamVo;
import com.contract.entity.CPayChannel;
import com.contract.service.api.ApiAccountService;



@Controller
public class AccountController {

	
	@Autowired
	private ApiAccountService apiAccountService;

	@Autowired
	private RedisUtilsService redisUtilsService;
	
	/**
	 * 获取个人信息
	 * @param token
	 * @return
	 */
	@RequestMapping(value = MappingUtil.getMyInfos)
	@ResponseBody
	public RestResponse getMyInfos(String token) {
		RestResponse reuslt=apiAccountService.getMyInfos(token);
		return reuslt;
	}
	
	/**
	 * 获取钱包信息
	 * @param token
	 * @param coin  钱包类型 USDT ETH EOS BTC 目前只有usdt
	 * @return
	 */
	@RequestMapping(value = MappingUtil.getWallet)
	@ResponseBody
	public RestResponse getWallet(String token,String coin) {
		if(StringUtils.isEmpty(coin)) {
			return GetRest.getFail("请选择币种");
		}
		RestResponse reuslt=apiAccountService.getWallet(token,coin);
		return reuslt;
	}
	
	/**
	 * 实名认证
	 * @param name
	 * @param idcard
	 * @return
	 */
	@RequestMapping(value = MappingUtil.handleAuth)
	@ResponseBody
	public RestResponse handleAuth(String token,String name,String idcard,String cardurls) {
		if(StringUtils.isBlank(name)) {
			return GetRest.getFail("请填写真实名称");
		}
		if(StringUtils.isBlank(idcard)) {
			return GetRest.getFail("请填写证件号码");
		}
		if(StringUtils.isEmpty(cardurls)) {
			return GetRest.getFail("请上传证件照");
		}
		if(cardurls.split(",").length!=2) {
			return GetRest.getFail("请选择完整证件照");
		}
		RestResponse reuslt=apiAccountService.handleAuth(token,name,idcard,cardurls);
		return reuslt;
	}
	
	/**
	 * 查询我的收款方式
	 * @param token
	 * @return
	 */
	@RequestMapping(value = MappingUtil.queryPayChannel)
	@ResponseBody
	public RestResponse queryPayChannel(String token) {
		RestResponse reuslt=apiAccountService.queryPayChannel(token);
		return reuslt;
	}
	
	/**
	 * 添加收款方式
	 * @return
	 */
	@RequestMapping(value = MappingUtil.addPayChannel)
	@ResponseBody
	public RestResponse addPayChannel(String token,CPayChannel channel) {
		RestResponse reuslt=apiAccountService.addPayChannel(token,channel);
		return reuslt;
	}
	
	/**
	 * 删除支付渠道信息
	 * @param token
	 * @param id
	 * @return
	 */
	@RequestMapping(value = MappingUtil.removePayChannel)
	@ResponseBody
	public RestResponse removePayChannel(String token,Integer id) {
		RestResponse reuslt=apiAccountService.removePayChannel(token,id);
		return reuslt;
	}
	
	/**
	 * 查询usdt资金明细
	 * @return
	 */
	@RequestMapping(value = MappingUtil.queryUsdtDetail)
	@ResponseBody
	public RestResponse queryUsdtDetail(String token,MoneyDto detail) {
		RestResponse reuslt=apiAccountService.queryUsdtDetail(token,detail);
		return reuslt;
	}
	
	/**
	 * 查询逐仓usdt资金明细
	 * @return
	 */
	@RequestMapping(value = MappingUtil.queryZCDetail)
	@ResponseBody
	public RestResponse queryZCDetail(String token,MoneyDto detail) {
		RestResponse reuslt=apiAccountService.queryZCDetail(token,detail);
		return reuslt;
	}
	
	/**
	 * usdt提币
	 * @param token
	 * @param money
	 * @param addr
	 * @param payword
	 * @return
	 */
	@RequestMapping(value = MappingUtil.handleCashUsdt)
	@ResponseBody
	public RestResponse handleCashUsdt(String token,BigDecimal money,String addr,String payword) {
		if(money==null || money.compareTo(BigDecimal.ZERO)<0) {
			return GetRest.getFail("提币数量不得小于0");
		}
		if(StringUtils.isBlank(addr)) {
			return GetRest.getFail("请填写提币地址");
		}
		if(StringUtils.isBlank(payword)) {
			return GetRest.getFail("请填写交易密码");
		}
		RestResponse reuslt=apiAccountService.handleCashUsdt(token,money,addr,payword);
		return reuslt;
	}
	
	/**
	 * 获取个人分享信息
	 * @return
	 */
	@RequestMapping(value = MappingUtil.getMyShare)
	@ResponseBody
	public RestResponse getMyShare(String token) {
		RestResponse reuslt=apiAccountService.getMyShare(token);
		return reuslt;
	}
	
	/**
	 * 转账
	 * @param token
	 * @param money
	 * @param payword
	 * @param type 1合约到逐仓 2逐仓到合约
	 * @return
	 */
	@RequestMapping(value = MappingUtil.handleTrans)
	@ResponseBody
	public RestResponse handleTrans(String token,BigDecimal money,String payword,Integer type) {
		if(!FunctionUtils.isEquals(1, type) && !FunctionUtils.isEquals(2, type)) {
			return GetRest.getFail("划转类型错误");
		}
		if(money.compareTo(BigDecimal.ZERO)<=0) {
			return GetRest.getFail("划转金额不得小于0");
		}
		RestResponse reuslt=apiAccountService.handleTrans(token,money,payword,type);
		return reuslt;
	}
	
	@RequestMapping(value = MappingUtil.queryTeam)
	@ResponseBody
	public RestResponse queryTeam(String token,TeamVo teamVo) {
		RestResponse reuslt=apiAccountService.queryTeam(token,teamVo);
		return reuslt;
	}
	
	/**
	 * 方法：/account/getShareinfo
	 * 获取订单分享内容
	 * @param token
	 * @param ordercode
	 * @param type 1全仓 2逐仓
	 * @return
	 */
	@RequestMapping(value = MappingUtil.getShareinfo)
	@ResponseBody
	public RestResponse getShareinfo(String token,String ordercode,Integer type ) {
		RestResponse reuslt=apiAccountService.getShareinfo(token,ordercode,type);
		return reuslt;
	}

	/**
	 * 获取key线历史数据
	 * @param token
	 * @param symbol
	 * @param klineType
	 * @return
	 */
	@RequestMapping(value = MappingUtil.getKlineList)
	@ResponseBody
	public RestResponse getKlineList(String token, String symbol, String klineType) {
		String key = "kline_" + symbol.toUpperCase()+"_" + klineType;
		String key1 = redisUtilsService.getKey(key);
		List<KlineDto> lists = JSONObject.parseArray(key1, KlineDto.class);
		return GetRest.getSuccess("成功", lists);
	}

	/**
	 * 获取当前key线数据
	 * @param token
	 * @param symbol
	 * @param klineType
	 * @return
	 */
	@RequestMapping(value = MappingUtil.getKlineCurrent)
	@ResponseBody
	public RestResponse getKlineCurrent(String token, String symbol, String klineType) {
		String key = "kline_" + symbol.toUpperCase()+"_" + klineType+"_current";
		String key1 = redisUtilsService.getKey(key);
		KlineDto klineDto = JSONObject.parseObject(key1, KlineDto.class);
		return GetRest.getSuccess("成功", klineDto);
	}

}
