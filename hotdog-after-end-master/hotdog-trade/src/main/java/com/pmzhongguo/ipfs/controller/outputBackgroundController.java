package com.pmzhongguo.ipfs.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ipfs.entity.IpfsOutput;
import com.pmzhongguo.ipfs.entity.IpfsUserBonus;
import com.pmzhongguo.ipfs.service.IpfsOutputManager;
import com.pmzhongguo.ipfs.service.IpfsUserBonusManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@Controller
@RequestMapping("backstage/ipfs")
public class outputBackgroundController extends TopController {

	@Autowired
	private IpfsOutputManager ipfsOutputManager;

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "output_list", method = RequestMethod.GET)
	public String toListOutput(HttpServletRequest request,
			HttpServletResponse response) {
		return "ipfs/output_list";
	}


	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "output", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listOutput(HttpServletRequest request, HttpServletResponse response) {
		Map params = $params(request);
		List<IpfsOutput> list = ipfsOutputManager.findByConditionPage(params);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", params.get("total"));
		return map;
	}

}
