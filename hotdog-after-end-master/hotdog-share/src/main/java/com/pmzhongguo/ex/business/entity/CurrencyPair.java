package com.pmzhongguo.ex.business.entity;

import java.io.Serializable;
import java.math.BigDecimal;


public class CurrencyPair implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -2987496900053699835L;
    private Integer id;//
    private String dsp_name;//展示名称，如BTC/CNY
    private String base_currency;//基础货币，如BTC
    private String quote_currency;//计价货币，如CNY
    private String key_name; //交易对名称，用于前后台传参，如BTCCNY
    private Integer price_precision;//价格小数位，如人民币，最多到分，即2位
    private Integer volume_precision;//买卖数量小数位，如比特币，最小可交易0.0001个，即4位
    private BigDecimal taker_fee;//吃单手续费
    private BigDecimal maker_fee;//挂单手续费
    private String open_time;//开启交易时间
    private BigDecimal min_price; //最低价格，即开盘时限价，0表示不限制
    private BigDecimal max_price; //最高价格，即开盘时限价，0表示不限制
    private Integer p_status;//状态：1：正常，2：未启用
    private Integer can_buy; //是否可买入，1表示是，0表示否
    private Integer can_sell; //是否可卖出，1表示是，0表示否
    private String stop_desc; //暂停买卖说明，中英文竖线分割
    private BigDecimal p_depth;//深度步长
    private Integer p_order;//排序，越大越靠后
    private Integer area_id; //所在交易区
    private Integer is_area_first; //是否该交易区拍第一
    private BigDecimal min_buy_volume; //最小买入量（0表示不限制）
    private BigDecimal min_buy_amount; //最小买入金额（0表示不限制）
    private BigDecimal min_sell_volume; //最小卖出量（0表示不限制）
    private BigDecimal fraud_magnitude; //刷量数量级，系统会在本数量和该数量除以10的范围内刷量

    private Integer is_flash_sale_open;//是否开启预售抢购
    private String flash_sale_open_time;//抢购开启时间
    private String flash_sale_close_time;//抢购结束时间
    private BigDecimal max_buy_volume; //最大买入量（0表示不限制）
    private BigDecimal fixed_buy_price; //固定买入价（0表示不限制）

    private String p_status_name; //状态名称
    private String quote_currency_name;
    private String base_currency_name;

    private BigDecimal ups_downs_limit; //最大涨跌幅限制为多少，默认为0
    private Integer is_ups_downs_limit; //是否开启涨跌幅限制，1为限制，0为不限制
    private BigDecimal closePrice;//收盘价
    private BigDecimal highPrice;//当天最高价
    private BigDecimal lowPrice;//当天最低价

    public BigDecimal getFraud_magnitude()
    {
        return fraud_magnitude;
    }

    public void setFraud_magnitude(BigDecimal fraud_magnitude)
    {
        this.fraud_magnitude = fraud_magnitude;
    }

    public BigDecimal getFixed_buy_price()
    {
        return fixed_buy_price;
    }

    public void setFixed_buy_price(BigDecimal fixed_buy_price)
    {
        this.fixed_buy_price = fixed_buy_price;
    }

    public BigDecimal getMin_buy_amount()
    {
        return min_buy_amount;
    }

    public void setMin_buy_amount(BigDecimal min_buy_amount)
    {
        this.min_buy_amount = min_buy_amount;
    }

    public BigDecimal getMin_sell_volume()
    {
        return min_sell_volume;
    }

    public void setMin_sell_volume(BigDecimal min_sell_volume)
    {
        this.min_sell_volume = min_sell_volume;
    }

    public BigDecimal getMin_buy_volume()
    {
        return min_buy_volume;
    }

    public void setMin_buy_volume(BigDecimal min_buy_volume)
    {
        this.min_buy_volume = min_buy_volume;
    }

    public String getQuote_currency_name()
    {
        return quote_currency_name;
    }

    public void setQuote_currency_name(String quote_currency_name)
    {
        this.quote_currency_name = quote_currency_name;
    }

    public String getBase_currency_name()
    {
        return base_currency_name;
    }

    public void setBase_currency_name(String base_currency_name)
    {
        this.base_currency_name = base_currency_name;
    }

    public Integer getCan_buy()
    {
        return can_buy;
    }

    public void setCan_buy(Integer can_buy)
    {
        this.can_buy = can_buy;
    }

    public Integer getCan_sell()
    {
        return can_sell;
    }

    public void setCan_sell(Integer can_sell)
    {
        this.can_sell = can_sell;
    }

    public String getStop_desc()
    {
        return stop_desc;
    }

    public void setStop_desc(String stop_desc)
    {
        this.stop_desc = stop_desc;
    }

    public Integer getArea_id()
    {
        return area_id;
    }

    public void setArea_id(Integer area_id)
    {
        this.area_id = area_id;
    }

    public Integer getIs_area_first()
    {
        return is_area_first;
    }

    public void setIs_area_first(Integer is_area_first)
    {
        this.is_area_first = is_area_first;
    }

    public BigDecimal getMin_price()
    {
        return min_price;
    }

    public void setMin_price(BigDecimal min_price)
    {
        this.min_price = min_price;
    }

    public BigDecimal getMax_price()
    {
        return max_price;
    }

    public void setMax_price(BigDecimal max_price)
    {
        this.max_price = max_price;
    }

    public String getKey_name()
    {
        return key_name;
    }

    public void setKey_name(String key_name)
    {
        this.key_name = key_name;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDsp_name()
    {
        return dsp_name;
    }

    public void setDsp_name(String dsp_name)
    {
        this.dsp_name = dsp_name;
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

    public Integer getPrice_precision()
    {
        return price_precision;
    }

    public void setPrice_precision(Integer price_precision)
    {
        this.price_precision = price_precision;
    }

    public Integer getVolume_precision()
    {
        return volume_precision;
    }

    public void setVolume_precision(Integer volume_precision)
    {
        this.volume_precision = volume_precision;
    }

    public BigDecimal getTaker_fee()
    {
        return taker_fee;
    }

    public void setTaker_fee(BigDecimal taker_fee)
    {
        this.taker_fee = taker_fee;
    }

    public BigDecimal getMaker_fee()
    {
        return maker_fee;
    }

    public void setMaker_fee(BigDecimal maker_fee)
    {
        this.maker_fee = maker_fee;
    }

    public String getOpen_time()
    {
        return open_time;
    }

    public void setOpen_time(String open_time)
    {
        this.open_time = open_time;
    }

    public Integer getP_status()
    {
        return p_status;
    }

    public void setP_status(Integer p_status)
    {
        this.p_status = p_status;
    }

    public BigDecimal getP_depth()
    {
        return p_depth;
    }

    public void setP_depth(BigDecimal p_depth)
    {
        this.p_depth = p_depth;
    }

    public Integer getP_order()
    {
        return p_order;
    }

    public void setP_order(Integer p_order)
    {
        this.p_order = p_order;
    }

    public String getP_status_name()
    {
        return p_status_name;
    }

    public void setP_status_name(String p_status_name)
    {
        this.p_status_name = p_status_name;
    }

    public Integer getIs_flash_sale_open() {
        return is_flash_sale_open;
    }

    public void setIs_flash_sale_open(Integer is_flash_sale_open) {
        this.is_flash_sale_open = is_flash_sale_open;
    }

    public String getFlash_sale_open_time() {
        return flash_sale_open_time;
    }

    public void setFlash_sale_open_time(String flash_sale_open_time) {
        this.flash_sale_open_time = flash_sale_open_time;
    }

    public String getFlash_sale_close_time() {
        return flash_sale_close_time;
    }

    public void setFlash_sale_close_time(String flash_sale_close_time) {
        this.flash_sale_close_time = flash_sale_close_time;
    }

    public BigDecimal getMax_buy_volume() {
        return max_buy_volume;
    }

    public void setMax_buy_volume(BigDecimal max_buy_volume) {
        this.max_buy_volume = max_buy_volume;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public BigDecimal getUps_downs_limit() {
        return ups_downs_limit;
    }

    public void setUps_downs_limit(BigDecimal ups_downs_limit) {
        this.ups_downs_limit = ups_downs_limit;
    }

    public Integer getIs_ups_downs_limit() {
        return is_ups_downs_limit;
    }

    public void setIs_ups_downs_limit(Integer is_ups_downs_limit) {
        this.is_ups_downs_limit = is_ups_downs_limit;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    @Override
    public String toString() {
        return "CurrencyPair{" +
                "id=" + id +
                ", dsp_name='" + dsp_name + '\'' +
                ", base_currency='" + base_currency + '\'' +
                ", quote_currency='" + quote_currency + '\'' +
                ", key_name='" + key_name + '\'' +
                ", price_precision=" + price_precision +
                ", volume_precision=" + volume_precision +
                ", taker_fee=" + taker_fee +
                ", maker_fee=" + maker_fee +
                ", open_time='" + open_time + '\'' +
                ", min_price=" + min_price +
                ", max_price=" + max_price +
                ", p_status=" + p_status +
                ", can_buy=" + can_buy +
                ", can_sell=" + can_sell +
                ", stop_desc='" + stop_desc + '\'' +
                ", p_depth=" + p_depth +
                ", p_order=" + p_order +
                ", area_id=" + area_id +
                ", is_area_first=" + is_area_first +
                ", min_buy_volume=" + min_buy_volume +
                ", min_buy_amount=" + min_buy_amount +
                ", min_sell_volume=" + min_sell_volume +
                ", fraud_magnitude=" + fraud_magnitude +
                ", is_flash_sale_open=" + is_flash_sale_open +
                ", flash_sale_open_time='" + flash_sale_open_time + '\'' +
                ", flash_sale_close_time='" + flash_sale_close_time + '\'' +
                ", max_buy_volume=" + max_buy_volume +
                ", fixed_buy_price=" + fixed_buy_price +
                ", p_status_name='" + p_status_name + '\'' +
                ", quote_currency_name='" + quote_currency_name + '\'' +
                ", base_currency_name='" + base_currency_name + '\'' +
                '}';
    }
}
