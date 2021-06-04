package com.pmzhongguo.ex.business.service;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pmzhongguo.ex.business.entity.Article;
import com.pmzhongguo.ex.business.entity.Column;
import com.pmzhongguo.ex.business.mapper.CmsMapper;
import com.pmzhongguo.ex.core.service.BaseServiceSupport;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.PaginData;
import com.pmzhongguo.ex.framework.service.CommonService;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


@Service
@Transactional
public class CmsService extends BaseServiceSupport implements IDataProcess {
	@Autowired
	private CmsMapper cmsMapper;

	public List getAllColumn(Map params) {
		return cmsMapper.listColumnPage(params);
	}
	public List<Map<String,Object>> findArticleTitleOrContentByPage(Map params) {
		List<Map<String,Object>> list = cmsMapper.findArticleTitleOrContentPage(params);
		List<Map<String,Object>> articleList = new ArrayList<>();
		for(Map<String,Object> map : list) {
			Integer columnId = (Integer) map.get("column_id");
			// 先从本地缓存获取
			ConcurrentHashMap<Integer, LinkedHashMap<Integer,Object>> articleParentInfo = HelpUtils.getArticleParentInfo();
			LinkedHashMap<Integer,Object> cacheInfo = new LinkedHashMap<Integer,Object>();
			if (articleParentInfo != null) {
				// 是否缓存了该文章id的路径信息
				cacheInfo = articleParentInfo.get(columnId);
				// 如果缓存没有就去数据库查,并缓存
				if(cacheInfo == null) {
					cacheInfo = cacheArticleInfo(columnId);
				}
			}else {
				// 如果缓存没有就去数据库查,并缓存
				cacheInfo = cacheArticleInfo(columnId);
			}

			map.put("parent_info",cacheInfo);
			articleList.add(map);
		}
		return articleList;
	}

	/**
	 * 缓存某篇文章父id的路径
	 * @param columnId
	 * @return
	 */
	private LinkedHashMap<Integer,Object> cacheArticleInfo(Integer columnId) {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		LinkedHashMap<Integer,Object> cacheInfo = findArticlePathInfo(columnId);
		ConcurrentHashMap<Integer,LinkedHashMap<Integer,Object>> concurrentHashMap
				= (ConcurrentHashMap<Integer,LinkedHashMap<Integer,Object>>)wac.getServletContext().getAttribute(HelpUtils.ARTICLE_TITLE_ID);
		if (concurrentHashMap == null) {
			concurrentHashMap = new ConcurrentHashMap<Integer,LinkedHashMap<Integer,Object>>();
		}
		concurrentHashMap.put(columnId,cacheInfo);
		// 将这个文章的路径缓存到本地
		wac.getServletContext().setAttribute(HelpUtils.ARTICLE_TITLE_ID,concurrentHashMap);
		return cacheInfo;
	}

	/**
	 * 更新缓存某篇文章父id的路径
	 * @param columnId
	 */
	public void cacheUpdateArticle(Integer columnId) {
		ConcurrentHashMap<Integer, LinkedHashMap<Integer,Object>> articleParentInfo = HelpUtils.getArticleParentInfo();
		if (articleParentInfo == null) {
			articleParentInfo = new ConcurrentHashMap<Integer, LinkedHashMap<Integer,Object>>();
		}
		LinkedHashMap<Integer, Object> cacheInfo = articleParentInfo.get(columnId);
		// 如果不存在就缓存
		if(null == cacheInfo || cacheInfo.size() == 0) {
			cacheArticleInfo(columnId);
		}else {
			// 如果存在就先移除再缓存
			articleParentInfo.remove(columnId);
			cacheArticleInfo(columnId);
		}

	}

	public Column findColumnById(Integer id) {
		return cmsMapper.loadColumnById(id);
	}

	public void addColumn(Column station) {
		cmsMapper.addColumn(station);
	}

	public void updateColumn(Column station) {
		cmsMapper.updateColumn(station);
	}

	public void delColumn(Integer id) {
		cmsMapper.delColumn(id);

	}

	public List getAllArticle(Map params) {
		return cmsMapper.listArticlePage(params);
	}
	
	public PaginData getAllArticleSimple(Map params) {
		//return cmsMapper.listArticlePage(params);
		
		PaginData pd = new PaginData(params, 1);
		pd.setList(cmsMapper.listArticleSimple(params));
		Map dataMap = cmsMapper.listArticleCount(params);
		pd.setCount(Integer.parseInt(dataMap.get("total") + ""));
		pd.setStats(dataMap);
		return pd;
	}

	public void delArticle(Integer id) {
		cmsMapper.delArticle(id);
		JedisUtil.getInstance().del("kexengine_article_" + id);
	}
	
	public Article findArticleById(Integer id) {
		
		Article article = article = cmsMapper.loadArticleById(id);
		
		return article;
	}

	public void addArticle(Article article) {
		cmsMapper.addArticle(article);
	}

	public void updateArticle(Article article) {
		cmsMapper.updateArticle(article);
		JedisUtil.getInstance().del("kexengine_article_" + article.getId());
	}

	public void updateArticleStatus(Article article) {
		cmsMapper.updateArticleStatus(article);
		
		JedisUtil.getInstance().del("kexengine_article_" + article.getId());
	}
	
	public void cacheNoticeLst(ServletContext servletContext) {
		Integer columnId = 100;
		Integer num = 5;
		boolean isPicAndTitle = false;
		// 栏目内文章列表
		Map params = new HashMap();
		params.put("page", 1);
		params.put("pagesize", num);
		params.put("wwwOrder", "order by a_order ASC, a.id DESC");
		params.put("column_id", columnId);
		params.put("a_status", 23);
		if (isPicAndTitle) {
			params.put("isPicAndTitle", "1");
		}
		
		PaginData pd = getAllArticleSimple(params);
		
		servletContext.setAttribute(HelpUtils.NOTICE_LST, pd.getList());
	}
	
	
	public void cacheNewsLst(ServletContext servletContext) {
		Integer columnId = 1;
		Integer num = 5;
		boolean isPicAndTitle = false;
		// 栏目内文章列表
		Map params = new HashMap();
		params.put("page", 1);
		params.put("pagesize", num);
		params.put("wwwOrder", "order by a_order ASC, a.id DESC");
		params.put("column_id", columnId);
		params.put("a_status", 23);
		if (isPicAndTitle) {
			params.put("isPicAndTitle", "1");
		}
		
		List list = getAllArticle(params);
		
		servletContext.setAttribute(HelpUtils.NEWS_LST, list);
	}
	
	public void cacheHelpLst(ServletContext servletContext) {
		Integer columnId = 200;
		Integer num = 10;
		boolean isPicAndTitle = false;
		// 栏目内文章列表
		Map params = new HashMap();
		params.put("page", 1);
		params.put("pagesize", num);
		params.put("wwwOrder", "order by a_order ASC, a.id DESC");
		params.put("column_id", columnId);
		params.put("a_status", 23);
		if (isPicAndTitle) {
			params.put("isPicAndTitle", "1");
		}
		
		List list = getAllArticle(params);
		
		servletContext.setAttribute(HelpUtils.HELP_LST, list);
	}

	public void setArticleThumb(Article article) {
		cmsMapper.setArticleThumb(article);
	}
	
	
	/**
	 * 生成首页
	 */
	public void genIndexHtml(HttpServletRequest request, Integer siteId) {
		// 获得首页的URL
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
		String _url = basePath + "/noau_cms/viewIndex?siteId=" + siteId;
		
		// 获得保存文件的位置
		String realRootPath = request.getSession().getServletContext().getRealPath(File.separator);
		
		//HtmlUtil.JspToHtmlByURL(_url, realRootPath + "/index.html");
	}
	
	/**
	 * 生成文章页
	 */
	public void genArticleHtml(HttpServletRequest request, Integer aritcleId) {
		// 获得文章的URL
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
		String _url = basePath + "/noau_cms/viewArticle?id=" + aritcleId;
		
		// 获得保存文件的位置
		String realRootPath = request.getSession().getServletContext().getRealPath(File.separator);
		
	}

	public List<Map<String,Object>> getRecentlyNews(Map<String,Integer> map) {
		return cmsMapper.findArticleWithRecentlyByPage(map);
	}

	/**
	 * 传入文章的父id找到所有的父信息
	 * @param columnId 父id
	 * @return
	 */
	public LinkedHashMap<Integer,Object> findArticlePathInfo(Integer columnId) {
		List<Map<String, Object>> columns = cmsMapper.loadColumns();
		LinkedHashMap<Integer,Object> parentInfo = findArticleParentInfo(columns,columnId,new LinkedHashMap<>());
		return parentInfo;
	}

	/**
	 * 传入文章的父id找到所有的父信息，递归查找
	 * @param columns 所有的父信息
	 * @param columnId 文章的父id
	 * @param parentInfo 返回的路径信息
	 * @return
	 */
	private LinkedHashMap<Integer,Object> findArticleParentInfo(List<Map<String, Object>> columns,int columnId,LinkedHashMap<Integer,Object> parentInfo) {
		// 递归退出条件，如果columnId=0，说明是到顶级栏目了
		if(columnId == 0) {
			return parentInfo;
		}
		for (Map<String, Object> topArticle: columns) {
			Integer id = (Integer)topArticle.get("id");
			Integer parentId = (Integer)topArticle.get("parent_id");
			if (id == columnId) {
				parentInfo.put(columnId,topArticle);
				findArticleParentInfo(columns,parentId,parentInfo);
			}
		}
		return parentInfo;

	}

	@Override
	public void dataSync(ServletContext servletContext) {
		cacheNoticeLst(servletContext);
		cacheNewsLst(servletContext);
		cacheHelpLst(servletContext);
	}
}
