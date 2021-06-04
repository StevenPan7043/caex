package com.pmzhongguo.ex.core.web;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.scheduler.FrozenMemberScheduler;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.utils.JsonUtil;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.entity.FrmUser;
import com.pmzhongguo.ex.framework.service.FrmUserService;
import com.pmzhongguo.zzextool.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 自定义拦截器
 * 
 * @author tushen
 * @date Nov 5, 2011
 */
public class BaseInterceptor extends HandlerInterceptorAdapter {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	protected Logger visitLogger = LoggerFactory.getLogger("visitLogger");
	@Resource
	private FrmUserService userService;

	/**
	 * 最后执行，可用于释放资源
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

	/**
	 * 显示视图前执行
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	/**
	 * Controller之前执行
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		String origiUri = request.getServletPath();
		
		if ("/".equals(origiUri)) {
			return super.preHandle(request, response, handler);
		}
		// 移除已冻结的用户
		removeFrozenMemberSession(request);

		String requestType = request.getHeader("X-Requested-With");
		int flag = authenticate(request);
//		logger.info(HelpUtils.getIpAddr(request) + "  visit:" + origiUri);
		visitLogger.info(HelpUtils.getRequestInfo(request) + " " + HelpUtils.getRequestHead(request));
		if (null == requestType) {
			switch (flag) {
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
			default:
				return super.preHandle(request, response, handler);

			}
			response.sendRedirect( HelpUtils.getMgrConfig().getServer_url() + "/error?flag="
					+ flag);
			return false;
		} else {
			switch (flag) {
			case 1:
				throw new BusinessException(-10, ErrorInfoEnum.LANG_NO_LOGIN.getErrorCNMsg());
			case 2:
				throw new BusinessException(-10, ErrorInfoEnum.ACCESS_HAS_BEEN_DENIED.getErrorCNMsg());
			case 3:
				throw new BusinessException(-10, ErrorInfoEnum.ACCESS_HAS_BEEN_DENIED.getErrorCNMsg());
			default:
				return super.preHandle(request, response, handler);

			}
		}

	}

	/**
	 * 通过URL处理授权访问权限<br>
	 * 状态0：正常访问<br>
	 * 状态1：没有登录，非法访问资源<br>
	 * 状态2：重复登录<br>
	 * 状态3：没有权限<br>
	 * 状态4：过滤器内部错误<br>
	 * 2008-05-21修改 修改返回类型，及处理URL的方法
	 * 
	 * @param request
	 * @return
	 */
	private int authenticate(HttpServletRequest request) {
		String path = request.getRequestURI();
		try {
            if
            (!path.contains("/backstage/") &&
                    (path.contains("/m/") || path.contains("/o/") || path.contains("/a/") || path.contains("/n/")//
							|| path.contains("/contract")
                            || path.contains("/noau_") || path.contains("/common/")//
                            || path.contains("/adminpmzhongguo") || path.contains("/api-docs")//
                            || path.contains("/error") || path.contains("/member_trace")//
                            || path.contains("/tr")// 交易排名功能
                            || path.contains("/ir")// 收入返还API
                            || path.contains("/irb")// 收入返还后台管理
                            || path.contains("/count")// 统计接口
                            || path.contains("/vote")// 投票接口
                            || path.contains("/au")// 投票接口
                            || path.contains("/strf")// 交易返手续费
                            || path.contains("/lock")// 锁仓
                            || path.contains("/otc")// otc
                            || path.contains("/e/")// 异常提示
                            || path.contains("/img/")// 通用
                            || path.contains("/crowd/")// 众筹
                            || path.contains("/api/")// api管理
                            || path.contains("/market")
                            || path.contains("/settingSymbolPrice/")
                            || path.contains("/getSymbolPrice")
                            || path.contains("/validateThirdMarket")
                            || path.contains("/ct")  //社区划转
                            || path.contains("/tk")  //lastWinner获取token信息
                            || path.contains("/currency")  // 币种
                            || path.contains("/tc")
                            || path.contains("/auth") //faceId
                            || path.contains("/dataLab/")
							|| path.contains("/ipfs/")
                    )
            ) {
                return 0;
            }

			// --------------------- 以下基本是对后台进行校验 -----------------------------

			path = path.substring(request.getContextPath().length())
					.replaceAll("(/{2,})", "/");

			HttpSession session = request.getSession();



			// 判断用户是否登陆
			FrmUser user = (FrmUser) session
					.getAttribute(Constants.SYS_SESSION_USER);

			if (user == null) {
				logger.info("未登录且尝试访问资源:" + path);
				return 1;
			} else {

				// MDC是log4j种非常有用类，它们用于存储应用程序的上下文信息（context
				// infomation），从而便于在log中使用这些上下文信息
				MDC.put("user_name", null == user ? "" : user.getUser_name());

				// 判断用户 是否拥有此权限
				List<Map<String, Object>> list = null;

				if (null != user) {
					list = (List<Map<String, Object>>) session
							.getAttribute(Constants.USER_FUNCTION_KEY);
				} else {
					list = (List<Map<String, Object>>) session
							.getAttribute(Constants.FACTORY_FUNCTION_KEY);
				}

				if (list != null && list.size() > 0) {
					int returnval = 2;
					for (Map<String, Object> map : list) {
						if (path.equalsIgnoreCase(map.get("fuc_url_url") + "")) {
							returnval = 0;

							// 记录日志
							if (1 == Integer.parseInt(map.get("is_need_log")
									+ "")) {
								Map<String, Object> contentMap = new HashMap<String, Object>();
								contentMap.put("queryString",
										request.getQueryString());
								contentMap.put("parameterMap",
										HelpUtils.getparameter(request));
								userService
										.logVisit(
												null == user ? "" : user
														.getUser_name(),
												null == user ? "" : user
														.getUser_real_name(),
												HelpUtils.getIpAddr(request),
												map.get("fuc_url_desc")
														+ JsonUtil
																.toJson(contentMap),
												map.get("fuc_url_url") + "");
							}

							break;
						}
					}
					return returnval;
				} else {
					logger.info("已经登录未授权用户" + user.getUser_real_name()
							+ "访问URL:" + path);
					return 2;
				}
			}

		} catch (Exception e) {
			logger.error("权限认证错误", e.toString());
			e.printStackTrace();
			return 3;
		}
	}

	/**
	 * 移除已冻结的用户
	 * @param request
	 */
	private void removeFrozenMemberSession( HttpServletRequest request) {
		if (request.getSession() != null) {
			Member member = JedisUtilMember.getInstance().getMember(request, null);
            // 用户已被冻结，就移除session
            if (member != null
                    && FrozenMemberScheduler.memberIdMap.containsKey(member.getId()))  {
                JedisUtilMember.getInstance().removeMember(request,null);
                throw new BusinessException(Resp.FAIL,ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg());
            }

		}
	}

}