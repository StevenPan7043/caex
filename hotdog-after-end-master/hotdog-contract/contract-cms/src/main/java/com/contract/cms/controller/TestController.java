package com.contract.cms.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.contract.common.GetRest;
import com.contract.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.contract.common.DateUtil;
import com.contract.common.PathUtils;
import com.contract.dao.CContractOrderMapper;
import com.contract.dao.CCustomerMapper;
import com.contract.dao.CWalletMapper;
import com.contract.dao.CoinsMapper;
import com.contract.entity.CContractOrder;
import com.contract.entity.CCustomer;
import com.contract.entity.CWallet;
import com.contract.entity.Coins;
import com.contract.entity.CoinsExample;
import com.contract.enums.CoinEnums;
import com.contract.enums.SysParamsEnums;
import com.contract.service.redis.RedisUtilsService;
import com.contract.service.wallet.WalletService;
import com.contract.service.wallet.btc.UsdtSend;
import com.contract.service.wallet.btc.enums.NodeEnums;
import com.contract.service.wallet.huobi.HuoBiUtils;


import com.huobi.response.Kline;
import com.image.common.RestUploadFileInfo;

@Controller
public class TestController {

	
	@Autowired
	private WalletService walletService;
	@Autowired
	private RedisUtilsService redisUtilsService;
	@Autowired
	private CCustomerMapper cCustomerMapper;
	@Autowired
	private CoinsMapper coinsMapper;
	@Autowired
	private CWalletMapper cWalletMapper;
	@Autowired
	private CContractOrderMapper cContractOrderMapper;
	
//	@RequestMapping(value = "/testUsdt")
//	@ResponseBody
//	public RestResponse testUsdt() {
//		Set<String> list=redisUtilsService.getKeys("*");
//		for(String l:list) {
//			redisUtilsService.deleteKey(l);
//		}
//		return GetRest.getSuccess("");
//	}
	
	@RequestMapping(value = "/createQr")
	@ResponseBody
	public RestResponse createQr() {
		List<CCustomer> list=cCustomerMapper.selectByExample(null);
		for(CCustomer l:list) {
			try {
				String content = redisUtilsService.getKey(SysParamsEnums.app_host.getKey()) + "/share/"
						+ l.getInvitationcode();
				RestUploadFileInfo r = com.image.common.Service.uploadQR(PathUtils.cus_qr, content);
				if (r.isStatus()) {
					l.setQrurl(r.getServiceName() + r.getFilePath() + r.getFileName());
				}
			} catch (Exception e) {
				System.out.println("生成二维码失败");
			}
			cCustomerMapper.updateByPrimaryKeySelective(l);
		}
		return GetRest.getSuccess("");
	}
	
	@RequestMapping(value = "/recharge")
	@ResponseBody
	public RestResponse recharge() {
		walletService.autoUsdtRecharge();
		return GetRest.getSuccess("");
	}
	
	@RequestMapping(value = "/testRecharge")
    @ResponseBody
	public RestResponse testRecharge() throws Throwable {
		try {
			String code = UsdtSend.sendrawtransaction(NodeEnums.node_1,"1JuwMsYRoRd6xHP4VrhQWD1NaFVsoPb7i2", "1LLztf4tTo3Qqsi5UhQWUjQ7nebsLPStL4",
					"1ABR6MYXSQn6UdbFdRdX7hTWRzTWNuvBAR", "0.01");
			System.out.println(code);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return GetRest.getSuccess("");
	}
	
	public static void main(String[] args) throws Throwable {
		try {
			for(int i=0;i<1;i++) {
				String code = UsdtSend.sendrawtransaction(NodeEnums.node_1,"1JuwMsYRoRd6xHP4VrhQWD1NaFVsoPb7i2", "1LLztf4tTo3Qqsi5UhQWUjQ7nebsLPStL4",
						"1AuavpB4GJQzLgSPZZ1XrbnC3umjMqmuUF", "0.01");
				System.out.println(code);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/updateYes")
    @ResponseBody
	public RestResponse updateYes() {

		try {
			CoinsExample coinsExample=new CoinsExample();
			coinsExample.setOrderByClause("sort asc");
			List<Coins> coins=coinsMapper.selectByExample(coinsExample);
			for(Coins s:coins) {
				String symbol=s.getSymbol().replace("_", "");
				List<Kline> list=HuoBiUtils.getInstance().queryKline(symbol, "1day", "1");
				if(list!=null && list.size()>0) {
					Kline kline=list.get(0);
					s.setOpenval(new BigDecimal(kline.getOpen()));
					s.setUpdatetime(DateUtil.currentDate());
					coinsMapper.updateByPrimaryKeySelective(s);
				}
				Thread.sleep(1000);
			}
			String coinarr=JSONArray.toJSONString(coins);
			redisUtilsService.setKey("coins_key", coinarr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return GetRest.getSuccess("");
	}
	
	
	@RequestMapping(value = "/autoEntrustOrder")
    @ResponseBody
	public RestResponse autoEntrustOrder() {
		walletService.autoEntrustOrder();
		return GetRest.getSuccess("");
	}
	
	@RequestMapping(value = "/rechargeeth")
    @ResponseBody
	public RestResponse rechargeeth() {
		walletService.autoECR20UsdtRecharge();
		return GetRest.getSuccess("");
	}
	
	
	@RequestMapping(value = "/testRedis")
    @ResponseBody
	public RestResponse testRedis(String ordercode) {
		CContractOrder cContractOrder=cContractOrderMapper.getByOrdercode(ordercode);
		//订单放入缓存
		Integer cid=cContractOrder.getCid();
		Coins coins=coinsMapper.getBySymbol(cContractOrder.getCoin());
		String orderkey="order_"+cid+"_"+coins.getName()+"_"+cContractOrder.getOrdercode();
		redisUtilsService.setKey(orderkey, JSONObject.toJSONString(cContractOrder));
		
		
		//***保存 货币总额
		CWallet  wallet=cWalletMapper.selectByPrimaryKey(cid, CoinEnums.USDT.name());
		redisUtilsService.setOutMoney(cContractOrder, wallet,cid, coins.getName(), true);
		
		return GetRest.getSuccess("");
	}
	
	@RequestMapping(value = "/delRedis")
    @ResponseBody
	public RestResponse delRedis() {
		Set<String> keys=redisUtilsService.getKeys("*_XLM/USDT*");
		redisUtilsService.deleteKey(keys);
		return GetRest.getSuccess("");
	}
	
	@RequestMapping(value = "/guijiECR")
    @ResponseBody
	public void guijiECR() {
		walletService.autoECR20GJ();
	}
	
	@RequestMapping(value = "/createWallet")
    @ResponseBody
	public void createWallet() {
		walletService.createWallet();
	}
}
