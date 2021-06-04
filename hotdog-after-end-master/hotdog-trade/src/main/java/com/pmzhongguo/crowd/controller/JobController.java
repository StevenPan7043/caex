package com.pmzhongguo.crowd.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.crowd.config.scheduler.CrowdOrderJobScheduler;
import com.pmzhongguo.crowd.entity.CrowdJob;
import com.pmzhongguo.crowd.service.JobService;
import com.pmzhongguo.ex.core.utils.CopyObjAttributeUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 众筹下单定时调度任务
 * @date: 2019-04-11 14:22
 * @author: 十一
 */
@ApiIgnore
@Controller
@RequestMapping("/crowd/job")
public class JobController extends TopController {

    private static Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    private JobService jobService;

    @ApiOperation(value = "编辑/添加页面")
    @RequestMapping(value = "/edit_page", method = RequestMethod.GET)
    public String toEditPage(HttpServletRequest request) {
        Integer id = $int("id");
        if (!HelpUtils.nullOrBlank(id)) {
            CrowdJob job = jobService.findJobById(id);
            $attr("info", job);
        }
        return "crowd/job/job_edit";
    }

    @ApiOperation(value = "编辑/添加任务")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ResponseBody
    public Resp editJob(HttpServletRequest request) {
        Map<String,Object> param = $params(request);
        CrowdJob crowdJob = (CrowdJob)CopyObjAttributeUtil.map2Obj(param, CrowdJob.class);

        Resp resp = null;
        if(HelpUtils.nullOrBlank(crowdJob.getId())) {
            jobService.insertJob(crowdJob);
        }else {
//            jobService.editJob(crowdJob);
        }

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }



    @ApiOperation(value = "启动任务")
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    @ResponseBody
    public Resp startJob(HttpServletRequest request) {

        boolean result = jobService.startJob($int("id"));
        if(result) {
            return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
        }
        return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());

    }

    @ApiOperation(value = "暂停任务")
    @RequestMapping(value = "/pause", method = RequestMethod.GET)
    @ResponseBody
    public Resp pauseJob(HttpServletRequest request) {

        boolean result = jobService.pauseJob($int("id"));
        if(result) {
            return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
        }
        return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());

    }

    @ApiOperation(value = "恢复任务")
    @RequestMapping(value = "/resume", method = RequestMethod.GET)
    @ResponseBody
    public Resp resumeJob(HttpServletRequest request) {

        boolean result = jobService.resumeJob($int("id"));
        if(result) {
            return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
        }
        return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());

    }

    @ApiOperation(value = "调度任务页面", notes = "分页获取轮播图列表")
    @RequestMapping(value = "/list_page", method = RequestMethod.GET)
    @ApiIgnore
    public String toJobPage() {

        return "crowd/job/job_list";
    }

    @ApiOperation(value = "分页获取调度任务")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiIgnore
    @ResponseBody
    public Map<String,Object> findJobByPage(HttpServletRequest request) {
        Map<String,Object> param = $params(request);
        List<CrowdJob> crowdJobList =  jobService.findJobByPage(param);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("Rows", crowdJobList);
        map.put("Total", param.get("total"));
        return map;
    }

    @ApiOperation(value = "删除调度任务")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiIgnore
    @ResponseBody
    public Resp delJob() {

        boolean result = jobService.deleteJob($int("id"));
        if(result) {
            return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
        }
        return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_OPERTION_FAIL.getErrorCNMsg());
    }




}
