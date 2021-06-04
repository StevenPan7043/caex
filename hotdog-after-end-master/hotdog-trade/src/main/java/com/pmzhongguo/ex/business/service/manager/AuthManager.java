package com.pmzhongguo.ex.business.service.manager;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.pmzhongguo.ex.business.entity.AuthIdentity;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.model.OssConstants;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.business.vo.AuthIdentityVo;
import com.pmzhongguo.ex.core.config.OcrConstant;
import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.faceid.Base64Util;
import com.pmzhongguo.zzextool.utils.StringUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.pmzhongguo.ex.core.config.OcrConstant.*;

/**
 * @description: 认证处理
 * @date: 2019-12-29 14:48
 * @author: 十一
 */
@Component
public class AuthManager {

    protected Logger logger = LoggerFactory.getLogger(AuthManager.class);

    @Autowired
    public DaoUtil daoUtil;

    @Autowired
    public MemberService memberService;

    ExecutorService ocrPool = Executors.newFixedThreadPool(20, new ZzexThreadFactory("AuthController_uploader"));

    /**
     * 处理已经认证的
     *
     * @param oIdAuthIdentity
     * @param member
     * @param ip
     * @return
     */
    @Transactional
    public ObjResp auth(AuthIdentity oIdAuthIdentity, Member member, String ip) {
        if (oIdAuthIdentity.getId_status() == 0) {
            return new ObjResp(ErrorInfoEnum.OCR_OCR_ID_STATUS_UNDER_REVIEW.getErrorCode()
                    , ErrorInfoEnum.OCR_OCR_ID_STATUS_UNDER_REVIEW.getErrorENMsg(), null);
        } else if (oIdAuthIdentity.getId_status() == 1) {
            return new ObjResp(ErrorInfoEnum.OCR_ID_STATUS_AUDIT_SUCCEEDED.getErrorCode()
                    , ErrorInfoEnum.OCR_ID_STATUS_AUDIT_SUCCEEDED.getErrorENMsg(), null);
        }
        //可能会出现用户上传信息已经入库，但是又因身份证号等信息对不上重新上传，所以这里要做删除操作
        //记录操作
        daoUtil.update("insert into m_member_oper_log (member_id,m_name,oper_type,oper_time,oper_memo,oper_ip)values(?,?,?,?,?,?)",
                member.getId(), member.getM_name(), 2, HelpUtils.formatDate6(new Date()),
                "OCR re upload Old AuthIdentity: id_number:" + oIdAuthIdentity.getId_number() +
                        " ,m_name:" + oIdAuthIdentity.getM_name() +
                        " ,name:" + oIdAuthIdentity.getFamily_name() + oIdAuthIdentity.getFamily_name() +
                        " ,last_submit_time:" + oIdAuthIdentity.getLast_submit_time() +
                        " ,audit_time:" + oIdAuthIdentity.getAuditor()
                , ip);
        //删除
        daoUtil.update("delete from m_auth_identity where id = ?", member.getId());
        return new ObjResp(ObjResp.SUCCESS, ObjResp.SUCCESS_MSG, null);
    }


    private File getFile(MultipartFile iDcardFront) throws IOException {
        //压缩图片并将尺寸固定在1080*x(自动扩展),服务商接收的图片尺寸 最短边>15px， 图片最长边<4096px
        File file = null;
        //因APP端传来的图片名为 iDcardFile 而非文件名 这里为兼容APP端做校验
        if (iDcardFront.getOriginalFilename().equals("iDcardFile")) {
            file = new File(iDcardFront.getOriginalFilename() + ".jpg");
        } else {
            file = new File(iDcardFront.getOriginalFilename());
        }
        Thumbnails.of(iDcardFront.getInputStream()).outputFormat("jpg").size(1024, 768).toFile(file);
        return file;
    }

    /**
     * 处理身份证前照，身份证号码识别
     *
     * @param iDcardFront
     * @param memberId
     * @return 返回扫描结果
     * @throws Exception
     */
    public ObjResp scanImageWithFront(MultipartFile iDcardFront, int memberId) throws Exception {
        File file = getFile(iDcardFront);
        if (file == null) {
            return new ObjResp(ErrorInfoEnum.OCR_FRONT_ERROR.getErrorCode(), ErrorInfoEnum.OCR_FRONT_ERROR.getErrorENMsg(), null);
        }
        byte[] imageByteArray = FileUtils.inputStream2ByteArray(new FileInputStream(file));
        String imageBase64 = new String(Base64.encodeBase64(imageByteArray), "UTF-8");
        String result = doPostForAuth(OCR_WEBOCR_URL, buildHttpHeaderForOcr(), "image=" + URLEncoder.encode(imageBase64, "UTF-8"));
        JSONObject frontMSGResult = JSONObject.parseObject(result);
        if (StringUtil.isNullOrBank(frontMSGResult)) {
            logger.warn("orc 扫描身份证获取结果为null: " + result);
            return new ObjResp(ErrorInfoEnum.OCR_FRONT_ERROR.getErrorCode(), ErrorInfoEnum.OCR_FRONT_ERROR.getErrorENMsg(), null);
        }
        String desc = frontMSGResult.getString("desc");
        frontMSGResult = frontMSGResult.getJSONObject("data");
        if ("success".equals(desc)) {

            if (StringUtil.isNullOrBank(frontMSGResult.getString("id_number"))) {
                return new ObjResp(ErrorInfoEnum.OCR_FRONT_ERROR.getErrorCode(), ErrorInfoEnum.OCR_FRONT_ERROR.getErrorENMsg(), null);
            }
        } else {
            logger.warn("orc 扫描身份证识别错误: " + frontMSGResult.toJSONString());
            return new ObjResp(ErrorInfoEnum.OCR_FRONT_ERROR.getErrorCode(), ErrorInfoEnum.OCR_FRONT_ERROR.getErrorENMsg(), null);
        }
        //校验身份证号是否存在
        Integer alreadyIdNumCount = daoUtil.queryForInt("SELECT COUNT(1) FROM m_auth_identity WHERE id <> ? AND id_number = ?"
                , memberId, frontMSGResult.getString("id_number"));
        if (alreadyIdNumCount > 0) {
            return new ObjResp(ErrorInfoEnum.LANG_ID_NUM_ALREADY_EXIST.getErrorCode()
                    , ErrorInfoEnum.LANG_ID_NUM_ALREADY_EXIST.getErrorENMsg(), null);
        }
        return new ObjResp(ObjResp.SUCCESS, ObjResp.SUCCESS_MSG, frontMSGResult);
    }

    /**
     * 组装http请求头
     */
    private static Map<String, String> buildHttpHeaderForOcr() throws UnsupportedEncodingException {
        String curTime = System.currentTimeMillis() / 1000L + "";
        String param = "{\"engine_type\":\"" + OcrConstant.OCR_ENGINE_TYPE + "\",\"head_portrait\":\""
                + OcrConstant.OCR_HEAD_PORTRAIT + "\",\"id_number_image\":\"" + OcrConstant.OCR_ID_NUMBER_IMAGE +"\"}";
        String paramBase64 = new String(Base64.encodeBase64(param.getBytes("UTF-8")));
        String checkSum = DigestUtils.md5Hex(OcrConstant.OCR_API_KEY + curTime + paramBase64);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.put("X-Param", paramBase64);
        header.put("X-CurTime", curTime);
        header.put("X-CheckSum", checkSum);
        header.put("X-Appid", OcrConstant.OCR_APPID);
        return header;
    }


    /**
     * 组装图片对比http请求头
     *
     * @throws JSONException
     */
    private static Map<String, String> buildHttpHeaderForCheck() throws UnsupportedEncodingException, JSONException {
        String curTime = System.currentTimeMillis() / 1000L + "";
        JSONObject param = new JSONObject();
        param.put("get_image", true);
        String params = param.toString();
        String paramBase64 = new String(Base64.encodeBase64(params.getBytes("UTF-8")));

        String checkSum = DigestUtils.md5Hex(CHECK_API_KEY + curTime + paramBase64);
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        header.put("X-Param", paramBase64);
        header.put("X-CurTime", curTime);
        header.put("X-CheckSum", checkSum);
        header.put("X-Appid", CHECK_APPID);
        return header;
    }

    /**
     * 获取base编码后的图片
     * @return
     */
    public String getImageWithBaseFormat(MultipartFile multipartFile) throws Exception{
        File frontFile = getFile(multipartFile);
        byte[] frontImageByteArray = FileUtils.toByteArray(frontFile);
        String frontImageBase = new String(Base64.encodeBase64(frontImageByteArray), "UTF-8");
        return frontImageBase;
    }
    /**
     * 人脸对比
     * @param memberPhotoFile  用户照片
     * @return
     */
    public ObjResp checkImage(MultipartFile memberPhotoFile,AuthIdentity authIdentity) throws Exception{


        URL url = new URL(authIdentity.getId_front_img());
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(3 * 1000);
        InputStream inputStream = conn.getInputStream();
        byte[] frontImageByteArray = FileUtils.inputStream2ByteArray(inputStream);
        String iDcardImageBase = new String(Base64.encodeBase64(frontImageByteArray), "UTF-8");

        File memberPhoto = getFile(memberPhotoFile);
        byte[] memberPhotoImageByteArray = FileUtils.toByteArray(memberPhoto);
        String memberPhotoImageBase = new String(Base64.encodeBase64(memberPhotoImageByteArray), "UTF-8");

//        String result = doPostForAuth(CHECK_WEBWFV_URL, buildHttpHeaderForCheck(), "first_image="
//                + URLEncoder.encode(iDcardImageBase, "UTF-8")
//                + "&" + "second_image=" + URLEncoder.encode(memberPhotoImageBase, "UTF-8"));
//        JSONObject jsonResult = JSONObject.parseObject(result);
//        if (jsonResult == null) {
//            return new ObjResp(ErrorInfoEnum.PORTRAIT_CONTRAST_ERROR.getErrorCode()
//                    , ErrorInfoEnum.PORTRAIT_CONTRAST_ERROR.getErrorENMsg(), null);
//        }
//        if (!"success".equals(jsonResult.getString("desc"))) {
//            return new ObjResp(ErrorInfoEnum.PORTRAIT_CONTRAST_ERROR.getErrorCode()
//                    , ErrorInfoEnum.PORTRAIT_CONTRAST_ERROR.getErrorENMsg(), null);
//        }
        //信息入库
        Map<String, Object>[] fileMaps = FaceIdUploader.multipartFiles2Map(Base64Util.base64ToMultipart("data:image/jpg;base64," + memberPhotoImageBase));
        ocrPool.execute(new FaceIdUploader(fileMaps));
        authIdentity.setId_handheld_img(OssConstants.ALIYUN_HOST+OssConstants.ALIYUN_UEDITOR_DIR + fileMaps[0].get("randomName"));
        authIdentity.setId_status(1);
        authIdentity.setAuditor("OCR_人脸比对");
        authIdentity.setReject_reason("");
        memberService.updateAuthIdentity(authIdentity);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,null);
    }

    public void addAuthInfo(JSONObject scanJsonResult, int memberId
            , AuthIdentityVo authIdentityVo, Map<String, String> baseMap) {
        AuthIdentity authIdentity = new AuthIdentity();
        authIdentity.setId(memberId);
        authIdentity.setId_number(scanJsonResult.getString("id_number"));
        //截取姓 和 名
        String name = scanJsonResult.getString("name");
        authIdentity.setFamily_name(name.substring(0, 1));
        authIdentity.setGiven_name(name.substring(1));
        if ("男".equals(scanJsonResult.getString("sex"))) {
            authIdentity.setSex("M");
        } else {
            authIdentity.setSex("F");
        }
        //身份证正面赋值
        authIdentity.setNationality("中国Chinese");
        authIdentity.setBirthday(scanJsonResult.getString("birthday"));
        authIdentity.setId_type("IDCARD");
        authIdentity.setProvince(scanJsonResult.getString("address"));
        authIdentity.setNation(scanJsonResult.getString("people"));
        //身份证背面赋值
        authIdentity.setCity("暂无");
        authIdentity.setId_begin_date("暂无");
        authIdentity.setId_end_date("暂无");
        //上传图片
        String idcardFrontside = "data:image/jpg;base64," + baseMap.get("front");
        String idcardBackside = "data:image/jpg;base64," + baseMap.get("back");
        MultipartFile mult1 = Base64Util.base64ToMultipart(idcardFrontside);
        MultipartFile mult2 = Base64Util.base64ToMultipart(idcardBackside);
        Map<String, Object>[] fileMaps = FaceIdUploader.multipartFiles2Map(mult1, mult2);
        ocrPool.execute(new FaceIdUploader(fileMaps));
        authIdentity.setId_handheld_img("/");
        authIdentity.setId_front_img(OssConstants.ALIYUN_HOST + OssConstants.ALIYUN_UEDITOR_DIR + fileMaps[0].get("randomName"));
        authIdentity.setId_back_img(OssConstants.ALIYUN_HOST + OssConstants.ALIYUN_UEDITOR_DIR + fileMaps[1].get("randomName"));
        authIdentity.setId_status(1); authIdentity.setId_status(3);
        authIdentity.setReject_reason("未进行人像比对");
        authIdentity.setLast_submit_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        memberService.addAuthIdentity(authIdentity);
        BeanUtils.copyProperties(authIdentity,authIdentityVo);
        authIdentityVo.setName(authIdentity.getFamily_name() + authIdentity.getGiven_name());
    }


    /**
     * 发送post请求,认证专用
     *
     * @param url
     * @param header
     * @param body
     * @return
     */
    public static String doPostForAuth(String url, Map<String, String> header, String body) {
        String result = "";
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            // 设置 url
            URL realUrl = new URL(url);
            URLConnection connection = realUrl.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) connection;
            // 设置 header
            for (String key : header.keySet()) {
                httpURLConnection.setRequestProperty(key, header.get(key));
            }
            // 设置请求 body
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            out = new PrintWriter(httpURLConnection.getOutputStream());
            // 保存body
            out.print(body);
            // 发送body
            out.flush();
            if (HttpURLConnection.HTTP_OK != httpURLConnection.getResponseCode()) {
                System.out.println("Http 请求失败，状态码：" + httpURLConnection.getResponseCode());
                return null;
            }

            // 获取响应body
            in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            return null;
        }
        return result;
    }
}
