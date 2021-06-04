package com.contract.service.redis;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.contract.common.FunctionUtils;
import com.contract.dto.Depth;
import com.contract.dto.SymbolDto;
import com.contract.entity.CContractOrder;
import com.contract.entity.CWallet;
import com.contract.entity.Coins;
import com.contract.enums.SysParamsEnums;
import com.contract.service.wallet.WalletService;

import common.Logger;


@Service
public class RedisUtilsService {

    /**
     * 多套系统，共用一个redis时，防止key重复
     */
    private final static String KEY_PREFIX = "contract_";
    public static final String LOCK_PREFIX = "redis_lock";
    public static final int LOCK_EXPIRE = 10000; // ms

    protected final static Logger logger = Logger.getLogger(RedisUtilsService.class);
    @Autowired
    private volatile RedisTemplate<String, String> redisTemplate;
    @Autowired
    private WalletService walletService;

    /**
     * 秒级内做并发处理
     *
     * @param key
     * @param cacheSeconds
     * @return true 表示可以继续下面逻辑没有并发  false表示并发数据
     */
    public boolean setIncrSecond(String key, long cacheSeconds) {
        if (!key.startsWith(KEY_PREFIX)) {
            key = KEY_PREFIX + key;
        }
        try {
            setStringSerializer(redisTemplate);
            long count = redisTemplate.opsForValue().increment(key, 1);
            //此段代码出现异常则会出现死锁问题，key一直都存在
            if (count == 1) {
                //设置有效期X秒
                redisTemplate.expire(key, cacheSeconds, TimeUnit.SECONDS);
                return true;
            }
            //如果存在表示重复
            return false;
        } catch (Exception e) {
            logger.error("redis加锁异常", e);
            //出现异常删除锁
            redisTemplate.delete(key);
            return true;
        }
    }

    /**
     * 获取唯一Id
     *
     * @param key
     * @param hashKey
     * @param delta   增加量（不传采用1）
     * @return
     * @throws BusinessException
     */
    public Long incrementHash(String key, String hashKey, Long delta) {
        if (!key.startsWith(KEY_PREFIX)) {
            key = KEY_PREFIX + key;
        }
        try {
            if (null == delta) {
                delta = 1L;
            }
            setStringSerializer(redisTemplate);
            return redisTemplate.opsForHash().increment(key, hashKey, delta);
        } catch (Exception e) {//redis宕机时采用uuid的方式生成唯一id
            int first = new Random(10).nextInt(8) + 1;
            int randNo = UUID.randomUUID().toString().hashCode();
            if (randNo < 0) {
                randNo = -randNo;
            }
            return Long.valueOf(first + String.format("%16d", randNo));
        }
    }

    /**
     * 设置无限期 键值对信息
     *
     * @param key
     * @param value
     */
    public void setKey(String key, String value) {
        if (!key.startsWith(KEY_PREFIX)) {
            key = KEY_PREFIX + key;
        }
        setStringSerializer(redisTemplate);
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置有限期 键值对信息
     *
     * @param key
     * @param value
     * @param second 秒
     */
    public void setKey(String key, String value, long second) {
        if (!key.startsWith(KEY_PREFIX)) {
            key = KEY_PREFIX + key;
        }
        setStringSerializer(redisTemplate);
        redisTemplate.opsForValue().set(key, value, second, TimeUnit.SECONDS);
    }


    /**
     * 根据键获取值
     *
     * @param key
     * @return
     */
    public String getKey(String key) {
        if (!key.startsWith(KEY_PREFIX)) {
            key = KEY_PREFIX + key;
        }
        setStringSerializer(redisTemplate);
        String val = redisTemplate.opsForValue().get(key);
        return val;
    }

    /**
     * 根据键获取值
     *
     * @param key
     * @return
     */
    public Set<String> getKeys(String key) {
        if (!key.startsWith(KEY_PREFIX)) {
            key = KEY_PREFIX + key;
        }
        setStringSerializer(redisTemplate);
        Set<String> val = redisTemplate.keys(key);
        return val;
    }

    /**
     * 批量删除
     *
     * @param keys
     */
    public void deleteKey(Set<String> keys) {
        for (String key : keys) {
            if (!key.startsWith(KEY_PREFIX)) {
                key = KEY_PREFIX + key;
            }
            redisTemplate.delete(key);
        }
    }

    /**
     * 删除key
     *
     * @param key
     */
    public void deleteKey(String key) {
        if (!key.startsWith(KEY_PREFIX)) {
            key = KEY_PREFIX + key;
        }
        setStringSerializer(redisTemplate);
        redisTemplate.delete(key);
    }

    private void setStringSerializer(RedisTemplate<String, String> template) {
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        template.setDefaultSerializer(stringRedisSerializer);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(stringRedisSerializer);
    }

    /**
     * 获取货币详情
     */
    public List<SymbolDto> querySymDto() {
        Set<String> key = getKeys("maketdetail_*");
        List<SymbolDto> list = new ArrayList<>();
        for (String k : key) {
            String result = getKey(k);
            SymbolDto dto = JSONObject.parseObject(result, SymbolDto.class);
            list.add(dto);
        }
        return list;
    }

    /**
     * 深度
     *
     * @param key
     * @return
     */
    public Depth getDepth(String key) {
        String val = getKey(key);
        Depth depth = JSONObject.parseObject(val, Depth.class);
        return depth;
    }

    /**
     * 获取USDT 等值CNY
     *
     * @return
     */
    public BigDecimal getUsdtToCny() {
        String val = getKey("usdt_cny");
        BigDecimal price = new BigDecimal(val);
        return price;
    }

    /**
     * 获取行情币种
     *
     * @return
     */
    public List<Coins> queryCoins() {
        String val = getKey("coins_key");
        List<Coins> list = JSONArray.parseArray(val, Coins.class);
        return list;
    }

    /**
     * 获取行情币种
     *
     * @return
     */
    public Coins getCoinBySymbol(String symbol) {
        List<Coins> list = queryCoins();
        for (Coins c : list) {
            if (symbol.equals(c.getSymbol())) {
                return c;
            }
        }
        return null;
    }

    /**
     * 获取持仓订单
     */
    public List<CContractOrder> queryOrder(String key) {
        List<CContractOrder> list = new ArrayList<>();
        Set<String> keySetAll = getKeys(key);
        for (String k : keySetAll) {
            String string = getKey(k);
            if (!StringUtils.isEmpty(string)) {
                CContractOrder cContractOrder = JSONObject.parseObject(string, CContractOrder.class);
                SymbolDto symDto = getSymbolDto(cContractOrder.getCoin().replace("_", "/").toUpperCase());
                //1。获取当前 货币市价
                BigDecimal usdtPrice = symDto.getUsdtPrice();// 实时价 usdt
                cContractOrder.setStopprice(usdtPrice);
                BigDecimal old = FunctionUtils.mul(cContractOrder.getCoinnum(), cContractOrder.getBuyprice(), 4);
                BigDecimal now = FunctionUtils.mul(cContractOrder.getCoinnum(), cContractOrder.getStopprice(), 4);
                if (FunctionUtils.isEquals(1, cContractOrder.getType())) {
                    //买涨  按当前价增加为利润
                    BigDecimal rates = FunctionUtils.sub(now, old, 4);
                    cContractOrder.setRates(FunctionUtils.add(rates, cContractOrder.getRates(), 4));
                } else {
                    //买跌  按原始价
                    BigDecimal rates = FunctionUtils.sub(old, now, 4);
                    cContractOrder.setRates(FunctionUtils.add(rates, cContractOrder.getRates(), 4));
                }
                list.add(cContractOrder);
            }
        }
        return list;
    }

    /**
     * 获取持仓订单
     */
    public List<CContractOrder> queryOrder(Integer cid, SymbolDto symDto, String coin) {
        //设置自动爆仓价格
        String contract_auto_out = getKey(SysParamsEnums.contract_auto_out.getKey());
        BigDecimal contractAutoOut = new BigDecimal(contract_auto_out);
        String m = "conmoney_" + cid + "_" + coin;
        List<CContractOrder> list = walletService.autoOrderprice(new JSONObject(), cid, contractAutoOut, coin, m, symDto);
        return list;
    }

    public SymbolDto getSymbolDto(String coinname) {
        List<SymbolDto> list = querySymDto();
        for (SymbolDto l : list) {
            if (l.getName().equals(coinname)) {
                return l;
            }
        }
        return new SymbolDto();
    }

    /**
     * 获取货币总额
     *
     * @param cid
     * @param coinName
     * @return
     */
    public JSONObject getOutMoney(Integer cid, String coinName) {
        String moneykey = "conmoney_" + cid + "_" + coinName;
        String result = getKey(moneykey);

        JSONObject moneyMap = new JSONObject();
        if (StringUtils.isEmpty(result)) {
            moneyMap.put("totalmoney", BigDecimal.ZERO);
            moneyMap.put("totalrates", BigDecimal.ZERO);
            moneyMap.put("totalval", BigDecimal.ZERO);
            moneyMap.put("scale", BigDecimal.ZERO);
        } else {
            moneyMap = JSONObject.parseObject(result);
        }
        return moneyMap;
    }

    /**
     * 设置 各种货币对应不同会员的  总金额
     *
     * @param cContractOrder
     * @param wallet
     * @param cid
     * @param coinName
     * @param flag           true增加 持仓 false 减少爆仓
     */
    public void setOutMoney(CContractOrder cContractOrder, CWallet wallet, Integer cid, String coinName, boolean flag) {
        //修改货币总额
        //设置自动爆仓价格
        String contract_auto_out = getKey(SysParamsEnums.contract_auto_out.getKey());
        BigDecimal contractAutoOut = new BigDecimal(contract_auto_out);
        String moneykey = "conmoney_" + cid + "_" + coinName;
        String result = getKey(moneykey);
        JSONObject moneyMap = new JSONObject();
        //总保证金
        BigDecimal totalmoney = cContractOrder.getRealmoney();
        //总盈亏
        BigDecimal totalrates = cContractOrder.getRates();
        //预计评测估值 =余额+总保证金的70% +总盈亏
        BigDecimal totalval = wallet.getBalance();
        if (!StringUtils.isEmpty(result)) {
            moneyMap = JSONObject.parseObject(result);
            if (flag) {//持仓增加
                totalmoney = FunctionUtils.add(moneyMap.getBigDecimal("totalmoney"), totalmoney, 4);
                totalrates = FunctionUtils.add(moneyMap.getBigDecimal("totalrates"), totalrates, 4);
            } else {//爆仓减少
                totalmoney = FunctionUtils.sub(moneyMap.getBigDecimal("totalmoney"), totalmoney, 4);
                totalrates = FunctionUtils.sub(moneyMap.getBigDecimal("totalrates"), totalrates, 4);
            }
        }
        //预计评测估值 =余额+总保证金的70% +总盈亏
        totalval = FunctionUtils.add(totalval, FunctionUtils.mul(totalmoney, contractAutoOut, 4), 4);
        totalval = FunctionUtils.add(totalval, totalrates, 4);
        moneyMap.put("totalmoney", totalmoney);
        moneyMap.put("totalrates", totalrates);
        moneyMap.put("totalval", totalval);
        BigDecimal scale = FunctionUtils.div(totalval, totalmoney, 6);
        scale = FunctionUtils.mul(scale, new BigDecimal(100), 4);
        moneyMap.put("scale", scale);
        setKey(moneykey, moneyMap.toJSONString());
    }



    /**
     *  最终加强分布式锁
     *
     * @param key key值
     * @return 是否获取到
     */
    public boolean lock(String key){
        if (!key.startsWith(KEY_PREFIX)) {
            key = KEY_PREFIX + key;
        }
        String lock = key+LOCK_PREFIX ;
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + LOCK_EXPIRE + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());
            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(lock.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = Long.parseLong(new String(value));

                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }

    /**
     *  可设置有效期的分布式锁
     * @param key   key值
     * @param ms    锁的有效期 多少毫秒
     * @return  是否获取到
     */
    public boolean lock(String key, int ms){
        if (!key.startsWith(KEY_PREFIX)) {
            key = KEY_PREFIX + key;
        }
        String lock = key+LOCK_PREFIX ;
        // 利用lambda表达式
        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {

            long expireAt = System.currentTimeMillis() + ms + 1;
            Boolean acquire = connection.setNX(lock.getBytes(), String.valueOf(expireAt).getBytes());
            if (acquire) {
                return true;
            } else {

                byte[] value = connection.get(lock.getBytes());

                if (Objects.nonNull(value) && value.length > 0) {

                    long expireTime = Long.parseLong(new String(value));

                    if (expireTime < System.currentTimeMillis()) {
                        // 如果锁已经过期
                        byte[] oldValue = connection.getSet(lock.getBytes(), String.valueOf(System.currentTimeMillis() + LOCK_EXPIRE + 1).getBytes());
                        // 防止死锁
                        return Long.parseLong(new String(oldValue)) < System.currentTimeMillis();
                    }
                }
            }
            return false;
        });
    }
}
