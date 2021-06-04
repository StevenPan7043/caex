package com.contract.cms.controller;

import javax.servlet.http.HttpServletRequest;

import com.contract.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.contract.cms.common.MappingUtils;
import com.contract.service.cms.BaseService;


@Controller
public class BaseController {

	@Autowired
	private BaseService baseService;
	/**
	 * 显示修改密码页面
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showEditPassword)
	public ModelAndView showEditPassword() {
		ModelAndView view=new ModelAndView(MappingUtils.showEditPassword);
		return view;
	}
	
	@RequestMapping(value = MappingUtils.editPassword)
	@ResponseBody
	public RestResponse editPassword(HttpServletRequest request, String password, String newPassword, String newPasswordConfirm) {
		RestResponse result=baseService.editPassword(request, password, newPassword, newPasswordConfirm);
		return result;
	}
	
}
