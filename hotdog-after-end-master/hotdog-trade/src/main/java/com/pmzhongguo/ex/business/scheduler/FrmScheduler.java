package com.pmzhongguo.ex.business.scheduler;

import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.service.*;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedCaseInsensitiveMap;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class FrmScheduler {
    private Logger logger = LoggerFactory.getLogger(FrmScheduler.class);

    public static final String CRON_JOB_RUN_LOG = "CRON_JOB_RUN_LOG";
    public static final String CRON_JOB_START_LOG = "CRON_JOB_START_LOG";
    public static final String RELEASE_LOCK_PRE = "RELEASE_LOCK_PRE";
    private static final int RELEASE_LOCK_REDIS_LOCK_EXPIRE_TIME = 2 * 1000;
    public static final String BATCH_UNFROZEN_LOCK = "BATCH_UNFROZEN_LOCK";
    private static final String BATCH_CURRENCY_RECHARGE_AND_WITHDRAW = "BATCH_CURRENCY_RECHARGE_AND_WITHDRAW";
    private static final int BATCH_CURRENCY_RECHARGE_AND_WITHDRAW_EXPIRE_TIME = 60 * 1000;
    //解冻资产锁持续20分钟
    private static final int BATCH_UNFROZEN_LOCK_EXPIRE_TIME = 60 * 1000 * 20;
    /**
     * 排名查询锁持有的时间 2 分钟
     */
    private static final int TRANK_LOCK_REDIS_LOCK_EXPIRE_TIME = 60 * 2 * 1000;

    @Autowired
    DaoUtil daoUtil;

    /**
     * eabnk交易排名
     */
    public static final String EBANK_TRANK_LOCK_PRE = "EBANK_TRANK_LOCK_PRE";
    public static final String EBANK_TRANK_DATA = "_RANK_DATA";
    public static final String EBANK_TRANK_DATA_KEY = EBANK_TRANK_LOCK_PRE + EBANK_TRANK_DATA;

    public static boolean isRunnable = false;
    private static Logger zkLog = LoggerFactory.getLogger("zookeeper");
    //	private static String localIP = IPAddressPortUtil.getIPAddress();
    @Autowired
    private MemberService memberService;
    @Autowired
    private OTCService otcService;
    @Autowired
    private TradeRankingService rankingService;

    @Autowired
    private LockListService lockListService;
    @Autowired
    private CurrencyService currencyService;

    public static String printTimeStamp() {
        Calendar ca = Calendar.getInstance();
        ca.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ", Locale.CHINA);
        //显示当前时间 精确到毫秒
        return sdf.format(ca.getTime());
    }

    private static void saveRedisLog(String key, String value, int saveLength) {
        JedisUtil.getInstance().lpush(key, value);
        JedisUtil.getInstance().ltrim(key, 0, saveLength);
    }


    public FrmScheduler() {
        super();
//		String taskIp = HelpUtils.getMgrConfig().getCron_job_ip();
//		if(taskIp != null) {
//			taskIp = taskIp.trim();
//		}
//		if(localIP.equals(taskIp)) {
        isRunnable = true;
//			logger.warn("本机 " + localIP + " 拿到计划任务权限。计划任务由本机执行。");
//		}else {
//			logger.warn("计划任务服务器地址:" + taskIp + "  本机地址:" + localIP);
//		}
//
//		saveRedisLog(CRON_JOB_START_LOG, localIP + "_" +  taskIp + "  " + printTimeStamp(), 15);
    }

    public void batchProc10SecTask() {
        if (!isRunnable) {
            return;
        }
//		saveRedisLog(CRON_JOB_RUN_LOG, localIP + "  " + printTimeStamp(), 9);
//		otcService.cacheOTCTicker();
    }


    //@Scheduled(cron = "0 0/30 * * * ?")
    public void batchProc30MinTask() {
        //logger.warn("进入30分钟");

        if (!isRunnable) {
            return;
        }
        //每次计划任务只需要一个服务器执行一次
        //用全局锁
        boolean isLock = false;
        isLock = JedisUtil.getInstance().getLock(BATCH_UNFROZEN_LOCK, IPAddressPortUtil.IP_ADDRESS, BATCH_UNFROZEN_LOCK_EXPIRE_TIME);
        if (isLock) {
            Long start = System.currentTimeMillis();
            try {
                memberService.batchUnFrozen();
            } finally {
                boolean isRelease = false;
                isRelease = JedisUtil.getInstance().releaseLock(BATCH_UNFROZEN_LOCK, IPAddressPortUtil.IP_ADDRESS);
                if (!isRelease) {
                    logger.warn("Release BATCH_UNFROZEN_LOCK failed, time cost" + (System.currentTimeMillis() - start));
                }
            }
        }
        //跟老铁确认过暂时用不到，先关闭。
        //以后需要了再开
//		marketService.cacheToBTCPriceData();
    }


    //  @Scheduled(cron = "0 0/3 * * * ?")
    public void batchProc1MinTask() {
        if (!isRunnable) {
            return;
        }
        otcService.refreshLastTime();
    }

    /**
     * 锁仓释放
     */
    public void releaseLockTask() {
        if (!isRunnable) {
            return;
        }
        String lockKey = RELEASE_LOCK_PRE;
        boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS, RELEASE_LOCK_REDIS_LOCK_EXPIRE_TIME, 200, 30);
        if (isLock) {
            long start = System.currentTimeMillis();
            try {
                lockListService.releaseLock();
            } catch (Exception e) {
                logger.warn("锁仓释放失败：", e.toString());
                e.printStackTrace();
            } finally {
                boolean isRelease = JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
                if (!isRelease) {
                    long end = System.currentTimeMillis();
                    logger.warn("release MEMBER_ACCOUNT_LOCK fail。key:{}  耗时 : {}  ms", lockKey, end - start);
                }
            }
        }
    }

    /**
     * ebank查询
     */
    public void findEbankTrank() {
        String lockKey = EBANK_TRANK_LOCK_PRE;
        boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS, TRANK_LOCK_REDIS_LOCK_EXPIRE_TIME, 200, 30);
        if (isLock) {
            long start = System.currentTimeMillis();
            try {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("count", 10);
                param.put("type", "ebank");
                List<Map<String, Object>> rankData = rankingService.getEncryptionAccountTradeRankingByZcAndUsdtList(param);
                JedisUtil.getInstance().del(EBANK_TRANK_DATA_KEY);
                JedisUtil.getInstance().lpush(EBANK_TRANK_DATA_KEY, rankData);
                System.out.println("ebank排名交易=====" + HelpUtils.getCurrTime());
            } catch (Exception e) {
                logger.error("ebank排名交易查询失败" + e.getMessage());
            } finally {
                boolean isRelease = JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
                if (!isRelease) {
                    long end = System.currentTimeMillis();
                    logger.warn("release TRANK_LOCK_REDIS_LOCK fail。key:{}  耗时 : {}  ms", lockKey, end - start);
                }
            }
        }

    }

    //检测币种充值提现是否开启
    @Scheduled(cron = " * */2 * * * ?")
    public void batchCurrencyRechargeAndWithdraw() {
        String lockKey = BATCH_CURRENCY_RECHARGE_AND_WITHDRAW;
        boolean isLock = JedisUtil.getInstance().getLock(lockKey,IPAddressPortUtil.IP_ADDRESS,BATCH_CURRENCY_RECHARGE_AND_WITHDRAW_EXPIRE_TIME);
        if (isLock) {
            try {
                List<Currency> currencyLst = HelpUtils.getCurrencyLst();
                Boolean isChanged = false;
                for (Currency currency : currencyLst) {
                    isChanged = isChanged == true ? true : openRecharge(currency);
                    isChanged = isChanged == true ? true : openWithdraw(currency);
                }
                //若有改动就进行同步
                if (isChanged) {
                    JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_CURRENCY,JedisChannelConst.SYNC_MESSAGE);
                }
            }catch (Exception e){
                logger.error("检测币种充值提现是否开启失败" + e.getMessage());
            }finally {
                boolean isRelease = JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
                if (!isRelease) {
                    logger.warn("release BATCH_CURRENCY_RECHARGE_AND_WITHDRAW fail");
                }
            }

        }
    }

    public boolean openRecharge(Currency currency) {
        //只有在能否充值为否的时候判断是否要开启
        if (currency.getCan_recharge() == 1 || currency.getRecharge_open_time() == null){
           return false;
        }
        long time = DateUtils.differentDaysBySecond(currency.getRecharge_open_time(), null);
        if (time > StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME){
            currency.setCan_recharge(1);
            currencyService.editCurrency(currency);
            return true;
        }
        return false;
    }

    public boolean openWithdraw(Currency currency) {
        //只有在能否提现为是的时候判断是否要开启
        if (currency.getCan_withdraw() == 1 || currency.getWithdraw_open_time() == null){
            return false;
        }
        long time = DateUtils.differentDaysBySecond(currency.getWithdraw_open_time(), null);
        if (time > StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME){
            currency.setCan_withdraw(1);
            currencyService.editCurrency(currency);
            return true;
        }
        return false;
    }


}
