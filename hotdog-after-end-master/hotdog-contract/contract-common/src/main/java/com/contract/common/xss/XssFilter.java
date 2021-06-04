package com.contract.common.xss;

import org.springframework.web.util.WebUtils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class XssFilter implements Filter{
	FilterConfig filterConfig = null;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    @Override
    public void destroy() {
        this.filterConfig = null;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        XssHttpServletRequestWrapper requestWrapper = new XssHttpServletRequestWrapper((HttpServletRequest) request);
        // 这里不知道为什么会有这个问题zh-CN,所以重新把value设置一个值
        Cookie cookie = WebUtils.getCookie(requestWrapper, "org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE");
        if (cookie != null) {
            cookie.setValue("zh_CN");
        }
        chain.doFilter(requestWrapper, response);
    }
}
