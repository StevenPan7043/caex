package com.contract.service.cms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.contract.dto.UserDto;
import com.contract.entity.MtCliqueMenus;

/**
 * 平台session
 * 
 * @author YQQ
 *
 */

public class PlatSession {

	/** 验证码 */
	public static final String VALID_CODE = "validCode";

	/** 角色id */
	public static final String ROLE_ID = "roleId";

	/** 用户id */
	public static final String USER_ID = "userId";

	/** 菜单 */
	public static final String MENU_LIST = "menusList";

	/** 操作员 */
	public static final String MT_USER = "mtUser";
	
	public static final String menu_urls="menu_urls";

	/**
	 * 退出
	 */
	public static void exit(HttpServletRequest request) {
		request.getSession().removeAttribute(MENU_LIST);
		request.getSession().removeAttribute(USER_ID);
		request.getSession().removeAttribute(ROLE_ID);
		request.getSession().removeAttribute(MT_USER);
		request.getSession().removeAttribute(menu_urls);
	}

	/**
	 * 用户设置session
	 */
	public static void setMtUser(HttpServletRequest request, UserDto mtUser) {
		request.getSession().removeAttribute(MT_USER);
		request.getSession().setAttribute(MT_USER, mtUser);
	}

	/**
	 * 用户获取session
	 */
	public static UserDto getMtUser(HttpServletRequest request) {
		return (UserDto) request.getSession().getAttribute(MT_USER);
	}

	/**
	 * 菜单设置session
	 */
	public static void setMenuList(HttpServletRequest request, List<MtCliqueMenus> menuList) {
		request.getSession().removeAttribute(MENU_LIST);
		request.getSession().setAttribute(MENU_LIST, menuList);
	}

	/**
	 * 菜单获取session
	 */
	@SuppressWarnings("unchecked")
	public static List<MtCliqueMenus> getMenuList(HttpServletRequest request) {
		return (List<MtCliqueMenus>) request.getSession().getAttribute(MENU_LIST);
	}
	
	/**
	 * 菜单设置session
	 */
	public static void setMenuUrlList(HttpServletRequest request, List<String> menuurls) {
		request.getSession().removeAttribute(menu_urls);
		request.getSession().setAttribute(menu_urls, menuurls);
	}

	/**
	 * 菜单获取session
	 */
	@SuppressWarnings("unchecked")
	public static List<String> getMenuUrlList(HttpServletRequest request) {
		return (List<String>) request.getSession().getAttribute(menu_urls);
	}

	/**
	 * 用户id设置session
	 */
	public static void setUserId(HttpServletRequest request, Integer userId) {
		request.getSession().removeAttribute(USER_ID);
		request.getSession().setAttribute(USER_ID, userId);
	}

	/**
	 * 用户id获取session
	 */
	public static Integer getUserId(HttpServletRequest request) {
		return (Integer) request.getSession().getAttribute(USER_ID);
	}

	/**
	 * 角色id设置session
	 */
	public static void setRoleId(HttpServletRequest request, Integer roleId) {
		request.getSession().removeAttribute(ROLE_ID);
		request.getSession().setAttribute(ROLE_ID, roleId);
	}

	/**
	 * 角色id获取session
	 */
	public static Integer getRoleId(HttpServletRequest request) {
		return (Integer) request.getSession().getAttribute(ROLE_ID);
	}

	/**
	 * 验证码对象设置session
	 */
	public static void setValidCode(HttpServletRequest request, String validCode) {
		request.getSession().removeAttribute(VALID_CODE);
		request.getSession().setAttribute(VALID_CODE, validCode);
	}

	/**
	 * 验证码对象获取session
	 */
	public static String getValidCode(HttpServletRequest request) {
		return (String) request.getSession().getAttribute(VALID_CODE);
	}
	
}
