/**
 * zzex.com Inc.
 * Copyright (c) 2019/5/22 All Rights Reserved.
 */
package com.pmzhongguo.zzextool.dao;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.pmzhongguo.ex.business.entity.CoinRecharge;
import com.pmzhongguo.ex.business.entity.CoinRechargeAddr;
import com.pmzhongguo.zzextool.utils.DaoUtil;
import com.pmzhongguo.zzextool.utils.JsonUtil;
import com.pmzhongguo.zzextool.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author ：yukai
 * @date ：Created in 2019/5/22 15:20
 * @description：CoinRecharge Dao
 * @version: $
 */
@Slf4j
@Component
public class CoinRechargeDao {

    @Autowired
    private DaoUtil daoUtil;

    /**
     * 根据条件查询充值记录
     *
     * @param coinRecharge
     * @return
     */
    public List<Map<String, Object>> findCoinRecharge(CoinRecharge coinRecharge) {
        List<Object> params = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM m_coin_recharge where 1=1");
        if (StringUtils.isNotEmpty(coinRecharge.getR_txid())) {
            sql.append(" AND r_txid=?");
            params.add(coinRecharge.getR_txid());
        }
        List<Map<String, Object>> coinRechargeList = daoUtil.queryForList(sql.toString(), params.toArray());
        return coinRechargeList;
    }

    /**
     * 根据条件查询充值记录
     *
     * @param address
     * @return
     */
    public Integer findCoinRechargeAddrByAddr(String address) {
        List<Object> params = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("SELECT member_id FROM m_coin_recharge_addr_tmp where 1=1");
        if (StringUtils.isNotEmpty(address)) {
            sql.append(" AND address=?");
            params.add(address);
        }
        Integer member_id = daoUtil.queryForInt(sql.toString(), params.toArray());
        return member_id;
    }


    /**
     * 根据条件更新充值记录
     *
     * @param coinRecharge
     * @return
     */
    public int updateCoinRechargeById(CoinRecharge coinRecharge) {
        if (coinRecharge.getId() == null) {
            log.warn("sql 查询条件为空，不可以全局更新。请检查传入添加是否合法: " + JSON.toJSONString(coinRecharge));
            return -1;
        }
        StringBuilder sql = new StringBuilder("update m_coin_recharge set ");
        if (StringUtils.isNotEmpty(coinRecharge.getR_confirmations())) {
            sql.append(" r_confirmations=").append(coinRecharge.getR_confirmations()).append(",");
        }
        if (coinRecharge.getR_guiji() != null) {
            sql.append(" r_guiji=").append(coinRecharge.getR_guiji()).append(",");
        }
        sql.replace(sql.length() - 1, sql.length(), "");
        sql.append(" where id=?");
        return daoUtil.update(sql.toString(), coinRecharge.getId());
    }

    /**
     * 插入充值记录表数据
     *
     * @param coinRecharge
     * @return
     */
    public int insertCoinRecharge(CoinRecharge coinRecharge) {
        Object[] objects = new Object[]{
                coinRecharge.getCurrency_chain_type(),
                coinRecharge.getMember_id(),
                coinRecharge.getCurrency_id(),
                coinRecharge.getCurrency(),
                coinRecharge.getR_amount(),
                coinRecharge.getR_create_time(),
                coinRecharge.getR_address(),
                coinRecharge.getR_txid(),
                coinRecharge.getR_confirmations(),
                coinRecharge.getR_status(),
                coinRecharge.getR_gas(),
                coinRecharge.getR_guiji(),
                coinRecharge.getR_source(),
                coinRecharge.getOtc_ads_id()};
        StringBuilder sql = new StringBuilder("insert m_coin_recharge(" +
                "currency_chain_type," +
                "member_id," +
                "currency_id," +
                "currency," +
                "r_amount," +
                "r_create_time," +
                "r_address," +
                "r_txid," +
                "r_confirmations," +
                "r_status," +
                "r_gas," +
                "r_guiji," +
                "r_source," +
                "otc_ads_id) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        return daoUtil.update(sql.toString(), objects);
    }

    public int insertCoinRechargeAddr(CoinRechargeAddr coinRechargeAddr) {
        Object[] objects = new Object[]{
                coinRechargeAddr.getCurrency(),
                coinRechargeAddr.getAddress()};
        StringBuilder sql = new StringBuilder("insert m_coin_recharge_addr_pool(" +
                "currency," +
                "address) " +
                "values(?,?)");
        return daoUtil.update(sql.toString(), objects);
    }

    /**
     * 查询未分配地址
     *
     * @param coinRechargeAddr
     * @return
     */
    public List<CoinRechargeAddr> findCoinRechargeAddrPool(CoinRechargeAddr coinRechargeAddr) {
        List<Object> params = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM m_coin_recharge_addr_pool where 1=1");
        if (StringUtils.isNotEmpty(coinRechargeAddr.getCurrency())) {
            sql.append(" AND currency=?");
            params.add(coinRechargeAddr.getCurrency());
        }
        if (StringUtils.isNotEmpty(coinRechargeAddr.getAddress())) {
            sql.append(" AND address=?");
            params.add(coinRechargeAddr.getAddress());
        }
        List<Map<String, Object>> coinRechargeAddrList = daoUtil.queryForList(sql.toString(), params.toArray());
        List<CoinRechargeAddr> result = Lists.newArrayList();
        for (Map<String, Object> map : coinRechargeAddrList) {
            CoinRechargeAddr coinRecharge1 = JsonUtil.parseMap2Object(map, CoinRechargeAddr.class);
            result.add(coinRecharge1);
        }
        return result;
    }

    /**
     * 查询未分配地址
     *
     * @param coinRechargeAddr
     * @return
     */
    public Integer findCoinRechargeAddrPoolCount(CoinRechargeAddr coinRechargeAddr) {
        List<Object> params = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("SELECT count(*) FROM m_coin_recharge_addr_pool where 1=1");
        if (StringUtils.isNotEmpty(coinRechargeAddr.getCurrency())) {
            sql.append(" AND currency=?");
            params.add(coinRechargeAddr.getCurrency());
        }
        if (StringUtils.isNotEmpty(coinRechargeAddr.getAddress())) {
            sql.append(" AND address=?");
            params.add(coinRechargeAddr.getAddress());
        }
        Integer count = daoUtil.queryForInt(sql.toString(), params.toArray());
        return count;
    }

    /**
     * 查询币种已分配的充值地址
     *
     * @param coinRechargeAddr
     * @return
     */
    public List<Map<String, Object>> findCoinRechargeAddr(CoinRechargeAddr coinRechargeAddr) {
        List<Object> params = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM m_coin_recharge_addr where 1=1");
        if (StringUtils.isNotEmpty(coinRechargeAddr.getCurrency())) {
            sql.append(" AND currency=?");
            params.add(coinRechargeAddr.getCurrency());
        }
        if (StringUtils.isNotEmpty(coinRechargeAddr.getAddress())) {
            sql.append(" AND address=?");
            params.add(coinRechargeAddr.getAddress());
        }
        List<Map<String, Object>> coinRechargeAddrList = daoUtil.queryForList(sql.toString(), params.toArray());
        return coinRechargeAddrList;
    }

    /**
     * 查询币种已分配的充值地址集合
     *
     * @param currency
     * @return
     */
    public List<String> findCoinRechargeAddrList(String currency) {
        List<Object> params = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("SELECT address FROM m_coin_recharge_addr where 1=1");
        if (StringUtils.isNotEmpty(currency)) {
            sql.append(" AND currency=?");
            params.add(currency);
        }
        List<String> coinRechargeAddrList = daoUtil.queryForList(sql.toString(), params.toArray());
        return coinRechargeAddrList;
    }

    /**
     * 根据条件查询充值记录
     *
     * @param coinRecharge
     * @return
     */
    public List<CoinRecharge> findCoinRechargeSelect(CoinRecharge coinRecharge) {
        List<Object> params = Lists.newArrayList();
        StringBuilder sql = new StringBuilder("SELECT * FROM m_coin_recharge where 1=1");
        if (coinRecharge.getCurrency_id() != null) {
            sql.append(" AND currency_id=?");
            params.add(coinRecharge.getCurrency_id());
        }
        if (coinRecharge.getR_guiji() != null) {
            sql.append(" AND r_guiji=?");
            params.add(coinRecharge.getR_guiji());
        }
        if (StringUtils.isNotEmpty(coinRecharge.getR_address())) {
            sql.append(" AND r_address <> ?");
            params.add(coinRecharge.getR_address());
        }
        if (StringUtils.isNotEmpty(coinRecharge.getR_confirmations())) {
            sql.append(" AND r_confirmations > ").append(coinRecharge.getR_confirmations());
        }
        if (coinRecharge.getId() != null) {
            sql.append(" AND id > ").append(coinRecharge.getId());
        }
        if (!StringUtil.isNullOrBank(coinRecharge.getCurrency_chain_type())) {
            sql.append(" AND currency_chain_type=?");
            params.add(coinRecharge.getCurrency_chain_type());
        }
        sql.append(" order by id asc");
        List<Map<String, Object>> coinRechargeList = daoUtil.queryForList(sql.toString(), params.toArray());
        List<CoinRecharge> result = Lists.newArrayList();
        for (Map<String, Object> map : coinRechargeList) {
            CoinRecharge coinRecharge1 = JsonUtil.parseMap2Object(map, CoinRecharge.class);
            result.add(coinRecharge1);
        }
        return result;
    }

    /**
     * 根据地址查询对应的用户信息
     *
     * @param addressList
     * @return
     */
    public List<Map<String, Object>> findMemberAsset(List<String> addressList) {
        StringBuilder strAddr = new StringBuilder();
        for (String addr : addressList) {
            strAddr.append("'" + addr + "',");
        }
        strAddr.replace(strAddr.length() - 1, strAddr.length(), "");
        StringBuilder sql = new StringBuilder("select b.m_name as m_name, a.address as address from m_coin_recharge_addr_tmp a join m_member b on a.member_id=b.id where address in (" + strAddr + ")");
        List<Map<String, Object>> coinRechargeList = daoUtil.queryForList(sql.toString());
        return coinRechargeList;
    }

    /**
     * 批量更新充值记录为 1
     *
     * @param coinRecharges
     * @return
     */
    public int batchUpdateRecharge(List<CoinRecharge> coinRecharges) {
        StringBuilder sql = new StringBuilder("update m_coin_recharge set r_guiji=1 ");
        StringBuilder strAddr = new StringBuilder();
        for (CoinRecharge coinRecharge : coinRecharges) {
            strAddr.append(coinRecharge.getId() + ",");
        }
        if (strAddr.length() <= 0) {
            log.warn("sql 查询条件为空，不可以全局更新。请检查传入添加是否合法: " + JSON.toJSONString(coinRecharges));
            return -1;
        }
        strAddr.replace(strAddr.length() - 1, strAddr.length(), "");
        sql.append(" where id in (" + strAddr + ")");
        int row = daoUtil.update(sql.toString());
        if (row > 0) {
            log.info("更新充值记录的guiji状态为1, 更新成功。 充值记录ID为: id=" + strAddr);
        } else {
            log.error("更新充值记录的guiji状态为1, 更新失败!");
        }
        return row;
    }

}
