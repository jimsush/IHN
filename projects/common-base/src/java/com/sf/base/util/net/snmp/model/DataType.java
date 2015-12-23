/*
 * $Id: DataType.java, 2015-11-12 ����11:40:47 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.net.snmp.model;

/**
 * <p>
 * Title: DataType
 * </p>
 * <p>
 * SNMPЭ������
 * </p>
 * 
 * @author pl
 * created 2015-11-16 ����11:30:46
 * modified [who date description]
 * check [who date description]
 */
public enum DataType {
    
    INTEGER, 
    STRING,
    MACADDRESS, 
    IPADDRESS,
    TIMETICKS_STRING_TIMEZONE,
    TIMETICKS_STRING,
    TIMETICKS_LONG,
    DATEANDTIME,
    COUNTER64,
    BOOLEAN,
    BYTE,
    BYTEARRAY;
    
}