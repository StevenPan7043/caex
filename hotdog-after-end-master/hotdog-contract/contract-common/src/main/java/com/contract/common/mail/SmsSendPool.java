package com.contract.common.mail;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
