package com.contract.app.common;

import com.contract.entity.CContractOrder;
import redis.clients.jedis.BinaryClient;

import java.util.ArrayList;
import java.util.List;

public class MappingUtil {

	public static final String handleRegister="/customer/handleRegister";//注册
	public static final String login="/customer/login";//登陆
	public static final String handleForgetpwd="/customer/handleForgetpwd";//忘记密码
	public static final String editPass="/customer/editPass";
	public static final String exit="/customer/exit";
	public static final String share="/share";//分享注册页
	public static final String downLoad="/downLoad";
	public static final String allticker="/api/v1/allticker";
	
	public static final String sendPhonecode="/util/sendPhonecode";//发送通用验证码
	public static final String getAppVersion="/utils/getAppVersion";
	public static final String upload="/utils/upload";
	public static final String validCode="/utils/validCode";
	public static final String getSysInfo="/utils/getSysInfo";
	
	
	public static final String queryBanner="/home/queryBanner";
	public static final String queryNotice="/home/queryNotice";
	public static final String getNotice="/home/getNotice";
	public static final String querySymbol="/home/querySymbol";
	public static final String showKline="kline";
	
	
	public static final String getMyInfos="/account/getMyInfos";
	public static final String getWallet="/account/getWallet";
	public static final String handleAuth="/account/handleAuth";//申请实名
	public static final String queryPayChannel="/account/queryPayChannel";
	public static final String addPayChannel="/account/addPayChannel";
	public static final String removePayChannel="/account/removePayChannel";
	public static final String queryUsdtDetail="/account/queryUsdtDetail";
	public static final String queryZCDetail="/account/queryZCDetail";
	public static final String handleCashUsdt="/account/handleCashUsdt";
	public static final String getMyShare="/account/getMyShare";
	public static final String handleTrans="/account/handleTrans";
	public static final String queryTeam="/account/queryTeam";
	public static final String getShareinfo="/account/getShareinfo";
	public static final String getKlineList="/account/getKlineList";
	public static final String getKlineCurrent="/account/getKlineCurrent";

	
	public static final String queryContractOrder="/trade/queryContractOrder";//获取订单
	public static final String handlContractOrder="/trade/handlContractOrder";//处理合约下单
	public static final String queryCoins="/trade/queryCoins";
	public static final String queryGearing="/trade/queryGearing";
	public static final String handleCloseOrder="/trade/handleCloseOrder";
	public static final String handleEditOrder="/trade/handleEditOrder";
	public static final String queryZCmoney="/trade/queryZCmoney";
	public static final String queryContractOrderByType="/trade/queryContractOrderByType";//获取订单
	
	public static final String queryMyEntrust="/trade/queryMyEntrust";
	public static final String handleEntrust="/trade/handleEntrust";
	public static final String handleCancelEntrust="/trade/handleCancelEntrust";
	
	
	public static final String handlZCOrder="/trade/handlZCOrder";
	public static final String handleCloseZc="/trade/handleCloseZc";
	
	public static final String queryZCorder="/trade/queryZCorder";
	
	public static final String handleZCStop="/trade/handleZCStop";

	public static final String historyKline="/trade/historyKline";

	public static final String documentary="/trade/documentary";//跟单交易
	public static final String gdHistory="/gd/gdHistory";//查询历史跟单订单
	public static final String assets="/trade/assets";//资产
	public static final String into="/trade/into";//资产



}
