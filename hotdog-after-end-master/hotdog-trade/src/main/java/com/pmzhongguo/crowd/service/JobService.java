package com.pmzhongguo.crowd.service;


import com.pmzhongguo.crowd.config.rediskey.CrowdOrderSchedulerCountKey;
import com.pmzhongguo.crowd.config.scheduler.CrowdOrderJobScheduler;
import com.pmzhongguo.crowd.crowdenum.JobEnum;
import com.pmzhongguo.crowd.dao.JobMapper;
import com.pmzhongguo.crowd.entity.CrowdJob;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.resp.Resp;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @description: 定时调度任务
 * @date: 2019-04-11 14:42
 * @author: 十一
 */
@Service
@Transactional
public class JobService {

    private static Logger logger = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private JobMapper jobMapper;

    /**
     * 起始用户id,14w-24w,如果需要改，需要查看数据库，谨慎操作
     */
    private static final int START_MEMBER_ID = 140001;
    /**
     * 间隔增长
     */
    private static final int INCR_MEMBER_ID = 10000;

    /**
     * 任务最大数量
     */
    private static final int JOB_MAX = 10;

    /**
     * 启动调度任务
     * @return
     */
    public boolean startJob(Integer id) {
        CrowdJob crowdJob = findJobById(id);
        try {

            // 任务不存在，就创建
            addJob(crowdJob);
            // 启动
            scheduler.start();
        }catch (Exception e) {
            logger.error("<=================== 启动定时任务失败：" + e.getMessage());
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());
        }
        return true;
    }

    /**
     * 添加一个调度任务，任务不存在，就创建
     * @param crowdJob
     */
    private void addJob(CrowdJob crowdJob) throws SchedulerException {
        String jobClassName = crowdJob.getJob_class_name();
        String jobGroup = crowdJob.getJob_group();
        //构建job信息
        JobDetail jobDetail = JobBuilder.newJob(getClass(jobClassName).getClass())
                .withIdentity(jobClassName, jobGroup)
                .build();

        JobDataMap jobDataMap = jobDetail.getJobDataMap();

        jobDataMap.put("job",crowdJob);


        //表达式调度构建器(即任务执行的时间)
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(crowdJob.getCron_expression());

        //按新的cronExpression表达式构建一个新的trigger
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(jobClassName, jobGroup)
                .withSchedule(scheduleBuilder).build();
        boolean exists = scheduler.checkExists(trigger.getJobKey());

        if(!exists) {
            scheduler.scheduleJob(jobDetail, trigger);
        }


        TriggerKey triggerKey = TriggerKey.triggerKey(jobClassName, jobGroup);
        Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
        if (triggerState.name().equals(JobEnum.NORMAL.name())) {
            scheduler.pauseJob(trigger.getJobKey());
        }
    }
    /**
     * 暂停调度任务
     * @return
     */
    public boolean pauseJob(Integer id) {
        CrowdJob crowdJob = findJobById(id);
        try {
            JobKey jobKey = getJobKey(crowdJob.getJob_class_name(),crowdJob.getJob_group());
            // 暂停
            scheduler.pauseJob(jobKey);

        }catch (Exception e) {
            logger.error("<=================== 暂停定时任务失败：" + e.getMessage());
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());
        }
        return true;
    }

    /**
     * 恢复调度任务
     * @return
     */
    public boolean resumeJob(Integer id) {
        CrowdJob crowdJob = findJobById(id);
        try {
            JobKey jobKey = getJobKey(crowdJob.getJob_class_name(),crowdJob.getJob_group());
            // 恢复
            scheduler.resumeJob(jobKey);
        }catch (Exception e) {
            logger.error("<=================== 暂停定时任务失败：" + e.getMessage());
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());
        }
        return true;
    }

    /**
     * 删除调度任务
     * @return
     */
    public boolean deleteJob(Integer id) {
        CrowdJob crowdJob = findJobById(id);
        try {
            JobKey jobKey = getJobKey(crowdJob.getJob_class_name(),crowdJob.getJob_group());
            // 删除
            scheduler.deleteJob(jobKey);
            jobMapper.deleteById(crowdJob.getId());
            // 删除redis key
            JedisUtil.getInstance().del(CrowdOrderSchedulerCountKey.crowdOrderSchedulerCountKey
                    .OrderLimit(crowdJob.getJob_group(),crowdJob.getProject_id(),crowdJob.getOrder_num()));

        }catch (Exception e) {
            logger.error("<=================== 删除调度任务失败：" + e.getCause());
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());
        }
        return true;
    }

    /**
     * 编辑
     * @param crowdJob
     * @return
     */
    public boolean editJob(CrowdJob crowdJob) {
        try {
            crowdJob.setUpdate_time(HelpUtils.formatDate8(new Date()));
            jobMapper.update(crowdJob);
            JobKey jobKey = getJobKey(crowdJob.getJob_class_name(),crowdJob.getJob_group());
            // 删除任务
            scheduler.deleteJob(jobKey);
            // 重新添加
            addJob(crowdJob);

        }catch (Exception e) {
            logger.error("<=================== 更新失败：" + e.getMessage());
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());
        }
        return true;
    }

    /**
     * 新增任务保存到数据库
     * @param crowdJob
     * @return
     */
    public boolean insertJob(CrowdJob crowdJob) {


        Integer secNum = crowdJob.getSec_num();
        if(secNum > 10) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.JOB_SEC_NUM_THAN_10.getErrorCNMsg());
        }
        // 随机，不能重复
        crowdJob.setJob_group("job_group_"+HelpUtils.randomString(4)+System.currentTimeMillis());
        // 任务执行的类名
        crowdJob.setJob_class_name(CrowdOrderJobScheduler.class.getName());
        // cron表达式 手动拼接，精确到秒
        String expressionTemp = "0/%s * * * * ?";
        String expression = String.format(expressionTemp, crowdJob.getCron_expression());
        crowdJob.setCron_expression(expression);

        int count = jobMapper.countJobGroupName(crowdJob.getJob_group());
        if (count > 0) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.JOB_GROUP_NAME_DUPLICATION.getErrorCNMsg());
        }

        int countJob = jobMapper.countJob();
        if(countJob >= JOB_MAX) {
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.JOB_MAX.getErrorCNMsg());
        }

        crowdJob.setStart_member_id(START_MEMBER_ID + (countJob * INCR_MEMBER_ID));


        try {
            crowdJob.setCreate_time(HelpUtils.formatDate8(new Date()));
            // 默认就是未开始
            crowdJob.setStatus(JobEnum.NONE.getStatus());
            jobMapper.add(crowdJob);
        }catch (Exception e) {
            logger.error("<=================== 保存失败：" + e.getCause());
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());
        }
        return true;
    }

    /**
     * 分页查询任务列表，将当前系统运行的任务状态通过jobName来塞进去
     * @param param
     * @return
     */
    public List<CrowdJob> findJobByPage(Map<String,Object> param) {
        List<CrowdJob> CrowdJobList = jobMapper.findByPage(param);
        Map<String,String> jobGroupStateMap = new HashMap<>();
        try {
            // 获取所有的任务组job_group名称
            List<String> jobGroupNameList = scheduler.getJobGroupNames();
            for (String jobGroup : jobGroupNameList) {
                jobGroupStateMap.put(jobGroup,jobGroup);
            }

            for (CrowdJob job : CrowdJobList) {
                if (jobGroupStateMap.containsKey(job.getJob_group())) {
                    TriggerKey triggerKey = TriggerKey.triggerKey(job.getJob_class_name(), job.getJob_group());
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(triggerKey);
                    job.setStatus(JobEnum.statusOf(triggerState.name()));
                }
            }

        } catch (SchedulerException e) {
            e.printStackTrace();
        }


        return CrowdJobList;
    }

    public CrowdJob findJobById(Integer id) {
        return jobMapper.findById(id);

    }




    private JobKey getJobKey(String jobClassName, String jobGroup) {

        JobKey jobKey = JobKey.jobKey(jobClassName, jobGroup);
        return jobKey;
    }
    /**
     * 通过类名称获取该类实例
     * @param jobClassName
     * @return
     */
    private CrowdOrderJobScheduler getClass(String jobClassName) {
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName(jobClassName);
            instance = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("<=================== 通过类名称获取该类实例失败：" + e.getCause());
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());
        } catch (IllegalAccessException e) {
            logger.error("<=================== 通过类名称获取该类实例失败：" + e.getCause());
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());
        } catch (InstantiationException e) {
            logger.error("<=================== 通过类名称获取该类实例失败：" + e.getCause());
            throw new BusinessException(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());
        }

        return (CrowdOrderJobScheduler) instance;
    }
}
