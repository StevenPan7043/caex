package com.pmzhongguo.crowd.entity;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @description: 项目信息
 * @date: 2019-03-02 14:47
 * @author: 十一
 */
public class CrowdProject implements Serializable{


    private static final long serialVersionUID = 6826972501985800221L;

    private Integer id;


    /**
     * 项目名称
     */
    private String name;

    /**
     * 项目币种名称
     */
    private String currency;



    /**
     * 抢购价格
     */
    private BigDecimal rush_price;




    /**
     * 购买下限
     */
    private BigDecimal buy_lower_limit;

    /**
     * 购买上限
     */
    private BigDecimal buy_upper_limit;

    /**
     * 购买人数
     */
    private Integer buy_person_count;

    /**
     * 数量精度
     */
    private Integer c_precision;

    /**
     * 价格精度
     */
    private Integer p_precision;

    /**
     * 剩余数量
     */
    private BigDecimal remain_amount;

    /**
     * 发行数量
     */
    private BigDecimal release_amount;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 更新时间
     */
    private String update_time;

    /**
     * 是否锁仓
     */
    private boolean is_lock;

    /**
     * 计价币种名称
     */
    private String quote_currency;

    /**
     * 抢购开始时间
     */
    private String rush_begin_time;

    /**
     * 抢购结束时间
     */
    private String rush_end_time;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getRush_price() {
        return rush_price;
    }

    public void setRush_price(BigDecimal rush_price) {
        this.rush_price = rush_price;
    }


    public BigDecimal getBuy_lower_limit() {
        return buy_lower_limit;
    }

    public void setBuy_lower_limit(BigDecimal buy_lower_limit) {
        this.buy_lower_limit = buy_lower_limit;
    }

    public BigDecimal getBuy_upper_limit() {
        return buy_upper_limit;
    }

    public void setBuy_upper_limit(BigDecimal buy_upper_limit) {
        this.buy_upper_limit = buy_upper_limit;
    }

    public Integer getBuy_person_count() {
        return buy_person_count;
    }

    public void setBuy_person_count(Integer buy_person_count) {
        this.buy_person_count = buy_person_count;
    }

    public Integer getC_precision() {
        return c_precision;
    }

    public void setC_precision(Integer c_precision) {
        this.c_precision = c_precision;
    }

    public BigDecimal getRemain_amount() {
        return remain_amount;
    }

    public void setRemain_amount(BigDecimal remain_amount) {
        this.remain_amount = remain_amount;
    }

    public BigDecimal getRelease_amount() {
        return release_amount;
    }

    public void setRelease_amount(BigDecimal release_amount) {
        this.release_amount = release_amount;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public boolean isIs_lock() {
        return is_lock;
    }

    public void setIs_lock(boolean is_lock) {
        this.is_lock = is_lock;
    }

    public String getQuote_currency() {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency) {
        this.quote_currency = quote_currency;
    }

    public String getRush_begin_time() {
        return rush_begin_time;
    }

    public void setRush_begin_time(String rush_begin_time) {
        this.rush_begin_time = rush_begin_time;
    }

    public String getRush_end_time() {
        return rush_end_time;
    }

    public void setRush_end_time(String rush_end_time) {
        this.rush_end_time = rush_end_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getP_precision() {
        return p_precision;
    }

    public void setP_precision(Integer p_precision) {
        this.p_precision = p_precision;
    }
}
