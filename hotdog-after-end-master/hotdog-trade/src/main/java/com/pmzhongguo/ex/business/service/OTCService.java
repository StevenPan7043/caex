package com.pmzhongguo.ex.business.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.business.entity.OTCAds;
import com.pmzhongguo.ex.business.entity.OTCOwner;
import com.pmzhongguo.ex.business.mapper.OTCMapper;
import com.pmzhongguo.ex.business.resp.OTCOrder;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.SerializeUtil;
import com.pmzhongguo.ex.framework.entity.FrmUser;

@Service
@Transactional
public class OTCService extends BaseServiceSupport {

	public static Map<String, BigDecimal[]> tickersMap = new LinkedHashMap<String, BigDecimal[]>();
	public static List<Map<String, String>> OTCPairNameLst = new ArrayList<Map<String, String>>();
	private Logger logger = LoggerFactory.getLogger(OTCService.class);
	@Autowired
	private OTCMapper otcMapper;

	public List getAllOTCOwner(Map params) {
		return otcMapper.listOTCOwnerPage(params);
	}

	public OTCOwner findOTCOwnerById(Integer id) {
		return otcMapper.loadOTCOwnerById(id);
	}

	public void addOTCOwner(OTCOwner station) {
		otcMapper.addOTCOwner(station);
	}

	public void updateOTCOwner(OTCOwner station) {
		otcMapper.updateOTCOwner(station);
	}

	public void delOTCOwner(Integer id) {
		otcMapper.delOTCOwner(id);
	}

	public List<OTCAds> getAllOTCAds(Map params) {
		return otcMapper.listOTCAdsPage(params);
	}

	public OTCAds findOTCAdsById(Integer id) {
		return otcMapper.loadOTCAdsById(id);
	}

	public void addOTCAds(OTCAds ads) {
		if (ads.getQuote_currency().equals(ads.getBase_currency())) {
			throw new BusinessException(-1, ErrorInfoEnum.BASE_AND_QUOTE_CURRENCY_NOT_CONSISTENT.getErrorCNMsg());
		}

		otcMapper.addOTCAds(ads);
	}

	public void updateOTCAds(OTCAds ads) {
		if (ads.getQuote_currency().equals(ads.getBase_currency())) {
			throw new BusinessException(-1, ErrorInfoEnum.BASE_AND_QUOTE_CURRENCY_NOT_CONSISTENT.getErrorCNMsg());
		}

		otcMapper.updateOTCAds(ads);
	}

	public List<OTCOrder> getOrders(Map<String, Object> orderMap, Integer ad_type) {
		if (ad_type == 1) { // 买入
			return otcMapper.findBuyOrderPage(orderMap);
		} else {
			return otcMapper.findSellOrderPage(orderMap);
		}
	}

	public void refreshLastTime() {
		List<Map<String, Integer>> ids = daoUtil.queryForList("select id from otc_owner");
		for (Map<String, Integer> id : ids) {
			if (Math.random() > 0.5) {
				daoUtil.update("update otc_owner set last_time = ? where id = ?", HelpUtils.formatDate6(new Date()),
						id.get("id"));
			}
		}

		OTCPairNameLst = daoUtil.queryForList(
				"select distinct concat(base_currency, quote_currency) pairName, base_currency, quote_currency from otc_ads where c_status = 1");
	}

	public void cacheOTCTicker() {
		for (Map<String, String> map : OTCPairNameLst) {
			String symbol = map.get("pairName").toLowerCase();

			List<byte[]> lst = JedisUtil.getInstance().lrange(symbol + "_OTC_trade", 0, 10000); // 这里取1440是因为要生成聚合数据中的24小时内数据
			int k = 0;
			int i = 0;
			// 开盘，最高，最低，收盘
			BigDecimal[] dayKLine = { BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO };
			BigDecimal maybeOpenPrice = BigDecimal.ZERO; // 可能开盘价，因为实际开盘价一般为0，第一个出现的不为0的价格作为开盘价
			BigDecimal outOf24HourPrice = BigDecimal.ZERO; // 如果超过24小时没有价格，就用这个价格
			for (byte[] bt : lst) {
				// 时间戳, 价格, 数量
				BigDecimal[] curArr = (BigDecimal[]) SerializeUtil.deserialize(bt);

				// 超过24小时不要，因为是倒叙排列，所以，break
				Long nowStamp = HelpUtils.getNowTimeStampMillisecond();
				if (nowStamp - curArr[0].longValue() > 86400000) {
					outOf24HourPrice = curArr[1];

					if (i++ == lst.size() - 1) {
						dayKLine[0] = curArr[1].compareTo(BigDecimal.ZERO) <= 0 ? outOf24HourPrice : curArr[1];
						dayKLine[1] = curArr[1].compareTo(BigDecimal.ZERO) <= 0 ? outOf24HourPrice : curArr[1];
						dayKLine[2] = curArr[1].compareTo(BigDecimal.ZERO) <= 0 ? outOf24HourPrice : curArr[1];
						dayKLine[3] = curArr[1].compareTo(BigDecimal.ZERO) <= 0 ? outOf24HourPrice : curArr[1];
					}

					continue;
				}

				if (k == 0) {
					// 开盘，最高，最低，收盘
					dayKLine[0] = curArr[1].compareTo(BigDecimal.ZERO) <= 0 ? outOf24HourPrice : curArr[1];
					dayKLine[1] = curArr[1].compareTo(BigDecimal.ZERO) <= 0 ? outOf24HourPrice : curArr[1];
					dayKLine[2] = curArr[1].compareTo(BigDecimal.ZERO) <= 0 ? outOf24HourPrice : curArr[1];
					dayKLine[3] = curArr[1].compareTo(BigDecimal.ZERO) <= 0 ? outOf24HourPrice : curArr[1];
				} else {
					// 高
					if (dayKLine[1].compareTo(curArr[1]) < 0) {
						dayKLine[1] = curArr[1];
					}
					// 低
					if (dayKLine[2].compareTo(BigDecimal.ZERO) == 0) { // 如果原来为0，则不管什么情况，设置为新的
						dayKLine[2] = curArr[1];
					} else if (curArr[2].compareTo(BigDecimal.ZERO) > 0 && dayKLine[2].compareTo(curArr[1]) > 0) {
						dayKLine[2] = curArr[1];
					}
				}

				if (curArr[1].compareTo(BigDecimal.ZERO) > 0) {
					maybeOpenPrice = curArr[1];
				}

				if (k == lst.size() - 1) { // 最后一个，作为开盘价（因为是按时间倒叙排列的）
					// 这里判断的原因是，刚开盘的币，取第一个不为0（如果有）的作为开盘价
					dayKLine[0] = curArr[1].compareTo(BigDecimal.ZERO) == 0 ? maybeOpenPrice : curArr[1];
				}
				k++;
			}
			tickersMap.put(symbol + "_ticker", dayKLine);
		}
	}

	/**
	 * 商户基本信息
	 * 
	 * @return
	 */
	public Map<String, Object> ownerInfo() {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		FrmUser owner = HelpUtils.getFrmUser();
		Map<String, Object> user = otcMapper.getOwnerInfo(owner.getOtc_owner_id());
		List<Map<String, Object>> list = otcMapper.getOwnerList(owner.getOtc_owner_id());

		resultMap.put("user", user);
		resultMap.put("list", list);
		return resultMap;
	}

}
