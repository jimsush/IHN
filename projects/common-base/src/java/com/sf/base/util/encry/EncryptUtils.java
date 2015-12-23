/*
 * $Id: EncryptUtils.java, 2015-7-19 ����05:49:50 luwei Exp $
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
 * Description:���ܺͽ�����ص���
 * </p>
 * 
 * @author luwei
 * @created 2015-7-19 ����05:49:50
 * @modified [who date description]
 * @check [who date description]
 */
public class EncryptUtils {

    public static final String MAGIC_KEY_CLIENT = "1qaz2wsx3edc";
    public static final String MAGIC_KEY_SERVER = "4rfv5tgb6yhn";

    /**
     * ʹ�������м򵥵ı���
     * 
     * @param plainText
     *            ����
     * @param key
     *            ��Կ
     * @return �������ַ���
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
     * ���봮�Ľ��루���ʽ�����룩
     * 
     * @param encryptText
     *            ����
     * @param key
     *            ��Կ
     * @return �����������ַ���
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
     * �������
     * @param clientEncryptPwd
     * @return
     */
    public static String clientPwd2ServerPwd(String clientEncryptPwd) {
        String plainText = getEncrypt(clientEncryptPwd, MAGIC_KEY_CLIENT);
        String serverEncryptPwd = setEncrypt(plainText, MAGIC_KEY_SERVER);
        return serverEncryptPwd;
    }

    /**
     * �������
     * @param serverEncryptPwd
     * @return
     */
    public static String serverPwd2ClientPwd(String serverEncryptPwd) {
        String plainText = getEncrypt(serverEncryptPwd, MAGIC_KEY_SERVER);
        String clientEncryptPwd = setEncrypt(plainText, MAGIC_KEY_CLIENT);
        return clientEncryptPwd;
    }
}
