package com.pmzhongguo.ipfs.team.ipfsenum;


/**
 * @author Daily
 * @date 2020/7/24 17:23
 */
public enum TypeEnum {
    HASHREATE("hashreate", "算力"),
    MILL("mill", "矿机");
    private String type;
    private String desc;

    private TypeEnum(String type, String desc){
        this.type = type;
        this.desc = desc;
    }

    public static TypeEnum getEnumByType(String type) {
        for (TypeEnum t : TypeEnum.values()) {
            if (t.type.equals(type)) {
                return t;
            }
        }
        return null;
    }
}
