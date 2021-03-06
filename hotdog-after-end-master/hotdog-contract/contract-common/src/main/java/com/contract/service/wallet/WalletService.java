package com.contract.service.wallet;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.contract.common.*;
import com.contract.common.mail.SmsSendPool;
import com.contract.common.sms.SmsSend;
import com.contract.dao.*;
import com.contract.entity.*;
import com.contract.enums.*;
import com.contract.service.HuobiUtils;
import com.image.common.RestUploadFileInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;

import com.alibaba.fastjson.JSONObject;
import com.contract.dto.CposDto;
import com.contract.dto.SymbolDto;
import com.contract.dto.TxDto;
import com.contract.service.BipbService;
import com.contract.service.api.ApiTradeService;
import com.contract.service.redis.RedisUtilsService;
import com.contract.service.wallet.btc.UsdtConfig;
import com.contract.service.wallet.btc.UsdtSend;
import com.contract.service.wallet.btc.dto.BitDto;
import com.contract.service.wallet.btc.enums.FeeEnums;
import com.contract.service.wallet.btc.enums.NodeEnums;
import com.contract.service.wallet.eth.ECR20Utils;
import com.contract.service.wallet.eth.ETHService;

@Service
public class WalletService {

	Logger logger = LoggerFactory.getLogger(WalletService.class);
	@Autowired
	private CWalletMapper cWalletMapper;
	@Autowired
	private UsdtRechargeLogMapper usdtRechargeLogMapper;
	@Autowired
	private BipbService bipbService;
	@Autowired
	private CCashUsdtLogsMapper cCashUsdtLogsMapper;
	@Autowired
	private ApiTradeService apiTradeService;
	@Autowired
	private RedisUtilsService redisUtilsService;
	@Autowired
	private CEntrustOrderMapper cEntrustOrderMapper;
	@Autowired
	private ETHService ethService;
	@Autowired
	private TxLogsMapper txLogsMapper;
	@Autowired
	private CCustomerMapper cCustomerMapper;
	@Autowired
	private HuobiUtils huobiUtils;

	/**
	 * ????????????
	 */
	public void createWallet() {
		CWalletExample example = new CWalletExample();
		example.createCriteria().andAddrIsNull();
		List<CWallet> list = cWalletMapper.selectByExample(example);
		for (CWallet l : list) {
			CoinEnums enums = CoinEnums.valueOf(l.getType());
			// ????????????
			handleWallet(l, enums);
		}
	}

	public void handleWallet(CWallet l, CoinEnums enums) {
		switch (enums) {
			case USDT:
				NodeEnums n = NodeEnums.getRandom();
				String addr = UsdtConfig.getInstance(n).getNewAddress();
				l.setAddr(addr);
				l.setNodeid(n.getId());
				try {
					RestUploadFileInfo r = com.image.common.Service.uploadQR(PathUtils.cus_qr, addr);
					if (r.isStatus()) {
						l.setQrurl(r.getServiceName() + r.getFilePath() + r.getFileName());
					}
				} catch (Exception e) {
					System.out.println("??????OMNI???????????????");
				}

				// ??????????????????
				try {
					String password=String.valueOf(l.getCid());
					String path = ECR20Utils.creatAccount(password);// ???????????? ?????????cid
					Credentials wallet = ECR20Utils.loadWallet(path,password);// ???????????? ????????????????????????
					String uuid = wallet.getAddress();// ????????????
					l.setLabel(uuid);
					l.setPath(path);
					l.setPassword(password);
					try {
						RestUploadFileInfo r = com.image.common.Service.uploadQR(PathUtils.cus_qr, uuid);
						if (r.isStatus()) {
							l.setEcrqr(r.getServiceName() + r.getFilePath() + r.getFileName());
						}
					} catch (Exception e) {
						System.out.println("??????ERC20???????????????");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}


				cWalletMapper.updateByPrimaryKeySelective(l);
				break;

			default:
				break;
		}
	}

	/**
	 * usdt??????
	 */
	public void autoUsdtRecharge() {
		for (NodeEnums n : NodeEnums.values()) {
			addUsdtRecharge(n);
		}
	}

	/**
	 * ??????ECR20?????? ??????????????????????????????????????? ??????????????????
	 *
	 * @param n
	 * @return
	 */
	public String addUsdtRecharge(NodeEnums n) {
		// SysInfos infos=sysInfosMapper.getByKey(SysParamsEnums.tx_skip.getKey());
		Integer skip = 0;// Integer.parseInt(infos.getVals());
		Integer num = 2000;
		UsdtConfig config = new UsdtConfig(n);
		List<BitDto> list = config.getTxlist(num, skip);
		if (list != null && list.size() > 0) {
			for (BitDto l : list) {
				if (!l.isValid()) {
					continue;
				}
				BigDecimal usd = new BigDecimal(l.getAmount());
				if (usd.compareTo(new BigDecimal(0.01)) == 0) {// 0.01??????
					continue;
				}
				UsdtRechargeLogExample example = new UsdtRechargeLogExample();
				example.createCriteria().andHashEqualTo(l.getTxid());
				int count = usdtRechargeLogMapper.countByExample(example);
				if (count < 1) {
					try {
						CWallet wallet = cWalletMapper.getByAddr(l.getReferenceaddress());
						if (wallet != null) {// ?????????????????????????????? ??????????????????
							// ??? ????????????????????? ???????????????

							// ???????????????????????????????????????????????? ?????????????????????????????????????????????????????? ???????????????
							UsdtRechargeLog rechargeLog = new UsdtRechargeLog();
							rechargeLog.setCid(wallet.getCid());
							rechargeLog.setHash(l.getTxid());
							rechargeLog.setTimestamp(String.valueOf(l.getBlocktime()));
							rechargeLog.setFromaddr(l.getSendingaddress());
							rechargeLog.setToaddr(l.getReferenceaddress());
							rechargeLog.setValuecoin(l.getAmount());
							rechargeLog.setGasused(l.getFee());
							usdtRechargeLogMapper.insertSelective(rechargeLog);
							bipbService.handleCUsdtDetail(wallet.getCid(), HandleTypeEnums.recharge.getId(),
									StaticUtils.pay_in, l.getTxid(), usd, "???????????????" + l.getSendingaddress() + "?????????(OMNI)",
									wallet.getCid());
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
			// skip=skip+list.size();
			// infos.setVals(String.valueOf(skip));//???????????????????????????????????????
			// sysInfosMapper.updateByPrimaryKeySelective(infos);//
		}
		return "";
	}

	public void autoUsdtAccount() {
		List<CCashUsdtLogs> list = cCashUsdtLogsMapper.queryDzlist();
		for (CCashUsdtLogs l : list) {
			if (!StringUtils.isEmpty(l.getHashcode())) {
				CWallet cWallet = cWalletMapper.selectByPrimaryKey(l.getCid(), CoinEnums.USDT.name());
				NodeEnums n = NodeEnums.getById(cWallet.getNodeid());
				UsdtConfig config = new UsdtConfig(n);
				BitDto transaction = config.getTx(l.getHashcode());
				if (transaction != null && transaction.isValid()) {
					l.setDzstatus(StaticUtils.dz_yes);
					l.setDztime(DateUtil.currentDate());
					cCashUsdtLogsMapper.updateByPrimaryKeySelective(l);
				} else {
					l.setDzstatus(StaticUtils.dz_reset);
					l.setDztime(DateUtil.currentDate());
					cCashUsdtLogsMapper.updateByPrimaryKeySelective(l);
				}
			}
		}
	}

	public void autoUsd() {
		NodeEnums[] enums = NodeEnums.values();
		for (NodeEnums n : enums) {
			String plataddr = n.getAddr();
			System.out.println("-----------------------????????????" + n.getId() + "-----------------------");
			for (FeeEnums f : FeeEnums.values()) {// ?????????????????? ????????????????????????25???
				List<TxDto> list = usdtRechargeLogMapper.queryAddr();// ??????????????????10???
				for (TxDto l : list) {
					try {
						BigDecimal money = l.getCoin();
						System.out.println(" ??????BTC??????/????????????" + l.getToaddr() + " ??????USDT?????????" + plataddr);
						// Thread.sleep(2000);
						String code = UsdtSend.sendrawtransaction(n, l.getToaddr(), f.getFeeaddr(), plataddr,
								money.toPlainString());
						if ("-999".equals(code)) {
							break;
						}
						if (!StringUtils.isEmpty(code) && !"-999".equals(code)) {
							// ????????????????????? ????????????
							l.setTarhash(code);
							usdtRechargeLogMapper.updateStatus(l);
						}
					} catch (Throwable e2) {
						System.err.println(e2.getMessage());
					}
				}
			}
		}
		System.out.println("-----------------------????????????-----------------------");
	}

	/**
	 * ??????
	 */
	public void autoOrder() {
		// ????????????????????????
		String contract_auto_out = redisUtilsService.getKey(SysParamsEnums.contract_auto_out.getKey());
		BigDecimal contractAutoOut = new BigDecimal(contract_auto_out);
		List<Coins> coins = redisUtilsService.queryCoins();
		String lockKey = "autoOrder";
		boolean lock = redisUtilsService.lock(lockKey);
		long start = System.currentTimeMillis();
		if (lock) {
			try {
				for (Coins c : coins) {
					String allKeyMoeny = "conmoney_*_" + c.getName();
					Set<String> keySetAllTotal = redisUtilsService.getKeys(allKeyMoeny);
					// 1??????????????? ????????????
					SymbolDto dto = huobiUtils.getSymbolDto(c.getName());
					//????????????
					for (String m : keySetAllTotal) {
						String str = redisUtilsService.getKey(m);
						if (!StringUtils.isEmpty(str)) {
							JSONObject moneyMap = JSONObject.parseObject(str);
							if (moneyMap == null) {
								moneyMap = new JSONObject();
							}
							Integer cid = Integer.parseInt(m.split("_")[2]);
							if (dto != null) {
									autoOrderprice(moneyMap, cid, contractAutoOut, c.getName(), m, dto);
							}
						}
					}
					//????????????
//					String allKey = "zc_*_" + c.getName() + "_*";
//					Set<String> zcOrderKeys = redisUtilsService.getKeys(allKey);
//					for (String z : zcOrderKeys) {
//						String str = redisUtilsService.getKey(z);
//						if (!StringUtils.isEmpty(str)) {
//							CZcOrder cZcOrder = JSONObject.parseObject(str, CZcOrder.class);
//							BigDecimal usdtPrice = dto.getUsdtPrice();
//							cZcOrder.setStopprice(usdtPrice);
//							BigDecimal reward = BigDecimal.ZERO;//?????????=??????+??????
//							//????????? ?????????????????????????????????????????? ????????????????????????????????????????????? ????????? ?????????
//							BigDecimal real_reward = FunctionUtils.mul(cZcOrder.getZcscale(), cZcOrder.getCapital(), 6);
//							BigDecimal scope = BigDecimal.ZERO;
//
//							if (FunctionUtils.isEquals(1, cZcOrder.getType())) {
//								//??????????????? ????????????
//								//????????????
//								scope = FunctionUtils.sub(usdtPrice, cZcOrder.getBuyprice(), 6);
//							} else if (FunctionUtils.isEquals(2, cZcOrder.getType())) {
//
//								//??????????????? ????????????
//								//????????????
//								scope = FunctionUtils.sub(cZcOrder.getBuyprice(), usdtPrice, 6);
//							}
//							//??????/????????????????????????
//							BigDecimal scale = FunctionUtils.div(scope, cZcOrder.getZcprice(), 6);
//							if (scale.compareTo(BigDecimal.ONE) >= 0) {
//								scale = BigDecimal.ONE;
//								//???????????????????????????????????????
//								apiTradeService.handleZcClose(cZcOrder.getCid(), dto, cZcOrder, true);
//								continue;
//							}
//							//?????????????????? ?????????????????????
//							BigDecimal scope_reward = FunctionUtils.mul(scale, real_reward, 6);
//							reward = FunctionUtils.add(scope_reward, reward, 6);
//							if (reward.compareTo(BigDecimal.ZERO) <= 0) {
//								BigDecimal zreward = FunctionUtils.sub(BigDecimal.ZERO, reward, 6);//????????????????????????????????????
//								boolean flag = false;
//								if (cZcOrder.getStopfail() != null && cZcOrder.getStopfail().compareTo(BigDecimal.ZERO) != 0) {
//									if ((dto.getUsdtPrice().compareTo(cZcOrder.getStopfail()) <= 0 && FunctionUtils.isEquals(1, cZcOrder.getType()))
//											|| (dto.getUsdtPrice().compareTo(cZcOrder.getStopfail()) >= 0 && FunctionUtils.isEquals(2, cZcOrder.getType()))) {
//										flag = true;
//									}
//								}
//								if (zreward.compareTo(cZcOrder.getCapital()) >= 0 || flag) {
//									//???????????????????????????????????????
//									apiTradeService.handleZcClose(cZcOrder.getCid(), dto, cZcOrder, true);
//									continue;
//								}
//							} else {
//								//????????????????????????
//								if (cZcOrder.getStopwin() != null && cZcOrder.getStopwin().compareTo(BigDecimal.ZERO) != 0) {
//									//??????
//									if ((FunctionUtils.isEquals(1, cZcOrder.getType()) && dto.getUsdtPrice().compareTo(cZcOrder.getStopwin()) >= 0)
//											|| (FunctionUtils.isEquals(2, cZcOrder.getType()) && dto.getUsdtPrice().compareTo(cZcOrder.getStopwin()) <= 0)) {
//										{
//											apiTradeService.handleZcClose(cZcOrder.getCid(), dto, cZcOrder, true);
//											continue;
//										}
//									}
//								}
//							}
//							cZcOrder.setReward(reward);
//							redisUtilsService.setKey(z, JSONObject.toJSONString(cZcOrder));
//						}
//					}
				}
			} finally {
				long end = System.currentTimeMillis();
				redisUtilsService.deleteKey(lockKey);
				long time_consuming = end - start;
				//???????????? 5000ms--6000ms
				if(time_consuming > 7000L){
					logger.warn("WalletService.autoOrder????????????????????????:{}", time_consuming);
				}
//				}
			}
		}
	}

	/**
	 * ??????????????????????????????
	 * @param moneyMap ?????????????????? ?????? ??????
	 * @param cid ??????id
	 * @param contractAutoOut ???????????????
	 * @param coin ????????????BTC/USDT
	 * @param m ???????????????
	 */
	public List<CContractOrder> autoOrderprice(JSONObject moneyMap,Integer cid,BigDecimal contractAutoOut,String coin,String m,SymbolDto dto) {
		// ????????????
		BigDecimal totalmoney = BigDecimal.ZERO;
		// ?????????
		BigDecimal totalrates = BigDecimal.ZERO;
		// ???redis?????????
		String banlance_key = redisUtilsService.getKey(cid + "_banlance");
		BigDecimal banlance=BigDecimal.ZERO;
		if(!StringUtils.isEmpty(banlance_key)) {
			banlance = new BigDecimal(banlance_key);
		}else {
			CWallet cWallet=cWalletMapper.getByCid(cid);
			if(cWallet!=null) {
				banlance=cWallet.getBalance();
			}
		}

		// ?????????????????? =??????+???????????????70% +?????????
		BigDecimal totalval = banlance;
		// ????????????????????????????????? ??????????????????
		String allKey = "order_" + cid + "_" +coin + "_*";
		Set<String> keySetAll = redisUtilsService.getKeys(allKey);
		List<CContractOrder> contractOrders = new ArrayList<>();
		List<CContractOrder> failContractOrders = new ArrayList<>();
		List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();
		for (String k : keySetAll) {
			try {
				String string = redisUtilsService.getKey(k);
				if (!StringUtils.isEmpty(string)) {
					CContractOrder cContractOrder = JSONObject.parseObject(string, CContractOrder.class);
					if (cContractOrder == null) {
						continue;
					}
					BigDecimal usdtPrice = dto.getUsdtPrice();// ????????? usdt
					cContractOrder.setStopprice(usdtPrice);
					BigDecimal old = cContractOrder.getBuyprice();
					BigDecimal now = cContractOrder.getStopprice();
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("usdtprice",usdtPrice);
					// ?????????????????????
					if (cContractOrder.getStopwin() != null
							&& cContractOrder.getStopwin().compareTo(BigDecimal.ZERO) > 0) {
						if (FunctionUtils.isEquals(1, cContractOrder.getType())) {
							// ???????????????
							if (cContractOrder.getStopwin().compareTo(now) <= 0) {
								map.put("ordercode", cContractOrder.getOrdercode());
								orderList.add(map);
                                cContractOrder.setOuttype(2);
                                cContractOrder.setStoptime(DateUtil.currentDate());
                                cContractOrder.setStatus(2);
							}
						} else {
							if (cContractOrder.getStopwin().compareTo(now) >= 0) {
								map.put("ordercode", cContractOrder.getOrdercode());
								orderList.add(map);
                                cContractOrder.setOuttype(2);
                                cContractOrder.setStoptime(DateUtil.currentDate());
                                cContractOrder.setStatus(2);
							}
						}
					}
					// ?????????????????????
					if (cContractOrder.getStopdonat() != null
							&& cContractOrder.getStopdonat().compareTo(BigDecimal.ZERO) > 0) {
						if (FunctionUtils.isEquals(1, cContractOrder.getType())) {
							// ???????????????
							if (cContractOrder.getStopdonat().compareTo(now) >= 0) {
								map.put("ordercode", cContractOrder.getOrdercode());
								orderList.add(map);
                                cContractOrder.setOuttype(2);
                                cContractOrder.setStoptime(DateUtil.currentDate());
                                cContractOrder.setStatus(2);
							}
						} else {
							if (cContractOrder.getStopdonat().compareTo(now) <= 0) {
								map.put("ordercode", cContractOrder.getOrdercode());
								orderList.add(map);
                                cContractOrder.setOuttype(2);
                                cContractOrder.setStoptime(DateUtil.currentDate());
                                cContractOrder.setStatus(2);
							}
						}
					}
					if (FunctionUtils.isEquals(1, cContractOrder.getType())) {
						// ?????? ???????????????????????????
						BigDecimal rates = FunctionUtils.sub(now, old, 4);
						rates = FunctionUtils.mul(rates, cContractOrder.getCoinnum(), 4);
//						rates = FunctionUtils.add(rates, cContractOrder.getRates(), 4);
						cContractOrder.setRates(rates);
					} else {
						// ?????? ????????????
						BigDecimal rates = FunctionUtils.sub(old, now, 4);
						rates = FunctionUtils.mul(rates, cContractOrder.getCoinnum(), 4);
//						rates = FunctionUtils.add(rates, cContractOrder.getRates(), 4);
						cContractOrder.setRates(rates);
					}
					totalmoney = FunctionUtils.add(totalmoney, cContractOrder.getRealmoney(), 4);
					totalrates = FunctionUtils.add(totalrates, cContractOrder.getRates(), 4);



					redisUtilsService.setKey(k, JSONObject.toJSONString(cContractOrder));
					// ????????????
					if (cContractOrder.getRates().compareTo(BigDecimal.ZERO) >= 0) {
						contractOrders.add(cContractOrder);
					} else {
						failContractOrders.add(cContractOrder);
						//?????? ????????????
						BigDecimal halfBalance = cContractOrder.getRealmoney().multiply(new BigDecimal("0.5")).add(banlance);
						if(cContractOrder.getRates().negate().compareTo(halfBalance) >= 0){
							apiTradeService.handleCloseOrder(cContractOrder, cContractOrder.getStopprice(), "??????", true);
							logger.warn("autoOrderprice_force_sell:"
									+ "  memberId:" + cContractOrder.getCid()
									+ "  ordercode:" + cContractOrder.getOrdercode()
									+ "  banlance:" + banlance
									+ "  halfBalance:" + halfBalance.toPlainString()
									+ "  rate:" + cContractOrder.getRates());
						}
					}
				}
			} catch (Exception e) {
				System.out.println("????????????");
			}
		}
		if (!CollectionUtils.isEmpty(failContractOrders)) {
			contractOrders.addAll(failContractOrders);
		}
		// ?????????????????? =??????+???????????????70% +?????????
		totalval = FunctionUtils.add(totalval, FunctionUtils.mul(totalmoney, contractAutoOut, 4), 4);
		totalval = FunctionUtils.add(totalval, totalrates, 4);
		CCustomer cCustomer = cCustomerMapper.selectByPrimaryKey(cid);
		//??????????????????
		try{
			qcLoss(totalmoney,totalrates,cid,cCustomer.getPhone(),banlance);
		}catch (Exception e){
			logger.warn("qcLoss(totalmoney,totalrates,cid,cCustomer.getPhone(),banlance);cid: " +  cid + "  \n" + e.getStackTrace());
		}
		boolean auto = false;
		if (contractOrders != null && contractOrders.size() > 0) {
			if (totalval.compareTo(BigDecimal.ZERO) <= 0) {
				// ???????????????
//				SmsSendPool.getInstance().send(new SmsSend(cCustomer.getPhone(), LingKaiSmsEnums.LK_SMS_FULL_BURST.getType(), LingKaiSmsEnums.LK_SMS_FULL_BURST.getCode()));
//				// ????????????
//				// 1.???????????????????????????
//				redisUtilsService.deleteKey(keySetAll);
//				if (redisUtilsService.getKey("QC_LOSS_" + cid + "_0.3") != null) {
//					redisUtilsService.deleteKey("QC_LOSS_" + cid + "_0.3");
//				}
//				if (redisUtilsService.getKey("QC_LOSS_" + cid + "_0.5") != null) {
//					redisUtilsService.deleteKey("QC_LOSS_" + cid + "_0.5");
//				}
			}
		}
		// ??????????????????????????????????????? ????????????????????????
		if (!auto) {
			if (orderList != null && orderList.size() > 0) {
				// ???????????????????????? ????????????
				apiTradeService.handleCloseMore(orderList);
			}
		}
		// ????????????????????????
		moneyMap.put("totalmoney", totalmoney);
		moneyMap.put("totalrates", totalrates);
		moneyMap.put("totalval", totalval);
		BigDecimal scale=FunctionUtils.div(totalval, totalmoney, 6);
		scale=FunctionUtils.mul(scale, new BigDecimal(100), 4);
		moneyMap.put("scale", scale);
		redisUtilsService.setKey(m, moneyMap.toJSONString());
		return contractOrders;
	}

	/**
	 * ??????????????????30%????????????
	 * @param totalMoney ????????????
	 * @param totalRates ?????????
	 * @param cid
	 * @param phone		???????????????
	 * @param totalVal   ?????????
	 */
	private void qcLoss(BigDecimal totalMoney, BigDecimal totalRates, Integer cid, String phone, BigDecimal totalVal) {
		try {
			if ((totalMoney.compareTo(BigDecimal.ZERO) != 0) && totalRates.compareTo(BigDecimal.ZERO) < 0) {
				BigDecimal syBalance = totalRates.add(totalVal);
				if (syBalance.compareTo(BigDecimal.ZERO) >= 0) {
					return;
				}
				BigDecimal div = FunctionUtils.div(syBalance.abs(), totalMoney, 1);
				double loss_num = 0;
				if (div.compareTo(new BigDecimal("0.3")) >= 0) {
					loss_num = 0.3;
				} else {
					return;
				}
				String qc_loss_key = "QC_LOSS_" + cid + "_" + loss_num;
				String key = redisUtilsService.getKey(qc_loss_key);
				if (StringUtils.isEmpty(key) && div.compareTo(BigDecimal.ONE) < 0) {
					redisUtilsService.setKey(qc_loss_key, loss_num + "", 24 * 60 * 60);
					String format = String.format(LingKaiSmsEnums.LK_SMS_FULL_LOSS.getCode(), (int) (loss_num * 100) + "%");
					SmsSendPool.getInstance().send(new SmsSend(phone, LingKaiSmsEnums.LK_SMS_FULL_LOSS.getType(), format));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * ????????????
	 */
	public void autoEntrustOrder() {
		// ??????????????????????????????
		String allKey = "entrust_*";
		Set<String> keySetAll = redisUtilsService.getKeys(allKey);
		for(String k:keySetAll) {
			String string = redisUtilsService.getKey(k);
			if (!StringUtils.isEmpty(string)) {
				CEntrustOrder cEntrustOrder = JSONObject.parseObject(string, CEntrustOrder.class);
				if(cEntrustOrder!=null) {
					String coin=cEntrustOrder.getCoin();
					//1??????????????? ????????????
					SymbolDto symDto= huobiUtils.getSymbolDto(coin.replace("_", "/").toUpperCase());
					if(symDto==null) {
						continue;
					}
					BigDecimal usdtPrice =symDto.getUsdtPrice();// ????????? usdt
					if(FunctionUtils.isEquals(cEntrustOrder.getType(), 1)) {
						//???????????????
						if(usdtPrice.compareTo(cEntrustOrder.getPrice())<=0) {
							//???????????????????????? ????????? ?????????
							handleEntrust(cEntrustOrder);
							//??????key
							redisUtilsService.deleteKey(k);
						}
					}else {
						//???????????????
						if(usdtPrice.compareTo(cEntrustOrder.getPrice())>=0) {
							//???????????????????????? ????????? ?????????
							handleEntrust(cEntrustOrder);
							//??????key
							redisUtilsService.deleteKey(k);
						}
					}
				}
			}
		}
	}

	/**
	 * ????????????
	 * @param cEntrustOrder
	 */
	public void handleEntrust(CEntrustOrder cEntrustOrder) {
		cEntrustOrder.setStatus(StaticUtils.entrust_success);
		cEntrustOrder.setSuccesstime(DateUtil.currentDate());
		cEntrustOrderMapper.updateByPrimaryKeySelective(cEntrustOrder);
		//????????????
		apiTradeService.handleContractOrderServer(cEntrustOrder.getCid(), cEntrustOrder.getType(),cEntrustOrder.getCoinnum(), cEntrustOrder.getGearing()
				, cEntrustOrder.getCoin(),cEntrustOrder.getPrice(), cEntrustOrder.getTax(), true, cEntrustOrder.getRunTime());
	}

	/**
	 * eth????????????
	 */
	public void autoECR20UsdtRecharge() {
		CWalletExample example=new CWalletExample();
		example.setOrderByClause("cid desc");
		List<CWallet> list=cWalletMapper.selectByExample(example);
		ethService.ethRecharge(list);
	}

	/**
	 * ????????????
	 */
	public void autoECR20GJ() {
		List<CposDto> list=txLogsMapper.queryGj();
		BigInteger price=ECR20Utils.getGasPrice();
		System.out.println("ECR20??????PRICE:"+price);

		BigInteger limit=Convert.toWei(ECR20Utils.GAS_LIMIT, Convert.Unit.WEI).toBigInteger();
		BigDecimal minfree=ECR20Utils.getMinerFee(limit);
		System.out.println("minfree:"+minfree);
		for(CposDto l:list) {
			try {
				CWallet cWallet=cWalletMapper.getByLabel(l.getToaddr());
				if(cWallet!=null) {
					BigDecimal balance=ECR20Utils.getBlanceOf(l.getToaddr());
					BigDecimal tax=minfree;
					if(balance.compareTo(tax)<0) {
						tax=FunctionUtils.sub(tax, balance, 6);
						BigInteger eth=Convert.toWei(tax, Convert.Unit.ETHER).toBigInteger();//??????ETH??????
						//??????????????????
						for(ETHEnums e:ETHEnums.values()) {
							Credentials credentials = ECR20Utils.loadWallet(e.getPath(),e.getPassword());
							String hashcode=ECR20Utils.transto(credentials, l.getToaddr(),eth,price,limit);
							if(!StringUtils.isEmpty(hashcode)) {
								System.out.println("????????????"+l.getToaddr()+"???ETH:"+eth+"  ???????????????"+hashcode);
								break;
							}
						}
					}else {
						//??????????????????
						Credentials credentials = ECR20Utils.loadWallet(cWallet.getPath(), cWallet.getPassword());
//						System.out.println(new BigInteger(String.valueOf(l.getCpos())));
//						BigDecimal usdt=ECR20Utils.toDecimal(6,new BigInteger(String.valueOf(l.getCpos())));
						String code = ECR20Utils.ecr20Trans(credentials, ECR20Utils.usdt_contractAddress,"0x4fd532dfa0ff7689e8613599b37694846e760d7c",l.getCpos(),minfree);
						if(!StringUtils.isEmpty(code)) {
							l.setTranscode(code);
							txLogsMapper.updateStatus(l);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

//	/**
//	 * ????????????
//	 */
//	public void autoCancel() {
//		// ?????????????????????
//		String allKey = "timeout_*";
//		Set<String> keySetAll = redisUtilsService.getKeys(allKey);
//		for (String k : keySetAll) {
//			try {
//				String string = redisUtilsService.getKey(k);
//				if (!StringUtils.isEmpty(string)) {
//					List<CContractOrder> cContractOrders = JSONArray.parseArray(string, CContractOrder.class);
//					if (cContractOrders != null && cContractOrders.size() > 0) {
//						apiTradeService.orderTimeout(cContractOrders,k);
//					}
//				}
//			} catch (Exception e) {
//				System.out.println("????????????");
//			}
//		}
//	}

	public static void main(String[] args) {
		BigDecimal usdt=ECR20Utils.toDecimal(6,new BigInteger(String.valueOf(495000000)));
		System.out.println(usdt);
	}
}
