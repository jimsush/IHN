/*
 * $Id: EncryptUtils.java, 2015-7-19 下午05:49:50 luwei Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.encry;

/**
 * <p>
 * Title: EncryptUtils
 * </p>
 * <p>
 * Description:加密和解密相关的类
 * </p>
 * 
 * @author luwei
 * @created 2015-7-19 下午05:49:50
 * @modified [who date description]
 * @check [who date description]
 */
public class EncryptUtils {

    public static final String MAGIC_KEY_CLIENT = "1qaz2wsx3edc";
    public static final String MAGIC_KEY_SERVER = "4rfv5tgb6yhn";

    /**
     * 使用异或进行简单的编码
     * 
     * @param plainText
     *            明文
     * @param key
     *            密钥
     * @return 编码后的字符串
     */
    public static String setEncrypt(String plainText, String key) {
        int[] snNum = new int[plainText.length()];
        StringBuilder encryptText = new StringBuilder();
        String temp = "";
        for (int i = 0, j = 0; i < plainText.length(); i++, j++) {
            if (j == key.length())
                j = 0;
            snNum[i] = plainText.charAt(i) ^ key.charAt(j);
        }

        for (int k = 0; k < plainText.length(); k++) {
            if (snNum[k] < 10) {
                temp = "00" + snNum[k];
            } else {
                if (snNum[k] < 100) {
                    temp = "0" + snNum[k];
                }
            }
            encryptText.append(temp);
        }
        return encryptText.toString();
    }

    /**
     * 编码串的解码（异或方式来解码）
     * 
     * @param encryptText
     *            密文
     * @param key
     *            密钥
     * @return 解码后的明文字符串
     */
    public static String getEncrypt(String encryptText, String key) {
        char[] snNum = new char[encryptText.length() / 3];
        StringBuilder plainText = new StringBuilder();
        for (int i = 0, j = 0; i < encryptText.length() / 3; i++, j++) {
            if (j == key.length())
                j = 0;
            int n = Integer.parseInt(encryptText.substring(i * 3, i * 3 + 3));
            snNum[i] = (char) ((char) n ^ key.charAt(j));
        }

        for (int k = 0; k < encryptText.length() / 3; k++) {
            plainText.append(snNum[k]);
        }
        return plainText.toString();
    }

    /**
     * 密码加密
     * @param clientEncryptPwd
     * @return
     */
    public static String clientPwd2ServerPwd(String clientEncryptPwd) {
        String plainText = getEncrypt(clientEncryptPwd, MAGIC_KEY_CLIENT);
        String serverEncryptPwd = setEncrypt(plainText, MAGIC_KEY_SERVER);
        return serverEncryptPwd;
    }

    /**
     * 密码解密
     * @param serverEncryptPwd
     * @return
     */
    public static String serverPwd2ClientPwd(String serverEncryptPwd) {
        String plainText = getEncrypt(serverEncryptPwd, MAGIC_KEY_SERVER);
        String clientEncryptPwd = setEncrypt(plainText, MAGIC_KEY_CLIENT);
        return clientEncryptPwd;
    }
}
