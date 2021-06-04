package com.pmzhongguo.ex.business.scheduler;

import com.pmzhongguo.ex.business.mapper.AccountRebateMapper;
import com.pmzhongguo.ex.business.service.RebateService;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * @description: VIAZ返利定时任务
 * @date: 2019-08-09 11:49
 * @author: zn
 */


@Component
@PropertySource("classpath:spring/rebate.properties")
public class RebateScheduler {
    private static Logger logger = LoggerFactory.getLogger(RebateScheduler.class);
    @Autowired
    private DaoUtil daoUtil;
    @Autowired
    private AccountRebateMapper rebateMapper;
    @Autowired
    private RebateService rebateService;


    public static final String CREATE_REBATE_REWARD_LOCK = "CREATE_REBATE_REWARD_LOCK";
    //锁定时间一小时
    public static final int REBATE_REWARD_LOCK_EXPIRE_TIME = 60 * 1000 * 60;
    //存放有奖励的币种
    public static List<Map> rebateCurrencyList = new ArrayList<Map>();
    //
    private static Properties properties = null;

    static {
        //从配置文件中读取奖励币种信息
        properties = PropertiesUtil.loadProperties("spring/rebate.properties");
        try {
            String size = properties.getProperty("size");
            if (null != size && Integer.parseInt(size) != 0) {
                for (int i = 1; i <= Integer.parseInt(size); i++) {
                    Map<String, Object> currencys = new HashMap<>();
                    currencys.put("currency", properties.getProperty("currency" + i));
                    currencys.put("rewardNum", new BigDecimal(properties.getProperty("rewardNum" + i)));
                    currencys.put("staticMult", new BigDecimal(properties.getProperty("staticMult" + i)));
                    currencys.put("inviteMult", new BigDecimal(properties.getProperty("inviteMult" + i)));
                    currencys.put("openTime", properties.getProperty("openTime" + i));
                    rebateCurrencyList.add(currencys);
                }
                logger.warn("<====================读取rebate配置文件成功");
            }
        } catch (Exception e) {
            logger.warn("<====================读取rebate配置文件失败");
        }
    }

    //项目停止
   //@Scheduled(cron = "${rebate_cron}")
    public void createRebateData() {
        boolean isLock = JedisUtil.getInstance().getLock(CREATE_REBATE_REWARD_LOCK, IPAddressPortUtil.IP_ADDRESS, REBATE_REWARD_LOCK_EXPIRE_TIME);
        if (isLock) {
            long startTime = System.currentTimeMillis();
            try {
                //半小时之前
                //先查一条，如果有就表示今天已经生成过了，直接退出
                String sql = "select COUNT(*) from m_account_rebate WHERE create_time >'" + HelpUtils.dateToString(new Date(startTime - 3 * 60 * 60 * 1000)) + "'";
                Integer num = daoUtil.queryForInt(sql);
                if (0 != num) {
               //       return;
                }
                //遍历有返利项目的币种
                for (Map currencyMap : rebateCurrencyList) {
                    long time = DateUtils.differentDaysBySecond((String) currencyMap.get("openTime"), null);
                    if (time < StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME) {
                        logger.error("币种" + currencyMap.get("currency") + "未到开启奖励时间");
                        continue;
                    }
                    //找到符合静态奖励的用户
                    List rewardMemberList = findStaticRewardMember(currencyMap);
                    //list储存返利信息
                    List<AccountRebate> accountRebateList = new ArrayList<>();
                    if (null != rewardMemberList) {
                        for (int i = 0; i < rewardMemberList.size(); i++) {
                            Map rewardMap = (Map) rewardMemberList.get(i);
                            AccountRebate accountRebate = new AccountRebate();
                            try {
                                accountRebate.setMemberId((Integer) rewardMap.get("id"));
                                accountRebate.setCurrency((String) currencyMap.get("currency"));
                                accountRebate.setTotalBalance((BigDecimal) rewardMap.get("total_balance"));
                                accountRebate.setFrozenBalance((BigDecimal) rewardMap.get("frozen_balance"));
                                accountRebate.setAvailableBalance(accountRebate.getTotalBalance().subtract(accountRebate.getFrozenBalance()));
                                accountRebate.setRebateBalance(accountRebate.getAvailableBalance().multiply((BigDecimal) currencyMap.get("staticMult")));
                                accountRebate.setrStatus(0);
                                accountRebate.setrType(0);
                                int introduceMId = rewardMap.get("introduce_m_id") == null ? 0 : Integer.parseInt((String) rewardMap.get("introduce_m_id"));
                                accountRebate.setIntroduceMId(introduceMId);
                                accountRebateList.add(accountRebate);
                            } catch (Exception e) {
                                logger.error("用户ID" + rewardMap.get("id") + "生成静态返利奖励失败" + e.getMessage());
                            }
                        }
                    }
                    //找到符合动态邀请奖励的用户
                    rewardMemberList = selectInviteRewardList(currencyMap);
                    if (null != rewardMemberList) {
                        for (int i = 0; i < rewardMemberList.size(); i++) {
                            Map rewardMap = (Map) rewardMemberList.get(i);
                            AccountRebate accountRebate = new AccountRebate();
                            try {
                                accountRebate.setMemberId((Integer) rewardMap.get("id"));
                                accountRebate.setCurrency((String) currencyMap.get("currency"));
                                accountRebate.setTotalBalance((BigDecimal) rewardMap.get("total_balance"));
                                accountRebate.setFrozenBalance((BigDecimal) rewardMap.get("frozen_balance"));
                                accountRebate.setAvailableBalance(accountRebate.getTotalBalance().subtract(accountRebate.getFrozenBalance()));
                                accountRebate.setRebateBalance(accountRebate.getAvailableBalance().multiply((BigDecimal) currencyMap.get("inviteMult")));
                                accountRebate.setrStatus(0);
                                //两种类型区别在rtype上
                                accountRebate.setrType(1);
                                //介绍人ID是String类型的
                                int introduceMId = rewardMap.get("introduce_m_id") == null ? 0 : Integer.parseInt((String) rewardMap.get("introduce_m_id"));
                                accountRebate.setIntroduceMId(introduceMId);
                                accountRebateList.add(accountRebate);
                            } catch (Exception e) {
                                logger.error("用户ID" + rewardMap.get("id") + "生成邀请返利奖励失败，邀请人ID" + rewardMap.get("introduce_m_id") + e.getMessage());
                            }
                        }
                    }
                    //批量插入数据
                    rebateMapper.insertRebateForeach(accountRebateList);
                    //执行返利
                    accountRebateList = rebateMapper.getUnRewardedAccountRebateList();
                    if (null != accountRebateList) {
                        for (int i = 0; i < accountRebateList.size(); i++) {
                            try {
                                rebateService.transfer(accountRebateList.get(i));
                            }catch (Exception e){
                                e.getMessage();
                                logger.error(accountRebateList.get(i).getId()+"返利执行失败");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.warn("生成返利奖励数据失败" + e.getMessage());
            } finally {
                boolean isRelease = JedisUtil.getInstance().releaseLock(CREATE_REBATE_REWARD_LOCK, IPAddressPortUtil.IP_ADDRESS);
                if (!isRelease) {
                    logger.warn("生成返利数据锁释放失败，耗时" + (System.currentTimeMillis() - startTime) + "ms");
                }
            }

        }
    }


    //找到符合静态奖励的用户
    public List<Map> findStaticRewardMember(Map param) {
        return rebateMapper.selectStaticRewardList(param);
    }

    //找到符合动态邀请奖励的客户
    public List<Map> selectInviteRewardList(Map param) {
        return rebateMapper.selectInviteRewardList(param);
    }

}
