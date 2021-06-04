package com.pmzhongguo.ex.transfer.entity;

import com.pmzhongguo.ex.business.entity.ReqBaseSecret;

/**
 * @author jary
 * @creatTime 2019/5/20 9:56 AM
 */
public class RespUserSecret extends ReqBaseSecret {

    private int memberId;
    private String m_name;
    private String phone;
    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
