/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/17 All Rights Reserved.
 */
package com.pmzhongguo.ex.transfer.interceptor;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.pmzhongguo.ex.business.entity.RespObj;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.transfer.controller.*;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.ex.transfer.service.ThirdPartyService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/17 14:23
 * @description：划转 api 拦截器, api权限和ip的校验
 * @version: $
 */
@Component
public class TransferApiInterceptor extends HandlerInterceptorAdapter
{

    protected Logger logger = LoggerFactory.getLogger(TransferApiInterceptor.class);
    @Autowired
    private ThirdPartyService thirdPartyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        if (!(handler instanceof HandlerMethod))
        {
            // 跳过404
            // response.sendError(HttpServletResponse.SC_NOT_FOUND, "the requested resource is not available.");
            return true;
        }
        // 判断 IP白名单
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Object bean = handlerMethod.getBean();
        if (bean instanceof GSTTTransferController)
        {
            return volidateProject(request, response, GSTTTransferController.c_name);
        }
        if (bean instanceof JSCTransferController)
        {
            return volidateProject(request, response, JSCTransferController.c_name);
        }
        if (bean instanceof LastWinnerTransferController)
        {
            return volidateProject(request, response, LastWinnerTransferController.c_name);
        }
        if (bean instanceof GmcTransferController)
        {
            return volidateProject(request, response, GmcTransferController.c_name);
        }
        if (bean instanceof JEFFTransferController) {
            return volidateProject(request, response, JEFFTransferController.c_name);
        }
        if (bean instanceof YTCTTransferController) {
            return volidateProject(request, response, YTCTTransferController.c_name);
        }
        return true;
    }

    /**
     * 验证第三方信息
     *
     * @param request
     * @param response
     * @param c_name
     * @return
     */
    public boolean volidateProject(HttpServletRequest request, HttpServletResponse response, String c_name)
    {
        Map<String, String> param = getParameters(request);
        RespObj respObj = thirdPartyService.findThirdPartyInfo(c_name);
        if (respObj.getState() != 1)
        {
            logger.warn("<=================== 划转项目方信息获取失败【/" + c_name + "/】校验失败 ， 请求参数= " + JSON.toJSONString(param));
            SendMsgUtil.sendJsonMessage(response, respObj);
            return false;
        }
        ThirdPartyInfo thirdPartyInfo = (ThirdPartyInfo) respObj.getData();
//        logger.warn("全局变量参数：[{}]",new Gson().toJson(thirdPartyInfo));
        ThirdPartyService.thirdPartyInfoMap.put(thirdPartyInfo.getC_name(), thirdPartyInfo);
        String whiteIP = thirdPartyInfo.getWhiteIp();
        if (StringUtils.isEmpty(whiteIP)) {
            logger.warn("<=================== ip白名单未添加，项目方：【/" + c_name + "/】 ， 请求参数= " + JSON.toJSONString(param));
            return false;
        }
        List<String> whiteIpList = Arrays.asList(whiteIP.split(","));
        // 获取请求IP
        String ip = HelpUtils.getIpAddr(request);
        if (StringUtils.isNotEmpty(whiteIP) && !whiteIpList.contains(ip))
        {
            logger.warn("<=================== 划转项目方请求失败【/" + c_name + "/】请检查白名单: 白名单: " + whiteIP + ", 请求IP：" + ip);
            SendMsgUtil.sendJsonMessage(response, new RespObj(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_IP.getErrorCNMsg(), null));
            return false;
        }
        return true;
    }

    /**
     * 取所有请求参数
     *
     * @param request
     * @return
     */
    protected Map<String, String> getParameters(HttpServletRequest request)
    {
        Map<String, String> params = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        String parameterName;
        while (parameterNames.hasMoreElements())
        {
            parameterName = parameterNames.nextElement();
            params.put(parameterName, request.getParameter(parameterName));
        }
        return params;
    }
}
