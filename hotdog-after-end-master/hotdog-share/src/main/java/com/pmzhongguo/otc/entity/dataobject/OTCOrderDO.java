package com.pmzhongguo.otc.entity.dataobject;

import com.pmzhongguo.zzextool.utils.JsonUtil;

import java.io.Serializable;
import java.math.BigDecimal;

public class OTCOrderDO implements Serializable
{
    //订单ID
    private Integer id;

    //商家ID
    private Integer memberId;

    //会员名
    private String mName;

    //基础货币，如ZC
    private String baseCurrency;

    //计价货币，如CNY
    private String quoteCurrency;

    //订单号，订单号不允许重复
    private String number;

    //订单类型：1-buy、2-sell，即卖单、买单
    private Integer type;

    //价格类型：1-limit、2-market，即限价单、市价单
    private Integer priceType;

    //价格，对限价单，表示会员指定的价格，对于市价单，默认为0
    private BigDecimal price;

    //数量，对限价单，表示会员指定的数量，对于市价买单，表示买多少钱(计价货币)，市价卖单表示卖多少币(基础货币)
    private BigDecimal volume;

    //冻结，对限价卖单，同数量一致，对限价买单，等于价格*数量，对市价单，等于volume
    private BigDecimal frozen;

    //可交易数量，同volume
    private BigDecimal remainVolume;

    //最小计价货币交易额。限价单创建时直接设置最小计价货币交易金额
    private BigDecimal minQuote;

    //最大计价货币交易额。限价单创建时直接设置最大计价货币交易金额
    private BigDecimal maxQuote;

    //支付限制时间，单位分钟
    private Integer paymentTime;

    //最大计价货币交易额。限价卖单：remain_volume*price 限价买单：remain_volume
    private BigDecimal remain;

    //锁定数量，等待确认成交的数量，单位和含义，同volume
    private BigDecimal lockVolume;

    //锁定金额，对限价卖单，同已锁定数量，对限价买单，等于『成交』价格*数量。对市价单等于已成交数量
    private BigDecimal lockAmount;

    //已成交数量，单位和含义，同volume
    private BigDecimal doneVolume;

    //撤销数量，单位和含义，同volume
    private BigDecimal cancelVolume;

    //已解冻量，对限价卖单，同已成交数量，对限价买单，等于『成交』价格*数量。对市价单等于已成交数量
    private BigDecimal unfrozen;

    //创建时间
    private String createTime;

    //修改时间
    private String modifyTime;

    //下单IP
    private String operIp;

    //订单状态 0-watting, 1-trading, 2-done, 3-partial-canceled, 4-canceled
    private Integer status;

    //当前操作数量
    private BigDecimal curVolume;

    //当前操作的交易额
    private BigDecimal curAmount;

    //订单有效时间，单位天
    private Integer effectiveTime;

    //订单留言
    private String remark;

    //收款账号，有多个就以逗号分隔，对应o_account_info表的id，只有卖单才有值
    private String acountId;

    //是否广告
    private Integer isAds;

    //1-unchange 固定价格、2-float，浮动价格
    private Integer priceChangeType;

    //下单时的原始价格
    private BigDecimal origPrice;

    //下单时的基础汇率价格,计算浮动价格用
    private BigDecimal baseRate;

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

    public String getBaseCurrency()
    {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency)
    {
        this.baseCurrency = baseCurrency == null ? null : baseCurrency.trim().toUpperCase();
    }

    public String getQuoteCurrency()
    {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency)
    {
        this.quoteCurrency = quoteCurrency == null ? null : quoteCurrency.trim().toUpperCase();
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getPriceType()
    {
        return priceType;
    }

    public void setPriceType(Integer priceType)
    {
        this.priceType = priceType;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getVolume()
    {
        return volume;
    }

    public void setVolume(BigDecimal volume)
    {
        this.volume = volume;
    }

    public BigDecimal getFrozen()
    {
        return frozen;
    }

    public void setFrozen(BigDecimal frozen)
    {
        this.frozen = frozen;
    }

    public BigDecimal getRemainVolume()
    {
        return remainVolume;
    }

    public void setRemainVolume(BigDecimal remainVolume)
    {
        this.remainVolume = remainVolume;
    }

    public BigDecimal getMinQuote()
    {
        return minQuote;
    }

    public void setMinQuote(BigDecimal minQuote)
    {
        this.minQuote = minQuote;
    }

    public BigDecimal getMaxQuote()
    {
        return maxQuote;
    }

    public void setMaxQuote(BigDecimal maxQuote)
    {
        this.maxQuote = maxQuote;
    }

    public Integer getPaymentTime()
    {
        return paymentTime;
    }

    public void setPaymentTime(Integer paymentTime)
    {
        this.paymentTime = paymentTime;
    }

    public BigDecimal getRemain()
    {
        return remain;
    }

    public void setRemain(BigDecimal remain)
    {
        this.remain = remain;
    }

    public BigDecimal getLockVolume()
    {
        return lockVolume;
    }

    public void setLockVolume(BigDecimal lockVolume)
    {
        this.lockVolume = lockVolume;
    }

    public BigDecimal getLockAmount()
    {
        return lockAmount;
    }

    public void setLockAmount(BigDecimal lockAmount)
    {
        this.lockAmount = lockAmount;
    }

    public BigDecimal getDoneVolume()
    {
        return doneVolume;
    }

    public void setDoneVolume(BigDecimal doneVolume)
    {
        this.doneVolume = doneVolume;
    }

    public BigDecimal getCancelVolume()
    {
        return cancelVolume;
    }

    public void setCancelVolume(BigDecimal cancelVolume)
    {
        this.cancelVolume = cancelVolume;
    }

    public BigDecimal getUnfrozen()
    {
        return unfrozen;
    }

    public void setUnfrozen(BigDecimal unfrozen)
    {
        this.unfrozen = unfrozen;
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

    public String getOperIp()
    {
        return operIp;
    }

    public void setOperIp(String operIp)
    {
        this.operIp = operIp == null ? null : operIp.trim();
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public BigDecimal getCurVolume()
    {
        return curVolume;
    }

    public void setCurVolume(BigDecimal curVolume)
    {
        this.curVolume = curVolume;
    }

    public BigDecimal getCurAmount()
    {
        return curAmount;
    }

    public void setCurAmount(BigDecimal curAmount)
    {
        this.curAmount = curAmount;
    }

    public Integer getEffectiveTime()
    {
        return effectiveTime;
    }

    public void setEffectiveTime(Integer effectiveTime)
    {
        this.effectiveTime = effectiveTime;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getAcountId()
    {
        return acountId;
    }

    public void setAcountId(String acountId)
    {
        this.acountId = acountId;
    }

    public String getmName()
    {
        return mName;
    }

    public void setmName(String mName)
    {
        this.mName = mName;
    }

    public Integer getIsAds()
    {
        return isAds;
    }

    public void setIsAds(Integer isAds)
    {
        this.isAds = isAds;
    }

    public Integer getPriceChangeType()
    {
        return priceChangeType;
    }

    public void setPriceChangeType(Integer priceChangeType)
    {
        this.priceChangeType = priceChangeType;
    }

    public BigDecimal getOrigPrice()
    {
        return origPrice;
    }

    public void setOrigPrice(BigDecimal origPrice)
    {
        this.origPrice = origPrice;
    }

    public BigDecimal getBaseRate()
    {
        return baseRate;
    }

    public void setBaseRate(BigDecimal baseRate)
    {
        this.baseRate = baseRate;
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