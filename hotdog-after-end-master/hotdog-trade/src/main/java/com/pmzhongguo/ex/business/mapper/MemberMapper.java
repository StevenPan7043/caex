package com.pmzhongguo.ex.business.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.dto.AccountDto;
import com.pmzhongguo.ex.business.entity.*;
import com.pmzhongguo.ex.business.vo.*;
import com.pmzhongguo.ex.core.mapper.SuperMapper;
import com.pmzhongguo.ex.framework.entity.Country;
import org.apache.ibatis.annotations.Param;


public interface MemberMapper extends SuperMapper {

	public void addMember(Member member);
	
	public void updateMember(Member member);

	public List listMemberPage(Map params);

	public Member getMemberById(Integer id);

	public List<MemberRspVo> getMemberByIdList(@Param("idList")String  idList);
	public List<MemberRspVo> getMemberListByIntroIdStr(@Param("idList") String  idList);

	public Demand getDemandById(Integer id);

	public RespApiToken getApiToken(Map params);

	public Account getAccount(Map params);
	public Account getOTCAccount(Map params);

	public void addFrozenBalance(Account account);

	public void reduceFrozenBalance(Account account);
	
	public void addAccount(Account account);
	public void updateAccount(Account account);

	public void addTotalBalance(Account account);
	public void batchAddZCTotalBalanceByMemberId(List<Map<String, Object>> list);

	public void returnFrozenBalance(Account account);

	public List<AccountDto> listAccounts(Integer memberId);

	public List<AccountDto> listAccountsByPage(Map<String, Object> reqMap);

	public CoinWithdraw getCoinWithdraw(Map<String, Object> orderMap);

	public void updateCoinWithdraw(CoinWithdraw coinWithdraw);

	public void updateTransferCoinWithdraw(CoinWithdraw coinWithdraw);

	public void addCoinWithdraw(CoinWithdraw coinWithdraw);

	public CoinWithdraw getCoinWithdrawByID(Long id);

	public List<CoinWithdraw> listCoinWithdrawPage(Map param);
	
	public List<CoinWithdraw> listCoinWithdrawMemberPage(Map param);


	public List<CoinWithdraw> getSumAmountGroupByMemberId(Map param);

	public List<Map> listMemberWithdrawAddrById(Map param);

	public List<CoinRecharge> listCoinRechargePage(Map param);
	
	public List<CoinRecharge> listCoinRechargeMemberPage(Map param);

	public AuthIdentity getAuthIdentityByID(Integer id);

	public List<AuthIdentity> listAuthIdentityPage(Map param);

	public void updateAuthIdentity(AuthIdentity authIdentity);

	public void updateFaceIdAuthIdentity(AuthIdentity authIdentity);
	
	public void memberUpdateAuthIdentity(AuthIdentity authIdentity);

	public void memberUpdateAuthIdentityByFaceId(AuthIdentity authIdentity);
	
	public void addAuthIdentity(AuthIdentity authIdentity);
	
	public void memberAddDemand(Demand demand);

	public AuthVideo getAuthVideoByID(Integer id);
	
	public AuthVideo getMemberAuthVideoByID(Integer id);

	public List<AuthVideo> listAuthVideoPage(Map param);

	public void addAuthVideo(AuthVideo authVideo);
	
	public void updateAuthVideo(AuthVideo authVideo);

	public List<Map> listMemberOperLogPage(Map param);
	
	public List<Map> listIntroMemberPage(Map param);
	
	public List<Map> listDemandPage(Map param);

	public Member getMemberBy(Map params);

	public String listRechargeAddr(Map<String, Object> paramMap);

	public void addCoinRecharge(CoinRecharge coinRecharge);
	
	public void addCoinRechargeByMember(CoinRecharge coinRecharge);

	public void addCoinWithdrawAddr(CoinWithdrawAddr coinWithdrawAddr);

	public void delCoinWithdrawAddr(CoinWithdrawAddr coinWithdrawAddr);

	public void updateDemand(Demand demand);

	public List<DemandLog> getDemandLogByDemandId(Integer id);

	public void memberAddDemandLog(DemandLog dLog);

	public List<Map> getAssertsPage(Map param);
	public List<Map> getAssertsOtcPage(Map param);

	public List<Map> getAssertsCollectionPage(Map param);

	public void addManFrozen(CoinRecharge coinRecharge);

	public List<ManFrozen> listWaitUnFrozen();

	public List<Map> listManFrozenPage(Map param);

	public ManFrozen getManFrozenById(Long id);

	public CoinRecharge getCoinRechargeByID(Long id);

	public void confrimCoinRecharge(CoinRecharge coinRecharge);

	public void addCoinRechargeAddrPool(CoinAddressInner coinAddressInner);

	public ApiToken getApiTokenInner(ApiToken apiToken);
	
	public List<ApiToken> getApiTokensInner(Integer member_id);

	public List<RespApiToken> getApiTokens(Integer member_id);

	public void addApiToken(ApiToken apiToken);

	public void updateApiToken(ApiToken apiToken);

	public void delApiToken(Map newHashMap);

	public Integer getApiCount(Integer member_id);


	public List<Map> getMemberApiTokenPage(Map param);

	public List<Map> getMemberCoinRechargeAddrPage(Map param);

	public List<Map> getMemberCoinRechargeAddrPoolPage(Map param);
	public void deleteCoinRecharge(Long id);


    String findIntroduceIdByMemberId(Integer member_id);

    List<Map<String,Object>> findMemberIntroByPage(Map<String, Object> param);

    Integer findIntroAmountByMemberId(Integer memberId);

	List<Map<String,Object>> findIntroByPage(Map<String,Object> param);

	List<CoinRechargeAddr> getCoinRechargeAddrList(CoinRechargeAddr coinRechargeAddr);

    List<CoinRechargeExcelVo> getCoinRechargeList(Map<String, Object> param);

	List<CoinWithdrawExcelVo> getCoinWithdrawList(Map<String, Object> param);

	List<Country> findCountryList();

    Map<String, Object> getEtcTotal(String currency);

	BigDecimal getTotalRecharge(Map<String, Object> param);

	BigDecimal getTotalWithdrawal(Map<String, Object> param);

    Member findMemberByInviteId(Integer introduce_m_id);
    Member findMemberByInviteCode(@Param("invite_code") String invite_code);
}
