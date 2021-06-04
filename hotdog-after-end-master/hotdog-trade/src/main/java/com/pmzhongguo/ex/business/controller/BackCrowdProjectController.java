package com.pmzhongguo.ex.business.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.crowd.dto.CrowdProjectDto;
import com.pmzhongguo.crowd.entity.CrowdProject;
import com.pmzhongguo.crowd.service.CrowdProjectService;
import com.pmzhongguo.ex.business.resp.TickerResp;
import com.pmzhongguo.ex.business.service.MarketService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.LstResp;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @description: 一定要写注释啊
 * @date: 2019-03-03 10:09
 * @author: 十一
 */
@ApiIgnore
@Controller
@RequestMapping("backstage/crowd")
public class BackCrowdProjectController extends TopController {

    private static Logger logger = LoggerFactory.getLogger(BackCrowdProjectController.class);

    @Autowired
    private CrowdProjectService crowdProjectService;
    /**
     * 限定一个值
     */
    private static final BigDecimal ZZEX_LIMIT_PRICE = new BigDecimal("0.5");


    @ApiOperation(value = "分页获取众筹项目列表", notes = "page：页码，pageSize：每页数量", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/projects/{page}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public Resp findByPage(@PathVariable String page, @PathVariable String pageSize) {

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("pagesize",pageSize);
        params.put("page",page);
        List<Map<String, Object>> list = crowdProjectService.findByPage(params);
        for (Map<String, Object> map : list) {

            String currency = map.get("currency").toString();
            String quoteCurrency = map.get("quote_currency").toString();
            String pPrecision = map.get("p_precision").toString();
            if("ZGT".equalsIgnoreCase(currency) && "ZZEX".equalsIgnoreCase(quoteCurrency)){
                BigDecimal rushPrice = getRealRushPrice(Integer.parseInt(pPrecision));
                if(rushPrice != null) {
                    map.put("rush_price",rushPrice);
                }
            }
        }
        return new LstResp(Resp.SUCCESS, Resp.SUCCESS_MSG,Integer.parseInt(params.get("total") + ""), list == null ? new ArrayList() : list);
    }

    @ApiOperation(value = "根据获id取众筹项目详细信息", notes = "id：项目id", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp findById(@PathVariable Integer id) {

        CrowdProjectDto crowdProject = crowdProjectService.findCrowdProjectInfoById(id);
        if("ZGT".equalsIgnoreCase(crowdProject.getCurrency()) && "ZZEX".equalsIgnoreCase(crowdProject.getQuote_currency())) {
            BigDecimal realRushPirce = getRealRushPrice(crowdProject.getP_precision());
            if(realRushPirce != null) {
                crowdProject.setRush_price(realRushPirce);
            }
        }

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, crowdProject);
    }


    private BigDecimal getRealRushPrice(int pPrecision) {
        TickerResp tickerResp = MarketService.getTicker().get("zzexzc_ticker");
        if (tickerResp == null || BigDecimal.ZERO.compareTo(tickerResp.getClose()) >= 0) {
            return null;
        }
        // 限定价格/zzex价=真正的抢购价格
        BigDecimal rushPrice = ZZEX_LIMIT_PRICE.divide(tickerResp.getClose(), pPrecision, BigDecimal.ROUND_HALF_DOWN);
        return  rushPrice;
    }

    @ApiOperation(value = "根据id获取众筹项目购买信息", notes = "id：项目id", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/project/buy/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp findProjectBuyInfoById(@PathVariable Integer id) {

        Map<String,Object> map = crowdProjectService.findProjectBuyInfoById(id);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, map);
    }

    @ApiOperation(value = "下载文件", notes = "下载文件")
    @RequestMapping(value = "/project/file", method = RequestMethod.GET)
    public void getProjectFile(HttpServletRequest request, HttpServletResponse response) {

        //即设置一个响应的类型: application/x-msdownload
        //通知客户端浏览器: 这是一个需要下载的文件, 不能再按普通的 html 的方式打开.
        response.setContentType("application/x-msdownload");

        //response.setHeader("Content-Disposition", "attachment;filename=abc.txt");
        //设置用户处理的方式: 响应头: Content-Disposition
        //通知客户端浏览器: 不再有浏览器来处理该文件, 而是交由用户自行处理

        //.txt为下载文件的类型，必须与后面downloadFileName的类型相同；文件放在WebRoot目录下
        String fileName = "GCW-Whitepaper-1.0-EN.pdf";

        //所读取文件的地址，及类型
        String downloadFileName = request.getServletContext().getRealPath("/") + fileName;
        InputStream in = null;
        OutputStream out = null;
        try {

            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            //读取要下载的文件
            in = new FileInputStream(downloadFileName);

            //读取进来然后下载；
            out = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**===================================================================后台管理端,要加@ApiIgnore=============================================================**/

    /**
     * 项目列表
     *
     * @return
     */
    @RequestMapping(value = "/mgr/list_page", method = RequestMethod.GET)
    @ApiIgnore
    public String getProjectPage() {

        return "crowd/project/project_list";
    }

    /**
     * 后台查询
     * @param request
     * @return
     */
    @RequestMapping(value = "/mgr/list", method = RequestMethod.GET)
    @ApiIgnore
    @ResponseBody
    public Map findMgrByPage(HttpServletRequest request) {

        Map<String,Object> params = $params(request);
        List<Map<String,Object>> list =  crowdProjectService.findMgrByPage(params);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("Rows", list);
        map.put("Total", params.get("total"));
        return map;
    }

    /**
     * 新增/编辑
     * @param request
     * @return
     */
    @RequestMapping(value = "/mgr/edit_page", method = RequestMethod.GET)
    @ApiIgnore
    public String toAddProjectOfMgr(HttpServletRequest request) {
        Integer id = $int("id");
        if(!HelpUtils.nullOrBlank(id)) {
            Map<String,Object> crowdProject = crowdProjectService.findMgrCrowdProjectInfoById(id);
            $attr("info",crowdProject);
        }
        for (Map.Entry<String, String> entry : getAliPolicy().entrySet()) {
            $attr(entry.getKey(), entry.getValue());
        }
        return "crowd/project/project_edit";
    }

    /**
     * 新增/编辑
     * @param request
     * @return
     */
    @RequestMapping(value = "/mgr/edit", method = RequestMethod.POST)
    @ApiIgnore
    @ResponseBody
    public Resp addProjectOfMgr(HttpServletRequest request) {
        Integer id = $int("id");
        Map<String, Object> params = $params(request);
        if(HelpUtils.nullOrBlank(id)) {
            crowdProjectService.addCrowdProject(params);
        }else {
            crowdProjectService.updateCrowdProject(params);
        }

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * 删除
     * @param request
     * @return
     */
    @RequestMapping(value = "/mgr/del", method = RequestMethod.POST)
    @ApiIgnore
    @ResponseBody
    public Resp delProjectOfMgr(HttpServletRequest request) {
        Integer id = $int("id");
       crowdProjectService.delCrowdProject(id);

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * 查找所有项目id
     * @param request
     * @return
     */
    @RequestMapping(value = "/mgr/project/ids", method = RequestMethod.GET)
    @ApiIgnore
    @ResponseBody
    public Resp findAllProjectId(HttpServletRequest request) {
        List<CrowdProject> list = crowdProjectService.findAllProjectIdsAndName();
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
    }

}
