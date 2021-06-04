package com.image.common;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class Service {
    private static final Logger loger = LoggerFactory.getLogger(Service.class);
    private static final String SERVICE_NAME = "http://img.sanxininfo.com/";

    /**
     * 上传任意文件
     *
     * @param fileName    文件名
     * @param inputStream 文件流
     * @param path        路径
     * @return
     */
    public static RestUploadFileInfo upload(String fileName, InputStream inputStream, String path) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        loger.debug("ext:" + ext);
        if ((ext == null) || (ext.length() == 0)) {
            RestUploadFileInfo RestUploadFileInfo = new RestUploadFileInfo();
            RestUploadFileInfo.setStatus(false);
            RestUploadFileInfo.setDesc("文件名称没有后缀");
            return RestUploadFileInfo;
        }
        RestUploadFileInfo RestUploadFileInfo = null;
        boolean isImage = (ext.equals("gif")) || (ext.equals("GIF")) || (ext.equals("jpg")) || (ext.equals("JPG")) || (ext.equals("png")) || (ext.equals("PNG")) || (ext.equals("JPEG")) || (ext.equals("jpeg"));
        if (isImage)
            RestUploadFileInfo = uploadImage(fileName, inputStream, path);
        else {
            RestUploadFileInfo = uploadOther(fileName, inputStream, path);
        }

        return RestUploadFileInfo;
    }

    /**
     * 二维码生成
     *
     * @param path
     * @param content
     * @return
     */
    public static RestUploadFileInfo uploadQR(String path, String content) {
        String result = HttpUtil.postServerBig(SERVICE_NAME + "image/uploadQR", "path=" + path + "&content=" + content);
        RestUploadFileInfo RestUploadFileInfo = (RestUploadFileInfo) JSON.parseObject(result, RestUploadFileInfo.class);
        return RestUploadFileInfo;
    }

    /**
     * 二维码生成logo
     *
     * @param path
     * @param content
     * @return
     */
    public static RestUploadFileInfo uploadQrLogo(String path, String content) {
        String result = HttpUtil.postServerBig(SERVICE_NAME + "image/uploadQrLogo", "path=" + path + "&content=" + content);
        RestUploadFileInfo RestUploadFileInfo = (RestUploadFileInfo) JSON.parseObject(result, RestUploadFileInfo.class);
        return RestUploadFileInfo;
    }

    public static RestUploadFileInfo check(String fileName, InputStream inputStream, String path) {
        if ((fileName == null) || (fileName.length() == 0)) {
            RestUploadFileInfo RestUploadFileInfo = new RestUploadFileInfo();
            RestUploadFileInfo.setStatus(false);
            RestUploadFileInfo.setDesc("文件名称不能为空");
            return RestUploadFileInfo;
        }

        if (inputStream == null) {
            RestUploadFileInfo RestUploadFileInfo = new RestUploadFileInfo();
            RestUploadFileInfo.setStatus(false);
            RestUploadFileInfo.setDesc("文件流不能为空");
            return RestUploadFileInfo;
        }

        RestUploadFileInfo RestUploadFileInfo = new RestUploadFileInfo();
        RestUploadFileInfo.setStatus(true);
        RestUploadFileInfo.setDesc("ok");
        return RestUploadFileInfo;
    }

    /**
     * 图片上传
     *
     * @param fileName
     * @param inputStream
     * @param path
     * @return
     */
    public static RestUploadFileInfo uploadImage(String fileName, InputStream inputStream, String path) {
        RestUploadFileInfo RestUploadFileInfoCheck = check(fileName, inputStream, path);
        if (!RestUploadFileInfoCheck.isStatus()) {
            return RestUploadFileInfoCheck;
        }
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        boolean isImage = (ext.equals("gif")) || (ext.equals("GIF")) || (ext.equals("jpg")) || (ext.equals("JPG")) || (ext.equals("png")) || (ext.equals("PNG")) || (ext.equals("JPEG")) || (ext.equals("jpeg"));
        if (!isImage) {
            RestUploadFileInfo RestUploadFileInfo = new RestUploadFileInfo();
            RestUploadFileInfo.setStatus(false);
            RestUploadFileInfo.setDesc("文件非图片格式，只支持gif,jpg,png");
            return RestUploadFileInfo;
        }
        String result = HttpUtil.postServerFile(SERVICE_NAME + "image/upload", fileName, inputStream, path);
        RestUploadFileInfo RestUploadFileInfo = (RestUploadFileInfo) JSON.parseObject(result, RestUploadFileInfo.class);
        return RestUploadFileInfo;
    }

    /**
     * 上传任意文件
     *
     * @param fileName
     * @param inputStream
     * @param path
     * @return
     */
    public static RestUploadFileInfo uploadOther(String fileName, InputStream inputStream, String path) {
        RestUploadFileInfo RestUploadFileInfoCheck = check(fileName, inputStream, path);
        if (!RestUploadFileInfoCheck.isStatus()) {
            return RestUploadFileInfoCheck;
        }
        String result = HttpUtil.postServerFile(SERVICE_NAME + "image/upload", fileName, inputStream, path);
        RestUploadFileInfo RestUploadFileInfo = (RestUploadFileInfo) JSON.parseObject(result, RestUploadFileInfo.class);
        return RestUploadFileInfo;
    }

    /**
     * 文件删除
     *
     * @param fileName 文件名称
     * @param path     路径
     * @return
     */
    public static RestDeleteFileInfo deleteImage(String path) {
        String result = HttpUtil.getServer(SERVICE_NAME + "image/delete", "?path=" + path);
        RestDeleteFileInfo RestUploadFileInfo = (RestDeleteFileInfo) JSON.parseObject(result, RestDeleteFileInfo.class);
        return RestUploadFileInfo;
    }
}