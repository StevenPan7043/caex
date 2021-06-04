package com.contract.cms.common;

import com.contract.entity.CContractOrder;

import java.util.ArrayList;
import java.util.List;

public class MappingUtils {

	public static final String login="/login";//登陆
	public static final String exit="/exit";//退出
	public static final String verifyImg="/verifyImg";//验证码
	public static final String enter="/enter";//
	public static final String clear="/clear";
	public static final String sendLoginCode="/sendLoginCode";
	
	public static final String showIndex="/plat/index";
	public static final String showWelcome="/plat/welcome";
	
	public static final String uploadFile="/utils/uploadFile";
	public static final String upload_RichFile="/utils/uploadRichFiles";
	
	
	/**********权限*************/
	public static final String showRoleList="/role/showRoleList";
	public static final String updateRolestatus="/role/updateRolestatus";
	public static final String showEditRole="/role/showEditRole";
	public static final String editRole="/role/editRole";
	
	public static final String showUserList="/role/showUserList";
	public static final String updateUserstatus="/role/updateUserstatus";
	public static final String showEditUser="/role/showEditUser";
	public static final String editUser="/role/editUser";
	public static final String restUserPwd="/role/restUserPwd";
	
	public static final String showEditPassword="/base/showEditPassword";
	public static final String editPassword="/base/editPassword";
	
	/****banner*/
	public static final String showBanners="/banner/showBanners";
	public static final String deleteBanner="/banner/deleteBanner";
	public static final String showBannerEdit="/banner/showBannerEdit";
	public static final String editBanner="/banner/editBanner";
	
	/****资讯/协议****/
	public static final String showNotices="/news/showNotices";
	public static final String deleteNew="/news/deleteNew";
	public static final String showNewEdit="/news/showNewEdit";
	public static final String editNew="/news/editNew";
	
	/*******会员********/
	public static final String showCustomerList="/customer/showCustomerList";
	public static final String showEditCustomer="/customer/showEditCustomer";
	public static final String updateCusStatus="/customer/updateCusStatus";
	public static final String resetPwd="/customer/resetPwd";
	
	public static final String showEditcus="/customer/showEditcus";
	public static final String editcus="/customer/editcus";
	
	public static final String showRecharge="/customer/showRecharge";
	public static final String getCustomer="/customer/getCustomer";
	public static final String handleMoney="/customer/handleMoney";
	public static final String showWallet="/customer/showWallet";
	public static final String showMoneyList="/customer/showMoneyList";
	public static final String updateCusAuthflag="/customer/updateCusAuthflag";
	public static final String showUsdtRechargeList="/customer/showUsdtRechargeList";
	public static final String showUsdtTransferList="/customer/showUsdtTransferList";
	public static final String showSalesman="/customer/showSalesman";
	public static final String handleSaleman="/customer/handleSaleman";
	public static final String showCusReport="/customer/showCusReport";
	
	public static final String showAuth="/customer/showAuth";
	
	public static final String showUnder="/customer/showUnder";
	public static final String showTransferUnder="/customer/showTransferUnder";

	public static final String showECR20Logs="/customer/showECR20Logs";
	
	/****参数设置*****/
	public static final String showParams="/params/showParams";
	public static final String editParam="/params/editParam";
	public static final String showEditparam="/params/showEditparam";
	public static final String showCoins="/params/showCoins";
	public static final String showEditCoin="/params/showEditCoin";
	public static final String editCoin="/params/editCoin";
	
	/**
	 * USDT
	 */
	public static final String showCashUsdtlist="/cash/showCashUsdtlist";
	public static final String handlerefundUsdtCash="/cash/handlerefundUsdtCash";
	public static final String showUsdtCheck="/cash/showUsdtCheck";
	public static final String handUsdtcheck="/cash/handUsdtcheck";
	public static final String dzUsdtSuccess="/cash/dzUsdtSuccess";
	
	public static final String showOrderList="/order/showOrderList";
	public static final String showReport="/order/showReport";
	public static final String showEntrustList="/order/showEntrustList";
	
	public static final String showZcList="/order/showZcList";

	public static final String handleCloseOrder="/order/handleCloseOrder";

	public static final String showRecordList = "/cycle/showRecordList";

	public static final String showUserBonus = "/cycle/showUserBonusList";

	public static final String showTeamBonus = "/cycle/showTeamBonusList";

	public static final String showDetailList = "/cycle/showDetailList";
}
