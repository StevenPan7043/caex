package com.pmzhongguo.ex.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.pmzhongguo.ex.business.entity.AuthIdentity;
import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.business.service.manager.AuthManager;
import com.pmzhongguo.ex.business.vo.AuthIdentityVo;
import com.pmzhongguo.ex.core.config.OcrConstant;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.pmzhongguo.ex.core.config.OcrConstant.*;

/**
 *
 * 人脸比对 WebAPI 接口调用示例 接口文档（必看）：https://doc.xfyun.cn/rest_api/%E4%BA%BA%E8%84%B8%E6%AF%94%E5%AF%B9.html
 *
 * (Very Important)创建完webapi应用添加合成服务之后一定要设置ip白名单，找到控制台--我的应用--设置ip白名单，
 * 如何设置参考：http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=41891
 * 错误码链接：https://www.xfyun.cn/document/error-code (code返回错误码时必看)
 *
 * @description: ocr实名认证
 * @date: 2019-12-29 13:53
 * @author: 十一
 */
@Api(value = "新-OCR用户实名认证接口", description = "新-OCR用户实名认证接口", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/auth")
public class OcrController {


    @Resource
    MemberService memberService;

    @Autowired
    private AuthManager authManager;

    @Autowired
    public DaoUtil daoUtil;


    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    /**
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
            return new ObjResp(ErrorInfoEnum.LANG_NO_LOGIN.getErrorCode(), ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        //校验上传的图片大小
        if (!FileUtils.checkFileSize(iDcardFront.getSize(), OCR_IMAGE_MAX_SIZE_LIMIT, CHECK_IMAGE_SIZE_UNIT)) {
            return new ObjResp(ErrorInfoEnum.OCR_UPLOAD_FRONT_IMAGE_EXCEED_SIZE.getErrorCode()
                    , ErrorInfoEnum.OCR_UPLOAD_FRONT_IMAGE_EXCEED_SIZE.getErrorENMsg(), null);
        }
        if (!FileUtils.checkFileSize(iDcardBack.getSize(), OcrConstant.OCR_IMAGE_MAX_SIZE_LIMIT, OcrConstant.CHECK_IMAGE_SIZE_UNIT)) {
            return new ObjResp(ErrorInfoEnum.OCR_UPLOAD_BACK_IMAGE_EXCEED_SIZE.getErrorCode()
                    , ErrorInfoEnum.OCR_UPLOAD_BACK_IMAGE_EXCEED_SIZE.getErrorENMsg(), null);
        }
        //校验是否已经实名
        AuthIdentity oIdAuthIdentity = memberService.getAuthIdentityById(member.getId());
        if (oIdAuthIdentity != null) {
            ObjResp objResp = authManager.auth(oIdAuthIdentity,member, HelpUtils.getIpAddr(request));
            if (!objResp.getState().equals(ObjResp.SUCCESS)) {
                return objResp;
            }
        }
        //请求限制
        Object countO = JedisUtil.getInstance().get(member.getId() + AuthUtils.OCR_MEMBER_REQUEST_COUNT_KEY, true);
        if (countO != null && Integer.parseInt(countO + "") >= Integer.parseInt(AuthPropertiesUtil.getPropValByKey("OCR_MEMBER_REQUEST_COUNT"))) {
            return new ObjResp(ErrorInfoEnum.OCR_REQUEST_TO_OFTEN.getErrorCode(), ErrorInfoEnum.OCR_REQUEST_TO_OFTEN.getErrorENMsg(), null);
        }

        AuthIdentityVo authIdentityVo = new AuthIdentityVo();
        try {

            // 1. 扫描身份证前照，获取身份证号码和图片
            ObjResp objResp = authManager.scanImageWithFront(iDcardFront, member.getId());
            if (!objResp.getState().equals(ObjResp.SUCCESS)) {
                return objResp;
            }
            JSONObject scanJsonResult = (JSONObject) objResp.getData();
            String iDcardFrontBase = authManager.getImageWithBaseFormat(iDcardFront);
            String iDcardBackBase = authManager.getImageWithBaseFormat(iDcardBack);
            Map<String, String> baseMap = new HashMap<>();
            baseMap.put("front",iDcardFrontBase);
            baseMap.put("back",iDcardBackBase);
            // 2. 数据库操作
            authManager.addAuthInfo(scanJsonResult,member.getId(),authIdentityVo,baseMap);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("upload OCR or aliOSS error,memberId:{}", member.getId(), e);
        } finally {
            JedisUtil.getInstance().incr(member.getId() + AuthUtils.OCR_MEMBER_REQUEST_COUNT_KEY);
            JedisUtil.getInstance().expire(member.getId() + AuthUtils.OCR_MEMBER_REQUEST_COUNT_KEY, Integer.parseInt(AuthPropertiesUtil.getPropValByKey("OCR_MEMBER_REQUEST_TIME")));
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, authIdentityVo);
    }

    @ApiOperation(value = "获取OCR识别结果", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getOcrResult", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getOcrResult(HttpServletRequest request) {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(ErrorInfoEnum.LANG_NO_LOGIN.getErrorCode(), ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        AuthIdentityVo authIdentityVo = new AuthIdentityVo();
        try {
            AuthIdentity authIdentity = memberService.getAuthIdentityById(member.getId());
            if (authIdentity != null) {
                BeanUtils.copyProperties(authIdentity,authIdentityVo);
                authIdentityVo.setName(authIdentity.getFamily_name() + authIdentity.getGiven_name());
            } else {
                return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
            }
        } catch (Exception e) {
            logger.error("authIdentityDo to authIdentityVo error,memberId:{}", member.getId(), e);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, authIdentityVo);
    }

    @ApiOperation(value = "人像比对,一张身份证，一张普通头像", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "portraitContrast", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp portraitContrast(MultipartFile mugShot, HttpServletRequest request) {
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(ErrorInfoEnum.LANG_NO_LOGIN.getErrorCode(), ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        //校验上传的图片大小

        if (!FileUtils.checkFileSize(mugShot.getSize(), OcrConstant.CHECK_IMAGE_MAX_SIZE_LIMIT, OcrConstant.CHECK_IMAGE_SIZE_UNIT)) {
            return new ObjResp(ErrorInfoEnum.OCR_UPLOAD_BACK_IMAGE_EXCEED_SIZE.getErrorCode()
                    , ErrorInfoEnum.PORTRAIT_CONTRAST_UPLOAD_IMAGE_EXCEED_SIZE.getErrorENMsg(), null);
        }
        //请求限制
        Object countO = JedisUtil.getInstance().get(member.getId() + AuthUtils.PORTRAIT_CONTRAST_MEMBER_REQUEST_COUNT_KEY, true);
        if (countO != null && Integer.parseInt(countO + "") >= Integer.parseInt(AuthPropertiesUtil.getPropValByKey("PORTRAIT_CONTRAST_MEMBER_REQUEST_COUNT"))) {
            return new ObjResp(ErrorInfoEnum.OCR_REQUEST_TO_OFTEN.getErrorCode(), ErrorInfoEnum.OCR_REQUEST_TO_OFTEN.getErrorENMsg(), null);
        }
        AuthIdentity authIdentity = memberService.getAuthIdentityById(member.getId());
        if (authIdentity == null) {
            return new ObjResp(ErrorInfoEnum.PORTRAIT_CONTRAST_UPLOAD_ID_CARD.getErrorCode(), ErrorInfoEnum.PORTRAIT_CONTRAST_UPLOAD_ID_CARD.getErrorENMsg(), null);
        } else {
            if (authIdentity.getId_status() == 0) {
                return new ObjResp(ErrorInfoEnum.OCR_OCR_ID_STATUS_UNDER_REVIEW.getErrorCode(), ErrorInfoEnum.OCR_OCR_ID_STATUS_UNDER_REVIEW.getErrorENMsg(), null);
            } else if (authIdentity.getId_status() == 1) {
                return new ObjResp(ErrorInfoEnum.OCR_ID_STATUS_AUDIT_SUCCEEDED.getErrorCode(), ErrorInfoEnum.OCR_ID_STATUS_AUDIT_SUCCEEDED.getErrorENMsg(), null);
            }
        }

        try {

            //人脸比对
            ObjResp objResp = authManager.checkImage(mugShot,authIdentity);
            return objResp;

        } catch (Exception e) {
            logger.error("portraitContrast error,memberId:{}", member.getId(), e);
        } finally {
            JedisUtil.getInstance().incr(member.getId() + AuthUtils.PORTRAIT_CONTRAST_MEMBER_REQUEST_COUNT_KEY);
            JedisUtil.getInstance().expire(member.getId() + AuthUtils.PORTRAIT_CONTRAST_MEMBER_REQUEST_COUNT_KEY, Integer.parseInt(AuthPropertiesUtil.getPropValByKey("PORTRAIT_CONTRAST_MEMBER_REQUEST_TIME")));
        }
        return new ObjResp(ErrorInfoEnum.PORTRAIT_CONTRAST_ERROR.getErrorCode()
                , ErrorInfoEnum.PORTRAIT_CONTRAST_ERROR.getErrorENMsg(), null);
    }

    @ApiOperation(value = "重新加载认证相关配置", hidden = true)
    @RequestMapping(value = "reloadConfig")
    @ResponseBody
    public String reloadConfig(String password) {
        if (password.equals(AuthPropertiesUtil.getPropValByKey("reload_password"))) {
            AuthPropertiesUtil.reloadProp();
            return "success";
        } else {
            return "error";
        }
    }
}
