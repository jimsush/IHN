/*
 * $Id: SfObjectUtils.java, 2015-9-28 ����03:24:18 Administrator Exp $
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
 * Description: ͨ�õ�object(object,int,long��)������
 * </p>
 * 
 * @author 
 * created 2015-9-28 ����03:24:18
 * modified [who date description]
 * check [who date description]
 */
public class SfObjectUtils {
    
    /**
     * �Ƚ���������,��ȡ���ǲ�ͬ������
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
     * ��ȡ��������ԣ���key-value��ʽ�浽map��
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
     * ����һ����������Ե�����һ��������
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
     * ��¡���л�����
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
     * 2��Long�����Ƿ����
     * @param l1
     * @param l2
     * @return
     */
    public static boolean longEquals(Long l1, Long l2) {
        return l1 != null ? l1.equals(l2) : l2 == null;
    }

    /**
     * ���������Ƿ���ͬ
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
     * ����map�Ƿ���ͬ
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
        if(src==dest)//ͬһ������ֱ��return true
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
     * ��4��byte����һ��int, code[0]Ϊ��λ
     * @param code 4���ֽ�
     * @return ����ֵ
     */
    public static int byteArray2Int(byte[] code) {
        int v = code[3]; // ��λ
        v += (code[2] << 8) & 0xffff;
        v += code[1] << 16;
        v += code[0] << 24;
        return v;
    }
    
    /** 
     * ��4��byte����һ��int, code[0]Ϊ��λ
     * @param code �ֽ�����
     * @param fromIndex ��ʼת����λ��
     * @return ����ֵ
     */
    public static int byteArray2Int(byte[] code,int fromIndex) {
        int v = code[fromIndex+3]; // ��λ
        v += (code[fromIndex+2] << 8) & 0xffff;
        v += code[fromIndex+1] << 16;
        v += code[fromIndex] << 24;
        return v;
    }

    /** 
     * ��int����һ��byte[]
     * @param v ����ֵ
     * @return �ֽ�����
     */
    public static byte[] int2ByteArray(int v) {
        byte[] code = new byte[4];
        code[3] = (byte) v; // ��λ
        code[2] = (byte) ((v & 0xff00) >> 8);
        code[1] = (byte) ((v & 0xff0000) >> 16);
        code[0] = (byte) ((v & 0xff000000) >> 24); // ��λ
        return code;
    }
    
    /**
     * ��Դbyte[]�����ݿ�����Ŀ��byte[]�У�srcBytes->dstBytes
     * @param dstBytes
     * @param dstFrom  Ŀ��byte[]����ʼλ��,0��ʼ
     * @param srcBytes
     * @param srcFrom  Դbyte[]����ʼλ��,0��ʼ
     * @param len      ��Ҫ�����ĳ���
     */
    public static void copyBytes(byte[] dstBytes,int dstFrom,byte[] srcBytes,int srcFrom,int len){
        for(int i=0;i<len;i++){
            dstBytes[dstFrom+i]=srcBytes[srcFrom+i];
        }
    }

    /**
     * ��Դbyte������Ŀ��byte[]�У�srcByte->dstBytes
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
     * �õ�Integer��byte�����ʾ
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
     * ��һ��list�ֳɶ����list
     * @param srcList 
     * @param maxSize ��list�ĳߴ磬����4/64��
     * @return list��ÿ��Ԫ���Ǹ���list
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
     * Google Collection�ķ��������㹹��һ����д��List
     * 
     * @param <E> Ԫ�����
     * @param elements ����Ԫ��
     * @return �б�
     */
    public static <E> List<E> newArrayList(E... elements) {
        ArrayList<E> list = new ArrayList<E>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }
    
    /**
     * ��List�еĶ���ȡһ�����ԣ�����һ���µ�List��,�����NetworkElement�����deviceName����һ��List��
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
