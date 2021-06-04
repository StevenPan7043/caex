package com.pmzhongguo.otc.faceid;

import com.pmzhongguo.ex.core.utils.HttpUtils;
import com.pmzhongguo.ex.core.utils.PropertiesUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 旷视科技faceid接口 文档：https://faceid.com/faceid-open-doc/docs/h5.html
 *
 * @author: Zn
 */
public class FaceIdUtil {
    private static Logger logger = LoggerFactory.getLogger(FaceIdUtil.class);
    public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    private static final String API_KEY = "BEv7LZPvz6QaDzF1OchUxYKGn9u0aLg9";
    private static final String API_SERCET = "GuxTdQPo7NGxqahZnU6U8xpIXXq_zGTT";
    //签名的有效时间 单位为秒
    public static final long EXPIRE_TIME = 60 * 30;
    //进行人脸验证的地址，需要和生成的BizToken进行拼接
    public static final String DO_URL = "https://openapi.faceid.com/lite/v1/do/";
    //获取token的url地址
    private static final String GET_BIZ_TOKEN_URL = "https://openapi.faceid.com/lite/v1/get_biz_token";
    private static final String GET_ORC_BIZ_TOKEN_URL = "https://openapi.faceid.com/lite_ocr/v1/get_biz_token";
    //private static final String RETURN_URL = PropertiesUtil.getPropValByKey("faceid_return_url") == null ? "www.zzexvip.com" : PropertiesUtil.getPropValByKey("faceid_return_url");
    //private static final String NOTIFY_URL = PropertiesUtil.getPropValByKey("faceid_notify_url") == null ? "www.zzexvip.com/m/notify" : PropertiesUtil.getPropValByKey("faceid_notify_url");
    private static final String RETURN_URL = PropertiesUtil.getPropValByKey("faceid_return_url");
    private static final String NOTIFY_URL = PropertiesUtil.getPropValByKey("faceid_notify_url");
    private static final String SIGN_VERSION = "hmac_sha1";
    private static final String LIVENESS_TYPE = "video_number";


    //获取人脸核身bizToken
    //biztoken包含以秒为单位的时间戳信息
    public static String getBizToken(String bizNo){
        String result = null;
        Map params = new HashMap(16);
        //签名算法版本，当前仅支持：hmac_sha1
        params.put("sign_version","hmac_sha1");
        //流程结束后跳转到客户定义的页面（回调方法为get return_url?biz_token=xxx），URL限定为http或https且非内网地址
        params.put("return_url",RETURN_URL);
        //流程结束后，FaceID会通知客户服务器，post请求到该url，必须以http或者https开头, post的参数名是data，data的值是一个json字符串，内容包括：biz_token, error_code，error_msg。
        params.put("notify_url",NOTIFY_URL);
        //本次身份验证服务的类型。0：表示活体照片和参考照片进行比对(人脸比对)1：表示活体照片和第三方权威数据进行比对（人脸核身）
        params.put("comparison_type","1");
        params.put("biz_no",bizNo);
        //身份证信息ORC
        params.put("group","1");
        //表示活体检测的类型，当前仅支持：video_number
        params.put("liveness_type",LIVENESS_TYPE);
        try {
            String sign = genSign(API_KEY, API_SERCET, EXPIRE_TIME);
            params.put("sign",sign);
            result = HttpUtils.post(GET_BIZ_TOKEN_URL, params);
            JSONObject object = JSONObject.fromObject(result);
            String bizToken = object.getString("biz_token");
            return bizToken;
        } catch (Exception e) {
            logger.warn("获取faceId的bizToken失败,请求用户id：" + bizNo,e);
        }
        return "";
    }

    //getresult
    public static String getResult(String bizToken,String url){
        String result = null;
        Map params = new HashMap(8);
        //签名算法版本，当前仅支持：hmac_sha1
        params.put("sign_version","hmac_sha1");
        params.put("biz_token",bizToken);
        try {
            params.put("sign",genSign(API_KEY,API_SERCET,EXPIRE_TIME));
            result = HttpUtils.get(url, params);
        } catch (Exception e) {
            logger.warn("获取faceId验证结果失败，bizToken:" + bizToken,e);
        }
        return result;
    }

    //身份证orc
    public static String getOrcBizToken(String bizNo) {
        String result = null;
        Map params = new HashMap(16);
        params.put("sign_version", SIGN_VERSION);
        params.put("return_url", RETURN_URL);
        params.put("notify_url", NOTIFY_URL);
        params.put("capture_image", 0);
        params.put("biz_no", bizNo);
        try {
            String sign = genSign(API_KEY, API_SERCET, EXPIRE_TIME);
            params.put("sign", sign);
            result = HttpUtils.post(GET_ORC_BIZ_TOKEN_URL, params);
            JSONObject object = JSONObject.fromObject(result);
            String bizToken = object.getString("biz_token");
            if (null != bizToken) {
                return bizToken;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getOrcRedirectUrl(String bizToken) {
        return "https://openapi.faceid.com/lite_ocr/v1/do/" + bizToken;
    }

    public static String getRedirectUrl(String bizToken) {
        return "https://openapi.faceid.com/lite/v1/do/" + bizToken;
    }

    /**
     * 生成签名字段
     *
     * @param apiKey
     * @param secretKey
     * @param expired
     * @return
     * @throws Exception
     */
    public static String genSign(String apiKey, String secretKey, long expired) throws Exception {
        long now = System.currentTimeMillis() / 1000;
        int rdm = Math.abs(new Random().nextInt());
        String plainText = String.format("a=%s&b=%d&c=%d&d=%d", apiKey, now + expired, now, rdm);
        byte[] hmacDigest = HmacSha1(plainText, secretKey);
        byte[] signContent = new byte[hmacDigest.length + plainText.getBytes().length];
        System.arraycopy(hmacDigest, 0, signContent, 0, hmacDigest.length);
        System.arraycopy(plainText.getBytes(), 0, signContent, hmacDigest.length,
                plainText.getBytes().length);
        return encodeToBase64(signContent).replaceAll("[\\s*\t\n\r]", "");
    }

    /**
     * 生成 base64 编码
     *
     * @param binaryData
     * @return
     */
    public static String encodeToBase64(byte[] binaryData) {
        String encodedStr = Base64.getEncoder().encodeToString(binaryData);
        return encodedStr;
    }

    /**
     * 生成 hmacsha1 签名
     *
     * @param binaryData
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] HmacSha1(byte[] binaryData, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        mac.init(secretKey);
        byte[] HmacSha1Digest = mac.doFinal(binaryData);
        return HmacSha1Digest;
    }

    /**
     * 生成 hmacsha1 签名
     *
     * @param plainText
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] HmacSha1(String plainText, String key) throws Exception {
        return HmacSha1(plainText.getBytes(), key);
    }

    //将结果码转换给客户看的原因
    public static String changeResultCodeToRejectReason(String resultCode,String resultMessage){
        switch (resultCode){
            case "1000" :
                return "未知错误请重试";
            case "2000":
                return "待比对人物与第三方权威数据照片对比不是同一个人";
            case "3000":
                if ("NO_ID_PHOTO".equals(resultMessage)){
                    return "第三方权威数据无照片，请联系客服";
                }
                return "身份证信息错误，请重试";
            case "3100":
                return "身份证识别错误，请到条件良好的环境拍摄身份证";
            case "3200":
                return "身份证识别错误，请到条件良好的环境拍摄身份证" ;
            case "4100":
                return "活体认证失败";
            case "4200":
                return "视频验证失败，请重试";
            case "6000":
                return "主动退出验证流程";
            case "9000":
                return "未进行身份识别验证";
                default:
                    return "未知错误，请联系管理员";
        }
    }
}




