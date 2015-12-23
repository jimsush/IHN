package com.sf.base.log.def;

import com.sf.base.log.impl.LogbackLoggerFactoryImpl;

/**
 * <p>
 * Title: SfLog
 * </p>
 * <p>
 * Description: ��־tool
 * </p>
 * 
 * @author sufeng
 */
public class SfLog {

    /**
     * �Ƿ��ʼ����
     */
    private static volatile boolean inited=false;
    
    /**
     * ��ֹ���߳��ظ�init log
     */
    private static final byte[] monitor=new byte[0];
    
    /**
     * logʵ��
     */
    private static LogbackLoggerFactoryImpl factory;
    
    /**
     * ��ʼ����־
     * @param logbackConfigFileLocation logback.xml���ļ�λ��,���������·��
     * ����bootstrap/conf/logback-server.xml,Ҳ������URL·��
     * @param defaultLogName ȱʡ��־��
     */
    public static void initLogging(String logbackConfigFileLocation,String defaultLogName){
        synchronized (monitor) {
            factory=new LogbackLoggerFactoryImpl(logbackConfigFileLocation,defaultLogName);
            inited=true;
        }
    }
    
    /**
     * �õ���־
     * @param logName
     * @return logger
     */
    public static Logger getLogger(String logName){
        if(!inited)
            throw new IllegalStateException("need invoke initLogging first");
        return factory.getLogger(logName);
    }
    
    /**
     * �õ�ȱʡ��־
     * @return ���ܱ�֤��ȡ��һ����־
     */
    public static Logger getDefaultLogger(){
        if(!inited)
            throw new IllegalStateException("need invoke initLogging first");
        return factory.getDefaultLogger();
    }
    
}
