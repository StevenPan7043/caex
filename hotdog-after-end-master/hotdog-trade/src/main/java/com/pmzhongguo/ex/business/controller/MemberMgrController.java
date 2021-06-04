package com.pmzhongguo.ex.business.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.crowd.vo.AccountVo;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.model.NoticeContentBuilder;
import com.pmzhongguo.ex.business.service.*;
import com.pmzhongguo.ex.business.service.manager.NoticeManager;
import com.pmzhongguo.ex.business.vo.CoinRechargeExcelVo;
import com.pmzhongguo.ex.business.vo.CoinWithdrawExcelVo;
import com.pmzhongguo.ex.core.web.*;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;
import com.pmzhongguo.ex.core.utils.*;
import com.pmzhongguo.otc.entity.dto.*;
import com.pmzhongguo.otc.otcenum.*;
import com.pmzhongguo.otc.service.*;
import com.pmzhongguo.zzextool.utils.ExportExcel;
import com.pmzhongguo.zzextool.utils.StringUtil;
import com.qiniu.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.dto.AccountDetailDto;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.entity.FrmUser;
import com.pmzhongguo.otc.entity.resp.OTCMerchantResp;
import com.pmzhongguo.otc.entity.resp.OTCOrderResp;
import com.pmzhongguo.otc.entity.resp.OTCTradeResp;

@ApiIgnore
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("backstage/member")
public class MemberMgrController extends TopController {

	@Autowired
	private LockListService lockListService;

	@Autowired
	private AccountDetailService accountDetailService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MerchantManager merchantManager;

    @Autowired
    private NoticeManager noticeManager;

	@Autowired
	private OTCTradeManager oTCTradeManager;

	@Autowired
	private OTCOrderManager oTCOrderManager;

	@Autowired
	private OTCAccountInfoManager oTCAccountInfoManager;

	@Autowired
	private WCharRecordManager wCharRecordManager;

	@Autowired
	private ReturnCommiService returnCommiService;
	@Autowired
	private MerchantLogManager merchantLogManager;

	@Autowired
	private CurrencyLockAccountService currencyLockAccountService;

	@Autowired
	private CurrencyLockAccountDetailService currencyLockAccountDetailService;
	
	private static CurrencyService currencyService;
	
	private static Map<String, Currency> currencyCacheMap = new HashMap<String, Currency>();
	
	static{
		WebApplicationContext wac = ContextLoader
				.getCurrentWebApplicationContext();
		MemberMgrController.currencyService = (CurrencyService) wac
				.getBean("currencyService");
	}

	/**
	 * 用户充值列表界面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coin_recharge_list", method = RequestMethod.GET)
	public String toListCoinRecharge(HttpServletRequest request, HttpServletResponse response) {
		$attr("OTC", 0);
		$attr("user_name", HelpUtils.getFrmUser().getUser_name());
		return "business/member/coin_recharge_list";
	}

	/**
	 * 用户充值列表界面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coin_recharge_otc_list", method = RequestMethod.GET)
	public String toListCoinRechargeOtc(HttpServletRequest request, HttpServletResponse response) {
		$attr("OTC", 1);
		if (HelpUtils.getFrmUser().getOtc_owner_id() > 0) {
			$attr("otc_owner_id", HelpUtils.getFrmUser().getOtc_owner_id());
		}
		return "business/member/coin_recharge_otc_list";
	}

	/**
	 * 用户充值列表数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coin_recharge", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listCoinRecharge(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		if (!HelpUtils.nullOrBlank(param.get("id"))) {
			param.put("member_id", (param.get("id") + "").replace(HelpUtils.PRE_INTRODUCE_ID, ""));
		}
		//热狗后台虚拟币充值查询来源新增正常充值
		if("SYS_RECHARGE".equals(param.get("r_address"))){

			param.remove("r_address");
			param.put("sourceAddress","SYS_RECHARGE");
		}
		List<CoinRecharge> list = memberService.getAllCoinRecharge(param);
		Map<String, Currency> currency = HelpUtils.getAllCurrencyMap();
		for (CoinRecharge c : list) {
			if (isVC(c)) {

				Currency currencyObj = currency.get(c.getCurrency());
				if(currencyObj == null) {
					continue;
				}
				if (ChainTypeEnum.ERC20.getType().equals(c.getCurrency_chain_type()) && "USDT".equalsIgnoreCase(c.getCurrency())) {
					currencyObj = currency.get("ETH");
					c.setR_address_(currencyObj.getCurrency_img());
					continue;
				}
				String url = currencyObj.getCurrency_img();
				if (!HelpUtils.nullOrBlank(url)) {
					c.setR_address_(url);
					continue;
				}
			}
			c.setR_address_("null");
		}
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 
	 * @param coinRecharge
	 * @return
	 */
	private boolean isVC(CoinRecharge coinRecharge) {
		boolean result = false;

		if (HelpUtils.nullOrBlank(coinRecharge.getR_address())) {
			return result;
		}
		// 银行支付宝微信
		if (coinRecharge.getR_address().indexOf("→") > -1) {
			return result;
		}

		if (coinRecharge.getR_address().equals("SYS_RECHARGE") || coinRecharge.getR_address().equals("MAN_RECHARGE")
				|| coinRecharge.getR_address().equals("SYS_REWARD")
				|| coinRecharge.getR_address().equals("REG_REWARD")) {
			return result;
		}
		// 校验通过
		result = true;
		return result;
	}

	/**
	 * 用户充值新增界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coin_recharge_add", method = RequestMethod.GET)
	public String toAddRecharge(HttpServletRequest request, HttpServletResponse response, CoinRecharge coinRecharge) {

		if (null != coinRecharge.getId()) {
			CoinRecharge coinRecharge2 = memberService.getCoinRecharge(coinRecharge.getId());
			$attr("coinRecharge2", coinRecharge2);
			$attr("member", memberService.getMemberById(coinRecharge2.getMember_id()));
			$attr("authIdentity", memberService.getAuthIdentityById(coinRecharge2.getMember_id()));
		}else {
			if (!HelpUtils.isAdmin()) {
				logger.error("<============== 非法访问，用户信息: {}" , JsonUtil.toJson(HelpUtils.getFrmUser()));
				return null;
			}
		}
		return "business/member/coin_recharge_add";
	}

	/**
	 * 删除虚拟币充值记录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coin_recharge_delete", method = RequestMethod.POST)
	@ResponseBody
	public Resp coinRechargeDelete(HttpServletRequest request, HttpServletResponse response,
			@RequestBody List<CoinRecharge> coinRechargeList) {
		for (CoinRecharge coinRecharge : coinRechargeList) {
			memberService.deleteCoinRecharge(coinRecharge.getId());
		}
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 用户充值增加
	 * 
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping(value = "coin_recharge_add_do", method = RequestMethod.POST)
	@ResponseBody
	public synchronized Resp manAddCoinRecharge(HttpServletRequest request, CoinRecharge coinRecharge) {
		try {
			// 确认入账
            if (null != coinRecharge.getId()) {

                return memberService.manConfirmCoinRecharge(coinRecharge);
            }

			// 增加入账
			if ("SYS_RECHARGE".equals(coinRecharge.getR_address())) {
				coinRecharge.setR_address(coinRecharge.getR_address_());
			}

			memberService.manAddCoinRecharge(coinRecharge);
			if (BeanUtil.isEmpty(coinRecharge.getMember_id())){
				return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_USER_NOT_EXIST.getErrorENMsg(), null);
			}

            NoticeContentBuilder parse
                    = new NoticeContentBuilder(MobiInfoTemplateEnum.JH_RECHARGE_CODE.getType()
                    ,coinRecharge.getCurrency().toUpperCase()
                    ,HelpUtils.formatDate8(new Date())
                    ,coinRecharge.getR_amount().stripTrailingZeros().toPlainString());
            if (StringUtil.isNullOrBank(parse.getContent())) {
                return Resp.failMsg(ErrorInfoEnum.PARAMS_ERROR.getErrorENMsg());
            }
			noticeManager.sendByMemberId(coinRecharge.getMember_id(),parse.getContent());
		} catch (Exception e) {
			e.printStackTrace();
			return new Resp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg());
		}
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 用户提现列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coin_withdraw_list", method = RequestMethod.GET)
	public String toListCoinWithdraw(HttpServletRequest request, HttpServletResponse response) {
		$attr("OTC", 0);
		return "business/member/coin_withdraw_list";
	}

	/**
	 * 用户提现列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coin_withdraw_otc_list", method = RequestMethod.GET)
	public String toListCoinWithdrawOTC(HttpServletRequest request, HttpServletResponse response) {
		$attr("OTC", 1);
		if (HelpUtils.getFrmUser().getOtc_owner_id() > 0) {
			$attr("otc_owner_id", HelpUtils.getFrmUser().getOtc_owner_id());
		}
		return "business/member/coin_withdraw_list";
	}

	/**
	 * 用户提现处理界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coin_withdraw_edit", method = RequestMethod.GET)
	public String toEditCoinWithdraw(HttpServletRequest request, HttpServletResponse response) {
		//CoinWithdraw info = memberService.getCoinWithdraw($long("id"));
		CoinWithdraw info = memberService.getCoinWithdraw($long("id"));
		String currency = info.getCurrency();
		if (!HelpUtils.isEmpty(HelpUtils.getEoslist(currency.toLowerCase()))){
			String display = displayaddr(info);
			$attr("is_in_eth", "2");
            $attr("displayaddr", display);
		}else {
            $attr("displayaddr", info.getMember_coin_addr());
        }
		$attr("info", info);
		FrmUser user = HelpUtils.getFrmUser();
		System.out.println(user.getId() + "----" + user.getUser_name());
		$attr("user", user);
		// 获得账户情况，确定不会冻结大于总余额
		String frozenCurrency = info.getCurrency();
		if (info.getOtc_ads_id() > 0) { // OTC冻结的是对方货币
			frozenCurrency = info.getOtc_oppsite_currency();
		}
		Double frozenToBalance = daoUtil.queryForObject(
				"SELECT total_balance - frozen_balance FROM m_account WHERE member_id = ? AND currency = ?",
				Double.class, info.getMember_id(), frozenCurrency);

		if (frozenToBalance < 0) {
			$attr("error", "该用户账户可疑，冻结余额大于总金额");
		}

		return "business/member/coin_withdraw_edit";
	}

	/**
	 *	处理提现信息
	 *	将三代币的地址和终端标签拆分
	 */
	protected String displayaddr(CoinWithdraw coinWithdraw){
		String addr = coinWithdraw.getMember_coin_addr();
		if (addr == null || coinWithdraw.getOtc_ads_id() > 0){
			return null;
		}
		String addrAndLabel[] = addr.split("▲");
		if (addrAndLabel.length == 1){
			//防止▲写在最后的情况
			return addrAndLabel[0];
		}else{
			coinWithdraw.setMember_coin_addr_label(addrAndLabel[1]);
		}
		return addrAndLabel[0];
	}

	/**
	 * 提现接口
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "transfer", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Resp transfer(HttpServletRequest request, HttpServletResponse response) {
		return memberService.transfer($params(request));
	}


    /**
     * 校验提币地址和缓存中的是否一致，防止别人篡改地址
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "transfer/addrCheck", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public Resp transferAddrCheck(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> params = $params(request);
        Object addr = JedisUtil.getInstance()
                .get(PeppaexConstants.WITHDRAW_ADDR_CACHE_KEY + params.get("id")
                ,true);
        if (addr == null) {
            return Resp.failMsg("地址不一致");
        }
        if (addr.equals(params.get("addr"))) {
            return Resp.successMsg("校验成功");
        }
        return Resp.failMsg("地址不一致");
    }
	/**
	 * 用户提现列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "coin_withdraw", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listCoinWithdraw(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		if (!HelpUtils.nullOrBlank(param.get("id"))) {
			param.put("member_id", (param.get("id") + "").replace(HelpUtils.PRE_INTRODUCE_ID, ""));
		}
		List<CoinWithdraw> list = memberService.getAllCoinWithdraw(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 用户提现处理
	 * 
	 * @param request
	 * @param
	 * @return
	 */
    @RequestMapping(value = "coin_withdraw_edit_do", method = RequestMethod.POST)
    @ResponseBody
	public Resp editCoinWithdraw(HttpServletRequest request, CoinWithdraw coinWithdraw) {
		try {
			if (BeanUtil.isEmpty(coinWithdraw.getMember_coin_addr())) {
				return new Resp(Resp.SUCCESS, ErrorInfoEnum.LANG_WITHDRAW_ADDR_NULL_TIP.getErrorENMsg());
			}
			memberService.updateCoinWithdraw(coinWithdraw, false);
		} catch (Exception e) {
			if (e instanceof BusinessException) {
				logger.warn("系统异常：原因：{}", ((BusinessException) e).getMsg());
			}
			e.printStackTrace();
			return new Resp(Resp.SUCCESS, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg());
		}
		JedisUtil.getInstance().del(PeppaexConstants.WITHDRAW_ADDR_CACHE_KEY+coinWithdraw.getId());
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 会员列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member_list", method = RequestMethod.GET)
	public String toListMember(HttpServletRequest request, HttpServletResponse response) {

		return "business/member/member_list";
	}

	/**
	 * 会员设置界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member_edit", method = RequestMethod.GET)
	public String toEditMember(HttpServletRequest request, HttpServletResponse response) {
		Member info = memberService.getMemberById($int("id"));
		AuthIdentity authIdentity = new AuthIdentity();
		if (info.getAuth_grade() != 0) {
			authIdentity = memberService.getAuthIdentityById(info.getId());
		}
		$attr("authIdentity", authIdentity);
		$attr("info", info);
		return "business/member/member_edit";
	}

	/**
	 * 会员列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listMember(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		if (!HelpUtils.nullOrBlank(param.get("id"))) {
			param.put("id", (param.get("id") + "").replace(HelpUtils.PRE_INTRODUCE_ID, ""));
		}
		List<Member> list = memberService.getAllMember(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 会员设置
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "member_edit_do", method = RequestMethod.POST)
	@ResponseBody
	public Resp editMember(HttpServletRequest request, Member member) {
		return memberService.editMember($params(request), member);
	}

	/**
	 * 会员身份验证列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "auth_identity_list", method = RequestMethod.GET)
	public String toListAuthIdentity(HttpServletRequest request, HttpServletResponse response) {

		return "business/member/auth_identity_list";
	}

	/**
	 * 会员身份验证审核界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "auth_identity_edit", method = RequestMethod.GET)
	public String toEditAuthIdentity(HttpServletRequest request, HttpServletResponse response) {
		AuthIdentity info = memberService.getAuthIdentityById($int("id"));
		$attr("info", info);
		return "business/member/auth_identity_edit";
	}

	/**
	 * 批量不通过
	 *
	 * @param request
	 * @param
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "auth_identity_not_pass", method = RequestMethod.POST)
	public Resp listAuthIdentityNotPass(HttpServletRequest request, @RequestBody List<AuthIdentity> authIdentityList) {
		for (AuthIdentity authIdentity : authIdentityList) {
			// 批量驳回审核中的状态
			if (authIdentity.getId_status() == 0 || authIdentity.getId_status() == 3) {
				authIdentity.setId_status(2);
				authIdentity.setReject_reason("上传信息不完善");
				memberService.updateAuthIdentity(authIdentity);
			}
		}
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 身份验证列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "auth_identity", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listAuthIdentity(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<AuthIdentity> list = memberService.getAllAuthIdentity(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 身份验证审核
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "auth_identity_edit_do", method = RequestMethod.POST)
	@ResponseBody
	public Resp editAuthIdentity(HttpServletRequest request, AuthIdentity authIdentity) {
		try {
			memberService.updateAuthIdentity(authIdentity);
		} catch (Exception e) {
			e.printStackTrace();
			return new Resp(Resp.FAIL, ErrorInfoEnum.UNDEFINE_ERROR.getErrorENMsg());
		}
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 会员操作记录列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "account_details_log_list", method = RequestMethod.GET)
	public String toListAccountOperLog(HttpServletRequest request, HttpServletResponse response) {
        List<String> optSourceList = new ArrayList<>();
        for (OptSourceEnum x:OptSourceEnum.values()) {
            optSourceList.add(x.getCode());
        }
        $attr("optSources",optSourceList);
		return "business/member/account_details_log_list";
	}

	/**
	 * OTC会员操作记录列表界面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "otc_account_details_log_list", method = RequestMethod.GET)
	public String toListOTCAccountOperLog(HttpServletRequest request, HttpServletResponse response) {

		return "business/member/otc_account_details_log_list";
	}

	/**
	 * 会员操作记录列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "account_oper_log", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listAccountOperLog(HttpServletRequest request, HttpServletResponse response) {

		if ("".equals($("memberId"))) {
			Map map = new HashMap();
			map.put("Rows", null);
			map.put("Total", 0);
			return map;
		}

		Map<String, Object> params = $params(request);
		if (!HelpUtils.nullOrBlank(String.valueOf(params.get("currency")))) {
			params.put("currency", String.valueOf(params.get("currency")).toUpperCase());
		}
		List<AccountDetailDto> list = accountDetailService.selectPage(params);
		if (list != null && list.size() > 0 && !HelpUtils.nullOrBlank(String.valueOf(params.get("currency")))
				&& !HelpUtils.nullOrBlank(String.valueOf(params.get("memberId")))) {

			Account laster = getLaster(params);
			initOperResult(list, laster);
		}
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", params.get("total"));
		return map;
	}

	/**
	 * OTC会员操作记录列表数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "otc_account_oper_log", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listOTCAccountOperLog(HttpServletRequest request, HttpServletResponse response) {

		if ("".equals($("memberId"))) {
			Map map = new HashMap();
			map.put("Rows", null);
			map.put("Total", 0);
			return map;
		}

		Map<String, Object> params = $params(request);
		if (!HelpUtils.nullOrBlank(String.valueOf(params.get("currency")))) {
			params.put("currency", String.valueOf(params.get("currency")).toUpperCase());
		}
		List<AccountDetailDto> list = accountDetailService.selectOTCPage(params);
		if (list != null && list.size() > 0 && !HelpUtils.nullOrBlank(String.valueOf(params.get("currency")))
				&& !HelpUtils.nullOrBlank(String.valueOf(params.get("memberId")))) {

			Account laster = getOTCLaster(params);
			initOTCOperResult(list, laster);
		}
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", params.get("total"));
		return map;
	}

	private void initOperResult(List<AccountDetailDto> list, Account laster) {
		for (int i = 0; i < list.size(); i++) {
			AccountDetailDto temp = list.get(i);
			Account before = getAccount(temp.getBeforeInfo());
//			1表示冻结[+frozen_balance]，2表示解冻[-frozen_balance，同时-total_balance]，
//			3表示增加余额[+total_balance]，4表示还原冻结[-frozen_balance]（跟解冻的区别是：解冻是成交，还原冻结是取消）
			boolean r = false;
			switch (temp.getProcType().intValue()) {
				case 1:
					r = before.getFrozen_balance().add(temp.getNum()).compareTo(laster.getFrozen_balance()) == 0;
					setOperResult(r, temp);
					break;
				case 2:
					r = before.getFrozen_balance().subtract(temp.getNum()).compareTo(laster.getFrozen_balance()) == 0;
					r = r && before.getTotal_balance().subtract(temp.getNum()).compareTo(laster.getTotal_balance()) == 0;
					setOperResult(r, temp);
					break;
				case 3:
					r = before.getTotal_balance().add(temp.getNum()).compareTo(laster.getTotal_balance()) == 0;
					setOperResult(r, temp);
					break;
				case 4:
					r = before.getFrozen_balance().subtract(temp.getNum()).compareTo(laster.getFrozen_balance()) == 0;
					setOperResult(r, temp);
				case 11:
					setOperResult(r, temp);
					break;
			}
			laster = before;
		}
	}
	private void initOTCOperResult(List<AccountDetailDto> list, Account laster) {
		for (int i = 0; i < list.size(); i++) {
			AccountDetailDto temp = list.get(i);
			Account before = getOTCAccount(temp.getBeforeInfo());
			boolean r = false;
			switch (temp.getProcType().intValue()) {
				case 1:
				case 5:
				case 10:
					r = before.getTotal_balance().add(temp.getNum()).compareTo(laster.getTotal_balance()) == 0;
					setOperResult(r, temp);
					break;
				case 2:
				case 7:
					r = before.getFrozen_balance().add(temp.getNum()).compareTo(laster.getFrozen_balance()) == 0;
					setOperResult(r, temp);
					break;
				case 3:
				case 9:
					r = before.getFrozen_balance().subtract(temp.getNum()).compareTo(laster.getFrozen_balance()) == 0;
					setOperResult(r, temp);
					break;
				case 4:
				case 8:
					r = before.getFrozen_balance().subtract(temp.getNum()).compareTo(laster.getFrozen_balance()) == 0;
					r = r && before.getTotal_balance().subtract(temp.getNum()).compareTo(laster.getTotal_balance()) == 0;
					setOperResult(r, temp);
					break;
				case 6:
					r = before.getTotal_balance().subtract(temp.getNum()).compareTo(laster.getTotal_balance()) == 0;
					setOperResult(r, temp);
					break;
			}
			laster = before;
		}
	}

	private void setOperResult(boolean r, AccountDetailDto dto) {
		if (r) {
			dto.setOperResult("正确");
		} else {
			if (dto.getProcType() == AccountOperateTypeEnum.FAIL.getType()) {
				dto.setOperResult("项目方账户扣款失败");
			} else {
				dto.setOperResult("错误");
			}
		}
	}

	private Account getLaster(Map<String, Object> params) {
		Account laster = new Account();
		Integer page = Integer.valueOf(String.valueOf(params.get("page")));
		if (page.intValue() == 1) {
			laster = memberService.getAccount(
					HelpUtils.newHashMap("currency", params.get("currency"), "member_id", params.get("memberId")));
		} else {
			params.put("page", page - 1);
			List<AccountDetailDto> list = accountDetailService.selectPage(params);
			String jsonStr = list.get(list.size() - 1).getBeforeInfo();
			laster = getAccount(jsonStr);
		}
		return laster;
	}
	private Account getOTCLaster(Map<String, Object> params) {
		Account laster = new Account();
		Integer page = Integer.valueOf(String.valueOf(params.get("page")));
		if (page.intValue() == 1) {
			laster = memberService.getOTCAccount(
					HelpUtils.newHashMap("currency", params.get("currency"), "member_id", params.get("memberId")));
		} else {
			params.put("page", page - 1);
			List<AccountDetailDto> list = accountDetailService.selectOTCPage(params);
			String jsonStr = list.get(list.size() - 1).getBeforeInfo();
			laster = getOTCAccount(jsonStr);
		}
		return laster;
	}
	private Account getAccount(String jsonStr) {
		Account dto = new Account();
		if (HelpUtils.nullOrBlank(jsonStr)) {
			dto.setTotal_balance(BigDecimal.ZERO);
			dto.setFrozen_balance(BigDecimal.ZERO);
		} else {
			dto = JsonUtil.fromJson(jsonStr, Account.class);
			if (dto.getFrozen_balance() == null) {
				dto.setFrozen_balance(BigDecimal.ZERO);
			}
			if(dto.getTotal_balance() == null) {
				dto.setTotal_balance(BigDecimal.ZERO);
			}
		}
		return dto;
	}
	private Account getOTCAccount(String jsonStr) {
		Account resultDto = new Account();
		if (HelpUtils.nullOrBlank(jsonStr)) {
			resultDto.setTotal_balance(BigDecimal.ZERO);
			resultDto.setFrozen_balance(BigDecimal.ZERO);
		} else {
			AccountVo accountVo = new AccountVo();
			accountVo = JsonUtil.fromJson(jsonStr, AccountVo.class);
			resultDto.setId(accountVo.getId());
			resultDto.setFrozen_balance(accountVo.getFrozenBalance());
			resultDto.setTotal_balance(accountVo.getTotalBalance());
			resultDto.setCurrency(accountVo.getCurrency());
			resultDto.setMember_id(accountVo.getMemberId());
			resultDto.setAvailable_balance(accountVo.getAvailableBalance());
		}
		return resultDto;
	}

	/**
	 * 会员操作记录列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member_oper_log_list", method = RequestMethod.GET)
	public String toListMemberOperLog(HttpServletRequest request, HttpServletResponse response) {

		return "business/member/member_oper_log_list";
	}

	/**
	 * 会员操作记录列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member_oper_log", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listMemberOperLog(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<Map> list = memberService.getAllMemberOperLog(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 工单列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "demand_list", method = RequestMethod.GET)
	public String toListDemand(HttpServletRequest request, HttpServletResponse response) {

		return "business/member/demand_list";
	}

	/**
	 * 工单列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "demand", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listDemand(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		param.remove("d_status");
		param.put("d_statusIn", HelpUtils.clearCheckBoxVal($("d_status")));
		List<Map> list = memberService.getAllDemand(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 获得工单数据（含回复信息）
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "showDemand", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Demand getDemand(HttpServletRequest request, HttpServletResponse response) {

		Demand demand = memberService.getDemandById($int("id"));
		List<DemandLog> demandLogList = memberService.getDemandLogByDemandId($int("id"));
		demand.setDemandLogList(demandLogList);

		return demand;
	}

	/**
	 * 新增工单回复
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "addDemandLog", method = RequestMethod.POST)
	@ResponseBody
	public Resp addDemandLog(HttpServletRequest request, HttpServletResponse response, @RequestBody DemandLog demandLog)
			throws Exception {
		demandLog.setLog_type(1);
		demandLog.setCreate_time(HelpUtils.formatDate8(new Date()));

		demandLog.setL_memo(HelpUtils.escapeHTMLTags(demandLog.getL_memo()));

		demandLog.setL_memo_type(1);

		memberService.addDemandLog(demandLog, demandLog.getD_status());

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	@RequestMapping("/asserts_edit")
	public String toEditUser(HttpServletRequest request, HttpServletResponse response) {
//		Map param = $params(request);
		String member_id = $("id");
		String currency = $("currency");
//		List<Map> list = memberService.getAllAsserts(param);
		List<Map> list = daoUtil.queryForList(
				"SELECT m.id, m.m_name,m.phone ,a.currency, a.total_balance, a.frozen_balance FROM m_account a, m_member m WHERE a.member_id = m.id "
						+ " AND a.currency = ? AND m.id = ?",
				currency, member_id);
		Map map = list.get(0);
		BigDecimal total_balance = (BigDecimal) map.get("total_balance");
		BigDecimal frozen_balance = (BigDecimal) map.get("frozen_balance");
		map.put("total_balance", total_balance.toPlainString());
		map.put("frozen_balance", frozen_balance.toPlainString());
		$attr("info", map);
		return "business/member/asserts_edit";
	}

	/**
	 * 跳转otc资产页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/assertsForOtc_edit")
	public String toOtcEditUser(HttpServletRequest request, HttpServletResponse response) {
//		Map param = $params(request);
		String member_id = $("id");
		String currency = $("currency");
//		List<Map> list = memberService.getAllAsserts(param);
		List<Map> list = daoUtil.queryForList(
				"SELECT m.id, m.m_name,m.phone ,a.currency, a.total_balance, a.frozen_balance FROM o_account a, m_member m WHERE a.member_id = m.id "
						+ " AND a.currency = ? AND m.id = ?",
				currency, member_id);
		Map map = list.get(0);
		BigDecimal total_balance = (BigDecimal) map.get("total_balance");
		BigDecimal frozen_balance = (BigDecimal) map.get("frozen_balance");
		map.put("total_balance", total_balance.toPlainString());
		map.put("frozen_balance", frozen_balance.toPlainString());
		$attr("info", map);
		return "business/member/assertsForOtc_edit";
	}

	/**
	 * 修改otc资产
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/edit_OtcAsserts", method = RequestMethod.POST)
	@ResponseBody
	public Resp editOtcUser(HttpServletRequest request) {
		String member_id = $("member_id");
		String total_balance = $("total_balance");
		String frozen_balance = $("frozen_balance");
		String currency = $("currency");
		daoUtil.update(
				"UPDATE o_account SET total_balance = ?, frozen_balance = ? where member_id = ? and currency = ?",
				total_balance, frozen_balance, member_id, currency);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	@RequestMapping(value = "/edit_asserts", method = RequestMethod.POST)
	@ResponseBody
	public Resp editUser(HttpServletRequest request) {
		String member_id = $("member_id");
		String total_balance = $("total_balance");
		String frozen_balance = $("frozen_balance");
		String currency = $("currency");
		daoUtil.update(
				"UPDATE m_account SET total_balance = ?, frozen_balance = ? where member_id = ? and currency = ?",
				total_balance, frozen_balance, member_id, currency);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 用户资产列表界面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "asserts_list", method = RequestMethod.GET)
	public String toListAsserts(HttpServletRequest request, HttpServletResponse response) {

		return "business/member/asserts_list";
	}

	/**
	 * 跳转otc资产
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "assertsForOtc_list", method = RequestMethod.GET)
	public String toAssertsForOtc_list(HttpServletRequest request, HttpServletResponse response) {

		return "business/member/assertsForOtc_list";
	}


	/**
	 * otc资产
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "assertsForOtc", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listAssertsForOtc(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<Map> list = memberService.getAllAssertsOtc(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}
	/**
	 * 用户资产列表数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "asserts", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listAsserts(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<Map> list = memberService.getAllAsserts(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 用户资产列表归集界面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "asserts_list_collection", method = RequestMethod.GET)
	public String toAssertsCollection(HttpServletRequest request, HttpServletResponse response) {

		return "business/member/asserts_list_collection";
	}

	/**
	 * 用户资产列表数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "asserts_collection", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map collectionAsserts(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<Map> list = memberService.getAssertsCollection(param);
		for (Map m : list) {
			// 将行转列的数据拆分为数组
			String[] currency = ((String) m.get("currency")).split(",");
			String[] total_balances = ((String) m.get("total_balance")).split(",");
			String[] frozen_balances = ((String) m.get("frozen_balance")).split(",");
			// 将不需要的数据移除
			m.remove("currency");
			m.remove("total_balance");
			m.remove("frozen_balance");
			// 循环币种将数据重新封装到MAP
			int j = currency.length;
			for (int i = 0; i < j; i++) {
				// 先初始化为zero的数据
				String total_balance = "0";
				String frozen_balance = "0";
				// 去掉非zero数据小数点后面的zero
				if (!HelpUtils.nullOrBlank(total_balances[i])
						&& new BigDecimal(total_balances[i]).compareTo(BigDecimal.ZERO) != 0) {
					total_balance = new BigDecimal(total_balances[i]).stripTrailingZeros().toPlainString();
				}
				if (!HelpUtils.nullOrBlank(frozen_balances[i])
						&& new BigDecimal(frozen_balances[i]).compareTo(BigDecimal.ZERO) != 0) {
					frozen_balance = new BigDecimal(frozen_balances[i]).stripTrailingZeros().toPlainString();
				}
				// 将数据存到map
				m.put(currency[i] + "_total_balance", total_balance);
				m.put(currency[i] + "_frozen_balance", frozen_balance);
			}
		}
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 	用户画像
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member_trace", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Map listMemberTrace(HttpServletRequest request, HttpServletResponse response) {
		ExecutorService member_trace_pool = Executors.newFixedThreadPool(4, new ZzexThreadFactory("member_trace_pool"));
		CountDownLatch countDownLatch = new CountDownLatch(6);
		Integer memberId = $int("memberId");
        // 基本信息
        Map infoMap = daoUtil.queryForMap("SELECT m.id, m.m_name,m.phone ,m.reg_time, m.last_login_time, m.last_mod_pwd_time, "
                + "m.last_login_ip, m.introduce_m_id, i.audit_time, i.id_status, i.family_name, "
                + "i.given_name, i.middle_name, i.id_number, i.id_back_img, i.id_front_img, i.id_handheld_img "
                + "FROM m_member m LEFT JOIN m_auth_identity i ON m.id = i.id WHERE m.id = ?", memberId);
//		member_trace_pool.execute(()->{
//			// 充值汇总
//			logger.warn("充值汇总开始");
//			long startTime = System.currentTimeMillis();
//			List rechargeSum = daoUtil.queryForList("select SUM(r_amount) sum, currency from m_coin_recharge WHERE member_id = ? and r_status = 1 GROUP BY currency", memberId);
//			infoMap.put("rechargeSum", rechargeSum);
//			countDownLatch.countDown(); //5
//			long endTime = System.currentTimeMillis(); //获取结束时间
//			logger.warn("充值汇总结束  " + (endTime - startTime));
//		});

        member_trace_pool.execute(()->{
            // 充值信息
            List rechargeLst = daoUtil.queryForList("SELECT currency, r_amount, r_create_time, r_status FROM m_coin_recharge WHERE member_id = ? and r_status = 1 order by currency_id, r_create_time", memberId);
            infoMap.put("rechargeLst", rechargeLst);
            countDownLatch.countDown(); //5
        });

		member_trace_pool.execute(()->{
			// 提现汇总
			List withdrawSum = daoUtil.queryForList("SELECT SUM(w_amount) sum, currency FROM m_coin_withdraw WHERE member_id = ? and w_status = 1 GROUP BY currency", memberId);
			infoMap.put("withdrawSum", withdrawSum);
			countDownLatch.countDown(); //4
		});
        member_trace_pool.execute(()->{
            // 资产信息
            List accountsLst = daoUtil.queryForList("SELECT currency, total_balance, frozen_balance FROM m_account WHERE member_id = ?", memberId);
            infoMap.put("accountsLst", accountsLst);
            countDownLatch.countDown(); //3
        });

        member_trace_pool.execute(()->{
            List otcAccountsLst = daoUtil.queryForList("SELECT * FROM o_account WHERE member_id = ?", memberId);
            infoMap.put("otcAccountsLst", otcAccountsLst);
            countDownLatch.countDown(); //2
        });
		member_trace_pool.execute(()->{
			List otcAccountsLst = daoUtil.queryForList("SELECT * FROM o_account_info WHERE member_id = ? and is_delete = ?", memberId,0);
			infoMap.put("otcAccountInfoLst", otcAccountsLst);
			countDownLatch.countDown(); //2
		});
		member_trace_pool.execute(()->{
            // 提现信息
            List withdrawLst = daoUtil.queryForList("SELECT currency, w_amount, w_create_time, w_status FROM m_coin_withdraw WHERE member_id = ? order by currency_id, w_create_time", memberId);
            infoMap.put("withdrawLst", withdrawLst);
            countDownLatch.countDown(); //1
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        member_trace_pool.shutdown();
        return infoMap;
	}

	/**
	 * 会员秘钥管理
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member_api_token_list", method = RequestMethod.GET)
	public String toMemberApiTokenList(HttpServletRequest request, HttpServletResponse response) {

		// 1.分页找到所有用户apiToken信息

		return "business/member/member_api_token_list";
	}

	/**
	 * 会员秘钥管理
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member_api_token", method = RequestMethod.GET)
	@ResponseBody
	public Map toMemberApiToken(HttpServletRequest request, HttpServletResponse response) {
		// 1.分页找到所有用户apiToken信息
		Map param = $params(request);
		List<Map> list = memberService.getApiTokenPage(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 表：frm_function insert into frm_function
	 * (id,fuc_parent_id,fuc_name,fuc_desc,fuc_url,fuc_order,is_hidden,is_need_auth,is_need_log,is_use,is_display_in_menu)
	 * values('CZTX-QBDZ','CZTX','钱包地址','钱包地址','/member/member_coin_recharge_addr_list',40,0,1,0,1,1);
	 *
	 * 表：frm_function_url insert into frm_function_url
	 * (frm_function_id,fuc_url_url,fuc_url_desc,is_need_log)values('CZTX-QBDZ','/member/member_coin_recharge_addr','钱包地址','0');
	 *
	 * 表： 充值地址-页面跳转
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member_coin_recharge_addr_list", method = RequestMethod.GET)
	public String toCoinRechargeAddrList(HttpServletRequest request, HttpServletResponse response) {

		return "business/member/coin_recharge_addr_list";
	}

	/**
	 * 充值地址-获取数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member_coin_recharge_addr", method = RequestMethod.GET)
	@ResponseBody
	public Map toCoinRechargeAddr(HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> param = $params(request);
		String hasMember = (String) param.get("hasMember");
		// hasMember=true那么从已分配的查找
		if ("true".equalsIgnoreCase(hasMember)) {
			List<Map> list = memberService.getMemberCoinRechargeAddr(param);
			Map map = new HashMap();
			map.put("Rows", list);
			map.put("Total", param.get("total"));
			return map;
		} else {
			List<Map> list = memberService.getMemberCoinRechargeAddrPool(param);
			Map map = new HashMap();
			map.put("Rows", list);
			map.put("Total", param.get("total"));
			return map;
		}

	}

	/**
	 * 锁仓释放页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "lock_release", method = RequestMethod.GET)
	public String lockRelease() {

		return "business/member/lock_release";
	}

	/**
	 * 锁仓充值页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "lock_recharge", method = RequestMethod.GET)
	public String lockRecharge() {

		return "business/member/lock_recharge";
	}

	@RequestMapping(value = "lock_recharge_list", method = RequestMethod.GET)
	@ResponseBody
	public Map lockRechargeList(HttpServletRequest request) {

		Map<String, Object> params = $params(request);
		List<Map> list = lockListService.findLocklist(params);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", params.get("total"));
		return map;
	}

	/**
	 * 锁仓编辑页面
	 *
	 * @param request
	 * @return
	 */
	/*
	 * @RequestMapping(value = "toUpdateLockRecharge", method = RequestMethod.GET)
	 * public String toUpdateLockRecharge(HttpServletRequest request) { Map<String,
	 * Object> params = $params(request); int id = Integer.parseInt((String)
	 * params.get("id")); LockRecharge lock = lockRechargeService.findOne(id);
	 * $attr("info", lock); return "business/member/lock_recharge_edit"; }
	 * 
	 *//**
		 * 更新锁仓
		 * 
		 * @param request
		 * @return
		 */
	/*
	 * @RequestMapping(value = "lock_recharge_update", method = RequestMethod.POST)
	 * 
	 * @ResponseBody public Resp lockRechargeUpdate(HttpServletRequest request) {
	 * Map<String, Object> params = $params(request);
	 * lockRechargeService.updateLockRechargeStatus(params); return new
	 * Resp(Resp.SUCCESS, Resp.SUCCESS_MSG); }
	 * 
	 *//**
		 * 锁仓释放列表
		 * 
		 * @param request
		 * @return
		 *//*
			 * @RequestMapping(value = "lock_release_list", method = RequestMethod.GET)
			 * 
			 * @ResponseBody public Map lockReleaseList(HttpServletRequest request) {
			 * 
			 * Map<String, Object> params = $params(request); List list =
			 * lockListService.findManageByPage(params); Map map = new HashMap();
			 * map.put("Rows", list); map.put("Total", params.get("total")); return map; }
			 */
	// ============= OTC新增内容

	/**
	 * 商家申请审核
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "merchant_audit_edit", method = RequestMethod.GET)
	public String toEditMerchantAudit(HttpServletRequest request, HttpServletResponse response) {
		MerchantDTO info = merchantManager.findById($int("id"));
		$attr("merchantinfo", info);
		return "otc/merchant_audit_edit";
	}

	/**
	 * 商家申请列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "merchant_audit_list", method = RequestMethod.GET)
	public String toListMerchantAudit(HttpServletRequest request, HttpServletResponse response) {
		return "otc/merchant_audit_list";
	}


	/**
	 * 商家申请列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "merchant_audit", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listMerchantAudit(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<MerchantDTO> list = merchantManager.findByConditionPage(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 申诉交易订单数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "trade_complain_audit", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listTradeComplainAudit(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		param.put("complainTypeStr", ComplainTypeEnum.NORMAL.getType() +","+ComplainTypeEnum.OTHER.getType());
		List<OTCTradeDTO> list = oTCTradeManager.findByConditionPage(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 申诉交易订单列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "trade_complain_audit_list", method = RequestMethod.GET)
	public String toListTadeComplainAudit(HttpServletRequest request, HttpServletResponse response) {
		return "otc/trade_complain_audit_list";
	}

	/**
	 * 申诉交易裁决
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "trade_complain_audit_edit", method = RequestMethod.GET)
	public String toEditTradecomplainAudit(HttpServletRequest request, HttpServletResponse response) {
		OTCTradeDTO info = oTCTradeManager.findById($int("id"));
		$attr("complainTradeInfo", info);
		return "otc/trade_complain_audit_edit";
	}

	/**
	 * otc订单数据列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "otc_order_list", method = RequestMethod.GET)
	public String toListOtcOrder(HttpServletRequest request, HttpServletResponse response) {

		return "business/data/otc_order_list";
	}

	/**
	 * otc订单数据列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "otcOrder", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listOtcOrder(HttpServletRequest request, HttpServletResponse response) {

		Map param = $params(request);
		param.put("status", OrderStatusEnum.WATTING);
		String sortname = String.valueOf(param.get("sortname"));
		if (HelpUtils.nullOrBlank(sortname)) {
			param.put("sortname", "create_time");
			param.put("sortorder", "desc");
		}
		List<OTCOrderDTO> list = oTCOrderManager.findByConditionPage(param);
		List<OTCOrderResp> result = null;
		if(!CollectionUtils.isEmpty(list)) {
			result = additionalOtcOrderData(list);
		}
		Map map = new HashMap();
		map.put("Rows", result);
		map.put("Total", param.get("total"));
		return map;
	}

	private List<OTCOrderResp> additionalOtcOrderData(List<OTCOrderDTO> list) {
		List<OTCOrderResp> result = new ArrayList<OTCOrderResp>();
		for (OTCOrderDTO dto : list) {
			OTCOrderResp resp = new OTCOrderResp();
			//
			resp.fromOTCOrderDTO(dto);
			if (!StringUtils.isBlank(dto.getAcountId())) {
				Map<String, Object> param = HelpUtils.newHashMap("id_list", dto.getAcountId());
				List<AccountInfoDTO> accountInfos = oTCAccountInfoManager.findByConditionPage(param);
				for (AccountInfoDTO accountInfo : accountInfos) {
					if (accountInfo.getType().getType() == PayTypeEnum.BANK.getType()) {
						resp.setBankAccount(accountInfo.getAccount());
					}
					if (accountInfo.getType().getType() == PayTypeEnum.ALIPAY.getType()) {
						resp.setAlipayAccount(accountInfo.getAccount());
					}
					if (accountInfo.getType().getType() == PayTypeEnum.WXPAY.getType()) {
						resp.setWxAccount(accountInfo.getAccount());
					}
				}
			}
			try {
				String limitAmount = dto.getMinQuote().stripTrailingZeros().toPlainString() + "-"
						+ dto.getMaxQuote().stripTrailingZeros().toPlainString();
				resp.setTemp(limitAmount);
			}catch (Exception e){
				resp.setTemp("异常限额");
			}
			result.add(resp);
		}
		return result;
	}

	@RequestMapping(value = "otcOrderCancel/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ObjResp cancelOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable int id) {
		OTCOrderDTO oTCOrderDTO = oTCOrderManager.findById(id);
		if (oTCOrderDTO == null) {
			return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ORDER_DOES_NOT_EXIST.getErrorENMsg(), null);
		}
		ObjResp resp = oTCTradeManager.cancelOrder(oTCOrderDTO);
		return resp;
	}

	/**
	 * otc 商家数据列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(value = "otc_merchant_list", method = RequestMethod.GET)
	public String toListOtcMerchant(HttpServletRequest request, HttpServletResponse response) {
		return "business/data/otc_merchant_list";
	}

	/**
	 *跳转到商家操作记录列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "otc_cancel_business", method = RequestMethod.GET)
	public String toCancelBusiness(HttpServletRequest request, HttpServletResponse response) {
		return "otc/merchantLog_audit_list";
	}


	/**
	 * otc 管理员注销商家操作记录列表
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "cancelbus", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map cancelbus(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<MerchantLogDTO> list = merchantLogManager.getCancelBusPage(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * otc 商家数据列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "otcMerchant", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listOtcMerchant(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<MerchantDTO> list = merchantManager.findByConditionPage(param);
		List<OTCMerchantResp> result = null;
		if(!CollectionUtils.isEmpty(list)) {
			result = additionalOtcMerchantData(list);
		}
		Map map = new HashMap();
		map.put("Rows", result);
		map.put("Total", param.get("total"));
		return map;
	}

	private List<OTCMerchantResp> additionalOtcMerchantData(List<MerchantDTO> list) {
		List<OTCMerchantResp> result = new ArrayList<OTCMerchantResp>();
		for (MerchantDTO dto : list) {
			OTCMerchantResp resp = new OTCMerchantResp();
			resp.fromMerchantDTO(dto);
			Map<String, Object> param = HelpUtils.newHashMap("memberId", dto.getMemberId());
			List<AccountInfoDTO> accountInfos = oTCAccountInfoManager.findByConditionPage(param);
			for (AccountInfoDTO accountInfo : accountInfos) {
				if (accountInfo.getType().getType() == PayTypeEnum.BANK.getType()) {
					resp.setBankAccount(accountInfo.getAccount());
				}
				if (accountInfo.getType().getType() == PayTypeEnum.ALIPAY.getType()) {
					resp.setAlipayAccount(accountInfo.getAccount());
				}
				if (accountInfo.getType().getType() == PayTypeEnum.WXPAY.getType()) {
					resp.setWxAccount(accountInfo.getAccount());
				}
			}
			int consum = oTCTradeManager.getConsumingTime(HelpUtils.newHashMap("memberId", dto.getMemberId(), "status", TradeStatusEnum.DONE));
			resp.setConsumingTime(consum);
			result.add(resp);
		}
		return result;
	}

	/**
	 * 修改otc 商家数据页面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "otc_merchant_edit", method = RequestMethod.GET)
	public String toEditMerchantJsp(Integer id ,HttpServletRequest request, HttpServletResponse response) {
		Map param = new HashMap<>();
		param.put("id",id);
		List<MerchantDTO> page = merchantManager.findByConditionPage(param);
		MerchantDTO info = page.get(0);
		$attr("info",info);
		return "business/data/otc_merchant_edit";
	}

	/**
	 * 修改otc vip
	 *
	 * @return
	 */
	@RequestMapping(value = "otcMerchantEdit", method = RequestMethod.POST)
	@ResponseBody
	public Resp editMerchant(MerchantDTO merchantDTO,String vip, HttpServletRequest request,String lockReleasePercent) {
		if (!HelpUtils.isInteger(vip)){
			return new Resp(Resp.FAIL,"vip值只能是整数");
		}
		Map param = new HashMap(2);
		param.put("vip",merchantDTO.getVip());
		if (merchantDTO.getVip()!= 0 && !CollectionUtils.isEmpty(merchantManager.findByConditionPage(param))){
			return new Resp(Resp.FAIL,"已存在此VIP值，可能是正在申请退商的用户");
		}
		int update = merchantManager.update(merchantDTO);
		return update == 1 ? new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG) : new Resp(Resp.FAIL,"fail");
	}

	
	/**
	 * otc 交易数据列表界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "otc_trade_list", method = RequestMethod.GET)
	public String toListOtcTrade(HttpServletRequest request, HttpServletResponse response) {
		return "business/data/otc_trade_list";
	}
	
	/**
	 * otc订单数据列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "otcTrade", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listOtcTrade(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<OTCTradeDTO> list = oTCTradeManager.findByConditionPage(param);
		List<OTCTradeResp> result = null;
		if(!CollectionUtils.isEmpty(list)) {
			result = additionalOtcTradeData(list);
		}
		Map map = new HashMap();
		map.put("Rows", result);
		map.put("Total", param.get("total"));
		return map;
	}
	
	private List<OTCTradeResp> additionalOtcTradeData(List<OTCTradeDTO> list) {
		List<OTCTradeResp> result = new ArrayList<OTCTradeResp>();
		for (OTCTradeDTO dto : list) {
			OTCTradeResp resp = new OTCTradeResp();
			resp.fromOTCTradeDTO(dto);
			if(dto.getAcountId() != null) {
				AccountInfoDTO accountInfo = oTCAccountInfoManager.findById(dto.getAcountId());
				resp.setPayType(accountInfo.getType());
				resp.setPayAccount(accountInfo.getAccount());
			}
			BigDecimal done = BigDecimal.ZERO;
			if(dto.gettType().getType() == OrderTypeEnum.BUY.getType()) {
				done = dto.getPrice().multiply(dto.getVolume());
			}else {
				done = dto.getVolume();
			}
			resp.setDone(done);
			result.add(resp);
		}
		return result;
	}
	
	/**
	 * otc订单聊天记录
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "otcTradeChatRecord", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public ObjResp getOtcTradeChatRecord(HttpServletRequest request, HttpServletResponse response) {
		String result = "";
		Map<String, Object> map = $params(request);
		map.put("sortorder", "desc");
		map.put("sortname", "create_time");
		map.put("page", 1);
		map.put("pagesize", 100);
		List<WCharRecordDTO> list = wCharRecordManager.findByConditionPage(map);
		if(!CollectionUtils.isEmpty(list)) {
			StringBuilder sb = new StringBuilder();
			for(WCharRecordDTO dto : list) {
				if(dto.getTaker().getType() == TakerEnum.SELF.getType()) {
					sb.append(dto.getNick_name());
				}else {
					sb.append(dto.getOppo_nick_name());
				}
				sb.append("  ");
				sb.append(dto.getCreateTime());
				sb.append("<br/>");
				sb.append(dto.getCharContent());
				sb.append("<br/>");
			}
			result = sb.toString();
		}else {
			result = "没有聊天记录";
		}
		ObjResp resp = new ObjResp();
		resp.setData(result);
		return resp;
	}

	/**
	 * 返佣邀请人数
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/returncommi/intro", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map findMemberIntroByPage(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<Map<String,Object>> list = memberService.findMgrMemberIntroByPage(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}



	/**
	 * 返佣明细
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/returncommi/detail", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map findReturnCommiDetail(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<Map<String,Object>> list = returnCommiService.findMgrReturnCommiAmountByPage(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 返佣币种明细
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/returncommi/currency/detail", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map findMgrReturnCommiAmountByPage(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<Map<String,Object>> list = returnCommiService.findMgrReturnCommiCurrAmountByPage(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}
	/**
	 * 返佣明细页面
	 * @return
	 */
	@RequestMapping(value = "/returncommi/list_page", method = RequestMethod.GET)
	public String toReturnCommiDetailPage() {
		return "business/member/returncommi/returncommi_intro_list";
	}


	/**
	 * 导出 充值记录
	 *
	 * @param request
	 * @param response
	 * @throws BusinessException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/coinRecharge/export", method = RequestMethod.GET)
	public void coinRechargeExport(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map<String,Object> param = $params(request);
		String fileName = "充值记录报表-"+HelpUtils.getCurrTime();
		ExportExcel excel = new ExportExcel(fileName, CoinRechargeExcelVo.class);
		List<CoinRechargeExcelVo> list = memberService.findCoinRechargeList(param);
		excel.setDataList(list);
		excel.write(response,  fileName + ".xls");
	}

	/**
	 * 导出 提现记录
	 *
	 * @param request
	 * @param response
	 * @throws BusinessException
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/coinWithdraw/export", method = RequestMethod.GET)
	public void withdrawRechargeExport(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map<String,Object> param = $params(request);
		String fileName = "提现记录报表-"+HelpUtils.getCurrTime();
		ExportExcel excel = new ExportExcel(fileName, CoinWithdrawExcelVo.class);
		List<CoinWithdrawExcelVo> list = memberService.findWithdrawCoinList(param);
		excel.setDataList(list);
		excel.write(response,  fileName + ".xls");
	}

	/**
	 * 购买释放
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/buy_release_list", method = RequestMethod.GET)
	public String toBuyReleaseList(HttpServletRequest request, HttpServletResponse response) {
		return "business/member/buy_release_list";
	}

	/**
	 * 购买释放
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/buy_release_list_data", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map getBuyReleaseList(HttpServletRequest request) {
		Map<String,Object> param = $params(request);
		List<Map<String,Object>> list = currencyLockAccountService.findMgrByPage(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 购买释放明细
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/buy_release_list_data/detail", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map getBuyReleaseDetailList(HttpServletRequest request) {
		Map<String,Object> param = $params(request);
		List<Map<String,Object>> list = currencyLockAccountDetailService.findMgrByPage(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}
	

	/**
	 * 用户冻结释放列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "frozen_release_list",method = RequestMethod.GET)
	public String toListFrozenRelease(HttpServletRequest request){
		return "business/member/frozen_release_list";
	}

	/**
	 * 用户冻结资产列表数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "frozen_release", method = RequestMethod.GET, consumes = "application/json")
	@ResponseBody
	public Map listFrozenRelease(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		List<Map> list = memberService.getAllManFrozen(param);
		Map map = new HashMap();
		map.put("Rows", list);
		map.put("Total", param.get("total"));
		return map;
	}

	/**
	 * 处理用户冻结数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "frozen_release_edit", method = RequestMethod.GET)
	public String toEditFrozenRelease(HttpServletRequest request, HttpServletResponse response) {
		Long id = $long("id");
		Map param = new HashMap();
		param.put("id",id);
		List<Map> info = memberService.getAllManFrozen(param);
		$attr("info",info.get(0));
		return "business/member/frozen_release_edit";
	}

	/**
	 * 处理用户冻结数据
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "frozen_release_edit_do", method = RequestMethod.POST)
	@ResponseBody
	public synchronized Resp editFrozenRelease(HttpServletRequest request, HttpServletResponse response) {
		Long id = $long("id");
		ManFrozen oneFrozen = memberService.getManFrozenById(id);
		return memberService.UnFrozenOne(oneFrozen);
	}

	/**
	 * 用户登录历史记录界面
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "member_login_history", method = RequestMethod.GET)
	public String loginHistoryFace(HttpServletRequest request, HttpServletResponse response) {

		return "business/member/member_login_history";
	}
	/**
	 * 用户登录历史记录
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "loginHistory", method = RequestMethod.GET)
	@ResponseBody
	public Map loginHistory(HttpServletRequest request, HttpServletResponse response) {
		Map param = $params(request);
		param.put("oper_type", 1);
		List<Map> loginList = memberService.getAllMemberOperLog(param);
		Map resultMap = new HashMap();
		resultMap.put("Rows", loginList);
		resultMap.put("Total", param.get("total"));
		return resultMap;
	}
}
