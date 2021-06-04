package com.pmzhongguo.ipfs.service;

import com.pmzhongguo.ex.business.service.MemberService;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.web.OptSourceEnum;
import com.pmzhongguo.ipfs.constant.CONSTANT;
import com.pmzhongguo.ipfs.entity.IpfsTeamBonus;
import com.pmzhongguo.ipfs.entity.IpfsTeamBonusDetail;
import com.pmzhongguo.ipfs.team.entity.TeamNode;
import com.pmzhongguo.ipfs.team.ipfsenum.LevelEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/23 14:15
 */
@Service
public class IpfsTeamBonusManager {

    @Autowired
    private IpfsTeamBonusService ipfsTeamBonusService;

    @Autowired
    private IpfsTeamBonusDetailManager ipfsTeamBonusDetailManager;

    @Autowired
    private MemberService memberService;

    @Autowired
    private DaoUtil daoUtil;

    public int insert(IpfsTeamBonus record){
        return ipfsTeamBonusService.insert(record);
    }

    /**
     * 根据条件查询记录
     * 如果需要分页用 findByConditionPage(Map<String, Object> param);
     * @param param
     * @return
     */
    public List<IpfsTeamBonus> findByCondition(Map<String, Object> param){
        return ipfsTeamBonusService.findByCondition(param);
    };

    public List<IpfsTeamBonus> findByConditionPage(Map<String, Object> param){
        return ipfsTeamBonusService.findByConditionPage(param);
    };

    /**
     * 团队奖励
     * @param node 需要发放团队奖励的节点
     * @param bonusDate 奖励日期
     */
    @Transactional
    public void reward(TeamNode node, String bonusDate){
        BigDecimal saveSubBonusBase = new BigDecimal(node.getSubBonusNum().toPlainString());
        List<IpfsTeamBonusDetail> details = new ArrayList<>();
        //先算出从伞下团队长获取的奖励
        List<LevelEnum> levelList = node.getLevel().subTeamLeadList();
        StringBuilder sb = new StringBuilder();
        //处理每个等级的子集合
        for(LevelEnum levelEnum : levelList){
            List<TeamNode> subNodeList = node.getLeaderChildren().get(levelEnum.getKey());
            if(subNodeList != null && subNodeList.size() > 0){
                sb.append(levelEnum.getDescription() + ":");
                BigDecimal childrenTotal = BigDecimal.ZERO;
                //处理子集合
                for (TeamNode subNode : subNodeList){
                    childrenTotal = childrenTotal.add(subNode.getBonusNum()).add(subNode.getSubBonusNum());
                    IpfsTeamBonusDetail detail = new IpfsTeamBonusDetail();
                    detail.setSonMemeberId(subNode.getMemberId());
                    detail.setSonLevel(subNode.getLevel().getKey());
                    details.add(detail);
                }
                sb.append(childrenTotal.toPlainString() + "*");
                BigDecimal rate = node.getLevel().getBonusRate().subtract(levelEnum.getBonusRate());
                //荣耀才有同级奖
                if(levelEnum == node.getLevel() && levelEnum == LevelEnum.HONOUR){
                    rate = levelEnum.getSameLevelBonusRate();
                }
                sb.append(rate.toPlainString());
                BigDecimal childrenTeamBonus = childrenTotal.multiply(rate).divide(CONSTANT.BIGDECIMAL_ONE_HUNDRED);
                //团队奖励增加
                node.setTeamBonus(node.getTeamBonus().add(childrenTeamBonus));
                //奖励已增加，那伞下分红就要减掉
                node.setSubBonusNum(node.getSubBonusNum().subtract(childrenTotal));
            }
            sb.append("|");
        }
        //余额算出从普通矿工获取的奖励
        sb.append(LevelEnum.GENERAL.getDescription() + ":" + node.getSubBonusNum() + "*" + node.getLevel().getBonusRate());
        BigDecimal childrenTeamBonus = node.getSubBonusNum().multiply(node.getLevel().getBonusRate()).divide(CONSTANT.BIGDECIMAL_ONE_HUNDRED);
        //生成一条团队奖励记录
        node.setTeamBonus(node.getTeamBonus().add(childrenTeamBonus));
        IpfsTeamBonus ipfsTeamBonus = new IpfsTeamBonus();
        ipfsTeamBonus.setMemberId(node.getMemberId());
        ipfsTeamBonus.setLevel(node.getLevel().getKey());
        ipfsTeamBonus.setTeamBonus(node.getTeamBonus());
        ipfsTeamBonus.setBonusRate(node.getLevel().getBonusRate());
        ipfsTeamBonus.setSubBonusBase(saveSubBonusBase);
        ipfsTeamBonus.setBonusDate(bonusDate);
        ipfsTeamBonus.setMemo(sb.toString());
        ipfsTeamBonus.setSubHashrateBase(node.getSubHashrate());
        int id = insert(ipfsTeamBonus);
        //添加资产
        memberService.accountProc(node.getTeamBonus(), "FIL", node.getMemberId(), 3, OptSourceEnum.IPFS);
        memberService.accountProc(node.getTeamBonus(), "FIL", node.getMemberId(), 1, OptSourceEnum.IPFS);

        //添加团队奖励明细记录
        for(IpfsTeamBonusDetail detail : details){
            detail.setTeamBonusId(Integer.valueOf(id));
            ipfsTeamBonusDetailManager.insert(detail);
        }
        List<TeamNode> subNodeList = node.getLeaderChildren().get(LevelEnum.GENERAL.getKey());
        if(subNodeList != null){
            for(TeamNode subNode : subNodeList){
                IpfsTeamBonusDetail detail = new IpfsTeamBonusDetail();
                detail.setSonMemeberId(subNode.getMemberId());
                detail.setSonLevel(subNode.getLevel().getKey());
                detail.setTeamBonusId(Integer.valueOf(id));
                ipfsTeamBonusDetailManager.insert(detail);
            }
        }
    }

    /**
     * 获取用户团队总收益
     * @param memberId
     * @return
     */
    public Map<String, Object> getTeamTatalBonus(int memberId){
        Date date = HelpUtils.dateAddInt(new Date(), -1);
        String bonusDate = HelpUtils.dateToSimpleStr(date);
        String query = "SELECT tt2.member_id, IFNULL(tt1.team_bonus, 0) team_bonus, IFNULL(tt2.totalBonus, 0) totalBonus FROM \n" +
                "(SELECT t1.`member_id`, t1.`team_bonus` FROM `ipfs_team_bonus` t1 \n" +
                "WHERE t1.`member_id` = ? AND t1.`bonus_date` = ?) tt1\n" +
                "RIGHT JOIN \n" +
                "(SELECT t2.`member_id`, SUM(t2.`team_bonus`) totalBonus FROM `ipfs_team_bonus` t2\n" +
                "WHERE t2.`member_id` = ? GROUP BY t2.member_id) tt2\n" +
                "ON tt1.member_id = tt2.member_id";
        Map<String, Object> map = daoUtil.queryForMap(query, memberId, bonusDate, memberId);
        return map;
    }

    public String getLevel(int memberId){
        Date date = HelpUtils.dateAddInt(new Date(), -1);
        String bonusDate = HelpUtils.dateToSimpleStr(date);
        String queryLevel = "SELECT t.`Level` FROM `ipfs_team_bonus` t WHERE t.`member_id` = ? AND t.`bonus_date` = ?;";
        Map<String, String> levelMap = daoUtil.queryForMap(queryLevel, memberId, bonusDate);
        //如果团队奖励有就返回团队等级
        if(levelMap != null){
            return levelMap.get("Level");
        }
        String query = "SELECT COUNT(*) FROM `ipfs_hashrate` t WHERE t.`member_id` = ?;";
        int count = daoUtil.queryForInt(query, memberId);
        //如果买有算力就返回普通矿工
        if(count > 0){
            return "0";
        }
        //否同返回空字符串
        return "";
    }

    public List<Map<String, Object>> getHashrateDetail(int memberId, String bonusDate){
        if(StringUtils.isBlank(bonusDate)){
            Date date = HelpUtils.dateAddInt(new Date(), -1);
            bonusDate = HelpUtils.dateToSimpleStr(date);
        }
        String query = "SELECT m.`m_name` phone, m.`id`, tbd.`son_level` LEVEL, IFNULL(tb1.team_bonus, 0) team_bonus,\n" +
                "IFNULL(tb1.sub_bonus_base, 0) sub_bonus_base ,(IFNULL(tb1.sub_hashrate_base, 0)+ th1.htotal) sub_hash  \n" +
                "FROM `ipfs_team_bonus` t \n" +
                "LEFT JOIN `ipfs_team_bonus_detail` tbd  ON t.`id` = tbd.`team_bonus_id`\n" +
                "LEFT JOIN (SELECT tb.member_id, tb.sub_hashrate_base, tb.`team_bonus`, tb.`sub_bonus_base` \n" +
                "FROM `ipfs_team_bonus` tb WHERE tb.`bonus_date` = ?) tb1\n" +
                "ON tb1.member_id = tbd.`son_memeber_id`\n" +
                "LEFT JOIN (SELECT th.`member_id`, SUM(th.`total`) htotal FROM `ipfs_hashrate` th GROUP BY th.`member_id`) th1\n" +
                "ON th1.member_id = tbd.`son_memeber_id`\n" +
                "LEFT JOIN `m_member` m ON m.`id` = tbd.`son_memeber_id`\n" +
                "WHERE t.`bonus_date` = ? AND t.`member_id` = ?;";
        List<Map<String, Object>> map = daoUtil.queryForList(query, bonusDate, bonusDate, memberId);
        return map;
    }
}
