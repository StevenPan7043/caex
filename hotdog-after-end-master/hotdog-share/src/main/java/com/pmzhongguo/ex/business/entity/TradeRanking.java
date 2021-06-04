package com.pmzhongguo.ex.business.entity;

/**
 * 交易排名列表对象
 *
 * @author Administrator
 */
public class TradeRanking
{

    private Integer no;
    private String uid;
    private String num;
    private String mname;
    private String account;
    private String reward;// 奖励

    public Integer getNo()
    {
        return no;
    }

    public void setNo(Integer no)
    {
        this.no = no;
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getNum()
    {
        return num;
    }

    public void setNum(String num)
    {
        this.num = num;
    }

    public String getMname()
    {
        return mname;
    }

    public void setMname(String mname)
    {
        this.mname = mname;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getReward()
    {
        return reward;
    }

    public void setReward(String reward)
    {
        this.reward = reward;
    }

    @Override
    public String toString()
    {
        return "TradeRanking [no=" + no + ", uid=" + uid + ", num=" + num + ", mname=" + mname + ", account=" + account
                + ", reward=" + reward + "]";
    }

}
