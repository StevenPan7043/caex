package com.pmzhongguo.ipfs.scheduler;

import com.pmzhongguo.ex.core.utils.HelpUtils;
import com.pmzhongguo.ex.core.utils.IPAddressPortUtil;
import com.pmzhongguo.ex.core.utils.JedisUtil;
import com.pmzhongguo.ipfs.entity.IpfsOutput;
import com.pmzhongguo.ipfs.entity.IpfsProject;
import com.pmzhongguo.ipfs.entity.IpfsTeamBonus;
import com.pmzhongguo.ipfs.entity.IpfsUserBonus;
import com.pmzhongguo.ipfs.service.*;
import com.pmzhongguo.ipfs.team.entity.TeamNode;
import com.pmzhongguo.ipfs.team.ipfsenum.LevelEnum;
import com.pmzhongguo.ipfs.team.service.TeamNodeManager;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Daily
 * @date 2020/7/14 18:01
 */
@Component
public class IpfsUserBonusScheduler {

    private static final Logger logger = LoggerFactory.getLogger(IpfsUserBonusScheduler.class);

    @Autowired
    private IpfsOutputManager ipfsOutputManager;

    @Autowired
    private IpfsUserBonusManager ipfsUserBonusManager;

    @Autowired
    private TeamNodeManager teamNodeManager;

    @Autowired
    private IpfsTeamBonusManager ipfsTeamBonusManager;

    @Autowired
    private IpfsProjectManager ipfsProjectManager;
    @Scheduled(cron = "0 0 1 * * ?")
    public void grantUserBonus() {
        String globalLockKey = "IPFSUSERBONUSSCHEDULER_GRANTUSERBONUS";
        boolean globalIsLock = JedisUtil.getInstance().getLock(globalLockKey, IPAddressPortUtil.IP_ADDRESS, 1000 * 60 * 60);
        if (globalIsLock) {

            try {
                Date date = HelpUtils.dateAddInt(new Date(), -1);
                String bonusDate = HelpUtils.dateToSimpleStr(date);
                //????????????????????????
                perReward(bonusDate);
                //??????????????????
                teamReward(bonusDate);
            } finally {
                JedisUtil.getInstance().releaseLock(globalLockKey, IPAddressPortUtil.IP_ADDRESS);
            }
        }
    }

    /**
     * ??????????????????
     *
     * @param bonusDate
     */
    public void personReward(String bonusDate) {
        StopWatch sw = new StopWatch("");
        sw.start("personReward start");
        List<IpfsOutput> ipfsOutputList = ipfsOutputManager.createAll(bonusDate);
        int r = ipfsOutputManager.insert(ipfsOutputList);
        if (r != ipfsOutputList.size()) {
            logger.warn("?????? " + ipfsOutputList.size() + " ???ipfs?????????????????????????????? " + r + " ??????");
        }
        for (IpfsOutput ipfsOutput : ipfsOutputList) {
            ipfsUserBonusManager.create(ipfsOutput.getProjectId(), ipfsOutput.getOutputDate(), ipfsOutput.getCapacity());
        }
        sw.stop();
        logger.warn(sw.prettyPrint());
    }

    /**
     * ????????????
     *
     * @param bonusDate
     */
    public void teamReward(String bonusDate) {
        StopWatch sw = new StopWatch("");
        sw.start("teamReward start");
        //?????????????????????
        Map<String, TeamNode> allNode = teamNodeManager.initNode(bonusDate);
        //??????????????????????????????????????????????????????
        List<TeamNode> rootList = teamNodeManager.buildTree(allNode);
        //???????????????????????????
        for (TeamNode node : rootList) {
            teamNodeManager.neatenTree(node);
        }
        //???????????????????????????????????????
        List<TeamNode> newRootList = new ArrayList<>();
        for (TeamNode node : rootList) {
            newRootList.addAll(teamNodeManager.neatenTreeLevel(node));
        }
        rootList.addAll(newRootList);
        newRootList.clear();

        //??????????????????root??????
        for (TeamNode node : rootList) {
            if (node.getLevel() == LevelEnum.GENERAL) {
                continue;
            }
            //??????????????????List
            List<TeamNode> treeList = teamNodeManager.buildList1(node);
            //????????????????????????
            for (TeamNode leafNode : treeList) {
                if (leafNode.getLevel() == LevelEnum.GENERAL) {
                    continue;
                }
                List<IpfsTeamBonus> bonuses = ipfsTeamBonusManager.findByCondition(HelpUtils.newHashMap("memberId", leafNode.getMemberId(), "bonusDate", bonusDate));
                if (CollectionUtils.isNotEmpty(bonuses)) {
                    continue;
                }
                ipfsTeamBonusManager.reward(leafNode, bonusDate);
            }
        }
        sw.stop();
        logger.warn(sw.prettyPrint());
    }

    /**
     * ??????????????????
     *
     * @param bonusDate
     */
    @Transactional
    public void perReward(String bonusDate) {
        List<IpfsOutput> ipfsOutputList = ipfsOutputManager.createAll(bonusDate);
        int r = ipfsOutputManager.insert(ipfsOutputList);
        if (r != ipfsOutputList.size()) {
            logger.warn("?????? " + ipfsOutputList.size() + " ???ipfs?????????????????????????????? " + r + " ??????");
        }
        Map<String, Object> param = new HashMap<>();
        param.put("outputStatus", 0);
        param.put("pageNum", 0);
        param.put("limit", 1000);
        List<IpfsOutput> allOut = ipfsOutputManager.findAllByStatus(param);
        if (allOut.isEmpty()) {
            return;
        }
        //??????ID
        List<Integer> projectIdList = allOut.stream().map(IpfsOutput::getProjectId).collect(Collectors.toList());
        Map<String, Object> param1 = new HashMap<>();
        param1.put("idList", projectIdList);
        List<IpfsProject> ipfsProjectList = ipfsProjectManager.findIpfsProject(param1);
        //????????????????????????
        Map<Integer, BigDecimal> feeMap = new HashMap<>();
        //????????????
        Map<Integer, String> typeMap = new HashMap<>();
        for (IpfsProject project : ipfsProjectList) {
            feeMap.put(project.getId(), project.getFee());
            typeMap.put(project.getId(), project.getType());
        }
        //?????????????????????????????????,??????:??????ID={????????????(yyyy-mm-dd)=????????????}
        Map<Integer, Map<String, BigDecimal>> projectMap = new HashMap<>();
        for (IpfsOutput ipfsOutput : allOut) {
            BigDecimal unlock = ipfsOutputManager.unlockOutPut(bonusDate, ipfsOutput, typeMap);
            if (projectMap.get(ipfsOutput.getProjectId()) == null) {
                Map<String, BigDecimal> map = new HashMap<>();
                map.put(ipfsOutput.getOutputDate(), unlock);
                projectMap.put(ipfsOutput.getProjectId(), map);
            } else {
                projectMap.get(ipfsOutput.getProjectId()).put(ipfsOutput.getOutputDate(), unlock);
            }
        }
        //????????????
        int i = ipfsOutputManager.reduceCapacity(allOut);
        if (i != allOut.size()) {
            logger.warn("????????????" + allOut.size() + " ???ipfsOutput?????????????????????" + i + " ??????");
        }

        ipfsUserBonusManager.create(projectMap, feeMap, projectIdList, bonusDate);
    }
}