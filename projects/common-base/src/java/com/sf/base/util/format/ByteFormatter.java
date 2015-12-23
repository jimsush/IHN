/*
 * $Id: ByteFormatter.java, 2015-11-12 上午11:40:47 sufeng Exp $
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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Title: ByteFormatter
 * </p>
 * <p>
 * Byte处理工具类，包括字节转换、拼装
 * </p>
 * 
 * @author pl
 * created 2015-2-20 上午09:57:29
 * modified [who date description]
 * check [who date description]
 */
public class ByteFormatter {

    /**
     * 将字节倒数第index位上的1转化为0
     * 
     * @param origin
     *            字节
     * @param index
     *            序号
     * @return 字节
     */
    public static byte clearHigh(byte origin, int index) {
        switch (index) {
        case 1:
            origin = (byte) (origin & 0xFE);
            break;
        case 2:
            origin = (byte) (origin & 0xFD);
            break;
        case 3:
            origin = (byte) (origin & 0xFB);
            break;
        case 4:
            origin = (byte) (origin & 0xF7);
            break;
        case 5:
            origin = (byte) (origin & 0xEF);
            break;
        case 6:
            origin = (byte) (origin & 0xDF);
            break;
        case 7:
            origin = (byte) (origin & 0xBF);
            break;
        case 8:
            origin = (byte) (origin & 0x7F);
            break;
        default:
            origin=0;
            break;
        }
        return origin;
    }

    /**
     * 将两个字节按照规则组成一个字节
     * 
     * @param high
     *            高位字节
     * @param low
     *            低位字节
     * @return 字节
     */
    public static byte compileTwoByteOne(byte high, byte low) {
        if (high < 0) {
            high = 0;
        }
        int resu = high * 16 + low;
        return (byte) resu;
    }

    /**
     * 将两个整数按照规则组成一个整数
     * 
     * @param high
     *            高位整数
     * @param low
     *            低位整数
     * @return int
     */
    public static int compileTwoIntOne(int high, int low) {
        if (high < 0) {
            high = 0;
        }
        int resu = high * 16 + low;
        return resu;
    }

    /**
     * 将ASCII字节转化为字节
     * 
     * @param src ascii
     * @return 字节
     */
    public static byte convertASCIIByteToByte(byte src) {
        byte resu = 0;
        if (src == 0x31) {
            resu = 1;
        } else if (src == 0x32) {
            resu = 2;
        } else if (src == 0x33) {
            resu = 3;
        } else if (src == 0x34) {
            resu = 4;
        } else if (src == 0x35) {
            resu = 5;
        } else if (src == 0x36) {
            resu = 6;
        } else if (src == 0x37) {
            resu = 7;
        } else if (src == 0x38) {
            resu = 8;
        } else if (src == 0x39) {
            resu = 9;
        } else if (src == 0x41) {
            resu = 0x0a;
        } else if (src == 0x42) {
            resu = 0x0b;
        } else if (src == 0x43) {
            resu = 0x0c;
        } else if (src == 0x44) {
            resu = 0x0d;
        } else if (src == 0x45) {
            resu = 0x0e;
        } else if (src == 0x46) {
            resu = 0x0f;
        }
        return resu;
    }

    /**
     * 将字节转化为字符串
     * 
     * @param value
     *            字节
     * @return 字符串
     */
    public static String convertByteToString(byte value) {
        String resu = null;
        if (value >= 0 && value <= 9) {
            resu = Byte.toString(value);
        } else {
            switch (value) {
            case 10:
                resu = "A";
                break;
            case 11:
                resu = "B";
                break;
            case 12:
                resu = "C";
                break;
            case 13:
                resu = "D";
                break;
            case 14:
                resu = "E";
                break;
            case 15:
                resu = "F";
                break;
            default:
                break;
            }
        }
        return resu;
    }

    /**
     * 将16进制转化为ASCII形式
     * 
     * @param array
     *            字节数组
     * @return 字符串
     */
    public static String convertHexToASCII(byte[] array) {
        if (array == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (byte element : array) {
            buffer.append(convertByteToString(element));
        }
        return buffer.toString();
    }

    /**
     * 将ASCII的字符串形式转化为16进制形式
     * 
     * @param src
     *            字符串
     * @return 字节数组
     */
    public static byte[] convertStringInASCIItoHexArray(String src) {
        String tmp = src.toUpperCase();
        tmp = tmp.replaceAll("-", "");
        byte[] array = tmp.getBytes();
        byte[] resu = new byte[(array.length + 1) / 2];
        for (int i = 0; i < array.length; i++) {
            if ((i % 2) == 0) {
                resu[i / 2] = convertASCIIByteToByte(array[i]);
            } else {
                resu[i / 2] = compileTwoByteOne(resu[i / 2], convertASCIIByteToByte(array[i]));
            }
        }
        return resu;
    }

    /**
     * 将16进制的字符串形式转化为ASCII形式
     * 
     * @param array
     *            字节数组
     * @return 字符串
     */
    public static String convertStringInHextoASCII(byte[] array) {
        if (array == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        int index = 0;
        for (byte element : array) {
            if (index == 0)
                buffer.append(getHexStringFromByte(element));
            else
                buffer.append("-").append(getHexStringFromByte(element));
            index++;
        }
        return buffer.toString();
    }

    /**
     * 复制字节数组
     * 
     * @param origin
     *            源数组
     * @param start
     *            开始位置
     * @param length
     *            复制长度
     * @param target
     *            目标位置
     * @param index
     *            目标数组开始序号
     * @return
     */
    public static byte[] scopyByteArray(byte[] origin, int start, int length, byte[] target, int index) {
        if (index < 0) {
            index = 0;
        }
        int i = 0;
        while (i < length && origin.length > (start + i) && target.length > (index + i)) {
            target[index + i] = origin[start + i];
            i++;
        }
        return target;
    }    

    /**
     * 将4个字节的字节数组转化为int形式
     * 
     * @param origin
     *            字节数组
     * @return 字符串
     */
    public static int from4BytesToInt(byte[] origin) {
        if (origin==null || origin.length != 4) {
            throw new IllegalArgumentException("Must Be 4 Bytes");
        }

        int resu = 0;
        if (origin != null) {
            for (int i = 0; i < origin.length; i++) {
                short temp = getShortFromOneByte((byte) (origin[i] & 0xff));
                resu += temp << 8 * (3 - i);
            }
        }
        return resu;
    }

    /**
     * 将4个字节的字节数组转化为IP地址的字符串形式
     * 
     * @param origin
     *            字节数组
     * @return 字符串
     */
    public static String from4BytesToIpAddrss(byte[] origin) {
        if (origin.length != 4) {
            throw new IllegalArgumentException("Must Be 4 Bytes");
        }
        short tmp = getShortFromOneByte((byte) (origin[0] & 0xff));
        StringBuffer resu = new StringBuffer();
        resu.append(Short.toString(tmp));
        for (int i = 1; i < 4; i++) {
            tmp = getShortFromOneByte((byte) (origin[i] & 0xff));
            resu.append(".");
            resu.append(Short.toString(tmp));
        }
        
        return resu.toString();
    }

    /**
     * 根据IP地址获取bytes
     * @param ip ip地址
     * @return
     */
    public static byte[] getBytesByIp(String ip){
        String[] ipStr=ip.split("\\.");
        byte[] bytes=new byte[4];
        int i=0;
        for(String s:ipStr){
            Short value=Short.parseShort(s);
            bytes[i]=value.byteValue();
            i++;
        }
        return bytes;
    }
    /**
     * 将6个字节的字节数组转化为MAC地址的字符串形式.添加分隔符,例如00-1A-69-12-34-56
     * 
     * @param origin mac字节数组
     * @return
     */
    public static String from6BytesToMac(byte[] origin) {
        return from6BytesToMac(origin, false);
    }

    /**
     * 将6个字节的字节数组转化为MAC地址的字符串形式
     * 
     * @param origin mac字节数组
     * @param isPlain
     *            是否添加分隔符:
     * @return
     */
    public static String from6BytesToMac(byte[] origin, boolean isPlain) {
        if (origin.length != 6) {
            throw new IllegalArgumentException("Must Be 6 Bytes");
        }

        String tmp = getHexStringFromByte((byte) (origin[0] & 0xff));
        StringBuffer resu = new StringBuffer();
        resu.append(tmp);
        for (int i = 1; i < 6; i++) {
            tmp = getHexStringFromByte((byte) (origin[i] & 0xff));
            if (!isPlain) {
                resu.append("-");
            }
            resu.append(tmp);
        }
        return resu.toString();
    }

    public static byte[] getBytesFromInt(int number) {
        byte[] bytes = new byte[4];
        for (int i = bytes.length - 1; i > -1; i--) {
            bytes[i] = Integer.valueOf(number & 0xff).byteValue();
            number = number >> 8;
        }
        return bytes;
    }

    /**
     * 将整数数组形式的Mac地址转化为字符串形式，由"-"隔开
     * 
     * @param array
     *            整数数组
     * @return 字符串
     */
    public static String generateMACfromIntArray(int array[]) {
        StringBuffer buf = new StringBuffer();
        int i = 0;
        for (int item : array) {
            if (i++ != 0)
                buf.append("-");
            buf.append(getHexStringFromByte((byte) item));
        }
        return buf.toString();
    }

    public static long getBitMap(int[] branchNo) {
        long res = 0x00;
        if (branchNo == null) {
            return res;
        }

        long init = 0x01;
        for (int bNo : branchNo) {
            res = res | init << (64 - bNo);
        }
        
        return res;
    }

    public static byte[] getBytes(long l) {
        byte[] bs = Long.toString(l).getBytes();
        return bs;
    }

    public static String getHexStr(long l) {
        String hexString = Long.toHexString(l);
        return hexString;
    }

    /**
     * 将字节转化为16进制的字符串表示
     * 
     * @param value
     *            字节
     * @return 字符串
     */
    public static String getHexStringFromByte(byte value) {
        String resu = null;
        byte high = (byte) ((value >> 4) & 0x0f);
        String highStr = convertByteToString(high);
        byte low = (byte) (value & 0x0f);
        String lowStr = convertByteToString(low);
        if (highStr != null && lowStr != null) {
            resu = highStr + lowStr;
        }
        return resu;
    }

    /**
     * 将2字节数组转为16进制表示的字符串形式,其中以0为前缀
     * 
     * @param array 字节数组
     * @return 字符串
     */
    public static String getHexStringFromByte2(byte[] array) {
        String resu = "";
        byte value = (byte) ((array[0] >> 4) & 0x000f);
        String highestStr = convertByteToString(value);
        value = (byte) (array[0] & 0x000f);
        String highStr = convertByteToString(value);
        value = (byte) ((array[1] >> 4) & 0x000f);
        String lowStr = convertByteToString(value);
        value = (byte) (array[1] & 0x000f);
        String lowestStr = convertByteToString(value);

        if (highestStr != null && highStr != null && lowStr != null && lowestStr != null) {
            resu = highestStr + highStr + lowStr + lowestStr;
        }
        return resu;
    }

    /**
     * 将4字节数组转为16进制表示的字符串形式,其中以0为前缀
     * 
     * @param array 字节数组
     * @return 字符串
     */
    public static String getHexStringFromByte4(byte[] array) {
        String resu = "";
        if (array.length < 2) {
            return resu;
        }
        byte value = (byte) ((array[2] >> 4) & 0x000f);
        String highestStr = convertByteToString(value);
        value = (byte) (array[2] & 0x000f);
        String highStr = convertByteToString(value);
        value = (byte) ((array[3] >> 4) & 0x000f);
        String lowStr = convertByteToString(value);
        value = (byte) (array[3] & 0x000f);
        String lowestStr = convertByteToString(value);

        if (highestStr != null && highStr != null && lowStr != null && lowestStr != null) {
            resu = highestStr + highStr + lowStr + lowestStr;
        }
        return resu;
    }

    /**
     * 将字节数组转化为16进制的字符串表示
     * 
     * @param array
     *            字节数组
     * @return 字符串
     */
    public static String getHexStringFromByteArray(byte[] array) {
        StringBuffer buf = new StringBuffer();
        for (byte element : array) {
            buf.append(" ");
            buf.append(getHexStringFromByte(element));
        }
        return buf.toString();
    }

    /**
     * 将字节数组转化为16进制的连续字符串表示
     * 
     * @param array
     *            字节数组
     * @return 字符串
     */
    public static String getHexStringFromByteArrayNoBlank(byte[] array) {
        StringBuffer buf = new StringBuffer();
        for (byte element : array) {
            buf.append(getHexStringFromByte(element));
        }
        return buf.toString();
    }

    /**
     * 将整数转为16进制表示的字符串形式,补足八位,以0为前缀
     * 
     * @param value 整形
     * @return 字符串
     */
    public static String getHexStringWith0PrefixInt2(int value, int length) {
        String hexString = Integer.toHexString(value);
        // 补足八位
        int zero = length - hexString.length();
        if (zero > 0)
            for (int i = 0; i < zero; i++) {
                hexString = "0" + hexString;
            }
        hexString = "0x" + hexString.toUpperCase();
        return hexString;
    }

    /**
     * 将Long型数转为16进制表示的字符串形式,其中以0为前缀
     * 
     * @param value long型
     * @return 字符串
     */
    public static String getHexStringWith0PrefixLong2(long value) {
        String hexString = Long.toHexString(value);
        hexString = "0x" + hexString;
        return hexString;
    }

    /**
     * 将大端规则的字节数组转化为整数
     * 
     * @param array 字节数组
     * @return int
     */
    public static int getIntFromByteArrayBigEnd(byte[] array) {
        int resu = 0;
        if (array.length <= 4) {
            for (int i = array.length - 1; i >= 0; i--) {
                resu = resu << 8;
                if (array[i] >= 0) {
                    resu = resu + (array[i]&0xff);
                } else {
                    resu = resu + (256 + array[i]);
                }
            }
        }
        return resu;
    }

    /**
     * 将小端规则的字节数组转化为整数
     * 
     * @param array 字节数组
     * @return int int值
     */
    public static int getIntFromByteArraySmallEnd(byte... array) {
        int resu = 0;
        if (array.length <= 4) {
            for (byte element : array) {
                resu = resu << 8;
                if (element >= 0) {
                    resu = resu + element;
                } else {
                    resu = resu + (256 + element);
                }
            }
        }
        return resu;
    }  
    
    
    public static long getLongFromByteArraySmallEnd(byte... array) {
        long resu = 0;
        if (array.length <= 4) {
            for (byte element : array) {
                resu = resu << 8;
                if (element >= 0) {
                    resu = resu + element;
                } else {
                    resu = resu + (256 + element);
                }
            }
        }
        return resu;
    }   

    /**
     * 将字节转换为整数
     * 
     * @param value
     *            字节
     * @return int int
     */
    public static int getIntFromOneByte(byte value) {
        int resu = 0;
        if (value >= 0) {
            resu = value;
        } else {
            resu = 256 + value;
        }
        return resu;
    }

    /**
     * 将字节转化为short型
     * 
     * @param low
     *            字节
     * @return short
     */
    public static short getShortFromOneByte(byte low) {
        short resu = 0;
        if (low >= 0) {
            resu = low;
        } else {
            resu = (short) (256 + low);
        }
        return resu;
    }

    /**
     * 判断字节的倒数第index位上的字节是否为1
     * 
     * @param value
     *            字节
     * @param index
     *            序号
     * @return 是否为1
     */
    public static boolean isSet(byte value, int index) {
        switch (index) {
        case 1:
            return (value & 0x01) != 0;
        case 2:
            return (value & 0x02) != 0;
        case 3:
            return (value & 0x04) != 0;
        case 4:
            return (value & 0x08) != 0;
        case 5:
            return (value & 0x10) != 0;
        case 6:
            return (value & 0x20) != 0;
        case 7:
            return (value & 0x40) != 0;
        case 8:
            return (value & 0x80) != 0;
        default:
            return false;
        }
    }

    /**
     * 判断int的倒数第index位上的字节是否为1
     * 
     * @param value
     *            字节
     * @param index
     *            序号
     * @return 是否为1
     */
    public static boolean isSetInt(int value, int index) {
        switch (index) {
        case 1:
            return (value & 0x01) != 0;
        case 2:
            return (value & 0x02) != 0;
        case 3:
            return (value & 0x04) != 0;
        case 4:
            return (value & 0x08) != 0;
        case 5:
            return (value & 0x10) != 0;
        case 6:
            return (value & 0x20) != 0;
        case 7:
            return (value & 0x40) != 0;
        case 8:
            return (value & 0x80) != 0;
        case 9:
            return (value & 0x100) != 0;
        case 10:
            return (value & 0x200) != 0;
        case 11:
            return (value & 0x400) != 0;
        case 12:
            return (value & 0x800) != 0;
        case 13:
            return (value & 0x1000) != 0;
        case 14:
            return (value & 0x2000) != 0;
        case 15:
            return (value & 0x4000) != 0;
        case 16:
            return (value & 0x8000) != 0;
        case 17:
            return (value & 0x10000) != 0;
        case 18:
            return (value & 0x20000) != 0;
        case 19:
            return (value & 0x40000) != 0;
        case 20:
            return (value & 0x80000) != 0;
        case 21:
            return (value & 0x100000) != 0;
        case 22:
            return (value & 0x200000) != 0;
        case 23:
            return (value & 0x400000) != 0;
        case 24:
            return (value & 0x800000) != 0;
        case 25:
            return (value & 0x1000000) != 0;
        case 26:
            return (value & 0x2000000) != 0;
        case 27:
            return (value & 0x4000000) != 0;
        case 28:
            return (value & 0x8000000) != 0;
        case 29:
            return (value & 0x10000000) != 0;
        case 30:
            return (value & 0x20000000) != 0;
        case 31:
            return (value & 0x40000000) != 0;
        case 32:
            return (value & 0x80000000) != 0;
        default:
            return false;
        }
    }

    /**
     * 将字符串形式的Mac地址转化为整数数组
     * 
     * @param mac
     *            Mac地址
     * @return int数组
     */
    public static int[] parseMACinStringtoArray(String mac) {
        List<Integer> intList = new ArrayList<Integer>();
        while (mac.length() >= 2) {
            String tmp = mac.substring(0, 2);
            mac = mac.substring(2);
            int tmpInt = Integer.parseInt(tmp, 16);
            if (tmpInt < 0) {
                tmp += 256;
            }
            intList.add(Integer.parseInt(tmp, 16));
        }
        int resu[] = new int[intList.size()];
        for (int i = 0; i < intList.size(); i++) {
            resu[i] = intList.get(i).intValue();
        }
        return resu;
    }

    /**
     * 将字节倒数第index位设置为1
     * 
     * @param origin
     * @param index
     * @return 字节
     */
    public static byte setHigh(byte origin, int index) {
        switch (index) {
        case 1:
            origin = (byte) (origin | 0x01);
            break;
        case 2:
            origin = (byte) (origin | 0x02);
            break;
        case 3:
            origin = (byte) (origin | 0x04);
            break;
        case 4:
            origin = (byte) (origin | 0x08);
            break;
        case 5:
            origin = (byte) (origin | 0x10);
            break;
        case 6:
            origin = (byte) (origin | 0x20);
            break;
        case 7:
            origin = (byte) (origin | 0x40);
            break;
        case 8:
            origin = (byte) (origin | 0x80);
            break;
        }
        return origin;
    }

    public static byte setLow(byte origin, int index) {
        switch (index) {
        case 1:
            origin = (byte) (origin | 0x80);
            break;
        case 2:
            origin = (byte) (origin | 0x40);
            break;
        case 3:
            origin = (byte) (origin | 0x20);
            break;
        case 4:
            origin = (byte) (origin | 0x10);
            break;
        case 5:
            origin = (byte) (origin | 0x08);
            break;
        case 6:
            origin = (byte) (origin | 0x04);
            break;
        case 7:
            origin = (byte) (origin | 0x02);
            break;
        case 8:
            origin = (byte) (origin | 0x01);
            break;
        }
        return origin;
    }

    /**
     * 将字节倒数第index位设置为1
     * 
     * @param origin
     * @param index
     * @return 字节
     */
    public static int setHighInt(int origin, int index) {
        switch (index) {
        case 1:
            origin = (origin | 0x01);
            break;
        case 2:
            origin = (origin | 0x02);
            break;
        case 3:
            origin = (origin | 0x04);
            break;
        case 4:
            origin = (origin | 0x08);
            break;
        case 5:
            origin = (origin | 0x10);
            break;
        case 6:
            origin = (origin | 0x20);
            break;
        case 7:
            origin = (origin | 0x40);
            break;
        case 8:
            origin = (origin | 0x80);
            break;
        case 9:
            origin = (origin | 0x100);
            break;
        case 10:
            origin = (origin | 0x200);
            break;
        case 11:
            origin = (origin | 0x400);
            break;
        case 12:
            origin = (origin | 0x800);
            break;
        case 13:
            origin = (origin | 0x1000);
            break;
        case 14:
            origin = (origin | 0x2000);
            break;
        case 15:
            origin = (origin | 0x4000);
            break;
        case 16:
            origin = (origin | 0x8000);
            break;
        case 17:
            origin = (origin | 0x10000);
            break;
        case 18:
            origin = (origin | 0x20000);
            break;
        case 19:
            origin = (origin | 0x40000);
            break;
        case 20:
            origin = (origin | 0x80000);
            break;
        case 21:
            origin = (origin | 0x100000);
            break;
        case 22:
            origin = (origin | 0x200000);
            break;
        case 23:
            origin = (origin | 0x400000);
            break;
        case 24:
            origin = (origin | 0x800000);
            break;
        case 25:
            origin = (origin | 0x1000000);
            break;
        case 26:
            origin = (origin | 0x2000000);
            break;
        case 27:
            origin = (origin | 0x4000000);
            break;
        case 28:
            origin = (origin | 0x8000000);
            break;
        case 29:
            origin = (origin | 0x10000000);
            break;
        case 30:
            origin = (origin | 0x20000000);
            break;
        case 31:
            origin = (origin | 0x40000000);
            break;
        case 32:
            origin = (origin | 0x80000000);
            break;
        }
        return origin;
    }

    public byte[] getByteArray(long longValue) {
        byte[] bb = Long.toString(longValue).getBytes();
        return bb;
    }

    public static String getStringByHexString(String str, int startIndex) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isEmpty(str))
            return sb.toString();
        // byte[] bytes = str.substring(startIndex).getBytes();会导致错位
        byte[] bytes = ArrayUtils.subarray(str.getBytes(), startIndex, str.getBytes().length);
        for (byte temp : bytes) {
            char tempChar = (char) (temp & 0xff);
            sb.append(tempChar);
        }
        return sb.toString();
    }

    public static String getStringByHexString(String str, int startIndex, int endIndex) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isEmpty(str))
            return sb.toString();
        byte[] bytes = ArrayUtils.subarray(str.getBytes(), startIndex, endIndex);
        for (byte temp : bytes) {
            char tempChar = (char) (temp & 0xff);
            sb.append(tempChar);
        }
        return sb.toString();
    }

    /**
     * 根据组播IP得到组播MAC
     * 
     * @param ip
     * @return
     */
    public static String getMultiMacByIp(String ip) {
        int[] macInt = new int[6];
        int[] values = IpAddrFormatter.getValues(ip);
        macInt[0] = 1;
        macInt[1] = 0;
        macInt[2] = 94;
        macInt[3] = values[1] % 128;
        macInt[4] = values[2];
        macInt[5] = values[3];
        return generateMACfromIntArray(macInt);
    }
    /**以下为新的方法 20100714 by pl**/
    
    public static int getIntByBytes(byte[] array,int startIndex,int length){
    	byte[] bytes=new byte[length];
    	for(int i=0;i<length;i++){
    		bytes[i]=array[startIndex+i];
    	}
    	return getIntFromByteArraySmallEnd(bytes);
    }
    
    public static long getLong(byte[] array){
        return getLongFromByteArraySmallEnd(array);
    }
    
    public static byte[] getBytesByInt(int value,int length){
    	byte[] bytes=new byte[length];
    	for(int i=length-1;i>=0;i--){
    		bytes[i]=(byte)(value&0xff);
    		value=value>>8;
    	}
    	return bytes;
    }
    
    public static byte[] getBytesByLong(long value,int length){
        byte[] bytes=new byte[length];
        for(int i=length-1;i>=0;i--){
            bytes[i]=(byte)(value&0xff);
            value=value>>8;
        }
        return bytes;
    }
    
    public static byte[] getBytesByString(String str, int length) {
		byte[] bytes = new byte[length];
        if (str == null)
            return bytes;
        byte[] strBytes = str.getBytes();
        copyBytes(bytes, 0, strBytes);
		return bytes;
	}
    
    public static String getStringByBytes(byte[] bytes){
        int length = bytes.length;
        int index;
        for (index = 0; index < length; index++) {
            if (bytes[index] == 0)
                break;
        }
        return new String(getSubBytes(bytes, 0, index));
    }
    
    public static byte[] getBytesByMac(String mac) {
        byte[] resu = new byte[6];
        if (StringUtils.isEmpty(mac) || mac.length() < 12)
            return resu;
        mac = mac.trim();
        mac = mac.replaceAll("-", "");
        mac = mac.replaceAll(":", "");
        Pattern macPattern = Pattern.compile("([0-9A-Fa-f]{2}){6}");
        Matcher macMatcher = macPattern.matcher(mac);
        if (macMatcher.matches()) {
            for (int i = 0; i < 6; i++) {
                resu[i] = (byte)Integer.parseInt(mac.substring(2 * i, 2 * i + 2), 16);
            }
            return resu;
        }
        return resu;
    }
    
    public static byte[] getSubBytes(byte[] array,int startIndex,int length){
    	byte[] bytes=new byte[length];
    	for(int i=0;i<length;i++){
    		bytes[i]=array[startIndex+i];
    	}
    	return bytes;
    }
    
    public static void copyBytes(byte[] target, int targetStartIndex,byte[] source) {
        int length = source.length;
		for (int i = 0; i < length; i++) {
			target[targetStartIndex + i] = source[i];
		}
    }
    
    public static byte[] appendBytes(byte[] bytes1,byte[] tytes2){
        byte[] contents=new byte[bytes1.length+tytes2.length];
        ByteFormatter.copyBytes(contents, 0, bytes1);
        ByteFormatter.copyBytes(contents, bytes1.length, tytes2);
        return contents;
    }
    
    public static double byteArrayToDouble(byte b[]) {
        long l;

        l = b[0];
        l &= 0xff;
        l |= ((long) b[1] << 8);
        l &= 0xffff;
        l |= ((long) b[2] << 16);
        l &= 0xffffff;
        l |= ((long) b[3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[4] << 32);
        l &= 0xffffffffffl;

        l |= ((long) b[5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[7] << 56);
        return Double.longBitsToDouble(l);
    }
    /**
     * convert float to byte array (of size 4)
     * @param f
     * @return
     */
    public static byte[] floatToByteArray(float f) {
        int i = Float.floatToRawIntBits(f);
        return getBytesByInt(i, 4);
    }
    
    /**
     * 主要处理Byte[]和byte[]这种恶心的事情
     * @param byteList
     * @param newBytes
     */
    public static void addAll(List<Byte> byteList, byte... newBytes) {
        Byte[] bytes = ArrayUtils.toObject(newBytes);
        CollectionUtils.addAll(byteList, bytes);
    }
    
    /**
     * 处理浮点数
     * @param byte6
     * @return
     */
    public static float getFloatFrom6Bytes(byte[] byte6){
    	if(byte6==null || byte6.length!=6)
    		return 0;
    	byte degress=byte6[1];
    	byte[] bs = ByteFormatter.getSubBytes(byte6, 2, 4);
    	long l=ByteFormatter.getLong(bs);
    	long factor=1;
    	for(int i=0;i<degress;i++){
    		factor*=10;
    	}
    	return (float)l/factor;
    }
    
    /**
     * 处理10字节的浮点数
     * @param byte6
     * @return
     */
    public static double getDoubleFrom10Bytes(byte[] byte10){
    	if(byte10==null || byte10.length!=10)
    		return 0;
    	byte degress=byte10[1];
    	byte[] bs = ByteFormatter.getSubBytes(byte10, 2, 8);
    	long l=ByteFormatter.getLong(bs);
    	long factor=1;
    	for(int i=0;i<degress;i++){
    		factor*=10;
    	}
    	return (double)l/factor;
    }

}
