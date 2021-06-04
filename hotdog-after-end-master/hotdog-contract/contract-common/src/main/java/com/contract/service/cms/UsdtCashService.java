package com.contract.service.cms;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.contract.common.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contract.dao.CCashUsdtLogsMapper;
import com.contract.dao.CCustomerMapper;
import com.contract.dao.CWalletMapper;
import com.contract.dao.MtCliqueUserMapper;
import com.contract.entity.CCashUsdtLogs;
import com.contract.entity.CCashUsdtLogsExample;
import com.contract.entity.CCustomer;
import com.contract.entity.CWallet;
import com.contract.entity.MtCliqueUser;
import com.contract.enums.CoinEnums;
import com.contract.enums.HandleTypeEnums;
import com.contract.exception.ThrowJsonException;
import com.contract.service.BipbService;
import com.contract.service.redis.RedisUtilsService;
import com.contract.service.wallet.btc.UsdtConfig;
import com.contract.service.wallet.btc.UsdtSend;
import com.contract.service.wallet.btc.enums.NodeEnums;
import com.github.pagehelper.PageHelper;

@Service
public class UsdtCashService {

	@Autowired
	private MtCliqueUserMapper cliqueUserMapper;
	@Autowired
	private CCashUsdtLogsMapper cCashUsdtLogsMapper;
	@Autowired
	private RedisUtilsService redisUtilsService;
	@Autowired
	private BipbService bipbService;
	@Autowired
	private CCustomerMapper cCustomerMapper;
	@Autowired
	private CWalletMapper cWalletMapper;
	
	public List<CCashUsdtLogs> queryCashUsdlist(CCashUsdtLogs cashLogs) {
		//设置业务员搜索
		if(!StringUtils.isEmpty(cashLogs.getSalemanname())) {
			CCustomer cCustomer=cCustomerMapper.getByPhone(cashLogs.getSalemanname());
			if(cCustomer==null) {
				cashLogs.setSalesman(-1);
			}else {
				cashLogs.setSalesman(cCustomer.getId());
			}
		}
		PageHelper.startPage(cashLogs.getPage(),cashLogs.getRows());
		List<CCashUsdtLogs> list = cCashUsdtLogsMapper.queryCashUsdlist(cashLogs);
		for(CCashUsdtLogs c:list) {
			MtCliqueUser cliqueUser=cliqueUserMapper.selectByPrimaryKey(c.getCheckid());
			if(cliqueUser!=null) {
				c.setChecker(cliqueUser.getLogin());
			}
			CCustomer customer=cCustomerMapper.selectByPrimaryKey(c.getParentid());
			if(customer!=null) {
				c.setParentname(customer.getRealname());
				c.setParentphone(customer.getPhone());
			}
			if(c.getSalesman()!=null) {
				CCustomer cCustomer=cCustomerMapper.selectByPrimaryKey(c.getSalesman());
				if(cCustomer!=null) {
					c.setSalemanname(cCustomer.getPhone());
				}
			}
		}
		return list;
	
	}
	
	public Map<String, Object> getCashmoney(CCashUsdtLogs cashLogs){
		return cCashUsdtLogsMapper.getTotalmoney(cashLogs);
	}

	public RestResponse handlerefundUsdCash(Integer id, Integer userId) {
		CCashUsdtLogs cashLogs= cCashUsdtLogsMapper.selectByPrimaryKey(id);
		if(cashLogs==null) {
			return GetRest.getFail("未找到记录");
		}
		if(!FunctionUtils.isEquals(StaticUtils.cash_ing, cashLogs.getStatus())) {
			return GetRest.getFail("当前状态不可审核");
		}
		String key="cashUsdcheck_"+cashLogs.getId();
		//获取值
		boolean cash_flag=redisUtilsService.setIncrSecond(key, 5);
		if(!cash_flag) {
			return GetRest.getFail("当前提币记录已在处理，5s后重新操作");
		}
		redisUtilsService.setKey(key, String.valueOf(id));
		cashLogs.setChecktime(DateUtil.currentDate());
		cashLogs.setStatus(StaticUtils.cash_refund);
		cashLogs.setCheckid(userId);
		cCashUsdtLogsMapper.updateByPrimaryKeySelective(cashLogs);
		//
		String paycode=FunctionUtils.getOrderCode("R");
		bipbService.handleCUsdtDetail(cashLogs.getCid(), HandleTypeEnums.refund.getId(), StaticUtils.pay_in, paycode, cashLogs.getMoney()
				, "提币申请被驳回",cashLogs.getCid());
		return GetRest.getSuccess("驳回成功");
	}

	public CCashUsdtLogs getUsdtdetail(Integer id) {
		CCashUsdtLogs cashLogs= cCashUsdtLogsMapper.selectByPrimaryKey(id);
		return cashLogs;
	}

	public RestResponse handEthcheck(CCashUsdtLogs cashLogs, String nodeid, String feeaddr, Integer userId) {
		CCashUsdtLogs cash= cCashUsdtLogsMapper.selectByPrimaryKey(cashLogs.getId());
		if(cash==null) {
			return GetRest.getFail("未找到记录");
		}
		if(!FunctionUtils.isEquals(cash.getDzstatus(), StaticUtils.dz_reset)) {
			if(!FunctionUtils.isEquals(StaticUtils.cash_ing, cash.getStatus())) {
				return GetRest.getFail("当前状态不可审核");
			}
		}
		String key="cashusdtcheck_"+cashLogs.getId();
		//获取值
		boolean lockflag_cash = redisUtilsService.setIncrSecond(key, 5);
		if(!lockflag_cash) {
			return GetRest.getFail("当前提币记录已在处理，请勿重复提交");
		}
		if(nodeid==null) {//如果选择节点
			return GetRest.getFail("请选择发送节点");
		}
		if(StringUtils.isEmpty(feeaddr)) {
			return GetRest.getFail("请选择手续费地址");
		}
		String code=FunctionUtils.getOrderCode("C");
		
		//查看体现人是什么节点 就用哪个节点审核
		NodeEnums enums=NodeEnums.getById(nodeid);
		String plataddr=enums.getAddr();//主账号地址
		
		CWallet wallet=cWalletMapper.getByAddr(cash.getToaddr());
		if(wallet!=null) {
			CWallet my=cWalletMapper.selectByPrimaryKey(cash.getCid(),CoinEnums.USDT.name());
			//表示接受人是平台会员 那么不用区块转换 直接修改金额
			bipbService.handleCUsdtDetail(wallet.getCid(), HandleTypeEnums.trans.getId(), StaticUtils.pay_in, code, cash.getRealmoney(), "接受【"+my.getAddr()+"】的充值", cash.getCid());
			cashLogs.setDzstatus(StaticUtils.dz_yes);
			cashLogs.setDztime(DateUtil.currentDate());
			plataddr=cash.getFromaddr();//如果是系统的会员 表示不需要实际转
		}else {
			//主账号钱包信息
			BigDecimal money=cash.getRealmoney();
			try {
				code= UsdtSend.sendrawtransaction(enums, enums.getAddr(),feeaddr,
						cash.getToaddr(), money.toPlainString());
				if(StringUtils.isEmpty(code)) {
					throw new ThrowJsonException("交易失败,请检查钱包账户BTC余额");
				}
				System.out.println("CODE:" + code);
			} catch (Throwable e) {
				throw new ThrowJsonException("审核失败,查看是否当前区块未确认订单太多，稍后再试");
			}
			CCashUsdtLogsExample example=new CCashUsdtLogsExample();
			example.createCriteria().andHashcodeEqualTo(code);
			int count=cCashUsdtLogsMapper.countByExample(example);
			if(count>0) {
				throw new ThrowJsonException("当前Hash单号已处理,请稍后重新提交");
			}
			//如果当前是重审 那么标记未对账 
			if(FunctionUtils.isEquals(cash.getDzstatus(), StaticUtils.dz_reset)) {
				cashLogs.setDzstatus(StaticUtils.dz_no);
			}
		}
		cashLogs.setFromaddr(plataddr);
		cashLogs.setHashcode(code);
		cashLogs.setChecktime(DateUtil.currentDate());
		cashLogs.setStatus(StaticUtils.cash_success);
		cashLogs.setCheckid(userId);
		cCashUsdtLogsMapper.updateByPrimaryKeySelective(cashLogs);
		return GetRest.getSuccess("审核通过");
	}

	public RestResponse dzEthSuccess(Integer id, Integer userId) {
		CCashUsdtLogs cash= cCashUsdtLogsMapper.selectByPrimaryKey(id);
		if(cash==null) {
			return GetRest.getFail("未找到记录");
		}
		cash.setDzstatus(StaticUtils.dz_yes);
		cash.setDztime(DateUtil.currentDate());
		cash.setCheckid(userId);
		cCashUsdtLogsMapper.updateByPrimaryKeySelective(cash);
		return GetRest.getSuccess("标记成功");
	}

}
