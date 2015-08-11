package com.ihn.server.util.logging;


import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * simple logger for better testing purpose
 * @author tong
 *
 */
public class SimpleLogger implements Logger{

    @Override
    public void debug(String s) {
        System.out.println(s);
    }

    @Override
    public void debug(String s, Object obj) {
    	out(s,obj);
    }

    @Override
    public void debug(String s, Object[] aobj) {
    }

    @Override
    public void debug(String s, Throwable throwable) {
        System.out.println(s);
        throwable.printStackTrace();
    }

    @Override
    public void debug(Marker marker, String s) {
    }

    @Override
    public void debug(String s, Object obj, Object obj1) {
    }

    @Override
    public void debug(Marker marker, String s, Object obj) {
    }

    @Override
    public void debug(Marker marker, String s, Object[] aobj) {
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
    }

    @Override
    public void debug(Marker marker, String s, Object obj, Object obj1) {
    }

    @Override
    public void error(String s) {
        System.out.println(s);
    }

    @Override
    public void error(String s, Object obj) {
    	out(s,obj);
    }

    @Override
    public void error(String s, Object[] aobj) {
    }

    @Override
    public void error(String s, Throwable throwable) {
        System.out.println(s);
        throwable.printStackTrace();
    }

    @Override
    public void error(Marker marker, String s) {
    }

    @Override
    public void error(String s, Object obj, Object obj1) {
    }

    @Override
    public void error(Marker marker, String s, Object obj) {
    }

    @Override
    public void error(Marker marker, String s, Object[] aobj) {
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
    }

    @Override
    public void error(Marker marker, String s, Object obj, Object obj1) {
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void info(String s) {
        System.out.println(s);
    }

    @Override
    public void info(String s, Object obj) {
    	out(s,obj);
    }

    @Override
    public void info(String s, Object[] aobj) {
    }

    @Override
    public void info(String s, Throwable throwable) {
        System.out.println(s);
        throwable.printStackTrace();
    }

    @Override
    public void info(Marker marker, String s) {
    }

    @Override
    public void info(String s, Object obj, Object obj1) {
    }

    @Override
    public void info(Marker marker, String s, Object obj) {
    }

    @Override
    public void info(Marker marker, String s, Object[] aobj) {
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
    }

    @Override
    public void info(Marker marker, String s, Object obj, Object obj1) {
    }

    @Override
    public boolean isDebugEnabled() {
        return true;
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public boolean isTraceEnabled() {
        return false;
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return true;
    }

    @Override
    public void trace(String s) {
        System.out.println(s);
    }

    @Override
    public void trace(String s, Object obj) {
    	out(s,obj);
    }

    @Override
    public void trace(String s, Object[] aobj) {
    }

    @Override
    public void trace(String s, Throwable throwable) {
        System.out.println(s);
        throwable.printStackTrace();
    }

    @Override
    public void trace(Marker marker, String s) {
    }

    @Override
    public void trace(String s, Object obj, Object obj1) {
    }

    @Override
    public void trace(Marker marker, String s, Object obj) {
    }

    @Override
    public void trace(Marker marker, String s, Object[] aobj) {
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
    }

    @Override
    public void trace(Marker marker, String s, Object obj, Object obj1) {
    }

    @Override
    public void warn(String s) {
        System.out.println(s);
    }

    @Override
    public void warn(String s, Object obj) {
    	out(s,obj);
    }

    @Override
    public void warn(String s, Object[] aobj) {
    }

    @Override
    public void warn(String s, Throwable throwable) {
        System.out.println(s);
        throwable.printStackTrace();
    }

    @Override
    public void warn(Marker marker, String s) {
    }

    @Override
    public void warn(String s, Object obj, Object obj1) {
    }

    @Override
    public void warn(Marker marker, String s, Object obj) {
    }

    @Override
    public void warn(Marker marker, String s, Object[] aobj) {
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
    }

    @Override
    public void warn(Marker marker, String s, Object obj, Object obj1) {
    }
    
    public void out(String s, Object obj){
    	if(obj==null)
    		System.out.println(s);
    	else
    		System.out.println(s+",obj="+obj.toString());
    }
    
}
