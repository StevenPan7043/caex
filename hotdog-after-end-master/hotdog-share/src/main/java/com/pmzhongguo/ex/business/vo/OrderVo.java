package com.pmzhongguo.ex.business.vo;

import java.math.BigDecimal;

/**
 * @author jary
 * @creatTime 2019/11/14 10:55 AM
 */
public class OrderVo {

    /**
     * 用户名
     */
    private String m_name;

    /**
     * 订单号
     */
    private String o_no;

    /**
     * 交易对
     */
    private String symbol;

    /**
     * 计价货币，如CNY
     */
    private String  quote_currency;

    /**
     * 订单状态
     */
    private String o_state;

    /**
     * 订单类型：buy、sell，即卖单、买单
     */
    private String o_type;

    /**
     * 已成交数量
     */
    private BigDecimal done_volume;//已成交数量，单位和含义，同volume

    /**
     * 总金额
     */
    private BigDecimal total_amount ;

    /**
     * 完成时间
     */
    private String complet_time;

    /**
     * 委托时间（创建时间）
     */
    private String create_time;
    /**
     * 委托价（挂单）
     */
    private BigDecimal price;
    /**
     * 委托量
     */
    private BigDecimal volume;

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getO_no() {
        return o_no;
    }

    public void setO_no(String o_no) {
        this.o_no = o_no;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getO_state() {
        return o_state;
    }

    public void setO_state(String o_state) {
        this.o_state = o_state;
    }

    public String getO_type() {
        return o_type;
    }

    public void setO_type(String o_type) {
        this.o_type = o_type;
    }

    public BigDecimal getDone_volume() {
        return done_volume;
    }

    public void setDone_volume(BigDecimal done_volume) {
        this.done_volume = done_volume;
    }

    public BigDecimal getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(BigDecimal total_amount) {
        this.total_amount = total_amount;
    }

    public String getQuote_currency() {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency) {
        this.quote_currency = quote_currency;
    }

    public String getComplet_time() {
        return complet_time;
    }

    public void setComplet_time(String complet_time) {
        this.complet_time = complet_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }
}
