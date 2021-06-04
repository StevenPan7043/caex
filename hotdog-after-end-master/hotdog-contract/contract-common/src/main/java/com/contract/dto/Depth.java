package com.contract.dto;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.contract.entity.CContractOrder;
import com.contract.entity.CZcOrder;

/**
 * @Author ISME
 * @Date 2018/1/14
 * @Time 14:39
 */

public class Depth {

    /**
     * id : 1489464585407
     * ts : 1489464585407
     * bids : [[7964,0.0678],[7963,0.9162]]
     * asks : [[7979,0.0736],[8020,13.6584]]
     */

    private String id;
    private String ts;
    /**
     * 货币价格
     */
    private BigDecimal coinPrice;
    /**
     * 货币人民币
     */
    private BigDecimal coinCny;
    
    /**
     * 买
     */
    private List<List<BigDecimal>> bids;
    
    /**
     * 卖
     */
    private List<List<BigDecimal>> asks;
    
    private List<CContractOrder> list;
    
    private List<CZcOrder> zclist;
    
    private JSONObject moneyMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public List<List<BigDecimal>> getBids() {
        return bids;
    }

    public void setBids(List<List<BigDecimal>> bids) {
        this.bids = bids;
    }

    public List<List<BigDecimal>> getAsks() {
        return asks;
    }

    public void setAsks(List<List<BigDecimal>> asks) {
        this.asks = asks;
    }

	public BigDecimal getCoinPrice() {
		return coinPrice;
	}

	public void setCoinPrice(BigDecimal coinPrice) {
		this.coinPrice = coinPrice;
	}

	public BigDecimal getCoinCny() {
		return coinCny;
	}

	public void setCoinCny(BigDecimal coinCny) {
		this.coinCny = coinCny;
	}

	public List<CContractOrder> getList() {
		return list;
	}

	public void setList(List<CContractOrder> list) {
		this.list = list;
	}

	public JSONObject getMoneyMap() {
		return moneyMap;
	}

	public void setMoneyMap(JSONObject moneyMap) {
		this.moneyMap = moneyMap;
	}

	public List<CZcOrder> getZclist() {
		return zclist;
	}

	public void setZclist(List<CZcOrder> zclist) {
		this.zclist = zclist;
	}
    
    
}
