/*
 * $Id: FacadeResponse.java, 2015-3-4 ����10:10:16 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.facade.def;

/**
 * <p>
 * Title: FacadeResponse
 * </p>
 * <p>
 * Description: facade�����з��ض���,��������ֵ���쳣,�Ƿ���Ҫǿ�Ʒ���,������holder
 * </p>
 * 
 * @author sufeng
 * created 2015-3-4 ����10:10:16
 * modified [who date description]
 * check [who date description]
 */
public class FacadeResponse {

    /**
     * ����ֵ
     */
    private Object returnValue;
    
    /**
     * �׳����쳣
     */
    private Throwable throwedException;
    
    /**
     * �Ƿ�ǿ�Ʒ���
     */
    private boolean forceReturn;
    
    public FacadeResponse(Object returnValue){
        this.returnValue=returnValue;
    }
    
    public FacadeResponse(Throwable throwedException){
        this.throwedException=throwedException;
    }
    
    /**
     * ����ֵ
     * @return
     */
    public Object getReturnValue() {
        return returnValue;
    }
    
    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
    
    /**
     * ���ص��쳣
     * @return
     */
    public Throwable getThrowedException() {
        return throwedException;
    }
    
    public void setThrowedException(Throwable throwedException) {
        this.throwedException = throwedException;
    }
    
    /**
     * �Ƿ�ǿ�Ʒ���
     * @param forceReturn
     */
    public void setForceReturn(boolean forceReturn) {
        this.forceReturn = forceReturn;
    }
    
    /**
     * �Ƿ�ǿ�Ʒ���
     * @return
     */
    public boolean isForceReturn() {
        return forceReturn;
    }
    
    @Override
    public String toString() {
        if(returnValue!=null)
            return returnValue.toString();
        if(throwedException!=null)
            return throwedException.getClass().getSimpleName();
        return "";
    }
    
    
}
