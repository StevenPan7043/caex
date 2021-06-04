package com.pmzhongguo.ipfs.team.ipfsenum;

import java.math.BigDecimal;

/**
 * @author Daily
 * @date 2020/7/18 17:50
 */
public class CONSTANT {

    //HONOUR_SON 表示荣耀伞下需要的下级部门数
    public static final int GOLD_SON = 5;
    public static final int PLATINUM_SON = 3;
    public static final int DIAMOND_SON = 3;
    public static final int HONOUR_SON = 3;

    //黄金会员伞下人员必须达到的总购买量
    public static final BigDecimal GOLD_SUB_HASHRATE = new BigDecimal(100000);

    //每级团队领导人自己必须达到的购买量
    public static final BigDecimal GOLD_HASHRATE = new BigDecimal(3000);
    public static final BigDecimal PLATINUM_HASHRATE = new BigDecimal(5000);
    public static final BigDecimal DIAMOND_HASHRATE = new BigDecimal(10000);
    public static final BigDecimal HONOUR_HASHRATE = new BigDecimal(20000);

    //团队层级奖励
    public static final BigDecimal GOLD_BONUS = new BigDecimal(10);
    public static final BigDecimal PLATINUM_BONUS = new BigDecimal(15);
    public static final BigDecimal DIAMOND_BONUS = new BigDecimal(20);
    public static final BigDecimal HONOUR_BONUS = new BigDecimal(25);
}
