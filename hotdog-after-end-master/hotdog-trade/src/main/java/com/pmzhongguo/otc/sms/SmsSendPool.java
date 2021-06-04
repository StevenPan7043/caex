package com.pmzhongguo.otc.sms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;

public class SmsSendPool {
	
	ExecutorService pool = Executors.newFixedThreadPool(2, new ZzexThreadFactory("SmsSend"));
	
	private SmsSendPool() {};
	
	private static class SingletonInstance {
		private static final SmsSendPool instance = new SmsSendPool();
	}

	public static SmsSendPool getInstance() {
		return SingletonInstance.instance;
	}
	
	public void send(Runnable runnable) {
		pool.execute(runnable);
	}
	
}
