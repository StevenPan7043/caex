package com.pmzhongguo.ipfs.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ipfs.entity.IpfsHashrate;
import com.pmzhongguo.ipfs.service.IpfsHashrateManager;
import com.pmzhongguo.ipfs.service.IpfsProjectManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@Controller
@RequestMapping("backstage/ipfs")
public class HashrateBackgroundController extends TopController {

	@Autowired
	private IpfsHashrateManager ipfsHashrateManager;

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "hashrate_list", method = RequestMethod.GET)
	public String toListHashrate(HttpServletRequest request,
			HttpServletResponse response) {
		return "ipfs/hashrate_list";
	}


	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "hashrate", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listHashrate(HttpServletRequest request, HttpServletResponse response) {
		Map params = $params(request);
		List<IpfsHashrate> list = ipfsHashrateManager.findByConditionPage(params);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", params.get("total"));
		return map;
	}
	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "hashrate_edit",method = RequestMethod.GET)
	public String toEditHashrate(HttpServletRequest request,HttpServletResponse response){
		IpfsHashrate info = ipfsHashrateManager.selectByPrimaryKey($int("id"));
		$attr("info", info);
		return "ipfs/hashrate_edit";
	}

	/**
	 *
	 * @param request
	 * @param record
	 * @return
	 */
	@RequestMapping(value = "hashrate_edit_do",method = RequestMethod.POST)
	@ResponseBody
	public Resp editHashrate(HttpServletRequest request,  IpfsHashrate record) {
		ipfsHashrateManager.updateByPrimaryKeySelective(record);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "PLHashrate_edit",method = RequestMethod.POST)
	@ResponseBody
	public Resp PLHashrateEdit(HttpServletRequest request,HttpServletResponse response) {
		return ipfsHashrateManager.PLHashrateEdit($params(request));
	}
}
