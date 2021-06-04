/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/1 All Rights Reserved.
 */
package com.pmzhongguo.ex.bean;

import java.io.Serializable;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/1 10:40
 * @description：App token 对象
 * @version: $
 */
public class SessionInfo implements Serializable {

    private static final long serialVersionUID = 8125626353111791507L;
    // 用户ID
    private Integer         id;

    // 用户名
    private String          m_name;

    // 国家代码
    private Integer         area_code;

    // 最后登陆ip
    private String          last_login_ip;

    // 最后登陆时间
    private Long            last_login_time;

    // token过期时间
    private Long            expire_time;

    public SessionInfo() {
    }


    public SessionInfo(Integer id, String m_name, Integer area_code, String last_login_ip, Long last_login_time, Long expire_time) {
        this.id = id;
        this.m_name = m_name;
        this.area_code = area_code;
        this.last_login_ip = last_login_ip;
        this.last_login_time = last_login_time;
        this.expire_time = expire_time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public Integer getArea_code() {
        return area_code;
    }

    public void setArea_code(Integer area_code) {
        this.area_code = area_code;
    }

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public Long getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(Long last_login_time) {
        this.last_login_time = last_login_time;
    }

    public Long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(Long expire_time) {
        this.expire_time = expire_time;
    }

}
