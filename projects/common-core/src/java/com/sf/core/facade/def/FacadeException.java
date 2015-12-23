/*
 * $Id: FacadeException.java, 2015-2-24 ����03:48:19 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.facade.def;

import com.sf.base.exception.SfException;

/**
 * <p>
 * Title: FacadeException
 * </p>
 * <p>
 * Description: Facadeʹ�õ��쳣��
 * </p>
 * 
 * @author sufeng
 * created 2015-2-24 ����03:48:19
 * modified [who date description]
 * check [who date description]
 */
public class FacadeException extends SfException {

    private static final long serialVersionUID = 2298659607995553625L;
    
    /**
     * facadeԶ�̵���ʧ��
     */
    public static final int FACADE_INVOKE_FAILED=101;
    
    public FacadeException(int errorCode, String... source) {
        super(errorCode, source);
    }
    
    public FacadeException(int errorCode,Throwable th,String... source) {
        super(errorCode,th,source);
    }

    

}
