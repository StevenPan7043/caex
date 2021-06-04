package com.pmzhongguo.ex.business.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.service.VoteService;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;

/**
 * 后台管理 - 投票管理
 * 
 * @author Administrator
 *
 */
@ApiIgnore
@Controller
@RequestMapping("/backstage/voteb")
public class VoteBackController extends TopController {

	@Resource
	private VoteService voteService;

	/**
	 * 所有币种
	 * 
	 * @return
	 */
	@GetMapping("/coins")
	@ResponseBody
	public ObjResp coins() {
		List<Map<Integer, String>> list = voteService.coins();
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
	}

	/**
	 * 投票设置页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "set")
	public String set(HttpServletRequest request, HttpServletResponse response) {
		// 查询设置信息
		Map<String, Object> vote = voteService.getSet();
		$attr("vote", vote);
		return "business/vote/set";
	}

	/**
	 * 修改投票设置信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value = "edit_set")
	@ResponseBody
	public Resp editSet(HttpServletRequest request, HttpServletResponse response) {
		// 编辑参数
		Map<String, Object> param = $params(request);
		// 编辑投票设置信息
		voteService.editSet(param);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 投票列表页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		// 查询设置信息
		try {
			Map<String, Object> data = voteService.info();
			$attr("data", data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "business/vote/list";
	}

	/**
	 * 投票列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping(value = "list_data")
	@ResponseBody
	public Map<String, Object> listData(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = $params(request);
		List<Map<String, Object>> list = voteService.list();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

}
