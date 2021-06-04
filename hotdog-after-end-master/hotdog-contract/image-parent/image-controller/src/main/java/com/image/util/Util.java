package com.image.util;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

public class Util
{
  public static final String getTimeString()
  {
    Calendar cal = new GregorianCalendar();
    String time = "" + cal.get(10) + cal.get(12) + cal.get(13) + cal.get(14);
    return time;
  }

  public static String getFilePath(HttpServletRequest request)
  {
    String savePath = request.getSession().getServletContext().getRealPath(Contant.files);
    return savePath;
  }
  public static String getServiceName(HttpServletRequest request)
  {
    String serviceName = Contant.IP_SERVER;
    return serviceName;
  }

  public static String getPath(String path)
  {
    if (path == null) {
      return "";
    }
    if (path.equals("/")) {
      return "";
    }
    return path;
  }
}