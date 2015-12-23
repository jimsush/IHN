/*
 * $Id: SfSnmpException.java, 2015-11-12 ����11:40:47 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.net.snmp.model;

import com.sf.base.exception.SfException;

/**
 * <p>
 * Title: SfSnmpException
 * </p>
 * <p>
 * SNMP�쳣
 * </p>
 * 
 * @author pl
 * created 2015-11-16 ����11:31:50
 * modified [who date description]
 * check [who date description]
 */
public class SfSnmpException extends SfException{
    
    private static final long serialVersionUID = 1149264460688263938L;
    
    public static final int SBI_GET_ERROR = 1001; //snmp get��������
    public static final int SBI_SET_ERROR = 1002; //snmp set��������
    public static final int SBI_NOSUCHINSTANCE = 1003; //����������
    
    public SfSnmpException(int errorCode, String... source) {
        super(errorCode, source);
    }

    public SfSnmpException(int errorCode, Throwable th, String... source) {
        super(errorCode, th, source);
    } 
    
}
