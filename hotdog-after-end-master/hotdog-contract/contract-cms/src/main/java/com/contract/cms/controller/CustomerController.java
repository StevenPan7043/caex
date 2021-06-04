package com.contract.cms.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.contract.common.RestResponse;
import com.contract.dto.*;
import com.contract.entity.*;
import com.contract.service.cms.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.contract.cms.common.MappingUtils;
import com.contract.common.FunctionUtils;
import com.contract.dao.CCustomerMapper;
import com.contract.enums.CoinEnums;
import com.contract.service.redis.RedisUtilsService;

import com.github.pagehelper.PageInfo;

@Controller
public class CustomerController {
	private static final Logger loger = LoggerFactory.getLogger(CustomerController.class);
	@Autowired
	private RedisUtilsService redisUtilsService;
	@Autowired
	private CustomerService customerService;

	@Autowired
    private CUsdtDetailService cUsdtDetailService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private OrderService orderService; 
	@Autowired
	private CCustomerMapper cCustomerMapper;

	/**
	 * 会员列表
	 * @param customer
	 * @param request
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showCustomerList)
	public ModelAndView showCustomerList(CCustomer customer,HttpServletRequest request) {
		List<MtCliqueUser> users=roleService.queryUserList(request);
		Integer roleid=PlatSession.getRoleId(request);
		if(!FunctionUtils.isEquals(1, roleid)) {
			customer.setUserid(PlatSession.getUserId(request));
		}
		BigDecimal totalmoney=customerService.getTotalmoney(customer);
		List<CCustomer> list=customerService.queryCustomerList(customer);
		PageInfo<CCustomer> pageInfo = new PageInfo<CCustomer>(list);
		ModelAndView view=new ModelAndView(MappingUtils.showCustomerList);
		view.addObject("pageInfo", pageInfo);
		view.addObject("customer", customer);
		view.addObject("users", users);
		view.addObject("roleid", roleid);
		view.addObject("totalmoney", totalmoney);
		return view;
	}

	/**
	 * 跳转到编辑会员界面
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showEditCustomer)
	public ModelAndView showEditCustomer(HttpServletRequest request, Integer id) {
		CCustomer cCustomer = customerService.getCCustomer(id);
		ModelAndView view = new ModelAndView(MappingUtils.showEditCustomer);
		view.addObject("cCustomer", cCustomer);
		return view;
	}

	/**
	 * 编辑会员
	 * @param customer
	 * @return
	 */
	@RequestMapping(value = MappingUtils.updateCusStatus)
	@ResponseBody
	public RestResponse updateCusStatus(CCustomer customer) {
		RestResponse result=customerService.updateCusStatus(customer);
		return result;
	}
	
	@RequestMapping(value = MappingUtils.resetPwd)
	@ResponseBody
	public RestResponse resetPwd(Integer id,Integer type) {
		RestResponse result=customerService.resetPwd(id,type);
		return result;
	}
	
	@RequestMapping(value = MappingUtils.showEditcus)
	public ModelAndView showEditcus(Integer cid) {
		CCustomer cCustomer=customerService.getByID(cid);
		ModelAndView view=new ModelAndView(MappingUtils.showEditcus);
		view.addObject("cus", cCustomer);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.editcus)
	@ResponseBody
	public RestResponse editcus(CCustomer customer) {
		RestResponse result=customerService.editcus(customer);
		return result;
	}
	
	@RequestMapping(value = MappingUtils.getCustomer)
	@ResponseBody
	public RestResponse getCustomer(String login) {
		RestResponse result=customerService.getCustomer(login);
		return result;
	}
	
	@RequestMapping(value = MappingUtils.showRecharge)
	public ModelAndView showRecharge() {
		CoinEnums [] coinEnums=CoinEnums.values();
		ModelAndView view=new ModelAndView(MappingUtils.showRecharge);
		view.addObject("coinEnums", coinEnums);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.handleMoney)
	@ResponseBody
	public RestResponse handleMoney(HttpServletRequest request,String login,BigDecimal cost,Integer isout,String coin,String remark) {
		Integer roleid=PlatSession.getRoleId(request);
		RestResponse result=customerService.handleMoney(roleid,login, cost, isout, coin, remark);
		return result;
	}
	
	@RequestMapping(value = MappingUtils.showWallet)
	public ModelAndView showWallet(Integer cid) {
		List<CWallet> wallets=customerService.queryWallet(cid);
		ModelAndView view=new ModelAndView(MappingUtils.showWallet);
		view.addObject("wallets", wallets);
		view.addObject("cid", cid);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.showMoneyList)
	public ModelAndView showMoneyList(MoneyDto dto,Integer flag) {
		List<MoneyDto> list=customerService.queryMoneyList(dto,flag);
		PageInfo<MoneyDto> pageInfo = new PageInfo<MoneyDto>(list);
		ModelAndView view=new ModelAndView(MappingUtils.showMoneyList);
		view.addObject("details", dto);
		view.addObject("flag", flag);
		view.addObject("pageInfo", pageInfo);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.updateCusAuthflag)
	@ResponseBody
	public RestResponse updateCusAuthflag(CCustomer customer) {
		RestResponse result=customerService.updateCusAuthflag(customer);
		return result;
	}

    /**
     * usdt充值列表
     * @param rechargeLog
     * @param request
     * @return
     */
	@RequestMapping(value = MappingUtils.showUsdtRechargeList)
	public ModelAndView showUsdtRechargeList(UsdtRechargeLog rechargeLog,HttpServletRequest request) {
		Integer roleid=PlatSession.getRoleId(request);
		if(!FunctionUtils.isEquals(1, roleid)) {
			rechargeLog.setUserid(PlatSession.getUserId(request));
		}
		List<MtCliqueUser> users=roleService.queryUserList(request);
		List<UsdtRechargeLog> list=customerService.queryUsdtRechargeList(rechargeLog);
		PageInfo<UsdtRechargeLog> pageInfo = new PageInfo<UsdtRechargeLog>(list);
		Map<String, Object> map=customerService.getRecharge(rechargeLog);
		
		ModelAndView view=new ModelAndView(MappingUtils.showUsdtRechargeList);
		view.addObject("rechargeLog", rechargeLog);
		view.addObject("pageInfo", pageInfo);
		view.addObject("users", users);
		view.addObject("roleid", roleid);
		view.addObject("map", map);
		return view;
	}

    /**
     * usdt划转列表
     * @param usdtTransferDto
     * @param request
     * @return
     */
    @RequestMapping(value = MappingUtils.showUsdtTransferList)
    public ModelAndView showUsdtTransferList(UsdtTransferDto usdtTransferDto, HttpServletRequest request) {
        Integer roleid=PlatSession.getRoleId(request);
		if(!FunctionUtils.isEquals(1, roleid)) {
            usdtTransferDto.setUserid(PlatSession.getUserId(request));
        }
		List<MtCliqueUser> users=roleService.queryUserList(request);
        List<UsdtTransferDto> list=cUsdtDetailService.queryUsdtRechargeList(usdtTransferDto);
		Map<String, Object> map = cUsdtDetailService.queryUsdtTransferMap(usdtTransferDto);
		PageInfo<UsdtTransferDto> pageInfo = new PageInfo<UsdtTransferDto>(list);

        ModelAndView view=new ModelAndView(MappingUtils.showUsdtTransferList);
        view.addObject("usdtTransferDto", usdtTransferDto);
        view.addObject("pageInfo", pageInfo);
        view.addObject("users", users);
        view.addObject("roleid", roleid);
        view.addObject("map", map);
        return view;
    }
	@RequestMapping(value = MappingUtils.showSalesman)
	public ModelAndView showSalesman(HttpServletRequest request) {
		Integer roleid=PlatSession.getRoleId(request);
		ModelAndView view=new ModelAndView(MappingUtils.showSalesman);
		view.addObject("roleid", roleid);
		return view;
	}

	/**
	 * 添加业务员
	 * @param login
	 * @param request
	 * @param passWord
	 * @param payWord
	 * @param invitationcode
	 * @param identity
	 * @return
	 */
	@RequestMapping(value = MappingUtils.handleSaleman)
	@ResponseBody
	public RestResponse handleSaleman(String login,HttpServletRequest request,String passWord, String payWord, String invitationcode,Integer identity) {
		RestResponse result=customerService.handleSaleman(login,passWord,payWord,invitationcode,identity,request);
		return result;
	}

	/**
	 * 会员报表
	 * @param customer
	 * @param request
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showCusReport)
	public ModelAndView showCusReport(CCustomer customer,HttpServletRequest request) {
		List<MtCliqueUser> users=roleService.queryUserList(request);
		Integer roleid=PlatSession.getRoleId(request);
		CContractOrder order=new CContractOrder();
		CZcOrder order1=new CZcOrder();
		if(!FunctionUtils.isEquals(1, roleid)) {
			customer.setUserid(PlatSession.getUserId(request));
		}
		//设置业务员搜索
		if(!StringUtils.isEmpty(customer.getSalemanname())) {
			CCustomer cCustomer=cCustomerMapper.getByPhone(customer.getSalemanname());
			if(cCustomer==null) {
				order1.setSalesman(-1);
				order.setSalesman(-1);
			}else {
				order1.setSalesman(cCustomer.getId());
				order.setSalesman(cCustomer.getId());
			}
		}
		order.setUserid(customer.getUserid());
		order.setLogin(customer.getPhone());
		order.setStarttime(customer.getStarttime());
		order.setEndtime(customer.getEndtime());
		ReportDto map=orderService.getOrderReport(order);
		
		
		order1.setUserid(customer.getUserid());
		order1.setLogin(customer.getPhone());
		order1.setStarttime(customer.getStarttime());
		order1.setEndtime(customer.getEndtime());
		ReportDto map1=orderService.getOrderReport1(order1);
		
		map.setNum((map.getNum() == null?0:map.getNum())+(map1.getNum() == null?0:map1.getNum()));
		map.setMoney(FunctionUtils.add(map.getMoney(), map1.getMoney(), 6));
		map.setTax(FunctionUtils.add(map.getTax(), map1.getTax(), 6));
		map.setRates(FunctionUtils.add(map.getRates(), map1.getRates(), 6));
		
		map.setBalance(FunctionUtils.add(map.getBalance(), map1.getBalance(), 6));

//		CCustomer queryDto = new CCustomer();
//		BeanUtils.copyProperties(customer, queryDto);
		customer.setStarttime(null);
		customer.setEndtime(null);
		List<CCustomer> list=customerService.queryCusport(customer);
		customer.setStarttime(order.getStarttime());
		customer.setEndtime(order.getEndtime());
		PageInfo<CCustomer> pageInfo = new PageInfo<CCustomer>(list);
		ModelAndView view=new ModelAndView(MappingUtils.showCusReport);
		view.addObject("pageInfo", pageInfo);
		view.addObject("customer", customer);
		view.addObject("users", users);
		view.addObject("roleid", roleid);
		view.addObject("map", map);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.showAuth)
	public ModelAndView showAuth(Integer cid) {
		CCustomer cCustomer=customerService.getByID(cid);
		ModelAndView view=new ModelAndView(MappingUtils.showAuth);
		if(cCustomer!=null && !StringUtils.isEmpty(cCustomer.getCardurls())) {
			view.addObject("zmurl", cCustomer.getCardurls().split(",")[0]);
			view.addObject("fmurl", cCustomer.getCardurls().split(",")[1]);
		}
		view.addObject("cCustomer", cCustomer);
		return view;
	}

	/**
	 * 业务员伞下充提数据
	 * @param cCustomer
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showUnder)
	public ModelAndView showUnder(CCustomer cCustomer) {
		BigDecimal totalCash=BigDecimal.ZERO;
		BigDecimal totalRecharge=BigDecimal.ZERO;
		BigDecimal totalTax=BigDecimal.ZERO;
		BigDecimal totalRates=BigDecimal.ZERO;
		BigDecimal totalBalance=BigDecimal.ZERO;
		List<Map<String, Object>> list=customerService.queryUnder(cCustomer);
		for(Map<String, Object> l:list) {
			totalCash=FunctionUtils.add(totalCash, new BigDecimal(String.valueOf(l.get("cashmoney"))), 6);
			totalRecharge=FunctionUtils.add(totalRecharge, new BigDecimal(String.valueOf(l.get("rechargemoney"))), 6);
			
			totalTax=FunctionUtils.add(totalTax, new BigDecimal(String.valueOf(l.get("tax"))), 6);
			totalTax=FunctionUtils.add(totalTax, new BigDecimal(String.valueOf(l.get("tax1"))), 6);
			
			totalRates=FunctionUtils.add(totalRates, new BigDecimal(String.valueOf(l.get("rates"))), 6);
			totalRates=FunctionUtils.add(totalRates, new BigDecimal(String.valueOf(l.get("rates1"))), 6);
			
			totalBalance=FunctionUtils.add(totalBalance, new BigDecimal(String.valueOf(l.get("balance"))), 6);
			totalBalance=FunctionUtils.add(totalBalance, new BigDecimal(String.valueOf(l.get("balance1"))), 6);
		}
		ModelAndView view=new ModelAndView(MappingUtils.showUnder);
		view.addObject("list", list);
		view.addObject("cCustomer", cCustomer);
		
		view.addObject("totalCash", totalCash);
		view.addObject("totalRecharge", totalRecharge);
		view.addObject("totalTax", totalTax);
		view.addObject("totalRates", totalRates);
		view.addObject("totalBalance", totalBalance);
		return view;
	}
	/**
	 * 业务员伞下划转数据
	 * @param cCustomer
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showTransferUnder)
	public ModelAndView showTransferUnder(CCustomer cCustomer) {
		List<SalesTeamDto> salesTeam = customerService.getSalesTeam(cCustomer);

		ModelAndView view=new ModelAndView(MappingUtils.showTransferUnder);
		view.addObject("list", salesTeam);
		view.addObject("cCustomer", cCustomer);
		return view;
	}
	@RequestMapping(value = MappingUtils.showECR20Logs)
	public ModelAndView showECR20Logs(TxLogs logs,HttpServletRequest request) {
		
		Integer roleid=PlatSession.getRoleId(request);
		if(!FunctionUtils.isEquals(1, roleid)) {
			logs.setUserid(PlatSession.getUserId(request));
		}
		List<MtCliqueUser> users=roleService.queryUserList(request);
		
		List<TxLogs> list=customerService.queryTxCposLogs(logs);
		PageInfo<TxLogs> pageInfo = new PageInfo<TxLogs>(list);
		
		Map<String, Object> map=customerService.getEcr20Recharge(logs);
		ModelAndView view=new ModelAndView(MappingUtils.showECR20Logs);
		view.addObject("logs", logs);
		view.addObject("pageInfo", pageInfo);
		view.addObject("users", users);
		view.addObject("roleid", roleid);
		view.addObject("map", map);
		return view;
	} 
}
