package com.pmzhongguo.crowd.dto;

import java.math.BigDecimal;


/**
 * @description: 项目信息
 * @date: 2019-03-02 14:47
 * @author: 十一
 */
public class CrowdProjectDto {

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
     *  图片url
     */
    private String img_url;


    /**
     * 是否显示
     */
    private Boolean is_show;

    /**
     * 抢购价格
     */
    private BigDecimal rush_price;

    /**
     * 项目摘要中文
     */
    private String project_abstract;

    /**
     * 项目摘要英文
     */
    private String project_abstract_en;

    /**
     * 项目状态，0:预热中，1:进行中，2:已结束
     */
    private Integer status;

    /**
     * 项目排序，数字越小越靠前
     */
    private String project_order;

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
     * 项目介绍中文
     */
    private String project_introduce;

    /**
     * 项目介绍英文
     */
    private String project_introduce_en;

    /**
     * 基础信息中文
     */
    private String base_info;

    /**
     * 基础信息英文
     */
    private String base_info_en;

    /**
     * 销售计划中文
     */
    private String sale_plan;

    /**
     * 销售计划英文
     */
    private String sale_plan_en;

    /**
     * 代币分配中文
     */
    private String currency_allocate;

    /**
     * 代币分配英文
     */
    private String currency_allocate_en;

    /**
     * 项目概况中文
     */
    private String project_overview;

    /**
     * 项目概况英文
     */
    private String project_overview_en;

    /**
     * 团队成员介绍中文
     */
    private String team_member;

    /**
     * 团队成员介绍英文
     */
    private String team_member_en;


    /**
     * 发展路线介绍中文
     */
    private String dev_line;

    /**
     * 发展路线介绍英文
     */
    private String dev_line_en;

    /**
     * 抢购开始时间
     */
    private String rush_begin_time;

    /**
     * 抢购结束时间
     */
    private String rush_end_time;


    /**
     * 是否锁仓
     */
    private Boolean is_lock;

    /**
     * 计价币种名称
     */
    private String quote_currency;

    /**
     * 白皮书
     */
    private String white_book;

    public String getWhite_book() {
        return white_book;
    }

    public void setWhite_book(String white_book) {
        this.white_book = white_book;
    }

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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }


    public BigDecimal getRush_price() {
        return rush_price;
    }

    public void setRush_price(BigDecimal rush_price) {
        this.rush_price = rush_price;
    }

    public String getProject_abstract() {
        return project_abstract;
    }

    public void setProject_abstract(String project_abstract) {
        this.project_abstract = project_abstract;
    }

    public String getProject_abstract_en() {
        return project_abstract_en;
    }

    public void setProject_abstract_en(String project_abstract_en) {
        this.project_abstract_en = project_abstract_en;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProject_order() {
        return project_order;
    }

    public void setProject_order(String project_order) {
        this.project_order = project_order;
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

    public String getProject_introduce() {
        return project_introduce;
    }

    public void setProject_introduce(String project_introduce) {
        this.project_introduce = project_introduce;
    }

    public String getProject_introduce_en() {
        return project_introduce_en;
    }

    public void setProject_introduce_en(String project_introduce_en) {
        this.project_introduce_en = project_introduce_en;
    }

    public String getBase_info() {
        return base_info;
    }

    public void setBase_info(String base_info) {
        this.base_info = base_info;
    }

    public String getBase_info_en() {
        return base_info_en;
    }

    public void setBase_info_en(String base_info_en) {
        this.base_info_en = base_info_en;
    }

    public String getSale_plan() {
        return sale_plan;
    }

    public void setSale_plan(String sale_plan) {
        this.sale_plan = sale_plan;
    }

    public String getSale_plan_en() {
        return sale_plan_en;
    }

    public void setSale_plan_en(String sale_plan_en) {
        this.sale_plan_en = sale_plan_en;
    }

    public String getCurrency_allocate() {
        return currency_allocate;
    }

    public void setCurrency_allocate(String currency_allocate) {
        this.currency_allocate = currency_allocate;
    }

    public String getCurrency_allocate_en() {
        return currency_allocate_en;
    }

    public void setCurrency_allocate_en(String currency_allocate_en) {
        this.currency_allocate_en = currency_allocate_en;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProject_overview() {
        return project_overview;
    }

    public void setProject_overview(String project_overview) {
        this.project_overview = project_overview;
    }

    public String getProject_overview_en() {
        return project_overview_en;
    }

    public void setProject_overview_en(String project_overview_en) {
        this.project_overview_en = project_overview_en;
    }

    public String getTeam_member() {
        return team_member;
    }

    public void setTeam_member(String team_member) {
        this.team_member = team_member;
    }

    public String getTeam_member_en() {
        return team_member_en;
    }

    public void setTeam_member_en(String team_member_en) {
        this.team_member_en = team_member_en;
    }

    public String getDev_line() {
        return dev_line;
    }

    public void setDev_line(String dev_line) {
        this.dev_line = dev_line;
    }

    public String getDev_line_en() {
        return dev_line_en;
    }

    public void setDev_line_en(String dev_line_en) {
        this.dev_line_en = dev_line_en;
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


    public String getQuote_currency() {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency) {
        this.quote_currency = quote_currency;
    }

    public Boolean getIs_show() {
        return is_show;
    }

    public void setIs_show(Boolean is_show) {
        this.is_show = is_show;
    }

    public Boolean getIs_lock() {
        return is_lock;
    }

    public void setIs_lock(Boolean is_lock) {
        this.is_lock = is_lock;
    }

    public Integer getP_precision() {
        return p_precision;
    }

    public void setP_precision(Integer p_precision) {
        this.p_precision = p_precision;
    }
}
