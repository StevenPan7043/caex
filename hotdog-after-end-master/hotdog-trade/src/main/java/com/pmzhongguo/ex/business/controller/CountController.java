package com.pmzhongguo.ex.business.controller;

import java.text.SimpleDateFormat;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.service.CountService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;

/**
 * 统计接口
 * 
 * @author zengweixiong
 */
@ApiIgnore
@Controller
@RequestMapping("backstage/")
public class CountController extends TopController {

	@Resource
	private CountService countService;

	/**
	 * 每天统计
	 * 
	 * @return
	 */
	@RequestMapping("count/today")
	public String today(HttpServletRequest request) {
		String startDate = $("startDate");
		String endDate = $("endDate");
		$attr("startDate", startDate);
		$attr("endDate", endDate);
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
			Map<String, Object> map = countService.countToday(HelpUtils.newHashMap("startDate", startDate + ":00", "endDate", endDate + ":00"));
			$attr("map", map);
		}
		return "business/data/count_today";
	}

}
