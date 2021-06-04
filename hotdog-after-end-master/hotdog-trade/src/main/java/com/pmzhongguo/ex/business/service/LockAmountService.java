package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.LockAmount;
import com.pmzhongguo.ex.business.mapper.CurrencyMapper;
import com.pmzhongguo.ex.business.mapper.LockAmountMapper;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Transactional
public class LockAmountService extends BaseServiceSupport {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LockAmountService.class);

	@Autowired
	private LockAmountMapper lockAmountMapper;

	@Resource
	private MarketService marketService;

	@Autowired
	CurrencyMapper currencyMapper;

	/**
	 * 查找所用币资产
	 * @param map
	 * @return
	 */
	public List findByPage(Map map) {
		List<LockAmount> list = lockAmountMapper.findByPage(map);
		return list;
	}

	public void update(Map map) {
		lockAmountMapper.update(map);
	}

	public void insert(Map map) {
		lockAmountMapper.insert(map);
	}

	/**
	 * 找到某一个币种资产
	 * @param memberId
	 * @param currencyId
	 * @return
	 */
	public LockAmount findOne(Integer memberId,Integer currencyId) {
		Map map = new HashMap();
		map.put("member_id",memberId);
		map.put("currency_id",currencyId);
		LockAmount lockAmount = lockAmountMapper.findOne(map);
		return lockAmount;
	}



	/**
	 * 锁仓划转
	 * 从已经释放的余额中扣除
	 * @param coinRecharge
	 * @param lockAmount
	 */
	public void transfer(CoinRecharge coinRecharge, LockAmount lockAmount) {

		Map lock = new HashMap();
		lock.put("id",lockAmount.getId());
		Currency currency = currencyMapper.getCurrency(lockAmount.getCurrency_id());
		BigDecimal releaseNum = lockAmount.getR_release()
				.subtract(coinRecharge.getR_amount()).setScale(currency.getC_precision(),BigDecimal.ROUND_HALF_UP);
		lock.put("r_release",releaseNum);
		BigDecimal amount = lockAmount.getAmount().subtract(coinRecharge.getR_amount()).setScale(currency.getC_precision(),BigDecimal.ROUND_HALF_UP);
		lock.put("amount",amount);
		lock.put("unnum",lockAmount.getUnnum());
		lockAmountMapper.update(lock);
	}

	/**
	 * 查找所有币总资产
	 * @param map
	 * @return
	 */
	public BigDecimal findOwnAmount(Map map) {
		List<LockAmount> allCurrency = lockAmountMapper.findAllCurrency(map);

		BigDecimal total = new BigDecimal(0);
		for(LockAmount lock : allCurrency) {
			Currency currency = currencyMapper.getCurrency(lock.getCurrency_id());
			String symbol = lock.getCurrency().toLowerCase() + "zc";
			TickerResp tickerResp = marketService.getTicker(symbol.toLowerCase());
			if(null != tickerResp && null != tickerResp.getClose()) {
				BigDecimal single = tickerResp.getClose().multiply(lock.getAmount()).setScale(currency.getC_precision(), BigDecimal.ROUND_HALF_UP);
				total = total.add(single).setScale(currency.getC_precision(),BigDecimal.ROUND_HALF_UP);
			}
		}
		return total;
	}
}
