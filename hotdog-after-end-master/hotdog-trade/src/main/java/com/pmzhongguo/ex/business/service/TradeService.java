package com.pmzhongguo.ex.business.service;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.pmzhongguo.ex.business.dto.TradeDto;
import com.pmzhongguo.ex.business.entity.Account;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.entity.Order;
import com.pmzhongguo.ex.business.entity.Trade;
import com.pmzhongguo.ex.business.mapper.OrderMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.utils.SerializeUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.qiniu.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Tuple;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class TradeService extends BaseServiceSupport {

    private static final String ORDER_TRADE_OPPO_REDIS_LOCK_PRE = "order_trade_oppo_"; // EXPIRE_TIME
    private static final int ORDER_TRADE_OPPO_REDIS_LOCK_EXPIRE_TIME = 5 * 1000;

    private static final String ORDER_TRADE_ENTRYORDER_REDIS_LOCK_PRE = "order_trade_entryOrder_"; // EXPIRE_TIME
    private static final int ORDER_TRADE_ENTRYORDER_REDIS_LOCK_EXPIRE_TIME = 5 * 1000;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ExService exService;

    // 对同一个队列，一个时刻只能一个线程访问，因此利用Guava进行变量锁
    private Interner<String> keyNamePool = Interners.newWeakInterner();


    // 对方父队列
    private Set<Tuple> oppositeOutterSet = null;
    // 对方子队列
    private Set<byte[]> oppositeInnerSet = null;

    /**
     * 交易处理，先吃单，吃不到，就挂单 本方法同时提供给用户取消订单使用，因为取消订单需要锁Redis队列，取消时，o_type设置为cancel
     * <p>
     * 队列说明：
     * <p>
     * 外层队列格式： 1、首先每个交易对两个队列，名字类似 ltcbtc_sel 和 ltcbtc_buy 2、队列下面存储的是
     * 价格和价格数量数组的对，以价格作为Key，价格和数量的数组作为Value
     * <p>
     * 内层队列格式： 1、首先每个交易对有N个队列，名字类似 ltcbtc_sel_2.6 ltcbtc_sel_2.5 * ltcbtc_buy_2.4
     * ltcbtc_buy_2.3，其中的2.6 等表示价格 2、队列下面存储的是 订单和时间戳的对，以时间戳作为Key，订单作为Value
     *
     * @param order
     * @param isUserCancel
     */
    public void trade(Order order, boolean isUserCancel) {
        String selfOutterKeyName = order.getTable_name() + "_" + order.getO_type(); // 自身队列Redis的KeyName

        String oppositeOutterKeyName = order.getTable_name() + "_"; // 对方（上面是买，这里就是卖，上面是卖，这里就是买）队列Redis的KeyName
        // 获取对方队列，如果对方是卖方，则按价格的升序排列，如果对方是买方，则按价格的降序排列
        if ("buy".equals(order.getO_type())) {
            oppositeOutterKeyName += "sell";
        } else {
            oppositeOutterKeyName += "buy";
        }

        // 全局锁 globalIsLock
        String globalLockKey = ORDER_TRADE_OPPO_REDIS_LOCK_PRE + order.getId();
        boolean globalIsLock = JedisUtil.getInstance().getLockRetry(globalLockKey, IPAddressPortUtil.IP_ADDRESS,
                ORDER_TRADE_OPPO_REDIS_LOCK_EXPIRE_TIME, 50, 100);
        long start = System.currentTimeMillis();
        if (globalIsLock) {
            try {

                // 交易
                trade(isUserCancel, selfOutterKeyName, oppositeOutterKeyName, order);

            } catch (Exception e) {
                logger.warn("order trade Exception! order:{}", order.toString(), e);
                throw new BusinessException(-1, ErrorInfoEnum.LANG_ORDER_TRADE_ERROR.getErrorENMsg());
            } finally {
                boolean isRelease = JedisUtil.getInstance().releaseLock(globalLockKey, IPAddressPortUtil.IP_ADDRESS);
                if (!isRelease) {
                    long end = System.currentTimeMillis();
                    logger.warn("交易锁解锁失败。key:{}  耗时:{} ms", globalLockKey, end - start);
                }
            }
        } else {
            logger.warn("未能拿到交易锁。key:{}", globalLockKey);
        }
//		}	synchronized end
    }

    private void trade(boolean isUserCancel, String selfOutterKeyName, String oppositeOutterKeyName, Order order) {
        // 如果是取消，则对方队列就是本方队列，因为下面的同步锁是锁oppositeOutterKeyName的
        if (isUserCancel) {
            oppositeOutterKeyName = selfOutterKeyName;
        }

        // 获取交易对，主要是要里面的手续费。
        CurrencyPair cp = HelpUtils.getCurrencyPairMap().get(order.getTable_name().toUpperCase());
        // 对方(挂单方)订单
        Order oppositeOrder = null;
        // 对方(挂单方)价格数量对
        BigDecimal[] oppositePriceVolume = null;

        // 吃单是否是市价买单，市价买单的处理不同一些，所以单独在这里赋值，后面好使用
        boolean isMarketBuyOrder = "market".equals(order.getO_price_type()) && "buy".equals(order.getO_type());

        // 同步锁，对同一个队列，只能有一个线程进入，每一个交易对的买方和卖方，都有两个队列，一个队列用于存储价格，一个队列用于存储订单(按时间戳排序)，需要两个队列的原因是Redis的有序集合只支持一个字段排序
        // 需要说明的是，对方队列需要同步锁，本方队列不需要锁，因为本方队列只涉及写入
        // 而对对方队列涉及写入和读取
        // 原则，挂单不可能是市价单
//synchronized (keyNamePool.intern(String.valueOf(order.getTable_name()))) {

        Tuple outterTuple = null; // 外部队列，存储 价格和数量的键值对，Key的价格
        Iterator<byte[]> innerIter = null;
        byte[] innerByte = null; // 内部队列的订单对象

        boolean isDeal = false; // 是否成交

        if (isUserCancel) { // 用户取消订单，也在此处处理，因为需要同步队列

            // 先在Redis中找到这个订单
            oppositeInnerSet = JedisUtil.getInstance().zrangeByScore(
                    (oppositeOutterKeyName + "_" + order.getPrice().doubleValue()).getBytes(),
                    order.getCreate_time_unix(), order.getCreate_time_unix());
            if (null == oppositeInnerSet) {
                throw new BusinessException(-1, ErrorInfoEnum.LANG_REDIS_ERROR.getErrorENMsg());
            }
            try {
                // 更新数据库
                this.cancelOrder(order);

                // 更新缓存
                this.cancelOrderByCancel(oppositeOutterKeyName + "_" + order.getPrice().doubleValue(),
                        oppositeOutterKeyName, order);
                return;
            } catch (Exception e) {
                logger.warn("cancel order Exception! order:{}", order.toString(), e);
                throw new BusinessException(-1, ErrorInfoEnum.LANG_CANCEL_ORDER_ERROR.getErrorENMsg());
            }
        }

        // 获取对方外层队列（价格）队列，如果对方是卖方，则按价格的升序排列，如果对方是买方，则按价格的降序排列
        // 外层队列是 价格和数量的关系，不管具体的订单
        if ("buy".equals(order.getO_type())) {
            oppositeOutterSet = JedisUtil.getInstance().zrangeByScoreWithScores(oppositeOutterKeyName.getBytes(),
                    Double.MAX_VALUE, 0);
        } else {
            oppositeOutterSet = JedisUtil.getInstance().zrevrangeByScoreWithScores(oppositeOutterKeyName.getBytes(),
                    Double.MAX_VALUE, 0);
        }

        // 连接不上Redis服务器
        if (null == oppositeOutterSet) {
            throw new BusinessException(-1, ErrorInfoEnum.LANG_REDIS_ERROR.getErrorENMsg());
        }

        Iterator<Tuple> outterIter = oppositeOutterSet.iterator();
        while (outterIter.hasNext()) {
            if (isDeal) {
                break;
            }

            outterTuple = outterIter.next();
            oppositePriceVolume = (BigDecimal[]) SerializeUtil.deserialize(outterTuple.getBinaryElement());
            if (oppositePriceVolume[1].compareTo(BigDecimal.ZERO) <= 0) { // 数量为0，则移除有序集
                JedisUtil.getInstance().zrem((oppositeOutterKeyName).getBytes(), outterTuple.getBinaryElement());

                // 进入下一次循环
                continue;
            }

            // 吃单方为买方，则需要吃单方价格大于等于挂卖单价格
            // 吃单方为卖方，则需要吃单方价格小于等于挂买单价格
            // 吃单方为市价单，则不需要比较价格
            if (("buy".equals(order.getO_type())
                    && order.getPrice().compareTo(BigDecimal.valueOf(outterTuple.getScore())) >= 0)
                    || ("sell".equals(order.getO_type())
                    && order.getPrice().compareTo(BigDecimal.valueOf(outterTuple.getScore())) <= 0)
                    || order.getPrice().compareTo(BigDecimal.ZERO) == 0) {
                // 获取对方内层（时间和对象）队列，时间都是按升序排列
                oppositeInnerSet = JedisUtil.getInstance().zrangeByScore(
                        (oppositeOutterKeyName + "_" + outterTuple.getScore()).getBytes(), Double.MAX_VALUE, 0);

                if (null == oppositeInnerSet || oppositeInnerSet.size() == 0) {
                    // 删除外层队列
                    JedisUtil.getInstance().zrem(oppositeOutterKeyName.getBytes(), outterTuple.getBinaryElement());

                    logger.error("出现挂单队列数量大于0，但实际数量小于等于0的情况：" + Arrays.toString(oppositePriceVolume));
                    continue;
                }

                innerIter = oppositeInnerSet.iterator();
                while (innerIter.hasNext()) {
                    innerByte = innerIter.next();
                    oppositeOrder = (Order) SerializeUtil.deserialize(innerByte);
                    String lockKey = ORDER_TRADE_OPPO_REDIS_LOCK_PRE + oppositeOrder.getId();
                    boolean isLock = JedisUtil.getInstance().getLockRetry(lockKey, IPAddressPortUtil.IP_ADDRESS,
                            ORDER_TRADE_OPPO_REDIS_LOCK_EXPIRE_TIME, 60, 50);
                    if (isLock) {
                        long localStart = System.currentTimeMillis();
                        try {
                            // 比对数据库订单和Redis缓存订单是否一致
                            Order oppositeOrderMySQL = exService.getOrder(oppositeOrder);
                            if (oppositeOrderMySQL == null) {
                                JedisUtil.getInstance().zrem(
                                        (oppositeOutterKeyName + "_" + outterTuple.getScore()).getBytes(), innerByte);
                                continue;
                            }
                            // 已经结束的交易直接清除缓存
                            if (oppositeOrderMySQL.getO_status().equals("done")
                                    || oppositeOrderMySQL.getO_status().equals("partial-canceled")
                                    || oppositeOrderMySQL.getO_status().equals("canceled")) {
                                JedisUtil.getInstance().zrem(
                                        (oppositeOutterKeyName + "_" + outterTuple.getScore()).getBytes(), innerByte);
                                continue;
                            }
                            if (oppositeOrderMySQL.getDone_volume().compareTo(oppositeOrder.getDone_volume()) != 0) {
                                logger.warn("交易时挂单成交数据不一致。member_id:" + oppositeOrderMySQL.getMember_id() + " o_no:"
                                        + oppositeOrderMySQL.getO_no() + "  pair_id:" + oppositeOrderMySQL.getPair_id()
                                        + " data_volume:" + oppositeOrderMySQL.getVolume() + " data_done_volume:"
                                        + oppositeOrderMySQL.getDone_volume() + " redisdone_volume:"
                                        + oppositeOrder.getVolume() + " redisdone_done_volume:"
                                        + oppositeOrder.getDone_volume());
                                oppositeOrder.setDone_volume(oppositeOrderMySQL.getDone_volume());
                            }

                            if (oppositeOrderMySQL.getUnfrozen().compareTo(oppositeOrder.getUnfrozen()) != 0) {
                                logger.warn("交易时挂单解冻数据不一致。member_id:" + oppositeOrderMySQL.getMember_id() + " o_no:"
                                        + oppositeOrderMySQL.getO_no() + "  pair_id:" + oppositeOrderMySQL.getPair_id()
                                        + " data_Frozen:" + oppositeOrderMySQL.getFrozen() + " data_Unfrozen:"
                                        + oppositeOrderMySQL.getFrozen() + " redisdone_Frozen:"
                                        + oppositeOrder.getFrozen() + " redisdone_Unfrozen:"
                                        + oppositeOrder.getUnfrozen());
                                oppositeOrder.setUnfrozen(oppositeOrderMySQL.getUnfrozen());
                            }

                            // 挂单（肯定是限价单）的成交数量等于购买数量，就不应该在队列中了
                            if (oppositeOrder.getVolume().subtract(oppositeOrder.getDone_volume())
                                    .compareTo(BigDecimal.ZERO) <= 0) { // 理论上不会出现这种情况，但还是做个处理
                                // 删除内层队列
                                JedisUtil.getInstance().zrem(
                                        (oppositeOutterKeyName + "_" + outterTuple.getScore()).getBytes(), innerByte);
                                logger.error("出现挂单数量小于等于0的情况：" + oppositeOrder.toString());
                                continue;
                            }

                            // -----------------计算吃单数量-----------------
                            BigDecimal takerVolume = order.getVolume();
                            // 减去已经成交量
                            takerVolume = takerVolume.subtract(order.getDone_volume());

                            // 市价买单的数量需要根据当前卖方的价格计算，----注意----，在市价买单结束前，done_volume存储的都是计价货币的金额
                            // 价格以挂单价格为准
                            if (isMarketBuyOrder) {
                                takerVolume = takerVolume.divide(oppositeOrder.getPrice(), cp.getVolume_precision(),
                                        BigDecimal.ROUND_DOWN);

                                // 吃单数量等于0，这种情况只有市价买单可能存在，因为传入的volume是计价货币，要除以价格，才能得出数量，可能是个很小的数字，因此需要退出
                                if (takerVolume.compareTo(BigDecimal.ZERO) <= 0) {
                                    // 退出前，还原冻结在本方法的最后执行
                                    // exService.accountProc(order.getFrozen().subtract(order.getUnfrozen()),
                                    // order.getQuote_currency(), order.getMember_id(), 4);
                                    // 退出循环吃单
                                    isDeal = true;
                                    break;
                                }
                            } else {
                                // 市价卖单/买单的数量就是实际数量
                            }
                            // +++++++++++++++++计算吃单数量+++++++++++++++++

                            // -----------------计算挂单数量（挂单队列中，当前循环到的订单的）-----------------
                            BigDecimal makerVolume = oppositeOrder.getVolume();
                            // 减去已经成交量
                            makerVolume = makerVolume.subtract(oppositeOrder.getDone_volume());
                            // +++++++++++++++++计算卖出数量（挂单队列中，当前循环到的订单的）+++++++++++++++++

                            // 检查冻结数 前面校验了数量和冻结数后， 还卡单的机率应该很小了。以前卡单主要是缓存订单数据跟订单数据不一致导致解冻失败
                            BigDecimal checkVolume = takerVolume.compareTo(makerVolume) == 1 ? makerVolume
                                    : takerVolume;
                            String checkCurrency = "buy".equals(oppositeOrder.getO_type())
                                    ? oppositeOrder.getQuote_currency()
                                    : oppositeOrder.getBase_currency();
                            BigDecimal checkFrozen = "buy".equals(oppositeOrder.getO_type())
                                    ? checkVolume.multiply(oppositeOrder.getPrice())
                                    : checkVolume;
                            Account account = memberService.getAccount(HelpUtils.newHashMap("currency", checkCurrency,
                                    "member_id", oppositeOrder.getMember_id()));
                            // 根据数量判断是否要撤单
                            boolean isCancelForVolume = account.getFrozen_balance().compareTo(checkFrozen) == -1;
                            BigDecimal remainFrozen = oppositeOrder.getFrozen().subtract(oppositeOrder.getUnfrozen());
                            // 根据冻结数判断是否要撤单
                            boolean isCancelForFrozen = account.getFrozen_balance().compareTo(remainFrozen) == -1;
                            // 判断冻结数是否大于总额，我就不信还有其他的卡单情况
                            boolean isErrorData = account.getTotal_balance()
                                    .compareTo(account.getFrozen_balance()) == -1;

                            if (isCancelForVolume || isCancelForFrozen || isErrorData) {
                                logger.warn("checkVolume:" + checkVolume + "  checkCurrency:" + checkCurrency
                                        + " checkFrozen:" + checkFrozen + " remainFrozen:" + remainFrozen
                                        + " Frozen_balance:" + account.getFrozen_balance());
                                logger.warn(" ===========================> order Cancel!" + oppositeOrder.toString());
                                // 更新数据库
                                this.cancelOrder(oppositeOrder);
                                // 更新缓存
                                this.cancelOrderByCancel(
                                        oppositeOutterKeyName + "_" + oppositeOrder.getPrice().doubleValue(),
                                        oppositeOutterKeyName, oppositeOrder);
                                continue;
                            }

                            // 开始交易
                            if (takerVolume.compareTo(makerVolume) == 0) { // 双方的数量刚好相等
                                // 设置吃单方本次成交数量
                                if (isMarketBuyOrder) {
                                    // 市价买单，还是存计价货币
                                    order.setCur_done_volume(takerVolume.multiply(oppositeOrder.getPrice()));
                                    order.setDone_volume(order.getDone_volume().add(order.getCur_done_volume()));
                                } else {
                                    // 市价卖单，存数量。限价买单，存数量。限价卖单，存数量
                                    order.setCur_done_volume(takerVolume);
                                    order.setDone_volume(order.getDone_volume().add(order.getCur_done_volume()));
                                }
                                // 设置吃单方本次解冻量
                                if ("buy".equals(order.getO_type())) { // 买单，解冻的都是金额
                                    order.setCur_unfrozen(takerVolume.multiply(oppositeOrder.getPrice()));
                                    generateUpsDown(order,oppositeOrder,"buy");
                                } else { // 卖单，解冻的都是数量
                                    order.setCur_unfrozen(takerVolume);
                                    generateUpsDown(order,oppositeOrder,"sell");
                                }

                                // 设置吃单为已成交
                                order.setO_status("done");

                                /*
                                 * if (isMarketBuyOrder) {
                                 *
                                 * } else { // 限价买单全部成交时，吃单因为可能多次成交，而当初冻结的价格是以他的报价乘以数量进行冻结的，因此这里需要给他还原冻结 //
                                 * 设置解冻量 if ("buy".equals(order.getO_type())) { // 买单
                                 * order.setUnfrozen(order.getUnfrozen().add(takerVolume.multiply(oppositeOrder.
                                 * getPrice()))); } else { //卖单解冻所有，因为冻结的是数量
                                 *
                                 * } }
                                 */
                                // 全部成交不设置unfrozen
                                this.changeOrder(order);

                                // 设置挂单方已成交数量
                                oppositeOrder.setCur_done_volume(takerVolume);
                                oppositeOrder.setDone_volume(oppositeOrder.getDone_volume().add(takerVolume));
                                // 设置挂单方本次解冻量
                                if ("buy".equals(oppositeOrder.getO_type())) { // 买单，解冻的都是金额
                                    oppositeOrder.setCur_unfrozen(takerVolume.multiply(oppositeOrder.getPrice()));
                                } else { // 卖单，解冻的都是数量
                                    oppositeOrder.setCur_unfrozen(takerVolume);
                                }
                                // 设置挂单为已成交
                                oppositeOrder.setO_status("done");
                                // 全部成交不设置unfrozen
                                this.changeOrder(oppositeOrder);

                                // 买卖双方全部成交，以挂单方价格（均以挂单方价格）成交
                                if ("buy".equals(order.getO_type())) {
                                    this.addTrade(order, oppositeOrder, takerVolume, "buy", cp, true, true,
                                            order.getTradeCommission(), oppositeOrder.getTradeCommission());
                                    generateUpsDown(order,oppositeOrder, "buy");
                                } else {
                                    this.addTrade(oppositeOrder, order, takerVolume, "sell", cp, true, true,
                                            oppositeOrder.getTradeCommission(), order.getTradeCommission());
                                    generateUpsDown(order,oppositeOrder, "sell");
                                }

                                // 将挂单移除出Redis队列
                                JedisUtil.getInstance().zrem(
                                        (oppositeOutterKeyName + "_" + outterTuple.getScore()).getBytes(), innerByte);
                                // 刷新外层Redis队列数量
                                this.refreshPriceVolumeSet(oppositeOutterKeyName, oppositeOrder.getPrice(),
                                        takerVolume.negate()); // 这里是减少，negate表示负数

                                // 退出循环吃单
                                isDeal = true;
                                break;

                            } else if (takerVolume.compareTo(makerVolume) == 1) { // 吃单方数量大
                                // 市价买单，还是存计价货币
                                if (isMarketBuyOrder) {
                                    order.setCur_done_volume(makerVolume.multiply(oppositeOrder.getPrice()));
                                    order.setDone_volume(order.getDone_volume().add(order.getCur_done_volume()));
                                } else {
                                    order.setCur_done_volume(makerVolume);
                                    order.setDone_volume(order.getDone_volume().add(makerVolume)); // 这里修改是因为已成交增加
                                }
                                // 设置吃单方本次解冻量和总解冻量（设置总解冻量的原因是要将这个order存入redis）
                                if ("buy".equals(order.getO_type())) { // 买单，解冻的都是金额
                                    order.setCur_unfrozen(makerVolume.multiply(oppositeOrder.getPrice()));
                                    order.setUnfrozen(order.getUnfrozen().add(order.getCur_unfrozen()));
                                } else { // 卖单，解冻的都是数量
                                    order.setCur_unfrozen(makerVolume);
                                    order.setUnfrozen(order.getUnfrozen().add(makerVolume));
                                }
                                // 设置吃单方为部分成交
                                order.setO_status("partial-done");
                                // 锁内执行
                                this.changeOrder(order);

                                // 设置挂单方已成交数量
                                oppositeOrder.setCur_done_volume(makerVolume);
                                oppositeOrder.setDone_volume(oppositeOrder.getDone_volume().add(makerVolume));
                                // 设置挂单方本次解冻量和总解冻量
                                if ("buy".equals(oppositeOrder.getO_type())) { // 买单，解冻的都是金额
                                    oppositeOrder.setCur_unfrozen(makerVolume.multiply(oppositeOrder.getPrice()));
                                    // oppositeOrder.setUnfrozen(oppositeOrder.getUnfrozen().add(oppositeOrder.getCur_unfrozen()));
                                } else { // 卖单，解冻的都是数量
                                    oppositeOrder.setCur_unfrozen(makerVolume);
                                    // oppositeOrder.setUnfrozen(oppositeOrder.getUnfrozen().add(makerVolume));
                                }
                                // 设置挂单为全部成交
                                oppositeOrder.setO_status("done");
                                // 全部成交不设置unfrozen
                                this.changeOrder(oppositeOrder);

                                // 吃单方部分成交，挂单方全部成交
                                if ("buy".equals(order.getO_type())) {
                                    this.addTrade(order, oppositeOrder, makerVolume, "buy", cp, false, true,
                                            order.getTradeCommission(), oppositeOrder.getTradeCommission());
                                    generateUpsDown(order,oppositeOrder, "buy");
                                } else {
                                    this.addTrade(oppositeOrder, order, makerVolume, "sell", cp, true, false,
                                            oppositeOrder.getTradeCommission(), order.getTradeCommission());
                                    generateUpsDown(order,oppositeOrder, "sell");
                                }

                                // 将挂单移除出Redis队列
                                JedisUtil.getInstance().zrem(
                                        (oppositeOutterKeyName + "_" + outterTuple.getScore()).getBytes(), innerByte);
                                // 刷新外层队列数量
                                this.refreshPriceVolumeSet(oppositeOutterKeyName, oppositeOrder.getPrice(),
                                        makerVolume.negate()); // 这里是减少，negate表示负数

                                // 买单继续吃单
                                continue;

                            } else if (takerVolume.compareTo(makerVolume) == -1) { // 挂单方数量大
                                // 设置吃单方本次成交数量
                                // 市价买单，还是存计价货币
                                if (isMarketBuyOrder) {
                                    order.setCur_done_volume(takerVolume.multiply(oppositeOrder.getPrice()));
                                    order.setDone_volume(order.getDone_volume().add(order.getCur_done_volume()));
                                } else {
                                    order.setCur_done_volume(takerVolume);
                                    order.setDone_volume(order.getDone_volume().add(order.getCur_done_volume()));
                                }
                                // 设置吃单方本次解冻量和总解冻量
                                if ("buy".equals(order.getO_type())) { // 买单，解冻的都是金额
                                    order.setCur_unfrozen(takerVolume.multiply(oppositeOrder.getPrice()));
                                    // order.setUnfrozen(order.getUnfrozen().add(order.getCur_unfrozen()));
                                } else { // 卖单，解冻的都是数量
                                    order.setCur_unfrozen(takerVolume);
                                    // order.setUnfrozen(order.getUnfrozen().add(takerVolume));
                                }

                                order.setO_status("done");
                                // 全部成交不设置unfrozen
                                this.changeOrder(order);

                                // 设置挂单方已成交数量
                                oppositeOrder.setCur_done_volume(takerVolume);
                                oppositeOrder.setDone_volume(oppositeOrder.getDone_volume().add(takerVolume)); // 这里修改是因为要重新进入Redis队列
                                // 设置挂单方本次解冻量和总解冻量
                                if ("buy".equals(oppositeOrder.getO_type())) { // 买单，解冻的都是金额
                                    oppositeOrder.setCur_unfrozen(takerVolume.multiply(oppositeOrder.getPrice()));
                                    oppositeOrder.setUnfrozen(
                                            oppositeOrder.getUnfrozen().add(oppositeOrder.getCur_unfrozen()));
                                } else { // 卖单，解冻的都是数量
                                    oppositeOrder.setCur_unfrozen(takerVolume);
                                    oppositeOrder.setUnfrozen(oppositeOrder.getUnfrozen().add(takerVolume));
                                }

                                // 设置挂单为部分成交
                                oppositeOrder.setO_status("partial-done");
                                this.changeOrder(oppositeOrder);

                                // 吃单方全部成交，以挂单方价格成交
                                if ("buy".equals(order.getO_type())) {
                                    this.addTrade(order, oppositeOrder, takerVolume, "buy", cp, true, false,
                                            order.getTradeCommission(), oppositeOrder.getTradeCommission());
                                    generateUpsDown(order,oppositeOrder, "buy");
                                } else {
                                    this.addTrade(oppositeOrder, order, takerVolume, "sell", cp, false, true,
                                            oppositeOrder.getTradeCommission(), order.getTradeCommission());
                                    generateUpsDown(order,oppositeOrder, "sell");
                                }

                                // 将挂单移除出Redis队列
                                JedisUtil.getInstance().zrem(
                                        (oppositeOutterKeyName + "_" + outterTuple.getScore()).getBytes(), innerByte);
                                // 将挂单重新进入Redis队列
                                JedisUtil.getInstance().zadd(
                                        (oppositeOutterKeyName + "_" + outterTuple.getScore()).getBytes(),
                                        oppositeOrder.getCreate_time_unix(), SerializeUtil.serialize(oppositeOrder));
                                // 刷新外层队列数量
                                this.refreshPriceVolumeSet(oppositeOutterKeyName, oppositeOrder.getPrice(),
                                        takerVolume.negate()); // 这里是减少，negate表示负数

                                // 退出循环吃单
                                isDeal = true;
                                break;
                            }
                        } catch (Exception e) {
                            logger.warn("order trade Exception! order:" + order.toString() + " \n oppositeOrder:"
                                    + oppositeOrder.toString(), e);
                            throw new BusinessException(-1, ErrorInfoEnum.LANG_ORDER_TRADE_ERROR.getErrorENMsg());
                        } finally {
                            boolean isRelease = JedisUtil.getInstance().releaseLock(lockKey,
                                    IPAddressPortUtil.IP_ADDRESS);
                            if (!isRelease) {
                                long end = System.currentTimeMillis();
                                logger.warn("订单交易锁解锁失败。key:{}  耗时:{} ms", lockKey, end - localStart);
                            }
                        }
                    } else {
                        // 如果没拿到锁说明这张挂单正在交易，跳过这张挂单继续交易下张挂单的交易
                        logger.warn("订单({}) 找到的挂单订单({}) 正在被交易，跳过到下一张挂单订单！", order.getId(), lockKey);
                    }
                }
            } else { // 买单的买价小于挂单的卖价，或者卖单的卖价大于挂单的买价，直接退出循环
                break;
            }
        }

        // 挂单
        entryOrders(isUserCancel, selfOutterKeyName, oppositeOutterKeyName, order);
    }

    private void entryOrders(boolean isUserCancel, String selfOutterKeyName, String oppositeOutterKeyName,
                             Order order) {
        // 全局锁 entryOrdersIsLock
        String entryOrdersLockKey = ORDER_TRADE_ENTRYORDER_REDIS_LOCK_PRE + order.getBase_currency() + "_" + order.getPrice().stripTrailingZeros().toPlainString();
        boolean entryOrdersIsLock = JedisUtil.getInstance().getLockRetry(entryOrdersLockKey,
                IPAddressPortUtil.IP_ADDRESS, ORDER_TRADE_ENTRYORDER_REDIS_LOCK_EXPIRE_TIME, 50, 100);
        try {
            if (entryOrdersIsLock) {

                // 先在Redis中找到这个订单
                if(selfOutterKeyName.equals(oppositeOutterKeyName)) {
                    oppositeInnerSet = null;
                }else {
                    oppositeInnerSet = JedisUtil.getInstance().zrangeByScore(
                            (oppositeOutterKeyName + "_" + order.getPrice().doubleValue()).getBytes(),
                            order.getCreate_time_unix(), order.getCreate_time_unix());
                }

                if (null != oppositeInnerSet && oppositeInnerSet.size() > 0 && order.getVolume().compareTo(order.getDone_volume()) > 0) {
                    JedisUtil.getInstance().releaseLock(entryOrdersLockKey,
                            IPAddressPortUtil.IP_ADDRESS);
                    // 交易
                    trade(isUserCancel, selfOutterKeyName, oppositeOutterKeyName, order);
                }else {
                    // 吃单没有成交，或者部分成交时，即下单量大于成交量，已成交的不用在此处处理
                    if (order.getVolume().compareTo(order.getDone_volume()) > 0) {
                        // 如果没有成交，并且是限价单，则存入挂单(买单和卖单分开)队列
                        // key的格式为btccny_sell、btccny_buy
                        if ("limit".equals(order.getO_price_type())) {
                            // 将吃单方挂入挂单中
                            JedisUtil.getInstance().zadd(
                                    (selfOutterKeyName + "_" + order.getPrice().doubleValue()).getBytes(),
                                    order.getCreate_time_unix(), SerializeUtil.serialize(order));

                            // 刷新外层队列数量
                            this.refreshPriceVolumeSet(selfOutterKeyName, order.getPrice(),
                                    order.getVolume().subtract(order.getDone_volume())); // 这里是增加数量
                        } else { // 如果是市价单，则直接结束订单

                            // 市价单因为不挂单，所以在没有完成的时候，需要还原冻结量
                            // 这里判断 (!"done") 是没问题的。因为如果done了，在addTrade里面就会给他还原冻结
                            if (!"done".equals(order.getO_status())) {
                                if ("sell".equals(order.getO_type())) {
                                    memberService.accountProc(order.getFrozen().subtract(order.getUnfrozen()),
                                            order.getBase_currency(), order.getMember_id(), 4, OptSourceEnum.ZZEX);
                                } else {
                                    memberService.accountProc(order.getFrozen().subtract(order.getUnfrozen()),
                                            order.getQuote_currency(), order.getMember_id(), 4, OptSourceEnum.ZZEX);
                                }
                            }

                            if (order.getDone_volume().compareTo(BigDecimal.ZERO) == 0) { // 一个都没成交，为取消
                                order.setCur_done_volume(BigDecimal.ZERO);
                                order.setCur_unfrozen(order.getFrozen().subtract(order.getUnfrozen()));
                                order.setO_status("canceled");
                                order.setCancel_time(HelpUtils.formatDate8(new Date()));
                                this.changeOrder(order);

                            } else { // 小于要求的量，为部分完成，需要设置为partial-canceled
                                order.setCur_done_volume(BigDecimal.ZERO);
                                order.setCur_unfrozen(order.getFrozen().subtract(order.getUnfrozen()));
                                order.setO_status("partial-canceled");
                                order.setCancel_time(HelpUtils.formatDate8(new Date()));
                                this.changeOrder(order);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.warn("Entry entryOrders Exception! order:{}", order.toString(), e);
            throw new BusinessException(-1, ErrorInfoEnum.LANG_ORDER_TRADE_ERROR.getErrorENMsg());
        } finally {
            JedisUtil.getInstance().releaseLock(entryOrdersLockKey,
                    IPAddressPortUtil.IP_ADDRESS);
        }

    }

    // 用户取消订单
    private void cancelOrder(Order order) {
        Order tempOrder = exService.getOrder(order);
        if (BeanUtil.isEmpty(tempOrder)) {
            logger.warn("cancel order Exception! order:{}", order.toString());
            throw new BusinessException(-1, ErrorInfoEnum.LANG_CANCEL_ORDER_ERROR.getErrorENMsg());
        }
        if ("watting".equals(tempOrder.getO_status()) || "partial-done".equals(tempOrder.getO_status())) {
            // 只有未完成、未取消的才处理 // 修改订单状态
            String oStatus = "";
            if ("partial-done".equals(tempOrder.getO_status())) {
                oStatus = "partial-canceled";
            } else { // 只有可能是partial-done 和 watting，因此这里就是watting
                oStatus = "canceled";
            }
            tempOrder.setO_status(oStatus);
            tempOrder.setCur_done_volume(BigDecimal.ZERO);
            tempOrder.setCur_unfrozen(tempOrder.getFrozen().subtract(tempOrder.getUnfrozen()));
            tempOrder.setTable_name(order.getTable_name());
            tempOrder.setCancel_time(HelpUtils.formatDate8(new Date()));
            this.changeOrder(tempOrder);

            // 退回解冻金额
            if ("buy".equals(order.getO_type())) {
                Account account = memberService.getAccount(HelpUtils.newHashMap("currency",
                        tempOrder.getQuote_currency(), "member_id", tempOrder.getMember_id()));
                if (tempOrder.getCur_unfrozen().compareTo(account.getFrozen_balance()) > 0) {
                    tempOrder.setCur_unfrozen(account.getFrozen_balance());
                }
                memberService.accountProc(tempOrder.getCur_unfrozen(), tempOrder.getQuote_currency(),
                        tempOrder.getMember_id(), 4, OptSourceEnum.ZZEX);
            } else {
                Account account = memberService.getAccount(HelpUtils.newHashMap("currency",
                        tempOrder.getBase_currency(), "member_id", tempOrder.getMember_id()));
                if (tempOrder.getCur_unfrozen().compareTo(account.getFrozen_balance()) > 0) {
                    tempOrder.setCur_unfrozen(account.getFrozen_balance());
                }
                memberService.accountProc(tempOrder.getCur_unfrozen(), tempOrder.getBase_currency(),
                        tempOrder.getMember_id(), 4, OptSourceEnum.ZZEX);
            }
        }
    }

    private void cancelOrderByCancel(String innerKey, String outerKey, Order order) {
        oppositeInnerSet = JedisUtil.getInstance().zrangeByScore(innerKey.getBytes(), order.getCreate_time_unix(),
                order.getCreate_time_unix());
        Iterator<byte[]> innerIter = oppositeInnerSet.iterator();
        while (innerIter.hasNext()) {
            byte[] innerByte = innerIter.next();
            Order oppositeOrder = (Order) SerializeUtil.deserialize(innerByte);
            if (oppositeOrder.getId().equals(order.getId())) {
                // 将挂单移除出Redis队列
                JedisUtil.getInstance().zrem(innerKey.getBytes(), innerByte);
                // 刷新外层Redis队列数量
                BigDecimal negateVolume = oppositeOrder.getVolume().subtract(oppositeOrder.getDone_volume());
                this.refreshPriceVolumeSet(outerKey, oppositeOrder.getPrice(), negateVolume.negate()); // 这里是减少，negate表示负数
                return;
            }
        }
    }

    /**
     * 刷新外层队列
     * <p>
     * 外层队列格式： 1、首先每个交易对两个key，名字就是 ltcbtc_sel 和 ltcbtc_buy 2、key下面存储的是
     * 价格和价格数量数组的队列，以价格作为Key，价格和数量的数组作为Value
     *
     * @param keyName
     * @param price
     * @param addVolume 本次新增的数量，可以为负数，比如成交了就是负数
     */
    private void refreshPriceVolumeSet(String keyName, BigDecimal price, BigDecimal addVolume) {
        // 获得Redis中已经存在的指定价格的PriceVolume对象
        Set<byte[]> alreadyPriceVolumeBytes = JedisUtil.getInstance().zrangeByScore((keyName).getBytes(),
                price.doubleValue(), price.doubleValue());
        BigDecimal[] priceVolume = new BigDecimal[2];
        priceVolume[0] = price;
        priceVolume[1] = BigDecimal.ZERO;

        // 如果该价格已经有，就要删除（当然，先存下来原来的，即priceVolume）
        if (alreadyPriceVolumeBytes.size() > 0) {
            byte[] oldPriceVolumeByte = alreadyPriceVolumeBytes.iterator().next();
            priceVolume = (BigDecimal[]) SerializeUtil.deserialize(oldPriceVolumeByte);
            // 老的要删除，下面放新的
            JedisUtil.getInstance().zrem((keyName).getBytes(), oldPriceVolumeByte);
        }
        // 如果新的数量大于0，才塞进去，因为addVolume可能为负数，因此这里可能等于0，等于0时，上面删了就不用再存了。
        if (addVolume.add(priceVolume[1]).compareTo(BigDecimal.ZERO) > 0) {
            priceVolume[1] = addVolume.add(priceVolume[1]);
            JedisUtil.getInstance().zadd((keyName).getBytes(), price.doubleValue(),
                    SerializeUtil.serialize(priceVolume));
        }
    }

    /**
     * 新增交易数据，注意，交易数据一次肯定是生成一对，即买方和卖方都需要
     *
     * @param buyOrder
     * @param sellOrder
     * @param curDoneVolume
     * @param taker           吃单方，分别取值sell和buy
     * @param cp
     * @param isBuyWholeDone  买单是否全部成交
     * @param isSellWholeDone 卖单是否全部成交
     */
    private void addTrade(Order buyOrder, Order sellOrder, BigDecimal curDoneVolume, String taker, CurrencyPair cp,
                          boolean isBuyWholeDone, boolean isSellWholeDone, Double buyTradeCommission, Double sellTradeCommission) {
        // 先处理卖单
        Trade sellTrade = new Trade();
        sellTrade.setVolume(curDoneVolume);
        sellTrade.setBase_currency(sellOrder.getBase_currency());
        sellTrade.setDone_time(HelpUtils.formatDate8(new Date()));
        sellTrade.setPrice("sell".equals(taker) ? buyOrder.getPrice() : sellOrder.getPrice()); // 成交价取挂单方的价格

        // 手续费，卖方为计价货币
        sellTrade.setFee(
                "sell".equals(taker) ? cp.getTaker_fee().multiply(sellTrade.getPrice()).multiply(sellTrade.getVolume())
                        : cp.getMaker_fee().multiply(sellTrade.getPrice()).multiply(sellTrade.getVolume()));

        // 手续费比例
        if (null == sellTradeCommission) {
            sellTradeCommission = 1.0;
        }
        sellTrade.setFee(sellTrade.getFee().multiply(BigDecimal.valueOf(sellTradeCommission)));

        sellTrade.setFee_currency(sellOrder.getQuote_currency());
        sellTrade.setMember_id(sellOrder.getMember_id());
        sellTrade.setO_id(sellOrder.getId());
        sellTrade.setOpposite_o_id(buyOrder.getId());
        sellTrade.setPair_id(cp.getId());
        sellTrade.setQuote_currency(sellOrder.getQuote_currency());
        sellTrade.setT_type("sell");
        sellTrade.setTaker("sell".equals(taker) ? "self" : "opposite");
        sellTrade.setTable_name(sellOrder.getTable_name());
        sellTrade.setToken_id(sellOrder.getToken_id());

        orderMapper.addTrade(sellTrade);
        // 插入返佣明细

        // 解冻基础货币，卖单冻结的都是数量，不管他是市价还是限价
        if (isSellWholeDone) { // 全部成交
            memberService.accountProc(sellOrder.getFrozen().subtract(sellOrder.getUnfrozen()),
                    sellOrder.getBase_currency(), sellOrder.getMember_id(), 2, OptSourceEnum.ZZEX);
        } else { // 部分成交
            memberService.accountProc(curDoneVolume, sellOrder.getBase_currency(), sellOrder.getMember_id(), 2,
                    OptSourceEnum.ZZEX);
        }
        // 增加计价货币
        memberService.accountProc(curDoneVolume.multiply(sellTrade.getPrice()).subtract(sellTrade.getFee()),
                sellOrder.getQuote_currency(), sellOrder.getMember_id(), 3, OptSourceEnum.ZZEX);

        // 然后处理买单
        Trade buyTrade = new Trade();
        buyTrade.setVolume(curDoneVolume);
        buyTrade.setBase_currency(buyOrder.getBase_currency());
        buyTrade.setDone_time(HelpUtils.formatDate8(new Date()));
        buyTrade.setPrice("buy".equals(taker) ? sellOrder.getPrice() : buyOrder.getPrice()); // 成交价取挂单方的价格

        // 手续费，买方为基础货币
        buyTrade.setFee("buy".equals(taker) ? cp.getTaker_fee().multiply(buyTrade.getVolume())
                : cp.getMaker_fee().multiply(buyTrade.getVolume()));

        // 手续费比例
        if (null == buyTradeCommission) {
            buyTradeCommission = 1.0;
        }
        buyTrade.setFee(buyTrade.getFee().multiply(BigDecimal.valueOf(buyTradeCommission)));

        buyTrade.setFee_currency(buyOrder.getBase_currency());
        buyTrade.setMember_id(buyOrder.getMember_id());
        buyTrade.setO_id(buyOrder.getId());
        buyTrade.setOpposite_o_id(sellOrder.getId());
        buyTrade.setPair_id(cp.getId());
        buyTrade.setQuote_currency(buyOrder.getQuote_currency());
        buyTrade.setT_type("buy");
        buyTrade.setTaker("buy".equals(taker) ? "self" : "opposite");
        buyTrade.setTable_name(buyOrder.getTable_name());
        buyTrade.setToken_id(buyOrder.getToken_id());
        orderMapper.addTrade(buyTrade);

        // 解冻计价货币
        if (isBuyWholeDone) { // 全部成交，要解冻原冻结的所有货币
            // 先解冻本次成交的数量
            BigDecimal curUnFrozen = curDoneVolume.multiply(buyTrade.getPrice());
            memberService.accountProc(curUnFrozen, buyOrder.getQuote_currency(), buyOrder.getMember_id(), 2,
                    OptSourceEnum.ZZEX);
            // 因为买单全部成交，下买单时，因为是按报价冻结的，而实际成交价可能低于报价，此时，需要将差价退回到买单账户
            memberService.accountProc(buyOrder.getFrozen().subtract(buyOrder.getUnfrozen()).subtract(curUnFrozen),
                    buyOrder.getQuote_currency(), buyOrder.getMember_id(), 4, OptSourceEnum.ZZEX);

            // 如果吃单方是市价买单，因为挂单不可能是市价单，因此这里只要判断buyOrder是不是市价即可
            /*
             * if ("market".equals(buyOrder.getO_price_type())) { if
             * (buyOrder.getVolume().subtract(buyOrder.getDone_volume()).compareTo(
             * BigDecimal.ZERO) == 1) { // 这种情况需退款，退款到计价货币账户
             * memberService.accountProc(buyOrder.getVolume().subtract(buyOrder.
             * getDone_volume()), buyOrder.getQuote_currency(), buyOrder.getMember_id(), 3);
             * } }
             */

        } else { // 部分成交

            /*
             * if ("market".equals(buyOrder.getO_price_type())) { //市价买单已经是金额，所以不用乘以价格了。
             * memberService.accountProc(buyOrder.getCur_done_volume(),
             * buyOrder.getQuote_currency(), buyOrder.getMember_id(), 2);x } else {
             * memberService.accountProc(curDoneVolume.multiply(buyTrade.getPrice()),
             * buyOrder.getQuote_currency(), buyOrder.getMember_id(), 2); }
             */
            memberService.accountProc(curDoneVolume.multiply(buyTrade.getPrice()), buyOrder.getQuote_currency(),
                    buyOrder.getMember_id(), 2, OptSourceEnum.ZZEX);

        }
        // 增加基础货币
        memberService.accountProc(curDoneVolume.subtract(buyTrade.getFee()), buyOrder.getBase_currency(),
                buyOrder.getMember_id(), 3, OptSourceEnum.ZZEX);

        // ----------------成交数据存Redis----------------
        // 时间，价格，数量，吃单方


        TradeDto tradeDto = new TradeDto();
        tradeDto.id = buyTrade.getId();
        tradeDto.timestamp = HelpUtils.getNowTimeStampMillisecond();
        tradeDto.price = buyTrade.getPrice();
        tradeDto.volume = curDoneVolume;
        tradeDto.taker = taker;

        //这些属性只是在存Es的时候会用到
        tradeDto.sell_Id = sellTrade.getId();
        tradeDto.sell_Taker = sellTrade.getT_type();
        tradeDto.buy_Id = buyTrade.getId();
        tradeDto.buy_Taker = buyTrade.getT_type();


        tradeToRedis(tradeDto, buyOrder.getTable_name(), true);
    }

    /**
     * 缓存成交数据到Redis
     *
     * @param tradeDto
     * @param symbol
     * @param needIncrLastGenKLen 因为涉及刷量，这里表示是否生成 symbol + "_last_gen_k_len"
     */
    public void tradeToRedis(TradeDto tradeDto, String symbol, boolean needIncrLastGenKLen) {
        List<byte[]> lst = JedisUtil.getInstance().lrange(symbol + "_trade", 0, 0);
        if (null != lst && lst.size() == 1) {
            TradeDto latestTDto = (TradeDto) SerializeUtil.deserialize(lst.get(0));
            if (null != latestTDto.price) {
                if (latestTDto.price.compareTo(tradeDto.price) == 1) {
                    tradeDto.upOrDown = -1;
                } else if (latestTDto.price.compareTo(tradeDto.price) == -1) {
                    tradeDto.upOrDown = 1;
                } else { // 如果相等，取上次的
                    tradeDto.upOrDown = latestTDto.upOrDown;
                }
            }
        } else { // 第一次成交，肯定是上涨
            tradeDto.upOrDown = 1;
        }
        //===
        //===发送消息将trade数据存放到elasticSearch中===
//        sendMessagePool.execute(new SendMessage(symbol,tradeDto));
        //=============================================
        JedisUtil.getInstance().lpush(symbol + "_trade", tradeDto);

        // 最大保留1500条
        JedisUtil.getInstance().ltrim(symbol + "_trade", 0, 1500);

        if (needIncrLastGenKLen) {
            JedisUtil.getInstance().incr(symbol + "_last_gen_k_len");
        }

    }
    /**
     * 涨跌幅限制判断，以挂单方价格作为标准
     * @param buyOrder
     * @param sellOrder
     * @param taker
     */
    public void generateUpsDown(Order buyOrder, Order sellOrder,String taker){
        //校验是否限制涨跌幅
        CurrencyPair currencyPair = HelpUtils.getCurrencyPairMap().get(buyOrder.getTable_name().toUpperCase());
        if (currencyPair.getIs_ups_downs_limit().equals(1)){
            //根据对应交易对查询是否有收盘价，没有则为第一单，设置收盘价及最高最低价
            BigDecimal price = "sell".equals(taker) ? buyOrder.getPrice() : sellOrder.getPrice();

            if (JedisUtil.getInstance().get(currencyPair.getKey_name().toUpperCase()+"_close_price",true)==null){
                JedisUtil.getInstance().set(currencyPair.getKey_name().toUpperCase()+"_close_price",price+"",true);
                BigDecimal tempPrice = price.multiply(currencyPair.getUps_downs_limit()).divide(new BigDecimal(100)).setScale(currencyPair.getPrice_precision(), BigDecimal.ROUND_DOWN);
                currencyPair.setClosePrice(price);//收盘价
                currencyPair.setHighPrice(price.add(tempPrice));//最高价
                currencyPair.setLowPrice(price.subtract(tempPrice));//最低价
                HelpUtils.getCurrencyPairMap().put(buyOrder.getTable_name().toUpperCase(),currencyPair);
                logger.info("generate "+buyOrder.getTable_name().toUpperCase()+" ups down ," +
                        "highPrice:"+currencyPair.getHighPrice()+
                        "lowPrice:"+currencyPair.getLowPrice()+
                        "closePrice:"+currencyPair.getClosePrice());
            }
        }
    }


    private void changeOrder(Order order) {
        orderMapper.changeOrder(order);
    }

	public BigDecimal getPlatformFlows(CurrencyPair currencyPair) {
		return orderMapper.getPlatformFlows(HelpUtils.newHashMap("table_name", currencyPair.getKey_name().toLowerCase(), "baseCurrency", currencyPair.getBase_currency()));
	}
}
