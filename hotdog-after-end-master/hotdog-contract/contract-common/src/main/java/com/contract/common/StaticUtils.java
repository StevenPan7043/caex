package com.contract.common;

public class StaticUtils {

	
	public static final Integer status_yes=0;//有效
	public static final Integer status_no=-1;//无效
	
	public static final Integer pay_in=1;//收入
	public static final Integer pay_out=0;//支出
	
	public static final String DEFAULT_PASSWORD="888888";
	
	/**
	 *提现状态
	 */
	public static final Integer cash_ing=1;//待审核
	public static final Integer cash_success=2;//审核通过
	public static final Integer cash_refund=3;//审核驳回
	
	public static final Integer dz_no=-1;//未对账；
	public static final Integer dz_yes=0;//对账成功
	public static final Integer dz_reset=1;//重审
	
	/**
	 * 实名认证状态
	 */
	public static final Integer auth_wait=1;//待审核
	public static final Integer auth_ing=2;//审核中
	public static final Integer auth_success=3;//已审核
	
	/**
	 * 委托
	 */
	public static final Integer entrust_ing=1;//委托中
	public static final Integer entrust_success=2;//委托成功
	public static final Integer entrust_cancel=3;//委托失败
	
	/**
	 * 会员身份
	 */
	public static final Integer identity_customer=1;//普通会员
	public static final Integer identity_agent=2;//业务会员
	public static final Integer identity_test=3;//模拟会员
	
}
