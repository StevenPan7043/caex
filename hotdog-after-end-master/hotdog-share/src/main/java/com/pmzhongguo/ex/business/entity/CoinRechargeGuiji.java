/**
 * zzex.com Inc.
 * Copyright (c) 2019/6/21 All Rights Reserved.
 */
package com.pmzhongguo.ex.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ：yukai
 * @date ：Created in 2019/6/21 16:17
 * @description：eth充值归集表(a_recharge)
 * @version: $
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinRechargeGuiji {

    private Long id;

    private String token;

    private String decimal;

    private String currency;

    private String tx;

    private String address;

    private BigDecimal balance;

    private Integer is_in_eth;

    private Integer rawok;

    private String gastx;

    private Integer gasok;

    private String gjtx;

    private Integer gjok;

    private String currency_chain_type;

    private String gjtx_time;
}
