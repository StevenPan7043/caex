package com.pmzhongguo.ex.core.web;

import com.pmzhongguo.otc.otcenum.PayTypeEnum;

import java.util.HashMap;
import java.util.Map;

//来源方枚举
public enum OptSourceEnum {
    //名称
    JSC(1,"JSC"),
    GSTTPROJECT(2,"GSTTProject"),
    LASTWINNER(3,"lastWiner"),
    GMC(4,"gmc"),
    LOCKACCOUNT(5,"lockAccount"),
    OTC(6,"otc"),
    ZZEX(7,"zzex"),
    CURRENCYLOCK(8,"currencylock"),
    OLDOTC(9,"OLDOTC"),
    JEFF(10,"jeff"),
    YTCT(11,"ytct"),
    NRS(12,"nrs"),
    CONTRACT(13,"contract"),
    IPFS(14,"ipfs");

    private int type;
    private String code;

    private OptSourceEnum(int type, String code) {
        this.type = type;
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public String getCode() {
        return code;
    }


    public static OptSourceEnum getEnumByCode(String code) {
        for (OptSourceEnum t : OptSourceEnum.values()) {
            if (t.code.equals(code)) {
                return t;
            }
        }
        return null;
    }
}
