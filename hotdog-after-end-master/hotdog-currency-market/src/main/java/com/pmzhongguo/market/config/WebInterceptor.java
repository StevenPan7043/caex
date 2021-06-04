package com.pmzhongguo.market.config;

import com.pmzhongguo.market.constants.InitConstant;
import com.pmzhongguo.market.utils.HttpRequestUtil;
import com.pmzhongguo.market.utils.JsonUtil;
import com.pmzhongguo.market.web.ObjResp;
import com.pmzhongguo.market.web.Resp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author jary
 * @creatTime 2019/8/10 10:37 AM
 */

public class WebInterceptor implements HandlerInterceptor {
    private Logger logger = LoggerFactory.getLogger(WebInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
//        String[] requestURI = request.getRequestURI().split("/");
//        String[] webRequestUrlList = InitConstant.WEB_REQUEST_URLS.split(";");
//        for (String urlItem : webRequestUrlList) {
//            if (requestURI[1].equals(urlItem)) {
//                return true;
//            }
//        }
//        logger.warn("web请求地址不合法：{},请求IP：{},请求参数：{}", requestURI, HttpRequestUtil.getIpAddr(request), null);
//        response.setContentType("application/json; charset=utf-8");
//        PrintWriter writer = response.getWriter();
//        writer.print(JsonUtil.bean2JsonString(new ObjResp(Resp.FAIL, "请求不合法", null)));
//        writer.flush();
//        writer.close();
//        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
