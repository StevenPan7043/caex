package com.pmzhongguo.ex.datalab.utils;

/**
 * 进制转换
 * @author jary
 * @creatTime 2019/11/30 5:34 PM
 */
public class BinarySystemUtil {

    /**
     * 10进制转2进制
     * @param value
     * @return
     */
    public static String ten2BinaryString(Integer value) {
        return Integer.toBinaryString(value);
    }

    /**
     * 2进制转10进制
     * @param value
     * @return
     */
    public static Integer binaryToDecimal(String value) {
        return Integer.valueOf(value, 2);
    }

    public static void main(String[] args) {
//        String string = ten2BinaryString(8);
//        System.out.println(ten2BinaryString(10));
//        Integer integer = binaryToDecimal("1000");
        int i = 10 & 2;
        System.out.println(ten2BinaryString(10) +"&"+ten2BinaryString(2)+"="+i);

        System.out.println(Integer.toBinaryString(i));
    }
}
