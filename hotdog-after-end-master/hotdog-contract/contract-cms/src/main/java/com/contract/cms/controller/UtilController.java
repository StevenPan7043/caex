package com.contract.cms.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.contract.cms.common.MappingUtils;
import com.contract.common.FunctionUtils;
import com.contract.common.PathUtils;


import com.image.common.RestUploadFileInfo;

@Controller
public class UtilController {
	
	/***
	 * 富文本图片上传 (百度富文本编辑器 )
	 * 
	 * @param upfile
	 * @param response
	 * @return
	 */
	@RequestMapping(value = MappingUtils.upload_RichFile)
	@ResponseBody
	public Object uploadimage(@Param("upfile") MultipartFile upfile, String path, HttpServletResponse response) {
		return FunctionUtils.uploadImage(upfile, path);
	}

	/***
	 * 异步上传图片
	 * @param file
	 * @return
	 */
	@RequestMapping(value = MappingUtils.uploadFile)
	@ResponseBody
	public RestResponse uploadFile(@RequestParam("file") MultipartFile upfile) {
		if (upfile != null) {
			RestUploadFileInfo fileInfo;
			try {
				BufferedImage image = ImageIO.read(upfile.getInputStream());
				String dimensionStr = image.getWidth() + "#" + image.getHeight();
				fileInfo = com.image.common.Service.upload(upfile.getOriginalFilename(), upfile.getInputStream(), PathUtils.common_url);
				if (fileInfo.isStatus()) {
					String url = fileInfo.getServiceName() + fileInfo.getFilePath() + fileInfo.getFileName();
					return GetRest.getSuccess("上传成功", url,dimensionStr);
				} else {
					return GetRest.getFail(fileInfo.getDesc());
				}
			} catch (IOException e) {
				e.printStackTrace();
				return GetRest.getFail("上传文件失败,请重试");
			}
		}
		return GetRest.getFail("请选择文件");
	}
	
}
