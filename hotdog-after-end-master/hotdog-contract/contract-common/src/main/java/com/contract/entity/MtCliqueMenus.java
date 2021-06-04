package com.contract.entity;

public class MtCliqueMenus {
    /**  */
    private Integer id;

    /** 父级id  0表示父级 */
    private Integer parentid;

    /** 菜单名称 */
    private String name;

    /** 菜单url */
    private String url;

    /** 0有效  -1无效 */
    private Short status;

    /** 图标 */
    private String icon;

    /** 排序  从大到小降序 */
    private Integer sort;

    /** 1菜单  2操作按钮 */
    private Integer type;

    private String checked;
    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mt_clique_menus.id
     *
     * @return the value of mt_clique_menus.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mt_clique_menus.id
     *
     * @param id the value for mt_clique_menus.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mt_clique_menus.parentid
     *
     * @return the value of mt_clique_menus.parentid
     *
     * @mbggenerated
     */
    public Integer getParentid() {
        return parentid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mt_clique_menus.parentid
     *
     * @param parentid the value for mt_clique_menus.parentid
     *
     * @mbggenerated
     */
    public void setParentid(Integer parentid) {
        this.parentid = parentid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mt_clique_menus.name
     *
     * @return the value of mt_clique_menus.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mt_clique_menus.name
     *
     * @param name the value for mt_clique_menus.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mt_clique_menus.url
     *
     * @return the value of mt_clique_menus.url
     *
     * @mbggenerated
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mt_clique_menus.url
     *
     * @param url the value for mt_clique_menus.url
     *
     * @mbggenerated
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mt_clique_menus.status
     *
     * @return the value of mt_clique_menus.status
     *
     * @mbggenerated
     */
    public Short getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mt_clique_menus.status
     *
     * @param status the value for mt_clique_menus.status
     *
     * @mbggenerated
     */
    public void setStatus(Short status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mt_clique_menus.icon
     *
     * @return the value of mt_clique_menus.icon
     *
     * @mbggenerated
     */
    public String getIcon() {
        return icon;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mt_clique_menus.icon
     *
     * @param icon the value for mt_clique_menus.icon
     *
     * @mbggenerated
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mt_clique_menus.sort
     *
     * @return the value of mt_clique_menus.sort
     *
     * @mbggenerated
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mt_clique_menus.sort
     *
     * @param sort the value for mt_clique_menus.sort
     *
     * @mbggenerated
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column mt_clique_menus.type
     *
     * @return the value of mt_clique_menus.type
     *
     * @mbggenerated
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column mt_clique_menus.type
     *
     * @param type the value for mt_clique_menus.type
     *
     * @mbggenerated
     */
    public void setType(Integer type) {
        this.type = type;
    }

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}
    
    
}