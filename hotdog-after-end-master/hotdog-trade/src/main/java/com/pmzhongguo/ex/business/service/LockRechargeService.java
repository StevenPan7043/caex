package com.pmzhongguo.ex.business.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.business.dto.LockRechargeDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.LockAmount;
import com.pmzhongguo.ex.business.entity.LockList;
import com.pmzhongguo.ex.business.entity.LockRecharge;
import com.pmzhongguo.ex.business.mapper.CurrencyMapper;
import com.pmzhongguo.ex.business.mapper.LockListMapper;
import com.pmzhongguo.ex.business.mapper.LockRechargeMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;


@Service
@Transactional
public class LockRechargeService extends BaseServiceSupport {

	@Autowired
	private LockRechargeMapper lockRechargeMapper;

	@Autowired
	private LockListMapper lockListMapper;

	@Autowired
	private LockAmountService lockAmountService;

	@Autowired
	private CurrencyMapper currencyMapper;

	/**
	 * 锁仓释放，定时任务
	 */
	public void releaseLock()  {
		List<LockRechargeDto> lockRecharges = lockRechargeMapper.findAll();
		String currentTimeStr = HelpUtils.formatDate4(new Date());
		for (LockRechargeDto vo : lockRecharges) {
			if(vo.getR_unnum().compareTo(new BigDecimal(0)) <= 0) {
				continue;
			}
			Map lockListMap = new HashMap();
			lockListMap.put("lk_id",vo.getId());
			lockListMap.put("undata",currentTimeStr);
			LockList lockList = lockListMapper.findByLockRechargeIdAndUndata(lockListMap);
			//如果当天已经释放就跳过
			if(lockList != null) {
				continue;
			}
			//计算当前时间和锁仓时间相差天数
			Date lockTime = null;
			Date currentTimeDate = null;
			try {
				lockTime = HelpUtils.formatDate10(vo.getLocktime().toString());
				currentTimeDate = HelpUtils.formatDate10(currentTimeStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//如果天数相差0天也跳过
			long minusDay = HelpUtils.minusTimeDate(lockTime,currentTimeDate);
			if(minusDay == 0) {
				continue;
			}

			//获取每个月分配比例
			String unlockpre = vo.getUnlockpre();
			String[] unlockpreArr = unlockpre.substring(1, unlockpre.length() - 1).split(",");

			//获取该月释放百分比
			int index = 0;
			if(minusDay > 30) {
				index = 1;
			}
			int currentPre = Integer.parseInt(unlockpreArr[index]);
			/**===========================单笔锁仓开始============================================*/
			//获取总数量
			BigDecimal amount = vo.getR_amount();
			System.out.println("--------总数量amount："+amount);
			//释放百分比
			BigDecimal releasePre = new BigDecimal(0.01).multiply(new BigDecimal(currentPre), MathContext.DECIMAL32).setScale(vo.getC_precision(),BigDecimal.ROUND_HALF_UP);
			System.out.println("--------释放百分比releasePre："+releasePre+"%");
			//获取当月分配数量
			BigDecimal monthDivNum = amount.multiply(releasePre,MathContext.DECIMAL32).setScale(vo.getC_precision(), BigDecimal.ROUND_HALF_UP);
			System.out.println("--------当月分配数量monthDivNum："+monthDivNum);
			// 获得未释放数量=未释放数量-今天要释放的数量
			BigDecimal unnum = null;
			BigDecimal dayDivNum = null;
			//如果是最后一天不管是9.9999还是10.0001全部释放
			if(minusDay == 60) {
				dayDivNum = vo.getR_unnum();
				unnum = new BigDecimal(0);
			}else {
				//获取当月当天分配数量
				dayDivNum = monthDivNum.divide(new BigDecimal(30), vo.getC_precision(), BigDecimal.ROUND_DOWN);
				System.out.println("--------当天分配数量dayDivNum："+dayDivNum);
				unnum = vo.getR_unnum().subtract(dayDivNum).setScale(vo.getC_precision(), BigDecimal.ROUND_HALF_UP);
			}
			//更新单笔锁仓余额
			Map lock = new HashMap();
			lock.put("r_unnum", unnum);
			System.out.println("--------单笔锁仓余额unnum："+unnum);
			lock.put("id", vo.getId());
			//已经释放数量=已释放数量+当天是否数量
			BigDecimal releaseNum = vo.getR_release_num().add(dayDivNum).setScale(vo.getC_precision(), BigDecimal.ROUND_HALF_UP);
			System.out.println("--------已经释放数量releaseNum："+releaseNum);
			lock.put("r_release_num", releaseNum);
			lock.put("r_update_time", HelpUtils.formatDate8(new Date()));
			lockRechargeMapper.update(lock);
			/**===========================单笔锁仓结束============================================*/

			//保存释放记录
			Map map = new HashMap();
			map.put("member_id", vo.getMember_id());
			map.put("currency_id", vo.getCurrency_id());
			map.put("currency", vo.getCurrency());
			map.put("u_num", dayDivNum);
			map.put("qishu",minusDay);
			map.put("qishu",minusDay);
			map.put("untime", HelpUtils.formatDate8(new Date()));
			map.put("undata", currentTimeStr);
			map.put("lk_id", vo.getId());
			lockListMapper.insert(map);

			//更新该币总资产
			LockAmount c_lockAmount = lockAmountService.findOne(vo.getMember_id(), vo.getCurrency_id());
			Map<String, Object> currencyAmount = new HashMap();
			//总释放=总释放+当天释放
			c_lockAmount.setR_release(c_lockAmount.getR_release().add(dayDivNum).setScale(vo.getC_precision(), BigDecimal.ROUND_HALF_UP));
			//总未释放=未释放-当天释放
			c_lockAmount.setUnnum(c_lockAmount.getUnnum().subtract(dayDivNum).setScale(vo.getC_precision(), BigDecimal.ROUND_HALF_UP));
			currencyAmount.put("unnum", c_lockAmount.getUnnum());
			currencyAmount.put("r_release", c_lockAmount.getR_release());
			currencyAmount.put("id", c_lockAmount.getId());
			lockAmountService.update(currencyAmount);
		}
	}


	public List<LockRecharge> findCurrencyByPage(Map map) {
		List<LockRecharge> list = lockRechargeMapper.findCurrencyByPage(map);
		return list;
	}


	public List<LockRecharge> findLockListByPage(Map map) {
		List<LockRecharge> lockList = lockRechargeMapper.findLockListByPage(map);
		return lockList;
	}

	public List<Map> findRecharge(Map map) {

		List<Map> recharge = lockRechargeMapper.findRechargeByPage(map);
		return recharge;
	}

	public void update(Map map) {
		lockRechargeMapper.update(map);
	}


	public LockRecharge findOne(Integer id) {
		return lockRechargeMapper.findOne(id);
	}

    /**
     * 后台确认更新锁仓状态和时间
     * @param params
     */
	public void updateLockRechargeStatus(Map params) {
        String r_status = (String)params.get("r_status");
        if(r_status != null && "1".equals(r_status)){

            LockRecharge lock = lockRechargeMapper.findOne(Integer.parseInt((String) params.get("id")));
            //状态：-1待付款[OTC]，0未确认，1已确认，2已取消,只有0才可以
            if(lock.getR_status() != 0 ){
                return;
            }

            int memberId = Integer.parseInt((String) params.get("member_id"));
            int currencyId = Integer.parseInt((String) params.get("currency_id"));
            LockAmount c_lockAmount = lockAmountService.findOne(memberId, currencyId);

            params.put("r_update_time",HelpUtils.formatDate8(new Date()));
            params.put("locktime",HelpUtils.formatDate4(new Date()));
            params.put("r_unnum",params.get("r_amount"));
            lockRechargeMapper.update(params);

            if(c_lockAmount != null){
				Currency currency = currencyMapper.getCurrency(lock.getCurrency_id());
				c_lockAmount.setAmount(lock.getR_amount().add(c_lockAmount.getAmount()).setScale(currency.getC_precision(),BigDecimal.ROUND_HALF_UP));
                c_lockAmount.setUnnum(lock.getR_unnum().add(c_lockAmount.getUnnum()).setScale(currency.getC_precision(),BigDecimal.ROUND_HALF_UP));
                Map amountMap = new HashMap();
                amountMap.put("amount",c_lockAmount.getAmount());
                amountMap.put("unnum",c_lockAmount.getUnnum());
                amountMap.put("r_release",c_lockAmount.getR_release());
                amountMap.put("id",c_lockAmount.getId());
                lockAmountService.update(amountMap);
            }else {
                Map lockAmount = new HashMap();
                lockAmount.put("amount",lock.getR_amount());
                lockAmount.put("currency",lock.getCurrency());
                lockAmount.put("currency_id",lock.getCurrency_id());
                lockAmount.put("member_id",lock.getMember_id());
                lockAmount.put("r_release",0);
                lockAmount.put("unnum",lock.getR_amount());
                lockAmountService.insert(lockAmount);
            }
        }
    }

}
