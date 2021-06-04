package com.pmzhongguo.ex.business.vo;

/**
 * @author jary
 * @creatTime 2019/5/23 4:24 PM
 */
public class MemberRspVo {

    private Integer id;//
    private String m_name;//手机号/用户名，暂定手机号，考虑可能要支持邮箱，使用varchar类型
    private String m_nick_name; //昵称
    private Integer level;
    private String introduce_m_id; //介绍人ID

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_nick_name() {
        return m_nick_name;
    }

    public void setM_nick_name(String m_nick_name) {
        this.m_nick_name = m_nick_name;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getIntroduce_m_id() {
        return introduce_m_id;
    }

    public void setIntroduce_m_id(String introduce_m_id) {
        this.introduce_m_id = introduce_m_id;
    }
}
