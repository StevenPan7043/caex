package com.pmzhongguo.gd.controller;


import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.gd.entity.GdProject;
import com.pmzhongguo.gd.service.GdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pmzhongguo.ex.core.web.TopController.*;

@Controller
@RequestMapping("backstage/gd/")
public class GdController {


    @Autowired
    private GdService gdservice;

    /**
     * @Author lidongxu
     * @Description //跟单项目页面
     * @Date 14:56 2020/12/21
     * @Param []
     * @return java.lang.String
     **/
    @RequestMapping("gd_list")
    public String gdList(){
        return "gd/gd_list";
    }


    /**
     * @Author lidongxu
     * @Description //跟单列表
     * @Date 14:56 2020/12/21
     * @Param
     * @return
     **/
    @RequestMapping("queryGdList")
    @ResponseBody
    public Map queryGdList(HttpServletRequest request){
        Map params = $params(request);

        List<GdProject> list = gdservice.queryGdListPage(params);
        Map map = new HashMap();
        map.put("Rows", list);
        map.put("Total", params.get("total"));
        return map;
    }

    /**
     * @Author lidongxu
     * @Description //跳转添加跟单项目
     * @Date 15:23 2020/12/21
     * @Param
     * @return
     **/
    @RequestMapping("gd_add")
    public String gdAdd(){
        return "gd/gd_edit";
    }

    /**
     * @Author lidongxu
     * @Description //添加跟单
     * @Date 15:46 2020/12/21
     * @Param [request, gdProject]
     * @return com.pmzhongguo.ex.core.web.resp.Resp
     **/
    @RequestMapping("gd_add_do")
    @ResponseBody
    public Resp  gdAddDo(HttpServletRequest request,GdProject gdProject){
        return gdservice.insert(gdProject);
    }

    /**
     * @Author lidongxu
     * @Description //修改跟单页面
     * @Date 15:47 2020/12/21
     * @Param
     * @return
     **/
    @RequestMapping("gd_edit")
    public String gdEdit(HttpServletRequest request){
        GdProject gd = gdservice.selectById($int("id"));
        $attr("info",gd);
        return "gd/gd_edit";
    }

    /**
     * @Author lidongxu
     * @Description //修改跟单
     * @Date 15:51 2020/12/21
     * @Param [request, gdProject]
     * @return com.pmzhongguo.ex.core.web.resp.Resp
     **/
    @RequestMapping("gd_edit_do")
    @ResponseBody
    public Resp gdEditDo(HttpServletRequest request,GdProject gdProject){
        return gdservice.update(gdProject);
    }
}
