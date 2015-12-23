/*
 * $Id: LocalServiceManager.java, 2015-1-30 ����03:28:58 sufeng Exp $
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
 * Description:service��ģ�����local����
 * </p>
 * 
 * @author sufeng
 * created 2015-1-30 ����03:28:58
 * modified [who date description]
 * check [who date description]
 */
public final class LocalServiceManager {
    
    /** local service */
    private StaticListableBeanFactory localServices = new StaticListableBeanFactory();
    /** ���ط�������ӿڵĹ�ϵ */
    private Map<Class<?>,String> localServiceClass=new ConcurrentHashMap<Class<?>, String>();
    
    /**
     * ע�᱾�ط���
     * @param serviceName
     * @param interfaceClass
     * @param service
     */
    public void setLocalService(String serviceName, Class<?> interfaceClass, Object service) {
        localServices.addBean(serviceName, service);
        localServiceClass.put(interfaceClass, serviceName);
    }
    
    /**
     * ��ȡ���ط���
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
     * ����������ӿ����Ƿ�ƥ��
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
     * ���ݽӿڻ�ȡ���ط�����
     * @param serviceInterfaceClassName
     * @return
     */
    public String getLocalServiceNameByClassName(Class<?> serviceInterfaceClassName){
        String serviceName = localServiceClass.get(serviceInterfaceClassName);
        return serviceName;
    }
    
    /**
     * ��ע�����
     * @param serviceName
     * @param serviceItf
     */
    public void unregisterService(String serviceName, Class<?> serviceItf){
        // TODO ����֮ǰ�����ۣ�v0.2ֻ��Ҫ�ṩ�ӿڣ�����Ҫ�ṩʵ��
        throw new UnsupportedOperationException();
    }
    
}
