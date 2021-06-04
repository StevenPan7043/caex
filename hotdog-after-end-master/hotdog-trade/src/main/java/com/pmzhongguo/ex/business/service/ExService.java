package com.pmzhongguo.ex.business.service;


import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import com.pmzhongguo.ex.business.DisruptorHandlerException;
import com.pmzhongguo.ex.business.dto.OrderRespDto;
import com.pmzhongguo.ex.business.dto.ReturnCommiCurrAmountDto;
import com.pmzhongguo.ex.business.dto.TradeRespDto;
import com.pmzhongguo.ex.business.entity.CoinWithdraw;
import com.pmzhongguo.ex.business.entity.Order;
import com.pmzhongguo.ex.business.entity.Trade;
import com.pmzhongguo.ex.business.mapper.MemberMapper;
import com.pmzhongguo.ex.business.mapper.OTCMapper;
import com.pmzhongguo.ex.business.mapper.OrderMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ex.core.web.PeppaexConstants;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.qiniu.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ExService extends BaseServiceSupport {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private OTCMapper otcMapper;

    // 对同一个用户，一个时刻只能一个线程访问，因此利用Guava进行变量锁
    private Interner<String> memberIdPool = Interners.newWeakInterner();

    private static final int RING_SIZE = 1024 * 1024;
    private RingBuffer<Order> ringBuffer = null;
    private Publisher publisher = null;

    public void initDisruptor() {
        Disruptor<Order> disruptor = new Disruptor<Order>(Order.FACTORY, RING_SIZE, DaemonThreadFactory.INSTANCE,
                ProducerType.MULTI, new BlockingWaitStrategy());
        disruptor.setDefaultExceptionHandler(new DisruptorHandlerException());
        // disruptor.handleEventsWith(new OrderConsumer()).then(new TradeConsumer());
        // disruptor.handleEventsWith(new TradeConsumer()).then(new StaticConsumer());
        disruptor.handleEventsWith(new TradeConsumer());
        ringBuffer = disruptor.getRingBuffer();
        publisher = new Publisher();
        disruptor.start();
    }

    public Order getOrder(Order order) {
        if (HelpUtils.isEmpty(order.getTable_name())) {
            order.setTable_name(order.getBase_currency().toLowerCase() + order.getQuote_currency().toLowerCase());
        }
        return orderMapper.getOrder(order);
    }

    //public Long addOrder1(OrderCreateDto orderDto, CurrencyPair currencyPair, String operIp, Integer tokenId,
    //		Integer memberId, Double trade_commission) throws Exception {
    //	return 0L;
    //}

    public synchronized Long allMemberFrozenAndProcOutter(BigDecimal frozen, String frozenCurrency, Integer memberId,
                                                          String type, Order order, CoinWithdraw coinWithdraw) {
        Long id = allMemberFrozenAndProc(frozen, frozenCurrency, memberId, type, order, coinWithdraw);
        return id;
    }

    /**
     * 所有涉及冻结用户资产的方法，都需要走这个方法，进行同步，后续处理也放到同步块中，是因为整个事物没处理完时，再进行冻结，取到的余额还是原来冻结前的余额
     *
     * @param frozen
     * @param frozenCurrency
     * @param memberId
     * @param type
     * @param order
     * @param coinWithdraw
     * @return
     */
    public synchronized Long allMemberFrozenAndProc(BigDecimal frozen, String frozenCurrency, Integer memberId,
                                                    String type, Order order, CoinWithdraw coinWithdraw) {
        // 防止同一个用户同时进入，导致没有判断余额而同时下单的的情况
        // synchronized (memberIdPool.intern(memberId + "_" + frozenCurrency)) {
        if ("order".equals(type)) {
            // 冻结
            memberService.accountProc(frozen, frozenCurrency, memberId, 1, OptSourceEnum.ZZEX);
            // 创建订单
            try {
                //再做一次order Volume检查
                if (order.getVolume().compareTo(BigDecimal.ZERO) <= 0) {
                    logger.warn("LANG_ILLEGAL_VOLUME !!" + order.toString());
                    throw new BusinessException(-1, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg());
                }
                orderMapper.addOrder(order);
                order.setO_status("watting");
            } catch (Exception e) {
                logger.warn("Order created Exception. " + order.toString(), e);
                throw new BusinessException(-1, ErrorInfoEnum.LANG_CREATE_ORDER_FAIL.getErrorENMsg());
            }
            tradeService.trade(order, false);
            return order.getId();
        } else if ("cancel".equals(type)) {
            tradeService.trade(order, true);
            return 0L;
        } else if ("coinWithdraw".equals(type)) {
            // 冻结
            memberService.accountProc(frozen, frozenCurrency, memberId, 1, OptSourceEnum.ZZEX);
            if (BeanUtil.isEmpty(coinWithdraw.getTransfer_no())) {
                coinWithdraw.setTransfer_no(0);
            }
            memberMapper.addCoinWithdraw(coinWithdraw);
            // 安全起见用户提币需要在缓存中放置一份地址，然后和数据库的对比，无误，才能打币
            String key = PeppaexConstants.WITHDRAW_ADDR_CACHE_KEY + coinWithdraw.getId();
            JedisUtil.getInstance()
                    .set(key, coinWithdraw.getMember_coin_addr(), true);
            Object o = JedisUtil.getInstance().get(key, true);
            if (o == null) {
                throw new BusinessException(Resp.FAIL, ErrorInfoEnum.CURRENCY_WITHDRAWAL_EXCEPTION.getErrorENMsg());
            }
            return coinWithdraw.getId();
        } else {
            return 0L;
        }
    }

    /**
     * 获取一笔交易详情
     *
     * @param params opposite_o_id,taker,table_name
     * @return
     */
    public Trade getTradeDetail(Map<String, Object> params) {
        return orderMapper.getTradeDetail(params);

    }


    public static class Publisher implements EventTranslatorOneArg<Order, Order> {
        @Override
        public void translateTo(Order event, long sequence, Order order) {
            event.setId(order.getId());
            event.setToken_id(order.getToken_id());
            event.setMember_id(order.getMember_id());
            event.setPair_id(order.getPair_id());
            event.setBase_currency(order.getBase_currency());
            event.setQuote_currency(order.getQuote_currency());
            event.setO_no(order.getO_no());
            event.setO_type(order.getO_type());
            event.setO_price_type(order.getO_price_type());
            event.setPrice(order.getPrice());
            event.setVolume(order.getVolume());
            event.setCreate_time_unix(order.getCreate_time_unix());
            event.setTable_name(order.getTable_name());
            event.setDone_volume(BigDecimal.ZERO);
            event.setCur_done_volume(BigDecimal.ZERO);
            event.setFrozen(order.getFrozen());
            event.setUnfrozen(BigDecimal.ZERO);
        }
    }

    public List<OrderRespDto> getOrderByIdAndTokenid(Map<String, Object> orderMap) {
        return orderMapper.getOrderByIdAndTokenid(orderMap);
    }

    public List<OrderRespDto> getOrders(Map<String, Object> orderMap) {
        List<OrderRespDto> list = orderMapper.getOrdersPage(orderMap);
        int total = orderMapper.getOrdersPageTotal(orderMap);
        orderMap.put("total", total);
        return list;
    }

    public List<TradeRespDto> getTrades(Map<String, Object> orderMap) {
        return orderMapper.getTradesPage(orderMap);
    }

    public List<Trade> getTrades4Lock(Map<String, Object> orderMap) {
        return orderMapper.getTrades(orderMap);
    }

    public List<Trade> findOppositeTrades(Map<String, Object> orderMap) {
        return orderMapper.findOppositeTrades(orderMap);
    }

    public long getAllMemberOrder(Map<String, Object> param) {
        return orderMapper.listMemberOrder(param);
    }


    /**
     * 从交易表中找到每个用户每天要返现的手续费总和
     *
     * @param param table_name：表名称，fee_currency: 手续费币种名称，done_time: 交易时间
     * @return
     */
    public List<ReturnCommiCurrAmountDto> findUnCountReturnCommiRecordOfDay(Map<String, Object> param) {
        return orderMapper.findUnCountReturnCommiRecordOfDay(param);
    }
}
