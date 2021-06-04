package com.pmzhongguo.ex.business.entity;

public class AuthIdentity
{
    private Integer id;//同m_member表ID
    private String given_name;
    private String middle_name;
    private String family_name;
    private String sex; //性别
    private String nationality; //国籍
    private String province; //省州
    private String city; //城市
    private String birthday; //生日
    private String nation; //民族
    private String id_type;//身份证明证件类型：二代身份证、港澳通行证、台湾通行证、护照
    private String id_number;//身份证明证件号码
    private String id_begin_date;//证件有效期起
    private String id_end_date;//证件有效期止
    private String id_handheld_img;//手持证件图片地址
    private String id_front_img;//证件正面图片地址
    private String id_back_img;//证件反面图片地址
    private String last_submit_time;//最后提交审核时间（若审核不通过，填重新提交审核的时间）
    private Integer id_status;//状态：0：审核中，1：审核通过，2：审核不通过，3：待人脸比对
    private String reject_reason;//审核不通过原因
    private String auditor;//审核人账号，关联frm_user表
    private String audit_time;//审核时间
    private String audit_history;//历史审核记录

    // 附加字段
    private String m_name;// 充值人
    private String last_login_ip;
    private String introduce_m_id;
    private String biz_token;//faceId的验证token


    public String getIntroduce_m_id()
    {
        return introduce_m_id;
    }

    public void setIntroduce_m_id(String introduce_m_id)
    {
        this.introduce_m_id = introduce_m_id;
    }

    public String getLast_login_ip()
    {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip)
    {
        this.last_login_ip = last_login_ip;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getBirthday()
    {
        return birthday;
    }

    public void setBirthday(String birthday)
    {
        this.birthday = birthday;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNationality()
    {
        return nationality;
    }

    public void setNationality(String nationality)
    {
        this.nationality = nationality;
    }

    public String getM_name()
    {
        return m_name;
    }

    public void setM_name(String m_name)
    {
        this.m_name = m_name;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getGiven_name()
    {
        return given_name;
    }

    public void setGiven_name(String given_name)
    {
        this.given_name = given_name;
    }

    public String getMiddle_name()
    {
        return middle_name;
    }

    public void setMiddle_name(String middle_name)
    {
        this.middle_name = middle_name;
    }

    public String getFamily_name()
    {
        return family_name;
    }

    public void setFamily_name(String family_name)
    {
        this.family_name = family_name;
    }

    public String getId_type()
    {
        return id_type;
    }

    public void setId_type(String id_type)
    {
        this.id_type = id_type;
    }

    public String getId_number()
    {
        return id_number;
    }

    public void setId_number(String id_number)
    {
        this.id_number = id_number;
    }

    public String getId_begin_date()
    {
        return id_begin_date;
    }

    public void setId_begin_date(String id_begin_date)
    {
        this.id_begin_date = id_begin_date;
    }

    public String getId_end_date()
    {
        return id_end_date;
    }

    public void setId_end_date(String id_end_date)
    {
        this.id_end_date = id_end_date;
    }

    public String getId_handheld_img()
    {
        return id_handheld_img;
    }

    public void setId_handheld_img(String id_handheld_img)
    {
        this.id_handheld_img = id_handheld_img;
    }

    public String getId_front_img()
    {
        return id_front_img;
    }

    public void setId_front_img(String id_front_img)
    {
        this.id_front_img = id_front_img;
    }

    public String getId_back_img()
    {
        return id_back_img;
    }

    public void setId_back_img(String id_back_img)
    {
        this.id_back_img = id_back_img;
    }

    public String getLast_submit_time()
    {
        return last_submit_time;
    }

    public void setLast_submit_time(String last_submit_time)
    {
        this.last_submit_time = last_submit_time;
    }

    public Integer getId_status()
    {
        return id_status;
    }

    public void setId_status(Integer id_status)
    {
        this.id_status = id_status;
    }

    public String getReject_reason()
    {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason)
    {
        this.reject_reason = reject_reason;
    }

    public String getAuditor()
    {
        return auditor;
    }

    public void setAuditor(String auditor)
    {
        this.auditor = auditor;
    }

    public String getAudit_time()
    {
        return audit_time;
    }

    public void setAudit_time(String audit_time)
    {
        this.audit_time = audit_time;
    }

    public String getAudit_history()
    {
        return audit_history;
    }

    public void setAudit_history(String audit_history)
    {
        this.audit_history = audit_history;
    }

    public String getBiz_token() {
        return biz_token;
    }

    public void setBiz_token(String biz_token) {
        this.biz_token = biz_token;
    }
}
