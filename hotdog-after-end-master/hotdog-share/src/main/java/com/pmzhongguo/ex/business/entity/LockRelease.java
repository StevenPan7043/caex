package com.pmzhongguo.ex.business.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description: 锁仓释放
 * @date: 2019-06-03 10:22
 * @author: 十一
 */
@Data
public class LockRelease
{

    private Integer id;//
    private Integer member_id;//会员ID
    private Integer lock_account_detail_id;//锁仓明细id
    private String currency;//货币类型，关联d_currency表的currency字段
    private BigDecimal release_volume;//释放数量
    private Integer is_release;//是否释放，0:未释放，1:已释放,2:划转到锁仓账号
    private String release_time;//释放时间
    private String create_time; //创建时间
    private String update_time; //修改时间

    public LockRelease()
    {
    }

    public Integer getLock_account_detail_id()
    {
        return lock_account_detail_id;
    }

    public void setLock_account_detail_id(Integer lock_account_detail_id)
    {
        this.lock_account_detail_id = lock_account_detail_id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getMember_id()
    {
        return member_id;
    }

    public void setMember_id(Integer member_id)
    {
        this.member_id = member_id;
    }

    public String getCurrency()
    {
        return currency;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public BigDecimal getRelease_volume()
    {
        return release_volume;
    }

    public void setRelease_volume(BigDecimal release_volume)
    {
        this.release_volume = release_volume;
    }

    public Integer getIs_release()
    {
        return is_release;
    }

    public void setIs_release(Integer is_release)
    {
        this.is_release = is_release;
    }

    public String getRelease_time()
    {
        return release_time;
    }

    public void setRelease_time(String release_time)
    {
        this.release_time = release_time;
    }

    public String getCreate_time()
    {
        return create_time;
    }

    public void setCreate_time(String create_time)
    {
        this.create_time = create_time;
    }

    public String getUpdate_time()
    {
        return update_time;
    }

    public void setUpdate_time(String update_time)
    {
        this.update_time = update_time;
    }
}
