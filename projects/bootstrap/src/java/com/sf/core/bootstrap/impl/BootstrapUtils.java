
package com.sf.core.bootstrap.impl;

import com.sf.base.log.def.Logger;
import com.sf.base.log.def.SimpleLogger;

/**
 * <p>
 * Title: BootstrapUtils
 * </p>
 * <p>
 * Description: bootstrap启动模块的工具类
 * </p>
 * 
 * @author sufeng
 */
public class BootstrapUtils {

    /**
     * 日志
     */
    private static Logger logger;
    
    /**
     * 缺省logger
     */
    private static Logger simpleLogger=new SimpleLogger("simple");
    
    /**
     * 获取logger
     * @return
     */
    public synchronized static Logger getLogger(){
        return logger==null ? simpleLogger : logger;
    }
    
    /**
     * 设置bootstrap使用的logger
     * @param logger
     */
    public static void setLogger(Logger logger){
        BootstrapUtils.logger=logger;
    }
    
}
