package com.pmzhongguo.ex.business.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 收入返还
 * 
 * @author Administrator
 *
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/irb")
public class IncomeReturnBackController {
	private static final Logger LOGGER = LoggerFactory.getLogger(IncomeReturnBackController.class);

	/**
	 * 交易返币
	 * 
	 * @return
	 */
	@GetMapping("/return_coin_list")
	public String returnCoinListView() {
		return "business/irb/return_coin_list";
	}

	/**
	 * 交易返币
	 * 
	 * @return
	 */
	@GetMapping("/return_procedure_fee_list")
	public String returnProcedureFeeListView() {
		return "business/irb/return_procedure_fee_list";
	}

}
