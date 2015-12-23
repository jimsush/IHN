package com.sf.base.log.def;


/**
 * <p>
 * Title: Logger
 * </p>
 * <p>
 * Description: 日志
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public interface Logger {

    /**
     * 调试输出
     * @param s
     */
    public void debug(String s);

    public void debug(String s, Object obj);

    public void debug(String s, Object[] aobj);

    public void debug(String s, Throwable throwable);

    public void debug(String s, Object obj, Object obj1);

    public void error(String s);

    public void error(String s, Object obj);

    public void error(String s, Object[] aobj);

    public void error(String s, Throwable throwable);

    public void error(String s, Object obj, Object obj1);

    public String getName();
    
    public void info(String s);

    public void info(String s, Object obj);

    public void info(String s, Object[] aobj);

    public void info(String s, Throwable throwable);

    public void info(String s, Object obj, Object obj1);

    public boolean isDebugEnabled();

    public boolean isErrorEnabled();

    public boolean isInfoEnabled();

    public boolean isTraceEnabled();

    public boolean isWarnEnabled();

    public void trace(String s);

    public void trace(String s, Object obj);

    public void trace(String s, Object[] aobj);

    public void trace(String s, Throwable throwable);

    public void trace(String s, Object obj, Object obj1);

    public void warn(String s);

    public void warn(String s, Object obj);

    public void warn(String s, Object[] aobj);

    public void warn(String s, Throwable throwable);

    public void warn(String s, Object obj, Object obj1);

}
