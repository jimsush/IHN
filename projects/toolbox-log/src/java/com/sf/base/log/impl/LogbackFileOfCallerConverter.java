package com.sf.base.log.impl;

import ch.qos.logback.classic.pattern.FileOfCallerConverter;
import ch.qos.logback.classic.spi.CallerData;
import ch.qos.logback.classic.spi.LoggingEvent;

/**
 * <p>
 * Title: LogbackFileOfCallerConverter
 * </p>
 * <p>
 * Description:从第1层堆栈中获取file name
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public class LogbackFileOfCallerConverter extends FileOfCallerConverter {

    public String convert(LoggingEvent le) {
        CallerData[] callerData = le.getCallerData();
        if (callerData != null && callerData.length>0) {
            if(callerData.length > 1)
                return callerData[1].getFileName(); //因为做了包装,需要取堆栈[1]的信息
            else
                return callerData[0].getFileName();
        } else {
            return CallerData.NA;
        }
    }

}
