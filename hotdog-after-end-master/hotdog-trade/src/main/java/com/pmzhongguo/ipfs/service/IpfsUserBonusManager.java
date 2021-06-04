package com.pmzhongguo.ipfs.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ipfs.entity.IpfsHashrate;
import com.pmzhongguo.ipfs.entity.IpfsOutput;
import com.pmzhongguo.ipfs.entity.IpfsProject;
import com.pmzhongguo.ipfs.entity.IpfsUserBonus;
import com.pmzhongguo.ipfs.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Daily
 * @date 2020/7/14 13:47
 */
@Service
public class IpfsUserBonusManager {
    private static Logger logger = LoggerFactory.getLogger(IpfsUserBonusManager.class);

    @Autowired
    private DaoUtil daoUtil;

    @Autowired
    private IpfsUserBonusService ipfsUserBonusService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private IpfsHashrateService ipfsHashrateService;

    /**
     * 根据条件查询用户奖励记录
     * 如果要分页请用 findByConditionPage
     *
     * @param param
     * @return
     */
    public List<IpfsUserBonus> findByCondition(Map<String, Object> param) {
        return ipfsUserBonusService.findByCondition(param);
    }

    ;

    public List<IpfsUserBonus> findByConditionPage(Map<String, Object> param) {
        return ipfsUserBonusService.findByConditionPage(param);
    }

    ;

    /**
     * 插入一条用户分红记录
     *
     * @param record
     * @return
     */
    public int insert(IpfsUserBonus record) {
        return ipfsUserBonusService.insert(record);
    }

    /**
     * 创建该项目的所有用户分红
     *
     * @param projectId
     * @param bonusDate
     * @return
     */
    public List<IpfsUserBonus> create(Integer projectId, String bonusDate) {
        String query = "SELECT m.`capacity` capacity FROM `ipfs_output` m WHERE m.`project_id` = ? AND m.`output_date` = ?;";
        Map<String, BigDecimal> map = daoUtil.queryForMap(query, projectId, bonusDate);
        return create(projectId, bonusDate, map.get("capacity"));
    }

    /**
     * 创建该项目的所有用户分红
     *
     * @param projectId
     * @param bonusDate
     * @param capacity
     * @return
     */
    public List<IpfsUserBonus> create(int projectId, String bonusDate, BigDecimal capacity) {
        String qureyProjectd = "SELECT fee FROM `ipfs_project` t WHERE t.id = ? ;";
        Map<String, Object> ProjectdMap = daoUtil.queryForMap(qureyProjectd, projectId);
        String strFee = String.valueOf(ProjectdMap.get("fee"));
        BigDecimal fee = StringUtils.isBlank(strFee) ? BigDecimal.ZERO : new BigDecimal(strFee);

        String query = "SELECT t.id, t.`project_code`, t.`member_id`, t.`output_currency`, t.num  FROM `ipfs_hashrate` t WHERE t.`status` = 1 AND t.`project_id` = ?;";
        List<Map<String, Object>> list = daoUtil.queryForList(query, projectId);
        List<IpfsUserBonus> bonuses = new ArrayList<>();
        for (Map<String, Object> map : list) {
            try {
                IpfsUserBonus ipfsUserBonus = new IpfsUserBonus();
                ipfsUserBonus.setHashrateId(Integer.valueOf(String.valueOf(map.get("id"))));
                ipfsUserBonus.setProjectId(projectId);
                ipfsUserBonus.setProjectCode(String.valueOf(map.get("project_code")));
                ipfsUserBonus.setMemberId(Integer.valueOf(String.valueOf(map.get("member_id"))));
                ipfsUserBonus.setOutputCurrency(String.valueOf(map.get("output_currency")));
                ipfsUserBonus.setBonusDate(bonusDate);
                BigDecimal num = new BigDecimal(String.valueOf(map.get("num")));
                BigDecimal bonusNum = num.multiply(capacity);
                ipfsUserBonus.setFee(bonusNum.multiply(fee));
                ipfsUserBonus.setBonusNum(bonusNum.subtract(ipfsUserBonus.getFee()));
                reward(ipfsUserBonus);
            } catch (NumberFormatException e) {
                logger.warn(map.toString() + "\n" + e.getStackTrace());
            }
        }
        return bonuses;
    }

    /**
     * 创建该项目的所有用户分红
     *
     * @param projectCapacityMap 产出总释放量
     * @param feeMap             手续费
     * @param projectIdList      项目ID
     * @param bonusDate
     * @return
     */
    public void create(Map<Integer, Map<String, BigDecimal>> projectCapacityMap, Map<Integer, BigDecimal> feeMap, List<Integer> projectIdList, String bonusDate) {
        Map<String, Object> hashrateParam = new HashMap<>();
        hashrateParam.put("projectIdList", projectIdList);
        hashrateParam.put("status", "1");
        List<IpfsHashrate> hashrateList = ipfsHashrateService.findHashrate(hashrateParam);
        Map<Integer, IpfsHashrate> hashMap = new HashMap<>();
        Map<Integer, BigDecimal> perBonusMap = new HashMap<>();//每个用户的总分红
        for (IpfsHashrate rate : hashrateList) {
            hashMap.put(rate.getId(), rate);
            Map<String, BigDecimal> outMap = projectCapacityMap.get(rate.getProjectId());
            for (String key : outMap.keySet()) {
                //相差天数
                if (outMap.get(key) != null && outMap.get(key).compareTo(BigDecimal.ZERO) > 0) {
                    int intervalDays = DateUtil.getIntervalDays(rate.getCreateTime(), DateUtil.stringToDate(key, DateUtil.YYYY_MM_DD));
                    if (intervalDays <= 0) {
                        BigDecimal num = new BigDecimal(rate.getNum());
                        if (perBonusMap.get(rate.getId()) == null) {
                            perBonusMap.put(rate.getId(), outMap.get(key).multiply(num));
                        } else {
                            perBonusMap.put(rate.getId(), perBonusMap.get(rate.getId()).add(outMap.get(key).multiply(num)));
                        }
                    }
                }
            }
        }
        for (Integer key : perBonusMap.keySet()) {
            try {
                IpfsUserBonus ipfsUserBonus = new IpfsUserBonus();
                ipfsUserBonus.setHashrateId(hashMap.get(key).getId());
                ipfsUserBonus.setProjectId(hashMap.get(key).getProjectId());
                ipfsUserBonus.setProjectCode(hashMap.get(key).getProjectCode());
                ipfsUserBonus.setMemberId(hashMap.get(key).getMemberId());
                ipfsUserBonus.setOutputCurrency(hashMap.get(key).getOutputCurrency());
                ipfsUserBonus.setBonusDate(bonusDate);
                BigDecimal num = new BigDecimal(hashMap.get(key).getNum());
                //释放量
                BigDecimal bonusNum = perBonusMap.get(key);
                //手续费比例
                BigDecimal fee = feeMap.get(hashMap.get(key).getProjectId());
                ipfsUserBonus.setFee(bonusNum.multiply(fee).setScale(6, BigDecimal.ROUND_UP));
                ipfsUserBonus.setBonusNum(bonusNum.subtract(ipfsUserBonus.getFee()));
                reward(ipfsUserBonus);
            } catch (NumberFormatException e) {
                logger.warn(String.format("用户分红异常,购买算力表ID:%s,分红:%s,手续费:%s",
                        hashMap.get(key).getId().toString(),
                        perBonusMap.get(key).toPlainString(),
                        feeMap.get(hashMap.get(key).getProjectId()).toPlainString()));
                e.getStackTrace();
            }
        }
    }

    /**
     * 插入分红记录并添加用户资产
     *
     * @param ipfsUserBonus
     */
    @Transactional
    public void reward(IpfsUserBonus ipfsUserBonus) {
        List<IpfsUserBonus> queryList = findByCondition(HelpUtils.newHashMap("hashrateId", ipfsUserBonus.getHashrateId(), "bonusDate", ipfsUserBonus.getBonusDate()));
        if (CollectionUtils.isNotEmpty(queryList)) {
            return;
        }
        insert(ipfsUserBonus);
        memberService.accountProc(ipfsUserBonus.getBonusNum(), ipfsUserBonus.getOutputCurrency(), ipfsUserBonus.getMemberId(), 3, OptSourceEnum.IPFS);
//        memberService.accountProc(ipfsUserBonus.getBonusNum(), ipfsUserBonus.getOutputCurrency(), ipfsUserBonus.getMemberId(), 1, OptSourceEnum.IPFS);
    }
}
