package com.pmzhongguo.crowd.dto;

import java.math.BigDecimal;

/**
 * @description: 众筹项目订单
 * @date: 2019-03-02 15:30
 * @author: 十一
 */
public class CrowdOrderDto {

    private Long id;

    private String m_name;


    /**
     * 币种名称
     */
    private String currency;

    /**
     * 计价币种名称
     */
    private String quote_currency;

    /**
     * 接收地址
     */
    private String addr;

    /**
     * 购买价格
     */
    private BigDecimal price;

    /**
     * 购买数量
     */
    private BigDecimal volume;

    /**
     * 总额
     */
    private BigDecimal total_price;

    /**
     * 是否锁仓，1:是，0:否
     */
    private boolean is_lock;


    /**
     * 订单来源，0:web，1:ios，2:Android，3:api
     */
    private Integer order_source;

    /**
     * 订单号，商品项目id+用户账号+随机10位
     */
    private String order_no;

    /**
     * 订单成交时间
     */
    private String done_time;

    /**
     * 操作ip
     */
    private String oper_ip;

    /**
     * 用户id
     */
    private Integer member_id;

    /**
     * 项目id
     */
    private Integer crd_pro_id;

    private String update_time;

    private Integer is_transfer;

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public Integer getIs_transfer() {
        return is_transfer;
    }

    public void setIs_transfer(Integer is_transfer) {
        this.is_transfer = is_transfer;
    }

    public Integer getMember_id() {
        return member_id;
    }

    public void setMember_id(Integer member_id) {
        this.member_id = member_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }



    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getQuote_currency() {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency) {
        this.quote_currency = quote_currency;
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

    public BigDecimal getTotal_price() {
        return total_price;
    }

    public void setTotal_price(BigDecimal total_price) {
        this.total_price = total_price;
    }

    public boolean isIs_lock() {
        return is_lock;
    }

    public void setIs_lock(boolean is_lock) {
        this.is_lock = is_lock;
    }

    public Integer getOrder_source() {
        return order_source;
    }

    public void setOrder_source(Integer order_source) {
        this.order_source = order_source;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getDone_time() {
        return done_time;
    }

    public void setDone_time(String done_time) {
        this.done_time = done_time;
    }

    public String getOper_ip() {
        return oper_ip;
    }

    public void setOper_ip(String oper_ip) {
        this.oper_ip = oper_ip;
    }

    public Integer getCrd_pro_id() {
        return crd_pro_id;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public void setCrd_pro_id(Integer crd_pro_id) {
        this.crd_pro_id = crd_pro_id;
    }
}
