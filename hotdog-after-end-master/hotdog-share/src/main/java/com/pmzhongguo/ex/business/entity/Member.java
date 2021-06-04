package com.pmzhongguo.ex.business.entity;

import java.io.Serializable;

public class Member implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -2895354388611265742L;

    private Integer id;

    //uid
    private String uid;

    //国家代码，目前默认均为86表示中国   en  英文/ ja 日文/ ko 韩文/ zh 中文
    private Integer area_code;

    //手机号/用户名，暂定手机号，考虑可能要支持邮箱，使用varchar类型
    private String m_name;

    //短信验证码，同时作为注册和找回密码的验证码
    private String sms_code;

    //短信验证码发送时间
    private String sms_code_time;

    //密码,md5后，取前25位
    private String m_pwd;

    //资金密码,md5后，取前25位
    private String m_security_pwd;
    //注册时间，获取手机验证码时就填充
    private String reg_time;
    private String last_login_ip; //最后登录IP
    private String last_login_time;//最后登录时间，第一次填注册时间
    private String last_mod_pwd_time; //最后修改密码时间，某些关键操作只能在修改密码后24小时后操作
    private Integer auth_grade;//认证等级，0表示未认证，1表示F1,2表示F2，提现人民币1级即可，提现比特币需2级认证，m_auth_identity审核通过即为1级，m_auth_video审核通过即为2级
    private Integer m_status;//状态：0：手机未验证，1：正常，2：冻结
    private String last_login_device; //最后登录设备 0:APP 1:ANDROID 2:IOS 3:PC

    //API状态：1：正常，2：冻结
    private Integer api_status;

    //介绍人ID
    private Integer introduce_m_id;
    // 邀请码
    private String invite_code;

    //昵称
    private String m_nick_name;

    //谷歌验证码私钥
    private String google_auth_key;

    //校验码
    private String validateCode;

    //隐藏了部分的用户名
    private String m_name_hidden;

    //用户输入的谷歌验证码
    private Integer google_auth_code;

    //用户修改谷歌验证码时，输入旧的谷歌验证码
    private Integer old_google_auth_code;

    // 佣金比例，1为正常收取，0为不收取，0.5为打5折
    private Double trade_commission;

    // 指纹或手势验证token
    private String authToken;

    //token
    private String token;

    // api请求限制 1不受限制，0限制
    private Integer api_limit;

    //手机号码
    private String phone;

    /**
     * 合约token
     */
    private String contactToken;

    /**
     * 合约是否被激活，默认未激活。-1：未激活；0激活
     */
    private Integer isValid;

    /**
     * 国际标准
     */
    private String inter_standard;

    /**
     *是否是模拟账号注册 1:是   0:否
     */
    private int checkeds;

    public String getContactToken() {
        return contactToken;
    }

    public void setContactToken(String contactToken) {
        this.contactToken = contactToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Integer getArea_code() {
        return area_code;
    }

    public void setArea_code(Integer area_code) {
        this.area_code = area_code;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getSms_code() {
        return sms_code;
    }

    public void setSms_code(String sms_code) {
        this.sms_code = sms_code;
    }

    public String getSms_code_time() {
        return sms_code_time;
    }

    public void setSms_code_time(String sms_code_time) {
        this.sms_code_time = sms_code_time;
    }

    public String getM_pwd() {
        return m_pwd;
    }

    public void setM_pwd(String m_pwd) {
        this.m_pwd = m_pwd;
    }

    public String getM_security_pwd() {
        return m_security_pwd;
    }

    public void setM_security_pwd(String m_security_pwd) {
        this.m_security_pwd = m_security_pwd;
    }

    public String getReg_time() {
        return reg_time;
    }

    public void setReg_time(String reg_time) {
        this.reg_time = reg_time;
    }

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getLast_mod_pwd_time() {
        return last_mod_pwd_time;
    }

    public void setLast_mod_pwd_time(String last_mod_pwd_time) {
        this.last_mod_pwd_time = last_mod_pwd_time;
    }

    public Integer getAuth_grade() {
        return auth_grade;
    }

    public void setAuth_grade(Integer auth_grade) {
        this.auth_grade = auth_grade;
    }

    public Integer getM_status() {
        return m_status;
    }

    public void setM_status(Integer m_status) {
        this.m_status = m_status;
    }

    public Integer getApi_status() {
        return api_status;
    }

    public void setApi_status(Integer api_status) {
        this.api_status = api_status;
    }

    public Integer getIntroduce_m_id() {
        return introduce_m_id;
    }

    public void setIntroduce_m_id(Integer introduce_m_id) {
        this.introduce_m_id = introduce_m_id;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    public String getM_nick_name() {
        return m_nick_name;
    }

    public void setM_nick_name(String m_nick_name) {
        this.m_nick_name = m_nick_name;
    }

    public String getGoogle_auth_key() {
        return google_auth_key;
    }

    public void setGoogle_auth_key(String google_auth_key) {
        this.google_auth_key = google_auth_key;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }

    public String getM_name_hidden() {
        return m_name_hidden;
    }

    public void setM_name_hidden(String m_name_hidden) {
        this.m_name_hidden = m_name_hidden;
    }

    public Integer getGoogle_auth_code() {
        return google_auth_code;
    }

    public void setGoogle_auth_code(Integer google_auth_code) {
        this.google_auth_code = google_auth_code;
    }

    public Integer getOld_google_auth_code() {
        return old_google_auth_code;
    }

    public void setOld_google_auth_code(Integer old_google_auth_code) {
        this.old_google_auth_code = old_google_auth_code;
    }

    public Double getTrade_commission() {
        return trade_commission;
    }

    public void setTrade_commission(Double trade_commission) {
        this.trade_commission = trade_commission;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getApi_limit() {
        return api_limit;
    }

    public void setApi_limit(Integer api_limit) {
        this.api_limit = api_limit;
    }

    public String getInter_standard() {
        return inter_standard;
    }

    public void setInter_standard(String inter_standard) {
        this.inter_standard = inter_standard;
    }

    public Integer getIsValid() {
        return isValid;
    }

    public void setIsValid(Integer isValid) {
        this.isValid = isValid;
    }

    public int getCheckeds() {
        return checkeds;
    }

    public String getLast_login_device() {
        return last_login_device;
    }

    public void setLast_login_device(String last_login_device) {
        this.last_login_device = last_login_device;
    }

    public void setCheckeds(int checkeds) {
        this.checkeds = checkeds;
    }
}
