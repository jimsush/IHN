package com.sf.base.log.impl;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import com.sf.base.log.def.Logger;
import com.sf.base.log.def.SimpleLogger;

/**
 * <p>
 * Title: LogbackLoggerFactoryImpl
 * </p>
 * <p>
 * Description:使用logback的日志工厂实现类
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class LogbackLoggerFactoryImpl {

    /** logback的上下文 */
    private LoggerContext lc;
    
    /** 缺省的最简单的日志实现 */
    private Logger simpleLogger = new SimpleLogger("simple");
    
    /** 子系统缺省日志 */
    private Logger defaultLogger;
    
    /** 子系统缺省日志名 */
    private String defaultLoggerName;
    
    /**
     * 日志的cache
     */
    private Map<String,Logger> allLogs=new ConcurrentHashMap<String,Logger>();
    
    /**
     * 构造日志工厂类
     * @param loggerConfigFile logback.xml的全路径
     * @param defaultLoggerName 
     */
    public LogbackLoggerFactoryImpl(String loggerConfigFile,String defaultLoggerName){
        this.defaultLoggerName=defaultLoggerName;
        
        // 在指定位置去找
        URL url =null;
        try {
            url =new URL(loggerConfigFile);
        } catch (Exception ex) {
            System.out.println(loggerConfigFile+" is not a URL");
        }
        if(url==null){
            try {
                url =getUrlFromFile(loggerConfigFile);
            } catch (Exception ex) {
                throw new RuntimeException(loggerConfigFile+" does not exist");
            }
        }
        reset(url);
    }
    
    /**
     * 获取URL
     * @param fileFullPath
     * @return
     * @throws Exception
     */
    private URL getUrlFromFile(String fileFullPath) throws Exception {
        File f = new File(fileFullPath);
        if (f.exists()) {
            URL url = f.toURI().toURL();
            return url;
        }
        return null;
    }
    
    /**
     * 重新设置日志
     * 
     * @param url 日志配置文件路径
     */
    private void reset(URL url) {
        simpleLogger.info("LogFactory reset :" + url);
        try {
            String name = url.toString();
            int index = name.lastIndexOf(".");
            URL testurl = new URL(new StringBuilder(name.substring(0, index)).append(name.substring(index)).toString());
            testurl.openConnection().connect();
            url = testurl;
        } catch (Exception e) {
            String errorInfo="[error] "+e.getClass().getSimpleName()+","+e.getMessage();
            simpleLogger.info(errorInfo);
        }
        simpleLogger.info("LogFactory configure :" + url);
        lc = (LoggerContext) LoggerFactory.getILoggerFactory();

        // 调用logback的api来初始化logger
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            lc.shutdownAndReset();
            configurator.setContext(lc);
            configurator.doConfigure(url);
            simpleLogger.info("LogFactory reset successful ...");
        } catch (JoranException ex) {
            StatusPrinter.print(lc.getStatusManager());
        }
        
        // 设置一下defaultLogger
        defaultLogger=getLogger(defaultLoggerName);
    }

    public Logger getLogger(String logName) {
        Logger theLogger = allLogs.get(logName);
        if(theLogger!=null)
            return theLogger;
        
        //还没有这个日志，就创建一个
        ch.qos.logback.classic.Logger logger = lc.getLogger(logName);
        Logger ourLogger=new LogbackLogger(logger);
        allLogs.put(logName,ourLogger);
        return ourLogger;
    }

    public Logger getDefaultLogger() {
        return defaultLogger==null ? simpleLogger : defaultLogger;
    }

}
