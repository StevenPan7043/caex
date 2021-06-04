package com.pmzhongguo.ex.business.entity;

public class Column
{
    private Integer id;//栏目ID
    private Integer parent_id;//父栏目，一级栏目填0
    private Integer site_id;//所属站点ID
    private String c_name;//栏目名称
    private String c_desc;//栏目说明
    private Integer c_type;//展示方式，关联字典，cms_column_type
    private String c_template_file;//自身模板文件
    private String c_article_template_file; //下属文章内容模板文件
    private Integer is_display_in_menu;//是否在菜单上展示，1：是，0：否
    private Integer c_order; //栏目排序，按升序排
    private Integer thumb_width; //缩略图宽度
    private Integer thumb_height; //缩略图高度
    private String is_use;//是否启用，1：是，0：否

    public String getC_article_template_file()
    {
        return c_article_template_file;
    }

    public void setC_article_template_file(String c_article_template_file)
    {
        this.c_article_template_file = c_article_template_file;
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

    public Integer getC_order()
    {
        return c_order;
    }

    public void setC_order(Integer c_order)
    {
        this.c_order = c_order;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getParent_id()
    {
        return parent_id;
    }

    public void setParent_id(Integer parent_id)
    {
        this.parent_id = parent_id;
    }

    public Integer getSite_id()
    {
        return site_id;
    }

    public void setSite_id(Integer site_id)
    {
        this.site_id = site_id;
    }

    public String getC_name()
    {
        return c_name;
    }

    public void setC_name(String c_name)
    {
        this.c_name = c_name;
    }

    public String getC_desc()
    {
        return c_desc;
    }

    public void setC_desc(String c_desc)
    {
        this.c_desc = c_desc;
    }

    public Integer getC_type()
    {
        return c_type;
    }

    public void setC_type(Integer c_type)
    {
        this.c_type = c_type;
    }

    public String getC_template_file()
    {
        return c_template_file;
    }

    public void setC_template_file(String c_template_file)
    {
        this.c_template_file = c_template_file;
    }

    public Integer getIs_display_in_menu()
    {
        return is_display_in_menu;
    }

    public void setIs_display_in_menu(Integer is_display_in_menu)
    {
        this.is_display_in_menu = is_display_in_menu;
    }

    public String getIs_use()
    {
        return is_use;
    }

    public void setIs_use(String is_use)
    {
        this.is_use = is_use;
    }
}
