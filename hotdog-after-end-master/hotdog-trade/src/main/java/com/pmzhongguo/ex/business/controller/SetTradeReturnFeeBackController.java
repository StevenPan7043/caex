package com.pmzhongguo.ex.business.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.SetTradeReturnFeeList;
import com.pmzhongguo.ex.business.service.SetTradeReturnFeeService;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;

/**
 * 后台管理 - 返还手续费设置
 * 
 * @author Administrator
 *
 */
@ApiIgnore
@Controller
@RequestMapping("/strf")
public class SetTradeReturnFeeBackController extends TopController {

	@Resource
	private SetTradeReturnFeeService setTradeReturnFeeService;

	/**
	 * 设置页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "set")
	public String set(HttpServletRequest request, HttpServletResponse response) {
		// 查询设置信息
		List<Map<String, Object>> list = setTradeReturnFeeService.getSet();
		$attr("list", list);
		return "business/strf/set";
	}

	/**
	 * 修改设置信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value = "edit_set")
	@ResponseBody
	public Resp editSet(SetTradeReturnFeeList lists) {
		setTradeReturnFeeService.editSet(lists.getLists());
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

}
