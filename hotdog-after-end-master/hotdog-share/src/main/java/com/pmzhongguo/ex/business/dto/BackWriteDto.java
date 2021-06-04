package com.pmzhongguo.ex.business.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @description: 币种回写所需要的参数
 * @date: 2019-08-20 19:56
 * @author: 十一
 */
@Data
@Builder
@ToString
public class BackWriteDto implements Serializable {

    private static final long serialVersionUID = -6487774644387318825L;
    /**
     * 块高
     */
    private Long blockHeight;

    /**
     * 交易hash
     */
    private String hash;

    /**
     * 回写最小数量
     */
    private BigDecimal minBackWriteAmount;

    /**
     * 回写起始页
     */
    private Integer page;

    /**
     * 每页数量
     */
    private Integer pageSize;

    /**
     * 币种名称
     */
    private String currencyName;

    /**
     * 排序方式 1, DESC 2, ASC
     */
    private String sort;

}
