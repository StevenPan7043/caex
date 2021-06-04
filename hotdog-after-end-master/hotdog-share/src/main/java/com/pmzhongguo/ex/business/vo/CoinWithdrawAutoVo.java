package com.pmzhongguo.ex.business.vo;

import com.pmzhongguo.ex.core.web.req.BaseSecretReq;
import lombok.*;

import java.math.BigDecimal;

/**
 * 提现
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CoinWithdrawAutoVo extends BaseSecretReq {
    private Long id;//订单ID
    private Integer member_id;//会员ID
    private Integer currency_id;//d_currency的id，CNY不会出现在此
    private String currency;//d_currency的currency
    private BigDecimal w_amount;//提现数量
    private BigDecimal w_fee;//手续费
    private String w_create_time;//订单创建时间
    private Integer member_coin_addr_id;//会员的币地址ID
    private String member_coin_addr;//会员的币地址
    private Integer w_status;//状态：0：待处理，1：已完成，2：已取消
    private String reject_reason;//拒绝原因
    private String w_txid;//交易ID
    private String w_from_addr; //转出地址（从哪个地址给他转的，目前主要用于玩客币）
    private String auditor;//处理人账号，管理frm_user表
    private String audit_time;//处理时间
    private String sign;//md5



}
