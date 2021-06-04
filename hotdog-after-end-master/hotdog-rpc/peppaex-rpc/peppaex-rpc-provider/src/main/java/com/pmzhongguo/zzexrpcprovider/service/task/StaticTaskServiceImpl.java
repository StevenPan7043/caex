/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/22 All Rights Reserved.
 */
package com.pmzhongguo.zzexrpcprovider.service.task;

import com.google.common.collect.Lists;
import com.pmzhongguo.ex.business.dto.TradeDto;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.zzexrpcprovider.bean.GenericServiceImpl;
import com.pmzhongguo.zzexrpcprovider.consts.QuotationKeyConst;
import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.threadpool.ZzexThreadFactory;
import com.pmzhongguo.zzextool.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/22 18:55
 * @description：static service impl
 * @version: $
 */
@Slf4j
@Service
public class StaticTaskServiceImpl extends GenericServiceImpl implements StaticTaskService {

    private static final int POOL_SIZE = StaticConst.POOL_STATIC;

    private static final String BATCH_KLINE_JOB_REDIS_LOCK = "BATCH_KLINE_JOB"; // EXPIRE_TIME
    private static final int BATCH_KLINE_JOB_REDIS_LOCK_EXPIRE_TIME = StaticConst.TIMEOUT_STATIC * 1000;
    // 最后一次刷量已经进过的时间（秒）
    public static Double lastFraudTime = 0.0;
    // 最后一次刷量的买一和卖一
    public static Map<String, TickerResp> lastFraudBidAskMap = new HashMap<>();
    private static DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String ip = IPAddressPortUtil.IP_ADDRESS;

    ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE, new ZzexThreadFactory("StaticTaskService"));

    /**
     * 根据类型获得对应的时间戳
     *
     * @param timeStamp
     * @param type      1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week, 1year
     * @return
     * @throws Exception
     */
    private static int getKLineTimeStamp(long timeStamp, String type) {

        Date date = new Timestamp(timeStamp);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        // 是周几
        if ("1week".equals(type)) {
            int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
            cal.setTime(new Date(date.getTime() - dayOfWeek * 24 * 60 * 60 * 1000));
        }

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);

        String strDate = "";
        if ("1min".equals(type)) {
            strDate = year + "-" + month + "-" + day + " " + hour + ":" + min + ":00";
        } else if ("5min".equals(type)) {
            strDate = year + "-" + month + "-" + day + " " + hour + ":" + ((min / 5) * 5) + ":00";
        } else if ("15min".equals(type)) {
            strDate = year + "-" + month + "-" + day + " " + hour + ":" + ((min / 15) * 15) + ":00";
        } else if ("30min".equals(type)) {
            strDate = year + "-" + month + "-" + day + " " + hour + ":" + ((min / 30) * 30) + ":00";
        } else if ("60min".equals(type)) {
            strDate = year + "-" + month + "-" + day + " " + hour + ":00:00";
        } else if ("1day".equals(type)) {
            strDate = year + "-" + month + "-" + day + " 00:00:00";
        } else if ("1mon".equals(type)) {
            strDate = year + "-" + month + "-1 00:00:00";
        } else if ("1year".equals(type)) {
            strDate = year + "-1-1 00:00:00";
        } else if ("1week".equals(type)) { // 星期已经在上面处理了
            strDate = year + "-" + month + "-" + day + " 00:00:00";
        }

        try {
            Date myDate2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
            return (int) (myDate2.getTime() / 1000);

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 打印日志
     *
     * @param content
     */
    public static void printLog(String content) {
        if (StaticConst.LOG_STATIC == 1) {
            log.warn("Static 打印日志: content = " + content);
        }
    }

    @Override
    public void executeStatic() {
        // 进行K线图等统计
        //createTrades(5,"btczc");
        List<CurrencyPair> cpLst = QuotationKeyConst.currencyPairs;
        for (CurrencyPair currencyPair : cpLst) {
            String symbol = currencyPair.getKey_name().toLowerCase();

            String currLockKey = BATCH_KLINE_JOB_REDIS_LOCK + "_" + currencyPair.getKey_name();
            boolean symbolLock = JedisUtil.getInstance().getLock(currLockKey, ip, BATCH_KLINE_JOB_REDIS_LOCK_EXPIRE_TIME);
            // 拿不到锁就等待任务
            if (symbolLock) {
                long startTime = System.currentTimeMillis();
                // 获得交易数据的长度 (由于以前方式会导致交易Redis数组会越来越长，现在修改位只保留1500条数据，因此不能根据数组长度来进行是否最新判断，这里注释掉)
                // long curGenKLen = JedisUtil.getInstance().llen(symbol + "_trade");
                Object oLastGenKLen = JedisUtil.getInstance().get(symbol + "_last_gen_k_len", true);
                if (null == oLastGenKLen) {
                    oLastGenKLen = "0";
                }
                long lastGenKLen = Long.parseLong(oLastGenKLen + "");
                if (lastGenKLen == 0) { // 等于表示一直没交易

                    // 用于生成没有交易的中间段的K线数据
                    TradeDto tradeDto = new TradeDto();
                    tradeDto.timestamp = DateUtils.getNowTimeStampMillisecond();
                    tradeDto.volume = BigDecimal.ZERO;
                    /**
                     *
                     * // 是否刷量 if (isFraud(currencyPair) { tradeDto = fraudKLine(currencyPair,
                     * tradeDto);
                     *
                     * //将刷量数据存Redis if (tradeDto.price != null) {
                     * tradeService.tradeToRedis(tradeDto, symbol, false); }
                     *
                     * }
                     */
                    saveKLineData(symbol, tradeDto, currLockKey, startTime);
                    // logger.error("出来啦~~~~~~~~~");
                    continue;
                }
                List<byte[]> lst = JedisUtil.getInstance().lrange(symbol + "_trade", 0, lastGenKLen - 1);
                JedisUtil.getInstance().set(symbol + "_last_gen_k_len", "0", true);
                if (null != lst && lst.size() > 0) {
                    pool.execute(() -> {
                        try {
                            // 这里要反向取，因为越新的要排在K线的越后面
//                            for (int i = lst.size() - 1; i >= 0; i--)
//                            {
//                                byte[] bt = lst.get(i);
//                                TradeDto tradeDto = (TradeDto) SerializeUtil.deserialize(bt);
//                                saveKLineData(symbol, tradeDto, currLockKey, startTime);
//                            }
                            if (lst.size() == 1) {
                                byte[] bt = lst.get(0);
                                TradeDto tradeDto = (TradeDto) SerializeUtil.deserialize(bt);
                                saveKLineData(symbol, tradeDto, currLockKey, startTime);
                            } else {
                                Map<Integer, BigDecimal[]> timestampKlineMap = new TreeMap();
                                saveKLineData(lst, symbol, timestampKlineMap);
                            }
                        } catch (Exception e) {
                            log.error("<====== Static线程执行异常: " + e.getLocalizedMessage());
                        } finally {
                            boolean isRelease = JedisUtil.getInstance().releaseLock(currLockKey, ip);
                            long endTime = System.currentTimeMillis();
                            if (!isRelease) {
                                log.error("release {} fail。 time-consuming:{}ms  symbol={} 交易数据大小: {}", currLockKey, (endTime - startTime), symbol, lst.size());
                            }
                        }
                    });
                } else {
                    boolean isRelease = JedisUtil.getInstance().releaseLock(currLockKey, ip);
                }
            }
        }
    }

    private void saveKLineData(List<byte[]> lst, String symbol, Map<Integer, BigDecimal[]> timestampKlineMap) {
        saveKLineData(lst, symbol, "1min", timestampKlineMap);
        saveKLineData(lst, symbol, "5min", timestampKlineMap);
        saveKLineData(lst, symbol, "15min", timestampKlineMap);
        saveKLineData(lst, symbol, "30min", timestampKlineMap);
        saveKLineData(lst, symbol, "60min", timestampKlineMap);
        saveKLineData(lst, symbol, "1day", timestampKlineMap);
        saveKLineData(lst, symbol, "1mon", timestampKlineMap);
        saveKLineData(lst, symbol, "1week", timestampKlineMap);
    }

    /**
     * 处理K线
     *
     * @param lst
     * @param symbol
     * @param type
     */

    private void saveKLineData(List<byte[]> lst, String symbol, String type, Map<Integer, BigDecimal[]> timestampKlineMap) {
        long len = JedisUtil.getInstance().llen(symbol + "_" + type);
        if (len > 2000) {
            JedisUtil.getInstance().ltrim(symbol + "_" + type, 0, 1500);
        }
        //遍历list，取出交易对
        for (int i = lst.size() - 1; i >= 0; i--) {
            byte[] bt = lst.get(i);
            TradeDto tradeDto = (TradeDto) SerializeUtil.deserialize(bt);
            int timestamp = getKLineTimeStamp(tradeDto.timestamp, type);
            //如果map里面没有，就把k线数据放进去
            if (null == timestampKlineMap.get(timestamp)) {
                //0 对应时间戳 1开盘价 2最高价 3最低价 4收盘价 5成交量
                BigDecimal datas[] = new BigDecimal[6];
                datas[0] = BigDecimal.valueOf(timestamp);
                datas[1] = tradeDto.price;
                datas[2] = tradeDto.price;
                datas[3] = tradeDto.price;
                datas[4] = tradeDto.price;
                datas[5] = tradeDto.volume;
                timestampKlineMap.put(timestamp, datas);
                //如果map里有，就把k线数据更新成最新的
            } else {
                //交易量为0
                if (tradeDto.volume.compareTo(BigDecimal.ZERO) == 0) {
                    continue;
                }
                BigDecimal datas[] = timestampKlineMap.get(timestamp);
                if (datas[5].compareTo(BigDecimal.ZERO) == 0) {
                    datas[1] = tradeDto.price;
                    datas[2] = tradeDto.price;
                    datas[3] = tradeDto.price;
                    datas[4] = tradeDto.price;
                    datas[5] = tradeDto.volume;
                    continue;
                }
                datas[1] = datas[1].compareTo(BigDecimal.ZERO) == 0 ? tradeDto.price : datas[1];
                datas[2] = datas[2].compareTo(tradeDto.price) < 0 ? tradeDto.price : datas[2];
                datas[3] = datas[3].compareTo(tradeDto.price) > 0 ? tradeDto.price : datas[3];
                datas[4] = tradeDto.price;
                datas[5] = datas[5].add(tradeDto.volume);

            }
        }
        //遍历map，逐个处理K线
        for (int timeStamp : timestampKlineMap.keySet()) {
            byte[] oldData = JedisUtil.getInstance().lpop(symbol + "_" + type, 0, 1);
            BigDecimal[] datas = {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
            if (null != oldData) {
                datas = (BigDecimal[]) SerializeUtil.deserialize(oldData);
            }
            List<BigDecimal[]> datasList = Lists.newArrayList();
            BigDecimal[] bigDecimals = timestampKlineMap.get(timeStamp);
            if (datas[0].intValue() != timeStamp) {
                if (timeStamp > datas[0].intValue()) {
                    this.addDatas(datas, symbol, bigDecimals, timeStamp, type);
                } else {
                    datasList.add(datas);
                    this.modifyOneDatas(symbol, bigDecimals, timeStamp, type, datasList);
                }
            } else {
                this.updateDatas(datas, symbol + "_" + type, bigDecimals);
            }
        }
        timestampKlineMap.clear();
    }

    /**
     * 是否需要刷量
     *
     * @param currencyPair
     * @return
     */
    private boolean isFraud(CurrencyPair currencyPair) {
        boolean result = QuotationKeyConst.frmConfig.getIs_stop_ex() == 0 && currencyPair.getCan_buy() == 1
                && currencyPair.getCan_sell() == 1 && currencyPair.getFraud_magnitude().compareTo(BigDecimal.ZERO) == 1
                && lastFraudTime >= QuotationKeyConst.frmConfig.getFraud_seconds();
        return result;
    }

    /**
     * 根据当前小时数调整刷量的频率
     *
     * @param lastFraudTime
     * @return
     */
    private Double getLastFraudTime(Double lastFraudTime) {
        // 获得当前小时数，根据当前小时数调整刷量的频率
        int curHour = DateUtils.getHour(new Date());
        switch (curHour) {
            case 0:
                lastFraudTime = lastFraudTime + 0.7;
                break;
            case 1:
                lastFraudTime = lastFraudTime + 0.6;
                break;
            case 2:
                lastFraudTime = lastFraudTime + 0.5;
                break;
            case 3:
                lastFraudTime = lastFraudTime + 0.4;
                break;
            case 4:
                lastFraudTime = lastFraudTime + 0.4;
                break;
            case 5:
                lastFraudTime = lastFraudTime + 0.4;
                break;
            case 6:
                lastFraudTime = lastFraudTime + 0.5;
                break;
            case 7:
                lastFraudTime = lastFraudTime + 0.7;
                break;
            case 8:
                lastFraudTime = lastFraudTime + 0.9;
                break;
            case 9:
                lastFraudTime = lastFraudTime + 1.2;
                break;
            case 10:
                lastFraudTime = lastFraudTime + 1.4;
                break;
            case 11:
                lastFraudTime = lastFraudTime + 1.6;
                break;
            case 12:
                lastFraudTime = lastFraudTime + 1.2;
                break;
            case 13:
                lastFraudTime = lastFraudTime + 1.3;
                break;
            case 14:
                lastFraudTime = lastFraudTime + 1.4;
                break;
            case 15:
                lastFraudTime = lastFraudTime + 1.5;
                break;
            case 16:
                lastFraudTime = lastFraudTime + 1.6;
                break;
            case 17:
                lastFraudTime = lastFraudTime + 1.5;
                break;
            case 18:
                lastFraudTime = lastFraudTime + 1.4;
                break;
            case 19:
                lastFraudTime = lastFraudTime + 1.4;
                break;
            case 20:
                lastFraudTime = lastFraudTime + 1.5;
                break;
            case 21:
                lastFraudTime = lastFraudTime + 1.5;
                break;
            case 22:
                lastFraudTime = lastFraudTime + 1.1;
                break;
            case 23:
                lastFraudTime = lastFraudTime + 0.9;
                break;
            default:
                lastFraudTime = lastFraudTime + 1;
                break;
        }
        return lastFraudTime;
    }

    /**
     * 按不同时间间隔缓存K线
     *
     * @param symbol
     * @param tradeDto
     */
    private void saveKLineData(String symbol, TradeDto tradeDto, String currLockKey, long startTime) {
        try {
            saveKLineData(symbol, tradeDto, getKLineTimeStamp(tradeDto.timestamp, "1min"), "1min");
            saveKLineData(symbol, tradeDto, getKLineTimeStamp(tradeDto.timestamp, "5min"), "5min");
            saveKLineData(symbol, tradeDto, getKLineTimeStamp(tradeDto.timestamp, "15min"), "15min");
            saveKLineData(symbol, tradeDto, getKLineTimeStamp(tradeDto.timestamp, "30min"), "30min");
            saveKLineData(symbol, tradeDto, getKLineTimeStamp(tradeDto.timestamp, "60min"), "60min");
            saveKLineData(symbol, tradeDto, getKLineTimeStamp(tradeDto.timestamp, "1day"), "1day");
            saveKLineData(symbol, tradeDto, getKLineTimeStamp(tradeDto.timestamp, "1mon"), "1mon");
            saveKLineData(symbol, tradeDto, getKLineTimeStamp(tradeDto.timestamp, "1week"), "1week");
            // saveKLineData(symbol, tradeDto, getKLineTimeStamp(tradeDto.timestamp,
            // "1year"), "1year");
        } catch (Exception e) {
            log.error("Static 线程执行异常: error : " + e.getLocalizedMessage());
        }
    }

    /**
     * k线简单处理
     *
     * @param symbol 交易对
     * @param type   K线类型
     */
    private void removeDuplicate(String symbol, String type) {
        String key = symbol + "_" + type;
        byte[] first = JedisUtil.getInstance().lpop(key, 0, 1);
        BigDecimal[] firstB = (BigDecimal[]) SerializeUtil.deserialize(first);
        byte[] second = JedisUtil.getInstance().lpop(key, 0, 1);
        BigDecimal[] secondB = (BigDecimal[]) SerializeUtil.deserialize(second);
        if (firstB[0].intValue() == secondB[0].intValue()) {
            //如果一样就合并
            firstB[5] = firstB[5].add(secondB[5]);
            JedisUtil.getInstance().lpush(symbol + "_" + type, firstB);
        }
        if (firstB[0].intValue() > secondB[0].intValue()) {
            //正确就原样压入
            JedisUtil.getInstance().lpush(symbol + "_" + type, secondB);
            JedisUtil.getInstance().lpush(symbol + "_" + type, firstB);
        } else {
            //顺序有问题就反压
            JedisUtil.getInstance().lpush(symbol + "_" + type, firstB);
            JedisUtil.getInstance().lpush(symbol + "_" + type, secondB);
        }
    }

    /**
     * 缓存K线的具体实现方法
     *
     * @param symbol
     * @param tradeDto
     * @param timeStamp
     * @param type
     */
    private void saveKLineData(String symbol, TradeDto tradeDto, int timeStamp, String type) {
        // logger.error(symbol + "_" + type);

        // K线最多只保留1500条数据
        long len = JedisUtil.getInstance().llen(symbol + "_" + type);
        if (len > 2000) {
            JedisUtil.getInstance().ltrim(symbol + "_" + type, 0, 1500);
        }

        byte[] oldData = JedisUtil.getInstance().lpop(symbol + "_" + type, 0, 1);

        BigDecimal[] datas = {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
        if (null != oldData) {
            datas = (BigDecimal[]) SerializeUtil.deserialize(oldData);
        }
        List<BigDecimal[]> datasList = Lists.newArrayList();

        // 时间戳1, 开盘价1, 最高价1, 最低价1, 收盘价1, 成交数量1, 成交笔数1, 成交额1
        if (datas[0].intValue() != timeStamp) {
            if (timeStamp > datas[0].intValue()) {
                this.addDatas(datas, symbol, tradeDto, timeStamp, type);
            } else {
                datasList.add(datas);
                this.modifyOneDatas(symbol, tradeDto, timeStamp, type, datasList);
            }
        } else {
            this.updateDatas(datas, symbol + "_" + type, tradeDto);
        }

        // 批量压回
        this.batchPushDatas(symbol + "_" + type, datasList);
        // 临时处理K线时间戳重复问题
        // removeDuplicate(symbol, type);
    }

    //新方法
    public void modifyOneDatas(String symbol, BigDecimal[] newDatas, int timeStamp, String type, List<BigDecimal[]> datasList) {
        String key = symbol + "_" + type;
        byte[] oldData = JedisUtil.getInstance().lpop(key, 0, 1);
        BigDecimal[] datas = (BigDecimal[]) SerializeUtil.deserialize(oldData);
        if (datas[0].intValue() != timeStamp) {
            // 判断是否大于
            if (timeStamp > datas[0].intValue()) {
                this.addDatas(datas, symbol, newDatas, timeStamp, type);
                return;
            } else {
                datasList.add(datas);
                this.modifyOneDatas(symbol, newDatas, timeStamp, type, datasList);
            }
        } else {
            this.updateDatas(datas, key, newDatas);
        }
    }

    public void modifyOneDatas(String symbol, TradeDto tradeDto, int timeStamp, String type, List<BigDecimal[]> datasList) {
        String key = symbol + "_" + type;
        byte[] oldData = JedisUtil.getInstance().lpop(key, 0, 1);
        BigDecimal[] datas = (BigDecimal[]) SerializeUtil.deserialize(oldData);
        if (datas[0].intValue() != timeStamp) {
            // 判断是否大于
            if (timeStamp > datas[0].intValue()) {
                this.addDatas(datas, symbol, tradeDto, timeStamp, type);
                return;
            } else {
                datasList.add(datas);
                this.modifyOneDatas(symbol, tradeDto, timeStamp, type, datasList);
            }
        } else {
            this.updateDatas(datas, key, tradeDto);
        }
    }

    public void addDatas(BigDecimal[] datas, String symbol, BigDecimal[] newDatas, int timeStamp, String type) {
        // 不相等，就是表示原来没有最新这个时间戳的数据
        if (datas[0].intValue() > 0) { // 原来有过数据(没有过数据的情况应该是新增加了一个交易对，并且该交易对没有交易)
            // 如果原来Redis中有上一次的数据（跟本次不是一个时间戳），还得push回去
            JedisUtil.getInstance().lpush(symbol + "_" + type, datas);

            // [原来从来没有过，就不用补，表示第一次] 这里还要补上中间空白的，如上一次成交是几分钟前，那么要把K线全部补齐
            // 上一次的时间戳
            int lastTimeStamp = datas[0].intValue();
            while (lastTimeStamp < timeStamp) {
                // 根据类型获取下一个时间戳， 判断交易是否在本时间戳范围内
                lastTimeStamp = getNextTimeStamp(lastTimeStamp, type);

                // 如果加上秒数后，还小于，或者本次成交量等于0，就插入，否则就要退出while循环了，下面会继续处理本次有数据的情况
                if (newDatas[5].compareTo(BigDecimal.ZERO) == 0 || lastTimeStamp < timeStamp) {
                    datas[0] = BigDecimal.valueOf(lastTimeStamp);
                    // 开高低收用上一次的收盘价
                    datas[1] = datas[4];
                    datas[2] = datas[4];
                    datas[3] = datas[4];
                    datas[4] = datas[4];
                    // 成交数据全部为0
                    datas[5] = BigDecimal.ZERO;

                    JedisUtil.getInstance().lpush(symbol + "_" + type, datas);
                }
            }
        } else if (newDatas[5].compareTo(BigDecimal.ZERO) == 0) { // 原来一直没有任何K线，并且这次交易数据为0，表示一个新的交易对刚配置，还没有任何一个成交，
            // 数据全部置为0
            datas[0] = BigDecimal.valueOf(timeStamp);
            JedisUtil.getInstance().lpush(symbol + "_" + type, datas);
        }

        // 如果本次成交量等于0，不用处理了，上面两个情况都已经处理
        if (newDatas[5].compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        datas = new BigDecimal[6];
        datas[0] = BigDecimal.valueOf(timeStamp);
        datas[1] = newDatas[1];
        datas[2] = newDatas[2];
        datas[3] = newDatas[3];
        datas[4] = newDatas[4];
        datas[5] = newDatas[5];
        JedisUtil.getInstance().lpush(symbol + "_" + type, datas);
    }

    public void addDatas(BigDecimal[] datas, String symbol, TradeDto tradeDto, int timeStamp, String type) {
        // 不相等，就是表示原来没有最新这个时间戳的数据
        if (datas[0].intValue() > 0) { // 原来有过数据(没有过数据的情况应该是新增加了一个交易对，并且该交易对没有交易)
            // 如果原来Redis中有上一次的数据（跟本次不是一个时间戳），还得push回去
            JedisUtil.getInstance().lpush(symbol + "_" + type, datas);

            // [原来从来没有过，就不用补，表示第一次] 这里还要补上中间空白的，如上一次成交是几分钟前，那么要把K线全部补齐
            // 上一次的时间戳
            int lastTimeStamp = datas[0].intValue();
            while (lastTimeStamp < timeStamp) {
                // 根据类型获取下一个时间戳， 判断交易是否在本时间戳范围内
                lastTimeStamp = getNextTimeStamp(lastTimeStamp, type);

                // 如果加上秒数后，还小于，或者本次成交量等于0，就插入，否则就要退出while循环了，下面会继续处理本次有数据的情况
                if (tradeDto.volume.compareTo(BigDecimal.ZERO) == 0 || lastTimeStamp < timeStamp) {
                    datas[0] = BigDecimal.valueOf(lastTimeStamp);
                    // 开高低收用上一次的收盘价
                    datas[1] = datas[4];
                    datas[2] = datas[4];
                    datas[3] = datas[4];
                    datas[4] = datas[4];
                    // 成交数据全部为0
                    datas[5] = BigDecimal.ZERO;

                    JedisUtil.getInstance().lpush(symbol + "_" + type, datas);
                }
            }
        } else if (tradeDto.volume.compareTo(BigDecimal.ZERO) == 0) { // 原来一直没有任何K线，并且这次交易数据为0，表示一个新的交易对刚配置，还没有任何一个成交，
            // 数据全部置为0
            datas[0] = BigDecimal.valueOf(timeStamp);
            JedisUtil.getInstance().lpush(symbol + "_" + type, datas);
        }

        // 如果本次成交量等于0，不用处理了，上面两个情况都已经处理
        if (tradeDto.volume.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }

        datas = new BigDecimal[6];
        datas[0] = BigDecimal.valueOf(timeStamp);
        datas[1] = tradeDto.price;
        datas[2] = tradeDto.price;
        datas[3] = tradeDto.price;
        datas[4] = tradeDto.price;
        datas[5] = tradeDto.volume;
        JedisUtil.getInstance().lpush(symbol + "_" + type, datas);

    }


    //新方法
    public void updateDatas(BigDecimal[] datas, String key, BigDecimal newDatas[]) {
        // 已经有这个K线
        if (newDatas[5].compareTo(BigDecimal.ZERO) == 0) {
            // 没有产生新交易，把上面弹出来的存回去，本次操作相当于什么事情都没干
            JedisUtil.getInstance().lpush(key, datas);
            return;
        }

        // 时间戳1, 开盘价1, 最高价1, 最低价1, 收盘价1, 成交数量1, 成交笔数1, 成交额1
        // 时间不要，如果原来的数据是0（这种情况，是刚新增了一个交易对，没有任何成交的情况），则开盘价要设置为新的，如果原来的数据不是0，则开盘价不要
        if (datas[1].compareTo(BigDecimal.ZERO) == 0) {
            datas[1] = newDatas[1];
            datas[2] = newDatas[2];
            datas[3] = newDatas[3];
        } else {
            datas[2] = newDatas[2].compareTo(datas[2]) > 0 ? newDatas[2] : datas[2];
            datas[3] = newDatas[2].compareTo(datas[3]) > 0 ? datas[3] : newDatas[3];
        }
        datas[4] = newDatas[4]; // 收盘价恒定为最后的价格
        datas[5] = newDatas[5].add(datas[5]);
        JedisUtil.getInstance().lpush(key, datas);
    }

    public void updateDatas(BigDecimal[] datas, String key, TradeDto tradeDto) {
        // 已经有这个K线
        if (tradeDto.volume.compareTo(BigDecimal.ZERO) == 0) {
            // 没有产生新交易，把上面弹出来的存回去，本次操作相当于什么事情都没干
            JedisUtil.getInstance().lpush(key, datas);
            return;
        }

        // 时间戳1, 开盘价1, 最高价1, 最低价1, 收盘价1, 成交数量1, 成交笔数1, 成交额1
        // 时间不要，如果原来的数据是0（这种情况，是刚新增了一个交易对，没有任何成交的情况），则开盘价要设置为新的，如果原来的数据不是0，则开盘价不要
        if (datas[1].compareTo(BigDecimal.ZERO) == 0) {
            datas[1] = tradeDto.price;
            datas[2] = tradeDto.price;
            datas[3] = tradeDto.price;
        } else {
            datas[2] = tradeDto.price.compareTo(datas[2]) > 0 ? tradeDto.price : datas[2];
            datas[3] = tradeDto.price.compareTo(datas[3]) > 0 ? datas[3] : tradeDto.price;
        }
        datas[4] = tradeDto.price; // 收盘价恒定为最后的价格
        datas[5] = tradeDto.volume.add(datas[5]);
        JedisUtil.getInstance().lpush(key, datas);
    }

    /**
     * 倒序批量插入
     *
     * @param key
     */
    public void batchPushDatas(String key, List<BigDecimal[]> datasList) {
        Collections.reverse(datasList);
        datasList.forEach(item -> {
            JedisUtil.getInstance().lpush(key, item);
        });
    }

    /**
     * 获取本时间戳的结束时间，也就是下一个时间戳的时间
     *
     * @param lastTimeStamp
     * @param type
     * @return
     */
    private int getNextTimeStamp(int lastTimeStamp, String type) {
        if ("1min".equals(type)) {
            lastTimeStamp = lastTimeStamp + 60;
        } else if ("5min".equals(type)) {
            lastTimeStamp = lastTimeStamp + 300;
        } else if ("15min".equals(type)) {
            lastTimeStamp = lastTimeStamp + 900;
        } else if ("30min".equals(type)) {
            lastTimeStamp = lastTimeStamp + 1800;
        } else if ("60min".equals(type)) {
            lastTimeStamp = lastTimeStamp + 3600;
        } else if ("1day".equals(type)) {
            lastTimeStamp = lastTimeStamp + 86400;
        } else if ("1mon".equals(type)) {
            Date date = new Timestamp(Long.parseLong(lastTimeStamp + "000"));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.MONTH, 1);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            String strDate = year + "-" + month + "-1 00:00:00";

            try {
                Date myDate2 = dateFormat2.parse(strDate);
                lastTimeStamp = (int) (myDate2.getTime() / 1000);
            } catch (Exception e) {
                e.printStackTrace();
                lastTimeStamp = 0;
            }

        } else if ("1year".equals(type)) {
            Date date = new Timestamp(Long.parseLong(lastTimeStamp + "000"));
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.YEAR, 1);
            int year = cal.get(Calendar.YEAR);
            String strDate = year + "-1-1 00:00:00";

            try {
                Date myDate2 = dateFormat2.parse(strDate);
                lastTimeStamp = (int) (myDate2.getTime() / 1000);
            } catch (Exception e) {
                e.printStackTrace();
                lastTimeStamp = 0;
            }
        } else if ("1week".equals(type)) {
            lastTimeStamp = lastTimeStamp + 604800;
        }
        return lastTimeStamp;
    }

    public void createTrades(int n, String symbol) {
        BigDecimal[] datas = {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
        for (int i = 0; i < n; i++) {
            TradeDto tradeDto = new TradeDto();
            tradeDto.volume = BigDecimal.ZERO.add(BigDecimal.valueOf(Math.random() * 2));
            tradeDto.price = BigDecimal.valueOf(65000 + Math.random() * 5000);
            tradeDto.timestamp = (long) (System.currentTimeMillis() - 10000 * Math.random());
            tradeDto.taker = "123";
            tradeDto.upOrDown = 1;
            tradeDto.id = System.currentTimeMillis();

            tradeToRedis(tradeDto, symbol, true);

            datas[1] = datas[1].compareTo(BigDecimal.ZERO) == 0 ? tradeDto.price : datas[1];
            datas[2] = datas[2].compareTo(tradeDto.price) >= 0 ? datas[2] : tradeDto.price;
            datas[3] = datas[3].compareTo(tradeDto.price) <= 0 ? datas[3] : tradeDto.price;
            datas[4] = tradeDto.price;
            datas[5] = datas[5].add(tradeDto.volume);
        }
        log.warn("交易数据" + Arrays.toString(datas));
    }


    public void tradeToRedis(TradeDto tradeDto, String symbol, boolean needIncrLastGenKLen) {
        List<byte[]> lst = JedisUtil.getInstance().lrange(symbol + "_trade", 0, 0);
        if (null != lst && lst.size() == 1) {
            TradeDto latestTDto = (TradeDto) SerializeUtil.deserialize(lst.get(0));
            if (null != latestTDto.price) {
                if (latestTDto.price.compareTo(tradeDto.price) == 1) {
                    tradeDto.upOrDown = -1;
                } else if (latestTDto.price.compareTo(tradeDto.price) == -1) {
                    tradeDto.upOrDown = 1;
                } else { //如果相等，取上次的
                    tradeDto.upOrDown = latestTDto.upOrDown;
                }
            }
        } else { // 第一次成交，肯定是上涨
            tradeDto.upOrDown = 1;
        }

        JedisUtil.getInstance().lpush(symbol + "_trade", tradeDto);

        // 最大保留1500条
        JedisUtil.getInstance().ltrim(symbol + "_trade", 0, 1500);

        if (needIncrLastGenKLen) {
            JedisUtil.getInstance().incr(symbol + "_last_gen_k_len");
        }
    }
}
