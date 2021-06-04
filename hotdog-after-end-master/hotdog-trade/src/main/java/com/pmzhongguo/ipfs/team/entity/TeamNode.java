package com.pmzhongguo.ipfs.team.entity;

import com.pmzhongguo.ipfs.team.ipfsenum.CONSTANT;
import com.pmzhongguo.ipfs.team.ipfsenum.LevelEnum;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/20 10:08
 */
@Data
public class TeamNode extends Node {

    /**
     * 伞下普通矿工的算力USDTD
     * 判断是否达到黄金矿工等级用
     */
    private BigDecimal subHashrate = BigDecimal.ZERO;
    /**
     * 伞下普通矿工的日常奖励
     */
    private BigDecimal subBonusNum = BigDecimal.ZERO;


    /**
     * 团队收益
     */
    private BigDecimal teamBonus  = BigDecimal.ZERO;;

    /**
     * 父节点
     */
    protected TeamNode parentNode;

    /**
     * key为等级
     * 分类后的伞下矿工
     */
    private Map<String, List<TeamNode>> LeaderChildren = new HashMap<>();

    /**
     * 伞下矿工
     * key为矿工memberId
     */
    private List<TeamNode> userChildren = new ArrayList<>();

    /**
     * 处理节点数据
     */
    public void neatenNode() {
        if (userChildren.size() > 0) {
            for (TeamNode node : userChildren) {
                String level = node.getLevel().getKey();
                List<TeamNode> list = LeaderChildren.get(level);
                if (list == null) {
                    list = new ArrayList<TeamNode>();
                }
                list.add(node);
                subHashrate = subHashrate.add(node.hashrate).add(node.subHashrate);
                subBonusNum = subBonusNum.add(node.bonusNum).add(node.subBonusNum);
                LeaderChildren.put(level, list);
            }
            userChildren.clear();
        }
        setLevel();
        //如果是普通矿工而且还有下级矿工，就把下级给上级
        boolean isGENERAL = this.level.equals(LevelEnum.GENERAL);
        List<TeamNode> list = LeaderChildren.get(LevelEnum.GENERAL.getKey());
        boolean isList = CollectionUtils.isNotEmpty(list);
        boolean isParentNode = this.getParentNode() != null;
        if(isGENERAL && isList && isParentNode){
            subHashrate = BigDecimal.ZERO;
            subBonusNum = BigDecimal.ZERO;
            this.getParentNode().getUserChildren().addAll(LeaderChildren.get(LevelEnum.GENERAL.getKey()));
            LeaderChildren.remove(LevelEnum.GENERAL.getKey());
        }
    }

    /**
     * 设置节点等级
     */
    public void setLevel() {
        if(
                this.getMemberId().intValue() == 713544     //13738371691
                || this.getMemberId().intValue() == 713568  //13812798285
                || this.getMemberId().intValue() == 713716  //15051690111
                || this.getMemberId().intValue() == 713541  //15071517231
                || this.getMemberId().intValue() == 713542  //18913222224
        ){
            level = LevelEnum.HONOUR;
            return;
        }
        LevelEnum hashrateLevel = getMaxHashrateLevel();
        LevelEnum subordinateLevel = getMaxSubordinateLevel();
        if (hashrateLevel.compareTo(subordinateLevel) > -1) {
            level = subordinateLevel;
        } else {
            level = hashrateLevel;
        }
    }

    public void setLevel(LevelEnum level) {
        this.level = level;
    }

    /**
     * 获取个人租赁算力能达到的最大等级
     *
     * @return
     */
    private LevelEnum getMaxHashrateLevel() {
        if (hashrate.compareTo(CONSTANT.HONOUR_HASHRATE) > -1) {
            return LevelEnum.HONOUR;
        } else if (hashrate.compareTo(CONSTANT.DIAMOND_HASHRATE) > -1) {
            return LevelEnum.DIAMOND;
        } else if (hashrate.compareTo(CONSTANT.PLATINUM_HASHRATE) > -1) {
            return LevelEnum.PLATINUM;
        } else if (hashrate.compareTo(CONSTANT.GOLD_HASHRATE) > -1) {
            return LevelEnum.GOLD;
        } else {
            return LevelEnum.GENERAL;
        }
    }

    /**
     * 获取伞下部门达到的等级
     *
     * @return
     */
    private LevelEnum getMaxSubordinateLevel() {
        //判断是否是荣耀矿工情况一
        if (LeaderChildren.get(LevelEnum.HONOUR.getKey()) != null) {
            return LevelEnum.HONOUR;
        }
        //判断是否是荣耀矿工情况二
        if (LeaderChildren.get(LevelEnum.DIAMOND.getKey()) != null) {
            if (LeaderChildren.get(LevelEnum.DIAMOND.getKey()).size() >= CONSTANT.HONOUR_SON) {
                return LevelEnum.HONOUR;
            }
            return LevelEnum.DIAMOND;
        }
        //判断是否是钻石矿工
        if (LeaderChildren.get(LevelEnum.PLATINUM.getKey()) != null) {
            if (LeaderChildren.get(LevelEnum.PLATINUM.getKey()).size() >= CONSTANT.DIAMOND_SON) {
                return LevelEnum.DIAMOND;
            }
            return LevelEnum.PLATINUM;
        }
        //判断是否是铂金矿工
        if (LeaderChildren.get(LevelEnum.GOLD.getKey()) != null) {
            if (LeaderChildren.get(LevelEnum.GOLD.getKey()).size() >= CONSTANT.PLATINUM_SON) {
                return LevelEnum.PLATINUM;
            }
            return LevelEnum.GOLD;
        }
        /**
         * 判断是否是黄金矿工
         */
        if (LeaderChildren.get(LevelEnum.GENERAL.getKey()) != null
                && LeaderChildren.get(LevelEnum.GENERAL.getKey()).size() >= CONSTANT.GOLD_SON
                && subHashrate.compareTo(CONSTANT.GOLD_SUB_HASHRATE) > -1) {
            return LevelEnum.GOLD;
        }
        return LevelEnum.GENERAL;
    }

//    /**
//     * 设置节点用户能达到的最高等级
//     * 一般在neatenNode()方法后使用
//     */
//    public void setLevel() {
//        if (isHonour()) {
//            level = LevelEnum.HONOUR;
//        } else if (isDiamond()) {
//            level = LevelEnum.DIAMOND;
//        } else if (isPlatinum()) {
//            level = LevelEnum.PLATINUM;
//        } else if (isGold()) {
//            level = LevelEnum.GOLD;
//        }
//    }
//
//    private boolean isHonour() {
//        return LeaderChildren.get(LevelEnum.HONOUR.getKey()) != null
//                && LeaderChildren.get(LevelEnum.HONOUR.getKey()).size() >= CONSTANT.HONOUR_SON
//                && hashrate.compareTo(CONSTANT.HONOUR_HASHRATE) > -1;
//    }
//
//    private boolean isDiamond() {
//        return LeaderChildren.get(LevelEnum.DIAMOND.getKey()) != null
//                && LeaderChildren.get(LevelEnum.DIAMOND.getKey()).size() >= CONSTANT.DIAMOND_SON
//                && hashrate.compareTo(CONSTANT.DIAMOND_HASHRATE) > -1;
//    }
//
//    private boolean isPlatinum() {
//        return LeaderChildren.get(LevelEnum.PLATINUM.getKey()) != null
//                && LeaderChildren.get(LevelEnum.PLATINUM.getKey()).size() >= CONSTANT.PLATINUM_SON
//                && hashrate.compareTo(CONSTANT.PLATINUM_HASHRATE) > -1;
//    }
//
//    private boolean isGold() {
//        return LeaderChildren.get(LevelEnum.GOLD.getKey()) != null
//                && LeaderChildren.get(LevelEnum.GENERAL.getKey()).size() >= CONSTANT.GOLD_SON
//                && subHashrate.compareTo(CONSTANT.GOLD_SUB_HASHRATE) > -1
//                && hashrate.compareTo(CONSTANT.GOLD_HASHRATE) > -1;
//    }

}
