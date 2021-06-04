package com.pmzhongguo.ex.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.pmzhongguo.ex.business.mapper.CustomServiceMapper;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pmzhongguo.ex.business.service.AppUpdateService;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

/**
 * app更新设置功能
 * 
 * @author zengweixiong
 *
 */
@Api(value = "app更新设置功能", description = "app更新设置功能", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("au")
public class AppUpdateController extends TopController {

	@Resource
	private AppUpdateService appUpdateService;

	/**
	 * 获取APP更新设置信息
	 * 
	 * @param request
	 * @return
	 */
	@ApiOperation(value = " 获取APP更新设置信息", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
	@RequestMapping(value = "info/{type}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp info(@PathVariable("type") Integer type, HttpServletRequest request) {

		Map<String,Object> appInfoMap = (Map<String,Object>)request.getServletContext().getAttribute(HelpUtils.APP_INFO_MAP);
		if (!HelpUtils.nullOrBlank(appInfoMap)) {
			Map<String, Object> result = (Map<String, Object>)appInfoMap.get(type);
			if(!HelpUtils.nullOrBlank(result)) {
				return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, result);
			}
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("type", type);
		Map<String, Object> map = appUpdateService.info(param);
		appUpdateService.cacheAppUpdateInfo(request.getServletContext());
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, map);
	}


	@ApiOperation(value = "获取客服二维码",httpMethod = "GET")
	@RequestMapping(value = "customerService", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp info(HttpServletRequest request) {
		//List<Map> info = appUpdateService.findAllQrCode();
		List<Map> info = (List<Map>) request.getServletContext().getAttribute(HelpUtils.CUSTOM_SERVICE_QR_CODE_LST);
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, info);
	}
}
