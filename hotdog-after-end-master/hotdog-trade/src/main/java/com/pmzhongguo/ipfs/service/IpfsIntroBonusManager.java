package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ipfs.constant.CONSTANT;
import com.pmzhongguo.ipfs.entity.IpfsIntroBonus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Daily
 * @date 2020/7/15 18:09
 */
@Service
public class IpfsIntroBonusManager {

    ExecutorService pool = Executors.newFixedThreadPool(2);

    @Autowired
    private IpfsIntroBonusService ipfsIntroBonusService;

    @Autowired
    private DaoUtil daoUtil;

    @Autowired
    private MemberService memberService;

    /**
     * 根据购买算力ID给推荐人奖励
     *
     * @param hashrateId
     */
    @Transactional
    public void reward(int memberId, int IntroId, int hashrateId, int projectId) {
        String projectQuery = "SELECT t.`quote_currency`, t.`code`, t.`intro_bonus` FROM `ipfs_project` t WHERE t.id = ?;";
        Map<String, Object> projectMap = daoUtil.queryForMap(projectQuery, projectId);
        String hashrateQuery = "SELECT t.`total` FROM `ipfs_hashrate` t WHERE t.`id` = ?;";
        Map<String, Object> hashrateMap = daoUtil.queryForMap(hashrateQuery, hashrateId);
        String currency = String.valueOf(projectMap.get("quote_currency"));
        String projectcode = String.valueOf(projectMap.get("code"));
        BigDecimal introBonus = new BigDecimal(String.valueOf(projectMap.get("intro_bonus")));
        BigDecimal total = new BigDecimal(String.valueOf(hashrateMap.get("total")));
        BigDecimal bonusNum = total.multiply(introBonus);

        //如果介绍人自己购买的算力达到3000U就全额奖励
        Map<String, Object> buyHashrate = daoUtil.queryForMap("SELECT SUM(t.`total`) total FROM `ipfs_hashrate` t WHERE t.`member_id` = ?; ", IntroId);
        if (buyHashrate != null && buyHashrate.get("total") != null) {
            BigDecimal introHashrate = new BigDecimal(String.valueOf(buyHashrate.get("total")));
            if (introHashrate.compareTo(CONSTANT.INTRODUCER_HASHRATE) < 0) {
//                bonusNum = bonusNum.multiply(new BigDecimal(0.5));
                return;
            }
        } else {
            return;
        }


        //插入推荐分红记录
        IpfsIntroBonus record = new IpfsIntroBonus();
        record.setProjectId(projectId);
        record.setProjectCode(projectcode);
        record.setHashrateId(hashrateId);
        record.setMemberId(memberId);
        record.setIntroId(IntroId);
        record.setBonusCurrency(currency);
        record.setBonusNum(bonusNum);
        insert(record);

        //增加推荐人资产
        memberService.accountProc(bonusNum, currency, IntroId, 3, OptSourceEnum.IPFS);
    }

    /**
     * 插入一条推荐人分红记录
     *
     * @param record
     * @return
     */
    public int insert(IpfsIntroBonus record) {
        return ipfsIntroBonusService.insert(record);
    }


    public List<IpfsIntroBonus> findByConditionPage(Map<String, Object> param) {
        return ipfsIntroBonusService.findByConditionPage(param);
    }

    ;

    /**
     * 获取推荐奖励
     *
     * @param memberId
     * @return
     */
    public BigDecimal getTotal(int memberId) {
        String query = "SELECT SUM(t.`bonus_num`) total FROM `ipfs_intro_bonus` t WHERE t.`member_id` = ?;";
        Map<String, Object> map = daoUtil.queryForMap(query, memberId);
        if (map.get("total") == null) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(String.valueOf(map.get("total")));
    }
}
