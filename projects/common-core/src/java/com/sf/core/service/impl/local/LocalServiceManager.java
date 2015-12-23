/*
 * $Id: LocalServiceManager.java, 2015-1-30 下午03:28:58 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.service.impl.local;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.support.StaticListableBeanFactory;

import com.sf.core.container.def.CoreException;

/**
 * <p>
 * Title: LocalServiceManager
 * </p>
 * <p>
 * Description:service子模块管理local服务
 * </p>
 * 
 * @author sufeng
 * created 2015-1-30 下午03:28:58
 * modified [who date description]
 * check [who date description]
 */
public final class LocalServiceManager {
    
    /** local service */
    private StaticListableBeanFactory localServices = new StaticListableBeanFactory();
    /** 本地服务名与接口的关系 */
    private Map<Class<?>,String> localServiceClass=new ConcurrentHashMap<Class<?>, String>();
    
    /**
     * 注册本地服务
     * @param serviceName
     * @param interfaceClass
     * @param service
     */
    public void setLocalService(String serviceName, Class<?> interfaceClass, Object service) {
        localServices.addBean(serviceName, service);
        localServiceClass.put(interfaceClass, serviceName);
    }
    
    /**
     * 获取本地服务
     * @param <T>
     * @param serviceName
     * @param serviceItf
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getLocalService(String serviceName,Class<T> serviceItf) {
        checkService(serviceName,serviceItf);
        
        return (T) localServices.getBean(serviceName, serviceItf);
    }
    
    /**
     * 检查服务名与接口名是否匹配
     * @param <T>
     * @param serviceName
     * @param serviceItf
     */
    private <T> void checkService(String serviceName,Class<T> serviceItf){
        String serviceNameInContainer = localServiceClass.get(serviceItf);
        if(!serviceName.equals(serviceNameInContainer)){
            throw new CoreException(CoreException.INTERFACE_NOT_MATCH_SERVICENAME,serviceItf.getSimpleName(),serviceName);
        }
    }
    
    /**
     * 根据接口获取本地服务名
     * @param serviceInterfaceClassName
     * @return
     */
    public String getLocalServiceNameByClassName(Class<?> serviceInterfaceClassName){
        String serviceName = localServiceClass.get(serviceInterfaceClassName);
        return serviceName;
    }
    
    /**
     * 反注册服务
     * @param serviceName
     * @param serviceItf
     */
    public void unregisterService(String serviceName, Class<?> serviceItf){
        // TODO 根据之前的讨论，v0.2只需要提供接口，不需要提供实现
        throw new UnsupportedOperationException();
    }
    
}
