package com.pmzhongguo.ex.core.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

import com.pmzhongguo.ex.core.utils.HelpUtils;

public class ZZEXDispatcherServlet extends DispatcherServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1522099662731455994L;
	protected Logger logger = LoggerFactory.getLogger("noMapping");
	
	@Override
	protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
//		logger.info(" No mapping found for HTTP request with " + HelpUtils.getRequestInfo(request) + " " + HelpUtils.getRequestHead(request));
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
	}
}
