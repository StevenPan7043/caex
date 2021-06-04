package com.pmzhongguo.otc.entity.dataobject;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.zzextool.utils.StringUtil;

import java.io.Serializable;
import java.math.BigDecimal;

public class AccountDO implements Serializable
{
    private Integer id;

    private Integer memberId;

    private String currency;

    private BigDecimal totalBalance;

    private BigDecimal frozenBalance;

    private String createTime;

    private String modifyTime;

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

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = StringUtil.isNullOrBank(currency) ? null : currency.trim().toUpperCase();
    }

    public BigDecimal getTotalBalance()
    {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance)
    {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getFrozenBalance()
    {
        return frozenBalance;
    }

    public void setFrozenBalance(BigDecimal frozenBalance)
    {
        this.frozenBalance = frozenBalance;
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
        this.modifyTime = modifyTime;
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