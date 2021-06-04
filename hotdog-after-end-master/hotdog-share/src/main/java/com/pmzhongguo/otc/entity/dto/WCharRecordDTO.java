package com.pmzhongguo.otc.entity.dto;

import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.otc.otcenum.TakerEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;

import java.io.Serializable;

public class WCharRecordDTO implements Serializable {
    private Integer id;

    private Integer memberId;

    private String nick_name;
    
    private Integer oppositeId;
    
    private String oppo_nick_name;

    private String charContent;

    private TakerEnum taker;

    private WhetherEnum isRead;

    private String createTime;

    private String modifyTime;
    
    private Integer tradeId;
    
    private Integer oppositeTId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Integer getOppositeId() {
        return oppositeId;
    }

    public void setOppositeId(Integer oppositeId) {
        this.oppositeId = oppositeId;
    }

    public String getCharContent() {
        return charContent;
    }

    public void setCharContent(String charContent) {
        this.charContent = charContent == null ? null : charContent.trim();
    }

    public TakerEnum getTaker() {
		return taker;
	}

	public void setTaker(TakerEnum taker) {
		this.taker = taker;
	}

	public WhetherEnum getIsRead() {
		return isRead;
	}

	public void setIsRead(WhetherEnum isRead) {
		this.isRead = isRead;
	}

	public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime == null ? null : modifyTime.trim();
    }
    
	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getOppo_nick_name() {
		return oppo_nick_name;
	}

	public void setOppo_nick_name(String oppo_nick_name) {
		this.oppo_nick_name = oppo_nick_name;
	}

	public Integer getTradeId() {
		return tradeId;
	}

	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public Integer getOppositeTId() {
		return oppositeTId;
	}

	public void setOppositeTId(Integer oppositeTId) {
		this.oppositeTId = oppositeTId;
	}

	@Override
	public String toString() {
		String result = "";
		try {
			result = JsonUtil.beanToJson(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}