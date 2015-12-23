/*
 * $Id: SfObjectUtils.java, 2015-9-28 下午03:24:18 Administrator Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.format;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;

import com.sf.base.exception.ExceptionUtils;



/**
 * <p>
 * Title: SfObjectUtils
 * </p>
 * <p>
 * Description: 通用的object(object,int,long等)操作类
 * </p>
 * 
 * @author 
 * created 2015-9-28 下午03:24:18
 * modified [who date description]
 * check [who date description]
 */
public class SfObjectUtils {
    
    /**
     * 比较两个对象,获取他们不同的属性
     * @param thisObject
     * @param anotherObject
     * @return
     */
    public static Map<String, List<Object>> getDifferentProperities(Object thisObject,Object anotherObject) {
        if (thisObject == null || anotherObject == null)
            throw new NullPointerException();

        if (thisObject.getClass() != anotherObject.getClass())
            throw new IllegalArgumentException(
                    "the two objects are not the same class type");

        Map<String, List<Object>> differentProperities = new HashMap<String, List<Object>>();

        Class<?> clazz = thisObject.getClass();

        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();

            try {
                AccessibleObject.setAccessible(fields, true);

                for (Field field : fields) {
                    Object obj1 = field.get(thisObject);
                    Object obj2 = field.get(anotherObject);
                    if (!ObjectUtils.equals(obj1, obj2)) {
                        List<Object> list = new ArrayList<Object>();
                        list.add(obj1);
                        list.add(obj2);
                        differentProperities.put(field.getName(), list);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            clazz = clazz.getSuperclass();
        }

        return differentProperities;

    }


    /**
     * 获取对象的属性，以key-value方式存到map中
     * 
     * @param obj
     * @return
     */
    public static Map<String, String> getProperities(Object obj) {
        if (obj == null)
            throw new NullPointerException();
        final Map<String, String> attrs = new HashMap<String, String>();
        final Field[] fields = obj.getClass().getFields();
        for (Field field : fields) {
            try {
                Object value = field.get(obj);
                if (value == null)
                    attrs.put(field.getName(), "");
                else
                    attrs.put(field.getName(), value.toString());
            } catch (Exception ex) {
                System.out.println("occur exception:"+ExceptionUtils.getCommonExceptionInfo(ex));
                continue;
            }
        }
        return attrs;
    }

    /**
     * 拷贝一个对象的属性到另外一个对象上
     * @param <T>
     * @param dest
     * @param src
     */
    public static <T extends Object> void copyProperities(T dest, T src) {
        if (dest == null || src == null)
            throw new NullPointerException();

        if (dest.getClass() != src.getClass())
            throw new IllegalArgumentException(
                    "dest and src are not the same class type");

        Class<?> clazz = dest.getClass();

        while (clazz !=null) {
            Field[] fields = clazz.getDeclaredFields();
            try {
                AccessibleObject.setAccessible(fields, true);

                for (Field field : fields) {
                    Object obj1 = field.get(dest);
                    Object obj2 = field.get(src);
                    if (!ObjectUtils.equals(obj1, obj2)) {
                        field.set(dest, obj2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 克隆序列化对象
     * 
     * @param object
     * @return
     */
    public static Object clone(Object object) {
        ObjectInputStream in = null;
        Object obj = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(object);
            out.flush();
            out.close();
            in = new ObjectInputStream(new ByteArrayInputStream(bos
                    .toByteArray()));
            obj = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return obj;

    }

    /**
     * 2个Long对象是否相等
     * @param l1
     * @param l2
     * @return
     */
    public static boolean longEquals(Long l1, Long l2) {
        return l1 != null ? l1.equals(l2) : l2 == null;
    }

    /**
     * 两个集合是否相同
     * @param src
     * @param dest
     * @return
     */
    public static boolean collectionEquals(Collection<?> src,Collection<?> dest){
        if(null==src && null==dest)
            return true;
        else if(null==src && dest!=null)
            return false;
        else if(src!=null && null==dest)
            return false;
        if(src==dest)
            return true;
        return CollectionUtils.isEqualCollection(src, dest);
    }
    
    /**
     * 两个map是否相同
     * @param src
     * @param dest
     * @return
     */
    @SuppressWarnings("unchecked")
    public static boolean mapEquals(Map src,Map dest){
        if(null==src && null==dest)
            return true;
        else if(null==src && dest!=null)
            return false;
        else if(src!=null && null==dest)
            return false;
        if(src==dest)//同一个引用直接return true
            return true;
        if(src.size()!=dest.size()) 
            return false;
        Set<Map.Entry> srcEntries = src.entrySet();
        for(Map.Entry entry : srcEntries){
            Object key = entry.getKey();
            Object value=entry.getValue();
            Object destValue=dest.get(key);
            if(value!=null){
                if(!value.equals(destValue))
                    return false;
            }else{
                if(destValue!=null) //value==null
                    return false;
            }
        }

        return true;
    }
    
    /** 
     * 由4个byte构建一个int, code[0]为高位
     * @param code 4个字节
     * @return 整型值
     */
    public static int byteArray2Int(byte[] code) {
        int v = code[3]; // 低位
        v += (code[2] << 8) & 0xffff;
        v += code[1] << 16;
        v += code[0] << 24;
        return v;
    }
    
    /** 
     * 由4个byte构建一个int, code[0]为高位
     * @param code 字节数组
     * @param fromIndex 开始转换的位置
     * @return 整型值
     */
    public static int byteArray2Int(byte[] code,int fromIndex) {
        int v = code[fromIndex+3]; // 低位
        v += (code[fromIndex+2] << 8) & 0xffff;
        v += code[fromIndex+1] << 16;
        v += code[fromIndex] << 24;
        return v;
    }

    /** 
     * 由int构建一个byte[]
     * @param v 整型值
     * @return 字节数组
     */
    public static byte[] int2ByteArray(int v) {
        byte[] code = new byte[4];
        code[3] = (byte) v; // 低位
        code[2] = (byte) ((v & 0xff00) >> 8);
        code[1] = (byte) ((v & 0xff0000) >> 16);
        code[0] = (byte) ((v & 0xff000000) >> 24); // 高位
        return code;
    }
    
    /**
     * 把源byte[]的内容拷贝到目标byte[]中：srcBytes->dstBytes
     * @param dstBytes
     * @param dstFrom  目标byte[]的起始位置,0开始
     * @param srcBytes
     * @param srcFrom  源byte[]的起始位置,0开始
     * @param len      需要拷贝的长度
     */
    public static void copyBytes(byte[] dstBytes,int dstFrom,byte[] srcBytes,int srcFrom,int len){
        for(int i=0;i<len;i++){
            dstBytes[dstFrom+i]=srcBytes[srcFrom+i];
        }
    }

    /**
     * 把源byte拷贝到目标byte[]中：srcByte->dstBytes
     * @param dstBytes
     * @param dstFrom
     * @param srcByte
     * @param len
     */
    public static void copyBytes(byte[] dstBytes,int dstFrom,byte srcByte,int len){
        for(int i=0;i<len;i++){
            dstBytes[dstFrom+i]=srcByte;
        }
    }
    
    /**
     * 得到Integer的byte数组表示
     * @param intValue
     * @param length
     * @return
     */
    public static byte[] getBytesByInt(int intValue, int length) {
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            int temp = 1;
            for (int j = 0; j < i; j++)
                temp *= 256;
            bytes[i] = Integer.valueOf((intValue / temp) % 256).byteValue();
        }
        return bytes;
    }

    /**
     * 把一个list分成多个子list
     * @param srcList 
     * @param maxSize 子list的尺寸，比如4/64等
     * @return list中每个元素是个子list
     */
    public static <T> List<List<T>> split2MultiSubList(List<T> srcList,int maxSize){
        if(CollectionUtils.isEmpty(srcList) || maxSize==0)
            return null;
        int size=srcList.size();
        if(size<=maxSize){
            List<List<T>> result=new ArrayList<List<T>>();
            result.add(srcList);
            return result;
        }
        
        int num=size/maxSize + 1;
        List<List<T>> result=new ArrayList<List<T>>(num);
        
        int j=0;
        List<T> element=null;
        for(T obj : srcList){
            if(j==0){
                element=new ArrayList<T>();
                result.add(element);
            }
            element.add(obj);
            j++;
            
            if(j==maxSize){
                j=0;
            }
        }
        return result;
    }
    
    /**
     * Google Collection的方法，方便构造一个可写的List
     * 
     * @param <E> 元素类别
     * @param elements 数组元素
     * @return 列表
     */
    public static <E> List<E> newArrayList(E... elements) {
        ArrayList<E> list = new ArrayList<E>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }
    
    /**
     * 从List中的对象取一个属性，放入一个新的List中,比如把NetworkElement对象的deviceName放入一个List中
     * @param <T>
     * @param <M>
     * @param objectList 
     * @param propertyName
     * @param m
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T, M> List<M> getPropertyList(List<T> objectList, String propertyName, Class<M> m) {
        List<M> mlist = new ArrayList<M>();
        for (T t : objectList) {
            try {
                mlist.add((M) PropertyUtils.getProperty(t, propertyName));
            } catch (Exception e) {
                System.out.println(ExceptionUtils.getCommonExceptionInfo(e));
            }
        }
        return mlist;
    }

}
