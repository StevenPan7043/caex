package com.pmzhongguo.ex.core.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Created by 张众
 * @Date 2019/11/30 16:14SecretKey
 * @Description
 */
public class AuthUtils {
    private static final String OCR_URL = "http://api.selectholiday.cn/invoke/ocr/idcard";
    private static final String OCR_KEY = "2c9a089a6e5edab5016eba6700a3165b";
    public static final String OCR_IMAGE_FRONT = "front";
    public static final String OCR_IMAGE_BACK = "back";
    //用户请求OCR限制前缀 保存redis中 memberid + _ocr_member_request_count
    public static final String OCR_MEMBER_REQUEST_COUNT_KEY = "_ocr_member_request_count";
    //用户请求人脸比对限制前缀 保存redis中 memberid + _ocr_member_request_count
    public static final String PORTRAIT_CONTRAST_MEMBER_REQUEST_COUNT_KEY = "_portrait_contrast_member_request_count";

    /**
     *
     * @param idCardSide 身份证正反面
     * @param image base64 编码后的图片 (不带图片头)
     * @return
     */
    public static String ocr(String idCardSide,String image){
        Map<String, String> map = new HashMap<>();
        map.put("key",AuthPropertiesUtil.getPropValByKey("OCR_KEY"));
        map.put("image",image);
        map.put("idCardSide",idCardSide);
        String result = HelpUtils.post(AuthPropertiesUtil.getPropValByKey("OCR_URL"), map);
        return result;
    }

    /**
     *
     * @param imageBase64 Base64加密后的人脸图
     * @param realname 身份证中的名称
     * @param idcard 身份证号
     * @return
     */
    public static String sendPortraitContrast(String imageBase64, String realname, String idcard) {
        Map<String, Object> map = new HashMap<>();
        map.put("key", AuthPropertiesUtil.getPropValByKey("PORTRAIT_CONTRAST_KEY"));
        map.put("photo", imageBase64);
        map.put("realname", realname);
        map.put("idcard", idcard);
        //创建签名 sign
        String date = DateUtil.format(DateUtil.date(), "yyyyMMdd");
        String str = AuthPropertiesUtil.getPropValByKey("PORTRAIT_CONTRAST_KEY") + idcard + AuthPropertiesUtil.getPropValByKey("PORTRAIT_CONTRAST_SECRETKEY") + date;
        String sign = SecureUtil.md5(str);
        map.put("sign", sign);
        return HttpUtil.post(AuthPropertiesUtil.getPropValByKey("PORTRAIT_CONTRAST_URL"), map);
    }
}
