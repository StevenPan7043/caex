package com.pmzhongguo.ex.framework.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.MacMD5;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.entity.FrmUser;
import com.pmzhongguo.ex.framework.service.FrmUserService;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;


@ApiIgnore
@Controller
public class UserController extends TopController {

	@Resource
	private FrmUserService userService;

	@RequestMapping("/frm/toListUser")
	public String toListUser(HttpServletRequest request,
			HttpServletResponse response) {

		return "framework/user/user_list";
	}

	@RequestMapping(value = "/frm/listUser", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listUser(HttpServletRequest request, HttpServletResponse response) {
		// String user_real_name = $("user_real_name");
		Map param = $params(request);
		// param.put("user_real_name",user_real_name);
		List list = userService.getAllUser(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	@RequestMapping(value = "/frm/resetUserPassword", method = RequestMethod.POST)
	@ResponseBody
	public Resp resetUserPassword() {
		Integer id = $int("id");
		userService.resetUserPassword(id);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	@RequestMapping("/frm/loadUserFuncs")
	public String loadUserFuncs() {
		Integer userId = $int("id");
		List<Map<String, Object>> functions = userService.getAllFuncs(userId);
		$attrs("functions", functions, "objectId", userId, "operatorType",
				"user");

		return "framework/user/right";
	}

	@RequestMapping(value = "/frm/saveRight", method = RequestMethod.POST)
	@ResponseBody
	public Resp saveRight() {
		Integer objectId = $int("objectId");
		String operatorType = $("operatorType");
		String funcIds = $("funcIds");
		userService.saveRight(objectId, operatorType, funcIds);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	@RequestMapping("/frm/toEditUser")
	public String toEditUser(HttpServletRequest request,
			HttpServletResponse response) {
		Integer userId = $int("id");
		FrmUser info = this.userService.getUser(userId);
		if (HelpUtils.nullOrBlank(info.getGoogle_auth_key())) {
			GoogleAuthenticator gAuth = new GoogleAuthenticator();
			final GoogleAuthenticatorKey key = gAuth.createCredentials();
			info.setGoogle_auth_key(key.getKey());
		}
		$attr("info", info);
		return "framework/user/user_edit";
	}

	@RequestMapping("/frm/toAddUser")
	public String toAddUser(HttpServletRequest request,
			HttpServletResponse response) {
		attrCommonDateTime();
		FrmUser info = new FrmUser();
		GoogleAuthenticator gAuth = new GoogleAuthenticator();
		final GoogleAuthenticatorKey key = gAuth.createCredentials();
		info.setGoogle_auth_key(key.getKey());
		$attr("info", info);
		return "framework/user/user_edit";
	}

	@RequestMapping(value = "/frm/addUser", method = RequestMethod.POST)
	@ResponseBody
	public Resp addUser(FrmUser user, Model model) {
		userService.addUser(user);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	@RequestMapping(value = "/frm/editUser", method = RequestMethod.POST)
	@ResponseBody
	public Resp editUser(FrmUser user, Model model) {
		Integer userId = $int("userId");
		user.setId(userId);
		userService.editUser(user);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	@RequestMapping(value = "/frm/delUser", method = RequestMethod.POST)
	@ResponseBody
	public Resp delUser() {
		Integer id = $int("id");
		userService.delUser(id);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	@RequestMapping("/noau_toupdatePassword")
	public String toUpdatePassword(HttpServletRequest request,
			HttpServletResponse response) {
		$attr("uType", "USER");
		return "framework/user/update_password";
	}

	@RequestMapping("/noau_updatePassword")
	@ResponseBody
	public Resp updatePassword(HttpServletRequest request,
			HttpServletResponse response) {

		String newPassword = $("newPassword");
		String oldPassword = $("oldPassword");

		FrmUser user = HelpUtils.getFrmUser();
		FrmUser paramsUser = new FrmUser();
		paramsUser.setUser_pwd(MacMD5.CalcMD5(oldPassword));
		paramsUser.setUser_name(user.getUser_name());
		List<FrmUser> userList = userService.getLoginUser(paramsUser);
		if (null == userList || userList.size() == 0) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.PASSWORD_ERROR.getErrorCNMsg(), "ReLogin");
		} else {
			this.daoUtil.update(
					"UPDATE frm_user u SET u.user_pwd=? WHERE u.id=?",
					MacMD5.CalcMD5(newPassword), user.getId());
		}
		return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, "ReLogin");
	}
}
