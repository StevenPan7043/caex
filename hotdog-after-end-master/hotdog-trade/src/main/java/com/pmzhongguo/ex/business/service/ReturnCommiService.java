
package com.pmzhongguo.ex.business.service;


import com.alibaba.fastjson.JSONObject;
import com.pmzhongguo.ex.business.dto.ReturnCommiCurrAmountDto;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.mapper.ReturnCommiMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;

import com.pmzhongguo.ex.core.web.RechargeTypeEnum;
import com.pmzhongguo.ex.framework.entity.FrmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


import java.math.BigDecimal;
import java.util.*;

/**
 * 返佣,部分需要添加事务@Transactional注解
 */
@Service
@Transactional
public class ReturnCommiService extends BaseServiceSupport{

	@Autowired
	private ReturnCommiMapper returnCommiMapper;

	@Autowired
	private MemberService memberService;

	private static Logger returnCommiLogger = LoggerFactory.getLogger("returnCommi");

	/**
	 * 添加用户的返佣信息
	 * @param member
	 */
	@Transactional
	public void add(Member member) {
		ReturnCommi returnCommi = new ReturnCommi();
		returnCommi.setMember_id(member.getId());
		returnCommi.setIntroduce_id(String.valueOf(member.getIntroduce_m_id()));
		// 查看系统配置的返佣比例
		FrmConfig mgrConfig = HelpUtils.getMgrConfig();
		// 返佣天数 = 月数 x 30
		int day = mgrConfig.getReturn_commi_time() * 30;
		String returnCommiTime = HelpUtils.getSomeDay(day);
		returnCommi.setReturn_commi_rate(mgrConfig.getReturn_commi_rate());
		returnCommi.setReturn_commi_time(returnCommiTime);
		returnCommi.setCreate_time(HelpUtils.formatDate8(new Date()));
		returnCommi.setUpdate_time(HelpUtils.formatDate8(new Date()));
		returnCommiMapper.insertReturnCommi(returnCommi);
	}

	/**
	 * 根据用户id和介绍人id查找有效时间内的返佣信息
	 * @param param member_id,return_commi_time,introduce_id
	 * @return
	 */
	@Transactional
	public ReturnCommi findHasVaildReturnCommiByMemberIdAndIntroduceId(Map<String, Object> param) {
		return returnCommiMapper.findHasVaildReturnCommiByMemberIdAndIntroduceId(param);
	}

	/**
	 * 每天每个币种每个用户的数量
	 * @param param
	 */
	@Transactional
	public void insertReturnCommiCurrAmount(Map<String, Object> param) {
		param.put("create_time",HelpUtils.formatDate8(new Date()));
		param.put("update_time",HelpUtils.formatDate8(new Date()));
		returnCommiMapper.insertReturnCommiCurrAmount(param);
	}

	/**
	 * 遍历返现
	 * @param list
	 * @param currency
	 */
	@Transactional(rollbackFor = Exception.class)
	public void handleCountCurrReturnCommi(List<ReturnCommiCurrAmountDto> list, String currency) {

		Map<String,Object> param = new HashMap<>();
		param.put("return_commi_time",HelpUtils.formatDate8(new Date()));

		for (ReturnCommiCurrAmountDto returnCommiCurrAmountDto : list) {

			if (returnCommiCurrAmountDto.getFee_amount().compareTo(BigDecimal.ZERO) <= 0) {
				continue;
			}

			Integer memberId = returnCommiCurrAmountDto.getMember_id();
			String introId = memberService.findIntroduceIdByMemberId(memberId);
			param.put("member_id",memberId);
			param.put("introduce_id",introId);
			// 查找返现具体返佣率
			ReturnCommi returnCommi = findHasVaildReturnCommiByMemberIdAndIntroduceId(param);
			// 如果该用户没有返佣就下一个
			if(returnCommi == null) {
				continue;
			}


			Map<String,Object> currAmount = new HashMap<>();
			currAmount.put("member_id",introId);
			currAmount.put("currency",currency);

			BigDecimal zcRate = HelpUtils.getSomeCurrencyWithZcRate(returnCommiCurrAmountDto.getFee_currency().toLowerCase());
			currAmount.put("amount",returnCommiCurrAmountDto.getFee_amount().multiply(returnCommi.getReturn_commi_rate()));
			currAmount.put("rate",zcRate);
			currAmount.put("return_commi_id",returnCommi.getId());
			try {
				insertReturnCommiCurrAmount(currAmount);
			}catch (Exception e) {
				returnCommiLogger.error("<================ 插入每天每币每人返佣数量失败信息： " + e.fillInStackTrace());
				returnCommiLogger.error("<================ 插入每天每币每人返佣数量失败： " + JSONObject.toJSONString(currAmount));
			}
		}

	}




	/**
	 * 查找每天每个用户所有币种的手续费数量总和
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findCurrReturnCommiOfDay(Map<String, Object> param) {
		return returnCommiMapper.findCurrReturnCommiOfDay(param);
	}

	/**
	 * 批量插入
	 * @param list
	 */
	@Transactional
	public List<CoinRecharge> batchInsertAmountReturnCommiOfDay(List<Map<String, Object>> list) {
		List<CoinRecharge> rechargeList = new ArrayList<>();
		List<Map<String, Object>> saveList = new ArrayList<>(list.size());
		for (Map<String, Object> currCommi : list) {
			String return_commi_amount = currCommi.get("return_commi_amount").toString();
			BigDecimal amount = new BigDecimal(return_commi_amount);
			if (amount.compareTo(BigDecimal.ZERO) <= 0)  {
				continue;
			}
			currCommi.put("create_time", HelpUtils.formatDate8(new Date()));
			currCommi.put("update_time",HelpUtils.formatDate8(new Date()));
			currCommi.put("currency", HelpUtils.getOfficialCurrencyToUpper());
			saveList.add(currCommi);

			CoinRecharge coinRecharge = new CoinRecharge();
			String member_id =  currCommi.get("member_id").toString();
			coinRecharge.setMember_id(Integer.parseInt(member_id));
			coinRecharge.setR_address(RechargeTypeEnum.RETURN_COMMISSION.getType());
			coinRecharge.setCurrency(HelpUtils.getOfficialCurrencyToUpper());
			coinRecharge.setR_amount(amount);
			coinRecharge.setIs_frozen(0);
			coinRecharge.setR_guiji(1);
			rechargeList.add(coinRecharge);
		}
		if (!CollectionUtils.isEmpty(saveList)) {
			returnCommiMapper.batchInsertAmountReturnCommiOfDay(saveList);
		}
		return rechargeList;
	}

	/**
	 * 用户各币种每天返佣总数
	 * @param param
	 * @return
	 */
	public List<Map<String,Object>> findByPage(Map<String,Object> param) {
		return returnCommiMapper.findReturnCommiAmountWithCurrencyByPage(param);
	}



	/**
	 * 查找返佣有效时间内的member_id
	 * @param curr_time
	 * @return
	 */
	public List<Integer> findReturnCommiByTimeAndGroupByMemberId(String curr_time) {
		return returnCommiMapper.findReturnCommiByTimeAndGroupByMemberId(curr_time);
	}

	/**
	 * 币种明细分页
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> findMgrReturnCommiAmountByPage(Map param) {
		return returnCommiMapper.findMgrReturnCommiAmountByPage(param);
	}

	/**
	 * 返佣总数分页
	 */
	public List<Map<String, Object>> findMgrReturnCommiCurrAmountByPage(Map param) {
		return returnCommiMapper.findMgrReturnCommiCurrAmountByPage(param);
	}

	/**
	 *
	 * @param memberId
	 * @return
	 */
	public Map<String,Object> findReturnCommiTotalAndIntroByMemberId(Integer memberId) {
		BigDecimal amount = returnCommiMapper.findReturnCommiAmountTotalByMemberId(memberId);
		Integer introNum = memberService.findIntroAmountByMemberId(memberId);
		Map<String,Object> map = new HashMap();
		map.put("introNum",introNum);
		String amountStr = amount == null ? "0.0"
				: amount.setScale(4,BigDecimal.ROUND_HALF_DOWN).toPlainString();
		map.put("amount",amountStr);
		return map;
	}

	/**
	 * 是否存在
	 * @param member
	 * @return
	 */
	public boolean isExistByMemberIdAndIntroduceId(Member member) {
		Map<String,Object> map = new HashMap();
		map.put("member_id",member.getId());
		map.put("introduce_id",member.getIntroduce_m_id());
		int count = returnCommiMapper.isExistByMemberIdAndIntroduceId(map);
		return count > 0;
	}
}