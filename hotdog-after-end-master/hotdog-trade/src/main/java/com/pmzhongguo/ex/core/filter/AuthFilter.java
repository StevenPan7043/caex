package com.pmzhongguo.ex.core.filter;

import com.pmzhongguo.ex.core.web.Constants;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * jsp页面过滤器，浏览器中只要访问jsp/* 页面就要到这里进行判断是否登录，只有登录才可以访问页面
 */
public class AuthFilter implements Filter {

	@Override
	public void destroy() {	
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		/**
		 * 1,doFilter方法的第一个参数为ServletRequest对象。此对象给过滤器提供了对进入的信息（包括*
		 * 表单数据、cookie和HTTP请求头）的完全访问。第二个参数为ServletResponse，通常在简单的过*
		 * 滤器中忽略此参数。最后一个参数为FilterChain，此参数用来调用servlet或JSP页。
		 */
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		/**
		 * 如果处理HTTP请求，并且需要访问诸如getHeader或getCookies等在ServletRequest中*
		 * 无法得到的方法，就要把此request对象构造成HttpServletRequest
		 */
		HttpServletResponse response = (HttpServletResponse) servletResponse;


		// 如果session为空表示用户没有登录就重定向到login.jsp页面
		HttpSession loginSession = request.getSession();
		if(loginSession == null || loginSession.getAttribute(Constants.SYS_SESSION_USER) == null) {
			System.out.println(request.getContextPath()+"/noau_logout ===========>");
			response.sendRedirect(request.getContextPath()+"/noau_logout");
			return;
		}






		// 加入filter链继续向下执行
		filterChain.doFilter(request, response);
		/**
		 * 调用FilterChain对象的doFilter方法。Filter接口的doFilter方法取一个FilterChain对象作* 为它
		 * 的一个参数。在调用此对象的doFilter方法时，激活下一个相关的过滤器。如果没有另*
		 * 一个过滤器与servlet或JSP页面关联，则servlet或JSP页面被激活。
		 */
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}

