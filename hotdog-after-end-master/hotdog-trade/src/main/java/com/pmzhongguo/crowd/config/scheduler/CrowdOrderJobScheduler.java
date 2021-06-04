package com.pmzhongguo.crowd.config.scheduler;

import com.pmzhongguo.crowd.config.rediskey.CrowdOrderSchedulerCountKey;
import com.pmzhongguo.crowd.config.support.BeanContext;
import com.pmzhongguo.crowd.entity.CrowdJob;
import com.pmzhongguo.crowd.entity.CrowdProject;
import com.pmzhongguo.crowd.service.CrowdOrderService;
import com.pmzhongguo.crowd.service.CrowdProjectService;
import com.pmzhongguo.crowd.service.JobService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @see Job 用来执行具体的功能需求
 * @see org.quartz.Trigger 用来设置时间相关参数，非常强大
 * @see org.quartz.Scheduler 调度器，定时任务协调
 * @see org.quartz.spi.JobStore 存储Job，Trigger的运行状态
 *
 * @description: 下单任务
 * @date: 2019-04-11 10:41
 * @author: 十一
 */
@Component
public class CrowdOrderJobScheduler implements Job {

    private static Logger logger = LoggerFactory.getLogger(CrowdOrderJobScheduler.class);


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        CrowdJob job = (CrowdJob)jobDataMap.get("job");
        // 下单的项目id
        int projectId = job.getProject_id();
        CrowdProjectService crowdProjectService = (CrowdProjectService)BeanContext.getBeanByType(CrowdProjectService.class);
        CrowdProject crowdProject = crowdProjectService.findOrderInfoById(projectId);
        if(HelpUtils.stringToDateWithTime(crowdProject.getRush_begin_time()+":00").getTime()
                >= System.currentTimeMillis()) {
            logger.warn("<====================== 抢购时间未开始,币种名称：" + crowdProject.getCurrency()
                    + " 抢购时间：" + crowdProject.getRush_begin_time()+":00");
            return;
        }
        if(HelpUtils.stringToDateWithTime(crowdProject.getRush_end_time()+":00").getTime()
                < System.currentTimeMillis()) {
            logger.warn("<====================== 抢购时间已结束,币种名称：" + crowdProject.getCurrency()
                    + " 抢购时间：" + crowdProject.getRush_begin_time()+":00");
            JobService jobService = (JobService) BeanContext.getBeanByType(JobService.class);
            // 暂停
            jobService.deleteJob(job.getId());
            return;
        }

        // 每s下单次数
        int secNum = job.getSec_num();
        if (secNum > 1) {
            // 一次任务执行多次
            while (secNum > 0) {
                executeOrder(job);
                secNum --;
            }
        }else {
            // 一次任务执行1次
            executeOrder(job);
        }


    }

    private void executeOrder(CrowdJob job) {
        Object o =  JedisUtil.getInstance().get(CrowdOrderSchedulerCountKey.crowdOrderSchedulerCountKey.OrderLimit(job.getJob_group(),job.getProject_id(),job.getOrder_num()),true);
        if (o != null && Integer.parseInt(o.toString()) >= job.getOrder_num()) {
            logger.info("================== 所有下单完成，下单次数: " + job.getOrder_num());
            // 完成就暂停
            JobService jobService = (JobService) BeanContext.getBeanByType(JobService.class);
            jobService.deleteJob(job.getId());
            return;
        }
        CrowdOrderService crowdOrderService = (CrowdOrderService) BeanContext.getBeanByType(CrowdOrderService.class);
        ObjResp resp = crowdOrderService.mockOrder(job);
        if(resp != null && resp.getState() == 1) {
            // 下单成功 计数+1
            JedisUtil.getInstance().incr(CrowdOrderSchedulerCountKey.crowdOrderSchedulerCountKey.OrderLimit(job.getJob_group(),job.getProject_id(),job.getOrder_num()));
            logger.info("================== crowd project. create order successful. memberId: " + resp.getData());
        }else {
            logger.error("=================================== crowd project 100W. create order failure. cause: " + resp.getData());
        }
    }

}
