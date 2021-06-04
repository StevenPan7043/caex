package com.contract.service.api;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.contract.common.*;
import com.contract.common.mail.SmsSendPool;
import com.contract.common.sms.SmsSend;
import com.contract.dao.*;
import com.contract.entity.*;
import com.contract.enums.*;
import com.contract.service.HuobiUtils;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.contract.dto.CustomerDto;
import com.contract.dto.SymbolDto;
import com.contract.exception.ThrowJsonException;
import com.contract.service.BipbService;
import com.contract.service.PattenService;
import com.contract.service.UtilsService;
import com.contract.service.redis.RedisUtilsService;
import com.github.pagehelper.PageHelper;

@Service
public class ApiTradeService {

    Logger logger = LoggerFactory.getLogger(ApiTradeService.class);

    {
        logger.warn("ApiTradeService 查询全仓止盈止损日志用。");
    }

    @Autowired
    private CContractOrderMapper cContractOrderMapper;
    @Autowired
    private UtilsService utilsService;
    @Autowired
    private CCustomerMapper cCustomerMapper;
    @Autowired
    private RedisUtilsService redisUtilsService;
    @Autowired
    private BipbService bipbService;
    @Autowired
    private CoinsMapper coinsMapper;
    @Autowired
    private CWalletMapper cWalletMapper;
    @Autowired
    private CEntrustOrderMapper cEntrustOrderMapper;
    @Autowired
    private PattenService pattenService;
    @Autowired
    private CZcOrderMapper cZcOrderMapper;
    @Autowired
    private HuobiUtils huobiUtils;
    @Autowired
    private GdBuyRecordMapper gdBuyRecordMapper;
    @Autowired
    private GdUserBonusMapper gdUserBonusMapper;
    @Autowired
    private GdDetailMapper gdDetailMapper;

    /**
     * 查询订单
     *
     * @param token
     * @return
     */
    public RestResponse queryContractOrder(String token, CContractOrder order) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        PageHelper.startPage(order.getPage(), order.getRows());
        order.setCid(customerDto.getId());
        List<CContractOrder> list = cContractOrderMapper.queryOrderList(order);
        return GetRest.getSuccess("成功", list);
    }

    /**
     * 处理合约下单
     *
     * @param token
     * @param type    1开多  2开空
     * @param coinnum 持仓量
     * @param symbol  货币 BTC/USDT ETH/USDT等等
     * @return
     */
    public RestResponse handleContractOrder(String token, Integer type, BigDecimal coinnum, Integer gearing, String symbol, BigDecimal usdtPrice, Integer time) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        CCustomer cCustomer = cCustomerMapper.selectByPrimaryKey(customerDto.getId());
        if (cCustomer == null || FunctionUtils.isEquals(StaticUtils.status_no, cCustomer.getStatus())) {
            return GetRest.getFail("会员信息不存在或已冻结");
        }
//        if (FunctionUtils.isEquals(StaticUtils.identity_agent, cCustomer.getIdentity())) {
//            return GetRest.getFail("当前会员身份不可开仓");
//        }
        Integer cid = cCustomer.getId();
        //1秒级并发处理
        String key = "contract_" + cid;
        boolean lockflag_cash = redisUtilsService.setIncrSecond(key, 3);
        if (!lockflag_cash) {
            return GetRest.getFail("合约交易频繁操作,3s后重试");
        }
//        //1。获取当前 货币市价
//        SymbolDto symDto = redisUtilsService.getSymbolDto(symbol.replace("_", "/").toUpperCase());
//        if (symDto == null) {
//            throw new ThrowJsonException("币种不存在");
//        }
//        BigDecimal usdtPrice = symDto.getUsdtPrice();// 实时价 usdt
        //处理合约
        handleContractOrderServer(cid, type, coinnum, gearing, symbol, usdtPrice, BigDecimal.ZERO, false, time);

        return GetRest.getSuccess("操作成功");
    }

    /**
     * @param cid     用户
     * @param type    开多 开空
     * @param coinnum 数量
     * @param gearing 杠杆
     * @param symbol
     * @param tax     手续费 委托交易需要传
     * @param entrust 是否委托
     * @param time    持仓时长 单位s
     */
    public void handleContractOrderServer(Integer cid, Integer type, BigDecimal coinnum, Integer gearing, String symbol, BigDecimal usdtPrice, BigDecimal tax, boolean entrust, Integer time) {
        String paycode = FunctionUtils.getOrderCode("D");
        //2。计算当前持仓量价值多少usdt
        BigDecimal money = FunctionUtils.mul(coinnum, usdtPrice, 4);
        //3.根据杠杆计算实际需要花费多少usdt
        BigDecimal realmoney = FunctionUtils.div(money, new BigDecimal(gearing), 4);
        CContractOrder cContractOrder = new CContractOrder();
        cContractOrder.setCid(cid);
        cContractOrder.setRealmoney(realmoney);
        cContractOrder.setGearing(gearing);
        cContractOrder.setMoney(money);
        cContractOrder.setBuyprice(usdtPrice);

        cContractOrder.setCoin(symbol);
        cContractOrder.setCoinnum(coinnum);
        cContractOrder.setCreatetime(DateUtil.currentDate());
        cContractOrder.setRunTime(time);
        cContractOrder.setStatus(1);
        cContractOrder.setSettleflag(-1);
        //设置自动爆仓价格
        String contract_auto_out = redisUtilsService.getKey(SysParamsEnums.contract_auto_out.getKey());
        BigDecimal contractAutoOut = new BigDecimal(contract_auto_out);
        BigDecimal autoMoney = FunctionUtils.mul(contractAutoOut, realmoney, 4);
        cContractOrder.setAutomoney(autoMoney);
        //计算点差
        Coins coins = coinsMapper.getBySymbol(symbol);
        BigDecimal spread = FunctionUtils.getScale(coins.getBeginspread(), coins.getStopspread());
        cContractOrder.setSpread(spread);
//		cContractOrder.setSpread(BigDecimal.ZERO);
        //点差根据保证金计算
        BigDecimal spreadMoney = FunctionUtils.mul(money, cContractOrder.getSpread(), 4);//点差价
        cContractOrder.setSpreadmoney(spreadMoney);
        cContractOrder.setType(type);
        cContractOrder.setOrdercode(paycode);
        switch (type) {
            case 1:
                if (!entrust) {
                    //计算手续费
                    String contract_down = redisUtilsService.getKey(SysParamsEnums.contract_up.getKey());
                    BigDecimal contractDown = new BigDecimal(contract_down);
                    tax = FunctionUtils.mul(money, contractDown, 4);//如果不是委托手续费自己计算
                }
                cContractOrder.setTax(tax);
                //处理开多
                cContractOrder = handleOrderUp(cContractOrder, entrust);
                break;
            case 2:
                if (!entrust) {
                    //计算手续费
                    String contract_down = redisUtilsService.getKey(SysParamsEnums.contract_down.getKey());
                    BigDecimal contractDown = new BigDecimal(contract_down);
                    tax = FunctionUtils.mul(money, contractDown, 4);//如果不是委托手续费自己计算
                }
                cContractOrder.setTax(tax);
                //处理开空
                cContractOrder = handleOrderDown(cContractOrder, entrust);
                break;
        }
        //开仓触发奖励
        CCustomer cCustomer = cCustomerMapper.selectByPrimaryKey(cid);
        pattenService.handlePatten(cCustomer, tax, "合约账户开仓推荐奖励");
        //订单放入缓存
        String orderkey = "order_" + cid + "_" + coins.getName() + "_" + cContractOrder.getOrdercode();
        redisUtilsService.setKey(orderkey, JSONObject.toJSONString(cContractOrder));

        //***保存 货币总额
        CWallet wallet = cWalletMapper.selectByPrimaryKey(cid, CoinEnums.USDT.name());
        redisUtilsService.setOutMoney(cContractOrder, wallet, cid, coins.getName(), true);
    }


    /**
     * 合约交易开多
     *
     * @param cContractOrder
     * @param entrust        是否委托 委托已经扣除过本金  只要扣除手续费
     */
    public CContractOrder handleOrderUp(CContractOrder cContractOrder, boolean entrust) {
        if (!entrust) {
            //处理金额
            bipbService.handleCUsdtDetail(cContractOrder.getCid(), HandleTypeEnums.contract.getId(), StaticUtils.pay_out, cContractOrder.getOrdercode(), cContractOrder.getRealmoney(), "合约交易-开多", cContractOrder.getCid());
        }
        //手续费直接扣除
        if (!entrust) {
            if (cContractOrder.getTax() != null && cContractOrder.getTax().compareTo(BigDecimal.ZERO) > 0) {
                bipbService.handleCUsdtDetail(cContractOrder.getCid(), HandleTypeEnums.contract_tax.getId(), StaticUtils.pay_out, cContractOrder.getOrdercode(), cContractOrder.getTax(), "合约交易-开多手续费", cContractOrder.getCid());
            }
        }
        //手续费已经扣除 就不需要预先设置手续费亏损
//		cContractOrder.setTax(tax);
//		cContractOrder.setRates(FunctionUtils.sub(BigDecimal.ZERO, tax, 4));
        cContractOrder.setRates(BigDecimal.ZERO);
        cContractOrderMapper.insertSelective(cContractOrder);
        return cContractOrder;
    }

    /**
     * 合约交易开多
     *
     * @param cContractOrder
     */
    public CContractOrder handleOrderDown(CContractOrder cContractOrder, boolean entrust) {
        if (!entrust) {
            //处理金额
            bipbService.handleCUsdtDetail(cContractOrder.getCid(), HandleTypeEnums.contract.getId(), StaticUtils.pay_out, cContractOrder.getOrdercode(), cContractOrder.getRealmoney(), "合约交易-开空", cContractOrder.getCid());
        }
        //手续费直接扣除
        if (!entrust) {
            if (cContractOrder.getTax() != null && cContractOrder.getTax().compareTo(BigDecimal.ZERO) > 0) {
                bipbService.handleCUsdtDetail(cContractOrder.getCid(), HandleTypeEnums.contract_tax.getId(), StaticUtils.pay_out, cContractOrder.getOrdercode(), cContractOrder.getTax(), "合约交易-开空手续费", cContractOrder.getCid());
            }
        }
        //手续费已经扣除 就不需要预先设置手续费亏损
//		cContractOrder.setTax(tax);
//		cContractOrder.setRates(FunctionUtils.sub(BigDecimal.ZERO, tax, 4));
        cContractOrder.setRates(BigDecimal.ZERO);
        cContractOrderMapper.insertSelective(cContractOrder);
        return cContractOrder;
    }

    /**
     * 手动平仓
     *
     * @return
     */
    public RestResponse validCloseOrder(String token, String ordercode) {
//		CustomerDto customerDto=utilsService.getCusByToken(token);
        if (StringUtils.isEmpty(ordercode)) {
            return GetRest.getFail("未找到订单号");
        }
        //1秒级并发处理
        String key = "closeorder_" + ordercode;
        boolean lockflag_cash = redisUtilsService.setIncrSecond(key, 3);
        if (!lockflag_cash) {
            return GetRest.getFail("平仓频繁操作,3s后重试");
        }
        CContractOrder cContractOrder = cContractOrderMapper.getByOrdercode(ordercode);
        if (cContractOrder == null) {
            throw new ThrowJsonException("订单不存在");
        }
//        long time = DateUtil.getHMS(new Date(), cContractOrder.getCreatetime());
//        if (time < 60 * 1000) {
//            return GetRest.getFail(ErrorInfoEnum.LANG_CLOSE_ORDER_LIMIT.getErrorCNMsg(), ErrorInfoEnum.LANG_CLOSE_ORDER_LIMIT.getErrorENMsg());
//        }
        handleCloseOrder(cContractOrder, null, "手动平仓", false);
        return GetRest.getSuccess("成功");
    }

    /**
     * 止盈止损 批量操作
     *
     * @param orderlist
     */
    public void handleCloseMore(List<Map<String, Object>> orderlist) {
        for (Map<String, Object> l : orderlist) {
            String ordercode = String.valueOf(l.get("ordercode"));
            BigDecimal usdtPrice = new BigDecimal(String.valueOf(l.get("usdtprice")));
            CContractOrder cContractOrder = cContractOrderMapper.getByOrdercode(ordercode);
            handleCloseOrder(cContractOrder, usdtPrice, "止盈/止损自动平仓", true);
        }
    }

    /**
     * 手动平仓
     *
     * @param usdtPrice
     * @param isauto
     * @param cContractOrder
     * @return
     */
    public void handleCloseOrder(CContractOrder cContractOrder, BigDecimal usdtPrice, String remark, boolean isauto) {
        //临时打印日志用
        boolean isPrint = false;
        if (cContractOrder == null) {
            throw new ThrowJsonException("订单不存在");
        }
//        if (!FunctionUtils.isEquals(1, cContractOrder.getStatus())) {
//            throw new ThrowJsonException("订单已完成");
//        }
        if (!FunctionUtils.isEquals(StaticUtils.status_no, cContractOrder.getSettleflag())) {
            throw new ThrowJsonException("订单已处理");
        }
        //1。获取当前 货币市价
        if (usdtPrice == null || usdtPrice.compareTo(BigDecimal.ZERO) <= 0) {
            SymbolDto symDto = huobiUtils.getSymbolDto(cContractOrder.getCoin().replace("_", "/").toUpperCase());
            if (symDto == null) {
                throw new ThrowJsonException("币种不存在");
            }
            usdtPrice = symDto.getUsdtPrice();// 实时价 usdt
        }
        cContractOrder.setStopprice(usdtPrice);
        BigDecimal nowPrice = usdtPrice;//实时价 计算点差
        BigDecimal old = FunctionUtils.mul(cContractOrder.getCoinnum(), cContractOrder.getBuyprice(), 4);
        BigDecimal now = FunctionUtils.mul(cContractOrder.getCoinnum(), nowPrice, 4);
        cContractOrder.setRemark(remark);
        if (FunctionUtils.isEquals(1, cContractOrder.getType())) {
            //买涨  按当前价增加为利润
            BigDecimal rates = FunctionUtils.sub(now, old, 4);
            cContractOrder.setRates(FunctionUtils.add(rates, cContractOrder.getRates(), 4));
        } else {
            //买跌  按原始价
            BigDecimal rates = FunctionUtils.sub(old, now, 4);
            cContractOrder.setRates(FunctionUtils.add(rates, cContractOrder.getRates(), 4));
        }
        cContractOrder.setSettleflag(StaticUtils.status_yes);
        cContractOrder.setOuttype(1);
        cContractOrder.setStoptime(DateUtil.currentDate());
        cContractOrder.setStatus(2);

        //平仓扣除手续费
        BigDecimal tax = BigDecimal.ZERO;
        //计算手续费
        String out_scale = redisUtilsService.getKey(SysParamsEnums.out_scale.getKey());
        BigDecimal outScale = new BigDecimal(out_scale);
        BigDecimal money = FunctionUtils.mul(usdtPrice, cContractOrder.getCoinnum(), 4);//如果不是委托手续费自
        tax = FunctionUtils.mul(money, outScale, 4);//如果不是委托手续费自己计算

        cContractOrder.setRates(FunctionUtils.sub(cContractOrder.getRates(), tax, 4));
        int i = cContractOrderMapper.updateByPrimaryKeySelective(cContractOrder);
        if (cContractOrder.getRates().compareTo(BigDecimal.ZERO) < 0) {
            isPrint = true;
        }
        int isout = StaticUtils.pay_in;
        if (isPrint) {
            logger.warn("订单号：" + cContractOrder.getOrdercode() + " 算积累亏损前。Rates:" + cContractOrder.getRates() + " Spreadmoney:" + cContractOrder.getSpreadmoney());
        }
        BigDecimal cost = FunctionUtils.sub(cContractOrder.getRates(), cContractOrder.getSpreadmoney(), 4);//累积亏损的金额
        if (isPrint) {
            logger.warn("订单号：" + cContractOrder.getOrdercode() + " 算积累亏损后。cost:" + cost);
        }
        //如果是自定平仓
        if (isauto) {
            //如果当前盈亏是负数的时候判断 当前 亏损的金额是否大于保证金 如果亏损的金额大于保证金则不返还保证金 如果小于保证金则返还
            boolean flag = true;
            if (cost.compareTo(BigDecimal.ZERO) < 0) {
                BigDecimal win = FunctionUtils.sub(BigDecimal.ZERO, cost, 4);//将负数转换为正数
                if (win.compareTo(cContractOrder.getRealmoney()) >= 0) {//如归亏损的金额 大于保证金 表示保证金就不用计算返还了
                    flag = false;
                }
            }
        }
        cost = FunctionUtils.add(cost, cContractOrder.getRealmoney(), 4);//加上本金
        if (isPrint) {
            logger.warn("订单号:" + cContractOrder.getOrdercode() + " 加上本金后。cost:" + cost + " 加的本金Realmoney：" + cContractOrder.getRealname());
        }
        if (cost.compareTo(BigDecimal.ZERO) < 0) {
            isout = StaticUtils.pay_out;
            cost = FunctionUtils.sub(BigDecimal.ZERO, cost, 4);
            if (isPrint) {
                logger.warn("订单号:" + cContractOrder.getOrdercode() + " cost变成正数后。cost:" + cost);
            }
        }
        bipbService.handleCUsdtDetail(cContractOrder.getCid(), HandleTypeEnums.orderout.getId(), isout, cContractOrder.getOrdercode(), cost, remark, cContractOrder.getCid());
        //平仓手续费
//		if(tax.compareTo(BigDecimal.ZERO)>0) {
//			bipbService.handleCUsdtDetail(cContractOrder.getCid(), HandleTypeEnums.order_tax.getId(), StaticUtils.pay_out, cContractOrder.getOrdercode(),tax, "合约交易平仓手续费",cContractOrder.getCid());
//		}
        //平仓清除缓存
        Coins coins = coinsMapper.getBySymbol(cContractOrder.getCoin());
        String orderkey = "order_" + cContractOrder.getCid() + "_" + coins.getName() + "_" + cContractOrder.getOrdercode();
        redisUtilsService.deleteKey(orderkey);

        //修改货币总额
        //设置自动爆仓价格
        CWallet wallet = cWalletMapper.selectByPrimaryKey(cContractOrder.getCid(), CoinEnums.USDT.name());
        redisUtilsService.setOutMoney(cContractOrder, wallet, cContractOrder.getCid(), coins.getName(), false);
        //将盈利订单放入缓存中
        if(cContractOrder.getRates().compareTo(BigDecimal.ZERO)>0){
            ListUtils.addWinOrder(cContractOrder);
        }
    }


    /**
     * 自动平仓处理过期
     *
     * @param cContractOrders
     */
    public void handleorderTimeout(List<CContractOrder> cContractOrders, String outKey) {
//        for (CContractOrder cContractOrder : cContractOrders) {
//            CContractOrder order = cContractOrderMapper.getByOrdercode(cContractOrder.getOrdercode());
//            if (order == null) {
//                continue;
//            }
//            if (!FunctionUtils.isEquals(StaticUtils.status_no, order.getSettleflag())) {
//                continue;
//            }
//            if (cContractOrder.getSpreadmoney() == null) {
//                cContractOrder.setSpreadmoney(BigDecimal.ZERO);
//            }
//            cContractOrder.setRemark("自动平仓");
//            cContractOrder.setSettleflag(StaticUtils.status_yes);
//            //平仓扣除手续费
//            BigDecimal tax = BigDecimal.ZERO;
//            //计算手续费
//            String out_scale = redisUtilsService.getKey(SysParamsEnums.out_scale.getKey());
//            BigDecimal outScale = new BigDecimal(out_scale);
//            BigDecimal money = FunctionUtils.mul(order.getStopprice(), cContractOrder.getCoinnum(), 4);//如果不是委托手续费自
//            tax = FunctionUtils.mul(money, outScale, 4);//如果不是委托手续费自己计算
//            //手续费累加平仓的
//            cContractOrder.setRates(FunctionUtils.sub(cContractOrder.getRates(), tax, 4));
//
//            cContractOrderMapper.updateByPrimaryKeySelective(cContractOrder);
//            int isout = StaticUtils.pay_in;
//            BigDecimal cost = FunctionUtils.sub(cContractOrder.getRates(), cContractOrder.getSpreadmoney(), 4);
//            //如果当前盈亏是负数的时候判断 当前 亏损的金额是否大于保证金 如果亏损的金额大于保证金则不返还保证金 如果小于保证金则返还
//            boolean flag = true;
//            if (cost.compareTo(BigDecimal.ZERO) < 0) {
//                BigDecimal win = FunctionUtils.sub(BigDecimal.ZERO, cost, 4);//将负数转换为正数
//                if (win.compareTo(cContractOrder.getRealmoney()) >= 0) {//如归亏损的金额 大于保证金 表示保证金就不用计算返还了
//                    flag = false;
//                }
//            }
//            cost = FunctionUtils.add(cost, cContractOrder.getRealmoney(), 4);//加上保证金
//            if (cost.compareTo(BigDecimal.ZERO) < 0) {
//                isout = StaticUtils.pay_out;
//                cost = FunctionUtils.sub(BigDecimal.ZERO, cost, 4);
//            }
//            bipbService.handleCUsdtDetail(cContractOrder.getCid(), HandleTypeEnums.orderout.getId(), isout, cContractOrder.getOrdercode(), cost, "自动平仓", cContractOrder.getCid());
//
//            //扣除平仓手续费
////			if(tax.compareTo(BigDecimal.ZERO)>0) {
////				bipbService.handleCUsdtDetail(cContractOrder.getCid(), HandleTypeEnums.order_tax.getId(), StaticUtils.pay_out, cContractOrder.getOrdercode(),tax, "合约交易平仓手续费",cContractOrder.getCid());
////			}
//        }
//        System.out.println("处理完自动平仓线程单号：" + outKey);
//        redisUtilsService.deleteKey(outKey);
    }

    /**
     * 自动平仓处理过期2
     *
     * @param cContractOrders
     */
    public void handleorderTimeoutAutoAll(List<CContractOrder> cContractOrders, String outKey) {
        for (CContractOrder cContractOrder : cContractOrders) {
            CContractOrder order = cContractOrderMapper.getByOrdercode(cContractOrder.getOrdercode());
            if (order == null) {
                continue;
            }
            if (!FunctionUtils.isEquals(StaticUtils.status_no, order.getSettleflag())) {
                continue;
            }
            if (cContractOrder.getSpreadmoney() == null) {
                cContractOrder.setSpreadmoney(BigDecimal.ZERO);
            }
            cContractOrder.setRemark("自动平仓");
            cContractOrder.setSettleflag(StaticUtils.status_yes);
            //平仓扣除手续费
            BigDecimal tax = BigDecimal.ZERO;
            //计算手续费
            String out_scale = redisUtilsService.getKey(SysParamsEnums.out_scale.getKey());
            BigDecimal outScale = new BigDecimal(out_scale);
            BigDecimal money = FunctionUtils.mul(order.getStopprice(), cContractOrder.getCoinnum(), 4);//如果不是委托手续费自
            tax = FunctionUtils.mul(money, outScale, 4);//如果不是委托手续费自己计算
            //手续费累加平仓的
            cContractOrder.setRates(FunctionUtils.sub(cContractOrder.getRates(), tax, 4));
            cContractOrderMapper.updateByPrimaryKeySelective(cContractOrder);
        }
        redisUtilsService.deleteKey(outKey);
    }

    /**
     * 止盈止损
     *
     * @param token
     * @param ordercode
     * @param stopwin
     * @param stopdonat
     * @return
     */
    public RestResponse handleEditOrder(String token, String ordercode, BigDecimal stopwin, BigDecimal stopdonat) {
        CustomerDto customerDto = utilsService.getCusByToken(token);

        CContractOrder cContractOrder = cContractOrderMapper.getByOrdercode(ordercode);
        if (cContractOrder == null || !FunctionUtils.isEquals(1, cContractOrder.getStatus())) {
            return GetRest.getFail("当前订单已完成或不存在,禁止修改");
        }
        if (!FunctionUtils.isEquals(customerDto.getId(), cContractOrder.getCid())) {
            return GetRest.getFail("非本人操作");
        }
        //1秒级并发处理
        String key = "handleEditOrder_" + ordercode;
        boolean lockflag_cash = redisUtilsService.setIncrSecond(key, 3);
        if (!lockflag_cash) {
            return GetRest.getFail("请勿频繁进行此操作,3s后重试");
        }
//        BigDecimal priceLimit = cContractOrder.getBuyprice().divide(new BigDecimal(cContractOrder.getGearing())).divide(new BigDecimal(2)).setScale(getNumberOfDecimalPlace(cContractOrder.getBuyprice()), BigDecimal.ROUND_HALF_UP);
        if (FunctionUtils.isEquals(1, cContractOrder.getType())) {
            //买涨时判断
            if (stopwin.compareTo(cContractOrder.getBuyprice()) < 0) {
                return GetRest.getFail("止盈价格不得低于持仓价");
            }
            if (stopdonat.compareTo(cContractOrder.getBuyprice()) > 0) {
                return GetRest.getFail("止损价格不得高于持仓价");
            }
//            priceLimit = cContractOrder.getBuyprice().subtract(priceLimit);
//            if(stopdonat.compareTo(priceLimit) > 0){
//                return GetRest.getFail("止损价格不得高于" + priceLimit);
//            }
        } else {
            //买跌时判断
            if (stopwin.compareTo(cContractOrder.getBuyprice()) > 0) {
                return GetRest.getFail("止盈价格不得高于持仓价");
            }
            if (stopdonat.compareTo(cContractOrder.getBuyprice()) < 0) {
                return GetRest.getFail("止损价格不得低于持仓价");
            }
//            priceLimit = cContractOrder.getBuyprice().add(priceLimit);
//            if (stopdonat.compareTo(priceLimit) < 0) {
//                return GetRest.getFail("止损价格不得低于" + priceLimit);
//            }
        }
        cContractOrder.setStopwin(stopwin);
        cContractOrder.setStopdonat(stopdonat);
        cContractOrderMapper.updateByPrimaryKeySelective(cContractOrder);


        //更新订单缓存
        Coins coins = coinsMapper.getBySymbol(cContractOrder.getCoin());
        String orderkey = "order_" + customerDto.getId() + "_" + coins.getName() + "_" + cContractOrder.getOrdercode();
        redisUtilsService.setKey(orderkey, JSONObject.toJSONString(cContractOrder));
        return GetRest.getSuccess("成功");
    }

    public static int getNumberOfDecimalPlace(BigDecimal bigDecimal) {
        final String s = bigDecimal.toPlainString();
        System.out.println(s);
        final int index = s.indexOf('.');
        if (index < 0) {
            return 0;
        }
        return s.length() - 1 - index;
    }

    /**
     * 查询我的委托
     *
     * @param token
     * @param entrustOrder
     * @return
     */
    public RestResponse queryMyEntrust(String token, CEntrustOrder entrustOrder) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        PageHelper.startPage(entrustOrder.getPage(), entrustOrder.getRows());
        entrustOrder.setCid(customerDto.getId());
        List<CEntrustOrder> list = cEntrustOrderMapper.queryList(entrustOrder);
        return GetRest.getSuccess("成功", list);
    }

    /**
     * 委托
     *
     * @param token
     * @param type
     * @param coinnum
     * @param gearing
     * @param symbol
     * @param price
     * @param time
     * @return
     */
    public RestResponse handleEntrust(String token, Integer type, BigDecimal coinnum, Integer gearing,
                                      String symbol, BigDecimal price, Integer time) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        CCustomer cCustomer = cCustomerMapper.selectByPrimaryKey(customerDto.getId());
        if (cCustomer == null || FunctionUtils.isEquals(StaticUtils.status_no, cCustomer.getStatus())) {
            return GetRest.getFail("会员信息不存在或已冻结");
        }
//        if (FunctionUtils.isEquals(StaticUtils.identity_agent, cCustomer.getIdentity())) {
//            return GetRest.getFail("当前会员身份不可开仓");
//        }
        //1秒级并发处理
        String key = "entrust_" + customerDto.getId();
        boolean lockflag_cash = redisUtilsService.setIncrSecond(key, 3);
        if (!lockflag_cash) {
            return GetRest.getFail("委托交易频繁操作,3s后重试");
        }
        //1。获取当前 货币市价
        SymbolDto symDto = huobiUtils.getSymbolDto(symbol.replace("_", "/").toUpperCase());
        if (symDto == null) {
            throw new ThrowJsonException("币种不存在");
        }
        BigDecimal usdtPrice = symDto.getUsdtPrice();// 实时价 usdt

        //2。计算当前持仓量价值多少usdt  理论
        BigDecimal money = FunctionUtils.mul(coinnum, price, 4);
        //3.根据杠杆计算实际需要花费多少usdt
        BigDecimal realmoney = FunctionUtils.div(money, new BigDecimal(gearing), 4);
        switch (type) {
            case 1:
                //处理开多
                if (price.compareTo(usdtPrice) >= 0) {
                    return GetRest.getFail("委托价不得大于等于实时价");
                }
                break;
            case 2:
                //处理开空
                if (price.compareTo(usdtPrice) <= 0) {
                    return GetRest.getFail("委托价不得小于等于实时价");
                }
                break;
            default:
                break;
        }
        String ordercode = FunctionUtils.getOrderCode("E");
        CEntrustOrder cEntrustOrder = new CEntrustOrder();
        cEntrustOrder.setOrdercode(ordercode);
        cEntrustOrder.setCid(cCustomer.getId());
        cEntrustOrder.setType(type);
        cEntrustOrder.setRealmoney(realmoney);
        cEntrustOrder.setGearing(gearing);
        cEntrustOrder.setMoney(money);
        cEntrustOrder.setPrice(price);
        cEntrustOrder.setCoin(symbol);
        cEntrustOrder.setCoinnum(coinnum);
        cEntrustOrder.setCreatetime(DateUtil.currentDate());
        cEntrustOrder.setStatus(StaticUtils.entrust_ing);
        cEntrustOrder.setRunTime(time);

        BigDecimal tax = BigDecimal.ZERO;
        if (FunctionUtils.isEquals(1, type)) {
            //计算手续费
            String contract_down = redisUtilsService.getKey(SysParamsEnums.contract_up.getKey());
            BigDecimal contractDown = new BigDecimal(contract_down);
            tax = FunctionUtils.mul(money, contractDown, 4);//如果不是委托手续费自己计算
        } else {
            //计算手续费
            String contract_down = redisUtilsService.getKey(SysParamsEnums.contract_down.getKey());
            BigDecimal contractDown = new BigDecimal(contract_down);
            tax = FunctionUtils.mul(money, contractDown, 4);//如果不是委托手续费自己计算
        }


        cEntrustOrder.setTax(tax);
        cEntrustOrderMapper.insertSelective(cEntrustOrder);
        //扣除保证金
        bipbService.handleCUsdtDetail(cCustomer.getId(), HandleTypeEnums.entrust.getId(), StaticUtils.pay_out, ordercode, realmoney, "委托交易", cCustomer.getId());
        if (tax.compareTo(BigDecimal.ZERO) > 0) {
            //扣除手续费
            bipbService.handleCUsdtDetail(cCustomer.getId(), HandleTypeEnums.contract_tax.getId(), StaticUtils.pay_out, ordercode, tax, "委托交易手续费", cCustomer.getId());
        }

        //添加到缓存
        String orderkey = "entrust_" + cCustomer.getId() + "_" + ordercode;
        redisUtilsService.setKey(orderkey, JSONObject.toJSONString(cEntrustOrder));
        return GetRest.getSuccess("委托成功");
    }


    public RestResponse handleCancelEntrust(String token, String ordercode) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        CCustomer cCustomer = cCustomerMapper.selectByPrimaryKey(customerDto.getId());
        if (cCustomer == null || FunctionUtils.isEquals(StaticUtils.status_no, cCustomer.getStatus())) {
            return GetRest.getFail("会员信息不存在或已冻结");
        }
        CEntrustOrder cEntrustOrder = cEntrustOrderMapper.getByOrdercode(ordercode);
        if (cEntrustOrder == null || !FunctionUtils.isEquals(StaticUtils.entrust_ing, cEntrustOrder.getStatus())) {
            return GetRest.getFail("委托订单不存在,或状态不可取消");
        }

        if (!FunctionUtils.isEquals(cCustomer.getId(), cEntrustOrder.getCid())) {
            return GetRest.getFail("非本人操作");
        }
        cEntrustOrder.setStatus(StaticUtils.entrust_cancel);
        cEntrustOrder.setCanceltime(DateUtil.currentDate());
        cEntrustOrderMapper.updateByPrimaryKeySelective(cEntrustOrder);

        BigDecimal refund = FunctionUtils.add(cEntrustOrder.getTax(), cEntrustOrder.getRealmoney(), 6);
        //扣除保证金
        bipbService.handleCUsdtDetail(cCustomer.getId(), HandleTypeEnums.entrust_cancel.getId(), StaticUtils.pay_in, ordercode, refund, "取消委托交易", cCustomer.getId());
        //删除key
        String orderkey = "entrust_" + cCustomer.getId() + "_" + ordercode;
        redisUtilsService.deleteKey(orderkey);
        return GetRest.getSuccess("取消成功");
    }

    /**
     * 处理逐仓下单
     *
     * @param token
     * @param price
     * @param type
     * @param coinnum
     * @param symbol
     * @return
     */
    public RestResponse handleZCOrder(String token, Integer type, BigDecimal coinnum, BigDecimal price,
                                      String symbol) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        CCustomer cCustomer = cCustomerMapper.selectByPrimaryKey(customerDto.getId());
        if (cCustomer == null || FunctionUtils.isEquals(StaticUtils.status_no, cCustomer.getStatus())) {
            return GetRest.getFail("会员信息不存在或已冻结");
        }
//        if (FunctionUtils.isEquals(StaticUtils.identity_agent, cCustomer.getIdentity())) {
//            return GetRest.getFail("当前会员身份不可开仓");
//        }
        Integer cid = cCustomer.getId();
        //1秒级并发处理
        String key = "zc_" + cid;
        boolean lockflag_cash = redisUtilsService.setIncrSecond(key, 3);
        if (!lockflag_cash) {
            return GetRest.getFail("逐仓交易频繁操作,3s后重试");
        }
        //1。获取当前 货币市价
        SymbolDto symDto = redisUtilsService.getSymbolDto(symbol.replace("_", "/").toUpperCase());
        if (symDto == null) {
            throw new ThrowJsonException("币种不存在");
        }
        if (!FunctionUtils.isEquals(StaticUtils.status_yes, symDto.getType())) {
            throw new ThrowJsonException("币种不可操作逐仓");
        }


        BigDecimal usdtPrice = symDto.getUsdtPrice();// 实时价 usdt
        //处理逐仓
        String paycode = FunctionUtils.getOrderCode("Z");
        //2。计算当前逐仓订单金额本金
        BigDecimal money = FunctionUtils.mul(coinnum, price, 4);
        //获取手续费参数
        BigDecimal tax = FunctionUtils.mul(money, symDto.getZctax(), 6);

        BigDecimal stopWin = BigDecimal.ZERO;
        BigDecimal stopfail = BigDecimal.ZERO;
        //保存逐仓订单
        CZcOrder cContractOrder = new CZcOrder();
        cContractOrder.setCid(cid);
        cContractOrder.setType(type);
        cContractOrder.setOrdercode(paycode);
        cContractOrder.setCapital(money);
        cContractOrder.setTax(tax);
        cContractOrder.setBuyprice(usdtPrice);
        cContractOrder.setStopprice(usdtPrice);
        cContractOrder.setCoin(symbol);
        cContractOrder.setCreatetime(DateUtil.currentDate());
        cContractOrder.setZcprice(symDto.getZcstopscale());
        cContractOrder.setZcscale(symDto.getZcscale());
        cContractOrder.setStopwin(stopWin);
        cContractOrder.setStopfail(stopfail);
        cZcOrderMapper.insertSelective(cContractOrder);
        //扣除逐仓资金
        bipbService.handleCZcDetail(cid, HandleTypeEnums.zcorder.getId(), StaticUtils.pay_out, paycode, FunctionUtils.add(money, tax, 6), "逐仓开仓", cid);

        //把逐仓订单设置到缓存
        String orderkey = "zc_" + customerDto.getId() + "_" + symDto.getName() + "_" + cContractOrder.getOrdercode();
        redisUtilsService.setKey(orderkey, JSONObject.toJSONString(cContractOrder));
        //开仓触发奖励
        pattenService.handlePatten(cCustomer, tax, "逐仓账户开仓推荐奖励");
        return GetRest.getSuccess("操作成功");
    }

    public RestResponse handleCloseZc(String token, String ordercode) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        //1秒级并发处理
        String key = "closezcorder_" + ordercode;
        boolean lockflag_cash = redisUtilsService.setIncrSecond(key, 3);
        if (!lockflag_cash) {
            return GetRest.getFail("平仓频繁操作,3s后重试");
        }
        CZcOrder cZcOrder = cZcOrderMapper.getByOrdercode(ordercode);
        if (cZcOrder == null || !FunctionUtils.isEquals(1, cZcOrder.getStatus())) {
            return GetRest.getFail("当前订单不存在或已平仓");
        }

        SymbolDto symDto = redisUtilsService.getSymbolDto(cZcOrder.getCoin().replace("_", "/").toUpperCase());
        if (symDto == null) {
            throw new ThrowJsonException("币种不存在");
        }
//        long time = DateUtil.getHMS(new Date(), cZcOrder.getCreatetime());
//        if (time < 60 * 1000) {
//            return GetRest.getFail(ErrorInfoEnum.LANG_CLOSE_ORDER_LIMIT.getErrorCNMsg(), ErrorInfoEnum.LANG_CLOSE_ORDER_LIMIT.getErrorENMsg());
//        }
        handleZcClose(customerDto.getId(), symDto, cZcOrder, false);
        return GetRest.getSuccess("平仓完成");
    }

    /**
     * 处理关闭逐仓
     *
     * @param cid
     * @param symDto
     * @param cZcOrder
     * @param auto     是否自动
     */
    public void handleZcClose(Integer cid, SymbolDto symDto, CZcOrder cZcOrder, boolean auto) {
        BigDecimal usdtPrice = symDto.getUsdtPrice();// 实时价 usdt
        cZcOrder.setStopprice(usdtPrice);
        cZcOrder.setStatus(2);
        cZcOrder.setStoptime(DateUtil.currentDate());
        //先求出 当前逐仓订单最多应该转到的钱 然后看平仓的时候占里面多少比例 也许亏 也许赚
        BigDecimal real_reward = FunctionUtils.mul(cZcOrder.getZcscale(), cZcOrder.getCapital(), 6);
        BigDecimal scope = BigDecimal.ZERO;
        String typeStr = "";
        if (FunctionUtils.isEquals(1, cZcOrder.getType())) {
            //如果是开多 涨了就赚
            //计算涨幅
            typeStr = "开多";
            scope = FunctionUtils.sub(usdtPrice, cZcOrder.getBuyprice(), 6);
        } else if (FunctionUtils.isEquals(2, cZcOrder.getType())) {
            //如果是开空 跌了就赚
            //计算涨幅
            typeStr = "开空";
            scope = FunctionUtils.sub(cZcOrder.getBuyprice(), usdtPrice, 6);
        }
        //涨幅/逐仓理论涨幅金额
        BigDecimal scale = FunctionUtils.div(scope, cZcOrder.getZcprice(), 6);
        if (scale.compareTo(BigDecimal.ONE) >= 0) {
            scale = BigDecimal.ONE;
        } else {
            //如果当前亏损 亏得比例大于0 那么最多只能亏完本金也就是-1
            BigDecimal zScale = FunctionUtils.sub(BigDecimal.ZERO, scale, 6);
            if (zScale.compareTo(BigDecimal.ONE) >= 0) {
                scale = FunctionUtils.sub(BigDecimal.ZERO, BigDecimal.ONE, 6);
            }
        }
        //计算本次赚了 或者是亏了多少
        BigDecimal scope_reward = FunctionUtils.mul(scale, real_reward, 6);
        BigDecimal reward = scope_reward;
        cZcOrder.setReward(reward);
        cZcOrder.setStatus(2);
        if (auto) {
            cZcOrder.setRemark("逐仓自动平仓");
        } else {
            cZcOrder.setRemark("逐仓手动平仓");
        }
        cZcOrderMapper.updateByPrimaryKeySelective(cZcOrder);
        BigDecimal cost = FunctionUtils.add(cZcOrder.getCapital(), reward, 6);
        if (cost.compareTo(BigDecimal.ZERO) > 0) {
            bipbService.handleCZcDetail(cid, HandleTypeEnums.orderout.getId(), StaticUtils.pay_in, cZcOrder.getOrdercode(), cost, cZcOrder.getRemark(), cid);
        }
        //平仓删除
        String orderkey = "zc_" + cid + "_" + symDto.getName() + "_" + cZcOrder.getOrdercode();
        redisUtilsService.deleteKey(orderkey);
        if (auto) {
            String format = "";
            if (cZcOrder.getReward().compareTo(BigDecimal.ZERO) < 0) {
                format = String.format(LingKaiSmsEnums.LK_SMS_ZC_LOSS.getCode(), symDto.getName(), typeStr);
            } else {
                format = String.format(LingKaiSmsEnums.LK_SMS_ZC_PROFIT.getCode(), symDto.getName(), typeStr);
            }
            CCustomer cCustomer = cCustomerMapper.selectByPrimaryKey(cZcOrder.getCid());
            SmsSendPool.getInstance().send(new SmsSend(cCustomer.getPhone(), LingKaiSmsEnums.LK_SMS_ZC_LOSS.getType(), format));
        }
    }

    public RestResponse queryZCorder(String token, CZcOrder zcOrder) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        PageHelper.startPage(zcOrder.getPage(), zcOrder.getRows());
        CZcOrderExample example = new CZcOrderExample();
        example.createCriteria().andCidEqualTo(customerDto.getId()).andStatusEqualTo(2);
        example.setOrderByClause("id desc");
        List<CZcOrder> list = cZcOrderMapper.selectByExample(example);
        return GetRest.getSuccess("成功", list);
    }

    public RestResponse handleZCStop(String token, String ordercode, BigDecimal stopwin, BigDecimal stopfail) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        //1秒级并发处理
        String key = "stopzcorder_" + ordercode;
        boolean lockflag_cash = redisUtilsService.setIncrSecond(key, 3);
        if (!lockflag_cash) {
            return GetRest.getFail("修改频繁操作,3s后重试");
        }
        CZcOrder cZcOrder = cZcOrderMapper.getByOrdercode(ordercode);
        if (cZcOrder == null || !FunctionUtils.isEquals(1, cZcOrder.getStatus())) {
            return GetRest.getFail("当前订单不存在或已平仓");
        }
        if (FunctionUtils.isEquals(1, cZcOrder.getType())) {
            //买涨时判断
            if (stopwin != null) {
                if (stopwin.compareTo(cZcOrder.getBuyprice()) < 0) {
                    return GetRest.getFail("止盈价格不得低于持仓价");
                }
                if (stopwin.compareTo(cZcOrder.getBuyprice().add(cZcOrder.getZcprice())) > 0) {
                    return GetRest.getFail("止盈价格不得高于自动平仓价");
                }
            }
            if (stopfail != null) {
                if (stopfail.compareTo(cZcOrder.getBuyprice()) > 0) {
                    return GetRest.getFail("止损价格不得高于持仓价");
                }
                if (stopfail.compareTo(cZcOrder.getBuyprice().subtract(cZcOrder.getZcprice())) < 0) {
                    return GetRest.getFail("止损价格不得低于自动平仓价");
                }
            }
        } else {
            //买跌时判断
            if (stopwin != null) {
                if (stopwin.compareTo(cZcOrder.getBuyprice()) > 0) {
                    return GetRest.getFail("止盈价格不得高于持仓价");
                }
                if (stopwin.compareTo(cZcOrder.getBuyprice().subtract(cZcOrder.getZcprice())) < 0) {
                    return GetRest.getFail("止盈价格不得低于自动平仓价");
                }
            }
            if (stopfail != null) {
                if (stopfail.compareTo(cZcOrder.getBuyprice()) < 0) {
                    return GetRest.getFail("止损价格不得低于持仓价");
                }
                if (stopfail.compareTo(cZcOrder.getBuyprice().add(cZcOrder.getZcprice())) > 0) {
                    return GetRest.getFail("止损价格不得高于自动平仓价");
                }
            }
        }
        BigDecimal winmoney = BigDecimal.ZERO;
        BigDecimal failmoney = BigDecimal.ZERO;
//        BigDecimal real_reward = FunctionUtils.mul(cZcOrder.getZcscale(), cZcOrder.getCapital(), 6);
        //赢的金额
//        if (stopwin != null) {
////            winmoney = FunctionUtils.mul(stopwin, real_reward, 6);
//            winmoney = FunctionUtils.sub(stopwin, cZcOrder.getBuyprice(), 6).multiply(cZcOrder.getZcscale());
//        }
//        //亏的金额
//        if (stopfail != null) {
////            failmoney = FunctionUtils.mul(stopfail, real_reward, 6);
//            failmoney = FunctionUtils.sub(stopfail, cZcOrder.getBuyprice(), 6).multiply(cZcOrder.getZcscale());
//        }
        cZcOrder.setStopfail(stopfail);
        cZcOrder.setStopwin(stopwin);
        cZcOrderMapper.updateByPrimaryKeySelective(cZcOrder);
        //把逐仓订单设置到缓存
        String coin = cZcOrder.getCoin().replace("_", "/").toUpperCase();
        String orderkey = "zc_" + customerDto.getId() + "_" + coin + "_" + cZcOrder.getOrdercode();
        redisUtilsService.setKey(orderkey, JSONObject.toJSONString(cZcOrder));
        return GetRest.getSuccess("设置成功");
    }

    /**
     * 查询订单
     *
     * @param token
     * @return
     */
    public RestResponse queryContractOrderByType(String token, Integer type) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        CContractOrderExample example = new CContractOrderExample();
        example.setOrderByClause("`id`  DESC");
        List<CContractOrder> cContractOrderList = new ArrayList<>();
        if (type == 1) {
            example.createCriteria().andRatesGreaterThan(BigDecimal.ZERO);
            cContractOrderList = cContractOrderMapper.selectByExample(example);
            cContractOrderList = cContractOrderList.stream().sorted(Comparator.comparing(CContractOrder::getCreatetime).reversed()).limit(20).collect(Collectors.toList());

        } else {
            example.createCriteria().andStatusEqualTo(2).andCidEqualTo(customerDto.getId()).andRunTimeGreaterThan(0);
            PageHelper.startPage(1, 20);
            cContractOrderList = cContractOrderMapper.selectByExample(example);
        }

        Map map = new HashMap();
        map.put("cContractOrderList",cContractOrderList);
        map.put("total",cContractOrderList.size());
        return GetRest.getSuccess("成功", map);
    }

    /**
     * 跟单交易
     *
     * @param token
     * @return
     */
    public RestResponse documentary(String token, Integer type) {
        CustomerDto customerDto = utilsService.getCusByToken(token);
        CContractOrderExample example = new CContractOrderExample();
        example.setOrderByClause("`id`  DESC");
        CContractOrderExample.Criteria criteria = example.createCriteria().andCidEqualTo(customerDto.getId());
        if (type == 1) {
            criteria.andRatesGreaterThan(BigDecimal.ZERO);
        } else {
            criteria.andStatusEqualTo(2);
        }
        PageHelper.startPage(1, 20);
        List<CContractOrder> cContractOrderList = cContractOrderMapper.selectByExample(example);
        return GetRest.getSuccess("成功", cContractOrderList);
    }


    /**
     * 根据memberId查询用户历史跟单
     */
    public RestResponse gdHistory(Integer memberId){
        GdBuyRecordExample gdBuyRecordExample = new GdBuyRecordExample();
        gdBuyRecordExample.createCriteria().andMemberIdEqualTo(memberId);
        PageHelper.startPage(1, 20);
        List<GdBuyRecord> gdBuyRecords = gdBuyRecordMapper.selectByExample(gdBuyRecordExample);
        return GetRest.getSuccess("成功",gdBuyRecords);
    }


    /**
     * 查询账户资金  累计收益  收益明细
     */
    public RestResponse assets(String token,int page){
        CustomerDto cusByToken = utilsService.getCusByToken(token);

        //查询账户跟单资金
        CWalletExample cWalletExample = new CWalletExample();
        cWalletExample.createCriteria().andCidEqualTo(cusByToken.getId());
        List<CWallet> cWallets = cWalletMapper.selectByExample(cWalletExample);
        Map map = new HashMap();
        if(cWallets  == null){
            map.put("gdbalance",0);
        }else {
            map.put("gdbalance", cWallets.get(0).getGdbalance());
        }
        //查询收益明细
        GdUserBonusExample userBonusExample = new GdUserBonusExample();
        userBonusExample.createCriteria().andMemberIdEqualTo(cusByToken.getId());
        PageHelper.startPage(page, 20);
        List<GdUserBonus> gdUserBonuses = gdUserBonusMapper.selectByExample(userBonusExample);
        gdUserBonuses = gdUserBonuses.stream().sorted(Comparator.comparing(GdUserBonus::getCreateTime).reversed()).limit(20).collect(Collectors.toList());
        PageInfo<GdUserBonus> pageInfo = new PageInfo<>(gdUserBonuses);

//        map.put("total",pageInfo.getTotal());
        map.put("gdUserBonuses",pageInfo);
        //查询累计收益
        BigDecimal sumBonus = gdUserBonusMapper.sumBonus(cusByToken.getId());
        map.put("sumBonus",sumBonus);
        return  GetRest.getSuccess("成功",map);
    }


    /**
     * 转入
     * isout 1转入 0转出
     */
    public RestResponse into(String token,Integer isout,Integer page){
        CustomerDto cusByToken = utilsService.getCusByToken(token);
        GdDetailExample gdDetailExample = new GdDetailExample();
        if(isout.intValue() == 1) {
            gdDetailExample.createCriteria().andCidEqualTo(cusByToken.getId()).andIsoutEqualTo(isout).andRemarkEqualTo("币币转跟单");
        }else {
            gdDetailExample.createCriteria().andCidEqualTo(cusByToken.getId()).andIsoutEqualTo(isout).andRemarkEqualTo("跟单转币币");

        }
        PageHelper.startPage(page, 20);
        List<GdDetail> gdDetails = gdDetailMapper.selectByExample(gdDetailExample);
        gdDetails = gdDetails.stream().sorted(Comparator.comparing(GdDetail::getCreatetime).reversed()).limit(20).collect(Collectors.toList());

        PageInfo<GdDetail> pageInfo = new PageInfo<>(gdDetails);
//        Map map = new HashMap();
//        map.put("total",pageInfo.getTotal());
//        map.put("cUsdtDetails",pageInfo);
        return GetRest.getSuccess("成功",pageInfo);
    }
}
