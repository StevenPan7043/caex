package com.pmzhongguo.ex.transfer.entity;

import com.pmzhongguo.ex.business.entity.ReqBaseSecret;

/**
 * @author jary
 * @creatTime 2019/5/24 5:20 PM
 */
public class AccountIntroReqVo extends ReqBaseSecret {

    private String m_name;
    private String direction;
    private String level;
    private String account;
    private String tradeID;

    /**
     * 冲提币地址
     */
    private String addr;

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTradeID() {
        return tradeID;
    }

    public void setTradeID(String tradeID) {
        this.tradeID = tradeID;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
