/*
 * $Id: SfStringUtils.java, 2015-4-16 下午12:33:04  Exp $
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
 * Description: 字符串转换、操作工具类
 * </p>
 * 
 * @author 
 * created 2015-4-16 下午12:33:04
 * modified [who date description]
 * check [who date description]
 */
public final class SfStringUtils {

    /**
     * 将long转化为length长度的String（10进制，不够的前面补0）
     * 
     * @param value  传入的数值
     * @param length 返回字符串的长度
     * @return 转换后长度固定的字符串
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
     * 把字符串转换为数值型（int,long,double,float,string）
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
     * 字符串转换方法:第一个字母大写
     * 
     * @param name
     *            需要转换的字符串
     * @return String 转换后的字符串
     * @author 
     */
    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * 计算两个字符串相同部分的长度（从左至右）
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
     * 获取一个spring bean的名称
     * @param clazz bean类名
     * @return 首字母小写，其他与类SimpleName一致
     */
    public static String getSpringBeanName(Class<?> clazz){
        String simName = clazz.getSimpleName();
        return simName.substring(0,1).toLowerCase()+simName.substring(1);
    }
    
    /**
     * 将byte转换为str
     * @param b
     * @param strLen 字符串长度，不够前面填充0
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
            // 不是8位，用0补全
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
     * 将List中的数据根据分隔符转换成字符串
     * 
     * @param <T>
     *            List中数据的类型，支持泛型
     * @param list
     *            要转换的数据
     * @param separator
     *            转换后数据间的分隔符
     * @return String 根据分隔符转换成的字符串
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
