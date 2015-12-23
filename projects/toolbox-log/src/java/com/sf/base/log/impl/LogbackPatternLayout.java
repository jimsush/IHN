package com.sf.base.log.impl; 

import ch.qos.logback.classic.PatternLayout;

/**
 * <p>
 * Title: LogbackPatternLayout
 * </p>
 * <p>
 * Description: 需要配置到logback.xml中
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class LogbackPatternLayout extends PatternLayout{
    
    // 因为对logback做了包装，导致file,line必须打印第1层的堆栈，而不是第0层的堆栈
    
    static{
        defaultConverterMap.put("L", LogbackLineOfCallerConverter.class.getName());
        defaultConverterMap.put("line", LogbackLineOfCallerConverter.class.getName());

        defaultConverterMap.put("F", LogbackFileOfCallerConverter.class.getName());
        defaultConverterMap.put("file", LogbackFileOfCallerConverter.class.getName());
    }

}
