package com.pmzhongguo.otc.sms;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 短信API服务调用示例代码 － 聚合数据 在线接口文档：http://www.juhe.cn/docs/54
 **/

public class JuheUtil {

	private static Logger logger = LoggerFactory.getLogger(JuheUtil.class);

	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	// 配置您申请的KEY
	public static final String APPKEY = "bb602947041cd42804d8f1f6e5f44f2d";
	// 聚合国际短信key
	private static final String JUHE_GJ_APIKEY = "f270b36e3f7183a4fee47da56d16ce02";


	// 1.屏蔽词检查测
	public static void getRequest1() {
		String result = null;
		String url = "http://v.juhe.cn/sms/black";// 请求接口地址
		Map<String, Object> params = new HashMap<String, Object>();// 请求参数
		params.put("word", "");// 需要检测的短信内容，需要UTF8 URLENCODE
		params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)

		try {
			result = net(url, params, "GET");
			JSONObject object = JSONObject.fromObject(result);
			if (object.getInt("error_code") == 0) {
				System.out.println(object.get("result"));
			} else {
				System.out.println(object.get("error_code") + ":" + object.get("reason"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 2.发送短信
	public static void getRequest2() {
		String result = null;
		String url = "http://v.juhe.cn/sms/send";// 请求接口地址
		Map<String, Object> params = new HashMap<String, Object>();// 请求参数
		params.put("mobile", "");// 接收短信的手机号码
		params.put("tpl_id", "");// 短信模板ID，请参考个人中心短信模板设置
		params.put("tpl_value", "");// 变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，
									// <a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
		params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
		params.put("dtype", "");// 返回数据的格式,xml或json，默认json

		try {
			result = net(url, params, "GET");
			JSONObject object = JSONObject.fromObject(result);
			if (object.getInt("error_code") == 0) {
				System.out.println(object.get("result"));
			} else {
				System.out.println(object.get("error_code") + ":" + object.get("reason"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  聚合国内短信发送
	 * @param mobile
	 * @param tpl_id
	 * @param tpl_value
	 */
	public static void send(String mobile, int tpl_id, String tpl_value) {
		String result = null;
		String url = "http://v.juhe.cn/sms/send";// 请求接口地址
		Map<String, Object> params = new HashMap<String, Object>();// 请求参数
		params.put("mobile", mobile);// 接收短信的手机号码
		params.put("tpl_id", tpl_id);// 短信模板ID，请参考个人中心短信模板设置
//		#code#=1234&#company#=聚合数据
		params.put("tpl_value", tpl_value);// 变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
		params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
		params.put("dtype", "json");// 返回数据的格式,xml或json，默认json

		try {
			result = net(url, params, "GET");
			JSONObject object = JSONObject.fromObject(result);
			if (object.getInt("error_code") == 0) {
				System.out.println(object.get("result"));
			} else {
				logger.error("短信发送失败,手机号：" + mobile + "  tpl_id: " + tpl_id + "  tpl_value: " + tpl_value);
				logger.error("短信发送失败：error_code: " + object.get("error_code") + "  reason：" + object.get("reason"));
			}
		} catch (Exception e) {
			logger.warn("mobile:" + mobile + " tpl_id:" + tpl_id + " tpl_value:" + tpl_value + "\r\n ", e);
		}
	}

	/**
	 *	 聚合国际短信发送
	 * @param areaNum
	 * @param mobile
	 * @param tpl_id
	 * @param tpl_value
	 */
	public static void internationalSend(String areaNum, String mobile, int tpl_id, String tpl_value) {
		String result = null;
		String url = "http://v.juhe.cn/smsInternational/send.php";// 请求接口地址
		Map<String, Object> params = new HashMap<String, Object>();// 请求参数
		params.put("mobile", mobile);// 接收短信的手机号码
		params.put("tplId", tpl_id);// 短信模板ID，请参考个人中心短信模板设置
//		#code#=1234&#company#=聚合数据
		params.put("areaNum", areaNum);
		params.put("tplValue", tpl_value);// 变量名和变量值对。如果你的变量名或者变量值中带有#&=中的任意一个特殊符号，请先分别进行urlencode编码后再传递，<a href="http://www.juhe.cn/news/index/id/50" target="_blank">详细说明></a>
		params.put("key", JUHE_GJ_APIKEY);// 应用APPKEY(应用详细页查询)
//		params.put("dtype", "json");// 返回数据的格式,xml或json，默认json
		try {
			result = net(url, params, "GET");
			JSONObject object = JSONObject.fromObject(result);
			if (object.getInt("error_code") == 0) {
				System.out.println(object.get("result"));
			} else {
				logger.error("JuHe国际短信发送失败,手机号：" + mobile + "  tpl_id: " + tpl_id + "  tpl_value: " + tpl_value);
				logger.error("JuHe国际短信发送失败：error_code: " + object.get("error_code") + "  reason：" + object.get("reason"));
			}
		} catch (Exception e) {
			logger.warn("mobile:" + mobile + " tpl_id:" + tpl_id + " tpl_value:" + tpl_value + "\r\n ", e);
		}
	}
	public static void main(String[] args) {
		mySend();
	}
	
	public static void mySend() {
//		127970	【ZZEX】您已向#name#用户出售#volu# #base#，出售单价为#pric# #quote#， 订单号为#id#，等待对方支付。
//		"#name#=风情万种&#volu#=100.000&#base#=ETH&#pric#=10.00&#quote#=CNY&#id#=13213-1231-13-1231sa";
		String format_127970 = "#name#=%s&#volu#=%s&#base#=%s&#pric#=%s&#quote#=%s&#id#=%s";
		String tpl_value = String.format(format_127970, "风情万种", "100.000", "ETH", "10.00", "CNY", "13213-1231-13-1231sa");
//		send("13916155453", 127970, tpl_value);
//		String format_127972 = "#name#=%s&#volu#=%s&#base#=%s&#pric#=%s&#quote#=%s&#id#=%s&#m#=%s";
//		127972	【ZZEX】您已向#name#用户购买#volu# #base#，出售单价为#pric# #quote#，订单号为#id#，请于#m#分钟内付款。
		
//		127975	【ZZEX】#name#已将订单#id#取消，如有疑问请联系平台客服。
//		String tpl_value = "#name#=风情万种&#id#=13213-1231-13-1231sa";
//		send("13916155453", "127975", tpl_value);
	}

	/**
	 *
	 * @param strUrl 请求地址
	 * @param params 请求参数
	 * @param method 请求方法
	 * @return 网络请求字符串
	 * @throws Exception
	 */
	public static String net(String strUrl, Map<String, Object> params, String method) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals("GET")) {
				strUrl = strUrl + "?" + urlencode(params).replaceAll("%", "%25");
			}
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals("GET")) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals("POST")) {
				try {
					DataOutputStream out = new DataOutputStream(conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	// 将map型转为请求参数型
	public static String urlencode(Map<String, Object> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, Object> i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}