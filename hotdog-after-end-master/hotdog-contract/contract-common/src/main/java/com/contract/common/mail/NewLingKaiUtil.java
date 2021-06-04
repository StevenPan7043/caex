package com.contract.common.mail;

import common.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * @author Daily
 * @date 2020/7/24 12:25
 */
public class NewLingKaiUtil {
    private static Logger logger = Logger.getLogger(NewLingKaiUtil.class);

    /**
     * 账号
     */
    private static final String CORP_ID = "CXS000958";
    /**
     * 密码
     */
    private static final String PWD = "123456@";
    /**
     * 请求url
     */
//    private static final String URL = "http://yzm.mb345.com/ws/BatchSend2.aspx?CorpID="+CORP_ID+"&Pwd="+PWD+"&Mobile=%s&Content=%s&SendTime=%s&cell=";


    @SuppressWarnings({ "static-access", "static-access" })
    public static void main(String[] args) {
        try {

            NewLingKaiUtil test = new NewLingKaiUtil();

            // Http Get请求
//            test.sendSMSGet("17712477742",
//                    "Java Http GET 方式短信调试已经成功!!!!!【成都凌凯】", "");

            // Http post 请求
            test.sendSMSPost("17712477742",
                    "Java Http POST 方式短信调试已经成功!!!!!【成都凌凯】", "");

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * http get请求 发送方法 其他方法同理 返回值>0 提交成功
     *
     * @param Mobile
     *            手机号码
     * @param Content
     *            发送内容
     * @param send_time
     *            定时发送的时间；可以为空，为空时为及时发送
     * @return 返回值
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public static int sendSMSGet(String Mobile, String Content, String send_time)
            throws MalformedURLException, UnsupportedEncodingException {
        URL url = null;
        String CorpID = CORP_ID;// 账户名
        String Pwd = PWD;// 密码
        String send_content = URLEncoder.encode(
                Content.replaceAll("<br/>", " "), "GBK");// 发送内容
        url = new URL("https://yzm.mb345.com/ws/BatchSend2.aspx?CorpID="
                + CorpID + "&Pwd=" + Pwd + "&Mobile=" + Mobile + "&Content="
                + send_content + "&Cell=&SendTime=" + send_time);
        BufferedReader in = null;
        int inputLine = 0;
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            inputLine = new Integer(in.readLine()).intValue();
        } catch (Exception e) {
            inputLine = -2;
            logger.warn(Mobile + " 网络异常,发送短信失败！" + inputLine);
        }
        if(inputLine < 0){
            logger.warn(Mobile + " 发送短信返回值：  " + inputLine);
        }
        return inputLine;
    }

    /**
     * Hppt POST请求发送方法 返回值>0 为 提交成功
     *
     * @param Mobile
     *            电话号码
     * @param Content
     *            发送内容
     * @param send_time
     *            定时发送时间，为空时，为及时发送
     * @return
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     */
    public static int sendSMSPost(String Mobile, String Content,
                                  String send_time) throws MalformedURLException,
            UnsupportedEncodingException {

        String inputLine = "";
        int value = -2;
        DataOutputStream out = null;
        InputStream in = null;

        String CorpID = CORP_ID;// 账户名
        String Pwd = PWD;// 密码
        String send_content = URLEncoder.encode(
                Content.replaceAll("<br/>", " "), "GBK");// 发送内容

//        String strUrl = "http://sdk2.028lk.com:9880/sdk2/BatchSend2.aspx";
        String strUrl = "https://yzm.mb345.com/ws/BatchSend2.aspx";
        String param = "CorpID=" + CorpID + "&Pwd=" + Pwd + "&Mobile=" + Mobile
                + "&Content=" + send_content + "&Cell=&SendTime=" + send_time;

        try {

            inputLine = sendPost(strUrl, param);

            System.out.println("开始发送短信手机号码为 ：" + Mobile);

            value = new Integer(inputLine).intValue();

        } catch (Exception e) {

            System.out.println("网络异常,发送短信失败！");
            value = -2;

        }

        System.out.println(String.format("返回值：%d", value));

        return value;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

}
