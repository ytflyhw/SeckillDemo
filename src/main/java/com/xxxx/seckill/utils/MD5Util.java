package com.xxxx.seckill.utils;


import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToFormPass(String inputPass){
        String str = "" + salt.charAt(0) + salt.charAt(1) + inputPass + salt.charAt(4) + salt.charAt(5);
        return md5(str);
    }

    public static String formPassToDBpass(String formPass, String salt){
        String str = "" + salt.charAt(2) + salt.charAt(3) + formPass + salt.charAt(6) + salt.charAt(7);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass, String salt){
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBpass(formPass, salt);
        return  dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(inputPassToDBPass("123456", "abcdefgh"));
    }
}