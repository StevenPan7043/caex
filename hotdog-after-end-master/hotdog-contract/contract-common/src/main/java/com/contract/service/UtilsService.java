package com.contract.service;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import com.contract.common.mail.SmsSendPool;
import com.contract.common.sms.SmsSend;
import com.contract.enums.LingKaiSmsEnums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.contract.common.FunctionUtils;
import com.contract.common.RpxUtils;
import com.contract.common.mail.SendMail;
import com.contract.common.number.RandNumUtils;
import com.contract.dao.MailLogsMapper;
import com.contract.dto.CustomerDto;
import com.contract.entity.MailLogs;
import com.contract.enums.RandNumType;
import com.contract.exception.ThrowJsonException;
import com.contract.service.redis.RedisUtilsService;

@Service
public class UtilsService {

    @Autowired
    private MailLogsMapper mailLogsMapper;
    @Autowired
    private RedisUtilsService redisUtilsService;

    /**
     * 发送手机验证码
     *
     * @param email
     * @return
     */
    public RestResponse sendPhonecode(String phone, String imgcode) {
        String valid_email = RpxUtils.valid_phone(phone);
        if (!StringUtils.isEmpty(valid_email)) {
            return GetRest.getFail(valid_email);
        }
        if (StringUtils.isEmpty(imgcode)) {
            return GetRest.getFail("请填写图片验证码");
        }
        String code = redisUtilsService.getKey("validcode_" + phone);
        if (StringUtils.isEmpty(code)) {
            return GetRest.getFail("图片验证码已过期");
        }
        if (!code.toLowerCase().equals(imgcode.toLowerCase())) {
            return GetRest.getFail("图片验证码错误");
        }
        String validcode = RandNumUtils.get(RandNumType.NUMBER, 6);
        String send_email = "sendphone_" + phone;
        //一分钟操作一次
        boolean lockflag_send = redisUtilsService.setIncrSecond(send_email, 60);
        if (!lockflag_send) {
            return GetRest.getFail("请勿快速提交，1分钟后再试");
        }
        RestResponse result = SendMail.sendPhoneValid(phone, validcode);
        if (result.isStatus()) {
            MailLogs logs = new MailLogs();
            logs.setEmail(phone);
            logs.setParams(validcode);
            logs.setStatus(0);
            logs.setType(1);
            mailLogsMapper.insertSelective(logs);
        }
        return GetRest.getSuccess("发送成功");
    }

    /**
     * 验证邮箱验证码
     *
     * @param phone
     * @param validcode
     * @return
     */
    public RestResponse validPhone(String phone, String validcode) {
        if (StringUtils.isBlank(phone)) {
            return GetRest.getFail("未找到验证手机号");
        }
        if (StringUtils.isBlank(validcode)) {
            return GetRest.getFail("请填写验证码");
        }
        MailLogs logs = new MailLogs();
        logs.setEmail(phone);
        logs.setType(1);
        logs = mailLogsMapper.getMailOne(logs);
        if (logs == null || !validcode.equals(logs.getParams())) {
            return GetRest.getFail("验证码错误");
        }
        long endtime = logs.getCreatetime().getTime() + 10 * 3600;
        if (FunctionUtils.isEquals(1, logs.getStatus())) {
            return GetRest.getFail("验证码已使用");
        }
        if (FunctionUtils.isEquals(2, logs.getStatus())) {
            return GetRest.getFail("验证码已过期");
        }
        if (FunctionUtils.isEquals(0, logs.getStatus())) {
            if (endtime < logs.getCreatetime().getTime()) {
                logs.setStatus(2);
                mailLogsMapper.updateByPrimaryKeySelective(logs);
                return GetRest.getFail("验证码已过期");
            } else {
                logs.setStatus(1);
                mailLogsMapper.updateByPrimaryKeySelective(logs);
            }
        }
        return GetRest.getSuccess("验证通过");
    }


    /**
     * 根据token获取用户数据
     *
     * @param token
     * @return
     */
    public CustomerDto getCusByToken(String token) {
        if (StringUtils.isEmpty(token)) {
            throw new ThrowJsonException("1001");
        }
        String dto = redisUtilsService.getKey(token);
        if (StringUtils.isEmpty(dto)) {
            throw new ThrowJsonException("1001");
        }
        CustomerDto customerDto = JSONObject.parseObject(dto, CustomerDto.class);
        if (customerDto == null) {
            throw new ThrowJsonException("1001");
        }
        return customerDto;
    }

    /**
     * 发送手机验证码
     *
     * @param phone or eamil
     * @return
     */
    public RestResponse sendPhonecode(String phone) {
        String valid_email = RpxUtils.valid_phone(phone);
        if (!StringUtils.isEmpty(valid_email)) {
            return GetRest.getFail(valid_email);
        }
        String validcode = RandNumUtils.get(RandNumType.NUMBER, 6);
        String send_email = "sendphone_" + phone;
        //一分钟操作一次
        boolean lockflag_send = redisUtilsService.setIncrSecond(send_email, 60);
        if (!lockflag_send) {
            return GetRest.getFail("请勿快速提交，1分钟后再试");
        }
        String format = String.format(LingKaiSmsEnums.LK_SMS_CODE.getCode(), validcode);
        SmsSendPool.getInstance().send(new SmsSend(phone, LingKaiSmsEnums.LK_SMS_CODE.getType(), format));
        MailLogs logs = new MailLogs();
        logs.setEmail(phone);
        logs.setParams(validcode);
        logs.setStatus(0);
        logs.setType(1);
        mailLogsMapper.insertSelective(logs);
        return GetRest.getSuccess("发送成功");
    }
}
