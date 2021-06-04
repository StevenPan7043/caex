package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.ex.core.web.req.BaseSecretReq;


public class CoinAddressInner extends BaseSecretReq
{
    private String member_id; //member_id
    private String currency;//d_currency的currency
    private String address; //地址

    public String getMember_id()
    {
        return member_id;
    }

    public void setMember_id(String member_id)
    {
        this.member_id = member_id;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}
