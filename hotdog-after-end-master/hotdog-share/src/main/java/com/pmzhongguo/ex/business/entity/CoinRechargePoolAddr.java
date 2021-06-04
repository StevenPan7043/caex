package com.pmzhongguo.ex.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地址池
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinRechargePoolAddr {
    /**
     * d_currency的currency
     */
    private Integer id;
    private String currency;
    private String address;
    private String private_key;
}
