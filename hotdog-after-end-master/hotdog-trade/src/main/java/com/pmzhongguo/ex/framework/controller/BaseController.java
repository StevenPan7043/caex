package com.pmzhongguo.ex.framework.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.PaginData;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.service.CommonService;

@ApiIgnore
@Controller
public class BaseController extends TopController {

	@Resource
	private CommonService commonService;


	@InitBinder
	protected void ininBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

	/**
	 * 下拉列表-弹出列表
	 */
	@RequestMapping("/common/grid")
	@ResponseBody
	public Map grid(HttpServletRequest request, HttpServletResponse response) {

		PaginData pd = commonService.getCommonList($params(request));

		Map map = new HashMap();
		map.put("Rows", pd.getList());
		map.put("Total", pd.getCount());
		return map;
	}
	
	/**
	 * 下拉列表-取字典值 code参数规则：code#type， 如果type=id，则id为id、text为dic_data_name
	 * 如果type=name，则id为dic_data_name、text为dic_data_name
	 * 如性别：sex$$id，代表性别以id方式取，取到的值为 1-男 0-女 sex$$text，代表性别以text方式取，取到的值为 男-男 女-女
	 */
	@RequestMapping("/common/select")
	@ResponseBody
	public ObjResp select(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> data = null;
		String _code = $("_code");
		if (_code.indexOf("~") > -1) {
			String[] codes = _code.split("~");
			data = commonService.getComboData(codes);
		} else {
			data = commonService.getComboData(new String[] { $("_code") });
		}
		return new ObjResp(Resp.SUCCESS, "success", data);
	}
	
	@RequestMapping("/noau_listArticleColumnBySiteId")
	@ResponseBody
	public ObjResp listArticleColumnBySiteId(HttpServletRequest request,
			HttpServletResponse response) {
		Map map = new HashMap();
		int siteId = $int("siteId", 0);
		if (siteId > 0) {
			map.put("columnsList", commonService.getLeafColumnsBySiteId(HelpUtils
					.newHashMap("siteId", siteId)));
		}
		return new ObjResp(Resp.SUCCESS, "success", map);
	}
	
	@RequestMapping("/noau_listParentColumnBySiteId")
	@ResponseBody
	public ObjResp listParentColumnBySiteId(HttpServletRequest request,
			HttpServletResponse response) {
		Map map = new HashMap();
		int siteId = $int("siteId", 0);
		int excludeId = $int("excludeId", 0);
		if (siteId > 0) {
			map.put("columnsList", commonService.getColumnsBySiteId(HelpUtils
					.newHashMap("siteId", siteId, "excludeId", excludeId)));
		}
		return new ObjResp(Resp.SUCCESS, "success", map);
	}

	@RequestMapping("/error")
	public String errorDispatch(HttpServletRequest request,
			HttpServletResponse response) {
		String flag = request.getParameter("flag");
		String errMsg = "";
		switch (Integer.parseInt(flag)) {
		case 1:
			errMsg = "您没有登录或长时间未操作系统，请重新登录！";
			break;
		case 2:
			errMsg = "您没有访问该功能的权限！";
			break;
		case 3:
			errMsg = "系统权限认证错误，请与管理员联系！";
			break;
		default:
			errMsg = "";

		}
		request.setAttribute("error", errMsg);
		request.setAttribute("flag", flag);
		return "error/error";
	}
}
