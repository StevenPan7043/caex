package com.pmzhongguo.ex.business.entity;

import com.lmax.disruptor.EventFactory;

import java.io.Serializable;
import java.math.BigDecimal;

public class Order implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Long id;//
    private Integer token_id;//api_token表的ID，从网站或APP来的订单填0，其他填token_id，便于用户做统计
    private Integer member_id;//会员ID
    private Integer pair_id;//交易对ID
    private String base_currency;//基础货币，如BTC
    private String quote_currency;//计价货币，如CNY
    private String o_no;//订单号，API访问时，由会员提供，以方便客户查询自己订单状态，对同一会员，订单号不允许重复
    private String o_type;//订单类型：buy、sell，即卖单、买单
    private String o_price_type;//价格类型：limit、market，即限价单、市价单
    private BigDecimal price;//价格，对限价单，表示会员指定的价格，对于市价单，默认为0
    private BigDecimal volume;//数量，对限价单，表示会员指定的数量，对于市价买单，表示买多少钱(计价货币)，市价卖单表示卖多少币(基础货币)
    private BigDecimal frozen;//冻结，对限价卖单，同数量一致，对限价买单，等于价格*数量，对市价单，等于volume
    private BigDecimal done_volume;//已成交数量，单位和含义，同volume
    private BigDecimal unfrozen;//已解冻量，对限价卖单，同已成交数量，对限价买单，等于『成交』价格*数量。对市价单等于已成交数量
    private String source;//订单来源：API、WEB、Wap、Android、iOS
    private String create_time;//创建时间
    private Integer create_time_unix;//创建时间戳
    private String done_time;//成交时间，多次成交取最后一次时间
    private String cancel_time;//撤销时间
    private String oper_ip;//下单IP
    private String o_status;//watting,partial-done,done,partial-canceled,canceled

    // 附加字段
    private String table_name; //表名
    private BigDecimal cur_done_volume;// 本次成交数量
    private BigDecimal cur_unfrozen;// 本次解冻数量
    private String m_name; //会员的账号
    private Double tradeCommission; // 交易手续费比例

    private String frozenCurrency;
    public static final EventFactory<Order> FACTORY = new EventFactory<Order>()
    {
        @Override
        public Order newInstance()
        {
            return new Order();
        }
    };

    @Override
    public String toString()
    {
        return "Order [id=" + id + ", token_id=" + token_id + ", member_id=" + member_id + ", pair_id=" + pair_id
                + ", base_currency=" + base_currency + ", quote_currency=" + quote_currency + ", o_no=" + o_no
                + ", o_type=" + o_type + ", o_price_type=" + o_price_type + ", price=" + price + ", volume=" + volume
                + ", frozen=" + frozen + ", done_volume=" + done_volume + ", unfrozen=" + unfrozen + ", source="
                + source + ", create_time=" + create_time + ", create_time_unix=" + create_time_unix + ", done_time="
                + done_time + ", cancel_time=" + cancel_time + ", oper_ip=" + oper_ip + ", o_status=" + o_status
                + ", table_name=" + table_name + ", cur_done_volume=" + cur_done_volume + ", cur_unfrozen="
                + cur_unfrozen + ", m_name=" + m_name + ", tradeCommission=" + tradeCommission + "]";
    }

    public BigDecimal getCur_unfrozen()
    {
        return cur_unfrozen;
    }


    public void setCur_unfrozen(BigDecimal cur_unfrozen)
    {
        this.cur_unfrozen = cur_unfrozen;
    }


    public String getM_name()
    {
        return m_name;
    }


    public void setM_name(String m_name)
    {
        this.m_name = m_name;
    }

    public Double getTradeCommission()
    {
        return tradeCommission;
    }

    public void setTradeCommission(Double tradeCommission)
    {
        this.tradeCommission = tradeCommission;
    }

    public Integer getCreate_time_unix()
    {
        return create_time_unix;
    }

    public void setCreate_time_unix(Integer create_time_unix)
    {
        this.create_time_unix = create_time_unix;
    }

    public BigDecimal getCur_done_volume()
    {
        return cur_done_volume;
    }

    public void setCur_done_volume(BigDecimal cur_done_volume)
    {
        this.cur_done_volume = cur_done_volume;
    }

    public Integer getToken_id()
    {
        return token_id;
    }

    public void setToken_id(Integer token_id)
    {
        this.token_id = token_id;
    }

    public String getTable_name()
    {
        return table_name;
    }

    public void setTable_name(String table_name)
    {
        this.table_name = table_name;
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

    public Integer getPair_id()
    {
        return pair_id;
    }

    public void setPair_id(Integer pair_id)
    {
        this.pair_id = pair_id;
    }

    public String getBase_currency()
    {
        return base_currency;
    }

    public void setBase_currency(String base_currency)
    {
        this.base_currency = base_currency;
    }

    public String getQuote_currency()
    {
        return quote_currency;
    }

    public void setQuote_currency(String quote_currency)
    {
        this.quote_currency = quote_currency;
    }

    public String getO_no()
    {
        return o_no;
    }

    public void setO_no(String o_no)
    {
        this.o_no = o_no;
    }

    public String getO_type()
    {
        return o_type;
    }

    public void setO_type(String o_type)
    {
        this.o_type = o_type;
    }

    public String getO_price_type()
    {
        return o_price_type;
    }

    public void setO_price_type(String o_price_type)
    {
        this.o_price_type = o_price_type;
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

    public BigDecimal getDone_volume()
    {
        return done_volume;
    }

    public void setDone_volume(BigDecimal done_volume)
    {
        this.done_volume = done_volume;
    }

    public BigDecimal getUnfrozen()
    {
        return unfrozen;
    }

    public void setUnfrozen(BigDecimal unfrozen)
    {
        this.unfrozen = unfrozen;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getDone_time()
    {
        return done_time;
    }

    public void setDone_time(String done_time)
    {
        this.done_time = done_time;
    }

    public String getCancel_time()
    {
        return cancel_time;
    }

    public void setCancel_time(String cancel_time)
    {
        this.cancel_time = cancel_time;
    }

    public String getOper_ip()
    {
        return oper_ip;
    }

    public void setOper_ip(String oper_ip)
    {
        this.oper_ip = oper_ip;
    }

    public String getO_status()
    {
        return o_status;
    }

    public void setO_status(String o_status)
    {
        this.o_status = o_status;
    }


    public String getFrozenCurrency() {
        return frozenCurrency;
    }

    public void setFrozenCurrency(String frozenCurrency) {
        this.frozenCurrency = frozenCurrency;
    }
}
