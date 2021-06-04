package com.pmzhongguo.zzextool.utils;

import com.pmzhongguo.zzextool.consts.StaticConst;
import com.pmzhongguo.zzextool.enums.CommonEnum;
import com.pmzhongguo.zzextool.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Slf4j
public class JedisUtil
{

    /**
     * 私有构造器.
     */
    private JedisUtil()
    {

    }

    private static JedisPool pool = null;


    public List<String> hvals(final String key)
    {
        Jedis shardedJedis = getJedis();
        List<String> result = null;

        if (shardedJedis == null)
        {
            return result;
        }

        try
        {
            result = shardedJedis.hvals(key);

        } catch (Exception e)
        {
            log.error(e.getMessage(), e);

        } finally
        {
            closeJedis(shardedJedis);
        }
        return result;
    }

    /**
     * 设置单个值
     *
     * @param key
     * @param value
     * @return
     */
    public void set(String key, Object value, Boolean isSimple)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null || null == value || null == key)
        {
            return;
        }
        try
        {
            if (null == isSimple || isSimple == true)
            {
                shardedJedis.set(key, (String) value);
            } else
            {
                shardedJedis
                        .set(key.getBytes(), SerializeUtil.serialize(value));
            }

        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }

    }

    /**
     * 获取单个值
     *
     * @param key
     * @return
     */
    public Object get(String key, Boolean isSimple)
    {
        Jedis shardedJedis = getJedis();

        Object result = null;

        if (shardedJedis == null)
        {
            return result;
        }

        try
        {
            if (null == isSimple || isSimple == true)
            {
                result = shardedJedis.get(key);
            } else
            {
                result = SerializeUtil.deserialize(shardedJedis.get(key
                        .getBytes()));
            }

        } catch (Exception e)
        {
            System.out.println(e);
            log.error(e.getMessage(), e);

        } finally
        {
            closeJedis(shardedJedis);
        }
        return result;
    }

    /**
     * 获取单个值
     *
     * @param key
     * @return
     */
    public long getSetLong(String key, long newVal)
    {
        Jedis shardedJedis = getJedis();

        long result = 0;

        if (shardedJedis == null)
        {
            return result;
        }

        try
        {
            String oldVal = shardedJedis.getSet(key, newVal + "");
            result = (null == oldVal ? result : Long.valueOf(oldVal));

        } catch (Exception e)
        {
            log.error(e.getMessage(), e);

        } finally
        {
            closeJedis(shardedJedis);
        }
        return result;
    }

    /**
     * 将key对应的值加1
     *
     * @param key
     */
    public void incr(String key)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null)
        {
            return;
        }
        try
        {
            shardedJedis.incr(key);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);

        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    /**
     * 设置单个值
     *
     * @param key
     * @return
     */
    public void del(String key)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null || null == key)
        {
            return;
        }
        try
        {
            shardedJedis.del(key);

        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }

    }

    /**
     * 向队列插入一个对象
     *
     * @param keyName
     * @param score
     * @param obj
     */
    public void zadd(byte[] keyName, double score, byte[] obj)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null || null == obj || null == keyName)
        {
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        }
        try
        {
            shardedJedis.zadd(keyName, score, obj);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    /**
     * 向队列插入一个简单对象
     *
     * @param keyName
     * @param score
     * @param obj
     */
    public void zadd(String keyName, double score, BigDecimal obj)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null || null == obj || null == keyName)
        {
            return;
        }
        try
        {
            shardedJedis.zadd(keyName, score, String.valueOf(obj));
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
    }


    /**
     * 向列表插入一个数组对象
     *
     * @param keyName
     * @param obj
     */
    public void lpush(String keyName, Object obj)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null || null == obj || null == keyName)
        {
            return;
        }
        try
        {
            shardedJedis.lpush(keyName.getBytes(), SerializeUtil.serialize(obj));
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    /**
     * 向列表插入一个数组对象
     *
     * @param keyName
     * @param start
     * @param end
     */
    public void ltrim(String keyName, long start, long end)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null || null == keyName)
        {
            return;
        }
        try
        {
            shardedJedis.ltrim(keyName.getBytes(), start, end);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    /**
     * 向列表插入一个数组对象
     *
     * @param keyName
     */
    public long llen(String keyName)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null)
        {
            return 0;
        }
        try
        {
            return shardedJedis.llen(keyName.getBytes());
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }

        return 0;
    }

    /**
     * 向列表插入一个简单对象
     *
     * @param keyName
     * @param obj
     */
    public void lpush(String keyName, String obj)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null || null == obj || null == keyName)
        {
            return;
        }
        try
        {
            shardedJedis.lpush(keyName, obj);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    /**
     * 将对象移除
     *
     * @param keyName
     * @param obj
     */
    public void zrem(byte[] keyName, byte[] obj)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null || null == obj || null == keyName)
        {
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        }
        try
        {
            shardedJedis.zrem(keyName, obj);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    /**
     * 正向（从低到高）获得队列
     *
     * @param keyName
     * @param min
     * @param max
     * @return
     */
    public Set<Tuple> zrangeByScoreWithScores(byte[] keyName, double max, double min)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null)
        {
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        }
        try
        {
            return shardedJedis.zrangeByScoreWithScores(keyName, min, max);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    public List<byte[]> lrange(final String key, final long start,
                               final long end)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null)
        {
            return null;
        }
        try
        {
            return shardedJedis.lrange(key.getBytes(), start, end);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
        return null;
    }

    public byte[] lpop(final String key, final long start,
                       final long end)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null)
        {
            return null;
        }
        try
        {
            return shardedJedis.lpop(key.getBytes());
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
        return null;
    }

    public List<String> lrangeString(final String key, final int start, final int end)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null)
        {
            return null;
        }
        try
        {
            return shardedJedis.lrange(key, start, end);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
        return null;
    }

    /**
     * 正向（从低到高）获得队列(不需要Score)
     *
     * @param keyName
     * @param min
     * @param max
     * @return
     */
    public Set<byte[]> zrangeByScore(byte[] keyName, double max, double min)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null)
        {
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        }
        try
        {
            return shardedJedis.zrangeByScore(keyName, min, max);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        } finally
        {
            closeJedis(shardedJedis);
        }
    }


    /**
     * 反向（从高到低）获得队列(不需要Score)
     *
     * @param keyName
     * @param min
     * @param max
     * @return
     */
    public Set<byte[]> zrevrangeByScore(byte[] keyName, double max, double min)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null)
        {
            return null;
        }
        try
        {
            return shardedJedis.zrevrangeByScore(keyName, max, min);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
        return null;
    }

    /**
     * 反向（从高到底）获得队列
     *
     * @param keyName
     * @param min
     * @param max
     * @return
     */
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] keyName, double max, double min)
    {
        Jedis shardedJedis = getJedis();

        if (shardedJedis == null)
        {
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        }
        try
        {
            return shardedJedis.zrevrangeByScoreWithScores(keyName, max, min);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    public void sadd(String key, String value)
    {
        Jedis shardedJedis = getJedis();
        if (shardedJedis == null)
        {
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        }
        try
        {
            shardedJedis.sadd(key, value);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        } finally
        {
            closeJedis(shardedJedis);
        }

    }

    public void srem(String key, String value)
    {
        Jedis shardedJedis = getJedis();
        if (shardedJedis == null)
        {
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        }
        try
        {
            shardedJedis.srem(key, value);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    public boolean sismember(String key, String value)
    {
        Jedis shardedJedis = getJedis();
        if (shardedJedis == null)
        {
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        }
        try
        {
            return shardedJedis.sismember(key, value);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
            throw new BusinessException(-1, CommonEnum.LANG_REDIS_ERROR.getErrorENMsg());
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    /**
     * 构建redis连接池
     *
     * @return JedisPool
     */
    private static JedisPool getPool()
    {
        if (pool == null)
        {
            JedisPoolConfig config = new JedisPoolConfig();
            // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);
            config.setMaxTotal(100);
//			pool = new JedisPool(config, HelpUtils.getMgrConfig().getRedis_ip(), Integer.parseInt(HelpUtils.getMgrConfig().getRedis_port()),
//			Protocol.DEFAULT_TIMEOUT, HelpUtils.getMgrConfig().getRedis_pwd());
            pool = new JedisPool(config, StaticConst.REDIS_Host, Integer.parseInt(StaticConst.REDIS_Port),
                    10 * 1000, StaticConst.REDIS_Password);
        }
        return pool;
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class RedisUtilHolder
    {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static JedisUtil instance = new JedisUtil();
    }

    /**
     * 当getInstance方法第一次被调用的时候，它第一次读取
     * RedisUtilHolder.instance，导致RedisUtilHolder类得到初始化；而这个类在装载并被初始化的时候，会初始化它的静
     * 态域，从而创建RedisUtil的实例，由于是静态的域，因此只会在虚拟机装载类的时候初始化一次，并由虚拟机来保证它的线程安全性。
     * 这个模式的优势在于，getInstance方法并没有被同步，并且只是执行一个域的访问，因此延迟初始化并没有增加任何访问成本。
     */
    public static JedisUtil getInstance()
    {
        return RedisUtilHolder.instance;
    }

    /**
     * 获取Redis实例.
     *
     * @return Redis工具类实例
     */
    private Jedis getJedis()
    {
        Jedis jedis = null;
        try
        {
            jedis = getPool().getResource();
            // log.info("get redis master1!");
        } catch (Exception e)
        {
            log.error("get redis master1 failed!", e);
            // 销毁对象
            if (jedis != null)
            {
                jedis.close();
            }
        }
        return jedis;
    }

    /**
     * 释放redis实例到连接池.
     *
     * @param jedis redis实例
     */
    private void closeJedis(Jedis jedis)
    {
        if (jedis != null)
        {
            jedis.close();
        }
    }


    /**
     * 图片验证码
     *
     * @param check_code_token
     * @param check_code
     * @param second           过期时间，单位秒
     */
    public void setCheckCode(String check_code_token, String check_code, int second)
    {
        Jedis shardedJedis = getJedis();
        if (shardedJedis == null)
        {
            return;
        }
        try
        {
            shardedJedis.set(check_code_token, check_code);
            JedisUtil.getInstance().expire(check_code_token, second);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    /**
     * 获取用户信息
     *
     * @param check_code_token
     * @return
     */
    public String getCheckCode(String check_code_token)
    {
        String checkCode = "";
        Jedis shardedJedis = getJedis();
        if (shardedJedis == null)
        {
            return null;
        }

        try
        {
            checkCode = shardedJedis.get(check_code_token);
            //removeCheckCode(check_code_token);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
        return checkCode;
    }

    /**
     * 移除用户登录
     *
     * @param check_code_token
     * @return
     */
    public void removeCheckCode(String check_code_token)
    {
        Jedis shardedJedis = getJedis();
        if (shardedJedis == null || null == check_code_token)
        {
            return;
        }

        try
        {
            shardedJedis.del(check_code_token);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);

        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    /**
     * 设置Key的失效时间
     *
     * @param key
     * @param second
     */
    public void expire(String key, int second)
    {

        Jedis shardedJedis = getJedis();
        if (shardedJedis == null || null == key)
        {
            return;
        }
        try
        {
            shardedJedis.expire(key, second);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识（解锁时验证是否有解锁的权限）
     * @param expireTime 超期时间 单位毫秒
     * @return 是否获取成功
     */
    public boolean getLock(String lockKey, String requestId, int expireTime)
    {
        Jedis shardedJedis = getJedis();
        boolean result = false;
        if (shardedJedis == null)
        {
            return result;
        }
        try
        {
            result = JedisLock.tryGetDistributedLock(shardedJedis, lockKey, requestId, expireTime);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
        return result;
    }

    /**
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @param retryNum   重试次数
     * @param interval   间隔时间 ms
     * @return
     */
    public boolean getLockRetry(String lockKey, String requestId, int expireTime, int retryNum, int interval)
    {
        boolean isLock = getLock(lockKey, requestId, expireTime);
        int retry = 1;
        while (!isLock && retry <= retryNum)
        {
            log.info("redislock Key:" + lockKey + " retryNum:" + retryNum + " interval:" + interval + " get retry:" + retry);
            retry++;
            try
            {
                Thread.sleep(interval);
            } catch (Exception e)
            {
            }
            isLock = getLock(lockKey, requestId, expireTime);
        }
        return isLock;
    }

    /**
     * 解锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识	（验证是否有解锁的权限）
     * @return
     */
    public boolean releaseLock(String lockKey, String requestId)
    {
        Jedis shardedJedis = getJedis();
        boolean result = false;
        if (shardedJedis == null)
        {
            return result;
        }
        try
        {
            result = JedisLock.releaseDistributedLock(shardedJedis, lockKey, requestId);
        } catch (Exception e)
        {
            log.error(e.getMessage(), e);
        } finally
        {
            closeJedis(shardedJedis);
        }
        return result;
    }


    /**
     *
     * 发布消息
     *
     * @param channel
     * @param message
     * @return
     */
    public boolean publish(String channel,String message){
        Jedis shardedJedis = getJedis();
        boolean result =false;
        if (shardedJedis == null) {
            return result;
        }
        try {
            result = 0 != shardedJedis.publish(channel, message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            closeJedis(shardedJedis);
        }
        return result;
    }

    /**
     *
     * 监听
     *
     * @param jedisPubSub
     * @param channels
     * @return
     */
    public boolean subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        Jedis shardedJedis = getJedis();
        boolean result =false;
        if (shardedJedis == null) {
            return result;
        }try {
            shardedJedis.subscribe(jedisPubSub, channels);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return false;
        }finally {
            closeJedis(shardedJedis);
        }
        return true;

    }
}
