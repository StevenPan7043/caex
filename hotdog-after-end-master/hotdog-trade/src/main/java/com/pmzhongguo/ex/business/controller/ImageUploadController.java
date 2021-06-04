package com.pmzhongguo.ex.business.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.model.OssConstants;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.stream.FileImageInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @description: 图片上传百度富文本
 * @date: 2019-01-17 17:22
 * @author: 十一
 */
@ApiIgnore
@Controller
@RequestMapping("/m/upload")
public class ImageUploadController extends TopController{

    private Logger logger = LoggerFactory.getLogger(ImageUploadController.class);
    /**
     *
     * @return 名称：yyyyMMddHHmmss + 时间戳 + 4为随机数
     */
    private static String randomFileName() {
        Random random = new Random();
        return HelpUtils.getCurrTime() + System.currentTimeMillis() + random.nextInt(10000);
    }

    /**
     *
     * @param upfile 从MultipartFile对象中获取上传文件
     * @return map，url是在浏览器中可以访问的图片链接
     */
    @RequestMapping(value = "/ueditor",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> ImgUeditorUpload(@RequestParam("upfile") MultipartFile upfile) {
        Map<String,Object> parm = new HashMap<>();
        OSSClient ossClient = new OSSClient(OssConstants.ALIYUN_OSS_ENDPOINT, OssConstants.ALIYUN_ACCESS_KEY, OssConstants.ALIYUN_ACCESS_SECRET);
        String fileName = upfile.getOriginalFilename();
        try {
            byte[] bytes = upfile.getBytes();
            String contentType = upfile.getContentType();
            fileName = randomFileName() + fileName;
            uploadByteOSS(ossClient, bytes, contentType, fileName);
        } catch (IOException e) {
            logger.error("富文本上传图片失败：" + e.getMessage());
            e.printStackTrace();
        }
        String url = OssConstants.ALIYUN_HOST + OssConstants.ALIYUN_UEDITOR_DIR + fileName;
        parm.put("state", "SUCCESS");
        parm.put("url", url);
        System.out.println("访问路径: "+url);
        return parm;
    }

    /**
     *
     * @param ossClient oss对象
     * @param b 图片字节数组
     * @param contentType 图片类型
     * @param fileName 图片名称：yyyyMMddHHmmss + 时间戳 + 4为随机数 + 图片原名称
     * @return
     */
    public static String uploadByteOSS(OSSClient ossClient, byte[] b, String contentType, String fileName) {

        Long fileSize = (long) b.length;
        // 创建上传Object的Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileSize);
        // 指定该Object被下载时的网页的缓存行为
        metadata.setCacheControl("no-cache");
        // 指定该Object下设置Header
        metadata.setHeader("Pragma", "no-cache");
        // 指定该Object被下载时的内容编码格式
        metadata.setContentEncoding("utf-8");
        // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
        // 如果没有扩展名则填默认值application/octet-stream
        metadata.setContentType(contentType);
        // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
        metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
        // http://bigen.oss-cn-shanghai.aliyuncs.com/ueditor/upfile  实际上是这个路径
        ossClient.putObject(OssConstants.BUCKET_TEMP_NAME, OssConstants.ALIYUN_UEDITOR_DIR + fileName, new ByteArrayInputStream(b),metadata);
        ossClient.shutdown();
        String filepath = OssConstants.ALIYUN_UEDITOR_DIR + fileName;
        System.out.println("oss路径："+filepath);
        return filepath;
    }


    public static void main(String[] args) {
//        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
//        byte[] bytes = image2byte("/temp/shoukuanma.jpg");
//        String s = uploadByteOSS(ossClient, bytes, BUCKET_NAME, "shoukuname.jpg");
//        System.out.println(s);
    }

}
