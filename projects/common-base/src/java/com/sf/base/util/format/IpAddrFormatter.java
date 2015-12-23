/*
 * $Id: IpAddrFormatter.java, 2015-4-29 下午03:12:45  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.format;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import sun.net.util.IPAddressUtil;

/**
 * <p>
 * Title: IpAddrFormatter
 * </p>
 * <p>
 * Description: IP地址转换工具类
 * </p>
 * 
 * @author 
 * created 2015-4-29 下午03:12:45
 * modified [who date description]
 * check [who date description]
 */
public final class IpAddrFormatter {

    final static int INADDRSZ = 4;
    
    private IpAddrFormatter() {}

    /**
     * 比较2个IP地址
     * @param ip1
     * @param ip2
     * @return
     */
    public static int compare(String ip1, String ip2) {
        final int[] ip1Values = getValues(ip1);
        final int[] ip2Values = getValues(ip2);
        if (ip1Values[0] - ip2Values[0] != 0)
            return ip1Values[0] - ip2Values[0];
        if (ip1Values[1] - ip2Values[1] != 0)
            return ip1Values[1] - ip2Values[1];
        if (ip1Values[2] - ip2Values[2] != 0)
            return ip1Values[2] - ip2Values[2];
        if (ip1Values[3] - ip2Values[3] != 0)
            return ip1Values[3] - ip2Values[3];
        return 0;
    }

    /**
     * 下一个IP
     * @param ip
     * @return
     */
    public static String nextIpAddress(String ip) {
        if (StringUtils.isEmpty(ip))
            throw new IllegalArgumentException("ip is not valid ip address");

        String[] numbers = ip.split("\\.");

        if (numbers.length != 4)
            throw new IllegalArgumentException("ip is not valid ip address");

        int[] values = new int[4];
        for (int i = 0; i < 4; i++) {
            int value = Integer.parseInt(numbers[i]);
            if (value < 0 || value > 255)
                throw new IllegalArgumentException("ip is not valid ip address");
            values[i] = value;
        }
        if (values[3] + 1 == 255) {
            values[3] = 0;
            if (values[2] + 1 == 255) {
                values[2] = 0;
                if (values[1] + 1 == 255) {
                    values[1] = 0;
                    values[0] = (values[0] + 1);
                } else
                    values[1] = (values[1] + 1);
            } else {
                values[2] = (values[2] + 1);
            }
        } else {
            values[3] = (values[3] + 1);
        }
        return values[0] + "." + values[1] + "." + values[2] + "." + values[3];

    }

    /**
     * ip字符串转换为int数组
     * @param ip
     * @return
     */
    public static int[] getValues(String ip) {
        if (StringUtils.isEmpty(ip))
            throw new IllegalArgumentException("ip is not valid ip address");

        String[] numbers = ip.split("\\.");

        if (numbers.length != 4)
            throw new IllegalArgumentException("ip is not valid ip address");

        int[] values = new int[4];
        for (int i = 0; i < 4; i++) {
            int value = Integer.parseInt(numbers[i]);
            if (value < 0 || value > 255)
                throw new IllegalArgumentException("ip is not valid ip address");
            values[i] = value;
        }
        return values;
    }

    /**
     * 判断当前Ip地址是否有效,同时地址非回环地址和非多播IP地址
     * 
     * @param ipAddress
     * @return
     */
    public static boolean isValidAddress(String ip) {

        if (StringUtils.isEmpty(ip))
            return false;

        String[] numbers = ip.split("\\.");

        if (numbers.length != 4)
            return false;

        byte[] values = new byte[4];
        for (int i = 0; i < 4; i++) {
            int value = Integer.parseInt(numbers[i]);
            if (value < 0 || value > 255)
                return false;
            values[i] = (byte) value;
        }

        try {
            InetAddress inetAddress = InetAddress.getByAddress(values);
            if (inetAddress.isAnyLocalAddress() || inetAddress.isMulticastAddress()
                    || inetAddress.isLoopbackAddress())// 组播地址、回环地址等排除在外
                return false;
        } catch (UnknownHostException e) {
            return false;
        }

        return true;
    }

    /**
     * ip地址是否合法，仅做简单判断
     * 
     * @param text
     * @return
     */
    public static boolean isValidIp(String text) {
        // 确保仅包含数字和3个点号
        Matcher m = Pattern.compile("\\d+{1,3}\\.\\d+{1,3}\\.\\d+{1,3}\\.\\d+{1,3}").matcher(text.trim());
        if (!m.matches())
            return false;

        String[] strs = text.trim().split("\\.");

        if (Integer.parseInt(strs[0]) > 255 || Integer.parseInt(strs[1]) > 255
                || Integer.parseInt(strs[2]) > 255 || Integer.parseInt(strs[3]) > 255) {
            return false;
        }
        return true;
    }

    /**
     * 是否为组播地址
     * 
     * @return
     */
    public static boolean isMulticastAddress(String ip) {
        if (StringUtils.isEmpty(ip))
            return false;
        if (ip.startsWith("224.0.0."))
            return false;

        String[] numbers = ip.split("\\.");

        if (numbers.length != 4)
            return false;

        byte[] values = new byte[4];
        for (int i = 0; i < 4; i++) {
            int value = Integer.parseInt(numbers[i]);
            if (value < 0 || value > 255)
                return false;
            values[i] = (byte) value;
        }

        try {
            InetAddress inetAddress = InetAddress.getByAddress(values);
            return inetAddress.isMulticastAddress();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 子网掩码的比较
     * 
     * if m1 has more bits than m2, return -1 if m1 is equale to m2, return 0 if
     * m1 has less bits than m2, return 1
     * 
     * @param m1
     * @param m2
     * @return
     */
    public static int compareMask(String m1, String m2) {
        byte[] b1 = IPAddressUtil.textToNumericFormatV4(m1);
        byte[] b2 = IPAddressUtil.textToNumericFormatV4(m2);
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] == b2[i]) {
                continue;
            }
            if ((b1[i] & b2[i]) == b2[i]) {
                return -1;
            } else {
                return 1;
            }
        }
        return 0;
    }

    /**
     * 得到同一网段的ip地址数量
     * 
     * @param mask
     * @return
     */
    public static int getIPCountOfMask(String mask) {
        byte[] b1 = IPAddressUtil.textToNumericFormatV4(mask);
        int len = 0;
        int i = 0;
        for (i = 0; i < b1.length; i++) {
            if (b1[i] == -1) {
                len += 8;
            } else {
                break;
            }
        }
        if (b1[i] != 0) {
            len++;
            while ((b1[i] = (byte) (b1[i] << 1)) != 0) {
                len++;
            }
        }
        len = (int) Math.pow(2, 32 - len);
        return len;
    }

    /**
     * 得到同一网段的所有ip地址
     * 
     * @param netAddr
     * @param mask
     * @return
     */
    public static List<String> getIPsInNet(String netAddr, String mask) {
        int len = getIPCountOfMask(mask);
        byte[] start = IPAddressUtil.textToNumericFormatV4(netAddr);

        List<String> list = new Vector<String>();
        int startint = 0;
        for (int i = 0; i < start.length; i++) {
            startint |= (start[i] << (start.length - i - 1) * 8) & 0xffffffff >>> i * 8;
        }
        for (int i = 1; i < len; i++) {
            int ipint = (i | startint);
            byte[] numip = new byte[4];
            for (int j = 0; j < 4; j++) {
                numip[j] = (byte) (((ipint & (0xff << (4 - j - 1) * 8)) >>> (4 - j - 1) * 8) & 0xff);
            }
            list.add(numericToTextFormat(numip));
        }
        return list;
    }

    /**
     * 得到子网地址
     * 
     * @param ip
     * @param mask
     * @return
     */
    public static String getNetAddr(String ip, String mask) {
        byte[] ipbs = IPAddressUtil.textToNumericFormatV4(ip);
        byte[] ipms = IPAddressUtil.textToNumericFormatV4(mask);
        byte[] result = new byte[ipbs.length];
        for (int i = 0; i < ipbs.length; i++) {
            result[i] = (byte) (ipbs[i] & ipms[i]);
        }
        return numericToTextFormat(result);
    }

    /**
     * 得到广播地址
     * 
     * @param ip
     *            ip地址
     * @param mask
     *            子网掩码
     * @return
     */
    public static String getNetBrdcstAddr(String ip, String mask) {
        byte[] ipbs = IPAddressUtil.textToNumericFormatV4(ip);
        byte[] ipms = IPAddressUtil.textToNumericFormatV4(mask);
        byte[] result = new byte[ipbs.length];
        for (int i = 0; i < ipbs.length; i++) {
            result[i] = (byte) (ipbs[i] | ~ipms[i]);
        }
        return numericToTextFormat(result);
    }

    /**
     * 判断2个IP是否属于同一个网段
     * 
     * @param ipAddr1
     *            第一个IP地址,如192.168.8.2
     * @param mask1
     *            第一个子网掩码,如255.255.255.0
     * @param ipAddr2
     *            第二个IP地址,如192.168.18.52
     * @param mask2
     *            第二个子网掩码,如255.255.255.0
     * @return
     */
    public static boolean isInSameNet(String ipAddr1, String mask1, String ipAddr2, String mask2) {
        String netAddr1 = getNetAddr(ipAddr1, mask1);
        String netAddr2 = getNetAddr(ipAddr2, mask2);
        return netAddr1.equals(netAddr2);
    }

    /**
     * 一个字符串是否是IP格式表达
     * 
     * @param ip
     * @return
     */
    public static boolean isIPAddress(String ip) {
        long ipLong = textToLongFormat(ip);
        return ipLong != -1;
    }

    /**
     * 检查起始IP是否不小于结束IP <br>
     * <li>输入的IP和Mask必须为不为null,否则返回false <li>输入的IP和Mask必须为有效值,否则返回false <li>
     * 输入的IP必须在一个网段,否则返回false
     * 
     * @param startIP
     * @param startMask
     * @param endIP
     * @param endMask
     * @return
     */
    public static boolean isStartIPBeforeEndIP(String startIP, String startMask, String endIP, String endMask) {
        // 检查输入是否有效
        if (startIP == null || startMask == null || endIP == null || endMask == null) {
            return false;
        }

        // 检查格式
        if (!isIPAddress(startIP) || !isIPAddress(startMask) || !isIPAddress(endIP) || !isIPAddress(endMask)) {
            return false;
        }
        // 检查网段
        if (!isInSameNet(startIP, startMask, endIP, endMask)) {
            return false;
        }

        // 比较顺序
        long startIPLong = textToLongFormat(startIP);
        long endIPLong = textToLongFormat(endIP);
        if (startIPLong > endIPLong) {
            return false;
        }

        return true;
    }

    /**
     * 获取本地所有ip地址
     * 
     * @return
     */
    public static List<String> getAllLocalIpAddress() {
        List<String> ipList = new ArrayList<String>();
        try {
            InetAddress[] addresses = InetAddress.getAllByName(InetAddress.getLocalHost().getHostName());
            if (addresses != null) {
                for (InetAddress ip : addresses) {
                    if (ip instanceof Inet4Address)
                        ipList.add(ip.getHostAddress());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return ipList;
    }

    /**
     * 判断子网掩码是否合法
     * 
     * @param ipMask
     * @return
     */
    public static boolean isMaskValid(String ipMask) {
        if (!isIPAddress(ipMask))
            return false;
        String bits = convertIp2Bits(ipMask);
        String regex = "[1]*[0]*";
        Pattern pat = Pattern.compile(regex);
        Matcher matcher = pat.matcher(bits);
        return matcher.matches();
    }

    /**
     * 判断子网掩码是否合法（反码）
     * 
     * @param ipMask
     * @return
     */
    public static boolean isMaskValid2(String ipMask) {
        if (!isIPAddress(ipMask))
            return false;
        String bits = convertIp2Bits(ipMask);
        String regex = "[0]*[1]*";
        Pattern pat = Pattern.compile(regex);
        Matcher matcher = pat.matcher(bits);
        return matcher.matches();
    }

    /**
     * 将IP地址转换为二进制字符串
     * 
     * @param ipAddr
     * @return
     */
    public static String convertIp2Bits(String ipAddr) {
        if (!isIPAddress(ipAddr))
            return "";
        String[] paras = ipAddr.trim().split("\\.");
        if (paras.length != 4)
            return "";
        StringBuilder sb = new StringBuilder();
        sb.append(completeString2lenBefore(Integer.toString(Integer.parseInt(paras[0]), 2), 8));
        sb.append(completeString2lenBefore(Integer.toString(Integer.parseInt(paras[1]), 2), 8));
        sb.append(completeString2lenBefore(Integer.toString(Integer.parseInt(paras[2]), 2), 8));
        sb.append(completeString2lenBefore(Integer.toString(Integer.parseInt(paras[3]), 2), 8));
        String bits = sb.toString();
        return bits;
    }

    public static String completeString2lenBefore(String src, int len) {
        if (src.length() >= len)
            return src;
        int dec = len - src.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dec; i++)
            sb.append("0");
        sb.append(src);
        return sb.toString();
    }

    /**
     * 获取本机的IP地址与子网掩码
     * 
     * @return
     */
    public static Map<String, String> getLocalIpAndMask() {
        Map<String, String> ipMap = new HashMap<String, String>();
        try {
            Enumeration<NetworkInterface> eni = NetworkInterface.getNetworkInterfaces();
            while (eni.hasMoreElements()) {
                NetworkInterface ni = eni.nextElement();
                List<InterfaceAddress> lia = ni.getInterfaceAddresses();
                Iterator<InterfaceAddress> iia = lia.iterator();
                while (iia.hasNext()) {
                    InterfaceAddress ia = iia.next();
                    if(ia==null)
                        continue;
                    InetAddress a = ia.getAddress();
                    if(a instanceof Inet6Address)
                        continue;
                    if (!a.isLoopbackAddress()) {
                        String ipaddr = a.getHostAddress();
                        long mask = 0;
                        for (int n = 0; n < ia.getNetworkPrefixLength(); n++) {
                            mask |= 1 << (31 - n);
                        }
                        String ipmask = ((mask >> 24) & 0xff) + "." + ((mask >> 16) & 0xff) + "."
                                + ((mask >> 8) & 0xff) + "." + (mask & 0xff);
                        if(ipmask==null || ipmask.equals("") 
                                || ipmask.equals("0.0.0.0") || ipmask.equals("255.255.255.255"))
                            ipmask=WatchUtil.getDefaultNetMask(ipaddr);
                        ipMap.put(ipaddr, ipmask);
                        
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipMap;
    }

    /**
     * 从ip地址段中解析所有的ip地址.
     * 
     * @param from
     *            起始ip地址
     * @param to
     *            终止ip地址
     * @param list
     *            地址段中所有的ip地址
     */
    public static List<String> getIpListFromSegment(String from, String to) {
        List<String> list = new ArrayList<String>();
        try {
            long[] fromIp = ipToIntArray(from);
            long[] toIp = ipToIntArray(to);
            long[] pointIp = fromIp;
            list.add("" + pointIp[0] + "." + pointIp[1] + "." + pointIp[2] + "." + pointIp[3]);
            int i = 0;
            while (!(pointIp[0] == toIp[0] && pointIp[1] == toIp[1] && pointIp[2] == toIp[2] && pointIp[3] == toIp[3])) {
                pointIp[3]++;
                if (pointIp[3] > 255) {
                    pointIp[2]++;
                    pointIp[3] = 0;
                }
                if (pointIp[2] > 255) {
                    pointIp[1]++;
                    pointIp[2] = 0;
                }
                if (pointIp[1] > 255) {
                    pointIp[0]++;
                    pointIp[1] = 0;
                }
                i++;
                list.add("" + pointIp[0] + "." + pointIp[1] + "." + pointIp[2] + "." + pointIp[3]);
            }
        } catch (Exception ex) {
        }
        return list;
    }

    /**
     * @param ip
     *            ip
     * @return long
     */
    public static long[] ipToIntArray(String ip) {
        long[] ipInt = { 0, 0, 0, 0 };
        int i = 0;
        if (ip == null) {
            return ipInt;
        }
        StringTokenizer tokens = new StringTokenizer(ip, ".");
        while (tokens.hasMoreTokens()) {
            ipInt[i++] = Long.parseLong((String) tokens.nextToken());
        }
        return ipInt;
    }
    
    /**
     * 检验两个IP范围之间是否冲突
     * @param startIp1
     * @param endIp1
     * @param startIp2
     * @param endIp2
     * @return
     */
    public static boolean isIpRangeConfict(String startIp1, String endIp1, String startIp2, String endIp2) {
        int temp1 = compare(startIp1, startIp2);
        int temp2 = compare(endIp1, startIp2);
        int temp3 = compare(startIp1, endIp2);
        int temp4 = compare(endIp1, endIp2);
        if (temp1 < 0 && temp2 < 0 && temp3 < 0 && temp4 < 0)
            return false;
        if (temp1 > 0 && temp2 > 0 && temp3 > 0 && temp4 > 0)
            return false;
        return true;
    }
    
    /**
     * ip长整型转换为文本型
     * 
     * @param src
     * @return
     */
    public static String longToTextFormat(long src) {
        byte[] bs = new byte[4];
        for (int i = 0; i < bs.length; i++) {
            bs[bs.length - 1 - i] = (byte) ((src >> i * 8) & 0xff);
        }
        return numericToTextFormat(bs);
    }

    /**
     * ip地址格式数据转换为文本型
     * 
     * @param src
     * @return
     */
    public static String numericToTextFormat(byte[] src) {
        return (src[0] & 0xff) + "." + (src[1] & 0xff) + "." + (src[2] & 0xff) + "." + (src[3] & 0xff);
    }

    /**
     * ip文本型转化为长整型,IP无效返回-1
     * 
     * @param src
     * @return IP无效返回-1
     */
    public static long textToLongFormat(String src) {
        if (src == null || src.isEmpty()) {
            return -1;
        }

        byte[] bs = IPAddressUtil.textToNumericFormatV4(src);
        if (bs == null) {
            return -1;
        }

        long res = 0;
        for (int i = 0; i < bs.length; i++) {
            res |= (bs[i] << (3 - i) * 8) & (0xffffffffL >>> i * 8);
        }
        return res;
    }
    
    /**
     * 转换子网掩码，例如将24表示为255.255.255.0
     * @param maskInt
     * @return
     */
    public static String getMaskByInt(int maskInt){
        if (maskInt > 24) {
            int temp = 0;
            for (int i = 1; i <= maskInt - 24; i++) {
                temp = temp + (1 << (8 - i));
            }
            return "255.255.255." + temp;
        } else if (maskInt > 16) {
            int temp = 0;
            for (int i = 1; i <= maskInt - 16; i++) {
                temp = temp + (1 << (8 - i));
            }
            return "255.255." + temp + ".0";
        } else if (maskInt > 8) {
            int temp = 0;
            for (int i = 1; i <= maskInt - 8; i++) {
                temp = temp + (1 << (8 - i));
            }
            return "255." + temp + ".0.0";
        } else {
            int temp = 0;
            for (int i = 1; i <= maskInt; i++) {
                temp = temp + (1 << (8 - i));
            }
            return temp + ".0.0.0";
        }
    }
    
    /**
     * 转换子网掩码，例如将255.255.255.0表示为24
     * @param mask
     * @return
     */
    public static int getInByMask(String mask) {
        String binaryString = convertIp2Bits(mask);
        int index = binaryString.lastIndexOf('1');
        return index + 1;
    }
    
}
