/*
 * $Id: SnmpOidUtil.java, 2015-11-12 上午11:40:47 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.net.snmp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Title: SnmpOidUtil
 * </p>
 * <p>
 * OID相关的工具类
 * </p>
 * 
 * @author pl
 * created 2015-11-16 上午10:15:42
 * modified [who date description]
 * check [who date description]
 */
public class SnmpOidUtil {
    
    /**
     * 将int数组转换为以"."隔开的字符串形式
     * 
     * @param indexes
     *            int数组
     * @return 字符串
     */
    public static String getIndexes(int... indexes) {
        StringBuffer buf = new StringBuffer();
        for (int index : indexes) {
            buf.append(".");
            buf.append(index);
        }
        return buf.toString();
    }    

    /**
     * 将.1.2转化为数组int[]={1,2}
     * 
     * @param indexStr
     * @return
     */
    public static int[] getIndexByString(String indexStr) {
        String[] oids = indexStr.split("\\.");
        int length = oids.length;
        int[] index = new int[length - 1];
        for (int i = 0; i < length - 1; i++) {
            index[i] = Integer.parseInt(oids[i + 1]);
        }
        return index;
    }

    /**
     * 将OID后面的特定长度截取为索引数组
     * @param oid OID
     * @param length 长度
     * @return
     */
    public static int[] getIndexByOid(String oid, int length) {
        int[] index = new int[length];
        String[] oids = oid.split("\\.");
        int oidsLength = oids.length;
        for (int i = 0; i < length; i++) {
            String str = oids[oidsLength - length + i];
            index[i] = Integer.parseInt(str);
        }
        return index;
    }    

    /**
     * 根据oid得到entry
     * 
     * @param oid
     * @param length
     *            标识oid的最后有几个index
     * @return
     */
    public static String getEntryByOid(String oid, int length) {
        for (int i = 0; i < length; i++) {
            int index = oid.lastIndexOf(".");
            oid = oid.substring(0, index);
        }
        return oid;
    }

    /**
     * 判断前者是否为OID加索引
     * @param oidWithIndex
     * @param oid
     * @return
     */
    public static boolean isMibNodeByOid(String oidWithIndex, String oid) {
        if (StringUtils.isEmpty(oid))
            return false;
        if (oidWithIndex.startsWith(oid)) {
            int length = oid.length();
            if (oidWithIndex.substring(length).startsWith("."))
                return true;
        }
        return false;
    }
    
    /**
     * 对OID根据索引进行过滤
     * @param allIndexs
     * @param index
     * @return
     */
    public static List<String> filterIndex(List<String> allIndexs, int... index) {
        List<String> list = new ArrayList<String>();
        String temp = getIndexes(index);
        for (String string : allIndexs) {
            if (isMibNodeByOid(string, temp))
                list.add(string);
        }
        return list;
    }
    
    /**
     * 将多个OID使用逗号隔开
     * @param oids
     * @return
     */
    public static String getOidDisplayName(String[] oids){
        if(oids==null || oids.length==0)
            return "";
        if(oids.length==1)
            return oids[0];
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<oids.length;i++){
            if(i==0)
                sb.append(oids[i]);
            else
                sb.append(","+oids[i]);
        }
        return sb.toString();
    }    
}
