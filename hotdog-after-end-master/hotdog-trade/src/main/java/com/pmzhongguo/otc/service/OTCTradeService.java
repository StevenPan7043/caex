package com.pmzhongguo.otc.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JsonUtil;
import com.pmzhongguo.otc.dao.OTCTradeMapper;
import com.pmzhongguo.otc.entity.convertor.OTCTradeConvertor;
import com.pmzhongguo.otc.entity.dataobject.OTCTradeDO;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.otcenum.ComplainTypeEnum;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.TakerEnum;
import com.pmzhongguo.otc.otcenum.TradeStatusEnum;

@Service
@Transactional
public class OTCTradeService {
	
	protected Logger TradeLogger = LoggerFactory.getLogger("tradeInfo");

	@Autowired
	private OTCTradeMapper oTCTradeMapper;
	
	/**
	 * 	条件 memberId|startDate|endDate|status 
     * 	数据库时间字段 dbDate 	
	 * @param param
	 * @return
	 */
	public int countDone(Map<String, Object> param) {
		OTCTradeConvertor.mapEnumName2type(param);
		int count = oTCTradeMapper.countDone(param);
		return count;
	}
	
	/**
	 * 	根据 memberId 查询申诉记录数
	 * @param param
	 * @return
	 */
	public int countComplain(Map<String, Object> param) {
		OTCTradeConvertor.mapEnumName2type(param);
		int count = oTCTradeMapper.countComplain(param);
		return count;
	}
	
	/**
     * 	memberId and status
     * @param param
     * @return 放行时间
     */
    public int getConsumingTime(Map<String, Object> param) {
    	try {
    		OTCTradeConvertor.mapEnumName2type(param);
        	Map<String, Object> result = oTCTradeMapper.getConsumingTime(param);
        	int total = Integer.valueOf(result.get("total") + ""); 
        	int consumingTime = 0;
        	if(!HelpUtils.isMapNullValue(result, "consumingTime")) {
        		consumingTime = Integer.valueOf(result.get("consumingTime") + "");
        	}
        	if(total != 0 && consumingTime != 0) {
        		return consumingTime/total;
        	}
    	}catch(Exception e) {
    		TradeLogger.warn(JsonUtil.toJson(param) + " getConsumingTime Exception!" + " cause:" + e.getCause() + " " + e);
    	}
    	return 0;
    };

	/**
	 * 根据id查找交易
	 * 
	 * @param id
	 * @return
	 */
	public OTCTradeDTO findById(int id) {
		OTCTradeDO oTCTradeDO = oTCTradeMapper.findById(id);
		return oTCTradeDO == null ? null : OTCTradeConvertor.DO2DTO(oTCTradeDO);
	}

	public List<OTCTradeDTO> findByConditionPage(Map<String, Object> param) {
		OTCTradeConvertor.mapEnumName2type(param);
		List<OTCTradeDO> list = oTCTradeMapper.findByConditionPage(param);
		return CollectionUtils.isEmpty(list) ? null : OTCTradeConvertor.DO2DTO(list);
	}

	/**
	 * 	与findByConditionPage不同在与status条件写死为status in (0, 1)
	 * @param param
	 * @return
	 */
	public List<OTCTradeDTO> findBytradingPage(Map<String, Object> param) {
		OTCTradeConvertor.mapEnumName2type(param);
		List<OTCTradeDO> list = oTCTradeMapper.findBytradingPage(param);
		return CollectionUtils.isEmpty(list) ? null : OTCTradeConvertor.DO2DTO(list);
	}

    /**
     * 申诉中的单子
     *
     * @param param
     * @return
     */
    public List<OTCTradeDTO> findByComplainTradePage(Map<String, Object> param) {
        OTCTradeConvertor.mapEnumName2type(param);
        List<OTCTradeDO> list = oTCTradeMapper.findByComplainTradePage(param);
        List<OTCTradeDTO> otcTradeDTOS = OTCTradeConvertor.DO2DTO(list);
        return CollectionUtils.isEmpty(list) ? null : OTCTradeConvertor.DO2DTO(list);
    }

	/**
	 * 根据id删除交易
	 * 
	 * @param id
	 * @return
	 */
	public int deleteByPrimaryKey(Integer id) {
		int i = 0;
		if (id == null) {
			return i;
		}
		i = oTCTradeMapper.deleteByPrimaryKey(id);
		return i;
	}
	
	public int modifyStatus(Integer tradeId, TradeStatusEnum status, String memo) {
		OTCTradeDTO cancelTrade = new OTCTradeDTO();
		cancelTrade.setId(tradeId);
		cancelTrade.setStatus(status);
		cancelTrade.setMemo(memo);
		if(status.getType() == TradeStatusEnum.UNCONFIRMED.getType()) {
			cancelTrade.setPayTime(HelpUtils.dateToString(new Date()));
		}
		if(status.getType() == TradeStatusEnum.DONE.getType() || status.getType() == TradeStatusEnum.CANCELED.getType()) {
			OTCTradeDTO old = findById(tradeId);
			cancelTrade.setDoneTime(HelpUtils.dateToString(new Date()));
			Date beginDate = HelpUtils.stringToDateWithTime(old.getCreateTime());
			long consumingTime = HelpUtils.minusMinute(new Date(), beginDate);
			cancelTrade.setConsumingTime(Integer.valueOf(consumingTime + ""));
		}
		int count = updateByPrimaryKeySelective(cancelTrade);
		return count;
	}

	/**
	 * 	
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(OTCTradeDTO record) {
		int i = 0;
		if (record.getId() == null) {
			return i;
		}
		OTCTradeDO oTCTradeDO = OTCTradeConvertor.DTO2DO(record);
		i = oTCTradeMapper.updateByPrimaryKeySelective(oTCTradeDO);
		return i;
	}
	
	/**
	 * 添加一个交易
	 * 
	 * @param record
	 * @return
	 */
	public int insert(OTCTradeDTO record) {
		OTCTradeDO oTCTradeDO = OTCTradeConvertor.DTO2DO(record);
		oTCTradeMapper.insert(oTCTradeDO);
		return oTCTradeDO.getId() == null ? 0 : oTCTradeDO.getId();
	}

	/**
	 * 	根据订单生成交易记录
	 * 
	 * @param record
	 * @param oppositeTId
	 * @return
	 */
	public OTCTradeDTO init(OTCOrderDTO record, Integer oppositeOId, Integer oppositeTId) {
		OTCTradeDTO dto = new OTCTradeDTO();
		dto.setBaseCurrency(record.getBaseCurrency());
		dto.setMemberId(record.getMemberId());
		dto.setoId(record.getId());
		dto.setOppositeOId(oppositeOId);
		dto.setPrice(record.getPrice());
		dto.setQuoteCurrency(record.getQuoteCurrency());
		dto.setStatus(TradeStatusEnum.NP);
		dto.settType(record.getType());
		dto.setVolume(record.getCurVolume());
		dto.setComplainType(ComplainTypeEnum.NORMAL);
		if (record.getMinQuote() == null ||record.getMinQuote().compareTo(BigDecimal.ZERO) == 0) {
			dto.setTaker(TakerEnum.SELF);
		} else {
			dto.setTaker(TakerEnum.OPPOSITE);
		}
		dto.setOppositeTId(oppositeTId);
		return dto;
	}
	
	
}
