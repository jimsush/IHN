/*
 * $Id: DefaultRemoteServiceManagerImpl.java, 2015-1-31 ����03:10:34 sufeng Exp $
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
 * Description: ��ͨ��facade,proxyģʽ��Զ�̷����������ʵ��
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 ����03:10:34
 * modified [who date description]
 * check [who date description]
 */
public class DefaultRemoteServiceManagerImpl implements RemoteServiceManager{
    
    @Override
    public void init() {
    }
    
    /**
     * �ͷ��񵼳�ΪRMI����
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
     * ��ȡԶ�̷���(�˴������棬��ȫ���ϲ���cache)
     * @param remoteCommunicationObject
     * @param serviceName
     * @param serviceItf ����ӿ�����
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
        // TODO ����֮ǰ�����ۣ�v0.2ֻ��Ҫ�ṩ�ӿڣ�����Ҫ�ṩʵ��
        throw new UnsupportedOperationException();
    }
    
}
