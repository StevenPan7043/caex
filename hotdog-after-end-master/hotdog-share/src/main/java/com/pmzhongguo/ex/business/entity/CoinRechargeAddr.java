package com.pmzhongguo.ex.business.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 充币地址
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinRechargeAddr {
    /**
     * d_currency的currency
     */
    private String currency;
    private String address;
    private Integer member_id;
    private String token;
    private String private_key;
    private String create_time;
    private String currency_chain_type;


}
