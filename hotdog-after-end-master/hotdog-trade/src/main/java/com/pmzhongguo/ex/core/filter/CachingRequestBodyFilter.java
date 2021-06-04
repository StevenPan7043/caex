package com.pmzhongguo.ex.core.filter;

import com.pmzhongguo.ex.core.web.MyRequestWrapper;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description:  缓存request
 * @date: 2019-06-05 15:56
 * @author: 十一
 */
public class CachingRequestBodyFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        // 多次调用
        MyRequestWrapper multiReadRequest = new MyRequestWrapper((HttpServletRequest) request);
        chain.doFilter(multiReadRequest, response);
    }

    @Override
    public void destroy() {

    }
}
