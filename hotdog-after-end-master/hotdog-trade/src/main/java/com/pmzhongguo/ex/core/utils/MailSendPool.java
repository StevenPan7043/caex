package com.pmzhongguo.ex.core.utils;

import com.pmzhongguo.ex.core.threadpool.ZzexThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MailSendPool {

	ExecutorService pool = Executors.newFixedThreadPool(2, new ZzexThreadFactory("mail-send"));

	private MailSendPool() {}
	
	private static class SingletonInstance {
		private static final MailSendPool instance = new MailSendPool();
	}

	public static MailSendPool getInstance() {
		return SingletonInstance.instance;
	}
	
	public void send(Runnable runnable) {
		pool.execute(runnable);
	}
	
}
