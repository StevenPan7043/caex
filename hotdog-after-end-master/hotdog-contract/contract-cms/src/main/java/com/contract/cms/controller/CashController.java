package com.contract.cms.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.contract.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.contract.cms.common.MappingUtils;
import com.contract.common.FunctionUtils;
import com.contract.entity.CCashUsdtLogs;
import com.contract.entity.MtCliqueUser;
import com.contract.service.cms.PlatSession;
import com.contract.service.cms.RoleService;
import com.contract.service.cms.UsdtCashService;
import com.contract.service.wallet.btc.enums.FeeEnums;
import com.contract.service.wallet.btc.enums.NodeEnums;

import com.github.pagehelper.PageInfo;

@Controller
public class CashController {

	@Autowired
	private UsdtCashService usdtCashService;
	@Autowired
	private RoleService roleService;
	
	
	
	@RequestMapping(value = MappingUtils.showCashUsdtlist)
	public ModelAndView showCashEthlist(CCashUsdtLogs cashLogs,HttpServletRequest request) {
		Integer roleid=PlatSession.getRoleId(request);
		if(!FunctionUtils.isEquals(1, roleid)) {
			cashLogs.setUserid(PlatSession.getUserId(request));
		}
		List<MtCliqueUser> users=roleService.queryUserList(request);
		List<CCashUsdtLogs> list=usdtCashService.queryCashUsdlist(cashLogs);
		PageInfo<CCashUsdtLogs> pageInfo = new PageInfo<CCashUsdtLogs>(list);
		Map<String, Object> map=usdtCashService.getCashmoney(cashLogs);
		ModelAndView view=new ModelAndView(MappingUtils.showCashUsdtlist);
		view.addObject("logs", cashLogs);
		view.addObject("map", map);
		view.addObject("pageInfo", pageInfo);
		view.addObject("users", users);
		view.addObject("roleid", roleid);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.handlerefundUsdtCash)
	@ResponseBody
	public RestResponse handlerefundEthCash(HttpServletRequest request, Integer id) {
		RestResponse result=usdtCashService.handlerefundUsdCash(id,PlatSession.getUserId(request));
		return result;
	}
	
	@RequestMapping(value = MappingUtils.showUsdtCheck)
	public ModelAndView showEthCheck(Integer id) {
		CCashUsdtLogs cashLogs=usdtCashService.getUsdtdetail(id);
		NodeEnums [] enums=NodeEnums.values();
		FeeEnums [] feeEnums=FeeEnums.values();
		ModelAndView view=new ModelAndView(MappingUtils.showUsdtCheck);
		view.addObject("cashLogs", cashLogs);
		view.addObject("enums", enums);
		view.addObject("feeEnums", feeEnums);
		return view;
	} 
	
	@RequestMapping(value = MappingUtils.handUsdtcheck)
	@ResponseBody
	public RestResponse handEthcheck(HttpServletRequest request,CCashUsdtLogs cashLogs,String nodeid,String feeaddr) {
		RestResponse result=usdtCashService.handEthcheck(cashLogs,nodeid,feeaddr,PlatSession.getUserId(request));
		return result;
	}
	
	@RequestMapping(value = MappingUtils.dzUsdtSuccess)
	@ResponseBody
	public RestResponse dzEthSuccess(HttpServletRequest request,Integer id) {
		RestResponse result=usdtCashService.dzEthSuccess(id,PlatSession.getUserId(request));
		return result;
	}
}
