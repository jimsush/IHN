/*
 * $Id: CoreException.java, 2015-2-24 下午03:46:34 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;

import com.sf.base.exception.SfException;

/**
 * <p>
 * Title: CoreException
 * </p>
 * <p>
 * Description: 容器核心使用的异常类
 * </p>
 * 
 * @author sufeng
 * created 2015-2-24 下午03:46:34
 * modified [who date description]
 * check [who date description]
 */
public class CoreException extends SfException {

    private static final long serialVersionUID = 2298659607995553625L;
    
    /**
     * 模块加载失败 {0}
     */
    public static final int MODULE_LOAD_FAILED=1;
    
    /**
     * core模块init失败 {0}
     */
    public static final int CORE_MODULE_INIT_FAILED=2;
    
    /**
     * 接口名没有match到对应到的service name,{0}{1}
     */
    public static final int INTERFACE_NOT_MATCH_SERVICENAME=3;
    
    /**
     * 注册远程服务失败,{0}
     */
    public static final int REGISTER_REMOTE_SERVICE_FAILED=4;
    
    /**
     * 服务端口被占用,{0}
     */
    public static final int PORT_USED=5;
    
    /** 
     * spring加载失败,{0}
     */
    public static final int SPRING_LOAD_FAILED=6;
    
    /** 
     * JMS消息管理器初始化失败,{0}
     */
    public static final int JMS_MANAGER_INIT_FAILED=7;
    
    /** 
     * 获取JMS连接工厂失败,{0}
     */
    public static final int JMS_CONNECTION_FACTORY_LOOKUP_FAILED=8;
    
    /** 
     * 获取JMS topic失败,{0}
     */
    public static final int JMS_TOPIC_LOOKUP_FAILED=9;
    
    /** 
     * 创建JMS topic失败,{0}
     */
    public static final int JMS_TOPIC_INIT_FAILED=10;
    
    /**
     * 无效的session
     */
    public static final int ILLEGAL_SESSION=11;
    
    public CoreException(int errorCode, String... source) {
        super(errorCode, source);
    }
    
    public CoreException(int errorCode, Throwable th, String... source) {
        super(errorCode, th, source);
    }

    

}
