package com.pmzhongguo.ex.business.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 锁仓释放币dto
 * @date: 2019-06-12 14:49
 * @author: 十一
 */
@Data
public class CurrencyLockReleaseDto {

    private Integer memberId;

    private Integer id;

    private BigDecimal releaseVolume;

    private String currency;

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public BigDecimal getReleaseVolume() {
        return releaseVolume;
    }

    public void setReleaseVolume(BigDecimal releaseVolume) {
        this.releaseVolume = releaseVolume;
    }

    public String getCurrency() {
        return currency;
    }
}
