package com.pmzhongguo.ex.core.web;

/**
 * @description: 通用枚举，项目中大量的0和1对应是和否，建议使用该枚举
 * @date: 2019-06-11 19:14
 * @author: 十一
 */
public enum CommonEnum
{

    /**
     * 是
     */
    YES(1),
    /**
     * 否
     */
    NO(0);


    private Integer code;

    CommonEnum(Integer code)
    {
        this.code = code;
    }

    public Integer getCode()
    {
        return code;
    }

    public static void main(String[] args)
    {
        System.out.println(CommonEnum.NO);
    }
}
