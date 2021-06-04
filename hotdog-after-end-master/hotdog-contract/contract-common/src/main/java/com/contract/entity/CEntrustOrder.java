package com.contract.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.contract.common.DateUtil;
import com.contract.service.Page;

public class CEntrustOrder extends Page{
    /**  
	 * 
	 */
	private static final long serialVersionUID = 3662069942274237517L;

	/**  */
    private Integer id;

    /** 订单号 */
    private String ordercode;

    /** 用户id */
    private Integer cid;

    /** 1开多 2开空 */
    private Integer type;

    /** 实际花费金额 */
    private BigDecimal realmoney;

    /** 杠杆倍数 */
    private Integer gearing;

    /** 杠杆放大金额 */
    private BigDecimal money;

    /** 委托单价 */
    private BigDecimal price;
    
    /** 手续费 */
    private BigDecimal tax;

    /** 货币类型 见coin表 */
    private String coin;

    /** 持仓量 */
    private BigDecimal coinnum;

    /** 委托时间 */
    private Date createtime;

    /** 1委托中 2委托成功 3已取消 */
    private Integer status;

    /** 成功时间 */
    private Date successtime;

    /** 取消时间 */
    private Date canceltime;
    
    private String createtimeStr;
    
    private String succestimeStr;
    
    private String canceltimeStr;
    
    private String phone;
    
    private Integer salesman;
    private String salemanname;
    
    private Integer userid;
    
    private Integer parentid;
    private String parentname;
    
    private String username;
    
    private String realname;
    
    private Integer identity;
    /** 持仓时长 单位s */
    private Integer runTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.id
     *
     * @return the value of c_entrust_order.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.id
     *
     * @param id the value for c_entrust_order.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.ordercode
     *
     * @return the value of c_entrust_order.ordercode
     *
     * @mbggenerated
     */
    public String getOrdercode() {
        return ordercode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.ordercode
     *
     * @param ordercode the value for c_entrust_order.ordercode
     *
     * @mbggenerated
     */
    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.cid
     *
     * @return the value of c_entrust_order.cid
     *
     * @mbggenerated
     */
    public Integer getCid() {
        return cid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.cid
     *
     * @param cid the value for c_entrust_order.cid
     *
     * @mbggenerated
     */
    public void setCid(Integer cid) {
        this.cid = cid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.type
     *
     * @return the value of c_entrust_order.type
     *
     * @mbggenerated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.type
     *
     * @param type the value for c_entrust_order.type
     *
     * @mbggenerated
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.realmoney
     *
     * @return the value of c_entrust_order.realmoney
     *
     * @mbggenerated
     */
    public BigDecimal getRealmoney() {
        return realmoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.realmoney
     *
     * @param realmoney the value for c_entrust_order.realmoney
     *
     * @mbggenerated
     */
    public void setRealmoney(BigDecimal realmoney) {
        this.realmoney = realmoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.gearing
     *
     * @return the value of c_entrust_order.gearing
     *
     * @mbggenerated
     */
    public Integer getGearing() {
        return gearing;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.gearing
     *
     * @param gearing the value for c_entrust_order.gearing
     *
     * @mbggenerated
     */
    public void setGearing(Integer gearing) {
        this.gearing = gearing;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.money
     *
     * @return the value of c_entrust_order.money
     *
     * @mbggenerated
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.money
     *
     * @param money the value for c_entrust_order.money
     *
     * @mbggenerated
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.price
     *
     * @return the value of c_entrust_order.price
     *
     * @mbggenerated
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.price
     *
     * @param price the value for c_entrust_order.price
     *
     * @mbggenerated
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.coin
     *
     * @return the value of c_entrust_order.coin
     *
     * @mbggenerated
     */
    public String getCoin() {
        return coin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.coin
     *
     * @param coin the value for c_entrust_order.coin
     *
     * @mbggenerated
     */
    public void setCoin(String coin) {
        this.coin = coin;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.coinnum
     *
     * @return the value of c_entrust_order.coinnum
     *
     * @mbggenerated
     */
    public BigDecimal getCoinnum() {
        return coinnum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.coinnum
     *
     * @param coinnum the value for c_entrust_order.coinnum
     *
     * @mbggenerated
     */
    public void setCoinnum(BigDecimal coinnum) {
        this.coinnum = coinnum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.createtime
     *
     * @return the value of c_entrust_order.createtime
     *
     * @mbggenerated
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.createtime
     *
     * @param createtime the value for c_entrust_order.createtime
     *
     * @mbggenerated
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.status
     *
     * @return the value of c_entrust_order.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.status
     *
     * @param status the value for c_entrust_order.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.successtime
     *
     * @return the value of c_entrust_order.successtime
     *
     * @mbggenerated
     */
    public Date getSuccesstime() {
        return successtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.successtime
     *
     * @param successtime the value for c_entrust_order.successtime
     *
     * @mbggenerated
     */
    public void setSuccesstime(Date successtime) {
        this.successtime = successtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column c_entrust_order.canceltime
     *
     * @return the value of c_entrust_order.canceltime
     *
     * @mbggenerated
     */
    public Date getCanceltime() {
        return canceltime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column c_entrust_order.canceltime
     *
     * @param canceltime the value for c_entrust_order.canceltime
     *
     * @mbggenerated
     */
    public void setCanceltime(Date canceltime) {
        this.canceltime = canceltime;
    }

	public String getCreatetimeStr() {
		if(createtime!=null) {
			createtimeStr=DateUtil.toDateString(createtime,"yyyy-MM-dd HH:mm:ss");
		}
		return createtimeStr;
	}

	public String getSuccestimeStr() {
		if(successtime!=null) {
			succestimeStr=DateUtil.toDateString(successtime,"yyyy-MM-dd HH:mm:ss");
		}
		return succestimeStr;
	}

	public String getCanceltimeStr() {
		if(canceltime!=null) {
			canceltimeStr=DateUtil.toDateString(canceltime,"yyyy-MM-dd HH:mm:ss");
		}
		return canceltimeStr;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getSalesman() {
		return salesman;
	}

	public void setSalesman(Integer salesman) {
		this.salesman = salesman;
	}

	public String getSalemanname() {
		return salemanname;
	}

	public void setSalemanname(String salemanname) {
		this.salemanname = salemanname;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public BigDecimal getTax() {
		return tax;
	}

	public void setTax(BigDecimal tax) {
		this.tax = tax;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public String getParentname() {
		return parentname;
	}

	public void setParentname(String parentname) {
		this.parentname = parentname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getIdentity() {
		return identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }
}