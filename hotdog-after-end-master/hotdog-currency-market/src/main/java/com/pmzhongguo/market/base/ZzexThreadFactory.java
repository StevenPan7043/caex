package com.pmzhongguo.market.base;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ZzexThreadFactory implements ThreadFactory {

	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;

	public ZzexThreadFactory(String namePrefix) {
	        this.namePrefix = namePrefix + "-pool-";
	    }

	@Override
	public Thread newThread(Runnable r) {
		// TODO Auto-generated method stub
		Thread t = new Thread(r, namePrefix + threadNumber.getAndIncrement());
		if (t.isDaemon())
			t.setDaemon(true);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		return t;
	}

}
