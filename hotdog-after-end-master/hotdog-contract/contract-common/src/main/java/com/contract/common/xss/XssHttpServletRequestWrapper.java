package com.contract.common.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 * 防止SXX 入侵
 * @author arno
 *
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper{
	public XssHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }
    public String[] getParameterValues(String parameter) {
      String[] values = super.getParameterValues(parameter);
      if (values==null)  {
                  return null;
          }
      int count = values.length;
      String[] encodedValues = new String[count];
      for (int i = 0; i < count; i++) {
                 encodedValues[i] = cleanXSS(values[i]);
       }
      return encodedValues;
    }
    public String getParameter(String parameter) {
          String value = super.getParameter(parameter);
          if (value == null) {
                 return null;
                  }
          return cleanXSS(value);
    }
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;
        return cleanXSS(value);
    }
    private String cleanXSS(String value) {
                //You'll need to remove the spaces from the html entities below
//        value = value.replaceAll("<", "& lt;").replaceAll(">", "& gt;");
//        value = value.replaceAll("\\(", "& #40;").replaceAll("\\)", "& #41;");
//        value = value.replaceAll("'", "& #39;");
//        value = value.replaceAll("eval\\((.*)\\)", "");
//        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        value = value.replaceAll("select", "");
        value = value.replaceAll("delete", "");
        value = value.replaceAll("update", "");
        value = value.replaceAll("insert", "");
        value = value.replaceAll("drop", "");
        value = value.replaceAll("web.xml", "");
        value = value.replaceAll("SCRIPT", "");
        value = value.replaceAll("SELECT", "");
        value = value.replaceAll("DELETE", "");
        value = value.replaceAll("UPDATE", "");
        value = value.replaceAll("INSERT", "");
        value = value.replaceAll("DROP", "");
        value = value.replaceAll("WEB.", "");
        value = value.replaceAll("';", "");
        return value;
    }
}
