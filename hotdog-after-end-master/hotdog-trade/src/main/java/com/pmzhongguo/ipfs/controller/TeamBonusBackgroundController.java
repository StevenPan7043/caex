package com.pmzhongguo.ipfs.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ipfs.entity.IpfsTeamBonus;
import com.pmzhongguo.ipfs.entity.IpfsUserBonus;
import com.pmzhongguo.ipfs.service.IpfsTeamBonusManager;
import com.pmzhongguo.ipfs.service.IpfsUserBonusManager;
import org.apache.commons.lang3.StringUtils;
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
public class TeamBonusBackgroundController extends TopController {

	@Autowired
	private IpfsTeamBonusManager ipfsTeamBonusManager;

	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "team_bonus_list", method = RequestMethod.GET)
	public String toListTeamBonus(HttpServletRequest request,
			HttpServletResponse response) {
		return "ipfs/team_bonus_list";
	}


	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "teamBonus", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listTeamBonus(HttpServletRequest request, HttpServletResponse response) {
		Map params = $params(request);
		List<IpfsTeamBonus> list = ipfsTeamBonusManager.findByConditionPage(params);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", params.get("total"));
		return map;
	}

	@RequestMapping(value = "team_bonus_detail_list", method = RequestMethod.GET)
	public String toListTeamDetailBonus(HttpServletRequest request,
								  HttpServletResponse response) {
		return "ipfs/team_bonus_detail_list";
	}

	@RequestMapping(value = "teamDetailBonus", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listTeamDetailBonus(HttpServletRequest request, HttpServletResponse response) {
//		Map params = $params(request);

		String bonusDate = $("bonusDate");
		String idStr = $("memberId");
		if(StringUtils.isBlank(bonusDate) ||StringUtils.isBlank(idStr)){
			return null;
		}
		int memberId = Integer.parseInt(idStr);
		List<Map<String, Object>> list = ipfsTeamBonusManager.getHashrateDetail(memberId, bonusDate);
		Map map = new HashMap();
		map.put("Rows", list);
//		map.put("Total", params.get("total"));
		map.put("Total", list.size());
		return map;
	}

}
