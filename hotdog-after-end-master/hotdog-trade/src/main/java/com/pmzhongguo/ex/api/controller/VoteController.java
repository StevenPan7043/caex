package com.pmzhongguo.ex.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pmzhongguo.ex.business.service.VoteService;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * 投票功能
 * 
 * @author zengweixiong
 *
 */
@Api(value = "投票功能接口", description = "会员投票，需登录认证", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("vote")
public class VoteController extends TopController {

	@Resource
	private VoteService voteService;

	/**
	 * 投票是否开启接口
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ApiOperation(value = "投票是否开启", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "isopen", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp isopen() {
		Map<String, Object> map = voteService.getSet();
		Integer state = Integer.parseInt("" + map.get("isopen"));
		if (0 == state) {
			return new ObjResp(1, "未开启", 0);
		}
		return new ObjResp(1, "开启", 1);
	}

	/**
	 * 投票结束时间接口
	 * 
	 * @return
	 */
	@ApiOperation(value = "投票结束时间接口", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "endtime", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp endtime() {
		Map<String, Object> map = voteService.getSet();
		return new ObjResp(1, "投票结束时间", map.get("endtime"));
	}

	/**
	 * 投票情况接口
	 * 
	 * @return
	 */
	@ApiOperation(value = "投票情况接口", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "info", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp info() {
		Map<String, Object> map = voteService.info();
		return new ObjResp(1, "投票情况接口: oppose 反对; like 赞成", map);
	}

	/**
	 * 投票列表
	 * 
	 * @param pagesize
	 *            页码
	 * @param size
	 * @param uid
	 * @return
	 */
	@ApiOperation(value = "投票列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/list/{pagesize}/{size}/{uid}")
	@ResponseBody
	public ObjResp list(@PathVariable("pagesize") Integer pagesize, @PathVariable("size") Integer size,
			@PathVariable("uid") Integer uid) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("size", size);
		param.put("uid", uid);
		param.put("startsize", (pagesize - 1) * size);

		Map<String, Object> map = voteService.apiList(param);
		return new ObjResp(1, "投票列表", map);
	}

	/**
	 * 投票点击查询币种信息
	 * 
	 * @return
	 */
	@ApiOperation(value = "投票点击查询币种信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/searchcoin")
	@ResponseBody
	public ObjResp searchcoin() {
		Map<String, Object> map = voteService.getSet();
		map.put("currency", map.get("currency"));
		map.put("currencyNum", Integer.parseInt("" + map.get("num")));
		return new ObjResp(1, "投票点击查询币种信息", map);
	}

	/**
	 * 赞成票接口
	 * 
	 * @return
	 */
	@ApiOperation(value = "赞成票接口", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/like/{uid}/{pollNum}")
	@ResponseBody
	public ObjResp like(@PathVariable("uid") Integer uid, @PathVariable("pollNum") Integer pollNum) {
		voteService.add(uid, pollNum, 1);
		return new ObjResp(1, "赞成票接口", null);
	}

	/**
	 * 反对票接口
	 * 
	 * @return
	 */
	@ApiOperation(value = "反对票接口", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping("/oppose/{uid}/{pollNum}")
	@ResponseBody
	public ObjResp oppose(@PathVariable("uid") Integer uid, @PathVariable("pollNum") Integer pollNum) {
		voteService.add(uid, pollNum, 0);
		return new ObjResp(1, "反对票接口", null);
	}
	
	

}
