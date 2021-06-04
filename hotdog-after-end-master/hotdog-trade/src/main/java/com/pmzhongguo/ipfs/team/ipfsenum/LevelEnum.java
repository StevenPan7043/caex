package com.pmzhongguo.ipfs.team.ipfsenum;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//来源方枚举
public enum LevelEnum {

    GENERAL("0","普通"),
    GOLD("1","黄金"),
    PLATINUM("2","铂金"),
    DIAMOND("3","钻石"),
    HONOUR("4","荣耀");

    private String key;
    private String description;

    private LevelEnum(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey(){
        return key;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据Key获取枚举
     * @param key
     * @return
     */
    public static LevelEnum getEnumByKey(String key) {
        for (LevelEnum t : LevelEnum.values()) {
            if (t.key.equals(key)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 根据ordinal获取枚举
     * @param ordinal
     * @return
     */
    public static LevelEnum valueOf(int ordinal){
        if(ordinal < 0 || ordinal >= values().length){
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }

    /**
     * 获取ordinal比自己小的枚举列表
     * @return
     */
    public List<LevelEnum> ltList(){
        int ordinal = ordinal();
        List<LevelEnum> list = new ArrayList<>();
        for (int i = ordinal - 1; i >= 0; i--){
            list.add(LevelEnum.valueOf(i));
        }
        return list;
    }

    /**
     * 获取伞下高级矿工等级列表
     * 包含同级，不包含GENERAL
     * @return
     */
    public List<LevelEnum> subTeamLeadList(){
        int ordinal = ordinal();
        List<LevelEnum> list = new ArrayList<>();
        for (int i = ordinal; i > 0; i--){
            list.add(LevelEnum.valueOf(i));
        }
        return list;
    }

    /**
     * 获取当前等级的奖励率
     * @return
     */
    public BigDecimal getBonusRate() {
        switch (this) {
            case GENERAL:
                return BigDecimal.ZERO;
            case GOLD:
                return CONSTANT.GOLD_BONUS;
            case PLATINUM:
                return CONSTANT.PLATINUM_BONUS;
            case DIAMOND:
                return CONSTANT.DIAMOND_BONUS;
            case HONOUR:
                return CONSTANT.HONOUR_BONUS;
            default:
                return BigDecimal.ZERO;
        }
    }

    public BigDecimal getSameLevelBonusRate() {
        return new BigDecimal("20");
    }
}
