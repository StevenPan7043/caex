package com.pmzhongguo.ex.core.filter;


import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.PropertiesUtil;

/**
 * sql注入拦截
 */
public class SqlInjectionFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger("sqlinjection");
    
    private static final  String[] badStrs = PropertiesUtil.getPropValByKey("sql_injection").split("\\|");

    private static final  String[] SQL_FILTER_EXCLUDE_URL = PropertiesUtil.getPropValByKey("sql_filter_exclude_url").split(",");

    private static Map<String,String> sqlFilterExcludeUrlMap = new HashMap<String,String>();

    protected String encoding = "UTF-8";

    static {
        if(!HelpUtils.nullOrBlank(SQL_FILTER_EXCLUDE_URL)) {

            for(String url : SQL_FILTER_EXCLUDE_URL) {
                logger.error("<====================sql关键字免拦截url：" + url);
                sqlFilterExcludeUrlMap.put(url,url);
            }
        }

    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest args0, ServletResponse args1,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) args0;
        HttpServletResponse res = (HttpServletResponse) args1;
        req.setCharacterEncoding(encoding);
        res.setContentType("text/html;charset="+encoding);
        //获得所有请求参数名
        Enumeration params = req.getParameterNames();
        String sql = "";
        while (params.hasMoreElements()) {
            //得到参数名
            String name = params.nextElement().toString();
            //得到参数对应值
            String[] value = req.getParameterValues(name);
            for (int i = 0; i < value.length; i++) {
                sql = sql + value[i];
            }
        }
        //有sql关键字，跳转到error.html
        String sqlStr = sqlValidate(sql);
        if (!HelpUtils.nullOrBlank(sqlStr)) {
            logger.error("keyword:" + sqlStr + "   sql拦截，疑是sql注入：" + HelpUtils.getRequestInfo(req) + " " + HelpUtils.getRequestHead(req));
            String requestURI = req.getRequestURI();

            // 富文本编辑敏感词处理
            if(sqlFilterExcludeUrlMap.containsKey(requestURI)) {
                res.sendRedirect("/e/cms-article-sensitive/"+sqlStr);
            }

        } else {
            chain.doFilter(args0, args1);
        }
    }

    protected static String sqlValidate(String str) {
        //统一转为小写
        str = str.toLowerCase();
        //过滤掉的sql关键字，可以手动添加
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return badStrs[i];
            }
        }
        return "";
    }
}

