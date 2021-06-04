package com.pmzhongguo.ex.business.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.core.utils.HelpUtils;

/**
 * 交易返还手续费
 * 
 * @author zengweixiong
 *
 */
@Service
public class TradeReturnFeeService {

	@Resource
	private SetTradeReturnFeeService setTradeReturnFeeService;

	@Resource
	private MemberService memberService;

	/**
	 * 处理交易手续费返还
	 * 
	 * @param coinRecharge
	 */
	public void add(CoinRecharge coinRecharge) {
		Integer memberId = coinRecharge.getMember_id();
		// 获取币种ID
		Currency currency = HelpUtils.getCurrencyMap().get(coinRecharge.getCurrency());
		coinRecharge.setCurrency_id(currency.getId());
		// 交易返还手续费地址类型
		coinRecharge.setR_address("TRADE_RETURN_FEE_REWARD");

		// 处理返还手续费
		BigDecimal fee = coinRecharge.getR_amount();

		// 获取交易返还设置信息
		List<Map<String, Object>> sets = setTradeReturnFeeService.getSet();
		Integer id;
		Integer rate;
		// 1 第一代返手续费比例 33
		// 2 第二代返手续费比例 22
		// 3 第三代返手续费比例 11
		Map<String, Object> setMap = new HashMap<String, Object>(3);
		for (Map<String, Object> map : sets) {
			id = Integer.valueOf("" + map.get("id"));
			rate = Integer.valueOf("" + map.get("rate"));
			setMap.put("lv" + id, rate);
		}

		// 更具用户ID获取用户的3代推荐关系
		List<Map<String, Object>> fasts = setTradeReturnFeeService.getFastMembers(memberId);
		Map<String, Object> fast;
		for (int i = 1; i < fasts.size(); i++) {
			System.err.println(fasts.get(i));
			fast = fasts.get(i);
			Integer lv = Integer.valueOf(lvToInt("" + fast.get("lv")));
			Integer pid = Integer.valueOf("" + fast.get("id"));

			Integer rate1 = (Integer) setMap.get("lv" + (lv - 1));

			BigDecimal returnFee = fee.multiply(new BigDecimal(rate1 / 100.0));

			coinRecharge.setMember_id(pid);
			coinRecharge.setR_amount(returnFee);
			coinRecharge.setIs_frozen(0);
			// 充值返还手续费
			memberService.manAddCoinRecharge(coinRecharge);
		}

	}

	private Integer lvToInt(String s) {
		return Integer.valueOf(s.substring(0, s.indexOf(".")));
	}

}
