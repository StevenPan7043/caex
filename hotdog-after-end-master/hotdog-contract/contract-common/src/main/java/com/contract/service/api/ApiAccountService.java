package com.contract.service.api;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.contract.common.*;
import com.contract.service.HuobiUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.contract.common.pwd.PwdEncode;
import com.contract.dao.CCashUsdtLogsMapper;
import com.contract.dao.CContractOrderMapper;
import com.contract.dao.CCustomerMapper;
import com.contract.dao.CPayChannelMapper;
import com.contract.dao.CUsdtDetailMapper;
import com.contract.dao.CWalletMapper;
import com.contract.dao.CZcDetailMapper;
import com.contract.dao.CZcOrderMapper;
import com.contract.dto.CustomerDto;
import com.contract.dto.MoneyDto;
import com.contract.dto.SymbolDto;
import com.contract.dto.TeamVo;
import com.contract.entity.CCashUsdtLogs;
import com.contract.entity.CContractOrder;
import com.contract.entity.CContractOrderExample;
import com.contract.entity.CCustomer;
import com.contract.entity.CCustomerExample;
import com.contract.entity.CPayChannel;
import com.contract.entity.CPayChannelExample;
import com.contract.entity.CUsdtDetail;
import com.contract.entity.CWallet;
import com.contract.entity.CZcOrder;
import com.contract.enums.CoinEnums;
import com.contract.enums.HandleTypeEnums;
import com.contract.enums.SysParamsEnums;
import com.contract.exception.ThrowJsonException;
import com.contract.service.BipbService;
import com.contract.service.UtilsService;
import com.contract.service.redis.RedisUtilsService;
import com.github.pagehelper.PageHelper;

@Service
public class ApiAccountService {
	private static final Logger logger  = LoggerFactory.getLogger(ApiAccountService.class);
	@Autowired
	private UtilsService utilsService;
	@Autowired
	private CCustomerMapper cCustomerMapper;
	@Autowired
	private CWalletMapper cWalletMapper;
	@Autowired
	private RedisUtilsService redisUtilsService;
	@Autowired
	private CPayChannelMapper cPayChannelMapper;
	@Autowired
	private CUsdtDetailMapper cUsdtDetailMapper;
	@Autowired
	private BipbService bipbService;
	@Autowired
	private CCashUsdtLogsMapper cCashUsdtLogsMapper;
	@Autowired
	private CContractOrderMapper cContractOrderMapper;
	@Autowired
	private CZcDetailMapper cZcDetailMapper;
	@Autowired
	private CZcOrderMapper cZcOrderMapper;
	@Autowired
	private HuobiUtils huobiUtils;
	/**
	 * ??????????????????
	 * @param token
	 * @return
	 */
	public RestResponse getMyInfos(String token) {
		CustomerDto customerDto = null;
		try {
			customerDto = utilsService.getCusByToken(token);
			CCustomer cCustomer = cCustomerMapper.selectByPrimaryKey(customerDto.getId());
			if (cCustomer == null || FunctionUtils.isEquals(StaticUtils.status_no, cCustomer.getStatus())) {
				return GetRest.getFail("?????????????????????????????????");
			}
			customerDto.setRealname(cCustomer.getRealname());
			customerDto.setAuthflag(cCustomer.getAuthflag());
			CWallet cWallet = cWalletMapper.selectByPrimaryKey(customerDto.getId(), CoinEnums.USDT.name());
			if (cWallet == null) {
				return GetRest.getFail("?????????????????????");
			}
			customerDto.setUsdt(cWallet.getBalance());
			BigDecimal usdtCny = redisUtilsService.getUsdtToCny();
			customerDto.setUsdtCny(FunctionUtils.mul(usdtCny, customerDto.getUsdt(), 2));

			customerDto.setZcUsdt(cWallet.getZcbalance());
			customerDto.setZcUsdtCny(FunctionUtils.mul(usdtCny, customerDto.getZcUsdt(), 2));

			customerDto.setTotalUsdt(FunctionUtils.add(customerDto.getUsdt(), customerDto.getZcUsdt(), 6));
			customerDto.setTotalCny(FunctionUtils.add(customerDto.getUsdtCny(), customerDto.getZcUsdtCny(), 2));
		} catch (Exception e) {
			logger.warn("?????????????????????????????????token:[{}],???????????????{}", token, e.getMessage());
			e.printStackTrace();
			return GetRest.getFail("??????????????????????????????", customerDto);
		}
		return GetRest.getSuccess("??????", customerDto);
	}
	
	/**
	 * 
	 * @param token
	 * @param coin
	 * @return
	 */
	public RestResponse getWallet(String token, String coin) {
		CustomerDto customerDto=utilsService.getCusByToken(token);
		CWallet cWallet=cWalletMapper.selectByPrimaryKey(customerDto.getId(),coin);
		if(cWallet==null) {
			return GetRest.getFail("?????????????????????");
		}
		BigDecimal frozen=cContractOrderMapper.getFrozen(customerDto.getId());
		BigDecimal usdtCny=redisUtilsService.getUsdtToCny();
		Map<String, Object> map=new HashMap<>();
		map.put("addr", cWallet.getAddr());
		map.put("label", cWallet.getLabel());
		map.put("ecrqr", cWallet.getEcrqr());
		map.put("qrurl", cWallet.getQrurl());
		map.put("frozen",frozen.setScale(BigDecimal.ROUND_HALF_UP, 4).toPlainString());
		map.put("balance", cWallet.getBalance().setScale(BigDecimal.ROUND_HALF_UP, 4).toPlainString());
		map.put("cny", FunctionUtils.mul(cWallet.getBalance(), usdtCny, 2).toPlainString());
		return GetRest.getSuccess("??????",map);
	}

	/**
	 * ??????
	 * @param name
	 * @param idcard
	 * @return
	 */
	public RestResponse handleAuth(String token,String name, String idcard,String cardurls) {
		CCustomerExample cCustomerExample=new CCustomerExample();
		cCustomerExample.createCriteria().andIdcardEqualTo(idcard);
		int count=cCustomerMapper.countByExample(cCustomerExample);
		if(count>0) {
			return GetRest.getFail("?????????????????????");
		}
		CustomerDto customerDto=utilsService.getCusByToken(token);
		CCustomer cCustomer=cCustomerMapper.selectByPrimaryKey(customerDto.getId());
		if(cCustomer==null || FunctionUtils.isEquals(StaticUtils.status_no, cCustomer.getStatus())) {
			return GetRest.getFail("?????????????????????????????????");
		}
		if(FunctionUtils.isEquals(StaticUtils.auth_ing,cCustomer.getAuthflag())) {
			return GetRest.getFail("?????????");
		}
		if(FunctionUtils.isEquals(StaticUtils.auth_success,cCustomer.getAuthflag())) {
			return GetRest.getFail("?????????");
		}
		cCustomer.setCardurls(cardurls);
		cCustomer.setRealname(name);
		cCustomer.setIdcard(idcard);
		cCustomer.setAuthflag(StaticUtils.auth_ing);
		cCustomerMapper.updateByPrimaryKeySelective(cCustomer);
		return GetRest.getSuccess("????????????");
	}

	public RestResponse queryPayChannel(String token) {
		CustomerDto customerDto=utilsService.getCusByToken(token);
		
		CPayChannelExample example=new CPayChannelExample();
		example.createCriteria().andCidEqualTo(customerDto.getId());
		List<CPayChannel> list=cPayChannelMapper.selectByExample(example);
		return GetRest.getSuccess("??????",list);
	}

	/**
	 * ????????????
	 * @param channel
	 * @return
	 */
	public RestResponse addPayChannel(String token,CPayChannel channel) {
		CustomerDto customerDto=utilsService.getCusByToken(token);
		CCustomer cCustomer=cCustomerMapper.selectByPrimaryKey(customerDto.getId());
		if(cCustomer==null || FunctionUtils.isEquals(StaticUtils.status_no,cCustomer.getStatus())) {
			return GetRest.getFail("?????????????????????????????????");
		}
		if( !FunctionUtils.isEquals(StaticUtils.auth_success,cCustomer.getAuthflag())) {
			return GetRest.getFail("??????????????????");
		}
		channel.setCid(customerDto.getId());
		switch (channel.getType()) {
		case 1:
			//?????????
			if(StringUtils.isBlank(channel.getName())) {
				return GetRest.getFail("???????????????");
			}
			if(StringUtils.isBlank(channel.getAccount())) {
				return GetRest.getFail("????????????????????????");
			}
			if(StringUtils.isBlank(channel.getPayqr())) {
				return GetRest.getFail("??????????????????");
			}
			cPayChannelMapper.insertSelective(channel);
			break;
		case 2:
			//??????
			if(StringUtils.isBlank(channel.getName())) {
				return GetRest.getFail("???????????????");
			}
			if(StringUtils.isBlank(channel.getAccount())) {
				return GetRest.getFail("??????????????????");
			}
			if(StringUtils.isBlank(channel.getPayqr())) {
				return GetRest.getFail("??????????????????");
			}
			cPayChannelMapper.insertSelective(channel);
			break;
		case 3:
			//?????????
			if(StringUtils.isBlank(channel.getName())) {
				return GetRest.getFail("?????????????????????");
			}
			if(StringUtils.isBlank(channel.getBankname())) {
				return GetRest.getFail("????????????????????????");
			}
			if(StringUtils.isBlank(channel.getAccount())) {
				return GetRest.getFail("?????????????????????");
			}
		
			cPayChannelMapper.insertSelective(channel);
			break;

		default:
			return GetRest.getFail("????????????????????????");
		}
		return GetRest.getSuccess("????????????");
	}

	/**
	 * ??????
	 * @param token
	 * @param id
	 * @return
	 */
	public RestResponse removePayChannel(String token, Integer id) {
		CustomerDto customerDto=utilsService.getCusByToken(token);
		
		CPayChannelExample example=new CPayChannelExample();
		example.createCriteria().andCidEqualTo(customerDto.getId()).andIdEqualTo(id);
		int count=cPayChannelMapper.deleteByExample(example);
		if(count<1) {
			return GetRest.getFail("???????????????????????????");
		}
		return GetRest.getSuccess("????????????");
	}

	/**
	 * ??????usdt
	 * @param token
	 * @param detail
	 * @return
	 */
	public RestResponse queryUsdtDetail(String token, MoneyDto detail) {
		CustomerDto customerDto=utilsService.getCusByToken(token);
		
		detail.setCid(customerDto.getId());
		detail.setTypeid(-1);
		PageHelper.startPage(detail.getPage(),detail.getRows());
		List<MoneyDto> list=cUsdtDetailMapper.queryUsdtDetail(detail);
		return GetRest.getSuccess("??????",ListUtils.getList(list));
	}

	/**
	 * 
	 * @param token
	 * @param money
	 * @param payword
	 * @return
	 */
	public RestResponse handleCashUsdt(String token, BigDecimal money,String addr, String payword) {
		CustomerDto customerDto=utilsService.getCusByToken(token);
		
		CCustomer cCustomer=cCustomerMapper.selectByPrimaryKey(customerDto.getId());
		if(cCustomer==null || FunctionUtils.isEquals(StaticUtils.status_no, cCustomer.getStatus())) {
			return GetRest.getFail("?????????????????????????????????");
		}
		CContractOrderExample example=new CContractOrderExample();
		example.createCriteria().andCidEqualTo(cCustomer.getId()).andStatusEqualTo(1);
		int count=cContractOrderMapper.countByExample(example);
		if(count>0) {
			return GetRest.getFail("???????????????????????????,???????????????????????????");
		}
		
		String pwd=PwdEncode.encodePwd(payword);
		if(!pwd.equals(cCustomer.getPayword())) {
			return GetRest.getFail("??????????????????");
		}
		Integer authGrade = cCustomerMapper.selectAuthGrade(cCustomer.getId());
		if(!FunctionUtils.isEquals(1, authGrade)) {
			return GetRest.getFail("??????????????????");
		}
		if(!FunctionUtils.isEquals(StaticUtils.status_yes, cCustomer.getCancash())) {
			return GetRest.getFail("???????????????????????????");
		}
		CWallet wallet=cWalletMapper.selectByPrimaryKey(customerDto.getId(), CoinEnums.USDT.name());
		if(wallet==null) {
			return GetRest.getFail("?????????????????????");
		}
		if(money.compareTo(wallet.getBalance())>0) {
			return GetRest.getFail("??????????????????");
		}
		
//		boolean valid_addr=UsdtConfig.getInstance(NodeEnums.getById(wallet.getNodeid())).vailedAddress(addr);
//		if(!valid_addr) {
//			return GetRest.getFail("????????????????????????");
//		}
		String cash_tax=redisUtilsService.getKey(SysParamsEnums.usdt_cash_scale.getKey());
		BigDecimal cashtax=new BigDecimal(cash_tax);
		
		String cash_min=redisUtilsService.getKey(SysParamsEnums.usdt_cash_min.getKey());
		BigDecimal cashmin=new BigDecimal(cash_min);
		
		String cash_max=redisUtilsService.getKey(SysParamsEnums.usdt_cash_max.getKey());
		BigDecimal cashmax=new BigDecimal(cash_max);
		
		if(money.compareTo(cashmin)<0) {
			return GetRest.getFail("????????????????????????:"+cashmin);
		}
		if(money.compareTo(cashmax)>0) {
			return GetRest.getFail("????????????????????????:"+cashmax);
		}
		BigDecimal tax=cashtax;//?????????????????????
		BigDecimal realmoney=FunctionUtils.sub(money, tax, 6);
		if(realmoney.compareTo(BigDecimal.ZERO)<0) {
			return GetRest.getFail("???????????????");
		}
		//1??????????????????
		String key="cashusdt_"+customerDto.getId();
		boolean lockflag_cash= redisUtilsService.setIncrSecond(key, 3);
		if(!lockflag_cash) {
			return GetRest.getFail("??????????????????,3s?????????");
		}
		String paycode=FunctionUtils.getOrderCode("U");
		
		CCashUsdtLogs cashUsdtLogs=new CCashUsdtLogs();
		cashUsdtLogs.setCid(customerDto.getId());
		// ??????????????????
		bipbService.handleCUsdtDetail(customerDto.getId(), HandleTypeEnums.cash.getId(), StaticUtils.pay_out, paycode, money, "??????USDT??????????????????"+addr+"???",customerDto.getId());
		CWallet target=cWalletMapper.getByAddr(addr);
		if(target!=null) {
			//????????????
//			bipbService.handleCUsdtDetail(target.getCid(), HandleTypeEnums.recharge.getId(), StaticUtils.pay_in, paycode, money, "???????????????"+wallet.getAddr()+"????????????",customerDto.getId());
			cashUsdtLogs.setFromaddr(wallet.getAddr());
			cashUsdtLogs.setHashcode("????????????");
		}
		cashUsdtLogs.setStatus(StaticUtils.cash_ing);
		cashUsdtLogs.setDzstatus(StaticUtils.dz_no);
		cashUsdtLogs.setMoney(money);
		cashUsdtLogs.setTax(tax);
		cashUsdtLogs.setRealmoney(realmoney);
		cashUsdtLogs.setToaddr(addr);
		cCashUsdtLogsMapper.insertSelective(cashUsdtLogs);
		return GetRest.getSuccess("??????");
	}

	/**
	 * ????????????????????????
	 * @param token
	 * @return
	 */
	public RestResponse getMyShare(String token) {
		CustomerDto dto=utilsService.getCusByToken(token);
		
		Map<String, Object> map=new HashMap<>();
		map.put("invitationcode", dto.getInvitationcode());
		map.put("qrurl", dto.getQrurl());
		String shareurl=redisUtilsService.getKey(SysParamsEnums.app_host.getKey())+"/share/"+dto.getInvitationcode();
		map.put("shareurl",shareurl);
		return GetRest.getSuccess("??????",map);
	}

	public RestResponse queryZCDetail(String token, MoneyDto detail) {
		CustomerDto customerDto=utilsService.getCusByToken(token);
		
		detail.setCid(customerDto.getId());
		detail.setTypeid(-1);
		PageHelper.startPage(detail.getPage(),detail.getRows());
		List<MoneyDto> list=cZcDetailMapper.queryZcDetail(detail);
		return GetRest.getSuccess("??????",ListUtils.getList(list));
	}

	/**
	 * ??????
	 * @param token
	 * @param money
	 * @param payword
	 * @return
	 */
	public RestResponse handleTrans(String token, BigDecimal money, String payword,Integer type) {
		CustomerDto customerDto=utilsService.getCusByToken(token);
		CCustomer cCustomer=cCustomerMapper.selectByPrimaryKey(customerDto.getId());
		if(cCustomer==null || FunctionUtils.isEquals(StaticUtils.status_no,cCustomer.getStatus())) {
			return GetRest.getFail("?????????????????????????????????");
		}
		String pwd=PwdEncode.encodePwd(payword);
		if(!pwd.equals(cCustomer.getPayword())) {
			return GetRest.getFail("??????????????????");
		}
		CContractOrderExample example=new CContractOrderExample();
		example.createCriteria().andCidEqualTo(cCustomer.getId()).andStatusEqualTo(1);
		int count=cContractOrderMapper.countByExample(example);
		if(count>0) {
			return GetRest.getFail("???????????????????????????,???????????????????????????");
		}
		String paycode=FunctionUtils.getOrderCode("T");
		if(FunctionUtils.isEquals(type, 1)) {
			bipbService.handleCUsdtDetail(cCustomer.getId(), HandleTypeEnums.transfer.getId(), StaticUtils.pay_out, paycode, money, "?????????????????????", cCustomer.getId());
			bipbService.handleCZcDetail(cCustomer.getId(), HandleTypeEnums.transfer.getId(), StaticUtils.pay_in, paycode, money, "??????????????????????????????", cCustomer.getId());
		}else if(FunctionUtils.isEquals(type, 2)) {
			bipbService.handleCUsdtDetail(cCustomer.getId(), HandleTypeEnums.transfer.getId(), StaticUtils.pay_in, paycode, money, "??????????????????????????????", cCustomer.getId());
			bipbService.handleCZcDetail(cCustomer.getId(), HandleTypeEnums.transfer.getId(), StaticUtils.pay_out, paycode, money, "?????????????????????", cCustomer.getId());
		}
		return GetRest.getSuccess("????????????");
	}

	public RestResponse queryTeam(String token, TeamVo teamVo) {
		CustomerDto customerDto=utilsService.getCusByToken(token);
		PageHelper.startPage(teamVo.getPage(),teamVo.getRows());
		List<TeamVo> list=cCustomerMapper.queryTeam(customerDto.getId());
		CUsdtDetail cUsdtDetail=new CUsdtDetail();
		cUsdtDetail.setCid(customerDto.getId());
		for(TeamVo l:list) {
			cUsdtDetail.setTargetid(l.getId());
			BigDecimal money=cUsdtDetailMapper.getMoneyByTargetid(cUsdtDetail);
			l.setMoney(money);
		}
		return GetRest.getSuccess("??????",list);
	}

	public RestResponse getShareinfo(String token,String ordercode, Integer type) {
		CustomerDto customerDto=utilsService.getCusByToken(token);
		Map<String, Object> map=new HashMap<>();
		if(FunctionUtils.isEquals(1, type)) {
			CContractOrder order=cContractOrderMapper.getByOrdercode(ordercode);
			if(order==null) {
				return GetRest.getFail("???????????????");
			}
			//1??????????????? ????????????
			String coin=order.getCoin().replace("_", "/").toUpperCase();
			if(FunctionUtils.isEquals(1, order.getStatus())) {
				//??????????????????
				String orderkey="order_"+customerDto.getId()+"_"+coin+"_"+order.getOrdercode();
				String result=redisUtilsService.getKey(orderkey);
				order=JSONObject.parseObject(result, CContractOrder.class);
			}
			SymbolDto symDto= huobiUtils.getSymbolDto(coin);
			if(symDto==null) {
				return GetRest.getFail("???????????????");
			}
			map.put("type", order.getType());
			map.put("price", symDto.getUsdtPrice());
			map.put("buyprice", order.getBuyprice());
			BigDecimal reward=FunctionUtils.div(order.getRates(), order.getRealmoney(),6);
			reward=FunctionUtils.mul(reward, new BigDecimal(100), 4);
			if(reward.compareTo(BigDecimal.ZERO)>=0) {
				map.put("isout", 1);
				map.put("reward", "+"+reward);
			}else {
				map.put("isout", 0);
				map.put("reward", reward);
			}
		}else if(FunctionUtils.isEquals(2, type)) {
			CZcOrder order=cZcOrderMapper.getByOrdercode(ordercode);
			if(order==null) {
				return GetRest.getFail("???????????????");
			}
			//1??????????????? ????????????
			String coin=order.getCoin().replace("_", "/").toUpperCase();
			if(FunctionUtils.isEquals(1, order.getStatus())) {
				//??????????????????
				String orderkey="zc_"+customerDto.getId()+"_"+coin+"_"+order.getOrdercode();
				String result=redisUtilsService.getKey(orderkey);
				order=JSONObject.parseObject(result, CZcOrder.class);
			}
			SymbolDto symDto= huobiUtils.getSymbolDto(coin);
			if(symDto==null) {
				return GetRest.getFail("???????????????");
			}
			map.put("type", order.getType());
			map.put("price", symDto.getUsdtPrice());
			map.put("buyprice", order.getBuyprice());
			BigDecimal reward=FunctionUtils.div(order.getReward(), order.getCapital(),6);
			reward=FunctionUtils.mul(reward, new BigDecimal(100), 4);
			if(reward.compareTo(BigDecimal.ZERO)>=0) {
				map.put("isout", 1);
				map.put("reward", "+"+reward);
			}else {
				map.put("isout", 0);
				map.put("reward", reward);
			}
		}
		map.put("qrurl","http://img.sanxininfo.com:8999//files/cus/qr/20191116/0508688.png");
		return GetRest.getSuccess("??????",map);
	}

}
