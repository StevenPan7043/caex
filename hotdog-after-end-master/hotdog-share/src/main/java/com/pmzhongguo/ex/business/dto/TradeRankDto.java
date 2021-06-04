package com.pmzhongguo.ex.business.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 交易排名dto
 * @date: 2019-01-18 19:07
 * @author: 十一
 */
public class TradeRankDto implements Serializable {

    private static final long serialVersionUID = 210745994414157536L;

    /**
     * 交易对类型，如：EBANKZC
     */
    private String type;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 总数量
     */
    private BigDecimal num;

    /**
     * 用户账号，隐藏处理，如：138*****083
     */
    private String account;

    /**
     * 24h的量
     */
    private BigDecimal dayNum;

    /**
     * 排名
     */
    private Integer no;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public BigDecimal getNum() {
        return num;
    }

    public void setNum(BigDecimal num) {
        this.num = num;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getDayNum() {
        return dayNum;
    }

    public void setDayNum(BigDecimal dayNum) {
        this.dayNum = dayNum;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }
}
