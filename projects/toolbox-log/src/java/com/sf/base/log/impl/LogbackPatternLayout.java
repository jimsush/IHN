package com.sf.base.log.impl; 

import ch.qos.logback.classic.PatternLayout;

/**
 * <p>
 * Title: LogbackPatternLayout
 * </p>
 * <p>
 * Description: ��Ҫ���õ�logback.xml��
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public class LogbackPatternLayout extends PatternLayout{
    
    // ��Ϊ��logback���˰�װ������file,line�����ӡ��1��Ķ�ջ�������ǵ�0��Ķ�ջ
    
    static{
        defaultConverterMap.put("L", LogbackLineOfCallerConverter.class.getName());
        defaultConverterMap.put("line", LogbackLineOfCallerConverter.class.getName());

        defaultConverterMap.put("F", LogbackFileOfCallerConverter.class.getName());
        defaultConverterMap.put("file", LogbackFileOfCallerConverter.class.getName());
    }

}
