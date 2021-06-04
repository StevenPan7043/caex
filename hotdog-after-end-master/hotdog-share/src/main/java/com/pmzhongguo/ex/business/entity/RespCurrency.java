package com.pmzhongguo.ex.business.entity;


import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

public class RespCurrency
{

    @ApiModelProperty(value = "虚拟币ID")
    private Integer id;//
    @ApiModelProperty(value = "虚拟币代码")
    private String currency;// 货币代码，如：CNY,BTC,LTC等
    @ApiModelProperty(value = "虚拟币名称")
    private String currency_name;// 货币名称，如：人民币，比特币，莱特币等
    @ApiModelProperty(value = "小数位数")
    private Integer c_precision;// 小数位
    @ApiModelProperty(value = "最小充值额度")
    private BigDecimal c_min_recharge;// 最小充值额，小于此额度不认
    @ApiModelProperty(value = "最小提现额")
    private BigDecimal c_min_withdraw;// 最小提现额，小于此额度不让提现
    @ApiModelProperty(value = "提现手续费，分固定和百分比，详见下面的withdraw_fee_percent")
    private BigDecimal withdraw_fee; // 提现手续费
    @ApiModelProperty(value = "提现手续费是否按百分比，0表示固定手续费，即固定为withdraw_fee，1表示提现手续费按百分比，即提现额的百分之withdraw_fee")
    private Integer withdraw_fee_percent; // 提现手续费是否百分比，0表示否
    @ApiModelProperty(value = "最低提现手续费，在百分比为1时有用")
    private BigDecimal withdraw_fee_min; // 最低提现手续费
    @ApiModelProperty(value = "最高提现手续费，在百分比为1时有用，为0时，表示按百分比上不封顶")
    private BigDecimal withdraw_fee_max; // 最高提现手续费
    @ApiModelProperty(value = "是否为以太坊架构的币")
    private Integer is_in_eth; // 是否为以太坊架构的币
    @ApiModelProperty(value = "是否可充值")
    private Integer can_recharge;// 是否可充值
    @ApiModelProperty(value = "是否可提现")
    private Integer can_withdraw;// 是否可提现
    @ApiModelProperty(value = "排序")
    private Integer c_order;// 排序
    @ApiModelProperty(value = "中文简要描述")
    private String c_intro_cn;// 简要描述（中文）
    @ApiModelProperty(value = "英文简要描述")
    private String c_intro_en;// 简要描述（英文）
    @ApiModelProperty(value = "不能充值时中文描述")
    private String c_cannot_recharge_desc_cn;// 不能充值时描述（中文）
    @ApiModelProperty(value = "不能充值时英文描述")
    private String c_cannot_recharge_desc_en;// 不能充值时描述（英文）
    @ApiModelProperty(value = "不能提现时中文描述")
    private String c_cannot_withdraw_desc_cn;// 不能提现时描述（中文）
    @ApiModelProperty(value = "不能提现时英文描述")
    private String c_cannot_withdraw_desc_en;// 不能提现时描述（英文）

    public Integer getIs_in_eth()
    {
        return is_in_eth;
    }

    public void setIs_in_eth(Integer is_in_eth)
    {
        this.is_in_eth = is_in_eth;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public String getCurrency_name()
    {
        return currency_name;
    }

    public void setCurrency_name(String currency_name)
    {
        this.currency_name = currency_name;
    }

    public Integer getC_precision()
    {
        return c_precision;
    }

    public void setC_precision(Integer c_precision)
    {
        this.c_precision = c_precision;
    }

    public BigDecimal getC_min_recharge()
    {
        return c_min_recharge;
    }

    public void setC_min_recharge(BigDecimal c_min_recharge)
    {
        this.c_min_recharge = c_min_recharge;
    }

    public BigDecimal getC_min_withdraw()
    {
        return c_min_withdraw;
    }

    public void setC_min_withdraw(BigDecimal c_min_withdraw)
    {
        this.c_min_withdraw = c_min_withdraw;
    }

    public BigDecimal getWithdraw_fee()
    {
        return withdraw_fee;
    }

    public void setWithdraw_fee(BigDecimal withdraw_fee)
    {
        this.withdraw_fee = withdraw_fee;
    }

    public Integer getWithdraw_fee_percent()
    {
        return withdraw_fee_percent;
    }

    public void setWithdraw_fee_percent(Integer withdraw_fee_percent)
    {
        this.withdraw_fee_percent = withdraw_fee_percent;
    }

    public BigDecimal getWithdraw_fee_min()
    {
        return withdraw_fee_min;
    }

    public void setWithdraw_fee_min(BigDecimal withdraw_fee_min)
    {
        this.withdraw_fee_min = withdraw_fee_min;
    }

    public BigDecimal getWithdraw_fee_max()
    {
        return withdraw_fee_max;
    }

    public void setWithdraw_fee_max(BigDecimal withdraw_fee_max)
    {
        this.withdraw_fee_max = withdraw_fee_max;
    }

    public Integer getCan_recharge()
    {
        return can_recharge;
    }

    public void setCan_recharge(Integer can_recharge)
    {
        this.can_recharge = can_recharge;
    }

    public Integer getCan_withdraw()
    {
        return can_withdraw;
    }

    public void setCan_withdraw(Integer can_withdraw)
    {
        this.can_withdraw = can_withdraw;
    }

    public Integer getC_order()
    {
        return c_order;
    }

    public void setC_order(Integer c_order)
    {
        this.c_order = c_order;
    }

    public String getC_intro_cn()
    {
        return c_intro_cn;
    }

    public void setC_intro_cn(String c_intro_cn)
    {
        this.c_intro_cn = c_intro_cn;
    }

    public String getC_intro_en()
    {
        return c_intro_en;
    }

    public void setC_intro_en(String c_intro_en)
    {
        this.c_intro_en = c_intro_en;
    }

    public String getC_cannot_recharge_desc_cn()
    {
        return c_cannot_recharge_desc_cn;
    }

    public void setC_cannot_recharge_desc_cn(String c_cannot_recharge_desc_cn)
    {
        this.c_cannot_recharge_desc_cn = c_cannot_recharge_desc_cn;
    }

    public String getC_cannot_recharge_desc_en()
    {
        return c_cannot_recharge_desc_en;
    }

    public void setC_cannot_recharge_desc_en(String c_cannot_recharge_desc_en)
    {
        this.c_cannot_recharge_desc_en = c_cannot_recharge_desc_en;
    }

    public String getC_cannot_withdraw_desc_cn()
    {
        return c_cannot_withdraw_desc_cn;
    }

    public void setC_cannot_withdraw_desc_cn(String c_cannot_withdraw_desc_cn)
    {
        this.c_cannot_withdraw_desc_cn = c_cannot_withdraw_desc_cn;
    }

    public String getC_cannot_withdraw_desc_en()
    {
        return c_cannot_withdraw_desc_en;
    }

    public void setC_cannot_withdraw_desc_en(String c_cannot_withdraw_desc_en)
    {
        this.c_cannot_withdraw_desc_en = c_cannot_withdraw_desc_en;
    }
}
