package com.contract.service.api;

import com.contract.common.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.contract.common.number.RandNumUtils;
import com.contract.common.pwd.PwdEncode;
import com.contract.dao.CCustomerMapper;
import com.contract.dao.CWalletMapper;
import com.contract.dto.CustomerDto;
import com.contract.entity.CCustomer;
import com.contract.entity.CCustomerExample;
import com.contract.entity.CWallet;
import com.contract.enums.CoinEnums;
import com.contract.enums.RandNumType;
import com.contract.enums.SysParamsEnums;
import com.contract.exception.ThrowJsonException;
import com.contract.service.UtilsService;
import com.contract.service.redis.RedisUtilsService;
import com.image.common.RestUploadFileInfo;

@Service
public class AppLoginService {

    @Autowired
    private RedisUtilsService redisUtilsService;
    @Autowired
    private CCustomerMapper cCustomerMapper;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private CWalletMapper cWalletMapper;

    /**
     * 注册
     *
     * @param customer
     * @param parentcode
     * @param validcode
     * @return
     */
    public RestResponse handleRegister(CCustomer customer, String parentcode, String validcode) {
        String login_key = "register_login_" + customer.getPhone();
        boolean lockflag_phone = redisUtilsService.setIncrSecond(login_key, 10);
        //设置2s有效期这个手机号基本一天手机号注册可以成功的如果消失了后面还有固定判断 主要是保证短时间不要有并发问题
        if (!lockflag_phone) {
            return GetRest.getFail("请勿重复提交注册帐号，10s稍后再试");
        }
        CCustomerExample example = new CCustomerExample();
        example.createCriteria().andPhoneEqualTo(customer.getPhone());
        int count = cCustomerMapper.countByExample(example);
        if (count > 0) {
            return GetRest.getFail("手机号已存在");
        }
        String pass_pwd = PwdEncode.encodePwd(customer.getPassword());
        String pay_pwd = PwdEncode.encodePwd(customer.getPayword());
        customer.setPassword(pass_pwd);
        customer.setPayword(pay_pwd);
        RestResponse valid = utilsService.validPhone(customer.getPhone(), validcode);
        if (!valid.isStatus()) {
            throw new ThrowJsonException(valid.getDesc());
        }
        String invitationcode = RandNumUtils.get(RandNumType.NUMBER_LETTER, 6);
        customer.setInvitationcode(invitationcode);
        //如果存在推荐人
        if (!StringUtils.isBlank(parentcode)) {
            CCustomer parent = cCustomerMapper.getByInvitationcode(parentcode);
            if (parent == null) {
                return GetRest.getFail("邀请码不存在");
            }
            if (parent.getSalesman() == null) {
                throw new ThrowJsonException("当前邀请码暂未激活,请确认后使用");
            }
            customer.setUserid(parent.getUserid());
            customer.setSalesman(parent.getSalesman());
            customer.setParentid(parent.getId());
            //保存推荐关系网
            customer.setLayer(parent.getLayer() + 1);
            if (!StringUtils.isBlank(parent.getPushgenes())) {
                customer.setPushgenes(parent.getPushgenes() + "," + parent.getId());
            } else {
                customer.setPushgenes(String.valueOf(parent.getId()));
            }
        }
        try {
            String content = redisUtilsService.getKey(SysParamsEnums.app_host.getKey()) + "/share/"
                    + customer.getInvitationcode();
            RestUploadFileInfo r = com.image.common.Service.uploadQR(PathUtils.cus_qr, content);
            if (r.isStatus()) {
                customer.setQrurl(r.getServiceName() + r.getFilePath() + r.getFileName());
            }
        } catch (Exception e) {
            System.out.println("生成二维码失败");
        }
        int i = cCustomerMapper.insertSelective(customer);
        if (i < 1) {
            return GetRest.getSuccess("注册失败");
        }
        //保存钱包
        for (CoinEnums e : CoinEnums.values()) {
            CWallet wallet = new CWallet();
            wallet.setCid(customer.getId());
            wallet.setType(e.name());
//			if(CoinEnums.ETH.name().equals(e.name())) {
//				wallet.setPassword(customer.getPhone());
//			}
            cWalletMapper.insertSelective(wallet);
        }
        redisUtilsService.setKey(customer.getId() + "_banlance", "0");
        return GetRest.getSuccess("注册成功");
    }

    /**
     * 登陆
     *
     * @param login
     * @param password
     * @return
     */
    public RestResponse login(String login, String password) {
        String key = login + "_token";
        String enter_key = redisUtilsService.getKey(key);
        if (!StringUtils.isEmpty(enter_key)) {
            redisUtilsService.deleteKey(enter_key);
            //退出
            redisUtilsService.deleteKey(key);
        }
        CCustomer customer = cCustomerMapper.getByPhone(login);
        if (customer == null) {
            return GetRest.getFail("帐号或密码错误");
        }
        if (FunctionUtils.isEquals(StaticUtils.status_no, customer.getStatus())) {
            return GetRest.getFail("帐号已被冻结");
        }
        String pwd = PwdEncode.encodePwd(password);
        if (!pwd.equals(customer.getPassword())) {
            return GetRest.getFail("帐号或密码错误");
        }
        CustomerDto customerDto = new CustomerDto();
        BeanUtils.copyProperties(customer, customerDto);
        String jsonObject = JSONObject.toJSONString(customerDto);
        String token = PwdEncode.encodePwd(login + "_" + DateUtil.currentTimeMillis());
        // token 有效时间：30 天
        long maxtime = 30 * 24 * 60 * 60;
        redisUtilsService.setKey(token, jsonObject, maxtime);
        redisUtilsService.setKey(key, token, maxtime);
        return GetRest.getSuccess("登入成功", token);
    }

    /**
     * 退出
     *
     * @param token
     * @return
     */
    public RestResponse exit(String token) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        redisUtilsService.deleteKey(token);
        String key = customerDto.getLogin() + "_token";
        redisUtilsService.deleteKey(key);
        return GetRest.getSuccess("退出成功");
    }


    /**
     * 忘记密码
     *
     * @param email
     * @param password
     * @param validcode
     * @return
     */
    public RestResponse handleForgetpwd(String phone, String password, String validcode) {
        CCustomer customer = cCustomerMapper.getByPhone(phone);
        if (customer == null) {
            return GetRest.getFail("帐号不存在");
        }
        if (FunctionUtils.isEquals(StaticUtils.status_no, customer.getStatus())) {
            return GetRest.getFail("帐号已被冻结");
        }
        if (!phone.equals(customer.getPhone())) {
            return GetRest.getFail("帐号与手机号不匹配");
        }
        RestResponse valid = utilsService.validPhone(phone, validcode);
        if (!valid.isStatus()) {
            throw new ThrowJsonException(valid.getDesc());
        }
        String pwd = PwdEncode.encodePwd(password);
        customer.setPassword(pwd);
        cCustomerMapper.updateByPrimaryKeySelective(customer);
        return GetRest.getSuccess("密码重置成功");
    }

    /**
     * 修改密码
     *
     * @param cid
     * @param password
     * @param validcode
     * @param type      1修改密码 2修改交易密码
     * @return
     */
    public RestResponse editPass(String token, String password, String validcode, Integer type) {
        CustomerDto dto = utilsService.getCusByToken(token);
        Integer cid = dto.getId();
        if (StringUtils.isEmpty(password)) {
            return GetRest.getFail("请填写密码");
        }
        if (StringUtils.isEmpty(validcode)) {
            return GetRest.getFail("请填写验证码");
        }
        if (type == null) {
            return GetRest.getFail("修改类型错误");
        }
        CCustomer customer = cCustomerMapper.selectByPrimaryKey(cid);
        if (customer == null) {
            return GetRest.getFail("会员信息不存在");
        }
        String pwd = PwdEncode.encodePwd(password);
        if (FunctionUtils.isEquals(1, type)) {
            customer.setPassword(pwd);
            cCustomerMapper.updateByPrimaryKeySelective(customer);
        }
        if (FunctionUtils.isEquals(2, type)) {
            String valid = RpxUtils.valid_paypwd(password);
            if (!StringUtils.isEmpty(valid)) {
                return GetRest.getFail(valid);
            }
            customer.setPayword(pwd);
            cCustomerMapper.updateByPrimaryKeySelective(customer);
        }
        RestResponse valid = utilsService.validPhone(customer.getPhone(), validcode);
        if (!valid.isStatus()) {
            throw new ThrowJsonException(valid.getDesc());
        }
        return GetRest.getSuccess("成功");
    }


    /**
     * 后台注册-不需要短信验证码
     *
     * @param customer
     * @param parentcode
     * @return
     */
    public RestResponse handleRegister(CCustomer customer, String parentcode) {
        String login_key = "register_login_" + customer.getPhone();
        boolean lockflag_phone = redisUtilsService.setIncrSecond(login_key, 10);
        //设置2s有效期这个手机号基本一天手机号注册可以成功的如果消失了后面还有固定判断 主要是保证短时间不要有并发问题
        if (!lockflag_phone) {
            return GetRest.getFail("请勿重复提交注册帐号，10s稍后再试");
        }
        CCustomerExample example = new CCustomerExample();
        example.createCriteria().andPhoneEqualTo(customer.getPhone());
        int count = cCustomerMapper.countByExample(example);
        if (count > 0) {
            return GetRest.getFail("手机号已存在");
        }
        String pass_pwd = PwdEncode.encodePwd(customer.getPassword());
        String pay_pwd = PwdEncode.encodePwd(customer.getPayword());
        customer.setPassword(pass_pwd);
        customer.setPayword(pay_pwd);
        String invitationcode = RandNumUtils.get(RandNumType.NUMBER_LETTER, 6);
        customer.setInvitationcode(invitationcode);
        //如果存在推荐人
        if (!StringUtils.isBlank(parentcode)) {
            CCustomer parent = cCustomerMapper.getByInvitationcode(parentcode);
            if (parent == null) {
                return GetRest.getFail("邀请码不存在");
            }
            if (parent.getSalesman() == null) {
                throw new ThrowJsonException("当前邀请码暂未激活,请确认后使用");
            }
            customer.setUserid(parent.getUserid());
            customer.setSalesman(parent.getSalesman());
            customer.setParentid(parent.getId());
            //保存推荐关系网
            customer.setLayer(parent.getLayer() + 1);
            if (!StringUtils.isBlank(parent.getPushgenes())) {
                customer.setPushgenes(parent.getPushgenes() + "," + parent.getId());
            } else {
                customer.setPushgenes(String.valueOf(parent.getId()));
            }
        }
        try {
            String content = redisUtilsService.getKey(SysParamsEnums.app_host.getKey()) + "/share/"
                    + customer.getInvitationcode();
            RestUploadFileInfo r = com.image.common.Service.uploadQR(PathUtils.cus_qr, content);
            if (r.isStatus()) {
                customer.setQrurl(r.getServiceName() + r.getFilePath() + r.getFileName());
            }
        } catch (Exception e) {
            System.out.println("生成二维码失败");
        }
        int i = cCustomerMapper.insertSelective(customer);
        if (i < 1) {
            return GetRest.getSuccess("注册失败");
        }
        //保存钱包
        for (CoinEnums e : CoinEnums.values()) {
            CWallet wallet = new CWallet();
            wallet.setCid(customer.getId());
            wallet.setType(e.name());
//			if(CoinEnums.ETH.name().equals(e.name())) {
//				wallet.setPassword(customer.getPhone());
//			}
            cWalletMapper.insertSelective(wallet);
        }
        redisUtilsService.setKey(customer.getId() + "_banlance", "0");
        return GetRest.getSuccess("注册成功");
    }
}
