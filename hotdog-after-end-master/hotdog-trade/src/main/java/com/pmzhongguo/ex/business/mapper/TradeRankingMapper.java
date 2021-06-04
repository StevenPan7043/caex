package com.pmzhongguo.ex.business.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.dto.TradeRankDto;
import com.pmzhongguo.ex.business.entity.TradeRanking;
import com.pmzhongguo.ex.business.entity.TradeRankingSet;
import com.pmzhongguo.ex.core.mapper.SuperMapper;

/**
 * 交易排名DAO
 * 
 * @author Administrator
 *
 */
public interface TradeRankingMapper extends SuperMapper {

	/**
	 * 获取交易排名; 分页
	 * 
	 * @return
	 */
	List<TradeRanking> list(Map<String, Object> map) throws Exception;

	/**
	 * 总条数
	 * 
	 * @param startdate
	 *            开始日期
	 * @return
	 */
	Integer count(Map<String, Object> map) throws Exception;

	/**
	 * 交易排名查询
	 * 
	 * @param param
	 * @return
	 */
	TradeRanking search(Map<String, Object> param);

	/* ===================后台管理====================== */

	/**
	 * 查询所有交易对的排名开始状态 列表
	 * 
	 * @param ttrsid
	 * 
	 * @return List
	 */
	List<TradeRankingSet> getCurrencyPairList();

	/**
	 * 设置交易对 的交易排名状态
	 * 
	 * @param tradeRankingSet
	 *            TradeRankingSet
	 * @return
	 */
	void insertTradeRankingSet(TradeRankingSet tradeRankingSet);

	/**
	 * 设置交易对 的交易排名状态
	 * 
	 * @param tradeRankingSet
	 *            TradeRankingSet
	 * @return
	 */
	void updateTradeRankingSet(TradeRankingSet tradeRankingSet);

	/**
	 * 交易排名查询
	 * 
	 * @param param
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<TradeRanking> getTradeRankingList(Map param);

	/**
	 * 查询一条数据
	 * 
	 * @param ttrsid
	 * @return
	 */
	TradeRankingSet fineTradeRankingOne(Integer dcpid);

	/**
	 * 根据keyname 查询交易排名设置信息
	 * 
	 * @param object
	 * @return
	 */
	TradeRankingSet findByKeyName(String keyname);

	/**
	 * 查询说有的交易对排名设置
	 * 
	 * @return
	 */
	List<TradeRankingSet> getTradeRankingSetList();

	List<Map<String,Object>> getTradeRankingAndDayTradeList(Map<String,Object> map);

	/**
	 * 总成交量
	 * @param map zc：zc的交易对，usdt：usdt的交易对，startdate：开始排名时间，enddate：结束交易时间，count：数量
	 * @return
	 */
	List<Map<String,Object>> getTotalTradeRankingByZcAndUsdtList(Map<String,Object> map);

	/**
	 *	24h的成交量
	 * @param map zc：zc的交易对，usdt：usdt的交易对，startdate：开始排名时间，enddate：结束交易时间，count：数量
	 * @return
	 */
	List<Map<String,Object>> getDayTradeRankingByZcAndUsdtList(Map<String,Object> map);



/**	==================================================================================== */
	/**
	 * 自身可以累加 总成交量
	 * @param map zc：zc的交易对，usdt：usdt的交易对，startdate：开始排名时间，enddate：结束交易时间，count：数量
	 * @return
	 */
	List<Map<String,Object>> getSelfTotalTradeRankingByZcAndUsdtList(Map<String,Object> map);

	/**
	 *	自身可以累加   24h的成交量
	 * @param map zc：zc的交易对，usdt：usdt的交易对，startdate：开始排名时间，enddate：结束交易时间，count：数量
	 * @return
	 */
	List<Map<String,Object>> getSelfDayTradeRankingByZcAndUsdtList(Map<String,Object> map);


	/**
	 * 查询用户持有该币数量排名
	 * @param map
	 */
	List<Map<String,Object>> findMemberAccountRankByCurrency(Map<String, Object> map);

	/**
	 * 查看某个币是否开启交易
	 * @param map
	 * @return
	 */
	Map<String, Object> findCurrencyRankingIsOpen(Map<String, Object> map);

	/**
	 * 查询某个币所有开启的交易对
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> findCurrencyPairTradeRankList(Map<String, String> map);

	/**
	 * 查询交易对总交易量排名
	 * @param map pair:交易对
	 * @return
	 */
	List<TradeRankDto> getTotalTradeRankingList(Map<String, Object> map);

	/**
	 * 获取eth，zc，usdt三者24h的量
	 * @param map
	 * @return
	 */
	List<TradeRankDto> getDayTradeRankingByZcAndUsdtAndEthList(Map<String, String> map);

	void insertCurrencyRank(Map<String, Object> map);

	List<Map<String,Object>> findCurrencyList();

	void updateCurrencyTradeRankingSet(Map<String, Object> map);

	Map<String,Object> fineCurrencyTradeRankingOne(Integer id);
}
