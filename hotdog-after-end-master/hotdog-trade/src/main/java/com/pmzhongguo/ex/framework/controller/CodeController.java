package com.pmzhongguo.ex.framework.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import com.pmzhongguo.ex.core.web.Constants;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.SCaptcha;

@ApiIgnore
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class CodeController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private int width = 90;// 定义图片的width
	private int height = 30;// 定义图片的height
	private int codeCount = 4;// 定义图片上显示验证码的个数
	private int xx = 15;
	private int fontHeight = 25;
	private int codeY = 25;
	char[] codeSequence = { '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	@ResponseBody  
    @RequestMapping(value = "/m/kaptcha", method = RequestMethod.GET)  
	public Map<String, Object> validateCodeSimple(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		// randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
		StringBuffer randomCode = new StringBuffer();

		// 定义图像buffer
		BufferedImage buffImg = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics gd = buffImg.getGraphics();
		// 创建一个随机数生成器类
		Random random = new Random();
		// 将图像填充为白色
		gd.setColor(Color.WHITE);
		gd.fillRect(0, 0, width, height);

		// 创建字体，字体的大小应该根据图片的高度来定。
		Font font = new Font("Fixedsys", Font.BOLD, fontHeight);
		// 设置字体。
		gd.setFont(font);

		// 画边框。
		// gd.setColor(Color.lightGray);
		// gd.drawRect(0, 0, width - 1, height - 1);

		// 随机产生40条干扰线，使图象中的认证码不易被其它程序探测到。
		gd.setColor(Color.BLACK);
		// gd.drawLine(0, 0, 0, 0);
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(6);
			int yl = random.nextInt(6);
			// gd.drawLine(x, y, x + xl, y + yl);
		}

		int red = 0, green = 0, blue = 0;

		// 随机产生codeCount数字的验证码。
		for (int i = 0; i < codeCount; i++) {
			// 得到随机产生的验证码数字。
			String code = String.valueOf(codeSequence[random.nextInt(9)]);
			// 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);

			// 用随机产生的颜色将验证码绘制到图像中。
			gd.setColor(new Color(red, green, blue));
			gd.drawString(code, (i + 1) * xx, codeY);

			// 将产生的四个随机数组合在一起。
			randomCode.append(code);
		}
		
		SCaptcha instance = new SCaptcha(100, 32, 4, 10);  
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();    
		ImageIO.write(instance.getBuffImg(), "jpg", outputStream);
		
		
		
		// 生成captcha的token  
        Map<String, Object> map = new HashMap();
        String kaptchaToken = genKaptchaToken();
        JedisUtil.getInstance().setCheckCode(kaptchaToken, instance.getCode(), Constants.CHECK_CODE_TIME_OUT);
        map.put("check_code_token", kaptchaToken);
        
     // 对字节数组Base64编码    
        Base64 encoder = new Base64();
        map.put("check_code_img", "data:image/jpeg;base64," + new String(Base64.encodeBase64(outputStream.toByteArray())));
  
        return map;  
	}
	
	private String genKaptchaToken() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	@RequestMapping("/noau_validateCode")
	public void validateCode(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		// 将四位数字的验证码保存到Session中。
		HttpSession session = req.getSession();
		
        // 设置响应的类型格式为图片格式  
		resp.setContentType("image/jpeg");  
        // 禁止图像缓存。  
		resp.setHeader("Pragma", "no-cache");  
		resp.setHeader("Cache-Control", "no-cache");  
		resp.setDateHeader("Expires", 0);  
        SCaptcha instance = new SCaptcha(100, 32, 4, 10);  
        session.setAttribute("validateCode", instance.getCode());
        instance.write(resp.getOutputStream());  
		
	}
}
