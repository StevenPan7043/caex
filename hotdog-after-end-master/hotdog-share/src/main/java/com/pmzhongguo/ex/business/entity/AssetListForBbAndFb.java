package com.pmzhongguo.ex.business.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author jary
 * @creatTime 2019/4/11 11:35 AM
 */
public class AssetListForBbAndFb implements Serializable, Comparable<AssetListForBbAndFb>
{
    private static final long serialVersionUID = -7145856619711866168L;

    /**
     * 币种名称
     */
    private String name;

    private double enable;

    private double frozen;

    /**
     * 总数折合率
     */
    private double equal;
    /**
     * 排序
     */
    private Integer c_order;

    /**
     * 锁仓数量
     */
    private BigDecimal lock_num;

    /**
     * 锁仓待释放
     */
    private BigDecimal wait_release_num;

    public BigDecimal getWait_release_num()
    {
        return wait_release_num;
    }

    public void setWait_release_num(BigDecimal wait_release_num)
    {
        this.wait_release_num = wait_release_num;
    }

    public AssetListForBbAndFb()
    {
    }

    public AssetListForBbAndFb(String name, double enable, double frozen, double equal, Integer c_order, BigDecimal lock_num, BigDecimal wait_release_num)
    {
        this.name = name;
        this.enable = enable;
        this.frozen = frozen;
        this.equal = equal;
        this.c_order = c_order;
        this.lock_num = lock_num;
        this.wait_release_num = wait_release_num;
    }

    public AssetListForBbAndFb(String name, double enable, double frozen, double equal)
    {
        this.name = name;
        this.enable = enable;
        this.frozen = frozen;
        this.equal = equal;
    }


    public Integer getC_order()
    {
        return c_order;
    }

    public void setC_order(Integer c_order)
    {
        this.c_order = c_order;
    }

    public static long getSerialVersionUID()
    {
        return serialVersionUID;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public double getEnable()
    {
        return enable;
    }

    public void setEnable(double enable)
    {
        this.enable = enable;
    }

    public double getFrozen()
    {
        return frozen;
    }

    public void setFrozen(double frozen)
    {
        this.frozen = frozen;
    }

    public double getEqual()
    {
        return equal;
    }

    public void setEqual(double equal)
    {
        this.equal = equal;
    }

    public BigDecimal getLock_num()
    {
        return lock_num;
    }

    public void setLock_num(BigDecimal lock_num)
    {
        this.lock_num = lock_num;
    }

    @Override
    public int compareTo(AssetListForBbAndFb o)
    {
        if (this.c_order > o.c_order)
        {
            return 1;
        } else if (this.c_order < o.c_order)
        {
            return -1;
        } else
        {
            return 0;
        }
    }
}
