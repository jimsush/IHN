/*
 * $Id: ExceptionUtils.java, 2015-9-17 ����03:40:51 sufeng Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * <p>
 * Title: ExceptionUtils
 * </p>
 * <p>
 * Description: �쳣����������
 * </p>
 * 
 * @author sufeng
 * created 2015-2-21 ����03:29:13
 * modified [who date description]
 * check [who date description]
 */
public class ExceptionUtils {
    
    /**
     * �õ�δ��װ��ԭʼ�쳣
     * @param ex �쳣
     * @return
     */
    public static Throwable getRawThrowable(Throwable ex){
        if(ex==null)
            return null;
        if(ex instanceof ExecutionException){
            ExecutionException eEx=(ExecutionException)ex;
            return eEx.getCause();
        }else if(ex instanceof InvocationTargetException){
            InvocationTargetException iEx=(InvocationTargetException)ex;
            return iEx.getTargetException();
        }
        return ex;
    }
    
    /**
     * �õ�һ���쳣�ļ���Ϣ������class����message����һ���ջ
     * @param ex �쳣
     * @return
     */
    public static String getCommonExceptionInfo(Throwable arg){
        Throwable th=getRawThrowable(arg);
        if(th==null)
            return null;
        
        StringBuilder sb=new StringBuilder();
        sb.append("class=").append(th.getClass().getSimpleName()).append(",");
        if(th.getMessage()!=null)
            sb.append("message=").append(th.getMessage()).append(",");
        if(th instanceof SfException){
            SfException sfEx=(SfException)th;
            sb.append("code=").append(sfEx.getErrorCode()).append(",");
            String[] src = sfEx.getSource();
            if(src!=null && src.length>0){
                sb.append("source=");
                for(int i=0; i<src.length; i++){
                    sb.append(src[i]).append(",");
                }
            }
        }
        StackTraceElement[] traces = th.getStackTrace();
        if(traces==null || traces.length==0)
            return sb.toString();
        
        sb.append("1st stack=").append(traces[0]);
        return sb.toString();
    }
    
    /**
     * �쳣�ĵ�һ���ջ��Ϣ
     * @param ex �쳣
     * @return
     */
    public static String getExFirstStackInfo(Exception ex){
        Throwable th = getRawThrowable(ex);
        String clazz=th.getClass().getSimpleName();
        StackTraceElement[] traces = th.getStackTrace();
        if(traces==null || traces.length==0)
            return clazz;
        return clazz+": "+traces[0].toString();
    }

    /**
     * �õ��쳣�Ļ�����Ϣ
     * @param ex �쳣
     * @return
     */
    public static String getSimpleString(Exception ex) {
        if (ex == null)
            return null;

        Throwable th = getRawThrowable(ex);
        ToStringBuilder tsb =null;
        if(th instanceof SfException){
            SfException sfEx=(SfException)th;
            tsb = new ToStringBuilder(th, ToStringStyle.MULTI_LINE_STYLE).append("catalog",
                th.getClass().getSimpleName()).append("code", sfEx.getErrorCode()).append("message",
                th.getMessage());
        }else{
            tsb = new ToStringBuilder(th, ToStringStyle.MULTI_LINE_STYLE).append("catalog",
                    th.getClass().getSimpleName()).append("message",
                    th.getMessage());
        }
        
        if (ex.getCause() != null)
            tsb.append("cause", ex.getCause().getMessage());
        return tsb.toString();
    }

    /**
     * �õ��쳣����ϸ��Ϣ:����stack
     * @param ex �쳣
     * @return
     */
    public static String getStackString(Exception ex) {
        if (ex == null)
            return null;

        Throwable th = getRawThrowable(ex);
        ToStringBuilder tsb = null;
        if(th instanceof SfException){
            SfException sfEx=(SfException)th;
            tsb=new ToStringBuilder(th, ToStringStyle.MULTI_LINE_STYLE).append("catalog",
                    th.getClass().getSimpleName()).append("code", sfEx.getErrorCode()).append("message",
                    th.getMessage());
        }else{
            tsb=new ToStringBuilder(th, ToStringStyle.MULTI_LINE_STYLE).append("catalog",
                    th.getClass().getSimpleName()).append("message",th.getMessage());
        }
        
        if (ex.getCause() != null) {
            String fullCause = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(ex
                    .getCause());
            tsb.append("cause", fullCause);
        }
        
        return tsb.toString();
    }


}
