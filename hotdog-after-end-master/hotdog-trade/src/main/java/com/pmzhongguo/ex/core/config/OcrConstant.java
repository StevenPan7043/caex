package com.pmzhongguo.ex.core.config;

/**
 *
 * 人脸图片规定：人脸比对图片格式必须为JPG（JPEG）,BMP,PNG,GIF,TIFF之一,宽和高必须大于 8px,小于等于 4000px,要求编码后图片大小不超过5M
 * @description: 一定要写注释啊
 * @date: 2019-12-29 14:33
 * @author: 十一
 */
public class OcrConstant {


    /**
     * webapi 接口地址
     */
    public static final String CHECK_WEBWFV_URL = "https://api.xfyun.cn/v1/service/v1/image_identify/face_verification";
    /**
     * 应用ID (必须为webapi类型应用，并开通人脸比对服务，参考帖子如何创建一个webapi应用：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=36481)
     */
    public static final String CHECK_APPID = "5e033445";
    /**
     * 接口密钥(webapi类型应用开通人脸比对服务后，控制台--我的应用---人脸比对---相应服务的apikey)
     */
    public static final String CHECK_API_KEY = "d23161f9425313f7adee18292d0fabc6";

    /**
     * 图片格式
     */
    public static final String CHECK_IMAGE_FORMAT = "|JPG|JPEG|BMP|PNG|GIF|TIFF|";

    /**
     * 图片高、宽度最小限制
     */
    public static final int CHECK_IMAGE_WIDTH_OR_HEIGHT_LOWER_LIMIT = 8;

    /**
     * 图片高、宽度最大限制
     */
    public static final int CHECK_IMAGE_WIDTH_OR_HEIGHT_UPPER_LIMIT = 4000;

    /**
     * 编码后图片最大容量
     */
    public static final int CHECK_IMAGE_MAX_SIZE_LIMIT = 5;

    /**
     * 编码后图片最大容量，单位（M）
     */
    public static final String CHECK_IMAGE_SIZE_UNIT = "M";



//    =======================  身份证信息识别  ================================================

    /**
     * OCR webapi 接口地址
     */
    public static final String OCR_WEBOCR_URL = "http://webapi.xfyun.cn/v1/service/v1/ocr/idcard";
    /**
     * 应用APPID（必须为webapi类型应用，并开通身份证识别服务，参考帖子如何创建一个webapi应用：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=36481）
     */
    public static final String OCR_APPID = "5ed49c35";
    /**
     * 接口密钥（webapi类型应用开通身份证识别服务后，控制台--我的应用---身份证识别---相应服务的apikey）
     */
    public static final String OCR_API_KEY = "17cf05a13e66255e6aaa743c74e5051e";
    /**
     * 引擎类型
     */
    public static final String OCR_ENGINE_TYPE = "idcard";
    /**
     * 是否返回头像图片
     */
    public static final String OCR_HEAD_PORTRAIT = "1";
    /**
     * 是否返回切片图
     */
    //public static final String OCR_CROP_IMAGE = "0";

    /**
     * 编码后图片最大容量
     */
    public static final int OCR_IMAGE_MAX_SIZE_LIMIT = 4;

    /**
     * 是否返回身份证号码区域截图
     */
    public static final String OCR_ID_NUMBER_IMAGE = "1";


}
