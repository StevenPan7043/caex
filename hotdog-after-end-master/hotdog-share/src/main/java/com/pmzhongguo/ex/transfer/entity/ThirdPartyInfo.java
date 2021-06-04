/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/6 All Rights Reserved.
 */
package com.pmzhongguo.ex.transfer.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/6 14:27
 * @description：第三方划转项目方信息
 * @version: $
 */
public class ThirdPartyInfo implements Serializable
{

    private static final long serialVersionUID = 2096976490597087364L;
    private Integer id;

    /**
     * 服务端apiKey
     */
    private String s_apiKey;

    /**
     * 服务端secretKey
     */
    private String s_secretKey;

    /**
     * 客户端回调地址
     */
    private String c_ip;

    /**
     * 接口是否可用(0 可用, 1 不可用)
     */
    private Integer c_flag;

    /**
     * 客户端appKey
     */
    private String c_appKey;

    /**
     * 项目方名称
     */
    private String c_name;

    /**
     * 客户端成功状态码
     */
    private String c_code;

    /**
     * 项目方批次，1：第一批次，2：第二批次
     */
    private Integer c_name_type;

    /**
     * IP 白名单
     */
    private String whiteIp;

    /**
     * 可充值币种(币种使用 , 分割)
     */
    private String can_deposit_currency;

    /**
     * 可提币币种(币种使用 , 分割)
     */
    private String can_withdraw_currency;

    /**
     * 拓展字段(json)
     */
    private String ext;

    /**
     * 创建时间
     */
    private String create_time;

    /**
     * 更新时间
     */
    private String update_time;


    // 缓存可用币种信息
    private List<String> deposit_currency_list;

    private List<String> withdraw_currency_list;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getS_apiKey()
    {
        return s_apiKey;
    }

    public void setS_apiKey(String s_apiKey)
    {
        this.s_apiKey = s_apiKey;
    }

    public String getS_secretKey()
    {
        return s_secretKey;
    }

    public void setS_secretKey(String s_secretKey)
    {
        this.s_secretKey = s_secretKey;
    }

    public String getC_ip()
    {
        return c_ip;
    }

    public void setC_ip(String c_ip)
    {
        this.c_ip = c_ip;
    }

    public Integer getC_flag()
    {
        return c_flag;
    }

    public void setC_flag(Integer c_flag)
    {
        this.c_flag = c_flag;
    }

    public String getC_appKey()
    {
        return c_appKey;
    }

    public void setC_appKey(String c_appKey)
    {
        this.c_appKey = c_appKey;
    }

    public String getC_name()
    {
        return c_name;
    }

    public void setC_name(String c_name)
    {
        this.c_name = c_name;
    }

    public String getC_code()
    {
        return c_code;
    }

    public void setC_code(String c_code)
    {
        this.c_code = c_code;
    }

    public String getWhiteIp()
    {
        return whiteIp;
    }

    public void setWhiteIp(String whiteIp)
    {
        this.whiteIp = whiteIp;
    }

    public String getCan_deposit_currency()
    {
        return can_deposit_currency;
    }

    public void setCan_deposit_currency(String can_deposit_currency)
    {
        this.can_deposit_currency = can_deposit_currency;
    }

    public String getCan_withdraw_currency()
    {
        return can_withdraw_currency;
    }

    public void setCan_withdraw_currency(String can_withdraw_currency)
    {
        this.can_withdraw_currency = can_withdraw_currency;
    }

    public String getExt()
    {
        return ext;
    }

    public void setExt(String ext)
    {
        this.ext = ext;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getUpdate_time()
    {
        return update_time;
    }

    public void setUpdate_time(String update_time)
    {
        this.update_time = update_time;
    }

    public List<String> getDeposit_currency_list()
    {
        return deposit_currency_list;
    }

    public void setDeposit_currency_list(List<String> deposit_currency_list)
    {
        this.deposit_currency_list = deposit_currency_list;
    }

    public List<String> getWithdraw_currency_list()
    {
        return withdraw_currency_list;
    }

    public void setWithdraw_currency_list(List<String> withdraw_currency_list)
    {
        this.withdraw_currency_list = withdraw_currency_list;
    }

    public Integer getC_name_type() {
        return c_name_type;
    }

    public void setC_name_type(Integer c_name_type) {
        this.c_name_type = c_name_type;
    }
}
