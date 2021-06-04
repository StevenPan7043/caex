package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ipfs.constant.CONSTANT;
import com.pmzhongguo.ipfs.entity.IpfsHashrate;
import com.pmzhongguo.ipfs.entity.IpfsProject;
import com.pmzhongguo.ipfs.entity.IpfsUserBonus;
import com.pmzhongguo.ipfs.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IpfsManager {

    @Autowired
    private IpfsHashrateService ipfsHashrateService;
    @Autowired
    private IpfsProjectService ipfsProjectService;
    @Autowired
    private IpfsUserBonusService ipfsUserBonusService;
    @Autowired
    private IpfsOutputService ipfsOutputService;
    @Autowired
    private DaoUtil daoUtil;


    public Map<String, Object> getIpfsInfo(Integer memberId) {
        String qureyone = "SELECT id,member_id,currency,total_balance,frozen_balance FROM `m_account` WHERE member_id = ? AND currency='FIL';";
        Map<String, Object> accountMap = daoUtil.queryForMap(qureyone, memberId);
        Map<String, Object> params = new HashMap<>();
        params.put("memberId", memberId);
        params.put("status", "1");
        List<IpfsHashrate> hashrateList = ipfsHashrateService.findHashrate(params);
        BigDecimal all_rate = BigDecimal.ZERO;//总收益
        BigDecimal kj_rate = BigDecimal.ZERO;//矿迟总收益
        BigDecimal sl_rate = BigDecimal.ZERO;//算力总收益
        BigDecimal hash_rate = BigDecimal.ZERO;//总算力
        BigDecimal lock_num = BigDecimal.ZERO;//封存量
        BigDecimal allTate = BigDecimal.ZERO;//昨天每T分红
        Map<String, Object> result = new HashMap<>();
        if (hashrateList.isEmpty() || accountMap.isEmpty()) {
            result.put("all_rate", all_rate);
            result.put("kj_rate", kj_rate);
            result.put("sl_rate", sl_rate);
            result.put("hash_rate", hash_rate);
            result.put("lock_num", lock_num);
            result.put("allTate", allTate);
            return result;
        }
        Object total_balance = accountMap.get("total_balance");
        Object frozen_balance = accountMap.get("frozen_balance");
        //项目ID
        List<Integer> projectList = hashrateList.stream().map(IpfsHashrate::getProjectId).collect(Collectors.toList());
        params.put("idList", projectList);
        List<IpfsProject> ipfsProjectMap = ipfsProjectService.findIpfsProject(params);
        //获取算力
        Map<Integer, BigDecimal> collect = new HashMap<>();
        for (IpfsProject project : ipfsProjectMap) {
            collect.put(project.getId(), project.getExchangeRate());
        }
        Date date = DateUtil.dateAddInt(new Date(), -1);
        String yesterday = DateUtil.dateToSimpleStr(date, DateUtil.YYYY_MM_DD);
        params.put("outputDate", yesterday);
        params.put("projectIdList", projectList);
        params.put("startTime", yesterday);
        //每个项目的总收益
        BigDecimal allBonus = ipfsUserBonusService.sumBonus(params);
        //昨天分红
        allTate = ipfsOutputService.queryCapacityDay(params);
        //今天之前的项目的总算力
        BigDecimal all_sl = BigDecimal.ZERO;
        for (IpfsHashrate rate : hashrateList) {
            hash_rate = hash_rate.add(collect.get(rate.getProjectId()).multiply(BigDecimal.valueOf(rate.getNum())));
            //27号为第一天分红，之前购买的用户以此为起始时间
            Date startDate = DateUtil.stringToDate("2020-10-27", DateUtil.YYYY_MM_DD);
            int day = DateUtil.getIntervalDays(startDate, rate.getCreateTime());
            if (day > 0) {
                rate.setCreateTime(startDate);
            }
            int intervalDays = DateUtil.getIntervalDays(date, rate.getCreateTime()) + 1;
            if (intervalDays > 0) {
                all_sl = all_sl.add(collect.get(rate.getProjectId()));
                if (rate.getType().equals("1")) {
                    if (BigDecimal.valueOf(intervalDays).compareTo(CONSTANT.LOCK_DAY) < 0) {
                        lock_num = lock_num.add(new BigDecimal(intervalDays * rate.getNum()).divide(CONSTANT.LOCK_DAY, 6, BigDecimal.ROUND_HALF_UP));
                    } else {
                        lock_num = lock_num.add(new BigDecimal(intervalDays * rate.getNum()));
                    }
                } else {
                    if (BigDecimal.valueOf(intervalDays).compareTo(CONSTANT.KJ_LOCK_DAY) < 0) {
                        lock_num = lock_num.add(new BigDecimal(intervalDays * rate.getNum()).multiply(collect.get(rate.getProjectId())).divide(CONSTANT.KJ_LOCK_DAY, 6, BigDecimal.ROUND_HALF_UP));
                    } else {
                        lock_num = lock_num.add(new BigDecimal(intervalDays * rate.getNum()).multiply(collect.get(rate.getProjectId())));
                    }
                }
            }
        }
        String query = "SELECT tt2.member_id, IFNULL(tt1.team_bonus, 0) team_bonus, IFNULL(tt2.totalBonus, 0) totalBonus FROM \n" +
                "(SELECT t1.`member_id`, t1.`team_bonus` FROM `ipfs_team_bonus` t1 \n" +
                "WHERE t1.`member_id` = ? AND t1.`bonus_date` = ?) tt1\n" +
                "RIGHT JOIN \n" +
                "(SELECT t2.`member_id`, SUM(t2.`team_bonus`) totalBonus FROM `ipfs_team_bonus` t2\n" +
                "WHERE t2.`member_id` = ? GROUP BY t2.member_id) tt2\n" +
                "ON tt1.member_id = tt2.member_id";
        Map<String, Object> map = daoUtil.queryForMap(query, memberId, yesterday, memberId);
        if (map != null && !map.isEmpty()) {
            result.put("kj_rate", new BigDecimal(map.get("team_bonus").toString()));
        } else {
            result.put("kj_rate", kj_rate);
        }
        result.put("all_rate", new BigDecimal(total_balance.toString()).subtract(new BigDecimal(frozen_balance.toString())));
        result.put("sl_rate", allBonus);
        result.put("hash_rate", hash_rate);
        result.put("lock_num", lock_num);
        result.put("allTate", allTate.divide(all_sl, 6, BigDecimal.ROUND_HALF_UP));
        return result;
    }
}
