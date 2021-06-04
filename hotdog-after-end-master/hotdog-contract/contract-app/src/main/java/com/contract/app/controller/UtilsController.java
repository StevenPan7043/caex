package com.contract.app.controller;

import java.awt.image.BufferedImage;

import javax.servlet.http.HttpServletResponse;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.contract.app.common.MappingUtil;
import com.contract.common.PathUtils;
import com.contract.common.VerifyCode;
import com.contract.enums.SysParamsEnums;
import com.contract.exception.ThrowJsonException;
import com.contract.service.UtilsService;
import com.contract.service.redis.RedisUtilsService;


import com.image.common.RestUploadFileInfo;
import com.image.common.Service;

@Controller
public class UtilsController {

	@Autowired
	private UtilsService utilsService;
	@Autowired
	private RedisUtilsService redisUtilsService;
	
	/**
	 * 发送验证码
	 * @param phone
	 * @return
	 */
	@RequestMapping(value = MappingUtil.sendPhonecode)
	@ResponseBody
	public RestResponse sendPhonecode(String phone, String validcode) {
		RestResponse result=utilsService.sendPhonecode(phone,validcode);
		return result;
	}
	
	@RequestMapping(value = MappingUtil.getAppVersion)
	@ResponseBody
	public JSONObject getAppVersion() {
		String appdownload_url=redisUtilsService.getKey(SysParamsEnums.appdownload_url.getKey());
		String app_version=redisUtilsService.getKey(SysParamsEnums.app_version.getKey());
		String desc_val=redisUtilsService.getKey(SysParamsEnums.desc_val.getKey());
		JSONObject map=new JSONObject();
		map.put("code", 200);
		map.put("appdownload_url", appdownload_url);
		map.put("app_version", app_version);
		map.put("desc_val", desc_val);
		return map;
	}
	
	@RequestMapping(value = MappingUtil.upload)
	@ResponseBody
	public RestResponse upload( @RequestParam(value = "file", required = false) MultipartFile file) {
		try {
			RestUploadFileInfo r=Service.uploadImage(file.getOriginalFilename(), file.getInputStream(), PathUtils.cus_head);
			if(!r.isStatus()) {
				return GetRest.getFail(r.getDesc());
			}
			return GetRest.getSuccess("上传成功",r.getServiceName()+r.getFilePath()+r.getFileName());
		} catch (Exception e) {
			return GetRest.getFail("上传失败");
		}
	}
	
	
	@RequestMapping(value = MappingUtil.validCode)
	public void validCode(String login, HttpServletResponse response) {
		if(StringUtils.isEmpty(login)) {
			throw new ThrowJsonException("未获取到账户信息");
		}
		try {
			// 创建验证码对象
			VerifyCode verifyCode = new VerifyCode();
			// 创建图片缓存区
			BufferedImage image = verifyCode.getImage();
			redisUtilsService.setKey("validcode_"+login, verifyCode.getText(), 180);//验证码180s过期
			VerifyCode.output(image, response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new ThrowJsonException("生成失败");
	}
	
	/**
	 * 获取系统信息
	 * @return
	 */
	@RequestMapping(value = MappingUtil.getSysInfo)
	@ResponseBody
	public RestResponse getSysInfo() {
		String usdt_cash_min=redisUtilsService.getKey(SysParamsEnums.usdt_cash_min.getKey());
		String usdt_cash_max=redisUtilsService.getKey(SysParamsEnums.usdt_cash_max.getKey());
		String usdt_cash_scale=redisUtilsService.getKey(SysParamsEnums.usdt_cash_scale.getKey());
		String service_phone=redisUtilsService.getKey(SysParamsEnums.service_phone.getKey());
		String appdownload_url=redisUtilsService.getKey(SysParamsEnums.appdownload_url.getKey());
		String about_us=redisUtilsService.getKey(SysParamsEnums.about_us.getKey());
		JSONObject map=new JSONObject();
		map.put("usdt_cash_min", usdt_cash_min);
		map.put("usdt_cash_max", usdt_cash_max);
		map.put("usdt_cash_scale", usdt_cash_scale);
		map.put("service_phone", service_phone);
		map.put("about_us", about_us);
		map.put("appdownload_url", appdownload_url);
		return GetRest.getSuccess("成功",map);
	}
}
