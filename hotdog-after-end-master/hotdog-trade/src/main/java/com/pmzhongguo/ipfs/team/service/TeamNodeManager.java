package com.pmzhongguo.ipfs.team.service;

import com.pmzhongguo.ex.business.service.manager.IntroduceRelationManager;
import com.pmzhongguo.ipfs.team.entity.Node;
import com.pmzhongguo.ipfs.team.entity.TeamNode;
import com.pmzhongguo.ipfs.team.ipfsenum.LevelEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author Daily
 * @date 2020/7/18 11:44
 */
@Service
public class TeamNodeManager {

    @Autowired
    private IntroduceRelationManager introduceRelationManager;

    @Autowired
    private TeamNodeService teamNodeService;

    /**
     * 获得团队所有初始化节点
     *
     * @param Bonusdate yyyy-MM-dd
     */
    public Map<String, TeamNode> initNode(String Bonusdate) {
        return teamNodeService.initNode(Bonusdate);
    }

    /**
     * 将各个矿工节点联接成Tree
     *
     * @param maps
     * @return 返回根节点的list
     */
    public List<TeamNode> buildTree(Map<String, TeamNode> maps) {
        Set<String> set = maps.keySet();
        for (String key : set) {
            TeamNode node = maps.get(key);
            String parentid = introduceRelationManager.getParentid(node.getMemberId(), set);
            if (StringUtils.isNotBlank(parentid)) {
                TeamNode parentNode = maps.get(parentid);
                //设置父ID
                node.setParentNode(parentNode);
                //把节点关联到父节点
                parentNode.getUserChildren().add(node);
            }
        }
        List<TeamNode> rootList = new ArrayList<>();
        for (String key : set) {
            //Parentid为null表示root节点
            if (maps.get(key).getParentNode() == null) {
                rootList.add(maps.get(key));
            }
        }
        return rootList;
    }

    /**
     * 将一棵树整理成队列
     * 头是root,尾是最底层
     *
     * @param root
     * @return
     */
    public List<TeamNode> buildList(TeamNode root) {
        List<TeamNode> list = new ArrayList<>();
        list.add(root);
        for (int i = 0; i < list.size(); i++) {
            list.addAll(list.get(i).getUserChildren());
        }
        return list;
    }

    public List<TeamNode> buildList1(TeamNode root) {
        List<TeamNode> list = new ArrayList<>();
        List<String> listKey = get();
        list.add(root);
        for (int i = 0; i < list.size(); i++) {
            for(String key : listKey){
                Map<String, List<TeamNode>> tempList = list.get(i).getLeaderChildren();
                if(tempList != null && tempList.get(key) != null){
                    list.addAll(tempList.get(key));
                }
            }

        }
        return list;
    }

    private List<String> get(){
        List<String> list = new ArrayList<>();
        LevelEnum[] values = LevelEnum.values();
        for(LevelEnum e : values){
            list.add(e.getKey());
        }
        return list;
    }

    public void neatenTree(TeamNode root) {
        List<TeamNode> list = buildList(root);
        for (int i = list.size() - 1; i >= 0; i--) {
            TeamNode node = list.get(i);
            node.neatenNode();
        }
    }

    /**
     * 整理树的等级顺序
     * 子节点等级不能大于父节点
     * 子节点找不到适合的父节点就返回自己做为root节点
     *
     * @param root 根节点
     * @return
     */
    public List<TeamNode> neatenTreeLevel(TeamNode root) {
        List<TeamNode> list = buildList1(root);
        List<TeamNode> rootList = new ArrayList<>();
//        for (TeamNode node : list) {
        for (int i = list.size() - 1; i >= 0; i--) {
            TeamNode node = list.get(i);
            if (node.getParentNode() != null) {
                //团队长节点
                if (node.getLevel().ordinal() > node.getParentNode().getLevel().ordinal()) {
                    //拿到父节点的list
                    List<TeamNode> nodes = node.getParentNode().getLeaderChildren().get(String.valueOf(node.getLevel().ordinal()));
                    if(nodes == null){
                        nodes = new ArrayList<>();
                        node.getParentNode().getLeaderChildren().put(String.valueOf(node.getLevel().ordinal()), nodes);
                    }
                    BigDecimal removeSubHashrate = BigDecimal.ZERO;
                    BigDecimal removeSubBonusNum = BigDecimal.ZERO;
                    for (TeamNode removeNode : nodes){
                        removeSubHashrate =  removeSubHashrate.add(removeNode.getSubHashrate()).add(removeNode.getHashrate());
                        removeSubBonusNum = removeSubBonusNum.add(removeNode.getSubBonusNum()).add(removeNode.getBonusNum());
                    }
                    //找到最上层的父节点
                    TeamNode parentNode = getParentNode(node, node.getParentNode(), removeSubHashrate, removeSubBonusNum);
                    //将节点从父节点移除
                    node.getParentNode().getLeaderChildren().remove(String.valueOf(node.getLevel().ordinal()));
                    node.getParentNode().neatenNode();
                    if (parentNode == null) {
                        //为null说明自己做为root节点
                        rootList.addAll(nodes);
                    } else {
                        //将子节点加到父节点
                        List<TeamNode> parentNodes = parentNode.getLeaderChildren().get(node.getLevel().getKey());
                        parentNodes.addAll(nodes);
                        parentNode.getLeaderChildren().put(node.getLevel().getKey(), parentNodes);
                        parentNode.neatenNode();
                    }
                }
            }
        }
        return rootList;
    }


    /**
     * 获取大于等于节点等级的父节点
     *
     * @param node
     * @param parentNode 返回符合条件的父节点
     *                   如果为null表明没有符合条件的父节点，自己做为父节点
     * @return
     */
    public TeamNode getParentNode(TeamNode node, TeamNode parentNode, BigDecimal removeSubHashrate, BigDecimal removeSubBonusNum ) {
        //将子节点的数据从非父节点去掉
        parentNode.setSubHashrate(parentNode.getSubHashrate().subtract(removeSubHashrate));
        parentNode.setSubBonusNum(parentNode.getSubBonusNum().subtract(removeSubBonusNum));
        //当前节点等级小于子节点，所以要再找上一级的父节点
        parentNode = parentNode.getParentNode();
        if (parentNode == null) {
            return null;
        }
        if (node.getLevel().ordinal() > parentNode.getLevel().ordinal()) {
            return getParentNode(node, parentNode, removeSubHashrate, removeSubBonusNum);
        }
        return parentNode;
    }
}
