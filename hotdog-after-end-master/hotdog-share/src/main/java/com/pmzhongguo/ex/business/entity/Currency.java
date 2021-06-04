package com.pmzhongguo.ex.business.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Currency implements Serializable {
    private static final long serialVersionUID = -8690075618478428183L;
    private Integer id;//
    private String currency;//货币代码，如：CNY,BTC,LTC等
    private String currency_name;//货币名称，如：人民币，比特币，莱特币等
    private String currency_img;//货币图片
    private Integer c_precision;//小数位
    private BigDecimal c_min_recharge;//最小充值额，小于此额度不认
    private BigDecimal c_min_withdraw;//最小提现额，小于此额度不让提现
    private BigDecimal c_max_withdraw;//每天最大提现额，大于此额度不让提现
    private Integer c_limit_withdraw;//每天最大提现次数，大于此次数不让提现
    private BigDecimal withdraw_fee; //提现手续费
    private Integer withdraw_fee_percent; //提现手续费是否百分比，0表示否
    private BigDecimal withdraw_fee_min; //最低提现手续费
    private BigDecimal withdraw_fee_max; //最高提现手续费
    private Integer is_coin;//是否为数字货币，目前除CNY之外，都是数字货币
    private Integer is_in_eth; //是否为以太坊架构的币
    private Integer can_recharge;//是否可充值
    private Integer can_withdraw;//是否可提现
    private Integer can_internal_transfer;//是否支持内部划转
    /**
     * 提现规则，key:value格式存储，key是校验类型，value是校验规则
     * minlen:5,表示地址长度最小为5，maxlen:18,表示地址长度最大为18,pre:0x,表示地址开头必须为0x；多个条件用“,”分隔，例如：minlen:5,maxlen:18
     * 默认不做任何校验
     */
    private String withdraw_rule;
    private Integer is_show;//是否显示(主要在个人资产界面）
    private Integer c_order;//排序
    private String c_intro_cn;//简要描述（中文）
    private String c_intro_en;//简要描述（英文）
    private String c_cannot_recharge_desc_cn;//不能充值时描述（中文）
    private String c_cannot_recharge_desc_en;//不能充值时描述（英文）
    private String c_cannot_withdraw_desc_cn;//不能提现时描述（中文）
    private String c_cannot_withdraw_desc_en;//不能提现时描述（英文）
    private Integer is_otc; //该币种是否支持法币交易,1:支持，0:不支持
    private Integer is_lock; //是否锁仓
    private BigDecimal lock_release_percent; // 释放百分比,1是百分百，0.1是百分之十
    private Integer lock_release_time; // 释放时间,单位分钟
    private String recharge_open_time; //充值开启时间
    private String withdraw_open_time; //提现开启时间

    // 钱包使用的字段
    private String ip;// 钱包服务ip
    private String port; // 服务端口
    private String user; // rpc用户
    private String pass; // rpc 密码
    private String tokenaddr; //合约
    private String decimal; // 代币精度/默认1e18
    private String sendamount; // 数量低于多少不汇总

    // 钱包配置字段
    private String guijiCron;
    private String rechargeCron;
    private String addressCron;
    private String withdrawCron;
    /**
     * 是否开启自动确认，1：开启，0：关闭，默认0
     */
    private Integer auto_confirm;
    private Integer num;
    private Integer createNum;
    private Integer status; // 执行状态, 0-未执行, 1-执行中


    /**
     * usdt_ERC20冲提币
     */
    private Integer usdtRechargeERC20;
    private Integer usdtWithdrawERC20;
    /**
     * usdt_OMNI冲提币
     */
    private Integer usdtRechargeOMNI;
    private Integer usdtWithdrawOMNI;

    /**
     * 自动提币上限，为0表示不能自动提币
     */
    private BigDecimal auto_withdraw_up_limit;

    public BigDecimal getAuto_withdraw_up_limit() {
        return auto_withdraw_up_limit;
    }

    public void setAuto_withdraw_up_limit(BigDecimal auto_withdraw_up_limit) {
        this.auto_withdraw_up_limit = auto_withdraw_up_limit;
    }

    public String getWithdraw_rule() {
        return withdraw_rule;
    }

    public void setWithdraw_rule(String withdraw_rule) {
        this.withdraw_rule = withdraw_rule;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public String getCurrency_img() {
        return currency_img;
    }

    public void setCurrency_img(String currency_img) {
        this.currency_img = currency_img;
    }

    public Integer getC_precision() {
        return c_precision;
    }

    public void setC_precision(Integer c_precision) {
        this.c_precision = c_precision;
    }

    public BigDecimal getC_min_recharge() {
        return c_min_recharge;
    }

    public void setC_min_recharge(BigDecimal c_min_recharge) {
        this.c_min_recharge = c_min_recharge;
    }

    public BigDecimal getC_min_withdraw() {
        return c_min_withdraw;
    }

    public void setC_min_withdraw(BigDecimal c_min_withdraw) {
        this.c_min_withdraw = c_min_withdraw;
    }

    public BigDecimal getC_max_withdraw() {
        return c_max_withdraw;
    }

    public void setC_max_withdraw(BigDecimal c_max_withdraw) {
        this.c_max_withdraw = c_max_withdraw;
    }

    public Integer getC_limit_withdraw() {
        return c_limit_withdraw;
    }

    public void setC_limit_withdraw(Integer c_limit_withdraw) {
        this.c_limit_withdraw = c_limit_withdraw;
    }

    public BigDecimal getWithdraw_fee() {
        return withdraw_fee;
    }

    public void setWithdraw_fee(BigDecimal withdraw_fee) {
        this.withdraw_fee = withdraw_fee;
    }

    public Integer getWithdraw_fee_percent() {
        return withdraw_fee_percent;
    }

    public void setWithdraw_fee_percent(Integer withdraw_fee_percent) {
        this.withdraw_fee_percent = withdraw_fee_percent;
    }

    public BigDecimal getWithdraw_fee_min() {
        return withdraw_fee_min;
    }

    public void setWithdraw_fee_min(BigDecimal withdraw_fee_min) {
        this.withdraw_fee_min = withdraw_fee_min;
    }

    public BigDecimal getWithdraw_fee_max() {
        return withdraw_fee_max;
    }

    public void setWithdraw_fee_max(BigDecimal withdraw_fee_max) {
        this.withdraw_fee_max = withdraw_fee_max;
    }

    public Integer getIs_coin() {
        return is_coin;
    }

    public void setIs_coin(Integer is_coin) {
        this.is_coin = is_coin;
    }

    public Integer getIs_in_eth() {
        return is_in_eth;
    }

    public void setIs_in_eth(Integer is_in_eth) {
        this.is_in_eth = is_in_eth;
    }

    public Integer getCan_recharge() {
        return can_recharge;
    }

    public void setCan_recharge(Integer can_recharge) {
        this.can_recharge = can_recharge;
    }

    public Integer getCan_withdraw() {
        return can_withdraw;
    }

    public void setCan_withdraw(Integer can_withdraw) {
        this.can_withdraw = can_withdraw;
    }

    public Integer getIs_show() {
        return is_show;
    }

    public void setIs_show(Integer is_show) {
        this.is_show = is_show;
    }

    public Integer getC_order() {
        return c_order;
    }

    public void setC_order(Integer c_order) {
        this.c_order = c_order;
    }

    public String getC_intro_cn() {
        return c_intro_cn;
    }

    public void setC_intro_cn(String c_intro_cn) {
        this.c_intro_cn = c_intro_cn;
    }

    public String getC_intro_en() {
        return c_intro_en;
    }

    public void setC_intro_en(String c_intro_en) {
        this.c_intro_en = c_intro_en;
    }

    public String getC_cannot_recharge_desc_cn() {
        return c_cannot_recharge_desc_cn;
    }

    public void setC_cannot_recharge_desc_cn(String c_cannot_recharge_desc_cn) {
        this.c_cannot_recharge_desc_cn = c_cannot_recharge_desc_cn;
    }

    public String getC_cannot_recharge_desc_en() {
        return c_cannot_recharge_desc_en;
    }

    public void setC_cannot_recharge_desc_en(String c_cannot_recharge_desc_en) {
        this.c_cannot_recharge_desc_en = c_cannot_recharge_desc_en;
    }

    public String getC_cannot_withdraw_desc_cn() {
        return c_cannot_withdraw_desc_cn;
    }

    public void setC_cannot_withdraw_desc_cn(String c_cannot_withdraw_desc_cn) {
        this.c_cannot_withdraw_desc_cn = c_cannot_withdraw_desc_cn;
    }

    public String getC_cannot_withdraw_desc_en() {
        return c_cannot_withdraw_desc_en;
    }

    public void setC_cannot_withdraw_desc_en(String c_cannot_withdraw_desc_en) {
        this.c_cannot_withdraw_desc_en = c_cannot_withdraw_desc_en;
    }

    public Integer getIs_otc() {
        return is_otc;
    }

    public void setIs_otc(Integer is_otc) {
        this.is_otc = is_otc;
    }

    public Integer getIs_lock() {
        return is_lock;
    }

    public void setIs_lock(Integer is_lock) {
        this.is_lock = is_lock;
    }

    public BigDecimal getLock_release_percent() {
        return lock_release_percent;
    }

    public void setLock_release_percent(BigDecimal lock_release_percent) {
        this.lock_release_percent = lock_release_percent;
    }

    public Integer getLock_release_time() {
        return lock_release_time;
    }

    public void setLock_release_time(Integer lock_release_time) {
        this.lock_release_time = lock_release_time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getTokenaddr() {
        return tokenaddr;
    }

    public void setTokenaddr(String tokenaddr) {
        this.tokenaddr = tokenaddr;
    }

    public String getDecimal() {
        return decimal;
    }

    public void setDecimal(String decimal) {
        this.decimal = decimal;
    }

    public String getSendamount() {
        return sendamount;
    }

    public void setSendamount(String sendamount) {
        this.sendamount = sendamount;
    }

    public String getGuijiCron() {
        return guijiCron;
    }

    public void setGuijiCron(String guijiCron) {
        this.guijiCron = guijiCron;
    }

    public String getRechargeCron() {
        return rechargeCron;
    }

    public void setRechargeCron(String rechargeCron) {
        this.rechargeCron = rechargeCron;
    }

    public String getAddressCron() {
        return addressCron;
    }

    public void setAddressCron(String addressCron) {
        this.addressCron = addressCron;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getCreateNum() {
        return createNum;
    }

    public void setCreateNum(Integer createNum) {
        this.createNum = createNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRecharge_open_time() {
        return recharge_open_time;
    }

    public void setRecharge_open_time(String recharge_open_time) {
        this.recharge_open_time = recharge_open_time;
    }

    public String getWithdraw_open_time() {
        return withdraw_open_time;
    }

    public void setWithdraw_open_time(String withdraw_open_time) {
        this.withdraw_open_time = withdraw_open_time;
    }

    public Integer getAuto_confirm() {
        return auto_confirm;
    }

    public void setAuto_confirm(Integer auto_confirm) {
        this.auto_confirm = auto_confirm;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getUsdtRechargeERC20() {
        return usdtRechargeERC20;
    }

    public void setUsdtRechargeERC20(Integer usdtRechargeERC20) {
        this.usdtRechargeERC20 = usdtRechargeERC20;
    }

    public Integer getUsdtWithdrawERC20() {
        return usdtWithdrawERC20;
    }

    public void setUsdtWithdrawERC20(Integer usdtWithdrawERC20) {
        this.usdtWithdrawERC20 = usdtWithdrawERC20;
    }

    public Integer getUsdtRechargeOMNI() {
        return usdtRechargeOMNI;
    }

    public void setUsdtRechargeOMNI(Integer usdtRechargeOMNI) {
        this.usdtRechargeOMNI = usdtRechargeOMNI;
    }

    public Integer getUsdtWithdrawOMNI() {
        return usdtWithdrawOMNI;
    }

    public void setUsdtWithdrawOMNI(Integer usdtWithdrawOMNI) {
        this.usdtWithdrawOMNI = usdtWithdrawOMNI;
    }

    public Integer getCan_internal_transfer() {
        return can_internal_transfer;
    }

    public void setCan_internal_transfer(Integer can_internal_transfer) {
        this.can_internal_transfer = can_internal_transfer;
    }

    public String getWithdrawCron() {
        return withdrawCron;
    }

    public void setWithdrawCron(String withdrawCron) {
        this.withdrawCron = withdrawCron;
    }
}
