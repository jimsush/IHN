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

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.adventnet.snmp.snmp2.SnmpCounter;
import com.adventnet.snmp.snmp2.SnmpCounter64;
import com.adventnet.snmp.snmp2.SnmpGauge;
import com.adventnet.snmp.snmp2.SnmpInt;
import com.adventnet.snmp.snmp2.SnmpIpAddress;
import com.adventnet.snmp.snmp2.SnmpNull;
import com.adventnet.snmp.snmp2.SnmpString;
import com.adventnet.snmp.snmp2.SnmpTimeticks;
import com.adventnet.snmp.snmp2.SnmpUnsignedInt;
import com.adventnet.snmp.snmp2.SnmpVar;
import com.adventnet.snmp.snmp2.SnmpVarBind;
import com.sf.base.util.format.ByteFormatter;
import com.sf.base.util.format.DateFormatter;
import com.sf.base.util.net.snmp.model.DataType;

/**
 * <p>
 * Title: SnmpContentUtil
 * </p>
 * <p>
 * 用于SnmpVar类型转换的工具类
 * </p>
 * 
 * @author pl
 * created 2015-2-20 上午09:58:40
 * modified [who date description]
 * check [who date description]
 */
public class SnmpContentUtil {

    public static final int ON_ACTION = 1;

    public static final int CREATE_VALUE = 4;

    public static final int DELETE_VALUE = 6;

    /**
     * bool转换snmpvar
     * 
     * @param in
     * @return
     */
    public static SnmpVar bool2snmpvar(boolean in) {
        if (in) {
            return new SnmpInt(1);
        } else {
            return new SnmpInt(0);
        }
    }

    /**
     * Mac地址转化SnmpVar
     * 
     * @param mac
     * @return
     */
    public static SnmpVar mac2Snmpvar(String mac) {
        return new SnmpString(ByteFormatter.convertStringInASCIItoHexArray(mac));
    }

    /**
     * SnmpVar转换为boolean
     * @param var
     * @return
     */
    public static boolean snmpVar2Boolean(SnmpVar var) {
        if (var == null) {
            return false;
        }
        int intValue = snmpVar2Int(var);
        return intValue==1;
    }

    /**
     * SnmpVar转换为byte
     * @param var
     * @return
     */
    public static byte snmpVar2Byte(SnmpVar var) {
        if (var == null) {
            return 0;
        }
        return (byte) snmpVar2Int(var);
    }

    /**
     * 将SnmpVar转换为byte[]
     * 
     * @param xVar
     *            SnmpVar
     * @return byte[]
     * @throws Exception
     */
    public static byte[] snmpVar2ByteArray(SnmpVar xVar) {
        if (xVar == null) {
            return new byte[0];
        }
        byte[] array = new byte[0];
        if (xVar instanceof SnmpString) {
            SnmpString strVar = (SnmpString) xVar;
            array = strVar.toBytes();
        }
        return array;
    }

    /**
     * 将SnmpVar转换为int
     * 
     * @param xVar
     *            SnmpVar
     * @return int
     * @throws Exception
     */
    public static int snmpVar2Int(SnmpVar var) {
        int result = -1;
        if (var instanceof SnmpInt) {
            SnmpInt intVar = (SnmpInt) var;
            result = intVar.intValue();
        } else if (var instanceof SnmpCounter) {
            SnmpCounter counter = (SnmpCounter) var;
            result = (int) counter.longValue();
        } else if (var instanceof SnmpGauge) {
            SnmpGauge gauge = (SnmpGauge) var;
            result = (int) gauge.longValue();
        } else if (var instanceof SnmpUnsignedInt) {
            SnmpUnsignedInt unsignedInt = (SnmpUnsignedInt) var;
            result = (int) unsignedInt.longValue();
        } else if (var instanceof SnmpNull) {
            result = -1;
        } else if (var instanceof SnmpCounter64) {
            SnmpCounter64 counter = (SnmpCounter64) var;
            result = Integer.parseInt(counter.getNumericValueAsString());
        }
        return result;
    }

    /**
     * 将SnmpVar转换为IP地址
     * 
     * @param xVar
     *            SnmpVar
     * @return IP地址的字符串表示
     * @throws Exception
     */
    public static String snmpVar2IpAddress(SnmpVar var) {
        String result = "";
        if (var instanceof SnmpIpAddress) {
            SnmpIpAddress strSnmp = (SnmpIpAddress) var;
            result = strSnmp.toString();
        }
        return result;
    }

    /**
     * 将SnmpVar转换为int
     * 
     * @param xVar
     *            SnmpVar
     * @return int
     * @throws Exception
     */
    public static long snmpVar2Long(SnmpVar var) {
        long result = 0;
        if (var instanceof SnmpInt) {
            SnmpInt intVar = (SnmpInt) var;
            result = intVar.longValue();
        } else if (var instanceof SnmpGauge) {
            SnmpGauge gauge = (SnmpGauge) var;
            result = gauge.longValue();
        }

        return result;
    }

    /**
     * SnmpVar转化Mac地址
     * 
     * @param var
     * @return
     */
    public static String snmpvar2Mac(SnmpVar var) {
        byte[] bytes = var.toBytes();
        if (bytes.length == 6) {
            return ByteFormatter.convertStringInHextoASCII(var.toBytes());
        } else if (bytes.length == 17) {
            String str = var.toString();
            return str.replaceAll(":", "-");
        } else
            return var.toString();
    }

    /**
     * 将SnmpVar转换为字符串
     * 
     * @param xVar
     *            SnmpVar
     * @return 字符串
     * @throws Exception
     */
    public static String snmpVar2Str(SnmpVar var) {
        String result = null;

        if (var == null) {
            return "";
        }

        // 增加中文支持
        if (var instanceof SnmpString) {
            String str = new String(snmpVar2ByteArray(var));
            if (str.getBytes().length != str.length()) {
                result = str;
            } else {
                SnmpString strSnmp = (SnmpString) var;
                result = (strSnmp.toString());
            }
        } else if (var instanceof SnmpInt) {
            SnmpInt intSnmp = (SnmpInt) var;
            result = "" + intSnmp.intValue();
        } else if (var instanceof SnmpGauge) {
            SnmpGauge snmpGauge = (SnmpGauge) var;
            result = "" + snmpGauge.longValue();
        }

        return result;
    }

    /**
     * 将SnmpVar转换为时间，排除+8时区的问题
     * 
     * @param var
     * @return
     */
    public static String snmpVar2Timeticks_String_TimeZone(SnmpVar var) {
        String resu = "";
        if (var == null) {
            return resu;
        }
        if (var instanceof SnmpTimeticks) {
            SnmpTimeticks loSnmp = (SnmpTimeticks) var;
            resu = loSnmp.toString();
        }
        if (var instanceof SnmpGauge) {
            SnmpGauge timeVar = (SnmpGauge) var;
            resu = DateFormatter.getLongDate((timeVar.longValue() - 8 * 3600) * 1000);
        } else if (var instanceof SnmpInt) {
            SnmpInt timeVar = (SnmpInt) var;
            resu = DateFormatter.getLongDate((timeVar.longValue() - 8 * 3600) * 1000);
        }
        return resu;
    }

    /**
     * 将SnmpVar转换为时间
     * 
     * @param xVar
     *            SnmpVar
     * @return
     * @throws Exception
     */
    public static String snmpVar2Timeticks_String(SnmpVar var) {// 改写方法返回String
        String resu = "";
        if (var == null) {
            return resu;
        }
        if (var instanceof SnmpTimeticks) {
            SnmpTimeticks loSnmp = (SnmpTimeticks) var;
            resu = loSnmp.toString();
        }
        if (var instanceof SnmpGauge) {
            SnmpGauge timeVar = (SnmpGauge) var;
            resu = DateFormatter.getLongDate(timeVar.longValue() * 1000);
        } else if (var instanceof SnmpInt) {
            SnmpInt timeVar = (SnmpInt) var;
            resu = DateFormatter.getLongDate(timeVar.longValue() * 1000);
        }

        return resu;
    }

    /**
     * 需要对TimeTiket
     * 
     * @param var
     * @param multi
     * @return
     */
    public static String snmpVar2Timeticks_String_withMulti(SnmpVar var, int multi) {
        String resu = "";
        if (var instanceof SnmpTimeticks) {
            SnmpTimeticks loSnmp = (SnmpTimeticks) var;
            SnmpTimeticks temp = new SnmpTimeticks(loSnmp.longValue() * multi);
            resu = temp.toString();
        }
        return resu;
    }

    /**
     * 将SnmpVar转换为时间
     * 
     * @param xVar
     *            SnmpVar
     * @return
     * @throws Exception
     */
    public static Long snmpVar2Timeticks_Long(SnmpVar var) {// 改写方法返回String
        Long resu = 0L;
        if (var == null) {
            return resu;
        }
        if (var instanceof SnmpTimeticks) {
            SnmpTimeticks loSnmp = (SnmpTimeticks) var;
            resu = loSnmp.longValue();
        }
        if (var instanceof SnmpGauge) {
            SnmpGauge timeVar = (SnmpGauge) var;
            resu = timeVar.longValue();
        } else if (var instanceof SnmpInt) {
            SnmpInt timeVar = (SnmpInt) var;
            resu = timeVar.longValue();
        }

        return resu;
    }

    /**
     * SnmpVar转换为DateTime类型
     * @param var
     * @return
     */
    public static String snmpVar2DateAndTime(SnmpVar var) {
        // 添加对于系统运行时间是int型的处理，一般是非标准设备比如C2000
        if (var instanceof SnmpGauge) {
            SnmpGauge gauge = (SnmpGauge) var;
            return convertLong2DateTime(gauge.longValue());
        }
        byte[] bytes = var.toBytes();
        int i = bytes.length;
        byte abyte0[] = new byte[i * 2];
        int j = 0;
        for (int k = 0; k < bytes.length; k++) {
            abyte0[j] = (byte) (bytes[k] >> 4 & 15);
            abyte0[++j] = (byte) (bytes[k] & 15);
            j++;
        }
        i = abyte0.length;
        if (i != 16 && i != 22) {
            return null;
        }
        String s = "";
        j = abyte0[0] * 4096 + abyte0[1] * 256 + abyte0[2] * 16 + abyte0[3];
        s = s + j + "-";
        int k = abyte0[4] * 16 + abyte0[5];
        s = s + k + "-";
        int l = abyte0[6] * 16 + abyte0[7];
        s = s + l + ",";
        int i1 = abyte0[8] * 16 + abyte0[9];
        s = s + i1 + ":";
        int j1 = abyte0[10] * 16 + abyte0[11];
        s = s + j1 + ":";
        int k1 = abyte0[12] * 16 + abyte0[13];
        s = s + k1 + ".";
        int l1 = abyte0[14] * 16 + abyte0[15];
        s = s + l1;
        if (i > 16 && i != 32) {
            s = s + ",";
            char c = (char) (abyte0[16] * 16 + abyte0[17]);
            s = s + c;
            int i2 = abyte0[18] * 16 + abyte0[19];
            s = s + i2 + ":";
            int j2 = abyte0[20] * 16 + abyte0[21];
            s = s + j2;
        }
        return s;
    }

    /**
     * 将byte数组转换为SnmpString
     * @param data
     * @return
     */
    public static SnmpString str2SnmpVar(byte[] data) {
        SnmpString var;
        var = new SnmpString(data);
        return var;
    }

    /**
     * 将String转化为SnmpString 支持中文字符
     * 
     * @param str
     * @return
     */
    public static SnmpString str2SnmpVar(String str) {
        SnmpString var;
        if (str == null) {
            return null;
        }
        byte[] data = str.getBytes();
        if (data.length != str.length()) {
            var = new SnmpString(data);
        } else {
            var = new SnmpString(str);
        }
        return var;
    }

    /**
     * 将SnmpVar转换为需要的类型
     * @param type
     * @param var
     * @return
     */
    public static Object getValueBySnmpVar(DataType type, SnmpVar var) {
        Object value = null;
        switch (type) {
        case INTEGER:
            value = snmpVar2Int(var);
            break;
        case BYTE:
            value = snmpVar2Byte(var);
            break;
        case IPADDRESS:
            value = snmpVar2IpAddress(var);
            break;
        case STRING:
            value = snmpVar2Str(var);
            break;
        case MACADDRESS:
            value = snmpvar2Mac(var);
            break;
        case COUNTER64:
            value = snmpVar2Int(var);
            break;
        case TIMETICKS_STRING_TIMEZONE:
            value = snmpVar2Timeticks_String_TimeZone(var);
            break;
        case TIMETICKS_STRING:
            value = snmpVar2Timeticks_String(var);
            break;
        case TIMETICKS_LONG:
            value = snmpVar2Timeticks_Long(var);
            break;
        case DATEANDTIME:
            value = snmpVar2DateAndTime(var);
            break;
        case BOOLEAN:
            int temp = snmpVar2Int(var);
            if (temp == 0)
                value = false;
            else
                value = true;
            break;
        case BYTEARRAY:
            value = snmpVar2ByteArray(var);
            break;
        }
        return value;
    }

    /**
     * 根据需要转换SnmpVar类型
     * @param type
     * @param obj
     * @return
     */
    public static SnmpVar generateSnmpVarByType(DataType type, Object obj) {
        SnmpVar var = null;
        switch (type) {
        case INTEGER:
            if (obj instanceof Integer) {
                Integer intObj = (Integer) obj;
                var = new SnmpInt(intObj.intValue());
            } else if (obj instanceof Long) {
                Long intObj = (Long) obj;
                var = new SnmpInt(intObj.longValue());
            } else if (obj instanceof Boolean) {
                Boolean bool = (Boolean) obj;
                if (bool.booleanValue())
                    var = new SnmpInt(1);
                else
                    var = new SnmpInt(0);
            } else if (obj instanceof Double) {
                var = new SnmpInt(((Double) obj).intValue());
            } else if (obj instanceof Float) {
                var = new SnmpInt(((Float) obj).intValue());
            }
            break;
        case IPADDRESS:
            String ipAddr = (String) obj;
            var = new SnmpIpAddress(ipAddr);
            break;
        case STRING:
            String str = (String) obj;
            var = SnmpContentUtil.str2SnmpVar(str);
            break;
        case MACADDRESS:
            str = (String) obj;
            var = new SnmpString(ByteFormatter.convertStringInASCIItoHexArray(str));
            break;
        case BOOLEAN:
            Boolean bool = (Boolean) obj;
            if (bool.booleanValue())
                var = new SnmpInt(1);
            else
                var = new SnmpInt(0);
            break;
        case BYTE:
            Byte byteobj = (Byte) obj;
            var = new SnmpInt(byteobj.intValue());
            break;
        case BYTEARRAY:
            byte[] byteArrayObj = (byte[]) obj;
            var = new SnmpString(byteArrayObj);
            break;
        case TIMETICKS_LONG:
            var = new SnmpGauge((Long) obj);
            break;
        }
        return var;
    }

    /**
     * 将long类型转换为DateTime类型
     * @param seconds
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String convertLong2DateTime(long seconds) {
        if (seconds < 0)
            return "";
        Date date = new Date(seconds * 1000);
        int timeZone = 8;
        StringBuffer buffer = new StringBuffer();
        int year = date.getYear() + 1900;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int minute = date.getMinutes();
        int second = date.getSeconds();
        buffer.append(year).append("-").append(month).append("-").append(day);
        buffer.append(",").append(hour).append(":").append(minute).append(":").append(second);
        buffer.append(".0,");
        if (timeZone >= 0)
            buffer.append("+").append(timeZone);
        else
            buffer.append("-").append(-timeZone);
        buffer.append(":0");
        return buffer.toString();
    }

    /**
     * 
     * @param seconds
     *            秒
     * @param timeZone
     *            减去的时区数目
     * @return
     */
    public static String dealWithTimeZone(String str, int timeZone) {
        Date temp;
        try {
            temp = DateUtils.parseDate(str, new String[] { DateFormatter.LONG_FORMAT.toPattern() });
        } catch (ParseException e) {
            temp = new Date();
        }
        long time = temp.getTime() - 3600L * 1000 * timeZone;
        return DateFormatter.getLongDate(time);
    }

    /**
     * 用于从设备取到的SnmpVarBind数组中，根据结点OID去取对应的SnmpVar
     * 
     * @param binds
     * @param oid
     * @return
     */
    public static SnmpVar getSnmpVarByOid(SnmpVarBind[] binds, String oid) {
        for (SnmpVarBind snmpVarBind : binds) {
            if (SnmpOidUtil.isMibNodeByOid(snmpVarBind.getObjectID().toString(), oid))
                return snmpVarBind.getVariable();
        }
        return null;
    }

    /**
     * 比较每个SnmpVarBind[]里面的元素个数是不是一致的
     * 
     * @param vars
     * @return
     */
    public static boolean isOrderlySnmpVarBind(SnmpVarBind[][] vars) {
        if (vars == null)
            return false;
        int len = vars.length;
        if (len == 0)
            return false;

        int row = 0;
        if (vars[0] != null)
            row = vars[0].length;

        for (int i = 0; i < len; i++) {
            SnmpVarBind[] binds = vars[i];
            if (binds == null)
                return false;
            if (row != binds.length)
                return false;
        }
        return true;
    }

}
