package com.sf.base.log.impl;

import com.sf.base.log.def.Logger;



/**
 * <p>
 * Title: LogbackLogger
 * </p>
 * <p>
 * Description:logbackµƒlogger  ≈‰∆˜
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class LogbackLogger implements Logger{

    private org.slf4j.Logger logbackLogger;
    
    public LogbackLogger(org.slf4j.Logger logbackLogger){
        this.logbackLogger=logbackLogger;
    }
    
    @Override
    public void debug(String s) {
        logbackLogger.debug(s);
    }

    @Override
    public void debug(String s, Object obj) {
        logbackLogger.debug(s,obj);
    }

    @Override
    public void debug(String s, Object[] aobj) {
        logbackLogger.debug(s,aobj);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        logbackLogger.debug(s,throwable);
    }

    @Override
    public void debug(String s, Object obj, Object obj1) {
        logbackLogger.debug(s,obj,obj1);
    }

    @Override
    public void error(String s) {
        logbackLogger.error(s);
    }

    @Override
    public void error(String s, Object obj) {
        logbackLogger.error(s,obj);
    }

    @Override
    public void error(String s, Object[] aobj) {
        logbackLogger.error(s,aobj);
    }

    @Override
    public void error(String s, Throwable throwable) {
        logbackLogger.error(s,throwable);
    }

    @Override
    public void error(String s, Object obj, Object obj1) {
        logbackLogger.error(s,obj,obj1);
    }

    @Override
    public String getName() {
        return logbackLogger.getName();
    }

    @Override
    public void info(String s) {
        logbackLogger.info(s);
    }

    @Override
    public void info(String s, Object obj) {
        logbackLogger.info(s,obj);
    }

    @Override
    public void info(String s, Object[] aobj) {
        logbackLogger.info(s,aobj);
    }

    @Override
    public void info(String s, Throwable throwable) {
        logbackLogger.info(s,throwable);
    }

    @Override
    public void info(String s, Object obj, Object obj1) {
        logbackLogger.info(s,obj,obj1);
    }

    @Override
    public boolean isDebugEnabled() {
        return logbackLogger.isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return logbackLogger.isErrorEnabled();
    }

    @Override
    public boolean isInfoEnabled() {
        return logbackLogger.isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return logbackLogger.isTraceEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return logbackLogger.isWarnEnabled();
    }

    @Override
    public void trace(String s) {
        logbackLogger.trace(s);
    }

    @Override
    public void trace(String s, Object obj) {
        logbackLogger.trace(s,obj);
    }

    @Override
    public void trace(String s, Object[] aobj) {
        logbackLogger.trace(s,aobj);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        logbackLogger.trace(s,throwable);
    }

    @Override
    public void trace(String s, Object obj, Object obj1) {
        logbackLogger.trace(s,obj,obj1);
    }

    @Override
    public void warn(String s) {
        logbackLogger.warn(s);
    }

    @Override
    public void warn(String s, Object obj) {
        logbackLogger.warn(s,obj);
    }

    @Override
    public void warn(String s, Object[] aobj) {
        logbackLogger.warn(s,aobj);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        logbackLogger.warn(s,throwable);
    }

    @Override
    public void warn(String s, Object obj, Object obj1) {
        logbackLogger.warn(s,obj,obj1);
    }

}
