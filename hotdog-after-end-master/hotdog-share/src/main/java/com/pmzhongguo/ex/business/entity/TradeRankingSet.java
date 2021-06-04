package com.pmzhongguo.ex.business.entity;

/**
 * 交易排名设置数据对象
 *
 * @author Administrator
 */
public class TradeRankingSet
{

    private Integer dcpid;
    private String dspname;
    private String keyname;
    private Integer ttrsid;
    private String opentime;
    private String startdate;
    private String enddate;
    private Integer isopen;
    private Integer isSendReward;

    public Integer getDcpid()
    {
        return dcpid;
    }

    public void setDcpid(Integer dcpid)
    {
        this.dcpid = dcpid;
    }

    public String getDspname()
    {
        return dspname;
    }

    public void setDspname(String dspname)
    {
        this.dspname = dspname;
    }

    public String getKeyname()
    {
        return keyname;
    }

    public void setKeyname(String keyname)
    {
        this.keyname = keyname;
    }

    public Integer getTtrsid()
    {
        return ttrsid;
    }

    public void setTtrsid(Integer ttrsid)
    {
        this.ttrsid = ttrsid;
    }

    public String getOpentime()
    {
        return opentime;
    }

    public void setOpentime(String opentime)
    {
        this.opentime = opentime;
    }

    public String getStartdate()
    {
        return startdate;
    }

    public void setStartdate(String startdate)
    {
        this.startdate = startdate;
    }

    public String getEnddate()
    {
        return enddate;
    }

    public void setEnddate(String enddate)
    {
        this.enddate = enddate;
    }

    public Integer getIsopen()
    {
        return isopen;
    }

    public void setIsopen(Integer isopen)
    {
        this.isopen = isopen;
    }

    public Integer getIsSendReward()
    {
        return isSendReward;
    }

    public void setIsSendReward(Integer isSendReward)
    {
        this.isSendReward = isSendReward;
    }

}
