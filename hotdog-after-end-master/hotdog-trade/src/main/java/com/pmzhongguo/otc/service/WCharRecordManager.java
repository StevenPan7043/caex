package com.pmzhongguo.otc.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.otc.entity.dto.OTCTradeDTO;
import com.pmzhongguo.otc.entity.dto.WCharRecordDTO;
import com.pmzhongguo.otc.otcenum.TakerEnum;
import com.pmzhongguo.otc.otcenum.WhetherEnum;

@Service
@Transactional
public class WCharRecordManager {

	@Autowired
	private WCharRecordService wCharRecordService;
	
	@Autowired
	private OTCTradeManager oTCTradeManager;
	
	@Autowired
	public DaoUtil daoUtil;
	
	public int insert(WCharRecordDTO record) {
		if(HelpUtils.nullOrBlank(record.getCreateTime())) {
			record.setCreateTime(HelpUtils.dateToString(new Date()));
		}
		return wCharRecordService.insert(record);
	}
	
	public WCharRecordDTO insertDouble(WCharRecordDTO record){
		//存入对方交易id
		OTCTradeDTO oTCTradeDTO = oTCTradeManager.findById(record.getTradeId());
		record.setOppositeTId(oTCTradeDTO.getOppositeTId());
		record.setTaker(TakerEnum.SELF);
		record.setIsRead(WhetherEnum.YES);
		insert(record);
		WCharRecordDTO opposite = new WCharRecordDTO();
		BeanUtils.copyProperties(record,opposite);
		opposite.setMemberId(record.getOppositeId());
		opposite.setOppositeId(record.getMemberId());
		opposite.setTradeId(record.getOppositeTId());
		opposite.setOppositeTId(record.getTradeId());
		opposite.setIsRead(WhetherEnum.NO);
		opposite.setTaker(TakerEnum.OPPOSITE);
		int oppositeId = insert(opposite);
		opposite.setId(oppositeId);
		return opposite;
	}
	
	public List<Map<String, Object>> getNoRecordInfo(Integer memberId){
		List<Map<String, Object>>  result = daoUtil.queryForList("SELECT w.opposite_id AS oppositeId, m.`m_nick_name` AS nick_name, w.trade_id AS tradeId, COUNT(*) AS COUNT FROM `w_char_record`  w  LEFT JOIN m_member m ON m.id = w.opposite_id WHERE w.member_id = ? AND w.is_read = 0 GROUP BY w.opposite_id, w.`trade_id`;", memberId);
		return result;
	}
	
	public List<WCharRecordDTO> findByConditionPage(Map<String, Object> param){
		return wCharRecordService.findByConditionPage(param);
	}
	
	public void batchUpdateIsRead(Integer memberId, Integer oppositeId) {
		int count = daoUtil.queryForInt("SELECT COUNT(*) FROM `w_char_record` w WHERE w.`member_id` = ? AND w.`opposite_id` = ? AND w.`is_read` = 0;", memberId, oppositeId);
		if(count > 0) {
			WCharRecordDTO record = new WCharRecordDTO();
			record.setMemberId(memberId);
			record.setOppositeId(oppositeId);
			updateIsRead(record);
		}
	}
	
	public int updateIsRead(Integer id) {
		WCharRecordDTO record = new WCharRecordDTO();
		record.setId(id);
		return wCharRecordService.updateIsRead(record);
	}
	
	public int updateIsRead(WCharRecordDTO record) {
		return wCharRecordService.updateIsRead(record);
	}
}
