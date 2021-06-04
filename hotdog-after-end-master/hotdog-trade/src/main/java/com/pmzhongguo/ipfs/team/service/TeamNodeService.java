package com.pmzhongguo.ipfs.team.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.pmzhongguo.ex.core.utils.DaoUtil;
import com.pmzhongguo.ipfs.team.entity.TeamNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Daily
 * @date 2020/7/17 18:04
 */
@Service
public class TeamNodeService {

    @Autowired
    private DaoUtil daoUtil;

    /**
     * 获得团队所有初始化节点
     * @param Bonusdate yyyy-MM-dd
     */
    public Map<String, TeamNode> initNode(String Bonusdate){
        Map<String, TeamNode> nodeMap = new HashMap<>();
        int start = 0;
        int limit = 100;
        while (true) {
//            SELECT t11.member_id, t11.bonus_num, t22.total
//            FROM  (SELECT t1.`member_id`, SUM(t1.`bonus_num`) bonus_num
//            FROM `ipfs_user_bonus` t1 WHERE t1.`bonus_date` = ? GROUP BY t1.`member_id`) t11
//            RIGHT JOIN (SELECT t2.`member_id`, SUM(t2.`total`) total
//            FROM `ipfs_hashrate` t2
//            LEFT JOIN `ipfs_project` tp ON tp.id = t2.`project_id`
//            WHERE t2.`status` = '1'   AND tp.`run_status` = 2
//            GROUP BY t2.`member_id`) t22 ON t22.member_id = t11.member_id
//            ORDER BY t11.member_id LIMIT ?, ?
            String query = "SELECT \n" +
                    "  t11.member_id,\n" +
                    "  t11.bonus_num,\n" +
                    "  t22.total \n" +
                    "FROM\n" +
                    "  (SELECT \n" +
                    "    t1.`member_id`,\n" +
                    "    SUM(t1.`bonus_num`) bonus_num \n" +
                    "  FROM\n" +
                    "    `ipfs_user_bonus` t1 \n" +
                    "  WHERE t1.`bonus_date` = ? \n" +
                    "  GROUP BY t1.`member_id`) t11 \n" +
                    "  RIGHT JOIN \n" +
                    "    (SELECT \n" +
                    "      t2.`member_id`,\n" +
                    "      SUM(t2.`total`) total \n" +
                    "    FROM\n" +
                    "      `ipfs_hashrate` t2 LEFT JOIN `ipfs_project` tp ON tp.id = t2.`project_id` \n" +
                    "      WHERE t2.`status` = '1'  AND tp.`run_status` = 2 \n" +
                    "    GROUP BY t2.`member_id`) t22 \n" +
                    "    ON t22.member_id = t11.member_id \n" +
                    "ORDER BY t11.member_id \n" +
                    "LIMIT ?, ?";
            List<Map<String, Integer>> idList = daoUtil.queryForList(query, Bonusdate, start, limit);
            if(CollectionUtils.isEmpty(idList)){
                return nodeMap;
            }
            for(Map<String, Integer> m : idList){
                TeamNode node = new TeamNode();
                String memeberId = String.valueOf(m.get("member_id"));
                node.setMemberId(Integer.valueOf(memeberId));
                node.setBonusNum(new BigDecimal(String.valueOf(m.get("bonus_num"))));
                node.setHashrate(new BigDecimal(String.valueOf(m.get("total"))));
                nodeMap.put(memeberId, node);
            }
            start += limit;
        }
    }
}
