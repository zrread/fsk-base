package com.fsk.framework.extend.utils.security;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    /**
     * MD5方法
     *
     * Param text 明文
     * Param key 密钥
     * Return 密文
     * throws Exception
     */
    public static String md5(String text, String key) throws Exception {
        //加密后的字符串
        String encodeStr=DigestUtils.md5Hex(text + key);
        System.out.println("MD5加密后的字符串为:encodeStr="+encodeStr);
        return encodeStr;
    }

    /**
     * MD5验证方法
     *
     * Param text 明文
     * Param key 密钥
     * Param md5 密文
     * Return true/false
     * throws Exception
     */
    public static boolean verify(String text, String key, String md5) throws Exception {
        //根据传入的密钥进行验证
        String md5Text = md5(text, key);
        if(md5Text.equalsIgnoreCase(md5))
        {
            System.out.println("MD5验证通过");
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        try {
            System.out.println(md5("qqqqqqqq","U0000400"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
