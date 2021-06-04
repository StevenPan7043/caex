package com.pmzhongguo.ex.business.vo;

import com.pmzhongguo.zzextool.annotation.ExcelField;


/**
 * @description: 导出excel
 * @date: 2019-05-31 16:43
 * @author: 十一
 */
public class OperLogExcelVo
{


    @ExcelField(title = "操作人", align = 2, sort = 1)
    private String user_real_name;

    @ExcelField(title = "操作内容", align = 2, sort = 2)
    private String oper_desc;

    @ExcelField(title = "操作时间", align = 2, sort = 3)
    private String timestr;


    @ExcelField(title = "操作ip", align = 2, sort = 4)
    private String user_ip;

    @ExcelField(title = "操作URL", align = 2, sort = 5)
    private String oper_url;

    public String getUser_real_name()
    {
        return user_real_name;
    }

    public void setUser_real_name(String user_real_name)
    {
        this.user_real_name = user_real_name;
    }

    public String getOper_desc()
    {
        return oper_desc;
    }

    public void setOper_desc(String oper_desc)
    {
        this.oper_desc = oper_desc;
    }

    public String getTimestr()
    {
        return timestr;
    }

    public void setTimestr(String timestr)
    {
        this.timestr = timestr;
    }

    public String getUser_ip()
    {
        return user_ip;
    }

    public void setUser_ip(String user_ip)
    {
        this.user_ip = user_ip;
    }

    public String getOper_url()
    {
        return oper_url;
    }

    public void setOper_url(String oper_url)
    {
        this.oper_url = oper_url;
    }
}
