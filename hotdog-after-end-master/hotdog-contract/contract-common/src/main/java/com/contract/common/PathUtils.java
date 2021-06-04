package com.contract.common;

public class PathUtils {

	public static final String cus_head="/cus/head/"+DateUtil.toDateString(DateUtil.currentDate(), "yyyyMMdd")+"/";
	public static final String cus_qr="/cus/qr/"+DateUtil.toDateString(DateUtil.currentDate(), "yyyyMMdd")+"/";
	
	public static final String notice_url="/notice/"+DateUtil.toDateString(DateUtil.currentDate(), "yyyyMMdd")+"/";
	public static final String common_url="/common/"+DateUtil.toDateString(DateUtil.currentDate(), "yyyyMMdd")+"/";
	
	public static final String banner_url="/banner/"+DateUtil.toDateString(DateUtil.currentDate(), "yyyyMMdd")+"/";
	
	public static final String match_url="/match/"+DateUtil.toDateString(DateUtil.currentDate(), "yyyyMMdd")+"/";
	public static final String job_url="/jobs/"+DateUtil.toDateString(DateUtil.currentDate(), "yyyyMMdd")+"/";
}
