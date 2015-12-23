/*
 * $Id: SfStringUtils.java, 2015-4-16 ����12:33:04  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.format;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Title: SfStringUtils
 * </p>
 * <p>
 * Description: �ַ���ת��������������
 * </p>
 * 
 * @author 
 * created 2015-4-16 ����12:33:04
 * modified [who date description]
 * check [who date description]
 */
public final class SfStringUtils {

    /**
     * ��longת��Ϊlength���ȵ�String��10���ƣ�������ǰ�油0��
     * 
     * @param value  �������ֵ
     * @param length �����ַ����ĳ���
     * @return ת���󳤶ȹ̶����ַ���
     */
    public static String toString(Long value, int length) {
        String strValue = value.toString();
        StringBuilder sb = new StringBuilder();
        if (strValue.length() < length)
            for (int i = 0; i < length - strValue.length(); i++)
                sb.append("0");
        sb.append(strValue);
        return sb.toString();

    }

    /**
     * ���ַ���ת��Ϊ��ֵ�ͣ�int,long,double,float,string��
     * @param <T> int,long,double,float,string
     * @param obj 
     * @param t int,long,double,float,string
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert2T(String obj,Class<T> t,Object defaultValue){
        if(Integer.class==t){
            try{
                return (T)Integer.valueOf(obj);
            }catch (Exception e) {
                return (T)defaultValue;
            }
        }else if(Long.class==t){
            try{
                return (T)Long.valueOf(obj);
            }catch (Exception e) {
                return (T)defaultValue;
            }
        }else if(Float.class==t){
            try{
                return (T)Float.valueOf(obj);
            }catch (Exception e) {
                return (T)defaultValue;
            }
        }else if(Double.class==t){
            try{
                return (T)Double.valueOf(obj);
            }catch (Exception e) {
                return (T)defaultValue;
            }
        }else if(String.class==t){
            return (T)obj;
        }
        return null;
    }
    
    /**
     * �ַ���ת������:��һ����ĸ��д
     * 
     * @param name
     *            ��Ҫת�����ַ���
     * @return String ת������ַ���
     * @author 
     */
    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * ���������ַ�����ͬ���ֵĳ��ȣ��������ң�
     * 
     * @param src
     * @param dst
     * @return
     */
    public static int getSameStrLen(String src, String dst) {
        if (StringUtils.isEmpty(src) || StringUtils.isEmpty(dst))
            return -1;
        int len = src.length() > dst.length() ? dst.length() : src.length();
        for (int i = 0; i < len; i++) {
            if (src.charAt(i) != dst.charAt(i)) {
                return i;
            }
        }
        return len;
    }
    
    /**
     * ��ȡһ��spring bean������
     * @param clazz bean����
     * @return ����ĸСд����������SimpleNameһ��
     */
    public static String getSpringBeanName(Class<?> clazz){
        String simName = clazz.getSimpleName();
        return simName.substring(0,1).toLowerCase()+simName.substring(1);
    }
    
    /**
     * ��byteת��Ϊstr
     * @param b
     * @param strLen �ַ������ȣ�����ǰ�����0
     * @return
     */
    public static String byte2String(byte b, int strLen){
        String str=Integer.toHexString(b);
        int zeroLen=strLen-str.length();
        if(zeroLen>0){
            StringBuilder sb=new StringBuilder();
            for(int i=0;i<zeroLen; i++)
                sb.append("0");
            sb.append(str);
            return sb.toString(); 
        }else{
            return str;
        }
    }
    
    public static String int2Len8HexString(int value) {
        String str = Integer.toHexString(value);
        if (str.length() == 8) {
            return str;
        } else {
            // ����8λ����0��ȫ
            StringBuilder sb = new StringBuilder();
            int zeroLen = 8 - str.length();
            for (int i = 0; i < zeroLen; i++)
                sb.append("0");
            sb.append(str);
            return sb.toString();
        }
    }

    public static List<Integer> getStringByList(String str, String separator) {
        List<Integer> list = new ArrayList<Integer>();
        if (StringUtils.isEmpty(str))
            return list;
        String[] split = str.split(separator);
        for (String string : split) {
            list.add(Integer.parseInt(string));
        }
        Collections.sort(list);
        return list;
    }

    /**
     * ��List�е����ݸ��ݷָ���ת�����ַ���
     * 
     * @param <T>
     *            List�����ݵ����ͣ�֧�ַ���
     * @param list
     *            Ҫת��������
     * @param separator
     *            ת�������ݼ�ķָ���
     * @return String ���ݷָ���ת���ɵ��ַ���
     */
    public static <T> String convertList2String(List<T> list, String separator) {
        String str = "";
        for (T t : list) {
            if (StringUtils.isEmpty(str))
                str = String.valueOf(t);
            else
                str = str + separator + String.valueOf(t);
        }
        return str;
    }

}
