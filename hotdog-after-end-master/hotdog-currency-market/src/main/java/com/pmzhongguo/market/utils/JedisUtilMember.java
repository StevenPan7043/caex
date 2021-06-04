package com.pmzhongguo.market.utils;

import com.pmzhongguo.market.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 会员的Redis库操作，定义为1
 * jedis.select(1);
 * @author Administrator
 *
 */
public class JedisUtilMember {
    protected static Logger logger = LoggerFactory.getLogger(JedisUtilMember.class);

    /**
     * 私有构造器.
     */
    private JedisUtilMember() {

    }

    private static JedisPool pool = null;

    /**
     * 设置设置用户信息 这里为以后APP留接口，APP不使用Session，使用Token，通过request.getHeader("xxx")获取，
     * 目前只实现session方式
     *
     * @param request
     * @param request
     * @return
     */
//    public void setMember(HttpServletRequest request, Member member) {
//        // 这里显示设为null不使用token方式
//        String token = null;
//
//        if (!StringUtils.isBlank(token)) { // token方式
////            Jedis shardedJedis = getJedis();
////            if (shardedJedis == null) {
////                return;
////            }
////            try {
////                shardedJedis.set(token.getBytes(), SerializeUtil.serialize(member));
////                JedisUtilMember.getInstance().expire(token);
////            } catch (Exception e) {
////                logger.error(e.getMessage(), e);
////            } finally {
////                closeJedis(shardedJedis);
////            }
//        } else if (null != request && null != request.getSession()) {
//            // session 方式
//            HttpSession session = request.getSession();
////            session.setAttribute(Constants.SYS_SESSION_MEMBER, member);
//        }
//    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    public User getMember(HttpServletRequest request, String token) {
        User m = null;

        // token 方式
        if (!StringUtils.isBlank(token)) {
//            Jedis shardedJedis = getJedis();
//            if (shardedJedis == null) {
//                return null;
//            }
//
//            try {
//                m = (Member) SerializeUtil.deserialize(shardedJedis.get(token.getBytes()));
//                if (null != m) {
//                    JedisUtilMember.getInstance().expire(token);
//                }
//            } catch (Exception e) {
//                logger.error(e.getMessage(), e);
//            } finally {
//                closeJedis(shardedJedis);
//            }
        } else if (null != request && null != request.getSession()) {
            // session方式
            HttpSession session = request.getSession();
            m = (User) session.getAttribute("loginUser");
        }
        return m;
    }

    /**
     * 移除用户登录
     *
     * @param key
     * @return
     */
//    public void removeMember(HttpServletRequest request, String token) {
//        // token 方式
//        if (!HelpUtils.nullOrBlank(token)) {
//            Jedis shardedJedis = getJedis();
//            if (shardedJedis == null || null == token) {
//                return;
//            }
//
//            try {
//                shardedJedis.del(token.getBytes());
//            } catch (Exception e) {
//                logger.error(e.getMessage(), e);
//
//            } finally {
//                closeJedis(shardedJedis);
//            }
//        } else if (null != request && null != request.getSession()) {
//            HttpSession session = request.getSession();
//            try {
//                session.removeAttribute(Constants.SYS_SESSION_MEMBER);
//            } catch (Exception e) {
//                logger.error("用户注销系统报错！", e);
//            }
//        }
//    }

    /**
     * 设置Key的失效时间
     * @param key
     * @param seconds
     */
//    public void expire(String key) {
//        int seconds = Constants.MEMBER_TOKEN_TIME_OUT;
//
//        Jedis shardedJedis = getJedis();
//        if (shardedJedis == null || null == key) {
//            return;
//        }
//        try {
//            shardedJedis.expire(key, seconds);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        } finally {
//            closeJedis(shardedJedis);
//        }
//    }


    /**
     * 构建redis连接池
     *
     *
     * @return JedisPool
     */
    private static JedisPool getPool() {
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setMaxTotal(-1); // 可用连接实例的最大数目,如果赋值为-1,表示不限制.
            config.setMaxIdle(1000); // 控制一个Pool最多有多少个状态为idle(空闲的)jedis实例,默认值也是8
            config.setMaxWaitMillis(1000 * 10); // 等待可用连接的最大时间,单位毫秒,默认值为-1,表示永不超时/如果超过等待时间,则直接抛出异常
            config.setTestOnBorrow(true); // 在borrow一个jedis实例时,是否提前进行validate操作,如果为true,则得到的jedis实例均是可用的

            pool = new JedisPool(config, PropertiesUtil.getPropValByKey("redis_ip"), Integer.parseInt(PropertiesUtil.getPropValByKey("redis_port")),
                    Protocol.DEFAULT_TIMEOUT, PropertiesUtil.getPropValByKey("redis_pwd"));
//            pool = new JedisPool(config, HelpUtils.getMgrConfig().getRedis_ip(), Integer.parseInt(HelpUtils.getMgrConfig().getRedis_port()),
//                    Protocol.DEFAULT_TIMEOUT, HelpUtils.getMgrConfig().getRedis_pwd());
        }
        return pool;
    }

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到时才会装载，从而实现了延迟加载。
     */
    private static class RedisUtilHolder {
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private static JedisUtilMember instance = new JedisUtilMember();
    }

    /**
     * 当getInstance方法第一次被调用的时候，它第一次读取
     * RedisUtilHolder.instance，导致RedisUtilHolder类得到初始化；而这个类在装载并被初始化的时候，会初始化它的静
     * 态域，从而创建RedisUtil的实例，由于是静态的域，因此只会在虚拟机装载类的时候初始化一次，并由虚拟机来保证它的线程安全性。
     * 这个模式的优势在于，getInstance方法并没有被同步，并且只是执行一个域的访问，因此延迟初始化并没有增加任何访问成本。
     */
    public static JedisUtilMember getInstance() {
        return RedisUtilHolder.instance;
    }

    /**
     * 获取Redis实例.
     *
     * @return Redis工具类实例
     */
    private Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = getPool().getResource();
            jedis.select(1);
        } catch (Exception e) {
            logger.error("get redis master2 failed!", e);
            // 销毁对象
            if (jedis != null) {
                jedis.close();
            }
        }
        return jedis;
    }

    /**
     * 释放redis实例到连接池.
     *
     * @param jedis
     *            redis实例
     */
    private void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

}
