package com.contract.app.task;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alibaba.fastjson.JSONObject;
import com.contract.app.socket.WebSocketClient;
import com.contract.common.FunctionUtils;
import com.contract.common.StaticUtils;
import com.contract.common.mail.SmsSendPool;
import com.contract.common.sms.SmsSend;
import com.contract.dao.CContractOrderMapper;
import com.contract.dao.CCustomerMapper;
import com.contract.dao.CWalletMapper;
import com.contract.entity.*;
import com.contract.enums.HandleTypeEnums;
import com.contract.enums.LingKaiSmsEnums;
import com.contract.enums.SysParamsEnums;
import com.contract.exception.ThrowJsonException;
import com.contract.service.BipbService;
import com.contract.service.api.ApiTradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.contract.common.DateUtil;
import com.contract.dao.CoinsMapper;
import com.contract.service.redis.RedisUtilsService;
import com.contract.service.wallet.WalletService;
import com.contract.service.wallet.btc.UsdtPrice;
import com.contract.service.wallet.huobi.HuoBiUtils;
import com.huobi.response.Kline;

import static com.contract.app.socket.WebSocketClientHandler.RECONN_FLAG;

@Component
public class WalletTask {
    private Logger logger = LoggerFactory.getLogger(WalletTask.class);

    @Autowired
    private WalletService walletService;
    @Autowired
    private RedisUtilsService redisUtilsService;
    @Autowired
    private CoinsMapper coinsMapper;

    @Autowired
    private ApiTradeService apiTradeService;

    @Autowired
    private CWalletMapper walletMapper;

    @Autowired
    private BipbService bipbService;

    @Autowired
    private CCustomerMapper cCustomerMapper;

    @Autowired
    private CContractOrderMapper cContractOrderMapper;

    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(4);

    /**
     * CRON表达式    含义
     * "0/5 * *  * * ? "		每5秒执行一次
     * "0 0 12 * * ?"    		每天中午十二点触发
     * "0 15 10 ? * *"    		每天早上10：15触发
     * "0 15 10 * * ?"    		每天早上10：15触发
     * "0 15 10 * * ? *"    	每天早上10：15触发
     * "0 15 10 * * ? 2005"    	2005年的每天早上10：15触发
     * "0 * 14 * * ?"    		每天从下午2点开始到2点59分每分钟一次触发
     * "0 0/5 14 * * ?"    		每天从下午2点开始到2：55分结束每5分钟一次触发
     * "0 0/5 14,18 * * ?"    	每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发
     * "0 0-5 14 * * ?"    		每天14:00至14:05每分钟一次触发
     * "0 10,44 14 ? 3 WED"    	三月的每周三的14：10和14：44触发
     * "0 15 10 ? * MON-FRI"    每个周一、周二、周三、周四、周五的10：15触发
     */


//	@Scheduled(cron="0 0/1 * * * ?")
//	public void autoHall() {
//		walletService.createWallet();
//	}

    /**
     * 自动更新usdt价格 30分钟更新一次
     */
    @Scheduled(cron = "0 0 0/4 * * ?")
    public void autoUsdtPrice() {
        // 1.获取到usdt等值人民币价格
        BigDecimal usdtTocny = UsdtPrice.getUsdtToCny();
        if (usdtTocny != null) {
            redisUtilsService.setKey("usdt_cny", usdtTocny.toPlainString());
        }
    }

    /**
     * 合约重连
     */
    @Scheduled(cron = "0 */15 * * * ?")
    public void contractReconnection() {
        if (RECONN_FLAG) {
            logger.warn("定时任务重连合约ws--{}", System.currentTimeMillis());
            List<Coins> coins = redisUtilsService.queryCoins();
            WebSocketClient.HBSocket(coins);
            RECONN_FLAG = false;
        }
    }

    /**
     * 自动更新 开盘价 每日更新一次
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void autoKlin() {
        try {
            CoinsExample coinsExample = new CoinsExample();
            coinsExample.setOrderByClause("sort asc");
            List<Coins> coins = coinsMapper.selectByExample(coinsExample);
            for (Coins s : coins) {
                String symbol = s.getSymbol().replace("_", "");
                List<Kline> list = HuoBiUtils.getInstance().queryKline(symbol, "1day", "1");
                if (list != null && list.size() > 0) {
                    Kline kline = list.get(0);
                    s.setOpenval(new BigDecimal(kline.getOpen()));
                    s.setUpdatetime(DateUtil.currentDate());
                    coinsMapper.updateByPrimaryKeySelective(s);
                }
                Thread.sleep(1000);
            }
            String coinarr = JSONArray.toJSONString(coins);
            redisUtilsService.setKey("coins_key", coinarr);
        } catch (Exception e) {

        }
    }

    /**
     * 自动爆仓 处理移除数据
     */
	@Scheduled(cron="0/3 * * * * ?")
    public void autoContractOrder() {
        walletService.autoOrder();
    }

    /**
     * 自动爆仓 处理移除数据
     */
    @Scheduled(cron = "0/3 * * * * ?")
    public void autoAll() {
        String contract_auto_out = redisUtilsService.getKey(SysParamsEnums.contract_auto_out.getKey());
        BigDecimal contractAutoOut = new BigDecimal(contract_auto_out);
        String allKey = "order_" + "*" + "_" + "*" + "_*";
        Set<String> ids = new HashSet<String>();
        Set<String> os = redisUtilsService.getKeys(allKey);
        for (String s : os) {
            String[] split = s.split("_");
            ids.add(split[2]);
        }
        String KeyPre = "autoAll_";
        for (String id : ids) {
            String lockKey = KeyPre + id;
            boolean lock = redisUtilsService.lock(lockKey, 5 * 60 * 1000);
            if (lock) {
                fixedThreadPool.execute(new AutoAllRunable(id, contractAutoOut, lockKey));
            }
        }

    }

    class AutoAllRunable implements Runnable {
        String id;
        BigDecimal contractAutoOut;
        String lockKey;

        public AutoAllRunable(String id, BigDecimal contractAutoOut, String lockKey) {
            this.id = id;
            this.contractAutoOut = contractAutoOut;
            this.lockKey = lockKey;
        }

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            try {
                List<CContractOrder> contractOrders = new ArrayList<>();
                CCustomer cCustomer = cCustomerMapper.selectByPrimaryKey(Integer.valueOf(id));
                String allo = "order_" + id + "_" + "*" + "_*";
                String allKeyMoeny = "conmoney_" + id + "_*";
                Set<String> ll = redisUtilsService.getKeys(allo);
                Set<String> keySetAllTotal = redisUtilsService.getKeys(allKeyMoeny);
                BigDecimal banlance = new BigDecimal(redisUtilsService.getKey(id + "_banlance"));
                // 总保证金
                BigDecimal totalmoney = BigDecimal.ZERO;
                // 总盈亏
                BigDecimal totalrates = BigDecimal.ZERO;
                //日志用，排查问题的可以删除这两个变量
                StringBuilder sb = new StringBuilder();
                String usdtPriceStr = "";
                for (String or : ll) {
                    String ors = redisUtilsService.getKey(or);
                    sb.append(ors);
                    CContractOrder order = JSONObject.parseObject(new String(ors), CContractOrder.class);

                    //检查一下订单状态是否为2（已完成）
                    if (FunctionUtils.isEquals(2, order.getStatus())) {
                        if (deleteRedisKey(order.getOrdercode(), or)) {
                            continue;
                        }
                        ;
                    }

                    String[] coin = order.getCoin().split("_");
                    //改动步骤
                    //1获取当前订单币种的行情
                    String mkey = "maketdetail_" + coin[0];//当前订单币种
                    String market = redisUtilsService.getKey(mkey);//当前币种行情
                    //				System.out.println("当前币种："+market);
                    JSONObject moneyMap = JSONObject.parseObject(market);
                    //2通过当前币种的实时价减去持仓价乘以持仓量 根据开多开空算出盈利，计算总保证金 进行对比爆仓比例-----爆仓
                    BigDecimal usdtPrice = moneyMap.getBigDecimal("usdtPrice");//当前币种实时价
                    //排查问题日志用
                    usdtPriceStr = usdtPrice.toPlainString();
                    BigDecimal buyprice = order.getBuyprice();//持仓价
                    if (order.getType() == 1) {//1开多 2开空
                        BigDecimal kd_profit = usdtPrice.subtract(buyprice).multiply(order.getCoinnum());//开多盈亏
                        totalrates = totalrates.add(kd_profit);
                    } else {
                        BigDecimal kk_profit = buyprice.subtract(usdtPrice).multiply(order.getCoinnum());//开空盈亏
                        totalrates = totalrates.add(kk_profit);
                    }
                    totalmoney = totalmoney.add(order.getRealmoney());
                }
                banlance = FunctionUtils.add(totalmoney, banlance, 4);
                banlance = FunctionUtils.mul(banlance, contractAutoOut, 4);
                totalmoney = FunctionUtils.add(banlance, totalrates, 4);
//			System.out.println(id+":会员余额："+banlance);
//			System.out.println("盈亏:"+totalrates);
                if (totalmoney.compareTo(BigDecimal.ZERO) <= 0) {
                    //此日志为解决未达到爆仓条件也会爆仓的问题而打印，后面可以去掉 by daily
                    logger.warn("autoAll:" + ll.toString()
                            + "  contractAutoOut:" + contractAutoOut.toPlainString()
                            + "  usdtPriceStr:" + usdtPriceStr
                            + "  start banlance:" + redisUtilsService.getKey(id + "_banlance")
                            + "  totalrates:" + totalrates.toPlainString()
                            + "  totalmoney:" + totalmoney.toPlainString()
                            + "  last banlance:" + banlance.toPlainString());
                    logger.warn(sb.toString());
                    //			if (1==1) {
                    // 设置过期的
                    String timeout = JSONArray.toJSONString(contractOrders);
                    for (String coi : ll) {
                        String outKey = "timeout_" + id + "_" + coi;
                        redisUtilsService.setKey(outKey, timeout);
                        String string = redisUtilsService.getKey(coi);
                        CContractOrder cContractOrder = JSONObject.parseObject(string, CContractOrder.class);
                        cContractOrder.setOuttype(2);
                        cContractOrder.setStoptime(DateUtil.currentDate());
                        cContractOrder.setStatus(2);
                        contractOrders.add(cContractOrder);
                        apiTradeService.handleorderTimeoutAutoAll(contractOrders, outKey);

                        bipbService.handleCUsdtDetail(cContractOrder.getCid(), HandleTypeEnums.orderout.getId(), 0, cContractOrder.getOrdercode(), cContractOrder.getStopprice(), "自动平仓", cContractOrder.getCid());
                        //					System.out.println("===========订单详细=====");
                        // 触发爆仓
                        // 1.删除所有对应持仓的
                        System.out.println(coi + "爆仓" + coi);
                        redisUtilsService.deleteKey(coi);
                    }
                    redisUtilsService.setKey(id + "_banlance", "0");
                    for (String total : keySetAllTotal) {
                        System.out.println(total);
                        String str = redisUtilsService.getKey(total);
                        JSONObject moneyMap = JSONObject.parseObject(str);
                        // 更新当前持仓总价
                        moneyMap.put("totalmoney", 0);
                        moneyMap.put("totalrates", 0);
                        moneyMap.put("totalval", 0);
                        BigDecimal scale = FunctionUtils.div(BigDecimal.ZERO, totalmoney, 6);
                        scale = FunctionUtils.mul(scale, new BigDecimal(100), 4);
                        moneyMap.put("scale", scale);
                        redisUtilsService.setKey(total, moneyMap.toJSONString());

                    }
                    CWallet wallet = walletMapper.getByCid(Integer.parseInt(id));
                    //				System.out.println("===========自动爆仓余额清零========操作金额:"+wallet.getBalance()+"======");
                    //以下代码强制余额清零
                    bipbService.handleCUsdtDetail(wallet.getCid(), HandleTypeEnums.orderout.getId(), 0, null, wallet.getBalance(), "自动平仓/自动爆仓", wallet.getCid());
                    // 设置过期的
                    SmsSendPool.getInstance().send(new SmsSend(cCustomer.getPhone(), LingKaiSmsEnums.LK_SMS_FULL_BURST.getType(), LingKaiSmsEnums.LK_SMS_FULL_BURST.getCode()));
                    // 触发爆仓
                    if (redisUtilsService.getKey("QC_LOSS_" + id + "_0.3") != null) {
                        redisUtilsService.deleteKey("QC_LOSS_" + id + "_0.3");
                    }
                    if (redisUtilsService.getKey("QC_LOSS_" + id + "_0.5") != null) {
                        redisUtilsService.deleteKey("QC_LOSS_" + id + "_0.5");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                long end = System.currentTimeMillis();
                redisUtilsService.deleteKey(lockKey);
                long time_consuming = end - start;
                if (time_consuming > 3000L) {
                    logger.warn("WalletService.autoAll lockey:{}自动爆仓执行时长:{}", lockKey, time_consuming);
                }
            }
        }
    }

    /**
     * 删除已经完成但是还存在Redis的订单
     *
     * @param ordercode
     * @param orderkey
     * @return 删除了rediskey就返回 true  否则 flase
     */
    private boolean deleteRedisKey(String ordercode, String orderkey) {
        CContractOrder cContractOrder = cContractOrderMapper.getByOrdercode(ordercode);
        if (cContractOrder != null && FunctionUtils.isEquals(2, cContractOrder.getStatus())) {
            logger.warn("autoAll completed QC order: " + cContractOrder.getOrdercode() + " deleted from redis!");
            redisUtilsService.deleteKey(orderkey);
            return true;
        }
        return false;
    }

    /**
     * 自动委托
     */
    @Scheduled(cron = "0/10 * * * * ?")
    public void autoEntrustOrder() {
        walletService.autoEntrustOrder();
    }

    /**
     * 自动平仓
     */
//	@Scheduled(cron="0/20 * * * * ?")
//	public void autoCancel() {
//		walletService.autoCancel();
//	}

    /**
     * 持仓到期，自动平仓
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void autoCycle() {
        String allKey = "order_" + "*" + "_" + "*" + "_*";
        Set<String> ids = new HashSet<String>();
        Set<String> os = redisUtilsService.getKeys(allKey);
        for (String s : os) {
            String[] split = s.split("_");
            ids.add(split[2]);
        }
        String KeyPre = "autoAll_";
        for (String id : ids) {
            String lockKey = KeyPre + id;
            fixedThreadPool.execute(new AutoCycleRunable(id, lockKey));
        }

    }

    class AutoCycleRunable implements Runnable {
        String id;
        String lockKey;

        public AutoCycleRunable(String id, String lockKey) {
            this.id = id;
            this.lockKey = lockKey;
        }

        @Override
        public void run() {
            try {
                System.out.println("执行成功 ---->进来了");
                String allo = "order_" + id + "_" + "*" + "_*";
                Set<String> ll = redisUtilsService.getKeys(allo);
                Date now_time = new Date();
                for (String or : ll) {
                    String ors = redisUtilsService.getKey(or);
                    if(ors == null||"".equals(ors)){
                        continue;
                    }
                    CContractOrder order = JSONObject.parseObject(new String(ors), CContractOrder.class);
                    if(order.getRunTime()==null || order.getRunTime() == 1000){
                        continue;
                    }
                    //检查一下订单状态是否为2（已完成）
                    if (!FunctionUtils.isEquals(StaticUtils.status_no, order.getSettleflag())) {
                        if (deleteRedisKey(order.getOrdercode(), or)) {
                            continue;
                        }
                    }
                    long time = DateUtil.getHMS(now_time, order.getCreatetime()) / 1000;
                    if (time < order.getRunTime().longValue()) {
                        continue;
                    }
                    String[] coin = order.getCoin().split("_");
                    //获取当前订单币种的行情
                    String mkey = "maketdetail_" + coin[0];//当前订单币种
                    String market = redisUtilsService.getKey(mkey);//当前币种行情
                    JSONObject moneyMap = JSONObject.parseObject(market);
                    BigDecimal usdtPrice = moneyMap.getBigDecimal("usdtPrice");//当前币种实时价
                            apiTradeService.handleCloseOrder(order, usdtPrice, "到期自动平仓", true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
