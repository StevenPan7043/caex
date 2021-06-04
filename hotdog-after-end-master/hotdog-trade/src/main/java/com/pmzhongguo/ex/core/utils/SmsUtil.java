package com.pmzhongguo.ex.core.utils;

import com.pmzhongguo.ex.business.entity.Member;
import com.pmzhongguo.ex.core.web.*;
import com.qiniu.util.BeanUtil;
import org.apache.commons.lang3.StringUtils;

import java.awt.geom.Area;

/**
 * @author jary
 * @creatTime 2019/7/2 3:07 PM
 */
public class SmsUtil {


    /**
     * 获取短信模版
     * @param areaCode
     * @param args
     * @return
     */
    public static String getSms(String areaCode, Object... args) {
        if (BeanUtil.isEmpty(areaCode) || areaCode.equalsIgnoreCase(AreaCodeEnum.CH.getAreaCode() + "")) {
            return String.format(InternationalSmsCNEnum.getEnumByType(Integer.valueOf(args[args.length - 1].toString())).getCnCode(), args);
        } else if (areaCode.equals(AreaCodeEnum.KO.getAreaCode() + "")) {
            return String.format(InternationalSmsKOEnum.getEnumByType(Integer.valueOf(args[args.length - 1].toString())).getCnCode(), args);
        } else if (areaCode.equals(AreaCodeEnum.JA.getAreaCode() + "")) {
            return String.format(InternationalSmsJAEnum.getEnumByType(Integer.valueOf(args[args.length - 1].toString())).getCnCode(), args);
        } else if (areaCode.equals(AreaCodeEnum.EN.getAreaCode() + "")) {
            return String.format(InternationalSmsENEnum.getEnumByType(Integer.valueOf(args[args.length - 1].toString())).getCnCode(), args);
        } else if (areaCode.equals(AreaCodeEnum.RU.getAreaCode() + "")) {
            return String.format(InternationalSmsRUEnum.getEnumByType(Integer.valueOf(args[args.length - 1].toString())).getCnCode(), args);
        } else if (areaCode.equals(AreaCodeEnum.HK.getAreaCode() + "") || areaCode.equals(AreaCodeEnum.TAIWAN.getAreaCode() + "")) {
            return String.format(InternationalSmsHKEnum.getEnumByType(Integer.valueOf(args[args.length - 1].toString())).getCnCode(), args);
        } else {
            return String.format(InternationalSmsCNEnum.getEnumByType(Integer.valueOf(args[args.length - 1].toString())).getCnCode(), args);
        }
    }
}
