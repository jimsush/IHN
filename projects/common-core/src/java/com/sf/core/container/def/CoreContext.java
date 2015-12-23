/*
 * $Id: CoreContext.java, 2015-10-11 上午09:38:59 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;

import java.io.Serializable;

import com.sf.core.message.def.MessageMetadata;


/**
 * <p>
 * Title: CoreContext
 * </p>
 * <p>
 * Description: 模块管理器接口
 * </p>
 * 
 * @author sufeng
 * created 2015-10-11 上午09:38:59
 * modified [who date description]
 * check [who date description]
 */
public abstract class CoreContext {
    
    /**
     * 获取本地通信对象
     */
    public abstract CommunicateObject local();
    
    /**
     * 注册local服务
     * @param serviceName     服务名
     * @param interfaceClass  接口类
     * @param serviceInstance 服务实体
     */
    public abstract void setLocalService(String serviceName, Class<?> interfaceClass, Object serviceInstance);
    
    /**
     * 发布服务为remote服务
     * @param serviceName     服务名
     * @param interfaceClass  远程服务的接口类
     * @param serviceInstance 服务实体
     */
    public abstract void setRemoteService(String serviceName, Class<?> interfaceClass, Object serviceInstance);
    
    /**
     * 反注册local服务
     * @param serviceName
     * @param serviceItf
     */
    public abstract void unregisterLocalService(String serviceName, Class<?> serviceItf);
    
    /**
     * 反注册remote服务
     * @param serviceName
     * @param serviceItf
     */
    public abstract void unregisterRemoteService(String serviceName, Class<?> serviceItf);
    
    /**
     * 连接到一个远端子系统
     * @param ip
     * @param port
     */
    public abstract RemoteCommunicateObject remote(String ip, int port);
    
    /**
     * 发送消息
     * @param messageName
     * @param messageInfo
     */
    public abstract void publish(String messageName,Serializable messageInfo);
    
    /**
     * 获取属性管理器
     * @return
     */
    public abstract ContainerPropertiesManager getPropertiesManager();

    /**
     * 获取本子系统对外提供远端消息服务的消息服务器信息
     * @return 消息服务器的基本配置信息
     */
    public abstract MessageMetadata getSelfMessageMetadata();
    
    /**
     * 获取模块管理器
     * @return
     */
    public static CoreContext getInstance() {return instance;}
    protected static CoreContext instance = null;
    
}
