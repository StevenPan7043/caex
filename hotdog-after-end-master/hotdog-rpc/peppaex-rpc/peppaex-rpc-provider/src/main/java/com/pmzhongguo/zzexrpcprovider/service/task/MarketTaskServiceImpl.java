/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/22 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.service.task;

import com.alibaba.fastjson.JSONObject;
import com.pmzhongguo.ex.business.dto.TradeDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.entity.Order;
import com.pmzhongguo.ex.business.resp.DepthResp;
import com.pmzhongguo.ex.business.resp.KLineResp;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.resp.TradeResp;
import com.pmzhongguo.ex.core.web.Constants;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.zzexrpcprovider.bean.GenericServiceImpl;
import com.pmzhongguo.zzexrpcprovider.config.DubboService;
import com.pmzhongguo.zzexrpcprovider.config.FrmZkWatch;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;
import com.pmzhongguo.zzexrpcprovider.utils.HelpUtils;
import com.pmzhongguo.zzexrpcprovider.utils.ZkUtil;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.threadpool.ZzexThreadFactory;
import com.pmzhongguo.zzextool.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.pmzhongguo.zzexrpcprovider.scheduler.UsdtCnyPriceScheduler.PRICE;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/22 17:38
 * @description：market task service impl
 * @version: $
 */
@Slf4j
@Service
public class MarketTaskServiceImpl extends GenericServiceImpl implements MarketTaskService {

    private static final int POOL_SIZE = StaticConst.POOL_MARKET;

    // 保存到redis的key
//	public static final String DEPTHSMAP_REDIS_KEY = "DEPTHSMAP_REDIS_KEY";
//	public static final String TRADESMAP_REDIS_KEY = "TRADESMAP_REDIS_KEY";
//	public static final String KLINESMAP_REDIS_KEY = "KLINESMAP_REDIS_KEY";
//	public static final String TICKERSMAP_REDIS_KEY = "TICKERSMAP_REDIS_KEY";
    public static Integer lastFraudTime = 0; // 调整刷量的频率
    // 跟老铁确认过暂时用不到
    private static Map<String, BigDecimal> toBTCPriceMap = new HashMap<String, BigDecimal>(); // 各币相对于BTC的价格
    private static boolean isToBTCPriceMapGen = false;// 第一次借助其他地方执行一次
    ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE, new ZzexThreadFactory("MarketTaskService"));
    String ip = IPAddressPortUtil.IP_ADDRESS;
    String lockKey = "cacheMarketDataJob_" + ip;
    @Autowired
    private DubboService dubboService;
    @Autowired
    private DaoUtil daoUtil;
    @Autowired
    private FrmZkWatch frmZkWatch;

    public static Map<String, KLineResp> getKlineResp() {
        return QuotationKeyConst._kLinesMap;
    }

    public static List<Map<String, TickerResp>> insertionSort(Map<String, TickerResp> maps) {
        // 创建两个list排序用
        List<Integer> array = new ArrayList<Integer>();
        List<String> strList = new ArrayList<String>();
        for (Map.Entry<String, TickerResp> map : maps.entrySet()) {
            TickerResp tr = (TickerResp) map.getValue();
            array.add(tr.order);
            strList.add(map.getKey());
        }
        // 排序
        int i, j, t = 0;
        String strTemp = "";
        for (i = 1; i < array.size(); i++) {
            if (array.get(i) < array.get(i - 1)) {
                t = array.get(i);
                strTemp = strList.get(i);
                for (j = i - 1; j >= 0 && t < array.get(j); j--) {
                    array.set(j + 1, array.get(j));
                    strList.set(j + 1, strList.get(j));
                }
                array.set(j + 1, t);
                strList.set(j + 1, strTemp);
            }
        }
        // 成生排序好的list
        List<Map<String, TickerResp>> result = new ArrayList<Map<String, TickerResp>>();
        for (i = 0; i < strList.size(); i++) {
            Map<String, TickerResp> map = new HashMap<String, TickerResp>();
            map.put(strList.get(i), maps.get(strList.get(i)));
            result.add(map);
        }
        return result;
    }

    @Override
    public void executeMarket() {
        try {
            //try里的代码是以前的业务代码，加了lock后放到try里
//			long startTime = System.currentTimeMillis();
            lastFraudTime++;

            List<CurrencyPair> cpLst = QuotationKeyConst.currencyPairs;
            // 判断K线Map是否为null,如果为null说明是第一次
//            boolean isKLinesMap = getKlineResp().isEmpty();
//            if (isKLinesMap)
//            {
//                log.warn("===============> isKLinesMap is true!First?");
//            }

            for (CurrencyPair currencyPair : cpLst) {
                long time = DateUtils.differentDaysBySecond(currencyPair.getOpen_time(), null);
                if (3 == currencyPair.getP_status() || currencyPair.getIs_flash_sale_open() == 1 || time > StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME) {
                    String currLockKey = lockKey + "_" + currencyPair.getKey_name();
                    boolean isLock = JedisUtil.getInstance().getLock(currLockKey, ip, 10 * 1000);
                    // 拿不到锁就丢弃任务
                    if (isLock) {
                        pool.execute(new CurrencyMarketDataJob(currencyPair, currLockKey));
                    }
                }
            }

            // 移除已经禁用的交易对
            for (Map.Entry<String, TickerResp> entry : this.getTicker().entrySet()) {
                boolean isExists = false;
                for (CurrencyPair currencyPair : cpLst) {
                    if ((currencyPair.getKey_name().toLowerCase() + "_ticker").equals(entry.getKey())) {
                        isExists = true;
                        break;
                    }
                }
                if (!isExists) {
                    QuotationKeyConst._tickersMap.remove(entry.getKey());
                }
            }

            if (lastFraudTime >= QuotationKeyConst.frmConfig.getFraud_seconds()) {
                lastFraudTime = 0;
            }

//			if (!isToBTCPriceMapGen) {
//				cacheToBTCPriceData();
//				isToBTCPriceMapGen = true;
//			}

//			saveData2Redis();
//			long endTime = System.currentTimeMillis();
//			logger.warn("=============>cacheMarketData run time:" + (endTime - startTime));
        } catch (Exception e) {
            log.warn(e.toString());
        }
    }

    /**
     * 获取深度数据
     */
    public Map<String, Object> depthData(CurrencyPair currencyPair, BigDecimal[] bid1, BigDecimal[] ask1, int count) {
        Map<String, Object> result = new HashMap<>();
        String symbol = currencyPair.getKey_name().toLowerCase();

        // --------------------------------买卖盘（参数：深度0,1,2,3,4,5）-----------------------------------------------------------------
        // 刷数据的买卖盘
        TickerResp fraudTickerBidAskResp = StaticTaskServiceImpl.lastFraudBidAskMap.get(symbol);

        int retryMax = 10;// 重试次数
        // 真实买单
        Set setBid = JedisUtil.getInstance().zrevrangeByScore((symbol + "_buy").getBytes(), Double.MAX_VALUE, 0);
        Iterator<byte[]> iterBid = setBid.iterator();

        // 真实卖单
        Set setAsk = JedisUtil.getInstance().zrangeByScore((symbol + "_sell").getBytes(), Double.MAX_VALUE, 0);
        Iterator<byte[]> iterAsk = setAsk.iterator();

        // 买方，从高到底
        byte[] bidsByte1 = null;
        byte[] bytes = null;
        Integer i = 0, j = 0;
        BigDecimal[][] bids = new BigDecimal[setBid.size()][2];
        int leng = setBid.size();
        while (iterBid.hasNext()) {
            j++;
            bytes = iterBid.next();

            // 如果到了刷数据时间，并且有刷量数据，并且刷量数据的买价大于当前实际有挂单的买价，并且刷量数据的买价小于实际挂单的卖价，则用假数据做买一
            if (i == 0 /*
             * && StaticServiceImpl.lastFraudTime >= HelpUtils.getMgrConfig().getFraud_seconds()
             */
                    && null != fraudTickerBidAskResp && null != fraudTickerBidAskResp.bis1
                    && fraudTickerBidAskResp.bis1[0]
                    .compareTo(((BigDecimal[]) SerializeUtil.deserialize(bytes))[0]) == 1
                    && iterAsk.hasNext() && fraudTickerBidAskResp.bis1[0]
                    .compareTo(((BigDecimal[]) SerializeUtil.deserialize(iterAsk.next()))[0]) == -1) {
                bids = new BigDecimal[setBid.size() + 1][2]; // 因为要加一条假数据，因此这里数组长度加1
                bid1 = fraudTickerBidAskResp.bis1;
                bids[i++] = fraudTickerBidAskResp.bis1;
                fraudTickerBidAskResp.bis1 = null;
            } else if (i == 0) {
                bidsByte1 = bytes;
                bid1 = (BigDecimal[]) SerializeUtil.deserialize(bytes);
            }
            //需要判定redis的内外部队列
            bids[i] = (BigDecimal[]) SerializeUtil.deserialize(bytes);
            // 随机改变原来的量，买一和卖一永远不变
            if (j > 1 && lastFraudTime >= QuotationKeyConst.frmConfig.getFraud_seconds()
                    && currencyPair.getFraud_magnitude().compareTo(BigDecimal.ZERO) == 1) {
                if (Math.random() > 0.7) {
                    bids[i][1] = bids[i][1].add(bids[i][1].multiply(BigDecimal.valueOf(Math.random()))
                            .setScale(currencyPair.getVolume_precision(), BigDecimal.ROUND_UP));// 数量在原数量基础上随机变动
                }

            }

            i++;
        }
        bids = reformBigDecimalArray(bids, symbol, "buy");

        // 卖方，从低到高
        byte[] asksByte1 = null;
        i = 0;
        j = 0;
        BigDecimal[][] asks = new BigDecimal[setAsk.size()][2];

        iterBid = setBid.iterator(); // 重新赋值买单，因为这里需要用到他做判断

        iterAsk = setAsk.iterator(); // 重新赋值，因为上面已经用了他的next一次
        leng = setAsk.size();
        while (iterAsk.hasNext()) {
            j++;
            bytes = iterAsk.next();

            if (i == 0 /*
             * && StaticServiceImpl.lastFraudTime >= HelpUtils.getMgrConfig().getFraud_seconds()
             */
                    && null != fraudTickerBidAskResp && null != fraudTickerBidAskResp.ask1
                    && fraudTickerBidAskResp.ask1[0]
                    .compareTo(((BigDecimal[]) SerializeUtil.deserialize(bytes))[0]) == -1
                    && iterBid.hasNext() && fraudTickerBidAskResp.ask1[0]
                    .compareTo(((BigDecimal[]) SerializeUtil.deserialize(iterBid.next()))[0]) == 1) {
                asks = new BigDecimal[setAsk.size() + 1][2];
                ask1 = fraudTickerBidAskResp.ask1;
                asks[i++] = fraudTickerBidAskResp.ask1;
                fraudTickerBidAskResp.ask1 = null;
            } else if (i == 0) {
                asksByte1 = bytes;
                ask1 = (BigDecimal[]) SerializeUtil.deserialize(bytes);
            }
            asks[i] = (BigDecimal[]) SerializeUtil.deserialize(bytes);

            // 随机改变原来的量，买一和卖一永远不变
            if (j > 1 && lastFraudTime >= QuotationKeyConst.frmConfig.getFraud_seconds()
                    && currencyPair.getFraud_magnitude().compareTo(BigDecimal.ZERO) == 1) {
                if (Math.random() > 0.7) {
                    asks[i][1] = asks[i][1].add(asks[i][1].multiply(BigDecimal.valueOf(Math.random()))
                            .setScale(currencyPair.getVolume_precision(), BigDecimal.ROUND_UP));// 数量在原数量基础上随机变动
                }
            }

            i++;
        }
        asks = reformBigDecimalArray(asks, symbol, "sell");

        // 判断买一价是否大于卖一价
        if (bid1 != null && ask1 != null && bidsByte1 != null && asksByte1 != null) {
            if (bid1[0].compareTo(ask1[0]) >= 0 && count < 3) {
                // 检查订单属性并重新检测
                if (isErrorData(bidsByte1, symbol, "buy", bid1) && isErrorData(asksByte1, symbol, "sell", ask1)) {
                    return this.depthData(currencyPair, bid1, ask1, ++count);
                }
            }
        }
        result.put("buy", bids);
        result.put("sell", asks);
        result.put("bid1", bid1);
        result.put("ask1", ask1);
        return result;
    }

    /**
     * 获取币对的最新价
     *
     * @param currency 币种
     * @return
     */
    public BigDecimal getNewestClose(String currency) {

        BigDecimal usdtCnyPrice = PRICE;
        if (currency.equalsIgnoreCase("usdt")) {
            return usdtCnyPrice;
        }
        TickerResp tickerResp = QuotationKeyConst._tickersMap.get(currency + "usdt_ticker");
        if (tickerResp != null) {
            return tickerResp.close.multiply(usdtCnyPrice);
        }
        tickerResp = QuotationKeyConst._tickersMap.get(currency + "eth_ticker");
        if (tickerResp != null) {
            TickerResp ethzc_ticker = QuotationKeyConst._tickersMap.get("ethusdt_ticker");
            if (ethzc_ticker == null) {
                return BigDecimal.ZERO;
            }
            return ethzc_ticker.close.multiply(tickerResp.close).multiply(usdtCnyPrice).setScale(8, BigDecimal.ROUND_HALF_UP);
        }
        tickerResp = QuotationKeyConst._tickersMap.get(currency + "btc_ticker");
        if (tickerResp != null) {
            TickerResp usdtzc_ticker = QuotationKeyConst._tickersMap.get("btcusdt_ticker");
            if (usdtzc_ticker == null) {
                return BigDecimal.ZERO;
            }

            return usdtzc_ticker.close.multiply(tickerResp.close).multiply(usdtCnyPrice).setScale(8, BigDecimal.ROUND_HALF_UP);
        }

        return BigDecimal.ZERO;

    }


    /**
     * @param bytes       外层对象
     * @param symbol      交易对名称
     * @param type        buy or sell
     * @param priceVolume
     * @return
     */
    private boolean isErrorData(byte[] bytes, String symbol, String type, BigDecimal[] priceVolume) {
        // 拿到内层key
        String InnerSetKey = symbol + "_" + type + "_" + priceVolume[0].doubleValue();
        try {
            Set<byte[]> oppositeInnerSet = JedisUtil.getInstance().zrangeByScore(InnerSetKey.getBytes(), Double.MAX_VALUE, 0);
            // 只有一条记录就做判断
//			if(oppositeInnerSet != null && oppositeInnerSet.size() == 1) {
//				byte[] innerByte = oppositeInnerSet.iterator().next();
//				Order oppositeOrder  = (Order) SerializeUtil.deserialize(innerByte);
//				Order oppositeOrderMySQL = exService.getOrder(oppositeOrder);
//				if(oppositeOrderMySQL == null) {
//					log.warn("数据库查不到订单：" + oppositeOrder.toString());
//					JedisUtil.getInstance().zrem(InnerSetKey.getBytes(), innerByte);
//					JedisUtil.getInstance().zrem((symbol + "_" + type).getBytes(), bytes);
//					return true;
//				}
//			}
            // 处理多条数据 两个if互斥，只能用一个
            if (oppositeInnerSet != null && oppositeInnerSet.size() > 0) {
                Iterator<byte[]> innerIter = oppositeInnerSet.iterator();
                while (innerIter.hasNext()) {
                    byte[] innerByte = innerIter.next();
                    Order oppositeOrder = (Order) SerializeUtil.deserialize(innerByte);
                    Order oppositeOrderMySQL = this.getOrder(oppositeOrder);
                    if (oppositeOrderMySQL == null) {
                        log.warn("redis缓存内层有数据，数据库查不到订单：" + oppositeOrder.toString());
                        // 删除内层订单数据
                        JedisUtil.getInstance().zrem(InnerSetKey.getBytes(), innerByte);
                        // 更新外层订单总量
                        BigDecimal takerVolume = oppositeOrder.getVolume().subtract(oppositeOrder.getDone_volume());
                        priceVolume[1] = this.refreshPriceVolumeSet(symbol + "_" + type, oppositeOrder.getPrice(),
                                takerVolume.negate()); // 这里是减少，negate表示负数
                    }
                }
                if (priceVolume[1].compareTo(BigDecimal.ZERO) <= 0) {
                    JedisUtil.getInstance().zrem((symbol + "_" + type).getBytes(), bytes);
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("cacheMarketData " + InnerSetKey + "cause:" + e.getCause(), e);
        }
        return false;
    }

    /**
     * 新增方法, 查询订单
     *
     * @param
     * @return
     * @author yukai
     * @description
     * @date 2019/4/9
     */
    private Order getOrder(Order order) {
        String tableName = order.getTable_name();
        StringBuilder sql = new StringBuilder("select * from t_order_").append(tableName).append(" where 1=1");
        if (order.getId() != 0) {
            sql.append(" and id=").append(order.getId());
        }
        if (StringUtils.isNotBlank(order.getO_no())) {
            sql.append(" and o_no=\'").append(order.getO_no());
        }
        if (order.getMember_id() != 0) {
            sql.append("\' and member_id=").append(order.getMember_id());
        }
        List orderList = daoUtil.queryForList(sql.toString());
        if (orderList != null && orderList.size() > 0) {
            return JsonUtil.parseMap2Object((Map<String, Object>) orderList.get(0), Order.class);
        }
        return null;
    }

    private void generateTradeResp(List<TradeDto> trades, int size, String symbol, int num) {
        List<TradeDto> subList = trades.subList(0, size > num ? num : size);
        List<TradeDto> saveList = new ArrayList<TradeDto>();
        saveList.addAll(subList);
        TradeResp tradeResp = new TradeResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbol, DateUtils.getNowTimeStampMillisecond(), saveList);
        this.getTradeResp().put(symbol + "_" + num, tradeResp);
        SyncMessageService.syncTradeMessage(symbol, num);
    }

    /**
     * K线生成
     *
     * @param symbol
     */
    @Override
    public void generateKLineResp(String symbol) {
        // 1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week, 1year
        generateKLineResp(symbol, "1min");
        generateKLineResp(symbol, "5min");
        generateKLineResp(symbol, "15min");
        generateKLineResp(symbol, "30min");
        generateKLineResp(symbol, "60min");
        generateKLineResp(symbol, "1day");
        generateKLineResp(symbol, "1mon");
        generateKLineResp(symbol, "1week");
    }

    /**
     * @param symbol 交易对名称
     * @param type   K线类型，取值1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week,
     *               1year
     */
    private void generateKLineResp(String symbol, String type) {
        List<byte[]> lst = JedisUtil.getInstance().lrange(symbol + "_" + type, 0, 1000); // 从1000改成100
        int size = lst.size();
        List<BigDecimal[]> kLines = new ArrayList<BigDecimal[]>(size);
        for (byte[] bt : lst) {
            BigDecimal[] oneKLine = (BigDecimal[]) SerializeUtil.deserialize(bt);
            oneKLine[0] = oneKLine[0].multiply(BigDecimal.valueOf(1000)); // 配合要求13位的时间戳
            kLines.add(oneKLine);
        }
        // 1000 条
        generateKLineResp(kLines, size, symbol, type, 1000);
        // 100 条
        generateKLineResp(kLines, size, symbol, type, 100);
        // 1条
        generateKLineResp(kLines, size, symbol, type, 1);
    }

//	private void saveData2Redis() {
//		JedisUtil.getInstance().set(DEPTHSMAP_REDIS_KEY, _depthsMap, false);
//		JedisUtil.getInstance().set(TRADESMAP_REDIS_KEY, _tradesMap, false);
//		JedisUtil.getInstance().set(KLINESMAP_REDIS_KEY, _kLinesMap, false);
//		JedisUtil.getInstance().set(TICKERSMAP_REDIS_KEY, _tickersMap, false);
//	}

    /**
     * @param kLines 获取的K线数据集合
     * @param maxNum K线数据集合长度
     * @param symbol 交易对名称
     * @param type   K线类型，取值1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week,
     *               1year
     * @param num    需要获取的K线数据集合长度
     */
    private void generateKLineResp(List<BigDecimal[]> kLines, int maxNum, String symbol, String type, int num) {
        List<BigDecimal[]> subList = kLines.subList(0, maxNum > num ? num : maxNum);
        List<BigDecimal[]> saveList = new ArrayList<BigDecimal[]>();
        saveList.addAll(subList);
        KLineResp kLineResp = new KLineResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbol, type,
                DateUtils.getNowTimeStampMillisecond(), saveList);
        SyncMessageService.syncKlineMessage(symbol, type, num, kLineResp);
        getKlineResp().put(symbol + "_" + type + "_" + num, kLineResp);
    }

    /**
     * 获得深度数据
     *
     * @param symbol
     * @param step
     * @return
     */
    @Override
    public DepthResp getDepth(String symbol, Integer step) {
        return this.getDepthResp().get(symbol + "_" + step);
    }

    /**
     * 获得交易数据
     *
     * @param symbol
     * @param size
     * @return
     */
    @Override
    public TradeResp getTrades(String symbol, Integer size) {
        return this.getTradeResp().get(symbol + "_" + size);
    }

    /**
     * 获得K线数据
     *
     * @param symbol
     * @param type
     * @param size
     * @return
     */
    @Override
    public KLineResp getKLine(String symbol, String type, Integer size) {
        if (StaticConst.TASK_NO_STATUS == 0) {
            return dubboService.getMarketApiService(DubboService.activeIp).getKLine(symbol, type, size);
        }
        return this.getKlineResp().get(symbol + "_" + type + "_" + size);
    }

    /**
     * 获得K线数据 升序
     *
     * @param symbol
     * @param type
     * @param size
     * @return
     */
    @Override
    public KLineResp getKLineAsc(String symbol, String type, Integer size) {
        KLineResp kLineResp = this.getKLine(symbol, type, size);
        List<BigDecimal[]> bigDecimals = kLineResp.data.get("lines");
        if (bigDecimals != null && bigDecimals.size() > 0) {
            Collections.reverse(bigDecimals);

            kLineResp.data.put("lines", bigDecimals);
        }
        return kLineResp;
    }

    /**
     * 获得行情汇总信息
     *
     * @param symbol
     * @return
     */
    @Override
    public TickerResp getTicker(String symbol) {
        Map<String, TickerResp> tickersMap = this.getTicker();
        return tickersMap.get(symbol + "_ticker");
    }

    @Override
    public String heartBeat() {
        if (StaticConst.TASK_NO_STATUS == 0) {
            return dubboService.getMarketApiService(DubboService.activeIp).heartBeat();
        }
        return "success";
    }

    /**
     * 获得所有行情汇总信息
     *
     * @return
     */
    @Override
    public Map<String, TickerResp> getTicker() {
        if (StaticConst.TASK_NO_STATUS == 0) {
            QuotationKeyConst._tickersMap = dubboService.getMarketApiService(DubboService.activeIp).getTickerResp();
        }
        return QuotationKeyConst._tickersMap;
    }

    @Override
    public Map<String, DepthResp> getDepthResp() {
        if (StaticConst.TASK_NO_STATUS == 0) {
            QuotationKeyConst._depthsMap = dubboService.getMarketApiService(DubboService.activeIp).getDepthResp();
        }
        return QuotationKeyConst._depthsMap;
    }

    @Override
    public Map<String, TradeResp> getTradeResp() {
        if (StaticConst.TASK_NO_STATUS == 0) {
            QuotationKeyConst._tradesMap = dubboService.getMarketApiService(DubboService.activeIp).getTradeResp();
        }
        return QuotationKeyConst._tradesMap;
    }

    /**
     * 刷新外层队列
     * <p>
     * 外层队列格式： 1、首先每个交易对两个key，名字就是 ltcbtc_sel 和 ltcbtc_buy 2、key下面存储的是
     * 价格和价格数量数组的队列，以价格作为Key，价格和数量的数组作为Value
     *
     * @param keyName
     * @param price
     * @param addVolume 内层数据删除后需要从总量里减少的数量
     */
    private BigDecimal refreshPriceVolumeSet(String keyName, BigDecimal price, BigDecimal addVolume) {
        // 获得Redis中已经存在的指定价格的PriceVolume对象
        Set<byte[]> alreadyPriceVolumeBytes = JedisUtil.getInstance().zrangeByScore((keyName).getBytes(),
                price.doubleValue(), price.doubleValue());
        BigDecimal[] priceVolume = new BigDecimal[2];
        priceVolume[0] = price;
        priceVolume[1] = BigDecimal.ZERO;

        // 如果该价格已经有，就要删除（当然，先存下来原来的，即priceVolume）
        if (alreadyPriceVolumeBytes != null && alreadyPriceVolumeBytes.size() > 0) {
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
        return priceVolume[1];
    }

    /**
     * 各种币相对于BTC的价格，目前用于判断用户24小时内提现额度是否超过规定的BTC
     */
    public void cacheToBTCPriceData() {
        BigDecimal cnyToBTC = BigDecimal.ONE; // 1cny对多少btc，下同
        BigDecimal ethToBTC = BigDecimal.ONE;
        BigDecimal xacToBTC = BigDecimal.ONE;
        Map<String, BigDecimal> tempToBTCPriceMap = new HashMap<String, BigDecimal>(); // 各币相对于BTC的价格
        tempToBTCPriceMap.put("BTC", BigDecimal.ONE); // BTC自己就是1
        for (Map.Entry<String, TickerResp> entry : this.getTicker().entrySet()) {
            String[] dspNameArr = entry.getValue().dspName.split("/");
            if ("BTC".equals(dspNameArr[0]) && "CNY".equals(dspNameArr[1])
                    && entry.getValue().close.compareTo(BigDecimal.ZERO) > 0) {
                cnyToBTC = BigDecimal.ONE.divide(entry.getValue().close, 8, BigDecimal.ROUND_DOWN);
                tempToBTCPriceMap.put("CNY", cnyToBTC);
                break;
            }
        }

        // 算直接跟BTC有交易对的价格
        for (Map.Entry<String, TickerResp> entry : this.getTicker().entrySet()) {
            String[] dspNameArr = entry.getValue().dspName.split("/");
            if ("BTC".equals(dspNameArr[1])) {
                tempToBTCPriceMap.put(dspNameArr[0], entry.getValue().close); // 直接跟BTC的交易对，就直接取价格即可。
            }
        }

        // 目前没有BTC对ETH和XAC的交易对，只能通过CNY来中转
        for (Map.Entry<String, TickerResp> entry : this.getTicker().entrySet()) {
            String[] dspNameArr = entry.getValue().dspName.split("/");
            if ("ETH".equals(dspNameArr[0]) && "CNY".equals(dspNameArr[1])) {
                ethToBTC = entry.getValue().close.multiply(cnyToBTC);
                tempToBTCPriceMap.put("ETH", ethToBTC);
            } else if ("XAC".equals(dspNameArr[0]) && "CNY".equals(dspNameArr[1])) {
                xacToBTC = entry.getValue().close.multiply(cnyToBTC);
                tempToBTCPriceMap.put("XAC", xacToBTC);
            }
        }

        // 第三次循环，算每个币相对于BTC的价格(到这里，就是没有直接跟BTC有交易对的)
        for (Map.Entry<String, TickerResp> entry : this.getTicker().entrySet()) {
            String[] dspNameArr = entry.getValue().dspName.split("/");

            // 如果已经存在，就下一个循环
            if (tempToBTCPriceMap.containsKey(dspNameArr[0])) {
                // log.error(dspNameArr[0] + "已经存在");
                continue;
            }

            if ("CNY".equals(dspNameArr[1])) {
                tempToBTCPriceMap.put(dspNameArr[0], entry.getValue().close.multiply(cnyToBTC)); // 属于CNY交易对
            } else if ("ETH".equals(dspNameArr[1])) {
                tempToBTCPriceMap.put(dspNameArr[0], entry.getValue().close.multiply(ethToBTC));
            } else if ("XAC".equals(dspNameArr[1])) {
                tempToBTCPriceMap.put(dspNameArr[0], entry.getValue().close.multiply(xacToBTC));
            }
        }
        toBTCPriceMap = tempToBTCPriceMap;
    }

    public boolean isErrorData(String symbol, String type, BigDecimal priceVolume[]) {
        String InnerSetKey = symbol + "_" + type + "_" + priceVolume[0].doubleValue();
        Set<byte[]> oppositeInnerSet = JedisUtil.getInstance().zrangeByScore(InnerSetKey.getBytes(), Double.MAX_VALUE, 0);
        return oppositeInnerSet == null || oppositeInnerSet.size() == 0;
    }

    public BigDecimal[][] reformBigDecimalArray(BigDecimal[][] bigDecimals, String symbol, String type) {
        int ErrorNumber = 0;
        int size = bigDecimals.length >= 50 ? 50 : bigDecimals.length;
        for (int k = 0; k < size; k++) {
            if (isErrorData(symbol, type, bigDecimals[k])) {
                ErrorNumber++;
//                log.warn("错误数据："+symbol + "_" + type +"价格:" + bigDecimals[k][0] + "数量:" + bigDecimals[k][1]);
//                JedisUtil.getInstance().zrem((symbol + "_" + type).getBytes(),SerializeUtil.serialize(bigDecimals[k]));
                bigDecimals[k][0] = BigDecimal.valueOf(-1);
            }
        }
        if (0 != ErrorNumber) {
            BigDecimal[][] temp = new BigDecimal[bigDecimals.length - ErrorNumber][2];
            for (int k = 0, l = 0; k < bigDecimals.length; k++) {
                if (bigDecimals[k][0].compareTo(BigDecimal.valueOf(-1)) != 0) {
                    temp[l] = bigDecimals[k];
                    l++;
                }
            }
            return temp;
        }
        return bigDecimals;
    }

    class CurrencyMarketDataJob implements Runnable {
        CurrencyPair currencyPair;
        String currLockKey;
        private BigDecimal[] bid1;
        private BigDecimal[] ask1;

        public CurrencyMarketDataJob(CurrencyPair currencyPair, String currLockKey) {
            this.currencyPair = currencyPair;
            this.currLockKey = currLockKey;
        }

        @Override
        public void run() {
            // ---------------------------行情数据（也称聚合数据）------------------------------
            try {
                //首先判断抢购情况
                if (currencyPair.getIs_flash_sale_open() == 1) {
                    changeFlashSale();
                }
                String symbol = currencyPair.getKey_name().toLowerCase();
                Map<String, Object> result = depthData(currencyPair, bid1, ask1, 1);
                BigDecimal[][] bids = (BigDecimal[][]) result.get("buy");
                BigDecimal[][] asks = (BigDecimal[][]) result.get("sell");
                bid1 = (BigDecimal[]) result.get("bid1");
                ask1 = (BigDecimal[]) result.get("ask1");
                if (isComingTickersCached(symbol)) {
                    return;
                }
                DepthResp dResp = new DepthResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbol, DateUtils.getNowTimeStampMillisecond(), bids, asks);
                SyncMessageService.syncDepthMessage(symbol, dResp);
                QuotationKeyConst._depthsMap.put(symbol + "_0", dResp); // 考虑以后有需要合并深度，因此这里加了 _0，表示不合并，以后要合并，再做考虑
                // --------------------------------买卖盘-----------------------------------------------------------------
                // ---------------------------交易数据（参数：数据条数，最大1000，分1/10/100/1000）------------------------------------------
                List<byte[]> lst = JedisUtil.getInstance().lrange(symbol + "_trade", 0, 100); // 从1000改成100
                int size = lst.size();
                List<TradeDto> trades = new ArrayList<TradeDto>(size);
                for (byte[] bt : lst) {
                    trades.add((TradeDto) SerializeUtil.deserialize(bt));
                }

                // 1000 条
                // TradeResp tradeResp = new TradeResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbol,
                // HelpUtils.getNowTimeStampMillisecond(), trades);
                // tradesMap.put(symbol + "_1000", tradeResp);
                // 100 条
                // tradeResp = new TradeResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbol,
                // HelpUtils.getNowTimeStampMillisecond(), trades.subList(0, size > 100 ? 100 :
                // size));
                // tradesMap.put(symbol + "_100", tradeResp);
                // 10 条
                generateTradeResp(trades, size, symbol, 50);
                generateTradeResp(trades, size, symbol, 1);
                // 1 条
                // ---------------------------交易数据------------------------------------------

                // ---------------------------K线（参数：K线类型、数据条数）----------------------------------------------
//                if (isKLinesMap)
//                {
//                    generateKLineResp(symbol);
//                }
//				generateKLineResp(symbol, "1year");
                // ---------------------------K线----------------------------------------------
                // 用于最下面的生成聚合数据
                BigDecimal[] dayKLine = null;
                lst = JedisUtil.getInstance().lrange(symbol + "_1min", 0, 1440); // 这里取1440是因为要生成聚合数据中的24小时内数据
                size = lst.size();
//				List<BigDecimal[]> kLines = new ArrayList<BigDecimal[]>(size);
                int k = 0;
                BigDecimal maybeOpenPrice = BigDecimal.ZERO; // 可能开盘价，因为实际开盘价一般为0，第一个出现的不为0的价格作为开盘价
                for (byte[] bt : lst) {
                    BigDecimal[] curArr = (BigDecimal[]) SerializeUtil.deserialize(bt);
                    if (k == 0) {
                        // 收盘价也在这里
                        // 用于最下面的生成聚合数据
                        dayKLine = curArr;
                    } else {
                        // 高
                        if (dayKLine[2].compareTo(curArr[2]) < 0) {
                            dayKLine[2] = curArr[2];
                        }
                        // 低
                        if (dayKLine[3].compareTo(BigDecimal.ZERO) == 0) { // 如果原来为0，则不管什么情况，设置为新的
                            dayKLine[3] = curArr[3];
                        } else if (curArr[3].compareTo(BigDecimal.ZERO) > 0 && dayKLine[3].compareTo(curArr[3]) > 0) {
                            dayKLine[3] = curArr[3];
                        }
                        dayKLine[5] = curArr[5].add(dayKLine[5]);
                    }

                    if (curArr[1].compareTo(BigDecimal.ZERO) > 0) {
                        maybeOpenPrice = curArr[1];
                    }

                    if (k == lst.size() - 1) { // 最后一个，作为开盘价（因为是按时间倒叙排列的）
                        // 这里判断的原因是，刚开盘的币，取第一个不为0（如果有）的作为开盘价
                        dayKLine[1] = curArr[1].compareTo(BigDecimal.ZERO) == 0 ? maybeOpenPrice : curArr[1];
                    }

                    k++;
                }
                Map<String, com.pmzhongguo.ex.business.entity.Currency> currencyMap = QuotationKeyConst.currencysMap;
                Currency currency = currencyMap.get(currencyPair.getBase_currency_name());
                if (currency == null) {
                    log.error("请检查币种配置。pairName: {}, base_currency_name: {}", currencyPair.getDsp_name(), currencyPair.getBase_currency_name());
                    return;
                }
                // ---------------------------行情数据（也称聚合数据）------------------------------
                // 计算cny 价格
                BigDecimal cnyClose = dayKLine[4].multiply(getNewestClose(currencyPair.getQuote_currency().toLowerCase()));

                // dayKLine = 时间戳0, 开盘价1, 最高价2, 最低价3, 收盘价4, cny最新价5, 成交数量6, 成交笔数1, 成交额1
                TickerResp tickerResp = new TickerResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbol,
                        DateUtils.getNowTimeStampMillisecond(), dayKLine[1], dayKLine[2], dayKLine[3], dayKLine[4],
                        cnyClose,
                        dayKLine[5].multiply(BigDecimal.valueOf(QuotationKeyConst.frmConfig.getVolume_multiply())), // 交易量放大
                        bid1, ask1, currencyPair.getDsp_name(), null != trades && trades.size() > 0 ? trades.get(0).upOrDown : 1,
                        currencyPair.getPrice_precision(), currencyPair.getVolume_precision(),
                        currencyPair.getP_order(), currencyPair.getArea_id(), currencyPair.getCan_buy(),
                        currencyPair.getCan_sell(), currencyPair.getStop_desc(), currencyPair.getQuote_currency_name(),
                        currencyPair.getBase_currency_name(), currencyPair.getMin_buy_volume(),
                        currencyPair.getMin_buy_amount(), currencyPair.getMin_sell_volume(),
                        currencyPair.getFixed_buy_price(), currency.getCan_recharge(),
                        currencyPair.getP_status()
                );
                SyncMessageService.syncTickerMessage(symbol, tickerResp);
                QuotationKeyConst._tickersMap.put(symbol + "_ticker", tickerResp);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("cacheMarketData Exception！ currencyPair:" + currencyPair.toString() + "  " + e);
//				throw new BusinessException(-1, "cacheMarketData Exception！ currencyPair:" + currencyPair.toString() + "  cause:" + e.getCause());
            } finally {
                boolean isRelease = JedisUtil.getInstance().releaseLock(currLockKey, ip);
            }
        }

        public boolean isComingTickersCached(String symbol) {
            //判断交易对是否满足开启时间
            if (3 == currencyPair.getP_status()) {
                long time = DateUtils.differentDaysBySecond(currencyPair.getOpen_time(), null);
                if (time > StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME) {
                    currencyPair.setP_status(1);
                    String sql = "UPDATE d_currency_pair SET p_status = 1 WHERE id = ?";
                    daoUtil.update(sql, currencyPair.getId());
                    JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_PAIR, JedisChannelConst.SYNC_MESSAGE);
                    //zk同步
                    boolean b = ZkUtil.getInstance().writeData(Constants.ZK_WATCH_PATH_CURRENCY_PAIR, HelpUtils.formatDate8(new Date()));
                    if (!b) {
                        log.error("<----------------- 交易对 数据同步失败");
                    }
                    log.warn(currencyPair.getDsp_name() + "交易对由即将上线变为正常，设定操作时间:" + currencyPair.getOpen_time());
                } else {
                    TickerResp tickerResp = new TickerResp(Resp.SUCCESS, Resp.SUCCESS_MSG, symbol,
                            DateUtils.getNowTimeStampMillisecond(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                            BigDecimal.ZERO, BigDecimal.ZERO, bid1, ask1, currencyPair.getDsp_name(), 1,
                            currencyPair.getPrice_precision(), currencyPair.getVolume_precision(),
                            currencyPair.getP_order(), currencyPair.getArea_id(), currencyPair.getCan_buy(),
                            currencyPair.getCan_sell(), currencyPair.getStop_desc(), currencyPair.getQuote_currency_name(),
                            currencyPair.getBase_currency_name(), currencyPair.getMin_buy_volume(),
                            currencyPair.getMin_buy_amount(), currencyPair.getMin_sell_volume(),
                            currencyPair.getFixed_buy_price(), 0,
                            currencyPair.getP_status()
                    );
                    SyncMessageService.syncTickerMessage(symbol, tickerResp);
                    QuotationKeyConst._tickersMap.put(symbol + "_ticker", tickerResp);
                    return true;
                }
            }
            return false;
        }

        public void changeFlashSale() {
            //判断是否开启抢购
            long opentime = DateUtils.differentDaysBySecond(currencyPair.getFlash_sale_open_time(), null);
            long closetime = DateUtils.differentDaysBySecond(currencyPair.getFlash_sale_close_time(), null);
            if (opentime < StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME) {
                return;
            } else if (opentime > StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME && closetime < StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME) {
                if (currencyPair.getP_status() == 3) {
                    currencyPair.setP_status(1);
                    String sql = "UPDATE d_currency_pair SET p_status = 1 WHERE id = ?";
                    updateCurrencyPairAndSync(sql);
                }
            } else if (closetime > StaticConst.ADVANCE_CURRENCY_PAIR_OPEN_TIME) {
                currencyPair.setP_status(3);
                String sql = "UPDATE d_currency_pair SET p_status = 3 , is_flash_sale_open = 0 WHERE id = ?";
                updateCurrencyPairAndSync(sql);
            }
        }

        protected void updateCurrencyPairAndSync(String sql) {
            daoUtil.update(sql, currencyPair.getId());
            JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_CURRENCY_PAIR, JedisChannelConst.SYNC_MESSAGE);
            //zk同步
            boolean b = ZkUtil.getInstance().writeData(Constants.ZK_WATCH_PATH_CURRENCY_PAIR, HelpUtils.formatDate8(new Date()));
            if (!b) {
                log.error("<----------------- 交易对 数据同步失败");
            }
        }
    }


}
