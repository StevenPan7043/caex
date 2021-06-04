package com.pmzhongguo.crowd.entity;



/**
 * @description: 项目币种描述信息
 * @date: 2019-03-04 11:23
 * @author: 十一
 */
public class CrowdProjectInfo {

    /**
     * 这个id，关联CrowdProject 中的id
     */
    private Integer id;

    /**
     * 项目名称
     */
    private String currency;

    /**
     *  图片url
     */
    private String img_url;


    /**
     * 是否显示
     */
    private String is_show;

    /**
     * 项目摘要中文
     */
    private String project_abstract;

    /**
     * 项目摘要英文
     */
    private String project_abstract_en;



    /**
     * 项目排序，数字越小越靠前
     */
    private String project_order;


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
     * 创建时间
     */
    private String create_time;

    /**
     * 更新时间
     */
    private String update_time;


    /**
     * 白皮书
     */
    private String white_book;

    /**
     * 是否锁仓
     */
    private boolean is_lock;

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

    public String getIs_show() {
        return is_show;
    }

    public void setIs_show(String is_show) {
        this.is_show = is_show;
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

    public String getProject_order() {
        return project_order;
    }

    public void setProject_order(String project_order) {
        this.project_order = project_order;
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

    public String getBase_info_en() {
        return base_info_en;
    }

    public void setBase_info_en(String base_info_en) {
        this.base_info_en = base_info_en;
    }

    public String getCurrency_allocate_en() {
        return currency_allocate_en;
    }

    public void setCurrency_allocate_en(String currency_allocate_en) {
        this.currency_allocate_en = currency_allocate_en;
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
}
