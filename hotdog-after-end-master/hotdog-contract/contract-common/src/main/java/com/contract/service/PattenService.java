package com.contract.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contract.common.FunctionUtils;
import com.contract.common.StaticUtils;
import com.contract.entity.CCustomer;
import com.contract.enums.HandleTypeEnums;
import com.contract.enums.SysParamsEnums;
import com.contract.service.redis.RedisUtilsService;

@Service
public class PattenService {

	@Autowired
	private RedisUtilsService redisUtilsService;
	@Autowired
	private BipbService bipbService;
	/**
	 * 处理推荐奖
	 * @param customer 触发人
	 * @param money 手续费
	 * @param remark 备注
	 */
	public void handlePatten(CCustomer customer,BigDecimal money,String remark) {
		String open_push=redisUtilsService.getKey(SysParamsEnums.open_push.getKey());
		if("0".equals(open_push)) {
			if(customer!=null && customer.getParentid()!=null && money!=null && money.compareTo(BigDecimal.ZERO)>0) {
				String push_scale=redisUtilsService.getKey(SysParamsEnums.push_scale.getKey());
				BigDecimal scale=new BigDecimal(push_scale);
				BigDecimal cost=FunctionUtils.mul(money, scale, 6);
				String paycode=FunctionUtils.getOrderCode("P");
				if(cost.compareTo(BigDecimal.ZERO)>0) {
					bipbService.handleCUsdtDetail(customer.getParentid(), HandleTypeEnums.push.getId(), StaticUtils.pay_in, paycode, cost, remark, customer.getId());
				}
			}
		}
	}
}
