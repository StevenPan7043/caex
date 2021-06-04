package com.pmzhongguo.ex.business.controller;

import com.contract.common.FunctionUtils;
import com.contract.common.StaticUtils;
import com.contract.enums.HandleTypeEnums;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.service.BipbService;
import com.pmzhongguo.ex.business.service.CurrencyLockAccountDetailService;
import com.pmzhongguo.ex.business.service.CurrencyLockAccountService;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("backstage/member")
public class ContractMgrController extends TopController {

	@Autowired
	private DaoUtil daoUtil;

	@Autowired
	private MemberService memberService;

	@Autowired
	protected BipbService bipbService ;


	@Autowired
	private CurrencyLockAccountService currencyLockAccountService;

	@Autowired
	private CurrencyLockAccountDetailService currencyLockAccountDetailService;
	
	static{
		WebApplicationContext wac = ContextLoader
				.getCurrentWebApplicationContext();
//		ContractMgrController.currencyService = (CurrencyService) wac
//				.getBean("currencyService");
	}


	@RequestMapping(value = "vm_asserts_list", method = RequestMethod.GET)
	public String toListAsserts(HttpServletRequest request, HttpServletResponse response) {
		return "business/member/vm_asserts_list";
	}

	@RequestMapping(value = "vm_asserts", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listAsserts(HttpServletRequest request, HttpServletResponse response) {
//		Map param = $params(request);
		String query = "SELECT t.id, t.`phone`, t1.`type` currency,  t1.`balance`, t1.`zcbalance` FROM `c_customer` t\n" +
				"LEFT JOIN `c_wallet` t1 ON t1.`cid` = t.`id`\n" +
				"WHERE t.`identity` = 3 ";
		String phone = $("m_name");
		if(StringUtils.isNotBlank(phone)){
			query = query + " AND t.phone = " + phone;
		}
		List<Map> list = daoUtil.queryForList(query);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", list.size());
		return map;
	}

	@RequestMapping("/vm_asserts_edit")
	public String toEditUser(HttpServletRequest request, HttpServletResponse response) {
//		Map param = $params(request);
		String member_id = $("id");
		String currency = $("currency");
		List<Map> list = daoUtil.queryForList(
				"SELECT t.id, t.`phone`, t1.`type` currency,  t1.`balance`, t1.`zcbalance` FROM `c_customer` t\n" +
						"LEFT JOIN `c_wallet` t1 ON t1.`cid` = t.`id`\n" +
						"WHERE t.`identity` = 3 AND t1.type = ? AND t.id = ?",
				currency, member_id);
		Map map = list.get(0);
		BigDecimal balance = new BigDecimal(String.valueOf(map.get("balance")));
		BigDecimal zcbalance = new BigDecimal(String.valueOf(map.get("zcbalance")));
		map.put("balance", balance.toPlainString());
		map.put("zcbalance", zcbalance.toPlainString());
		$attr("info", map);
		return "business/member/vm_asserts_edit";
	}

	@RequestMapping(value = "/vm_asserts_edit_do", method = RequestMethod.POST)
	@ResponseBody
	public Resp editUser(HttpServletRequest request) {
		String id = $("id");
		String balance = $("balance");
		String zcbalance = $("zcbalance");
		String old_balance = $("old_balance");
		String old_zcbalance = $("old_zcbalance");
		modifyBalance(Integer.valueOf(id), new BigDecimal(old_balance), new BigDecimal(balance));
		modifyZCBalance(Integer.valueOf(id), new BigDecimal(old_zcbalance), new BigDecimal(zcbalance));
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	private void modifyBalance(Integer memberId, BigDecimal old, BigDecimal num){
		if(old.compareTo(num) == 0){
			return;
		}
		String paycode = "";
		if(num.compareTo(old) > 0){
			BigDecimal cost = num.subtract(old);
			paycode= FunctionUtils.getOrderCode("I");
			bipbService.handleCUsdtDetail(memberId, HandleTypeEnums.ADMIN_MODIFY_BALANCE.getId(), StaticUtils.pay_in, paycode, cost, HandleTypeEnums.ADMIN_MODIFY_BALANCE.getName(), memberId);
		}else{
			BigDecimal cost = old.subtract(num);
			paycode= FunctionUtils.getOrderCode("O");
			bipbService.handleCUsdtDetail(memberId, HandleTypeEnums.ADMIN_MODIFY_BALANCE.getId(), StaticUtils.pay_out, paycode, cost, HandleTypeEnums.ADMIN_MODIFY_BALANCE.getName(), memberId);

		}

	}

	private void modifyZCBalance(int memberId, BigDecimal old, BigDecimal num){
		if(old.compareTo(num) == 0){
			return;
		}
		String paycode = "";
		if(num.compareTo(old) > 0){
			BigDecimal cost = num.subtract(old);
			paycode= FunctionUtils.getOrderCode("I");
			bipbService.handleCZcDetail(memberId, HandleTypeEnums.ADMIN_MODIFY_ZCBALANCE.getId(), StaticUtils.pay_in, paycode, cost, HandleTypeEnums.ADMIN_MODIFY_ZCBALANCE.getName(), memberId);
		}else{
			BigDecimal cost = old.subtract(num);
			paycode= FunctionUtils.getOrderCode("O");
			bipbService.handleCZcDetail(memberId, HandleTypeEnums.ADMIN_MODIFY_ZCBALANCE.getId(), StaticUtils.pay_out, paycode, cost, HandleTypeEnums.ADMIN_MODIFY_ZCBALANCE.getName(), memberId);

		}
	}
}
