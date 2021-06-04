package com.pmzhongguo.ex.business.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.pmzhongguo.ex.business.dto.TradeRankDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.utils.JedisUtil;
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
import org.springframework.transaction.annotation.Transactional;

/**
 * 交易排名service
 * 
 * 从Redis中获取数据
 * 
 * @author Administrator
 *
 */
@Service
public class TradeRankingService {

	@Autowired
	private TradeRankingMapper tradeRankingMapper;

	@Autowired
	private MemberService memberService;

	public static final String REDIS_TRADE_RANK_KEY_ = "REDIS_TRADE_RANK_KEY_";
	// 单位s
	private static final int REDIS_TRADE_RANK_KEY_EXIPRE = 120;



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

			return new ObjResp(Resp.SUCCESS, ErrorInfoEnum.LANG_SYMBOL_OPEN_TRADE_RANK.getErrorENMsg(), list);
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
				// 统计在该时间段内交易 的人数
				total = tradeRankingMapper.count(param);
				result.put("total", total);
				if (total == 0) {
					return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, result);
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
	@Transactional
	public Resp sendReward(String type) {
		TradeRankingSet tradeRankingSet = tradeRankingMapper.findByKeyName(type);
		if (tradeRankingSet == null) {
			return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorCNMsg());
		}
		if (tradeRankingSet.getIsopen() == 1) {
			if (tradeRankingSet.getIsSendReward() == 1) {
				return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_REWORD_HAS_SEND.getErrorCNMsg());
			}
			try {
				if (new Date().before(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(tradeRankingSet.getEnddate()))) {
					return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_TRADE_RANK_NOT_END_NOT_REWORD.getErrorCNMsg());
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorCNMsg());
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


	public List<Map<String,Object>> getTradeRankingAndDayTradeList(Map<String,Object> param) {
		TradeRankingSet tradeRankingSet = tradeRankingMapper.findByKeyName((String) param.get("type"));
		if (tradeRankingSet == null) {
			throw  new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorENMsg());
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
					throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_TRADE_RANK_DATA.getErrorENMsg());
				}
				return tradeRankingMapper.getTradeRankingAndDayTradeList(param);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw  new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_SYMBOL_NOT_OPEN_TRADE_RANK.getErrorENMsg());
		}
		throw  new BusinessException(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg());

	}

    /**
     * 判断该币种是否有交易排名数据
     * @param param type：交易对名称，比如ebankzc，ethusdt
     * @return
     */
    public boolean hasTradeRankingData(Map<String,Object> param) {
        TradeRankingSet tradeRankingSet = tradeRankingMapper.findByKeyName((String) param.get("type"));
        if (tradeRankingSet == null) {
            return false;
        }
        if (tradeRankingSet.getIsopen() == 1) {
            param.put("startdate", tradeRankingSet.getStartdate());
            param.put("enddate", tradeRankingSet.getEnddate());
            Integer total = 0;
            try {
                total = tradeRankingMapper.count(param);
                if (total == 0) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return false;
        }
        return false;

    }









    /**
	 * 这个是后台管理端调用
     * 总成交量,只能统计zc和usdt区的
     * @param map zc：zc的交易对，usdt：usdt的交易对，startdate：开始排名时间，enddate：结束交易时间，count：数量
     * @return
     */
    public List<Map<String,Object>> getTradeRankingByZcAndUsdtList(Map<String,Object> map) {
        String currency = (String)map.get("type");
        map.put("type",currency+"zc");
        boolean flag = hasTradeRankingData(map);
        if(!flag) {
            map.put("type",currency+"usdt");
            flag = hasTradeRankingData(map);
        }
        if(!flag) {
            return null;
        }
        map.put("zc",currency+"zc");
        map.put("usdt",currency+"usdt");
        List<Map<String, Object>> dayTradetList = tradeRankingMapper.getDayTradeRankingByZcAndUsdtList(map);
        List<Map<String, Object>> totalTradeList = tradeRankingMapper.getTotalTradeRankingByZcAndUsdtList(map);
        for(int i=0; i<totalTradeList.size(); i++) {
            boolean hasDayTrade = true;
            Map<String, Object> totalMap = totalTradeList.get(i);
            int totalTradeUid = (int)totalMap.get("uid");
            for(Map<String, Object> datMap : dayTradetList) {
                int dayTradeUid = (int)datMap.get("uid");
                if(totalTradeUid == dayTradeUid) {
                    hasDayTrade = false;
                    // 24h成交量合并
                    totalMap.put("day_trade",datMap.get("day_trade"));
                    break;
                }
            }
            if(hasDayTrade) {
                totalMap.put("day_trade",0);
            }
        }

        // 排序
        Collections.sort(totalTradeList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                BigDecimal num1 = new BigDecimal(o1.get("num").toString());
                BigDecimal num2 = new BigDecimal(o2.get("num").toString());
                return num2.compareTo(num1);
            }
        });

        for(int i=0; i<totalTradeList.size(); i++) {
            Map<String, Object> totalMap = totalTradeList.get(i);
            // 排名
            totalMap.put("no",i+1);
        }
        return totalTradeList;
    }

    /**
     * 总成交量，要对用户账户进行加密，只能统计zc和usdt区的
     * @param map zc：zc的交易对，usdt：usdt的交易对，startdate：开始排名时间，enddate：结束交易时间，count：数量
     * @return
     */
    public List<Map<String,Object>> getEncryptionAccountTradeRankingByZcAndUsdtList(Map<String,Object> map) {
        String currency = (String)map.get("type");
        map.put("type",currency+"zc");
        boolean flag = hasTradeRankingData(map);
        if(!flag) {
            map.put("type",currency+"usdt");
            flag = hasTradeRankingData(map);
        }
        if(!flag) {
            return null;
        }
        map.put("zc",currency+"zc");
        map.put("usdt",currency+"usdt");
        // 24h
        List<Map<String, Object>> dayTradetList = tradeRankingMapper.getDayTradeRankingByZcAndUsdtList(map);
        List<Map<String, Object>> totalTradeList = tradeRankingMapper.getTotalTradeRankingByZcAndUsdtList(map);
        for(int i=0; i<totalTradeList.size(); i++) {
            boolean hasDayTrade = true;
            Map<String, Object> totalMap = totalTradeList.get(i);
            // 账号加密
            String account = (String)totalMap.get("account");
            totalMap.put("account",account.substring(0,3) + "****" + account.substring(7,account.length()));
            int totalTradeUid = (int)totalMap.get("uid");
            for(Map<String, Object> datMap : dayTradetList) {
                int dayTradeUid = (int)datMap.get("uid");
                if(totalTradeUid == dayTradeUid) {
                    hasDayTrade = false;
                    // 添加24h成交量
                    totalMap.put("day_trade",datMap.get("day_trade"));
                    break;
                }
            }
            if(hasDayTrade) {
                totalMap.put("day_trade",0);
            }
        }

        // 排序
        Collections.sort(totalTradeList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                BigDecimal num1 = new BigDecimal(o1.get("num").toString());
                BigDecimal num2 = new BigDecimal(o2.get("num").toString());
                return num2.compareTo(num1);
            }
        });
        for(int i=0; i<totalTradeList.size(); i++) {
            Map<String, Object> totalMap = totalTradeList.get(i);
            // 排名
            totalMap.put("no",i+1);
        }
        return totalTradeList;
    }

	/**
	 * 查询某个货币持仓数量排名
	 * @param map type：货币EBANK，size：数量
	 * @return
	 */
	public List<Map<String, Object>> findMemberAccountRankByCurrency(Map<String, Object> map) {
		// 先判断是否开启
		Map<String, Object> currencyRank = tradeRankingMapper.findCurrencyRankingIsOpen(map);
		if (currencyRank == null || currencyRank.size() <= 0) {
			return new ArrayList<Map<String, Object>>();
		}
//		map.put("current_time",HelpUtils.dateToStryyyyMMddHHmm(new Date()));
		List<Map<String, Object>> trankList = tradeRankingMapper.findMemberAccountRankByCurrency(map);
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (int i=0; i<trankList.size(); i++) {
			Map<String, Object> trank = trankList.get(i);
			if (trank.get("account") == null) {
				continue;
			}
			String mName = (String)trank.get("account");
			trank.put("account",mName.substring(0,3) + "****" + mName.substring(7,mName.length()));
			trank.put("no",i+1);
			resultList.add(trank);
		}
		return resultList;
	}


	/**
	 * 查找交易数据
	 * @param type 币种
	 * @param count 多少条
	 * @return
	 */
	public List<TradeRankDto> findTradeRankData(String type, int count) {
		Map<String, String> param = new HashMap<String,String>(1);
		param.put("type",type);
		List<TradeRankDto> totalTradeList = new ArrayList<TradeRankDto>();
		Map<Integer, TradeRankDto> calcAccountTotal =  new HashMap<Integer, TradeRankDto>();
		// 要统计的交易对
		Map<String, String> dayParam = new HashMap<String,String>();
		dayParam.put("zc",null);
		dayParam.put("usdt",null);
		dayParam.put("eth",null);
		// 查出需要累加交易对数量
		List<Map<String, Object>> CurrencyPairList = findCurrencyPairTradeRankList(param);
		for (Map<String, Object> pair: CurrencyPairList) {
			Map<String, Object> paramTradeRank = new HashMap<String,Object>(3);
			String key_name = (String)pair.get("key_name");
			// 获取交易对
			dayParamSetter(key_name.toLowerCase(),dayParam);
			dayParam.put("startdate",(String) pair.get("startdate"));
			dayParam.put("enddate",(String)pair.get("enddate"));
			paramTradeRank.put("pair",key_name.toLowerCase());

			paramTradeRank.put("startdate",pair.get("startdate"));
			paramTradeRank.put("enddate",pair.get("enddate"));
			List<TradeRankDto> rankList = tradeRankingMapper.getTotalTradeRankingList(paramTradeRank);

			if(rankList != null && rankList.size() > 0) {
				for (TradeRankDto record : rankList) {
					Integer uid = record.getUid();
					// 该map是否有该uid的key
					if (calcAccountTotal.containsKey(uid)) {
						TradeRankDto rankDto = calcAccountTotal.get(uid);
						// 将已有的量相加
						rankDto.setNum(record.getNum().add(rankDto.getNum()));
						String mName = rankDto.getAccount();
						rankDto.setAccount(mName.substring(0,3) + "****" + mName.substring(7,mName.length()));
						calcAccountTotal.put(uid,rankDto);
					}else {
						String mName = record.getAccount();
						record.setAccount(mName.substring(0,3) + "****" + mName.substring(7,mName.length()));
						calcAccountTotal.put(uid,record);
					}
				}

			}
		}
		// 如果为空直接返回
		if(calcAccountTotal.size() <= 0) {
			return new ArrayList<TradeRankDto>();
		}
		Iterator<Map.Entry<Integer, TradeRankDto>> iterator = calcAccountTotal.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, TradeRankDto> record = iterator.next();
			totalTradeList.add(record.getValue());
		}
		// 排序
		Collections.sort(totalTradeList, new Comparator<TradeRankDto>() {
			@Override
			public int compare(TradeRankDto o1, TradeRankDto o2) {
				return o2.getNum().compareTo(o1.getNum());
			}
		});
		// 截取前n条
		int num = totalTradeList.size() > count ? count : totalTradeList.size();
		List<TradeRankDto> resultList = totalTradeList.subList(0, num);
		// 查找24小时交易的量
		dayParam.put("zc","lscpzc");
		List<TradeRankDto> dayList = tradeRankingMapper.getDayTradeRankingByZcAndUsdtAndEthList(dayParam);

		List<TradeRankDto> list = new ArrayList<TradeRankDto>();
		for(int i=0; i<resultList.size(); i++) {
			boolean hasDayTrade = true;
			TradeRankDto rankDto = resultList.get(i);

			int uid = rankDto.getUid();
			for(TradeRankDto dayRank : dayList) {
				int dayTradeUid = dayRank.getUid();
				if(uid == dayTradeUid) {
					hasDayTrade = false;
					// 24h成交量合并
					rankDto.setDayNum(dayRank.getDayNum());
					rankDto.setNo(i+1);
					rankDto.setType(type);
					list.add(rankDto);
					break;
				}
			}
			if(hasDayTrade) {
				rankDto.setNo(i+1);
				rankDto.setType(type);
				rankDto.setDayNum(BigDecimal.ZERO);
				list.add(rankDto);
			}
		}



		// 缓存处理
		JedisUtil.getInstance().del(REDIS_TRADE_RANK_KEY_+type.toUpperCase());
		JedisUtil.getInstance().lpush(REDIS_TRADE_RANK_KEY_+type.toUpperCase(),list);
		JedisUtil.getInstance().expire(REDIS_TRADE_RANK_KEY_+type.toUpperCase(),REDIS_TRADE_RANK_KEY_EXIPRE);
		return list;

	}

	/**
	 * 设置交易对
	 * @param type
	 * @param map
	 */
	private void dayParamSetter(String type,Map<String,String > map) {
		if(type.endsWith("zc")) {
			map.put("zc",type);
		}else if(type.endsWith("usdt")) {
			map.put("usdt",type);
		}else if(type.endsWith("eth")) {
			map.put("eth",type);
		}

	}

	/**
	 * 查询某个币所有开启的交易对
	 * @param param type: 币种名称，如：EBANK
	 * @return
	 */
	public List<Map<String, Object>> findCurrencyPairTradeRankList(Map<String, String> param) {
		List<Map<String, Object>> list = tradeRankingMapper.findCurrencyPairTradeRankList(param);
		return list;
	}


	public void insertCurrencyRank(Currency currency) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("currency",currency.getCurrency());
        map.put("create_time",HelpUtils.dateToString(new Date()));
        map.put("update_time",HelpUtils.dateToString(new Date()));
        // 默认不开启
        map.put("is_open",0);
	    tradeRankingMapper.insertCurrencyRank(map);
    }


	public List<Map<String, Object>> getCurrencyList() {
		return tradeRankingMapper.findCurrencyList();
	}

	public void setCurrencyTradeRankingSet(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id",params.get("id"));
		map.put("is_open",params.get("is_open"));
		map.put("create_time",HelpUtils.dateToString(new Date()));
		map.put("update_time",HelpUtils.dateToString(new Date()));
		map.put("startdate",params.get("startdate"));
		map.put("enddate",params.get("enddate"));
		tradeRankingMapper.updateCurrencyTradeRankingSet(map);
	}

	public Map<String,Object> fineCurrencyTradeRankingOne(Integer id) {
		return tradeRankingMapper.fineCurrencyTradeRankingOne(id);
	}
}
