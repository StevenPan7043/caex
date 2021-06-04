package com.pmzhongguo.ex.business.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.entity.OTCAds;
import com.pmzhongguo.ex.business.entity.OTCOwner;
import com.pmzhongguo.ex.business.resp.OTCOrder;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

public interface OTCMapper extends SuperMapper {

	public List listOTCOwnerPage(Map params);

	public OTCOwner loadOTCOwnerById(Integer id);

	public void addOTCOwner(OTCOwner station);

	public void updateOTCOwner(OTCOwner station);

	public void delOTCOwner(Integer id);

	public List<OTCAds> listOTCAdsPage(Map params);

	public OTCAds loadOTCAdsById(Integer id);

	public void addOTCAds(OTCAds station);

	public void updateOTCAds(OTCAds station);

	public List<OTCOrder> findBuyOrderPage(Map<String, Object> orderMap);

	public List<OTCOrder> findSellOrderPage(Map<String, Object> orderMap);

	/**
	 * 更具id查询 商户基本信息
	 * 
	 * @param id
	 * @return
	 */
	public Map<String, Object> getOwnerInfo(Integer id);

	/**
	 * 更具id查询 商户广告信息
	 * 
	 * @param id
	 * @return
	 */
	public List<Map<String, Object>> getOwnerList(Integer id);

	/**
	 * 取消订单返还广告交易额度
	 * 
	 * @param paramMap
	 */
	public void updateOtcAds(Map<String, Object> paramMap);

	/**
	 * 更新广告是否展示状态
	 * 
	 * @param status
	 * @param id
	 */
	public void updateStatus4Ads(Map<String, Object> paramMap);

}
