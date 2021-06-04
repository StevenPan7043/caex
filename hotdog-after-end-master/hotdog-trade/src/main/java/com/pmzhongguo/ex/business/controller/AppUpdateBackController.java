package com.pmzhongguo.ex.business.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.service.AppUpdateService;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;

/**
 * 后台管理 - APP更新设置信息管理
 * 
 * @author zengweixiong
 *
 */
@ApiIgnore
@Controller
@RequestMapping("backstage/aub")
public class AppUpdateBackController extends TopController {

	@Resource
	private AppUpdateService appUpdateService;

	private static Logger zkLog = LoggerFactory.getLogger("zookeeper");
	/**
	 * 设置页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "set_view")
	public String setView(HttpServletRequest request, HttpServletResponse response) {
		// 查询所有的设置信息
		List<Map<String, Object>> list = appUpdateService.list();
		$attr("android", list.get(0));
		$attr("ios", list.get(1));
		return "business/appupdate/set";
	}

	/**
	 * 修改设置信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping(value = "edit")
	@ResponseBody
	public Resp edit(HttpServletRequest request, HttpServletResponse response) {
		// 编辑参数
		Map<String, Object> param = $params(request);
		// 编辑投票设置信息
		appUpdateService.edit(param);
		// app更新信息
		JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_APP,JedisChannelConst.SYNC_MESSAGE);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}



	/**
	 * 客服二维码设置页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("set_QR_code")
	public String toQRCodeList(HttpServletRequest request, HttpServletResponse response){
		return "business/appupdate/qr_code_list";
	}

	/**
	 * 后台查询
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qr/list", method = RequestMethod.GET)
	@ResponseBody
	public Map findMgrByPage(HttpServletRequest request) {
		Map<String,Object> params = $params(request);
		List<Map<String,Object>> list =  appUpdateService.findMgrByPage(params);
		Map<String,Object> map = new HashMap<String,Object>(4);
		map.put("Rows", list);
		map.put("Total", params.get("total"));
		return map;
	}

	/**
	 * 新增/编辑
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qr/edit_page", method = RequestMethod.GET)
	@ApiIgnore
	public String toAddProjectOfMgr(HttpServletRequest request) {
		Integer id = $int("id");
		if(!HelpUtils.nullOrBlank(id)) {
			Map<String,Object> qrCode = appUpdateService.findQrCodeInfoById(id);
			$attr("info",qrCode);
		}
		for (Map.Entry<String, String> entry : getAliPolicy().entrySet()) {
			$attr(entry.getKey(), entry.getValue());
		}
		return "business/appupdate/qr_code_edit";
	}

	/**
	 * 新增/编辑
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qr/edit", method = RequestMethod.POST)
	@ApiIgnore
	@ResponseBody
	public Resp addProjectOfMgr(HttpServletRequest request) {
		Integer id = $int("id");
		Map<String, Object> params = $params(request);
		if(HelpUtils.nullOrBlank(id)) {
			appUpdateService.addQrCode(params);
		}else {
			appUpdateService.updateQrCode(params);
		}
		JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_APP,JedisChannelConst.SYNC_MESSAGE);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 删除
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/qr/del", method = RequestMethod.POST)
	@ApiIgnore
	@ResponseBody
	public Resp delProjectOfMgr(HttpServletRequest request) {
		Integer id = $int("id");
		appUpdateService.delQrCode(id);
		JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_APP,JedisChannelConst.SYNC_MESSAGE);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}
}
