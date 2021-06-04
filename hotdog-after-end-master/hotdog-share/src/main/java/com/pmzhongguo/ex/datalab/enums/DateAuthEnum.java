package com.pmzhongguo.ex.datalab.enums;

import com.pmzhongguo.zzextool.utils.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jary
 * @creatTime 2019/11/30 5:18 PM
 */
public enum DateAuthEnum {

    /**
     * 实验室
     */
    DATA_LAB("1000", "实验室","dataLab",false),
    /**
     * 交易数据
     */
    DATA_TRADE("0100", "交易数据","dataTrade",false),
    /**
     * 充提数据
     */
    DATA_COLLECTION("0010", "充提数据","dataCollection",false),

    /**
     * 我的钱包
     */
    DATA_WALLET("0001", "我的钱包","dataWallet",false);
    private String type;
    private String code;
    private String codeEn;
    private boolean flag;

    DateAuthEnum(String type, String code, String codeEn, boolean flag) {
        this.type = type;
        this.code = code;
        this.codeEn = codeEn;
        this.flag = flag;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getCodeEn() {
        return codeEn;
    }

    public boolean isFlag() {
        return flag;
    }

    /**
     * 获取权限
     *
     * @param binary 权限10进制值
     * @return
     */
    public static List<Map<String, Boolean>> getDateAuth(Integer binary) {
        List<Map<String, Boolean>> list = new ArrayList<>();
        int len = Integer.toBinaryString(binary).length();
        for (int i = 1; i <= len; i++) {
            int position = 1 << (i - 1);
            int value = binary & position;
            if (value == 0) {
                continue;
            }
            if (getDateAuthEnum(value) != null) {
                Map<String, Boolean> resultMap = new HashMap<>();
                resultMap.put(getDateAuthEnum(value).codeEn, true);
                list.add(resultMap);
            }
        }
        return list;
    }

    private static DateAuthEnum getDateAuthEnum(int value) {
        for (DateAuthEnum da : DateAuthEnum.values()) {
            String string = String.format("%04d", Integer.valueOf(Integer.toBinaryString(value)));
            if (da.type.equals(string)) {
                return da;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(JsonUtil.beanToJson(DateAuthEnum.getDateAuth(9)));
//        String format = String.format("%04d", 10);
//        System.out.println(format);
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
