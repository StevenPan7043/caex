package com.contract.cms.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.contract.cms.common.MappingUtils;
import com.contract.common.VerifyCode;
import com.contract.dto.NodeDetailDto;
import com.contract.dto.NodeDto;
import com.contract.dto.UserDto;
import com.contract.entity.MtCliqueMenus;
import com.contract.enums.ETHEnums;
import com.contract.service.cms.LoginService;
import com.contract.service.cms.PlatSession;
import com.contract.service.wallet.btc.UsdtConfig;
import com.contract.service.wallet.btc.enums.FeeEnums;
import com.contract.service.wallet.btc.enums.NodeEnums;
import com.contract.service.wallet.eth.ECR20Utils;


@Controller
public class LoginController {
	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private LoginService loginService;
	
	/**
	 * 
	 * 登陆页面
	 * @return
	 */
	@RequestMapping(value = MappingUtils.login)
	public ModelAndView login() {
		ModelAndView view=new ModelAndView(MappingUtils.login);
		return view;
	}
	
	/**
	 * 登陆
	 * @param login
	 * @param password
	 * @return
	 */
	@RequestMapping(value = MappingUtils.enter)
	@ResponseBody
	public RestResponse enter(HttpServletRequest request, String login, String password, String validCode, String verCode) {
		try {
			return loginService.enter(request, login, password, validCode, verCode);
		} catch (Exception e) {
			logger.warn("cms登录异常，异常信息：{}", e.getMessage());
			e.printStackTrace();
		}
		return GetRest.getFail("系统异常");
	}
	/**
	 * 获取验证码图片
	 */
	@RequestMapping(value = MappingUtils.verifyImg)
	public void verifyImg(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 创建验证码对象
			VerifyCode verifyCode = new VerifyCode();
			// 创建图片缓存区
			BufferedImage image = verifyCode.getImage();
			// 设置session
			PlatSession.setValidCode(request, verifyCode.getText());
			// 保存图片通过响应输出流输出
			VerifyCode.output(image, response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 退出
	 */
	@RequestMapping(value = MappingUtils.exit)
	public ModelAndView exit(HttpServletRequest request) {
		PlatSession.exit(request);
		return new ModelAndView("redirect:" + MappingUtils.login);
	}
	
	/**
	 * 进入平台首页
	 */
	@RequestMapping(value = MappingUtils.showIndex)
	public ModelAndView toIndex(HttpServletRequest request) {
		List<MtCliqueMenus> list = PlatSession.getMenuList(request);
		UserDto sysUser = PlatSession.getMtUser(request);
		ModelAndView view = new ModelAndView(MappingUtils.showIndex);
		view.addObject("list", list);
		view.addObject("sysUser", sysUser);
		return view;
	}

	/**
	 * 显示平台首页数据
	 */
	@RequestMapping(value = MappingUtils.showWelcome)
	public ModelAndView showWelcome(HttpServletRequest request){
		List<NodeDto> btcList=new ArrayList<>();
		for(int i=0;i<NodeEnums.values().length;i++) {
			NodeEnums e=NodeEnums.values()[i];
			NodeDto dto=new NodeDto();
			dto.setName("节点"+e.getId());
			dto.setMainAddr(e.getAddr());
			UsdtConfig config1=new UsdtConfig(e);
			double usd=config1.getBalance(e.getAddr());
			dto.setTotalmoney(String.valueOf(usd));
			BigDecimal  waitmoney=loginService.getUsdtRechargeLog(e.getId());
			dto.setWaitmoney(waitmoney.toPlainString());
			
			List<NodeDetailDto> btcDList=new ArrayList<>();
			for(int b=0;b<FeeEnums.values().length;b++) {
				FeeEnums f=FeeEnums.values()[b];
				NodeDetailDto detailDto=new NodeDetailDto();
				detailDto.setAddr(f.getFeeaddr());
				String money=config1.getBalanceApi(f.getFeeaddr());
				detailDto.setMoney(money);
				btcDList.add(detailDto);
			}
			dto.setList(btcDList);
			btcList.add(dto);
		}
		
		List<NodeDetailDto> ethDList=new ArrayList<>();
		for(ETHEnums e:ETHEnums.values()) {
			BigDecimal cpos=ECR20Utils.getERC20Balance(ECR20Utils.usdt_contractAddress, e.getAddr(), 4);//平台CPOS
			BigDecimal eth=ECR20Utils.getBlanceOf(e.getAddr());
			NodeDetailDto detailDto=new NodeDetailDto();
			detailDto.setAddr(e.getAddr());
			detailDto.setMoney(eth.toPlainString());
			detailDto.setToken(cpos.toPlainString());
			ethDList.add(detailDto);
		}
		
		BigDecimal totalrecharge=loginService.getRecharge();//总充值
		BigDecimal waitusd=loginService.getWaitUsdt();//未归集的
		ModelAndView view = new ModelAndView(MappingUtils.showWelcome);
		view.addObject("waitusd", waitusd);
		view.addObject("totalrecharge", totalrecharge);
		view.addObject("btcList", btcList);
		view.addObject("ethDList", ethDList);
		view.addObject("roleid", PlatSession.getRoleId(request));
		return view;
	}
	
	/**
	 * 平台登录发送短信验证码
	 * @return
	 */
	@RequestMapping(value = MappingUtils.sendLoginCode)
	@ResponseBody
	public RestResponse sendLoginCode(String login) {
		return loginService.sendLoginCode(login);
	}
}
