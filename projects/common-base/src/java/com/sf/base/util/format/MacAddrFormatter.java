/*
 * $Id: MacFormatter.java, 2015-11-12 ����10:49:13 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.format;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Title: MacAddrFormatter
 * </p>
 * <p>
 * Description:mac��ַת��
 * </p>
 * 
 * @author sufeng
 * created 2015-11-12 ����10:49:13
 * modified [who date description]
 * check [who date description]
 */
public class MacAddrFormatter {
    
    /**
     * mac��ַ��ʽת��,��"00:01:02:03:04:05"/"00-01-02-03-04-05"ת��Ϊ"000102030405"
     * 
     * @param macAddr
     * @return
     */
    public static String formatMacAddr(String macAddr) {
        if (StringUtils.isEmpty(macAddr))
            return "";
        if (macAddr.length() != 17) {
            return macAddr;
        }

        // ������װ
        StringBuilder bf = new StringBuilder();
        bf.append(macAddr.substring(0, 2)).append(macAddr.substring(3, 5)).append(macAddr.substring(6, 8))
                .append(macAddr.substring(9, 11)).append(macAddr.substring(12, 14)).append(
                        macAddr.substring(15, 17));
        return bf.toString();
    }

    /**
     * ��mac��ַת���ֽ�����
     * @param macAddr �ַ����͵�mac��ַ,����"01-02-03-04-05-06" "01:02:03:04:05:06"
     * @return �ֽ�����
     */
    public static byte[] formatMacAddr2ByteArray(String macAddr) {
        byte[] mac = new byte[6];
        if (StringUtils.isEmpty(macAddr))
            return mac;
        if (macAddr.length() != 17)
            return mac;

        mac[0] = Integer.valueOf(macAddr.substring(0, 2), 16).byteValue();
        mac[1] = Integer.valueOf(macAddr.substring(3, 5), 16).byteValue();
        mac[2] = Integer.valueOf(macAddr.substring(6, 8), 16).byteValue();
        mac[3] = Integer.valueOf(macAddr.substring(9, 11), 16).byteValue();
        mac[4] = Integer.valueOf(macAddr.substring(12, 14), 16).byteValue();
        mac[5] = Integer.valueOf(macAddr.substring(15, 17), 16).byteValue();
        return mac;
    }
    
    /**
     * ��aa-bb-cc-dd-ee-ffת��Ϊaa:bb:cc:dd:ee:ff
     * @param macAddr -�ָ��mac��ַ
     * @return :�ָ��mac��ַ
     */
    public static String macAddr2LinuxFormat(String macAddr) {
        if (StringUtils.isEmpty(macAddr))
            return null;
        if (macAddr.length() != 17)
            return null;

        StringBuilder sb = new StringBuilder();
        sb.append(macAddr.substring(0, 2)).append(":");
        sb.append(macAddr.substring(3, 5)).append(":");
        sb.append(macAddr.substring(6, 8)).append(":");
        sb.append(macAddr.substring(9, 11)).append(":");
        sb.append(macAddr.substring(12, 14)).append(":");
        sb.append(macAddr.substring(15, 17));
        return sb.toString();
    }
    
}
