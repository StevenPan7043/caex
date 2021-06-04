package com.pmzhongguo.ex.business.entity;

import java.util.List;

/**
 * 设置交易返还手续费
 *
 * @author zengweixiong
 */
public class SetTradeReturnFeeList
{

    private List<SetTradeReturnFee> lists;

    public List<SetTradeReturnFee> getLists()
    {
        return lists;
    }

    public void setLists(List<SetTradeReturnFee> lists)
    {
        this.lists = lists;
    }

    @Override
    public String toString()
    {
        return "SetTradeReturnFeeList [lists=" + lists + "]";
    }

}
