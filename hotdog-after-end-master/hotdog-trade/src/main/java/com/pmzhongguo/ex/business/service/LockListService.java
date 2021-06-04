package com.pmzhongguo.ex.business.service;

import com.pmzhongguo.ex.api.controller.LockController;
import com.pmzhongguo.ex.business.entity.Account;
import com.pmzhongguo.ex.business.entity.LockList;
import com.pmzhongguo.ex.business.mapper.LockListMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;


@Service
@Transactional
public class LockListService extends BaseServiceSupport {

    private Logger logger = LoggerFactory.getLogger(LockController.class);

    /**
     * 释放时间（单位：月）
     */
    private static final int[] RELEASE_DATE = {1, 3, 6};
    /**
     * 收益率
     */
    private static final BigDecimal[] RELEASE_DATE_RATE = {new BigDecimal("0.1"), new BigDecimal("0.45"), new BigDecimal("1.08")};
    private static Map<Integer, BigDecimal> RELEASE_DATE_MAP = new HashMap<Integer, BigDecimal>();
    /**
     * 锁仓时间
     * START_DATE: 2018-12-27 12:00:00
     * END_DATE: 2019-01-15 00:00:00
     */
    private static final int START_DATE = 1545883200;
    private static final int END_DATE =   1547535600;
    /**
     * 查找的数量
     */
    private static final int START_SIZE = 0;

    @Autowired
    private LockListMapper lockListMapper;

    @Autowired
    private MemberService memberService;

    public LockListService() {
        for (int i = 0; i < RELEASE_DATE.length; i++) {
            RELEASE_DATE_MAP.put(RELEASE_DATE[i], RELEASE_DATE_RATE[i]);
        }

    }


    /**
     * 锁仓充值
     *
     * @param lockListForm
     * @return
     */
    public Resp addLockRecharge(LockList lockListForm) {
        // 更新用户的账户余额
        Map accountMap = new HashMap();
        accountMap.put("member_id", lockListForm.getMember_id());
        accountMap.put("currency", lockListForm.getCurrency());
        Account account = memberService.getAccount(accountMap);
        // 判断是否有余额可用
        if(account == null) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
        }
        // 判断是否有余额可用
        if (account.getAvailable_balance().compareTo(lockListForm.getAmount()) < 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
        }
        account.setFrozen_balance(account.getFrozen_balance().add(lockListForm.getAmount()));
        memberService.updateAccount(account);


        LockList lockList = new LockList();
        int type = lockListForm.getL_type();
        BigDecimal date = RELEASE_DATE_MAP.get(type);
        if (date == null) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_LOCK_TYPE_ERROR.getErrorENMsg());
        }
        switch (type) {
            case 1:
                lockList.setUndata(HelpUtils.getAfterSomeDayIntTime(30));
                break;
            case 3:
                lockList.setUndata(HelpUtils.getAfterSomeDayIntTime(30 * 3));
                break;
            case 6:
                lockList.setUndata(HelpUtils.getAfterSomeDayIntTime(30 * 6));
                break;
            default:
                return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_LOCK_TYPE_ERROR.getErrorENMsg());
        }
        // 构造新的入库实体
        lockList.setLocktime(HelpUtils.getNowTimeStampInt());
        lockList.setCurrency_id(lockListForm.getCurrency_id());
        lockList.setCurrency(lockListForm.getCurrency());
        lockList.setMember_id(lockListForm.getMember_id());
        lockList.setAmount(lockListForm.getAmount());
        lockList.setL_type(lockListForm.getL_type());
        lockList.setL_status(0);
        lockListMapper.insert(lockList);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * 锁仓释放=划转
     */
    public void releaseLock() {
        int undate = HelpUtils.getNowTimeStampInt();
        // 找到所有当前时间大于等于释放时间的锁仓记录
        List<LockList> lockLists = lockListMapper.findAllLockAfterundataAndStatus(undate);
        for (LockList lockList : lockLists) {
            Map accountMap = new HashMap();
            accountMap.put("member_id", lockList.getMember_id());
            accountMap.put("currency", lockList.getCurrency());
            Account account = memberService.getAccount(accountMap);
            if (account == null) {
                account = new Account();
                account.setFrozen_balance(BigDecimal.ZERO);
                // total_balance = (amount * type) + amount
                account.setTotal_balance(lockList.getAmount()
                        .multiply(RELEASE_DATE_MAP.get(lockList.getL_type()))
                        .setScale(4, BigDecimal.ROUND_DOWN));
                account.setCurrency(lockList.getCurrency());
                account.setMember_id(lockList.getMember_id());
                memberService.addAccount(account);
            } else {
                if (account.getFrozen_balance().compareTo(lockList.getAmount()) < 0) {
                    logger.error("<================= 锁仓释放有问题,冻结资金小于锁仓资金");
                    account.setFrozen_balance(BigDecimal.ZERO);
                } else {
                    account.setFrozen_balance(account.getFrozen_balance()
                            .subtract(lockList.getAmount()));
                }
                // total_balance = (amount * type) + amount + total_balance
                account.setTotal_balance(lockList.getAmount()
                        .multiply(RELEASE_DATE_MAP.get(lockList.getL_type()))
                        .setScale(4, BigDecimal.ROUND_DOWN)
                        .add(account.getTotal_balance()));
                account.setCurrency(lockList.getCurrency());
                memberService.updateAccount(account);
            }
            // 标志为已释放
            lockList.setL_status(1);
            lockListMapper.updateById(lockList);
        }
    }

    public List<Map<String, Object>> findTopLockByMemberAndDate(Map<String, Object> params) {

        params.put("startDate", START_DATE);
        params.put("endtDate", END_DATE);
        params.put("startSize", START_SIZE);
        List<Map<String, Object>> list = lockListMapper.findTopLockByMemberAndDate(params);
        // 排序
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                BigDecimal num1 = new BigDecimal(o1.get("amount").toString());
                BigDecimal num2 = new BigDecimal(o2.get("amount").toString());
                return num2.compareTo(num1);
            }
        });
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            map.put("no", i + 1);
            resultList.add(map);
        }
        return resultList;
    }


    public List<LockList> findLockRecordByMemberId(Map<String, Object> params) {
        return lockListMapper.findLockRecordByPage(params);
    }

    public Map<String,Object> findLockTotalAmount(Map<String, Object> params) {
        return lockListMapper.findLockTotalAmountByMemberIdAndCurrency(params);
    }

    /**===============================后台管理端==========================================**/

    public List<Map> findLocklist(Map<String, Object> params) {
        return lockListMapper.findLocklistByPage(params);
    }
}
