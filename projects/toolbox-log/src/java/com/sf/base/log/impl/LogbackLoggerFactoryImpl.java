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
 * Description:ʹ��logback����־����ʵ����
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class LogbackLoggerFactoryImpl {

    /** logback�������� */
    private LoggerContext lc;
    
    /** ȱʡ����򵥵���־ʵ�� */
    private Logger simpleLogger = new SimpleLogger("simple");
    
    /** ��ϵͳȱʡ��־ */
    private Logger defaultLogger;
    
    /** ��ϵͳȱʡ��־�� */
    private String defaultLoggerName;
    
    /**
     * ��־��cache
     */
    private Map<String,Logger> allLogs=new ConcurrentHashMap<String,Logger>();
    
    /**
     * ������־������
     * @param loggerConfigFile logback.xml��ȫ·��
     * @param defaultLoggerName 
     */
    public LogbackLoggerFactoryImpl(String loggerConfigFile,String defaultLoggerName){
        this.defaultLoggerName=defaultLoggerName;
        
        // ��ָ��λ��ȥ��
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
     * ��ȡURL
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
     * ����������־
     * 
     * @param url ��־�����ļ�·��
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

        // ����logback��api����ʼ��logger
        try {
            JoranConfigurator configurator = new JoranConfigurator();
            lc.shutdownAndReset();
            configurator.setContext(lc);
            configurator.doConfigure(url);
            simpleLogger.info("LogFactory reset successful ...");
        } catch (JoranException ex) {
            StatusPrinter.print(lc.getStatusManager());
        }
        
        // ����һ��defaultLogger
        defaultLogger=getLogger(defaultLoggerName);
    }

    public Logger getLogger(String logName) {
        Logger theLogger = allLogs.get(logName);
        if(theLogger!=null)
            return theLogger;
        
        //��û�������־���ʹ���һ��
        ch.qos.logback.classic.Logger logger = lc.getLogger(logName);
        Logger ourLogger=new LogbackLogger(logger);
        allLogs.put(logName,ourLogger);
        return ourLogger;
    }

    public Logger getDefaultLogger() {
        return defaultLogger==null ? simpleLogger : defaultLogger;
    }

}
