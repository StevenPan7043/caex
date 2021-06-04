/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/20 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.basic;

import com.pmzhongguo.zzextool.annotation.MyScheduled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.lang.reflect.*;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/20 21:58
 * @description：定时任务基类
 * @version: $
 */
@Slf4j
public abstract class AbstractBaseTask {

    protected Integer poolSize = 1;

    protected String threadNamePrefix = "taskExecutor-";

    /**
     * 支持单继承多个调度实现
     */
    private final ConcurrentHashMap<String, ScheduledFuture> jobsMap = new ConcurrentHashMap<>(1);

    private final ConcurrentHashMap<String, MyScheduled> annotationMap = new ConcurrentHashMap<>(1);

    public AbstractBaseTask() {
    }

    /**
     * 多线程定时任务执行. 可以设置执行线程池数（默认一个线程）
     * 使用前必须得先调用initialize()进行初始化
     * schedule(Runnable task, Trigger trigger) 指定一个触发器执行定时任务。可以使用CronTrigger来指定Cron表达式，执行定时任务
     * shutDown()方法，执行完后可以关闭线程
     */
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 初始化调度执行池
     * --> setPoolSize(2)    设置调度池大小, 默认为1
     * --> setThreadNamePrefix(taskExecutor-)  设置线程名前缀
     * --> setWaitForTasksToCompleteOnShutdown(true)    当调度器shutdown被调用时等待当前被调度的任务完成
     * --> setAwaitTerminationSeconds(60)   等待时长
     * --> setRemoveOnCancelPolicy(true)    设置当任务被取消的同时从当前调度器移除的策略
     */
    protected void initPoolTask() {
        // 初始化调度线程池配置
        initTaskData();
        // 2. 实例化一个线程池任务调度器
        if (null == threadPoolTaskScheduler) {
            threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
            threadPoolTaskScheduler.setPoolSize(poolSize);
            threadPoolTaskScheduler.setThreadNamePrefix(threadNamePrefix);
            threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
            threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
            threadPoolTaskScheduler.setRemoveOnCancelPolicy(true);

            // 3. 初始化threadPoolTaskScheduler
            threadPoolTaskScheduler.initialize();
        }

    }

    /**
     * 初始化调度线程池配置
     */
    protected abstract void initTaskData();

    /**
     * 控制调度的开关
     *
     * @return
     */
    protected boolean isOpen() {
        return true;
    }

    /**
     * 定时任务开始执行的方法
     */
    public <T extends AbstractBaseTask> void startTask(T t) {
        // 1. 判断定时器开关
        if (!isOpen()) {
            return;
        }

        // 2. 实例化一个线程池任务调度器
        initPoolTask();
        // 获取带有自定义注解的方法
        Method[] methods = t.getClass().getDeclaredMethods();
        for (Method method : methods) {
            execute(t, method);
        }

    }

    private <T extends AbstractBaseTask> void execute(T t, Method method) {
        MyScheduled annotation = method.getAnnotation(MyScheduled.class);
        if (annotation == null) {
            return;
        }
        String cron = annotation.cron();
        if (StringUtils.isEmpty(cron)) {
            log.warn("当前调度方法没有设置cron: name=" + Thread.currentThread().getStackTrace()[2].getClassName() + ", method=" + method.getName());
            return;
        }
        String futureKey = t.getClass().getSimpleName() + "_" + method.getName();
        // 执行时发现已经存在执行的调度则先关闭
        if (jobsMap.keySet().contains(futureKey)) {
            log.warn("当前调度正在运行中... 先杀掉当前调度方法，再重新启动");
            stopMethodTask(futureKey);
        }
        log.info("<====== 开启调度: name=" + futureKey + ", cron=" + cron);
        // 4. 创建一个任务执行线程
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // 执行任务
                    method.setAccessible(true);
                    method.invoke(t);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e instanceof InvocationTargetException) {
                        InvocationTargetException ite = (InvocationTargetException) e;
                        log.error("<=== 任务名称：【{}】, 定时任务执行失败：【{}】", futureKey, ite.getTargetException().getMessage());
                    } else {
                        log.error("<=== 任务名称：【{}】, 定时任务执行失败：【{}】", futureKey, e.getMessage());
                    }
                }
            }
        };

        // 5. 创建一个触发器
        Trigger trigger = new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
//                log.warn("定时任务触发器："+t.threadNamePrefix);
                String cronTmp = annotation.cron();
                String name = annotation.name();
                if (StringUtils.isNotEmpty(name)) {
                    if (StringUtils.isNotEmpty(WalletConst.cronPool.get(name))) {
                        cronTmp = WalletConst.cronPool.get(name);
                    } else {
                        log.error(name + " 数据库中未配置 cron 信息! 采用系统默认配置 cron: " + cronTmp);
                    }
                }
                CronTrigger cronTrigger = new CronTrigger(cronTmp);
                return cronTrigger.nextExecutionTime(triggerContext);
            }
        };
        // 6. 执行schedule(Runnable task, Trigger trigger)任务调度方法
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(runnable, trigger);
        jobsMap.put(futureKey, future);
        annotationMap.put(futureKey, annotation);
        log.info("<=== Task: 【{}】 is running...", futureKey);

    }

    /**
     * 定时任务部分终止执行的方法
     */
    public void stopMethodTask(String futureKey) {
        try {
            ScheduledFuture scheduledFuture = jobsMap.get(futureKey);
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            } else {
                log.debug("jobId:{} is not running ", futureKey);
            }
            // 查看任务是否在正常执行之前结束,正常true
            boolean cancelled = scheduledFuture.isCancelled();
            while (!cancelled) {
                scheduledFuture.cancel(true);
                Thread.sleep(2000);
            }
            log.warn("job cancel:{}", scheduledFuture);
            jobsMap.remove(futureKey);
        } catch (Exception e) {
            log.error("调度停止失败: error=" + e.getCause());
        }
    }

    /**
     * 定时任务终止执行的方法
     */
    public void stopTask() {
        if (null != threadPoolTaskScheduler) {
            log.warn(this.getClass().getSimpleName() + "关闭当前调度...");
            //关闭线程
            threadPoolTaskScheduler.shutdown();
        }
    }

    /**
     * 修改corn配置，先取消再重新启动
     *
     * @param methodName 调度方法名
     * @param cron
     */
    public <T extends AbstractBaseTask> void reset(String methodName, String cron, T t) {
        if (StringUtils.isEmpty(methodName)) {
            return;
        }
        // 修改注解值(cron), 并重新启动调度任务
        String futureKey = t.getClass().getSimpleName() + "_" + methodName;
        stopMethodTask(futureKey);
        MyScheduled myScheduled = annotationMap.get(futureKey);
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(myScheduled);
        try {
            Field value = invocationHandler.getClass().getDeclaredField("memberValues");
            value.setAccessible(true);
            Map<String, Object> memberValues = (Map<String, Object>) value.get(invocationHandler);
            memberValues.put("cron", cron);
            log.info("调度重置成功: cron=" + myScheduled.cron());
            Method[] methods = t.getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    execute(t, method);
                    return;
                }
            }
        } catch (Exception e) {
            log.error("调度重置失败: error=" + e.getCause());
        }
    }

    /**
     * 获取调度执行状态
     *
     * @param methodName
     * @return
     */
    public boolean status(String methodName) {
        if (jobsMap.keySet().contains(this.getClass().getName() + methodName)) {
            return true;
        }
        return false;
    }

    /**
     * 验证币种是否运行执行定时任务
     *
     * @return
     */
    protected boolean volidateStatus(String currency) {
        Integer status = WalletConst.statusMap.get(currency + WalletConst.status);
        if (status != null && status.intValue() == 1) {
            return true;
        }
        log.info("币种: " + currency + " 的执行状态为: 未执行");
        return false;
    }
}
