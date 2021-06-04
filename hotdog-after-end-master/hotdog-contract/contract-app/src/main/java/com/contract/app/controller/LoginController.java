package com.contract.app.controller;


import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.contract.app.common.MappingUtil;
import com.contract.common.RpxUtils;
import com.contract.entity.CCustomer;
import com.contract.enums.SysParamsEnums;
import com.contract.service.api.AppLoginService;
import com.contract.service.redis.RedisUtilsService;

@Controller
public class LoginController {

    @Autowired
    private AppLoginService appLoginService;
    @Autowired
    private RedisUtilsService redisUtilsService;

    /**
     * 注册
     *
     * @param customer
     * @param parentcode 推荐人账号
     * @param validcode  验证码
     * @return
     */
    @RequestMapping(value = MappingUtil.handleRegister)
    @ResponseBody
    public RestResponse handleRegister(CCustomer customer, String parentcode, String validcode) {
        if (StringUtils.isBlank(validcode)) {
            return GetRest.getFail("请填写验证码");
        }
        if (StringUtils.isEmpty(parentcode)) {
            return GetRest.getFail("请填写推荐码");
        }
        if (!StringUtils.isEmpty(customer.getPhone())) {
            customer.setPhone(customer.getPhone().trim());
        }
        String valid_phone = RpxUtils.valid_phone(customer.getPhone());
        if (!StringUtils.isBlank(valid_phone)) {
            return GetRest.getFail(valid_phone);
        }
        String valid_password = RpxUtils.valid_password(customer.getPassword());
        if (!StringUtils.isBlank(valid_password)) {
            return GetRest.getFail(valid_password);
        }
        String valid_payword = RpxUtils.valid_password(customer.getPayword());
        if (!StringUtils.isBlank(valid_payword)) {
            return GetRest.getFail(valid_payword);
        }
        RestResponse result = appLoginService.handleRegister(customer, parentcode, validcode);
        return result;
    }

    /**
     * 登陆
     *
     * @param login
     * @param password
     * @return
     */
    @RequestMapping(value = MappingUtil.login)
    @ResponseBody
    public RestResponse login(String login, String password) {
        if (StringUtils.isBlank(login)) {
            return GetRest.getFail("请输入帐号");
        }
        if (StringUtils.isBlank(password)) {
            return GetRest.getFail("请输入密码");
        }
        RestResponse result = appLoginService.login(login, password);
        return result;
    }

    /**
     * 退出
     *
     * @param token
     * @return
     */
    @RequestMapping(value = MappingUtil.exit)
    @ResponseBody
    public RestResponse exit(String token) {
        RestResponse result = appLoginService.exit(token);
        return result;
    }

    /**
     * 忘记密码
     *
     * @param email
     * @param password
     * @param validcode
     * @return
     */
    @RequestMapping(value = MappingUtil.handleForgetpwd)
    @ResponseBody
    public RestResponse handleForgetpwd(String phone, String password, String validcode) {
        RestResponse result = appLoginService.handleForgetpwd(phone, password, validcode);
        return result;
    }

    /**
     * 修改密码
     *
     * @param cid
     * @param password
     * @param validcode
     * @param type
     * @return
     */
    @RequestMapping(value = MappingUtil.editPass)
    @ResponseBody
    public RestResponse editPass(String token, String password, String validcode, Integer type) {
        RestResponse result = appLoginService.editPass(token, password, validcode, type);
        return result;
    }

    @RequestMapping(value = MappingUtil.share + "/{invitationcode}")
    public ModelAndView showShare(@PathVariable String invitationcode) {
        String android_url = redisUtilsService.getKey(SysParamsEnums.android_url.getKey());
        String ios_url = redisUtilsService.getKey(SysParamsEnums.ios_url.getKey());
        ModelAndView view = new ModelAndView(MappingUtil.share);
        view.addObject("android_url", android_url);
        view.addObject("ios_url", ios_url);
        view.addObject("invitationcode", invitationcode);
        return view;
    }

    @RequestMapping(value = MappingUtil.downLoad)
    public ModelAndView downLoad() {
        String android_url = redisUtilsService.getKey(SysParamsEnums.android_url.getKey());
        String ios_url = redisUtilsService.getKey(SysParamsEnums.ios_url.getKey());
        ModelAndView view = new ModelAndView(MappingUtil.downLoad);
        view.addObject("android_url", android_url);
        view.addObject("ios_url", ios_url);
        return view;
    }
}
