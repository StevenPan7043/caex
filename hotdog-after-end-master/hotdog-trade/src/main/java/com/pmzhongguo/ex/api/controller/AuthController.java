package com.pmzhongguo.ex.api.controller;

import com.alibaba.fastjson.JSON;
import com.pmzhongguo.ex.business.entity.AuthIdentity;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.model.OssConstants;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.business.vo.AuthIdentityVo;
import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.faceid.Base64Util;
import com.pmzhongguo.ex.core.utils.AuthUtils;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Created by 张众
 * @Date 2019/11/27 16:25
 * @Description
 */
@Api(value = "用户实名认证接口", description = "用户实名认证接口", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("auth/ocr")
public class AuthController extends TopController {

    @Resource
    MemberService memberService;

    @Autowired
    public DaoUtil daoUtil;

    ExecutorService ocrPool = Executors.newFixedThreadPool(20, new ZzexThreadFactory("AuthController_uploader"));

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
     *
     * @param iDcardFront
     * @param iDcardBack
     * @return
     */
    @ApiOperation(value = "上传身份证正反面并进行OCR识别入库", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "uploadIDcard", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp uploadIDcard(MultipartFile iDcardFront, MultipartFile iDcardBack, HttpServletRequest request) {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(ErrorInfoEnum.LANG_NO_LOGIN.getErrorCode(), ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(),null);
        }
        //校验上传的图片大小
        if (!FileUtils.checkFileSize(iDcardFront.getSize(), Integer.parseInt(AuthPropertiesUtil.getPropValByKey("OCR_UPLOAD_SIZE")),AuthPropertiesUtil.getPropValByKey("OCR_UPLOAD_UNIT"))){
            return new ObjResp(ErrorInfoEnum.OCR_UPLOAD_FRONT_IMAGE_EXCEED_SIZE.getErrorCode(), ErrorInfoEnum.OCR_UPLOAD_FRONT_IMAGE_EXCEED_SIZE.getErrorENMsg(),null);
        }
        if (!FileUtils.checkFileSize(iDcardBack.getSize(), Integer.parseInt(AuthPropertiesUtil.getPropValByKey("OCR_UPLOAD_SIZE")),AuthPropertiesUtil.getPropValByKey("OCR_UPLOAD_UNIT"))){
            return new ObjResp(ErrorInfoEnum.OCR_UPLOAD_BACK_IMAGE_EXCEED_SIZE.getErrorCode(), ErrorInfoEnum.OCR_UPLOAD_BACK_IMAGE_EXCEED_SIZE.getErrorENMsg(),null);
        }
        //校验是否已经实名
        AuthIdentity oIdAuthIdentity = memberService.getAuthIdentityById(member.getId());
        if (oIdAuthIdentity!=null) {
            if (oIdAuthIdentity.getId_status() == 0) {
                return new ObjResp(ErrorInfoEnum.OCR_OCR_ID_STATUS_UNDER_REVIEW.getErrorCode(), ErrorInfoEnum.OCR_OCR_ID_STATUS_UNDER_REVIEW.getErrorENMsg(), null);
            } else if (oIdAuthIdentity.getId_status() == 1) {
                return new ObjResp(ErrorInfoEnum.OCR_ID_STATUS_AUDIT_SUCCEEDED.getErrorCode(), ErrorInfoEnum.OCR_ID_STATUS_AUDIT_SUCCEEDED.getErrorENMsg(), null);
            }
            //可能会出现用户上传信息已经入库，但是又因身份证号等信息对不上重新上传，所以这里要做删除操作
            //记录操作
            daoUtil.update("insert into m_member_oper_log (member_id,m_name,oper_type,oper_time,oper_memo,oper_ip)values(?,?,?,?,?,?)",
                    member.getId(),member.getM_name(),2,HelpUtils.formatDate6(new Date()),
                    "OCR re upload Old AuthIdentity: id_number:"+oIdAuthIdentity.getId_number()+
                            " ,m_name:"+oIdAuthIdentity.getM_name()+
                            " ,name:"+oIdAuthIdentity.getFamily_name()+oIdAuthIdentity.getFamily_name()+
                            " ,last_submit_time:"+oIdAuthIdentity.getLast_submit_time()+
                            " ,audit_time:"+oIdAuthIdentity.getAuditor()
                    ,
                    HelpUtils.getIpAddr(request));
            //删除
            daoUtil.update("delete from m_auth_identity where id = ?",member.getId());
        }
        //请求限制
        Object countO = JedisUtil.getInstance().get(member.getId() + AuthUtils.OCR_MEMBER_REQUEST_COUNT_KEY, true);
        if (countO!=null&&Integer.parseInt(countO+"")>=Integer.parseInt(AuthPropertiesUtil.getPropValByKey("OCR_MEMBER_REQUEST_COUNT"))){
            return new ObjResp(ErrorInfoEnum.OCR_REQUEST_TO_OFTEN.getErrorCode(), ErrorInfoEnum.OCR_REQUEST_TO_OFTEN.getErrorENMsg(),null);
        }

        AuthIdentityVo authIdentityVo = new AuthIdentityVo();
        try {
            //压缩图片并将尺寸固定在1080*x(自动扩展),服务商接收的图片尺寸 最短边>15px， 图片最长边<4096px
            File file = null;
            //因APP端传来的图片名为 iDcardFront 而非文件名 这里为兼容APP端做校验
            if (iDcardFront.getOriginalFilename().equals("iDcardFront")){
                file = new File(iDcardFront.getOriginalFilename()+".jpg");
            }else{
                file = new File(iDcardFront.getOriginalFilename());
            }
            Thumbnails.of(iDcardFront.getInputStream()).outputFormat("jpg").size(1080, 1440).toFile(file);
            //ocr扫描身份证证正面
            String frontBase64 = Base64Utils.encodeToString(FileUtils.toByteArray(file));
            JSONObject frontMSGResult = JSONObject.fromObject(AuthUtils.ocr(AuthUtils.OCR_IMAGE_FRONT, frontBase64));
            JSONObject frontResult = null;
            if (frontMSGResult.get("success").equals(true)){
                frontResult = JSONObject.fromObject(frontMSGResult.get("result"));
                if (frontResult.get("name").equals("")||
                        frontResult.get("address").equals("")||
                        frontResult.get("code").equals("")||
                        frontResult.get("nation").equals("")||
                        frontResult.get("sex").equals("")||
                        frontResult.get("birthday").equals("")){
                    return new ObjResp(ErrorInfoEnum.OCR_FRONT_ERROR.getErrorCode(), ErrorInfoEnum.OCR_FRONT_ERROR.getErrorENMsg(),null);
                }
            }else{
                //余额不足，日志报警
                if (frontMSGResult.get("code").equals(19001)){
                    logger.error("OCR Insufficient remaining quantity!!!");
                }
                return new ObjResp(ErrorInfoEnum.OCR_FRONT_ERROR.getErrorCode(), ErrorInfoEnum.OCR_FRONT_ERROR.getErrorENMsg(),null);
            }
            //校验身份证号是否存在
            Integer alreadyIdNumCount = daoUtil.queryForInt("SELECT COUNT(1) FROM m_auth_identity WHERE id <> ? AND id_number = ?", member.getId(), frontResult.get("code"));
            if (alreadyIdNumCount > 0) {
                return new ObjResp(ErrorInfoEnum.LANG_ID_NUM_ALREADY_EXIST.getErrorCode(), ErrorInfoEnum.LANG_ID_NUM_ALREADY_EXIST.getErrorENMsg(), null);
            }
            //压缩图片并将尺寸固定在1080*x(自动扩展),服务商接收的图片尺寸 最短边>15px， 图片最长边<4096px
            File file2 = null;
            //因APP端传来的图片名为 iDcardBack 而非文件名 这里为兼容APP端做校验
            if (iDcardBack.getOriginalFilename().equals("iDcardFront")){
                file2 = new File(iDcardBack.getOriginalFilename()+".jpg");
            }else{
                file2 = new File(iDcardBack.getOriginalFilename());
            }
            Thumbnails.of(iDcardBack.getInputStream()).outputFormat("jpg").size(1080, 1440).toFile(file2);
            //ocr扫描身份证证背面
            String backBase64 = Base64Utils.encodeToString(FileUtils.toByteArray(file2));
            JSONObject backMSGResult = JSONObject.fromObject(AuthUtils.ocr(AuthUtils.OCR_IMAGE_BACK, backBase64));
            JSONObject backResult = null;
            if (backMSGResult.get("success").equals(true)){
                backResult = JSONObject.fromObject(backMSGResult.get("result"));
                if (backResult.get("issuer").equals("")||backResult.get("endDate").equals("")||backResult.get("beginDate").equals("")){
                    return new ObjResp(ErrorInfoEnum.OCR_BACK_ERROR.getErrorCode(), ErrorInfoEnum.OCR_BACK_ERROR.getErrorENMsg(),null);
                }
            }else{
                //余额不足，日志报警
                if (backMSGResult.get("code").equals(19001)){
                    logger.error("OCR Insufficient remaining quantity!!!");
                }
                return new ObjResp(ErrorInfoEnum.OCR_BACK_ERROR.getErrorCode(), ErrorInfoEnum.OCR_BACK_ERROR.getErrorENMsg(),null);
            }
            //信息入库
            AuthIdentity authIdentity = new AuthIdentity();
            authIdentity.setId(member.getId());
            authIdentity.setId_number(frontResult.get("code").toString());
            //截取姓 和 名
            authIdentity.setFamily_name(frontResult.get("name").toString().substring(0,1));
            authIdentity.setGiven_name(frontResult.get("name").toString().substring(1));
            if (frontResult.get("sex").equals("男")){
                authIdentity.setSex("M");
            }else{
                authIdentity.setSex("F");
            }
            //身份证正面赋值
            authIdentity.setNationality("中国Chinese");
            authIdentity.setBirthday(frontResult.get("birthday").toString());
            authIdentity.setId_type("IDCARD");
            authIdentity.setProvince(frontResult.get("address").toString());
            authIdentity.setNation(frontResult.get("nation").toString());
            //身份证背面赋值
            authIdentity.setCity(backResult.get("issuer").toString());
            authIdentity.setId_begin_date(backResult.get("beginDate").toString());
            authIdentity.setId_end_date(backResult.get("endDate").toString());
            //上传图片
            String idcardFrontside = "data:image/jpg;base64," + frontBase64;
            String idcardBackside = "data:image/jpg;base64," + backBase64;
            MultipartFile mult1 = Base64Util.base64ToMultipart(idcardFrontside);
            MultipartFile mult2 = Base64Util.base64ToMultipart(idcardBackside);
            Map<String, Object>[] fileMaps = FaceIdUploader.multipartFiles2Map(mult1, mult2);
            ocrPool.execute(new FaceIdUploader(fileMaps));
            authIdentity.setId_handheld_img("/");
            authIdentity.setId_front_img(OssConstants.ALIYUN_HOST+OssConstants.ALIYUN_UEDITOR_DIR + fileMaps[0].get("randomName"));
            authIdentity.setId_back_img(OssConstants.ALIYUN_HOST+OssConstants.ALIYUN_UEDITOR_DIR + fileMaps[1].get("randomName"));
            authIdentity.setId_status(3);
            authIdentity.setReject_reason("未进行人像比对");
            authIdentity.setLast_submit_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
            memberService.addAuthIdentity(authIdentity);
            BeanUtils.copyProperties(authIdentityVo,authIdentity);
            authIdentityVo.setName(authIdentity.getFamily_name()+authIdentity.getGiven_name());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("upload OCR or aliOSS error,memberId:{}",member.getId(),e);
        } finally {
            JedisUtil.getInstance().incr(member.getId() + AuthUtils.OCR_MEMBER_REQUEST_COUNT_KEY);
            JedisUtil.getInstance().expire(member.getId() + AuthUtils.OCR_MEMBER_REQUEST_COUNT_KEY, Integer.parseInt(AuthPropertiesUtil.getPropValByKey("OCR_MEMBER_REQUEST_TIME")));
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,authIdentityVo);
    }

    @ApiOperation(value = "获取OCR识别结果", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getOcrResult", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getOcrResult(HttpServletRequest request) {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(ErrorInfoEnum.LANG_NO_LOGIN.getErrorCode(), ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(),null);
        }
        AuthIdentityVo authIdentityVo = new AuthIdentityVo();
        try {
            AuthIdentity authIdentity = memberService.getAuthIdentityById(member.getId());
            if (authIdentity!=null){
                BeanUtils.copyProperties(authIdentityVo,authIdentity);
                authIdentityVo.setName(authIdentity.getFamily_name()+authIdentity.getGiven_name());
            }else{
                return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,null);
            }
        } catch (Exception e) {
            logger.error("authIdentityDo to authIdentityVo error,memberId:{}",member.getId(),e);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,authIdentityVo);
    }

    @ApiOperation(value = "人像比对", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "portraitContrast", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp portraitContrast(MultipartFile mugShot,HttpServletRequest request) {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(ErrorInfoEnum.LANG_NO_LOGIN.getErrorCode(), ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(),null);
        }
        //校验上传的图片大小
        if (!FileUtils.checkFileSize(mugShot.getSize(), Integer.parseInt(AuthPropertiesUtil.getPropValByKey("PORTRAIT_CONTRAST_UPLOAD_SIZE")),AuthPropertiesUtil.getPropValByKey("PORTRAIT_CONTRAST_UPLOAD_UNIT"))){
            return new ObjResp(ErrorInfoEnum.PORTRAIT_CONTRAST_UPLOAD_IMAGE_EXCEED_SIZE.getErrorCode(), ErrorInfoEnum.PORTRAIT_CONTRAST_UPLOAD_IMAGE_EXCEED_SIZE.getErrorENMsg(),null);
        }
        //请求限制
        Object countO = JedisUtil.getInstance().get(member.getId() + AuthUtils.PORTRAIT_CONTRAST_MEMBER_REQUEST_COUNT_KEY, true);
        if (countO!=null&&Integer.parseInt(countO+"")>=Integer.parseInt(AuthPropertiesUtil.getPropValByKey("PORTRAIT_CONTRAST_MEMBER_REQUEST_COUNT"))){
            return new ObjResp(ErrorInfoEnum.OCR_REQUEST_TO_OFTEN.getErrorCode(), ErrorInfoEnum.OCR_REQUEST_TO_OFTEN.getErrorENMsg(),null);
        }
        AuthIdentity authIdentity = memberService.getAuthIdentityById(member.getId());
        if (authIdentity==null){
            return new ObjResp(ErrorInfoEnum.PORTRAIT_CONTRAST_UPLOAD_ID_CARD.getErrorCode(), ErrorInfoEnum.PORTRAIT_CONTRAST_UPLOAD_ID_CARD.getErrorENMsg(),null);
        }else{
            if (authIdentity.getId_status() == 0) {
                return new ObjResp(ErrorInfoEnum.OCR_OCR_ID_STATUS_UNDER_REVIEW.getErrorCode(), ErrorInfoEnum.OCR_OCR_ID_STATUS_UNDER_REVIEW.getErrorENMsg(), null);
            } else if (authIdentity.getId_status() == 1) {
                return new ObjResp(ErrorInfoEnum.OCR_ID_STATUS_AUDIT_SUCCEEDED.getErrorCode(), ErrorInfoEnum.OCR_ID_STATUS_AUDIT_SUCCEEDED.getErrorENMsg(), null);
            }
        }

        try {
            //压缩图片并将尺寸固定在1080*x(自动扩展),服务商接收的图片尺寸 最短边>15px， 图片最长边<4096px
            File file = null;
            //因APP端传来的图片名为 iDcardBack 而非文件名 这里为兼容APP端做校验
            if (mugShot.getOriginalFilename().equals("iDcardFront")){
                file = new File(mugShot.getOriginalFilename()+".jpg");
            }else{
                file = new File(mugShot.getOriginalFilename());
            }
            Thumbnails.of(mugShot.getInputStream()).outputFormat("jpg").size(1080, 1440).toFile(file);
            String mugShotBase64Side = Base64Utils.encodeToString(FileUtils.toByteArray(file));
            //人脸比对
            JSONObject resultMsg = JSONObject.fromObject(AuthUtils.sendPortraitContrast(mugShotBase64Side, authIdentity.getFamily_name() + authIdentity.getGiven_name(), authIdentity.getId_number()));
            if (resultMsg.get("success").equals(true)){
                JSONObject result = JSONObject.fromObject(resultMsg.get("result"));
                if (!result.get("matchCode").equals("1")){
                    return new ObjResp(ErrorInfoEnum.PORTRAIT_CONTRAST_ERROR.getErrorCode(), ErrorInfoEnum.PORTRAIT_CONTRAST_ERROR.getErrorENMsg(), null);
                }
            }else{
                //余额不足，日志报警
                if (resultMsg.get("code").equals(19001)){
                    logger.error("OCR Insufficient remaining quantity!!!");
                }
                return new ObjResp(ErrorInfoEnum.PORTRAIT_CONTRAST_ERROR.getErrorCode(), ErrorInfoEnum.PORTRAIT_CONTRAST_ERROR.getErrorENMsg(), null);
            }
            //信息入库
            Map<String, Object>[] fileMaps = FaceIdUploader.multipartFiles2Map(Base64Util.base64ToMultipart("data:image/jpg;base64," + mugShotBase64Side));
            ocrPool.execute(new FaceIdUploader(fileMaps));
            authIdentity.setId_handheld_img(OssConstants.ALIYUN_HOST+OssConstants.ALIYUN_UEDITOR_DIR + fileMaps[0].get("randomName"));
            authIdentity.setId_status(1);
            authIdentity.setAuditor("OCR_人脸比对");
            authIdentity.setReject_reason("");
            memberService.updateAuthIdentity(authIdentity);
        } catch (Exception e) {
            logger.error("portraitContrast error,memberId:{}",member.getId(),e);
        } finally {
            JedisUtil.getInstance().incr(member.getId() + AuthUtils.PORTRAIT_CONTRAST_MEMBER_REQUEST_COUNT_KEY);
            JedisUtil.getInstance().expire(member.getId() + AuthUtils.PORTRAIT_CONTRAST_MEMBER_REQUEST_COUNT_KEY, Integer.parseInt(AuthPropertiesUtil.getPropValByKey("PORTRAIT_CONTRAST_MEMBER_REQUEST_TIME")));
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG,null);
    }

    @ApiOperation(value = "重新加载认证相关配置",hidden = true)
    @RequestMapping(value = "reloadConfig")
    @ResponseBody
    public String reloadConfig(String password){
        if (password.equals(AuthPropertiesUtil.getPropValByKey("reload_password"))) {
            AuthPropertiesUtil.reloadProp();
            return "success";
        } else {
            return "error";
        }
    }
}
