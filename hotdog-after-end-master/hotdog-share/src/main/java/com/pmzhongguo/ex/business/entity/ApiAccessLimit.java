package com.pmzhongguo.ex.business.entity;

import com.google.common.cache.LoadingCache;

public class ApiAccessLimit
{
    private String interface_key; //接口key
    private Integer limit_count; //限制次数
    private LoadingCache<String, Integer> cache; //缓存

    public String getInterface_key()
    {
        return interface_key;
    }

    public void setInterface_key(String interface_key)
    {
        this.interface_key = interface_key;
    }

    public Integer getLimit_count()
    {
        return limit_count;
    }

    public void setLimit_count(Integer limit_count)
    {
        this.limit_count = limit_count;
    }

    public LoadingCache<String, Integer> getCache()
    {
        return cache;
    }

    public void setCache(LoadingCache<String, Integer> cache)
    {
        this.cache = cache;
    }

}
