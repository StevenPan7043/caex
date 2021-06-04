package com.pmzhongguo.ipfs.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ipfs.entity.IpfsIntroBonus;
import com.pmzhongguo.ipfs.entity.IpfsUserBonus;
import com.pmzhongguo.ipfs.service.IpfsIntroBonusManager;
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
public class IntroBonusBackgroundController extends TopController {

	@Autowired
	private IpfsIntroBonusManager ipfsIntroBonusManager;

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "intro_bonus_list", method = RequestMethod.GET)
	public String toListIntroBonus(HttpServletRequest request,
			HttpServletResponse response) {
		return "ipfs/intro_bonus_list";
	}


	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "introBonus", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listIntroBonus(HttpServletRequest request, HttpServletResponse response) {
		Map params = $params(request);
		List<IpfsIntroBonus> list = ipfsIntroBonusManager.findByConditionPage(params);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", params.get("total"));
		return map;
	}

}
