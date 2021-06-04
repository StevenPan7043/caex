package com.pmzhongguo.ex.business.vo;

import com.pmzhongguo.zzextool.annotation.ExcelField;

import java.math.BigDecimal;

/**
 * @description: 导出excel
 * @date: 2019-05-31 16:43
 * @author: 十一
 */
public class CoinRechargeExcelVo
{
    private Long id;//订单ID
    @ExcelField(title = "UID", align = 2, sort = 2)
    private Integer member_id;//会员ID
    private Integer currency_id;//d_currency的id，CNY不会出现在此
    @ExcelField(title = "虚拟币", align = 2, sort = 1)
    private String currency;//d_currency的currency
    @ExcelField(title = "数量", align = 2, sort = 4)
    private BigDecimal r_amount;//充值数量
    @ExcelField(title = "充值时间", align = 2, sort = 5)
    private String r_create_time;//充值时间
    @ExcelField(title = "我方地址", align = 2, sort = 6)
    private String r_address;//充值地址（会员在平台的唯一地址）
    @ExcelField(title = "TXID哈希值", align = 2, sort = 7)
    private String r_txid;//交易ID
    private String r_confirmations; //确认数
    @ExcelField(title = "状态", align = 2, sort = 9)
    private String r_status_desc;//状态：0未确认，1已确认
    private Integer r_status;//状态：0未确认，1已确认
    private String r_from_address; //转入地址（针对无法对接钱包的币使用）
    private String r_certify_image; //转账证明图片
    private String reject_reason; //拒绝原因
    private String auditor; //处理人账号，管理frm_user表
    private String audit_time; //处理时间
    @ExcelField(title = "来源", align = 2, sort = 8)
    private String r_source; //来源

    private Integer otc_ads_id; //OTC广告ID
    private String otc_oppsite_currency; //OTC对方币种
    private BigDecimal otc_price;//OTC价格
    private BigDecimal otc_money;//OTC应收金额
    private String otc_owner_name;//OTC对方姓名

    private String token; //token

    private String r_address_; //手动录入地址

    // 附加字段
    @ExcelField(title = "会员账号", align = 2, sort = 3)
    private String m_name;// 充值人
    private Integer is_frozen; //是否冻结
    private String unfrozen_time; //解冻时间
    private Integer r_gas;    //是否转手续费
    private Integer r_guiji;    //是否归集


    public String getOtc_oppsite_currency()
    {
        return otc_oppsite_currency;
    }

    public void setOtc_oppsite_currency(String otc_oppsite_currency)
    {
        this.otc_oppsite_currency = otc_oppsite_currency;
    }

    public Integer getOtc_ads_id()
    {
        return otc_ads_id;
    }

    public void setOtc_ads_id(Integer otc_ads_id)
    {
        this.otc_ads_id = otc_ads_id;
    }

    public BigDecimal getOtc_price()
    {
        return otc_price;
    }

    public void setOtc_price(BigDecimal otc_price)
    {
        this.otc_price = otc_price;
    }

    public BigDecimal getOtc_money()
    {
        return otc_money;
    }

    public void setOtc_money(BigDecimal otc_money)
    {
        this.otc_money = otc_money;
    }

    public String getOtc_owner_name()
    {
        return otc_owner_name;
    }

    public void setOtc_owner_name(String otc_owner_name)
    {
        this.otc_owner_name = otc_owner_name;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getR_source()
    {
        return r_source;
    }

    public void setR_source(String r_source)
    {
        this.r_source = r_source;
    }

    public String getReject_reason()
    {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason)
    {
        this.reject_reason = reject_reason;
    }

    public String getAuditor()
    {
        return auditor;
    }

    public void setAuditor(String auditor)
    {
        this.auditor = auditor;
    }

    public String getAudit_time()
    {
        return audit_time;
    }

    public void setAudit_time(String audit_time)
    {
        this.audit_time = audit_time;
    }

    public String getR_from_address()
    {
        return r_from_address;
    }

    public void setR_from_address(String r_from_address)
    {
        this.r_from_address = r_from_address;
    }

    public String getR_certify_image()
    {
        return r_certify_image;
    }

    public void setR_certify_image(String r_certify_image)
    {
        this.r_certify_image = r_certify_image;
    }

    public Integer getIs_frozen()
    {
        return is_frozen;
    }

    public void setIs_frozen(Integer is_frozen)
    {
        this.is_frozen = is_frozen;
    }

    public String getUnfrozen_time()
    {
        return unfrozen_time;
    }

    public void setUnfrozen_time(String unfrozen_time)
    {
        this.unfrozen_time = unfrozen_time;
    }

    public String getR_address_()
    {
        return r_address_;
    }

    public void setR_address_(String r_address_)
    {
        this.r_address_ = r_address_;
    }

    public String getR_confirmations()
    {
        return r_confirmations;
    }

    public void setR_confirmations(String r_confirmations)
    {
        this.r_confirmations = r_confirmations;
    }

    public String getM_name()
    {
        return m_name;
    }

    public void setM_name(String m_name)
    {
        this.m_name = m_name;
    }

    public Integer getR_status()
    {
        return r_status;
    }

    public void setR_status(Integer r_status)
    {
        this.r_status = r_status;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getMember_id()
    {
        return member_id;
    }

    public void setMember_id(Integer member_id)
    {
        this.member_id = member_id;
    }

    public Integer getCurrency_id()
    {
        return currency_id;
    }

    public void setCurrency_id(Integer currency_id)
    {
        this.currency_id = currency_id;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public BigDecimal getR_amount()
    {
        return r_amount;
    }

    public void setR_amount(BigDecimal r_amount)
    {
        this.r_amount = r_amount;
    }

    public String getR_create_time()
    {
        return r_create_time;
    }

    public void setR_create_time(String r_create_time)
    {
        this.r_create_time = r_create_time;
    }

    public String getR_address()
    {
        return r_address;
    }

    public void setR_address(String r_address)
    {
        this.r_address = r_address;
    }

    public String getR_txid()
    {
        return r_txid;
    }

    public void setR_txid(String r_txid)
    {
        this.r_txid = r_txid;
    }

    public Integer getR_gas()
    {
        return r_gas;
    }

    public void setR_gas(Integer r_gas)
    {
        this.r_gas = r_gas;
    }

    public Integer getR_guiji()
    {
        return r_guiji;
    }

    public void setR_guiji(Integer r_guiji)
    {
        this.r_guiji = r_guiji;
    }

    public String getR_status_desc()
    {
        return r_status_desc;
    }

    public void setR_status_desc(String r_status_desc)
    {
        this.r_status_desc = r_status_desc;
    }
}
