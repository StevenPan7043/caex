package com.zytx.business.filter;

import com.zytx.common.cache.SocketCache;
import com.zytx.common.constant.RespEnum;
import com.zytx.common.dto.UserDto;
import com.zytx.common.util.JWTUtil;
import com.zytx.common.util.RespUtil;
import com.zytx.common.util.StringUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
@WebFilter(filterName = "HttpFilter", urlPatterns = {"/*"})
public class HttpFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        System.out.println("初始化过滤器");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        System.out.println("进入目标资源之前先干点啥");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //指定允许其他域名访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        //响应头设置
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,token");
        //响应类型
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type","text/html;charset=UTF-8");

        // 浏览器是会先发一次options请求，如果请求通过，则继续发送正式的post请求
        // 配置options的请求返回
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
            return;
        }
        try {
            String url = request.getRequestURI();
            String token = request.getHeader("token");
            if (StringUtil.isNullOrBank(token)) {
                returnErrorMsg(response, RespUtil.getFail(RespEnum.FAIL, null, "NO_TOKEN"));
                return;
            }
            UserDto user = SocketCache.getUser(token);
            if (user == null) {
                returnErrorMsg(response, RespUtil.getFail(RespEnum.NO_USER, null));
                return;
            }
            Map<String, String> map = JWTUtil.parseToken(token);
            if (map.isEmpty()) {
                returnErrorMsg(response, RespUtil.getFail(RespEnum.FAIL, null, "token错误"));
                return;
            }
            request.setAttribute("id", map.get("id"));
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            returnErrorMsg(response, RespUtil.getFail(RespEnum.FAIL, null, "系统异常"));
        }
    }

    /**
     * 过滤器拦截错误信息时返回
     *
     * @param response
     * @param errMsg
     */
    private void returnErrorMsg(HttpServletResponse response, String errMsg) {
        try (PrintWriter out = response.getWriter()) {
            log.info("some err :" + errMsg);
            out.write(errMsg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        System.out.println("过滤器被销毁了");
    }
}
