package com.pmzhongguo.otc.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.otc.dao.OTCOrderMapper;
import com.pmzhongguo.otc.entity.convertor.OTCOrderConvertor;
import com.pmzhongguo.otc.entity.dataobject.OTCOrderDO;
import com.pmzhongguo.otc.entity.dto.OTCOrderDTO;
import com.pmzhongguo.otc.otcenum.OrderStatusEnum;
import com.pmzhongguo.otc.otcenum.OrderTypeEnum;
import com.pmzhongguo.otc.otcenum.PriceTypeEnum;

@Service
@Transactional
public class OTCOrderService {
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private OTCOrderMapper oTCOrderMapper;

	/**
	 * 添加一张订单
	 * 
	 * @param record
	 * 
	 * 
	 * @return
	 */
	public int insert(OTCOrderDTO record) {
		OTCOrderDO oTCOrderDO = OTCOrderConvertor.DTO2DO(record);
		oTCOrderMapper.insert(oTCOrderDO);
		return oTCOrderDO.getId() == null ? 0 : oTCOrderDO.getId();
	}

	/**
	 * 根据id查找订单
	 * 
	 * @param id
	 * @return
	 */
	public OTCOrderDTO findById(int id) {
		OTCOrderDO record = oTCOrderMapper.findById(id);
		return record == null ? null : OTCOrderConvertor.DO2DTO(record);
	}

	/**
	 * 根据条件分页查询
	 * 
	 * @param param
	 * @return
	 */
	public List<OTCOrderDTO> findByConditionPage(Map<String, Object> param) {
		OTCOrderConvertor.mapEnumName2type(param);
		List<OTCOrderDO> list = oTCOrderMapper.findByConditionPage(param);
		return CollectionUtils.isEmpty(list) ? null : OTCOrderConvertor.DO2DTO(list);
	}
	
	/**
	 * 	查询要撤销的订单
	 * @param param effectiveTime 生效时间  createTime 要小于参数的创建时间
	 * @return
	 */
	public List<OTCOrderDTO> getCancelOrderList(Map<String, Object> param) {
		OTCOrderConvertor.mapEnumName2type(param);
		List<OTCOrderDO> list = oTCOrderMapper.getCancelOrderList(param);
		return CollectionUtils.isEmpty(list) ? null : OTCOrderConvertor.DO2DTO(list);
	}

	/**
	 * 根据id删除订单
	 * 
	 * @param id
	 * @return
	 */
	public int deleteByPrimaryKey(Integer id) {
		int i = 0;
		if (id == null) {
			return i;
		}
		i = oTCOrderMapper.deleteByPrimaryKey(id);
		return i;
	}

	/**
	 * 
	 * @param record
	 * @return
	 */
	public int updateByPrimaryKeySelective(OTCOrderDTO record) {
		int i = 0;
		if (record.getId() == null) {
			return i;
		}
		OTCOrderDO oTCOrderDO = OTCOrderConvertor.DTO2DO(record);
		i = oTCOrderMapper.updateByPrimaryKeySelective(oTCOrderDO);
		return i;
	}

	/**
	 * 锁住要交易的数量和交易额
	 * 
	 * @param record
	 * @param curVolume
	 * @param curAmount
	 * @return
	 */
	public ObjResp lockTrade(OTCOrderDTO record) {
		if (record.getRemainVolume().compareTo(record.getCurVolume()) < 0) {
			String msg = "orderId:" + record.getId() + " Volume:" + record.getRemainVolume() + " lockVolume"
					+ record.getCurVolume();
			logger.warn(msg);
			return new ObjResp(Resp.FAIL, msg, null);
		}
		OTCOrderDO oTCOrderDO = initChangeOTCOrderDO(record);
		if (record.getRemainVolume().compareTo(record.getCurVolume()) == 0
				&& OrderStatusEnum.WATTING.getType() == record.getStatus().getType()) {
			oTCOrderDO.setStatus(OrderStatusEnum.TRADING.getType());
		}
		int count = oTCOrderMapper.lockTrade(oTCOrderDO);
		return runResult(count, record.getId() + " lockTrade fail!");
	}

	/**
	 * 交易确定
	 * 
	 * @param record
	 * @param curVolume
	 * @param curAmount
	 * @return
	 */
	public ObjResp doneTrade(OTCOrderDTO record) {
		if (record.getLockVolume().compareTo(record.getCurVolume()) < 0
				|| record.getLockAmount().compareTo(record.getCurAmount()) < 0) {
			String msg = "orderId:" + record.getId() + " doneTrade, lockVolume:" + record.getLockVolume()
					+ " unLockVolume" + record.getVolume();
			logger.warn(msg);
			return new ObjResp(Resp.FAIL, msg, null);
		}
		OTCOrderDO oTCOrderDO = initChangeOTCOrderDO(record);
		if (record.getLockVolume().compareTo(record.getVolume()) == 0
				&& OrderStatusEnum.TRADING.getType() == record.getStatus().getType()) {
			oTCOrderDO.setStatus(OrderStatusEnum.DONE.getType());
		}
		int count = oTCOrderMapper.doneTrade(oTCOrderDO);
		return runResult(count, record.getId() + " doneTrade fail!");
	}

	/**
	 * 撤销交易
	 * 
	 * @param record
	 * @param curVolume
	 * @param curAmount
	 * @return
	 */
	public ObjResp cancelTrade(OTCOrderDTO record) {
		if (record.getLockVolume().compareTo(record.getCurVolume()) < 0
				|| record.getLockAmount().compareTo(record.getCurAmount()) < 0) {
			String msg = "orderId:" + record.getId() + " cancelTrade, lockVolume:" + record.getLockVolume()
					+ " unLockVolume" + record.getCurVolume();
			logger.warn(msg);
			return new ObjResp(Resp.FAIL, msg, null);
		}
		OTCOrderDO oTCOrderDO = initChangeOTCOrderDO(record);
		if (OrderStatusEnum.TRADING.getType() == record.getStatus().getType()) {
			oTCOrderDO.setStatus(OrderStatusEnum.WATTING.getType());
		}
		int count = oTCOrderMapper.cancelTrade(oTCOrderDO);
		return runResult(count, record.getId() + " cancelTrade fail!");
	}

	/**
	 * 撤销订单 撤销订单时只有交易额变化，数量不变
	 * 
	 * @param record
	 * @return
	 */
	public ObjResp cancelOrder(OTCOrderDTO record) {
		record.setCurVolume(record.getRemainVolume());
		OTCOrderDO oTCOrderDO = initChangeOTCOrderDO(record);
		BigDecimal doneTotal = record.getDoneVolume().add(record.getCurVolume());
		if (record.getVolume().compareTo(doneTotal) == 0) {
			oTCOrderDO.setStatus(OrderStatusEnum.CANCELED.getType());
		} else {
			oTCOrderDO.setStatus(OrderStatusEnum.PC.getType());
		}
		int count = oTCOrderMapper.cancelOrder(oTCOrderDO);
		return runResult(count, record.getId() + " cancelOrder fail!");
	}

	/**
	 * 生成修改订单DO
	 * 
	 * @param id
	 * @param curVolume
	 * @param curAmount
	 * @return
	 */
	private OTCOrderDO initChangeOTCOrderDO(OTCOrderDTO record) {
		OTCOrderDO oTCOrderDO = new OTCOrderDO();
		oTCOrderDO.setId(record.getId());
		oTCOrderDO.setCurVolume(record.getCurVolume());
		oTCOrderDO.setCurAmount(record.getCurAmount());
		return oTCOrderDO;
	}

	/**
	 * 根据返回结果判断更新是否成功
	 * 
	 * @param count
	 * @return
	 */
	private ObjResp runResult(int count, String errorMsg) {
		if (count == 1) {
			return new ObjResp(Resp.SUCCESS, "", null);
		}
		return new ObjResp(Resp.FAIL, errorMsg, null);
	}
}
