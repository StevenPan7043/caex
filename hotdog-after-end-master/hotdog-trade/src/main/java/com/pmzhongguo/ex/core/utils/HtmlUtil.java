package com.pmzhongguo.ex.core.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.pmzhongguo.zzextool.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HtmlUtil {

	private static Logger logger = LoggerFactory.getLogger(HtmlUtil.class);

	/**
	 * 根据url生成静态页面
	 * 
	 * @param _url
	 *            动态文件路经 如：http://www.163.com/x.jsp
	 * @param _staticFile
	 *            文件存放路经如：x:\\abc\bbb.html
	 * @return
	 */
	public static boolean JspToHtmlByURL(String _url, String _staticFile) {
		try {
			URL url = new URL(_url);
			URLConnection uc = url.openConnection();
			InputStream is = uc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));

			java.io.FileOutputStream out = new java.io.FileOutputStream(
					_staticFile);
			java.io.BufferedWriter bw = new java.io.BufferedWriter(
					new java.io.OutputStreamWriter(out, "UTF-8"));

			char[] buffer = new char[4096];
			int len;
			while ((len = br.read(buffer)) != -1)
				bw.write(buffer, 0, len);
			br.close();
			bw.flush();
			bw.close();
			out.close();
			br.close();
			is.close();
			logger.debug("静态页面生成工具提示：生成静态网页成功！");
			return true;
		} catch (Exception e) {
			logger.error("静态页面生成工具提示：当前网站不能访问！" + e.toString());
			throw new BusinessException(-1, "生成静态文件失败：" + e.toString());
		}
	}
}
