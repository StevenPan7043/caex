package com.pmzhongguo.otc.entity.dto;

import com.pmzhongguo.otc.otcenum.PayTypeEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;
import com.pmzhongguo.zzextool.utils.JsonUtil;

import java.io.Serializable;

public class AccountInfoDTO implements Serializable
{
    private Integer id;

    private Integer memberId;

    private PayTypeEnum type;

    private String name;

    private String account;

    private String bankOrImg;

    private String createTime;

    private String modifyTime;

    private WhetherEnum isDelete;

    //资金密码，操作账户时用
    private String securityPwd;

    private static final long serialVersionUID = 1L;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Integer memberId)
    {
        this.memberId = memberId;
    }

    public PayTypeEnum getType()
    {
        return type;
    }

    public void setType(PayTypeEnum type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name == null ? null : name.trim();
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account == null ? null : account.trim();
    }

    public String getBankOrImg()
    {
        return bankOrImg;
    }

    public void setBankOrImg(String bankOrImg)
    {
        this.bankOrImg = bankOrImg == null ? null : bankOrImg.trim();
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getModifyTime()
    {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime)
    {
        this.modifyTime = modifyTime == null ? null : modifyTime.trim();
    }

    public WhetherEnum getIsDelete()
    {
        return isDelete;
    }

    public void setIsDelete(WhetherEnum isDelete)
    {
        this.isDelete = isDelete;
    }

    public String getSecurityPwd()
    {
        return securityPwd;
    }

    public void setSecurityPwd(String securityPwd)
    {
        this.securityPwd = securityPwd;
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