package com.pmzhongguo.ex.api.controller;

import com.pmzhongguo.ex.business.entity.Article;
import com.pmzhongguo.ex.business.service.CmsService;
import com.pmzhongguo.ex.business.service.NewsService;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;
import com.pmzhongguo.ex.framework.service.CommonService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.yunpian.sdk.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "新闻接口", description = "获得新闻公告信息", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("n")
public class NewsController extends TopController
{
    @Resource
    private CmsService cmsService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private NewsService newsService;

    /**
     * 获得滚动图片新闻
     *
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "获得滚动新闻", notes = "获得滚动新闻", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/news/banner", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getBannerArticle(HttpServletRequest request, HttpServletResponse response)
    {
        Map data = $params(request);
        // 栏目内文章列表
        Map params = new HashMap();
        String column_id = data.get("column_id") == null? "303" : (String) data.get("column_id");
        params.put("page", 1);
        params.put("pagesize", 10);
        params.put("a_status", 23);
        params.put("a_is_banner", 1);
        // 303 顶部轮播图, 309 内容轮播图, 310 排列图
        params.put("column_id", StringUtils.isBlank(column_id) ? 303 : column_id);
        params.put("sortname", "a_order, id ");
        params.put("sortorder", "asc, desc");

        // 只需要标题等信息，不要内容
        params.put("isPicAndTitle", "1");

        List list = cmsService.getAllArticle(params);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
    }

    /**
     * 根据栏目、数量获得最新新闻和公告列表
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "根据栏目、数量获得最新新闻和公告列表", notes = "根据栏目、数量获得最新新闻和公告列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/news/{columnId}/{page}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getTopArticleByColumn(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer columnId, @PathVariable Integer page, @PathVariable Integer pageSize)
    {

        Map list = getTopArticleByColumn(columnId, page, pageSize, true);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
    }


    @ApiOperation(value = "标题、内容关键词查询", notes = "根据栏目、数量获得最新新闻和公告列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/news/key/{page}/{pageSize}/{key}", method = RequestMethod.GET)
    @ResponseBody
    public Resp getArticleByTitleOrContent(HttpServletRequest request, HttpServletResponse response, @PathVariable String key, @PathVariable Integer page, @PathVariable Integer pageSize)
    {

        Map<String, Object> map = new HashMap<String, Object>();
        if (HelpUtils.nullOrBlank(key)) {
            return new Resp(Resp.FAIL, ErrorInfoEnum.LANG_FIND_CONDITION_NOT_NULL.getErrorENMsg());
        }
        map.put("a_title", key.trim());
        map.put("a_content", key.trim());
        map.put("page", page);
        map.put("pagesize", pageSize);
        List<Map<String, Object>> list = cmsService.findArticleTitleOrContentByPage(map);

        Map retMap = new HashMap();
        retMap.put("list", list);
        retMap.put("total", map.get("total"));
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, retMap);
    }

    /**
     * 获得前几条新闻
     *
     * @param columnId
     * @param page
     * @param pageSize
     * @param isPicAndTitle
     * @return
     */
    private Map getTopArticleByColumn(Integer columnId, Integer page, Integer pageSize, boolean isPicAndTitle)
    {
        // 栏目内文章列表
        Map params = new HashMap();
        params.put("page", page);
        params.put("pagesize", pageSize);
        params.put("column_id", columnId);
        params.put("a_status", 23);
        params.put("sortname", "a_order, id ");
        params.put("sortorder", "asc, desc");

        // 只需要标题等信息，不要内容
        if (isPicAndTitle) {
            params.put("isPicAndTitle", "1");
        }

        List list = cmsService.getAllArticle(params);

        Map retMap = new HashMap();
        retMap.put("list", list);
        retMap.put("total", params.get("total"));

        return retMap;
    }

    @ApiOperation(value = "获取一个新闻公告的详细内容", notes = "获取一个新闻公告的详细内容", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/news/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp appViewArticle(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id)
    {

        Article article = cmsService.findArticleById(id);

        // 把图片加上网址前缀
        String content = article.getA_content();
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        content = content.replaceAll("\"/ueditor/jsp/upload/image", "\"" + basePath + "/ueditor/jsp/upload/image");
        article.setA_content(content);

        // 更新阅读次数
        daoUtil.update("update cms_article set a_count = a_count + 1 where id = ?", id);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, article);
    }


    /**
     * 获得手续费费率
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "获得手续费费率", notes = "包括提现手续费（withdrawFees）和交易手续费（exFees）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/fees", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getFees(HttpServletRequest request, HttpServletResponse response)
    {

        Map map = new HashMap();
        List feeLst = daoUtil.queryForList("SELECT dsp_name, TRUNCATE(taker_fee * 100, 2) taker_fee, TRUNCATE(maker_fee * 100, 2) maker_fee FROM d_currency_pair WHERE p_status = 1");
        map.put("exFees", feeLst);

        List withdrawFeeLst = daoUtil.queryForList("SELECT currency, currency_name, withdraw_fee, withdraw_fee_percent, withdraw_fee_min, withdraw_fee_max FROM d_currency WHERE is_show = 1");
        map.put("withdrawFees", withdrawFeeLst);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, map);
    }


    @ApiOperation(value = "获得顶级分类列表", notes = "获得顶级分类列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/top-list", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getTopList()
    {

        List<Map<String, Object>> list = commonService.getColumnsWithTop();

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
    }

    @ApiOperation(value = "获取最新文章", notes = "获取最新文章", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/news/recently/{page}/{pageSize}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getRecentlyNews(@PathVariable Integer page, @PathVariable Integer pageSize)
    {
        Map<String, Integer> map = new HashMap();
        map.put("page", page);
        map.put("pagesize", pageSize);
        List<Map<String, Object>> list = cmsService.getRecentlyNews(map);

        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, list);
    }

    @ApiOperation(value = "获取某个id下的标题", notes = "获取某个id下的标题", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/news/{id}/submenu/title/{count}", method = RequestMethod.GET)
    @ResponseBody
    public ObjResp getSubMenuAndTitleWithParentId(@PathVariable Integer id, @PathVariable Integer count)
    {
        Map<String, Integer> map = new HashMap();
        map.put("page", 0);
        map.put("pagesize", count);
        List<Map<String, Object>> idList = daoUtil.queryForList("SELECT cc.id,cc.c_name as parent_name,cc.c_name_en as parent_name_en,parent_id from cms_column cc where parent_id = ?", id);
        List retList = new ArrayList<>(idList.size());
        for (int i = 0; i < idList.size(); i++) {
            Map<String, Object> column = idList.get(i);
            Map params = new HashMap();
            params.put("column_id", column.get("id"));
            params.put("pagesize", count);
            params.put("page", 1);
            List<Map<String, Object>> articleList = cmsService.getRecentlyNews(params);

            Map<String, Object> subMenu = new HashMap<String, Object>(2);
            subMenu.put("parent_name", column.get("parent_name"));
            subMenu.put("parent_name_en", column.get("parent_name_en"));
            subMenu.put("list", articleList);
            subMenu.put("total", params.get("total"));
            subMenu.put("parent_id", column.get("parent_id"));
            subMenu.put("id", column.get("id"));

            retList.add(subMenu);
        }
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, retList);
    }

    /**
     * 获取金色快讯列表
     *
     * @param pageSize
     * @param page
     * @param flag
     * @return
     */
    @ApiOperation(value = "获取金色快讯列表", notes = "获取金色快讯列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @GetMapping("/list/{page}/{pageSize}")
    @ResponseBody
    public ObjResp newsList(@PathVariable Integer pageSize, @PathVariable Integer page, String flag)
    {
        // down 拉取最新
        Map<String, Object> result = newsService.getNewsList(pageSize, page, StringUtil.isNullOrEmpty(flag) ? "down" : flag);
        return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, result);
    }
}
//1