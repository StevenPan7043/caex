package com.pmzhongguo.ex.business.vo;

/**
 * @Created by 张众
 * @Date 2019/11/30 17:46
 * @Description
 */
public class AuthIdentityVo {
    private String id_number;//身份证明证件号码
    private String province; //省州
    private String birthday; //生日
    private String sex; //性别
    private String name;//名称
    private String nation;//民族
    private String city; //城市
    private String id_begin_date;//民族
    private String id_end_date;//民族

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId_begin_date() {
        return id_begin_date;
    }

    public void setId_begin_date(String id_begin_date) {
        this.id_begin_date = id_begin_date;
    }

    public String getId_end_date() {
        return id_end_date;
    }

    public void setId_end_date(String id_end_date) {
        this.id_end_date = id_end_date;
    }
}
