package com.pmzhongguo.ex.business.vo;

import com.pmzhongguo.zzextool.utils.JsonUtil;

/**
 * @description: jsc app账号绑定
 * @date: 2019-05-05 16:51
 * @author: 十一
 */
public class JscAccountBindingVo
{

    private String account;

    private String fundPwd;

    private String sign;

    private Integer timestamp;

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getFundPwd()
    {
        return fundPwd;
    }

    public void setFundPwd(String fundPwd)
    {
        this.fundPwd = fundPwd;
    }

    public String getSign()
    {
        return sign;
    }

    public void setSign(String sign)
    {
        this.sign = sign;
    }

    public Integer getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp)
    {
        this.timestamp = timestamp;
    }

    @Override
    public String toString()
    {
        String result = "";
        try
        {
            result = JsonUtil.beanToJson(this);
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

}
