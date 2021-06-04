package com.pmzhongguo.ex.business.entity;

/**
 * @description: 币种资料
 * @date: 2018-11-10 11:30
 * @author: 十一
 */
public class CurrencyIntroduce
{

    private Integer id;//
    private Integer d_currency_id;//关联币种id
    private String name;//币种名称
    private String currency;//货币代码
    private String site;//官网
    private String introduce_cn;//币种介绍中文
    private String introduce_en;//币种介绍英文
    private String introduce_ko;//币种介绍韩文
    private String introduce_jp;//币种介绍日文
    private String release_amount;//发行总量
    private String release_time;//发行时间
    private Integer is_show;//是否删除: 1 显示; 0 不显示
    private String create_time;//创建时间
    private String update_time; // 更新时间
    private String release_price; // 发行价格
    private String circulation_amount; // 流通总量
    private String block_search; // 区块查询
    private String white_paper; // 白皮书


    public String getIntroduce_ko()
    {
        return introduce_ko;
    }

    public void setIntroduce_ko(String introduce_ko)
    {
        this.introduce_ko = introduce_ko;
    }

    public String getIntroduce_jp()
    {
        return introduce_jp;
    }

    public void setIntroduce_jp(String introduce_jp)
    {
        this.introduce_jp = introduce_jp;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getD_currency_id()
    {
        return d_currency_id;
    }

    public void setD_currency_id(Integer d_currency_id)
    {
        this.d_currency_id = d_currency_id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSite()
    {
        return site;
    }

    public void setSite(String site)
    {
        this.site = site;
    }

    public String getIntroduce_cn()
    {
        return introduce_cn;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public void setIntroduce_cn(String introduce_cn)
    {
        this.introduce_cn = introduce_cn;
    }

    public String getIntroduce_en()
    {
        return introduce_en;
    }

    public void setIntroduce_en(String introduce_en)
    {
        this.introduce_en = introduce_en;
    }

    public String getRelease_amount()
    {
        return release_amount;
    }

    public void setRelease_amount(String release_amount)
    {
        this.release_amount = release_amount;
    }

    public String getRelease_time()
    {
        return release_time;
    }

    public void setRelease_time(String release_time)
    {
        this.release_time = release_time;
    }

    public Integer getIs_show()
    {
        return is_show;
    }

    public void setIs_show(Integer is_show)
    {
        this.is_show = is_show;
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

    public String getRelease_price()
    {
        return release_price;
    }

    public void setRelease_price(String release_price)
    {
        this.release_price = release_price;
    }

    public String getCirculation_amount()
    {
        return circulation_amount;
    }

    public void setCirculation_amount(String circulation_amount)
    {
        this.circulation_amount = circulation_amount;
    }

    public String getBlock_search()
    {
        return block_search;
    }

    public void setBlock_search(String block_search)
    {
        this.block_search = block_search;
    }

    public String getWhite_paper()
    {
        return white_paper;
    }

    public void setWhite_paper(String white_paper)
    {
        this.white_paper = white_paper;
    }
}
