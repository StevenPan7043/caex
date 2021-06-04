package com.pmzhongguo.ex.business.entity;

public class AuthVideo
{
    private Integer id;//同m_member表ID
    private String v_im_info; //及时通讯号码
    private String v_video;//视频地址
    private Integer v_status;//状态：0：审核中，1：审核通过，2：审核不通过
    private String reject_reason;//审核不通过原因
    private String auditor;//审核人账号，关联frm_user表
    private String audit_time;//审核时间
    private String audit_history;//历史审核记录

    // 附加字段
    private String m_name;// 充值人
    // 附加字段
    private String real_name;// 充值人
    private Integer auth_grade; //认证级别

    public Integer getAuth_grade()
    {
        return auth_grade;
    }

    public void setAuth_grade(Integer auth_grade)
    {
        this.auth_grade = auth_grade;
    }

    public String getM_name()
    {
        return m_name;
    }

    public void setM_name(String m_name)
    {
        this.m_name = m_name;
    }

    public String getReal_name()
    {
        return real_name;
    }

    public void setReal_name(String real_name)
    {
        this.real_name = real_name;
    }

    public String getV_im_info()
    {
        return v_im_info;
    }

    public void setV_im_info(String v_im_info)
    {
        this.v_im_info = v_im_info;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getV_video()
    {
        return v_video;
    }

    public void setV_video(String v_video)
    {
        this.v_video = v_video;
    }

    public Integer getV_status()
    {
        return v_status;
    }

    public void setV_status(Integer v_status)
    {
        this.v_status = v_status;
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
}
