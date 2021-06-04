package com.contract.service.thead;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 *   线程池代理类
 * </p>
 * @title  ${线程池代理类}
 * @version V1.0
 * @since 2017-7-10
 */
public class ThreadPoolConfig {  
	private static final Logger log= LoggerFactory.getLogger(ThreadPoolConfig.class);
    ThreadPoolExecutor mThreadPoolExecutor;  
  
    private int corePoolSize;  
    private int maximumPoolSize;  
    private long keepAliveTime;  
    
    /**
     * 
     * @param corePoolSize 核心线程数 
     * @param maximumPoolSize 最大线程数
     * @param keepAliveTime 线程保持时间   单位为 mis
     */
    public ThreadPoolConfig(int corePoolSize, int maximumPoolSize, long keepAliveTime) {  
        this.corePoolSize = corePoolSize;  
        this.maximumPoolSize = maximumPoolSize;  
        this.keepAliveTime = keepAliveTime;
        log.info("**********************************************************************");
        log.info("配制池程池代理corePoolSize:{}|maximumPoolSize:{}|keepAliveTime:{}",corePoolSize,maximumPoolSize,keepAliveTime);
        log.info("**********************************************************************");
    }  
  
    private ThreadPoolExecutor initExecutor() {  
        if(mThreadPoolExecutor == null) {  
            synchronized(ThreadPoolConfig.class) {  
                if(mThreadPoolExecutor == null) {  
  
                    TimeUnit unit =  TimeUnit.MILLISECONDS;  
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();  
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();  
                    LinkedBlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
                    mThreadPoolExecutor = new ThreadPoolExecutor(  
                            corePoolSize,//核心线程数
                            maximumPoolSize==0?Integer.MAX_VALUE:maximumPoolSize,//最大线程数
                            keepAliveTime,//保持时间  
                            unit,//保持时间对应的单位  
                            workQueue,  
                            threadFactory,//线程工厂  
                            handler);//异常捕获器  
                }  
            }  
        }  
        return mThreadPoolExecutor;  
    }  
  
  
  
    /**执行任务*/  
    public void executeTask(Runnable r) {  
        initExecutor();  
        mThreadPoolExecutor.execute(r);  
        log.info("执行任务{}",r.getClass().getCanonicalName());
    }  
  
  
    /**提交任务*/  
    public Future<?> commitTask(Runnable r) {  
        initExecutor();  
         log.info("提交任务{}",r.getClass().getCanonicalName());
         return mThreadPoolExecutor.submit(r);  
        
    }  
  
    /**删除任务*/  
    public void removeTask(Runnable r) {  
        initExecutor();  
        mThreadPoolExecutor.remove(r);  
        log.info("删除任务{}",r.getClass().getCanonicalName());
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}