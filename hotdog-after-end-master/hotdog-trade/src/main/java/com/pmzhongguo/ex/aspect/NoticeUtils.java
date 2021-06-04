package com.pmzhongguo.ex.aspect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.pmzhongguo.ex.core.utils.MacMD5;

/**
 * 通知工具
 * 
 * @author zengweixiong
 *
 */
public class NoticeUtils {

	/**
	 * 发送短信
	 * 
	 * @param content
	 * @param mobile
	 * @return
	 */
	public static boolean sendSms(String content, String mobile) {
		try {
			// content = "【ZZEX】短信验证码：" + content + "，请在3分钟内验证，切勿泄露给他人。如非本人操作，请忽略。";
			// 连接超时及读取超时设置
			System.setProperty("sun.net.client.defaultConnectTimeout", "30000"); // 连接超时：30秒
			System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时：30秒
			String encode = "UTF-8";
			// 新建一个StringBuffer链接
			StringBuffer buffer = new StringBuffer();
			String contentUrlEncode = URLEncoder.encode(content, encode); // 对短信内容做Urlencode编码操作。注意：如
			// 把发送链接存入buffer中，如连接超时，可能是您服务器不支持域名解析，请将下面连接中的：【m.5c.com.cn】修改为IP：【115.28.23.78】
			buffer.append("http://m.5c.com.cn/api/send/index.php?username=" + "sypgr" + "&password_md5="
					+ MacMD5.CalcMD5("asdf1234", 32) + "&mobile=" + mobile + "&apikey="
					+ "fffae7c7c36f2c7e553491cc35f7e9ab" + "&content=" + contentUrlEncode + "&encode=" + encode);
			// 把buffer链接存入新建的URL中
			URL url = new URL(buffer.toString());
			// 打开URL链接
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// 使用POST方式发送
			connection.setRequestMethod("POST");

			// 使用长链接方式
			connection.setRequestProperty("Connection", "Keep-Alive");
			// 发送短信内容
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			// 获取返回值
			String result = reader.readLine();
			// 输出result内容，查看返回值，成功为success，错误为error，详见该文档起始注释
			System.out.println(result);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// public static void main(String[] args) {
	// String content = "【币根网】您有一条 176***7000 的用户，类型为买的订单，请及时处理。";
	// String mobile = "17671687000";
	// System.out.println(sendSms(content, mobile));
	// }
}
