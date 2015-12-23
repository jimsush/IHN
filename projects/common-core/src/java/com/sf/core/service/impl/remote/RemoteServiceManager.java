/*
 * $Id: RemoteServiceManager.java, 2015-1-31 下午12:22:10 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.service.impl.remote;

import com.sf.core.container.def.RemoteCommunicateObject;

/**
 * <p>
 * Title: RemoteServiceManager
 * </p>
 * <p>
 * Description: service子模块管理远程服务
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 下午12:22:10
 * modified [who date description]
 * check [who date description]
 */
public interface RemoteServiceManager {

    /**
     * 初始化远端服务管理器
     */
    public void init();
    
    /**
     * 就服务导出为RMI服务
     * @param rmiPort
     * @param serviceName
     * @param interfaceClass
     * @param service
     */
    public void exportRemoteService(int rmiPort,String serviceName, Class<?> interfaceClass,Object service);

    /**
     * 获取远程服务(此处不缓存，完全由上层来cache)
     * @param remoteCommunicationObject
     * @param serviceName
     * @param serviceItf 服务接口类名
     * 
     */
    public <T> T getRemoteService(RemoteCommunicateObject remoteCommunicationObject,String serviceName, Class<T> serviceItf);
    
    /**
     * 反注册服务
     * @param serviceName
     * @param serviceItf
     */
    public void unregisterService(String serviceName, Class<?> serviceItf);
    
}
