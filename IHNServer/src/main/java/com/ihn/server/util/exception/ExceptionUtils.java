package com.ihn.server.util.exception;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ExceptionUtils {

	public static final int EX_LEVEL_SIMPLE = 0; // simplest level
    public static final int EX_LEVEL_DETAIL = 1; // detail, with cause
    public static final int EX_LEVEL_STACK = 2; //  with stack trace
    public static final int EX_LEVEL_FULL = 3; //  very detailed info
    public static final int EX_LEVEL_CODEMESSAGE=4;

    /**
     * switch exception to String
     * @param ex
     * @return
     */
    public static String exToString(Exception ex) {
        if(ex instanceof IHNException){
            return exToString((IHNException)ex,EX_LEVEL_SIMPLE);
        }else{
            return org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(ex);
        }
    }
    
    public static String getExFirstStackInfo(Exception ex){
      //  if(ex.getMessage()!=null)
      //      return ex.getMessage();
        String clazz=ex.getClass().getSimpleName();
        StackTraceElement[] traces = ex.getStackTrace();
        if(traces==null || traces.length==0)
            return clazz;
        return clazz+": "+traces[0].toString();
    }
    
    /**
     * IHNException toString
     * @param ex
     * @param level
     * @return
     */
    public static String exToString(IHNException ex, int level) {
        String str = null;
        switch (level) {
        case EX_LEVEL_SIMPLE:
            str = getSimpleString(ex);
            break;
        case EX_LEVEL_STACK:
            str = getStackString(ex);
            break;
        default:
            str = getSimpleString(ex);
            break;
        }
        return str;
    }

    /**
     * get basic information of exception
     * 
     * @param ex
     * @return
     */
    public static String getSimpleString(IHNException ex) {
        if (ex == null)
            return null;

        ToStringBuilder tsb = new ToStringBuilder(ex, ToStringStyle.MULTI_LINE_STYLE).append("catalog",
                ex.getClass().getSimpleName()).append("code", ex.getErrorCode()).append("message",
                ex.getMessage());
        if (ex.getCause() != null)
            tsb.append("cause", ex.getCause().getMessage());
        return tsb.toString();
    }

    /**
     * get detailed exception information, includes stack
     * 
     * @param ex
     * @return
     */
    public static String getStackString(IHNException ex) {
        if (ex == null)
            return null;

        ToStringBuilder tsb = new ToStringBuilder(ex, ToStringStyle.MULTI_LINE_STYLE).append("catalog",
                ex.getClass().getSimpleName()).append("code", ex.getErrorCode()).append("message",
                ex.getMessage());
        if (ex.getCause() != null) {
            String fullCause = org.apache.commons.lang.exception.ExceptionUtils.getFullStackTrace(ex
                    .getCause());
            tsb.append("cause", fullCause);
        }
        return tsb.toString();
    }

    /**
     * get common exception info,include class,stack...
     * @param ex
     * @return
     */
    public static String getCommonExceptionInfo(Throwable arg){
    	Throwable th=arg;
    	if(arg instanceof ExecutionException){
    		ExecutionException eEx=(ExecutionException)arg;
    		th = eEx.getCause();
    	}else if(arg instanceof InvocationTargetException){
    		InvocationTargetException iEx=(InvocationTargetException)arg;
    		th=iEx.getTargetException();
    	}
    	if(th==null)
    		return null;
    	
        StringBuilder sb=new StringBuilder();
        sb.append("class=").append(th.getClass().getSimpleName()).append(",");
        if(th.getMessage()!=null)
            sb.append("message=").append(th.getMessage()).append(",");
        if(th instanceof IHNException){
        	IHNException ihnEx=(IHNException)th;
            sb.append("code=").append(ihnEx.getErrorCode()).append(",");
            String[] src = ihnEx.getSource();
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

    
}
