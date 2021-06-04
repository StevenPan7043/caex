package com.pmzhongguo.ex.core.web;

import java.util.Arrays;

/**
 * 国际语言、区号
 * @author jary
 * @creatTime 2019/7/3 9:17 AM
 */
public enum AreaCodeEnum {
    CH(86,"zh","中国","汉语"),
    EN(1,"en","美国","英语"),
    JA(81,"ja","日本","日语"),
    KO(82,"ko","韩国","韩语"),
    RU(7,"ru","俄罗斯","俄语"),
    HK(852,"hk","中国香港","繁体中文"),
    TAIWAN(886,"tw","中国台湾","繁体中文"),
    UK(44,"uk","英国","英语")
    ;


    /**
     * 区号
     */
    private Integer areaCode;

    /**
     * 语言
     */
    private String language;

    /**
     * 国家
     */
    private String country;

    /**
     * 中文语言
     */
    private String languageForCh;

    AreaCodeEnum(Integer areaCode, String language, String country, String languageForCh) {
        this.areaCode = areaCode;
        this.language = language;
        this.country = country;
        this.languageForCh = languageForCh;
    }

    public static AreaCodeEnum getEnumByAreaCode(Integer areaCode) {
        for (AreaCodeEnum o : AreaCodeEnum.values()) {

            if (o.getAreaCode() == areaCode) {
                return o;
            }
        }
        return null;
    }

    public static AreaCodeEnum getEnumByLanguage(String language) {
        for (AreaCodeEnum o : AreaCodeEnum.values()) {
            if (o.getLanguage().equals(language)) {
                return o;
            }
        }
        return null;
    }

    public Integer getAreaCode() {
        return areaCode;
    }

    public String getLanguage() {
        return language;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguageForCh() {
        return languageForCh;
    }
}
