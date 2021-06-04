package com.pmzhongguo.crowd.controller;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.crowd.entity.Banner;
import com.pmzhongguo.crowd.service.BannerService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 轮播图
 * @date: 2019-03-02 15:51
 * @author: 十一
 */
@Api(value = "banner轮播图", description = "banner轮播图", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/img")
public class BannerController extends TopController {

    @Autowired
    private BannerService bannerService;

    @ApiOperation(value = "获取轮播图列表", notes = "根据type获取不同场景的轮播图列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/banners/{scene}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp findByScene(@PathVariable String scene) {

        List<Map<String,Object>> bannerList = bannerService.findByScene(scene);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, HelpUtils.newHashMap("data", bannerList, "total", bannerList.size()));
    }





    /**========================================后台管理路由，要加注解@ApiIgnore========================================================*/

    @ApiOperation(value = "分页获取轮播图列表", notes = "分页获取轮播图列表")
    @RequestMapping(value = "/mgr/banners/list_page", method = RequestMethod.GET)
    @ApiIgnore
    public String getBannerPage() {

        return "crowd/banner/banner_list";
    }

    @ApiOperation(value = "编辑页面", notes = "编辑页面")
    @RequestMapping(value = "/mgr/banner/edit_page", method = RequestMethod.GET)
    @ApiIgnore
    public String getEditPage() {
        Integer id = $int("id");
        if(!HelpUtils.nullOrBlank(id)) {
            Banner banner = bannerService.findById(id);
            $attr("info", banner);
        }
        for (Map.Entry<String, String> entry : getAliPolicy().entrySet()) {
            $attr(entry.getKey(), entry.getValue());
        }
        return "crowd/banner/banner_edit";
    }

    @ApiOperation(value = "分页获取轮播图列表", notes = "分页获取轮播图列表", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/mgr/banners/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiIgnore
    public Map findBannerByPage(HttpServletRequest request) {
        Map<String,Object> param = $params(request);
        List<Banner> list = bannerService.findMgrBannerByPage(param);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("Rows", list);
        map.put("Total", param.get("total"));
        return map;
    }

    @ApiOperation(value = "编辑/保存轮播图", notes = "编辑/保存轮播图")
    @RequestMapping(value = "/mgr/banner/edit", method = RequestMethod.POST)
    @ApiIgnore
    @ResponseBody
    public Resp editBanner(HttpServletRequest request) {

        Integer id = $int("id");
        if(!HelpUtils.nullOrBlank(id)) {
            bannerService.updateBanner($params(request));
        }else {
            bannerService.addBanner($params(request));
        }

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
    }

    @ApiOperation(value = "删除轮播图", notes = "删除轮播图", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/mgr/banner/del", method = RequestMethod.POST)
    @ApiIgnore
    @ResponseBody
    public Resp delBanner(HttpServletRequest request) {
        bannerService.deleteById($int("id"));

        return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

    }


}