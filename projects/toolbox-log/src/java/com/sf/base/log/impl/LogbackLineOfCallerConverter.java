package com.sf.base.log.impl;

import ch.qos.logback.classic.pattern.LineOfCallerConverter;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * <p>
 * Title: LogbackLineOfCallerConverter
 * </p>
 * <p>
 * Description:logback�Ե����߽����˴���,�ӵ�1���ջ�л�ȡline
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public class LogbackLineOfCallerConverter extends LineOfCallerConverter{
    
    @Override
    public String convert(LoggingEvent le) {
        CallerData[] callerData = le.getCallerData();
        if (callerData != null && callerData.length>0) {
            if(callerData.length > 1)
                return Integer.toString(callerData[1].getLineNumber());//��Ϊ���˰�װ,��Ҫȡ��ջ[1]����Ϣ
            else
                return Integer.toString(callerData[0].getLineNumber());
        } else {
            return CallerData.NA;
        }
    }
    
}
