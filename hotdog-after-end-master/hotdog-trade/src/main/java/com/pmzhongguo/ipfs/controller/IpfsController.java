package com.pmzhongguo.ipfs.controller;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtilMember;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ipfs.entity.IpfsHashrate;
import com.pmzhongguo.ipfs.entity.IpfsProject;
import com.pmzhongguo.ipfs.entity.response.IpfsProjectResponse;
import com.pmzhongguo.ipfs.scheduler.IpfsUserBonusScheduler;
import com.pmzhongguo.ipfs.service.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Daily
 * @date 2020/7/15 14:13
 */
@Api(value = "ipfs", description = "云算力", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("ipfs")
public class IpfsController extends TopController {

    private static Logger logger = LoggerFactory.getLogger(IpfsController.class);

    @Autowired
    private IpfsHashrateManager ipfsHashrateManager;

    @Autowired
    private IpfsIntroBonusManager ipfsIntroBonusManager;

    @Autowired
    private IpfsUserBonusScheduler ipfsUserBonusScheduler;

    @Autowired
    private MemberService memberService;

    @Autowired
    private IpfsProjectManager ipfsProjectManager;

    @Autowired
    private IpfsTeamBonusManager ipfsTeamBonusManager;

    @Autowired
    private DaoUtil daoUtil;
    @Autowired
    private IpfsManager ipfsManager;

//    BeanUtils.copyProperties(membervo, member);

    @ApiOperation(value = "购买ipfs商品", notes = "输入字段：<br/>  商家ID:memberId 商品Id:projectId 订单数量:num", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "purchase", method = RequestMethod.POST)
    @ResponseBody
    public ObjResp purchase(HttpServletRequest request
            , HttpServletResponse response
            , @RequestParam int memberId
            , @RequestParam int projectId
            , @RequestParam int num) throws Exception {

        //自测用，自测完后关闭这个打开下面的注释
//        Member m = memberService.getMemberById(memberId);  //测试用， 代替Member m = JedisUtilMember.getInstance().getMember(request, null);

        // 先注释，自测完后要打开校验
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if (memberId != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
        }

        String queryVA = "SELECT COUNT(*) FROM `c_customer` t WHERE t.`identity` = 2 AND t.id = ?;";
        int va = daoUtil.queryForInt(queryVA, memberId);

        if (va > 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.CONTRACT_VA_OPERATION_LIMIT.getErrorENMsg(), null);
        }

        if (num <= 0) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ILLEGAL_AMOUNT.getErrorENMsg(), null);
        }

        if (ipfsHashrateManager.isLimitBuy(projectId, memberId, num)) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_GREATER_THAN_LIMIT.getErrorENMsg(), null);
        }

        ObjResp objResp = ipfsHashrateManager.purchase(memberId, projectId, num);

        if (objResp.getState().intValue() == Resp.SUCCESS.intValue() && m.getIntroduce_m_id() != null && m.getIntroduce_m_id().intValue() != memberId) {
            Integer hashrateId = Integer.valueOf(String.valueOf(objResp.getData()));
            try {
                ipfsIntroBonusManager.reward(memberId, m.getIntroduce_m_id(), hashrateId, projectId);
            } catch (NumberFormatException e) {
                logger.warn("IntroBonus hashrateId:" + hashrateId + "\n" + e.getStackTrace());
            }
        }
        return objResp;
    }

    @ApiOperation(value = "认购项目列表", notes = "输入字段：<br/>  销售状态: saleStatus 0所有状态1待售2销售中3售罄4结束 项目Id: id  0所有项目|指定项目Id<br/>  可选：  页数:page(从第1页开始)   每页条数:pagesize", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "ipfsProjectList", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp ipfsProjectList(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map params = $params(request);
        int id = Integer.parseInt(String.valueOf(params.get("id")));
        String saleStatus = String.valueOf(params.get("saleStatus"));
        if (saleStatus.equals("0")) {
            params.put("saleStatus", "");
        }
        if (id == 0) {
            params.remove("id");
        }
        List<IpfsProject> list = ipfsProjectManager.findByConditionPage(params);
        List<IpfsProjectResponse> responses = new ArrayList<>();
        for (IpfsProject l : list) {
            IpfsProjectResponse responseProject = new IpfsProjectResponse();
            BeanUtils.copyProperties(l, responseProject);
            if (responseProject.getSaleStatus().equals("1")) {
                responseProject.setBoughtNum(0);
            }
            responses.add(responseProject);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, responses);
    }

    @ApiOperation(value = "认购记录列表", notes = "输入字段：<br/>  商家ID:memberId 商品Id:projectId  0所有项目|指定项目Id<br/>  可选：  页数:page(从第1页开始)   每页条数:pagesize", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "ipfsHashrateList", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp ipfsHashrateList(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map params = $params(request);
        int memberId = Integer.parseInt(String.valueOf(params.get("memberId")));
        int projectId = Integer.parseInt(String.valueOf(params.get("projectId")));
        //自测用，自测完后关闭这个打开下面的注释
//        Member m = memberService.getMemberById(memberId);  //测试用， 代替Member m = JedisUtilMember.getInstance().getMember(request, null);

        // 先注释，自测完后要打开校验
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if (memberId != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
        }
        if (projectId == 0) {
            params.remove("projectId");
        }

        params.put("sortname", "create_time");
        params.put("sortorder", "DESC");
        List<IpfsHashrate> list = ipfsHashrateManager.findByConditionPage(params);
        Map ret = new HashMap();
        ret.put("rows", list);
        ret.put("total", params.get("total"));
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, ret);
    }

    @ApiOperation(value = "项目累计算力及个人收益", notes = "输入字段：<br/>  商家ID:memberId 商品Id:projectId  0所有项目|指定项目Id 销售状态: saleStatus 0所有状态1待售2销售中3售罄4结束<br/> ", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getUserTotalBonus", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getUserTotalBonus(HttpServletRequest request, HttpServletResponse response, @RequestParam int memberId, @RequestParam int projectId, @RequestParam String saleStatus) throws Exception {

        //自测用，自测完后关闭这个打开下面的注释
//        Member m = memberService.getMemberById(memberId);  //测试用， 代替Member m = JedisUtilMember.getInstance().getMember(request, null);

        // 先注释，自测完后要打开校验
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if (memberId != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
        }

        if (StringUtils.isBlank(saleStatus)) {
            saleStatus = "0";
        }
        List<Map<String, Object>> list = ipfsHashrateManager.getUserBonus(memberId, projectId, saleStatus);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
    }


    @ApiOperation(value = "项目累计团队收益", notes = "输入字段：<br/>  商家ID:memberId <br/> ", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getTeamTatalBonus", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getTeamTatalBonus(HttpServletRequest request, HttpServletResponse response, @RequestParam int memberId) throws Exception {

        //自测用，自测完后关闭这个打开下面的注释
//        Member m = memberService.getMemberById(memberId);  //测试用， 代替Member m = JedisUtilMember.getInstance().getMember(request, null);

        // 先注释，自测完后要打开校验
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if (memberId != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
        }
        Map<String, Object> map = ipfsTeamBonusManager.getTeamTatalBonus(memberId);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, map);
    }

    @ApiOperation(value = "获取项目文本信息", notes = "输入字段：<br/>   项目Id: id  <br/>  ", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getProjectBLOBs", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getProjectBLOBs(HttpServletRequest request, @RequestParam int id) throws Exception {
        String query = "SELECT t.`particular`, t.`allot_desc`, t.`question`, t.`risk_warning`, t.`particular_e`, t.`allot_desc_e`, t.`question_e`, t.`risk_warning_e` FROM `ipfs_project` t WHERE t.`id` = ?";
        Map<String, String> strs = daoUtil.queryForMap(query, id);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, strs);
    }

    @ApiOperation(value = "获取推荐人累计奖励", notes = "输入字段：<br/>  商家ID:memberId <br/>", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getIntroBonusTotal", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getIntroBonusTotal(HttpServletRequest request, HttpServletResponse response, @RequestParam int memberId) throws Exception {
        //自测用，自测完后关闭这个打开下面的注释
//        Member m = memberService.getMemberById(memberId);  //测试用， 代替Member m = JedisUtilMember.getInstance().getMember(request, null);

        // 先注释，自测完后要打开校验
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if (memberId != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
        }
        BigDecimal total = ipfsIntroBonusManager.getTotal(memberId);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, total);
    }

    @ApiOperation(value = "获取用户ipfs等级", notes = "输入字段：<br/>  商家ID:memberId <br/>", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getTeamLevel", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getTeamLevel(HttpServletRequest request, HttpServletResponse response, @RequestParam int memberId) throws Exception {
        //自测用，自测完后关闭这个打开下面的注释
//        Member m = memberService.getMemberById(memberId);  //测试用， 代替Member m = JedisUtilMember.getInstance().getMember(request, null);

        // 先注释，自测完后要打开校验
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if (memberId != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
        }
        String level = ipfsTeamBonusManager.getLevel(memberId);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, level);
    }

    @ApiOperation(value = "获取团队伞下业绩明细", notes = "输入字段：<br/>  商家ID:memberId <br/>", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getTeamHashrateDetail", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getTeamHashrateDetail(HttpServletRequest request, HttpServletResponse response, @RequestParam int memberId) throws Exception {
        //自测用，自测完后关闭这个打开下面的注释
//        Member m = memberService.getMemberById(memberId);  //测试用， 代替Member m = JedisUtilMember.getInstance().getMember(request, null);

        // 先注释，自测完后要打开校验
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if (memberId != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
        }
        List<Map<String, Object>> map = ipfsTeamBonusManager.getHashrateDetail(memberId, null);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, map);
    }

    //获取ipfs信息
    @RequestMapping(value = "getIpfsInfo", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getIpfsInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam int memberId) {
        // 先注释，自测完后要打开校验
        Member m = JedisUtilMember.getInstance().getMember(request, null);
        if (null == m) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_NO_LOGIN.getErrorENMsg(), null);
        }
        if (memberId != m.getId().intValue()) {
            return new ObjResp(Resp.FAIL, ErrorInfoEnum.LANG_ERROR_OPERATION_OBJECT.getErrorENMsg(), null);
        }

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, ipfsManager.getIpfsInfo(memberId));
    }
}
