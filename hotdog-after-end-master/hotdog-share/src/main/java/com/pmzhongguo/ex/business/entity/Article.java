package com.pmzhongguo.ex.business.entity;

import java.io.Serializable;

public class Article implements Serializable
{
    private static final long serialVersionUID = -7145856619799866168L;

    private Integer id;//文章ID
    private Integer column_id;//所属栏目ID
    private String a_title;//文章标题
    private String a_title_en;
    private String a_abstract;//文章摘要
    private String a_abstract_en;//文章摘要
    private String a_img_file;//文章缩略图
    private String a_img_file_en;
    private String a_source;//文章来源
    private String a_author;//文章作者
    private String a_time;//发布时间
    private Integer a_count;//点击次数
    private Integer a_content_type; //内容方式，0表示内容，1表示连接
    private String a_content;//文章内容
    private Integer a_is_banner; //是否轮播图
    private String a_content_en;//文章内容
    private String a_viedo_url; //视频链接
    private Integer a_viedo_width;
    private Integer a_viedo_height;
    private String a_relation_article_ids;//相关文章ID，以逗号分隔
    private Integer a_order; //排序，按升序排
    private Integer creator_id;//发布人ID
    private String creator_name;//发布人姓名
    private String create_time;//发布时间
    private Integer auditor_id;//审核人ID
    private String auditor_name;//审核人姓名
    private String audit_time;//审核时间
    private Integer a_status;//状态，关联字典article_status
    private String audit_comments;//审核意见，历史意见均保留

    private Integer thumb_width; //缩略图宽度
    private Integer thumb_height; //缩略图高度
    private String column_name; //栏目名称

    private Integer site_id; //所属网站ID
    private String jump_url; //跳转链接
    private Integer is_jump; //1跳转，0不跳转

    /**
     * 韩文
     */
    private String a_title_ko;
    private String a_abstract_ko;
    private String a_img_file_ko;
    private String a_content_ko;

    /**
     * 日文
     */
    private String a_title_jp;
    private String a_abstract_jp;
    private String a_img_file_jp;
    private String a_content_jp;

    public String getA_abstract_en()
    {
        return a_abstract_en;
    }

    public void setA_abstract_en(String a_abstract_en)
    {
        this.a_abstract_en = a_abstract_en;
    }

    public String getA_img_file_en()
    {
        return a_img_file_en;
    }

    public void setA_img_file_en(String a_img_file_en)
    {
        this.a_img_file_en = a_img_file_en;
    }

    public Integer getA_is_banner()
    {
        return a_is_banner;
    }

    public void setA_is_banner(Integer a_is_banner)
    {
        this.a_is_banner = a_is_banner;
    }

    public String getA_title_en()
    {
        return a_title_en;
    }

    public void setA_title_en(String a_title_en)
    {
        this.a_title_en = a_title_en;
    }

    public String getA_content_en()
    {
        return a_content_en;
    }

    public void setA_content_en(String a_content_en)
    {
        this.a_content_en = a_content_en;
    }

    public Integer getSite_id()
    {
        return site_id;
    }

    public void setSite_id(Integer site_id)
    {
        this.site_id = site_id;
    }

    public String getColumn_name()
    {
        return column_name;
    }

    public void setColumn_name(String column_name)
    {
        this.column_name = column_name;
    }

    public Integer getA_viedo_width()
    {
        return a_viedo_width;
    }

    public void setA_viedo_width(Integer a_viedo_width)
    {
        this.a_viedo_width = a_viedo_width;
    }

    public Integer getA_viedo_height()
    {
        return a_viedo_height;
    }

    public void setA_viedo_height(Integer a_viedo_height)
    {
        this.a_viedo_height = a_viedo_height;
    }

    public String getA_viedo_url()
    {
        return a_viedo_url;
    }

    public void setA_viedo_url(String a_viedo_url)
    {
        this.a_viedo_url = a_viedo_url;
    }

    public Integer getThumb_width()
    {
        return thumb_width;
    }

    public void setThumb_width(Integer thumb_width)
    {
        this.thumb_width = thumb_width;
    }

    public Integer getThumb_height()
    {
        return thumb_height;
    }

    public void setThumb_height(Integer thumb_height)
    {
        this.thumb_height = thumb_height;
    }

    public Integer getA_content_type()
    {
        return a_content_type;
    }

    public void setA_content_type(Integer a_content_type)
    {
        this.a_content_type = a_content_type;
    }

    public Integer getA_order()
    {
        return a_order;
    }

    public void setA_order(Integer a_order)
    {
        this.a_order = a_order;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getColumn_id()
    {
        return column_id;
    }

    public void setColumn_id(Integer column_id)
    {
        this.column_id = column_id;
    }

    public String getA_title()
    {
        return a_title;
    }

    public void setA_title(String a_title)
    {
        this.a_title = a_title;
    }

    public String getA_abstract()
    {
        return a_abstract;
    }

    public void setA_abstract(String a_abstract)
    {
        this.a_abstract = a_abstract;
    }

    public String getA_img_file()
    {
        return a_img_file;
    }

    public void setA_img_file(String a_img_file)
    {
        this.a_img_file = a_img_file;
    }

    public String getA_source()
    {
        return a_source;
    }

    public void setA_source(String a_source)
    {
        this.a_source = a_source;
    }

    public String getA_author()
    {
        return a_author;
    }

    public void setA_author(String a_author)
    {
        this.a_author = a_author;
    }

    public String getA_time()
    {
        return a_time;
    }

    public void setA_time(String a_time)
    {
        this.a_time = a_time;
    }

    public Integer getA_count()
    {
        return a_count;
    }

    public void setA_count(Integer a_count)
    {
        this.a_count = a_count;
    }

    public String getA_content()
    {
        return a_content;
    }

    public void setA_content(String a_content)
    {
        this.a_content = a_content;
    }

    public String getA_relation_article_ids()
    {
        return a_relation_article_ids;
    }

    public void setA_relation_article_ids(String a_relation_article_ids)
    {
        this.a_relation_article_ids = a_relation_article_ids;
    }

    public Integer getCreator_id()
    {
        return creator_id;
    }

    public void setCreator_id(Integer creator_id)
    {
        this.creator_id = creator_id;
    }

    public String getCreator_name()
    {
        return creator_name;
    }

    public void setCreator_name(String creator_name)
    {
        this.creator_name = creator_name;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public Integer getAuditor_id()
    {
        return auditor_id;
    }

    public void setAuditor_id(Integer auditor_id)
    {
        this.auditor_id = auditor_id;
    }

    public String getAuditor_name()
    {
        return auditor_name;
    }

    public void setAuditor_name(String auditor_name)
    {
        this.auditor_name = auditor_name;
    }

    public String getAudit_time()
    {
        return audit_time;
    }

    public void setAudit_time(String audit_time)
    {
        this.audit_time = audit_time;
    }

    public Integer getA_status()
    {
        return a_status;
    }

    public void setA_status(Integer a_status)
    {
        this.a_status = a_status;
    }

    public String getAudit_comments()
    {
        return audit_comments;
    }

    public void setAudit_comments(String audit_comments)
    {
        this.audit_comments = audit_comments;
    }

    public String getJump_url()
    {
        return jump_url;
    }

    public void setJump_url(String jump_url)
    {
        this.jump_url = jump_url;
    }

    public Integer getIs_jump()
    {
        return is_jump;
    }

    public void setIs_jump(Integer is_jump)
    {
        this.is_jump = is_jump;
    }

    public String getA_title_ko()
    {
        return a_title_ko;
    }

    public void setA_title_ko(String a_title_ko)
    {
        this.a_title_ko = a_title_ko;
    }

    public String getA_abstract_ko()
    {
        return a_abstract_ko;
    }

    public void setA_abstract_ko(String a_abstract_ko)
    {
        this.a_abstract_ko = a_abstract_ko;
    }

    public String getA_img_file_ko()
    {
        return a_img_file_ko;
    }

    public void setA_img_file_ko(String a_img_file_ko)
    {
        this.a_img_file_ko = a_img_file_ko;
    }

    public String getA_content_ko()
    {
        return a_content_ko;
    }

    public void setA_content_ko(String a_content_ko)
    {
        this.a_content_ko = a_content_ko;
    }

    public String getA_title_jp()
    {
        return a_title_jp;
    }

    public void setA_title_jp(String a_title_jp)
    {
        this.a_title_jp = a_title_jp;
    }

    public String getA_abstract_jp()
    {
        return a_abstract_jp;
    }

    public void setA_abstract_jp(String a_abstract_jp)
    {
        this.a_abstract_jp = a_abstract_jp;
    }

    public String getA_img_file_jp()
    {
        return a_img_file_jp;
    }

    public void setA_img_file_jp(String a_img_file_jp)
    {
        this.a_img_file_jp = a_img_file_jp;
    }

    public String getA_content_jp()
    {
        return a_content_jp;
    }

    public void setA_content_jp(String a_content_jp)
    {
        this.a_content_jp = a_content_jp;
    }
}
