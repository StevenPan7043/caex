package com.contract.service.cms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.contract.common.*;
import com.contract.dto.SalesTeamDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contract.common.pwd.PwdEncode;
import com.contract.dao.CContractOrderMapper;
import com.contract.dao.CCustomerMapper;
import com.contract.dao.CUsdtDetailMapper;
import com.contract.dao.CWalletMapper;
import com.contract.dao.CZcDetailMapper;
import com.contract.dao.MtCliqueUserMapper;
import com.contract.dao.TxLogsMapper;
import com.contract.dao.UsdtRechargeLogMapper;
import com.contract.dto.MoneyDto;
import com.contract.dto.ReportDto;
import com.contract.entity.CContractOrder;
import com.contract.entity.CCustomer;
import com.contract.entity.CWallet;
import com.contract.entity.CWalletExample;
import com.contract.entity.CZcOrder;
import com.contract.entity.MtCliqueUser;
import com.contract.entity.TxLogs;
import com.contract.entity.UsdtRechargeLog;
import com.contract.enums.CoinEnums;
import com.contract.enums.HandleTypeEnums;
import com.contract.exception.ThrowJsonException;
import com.contract.exception.ThrowPageException;
import com.contract.service.BipbService;
import com.contract.service.api.AppLoginService;
import com.github.pagehelper.PageHelper;
import org.springframework.util.CollectionUtils;

@Service
public class CustomerService {

	@Autowired
	private CCustomerMapper cCustomerMapper;
	@Autowired
	private BipbService bipbService;
	@Autowired
	private CWalletMapper cWalletMapper;
	@Autowired
	private CUsdtDetailMapper cUsdtDetailMapper;
	@Autowired
	private UsdtRechargeLogMapper usdtRechargeLogMapper;
	@Autowired
	private MtCliqueUserMapper cliqueUserMapper;
	@Autowired
	private CContractOrderMapper cContractOrderMapper;
	@Autowired
	private AppLoginService appLoginService;
	@Autowired
	private CZcDetailMapper cZcDetailMapper;
	@Autowired
	private TxLogsMapper logsMapper;
	@Autowired
	private OrderService orderService;
	/**
	 * 
	 * @param customer
	 * @return
	 */
	public List<CCustomer> queryCustomerList(CCustomer customer) {
		//设置业务员搜索
		if(!StringUtils.isEmpty(customer.getSalemanname())) {
			CCustomer cCustomer=cCustomerMapper.getByPhone(customer.getSalemanname());
			if(cCustomer==null) {
				customer.setSalesman(-1);
			}else {
				customer.setSalesman(cCustomer.getId());
			}
		}
		if(!StringUtils.isEmpty(customer.getParentname())) {
			CCustomer cus=cCustomerMapper.getByPhone(customer.getParentname());
			if(cus!=null) {
				customer.setParentid(cus.getId());
			}else {
				customer.setParentid(-1);
			}
		}
		PageHelper.startPage(customer.getPage(),customer.getRows());
		List<CCustomer> list=cCustomerMapper.queryList(customer);
		for(CCustomer l:list) {
			if(l.getParentid()!=null) {
				CCustomer cCustomer=cCustomerMapper.selectByPrimaryKey(l.getParentid());
				if(cCustomer!=null) {
					l.setParentname("账号:"+cCustomer.getPhone()+"/实名:"+cCustomer.getRealname());
				}
			}
			if(l.getSalesman()!=null) {
				CCustomer cCustomer=cCustomerMapper.selectByPrimaryKey(l.getSalesman());
				if(cCustomer!=null) {
					l.setSalemanname(cCustomer.getPhone());
				}
			}
			if(l.getUserid()!=null) {
				MtCliqueUser cliqueUser=cliqueUserMapper.selectByPrimaryKey(l.getUserid());
				if(cliqueUser!=null) {
					l.setUsername(cliqueUser.getLogin());
				}
			}
			CWallet cWallet=cWalletMapper.getByCid(l.getId());
			l.setBalance(cWallet.getBalance());
			l.setZcbalance(cWallet.getZcbalance());
			l.setGdbalance(cWallet.getGdbalance());
			Integer integer = cCustomerMapper.selectAuthGrade(l.getId());
			if (integer != null && integer.intValue() == 1){
				l.setAuthflag(3);
			}
		}
		return list;
	}
	
	public BigDecimal getTotalmoney(CCustomer customer) {
		//设置业务员搜索
		if(!StringUtils.isEmpty(customer.getSalemanname())) {
			CCustomer cCustomer=cCustomerMapper.getByPhone(customer.getSalemanname());
			if(cCustomer==null) {
				customer.setSalesman(-1);
			}else {
				customer.setSalesman(cCustomer.getId());
			}
		}
		if(!StringUtils.isEmpty(customer.getParentname())) {
			CCustomer cus=cCustomerMapper.getByPhone(customer.getParentname());
			if(cus!=null) {
				customer.setParentid(cus.getId());
			}else {
				customer.setParentid(-1);
			}
		}
		BigDecimal totalmoney=cCustomerMapper.getTotalmoney(customer);		
		return totalmoney;
	}

	public RestResponse updateCusStatus(CCustomer customer) {
		if (StringUtils.isNotEmpty(customer.getParentname())) {
			CCustomer byPhone = cCustomerMapper.getByPhone(customer.getParentname());
			if (byPhone == null) {
				return GetRest.getFail("邀请人信息不存在");
			}
			customer.setParentid(byPhone.getId());
			customer.setUserid(byPhone.getUserid());
			customer.setSalesman(byPhone.getSalesman());
		}
		int i = cCustomerMapper.updateByPrimaryKeySelective(customer);
		if (i < 1) {
			return GetRest.getFail("更新失败,记录不存在");
		}
		return GetRest.getSuccess("成功");
	}

	public RestResponse resetPwd(Integer id, Integer type) {
		CCustomer cCustomer=new CCustomer();
		cCustomer.setId(id);
		String pwd=PwdEncode.encodePwd(StaticUtils.DEFAULT_PASSWORD);
		if(FunctionUtils.isEquals(1, type)) {
			cCustomer.setPassword(pwd);
		}else if(FunctionUtils.isEquals(2, type)) {
			cCustomer.setPayword(pwd);
		}
		int i=cCustomerMapper.updateByPrimaryKeySelective(cCustomer);
		if(i<1) {
			return GetRest.getFail("更新失败,记录不存在");
		}
		return GetRest.getSuccess("密码重置成功:"+StaticUtils.DEFAULT_PASSWORD);
	}

	public CCustomer getCCustomer(Integer cid) {
		CCustomer cCustomer = cCustomerMapper.selectByPrimaryKey(cid);
		if (cCustomer.getParentid() != null) {
			CCustomer parent = cCustomerMapper.selectByPrimaryKey(cCustomer.getParentid());
			cCustomer.setParentname(parent.getPhone());
		}
		return cCustomer;
	}
	public CCustomer getByID(Integer cid) {
		return cCustomerMapper.selectByPrimaryKey(cid);
	}

	public RestResponse editcus(CCustomer customer) {
		cCustomerMapper.updateByPrimaryKeySelective(customer);
		return GetRest.getSuccess("成功");
	}

	
	public RestResponse getCustomer(String login) {
		CCustomer customer=cCustomerMapper.getByPhone(login);
		if(customer==null) {
			return GetRest.getFail("会员不存在");
		}
		return GetRest.getSuccess("成功",customer);
	}

	public RestResponse handleMoney(Integer roleid,String login,BigDecimal cost,Integer isout,String coin,String remark) {
		if(cost==null || cost.compareTo(BigDecimal.ZERO)<=0) {
			return GetRest.getFail("请填写操作金额");
		}
		if(isout==null) {
			return GetRest.getFail("操作类型错误");
		}
		CCustomer customer=cCustomerMapper.getByPhone(login);
		if(customer==null) {
			return GetRest.getFail("账户不存在");
		}
		if(!FunctionUtils.isEquals(1, roleid)) {
			if(!FunctionUtils.isEquals(StaticUtils.identity_test, customer.getIdentity())) {
				return GetRest.getFail("当前会员不是模拟账户分公司无法操作");
			}
//			if(!FunctionUtils.isEquals(customer.getUserid(), customer.getUserid())) {
//				return GetRest.getFail("当前模拟账户不属于此分公司无法操作");
//			}
		}
		if(FunctionUtils.isEquals(StaticUtils.status_no, customer.getStatus())) {
			return GetRest.getFail("账号已被冻结");
		}
		if(StringUtils.isEmpty(remark)) {
			remark="系统操作";
		}
		String paycode=FunctionUtils.getOrderCode("R");
		CoinEnums coinEnums=CoinEnums.valueOf(coin);
		switch (coinEnums) {
		case USDT:
			bipbService.handleCUsdtDetail(customer.getId(), HandleTypeEnums.sys.getId(), isout, paycode,cost,remark,customer.getId());
			break;
		default:
			break;
		}
		return GetRest.getSuccess("操作完成");
	}

	public List<CWallet> queryWallet(Integer cid) {
		CWalletExample example=new CWalletExample();
		example.createCriteria().andCidEqualTo(cid);
		List<CWallet> list=cWalletMapper.selectByExample(example);
		return list;
	}

	public List<MoneyDto> queryMoneyList(MoneyDto dto,Integer flag) {
		PageHelper.startPage(dto.getPage(),dto.getRows());
		CoinEnums enums=CoinEnums.valueOf(dto.getType());
		List<MoneyDto> list=new ArrayList<>();
		switch (enums) {
		case USDT:
			if(FunctionUtils.isEquals(1, flag)) {
				list=cUsdtDetailMapper.queryUsdtDetail(dto);
			}
			if(FunctionUtils.isEquals(2, flag)) {
				list=cZcDetailMapper.queryZcDetail(dto);
			}
			return list;
		default:
			throw new ThrowPageException("币种错误");
		}
	}

	public RestResponse updateCusAuthflag(CCustomer customer) {
		int i=cCustomerMapper.updateByPrimaryKeySelective(customer);
		if(i<1) {
			return GetRest.getFail("更新失败,记录不存在");
		}
		return GetRest.getSuccess("成功");
	}

	public List<UsdtRechargeLog> queryUsdtRechargeList(UsdtRechargeLog rechargeLog) {
		//设置业务员搜索
		if(!StringUtils.isEmpty(rechargeLog.getSalemanname())) {
			CCustomer cCustomer=cCustomerMapper.getByPhone(rechargeLog.getSalemanname());
			if(cCustomer==null) {
				rechargeLog.setSalesman(-1);
			}else {
				rechargeLog.setSalesman(cCustomer.getId());
			}
		}
		PageHelper.startPage(rechargeLog.getPage(),rechargeLog.getRows());
		List<UsdtRechargeLog> list=usdtRechargeLogMapper.queryList(rechargeLog);
		for(UsdtRechargeLog l:list) {
			if(l.getUserid()!=null) {
				MtCliqueUser cliqueUser=cliqueUserMapper.selectByPrimaryKey(l.getUserid());
				if(cliqueUser!=null) {
					l.setUsername(cliqueUser.getLogin());
				}
			}
			if(l.getParentid()!=null) {
				CCustomer parent=cCustomerMapper.selectByPrimaryKey(l.getParentid());
				if(parent!=null) {
					l.setParentphone(parent.getPhone());
					l.setParentname(parent.getRealname());
				}
			}
			if(l.getSalesman()!=null) {
				CCustomer cCustomer=cCustomerMapper.selectByPrimaryKey(l.getSalesman());
				if(cCustomer!=null) {
					l.setSalemanname(cCustomer.getPhone());
				}
			}
		}
		return list;
	}

	public RestResponse handleSaleman(String login, String passWord, String payWord, String invitationcode,Integer identity,HttpServletRequest request) {
		CCustomer customer=cCustomerMapper.getByPhone(login);
		if(customer==null) {
//			// 账户不存在-校验密码等参数是否填写
//			if(StringUtils.isEmpty(login)) {
//				return GetRest.getFail("请填写会员账号");
//			}
//			String valid_phone=RpxUtils.valid_phone(login);
//			if(!StringUtils.isBlank(valid_phone)) {
//				return GetRest.getFail(valid_phone);
//			}
//			String valid_password=RpxUtils.valid_password(passWord);
//			if(!StringUtils.isBlank(valid_password)) {
//				return GetRest.getFail(valid_password);
//			}
//			String valid_payword=RpxUtils.valid_password(payWord);
//			if(!StringUtils.isBlank(valid_payword)) {
//				return GetRest.getFail(valid_payword);
//			}
//			// 赋值
//			customer = new CCustomer();
//			customer.setPhone(login);
//			customer.setPassword(passWord);
//			customer.setPayword(payWord);
//			customer.setIdentity(identity);
//			// 校验完毕-注册
//			RestResponse registerResult = appLoginService.handleRegister(customer, invitationcode);
//			if (!registerResult.isStatus()) {
//				throw new ThrowJsonException(registerResult.getDesc());
//			}
//			// 再查询一遍用户信息
//			customer=cCustomerMapper.getByPhone(login);
//			if (customer == null) {
//				return GetRest.getFail("操作失败");
//			}
			return GetRest.getFail("会员不存在，只能操作已注册会员");
		}
		if(FunctionUtils.isEquals(StaticUtils.status_no, customer.getStatus())) {
			return GetRest.getFail("账号已被冻结");
		}
		if(FunctionUtils.isEquals(StaticUtils.identity_agent, customer.getIdentity())) {
			return GetRest.getFail("当前账户已是业务员");
		}
		if(FunctionUtils.isEquals(StaticUtils.identity_test, customer.getIdentity())) {
			return GetRest.getFail("当前账户已是模拟会员");
		}
		//自己是自己的业务员
		if(customer.getSalesman()==null) {
			customer.setSalesman(customer.getId());
		}
		//业务员若没有邀请人，默认邀请人是自己
		if (customer.getParentid() == null) {
			customer.setParentid(customer.getId());
		}
		if(FunctionUtils.isEquals(StaticUtils.identity_agent, identity)) {
			customer.setSalesflag(StaticUtils.status_yes);
			customer.setIdentity(StaticUtils.identity_agent);
		}
		//模拟账户默认到32用户下
		//还要创建一个id为32的管理用户太麻烦，直接放到1下面
		if(FunctionUtils.isEquals(StaticUtils.identity_test, identity)) {
//			customer.setUserid(32);
			customer.setUserid(1);
			customer.setIdentity(StaticUtils.identity_test);
		}else {
			customer.setUserid(PlatSession.getUserId(request));
		}
		
		cCustomerMapper.updateByPrimaryKeySelective(customer);
		return GetRest.getSuccess("成功");
	}
	
	/**
	 * 
	 * @param customer
	 * @return
	 */
	public List<CCustomer> queryCusport(CCustomer customer) {
		//设置业务员搜索
		if(!StringUtils.isEmpty(customer.getSalemanname())) {
			CCustomer cCustomer=cCustomerMapper.getByPhone(customer.getSalemanname());
			if(cCustomer==null) {
				customer.setSalesman(-1);
			}else {
				customer.setSalesman(cCustomer.getId());
			}
		}
		if(!StringUtils.isEmpty(customer.getParentname())) {
			CCustomer cus=cCustomerMapper.getByPhone(customer.getParentname());
			if(cus!=null) {
				customer.setParentid(cus.getId());
			}else {
				customer.setParentid(-1);
			}
		}
		PageHelper.startPage(customer.getPage(),customer.getRows());
		List<CCustomer> list=cCustomerMapper.queryList(customer);
		for(CCustomer l:list) {
			if(l.getSalesman()!=null) {
				CCustomer cCustomer=cCustomerMapper.selectByPrimaryKey(l.getSalesman());
				if(cCustomer!=null) {
					l.setSalemanname(cCustomer.getPhone());
				}
			}
			CContractOrder order=new CContractOrder();
			order.setCid(l.getId());
			ReportDto map=cContractOrderMapper.getOrderMoney(order);
			
			CZcOrder zcOrder=new CZcOrder();
			zcOrder.setCid(l.getId());
			ReportDto map1=orderService.getZCmoney(zcOrder);
			map.setNum((map.getNum() == null?0:map.getNum())+(map1.getNum() == null?0:map1.getNum()));
			map.setMoney(FunctionUtils.add(map.getMoney(), map1.getMoney(), 6));
			map.setTax(FunctionUtils.add(map.getTax(), map1.getTax(), 6));
			map.setRates(FunctionUtils.add(map.getRates(), map1.getRates(), 6));
			
			CWallet cWallet=cWalletMapper.selectByPrimaryKey(l.getId(),CoinEnums.USDT.name());
			map.setBalance(cWallet.getBalance());
			map.setZcbalance(cWallet.getZcbalance());
			l.setMap(map);
		}
		return list;
	}

	public Map<String, Object> getRecharge(UsdtRechargeLog rechargeLog) {
		//设置业务员搜索
		if(!StringUtils.isEmpty(rechargeLog.getSalemanname())) {
			CCustomer cCustomer=cCustomerMapper.getByPhone(rechargeLog.getSalemanname());
			if(cCustomer==null) {
				rechargeLog.setSalesman(-1);
			}else {
				rechargeLog.setSalesman(cCustomer.getId());
			}
		}
		return usdtRechargeLogMapper.getRechargeTotal(rechargeLog);
	}

	public List<Map<String, Object>> queryUnder(CCustomer cus) {
		CCustomer cCustomer=cCustomerMapper.getByPhone(cus.getSalemanname());
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		if(cCustomer!=null) {
			cus.setId(cCustomer.getId());
			list=cCustomerMapper.queryUnder(cus);
		}
		return list;
	}

	/**
	 * 获取业务员团队总信息
	 * @return
	 */
	public List<SalesTeamDto> getSalesTeam(CCustomer cus) {
//		cus.getSalemanname()
		List<SalesTeamDto> salesTeamList = cCustomerMapper.getSalesTeam(cus);
		if (!CollectionUtils.isEmpty(salesTeamList)) {
			for (SalesTeamDto s : salesTeamList) {
//				s.getCid()
				List<MoneyDto> userTransfer = cCustomerMapper.getUserTransfer(s);
				if (!CollectionUtils.isEmpty(userTransfer)) {
					for (MoneyDto moneyDto : userTransfer) {
						if (moneyDto.getIsout() == 0) {
							s.setTransOutSum(moneyDto.getCost());
						}
						if (moneyDto.getIsout() == 1) {
							s.setTransInSum(moneyDto.getCost());
						}
					}
				}
				CWallet byCid = cWalletMapper.getByCid(s.getCid());
				if (byCid != null){
					s.setTotalBalance(byCid.getBalance().add(byCid.getZcbalance()));
				}
			}
		}
		return salesTeamList;
	}



	public List<TxLogs> queryTxCposLogs(TxLogs logs) {
		
		//设置业务员搜索
		if(!StringUtils.isEmpty(logs.getSalemanname())) {
			CCustomer cCustomer=cCustomerMapper.getByPhone(logs.getSalemanname());
			if(cCustomer==null) {
				logs.setSalesman(-1);
			}else {
				logs.setSalesman(cCustomer.getId());
			}
		}
		PageHelper.startPage(logs.getPage(),logs.getRows());
		List<TxLogs> list=logsMapper.queryLogs(logs);
		for(TxLogs l:list) {
			if(l.getUserid()!=null) {
				MtCliqueUser cliqueUser=cliqueUserMapper.selectByPrimaryKey(l.getUserid());
				if(cliqueUser!=null) {
					l.setUsername(cliqueUser.getLogin());
				}
			}
			if(l.getParentid()!=null) {
				CCustomer parent=cCustomerMapper.selectByPrimaryKey(l.getParentid());
				if(parent!=null) {
					l.setParentphone(parent.getPhone());
					l.setParentname(parent.getRealname());
				}
			}
			if(l.getSalesman()!=null) {
				CCustomer cCustomer=cCustomerMapper.selectByPrimaryKey(l.getSalesman());
				if(cCustomer!=null) {
					l.setSalemanname(cCustomer.getPhone());
				}
			}
		}
		return list;
	}

	public Map<String, Object> getEcr20Recharge(TxLogs logs) {
		//设置业务员搜索
		if(!StringUtils.isEmpty(logs.getSalemanname())) {
			CCustomer cCustomer=cCustomerMapper.getByPhone(logs.getSalemanname());
			if(cCustomer==null) {
				logs.setSalesman(-1);
			}else {
				logs.setSalesman(cCustomer.getId());
			}
		}
		Map<String, Object> map=logsMapper.getTotal(logs);
		return map;
	}

	public CCustomer getByPhone(String phone) {
		return cCustomerMapper.getByPhone(phone);
	}
}
