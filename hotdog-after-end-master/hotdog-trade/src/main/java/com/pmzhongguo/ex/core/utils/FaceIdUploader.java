package com.pmzhongguo.ex.core.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author zn
 * 阿里图片异步上传
 */

public class FaceIdUploader implements Runnable {
    //用map储存路径，可以异步保存到数据库
    private Map<String,Object>[] multipartFiles;

    public FaceIdUploader(Map<String,Object>... multipartFiles){
       this.multipartFiles = multipartFiles;
    }

    @Override
    public void run() {
        AliyunOSS.uploadFaceId2OSS(multipartFiles);
    }

    //生成随机文件名
    public static String randomFileName()
    {
        Random random = new Random();
        return HelpUtils.getCurrTime() + System.currentTimeMillis() + random.nextInt(10000);
    }


    public static Map<String,Object>[] multipartFiles2Map(MultipartFile... multipartFiles){
        Map<String,Object>[] result = new Map[multipartFiles.length];
        for (int i = 0; i < multipartFiles.length; i++) {
            Map multi = new HashMap();
            multi.put("randomName",randomFileName() + multipartFiles[i].getOriginalFilename());
            multi.put("multipartFile",multipartFiles[i]);
            result[i] = multi;
        }
        return result;
    }


}
