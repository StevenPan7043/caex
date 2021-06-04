package com.pmzhongguo.ex.business.mapper;

import java.util.List;
import java.util.Map;

import com.pmzhongguo.ex.business.entity.Article;
import com.pmzhongguo.ex.business.entity.Column;
import com.pmzhongguo.ex.core.mapper.SuperMapper;


public interface CmsMapper extends SuperMapper {

	public List listColumnPage(Map params);

	public Column loadColumnById(Integer id);

	public void addColumn(Column station);

	public void updateColumn(Column station);

	public void delColumn(Integer id);

	public List listArticlePage(Map params);
	public List listArticleSimple(Map params);
	public Map listArticleCount(Map params);
	public List findArticleTitleOrContentPage(Map params);

	public Article loadArticleById(Integer id);

	public void delArticle(Integer id);

	public Article loaddelArticleById(Integer id);

	public void addArticle(Article article);

	public void updateArticle(Article article);

	public void updateArticleStatus(Article article);

	public void setArticleThumb(Article article);

	List<Map<String,Object>> findArticleWithRecentlyByPage(Map<String,Integer> map);

	List<Map<String,Object>> loadColumns();
}
