package com.image.common;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil
{
  public static String postServerFile(String httpurl, String fileName, InputStream inputStream, String path)
  {
    String result = "";
    try {
      String BOUNDARY = "---------7d4a6d158c9";
      URL url = new URL(httpurl + "?path=" + path);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();

      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

      conn.setRequestProperty("Charsert", "UTF-8");
      conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

      OutputStream out = new DataOutputStream(conn.getOutputStream());
      byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
      StringBuilder sb = new StringBuilder();
      sb.append("--");
      sb.append(BOUNDARY);
      sb.append("\r\n");
      sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fileName + "\"\r\n");

      sb.append("Content-Type:application/octet-stream\r\n\r\n");
      byte[] data = sb.toString().getBytes();
      out.write(data);
      DataInputStream in = new DataInputStream(inputStream);
      int bytes = 0;
      byte[] bufferOut = new byte[1024];
      while ((bytes = in.read(bufferOut)) != -1) {
        out.write(bufferOut, 0, bytes);
      }
      in.close();
      out.write(end_data);
      out.flush();
      out.close();

      BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

      String line = null;
      while ((line = reader.readLine()) != null)
        result = result + line;
    }
    catch (Exception e) {
      e.printStackTrace();
      System.out.println("发送POST请求出现异常！" + e);
    }
    return result;
  }

  public static String getServer(String url, String param) {
    URL u = null;
    HttpURLConnection con = null;
    url = url + param;
    try
    {
      u = new URL(url);
      con = (HttpURLConnection)u.openConnection();
      con.setRequestMethod("POST");
      con.setDoOutput(true);
      con.setDoInput(true);
      con.setUseCaches(false);
      con.setRequestProperty("connection", "Keep-Alive");
      con.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

      con.setRequestProperty("Content-Type", "application/json;charset=utf-8");
      DataOutputStream osw = new DataOutputStream(con.getOutputStream());
      osw.flush();
      osw.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (con != null) {
        con.disconnect();
      }
    }

    StringBuffer buffer = new StringBuffer();
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
      String temp;
      while ((temp = br.readLine()) != null)
        buffer.append(temp);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return buffer.toString();
  }

  public static String postServerBig(String url, String param) {
    HttpURLConnection con = null;
    try
    {
      URL u = new URL(url);
      u = new URL(url);
      con = (HttpURLConnection)u.openConnection();
      con.setRequestMethod("POST");
      con.setDoOutput(true);
      con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
      byte[] bypes = param.getBytes();
      con.getOutputStream().write(bypes);
    }
    catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (con != null) {
        con.disconnect();
      }
    }

    StringBuffer buffer = new StringBuffer();
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
      String temp;
      while ((temp = br.readLine()) != null)
        buffer.append(temp);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return buffer.toString();
  }
}