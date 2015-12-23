package com.sf.core.bootstrap.def;

/**
 * <p>
 * Title: ContainerConst
 * </p>
 * <p>
 * Description: bootstrap用到的常量和工具方法
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public class ContainerConst {
    
    /**
     * 子系统主目录
     */
    public static final String KEY_HOME_DIR="homeDir";
    
    /**
     * param key:配置logback文件的路径,其value一般配置为conf/logback.xml，以modules/xxcore/为根
     */
    public static final String PARAM_KEY_LOGGER_LOCATION="loggerConfigLocation";
    
    /**
     * param key:配置logback文件的路径,其value一般配置为conf/logback.xml，以modules/xxcore/为根
     */
    public static final String PARAM_KEY_DEFAULT_LOGGER="defaultLoggerName";
    
    /**
     * 退出系统
     * @param status
     */
    public static void exitSystem(int status){
        System.out.println("subsystem exit.");
        System.exit(status);
    }

}
