package com.pmzhongguo.crowd.entity;


/**
 * @description: 轮播图
 * @date: 2019-03-02 14:43
 * @author: 十一
 */
public class Banner {
    
    private Integer id;
    
    /**
     * banner 使用场景
     */
    private String scene;
    
    /**
     * banner名称
     */
    private String name;
    
    /**
     * banner 访问url
     */
    private String banner_url;

    /**
     * 排序
     */
    private String banner_order;

    /**
     * 更新时间
     */
    private String create_time;

    /**
     * 创建时间
     */
    private String update_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBanner_url() {
        return banner_url;
    }

    public void setBanner_url(String banner_url) {
        this.banner_url = banner_url;
    }

    public String getBanner_order() {
        return banner_order;
    }

    public void setBanner_order(String banner_order) {
        this.banner_order = banner_order;
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
}
