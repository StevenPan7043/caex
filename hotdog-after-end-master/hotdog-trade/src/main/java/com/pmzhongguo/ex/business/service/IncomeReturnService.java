package com.pmzhongguo.ex.business.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.business.entity.TradeRanking;
import com.pmzhongguo.ex.business.entity.TradeRankingSet;
import com.pmzhongguo.ex.business.mapper.TradeRankingMapper;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;

/**
 * 交易返每天手续费
 * 
 * 
 * 
 * @author Administrator
 *
 */
@Service
public class IncomeReturnService {

	@Autowired
	private TradeRankingMapper tradeRankingMapper;

	@Autowired
	private MemberService memberService;

	/* =================API 接口=========================== */

	/**
	 * 交易排名是否开启
	 * 
	 * @param param
	 * @return
	 */
	public ObjResp isopen(String type) {
		TradeRankingSet tradeRankingSet = tradeRankingMapper.findByKeyName(type);
		// 返回已开启的交易对
		List<TradeRankingSet> trsList = tradeRankingMapper.getTradeRankingSetList();
		List<String> list = new ArrayList<String>();
		for (TradeRankingSet trs : trsList) {
			if (trs != null) {
				if (trs.getIsopen() == 1) {
					list.add(trs.getKeyname());
				}
			}
		}
		if (tradeRankingSet == null) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorENMsg(), list);
		}
		if (tradeRankingSet.getIsopen() == 1) {

			return new ObjResp(Resp.SUCCESS, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorENMsg(), list);
		} else {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorENMsg(), list);
		}
	}

	/**
	 * 交易对 交易排名列表
	 * 
	 * @param param
	 * @return
	 */
	public ObjResp list(Map<String, Object> param) {
		TradeRankingSet tradeRankingSet = tradeRankingMapper.findByKeyName((String) param.get("type"));
		if (tradeRankingSet == null) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorENMsg(), null);
		}
		if (tradeRankingSet.getIsopen() == 1) {
			param.put("startdate", tradeRankingSet.getStartdate());
			param.put("enddate", tradeRankingSet.getEnddate());
			Integer total = 0;
			try {
				Map<String, Object> result = new HashMap<String, Object>();
				total = tradeRankingMapper.count(param);
				result.put("total", total);
				if (total == 0) {
					return new ObjResp(Resp.SUCCESS, ErrorInfoEnum.LANG_SYMBOL_NOT_TRADE_RANK_DATA.getErrorENMsg(), result);
				}
				List<TradeRanking> list = tradeRankingMapper.list(param);
				result.put("list", list);
				return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorENMsg(), null);
		}
		return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
	}

	/**
	 * 搜索交易排名
	 * 
	 * @param param
	 * @return
	 */
	public ObjResp search(Map<String, Object> param) {
		TradeRankingSet tradeRankingSet = tradeRankingMapper.findByKeyName((String) param.get("type"));
		if (tradeRankingSet == null) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorENMsg(), null);
		}
		if (tradeRankingSet.getIsopen() == 1) {
			param.put("startdate", tradeRankingSet.getStartdate());
			param.put("enddate", tradeRankingSet.getEnddate());
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, tradeRankingMapper.search(param));
		} else {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorENMsg(), null);
		}
	}

	/* ===================后台管理====================== */

	/**
	 * 后台获取[交易排名设置开启管理]列表
	 * 
	 * @return
	 */
	public List<TradeRankingSet> getCurrencyPairList() {
		return tradeRankingMapper.getCurrencyPairList();
	}

	/**
	 * 后台管理 编辑查询
	 * 
	 * @param ttrsid
	 * @return
	 */
	public TradeRankingSet fineTradeRankingOne(Integer dcpid) {
		return tradeRankingMapper.fineTradeRankingOne(dcpid);
	}

	/**
	 * 设置是否开启关闭 交易排名
	 * 
	 * @param id
	 * @param isopen
	 * @param isopen2
	 * @return
	 */
	public void setTradeRankingSet(TradeRankingSet tradeRankingSet) {
		if (tradeRankingSet.getOpentime() == null) {
			tradeRankingSet.setOpentime(HelpUtils.formatDate8(new Date()));
		}
		if (tradeRankingSet.getTtrsid() != null) {
			tradeRankingMapper.updateTradeRankingSet(tradeRankingSet);
		} else {
			tradeRankingMapper.insertTradeRankingSet(tradeRankingSet);
		}
	}

	/**
	 * 交易排名查询
	 * 
	 * @param param
	 * @return
	 */
	public Map<String, Object> getTradeRankingList(Map<String, Object> param) {
		Map<String, Object> result = new HashMap<String, Object>();
		TradeRankingSet tradeRankingSet = tradeRankingMapper.findByKeyName((String) param.get("type"));
		if (tradeRankingSet == null) {
			return result;
		}
		if (tradeRankingSet.getIsopen() == 1) {
			param.put("startdate", tradeRankingSet.getStartdate());
			param.put("enddate", tradeRankingSet.getEnddate());
			Integer total = 0;
			try {
				total = tradeRankingMapper.count(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (total == 0) {
				return result;
			}
			List<TradeRanking> list = tradeRankingMapper.getTradeRankingList(param);
			result.put("Total", total);
			result.put("Rows", list);
		}
		return result;
	}

	/**
	 * 发送排名奖励
	 * 
	 * @param type
	 *            交易对
	 * @throws ParseException
	 */
	public Resp sendReward(String type) {
		TradeRankingSet tradeRankingSet = tradeRankingMapper.findByKeyName(type);
		if (tradeRankingSet == null) {
			return new Resp(Resp.FAIL, "该交易对没有开启交易排名");
		}
		if (tradeRankingSet.getIsopen() == 1) {
			if (tradeRankingSet.getIsSendReward() == 1) {
				return new Resp(Resp.FAIL, "该交易排名奖励已发放, 不能重复发送");
			}
			try {
				if (new Date().before(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(tradeRankingSet.getEnddate()))) {
					return new Resp(Resp.FAIL, "交易排名还没有结束,不能发送奖励!");
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return new Resp(Resp.FAIL, "该交易对没有开启交易排名");
			}
			// 查询该交易对所有需要奖励的数据
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("page", 0);
			param.put("pagesize", 200);
			param.put("type", type);
			param.put("startdate", tradeRankingSet.getStartdate());
			param.put("enddate", tradeRankingSet.getEnddate());
			Integer total = 0;
			try {
				total = tradeRankingMapper.count(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (total == 0) {
				return new Resp(Resp.FAIL, "该交易对没有开启交易排名");
			}
			List<TradeRanking> list = tradeRankingMapper.getTradeRankingList(param);
			// 发放奖励
			// TODO 需要开启事物
			CoinRecharge cr = null;
			for (int i = 0; i < list.size(); i++) {
				TradeRanking tr = list.get(i);
				tr.setMname(new StringBuffer(tr.getUid()).append("---").append(tr.getMname()).toString());
				cr = new CoinRecharge();
				cr.setM_name(tr.getMname());
				cr.setMember_id(Integer.parseInt(tr.getUid()));
				// if (i >= 0 && i <= 2) {// 第1名将获得轿车一辆 第2名将获得苹果电脑一台 第3名将获得苹果X手机一部
				// } else
				if (i >= 3 && i <= 9) {// 第4-10名将获得88888个 <TCO>
					cr.setR_amount(new BigDecimal(88888));
					sendRewardService(cr);
				} else if (i >= 10 && i <= 49) {// 第11-50名将获得8888个TCO
					cr.setR_amount(new BigDecimal(8888));
					sendRewardService(cr);
				} else if (i >= 50 && i <= 99) {// 第51-100名将获得888个TCO
					cr.setR_amount(new BigDecimal(888));
					sendRewardService(cr);
				} else if (i >= 100 && i <= 199) {// 第101-200名将获得188个TCO
					cr.setR_amount(new BigDecimal(188));
					sendRewardService(cr);
				}
			}
		}
		tradeRankingSet.setIsSendReward(1);
		tradeRankingMapper.updateTradeRankingSet(tradeRankingSet);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 发送奖励服务
	 * 
	 * @param cr
	 */
	private void sendRewardService(CoinRecharge cr) {
		// cr.setM_name("100000---duliangde3723@163.com");
		// cr.setMember_id(100000);
		// cr.setR_amount(new BigDecimal(10));
		cr.setCurrency("TCO");
		cr.setR_address("TRADE_RANKING_REWARD");
		cr.setIs_frozen(0);
		try {
			System.out.println("发送奖励数据: ===> " + cr.getM_name() + " == " + cr.getMember_id() + " == " + cr.getCurrency()
					+ " == " + cr.getR_amount() + " == " + cr.getR_address() + " == " + cr.getIs_frozen() + " == ");
			Thread.sleep(10);
			memberService.manAddCoinRecharge(cr);

			// TODO 存储发送奖励记录
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
