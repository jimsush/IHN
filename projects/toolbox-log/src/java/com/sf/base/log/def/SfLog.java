package com.sf.base.log.def;

import com.sf.base.log.impl.LogbackLoggerFactoryImpl;

/**
 * <p>
 * Title: SfLog
 * </p>
 * <p>
 * Description: 日志tool
 * </p>
 * 
 * @author sufeng
 */
public class SfLog {

    /**
     * 是否初始化过
     */
    private static volatile boolean inited=false;
    
    /**
     * 防止多线程重复init log
     */
    private static final byte[] monitor=new byte[0];
    
    /**
     * log实现
     */
    private static LogbackLoggerFactoryImpl factory;
    
    /**
     * 初始化日志
     * @param logbackConfigFileLocation logback.xml的文件位置,可以是相对路径
     * 比如bootstrap/conf/logback-server.xml,也可以是URL路径
     * @param defaultLogName 缺省日志名
     */
    public static void initLogging(String logbackConfigFileLocation,String defaultLogName){
        synchronized (monitor) {
            factory=new LogbackLoggerFactoryImpl(logbackConfigFileLocation,defaultLogName);
            inited=true;
        }
    }
    
    /**
     * 得到日志
     * @param logName
     * @return logger
     */
    public static Logger getLogger(String logName){
        if(!inited)
            throw new IllegalStateException("need invoke initLogging first");
        return factory.getLogger(logName);
    }
    
    /**
     * 拿到缺省日志
     * @return 总能保证获取到一个日志
     */
    public static Logger getDefaultLogger(){
        if(!inited)
            throw new IllegalStateException("need invoke initLogging first");
        return factory.getDefaultLogger();
    }
    
}
