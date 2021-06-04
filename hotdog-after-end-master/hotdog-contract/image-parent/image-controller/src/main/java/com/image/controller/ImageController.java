package com.image.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.image.common.RestDeleteFileInfo;
import com.image.common.RestUploadFileInfo;
import com.image.util.Contant;
import com.image.util.QRCodeUtil;
import com.image.util.Util;

import common.Logger;

@Controller
public class ImageController{
  Logger log = Logger.getLogger(ImageController.class);

  @RequestMapping(value="/image/upload")
  @ResponseBody
  public RestUploadFileInfo upload(HttpServletRequest request, String path) { 
	CommonsMultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
    if (resolver.isMultipart(request)) {
      MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;

      Iterator iter = multiRequest.getFileNames();
      if (iter.hasNext()) {
        String fileInputName = (String)iter.next();

        MultipartFile file = multiRequest.getFile(fileInputName);

        String fileName = file.getOriginalFilename();
        
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        if("jpeg".equals(ext.toLowerCase())){
        	ext="jpg";
        }
        String name=Util.getTimeString();
        fileName =name + "." + ext;
//        String smallname=name+"_small" + "." + ext;
//        String middlename=name+"_middle" + "." + ext;
        String savePath = Util.getFilePath(request) + Util.getPath(path);
        File targetFile = new File(savePath, fileName);
        if (!targetFile.exists()) {
          targetFile.mkdirs();
        }
        try
        {
          file.transferTo(targetFile);
          RestUploadFileInfo RestUploadFileInfo = new RestUploadFileInfo();
          RestUploadFileInfo.setStatus(true);
          RestUploadFileInfo.setDesc("文件上传成功");
          RestUploadFileInfo.setFilePath("/"+Contant.files  + Util.getPath(path));
          RestUploadFileInfo.setFileName(fileName);
          RestUploadFileInfo.setServiceName(Util.getServiceName(request));
          boolean isImage = (ext.equals("jpg")) || (ext.equals("JPG")) || (ext.equals("png")) || (ext.equals("PNG")) || (ext.equals("JPEG")) || (ext.equals("jpeg"))|| (ext.equals("gif")) || (ext.equals("GIF"));
          if(isImage){
        	   //1生成APP小图
            //  ImgCompress.resize(targetFile, 220, 200, savePath, smallname);
              //2生成APP中图
           //   ImgCompress.resizeByWidth(targetFile, 690, savePath, middlename);
          }
          return RestUploadFileInfo;
        } catch (Exception e) {
          e.printStackTrace();

          RestUploadFileInfo RestUploadFileInfo = new RestUploadFileInfo();
          RestUploadFileInfo.setStatus(false);
          RestUploadFileInfo.setDesc("文件上传出现异常");
          return RestUploadFileInfo;
        }
      }
    }

    RestUploadFileInfo RestUploadFileInfo = new RestUploadFileInfo();
    RestUploadFileInfo.setStatus(false);
    RestUploadFileInfo.setDesc("文件不存在");
    return RestUploadFileInfo; }

  @RequestMapping(value="/image/uploadQR")
  @ResponseBody
  public RestUploadFileInfo uploadQR(HttpServletRequest request, String path, String content) {
    RestUploadFileInfo result = new RestUploadFileInfo();
    try {
      MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
      Map hints = new HashMap();
      hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
      hints.put(EncodeHintType.MARGIN,0);
      String realPath = "/"+Contant.files+ Util.getPath(path);
      String savePath = Util.getFilePath(request) + Util.getPath(path);
      BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 340, 340, hints);
      String fileName = Util.getTimeString();
      File file1 = new File(savePath, fileName + ".png");
      if (!file1.exists()) {
        file1.mkdirs();
      }
      MatrixToImageWriter.writeToFile(bitMatrix, "png", file1);
      result.setStatus(true);
      result.setDesc("二维码生成成功");
      result.setFilePath(realPath);
      result.setFileName(fileName + ".png");
      result.setServiceName(Util.getServiceName(request));
    } catch (Exception e) {
      result.setStatus(false);
      result.setDesc("二维码生成失败");
      return result;
    }
    return result;
  }
  @RequestMapping(value="/image/delete")
  @ResponseBody
  public RestDeleteFileInfo delete(HttpServletRequest request, String path) {
    String deleteFile =Util.getFilePath(request) + Util.getPath(path);
    File file = new File(deleteFile);

    if ((file.isFile()) && (file.exists()))
    {
      file.delete();
    } else {
      RestDeleteFileInfo RestUploadFileInfo = new RestDeleteFileInfo();
      RestUploadFileInfo.setStatus(true);
      RestUploadFileInfo.setDesc("文件已经不存在。");
      return RestUploadFileInfo;
    }
    RestDeleteFileInfo RestUploadFileInfo = new RestDeleteFileInfo();
    RestUploadFileInfo.setStatus(true);
    RestUploadFileInfo.setDesc("文件删除成功");
    return RestUploadFileInfo;
  }
  
  @RequestMapping(value="/image/uploadQrLogo")
  @ResponseBody
  public RestUploadFileInfo uploadQrLogo(HttpServletRequest request, String path, String content) {
	  RestUploadFileInfo result = new RestUploadFileInfo();
	  try {
		  String logo=request.getRealPath("/files")+"/bg.png";
		  String fileName1 = Util.getTimeString();
		  String realPath = "/"+Contant.files+ Util.getPath(path);
	      String savePath = Util.getFilePath(request) + Util.getPath(path);
		  QRCodeUtil.encode(content,logo,savePath,fileName1 + ".jpg"); 
		  result.setStatus(true);
	      result.setDesc("二维码生成成功");
	      result.setFilePath(realPath);
	      result.setFileName(fileName1 + ".jpg");
	      result.setServiceName(Util.getServiceName(request));
	  } catch (Exception e) {
		  result.setStatus(false);
	      result.setDesc("二维码生成失败");
	  }
	  return result;
  } 
  
  @RequestMapping(value="/index")
  public ModelAndView index(){
	  return new ModelAndView("/index");
  }
}