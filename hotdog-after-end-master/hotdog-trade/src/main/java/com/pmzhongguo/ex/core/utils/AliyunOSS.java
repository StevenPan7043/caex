package com.pmzhongguo.ex.core.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PolicyConditions;
import com.aliyun.oss.model.PutObjectResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.pmzhongguo.ex.business.model.OssConstants;
import com.pmzhongguo.ex.core.web.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 阿里云OSS存储服务 Introduce
 * <p>File：AliyunOSS.java</p>
 * <p>Description: ${description}</p>
 * <p>Copyright: Copyright (c) 2019/3/8 10:45</p>
 * <p>Company: zzex</p>
 *
 * @author yukai
 * @Version: 1.0
 */
public class AliyunOSS
{
    private static final Logger logger = LoggerFactory.getLogger(AliyunOSS.class);

    /**
     * 富文本上传的路径
     */
//    private static final String DIR = "ueditor/";

    /**
     * 获取阿里云OSS上传策略
     *
     * @param dir  上传路径
     * @param host 服务器上传地址
     * @return {@link String}
     */
    public static Map getPostPolicy(String dir, String host)
    {
        Map policy = null;
        OSSClient ossClient = new OSSClient(OssConstants.ALIYUN_OSS_ENDPOINT, OssConstants.ALIYUN_ACCESS_KEY, OssConstants.ALIYUN_ACCESS_SECRET);
        try {
            long expireEndTime = System.currentTimeMillis() + 60 * 1000;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);
            Map<String, String> respMap = Maps.newLinkedHashMap();
            respMap.put("dir", dir);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("accessid", OssConstants.ALIYUN_ACCESS_KEY);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
            policy = respMap;
        } catch (Exception e) {
            logger.error("获取策略失败：{} ", e.getLocalizedMessage());
        } finally {
            ossClient.shutdown();
        }
        return policy;
    }

    /**
     * 转移临时空间中的文件到正式空间
     * <p>
     * 将真实有性的文件转移到正式空间，转移完毕之后自动清理临时文件。
     * 另：临时空间可做定期清理以减少资料开销
     * </p>
     *
     * @param fileNames
     */
   /* public static boolean transferObjects(String... fileNames)
    {
        OSSClient ossClient = new OSSClient(Constants.ALIYUN_OSS_ENDPOINT, Constants.ALIYUN_ACCESS_KEY, Constants.ALIYUN_ACCESS_SECRET);
        boolean flag = true;
        try {
            for (String fileName : fileNames) {
                if (ossClient.doesObjectExist(Constants.BUCKET_TEMP_NAME, fileName)) {
                    ossClient.copyObject(Constants.BUCKET_TEMP_NAME, fileName, Constants.BUCKET_RELEASE_NAME, fileName);
                    ossClient.deleteObject(Constants.BUCKET_TEMP_NAME, fileName);
                }
            }
        } catch (RuntimeException e) {
            logger.error("转移文件失败：{} ", e.getLocalizedMessage());
            flag = false;
        } finally {
            ossClient.shutdown();
        }
        return flag;
    }*/

    /**
     * 删除临时空间中的文件
     *
     * @param fileName 文件名
     * @return {@link Boolean}
     */
    public static boolean deleteObject(String fileName)
    {
        boolean flag = true;
        OSSClient ossClient = new OSSClient(OssConstants.ALIYUN_OSS_ENDPOINT, OssConstants.ALIYUN_ACCESS_KEY, OssConstants.ALIYUN_ACCESS_SECRET);
        try {
            ossClient.deleteObject(OssConstants.BUCKET_TEMP_NAME, fileName);
        } catch (RuntimeException e) {
            flag = false;
            logger.error("删除临时文件失败：{} ", e.getLocalizedMessage());
        } finally {
            ossClient.shutdown();
        }
        return flag;
    }

    /**
     * 上传图片至OSS
     *
     * @param multipartFiles
     * @return
     */
    public static List<String> uploadObject2OSS(MultipartFile... multipartFiles)
    {
        OSSClient ossClient = new OSSClient(OssConstants.ALIYUN_OSS_ENDPOINT, OssConstants.ALIYUN_ACCESS_KEY, OssConstants.ALIYUN_ACCESS_SECRET);
        List<String> keys = Lists.newArrayList();
        for (MultipartFile multipartFile : multipartFiles) {
            try {
                String fileName = randomFileName() + multipartFile.getOriginalFilename();
                //以输入流的形式上传文件
                InputStream is = multipartFile.getInputStream();
                //创建上传Object的Metadata
                ObjectMetadata metadata = new ObjectMetadata();
                //上传的文件的长度
                metadata.setContentLength(is.available());
                //指定该Object被下载时的网页的缓存行为
                metadata.setCacheControl("no-cache");
                //指定该Object下设置Header
                metadata.setHeader("Pragma", "no-cache");
                //指定该Object被下载时的内容编码格式
                metadata.setContentEncoding("utf-8");
                //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
                //如果没有扩展名则填默认值application/octet-stream
                metadata.setContentType(multipartFile.getContentType());
                //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
                metadata.setContentDisposition("filename/filesize=" + fileName + "/" + is.available() + "Byte.");
                PutObjectResult putResult = ossClient.putObject(OssConstants.BUCKET_TEMP_NAME, OssConstants.ALIYUN_UEDITOR_DIR + fileName, is, metadata);
                keys.add(OssConstants.ALIYUN_UEDITOR_DIR + fileName);
//            resultStr = putResult.getETag();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
            }
        }
        return keys;
    }

    /**
     * @return 名称：yyyyMMddHHmmss + 时间戳 + 4为随机数
     */
    private static String randomFileName()
    {
        Random random = new Random();
        return HelpUtils.getCurrTime() + System.currentTimeMillis() + random.nextInt(10000);
    }


    /**
     * 上传人脸验证图片至OSS
     *
     * @return
     */
    public static List<String> uploadFaceId2OSS(Map<String,Object>... multipartFiles)
    {
        OSSClient ossClient = new OSSClient(OssConstants.ALIYUN_OSS_ENDPOINT, OssConstants.ALIYUN_ACCESS_KEY, OssConstants.ALIYUN_ACCESS_SECRET);
        List<String> keys = Lists.newArrayList();
        for (Map<String,Object> multi : multipartFiles) {
            try {
                String fileName = (String) multi.get("randomName");
                //以输入流的形式上传文件
                MultipartFile multipartFile = (MultipartFile) multi.get("multipartFile");
                InputStream is = multipartFile.getInputStream();
                //创建上传Object的Metadata
                ObjectMetadata metadata = new ObjectMetadata();
                //上传的文件的长度
                metadata.setContentLength(is.available());
                //指定该Object被下载时的网页的缓存行为
                metadata.setCacheControl("no-cache");
                //指定该Object下设置Header
                metadata.setHeader("Pragma", "no-cache");
                //指定该Object被下载时的内容编码格式
                metadata.setContentEncoding("utf-8");
                //文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
                //如果没有扩展名则填默认值application/octet-stream
                metadata.setContentType(multipartFile.getContentType());
                //指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）
                metadata.setContentDisposition("filename/filesize=" + fileName + "/" + is.available() + "Byte.");
                PutObjectResult putResult = ossClient.putObject(OssConstants.BUCKET_TEMP_NAME, OssConstants.ALIYUN_UEDITOR_DIR + fileName, is, metadata);
                keys.add(OssConstants.ALIYUN_UEDITOR_DIR + fileName);
//            resultStr = putResult.getETag();

            } catch (Exception e) {
                e.printStackTrace();
                logger.error("上传阿里云OSS服务器异常." + e.getMessage(), e);
            }
        }
        return keys;
    }


}
