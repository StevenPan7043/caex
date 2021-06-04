package com.pmzhongguo.ipfs.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.dto.CurrencyVerticalDto;
import com.pmzhongguo.ex.business.entity.Currency;
import com.pmzhongguo.ex.business.entity.CurrencyIntroduce;
import com.pmzhongguo.ex.business.entity.CurrencyPair;
import com.pmzhongguo.ex.business.service.*;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ChainTypeEnum;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ipfs.entity.IpfsOutput;
import com.pmzhongguo.ipfs.entity.IpfsProject;
import com.pmzhongguo.ipfs.entity.IpfsProjectWithBLOBs;
import com.pmzhongguo.ipfs.service.IpfsOutputManager;
import com.pmzhongguo.ipfs.service.IpfsProjectManager;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiIgnore
@Controller
@RequestMapping("backstage/ipfs")
public class ProjectBackgroundController extends TopController {

    @Autowired
    private IpfsProjectManager ipfsProjectManager;
    @Autowired
    private IpfsOutputManager ipfsOutputManager;

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "ipfs_list", method = RequestMethod.GET)
    public String toListIpfs(HttpServletRequest request,
                             HttpServletResponse response) {
        return "ipfs/ipfs_list";
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "ipfs_add", method = RequestMethod.GET)
    public String toAddIpfs(HttpServletRequest request,
                            HttpServletResponse response) {
        return "ipfs/ipfs_edit";
    }

    /**
     * @param request
     * @param record
     * @return
     */
    @RequestMapping(value = "ipfs_add_do", method = RequestMethod.POST)
    @ResponseBody
    public Resp addIpfs(HttpServletRequest request, IpfsProjectWithBLOBs record) {
        // 类型为算力，兑换比率肯定是1
        if (record.getType().equals("1")) {
            record.setExchangeRate(BigDecimal.ONE);
        }
        ipfsProjectManager.insert(record);
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * 虚拟币编辑界面
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "ipfs_edit", method = RequestMethod.GET)
    public String toEditIpfs(HttpServletRequest request,
                             HttpServletResponse response) {
        IpfsProjectWithBLOBs info = ipfsProjectManager.selectByPrimaryKey($int("id"));
        $attr("info", info);
        return "ipfs/ipfs_edit";
    }

    /**
     * @param request
     * @param record
     * @return
     */
    @RequestMapping(value = "ipfs_edit_do", method = RequestMethod.POST)
    @ResponseBody
    public Resp editIpfs(HttpServletRequest request, IpfsProjectWithBLOBs record) {
        // 类型为算力，兑换比率肯定是1
        if (record.getType().equals("1")) {
            record.setExchangeRate(BigDecimal.ONE);
        }

        if (record.getBoughtNum() > record.getPublishNum()) {
            record.setBoughtNum(record.getPublishNum());
        }
        //当项目停止运行时所有分红全部关闭
        if ("3".equals(record.getRunStatus())) {
            IpfsOutput out = new IpfsOutput();
            out.setProjectId(record.getId());
            out.setOutputStatus(2);
            ipfsOutputManager.updateIpfsOutput(out);
        }
        ipfsProjectManager.updateByPrimaryKeyWithBLOBs(record);
        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    /**
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "ipfs", method = RequestMethod.GET, consumes = "application/json")
    @ResponseBody
    public Map listIpfs(HttpServletRequest request, HttpServletResponse response) {
        Map params = $params(request);
        List<IpfsProject> list = ipfsProjectManager.findByConditionPage(params);
        Map map = new HashMap();
        map.put("Rows", list);
        map.put("Total", params.get("total"));
        return map;
    }

}
