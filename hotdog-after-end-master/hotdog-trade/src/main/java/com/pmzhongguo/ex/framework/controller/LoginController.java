package com.pmzhongguo.ex.framework.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.vo.OperLogExcelVo;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.MacMD5;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.entity.FrmUser;
import com.pmzhongguo.ex.framework.service.FrmUserService;
import com.pmzhongguo.zzextool.utils.ExportExcel;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@Controller
public class LoginController extends TopController {
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Resource
	private FrmUserService userService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}

	@RequestMapping("/adminpmzhongguo")
	public String adminpmzhongguo(HttpServletRequest request,
			HttpServletResponse response) {
		return "login";
	}

	/**
	 * //ModelAndView 返回类型
	 * 通过ModelAndView构造方法可以指定返回的页面名称，也可以通过setViewName()方法跳转到指定的页面 ,
	 * 使用addObject()设置需要返回的值，addObject()有几个不同参数的方法，可以默认和指定返回对象的名字。
	 * 调用addObject()方法将值设置到一个名为ModelMap的类属性，ModelMap是LinkedHashMap的子类， 具体请看类。
	 */
	@RequestMapping("/noau_login")
	@ResponseBody
	public Resp login(ModelMap map, @ModelAttribute("form") FrmUser userInfo,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		// 查找用户
		FrmUser user = null;
		try {
			// 验证码
			if (HelpUtils.getMgrConfig().getIs_use_validate() == 1) {
				String validateCode = $("validate_code");
				if (!((String) session.getAttribute("validateCode")).equalsIgnoreCase(validateCode)) {
					return new Resp(Resp.FAIL,
							ErrorInfoEnum.LANG_ILLEGAL_CHECK_CODE.getErrorCNMsg());
				}
			}

			String uid = request.getParameter("uid");
			String ClientDigest = request.getParameter("ClientDigest");
			String Random = (String) session.getAttribute("Random");

			userInfo.setUser_pwd(MacMD5.CalcMD5(userInfo.getUser_pwd()));
			List<FrmUser> list = userService.getLoginUser(userInfo);
			if (list.size() > 0) {
				user = list.get(0);

				if (0 == user.getIs_can_login()) {
					return new Resp(Resp.FAIL, ErrorInfoEnum.ACCOUNT_HAS_LOCKED.getErrorCNMsg());
				}

				GoogleAuthenticator gAuth = new GoogleAuthenticator();
				if (!gAuth.authorize(user.getGoogle_auth_key(), Integer.parseInt(userInfo.getGoogle_auth_key()))) {
					return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_GOOGLE_CODE_ERROR.getErrorCNMsg());
				}
				
				// 用户信息
				session.setAttribute(Constants.SYS_SESSION_USER, user);
				// 用户权限
				List<Map<String, Object>> ls = this.userService
						.getUserRightList(user.getId());
				request.getSession().setAttribute(Constants.USER_FUNCTION_KEY,
						ls);

				// 用户菜单
				session.setAttribute("userMune", userService.loadUserMune(user));
				request.setAttribute("user", user);

				logger.error(user.getUser_name() + ":enter logon");
			} else {
				// 用户不存在
				return new Resp(Resp.FAIL, ErrorInfoEnum.ACCOUNT_OR_PASSWORD_ERROR.getErrorCNMsg());
			}
		} catch (Exception e) {
			logger.warn("用户登录信息错误！", e);
			return new Resp(Resp.FAIL, ErrorInfoEnum.LOGIN_INFO_ERROR.getErrorCNMsg());
		}
		return new Resp(Resp.SUCCESS, "success");
	}

	
	
	@RequestMapping("/noau_desktop")
	public String desktop(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		FrmUser user = HelpUtils.getFrmUser();
		request.setAttribute("user", user);

		List<Map<String, Object>> userMuneList = (List<Map<String, Object>>) request
				.getSession().getAttribute("userMune");
		
		if (null == userMuneList) {
			response.sendRedirect( HelpUtils.getMgrConfig().getServer_url() + "/error?flag=1");
			return null;
		}

		// 待办事项
		Map<String, Map<String, Object>> dbMap = new HashMap<String, Map<String, Object>>();

		for (Map<String, Object> uMap : userMuneList) {
			if ("DSDGL-DSDZD".equals(uMap.get("id"))) {
				dbMap.put("DCLZD", uMap); // 待处理代售点账单
				continue;
			}
			if ("HYGL-TXSH".equals(uMap.get("id"))) {
				dbMap.put("TXSH", uMap); // 提现审核
				continue;
			}
			if ("HYGL-KHXQ".equals(uMap.get("id"))) {
				dbMap.put("KHXQ", uMap); // 客户需求
				continue;
			}
			if (dbMap.size() == 3)
				break;

		}
		for (Map.Entry<String, Map<String, Object>> entry : dbMap.entrySet()) {
			if ("DCLZD".equals(entry.getKey())) { // 待处理代售点账单
				Integer orderSize = this.daoUtil
						.queryForInt(
								"SELECT  COUNT(1) FROM a_agency_bill o WHERE o.f_status IN (0, 2, 8)",
								null);
				Map<String, Object> uMap = (Map<String, Object>) entry
						.getValue();
				uMap.put("dbCount",
						"当前共有 <span style='font-size:18px; font-weight: bold; color: blue;'>"
								+ orderSize + "</span> 个代售点账单待处理!");
				uMap.put("dbSize", orderSize);
				entry.setValue(uMap);
				continue;
			}
			if ("TXSH".equals(entry.getKey())) { // 提现审核
				Integer saleBalanceSize = this.daoUtil
						.queryForInt(
								"SELECT COUNT(1) FROM m_member_cash_apply b WHERE b.a_status = 1",
								null);
				Map<String, Object> uMap = (Map<String, Object>) entry
						.getValue();
				uMap.put("dbCount",
						"当前共有 <span style='font-size:18px; font-weight: bold; color: blue;'>"
								+ saleBalanceSize + "</span> 个提现申请需处理!");
				uMap.put("dbSize", saleBalanceSize);
				entry.setValue(uMap);
				continue;
			}
			if ("KHXQ".equals(entry.getKey())) { // 客户需求
				Integer factorySettlementAuditSize = this.daoUtil
						.queryForInt(
								"SELECT COUNT(1) FROM m_demand WHERE is_oper IN (1150, 1151)",
								null);
				Map<String, Object> uMap = (Map<String, Object>) entry
						.getValue();
				uMap.put("dbCount",
						"当前共有 <span style='font-size:18px; font-weight: bold; color: blue;'>"
								+ factorySettlementAuditSize
								+ "</span> 个客户需求待处理!");
				uMap.put("dbSize", factorySettlementAuditSize);
				entry.setValue(uMap);
				continue;
			}
		}

		$attrs("dbMap", dbMap);
		return "desktop";
	}

	@RequestMapping("/noau_logout")
	public String logout(ModelMap map,
			@ModelAttribute("form") FrmUser userInfo,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		FrmUser user = HelpUtils.getFrmUser();
		try {
			if (user != null) {
				logger.info("用户:" + user.getUser_real_name() + "退出系统！");
				this.removeSession(session);
			}
		} catch (Exception e) {
			logger.error("用户注销系统报错！", e);
		}
		return "login";
	}

	@RequestMapping("/frm/toManageRight")
	public String toManageRight(HttpServletRequest request) {
		$attr("userName", HelpUtils.getFrmUser().getUser_name());
		return "system/right/right_list";
	}

	@RequestMapping("/frm/listAllRight")
	@ResponseBody
	public Map listAllRight(HttpServletRequest request) {
		String functionId = $("functionId");
		List<Map<String, Object>> list = null;
		if (HelpUtils.nullOrBlank(functionId)) {
			list = userService.getTopMenuList();
		} else {
			list = userService.getSubMenuList(functionId);
		}

		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", list.size());
		return map;
	}

	/**
	 * 移除所有session中存在的信息
	 * 
	 * @param session
	 */
	private void removeSession(HttpSession session) {
		if (null != session.getAttribute(Constants.USER_FUNCTION_KEY))
			session.removeAttribute(Constants.USER_FUNCTION_KEY);
		if (null != session.getAttribute(Constants.SYS_SESSION_USER))
			session.removeAttribute(Constants.SYS_SESSION_USER);
		session.invalidate();
	}

	@RequestMapping("/frm/toListOperLog")
	public String toListOperLog(HttpServletRequest request) {
		attrCommonDateTime();

		return "framework/operlog_list";
	}

	@RequestMapping(value = "/frm/listOperLog", method = RequestMethod.GET, consumes="application/json")
	@ResponseBody
	public Map listOperLog(HttpServletRequest request) {
		Map params = $params(request);
		List<Map<String, Object>> list = userService.loadOperLogPage(params);

		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", params.get("total"));
		return map;
	}

	/**
	 * 导出 操作日志记录
	 *
	 * @param request
	 * @param response
	 * @throws BusinessException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/frm/operlog/export", method = RequestMethod.GET)
	public void withdrawRechargeExport(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map<String,Object> param = $params(request);
		String fileName = "操作日志记录报表-"+HelpUtils.getCurrTime();
		ExportExcel excel = new ExportExcel(fileName, OperLogExcelVo.class);
		List<OperLogExcelVo> list = userService.findOperLogList(param);
		if (list != null && list.size() > HelpUtils.EXPORT_LIMIT_NUM) {
			throw new BusinessException(Resp.FAIL,ErrorInfoEnum.EXPORT_EXCEL_LIMIT_GT_5000.getErrorCNMsg());
		}
		excel.setDataList(list);
		excel.write(response,  fileName + ".xls");
	}
}
