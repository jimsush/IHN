/*
 * $Id: SfSnmpException.java, 2015-11-12 上午11:40:47 sufeng Exp $
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
 * SNMP异常
 * </p>
 * 
 * @author pl
 * created 2015-11-16 上午11:31:50
 * modified [who date description]
 * check [who date description]
 */
public class SfSnmpException extends SfException{
    
    private static final long serialVersionUID = 1149264460688263938L;
    
    public static final int SBI_GET_ERROR = 1001; //snmp get操作错误
    public static final int SBI_SET_ERROR = 1002; //snmp set操作错误
    public static final int SBI_NOSUCHINSTANCE = 1003; //索引不存在
    
    public SfSnmpException(int errorCode, String... source) {
        super(errorCode, source);
    }

    public SfSnmpException(int errorCode, Throwable th, String... source) {
        super(errorCode, th, source);
    } 
    
}
