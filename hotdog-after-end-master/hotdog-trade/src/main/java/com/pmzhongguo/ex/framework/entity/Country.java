package com.pmzhongguo.ex.framework.entity;

/**
 * @author jary
 * @creatTime 2019/7/3 9:51 AM
 */
public class Country {

    private Integer id;
    private String country_name_en;
    private String country_name_cn;
    private String area_code;
    private Integer c_order;

    public Country() {
    }

    public Country(Integer id, String country_name_en, String country_name_cn, String area_code, Integer c_order) {
        this.id = id;
        this.country_name_en = country_name_en;
        this.country_name_cn = country_name_cn;
        this.area_code = area_code;
        this.c_order = c_order;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountry_name_en() {
        return country_name_en;
    }

    public void setCountry_name_en(String country_name_en) {
        this.country_name_en = country_name_en;
    }

    public String getCountry_name_cn() {
        return country_name_cn;
    }

    public void setCountry_name_cn(String country_name_cn) {
        this.country_name_cn = country_name_cn;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public Integer getC_order() {
        return c_order;
    }

    public void setC_order(Integer c_order) {
        this.c_order = c_order;
    }
}
