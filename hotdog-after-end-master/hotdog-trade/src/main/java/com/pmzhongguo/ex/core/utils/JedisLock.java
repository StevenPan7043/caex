package com.pmzhongguo.ex.core.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;

import redis.clients.jedis.Jedis;


/**
 * @author Daily
 *
 * 2018年10月19日
 */
public class JedisLock {
	private static final String LOCK_SUCCESS = "OK";
	/*
	EX seconds -- Set the specified expire time, in seconds.
	PX milliseconds -- Set the specified expire time, in milliseconds.
	NX -- Only set the key if it does not already exist.
	XX -- Only set the key if it already exist.
	*/
	private static final String SET_IF_NOT_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "PX";
	private static final Long RELEASE_SUCCESS = 1L;

	/**
	 * 	尝试获取分布式锁
	 * 	
	 * @param jedis      Redis客户端
	 * @param lockKey    锁
	 * @param requestId  请求标识 （解锁时验证是否有解锁的权限）
	 * @param expireTime 超期时间	 单位毫秒
	 * @return 是否获取成功
	 */
	public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
		String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
		if (LOCK_SUCCESS.equals(result)) {
			return true;
		}
		return false;
	}

	/**
	 * 释放分布式锁
	 * 
	 * @param jedis     Redis客户端
	 * @param lockKey   锁
	 * @param requestId 请求标识 （验证是否有解锁的权限）
	 * @return 是否释放成功
	 */
	public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

		if (RELEASE_SUCCESS.equals(result)) {
			return true;
		}
		return false;

	}
	
	public static void main(String[] args) {
		JedisUtil.getInstance().getLock("Test-lock", IPAddressPortUtil.IP_ADDRESS, 600 * 1000);
	}
}
