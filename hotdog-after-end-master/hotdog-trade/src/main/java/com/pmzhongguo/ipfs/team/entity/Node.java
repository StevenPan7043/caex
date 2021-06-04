package com.pmzhongguo.ipfs.team.entity;

import com.pmzhongguo.ipfs.team.ipfsenum.LevelEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daily
 * @date 2020/7/17 13:40
 */
@Data
public class Node {
    protected Integer memberId;
    /**
     * 自已的算力USDTD
     */
    protected BigDecimal hashrate;
    /**
     * 自已的奖励
     */
    protected BigDecimal bonusNum;

    /**
     * 自身等级
     */
    protected LevelEnum level = LevelEnum.GENERAL;

    /**
     * 普通矿工
     */
//    private List<Node> userChildren = new ArrayList<>();

}
