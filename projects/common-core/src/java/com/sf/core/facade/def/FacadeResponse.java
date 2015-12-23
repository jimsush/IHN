/*
 * $Id: FacadeResponse.java, 2015-3-4 上午10:10:16 sufeng Exp $
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
 * Description: facade调用中返回对象,包括返回值与异常,是否需要强制返回,类似于holder
 * </p>
 * 
 * @author sufeng
 * created 2015-3-4 上午10:10:16
 * modified [who date description]
 * check [who date description]
 */
public class FacadeResponse {

    /**
     * 返回值
     */
    private Object returnValue;
    
    /**
     * 抛出的异常
     */
    private Throwable throwedException;
    
    /**
     * 是否强制返回
     */
    private boolean forceReturn;
    
    public FacadeResponse(Object returnValue){
        this.returnValue=returnValue;
    }
    
    public FacadeResponse(Throwable throwedException){
        this.throwedException=throwedException;
    }
    
    /**
     * 返回值
     * @return
     */
    public Object getReturnValue() {
        return returnValue;
    }
    
    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
    
    /**
     * 返回的异常
     * @return
     */
    public Throwable getThrowedException() {
        return throwedException;
    }
    
    public void setThrowedException(Throwable throwedException) {
        this.throwedException = throwedException;
    }
    
    /**
     * 是否强制返回
     * @param forceReturn
     */
    public void setForceReturn(boolean forceReturn) {
        this.forceReturn = forceReturn;
    }
    
    /**
     * 是否强制返回
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
