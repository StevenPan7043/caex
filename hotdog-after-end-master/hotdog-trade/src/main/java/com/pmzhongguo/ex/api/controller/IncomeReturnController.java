package com.pmzhongguo.ex.api.controller;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 收入返还
 * 
 * @author Administrator
 *
 */
@Api(tags = "收入返还", value = "收入返还", description = "收入返还, 需要登录", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/ir")
public class IncomeReturnController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IncomeReturnController.class);

	/**
	 * 展示总的返还信息
	 * 
	 * @return
	 */
	@ApiOperation(value = "展示总的返还信息", notes = "统计信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/returnInfo")
	@ResponseBody
	public ObjResp returnInfo() {
		LOGGER.debug("参考 https://www.fcoin.com 的banner ");
		return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
	}

	/**
	 * 每天返币列表
	 * 
	 * @param coin
	 *            币种
	 * @return
	 */
	@ApiOperation(value = "每天返币列表", notes = "用户查看每天返币列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/returnCoinList/{coin}")
	@ResponseBody
	public ObjResp returnCoinList(@PathVariable("coin") String coin) {
		LOGGER.debug("接收的参数coin => {} ", coin);
		return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
	}

	/**
	 * 每天返交易手续费列表
	 * 
	 * @param coin
	 *            币种
	 * @return
	 */
	@ApiOperation(value = "每天返交易手续费列表", notes = "用户查看每天返交易手续费列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/returnProcedureFeeList/{coin}")
	@ResponseBody
	public ObjResp returnProcedureFeeList(@PathVariable("coin") String coin) {
		LOGGER.debug("接收的参数coin => {} ", coin);
		return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
	}

}
