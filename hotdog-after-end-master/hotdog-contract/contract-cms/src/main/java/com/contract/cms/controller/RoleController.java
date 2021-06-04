package com.contract.cms.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.contract.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.contract.cms.common.MappingUtils;
import com.contract.entity.MtCliqueMenus;
import com.contract.entity.MtCliqueRoles;
import com.contract.entity.MtCliqueUser;
import com.contract.service.cms.RoleService;


@Controller
public class RoleController {

	@Autowired
	private RoleService roleService;
	/**
	 * 查询角色
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showRoleList)
	public ModelAndView showRoleList(HttpServletRequest request) {
		List<MtCliqueRoles> list=roleService.queryRoles(request);
		ModelAndView view= new ModelAndView(MappingUtils.showRoleList);
		view.addObject("list", list);
		return view;
	}
	
	/**
	 * 修改角色状态
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = MappingUtils.updateRolestatus)
	@ResponseBody
	public RestResponse updateRolestatus(Integer id, Integer status) {
		RestResponse result=roleService.updateRolestatus(id,status);
		return result;
	}
	
	/**
	 * 跳转新增编辑角色页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showEditRole)
	public ModelAndView showEditRole(HttpServletRequest request,Integer id) {
		MtCliqueRoles cliqueRoles=roleService.getRoleById(id);
		List<MtCliqueMenus> list= roleService.queryMenus(request,id);
		ModelAndView view= new ModelAndView(MappingUtils.showEditRole);
		view.addObject("id", id);
		view.addObject("list", list);
		view.addObject("cliqueRoles", cliqueRoles);
		return view;
	}
	
	/**
	 * 编辑角色
	 * @param roles
	 * @param menuidlist
	 * @return
	 */
	@RequestMapping(value = MappingUtils.editRole)
	@ResponseBody
	public RestResponse editRole(MtCliqueRoles roles,HttpServletRequest request) {
		RestResponse result=roleService.editRole(roles,request);
		return result;
	}
	
	/**
	 * 显示操作员
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showUserList)
	public ModelAndView showUserList(HttpServletRequest request) {
		List<MtCliqueUser> list=roleService.queryUserList(request);
		ModelAndView view= new ModelAndView(MappingUtils.showUserList);
		view.addObject("list", list);
		return view;
	}
	
	/**
	 * 修改操作员
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = MappingUtils.updateUserstatus)
	@ResponseBody
	public RestResponse updateUserstatus(Integer id,Integer status) {
		RestResponse result=roleService.updateUserstatus(id,status);
		return result;
	}
	
	/**
	 * 编辑操作员
	 * @param id
	 * @return
	 */
	@RequestMapping(value = MappingUtils.showEditUser)
	public ModelAndView showEditUser(HttpServletRequest request,Integer id) {
		List<MtCliqueRoles> list=roleService.querySysroles(request);
		MtCliqueUser cliqueUser=roleService.getUserById(id);
		ModelAndView view =new ModelAndView(MappingUtils.showEditUser);
		view.addObject("cliqueUser", cliqueUser);
		view.addObject("list", list);
		view.addObject("id", id);
		return view;
	}
	
	/**
	 * 编辑集团操作员
	 * @param cliqueUser
	 * @return
	 */
	@RequestMapping(value = MappingUtils.editUser)
	@ResponseBody
	public RestResponse editUser(MtCliqueUser cliqueUser,HttpServletRequest request) {
		RestResponse result=roleService.editUser(cliqueUser,request);
		return result;
	}
	
	/**
	 * 重置操作员密码
	 * @return
	 */
	@RequestMapping(value = MappingUtils.restUserPwd)
	@ResponseBody
	public RestResponse restUserPwd(Integer id) {
		RestResponse result=roleService.restUserPwd(id);
		return result;
	}
}
