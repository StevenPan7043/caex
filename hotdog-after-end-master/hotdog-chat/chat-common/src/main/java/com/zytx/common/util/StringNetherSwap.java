package com.zytx.common.util;

/**
 * 字符串移形换位
 * @author jary
 * @creatTime 2020/5/15 4:36 PM
 */
public class StringNetherSwap {

    /**
     * 加密移形
     * @return
     */
    public static String nether(String chars) {
        String addChar = "a" + chars.charAt(0) + chars.charAt(chars.length() - 1);
        chars = chars + addChar;
        StringBuffer stringBuffer = new StringBuffer(chars);
        chars = stringBuffer.insert(3, chars.charAt(chars.length() - 1)).toString();
        chars = stringBuffer.insert(1, chars.charAt(chars.length() - 2)).toString();
        return stringBuffer.insert(5, chars.charAt(chars.length() - 2)).toString();
    }

    /**
     * 解密换位
     * @return
     */
    public static String swap(String chars) {
        StringBuffer buffer = new StringBuffer(chars);
        buffer = buffer.deleteCharAt(5);
        buffer = buffer.deleteCharAt(1);
        buffer = buffer.deleteCharAt(3);
        return buffer.substring(0, buffer.length() - 3);
    }

    /**
     * 加密
     * @param value
     * @return
     */
    public static String encryption(String value) {
        return DESEncodeUtil.encrypt(nether(value));
    }

    /**
     * 解密
     * @param value
     * @return
     */
    public static String decrypt(String value) {
        return swap(DESEncodeUtil.decrypt(value));
    }

    public static void main(String[] args) {
        String pwd = "123456";
        System.out.println("初始值："+pwd);
        String nether = StringNetherSwap.nether(pwd);
        System.out.println("移形后：" + nether);
        String encrypt = DESEncodeUtil.encrypt(nether);
        System.out.println("加密后：" + encrypt);

        String decrypt = DESEncodeUtil.decrypt(encrypt);
        System.out.println("解密后：" + encrypt);
        String swap = StringNetherSwap.swap(decrypt);
        System.out.println("最终值：" + swap);
    }
}
