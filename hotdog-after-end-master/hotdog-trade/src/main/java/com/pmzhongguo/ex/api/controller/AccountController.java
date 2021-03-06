package com.pmzhongguo.ex.api.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.dto.*;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.resp.AccountsResp;
import com.pmzhongguo.ex.business.resp.CoinRechargeResp;
import com.pmzhongguo.ex.business.resp.CoinWithdrawsResp;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.scheduler.UsdtCnyPriceScheduler;
import com.pmzhongguo.ex.business.service.*;
import com.pmzhongguo.ex.business.vo.CoinRechargeAutoVo;
import com.pmzhongguo.ex.business.vo.CoinWithdrawAutoVo;
import com.pmzhongguo.ex.business.vo.JscAccountBindingVo;
import com.pmzhongguo.ex.business.vo.WarehouseAccountVo;
import com.pmzhongguo.ex.core.currency.CurrencyWithdrawAddrRule;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.ex.core.web.*;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.core.web.resp.WithdrawAddrResp;
import com.pmzhongguo.ex.datalab.contants.CurrencyAuthConstant;
import com.pmzhongguo.ex.datalab.entity.AccountFee;
import com.pmzhongguo.ex.datalab.service.AccountFeeService;
import com.pmzhongguo.ex.transfer.entity.FundOperationLog;
import com.pmzhongguo.ex.transfer.entity.ThirdPartyInfo;
import com.pmzhongguo.ex.transfer.service.FundOperationLogService;
import com.pmzhongguo.ex.transfer.service.FundTransferLogManager;
import com.pmzhongguo.ex.transfer.service.ThirdPartyService;
import com.pmzhongguo.ex.validate.transfer.inter.AccountValidate;
import com.pmzhongguo.notice.dto.NoticeDto;
import com.pmzhongguo.notice.service.DingDingNoticeUtil;
import com.pmzhongguo.notice.support.NoticeConstant;
import com.pmzhongguo.otc.entity.dto.AccountDTO;
import com.pmzhongguo.otc.service.OTCAccountService;
import com.pmzhongguo.udun.constant.API;
import com.pmzhongguo.udun.constant.CoinType;
import com.pmzhongguo.udun.util.SignUtil;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.zzextool.utils.MD5Util;
import com.pmzhongguo.zzextool.utils.StringUtil;
import com.qiniu.util.BeanUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Api(value = "????????????", description = "??????????????????????????????????????????", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("a")
public class AccountController extends TopController {

    @Resource
    protected ExService exService;

    @Autowired
    private ApiAccessLimitService apiAccessLimitService;

    @Autowired
    private DaoUtil daoUtil;

    @Autowired
    private OTCAccountService OTCAccountService;

    @Autowired
    CurrencyLockAccountService currencyLockAccountService;

    @Autowired
    CurrencyLockReleaseService currencyLockReleaseService;

    @Resource
    private WarehouseAccountService warehouseAccountService;

    @Autowired
    private ThirdPartyService thirdPartyService;

    @Autowired
    private RuleService ruleService;

    @Autowired
    private FundTransferLogManager fundTransferLogManager;

    @Autowired
    private FundOperationLogService fundOperationLogService;

    @Autowired
    private AccountFeeService accountFeeService;

    @Autowired
    private AccountValidate accountValidate;

//	/**
//	 * gbt???????????????????????????????????????????????????????????????????????????php?????????
//	 */
//	private String GBT_WITHDRAW_TRANSFER_URL = "http://recharge.zzexvip.com/etz/withdraw";

    //????????????????????????????????????????????????????????????????????????Guava???????????????
    private Interner<String> withdrawMemberIdPool = Interners.newWeakInterner();

    @ApiOperation(value = "????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "accountsI", method = RequestMethod.GET)
    @ResponseBody
    public AccountsResp listAccountsI(HttpServletRequest request,
                                      HttpServletResponse response) {

        // ????????????????????????
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new AccountsResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null, null);
        }

        // ??????????????????
        List<AccountDto> jsonList = memberService.getAccounts(member.getId());
        if (CollectionUtils.isEmpty(jsonList)) {
            return new AccountsResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.getNowTimeStampMillisecond(), jsonList);
        }
        List<AccountDto> respList = new ArrayList<>();
        Map<String, Object> map = $params(request);
        if (BeanUtil.isEmpty(map.get("currency"))) {
            respList = jsonList;
        } else {
            for (AccountDto accountDto : jsonList) {
                if (accountDto.getCurrency().equalsIgnoreCase(map.get("currency") + "")) {
                    respList.add(accountDto);
                    break;
                }
            }
        }
        return new AccountsResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.getNowTimeStampMillisecond(), respList);
    }

    @ApiOperation(value = "??????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "withdrawInfo/{currency}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp withdrawInfo(HttpServletRequest request,
                                HttpServletResponse response, @PathVariable String currency) {

        // ????????????????????????
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        ObjResp obj = memberService.getWithdrawInfo(member.getId(), currency);
        return obj;
    }

    /**
     * app??????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    @ApiOperation(value = "????????????????????????????????????", notes = "bb?????????????????????????????????fb????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/getAssetsForBbAndFb", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp assetsReload(HttpServletRequest request,
                                HttpServletResponse response) throws IOException, ClassNotFoundException {
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        Map respMap = new HashMap();
        List<Currency> currencyList = HelpUtils.getCurrencyLst();
        List<AccountDto> accountDtoList = memberService.genMemberAccountsLst(m.getId());
        List<LockAccount> lockAccountList = currencyLockAccountService.findByMemberId(m.getId());
        List<AssetListForBbAndFb> bbList = new ArrayList<>();

        /*????????????*/
        boolean currFlag = false;
        for (Currency currencyItem : currencyList) {
            AccountDto accountDto = new AccountDto();
            for (AccountDto accountDtoItem : accountDtoList) {
                if (currencyItem.getCurrency().equals(accountDtoItem.getCurrency())) {
                    accountDto = accountDtoItem;
                    currFlag = true;
                }
            }
            if (currFlag) {
                bbList.add(new AssetListForBbAndFb(accountDto.getCurrency(), accountDto.getBalance().doubleValue(),
                        accountDto.getFrozen_balance().doubleValue(),
                        accountDto.getZc_balance().doubleValue(), currencyItem.getC_order(), BigDecimal.ZERO, BigDecimal.ZERO));
            } else {
                bbList.add(new AssetListForBbAndFb(currencyItem.getCurrency(), 0.0, 0.0, 0.0
                        , currencyItem.getC_order(), BigDecimal.ZERO, BigDecimal.ZERO));
            }
            currFlag = false;
        }


        // ????????????????????????
        List<CurrencyLockReleaseDto> waitLockReleaseDtoList = currencyLockReleaseService.findWaitReleaseByMemberId(m.getId());
        // ???????????????
        for (AssetListForBbAndFb assets : bbList) {
            BigDecimal sumWarehouseAccount = warehouseAccountService
                    .getSumWarehouseAccount(HelpUtils.newHashMap("memberId", m.getId(), "currency", assets.getName()));
            if (BeanUtil.isEmpty(sumWarehouseAccount)) {
                sumWarehouseAccount = BigDecimal.ZERO;
            }
            for (LockAccount lockAccount : lockAccountList) {
                if (lockAccount.getCurrency().equals(assets.getName())) {
                    BigDecimal lockNum = lockAccount.getLock_num() == null ? BigDecimal.ZERO : lockAccount.getLock_num();
                    assets.setLock_num(lockNum);
                    BigDecimal lockNumZcBalance = memberService.getZcBalance4Currency(lockAccount.getCurrency(), lockNum);
                    lockNumZcBalance = lockNumZcBalance.multiply(UsdtCnyPriceScheduler.PRICE)
                            .setScale(8, BigDecimal.ROUND_HALF_UP);
                    assets.setEqual(lockNumZcBalance.add(BigDecimal.valueOf(assets.getEqual())).doubleValue());
                }
            }
            assets.setLock_num(assets.getLock_num().add(sumWarehouseAccount));

            // ???list????????????????????????????????????????????????
            CurrencyLockReleaseDto waitLockReleaseDto =
                    waitLockReleaseDtoList.stream().filter(e -> assets.getName()
                            .equals(e.getCurrency())).findAny().orElse(null);
            BigDecimal waitVolume = waitLockReleaseDto == null ? BigDecimal.ZERO : waitLockReleaseDto.getReleaseVolume();
            assets.setWait_release_num(waitVolume);
        }



        /*????????????*/
        BigDecimal fbClose = new BigDecimal(0);
        List<AssetListForBbAndFb> fbList = new ArrayList<>();
        Map<String, TickerResp> ticker = MarketService.getTicker();
        List<Currency> currencyIsOtcLst = HelpUtils.getCurrencyIsOtcLst();
        for (Currency currencyItem : currencyIsOtcLst) {
            Map map = HelpUtils.newHashMap("memberId", m.getId(), "currency", currencyItem.getCurrency());
            AccountDTO record = OTCAccountService.findBymerchantIdAndCurrency(map);
            if (HelpUtils.isEmpty(record)) {
                fbList.add(new AssetListForBbAndFb(currencyItem.getCurrency(), 0.0, 0.0, 0.0));
            } else {
                if (HelpUtils.getOfficialCurrencyToLower().equals(record.getCurrency().toLowerCase())) {
                    fbClose = UsdtCnyPriceScheduler.PRICE;
                } else {
                    fbClose = this.getFbClose(record, ticker);
                }
                fbList.add(new AssetListForBbAndFb(currencyItem.getCurrency(), record.getTotalBalance().subtract(record.getFrozenBalance()).doubleValue()
                        , record.getFrozenBalance().doubleValue(), record.getTotalBalance().multiply(fbClose).doubleValue()));
            }
        }

//		AccountDTO record = OTCAccountService.findBymerchantIdAndCurrency(HelpUtils.newHashMap("memberId", m.getId(), "currency", "zzex"));
//		if (HelpUtils.isEmpty(record)){
//			fbList.add(new AssetListForBbAndFb("zzex", 0.0, 0.0, 0.0));
//		}
//		else{
//			fbList.add(new AssetListForBbAndFb("zzex", record.getTotalBalance().subtract(record.getFrozenBalance()).doubleValue(), record.getFrozenBalance().doubleValue(), record.getTotalBalance().multiply(this.getFbClose(record, ticker)).doubleValue()));
//		}
        Collections.sort(bbList);
        respMap.put("bb", bbList);
        respMap.put("fb", fbList);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, respMap);
    }

    private BigDecimal getFbClose(AccountDTO record, Map<String, TickerResp> ticker) {
        if (null != ticker && ticker.size() > 0) {
            TickerResp tickerResp = ticker.get(record.getCurrency().toLowerCase() + HelpUtils.getOfficialCurrencyToLower() + "_ticker");
            if (HelpUtils.isEmpty(tickerResp)) {
                logger.warn("[{}]??????????????????", record.getCurrency().toLowerCase() + HelpUtils.getOfficialCurrencyToLower() + "_ticker");
                return BigDecimal.valueOf(1);
            } else {
                return tickerResp.getClose();
            }

        } else {
            logger.warn("ticker??????????????????");
            return BigDecimal.valueOf(1);
        }
    }

    @ApiOperation(value = "??????????????????", notes = "????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "api/withdraw", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp createWithdraw(HttpServletRequest request, @RequestBody ApiWithdraw apiWithdraw) throws Exception {
        if (HelpUtils.getMgrConfig().getIs_stop_ex() == 1) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_STOP_EX.getErrorENMsg(), null);
        }

        // ????????????????????????????????????
        WithdrawCreateDtoInner withdrawCreateDto = new WithdrawCreateDtoInner();
        withdrawCreateDto.setAddr(apiWithdraw.getAddr());
        withdrawCreateDto.setAmount(apiWithdraw.getAmount());
        withdrawCreateDto.setCurrency(apiWithdraw.getCurrency());

        if (null == withdrawCreateDto.getCurrency()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
        }
        Currency currency = HelpUtils.getCurrencyMap().get(withdrawCreateDto.getCurrency());
        if (null == currency) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
        }
        if (1 != currency.getCan_withdraw()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_CANNOT_WITHDRAW.getErrorENMsg(), null);
        }

        if (null == withdrawCreateDto.getAddr()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADDRESS.getErrorENMsg(), null);
        }

        if (null == withdrawCreateDto.getAmount() || withdrawCreateDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
        }

        if (withdrawCreateDto.getAmount().compareTo(currency.getC_min_withdraw()) < 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_WITHDRAW_AMOUNT_MIN_THAN_MIN_TIP.getErrorENMsg(), null);
        }

        //?????????
        if (HelpUtils.getPrecision(withdrawCreateDto.getAmount()) > currency.getC_precision()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
        }


        apiAccessLimitService.check("createWithdraw", apiWithdraw.getApi_key());
        String validateRet = HelpUtils.preValidateBaseSecret(apiWithdraw);
        if (!"".equals(validateRet)) {
            if ("ILLEGAL_TIMESTAMP".equals(validateRet)) {
                return new ObjResp(Resp.FAIL, validateRet, HelpUtils.getNowTimeStampInt());
            }
            return new ObjResp(Resp.FAIL, validateRet, null);
        }

        String ip = HelpUtils.getIpAddr(request);

        // ????????????????????????????????????????????????
        ApiToken apiToken = memberService.getApiToken(apiWithdraw.getApi_key());
        validateApiToken(HelpUtils.objToMap(apiWithdraw), apiToken, ip, "Withdraw");

        return this.createWithDrawCommon(apiToken.getMember_id(), withdrawCreateDto, currency, request);

    }

    @ApiOperation(value = "?????????????????????", notes = "????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "withdraw/createI", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp createWithdrawI(HttpServletRequest request,
                                   HttpServletResponse response, @RequestBody WithdrawCreateDtoInner withdrawCreateDto) {

        if (HelpUtils.getMgrConfig().getIs_stop_ex() == 1) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_STOP_EX.getErrorENMsg(), null);
        }

        if (null == withdrawCreateDto.getCurrency()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
        }
        Currency currency = HelpUtils.getCurrencyMap().get(withdrawCreateDto.getCurrency());
        if (null == currency) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
        }
        if (1 != currency.getCan_withdraw()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_CANNOT_WITHDRAW.getErrorENMsg(), null);
        }

        if (null == withdrawCreateDto.getAddr()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADDRESS.getErrorENMsg(), null);
        }

        if (withdrawCreateDto.getAddr().contains("???")) {
            if (StringUtils.isEmpty(withdrawCreateDto.getAddr().split("???").length > 1 ? withdrawCreateDto.getAddr().split("???")[1] : null)) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_WITHDRAW_LABEL_NULL_TIP.getErrorENMsg(), null);
            }
        }

        if (null == withdrawCreateDto.getAmount() || withdrawCreateDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
        }

        if (withdrawCreateDto.getAmount().compareTo(currency.getC_min_withdraw()) < 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_WITHDRAW_AMOUNT_MIN_THAN_MIN_TIP.getErrorENMsg(), null);
        }

        //?????????
        if (HelpUtils.getPrecision(withdrawCreateDto.getAmount()) > currency.getC_precision()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
        }

        Member member = JedisUtilMember.getInstance().getMember(request, withdrawCreateDto.getToken());
        if (null == member) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        // ??????????????????????????????????????????
        Member oldMember = memberService.getMemberById(member.getId());
        List<String> names = Arrays.asList(UserWithdrawPropertiesUtil.getPropValByKey("names").split(","));
        //???????????????????????????????????????????????????????????????
        if (names.contains(oldMember.getM_name()) && Boolean.parseBoolean(UserWithdrawPropertiesUtil.getPropValByKey("switch"))) {
            if (!withdrawCreateDto.getSms_code().equals(UserWithdrawPropertiesUtil.getPropValByKey("code"))) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg(), null);
            }
        } else {
            if (HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !withdrawCreateDto.getSms_code().equals(oldMember.getSms_code())) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg(), null);
            }
        }
        if (!MacMD5.CalcMD5Member(withdrawCreateDto.getM_security_pwd()).equals(oldMember.getM_security_pwd())) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorENMsg(), null);
        }

        // ??????????????????????????????????????????????????????
        // ??????????????????key???????????????????????????????????????session????????????
        if (!HelpUtils.nullOrBlank(oldMember.getGoogle_auth_key())) {
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            if (null == withdrawCreateDto.getGoogle_auth_code()) {
                return new ObjResp(2, ErrorInfoEnum.LANG_GOOGLE_CODE_NULL.getErrorENMsg(), null);
            }
            if (!gAuth.authorize(oldMember.getGoogle_auth_key(), withdrawCreateDto.getGoogle_auth_code())) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_GOOGLE_CODE_ERROR.getErrorENMsg(), null);
            }
        }

        /**
         *  ????????????????????????????????????5 USDT
         *  added by Daily on 2002/7/2 13:41
         */
        if(!accountValidate.isTransfer(member.getId(), withdrawCreateDto.getCurrency(), withdrawCreateDto.getAmount())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.INSUFFICIENT_RESIDUAL_ASSETS.getErrorENMsg(), null);
        }

        if(accountValidate.isVirtualAccount(member.getId())){
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }

        List<ThirdPartyInfo> thirdPartty = thirdPartyService.getAllThirdPartty(HelpUtils.newHashMap("can_withdraw_currency", withdrawCreateDto.getCurrency().toLowerCase(), "c_name_type", 2));
        if (CollectionUtils.isEmpty(thirdPartty)) {
            try {
                return this.createWithDrawCommon(member.getId(), withdrawCreateDto, currency, request);
            } catch (Exception e) {

                if (e instanceof BusinessException) {
                    return new ObjResp(Resp.FAIL, e.getMessage(), null);
                }
                logger.error("<=== ???????????????????????????????????????{}???", e.fillInStackTrace());
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg(), null);
            }
        } else {
            if (thirdPartty.size() > 1) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_REJECT_REASON.getErrorENMsg(), null);
            }
            try {
                return thirdPartyService.thirdPartyWithdraw(member, withdrawCreateDto, thirdPartty, HelpUtils.getIpAddr(request));
            } catch (Exception e) {
                logger.error("<=== ??????????????????????????????????????????{}???", e.fillInStackTrace());
                // ?????????????????????
                FundOperationLog fundOperationLog = fundTransferLogManager.createFundOperationLog(member, withdrawCreateDto, thirdPartty.get(0).getC_name(), e.getMessage());
                fundOperationLogService.insert(fundOperationLog);
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.CURRENCY_WITHDRAWAL_EXCEPTION.getErrorENMsg(), null);
            }
        }
    }


    private ObjResp createWithDrawCommon(Integer memberId, WithdrawCreateDtoInner withdrawCreateDto, Currency currency, HttpServletRequest request) {
        // ????????????????????????
        CurrencyWithdrawAddrRule rule = new CurrencyWithdrawAddrRule(currency.getCurrency());
        boolean validate = rule.validate(withdrawCreateDto.getAddr());
        if (!validate) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.WITHDRAW_ADDR_ILLEGAL.getErrorENMsg(), null);
        }

        ObjResp obj = memberService.getWithdrawInfo(memberId, currency.getCurrency());
        Currency CurrencyInfo = HelpUtils.getCurrencyMap().get(currency.getCurrency());
        Map<String, Object> WithdrawInfo = (Map<String, Object>) obj.getData();
        BigDecimal max_withdraw = CurrencyInfo.getC_max_withdraw();
        Integer limit_withdraw = CurrencyInfo.getC_limit_withdraw();
        BigDecimal amount = new BigDecimal(String.valueOf(WithdrawInfo.get("amount")));
        amount = amount.add(withdrawCreateDto.getAmount());
        if (amount.compareTo(max_withdraw) == 1) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_LIMIT_OPERTION.getErrorENMsg(), null);
        }
        if (limit_withdraw != 0) {
            Integer times = Integer.valueOf(String.valueOf(WithdrawInfo.get("times")));
            if (times.intValue() >= limit_withdraw.intValue()) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_LIMIT_OPERTION.getErrorENMsg(), null);
            }
        }

        // ???????????????????????????????????????????????????????????????
        if (HelpUtils.getMgrConfig().getWithdraw_need_identity() == 1) {
            AuthIdentity authIdentity = memberService.getAuthIdentityById(memberId);
            if (null == authIdentity || authIdentity.getId_status() != 1) {
                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_AUTH_IDENTITY_FIRST.getErrorENMsg(), null);
            }
        }

        synchronized (withdrawMemberIdPool.intern(memberId + "_" + withdrawCreateDto.getCurrency())) {
            Long id = addWithdraw(null, withdrawCreateDto, currency.getId(), withdrawCreateDto.getAddr(), withdrawCreateDto.getMember_coin_addr_label(), HelpUtils.getIpAddr(request), memberId);
            if (memberService.ckeckInternalTransferCurrency(currency)) {
                memberService.internalTransfer(id);
            }
            // ?????????????????????????????????????????????
            Member memberToRefreshSmsCode = new Member();
            memberToRefreshSmsCode.setSms_code(HelpUtils.randomNumNo0(6));
            memberToRefreshSmsCode.setId(memberId);
            memberService.updateMember(memberToRefreshSmsCode, false, false);
            // ????????????
            withdrawNotice(withdrawCreateDto);
            return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, id);
        }
    }


    private static List<String> ethCurrencyNoticeList = new ArrayList<>(16);
    /**
     * ????????????????????????
     *
     * @param withdrawDto
     */
    private void withdrawNotice(WithdrawCreateDto withdrawDto) {
        String ethCurrencyNotice = PropertiesUtil.getPropValByKey("eth_notice");
        String[] arr = ethCurrencyNotice.split(",");
        if (arr.length > 0 && ethCurrencyNoticeList.size() != arr.length) {
            ethCurrencyNoticeList.clear();
            CollectionUtils.addAll(ethCurrencyNoticeList,arr);
        }
        Currency currency = HelpUtils.getCurrencyMap().get(withdrawDto.getCurrency());

        // eth ????????????????????????????????????
        if (currency.getIs_in_eth() == 1 && !ethCurrencyNoticeList.contains(withdrawDto.getCurrency())) {
            return;
        }
        String key = "WITHDRAW_NOTICE_CACHE_KEY";
        try {
            String content = String.format(NoticeConstant.WITHDRAW_NOTICE_MSG
                    , withdrawDto.getCurrency(), withdrawDto.getAmount());
            NoticeDto noticeDto = NoticeDto.builder()
                    .cacheKey(key)
                    .content(content)
                    .subject(NoticeConstant.WITHDRAW_NOTICE_SUBJECT)
                    .build();

            DingDingNoticeUtil.send(noticeDto);
        } catch (Exception e) {
            logger.warn("????????????????????????", e.fillInStackTrace());
        }
    }


    @ApiOperation(value = "???????????????????????????????????????????????????????????????????????????", notes = "????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "recharge/createI", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp createRechargeI(HttpServletRequest request,
                                   HttpServletResponse response, @RequestBody CoinRecharge coinRecharge) throws Exception {

        // ??????currency????????????
        if (null == coinRecharge.getCurrency()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
        }
        Currency currency = HelpUtils.getCurrencyMap().get(coinRecharge.getCurrency());
        if (null == currency) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
        }

        // ??????????????????
        if (null == coinRecharge.getR_from_address()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADDRESS.getErrorENMsg(), null);
        }

        // ??????????????????
        if (null == coinRecharge.getR_amount() || coinRecharge.getR_amount().compareTo(BigDecimal.ZERO) <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
        }

        // ????????????????????????
        Member member = JedisUtilMember.getInstance().getMember(request, coinRecharge.getToken());
        if (null == member) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        coinRecharge.setMember_id(member.getId());
        coinRecharge.setR_txid(member.getId() + "-" + System.currentTimeMillis() + HelpUtils.randomString(10));
        coinRecharge.setCurrency_id(currency.getId());
        coinRecharge.setR_confirmations("1");
        coinRecharge.setR_create_time(HelpUtils.formatDate8(new Date()));
        coinRecharge.setR_status(0); //???????????????

        memberService.addCoinRechargeByMember(coinRecharge);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, null);
    }

    @ApiOperation(value = "??????????????????????????????", notes = "1???????????????0???????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "withdrawNeedIdentity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp getWithdrawNeedIdentity(HttpServletRequest request,
                                           HttpServletResponse response) {

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.getMgrConfig().getWithdraw_need_identity());
    }

    /**
     * ????????????
     *
     * @param request
     * @param response
     * @param withdrawCancelDto
     * @return
     * @throws Exception
     */
//	@ApiOperation(value = "?????????????????????", notes = "?????????????????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
//	@RequestMapping(value = "withdraw/cancelI", method = RequestMethod.POST)
//	@ResponseBody
    public Resp cancelWithdrawI(HttpServletRequest request,
                                HttpServletResponse response, @RequestBody WithdrawCancelDto withdrawCancelDto) throws Exception {

        if (withdrawCancelDto.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_WITHDRAW_ID.getErrorENMsg());
        }

        // ????????????????????????
        Member member = JedisUtilMember.getInstance().getMember(request, withdrawCancelDto.getToken());
        if (null == member) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // ???????????????????????????
        CoinWithdraw coinWithdraw = memberService.getCoinWithdraw(HelpUtils.newHashMap("id", withdrawCancelDto.getId(), "member_id", member.getId()));

        if (null == coinWithdraw) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_WITHDRAW_ID.getErrorENMsg());
        }
        if (coinWithdraw.getW_status() != 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_WITHDRAW_STATUS.getErrorENMsg());
        }

//		memberService.updateCoinWithdraw(coinWithdraw, true);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }


    @ApiOperation(value = "??????????????????", notes = "currency:????????????BTC???LTC???ETH <br>page:????????????1???????????????20???????????????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "withdrawsI/{currency}/{page}", method = RequestMethod.GET)
    @ResponseBody
    public CoinWithdrawsResp getWithdrawsI(HttpServletRequest request,
                                           HttpServletResponse response, @PathVariable String currency, @PathVariable Integer page) {

        // ??????currency
        if (null == currency) {
            return new CoinWithdrawsResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null, 0);
        }
        Currency currencyObj = HelpUtils.getCurrencyMap().get(currency.toUpperCase());
        if (null == currencyObj) {
            return new CoinWithdrawsResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null, 0);
        }

        // ????????????????????????
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new CoinWithdrawsResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null, 0);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currency", currency);
        paramMap.put("member_id", member.getId());
        paramMap.put("page", (null == page ? "1" : page));
        paramMap.put("pagesize", 20);
        List<CoinWithdraw> jsonList = memberService.getMemberCoinWithdraw(paramMap);

        return new CoinWithdrawsResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList, Integer.parseInt(paramMap.get("total") + ""));
    }


    @ApiOperation(value = "??????????????????", notes = "currency:????????????BTC???LTC???ETH <br>page:????????????1???????????????20???????????????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "rechargesI/{currency}/{page}", method = RequestMethod.GET)
    @ResponseBody
    public CoinRechargeResp getRechargeI(HttpServletRequest request,
                                         HttpServletResponse response, @PathVariable String currency, @PathVariable Integer page) {

        if (null == currency) {
            return new CoinRechargeResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null, 0);
        }
        Currency currencyObj = HelpUtils.getCurrencyMap().get(currency.toUpperCase());
        if (null == currencyObj) {
            return new CoinRechargeResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null, 0);
        }

        // ????????????????????????
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new CoinRechargeResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null, 0);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currency", currency);
        paramMap.put("member_id", member.getId());
        paramMap.put("page", (null == page ? "1" : page));
        paramMap.put("pagesize", 20);
        List<CoinRecharge> jsonList = memberService.getMemberCoinRecharge(paramMap);

        return new CoinRechargeResp(Resp.SUCCESS, Resp.SUCCESS_MSG, jsonList, Integer.parseInt(paramMap.get("total") + ""));
    }

    @ApiOperation(value = "??????????????????", notes = "currency:????????????BTC???LTC???ETH", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "rechargeAddrI/{currency}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getRechargeAddrI(HttpServletRequest request,
                                    @RequestParam(required = false, defaultValue = "") String currencyChainType, @PathVariable String currency) {

        if (null == currency) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
        }
        Currency currencyObj = HelpUtils.getCurrencyMap().get(currency.toUpperCase());
        if (null == currencyObj) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
        }

        // ????????????????????????
        Member member = JedisUtilMember.getInstance().getMember(request, null);
        if (null == member) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currency", currency);
        paramMap.put("member_id", member.getId());
        paramMap.put("currency_chain_type", currencyChainType);
        String addr = memberService.getRechargeAddr(paramMap);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, addr);
    }

    // ????????????????????????????????????????????????????????????
//    @ApiIgnore
//    @RequestMapping(value = "coinrecharge/create", method = RequestMethod.POST)
//    @ResponseBody
//    public ObjResp createCoinRechargeInner(HttpServletRequest request,
//                                           HttpServletResponse response, @RequestBody CoinRecharge coinRecharge) throws Exception {
//
//        if (HelpUtils.getMgrConfig().getIs_stop_ex() == 1) {
//            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_STOP_EX.getErrorENMsg(), null);
//        }
//
//        if (null == coinRecharge.getCurrency()) {
//            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
//        }
//        Currency currency = HelpUtils.getCurrencyMap().get(coinRecharge.getCurrency());
//        if (null == currency) {
//            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
//        }
//        if (null == coinRecharge.getR_amount() || coinRecharge.getR_amount().compareTo(BigDecimal.ZERO) <= 0) {
//            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
//        }
//        AuthDto authDto = valideRecharge(coinRecharge, objToMap(coinRecharge));
//        if (!authDto.isSuccess()) {
//            return new ObjResp(Resp.FAIL, authDto.getMsg(), null);
//        }
//
//        Integer member_id = 0;
//        if ("XRP".equals(coinRecharge.getCurrency())) {
//            member_id = Integer.parseInt(coinRecharge.getR_address().split("-")[1].substring(2));
//        } else {
//            member_id = daoUtil.queryForInt("SELECT member_id FROM m_coin_recharge_addr WHERE (address = ? AND currency = ?) OR (EXISTS (SELECT 1 FROM d_currency WHERE currency = ? AND is_in_eth = 1) AND address = ? AND currency='ETH')", coinRecharge.getR_address(), coinRecharge.getCurrency(), coinRecharge.getCurrency(), coinRecharge.getR_address());
//        }
//
//        if (null == member_id || member_id <= 0) {
//            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADDRESS.getErrorENMsg(), null);
//        }
//
//        coinRecharge.setMember_id(member_id);
//        coinRecharge.setCurrency_id(currency.getId());
//        coinRecharge.setR_status(1);
//
//        Long id = memberService.addCoinRecharge(coinRecharge);
//
//        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, id);
//    }

    // ????????????????????????????????????????????????????????????
//    @ApiIgnore
//    @RequestMapping(value = "coinaddress/create", method = RequestMethod.POST)
//    @ResponseBody
//    public Resp createCoinAddressInner(HttpServletRequest request,
//                                       HttpServletResponse response, @RequestBody CoinAddressInner coinAddressInner) throws Exception {
//
//        if (null == coinAddressInner.getCurrency()) {
//            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
//        }
//        Currency currency = HelpUtils.getCurrencyMap().get(coinAddressInner.getCurrency());
//        if (null == currency) {
//            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
//        }
//        if (HelpUtils.nullOrBlank(coinAddressInner.getAddress())) {
//            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_ADDRESS.getErrorENMsg(), null);
//        }
//        AuthDto authDto = valideRecharge(coinAddressInner, objToMap(coinAddressInner));
//        if (!authDto.isSuccess()) {
//            return new ObjResp(Resp.FAIL, authDto.getMsg(), null);
//        }
//
//        memberService.addCoinRechargeAddrPool(coinAddressInner);
//
//        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
//    }


    @ApiOperation(value = "???????????????????????????", notes = "member_id?????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "genCoinAddress", method = RequestMethod.POST)
    @ResponseBody
    public Resp genAddress(HttpServletRequest request,
                           HttpServletResponse response, @RequestBody CoinRechargeAddr coinRechargeAddr) throws Exception {

        // ????????????????????????
        Member m = JedisUtilMember.getInstance().getMember(request, coinRechargeAddr.getToken());
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }

        // ????????????????????????
        coinRechargeAddr.setMember_id(m.getId());
        Currency currency = HelpUtils.getCurrencyMap().get(coinRechargeAddr.getCurrency().toUpperCase());
        if (StringUtil.isNullOrBank(currency)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg());
        }

        // ????????????????????????usdt ??? erc20 ??? omni ????????????
//        String chainType = HelpUtils.usdtChainType(coinRechargeAddr.getCurrency(), coinRechargeAddr.getCurrency_chain_type());
//        coinRechargeAddr.setCurrency_chain_type("");
//        if (!StringUtil.isNullOrBank(chainType)) {
//            coinRechargeAddr.setCurrency_chain_type(chainType);
//        }
        String address = this.memberService.genAddress(coinRechargeAddr);
        return new Resp(Resp.SUCCESS, address);
    }

    @ApiOperation(value = "????????????????????????", notes = "id???member_id???currency_id???c_status?????????", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "saveWithdrawAddr", method = RequestMethod.POST)
    @ResponseBody
    public Resp saveWithdrawAddr(HttpServletRequest request,
                                 HttpServletResponse response, @RequestBody CoinWithdrawAddr coinWithdrawAddr) throws Exception {

        if (HelpUtils.nullOrBlank(coinWithdrawAddr.getCurrency())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.PLEASE_SELECT_CURRENCY.getErrorENMsg());
        }
        // ????????????????????????
        Member m = JedisUtilMember.getInstance().getMember(request, coinWithdrawAddr.getToken());
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }
        // ???????????????????????????????????????
        if (HelpUtils.nullOrBlank(coinWithdrawAddr.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_NULL_TIP.getErrorENMsg());
        }
        // ????????????????????????????????????
        if (HelpUtils.nullOrBlank(coinWithdrawAddr.getAddr())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_WITHDRAW_ADDR_NULL_TIP.getErrorENMsg());
        }
        if (HelpUtils.nullOrBlank(coinWithdrawAddr.getAddr_label())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_WITHDRAW_LABEL_NULL_TIP.getErrorENMsg());
        }

        // ????????????????????????????????????
        Member oldMember = memberService.getMemberById(m.getId());
        if (null == oldMember || HelpUtils.isSmsCodeTimeOut(null, oldMember.getSms_code_time(), 10) || !coinWithdrawAddr.getSms_code().equals(oldMember.getSms_code())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SMSCODE_ERR_TIP.getErrorENMsg());
        }
        // ????????????????????????????????????????????????
        memberService.updateMemberSmsCode(oldMember);

        // ??????????????????????????????
        Integer alreadyNum = daoUtil.queryForInt("SELECT count(1) FROM m_coin_withdraw_addr WHERE member_id = ? AND addr = ? AND c_status = 1 AND currency = ?", m.getId(), coinWithdrawAddr.getAddr(), coinWithdrawAddr.getCurrency());
        if (alreadyNum > 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_REPEAT_ADDRESS.getErrorENMsg(), null);
        }

        coinWithdrawAddr.setMember_id(m.getId());
        coinWithdrawAddr.setCurrency_id(HelpUtils.getCurrencyMap().get(coinWithdrawAddr.getCurrency()).getId());
        // ??????app
        String chainType = HelpUtils.usdtChainType(coinWithdrawAddr.getCurrency(), coinWithdrawAddr.getCurrency_chain_type());
        if (StringUtil.isNullOrBank(chainType)) {
            coinWithdrawAddr.setCurrency_chain_type("");
        } else {
            coinWithdrawAddr.setCurrency_chain_type(ChainTypeEnum.OMNI.getType());
        }
        memberService.addCoinWithdrawAddr(coinWithdrawAddr);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    @ApiOperation(value = "????????????????????????", notes = "???????????????id???????????????????????????id???", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "delWithdrawAddr", method = RequestMethod.POST)
    @ResponseBody
    public Resp delWithdrawAddr(HttpServletRequest request,
                                HttpServletResponse response, @RequestBody CoinWithdrawAddr coinWithdrawAddr) throws Exception {

        // ????????????????????????
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg());
        }
        coinWithdrawAddr.setMember_id(m.getId());
        String chainType = HelpUtils.usdtChainType(coinWithdrawAddr.getCurrency(), coinWithdrawAddr.getCurrency_chain_type());
        if (StringUtil.isNullOrBank(chainType)) {
            coinWithdrawAddr.setCurrency_chain_type("");
        } else {
            coinWithdrawAddr.setCurrency_chain_type(ChainTypeEnum.OMNI.getType());
        }
        memberService.delCoinWithdrawAddr(coinWithdrawAddr);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    @ApiOperation(value = "?????????????????????????????????", notes = "?????????????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "assetsRechargeAddr/{currency}/{ts}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp assetsRechargeAddr(HttpServletRequest request,
                                      @RequestParam(required = false, defaultValue = "") String currencyChainType
            , @PathVariable String currency) {
        // ????????????????????????
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }

        Map rechargeInfo = new HashMap();
        String eos = HelpUtils.getEoslist(currency.toLowerCase());
        if (!HelpUtils.nullOrBlank(eos)) {
            rechargeInfo.put("rechargeAddr", eos);
            rechargeInfo.put("is_in_eth", "2");
        } else {
            if (ChainTypeEnum.ERC20.getType().equalsIgnoreCase(currencyChainType)) {
                rechargeInfo = memberService.getRechargeInfo(HelpUtils.newHashMap("currency_chain_type", "", "currency", currency.toUpperCase(), "member_id", m.getId()), "addr");
            } else {
                if ("BTT".equals(currency)) {
                    currency = "TRX";
                }
                rechargeInfo = memberService.getRechargeInfo(HelpUtils.newHashMap("currency_chain_type", currencyChainType, "currency", currency.toUpperCase(), "member_id", m.getId()), "addr");
            }
        }
        if (org.springframework.util.CollectionUtils.isEmpty(rechargeInfo)) {
//            Currency currencyInfo = HelpUtils.getCurrencyMap().get(currency.toUpperCase());
//            if (StringUtil.isNullOrBank(currencyInfo)) {
//                return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_CURRENCY.getErrorENMsg(), null);
//            }

            CoinRechargeAddr coinRechargeAddr = CoinRechargeAddr
                    .builder()
                    .currency(currency.toUpperCase())
                    .currency_chain_type("")
                    .member_id(m.getId()).build();
//            String chainType = HelpUtils.usdtChainType(currency, currencyChainType);
//            if (!StringUtil.isNullOrBank(chainType)) {
//                coinRechargeAddr.setCurrency_chain_type(chainType);
//            }
            if (!StringUtil.isNullOrBank(currencyChainType)) {
                coinRechargeAddr.setCurrency_chain_type(currencyChainType.toUpperCase());
            }

            String address = this.memberService.genAddress(coinRechargeAddr);
            rechargeInfo.put("rechargeAddr", address);
        }


        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, rechargeInfo);
    }


    @ApiOperation(value = "?????????????????????????????????", notes = "ts ?????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "assetsRecharge/{page}/{ts}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp assetsRecharge(HttpServletRequest request,
                                  HttpServletResponse response, @PathVariable Integer page) {
        // ????????????????????????
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> requestMap = $params(request);
        List<AccountFee> accountFeeList =
                accountFeeService.getAccountFeeByMemberId(HelpUtils.newHashMap(CurrencyAuthConstant.memberId, m.getId()));
        Map map = $params(request);
        map.put("page", null == page ? "1" : page);
        map.put("pagesize", BeanUtil.isEmpty(requestMap.get("pagesize")) ? 20 : requestMap.get("pagesize"));
        map.put("sortname", "mcr.r_create_time");
        map.put("sortorder", "desc");
        map.put("r_source", "chain");
//        map.put("r_status", 1);
        if (CollectionUtils.isEmpty(accountFeeList)) {
            map.put("member_id", m.getId());
        } else {
            if (!BeanUtil.isEmpty(requestMap.get("source"))) {
                map.put("r_source", requestMap.get("source"));
                map.put("member_id", m.getId());
            } else {
                map.put("rechargeType", "-");
                if (!BeanUtil.isEmpty(requestMap.get("member_id"))) {
                    map.put("member_id", (requestMap.get("member_id")));
                }
            }
        }
        if (!BeanUtil.isEmpty(requestMap.get("currency"))) {
            map.put("currency", (requestMap.get("currency") + "").toUpperCase());
        }
        Map rechargeInfo = memberService.getRechargeInfo(map, "recharge");

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, rechargeInfo);
    }


    @ApiOperation(value = "?????????????????????????????????", notes = "ts ?????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "assetsWithDraw/{page}/{ts}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ObjResp assetsRechargeWithDraw(HttpServletRequest request,
                                          HttpServletResponse response, @PathVariable Integer page) {
        // ????????????????????????
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> requestMap = $params(request);
        List<AccountFee> accountFeeList =
                accountFeeService.getAccountFeeByMemberId(HelpUtils.newHashMap(CurrencyAuthConstant.memberId, m.getId()));
        Map map = $params(request);
        map.put("page", null == page ? "1" : page);
        map.put("pagesize", BeanUtil.isEmpty(requestMap.get("pagesize")) ? 20 : requestMap.get("pagesize"));
        map.put("sortname", "id");
        map.put("sortorder", "desc");
        if (CollectionUtils.isEmpty(accountFeeList)) {
            map.put("member_id", m.getId());
        } else {
            map.put("wStatus", 1);
            if (!BeanUtil.isEmpty(requestMap.get("source"))) {
                map.put("member_id", m.getId());
            }
        }
        if (!BeanUtil.isEmpty(requestMap.get("currency"))) {
            map.put("currency", (requestMap.get("currency") + "").toUpperCase());
        }
        Map rechargeInfo = memberService.getRechargeInfo(map, "withdraw");

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, rechargeInfo);
    }


    @ApiOperation(value = "??????????????????????????????", notes = "?????????????????????????????????currency??????currency????????????????????????", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "withdrawAddr/{currency}/{ts}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Resp listMemberWithdrawAddrById(HttpServletRequest request,
                                           @RequestParam(required = false, defaultValue = "") String currencyChainType, @PathVariable String currency) {
        // ????????????????????????
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if (HelpUtils.nullOrBlank(currency)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.PLEASE_SELECT_CURRENCY.getErrorENMsg());
        }
        String chainType = "";
        if (currency.equalsIgnoreCase("currency")) {
            currency = "";
        } else {
            chainType = HelpUtils.usdtChainType(currency, currencyChainType);
        }
        if (StringUtil.isNullOrBank(chainType)) {
            currencyChainType = "";
        } else {
            currencyChainType = chainType;
        }
        Map map = HelpUtils.newHashMap("currency_chain_type", currencyChainType, "member_id", m.getId(), "currency", BeanUtil.isEmpty(currency) ? "" : currency.toUpperCase());
        List lst = memberService.listMemberWithdrawAddrById(map);
        String eos = HelpUtils.getEoslist(currency.toLowerCase());
        if (!HelpUtils.nullOrBlank(eos)) {
            return new WithdrawAddrResp(Resp.SUCCESS, Resp.SUCCESS_MSG, 2, lst);
        }
        return new WithdrawAddrResp(Resp.SUCCESS, Resp.SUCCESS_MSG, 0, lst);
    }


    /**
     * ??????????????????????????????
     * 1?????????????????????????????????API????????????????????????????????????????????????????????????api_key????????????????????????????????????????????????api_key????????????????????????????????????????????????????????????????????????
     * 2????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????id??????????????????????????????????????????????????????????????????
     * {
     * "state":1,
     * "msg":"success",
     * "data":{
     * "withdrawLst":[
     * {"id": 1, "m_name": "xxx@qq.com", "currency": "BTC", "w_amount": 10.00000000, "w_fee": 1.00000000},
     * {"id": 2, "m_name": "xxx@qq.com", "currency": "BTC", "w_amount": 10.00000000, "w_fee": 1.00000000},
     * {"id": 3, "m_name": "xxx@qq.com", "currency": "BTC", "w_amount": 10.00000000, "w_fee": 1.00000000}
     * ]
     * }
     * }
     * ?????????id???????????????????????????????????????m_name????????????????????????currency??????????????????w_amount??????????????????w_fee?????????????????????????????????????????????w_amount - w_fee???
     *
     * @param request
     * @param response
     * @param api_key
     * @param timestamp
     * @param sign
     * @return
     * @throws Exception
     */
    @ApiIgnore
    @RequestMapping(value = "withdraw/pending/{api_key}/{timestamp}/{sign_type}/{sign}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getPendingWithdraw(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @PathVariable String api_key, @PathVariable Integer timestamp,
                                      @PathVariable String sign_type, @PathVariable String sign) throws Exception {

        Map<String, Object> validateMap = new HashMap<String, Object>();
        validateMap.put("api_key", api_key);
        validateMap.put("sign", sign);
        validateMap.put("sign_type", sign_type);
        validateMap.put("timestamp", timestamp);

        AuthDto authDto = valideWithdraw(validateMap);
        if (!authDto.isSuccess()) {
            return new ObjResp(Resp.FAIL, authDto.getMsg(), null);
        }

        // ???????????????
        List lst = daoUtil.queryForList("SELECT w.id, m.m_name, w.currency, w.w_amount, w.w_fee, w.member_coin_addr FROM m_coin_withdraw w, m_member m WHERE w.w_status = 3 AND w.member_id = m.id and (currency = 'ETH' or currency = 'NEWG' or currency in (SELECT currency FROM d_currency WHERE is_in_eth = 1))");

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, lst);
    }

    /**
     * ???????????????????????????
     * 1?????????????????????????????????API?????????????????????????????????????????????????????????api_key??????????????????????????????????????????????????????api_key???????????????????????????????????????????????????????????????????????????????????????????????????????????????
     * 2????????????????????????????????????????????????
     * ???1???
     * {"api_key": "11111111111111", "timestamp": 1513533546, "id": 1, "w_status": 1, "reject_reason": "", "sign": "1513340640166MMANKA5Q651513340640166MMANKA5Q651513340640166MMANKA5Q65"}
     * ???2???
     * {"api_key": "11111111111111", "timestamp": 1513533546, "id": 1, "w_status": 2, "reject_reason": "??????????????????", "sign": "1513340640166MMANKA5Q651513340640166MMANKA5Q651513340640166MMANKA5Q65"}
     * ?????????api_key?????????????????????timestamp?????????????????????id?????????????????????????????????w_status????????????????????????1???????????????2???????????????reject_reason??????????????????sign??????????????????
     * <p>
     * ???????????????????????????????????????
     * {
     * "state":1,
     * "msg":"success"
     * "data": 1
     * }
     * state???1???????????????data????????????????????????????????????id????????????state???1??????????????????data?????????id????????????????????????????????????????????????
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiIgnore
    @RequestMapping(value = "withdraw/feedback", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp feedbackWithdraw(HttpServletRequest request,
                                    HttpServletResponse response, @RequestBody WithdrawFeedbackDto withdrawFeedbackDto) throws Exception {

        if (HelpUtils.getMgrConfig().getIs_stop_ex() == 1) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_STOP_EX.getErrorENMsg(), null);
        }

        if (null == withdrawFeedbackDto.getId()) {
            return new ObjResp(Resp.FAIL, "Illegal id", null);
        }
        if (null == withdrawFeedbackDto.getW_status()) {
            return new ObjResp(Resp.FAIL, "Illegal status", null);
        }
        if (2 == withdrawFeedbackDto.getW_status() && HelpUtils.nullOrBlank(withdrawFeedbackDto.getReject_reason())) {
            return new ObjResp(Resp.FAIL, "Illegal reject_reason", null);
        }
        AuthDto authDto = valideWithdraw(objToMap(withdrawFeedbackDto));
        if (!authDto.isSuccess()) {
            return new ObjResp(Resp.FAIL, authDto.getMsg(), null);
        }


        CoinWithdraw coinWithdraw = new CoinWithdraw();
        coinWithdraw.setId(withdrawFeedbackDto.getId());
        coinWithdraw.setW_status(withdrawFeedbackDto.getW_status());
        coinWithdraw.setReject_reason(withdrawFeedbackDto.getReject_reason());
        coinWithdraw.setW_txid(withdrawFeedbackDto.getW_txid());
        coinWithdraw.setAuditor("???????????????");

        memberService.updateCoinWithdraw(coinWithdraw, false);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, withdrawFeedbackDto.getId());
    }

    @ApiIgnore
    @RequestMapping(value = "/jsc/binding", method = RequestMethod.POST)
    @ResponseBody
    public Resp jscBindingAccount(@RequestBody JscAccountBindingVo vo) throws Exception {
        String salt = "jscapp";
        // ????????????????????????????????????
        String sign = MacMD5.encryptionStr(vo.getTimestamp() + salt + vo.getTimestamp());
        // ????????????
        if (!sign.equals(vo.getSign())) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SIGN.getErrorCNMsg());
        }
        Member member = memberService.getMemberBy(HelpUtils.newHashMap("m_name", vo.getAccount()));
        if (member == null) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorCNMsg());
        }

        String fundPwd = member.getM_security_pwd();
        // ????????????????????????
        if (fundPwd == null) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_PLS_SET_SEC_PWD.getErrorCNMsg());
        }
        // ??????????????????
        if (!MacMD5.CalcMD5Member(vo.getFundPwd()).equals(fundPwd)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_SEC_PWD_TIP.getErrorCNMsg());
        }
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

    }

    /**
     * ??????????????????
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @ApiIgnore
    @RequestMapping(value = "/auto/withdraw", method = RequestMethod.POST)
    @ResponseBody
    public Resp coinWithdrawAuto(@RequestBody CoinWithdrawAutoVo vo) throws Exception {
        // md5 ??????
        String salt = "zzex_auto_confirm";
        String info = vo.getId() + salt + vo.getCurrency();
        String sign = MD5Util.getMD5(info);
        if (!sign.equals(vo.getSign())) {
            logger.error("<=== ?????????????????? ?????????????????????sign ??????{}???,?????????sign ??????{}???", vo.getSign(), sign);
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SIGN.getErrorENMsg());
        }
        if (StringUtil.isNullOrBank(vo.getId())) {
            logger.error("<=== CoinWithdraw ID ??????????????????{}???", vo.toString());
            return new Resp(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg());
        }
        CoinWithdraw coinWithdraw = new CoinWithdraw();
        BeanUtils.copyProperties(vo, coinWithdraw);
        memberService.updateCoinWithdraw(coinWithdraw, false);
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * ??????????????????
     *
     * @param vo
     * @return
     * @throws Exception
     */
    @ApiIgnore
    @RequestMapping(value = "/auto/confirm", method = RequestMethod.POST)
    @ResponseBody
    public Resp coinRechargeAuto(@RequestBody CoinRechargeAutoVo vo) throws Exception {
        // md5 ??????
        String salt = "zzex_auto_confirm";
        String info = vo.getId() + salt + vo.getCurrency_id();
        String sign = MD5Util.getMD5(info);
        if (!sign.equals(vo.getSign())) {
            logger.error("<=== ?????????????????? ?????????????????????sign ??????{}???,?????????sign ??????{}???", vo.getSign(), sign);
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_SIGN.getErrorENMsg());
        }
        if (StringUtil.isNullOrBank(vo.getId())) {
            logger.error("<=== CoinRecharge ID ??????????????????{}???", vo.toString());
            return new Resp(Resp.FAIL, ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg());
        }
        CoinRecharge coinRecharge = new CoinRecharge();
        BeanUtils.copyProperties(vo, coinRecharge);
        memberService.manConfirmCoinRecharge(coinRecharge);
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * ??????????????????????????????
     *
     * @param request
     * @param response
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "getRuleList")
    @ResponseBody
    public Map<String, Object> getRuleList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = $params(request);
        if (BeanUtil.isEmpty(params.get("enable"))) {
            params.put("enable", EnableTypeEnum.ENABLE);
        }
        List<Rule> ruleList = ruleService.getRuleList(params);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("Rows", ruleList);
        map.put("Total", params.get("total"));
        return map;
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return
     */
    @ApiIgnore
    @RequestMapping("/getWarehouseList/{page}/{ts}")
    @ResponseBody
    public ObjResp getWarehouseList(HttpServletRequest request,
                                    HttpServletResponse response, @PathVariable Integer page) {
        // ????????????????????????
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m || m.getId() <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        Map<String, Object> params = $params(request);
        params.put("memberId", m.getId());
        params.put("unDeleted", LockReleaseEnum.DELETED.getType());
        params.put("page", (null == page ? "1" : page));
        List<WarehouseAccountVo> warehouseAccountListPage = warehouseAccountService.getWarehouseAccountListPage(params);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("Rows", warehouseAccountListPage);
        map.put("Total", params.get("total"));
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, map);
    }

    /**
     * ????????????????????????
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/getWarehouseList")
    @ResponseBody
    public Map<String, Object> getWarehouseList(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = $params(request);
        List<WarehouseAccountVo> warehouseAccountListPage = warehouseAccountService.getWarehouseAccountListPage(params);
        Map<String, Object> map = new HashMap<String, Object>(2);
        map.put("Rows", warehouseAccountListPage);
        map.put("Total", params.get("total"));
        return map;
    }

    /**
     * ??????????????????
     *
     * @param timestamp
     * @param nonce
     * @param body
     * @param sign
     * @return
     * @throws Exception
     */
    @RequestMapping("/wallet/notify")
    @ResponseBody
    public String tradeCallback(String timestamp,
                                String nonce,
                                String body,
                                String sign) throws Exception {
        logger.warn("=====??????????????????======");
        logger.warn("timestamp:{},nonce:{},sign:{},body:{}", timestamp, nonce, sign, body);
        if (!SignUtil.checkSign(API.API_KEY, timestamp, nonce, body, sign)) {
            return "error";
        }
        JSONObject jsonObject = JSONObject.parseObject(body);
        logger.warn("trade:{}", jsonObject);
        Map<String, Object> addressMap = daoUtil.queryForMap("SELECT mc.member_id,mc.currency,d.id as currency_id\n" +
                "FROM m_coin_recharge_addr mc LEFT JOIN d_currency d on mc.currency = d.currency WHERE address = ?", jsonObject.getString("address"));
        //????????????????????????????????????,??????amount???fee??????
        int precision = jsonObject.getInteger("decimals");
        BigDecimal amount = jsonObject.getBigDecimal("amount").divide(BigDecimal.TEN.pow(precision), 8, RoundingMode.DOWN);
        BigDecimal fee = jsonObject.getBigDecimal("fee").divide(BigDecimal.TEN.pow(precision), 8, RoundingMode.DOWN);
        String coinType = jsonObject.getString("coinType");
        CoinType type = CoinType.getCoin(coinType);
        String coin = type.getUnit();
        //TODO ????????????
        if (jsonObject.getInteger("tradeType") == 1) {
            logger.warn("=====??????????????????======");
            logger.warn("address:{},amount:{},mainCoinType:{},fee:{}", jsonObject.get("address"), jsonObject.get("amount"), jsonObject.get("mainCoinType"), jsonObject.get("fee"));
            logger.info("amount={},fee={}", amount.toPlainString(), fee.toPlainString());
            String sql = "insert m_coin_recharge(" +
                    "currency_chain_type," +
                    "member_id," +
                    "currency_id," +
                    "currency," +
                    "r_amount," +
                    "r_create_time," +
                    "r_address," +
                    "r_txid," +
                    "r_confirmations," +
                    "r_status," +
                    "r_gas," +
                    "r_guiji," +
                    "r_source," +
                    "otc_ads_id) " +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            daoUtil.update(sql, "", Integer.parseInt(addressMap.get("member_id").toString()),
                    Integer.parseInt(addressMap.get("currency_id").toString()), coin,
                    amount, new Date(), jsonObject.getString("address"),
                    addressMap.get("member_id") + "-" + System.currentTimeMillis() + HelpUtils.randomString(10)
                    , "1", 0, 0, 0, "", 0);

            //????????????
            String key = "RECHARGE_NOTICE_CACHE_KEY";
            String content = String.format(NoticeConstant.RECHARGE_NOTICE_MSG
                    , coin, amount.stripTrailingZeros().toPlainString());
            try {
                NoticeDto noticeDto = NoticeDto.builder()
                        .cacheKey(key)
                        .content(content)
                        .subject(NoticeConstant.RECHARGE_NOTICE_SUBJECT)
                        .build();

                DingDingNoticeUtil.send(noticeDto);
            } catch (Exception e) {
                logger.warn("????????????????????????", e.fillInStackTrace());
            }
        } else if (jsonObject.getInteger("tradeType") == 2) {
            logger.warn("=====????????????????????????=====");
            logger.info("address:{},amount:{},mainCoinType:{},fee:{}", jsonObject.get("address"), jsonObject.get("amount"), jsonObject.get("mainCoinType"), jsonObject.get("businessId"));
//            String businessId = jsonObject.getString("businessId");
//            CoinWithdraw coinWithdraw = memberService.getCoinWithdraw(Long.parseLong(businessId));
            if (jsonObject.getInteger("status") == 1) {
                logger.warn("????????????????????????");
            } else if (jsonObject.getInteger("status") == 2) {
                logger.warn("???????????????");
            } else if (jsonObject.getInteger("status") == 3) {
                logger.warn("???????????????");
            }
//            memberService.updateCoinWithdraw(coinWithdraw, false);
        }
        return "success";
    }
}
