/*
 * $Id: DefaultRemoteServiceManagerImpl.java, 2015-1-31 下午03:10:34 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.service.impl.remote;

import com.sf.base.exception.ExceptionUtils;
import com.sf.core.container.def.CoreException;
import com.sf.core.container.def.RemoteCommunicateObject;
import com.sf.core.service.impl.remote.exporter.RemoteServiceExporter;
import com.sf.core.service.impl.remote.importer.RemoteServiceProxyFactory;

/**
 * <p>
 * Title: DefaultRemoteServiceManagerImpl
 * </p>
 * <p>
 * Description: 不通过facade,proxy模式的远程服务管理器的实现
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 下午03:10:34
 * modified [who date description]
 * check [who date description]
 */
public class DefaultRemoteServiceManagerImpl implements RemoteServiceManager{
    
    @Override
    public void init() {
    }
    
    /**
     * 就服务导出为RMI服务
     * @param rmiPort
     * @param serviceName
     * @param interfaceClass
     * @param service
     */
    public void exportRemoteService(int rmiPort,String serviceName, Class<?> interfaceClass,Object service) {
        try{ 
            RemoteServiceExporter.register(interfaceClass, service, rmiPort);
        }catch(Exception ex){
            throw new CoreException(CoreException.REGISTER_REMOTE_SERVICE_FAILED,ex,ExceptionUtils.getCommonExceptionInfo(ex)); 
        }
    }

    /**
     * 获取远程服务(此处不缓存，完全由上层来cache)
     * @param remoteCommunicationObject
     * @param serviceName
     * @param serviceItf 服务接口类名
     * 
     */
    public synchronized <T> T getRemoteService(RemoteCommunicateObject remoteCommunicationObject,String serviceName, Class<T> serviceItf) {
        String remoteIp=remoteCommunicationObject.getRemoteIp();
        int remoteServerPort=remoteCommunicationObject.getRemoteServerPort();
        T obj = RemoteServiceProxyFactory.getRemoteService(remoteIp,remoteServerPort,serviceItf);
        return obj;
    }
    
    @Override
    public void unregisterService(String serviceName, Class<?> serviceItf) {
        // TODO 根据之前的讨论，v0.2只需要提供接口，不需要提供实现
        throw new UnsupportedOperationException();
    }
    
}
