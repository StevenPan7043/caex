package com.pmzhongguo.ex.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.contract.dao.CCustomerMapper;
import com.contract.entity.CCustomer;
import com.contract.entity.CCustomerExample;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.gson.Gson;
import com.pmzhongguo.ex.bean.SessionInfo;
import com.pmzhongguo.ex.business.dto.*;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.mapper.ContractCustomerMapper;
import com.pmzhongguo.ex.business.mapper.MemberMapper;
import com.pmzhongguo.ex.business.mapper.OTCMapper;
import com.pmzhongguo.ex.business.mapper.RuleMapper;
import com.pmzhongguo.ex.business.model.NoticeContentBuilder;
import com.pmzhongguo.ex.business.model.OssConstants;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.scheduler.UsdtCnyPriceScheduler;
import com.pmzhongguo.ex.business.service.manager.MemberServiceManager;
import com.pmzhongguo.ex.business.service.manager.NoticeManager;
import com.pmzhongguo.ex.business.vo.CoinRechargeExcelVo;
import com.pmzhongguo.ex.business.vo.CoinWithdrawExcelVo;
import com.pmzhongguo.ex.business.vo.MemberRspVo;
import com.pmzhongguo.ex.business.vo.WarehouseAccountVo;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.*;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.core.websocket.MySessionContext;
import com.pmzhongguo.ex.datalab.entity.AccountFee;
import com.pmzhongguo.ex.datalab.manager.AccountFeeChangeService;
import com.pmzhongguo.ex.datalab.manager.AccountFeeManager;
import com.pmzhongguo.ex.datalab.manager.AccountFeeReductionManager;
import com.pmzhongguo.ex.datalab.manager.AccountFeeThawManager;
import com.pmzhongguo.ex.datalab.service.AccountFeeService;
import com.pmzhongguo.ex.framework.entity.Country;
import com.pmzhongguo.otc.dao.AccountInfoMapper;
import com.pmzhongguo.otc.entity.dataobject.AccountInfoDO;
import com.pmzhongguo.otc.faceid.Base64Util;
import com.pmzhongguo.otc.faceid.FaceIdUtil;
import com.pmzhongguo.otc.otcenum.MobiInfoTemplateEnum;
import com.pmzhongguo.otc.sms.JuheSend;
import com.pmzhongguo.otc.sms.SmsSendPool;
import com.pmzhongguo.otc.utils.FunctionUtils;
import com.pmzhongguo.udun.constant.API;
import com.pmzhongguo.udun.constant.CoinType;
import com.pmzhongguo.udun.util.HttpUtil;
import com.pmzhongguo.udun.util.UdunUtil;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.zzextool.utils.StringUtil;
import com.qiniu.conts.CacheConst;
import com.qiniu.util.BeanUtil;
import com.qiniu.util.DateStyleEnum;
import com.qiniu.util.DateUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class MemberService extends BaseServiceSupport implements IDataProcess {
    private static final String COIN_RECHARGE_ADDR_REDIS_LOCK_PRE = "coin_recharge_addr_";  //EXPIRE_TIME
    private static final int COIN_RECHARGE_ADDR_REDIS_LOCK_EXPIRE_TIME = 2500;
//    private static final String EXCLUDE_MEMBER_ACCOUNT = PropertiesUtil.getPropValByKey("exclude_member_account");

    public static final String APITOKEN_PREFIX = "REDIS_APITOKEN_IS_NULL";
    private LoadingCache<String, String> apiTokenCache = CacheBuilder.newBuilder().initialCapacity(10).maximumSize(100)
            .expireAfterWrite(30, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    @Autowired
    private AccountDetailService accountDetailService;
    @Autowired
    private AccountInfoMapper accountInfoMapper;
    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private NoticeManager noticeManager;

    @Autowired
    private CurrencyLockReleaseService currencyLockReleaseService;

    @Autowired
    private CurrencyLockAccountService currencyLockAccountService;

    @Autowired
    RuleMapper ruleMapper;

    @Autowired
    MemberAuthRecordService memberAuthRecordService;


    @Autowired
    private AccountFeeService accountFeeService;

    @Autowired
    private MemberServiceManager memberServiceManager;

    @Autowired
    private AccountFeeReductionManager accountFeeReductionManager;

    @Autowired
    private AccountFeeThawManager accountFeeThawManager;

    // 对同一个用户，一个时刻只能一个线程访问，因此利用Guava进行变量锁
    private Interner<String> memberIdPool = Interners.newWeakInterner();

    // 对同一个用户，一个时刻只能一个线程访问，因此利用Guava进行变量锁
    private Interner<String> coinWithdrawIdPool = Interners.newWeakInterner();
    private static final String WITHDRAW_REDIS_KEY = "withdraw_redis_key";
    // 获取地址池Guava锁
    private Interner<String> currencyForAddrPool = Interners.newWeakInterner();

    @Autowired
    private OTCMapper otcMapper;

    @Autowired
    private ContractCustomerMapper contractCustomerMapper;

    @Autowired
    private RedisUtilsService redisUtilsService;


    @Autowired
    private WarehouseAccountService warehouseAccountService;

    public final static String API_TOKEN_NAME_PRE = "api_token_";

    private static final String MEMBER_ACCOUNT_REDIS_LOCK_PRE = "member_account_redis_lock_pre_";  //EXPIRE_TIME
    private static final int MEMBER_ACCOUNT_REDIS_LOCK_EXPIRE_TIME = 2 * 1000;

    ExecutorService pool = Executors.newFixedThreadPool(4, new ZzexThreadFactory("MemberService_AccountDetails"));
    ExecutorService facePool = Executors.newFixedThreadPool(1, new ZzexThreadFactory("MemberService_FaceIdUploader"));

    /**
     * 获得用户的资产，用于
     *
     * @param memberId
     * @return
     */
    public List<AccountDto> genMemberAccountsLst(Integer memberId) {
        List<AccountDto> accountLst = getAccounts(memberId);

        Map<String, Currency> currencyMap = HelpUtils.getCurrencyMap();
        for (AccountDto ad : accountLst) {
            Currency currency = currencyMap.get(ad.getCurrency());
            if (null == currency) {
                continue;
            }
            ad.setFrozen_balance(ad.getFrozen_balance().setScale(currency.getC_precision(), BigDecimal.ROUND_DOWN));
            ad.setBalance(ad.getBalance().setScale(currency.getC_precision(), BigDecimal.ROUND_DOWN));
            ad.setTotal_balance(ad.getTotal_balance().setScale(currency.getC_precision(), BigDecimal.ROUND_DOWN));
        }

        return accountLst;
    }

    /**
     * 根据条件查询充值记录
     * startTime	开始时间
     * endTime		结束时间
     * currency	币种
     *
     * @param param
     * @return
     */
    public List<CoinRechargeExcelVo> findCoinRechargeList(Map<String, Object> param) {
        List<CoinRechargeExcelVo> list = memberMapper.getCoinRechargeList(param);
        if (list != null && list.size() > HelpUtils.EXPORT_LIMIT_NUM) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.EXPORT_EXCEL_LIMIT_GT_5000.getErrorCNMsg());
        }

        Map<String, String> r_addressMap = ImmutableMap.<String, String>builder()
                .put("INVITE_REWARD", "推荐奖励")
                .put("MAN_RECHARGE", "平台人工充值")
                .put("REG_REWARD", "注册奖励")
                .put("SYS_REWARD", "系统奖励")
                .put("TRADE_RANKING_REWARD", "交易排名奖励")
                .put("TRADE_RETURN_FEE_REWARD", "交易返还手续费")
                .put("wxpay", "wxpay")
                .put("alipay", "alipay")
                .put("bank", "bank")
                .put("RETURN_COMMISSION", "返佣")
                .put("IEO众筹", "IEO众筹")
                .build();

        Map<Integer, String> r_statusMap = ImmutableMap.<Integer, String>builder()
                .put(-1, "待付款[OTC]")
                .put(0, "未确认")
                .put(1, "已确认")
                .put(2, "已取消")
                .build();
        String defaultSource = "币充值";
        for (CoinRechargeExcelVo e : list) {
            e.setR_status_desc(r_statusMap.get(e.getR_status()));
            String source = r_addressMap.get(e.getR_address()) == null ? defaultSource : r_addressMap.get(e.getR_address());
            e.setR_source(source);
        }

        return list;
    }

    /**
     * 根据条件查询充值记录
     * startTime	开始时间
     * endTime		结束时间
     * currency	币种
     *
     * @param param
     * @return
     */
    public List<CoinWithdrawExcelVo> findWithdrawCoinList(Map<String, Object> param) {
        List<CoinWithdrawExcelVo> list = memberMapper.getCoinWithdrawList(param);
        if (list != null && list.size() > HelpUtils.EXPORT_LIMIT_NUM) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.EXPORT_EXCEL_LIMIT_GT_5000.getErrorCNMsg());
        }


        Map<Integer, String> r_statusMap = ImmutableMap.<Integer, String>builder()
                .put(0, "待处理")
                .put(1, "已完成")
                .put(2, "已取消")
                .put(3, "处理中")
                .put(4, "已付款")
                .build();
        for (CoinWithdrawExcelVo e : list) {
            e.setW_toaccount(e.getW_amount().subtract(e.getW_fee()));
            e.setW_status_desc(r_statusMap.get(e.getW_status()));
        }

        return list;
    }

    /**
     * 查找用户所用的账号
     *
     * @param memberId 用户id
     * @return
     */
    public List<AccountDto> findAccountListByMemberId(Integer memberId) {
        return memberMapper.listAccounts(memberId);
    }

    public Member findMemberByInviteId(Integer introduce_m_id) {
        return memberMapper.findMemberByInviteId(introduce_m_id);
    }

    public Member findMemberByInviteCode(String inviteCode) {
        return memberMapper.findMemberByInviteCode(inviteCode);
    }


    class AccountDetails implements Runnable {

        Account account;
        BigDecimal num;
        String currency;
        Integer memberId;
        Integer procType;
        String optSource;

        public AccountDetails(Account account, BigDecimal num, String currency, Integer memberId, Integer procType, OptSourceEnum optSourceEnum) {
            super();
            this.account = account;
            this.num = num;
            this.currency = currency;
            this.memberId = memberId;
            this.procType = procType;
            this.optSource = null == optSourceEnum ? "" : optSourceEnum.getCode();


        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            accountDetailService.insert(new AccountDetailDto(memberId, currency, num, procType, account == null ? "" : account.toString(), optSource));
        }
    }

    /**
     * 冻结或解冻、增加余额
     *
     * @param num      操作数量
     * @param currency 操作币 BTC LTC CNY 等
     * @param memberId 会员ID
     * @param procType 1表示冻结[+frozen_balance]，2表示解冻[-frozen_balance，同时-total_balance]，3表示增加余额[+total_balance]，4表示还原冻结[-frozen_balance]（跟解冻的区别是：解冻是成交，还原冻结是取消）
     */
    public void accountProc(BigDecimal num, String currency, Integer memberId, Integer procType, OptSourceEnum optSourceEnum) {
        if (num.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        // 出现过解冻时，数量位数超过8位的情况，这里统一截断到8位
        // num = num.setScale(8, BigDecimal.ROUND_DOWN);

        // 只锁当前用户(利用guava的Interner的intern可以实现根据字符串同步，这里同时只允许同一个用户进入，不同的用户，不影响)
//		synchronized (memberIdPool.intern(String.valueOf(memberId))) {
        //将单机锁换成分布式锁
        String lockKey = MEMBER_ACCOUNT_REDIS_LOCK_PRE + memberId + "_" + currency;
        boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS, MEMBER_ACCOUNT_REDIS_LOCK_EXPIRE_TIME, 200, 30);
        if (isLock) {
            long start = System.currentTimeMillis();
            Account copyAccount = new Account();
            //判断是否在机器人列表里
//			boolean isDetail = EXCLUDE_MEMBER_ACCOUNT.indexOf("," + String.valueOf(memberId) + ",") < 0;
            Member m = getMemberById(memberId);
            boolean isDetail = (m.getApi_limit().intValue() == 0);

            try {
                Account account = this.getAccount(HelpUtils.newHashMap("currency", currency, "member_id", memberId));
                if (isDetail && null != account) {
                    //先把操作前的资产数据备份
                    BeanUtil.copy(account, copyAccount);
                }
                // 冻结
                if (1 == procType) {
                    // 该货币没有账户记录
                    if (null == account || account.getId() <= 0) {
                        logger.warn("operate:" + procType + " memberId:" + memberId + " " + currency + "'s account not exists");
                        throw new BusinessException(-1, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
                    }
                    BigDecimal availableBalance = account.getTotal_balance().subtract(account.getFrozen_balance());
                    // 可用余额不足
                    if (availableBalance.subtract(num).compareTo(BigDecimal.ZERO) < 0) {
                        logger.warn("operate:" + procType + " NOT_SUFFICIENT_FUNDS！memberId:" + memberId + " " + currency + " total:" + account.getTotal_balance().toPlainString()
                                + " frozen:" + account.getFrozen_balance().toPlainString() + ", Num:" + num.toPlainString());
                        throw new BusinessException(-1, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
                    }
                    // 余额够用，开始冻结
                    account.setFrozen_balance(num);
                    this.addFrozenBalance(account); // 这里特别注意MyBatis中，用加号操作Frozen_balance字段，避免多线程操作导致冻结额不准确。这里临时借用Frozen_balance作为本次冻结额
                } else if (2 == procType) { // 解冻
                    // 该货币没有账户记录
                    if (null == account || account.getId() <= 0) {
                        logger.warn("operate:" + procType + " memberId:" + memberId + " " + currency + "'s account not exists");
                        throw new BusinessException(-1, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
                    }

                    if (account.getTotal_balance().compareTo(num) == -1
                            || account.getFrozen_balance().compareTo(num) == -1) {
                        logger.error("operate:" + procType + " NOT_SUFFICIENT_FUNDS！memberId:" + memberId + " " + currency + " total:" + account.getTotal_balance().toPlainString()
                                + " frozen:" + account.getFrozen_balance().toPlainString() + ", Num:" + num.toPlainString());
                        throw new BusinessException(-1, ErrorInfoEnum.LANG_ACCOUNT_FUNDS_PROBLEM.getErrorENMsg());
                    }
                    // 账户存在，开始解冻
                    account.setFrozen_balance(num);
                    this.reduceFrozenBalance(account); // 这里特别注意MyBatis中，用减号操作Frozen_balance字段，避免多线程操作导致冻结额不准确。这里临时借用Frozen_balance作为本次解冻额
                } else if (3 == procType) { // 增加余额
                    // 该货币没有账户记录
                    if (null == account || account.getId() <= 0) {
                        if (num.compareTo(BigDecimal.ZERO) < 0) {
                            logger.warn("operate:" + procType + " memberId:" + memberId + " currency:" + currency + " num:" + num.stripTrailingZeros().toPlainString());
                            throw new BusinessException(-1, ErrorInfoEnum.TOTAL_BALANCE_LESS_THAN.getErrorENMsg());
                        }
                        account = new Account();
                        account.setTotal_balance(num);
                        account.setCurrency(currency);
                        account.setMember_id(memberId);
                        this.addAccount(account);
                    } else {
                        if (num.compareTo(BigDecimal.ZERO) < 0 && account.getTotal_balance().subtract(account.getFrozen_balance()).add(num).compareTo(BigDecimal.ZERO) < 0) {
                            logger.warn("operate:" + procType + " memberId:" + memberId + " currency:" + currency + " Total_balance:" + account.getTotal_balance() + " num:" + num.stripTrailingZeros().toPlainString());
                            throw new BusinessException(-1, ErrorInfoEnum.LACK_OF_EFFECTIVE_ASSETS.getErrorENMsg());
                        }
                        account.setTotal_balance(num);
                        this.addTotalBalance(account); // 这里特别注意MyBatis中，用减号操作total_balance字段，避免多线程操作导致余额不准确。这里临时借用total_balance作为本次余额
                    }
                } else if (4 == procType) { // 还原冻结
                    // 该货币没有账户记录
                    if (null == account || account.getId() <= 0) {
                        logger.warn("operate:" + procType + " memberId:" + memberId + " " + currency + "'s account not exists");
                        throw new BusinessException(-1, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
                    }

                    if (account.getFrozen_balance().compareTo(num) == -1) {
                        logger.warn(currency + "'s account(" + memberId + ") error, frozen:"
                                + account.getFrozen_balance().toPlainString() + ", returnNum:" + num.toPlainString());
                        throw new BusinessException(-1, ErrorInfoEnum.NOT_SUFFICIENT_FUNDS.getErrorENMsg());
                    }
                    // 账户存在，开始还原冻结
                    if (num.compareTo(BigDecimal.ZERO) == 1) {
                        account.setFrozen_balance(num);
                        this.returnFrozenBalance(account); // 这里特别注意MyBatis中，用减号操作Frozen_balance字段，避免多线程操作导致冻结额不准确。这里临时借用Frozen_balance作为本次还原额
                    }

                }
            } finally {
                boolean isRelease = JedisUtil.getInstance().releaseLock(lockKey, IPAddressPortUtil.IP_ADDRESS);
                if (!isRelease) {
                    long end = System.currentTimeMillis();
                    logger.warn("procType:" + procType + " release MEMBER_ACCOUNT_LOCK fail。key:{}  耗时 : {}  ms", lockKey, end - start);
                }
            }
            if (isDetail) {
                //如果没出异常就记录
                pool.execute(new AccountDetails(copyAccount, num, currency, memberId, procType, optSourceEnum));
            }
        } else {
            String msg = "get MEMBER_ACCOUNT_LOCK fail。key:" + lockKey + "。操作类型：" + procType + " currency：" + currency + " 金额：" + num.toPlainString();
            logger.warn(msg);
            throw new BusinessException(-1, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg());
        }

    }

    public void addMember(Member member) {
        memberMapper.addMember(member);
    }

    public void addCoinWithdrawAddr(CoinWithdrawAddr coinWithdrawAddr) {
        memberMapper.addCoinWithdrawAddr(coinWithdrawAddr);
    }

    public void delCoinWithdrawAddr(CoinWithdrawAddr coinWithdrawAddr) {
        memberMapper.delCoinWithdrawAddr(coinWithdrawAddr);
    }

    /**
     * @param member
     * @param isReg   是否注册
     * @param needLog 是否需要记录日志
     */
    public void updateMember(Member member, boolean isReg, boolean needLog) {
        Member oldMember = getMemberById(member.getId());

        if (null != member.getTrade_commission()
                && null != member.getM_status()
                && null != member.getApi_status()
                && null != member.getApi_limit()
                && (oldMember.getTrade_commission().doubleValue() != member.getTrade_commission().doubleValue()
                || oldMember.getM_status().intValue() != member.getM_status().intValue()
                || oldMember.getApi_status().intValue() != member.getApi_status().intValue()
                || oldMember.getApi_limit().intValue() != member.getApi_limit().intValue()
        )) {
            // 用户状态、佣金折扣、api状态有一个发生改变就刷新
            refreshApiToken(member);

        }

        memberMapper.updateMember(member);
        if (needLog) {
            daoUtil.update("insert into m_member_oper_log values(null, ?, ?, 1, ?, ?, ?, ?)", member.getId(),
                    member.getM_name(), member.getLast_login_time(), "Login", member.getLast_login_ip(), member.getLast_login_device());
        }
    }

    public void updateMember(Member member) {
        memberMapper.updateMember(member);
    }

    /**
     * 短信验证码无效
     *
     * @param member
     */
    public void updateMemberSmsCode(Member member) {
        if (member != null) {
            member.setSms_code(HelpUtils.randomNumNo0(4));
            memberMapper.updateMember(member);
        }
    }


    public void refreshApiToken(Member member) {

        List<ApiToken> apiTokenLst = memberMapper.getApiTokensInner(member.getId());

        for (ApiToken apiToken : apiTokenLst) {
            if (member.getApi_status() == 2 || member.getM_status() == 2) {
                // 删除Redis
                JedisUtil.getInstance().del(API_TOKEN_NAME_PRE + apiToken.getApi_key());
            } else {
                apiToken.setTrade_commission(member.getTrade_commission());
                // 修改是否限制
                apiToken.setApi_limit(member.getApi_limit());
                JedisUtil.getInstance().set(API_TOKEN_NAME_PRE + apiToken.getApi_key(), apiToken, false);
            }
        }
    }

    public List getAllMember(Map params) {
        return memberMapper.listMemberPage(params);
    }

    public List listMemberWithdrawAddrById(Map params) {
        return memberMapper.listMemberWithdrawAddrById(params);
    }

    public Member getMemberById(Integer id) {
        return memberMapper.getMemberById(id);
    }

    public List<MemberRspVo> getMemberListByIdStr(String idStr) {
        return memberMapper.getMemberByIdList(idStr);
    }

    public List<MemberRspVo> getMemberListByIntroIdStr(String idStr) {
        return memberMapper.getMemberListByIntroIdStr(idStr);
    }

    public Demand getDemandById(Integer id) {
        return memberMapper.getDemandById(id);
    }

    public Member getMemberBy(Map params) {
        return memberMapper.getMemberBy(params);
    }

    /**
     * 获取推荐人信息
     *
     * @return
     */
    public ObjResp getLeMemberMap(String direction, String level, Member member) {

        List<MemberRspVo> upMemberList = new ArrayList<>();
        List<MemberRspVo> downMemberList = new ArrayList<>();
        Map<String, List<MemberRspVo>> leMemberMap = new HashMap<>();
        StringBuilder upIds = new StringBuilder();
        StringBuilder downIds = new StringBuilder();

        if (direction.toLowerCase().trim().equals("up") && !member.getIntroduce_m_id().equals("0")) {
            upIds.append(member.getIntroduce_m_id());
            this.getUpMemberList(upIds, Integer.valueOf(level), upMemberList);
        } else if (direction.toLowerCase().trim().equals("down") && !member.getIntroduce_m_id().equals("0")) {
            downIds.append(member.getId());
            this.getDownMemberList(downIds, Integer.valueOf(level), downMemberList);

        } else if (StringUtils.isBlank(direction)) {
            upIds.append(member.getIntroduce_m_id());
            this.getUpMemberList(upIds, Integer.valueOf(level), upMemberList);

            downIds.append(member.getId());
            this.getDownMemberList(downIds, Integer.valueOf(level), downMemberList);
        }
        leMemberMap.put("up", upMemberList);
        leMemberMap.put("down", downMemberList);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, leMemberMap);
    }

    private void getUpMemberList(StringBuilder upIds, Integer level, List<MemberRspVo> upMemberList) {
        for (int i = 0; i < level; i++) {
            if (StringUtils.isBlank(upIds)) {
                break;
            }
            String upStr = "(" + upIds + ")";
            List<MemberRspVo> memberListResult = this.getMemberListByIdStr(upStr);
            upIds = new StringBuilder();
            if (!CollectionUtils.isEmpty(memberListResult)) {
                for (MemberRspVo memberVoItem : memberListResult) {
                    memberVoItem.setLevel(i + 1);
                    if (!StringUtils.isEmpty(memberVoItem.getIntroduce_m_id()) && memberVoItem.getIntroduce_m_id().equals("0")) {
                        continue;
                    }
                    upIds.append(memberVoItem.getIntroduce_m_id()).append(",");
                }
            }
            upMemberList.addAll(memberListResult);
            if (StringUtils.isBlank(upIds)) {
                break;
            } else {
                upIds = new StringBuilder(upIds.substring(0, upIds.length() - 1));
            }
        }
    }

    private void getDownMemberList(StringBuilder downIds, Integer level, List<MemberRspVo> upMemberList) {
        for (int i = 0; i < level; i++) {
            if (StringUtils.isBlank(downIds)) {
                break;
            }
            String down = "(" + downIds + ")";
            List<MemberRspVo> memberListResult = this.getMemberListByIntroIdStr(down);
            downIds = new StringBuilder();
            if (!CollectionUtils.isEmpty(memberListResult)) {
                for (MemberRspVo memberVoItem : memberListResult) {
                    memberVoItem.setLevel(i + 1);
                    downIds.append(memberVoItem.getId()).append(",");
                }
            }
            upMemberList.addAll(memberListResult);
            if (StringUtils.isBlank(downIds)) {
                break;
            } else {
                downIds = new StringBuilder(downIds.substring(0, downIds.length() - 1));
            }
        }
    }

    public Resp editMember(Map param, Member member) {
        try {
            logger.info("会员管理更新记录：【{}】", new Gson().toJson(param));
            AuthIdentity authIdentity = new AuthIdentity();
            if (!BeanUtil.isEmpty(member.getAuth_grade()) && member.getAuth_grade() != 0) {
                authIdentity = this.getAuthIdentityById(member.getId());
                if (!BeanUtil.isEmpty(authIdentity)) {
                    authIdentity.setId_status(0);
                    authIdentity.setFamily_name(String.valueOf(param.get("family_name")));
                    authIdentity.setGiven_name(String.valueOf(param.get("given_name")));
                    memberMapper.memberUpdateAuthIdentity(authIdentity);
                }
                List<AccountInfoDO> accountInfoList = accountInfoMapper.findByConditionPage(HelpUtils.newHashMap("memberId", member.getId(), "isDelete", 0));
                if (!CollectionUtils.isEmpty(accountInfoList)) {
                    for (AccountInfoDO accountInfoDO : accountInfoList) {
                        accountInfoDO.setName(String.valueOf(param.get("family_name")) + String.valueOf(param.get("given_name")));
                        accountInfoMapper.updateByPrimaryKeySelective(accountInfoDO);
                    }
                }
            }
            this.updateMember(member, false, false);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return new Resp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg());
        }
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * 获得可用（未冻结）余额
     *
     * @param params
     * @return
     */
    public Account getAccount(Map params) {
        return memberMapper.getAccount(params);
    }

    public Account getOTCAccount(Map params) {
        return memberMapper.getOTCAccount(params);
    }

    public void addFrozenBalance(Account account) {
        memberMapper.addFrozenBalance(account);
    }

    public void reduceFrozenBalance(Account account) {
        memberMapper.reduceFrozenBalance(account);
    }

    public void addAccount(Account account) {
        memberMapper.addAccount(account);
    }

    public void updateAccount(Account account) {
        memberMapper.updateAccount(account);
    }

    public void addTotalBalance(Account account) {
        memberMapper.addTotalBalance(account);
    }

    /**
     * 根据用户id 批量 新增账户ZC余额
     *
     * @param list
     */
    public void batchAddZCTotalBalanceByMemberId(List<Map<String, Object>> list) {
        memberMapper.batchAddZCTotalBalanceByMemberId(list);
    }

    public void returnFrozenBalance(Account account) {
        memberMapper.returnFrozenBalance(account);
    }

    public List<AccountDto> getAccounts(Integer memberId) {
        List<CurrencyLockReleaseDto> waitLockReleaseDtoList = currencyLockReleaseService.findWaitReleaseByMemberId(memberId);
        Map<String, TickerResp> ticker = MarketService.getTicker();
        List<AccountDto> accountDtos = memberMapper.listAccounts(memberId);
        if (ticker == null) {
            return accountDtos;
        }
        for (AccountDto accountDto : accountDtos) {

            if (accountDto.getCurrency().equalsIgnoreCase(HelpUtils.OFFICIAL_CURRENCY)) {
                accountDto.setZc_balance(accountDto.getTotal_balance().multiply(UsdtCnyPriceScheduler.PRICE));
                continue;
            }
            BigDecimal close = getCurrentPairCloseForZC(accountDto, ticker);
            if (close == null) {
                accountDto.setZc_balance(BigDecimal.ZERO);
                continue;
            }
            close = close.multiply(UsdtCnyPriceScheduler.PRICE).setScale(8, BigDecimal.ROUND_HALF_UP);
            // 该币的总数 * zc汇率最新价
            BigDecimal balance = accountDto.getTotal_balance().multiply(close).setScale(8, BigDecimal.ROUND_HALF_UP);
            BigDecimal lockWaitNumZcBalance = BigDecimal.ZERO;
            if (!CollectionUtils.isEmpty(waitLockReleaseDtoList)) {
                // 锁仓待释放币种数量折合ZC价格，从list中找到币种名称相同的任意一个即可
                CurrencyLockReleaseDto waitLockReleaseDto =
                        waitLockReleaseDtoList.stream().filter(e -> accountDto.getCurrency()
                                .equals(e.getCurrency())).findAny().orElse(null);
                BigDecimal waitVolume = waitLockReleaseDto == null ? BigDecimal.ZERO : waitLockReleaseDto.getReleaseVolume();
                lockWaitNumZcBalance = getZcBalance4Currency(accountDto.getCurrency().toUpperCase(), waitVolume);
                lockWaitNumZcBalance = lockWaitNumZcBalance.multiply(UsdtCnyPriceScheduler.PRICE).setScale(8, BigDecimal.ROUND_HALF_UP);
            }
            accountDto.setZc_balance(balance.add(lockWaitNumZcBalance));
        }
        return accountDtos;
    }

    /**
     * 获取某个币的zc折合价格
     *
     * @param currency 币种，例如：ETH
     * @param num      数量
     * @return
     */
    public BigDecimal getZcBalance4Currency(String currency, BigDecimal num) {
        // 是zc或等于0就直接返回
        if (HelpUtils.getOfficialCurrencyToUpper().equals(currency) || BigDecimal.ZERO.compareTo(num) == 0) {
            return num;
        }
        String official = HelpUtils.getOfficialCurrencyToLower();
        Currency currencyInfo = HelpUtils.getCurrencyMap().get(currency.toUpperCase());
        Map<String, TickerResp> ticker = MarketService.getTicker();
        // 从zc去那汇率
        TickerResp tickerResp = ticker.get(currency.toLowerCase() + official + "_ticker");
        if (tickerResp != null) {
            return tickerResp.getClose().multiply(num).setScale(currencyInfo.getC_precision(), BigDecimal.ROUND_HALF_UP);
        }
        // zc区没有从eth区拿
        tickerResp = ticker.get(currency.toLowerCase() + "eth_ticker");
        if (tickerResp != null) {
            TickerResp ethzc_ticker = ticker.get("eth" + official + "_ticker");
            if (ethzc_ticker != null) {
                return ethzc_ticker.close.multiply(tickerResp.close).multiply(num).setScale(currencyInfo.getC_precision(), BigDecimal.ROUND_HALF_UP);
            }
        }
        // zc，eth区没有从usdt区拿
        tickerResp = ticker.get(currency.toLowerCase() + "btc_ticker");
        if (tickerResp != null) {
            TickerResp usdtzc_ticker = ticker.get("btc" + official + "_ticker");
            if (usdtzc_ticker != null) {
                return usdtzc_ticker.close.multiply(tickerResp.close).multiply(num).setScale(currencyInfo.getC_precision(), BigDecimal.ROUND_HALF_UP);
            }

        }
        return num;
    }

    /**
     * 找到币的交易行情汇率
     *
     * @param accountDto
     * @param ticker
     * @return
     */
    public BigDecimal getCurrentPairCloseForZC(AccountDto accountDto, Map<String, TickerResp> ticker) {
        String official = HelpUtils.getOfficialCurrencyToLower();
        TickerResp tickerResp = ticker.get(accountDto.getCurrency().toLowerCase() + official + "_ticker");
        if (tickerResp != null) {
            return tickerResp.getClose();
        }
        tickerResp = ticker.get(accountDto.getCurrency().toLowerCase() + "eth_ticker");
        if (tickerResp != null) {
            TickerResp ethzc_ticker = ticker.get("eth" + official + "_ticker");
            if (ethzc_ticker == null) {
                return null;
            }
            return ethzc_ticker.close.multiply(tickerResp.close).setScale(8, BigDecimal.ROUND_HALF_UP);
        }
        tickerResp = ticker.get(accountDto.getCurrency().toLowerCase() + "btc_ticker");
        if (tickerResp != null) {
            TickerResp usdtzc_ticker = ticker.get("btc" + official + "_ticker");
            if (usdtzc_ticker == null) {
                return null;
            }

            return usdtzc_ticker.close.multiply(tickerResp.close).setScale(8, BigDecimal.ROUND_HALF_UP);
        }
        return null;
    }

    /**
     * 更新提现记录，可能的情况是后台管理员审核（通过、不通过）、会员自己取消提现，都放到这个方法的原因是需要做同步处理
     *
     * @param coinWithdraw
     * @param isMemberCancel
     */
    public void updateCoinWithdraw(CoinWithdraw coinWithdraw, boolean isMemberCancel) {
        String key = WITHDRAW_REDIS_KEY + coinWithdraw.getMember_id();
        boolean isLock = JedisUtil.getInstance().getLockRetry(key
                , IPAddressPortUtil.IP_ADDRESS, 5000, 5, 10);
//        synchronized (coinWithdrawIdPool.intern(String.valueOf(coinWithdraw.getId()))) {
//        }
        if (!isLock) {
            logger.warn("获取锁失败，用户id" + coinWithdraw.getMember_id());
            throw new BusinessException(-1, ErrorInfoEnum.LANG_FREQUENCY_OPERTION.getErrorENMsg());
        }
        try {
            boolean isSend = false;
            CoinWithdraw coinWithdrawServer = this.getCoinWithdraw(coinWithdraw.getId());
            if (coinWithdrawServer == null) {
                throw new BusinessException(-1, ErrorInfoEnum.LANG_ILLEGAL_WITHDRAW_ID.getErrorENMsg());
            }
            if (coinWithdrawServer.getW_status() != 0 && isMemberCancel) {
                throw new BusinessException(-1, ErrorInfoEnum.LANG_WITHDRAW_STATUS_IS_NOT_WATTING.getErrorENMsg());
            }

            if (coinWithdrawServer.getW_status() != 0 && coinWithdrawServer.getW_status() != 3 && !isMemberCancel) {
                throw new BusinessException(-1, ErrorInfoEnum.LANG_WITHDRAW_STATUS_IS_NOT_WATTING.getErrorENMsg());
            }

            if (isMemberCancel) {
                coinWithdraw.setReject_reason("用户主动取消");
                coinWithdraw.setAuditor("admin");
            } else {
                // 不为空的情况是提现客户端过来的，那边已经设置了
                if (null == coinWithdraw.getAuditor()) {
                    coinWithdraw.setAuditor(HelpUtils.getFrmUser().getUser_name());
                }
            }

            // 设置为处理中时，判断余额和冻结额的关系
            // 获得账户情况，确定不会冻结大于总余额
            Double frozenToBalance = 0.0;
            if (coinWithdrawServer.getOtc_ads_id() > 0) {
                frozenToBalance = daoUtil.queryForObject(
                        "SELECT total_balance - frozen_balance FROM m_account WHERE member_id = ? AND currency = ?",
                        Double.class, coinWithdrawServer.getMember_id(), coinWithdrawServer.getOtc_oppsite_currency());
            } else {
                frozenToBalance = daoUtil.queryForObject(
                        "SELECT total_balance - frozen_balance FROM m_account WHERE member_id = ? AND currency = ?",
                        Double.class, coinWithdrawServer.getMember_id(), coinWithdrawServer.getCurrency());
            }

            if (!isMemberCancel && coinWithdraw.getW_status() != 2 && frozenToBalance < 0) {
                throw new BusinessException(-1, ErrorInfoEnum.LANG_ACCOUNT_HAS_PROBLEM.getErrorENMsg(), null);
            }

            // 用户取消或者后台审核员取消
            if (isMemberCancel || (!isMemberCancel && coinWithdraw.getW_status() == 2)) {
                coinWithdraw.setW_status(2); // 用户取消的时候，coinWithdraw传过来不是2,这里设置为2
                if (coinWithdrawServer.getOtc_ads_id() > 0) { // OTC是还原对方货币
                    this.accountProc(coinWithdrawServer.getOtc_volume(), coinWithdrawServer.getOtc_oppsite_currency(),
                            coinWithdrawServer.getMember_id(), 4, OptSourceEnum.OLDOTC);
//					// TODO 返还OTC交易限额

//					BigDecimal otcAmount = otcVolume.multiply(coinWithdrawServer.getOtc_price());
//					Map<String, Object> paramMap = new HashMap<String, Object>(2);
//					paramMap.put("id", otcAdsId);
//					paramMap.put("maxQuote", otcVolume);
//					paramMap.put("volume", otcVolume);
//					paramMap.put("amount", otcAmount);
//					paramMap.put("status", 1);
//					otcMapper.updateOtcAds(paramMap);
                } else {
                    this.accountProc(coinWithdrawServer.getW_amount(), coinWithdrawServer.getCurrency(),
                            coinWithdrawServer.getMember_id(), 4, OptSourceEnum.ZZEX);
                }

            } else if (coinWithdraw.getW_status() == 1) { // 审核通过才解冻，因为加了处理中的状态
                if (coinWithdrawServer.getOtc_ads_id() > 0) { // OTC是解冻对方货币
                    // 卖出修改广告限额 , 总数量总金额最大限额添加
                    Integer otcAdsId = coinWithdrawServer.getOtc_ads_id();
                    BigDecimal otcVolume = coinWithdrawServer.getOtc_volume();
                    OTCAds ads = otcMapper.loadOTCAdsById(otcAdsId);
                    daoUtil.update(
                            "update otc_ads set total_volume = total_volume + ?, total_amount = total_amount + ? where id = ? ",
                            otcVolume, ads.getPrice().multiply(otcVolume), otcAdsId);
                    daoUtil.update(
                            "update otc_ads set max_quote=max_quote+? where owner_id = ? and base_currency=?",
                            otcVolume, ads.getOwner_id(), ads.getBase_currency());

                    this.accountProc(coinWithdrawServer.getOtc_volume(), coinWithdrawServer.getOtc_oppsite_currency(),
                            coinWithdrawServer.getMember_id(), 2, OptSourceEnum.OLDOTC);
                } else {
                    this.accountProc(coinWithdrawServer.getW_amount(), coinWithdrawServer.getCurrency(),
                            coinWithdrawServer.getMember_id(), 2, OptSourceEnum.ZZEX);
                }
                isSend = true;

            } else if (coinWithdraw.getW_status() == 4) {
//				if (coinWithdrawServer.getOtc_ads_id() > 0) {
//					this.accountProc(coinWithdrawServer.getOtc_volume(), coinWithdrawServer.getOtc_oppsite_currency(),
//						coinWithdrawServer.getMember_id(), 2);
//				}
            }
            coinWithdraw.setAudit_time(HelpUtils.formatDate8(new Date()));
            memberMapper.updateCoinWithdraw(coinWithdraw);
            if (isSend) {
                BigDecimal withdrawAmount = coinWithdrawServer.getW_amount().subtract(coinWithdrawServer.getW_fee());
                NoticeContentBuilder parse
                        = new NoticeContentBuilder(MobiInfoTemplateEnum.JH_WITHDRAWAL_CODE.getType()
                        , coinWithdrawServer.getCurrency().toUpperCase()
                        , HelpUtils.formatDate8(new Date())
                        , withdrawAmount.stripTrailingZeros().toPlainString());
                if (!StringUtil.isNullOrBank(parse.getContent())) {
                    noticeManager.sendByMemberId(coinWithdrawServer.getMember_id(), parse.getContent());
                }
            }
        } finally {
            JedisUtil.getInstance().releaseLock(key, IPAddressPortUtil.IP_ADDRESS);
        }
    }

    public CoinWithdraw getCoinWithdraw(Map<String, Object> orderMap) {
        return memberMapper.getCoinWithdraw(orderMap);
    }

    public CoinWithdraw getCoinWithdraw(Long id) {
        return memberMapper.getCoinWithdrawByID(id);
    }

    public List<CoinWithdraw> getAllCoinWithdraw(Map param) {
        return memberMapper.listCoinWithdrawPage(param);
    }

    public List<CoinWithdraw> getMemberCoinWithdraw(Map param) {
        return memberMapper.listCoinWithdrawMemberPage(param);
    }

    public List<CoinRecharge> getAllCoinRecharge(Map param) {
        return memberMapper.listCoinRechargePage(param);
    }

    public List<Map> getAllManFrozen(Map param) {
        return memberMapper.listManFrozenPage(param);
    }

    public ManFrozen getManFrozenById(Long id) {
        return memberMapper.getManFrozenById(id);
    }

    public List<CoinRecharge> getMemberCoinRecharge(Map param) {
        return memberMapper.listCoinRechargeMemberPage(param);
    }

    public AuthIdentity getAuthIdentityById(Integer id) {
        return memberMapper.getAuthIdentityByID(id);
    }

    public List<AuthIdentity> getAllAuthIdentity(Map param) {
        return memberMapper.listAuthIdentityPage(param);
    }

    public void addAuthIdentity(AuthIdentity authIdentity) {

        authIdentity.setLast_submit_time(HelpUtils.formatDate8(new Date()));

        // 根据情况判断是新增还是修改
        AuthIdentity ai = memberMapper.getAuthIdentityByID(authIdentity.getId());
        if (null == ai) {
            memberMapper.addAuthIdentity(authIdentity);
            String realName = (StringUtils.isEmpty(authIdentity.getFamily_name()) ? "" : authIdentity.getFamily_name()) +
                    (StringUtils.isEmpty(authIdentity.getGiven_name()) ? "" : authIdentity.getGiven_name());
            synCustomer(authIdentity.getId(), realName, authIdentity.getId_number(), 3);
        } else {
            if (ai.getId_status() == 2) { // 只有审核不通过才修改
                //原来走人工验证，现在走人脸验证/人工验证
                memberMapper.memberUpdateAuthIdentity(authIdentity);
            } else if (ai.getId_status() == 3) {
                memberMapper.memberUpdateAuthIdentity(authIdentity);
//                if (authIdentity.getId_status() == 3) {
//                    //原来走人脸验证，现在走人脸验证
//                    memberMapper.memberUpdateAuthIdentityByFaceId(authIdentity);
//                }else {
//                    //原来走人脸验证，现在进行人工验证
//                    memberMapper.memberUpdateAuthIdentity(authIdentity);
//                }
            } else if (ai.getId_status() == 1) {
                //加上人脸比对后后需要在设置状态为审核通过时也要进行修改
                memberMapper.memberUpdateAuthIdentity(authIdentity);
            }

        }
    }

    public void addDemand(Demand demand) {
        demand.setD_title(HelpUtils.escapeHTMLTags(demand.getD_title()));
        demand.setD_memo(HelpUtils.escapeHTMLTags(demand.getD_memo()));

        memberMapper.memberAddDemand(demand);
        DemandLog dLog = new DemandLog();
        dLog.setDemand_id(demand.getId());
        dLog.setL_memo_type(1); // 第一次工单全部是文本内容
        dLog.setL_memo(demand.getD_memo());
        dLog.setLog_type(0);
        dLog.setCreate_time(demand.getCreate_time());
        memberMapper.memberAddDemandLog(dLog);

        // 如果有附件，则保存附件
        if (!HelpUtils.nullOrBlank(demand.getD_attach_url())) {
            dLog.setL_memo_type(2);
            dLog.setL_memo(demand.getD_attach_url());
            memberMapper.memberAddDemandLog(dLog);
        }
    }

    public void addDemandLog(DemandLog dLog) {
        dLog.setL_memo(HelpUtils.escapeHTMLTags(dLog.getL_memo()));

        memberMapper.memberAddDemandLog(dLog);

        Demand demand = new Demand();
        demand.setId(dLog.getDemand_id());
        demand.setIs_has_unread_plat(1);
        demand.setD_status(dLog.getD_status());
        memberMapper.updateDemand(demand);
    }

    public void addDemandLog(DemandLog dLog, Integer dStatus) {
        memberMapper.memberAddDemandLog(dLog);

        // 设置需求的状态和用户那边是否有未读消息
        Demand demand = new Demand();
        demand.setId(dLog.getDemand_id());
        demand.setD_status(dStatus);
        demand.setIs_has_unread(1);
        memberMapper.updateDemand(demand);
    }

    /**
     * 人脸识别验证
     *
     * @param result 向faceId请求验证结果返回的json数据
     */
    public String authMemberByFaceId(String result) {
        JSONObject json = JSONObject.parseObject(result);
        String resultCode = json.getString("result_code");
        //发送请求时设置的号码，这里为memberid
        String bizNo = json.getString("biz_no");
        AuthIdentity authIdentity = new AuthIdentity();
        authIdentity.setId(Integer.parseInt(bizNo));
        AuthIdentity oldAuth = memberMapper.getAuthIdentityByID(Integer.parseInt(bizNo));
        //校验id和biz_token是否对应
        //校验是否通过了审核
        if (!"1000".equals(resultCode)) {
            authIdentity.setId_status(2);
            String resultMsg = json.getString("result_message");
            String rejectReason = FaceIdUtil.changeResultCodeToRejectReason(resultCode, resultMsg);
            authIdentity.setReject_reason(rejectReason);
            updateFaceIdAuthIdentity(authIdentity);
            return bizNo;
        }
        //获取身份证信息
        JSONObject idcardResult = json.getJSONObject("idcard_result");
        //身份证号
        String idcardNumber = idcardResult.getString("idcard_number");
        Integer alreadyIdNumCount = daoUtil.queryForInt("SELECT COUNT(1) FROM m_auth_identity WHERE id <> ? AND id_number = ?", bizNo, idcardNumber);
        if (alreadyIdNumCount > 0) {
            //身份证号已使用的逻辑
            authIdentity.setId_status(2);
            authIdentity.setReject_reason("证件号已经存在");
            updateFaceIdAuthIdentity(authIdentity);
            return bizNo;
        }
        //姓名
        String name = idcardResult.getString("name");
        JSONObject images = json.getJSONObject("images");
        //图片采用base64编码，这里进行拼接
        String idcardFrontside = "data:image/jpg;base64," + images.getString("idcard_frontside");
        String idcardBackside = "data:image/jpg;base64," + images.getString("idcard_backside");
        String imageBest = "data:image/jpg;base64," + images.getString("image_best");
        //阿里云oss批量上传
        MultipartFile mult1 = Base64Util.base64ToMultipart(imageBest);
        MultipartFile mult2 = Base64Util.base64ToMultipart(idcardFrontside);
        MultipartFile mult3 = Base64Util.base64ToMultipart(idcardBackside);
        Map<String, Object>[] flieMaps = FaceIdUploader.multipartFiles2Map(mult1, mult2, mult3);
        facePool.execute(new FaceIdUploader(flieMaps));
        String aliUrl = OssConstants.ALIYUN_HOST + OssConstants.ALIYUN_UEDITOR_DIR;
        //传回来只有完整的名字，先第一个字当姓
        authIdentity.setGiven_name(name.substring(1));
        authIdentity.setFamily_name(name.substring(0, 1));
        authIdentity.setSex("女".equals(idcardResult.getString("gender")) ? "F" : "M");
        //走人脸识别默认为中国
        authIdentity.setNationality("中国Chinese");
        authIdentity.setCity(idcardResult.getString("issued_by"));
        String birthYear = idcardResult.getString("birth_year");
        String birthMonth = idcardResult.getString("birth_month");
        String birthDay = idcardResult.getString("birth_day");
        //这个api返回的日期月份小于10时为一位数
        birthMonth = birthMonth != null && birthMonth.length() == 1 ? "0" + birthMonth : birthMonth;
        birthDay = birthDay != null && birthDay.length() == 1 ? "0" + birthDay : birthDay;
        authIdentity.setBirthday(birthYear + birthMonth + birthDay);
        authIdentity.setId_number(idcardNumber);
        authIdentity.setLast_submit_time(HelpUtils.formatDate8(new Date()));
        //证件有效期
        authIdentity.setId_begin_date(idcardResult.getString("valid_date_start"));
        authIdentity.setId_end_date(idcardResult.getString("valid_date_end"));
        authIdentity.setId_handheld_img(aliUrl + flieMaps[0].get("randomName"));
        authIdentity.setId_back_img(aliUrl + flieMaps[1].get("randomName"));
        authIdentity.setId_front_img(aliUrl + flieMaps[2].get("randomName"));
        authIdentity.setId_status(1);
        authIdentity.setId_type("IDCARD");
        updateFaceIdAuthIdentity(authIdentity);
        return bizNo;

    }


    public synchronized void updateFaceIdAuthIdentity(AuthIdentity authIdentity) {
        AuthIdentity old = getAuthIdentityById(authIdentity.getId());
        if (old.getId_status().intValue() == 0 || old.getId_status() == 1) {
            return;
        }
        //人脸验证中出现问题也会发请求
        if (old.getId_status() == 2 && !"旷视faceId人脸识别".equals(old.getAuditor())) {
            return;
        }
        authIdentity.setAuditor("旷视faceId人脸识别");
        authIdentity.setAudit_time(HelpUtils.formatDate8(new Date()));
        authIdentity.setAudit_history(authIdentity.getAudit_time() + "." + authIdentity.getAuditor() + "."
                + (authIdentity.getId_status() == 2 ? "审核不通过：" + authIdentity.getReject_reason() : "审核通过"));
        String auditHistory = authIdentity.getAudit_history();
        int start = auditHistory.length() > 1900 ? auditHistory.length() - 1900 : 0;
        authIdentity.setAudit_history(auditHistory.substring(start));
        if (authIdentity.getId_status() == 2) {
            memberMapper.updateAuthIdentity(authIdentity);
        } else if (authIdentity.getId_status() == 1) {
            //如果验证通过走新的逻辑
            memberMapper.updateFaceIdAuthIdentity(authIdentity);
        }

        Member member = new Member();
        member.setId(authIdentity.getId());
        if (authIdentity.getId_status() == 1) {
            // 审核通过，设置会员级别为2
            member.setAuth_grade(1);
        } else {
            member.setAuth_grade(0);
        }
        memberMapper.updateMember(member);

        //有需求要把以前的实名认证都改成第三方的验证，查备份表有没有以前的注册信息
        if (memberAuthRecordService.hasMemberAuthedBefore(member.getId())) {
            return;
        }
        // 实名认证通过，奖励该奖励的
        if (authIdentity.getId_status() == 1) {
            // 奖励
            member = memberMapper.getMemberById(member.getId());
            // 奖励自己
            if (HelpUtils.getMgrConfig().getReg_reward_currencys().length() > 0) {

                String[] currencys = HelpUtils.getMgrConfig().getReg_reward_currencys().split(",");
                String[] volumes = HelpUtils.getMgrConfig().getReg_reward_volume().split(",");

                for (int i = 0; i < currencys.length; i++) {
                    if (StringUtil.isNullOrBank(volumes[i]) || Double.parseDouble(volumes[i]) <= 0) continue;
                    CoinRecharge coinRecharge = new CoinRecharge();
                    coinRecharge.setMember_id(member.getId());
                    coinRecharge.setCurrency(currencys[i]);
                    Currency currency = HelpUtils.getCurrencyMap().get(coinRecharge.getCurrency());
                    coinRecharge.setCurrency_id(currency.getId());
                    coinRecharge
                            .setR_txid(member.getId() + "-" + System.currentTimeMillis() + HelpUtils.randomString(10));
                    coinRecharge.setR_address("REG_REWARD");
                    coinRecharge.setR_create_time(HelpUtils.formatDate8(new Date()));
                    coinRecharge.setR_confirmations("1");
                    coinRecharge.setR_status(1);
                    coinRecharge.setR_amount(BigDecimal.valueOf(Double.parseDouble(volumes[i])));

                    addCoinRecharge(coinRecharge);
                }
            }
            // 奖励推荐人
            if (!StringUtil.isNullOrBank(member.getIntroduce_m_id())) {
                if (HelpUtils.getMgrConfig().getInvite_reward_currencys().length() <= 0) {
                    return;
                }
                String[] currencys = HelpUtils.getMgrConfig().getInvite_reward_currencys().split(",");
                String[] volumes = HelpUtils.getMgrConfig().getInvite_reward_volume().split(",");
                Member memberByIntroduce = memberMapper.findMemberByInviteId(member.getIntroduce_m_id());
                if (memberByIntroduce == null) {
                    return;
                }
                for (int i = 0; i < currencys.length; i++) {
                    if (StringUtil.isNullOrBank(volumes[i]) || Double.parseDouble(volumes[i]) <= 0) continue;
                    CoinRecharge coinRecharge = new CoinRecharge();
                    coinRecharge.setMember_id(memberByIntroduce.getId());
                    coinRecharge.setCurrency(currencys[i]);
                    Currency currency = HelpUtils.getCurrencyMap().get(coinRecharge.getCurrency());
                    coinRecharge.setCurrency_id(currency.getId());
                    coinRecharge.setR_txid(member.getIntroduce_m_id() + "-" + System.currentTimeMillis()
                            + HelpUtils.randomString(10));
                    coinRecharge.setR_address("INVITE_REWARD");
                    coinRecharge.setR_create_time(HelpUtils.formatDate8(new Date()));
                    coinRecharge.setR_confirmations("1");
                    coinRecharge.setR_status(1);
                    coinRecharge.setR_amount(BigDecimal.valueOf(Double.parseDouble(volumes[i])));

                    addCoinRecharge(coinRecharge);
                }
            }
        }
    }

    public synchronized void updateAuthIdentity(AuthIdentity authIdentity) {
        // 已经审核，不做处理
        AuthIdentity old = getAuthIdentityById(authIdentity.getId());
        if (old.getId_status().intValue() != 0 && old.getId_status().intValue() != 3 && old.getId_status() != 2) {
            return;
        }
        if (HelpUtils.getFrmUser() != null) {
            authIdentity.setAuditor(HelpUtils.getFrmUser().getUser_name());
        }
        authIdentity.setAudit_time(HelpUtils.formatDate8(new Date()));
        authIdentity.setAudit_history(authIdentity.getAudit_time() + "." + authIdentity.getAuditor() + "."
                + (authIdentity.getId_status() == 2 ? "审核不通过：" + authIdentity.getReject_reason() : "审核通过"));
        String auditHistory = authIdentity.getAudit_history();
        int start = auditHistory.length() > 1900 ? auditHistory.length() - 1900 : 0;
        authIdentity.setAudit_history(auditHistory.substring(start));
        memberMapper.updateAuthIdentity(authIdentity);

        Member member = new Member();
        member.setId(authIdentity.getId());
        if (authIdentity.getId_status() == 1) {
            // 审核通过，设置会员级别为2
            member.setAuth_grade(1);
        } else {
            member.setAuth_grade(0);
        }
        memberMapper.updateMember(member);
        String realName = (StringUtils.isEmpty(old.getFamily_name()) ? "" : old.getFamily_name()) +
                (StringUtils.isEmpty(old.getGiven_name()) ? "" : old.getGiven_name());
        synCustomer(member.getId(), realName, old.getId_number(), 3);

        //有需求要把以前的实名认证都改成第三方的验证，查备份表有没有以前的注册信息
        if (memberAuthRecordService.hasMemberAuthedBefore(member.getId())) {
            return;
        }

        // 实名认证通过，奖励该奖励的
        if (authIdentity.getId_status() == 1) {
            // 奖励
            member = memberMapper.getMemberById(member.getId());

            // 奖励自己
            if (!StringUtil.isNullOrBank(HelpUtils.getMgrConfig().getReg_reward_currencys())
                    && !StringUtil.isNullOrBank(HelpUtils.getMgrConfig().getReg_reward_volume())) {

                String[] currencys = HelpUtils.getMgrConfig().getReg_reward_currencys().split(",");
                String[] volumes = HelpUtils.getMgrConfig().getReg_reward_volume().split(",");

                for (int i = 0; i < currencys.length; i++) {
                    if (StringUtil.isNullOrBank(volumes[i]) || Double.parseDouble(volumes[i]) <= 0) continue;
                    CoinRecharge coinRecharge = new CoinRecharge();
                    coinRecharge.setMember_id(member.getId());
                    coinRecharge.setCurrency(currencys[i]);
                    Currency currency = HelpUtils.getCurrencyMap().get(coinRecharge.getCurrency());
                    coinRecharge.setCurrency_id(currency.getId());
                    coinRecharge
                            .setR_txid(member.getId() + "-" + System.currentTimeMillis() + HelpUtils.randomString(10));
                    coinRecharge.setR_address("REG_REWARD");
                    coinRecharge.setR_create_time(HelpUtils.formatDate8(new Date()));
                    coinRecharge.setR_confirmations("1");
                    coinRecharge.setR_status(1);
                    coinRecharge.setR_amount(BigDecimal.valueOf(Double.parseDouble(volumes[i])));

                    addCoinRecharge(coinRecharge);
                }
            }

            // 奖励推荐人
            if (!StringUtil.isNullOrBank(member.getIntroduce_m_id())) {
                if (StringUtil.isNullOrBank(HelpUtils.getMgrConfig().getInvite_reward_currencys()) || StringUtil.isNullOrBank(HelpUtils.getMgrConfig().getReg_reward_volume())) {
                    return;
                }
                String[] currencys = HelpUtils.getMgrConfig().getInvite_reward_currencys().split(",");
                String[] volumes = HelpUtils.getMgrConfig().getInvite_reward_volume().split(",");
                Member memberByIntroduce = memberMapper.findMemberByInviteId(member.getIntroduce_m_id());
                for (int i = 0; i < currencys.length; i++) {
                    if (StringUtil.isNullOrBank(volumes[i]) || Double.parseDouble(volumes[i]) <= 0) continue;
                    CoinRecharge coinRecharge = new CoinRecharge();
                    coinRecharge.setMember_id(memberByIntroduce.getId());
                    coinRecharge.setCurrency(currencys[i]);
                    Currency currency = HelpUtils.getCurrencyMap().get(coinRecharge.getCurrency());
                    coinRecharge.setCurrency_id(currency.getId());
                    coinRecharge.setR_txid(member.getIntroduce_m_id() + "-" + System.currentTimeMillis()
                            + HelpUtils.randomString(10));
                    coinRecharge.setR_address("INVITE_REWARD");
                    coinRecharge.setR_create_time(HelpUtils.formatDate8(new Date()));
                    coinRecharge.setR_confirmations("1");
                    coinRecharge.setR_status(1);
                    coinRecharge.setR_amount(BigDecimal.valueOf(Double.parseDouble(volumes[i])));

                    addCoinRecharge(coinRecharge);
                }
            }
        }
    }

    /**
     * 同步合约认证
     *
     * @param id       用户id
     * @param realName 姓名
     * @param idCard   有效证件
     */
    private void synCustomer(Integer id, String realName, String idCard, Integer authFlag) {
        CCustomer cCustomer = contractCustomerMapper.selectByPrimaryKey(id);
        if (cCustomer != null) {
            if (StringUtils.isEmpty(cCustomer.getRealname())) {
                cCustomer.setRealname(realName);
            }
            if (StringUtils.isEmpty(cCustomer.getIdcard())) {
                cCustomer.setIdcard(idCard);
            }
            cCustomer.setAuthflag(authFlag);
            contractCustomerMapper.updateByPrimaryKeySelective(cCustomer);
        }
    }

    public AuthVideo getAuthVideoById(Integer id) {
        return memberMapper.getMemberAuthVideoByID(id);
    }

    public List<AuthVideo> getAllAuthVideo(Map param) {
        return memberMapper.listAuthVideoPage(param);
    }

    public void updateAuthVideo(AuthVideo authVideo) {
        authVideo.setAuditor(HelpUtils.getFrmUser().getUser_name());
        authVideo.setAudit_time(HelpUtils.formatDate8(new Date()));

        // 这里的update实际上应该是add
        AuthVideo av = memberMapper.getAuthVideoByID(authVideo.getId());
        if (null == av) {
            memberMapper.addAuthVideo(authVideo);
        } else {
            memberMapper.updateAuthVideo(authVideo);
        }

        Member member = new Member();
        member.setId(authVideo.getId());
        if (authVideo.getV_status() == 1) {
            // 审核通过，设置会员级别为2
            member.setAuth_grade(2);
        } else {
            member.setAuth_grade(1);
        }
        memberMapper.updateMember(member);
    }

    public List<Map> getAllMemberOperLog(Map param) {
        return memberMapper.listMemberOperLogPage(param);
    }

    public List<Map> getAllIntroMember(Map param) {
        return memberMapper.listIntroMemberPage(param);
    }

    public List<Map> getAllDemand(Map param) {
        return memberMapper.listDemandPage(param);
    }

    public String getRechargeAddr(Map<String, Object> paramMap) {
        return memberMapper.listRechargeAddr(paramMap);
    }

    public Long addCoinRecharge(CoinRecharge coinRecharge) {
        // OTC 改造
        if (null == coinRecharge.getOtc_ads_id()) {
            coinRecharge.setOtc_ads_id(0);
            coinRecharge.setOtc_oppsite_currency("");
            coinRecharge.setOtc_owner_name("");
            coinRecharge.setOtc_price(BigDecimal.ZERO);
            coinRecharge.setOtc_money(BigDecimal.ZERO);
        }
        if (coinRecharge.getR_guiji() == null) {
            coinRecharge.setR_guiji(0);
        }

        memberMapper.addCoinRecharge(coinRecharge);

        // 增加余额
        this.accountProc(coinRecharge.getR_amount(), coinRecharge.getCurrency(), coinRecharge.getMember_id(), 3, OptSourceEnum.ZZEX);

        return coinRecharge.getId();
    }

    /**
     * 增加提币记录
     *
     * @param coinRecharge
     */
    public void addCoinRechargeByMember(CoinRecharge coinRecharge) {

        if (null == coinRecharge.getOtc_ads_id()) {
            coinRecharge.setOtc_ads_id(0);
            coinRecharge.setOtc_oppsite_currency("");
            coinRecharge.setOtc_owner_name("");
            coinRecharge.setOtc_price(BigDecimal.ZERO);
            coinRecharge.setOtc_money(BigDecimal.ZERO);
        }

        memberMapper.addCoinRechargeByMember(coinRecharge);
    }

    /**
     * 增加充值记录
     *
     * @param coinWithdraw
     */
    public void addCoinWithdraw(CoinWithdraw coinWithdraw) {
        memberMapper.addCoinWithdraw(coinWithdraw);
    }

    public void setSecurityPwd(Member member) {
        memberMapper.updateMember(member);
    }

    public Map getRechargeInfo(Map params, String type) {
        Map retMap = new HashMap();

        if ("addr".equals(type)) {
//			if ("XRP".equals(params.get("currency"))) {
//				retMap.put("rechargeAddr", "xrp_addr");
//			} else {
//				retMap.put("rechargeAddr", memberMapper.listRechargeAddr(params));
//			}
            String address = memberMapper.listRechargeAddr(params);
            if (!StringUtil.isNullOrBank(address)) {
                retMap.put("rechargeAddr", address);
            }
        } else if ("withdraw".equals(type)) {
            retMap.put("withdrawLst", getMemberCoinWithdraw(params));
            retMap.put("total", params.get("total") == null ? 0 : params.get("total"));
        } else if ("recharge".equals(type)) {
            retMap.put("rechargeLst", getMemberCoinRecharge(params));
            retMap.put("total", params.get("total") == null ? 0 : params.get("total"));
        }

        return retMap;
    }

    public void updateDemand(Demand demand) {
        memberMapper.updateDemand(demand);
    }

    public List<DemandLog> getDemandLogByDemandId(Integer id) {
        return memberMapper.getDemandLogByDemandId(id);
    }

    public void manAddCoinRecharge(CoinRecharge coinRecharge) {
        Currency currency = HelpUtils.getCurrencyMap().get(coinRecharge.getCurrency());
        if (null == currency) {
            throw new BusinessException(-1, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorCNMsg());
        }
        coinRecharge.setCurrency_id(currency.getId());
        coinRecharge.setR_status(1);
        coinRecharge
                .setR_txid(coinRecharge.getMember_id() + "-" + System.currentTimeMillis() + HelpUtils.randomString(10));
        coinRecharge.setR_create_time(HelpUtils.formatDate8(new Date()));
        coinRecharge.setR_confirmations("1");

        addCoinRecharge(coinRecharge);

        // 如果需要冻结
        if (coinRecharge.getIs_frozen() == 1) {
            this.accountProc(coinRecharge.getR_amount(), coinRecharge.getCurrency(), coinRecharge.getMember_id(), 1, OptSourceEnum.ZZEX);
            memberMapper.addManFrozen(coinRecharge);
        }
    }


    @Autowired
    private AccountFeeManager accountFeeManager;

    public synchronized ObjResp manConfirmCoinRecharge(CoinRecharge coinRecharge) {
        CoinRecharge coinRechargeDataBase = this.getCoinRecharge(coinRecharge.getId());

        if (coinRechargeDataBase.getR_status() != 0 && coinRechargeDataBase.getR_status() != -1) {
            return new ObjResp(-1, ErrorInfoEnum.ORDER_STATUS_NOT_CONFIRM.getErrorCNMsg(), null);
        }

        if (coinRechargeDataBase.getR_status() == -1 && coinRecharge.getR_status() == 1) {
            return new ObjResp(-1, ErrorInfoEnum.ORDER_STATUS_NOT_CONFIRM.getErrorCNMsg(), null);
        }
        String auditor = HelpUtils.getFrmUser() == null
                ? "auto_confirm"
                : HelpUtils.getFrmUser().getUser_name();
        coinRecharge.setAuditor(auditor);
        coinRecharge.setAudit_time(HelpUtils.formatDate8(new Date()));


        Integer otcAdsId = coinRechargeDataBase.getOtc_ads_id();
        BigDecimal otcVolume = coinRechargeDataBase.getOtc_money();
        OTCAds ads = otcMapper.loadOTCAdsById(otcAdsId);
        // 取消入账
        if (coinRecharge.getR_status() == 2) {
            //数据实验室
            if (StringUtils.isNotEmpty(coinRechargeDataBase.getR_source()) && coinRechargeDataBase.getR_source().equals(RechargeTypeEnum.DATALAB.getType())) {
                ObjResp objResp = executeChange(coinRechargeDataBase, accountFeeReductionManager);
                if (objResp.getState().equals(Resp.FAIL)) {
                    return objResp;
                }
            } else {
                //取消入账后返还商户被冻结的额度
                if (otcAdsId != 0) {
                    daoUtil.update(
                            "update otc_ads set max_quote=max_quote+? where owner_id = ? and base_currency=?",
                            otcVolume, ads.getOwner_id(), ads.getBase_currency());
                }
                //取消入账后如果广告订单被隐藏了则显示出来
                if (ads != null && ads.getC_status() == 0) {
                    Map<String, Object> paramMap = new HashMap<String, Object>(2);
                    paramMap.put("status", 1);
                    paramMap.put("id", ads.getId());
                    otcMapper.updateStatus4Ads(paramMap);
                }

            }
        } else if (coinRecharge.getR_status() == 1) { // 确认
            if (otcAdsId != 0) {
                daoUtil.update(
                        "update otc_ads set total_volume = total_volume + ?, total_amount = total_amount + ? where id = ? ",
                        otcVolume, ads.getPrice().multiply(otcVolume), otcAdsId);
            }

            String currency = HelpUtils.nullOrBlank(coinRecharge.getCurrency()) ? coinRechargeDataBase.getCurrency() : coinRecharge.getCurrency();
            Currency currencyObj = HelpUtils.getCurrencyMap().get(currency);
            List<Rule> ruleList = ruleMapper.getRuleList(HelpUtils.newHashMap("currency", currency, "enable", 0));
            Rule rule = !CollectionUtils.isEmpty(ruleList) ? ruleList.get(0) : null;
            if (HelpUtils.isLockCurrency(currencyObj.getCurrency())) {
                if (rule == null || rule.getRuleType().equals(LockRuleTypeEnum.TRADE.getCodeCn()) || rule.getRuleType().equals(LockRuleTypeEnum.TRADE.getType() + "")) {
                    currencyLockAccountService.addLockAccount(coinRecharge.getR_amount(), currency, coinRecharge.getMember_id());
                } else {
                    ObjResp objResp = addWarehouseAccountVo(coinRechargeDataBase, currency);
                    if (objResp.getState().equals(Resp.FAIL)) {
                        return objResp;
                    }
                }
            } else {
                //数据实验室
                if (StringUtils.isNotEmpty(coinRechargeDataBase.getR_source()) && coinRechargeDataBase.getR_source().equals(RechargeTypeEnum.DATALAB.getType())) {
                    ObjResp objResp = executeChange(coinRechargeDataBase, accountFeeThawManager);
                    if (objResp.getState().equals(Resp.FAIL)) {
                        return objResp;
                    }
                }
                // 增加余额
                this.accountProc(coinRecharge.getR_amount(), currency, coinRecharge.getMember_id(), 3, OptSourceEnum.ZZEX);

            }
        }
        // 更改状态和审核人信息
        memberMapper.confrimCoinRecharge(coinRecharge);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    /**
     * 手续费资产提现审核
     *
     * @param coinRechargeDataBase
     * @return
     */
    private ObjResp executeChange(CoinRecharge coinRechargeDataBase, AccountFeeChangeService accountFeeChangeService) {
        AccountFee accountFeeChane = new AccountFee();
        accountFeeChane.setForzenAmount(coinRechargeDataBase.getR_amount().abs().negate());
        accountFeeChane.setFeeCurrency(coinRechargeDataBase.getCurrency());
        accountFeeChane.setMemberId(coinRechargeDataBase.getMember_id());
        return accountFeeManager.executeAccountFeeChange(accountFeeChangeService, accountFeeChane);
    }

    private ObjResp addWarehouseAccountVo(CoinRecharge coinRecharge, String currency) {
        List<WarehouseAccountVo> warehouseAccountList = warehouseAccountService.getWarehouseAccountListPage(HelpUtils.newHashMap("coinRechargeId", coinRecharge.getId()));
        if (!CollectionUtils.isEmpty(warehouseAccountList)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.COINRECHARGEID_IS_ALREADY_EXISTS.getErrorCNMsg(), null);
        }
        WarehouseAccountVo warehouseAccountVo = new WarehouseAccountVo();
        warehouseAccountVo.setMemberId(coinRecharge.getMember_id());
        warehouseAccountVo.setWarehouseAmount(coinRecharge.getR_amount());
        warehouseAccountVo.setCreateBy("后台管理员");
        warehouseAccountVo.setRAddress(coinRecharge.getR_txid());
        warehouseAccountVo.setCurrency(currency);
        warehouseAccountVo.setCoinRechargeId(coinRecharge.getId().intValue());
        warehouseAccountService.returnAssets(warehouseAccountVo);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    // 批处理解冻
    public void batchUnFrozen() {
        List<ManFrozen> list = memberMapper.listWaitUnFrozen();

        for (ManFrozen oneFrozen : list) {
            daoUtil.update("update m_man_frozen set f_status = 1 where id = ?", oneFrozen.getId());
            this.accountProc(oneFrozen.getFrozen_balance(), oneFrozen.getCurrency(), oneFrozen.getMember_id(), 4, OptSourceEnum.ZZEX);
            logger.error("batchUnfrozen: id=" + oneFrozen.getId());
        }
    }

    /**
     * 手动解冻冻结资产
     */
    public synchronized Resp UnFrozenOne(ManFrozen oneFrozen) {
        if (1 == oneFrozen.getF_status()) {
            return new Resp(Resp.FAIL, "unfrozened");
        }
        try {
            daoUtil.update("update m_man_frozen set f_status = 1 where id = ?", oneFrozen.getId());
            this.accountProc(oneFrozen.getFrozen_balance(), oneFrozen.getCurrency(), oneFrozen.getMember_id(), 4, OptSourceEnum.ZZEX);
            logger.error("Unfrozen: id=" + oneFrozen.getId());
        } catch (Exception e) {
            logger.error("unfrozenfailed: id + oneFrozen.getId()" + "  " + e.getMessage());
            //回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new Resp(Resp.FAIL, "FAILED");
        }
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    public String genAddress(CoinRechargeAddr coinRechargeAddr) {
//        String address = "";
//        String globalLockKey = "";
//        // 先判断他有没有地址(注意，以太坊系列的都用以太坊)x
//        Integer member_id = this.daoUtil.queryForInt("SELECT member_id FROM m_coin_recharge_addr WHERE (member_id = ? AND currency = ? AND currency_chain_type = ?) OR (EXISTS (SELECT 1 FROM d_currency WHERE currency = ? AND is_in_eth = 1 ) AND member_id = ? AND currency='ETH' AND currency_chain_type = ?)"
//                , coinRechargeAddr.getMember_id(), coinRechargeAddr.getCurrency(), coinRechargeAddr.getCurrency_chain_type(), coinRechargeAddr.getCurrency(), coinRechargeAddr.getMember_id(), coinRechargeAddr.getCurrency_chain_type());
//
//        if (null == member_id) {
//            // 从地址池取一个，插入m_coin_recharge_addr，然后从地址池删除
//            String currency = coinRechargeAddr.getCurrency();
//            Integer isInEth = HelpUtils.getCurrencyMap().get(currency).getIs_in_eth();
//            if (isInEth == 1 && !ChainTypeEnum.OMNI.getType().equalsIgnoreCase(coinRechargeAddr.getCurrency_chain_type())) {
//                currency = "ETH";
//            }
//            //BTT是波场代币，先写死
//            if ("BTT".equals(currency)) {
//                currency = "TRX";
//            }
//            globalLockKey = COIN_RECHARGE_ADDR_REDIS_LOCK_PRE + coinRechargeAddr.getMember_id() + "_" + currency;
//            boolean globalIsLock = JedisUtil.getInstance().getLock(globalLockKey, IPAddressPortUtil.IP_ADDRESS, COIN_RECHARGE_ADDR_REDIS_LOCK_EXPIRE_TIME);
//            long start = System.currentTimeMillis();
//            if (globalIsLock) {
//                try {
//                    boolean addressFlag = true;
//                    do {
//                        Map poolMap = daoUtil
//                                .queryForMap("select * from m_coin_recharge_addr_pool where currency = ? limit 0, 1", currency);
//                        if (null == poolMap) {
//                            throw new BusinessException(-1, ErrorInfoEnum.LANG_POOL_NO_ADDRESS_EXISTS.getErrorENMsg(), null);
//                        }
//                        CoinRechargeAddr coinParam = new CoinRechargeAddr();
//                        coinParam.setAddress(String.valueOf(poolMap.get("address")));
//                        // 查看 已分配表是否存在，存在就下一个
//                        List<CoinRechargeAddr> coinRechargeAddrList = memberMapper.getCoinRechargeAddrList(coinParam);
//                        if (!CollectionUtils.isEmpty(coinRechargeAddrList)) {
//                            daoUtil.update("delete from m_coin_recharge_addr_pool where id = ?", poolMap.get("id"));
//                            logger.warn("地址池删掉的地址是，id：【{}】，address：【{}】", poolMap.get("id"), poolMap.get("address"));
//                            continue;
//                        }
//                        // 地址池有，已分配表没有，就分配给用户
//                        daoUtil.update("insert into m_coin_recharge_addr(member_id,currency,address,create_time,currency_chain_type,private_key) " +
//                                        " values (?,?,?,?,?,?)", coinRechargeAddr.getMember_id(),
//                                currency, poolMap.get("address"), DateUtil.dateToString(new Date()
//                                        , DateStyleEnum.YYYY_MM_DD_HH_MM_SS), coinRechargeAddr.getCurrency_chain_type(), poolMap.get("private_key"));
//                        daoUtil.update("delete from m_coin_recharge_addr_pool where id = ?", poolMap.get("id"));
//                        address = poolMap.get("address") + "";
//                        addressFlag = false;
//                    } while (addressFlag);
//                } catch (Exception e) {
//                    throw new BusinessException(-1, e.getMessage(), null);
//                } finally {
//                    boolean isRelease = JedisUtil.getInstance().releaseLock(globalLockKey, IPAddressPortUtil.IP_ADDRESS);
//                    if (!isRelease) {
//                        long end = System.currentTimeMillis();
//                        logger.warn("充值地址锁解锁失败。key:{}  耗时:{} ms", globalLockKey, end - start);
//                    }
//                }
//            } else {
//                logger.warn("未能拿到充值地址锁。key:{}", globalLockKey);
//                throw new BusinessException(-1, ErrorInfoEnum.GET_LOCK_FAIL.getErrorENMsg(), null);
//            }
//        } else {
//            throw new BusinessException(-1, ErrorInfoEnum.LANG_ADDRESS_ALREADY_EXISTS.getErrorENMsg(), null);
//        }
//        logger.info("获取充值地址锁成功，address：【{}】", address);
//        return address;

        CoinType type = null;
        if (!StringUtil.isNullOrBank(coinRechargeAddr.getCurrency_chain_type())) {
            type = CoinType.getCoin(coinRechargeAddr.getCurrency_chain_type());
        } else {
            type = CoinType.getCoin(coinRechargeAddr.getCurrency());
        }
        if (type == null) {
            throw new BusinessException("币种错误");
        }
        String address = this.daoUtil.queryForObject("SELECT address FROM m_coin_recharge_addr WHERE member_id = ? AND currency = ?", String.class,
                coinRechargeAddr.getMember_id(), type.getMainUnit());

        if (StringUtil.isNullOrBank(address)) {
            Map<String, Object> param = new HashMap<>();
            param.put("merchantId", API.MERCHANT_ID);
            //币种先写死，后期再改
            param.put("coinType", type.getMainCoinType());
            param.put("walletId", API.WALLET_ID);
            coinRechargeAddr.setCurrency(type.getMainUnit());
            Map<String, Object> requestMap = UdunUtil.requestMap(param);
            String result = HttpUtil.getInstance().post(API.getUrl(API.CREATE_ADDRESS), requestMap);
            if (result == null) {
                throw new BusinessException("获取地址失败");
            }
            JSONObject jsonObject = JSON.parseObject(result);
            String code = jsonObject.getString("code");
            if (!"200".equals(code)) {
                logger.warn("requestMap:" + JSON.toJSONString(requestMap));
                throw new BusinessException("获取地址失败:" + result);
            }
            JSONObject data = jsonObject.getJSONObject("data");
            // 地址池有，已分配表没有，就分配给用户
            daoUtil.update("insert into m_coin_recharge_addr(member_id,currency,address,create_time,currency_chain_type,private_key) " +
                            " values (?,?,?,?,?,?)", coinRechargeAddr.getMember_id(),
                    coinRechargeAddr.getCurrency(), data.getString("address"), DateUtil.dateToString(new Date()
                            , DateStyleEnum.YYYY_MM_DD_HH_MM_SS), coinRechargeAddr.getCurrency_chain_type(), "");
            address = data.getString("address");
        }
        logger.info("获取充值地址锁成功，address：【{}】", address);
        return address;
    }

    public List<Map> getAllAsserts(Map param) {
        return memberMapper.getAssertsPage(param);
    }

    public List<Map> getAllAssertsOtc(Map param) {
        return memberMapper.getAssertsOtcPage(param);
    }

    public List<Map> getAssertsCollection(Map param) {
        return memberMapper.getAssertsCollectionPage(param);
    }

    public CoinRecharge getCoinRecharge(Long id) {
        return memberMapper.getCoinRechargeByID(id);
    }

    public void addCoinRechargeAddrPool(CoinAddressInner coinAddressInner) {
        memberMapper.addCoinRechargeAddrPool(coinAddressInner);
    }

    //用户确认收款
    public void confirmGathering(Long id) {
        CoinWithdraw coinWithdrawServer = this.getCoinWithdraw(id);
        //用户已确认收款，订单完成
        if (coinWithdrawServer.getW_status() == 1) {
            //大于0表示OTC订单
            if (coinWithdrawServer.getOtc_ads_id() > 0) { // OTC是解冻对方货币
                // 卖出修改广告限额 , 总数量总金额最大限额添加
                Integer otcAdsId = coinWithdrawServer.getOtc_ads_id();
                BigDecimal otcVolume = coinWithdrawServer.getOtc_volume();
                OTCAds ads = otcMapper.loadOTCAdsById(coinWithdrawServer.getOtc_ads_id());
                daoUtil.update(
                        "update otc_ads set total_volume = total_volume + ?, total_amount = total_amount + ? where id = ? ",
                        otcVolume, ads.getPrice().multiply(otcVolume), otcAdsId);
                daoUtil.update(
                        "update otc_ads set max_quote=max_quote+? where owner_id = ? and base_currency=?",
                        otcVolume, ads.getOwner_id(), ads.getBase_currency());

                this.accountProc(coinWithdrawServer.getOtc_volume(), coinWithdrawServer.getOtc_oppsite_currency(),
                        coinWithdrawServer.getMember_id(), 2, OptSourceEnum.OLDOTC);

                //查询最新的OTC数据
                OTCAds otcads = otcMapper.loadOTCAdsById(coinWithdrawServer.getOtc_ads_id());
                //最大限额大于最小限额或者最大限额等于最小限额就显示买单  ads.getMax_quote().compareTo(ads.getMin_quote())==1||
                System.out.println(otcads.getMax_quote() + "-------" + otcads.getMin_quote());
                if (ads.getMax_quote().compareTo(ads.getMin_quote()) == 1 || otcads.getMax_quote().compareTo(otcads.getMin_quote()) == 0) {
                    daoUtil.update(
                            "UPDATE otc_ads SET c_status=1 WHERE owner_id=? and ad_type=1",
                            otcads.getOwner_id());
                }
            }
        }

    }

    public Integer getApiCount(Integer member_id) {
        return memberMapper.getApiCount(member_id);
    }


    public RespObjApiKeySecret genApiToken(ReqGenApiKey reqGenApiKey, Member member) {
        // 是否超过3个
        Integer alreadNum = getApiCount(member.getId());
        if (alreadNum >= 3) {
            return new RespObjApiKeySecret(Resp.FAIL, ErrorInfoEnum.LANG_API_COUNT_MAX_3.getErrorENMsg(), null, null);
        }

        Map keyPair = KeySecretUtil.genKeyScrectPair();

        ApiToken apiToken = new ApiToken();
        apiToken.setApi_key(keyPair.get("api_key") + "");
        apiToken.setApi_secret(keyPair.get("api_secret") + "");
        apiToken.setCreate_time(HelpUtils.formatDate8(new Date()));
        apiToken.setLabel(reqGenApiKey.getLabel());
        apiToken.setTrusted_ip(reqGenApiKey.getTrusted_ip());
        apiToken.setMember_id(member.getId());
        apiToken.setTrade_commission(member.getTrade_commission());
        apiToken.setApi_privilege(reqGenApiKey.getApi_privilege());
        if (1 == member.getApi_limit()) {
            apiToken.setApi_limit(1);
        } else {
            apiToken.setApi_limit(0);
        }
        memberMapper.addApiToken(apiToken);

        // 刷新用户验证码
        Member newMember = new Member();
        newMember.setId(member.getId());
        newMember.setSms_code(HelpUtils.randomNumNo0(4));
        newMember.setM_name(member.getM_name());
        member.setLast_login_ip(member.getLast_login_ip());

        updateMember(newMember, false, false);

        // ApiKey和Token存Redis
        JedisUtil.getInstance().set(API_TOKEN_NAME_PRE + apiToken.getApi_key(), apiToken, false);

        return new RespObjApiKeySecret(Resp.SUCCESS, Resp.SUCCESS_MSG, apiToken.getApi_key(), apiToken.getApi_secret());
    }

    public Resp modifyApiToken(ReqModifyApiKey reqModifyApiKey, Member member) {
        ApiToken apiToken = new ApiToken();
        apiToken.setLabel(reqModifyApiKey.getLabel());
        apiToken.setTrusted_ip(reqModifyApiKey.getTrusted_ip());
        apiToken.setId(reqModifyApiKey.getId());
        apiToken.setMember_id(member.getId());
        apiToken.setApi_privilege(reqModifyApiKey.getApi_privilege());
        memberMapper.updateApiToken(apiToken);

        // 刷新用户验证码
        Member newMember = new Member();
        newMember.setId(member.getId());
        newMember.setSms_code(HelpUtils.randomNumNo0(4));
        newMember.setM_name(member.getM_name());
        newMember.setLast_login_ip(member.getLast_login_ip());

        updateMember(newMember, false, false);

        apiToken = memberMapper.getApiTokenInner(apiToken);
        apiToken.setTrade_commission(member.getTrade_commission());

        // ApiKey和Token存Redis
        JedisUtil.getInstance().set(API_TOKEN_NAME_PRE + apiToken.getApi_key(), apiToken, false);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * 登录校验
     *
     * @param request
     * @param member
     * @param needToken
     * @param googleAuthCode
     * @return
     */
    public ObjResp loginCommon(HttpServletRequest request, Member member, boolean needToken, Integer googleAuthCode) {

        if (null == member) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_EMAIL_OR_PASSWORD.getErrorENMsg(), null);
        }
        if (member.getM_status() == 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_PLEASE_LOGIN_AFTER_REGISTRATION.getErrorENMsg(), null);
        }
        if (member.getM_status() == 2) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg(), null);
        }

        // 判断谷歌验证码
        if (!HelpUtils.nullOrBlank(member.getGoogle_auth_key())) {
            // 如果输入了谷歌验证码，进行判断，如果没有输入直接返回
            if (null == googleAuthCode || googleAuthCode == 0) {
                return new ObjResp(2, ErrorInfoEnum.LANG_GOOGLE_CODE_NULL.getErrorENMsg(), null);
            } else {
                GoogleAuthenticator gAuth = new GoogleAuthenticator();
                if (!gAuth.authorize(member.getGoogle_auth_key(), googleAuthCode)) {
                    return new ObjResp(3, ErrorInfoEnum.LANG_GOOGLE_CODE_ERROR.getErrorENMsg(), null);
                }
            }
        }

        //4小时内跳过短信验证
        if (!com.qiniu.util.StringUtils.isNullOrEmpty(member.getLast_login_time())) {
            long hms = DateUtil.getHMS(new Date(), DateUtil.stringToDate(member.getLast_login_time(), DateStyleEnum.YYYY_MM_DD_HH_MM_SS));
            if (hms >= 0 && hms <= Constants.MEMBER_LAST_LOGIN_TIME) {
                return hideMemberInfo(request, member, needToken);
            }
        }
        CCustomer customer = contractCustomerMapper.selectByPrimaryKey(member.getId());
        if (FunctionUtils.isEquals(customer.getIdentity(), 3)) {
            return hideMemberInfo(request, member, needToken);
        }
        /**
         * 账户密码验证后，生成token发送短信
         */
        String loginToken = HelpUtils.randomString(10, member.getId());
        JedisUtil.getInstance().set(Constants.LOGIN_TOKEN_VERIFICATION + member.getM_name(), loginToken, true);
        JedisUtil.getInstance().expire(Constants.LOGIN_TOKEN_VERIFICATION + member.getM_name(), Constants.LOGIN_TOKEN_VERIFICATION_EXPIRE);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, loginToken);

    }

    /**
     * 登录校验
     *
     * @param request
     * @param member
     * @param needToken
     * @return
     */
    public ObjResp loginCommon(HttpServletRequest request, Member member, boolean needToken) {

        if (null == member) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_INVALID_EMAIL_OR_PASSWORD.getErrorENMsg(), null);
        }
        if (member.getM_status() == 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_PLEASE_LOGIN_AFTER_REGISTRATION.getErrorENMsg(), null);
        }
        if (member.getM_status() == 2) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ACCOUNT_LOCKED.getErrorENMsg(), null);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);

    }

    /**
     * 隐藏用户关键信息，并缓存
     *
     * @param request
     * @param member
     * @param needToken
     * @return
     */
    public ObjResp hideMemberInfo(HttpServletRequest request, Member member, boolean needToken) {
        // 同时登录合约
        ObjResp objResp = memberServiceManager.handlerContractLogin(member);
        if (objResp.getState() == -1) {
            return objResp;
        }
        if (member.getM_name().indexOf("@") > 0) {
            // 截取@前面的字符串（3232556）
            String str1 = member.getM_name().substring(0, member.getM_name().indexOf("@"));
            // 截取@及后面的字符串（@163.com）
            String str2 = member.getM_name().substring(member.getM_name().indexOf("@"));        // 拼接

            String mNameHidden = str1.substring(0, 1) + "***" + str2;
            member.setM_name_hidden(mNameHidden);
        } else {
            if (member.getM_name().length() == 11) {
                member.setM_name_hidden(member.getM_name().substring(0, 3) + "*****" + member.getM_name().substring(8, 11));
            } else {
                member.setM_name_hidden(member.getM_name().substring(0, 3) + "*****" + member.getM_name().substring(member.getM_name().length() - 3));
            }
        }

        //M_name_hidden字段在前端会出现null的情况，这里加上日志打印member信息
        if (StringUtils.isBlank(member.getM_name_hidden())) {
            logger.error("M_name_hidden is null!" + member.toString());
        }

        member.setM_pwd("");  //清空密码
        member.setSms_code(""); //清空验证码
        member.setUid(HelpUtils.PRE_INTRODUCE_ID + member.getId());
        // 刷新Session中的authKey，安全考虑，session不存储具体的key，只存一个1表示已经启用谷歌验证码
        member.setGoogle_auth_key(HelpUtils.nullOrBlank(member.getGoogle_auth_key()) ? "0" : "1");
        member.setM_security_pwd(HelpUtils.nullOrBlank(member.getM_security_pwd()) ? "0" : "1");

        if (needToken) {
            member.setToken(genToken(member.getM_name(), member.getId()));
        }
        CCustomer customer = contractCustomerMapper.selectByPrimaryKey(member.getId());
        if (!BeanUtil.isEmpty(customer)) {
            member.setIsValid(customer.getIsvalid());
        }
        JedisUtilMember.getInstance().setMember(request, member);
        MySessionContext.getInstance().addSession(request.getSession());

        // 更新最后登录时间、IP等信息
        Member newMember = new Member();
        newMember.setId(member.getId());
        newMember.setM_name(member.getM_name());
        newMember.setLast_login_time(HelpUtils.formatDate8(new Date()));
        newMember.setLast_login_ip(HelpUtils.getIpAddr(request));
        newMember.setLast_login_device(LoginDeviceEnum.findByType(StringUtil.isNullOrBank(member.getLast_login_device()) ? 0 : Integer.parseInt(member.getLast_login_device())).getCodeEn());
        updateMember(newMember, false, true);
        member.setAuthToken(createAuthToken(member));
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
    }


    /**
     * 创建用户session
     *
     * @param m_name
     * @param authToken
     * @param request
     * @return
     */
    public Resp createSession(String m_name, String authToken, String last_login_device, HttpServletRequest request) {
        //判断用户是否登陆
//        Member member = JedisUtilMember.getInstance().getMember(request, null);
//        if (member != null) {
//            if(StringUtils.isNotBlank(member.getToken()) && StringUtils.isNotBlank(member.getAuthToken())){
//                return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
//            }
//        }
        // 用户未登陆则自动登陆
        Member member = getMemberBy(HelpUtils.newHashMap("m_name", m_name));
        if (null == member) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PLEASE_LOGIN_AFTER_REGISTRATION.getErrorENMsg());
        }
        memberServiceManager.handlerContractLogin(member);
        if (member.getM_name().indexOf("@") > 0) {
            String str1 = member.getM_name().substring(0, member.getM_name().indexOf("@"));
            String str2 = member.getM_name().substring(member.getM_name().indexOf("@"));        // 拼接
            String mNameHidden = str1.substring(0, 1) + "***" + str2;
            member.setM_name_hidden(mNameHidden);
        } else {
            member.setM_name_hidden(member.getM_name().substring(0, 3) + "*****" + member.getM_name().substring(8, 11));
        }
        member.setM_pwd("");  //清空密码
        member.setSms_code(""); //清空验证码
        member.setUid(HelpUtils.PRE_INTRODUCE_ID + member.getId());
        // 刷新Session中的authKey，安全考虑，session不存储具体的key，只存一个1表示已经启用谷歌验证码
        member.setGoogle_auth_key(HelpUtils.nullOrBlank(member.getGoogle_auth_key()) ? "0" : "1");
        member.setM_security_pwd(HelpUtils.nullOrBlank(member.getM_security_pwd()) ? "0" : "1");

        // 重新设置session
        member.setToken(genToken(member.getM_name(), member.getId()));
        JedisUtilMember.getInstance().setMember(request, member);
        MySessionContext.getInstance().addSession(request.getSession());

        // 更新最后登录时间、IP等信息
        Member newMember = new Member();
        newMember.setId(member.getId());
        newMember.setM_name(member.getM_name());
        newMember.setLast_login_time(HelpUtils.formatDate8(new Date()));
        newMember.setLast_login_ip(HelpUtils.getIpAddr(request));
        newMember.setLast_login_device(LoginDeviceEnum.findByType(Integer.parseInt(last_login_device)).getCodeEn());
        updateMember(newMember, false, true);
        member.setAuthToken(authToken);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, member);
    }

    /**
     * 创建authToken
     *
     * @param member
     * @return
     */
    public static String createAuthToken(Member member) {
        Long lastLoginTime = HelpUtils.getLongTime();
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setId(member.getId());
        sessionInfo.setM_name(member.getM_name());
        sessionInfo.setArea_code(member.getArea_code());
        sessionInfo.setLast_login_ip(member.getLast_login_ip());
        sessionInfo.setLast_login_time(lastLoginTime);
        sessionInfo.setExpire_time(lastLoginTime + (7 * 60 * 60 * 24));
        String authJson = JSON.toJSONString(sessionInfo);
        String authToken = EncryptUtils.desEncrypt(authJson);
        String key = new StringBuffer(CacheConst.LOGIN_PREFIX).append("app_authtoken").append(sessionInfo.getId()).toString();
        // 将token保存到redis中
        JedisUtil.getInstance().set(key, authToken, true);
        JedisUtil.getInstance().expire(key, 7 * 86400);
        return authToken;
    }

    /**
     * 生成登录Token
     *
     * @param
     * @param
     * @return
     */
    private String genToken(String mName, Integer id) {
        return MacMD5.CalcMD5(MacMD5.CalcMD5(mName + "Pm,ZhongGuo,Ex." + id, 32), 32) + Integer.toHexString(id);
    }

    public RespApiToken getApiToken(Integer member_id, Long id) {
        RespApiToken token = memberMapper.getApiToken(HelpUtils.newHashMap("member_id", member_id, "id", id));
        return token;
    }

    public ApiToken getApiToken(String apikey) {
        // 得到ApiToken
        Object apiTokenObj = JedisUtil.getInstance().get(API_TOKEN_NAME_PRE + apikey, false);

        if (null == apiTokenObj) {
            String apiTokenCacheKey = APITOKEN_PREFIX + "_" + apikey;
            try {
                String value = apiTokenCache.get(apiTokenCacheKey);
                if (value.equals("null")) {
                    logger.warn("apikey:" + apikey + " redis ApiToken is null!");
                    apiTokenCache.put(apiTokenCacheKey, APITOKEN_PREFIX);
                }
            } catch (Exception e) {
                logger.warn("get apiTokenCache, apiTokenCacheKey:{}", apiTokenCacheKey, e);
            }
            return null;
        }
        ApiToken apiToken = (ApiToken) apiTokenObj;
        return apiToken;
    }

    public Resp delApiToken(RespApiToken token, Integer member_id) {
        memberMapper.delApiToken(HelpUtils.newHashMap("member_id", member_id, "id", token.getId()));

        // 删除Redis中对应数据
        JedisUtil.getInstance().del(API_TOKEN_NAME_PRE + token.getApi_key());

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    public RespApiTokens getApiTokens(Integer member_id) {
        List<RespApiToken> tokens = memberMapper.getApiTokens(member_id);

        return new RespApiTokens(Resp.SUCCESS, Resp.SUCCESS_MSG, tokens);
    }

    public List<Map> getApiTokenPage(Map param) {
        return memberMapper.getMemberApiTokenPage(param);
    }

    public List<Map> getMemberCoinRechargeAddr(Map param) {
        return memberMapper.getMemberCoinRechargeAddrPage(param);
    }

    public List<Map> getMemberCoinRechargeAddrPool(Map param) {
        return memberMapper.getMemberCoinRechargeAddrPoolPage(param);
    }

    public void deleteCoinRecharge(Long id) {
        memberMapper.deleteCoinRecharge(id);
    }


    public ObjResp getWithdrawInfo(Integer memberId, String currency) {
        ObjResp obj = new ObjResp();
        try {
            Currency CurrencyInfo = HelpUtils.getCurrencyMap().get(currency.toUpperCase());
            String dateStr = HelpUtils.formatDate(new Date());
            // 获得用户资产
            Map<String, Object> map = daoUtil.queryForMap("SELECT id,COUNT(*) as times, ifnull(SUM(w_amount), 0) as amount FROM `m_coin_withdraw` m WHERE member_id = ? and currency = ? and w_status != 2 and SUBSTRING(m.`w_create_time`, 1,10) = ?", memberId, currency, dateStr);
            map.put("max_withdraw", CurrencyInfo.getC_max_withdraw().stripTrailingZeros().toPlainString());
            map.put("limit_withdraw", CurrencyInfo.getC_limit_withdraw());
            obj.setData(map);
        } catch (Exception e) {
            logger.warn(e.toString());
        }
        return obj;
    }


    public String findIntroduceIdByMemberId(Integer member_id) {
        return memberMapper.findIntroduceIdByMemberId(member_id);
    }

    /**
     * 后台邀请查询
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> findMgrMemberIntroByPage(Map<String, Object> param) {
        return memberMapper.findMemberIntroByPage(param);
    }

    /**
     * 邀请总数
     *
     * @param memberId
     * @return
     */
    public Integer findIntroAmountByMemberId(Integer memberId) {
        return memberMapper.findIntroAmountByMemberId(memberId);
    }

    /**
     * 分页获取邀请人数
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> findIntroByPage(Map<String, Object> param) {
        return memberMapper.findIntroByPage(param);
    }

    public Resp transfer(Map param) {
        try {
            String[] split = null;
            if (BeanUtil.isEmpty(param)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_DATA_ERROR.getErrorENMsg(), null);
            }
            Object ids = param.get("ids");
            String idParam = String.valueOf(param.get("id"));
            if (BeanUtil.isEmpty(ids)) {
                ids = idParam;
            }
            split = String.valueOf(ids).split(",");
            for (int i = 0; i < split.length; i++) {
                param.put("id", split[i]);
                updateCW(param);
            }
        } catch (Exception e) {
            logger.warn(e.toString());
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    private void updateCW(Map param) {
        List<CoinWithdraw> list = this.getAllCoinWithdraw(param);
        if (CollectionUtils.isEmpty(list)) {
            new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_DATA_ERROR.getErrorENMsg(), null);
            return;
        }
        CoinWithdraw coinWithdraw = list.get(0);
        if (StringUtils.isBlank(coinWithdraw.getW_txid())) {
            coinWithdraw.setTransfer_no(1);
            memberMapper.updateTransferCoinWithdraw(coinWithdraw);
        }
    }

    public void internalTransfer(Long id) {
        internalTransfer(memberMapper.getCoinWithdrawByID(id));
    }


    public void internalTransfer(CoinWithdraw coinWithdraw) {
        String currency = coinWithdraw.getCurrency();
        Integer isInEth = HelpUtils.getCurrencyMap().get(currency).getIs_in_eth();
        if (isInEth == 1) {
            currency = "ETH";
        }
        CoinRechargeAddr coinRechargeAddr = CoinRechargeAddr.builder()
                .address(coinWithdraw.getMember_coin_addr())
                .currency(currency)
                .build();
        List<CoinRechargeAddr> coinRechargeAddrList = memberMapper.getCoinRechargeAddrList(coinRechargeAddr);
        if (CollectionUtils.isEmpty(coinRechargeAddrList)) {
            return;
        }
        CoinRechargeAddr address = coinRechargeAddrList.get(0);
        Member member = memberMapper.getMemberById(address.getMember_id());
        CoinRecharge coinRecharge = CoinRecharge.builder()
                .member_id(member.getId())
                .currency(coinWithdraw.getCurrency())
                .currency_id(coinWithdraw.getCurrency_id())
                .r_amount(coinWithdraw.getW_amount().subtract(coinWithdraw.getW_fee()))
                .r_address(RechargeTypeEnum.INTERNAL_TRANSFER.getType())
                .is_frozen(0)
                .r_txid(coinWithdraw.getId() + "-" + System.currentTimeMillis() + HelpUtils.randomString(10))
                //保证不被归集扫到
                .r_confirmations("1")
                .r_status(1)
                .auditor("admin")
                .m_name(member.getId() + "---" + member.getM_name())
                .r_create_time(HelpUtils.formatDate8(new Date()))
                .build();
        Member fromMember = memberMapper.getMemberById(coinWithdraw.getMember_id());
        coinWithdraw.setAuditor("admin");
        coinWithdraw.setM_name(fromMember.getM_name());
        coinWithdraw.setW_txid(RechargeTypeEnum.INTERNAL_TRANSFER.getType());
        CoinWithdraw coinWithdrawCopy = new CoinWithdraw();
        BeanUtil.copy(coinWithdraw, coinWithdrawCopy);
        coinWithdrawCopy.setW_status(1);
        updateCoinWithdraw(coinWithdrawCopy, false);
        addCoinRecharge(coinRecharge);

    }


    public boolean ckeckInternalTransferCurrency(Currency currency) {
        if ("USDT".equals(currency.getCurrency())) {
            return false;
        }
        Integer can_internal_transfer = currency.getCan_internal_transfer();
        return can_internal_transfer != null ? can_internal_transfer.intValue() == 1 : false;
    }

    /**
     * 获取国际语言区号list
     *
     * @return
     */
    public List<Country> findCountryList() {

        return memberMapper.findCountryList();
    }


    /**
     * 缓存国际语言
     *
     * @param servletContext
     */
    public void cacheCountry(ServletContext servletContext) {
        List<Country> countryList = this.findCountryList();

        /**
         * 存储为list格式
         */
        servletContext.setAttribute(HelpUtils.COUNTRYLIST, countryList);

        // 存储Map格式
        Map<String, Country> retMap = new HashMap<String, Country>();

        for (Country aCountryList : countryList) {
            retMap.put(aCountryList.getArea_code(), aCountryList);
        }
        servletContext.setAttribute(HelpUtils.COUNTRYMAP, retMap);
    }

    public List<CoinWithdraw> getSumAmountGroupByMemberId(Map param) {
        return memberMapper.getSumAmountGroupByMemberId(param);
    }

    /**
     * 持仓用户数
     *
     * @return
     */
    public Map<String, Object> getEtcTotal(String currency) {
        return memberMapper.getEtcTotal(currency);
    }

    public BigDecimal getTotalRecharge(Map<String, Object> param) {
        return memberMapper.getTotalRecharge(param);
    }

    public BigDecimal getTotalWithdrawal(Map<String, Object> param) {
        return memberMapper.getTotalWithdrawal(param);
    }

    public List<AccountDto> listAccountsByPage(Map<String, Object> reqMap) {
        return memberMapper.listAccountsByPage(reqMap);
    }

    @Override
    public void dataSync(ServletContext servletContext) {
        cacheCountry(servletContext);
    }

}
