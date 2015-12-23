/*
 * $Id: ContainerPropertiesManager.java, 2015-10-11 上午09:36:10 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;

import java.util.Properties;

/**
 * <p>
 * Title: ContainerPropertiesManager
 * </p>
 * <p>
 * Description: 容器属性管理,用来读写一些key-value的属性对
 * </p>
 * 
 * @author sufeng
 * created 2015-10-11 上午09:36:10
 * modified [who date description]
 * check [who date description]
 */
public interface ContainerPropertiesManager {

    /**
     * 当前子系统运行的当前目录
     */
    public static final String KEY_HOME_DIR="homeDir";
    
    /**
     * 提供远程服务的IP地址
     */
    public static final String KEY_SERVER_IP="server.ip";
    
    /**
     * 提供远程服务的端口号
     */
    public static final String KEY_RMI_PORT="rmi.port";
    
    /**
     * 提供远程消息服务器的ip,可选
     */
    public static final String KEY_JMS_IP="jms.ip";
    
    /**
     * 提供远程消息服务的端口号
     */
    public static final String KEY_JMS_PORT="jms.port";
    
    /**
     * 提供远程消息名字服务端口号
     */
    public static final String KEY_JMS_NAMING_PORT="jms.naming.port";
    
    /**
     * 是否需要启动JMS消息服务器:ture/false
     */
    public static final String KEY_JMS_LAUNCH="jms.server.launch";
    
    /**
     * JMS SERVER manager的实现类名
     */
    public static final String KEY_JMS_MANAGER_CLASS="jms.manager.class";
    
    /**
     * java.naming.factory.initial
     */
    public static final String KEY_JMS_FACTORY_CLASS="java.naming.factory.initial";
    
    /**
     * DB SERVER manager的实现类名
     */
    public static final String KEY_DB_MANAGER_CLASS="db.manager.class";
    
    /**
     * 当前子系统可以发出的消息名列表
     */
    public static final String KEY_SUBSYSTEM_MESSAGE_NAMES="subsystem.message";
    
    /**
     * 缺省服务端IP地址
     */
    public static final String VALUE_DEFAULT_SERVER_IP="127.0.0.1";
    
    /**
     * 缺省远程服务端口号
     */
    public static final String VALUE_DEFAULT_RMI_PORT="8888";
    
    /**
     * 缺省远端消息服务端口
     */
    public static final String VALUE_DEFAULT_JMS_PORT="16010";
    
    /**
     * 缺省远端消息名字服务端口号
     */
    public static final String VALUE_DEFAULT_JMS_NAMING_PORT="16400";
    
    /**
     * 设置属性(都在Module的before中put)
     * @param key
     * @param value
     */
    public void put(String key,String value);
    
    /**
     * 获取属性
     * @param key
     * @return
     */
    public String get(String key);
    
    /**
     * 得到当前所有属性对
     * @return
     */
    public Properties getProps();

}
