package com.pmzhongguo.ex.business.controller;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ex.core.web.ErrorInfoEnum;
import com.pmzhongguo.zzextool.consts.JedisChannelConst;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.mangofactory.swagger.annotations.ApiIgnore;
import com.pmzhongguo.ex.business.entity.Article;
import com.pmzhongguo.ex.business.entity.Column;
import com.pmzhongguo.ex.business.service.CmsService;
import com.pmzhongguo.zzextool.exception.BusinessException;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.TopController;
import com.pmzhongguo.ex.core.web.resp.ObjResp;
import com.pmzhongguo.ex.core.web.resp.Resp;

@ApiIgnore
@Controller
@RequestMapping("backstage/cms")
public class CmsController extends TopController {
	@Autowired CookieLocaleResolver resolver;
	@Resource
	private CmsService cmsService;

	private static Logger zkLog = LoggerFactory.getLogger("zookeeper");
	
	/**
	 * 新闻栏目列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toColumnList")
	public String toColumnList(HttpServletRequest request,
			HttpServletResponse response) {
		return "business/cms/column_list";
	}

	/**
	 * 新闻栏目数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listColumn")
	@ResponseBody
	public Map listColumn(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map params = $params(request);

		// 站点ID
		params.put("site_id", 1);

		List jsonList = cmsService.getAllColumn(params);
		Map map = new HashMap();
		map.put("Rows", jsonList);
		map.put("Total", params.get("total"));
		return map;
	}

	/**
	 * 新闻栏目增加页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toAddColumn")
	public String toAddColumn(HttpServletRequest request,
			HttpServletResponse response) {

		$attr("user_site_id", 1);

		return "business/cms/column_edit";
	}

	/**
	 * 新闻栏目增加
	 * @param request
	 * @param column
	 * @return
	 */
	@RequestMapping(value = "/addColumn", method = RequestMethod.POST)
	@ResponseBody
	public Resp addColumn(HttpServletRequest request, Column column) {

		cmsService.addColumn(column);

		cmsService.genIndexHtml(request, null);

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	/**
	 * 新闻栏目编辑页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toEditColumn")
	public String toEditColumn(HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = $int("id");
		Column column = cmsService.findColumnById(id);
		$attr("info", column);

		$attr("user_site_id", 1);

		return "business/cms/column_edit";
	}

	/**
	 * 新闻栏目编辑
	 * @param request
	 * @param column
	 * @return
	 */
	@RequestMapping(value = "/editColumn", method = RequestMethod.POST)
	@ResponseBody
	public Resp editColumn(HttpServletRequest request, Column column) {
		cmsService.updateColumn(column);

		cmsService.genIndexHtml(request, null);

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	/**
	 * 删除新闻栏目
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delColumn", method = RequestMethod.POST)
	@ResponseBody
	public Resp delColumn(HttpServletRequest request) {
		Integer id = $int("id");

		// 判断能否删除：
		Integer isCanDel = daoUtil.queryForInt("SELECT COUNT(*) FROM ( "
				+ "SELECT parent_id FROM cms_column WHERE parent_id = ? "
				+ "UNION ALL "
				+ "SELECT column_id FROM cms_article WHERE column_id = ? "
				+ ") t", id, id);
		if (isCanDel > 0) {
			throw new BusinessException(-1, ErrorInfoEnum.DEL_COLUMN_TIP.getErrorCNMsg());
		}

		cmsService.delColumn(id);

		cmsService.genIndexHtml(request, null);

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	/**
	 * 文章界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toArticleList")
	public String toArticleList(HttpServletRequest request,
			HttpServletResponse response) {

		// 站点ID
		$attr("user_site_id", 1);

		return "business/cms/article_list";
	}

	/**
	 * 文章数据列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/listArticle")
	@ResponseBody
	public Map listArticle(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map params = $params(request);
		Map map = new HashMap();
		map.put("Rows", cmsService.getAllArticle(params));
		map.put("Total", params.get("total"));
		return map;
	}

	/**
	 * 文章新增界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toAddArticle")
	public String toAddArticle(HttpServletRequest request,
			HttpServletResponse response) {

		attrCommonDateTime();

		$attr("user_site_id", 1);

		$attr("defAuthor", HelpUtils.nullOrBlank(HelpUtils.getMgrConfig()
				.getCms_def_author()) ? HelpUtils.getFrmUser()
				.getUser_real_name() : HelpUtils.getMgrConfig()
				.getCms_def_author());

		return "business/cms/article_edit";
	}

	/**
	 * 增加文章
	 * @param request
	 * @param article
	 * @return
	 */
	@RequestMapping(value = "/addArticle", method = RequestMethod.POST)
	@ResponseBody
	public Resp addArticle(HttpServletRequest request, Article article) {

		article.setCreate_time(HelpUtils.formatDate8(new Date()));
		article.setCreator_id(HelpUtils.getFrmUser().getId());
		article.setCreator_name(HelpUtils.getFrmUser().getUser_real_name());
		
		/*
		if ("".equals(article.getA_abstract())) {
			article.setA_abstract($("a_content_txt").substring(
					0,
					($("a_content_txt").length() > 200 ? 200 : $(
							"a_content_txt").length())));
		}
		*/

		cmsService.addArticle(article);

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	/**
	 * 编辑文章界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toEditArticle")
	public String toEditArticle(HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = $int("id");
		Article article = cmsService.findArticleById(id);
		$attr("info", article);

		$attr("user_site_id", 1);

		return "business/cms/article_edit";
	}
	
	
	@RequestMapping("/toSetArticleThumb")
	public String toSetArticleThumb(HttpServletRequest request,
			HttpServletResponse response) {
		Integer id = $int("id");
		Article article = cmsService.findArticleById(id);
		$attr("info", article);
		
		for (Map.Entry<String, String> entry : getAliPolicy().entrySet()) {
			$attr(entry.getKey(), entry.getValue());
		}

		return "business/cms/thumb_file_upload";
	}
	
	
	@ApiIgnore
	@RequestMapping(value = "/noau_cms/addArticleThumbImg", method = RequestMethod.POST)
	@ResponseBody
	public ObjResp addImg(HttpServletRequest request) throws Exception {
		try {
			
			Map<String, Object> map = new HashMap<String, Object>();
			if (request.getHeader("content-type") != null
					&& "application/x-www-form-urlencoded".equals(request
							.getHeader("content-type"))) {
				return new ObjResp(Resp.FAIL, "error", null);
			}
			// 将请求转换成
			MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
			
			String folder = HelpUtils.formatDate4(new Date());
			String path = request.getSession().getServletContext()
					.getRealPath("/")
					+ "ueditor/jsp/upload/thumbpic/" + folder + "/";
			String fileName = UUID.randomUUID().toString();
	
			Iterator<String> fns = mRequest.getFileNames();// 获取上传的文件列表
			if (fns.hasNext()) {
				String s = fns.next();
	
				MultipartFile mFile = mRequest.getFile(s);
				if (mFile.isEmpty()) {
					map.put("error", "EventAction.picture.failed");
				} else {
	
					File dir = new File(path);
					// 如果文件夹不存在则创建
					if (!dir.exists() && !dir.isDirectory()) {
						dir.mkdir();
					}
					String originFileName = mFile.getOriginalFilename();
					String suffix = originFileName.split("\\.")[originFileName
							.split("\\.").length - 1];
					fileName += (System.currentTimeMillis() + HelpUtils.randomString(10) + "." + suffix);
					File file = new File(path, fileName);
					
					FileUtils.copyInputStreamToFile(mFile.getInputStream(), file);// 存储文件
				}
			}
			return new ObjResp(Resp.SUCCESS, Resp.SUCCESS_MSG, folder + "/" + fileName);
		} catch (IOException e) {
			return new ObjResp(Resp.FAIL, e.toString(), null);
		}
	}

	@RequestMapping(value = "/setArticleThumb")
	@ResponseBody
	public String setArticleThumb(HttpServletRequest request, HttpServletResponse response)
			throws IllegalStateException, IOException {
		
		Article article = new Article();
		article.setId(Integer.parseInt(request.getParameter("id")));
		
		String head = request.getScheme() + (request.getScheme().indexOf("www.") > 0 ? "s" : "") + "://" + request.getServerName();
		
		String imgFile = request.getParameter("a_img_file");
		article.setA_img_file(HelpUtils.nullOrBlank(imgFile) || imgFile.startsWith("http") ? imgFile : head + imgFile);

		String imgFileEn = request.getParameter("a_img_file_en");
		article.setA_img_file_en(HelpUtils.nullOrBlank(imgFileEn) || imgFile.startsWith("http") ? imgFileEn : head + imgFileEn);

		String imgFileKo = request.getParameter("a_img_file_ko");
		article.setA_img_file_ko(HelpUtils.nullOrBlank(imgFileKo) || imgFileKo.startsWith("http") ? imgFileKo : head + imgFileKo);

		String imgFileJp = request.getParameter("a_img_file_jp");
		article.setA_img_file_jp(HelpUtils.nullOrBlank(imgFileJp) || imgFileJp.startsWith("http") ? imgFileJp : head + imgFileJp);

		cmsService.setArticleThumb(article);

		
		return "<script>parent.subDialog.hide();parent.$grid.load(\"gridBox\");</script>";
		
	}

	/**
	 * 编辑文章
	 * @param request
	 * @param article
	 * @return
	 */
	@RequestMapping(value = "/editArticle", method = RequestMethod.POST)
	@ResponseBody
	public Resp editArticle(HttpServletRequest request, Article article) {
		cmsService.updateArticle(article);

		// 因为修改文章会变成待审核状态，因此要重新生成
		cmsService.genIndexHtml(request, null);
		
		cmsService.cacheNoticeLst(request.getServletContext());
		cmsService.cacheNewsLst(request.getServletContext());
		cmsService.cacheHelpLst(request.getServletContext());

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	/**
	 * 删除文章
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/delArticle", method = RequestMethod.POST)
	@ResponseBody
	public Resp delArticle(HttpServletRequest request) {
		Integer id = $int("id");

		cmsService.delArticle(id);

		cmsService.genIndexHtml(request, null);
		
		cmsService.cacheNoticeLst(request.getServletContext());
		cmsService.cacheNewsLst(request.getServletContext());
		cmsService.cacheHelpLst(request.getServletContext());

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	/**
	 * 所有文章排序加1
	 * @return
	 */
	@RequestMapping(value = "/upArticleOrder", method = RequestMethod.POST)
	@ResponseBody
	public Resp upArticleOrder() {

		Integer siteId = 1;

		daoUtil.update(
				"update cms_article set a_order = a_order + 1 where column_id in (select id from cms_column where site_id = ?) ",
				siteId);

		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);

	}

	/**
	 * 送审文章
	 * @return
	 */
	@RequestMapping(value = "/sendAuditArticle", method = RequestMethod.POST)
	@ResponseBody
	public Resp sendAuditArticle() {
		Integer id = $int("id");

		Integer isCanDel = daoUtil
				.queryForInt(
						"SELECT COUNT(*) from cms_article where id = ? and a_status = 20",
						id);
		if (isCanDel <= 0) {
			throw new BusinessException(-1, ErrorInfoEnum.SEND_AUDIT_ARTICLE_TIP.getErrorCNMsg());
		}

		Article article = new Article();
		article.setId(id);
		article.setA_status(21);
		cmsService.updateArticleStatus(article);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 审核通过文章
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/auditOkArticle", method = RequestMethod.POST)
	@ResponseBody
	public Resp auditOkArticle(HttpServletRequest request) {
		Integer id = $int("id");

		Integer isCanDel = daoUtil
				.queryForInt(
						"SELECT COUNT(*) from cms_article where id = ? and a_status = 21",
						id);
		if (isCanDel <= 0) {
			throw new BusinessException(-1, ErrorInfoEnum.AUDIT_ARTICLE_TIP.getErrorCNMsg());
		}

		Article article = new Article();
		article.setId(id);
		article.setA_status(23);
		article.setAudit_time(HelpUtils.formatDate8(new Date()));
		article.setAuditor_id(HelpUtils.getFrmUser().getId());
		article.setAuditor_name(HelpUtils.getFrmUser().getUser_real_name());
		cmsService.updateArticleStatus(article);

		cmsService.genIndexHtml(request, null);
		cmsService.genArticleHtml(request, id);
		
		cmsService.cacheNoticeLst(request.getServletContext());
		cmsService.cacheNewsLst(request.getServletContext());
		cmsService.cacheHelpLst(request.getServletContext());

		//   如果文章审核通过，就要把缓存中的文章更新下
		Article newArticle = cmsService.findArticleById(id);
		cmsService.cacheUpdateArticle(newArticle.getColumn_id());
		// 同步
		syncArticle();
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}

	/**
	 * 驳回文章
	 * @return
	 */
	@RequestMapping(value = "/auditNoArticle", method = RequestMethod.POST)
	@ResponseBody
	public Resp auditNoArticle() {
		Integer id = $int("id");

		Integer isCanDel = daoUtil
				.queryForInt(
						"SELECT COUNT(*) from cms_article where id = ? and a_status = 21",
						id);
		if (isCanDel <= 0) {
			throw new BusinessException(-1, ErrorInfoEnum.AUDIT_ARTICLE_TIP.getErrorCNMsg());
		}

		Article article = new Article();
		article.setId(id);
		article.setA_status(22);
		article.setAudit_time(HelpUtils.formatDate8(new Date()));
		article.setAuditor_id(HelpUtils.getFrmUser().getId());
		article.setAuditor_name(HelpUtils.getFrmUser().getUser_real_name());
		article.setAudit_comments(article.getAudit_time()
				+ "&nbsp;"
				+ article.getAuditor_name()
				+ "：<br>"
				+ $("audit_comments")
				+ "<br>---------------------------------------------------------<br><br>");
		cmsService.updateArticleStatus(article);
		return new Resp(Resp.SUCCESS, Resp.SUCCESS_MSG);
	}


	/**
	 * 文章审核界面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/toArticleAuditList")
	public String toArticleAuditList(HttpServletRequest request,
									 HttpServletResponse response) {

		// 站点ID
		$attr("user_site_id", 1);

		return "business/cms/article_audit_list";
	}


	/**
	 * 同步文章数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/sync/article")
	public Resp syncArticle() {
		JedisUtil.getInstance().publish(JedisChannelConst.JEDIS_CHANNEL_SYNC_NEWS,JedisChannelConst.SYNC_MESSAGE);
		return new Resp(Resp.SUCCESS,Resp.SUCCESS_MSG);

	}
}
