/**
 * zzex.com Inc.
 * Copyright (c) 2019/4/1 All Rights Reserved.
 */
package com.pmzhongguo.ex.core.utils;

import com.qiniu.conts.CharsetConst;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author ：yukai
 * @date ：Created in 2019/4/1 11:52
 * @description：des 加解密
 * @version: $
 */
public class EncryptUtils {

    private static final Logger log = LoggerFactory.getLogger(EncryptUtils.class);

    public static final int     HASH_INTERATIONS = 1024;

    public static final int     SALT_SIZE        = 8;

    private static final String PARSE_KEY        = "zzex";

    private static final String ENCRYPT_NAME     = "DES";

    /**
     * DES加密
     * @param strMing
     * @return
     */
    public static String desEncrypt(String strMing)
    {
        String strMi = "";
        try
        {
            byte[] byteMing = strMing.getBytes(CharsetConst.CHARSET_UT);
            byte[] byteMi = encryptByte(byteMing);
            strMi = Base64.encodeBase64String(byteMi);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);
        }
        return strMi;
    }

    /**
     * DES解密
     * @param strMi
     * @return
     */
    public static String desDecrypt(String strMi)
    {
        String strMing = "";
        try
        {
            byte[] byteMi = Base64.decodeBase64(strMi);
            byte[] byteMing = decryptByte(byteMi);
            strMing = new String(byteMing, CharsetConst.CHARSET_UT);
        }
        catch (UnsupportedEncodingException e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        return strMing;
    }

    static byte[] encryptByte(byte[] byteS)
    {
        byte[] byteFina = null;
        try
        {
            Cipher cipher = Cipher.getInstance(ENCRYPT_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, generatorKey(PARSE_KEY));
            byteFina = cipher.doFinal(byteS);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);
        }
        return byteFina;
    }

    static byte[] decryptByte(byte[] byteD)
    {
        byte[] byteFina = null;
        try
        {
            Cipher cipher = Cipher.getInstance(ENCRYPT_NAME);
            cipher.init(Cipher.DECRYPT_MODE, generatorKey(PARSE_KEY));
            byteFina = cipher.doFinal(byteD);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);
        }
        return byteFina;
    }

    static Key generatorKey(String parseKey)
    {
        Key key = null;
        KeyGenerator generator = null;
        try
        {
            generator = KeyGenerator.getInstance(ENCRYPT_NAME);
        }
        catch (NoSuchAlgorithmException e)
        {
            log.error(ExceptionUtils.getStackTrace(e));
        }
        if (null != generator)
        {
            // generator.init(new SecureRandom(parseKey.getBytes()));//Linux及Solaris下异常
            SecureRandom secureRandom = null;
            try
            {
                secureRandom = SecureRandom.getInstance("SHA1PRNG");
            }
            catch (NoSuchAlgorithmException e)
            {
                log.error(ExceptionUtils.getStackTrace(e));
            }
            secureRandom.setSeed(parseKey.getBytes(Charset.forName(CharsetConst.CHARSET_UT)));
            generator.init(secureRandom);
            key = generator.generateKey();
        }
        return key;
    }

    public static void main(String[] args)
    {
      /* System.out.println(EncryptUtils.encryptByte("root_dev"));
        System.out.println(EncryptUtils.desEncrypt("12345678"));*/
        System.out.println("12132");

    }
}
