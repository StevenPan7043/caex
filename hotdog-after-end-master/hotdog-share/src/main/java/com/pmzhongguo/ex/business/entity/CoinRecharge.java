package com.pmzhongguo.ex.business.entity;

import com.pmzhongguo.ex.core.web.req.BaseSecretReq;
import lombok.*;

import java.math.BigDecimal;

/**
 * 充值记录表
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CoinRecharge extends BaseSecretReq {
    private Long id;//订单ID
    private Integer member_id;//会员ID
    private Integer currency_id;//d_currency的id，CNY不会出现在此
    private String currency;//d_currency的currency
    private BigDecimal r_amount;//充值数量
    private String r_create_time;//充值时间
    private String r_address;//充值地址（会员在平台的唯一地址）
    private String r_txid;//交易ID
    private String r_confirmations; //确认数
    private Integer r_status;//状态：-1待付款[OTC]，0未确认，1已确认，2已取消,3块高待确认
    private String r_from_address; //转入地址（针对无法对接钱包的币使用）
    private String r_certify_image; //转账证明图片
    private String reject_reason; //拒绝原因
    private String auditor; //处理人账号，管理frm_user表
    private String audit_time; //处理时间
    private String r_source; //来源

    private Integer otc_ads_id; //OTC广告ID
    private String otc_oppsite_currency; //OTC对方币种
    private BigDecimal otc_price;//OTC价格
    private BigDecimal otc_money;//OTC应收金额
    private String otc_owner_name;//OTC对方姓名
    private String currency_chain_type;//币种链类型

    private String token; //token

    private String r_address_; //手动录入地址

    // 附加字段
    private String m_name;// 充值人
    private Integer is_frozen; //是否冻结
    private String unfrozen_time; //解冻时间
    private Integer r_gas;    //是否转手续费
    private Integer r_guiji;    //是否归集


}
