/*
 * $Id: CoreContextImpl.java, 2015-9-17 ����05:03:49 aaron Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.impl;

import java.io.Serializable;

import com.sf.base.exception.ExceptionUtils;
import com.sf.base.util.format.SfStringUtils;
import com.sf.core.bootstrap.def.ModuleContext;
import com.sf.core.container.def.CommunicateObject;
import com.sf.core.container.def.ContainerPropertiesManager;
import com.sf.core.container.def.CoreContext;
import com.sf.core.container.def.CoreContext4Container;
import com.sf.core.container.def.CoreException;
import com.sf.core.container.def.RemoteCommunicateObject;
import com.sf.core.message.def.MessageMetadata;
import com.sf.core.message.impl.MessageServiceManager;
import com.sf.core.message.impl.MessageServiceManagerFactory;
import com.sf.core.service.impl.ServiceManagerFactory;
import com.sf.core.service.impl.local.LocalServiceManager;
import com.sf.core.service.impl.remote.RemoteServiceManager;

/**
 * <p>
 * Title: CoreContextImpl
 * </p>
 * <p>
 * Description: ģ�顢�����������
 * </p>
 * 
 * @author aaron
 * created 2015-9-17 ����05:03:49
 * modified [who date description]
 * check [who date description]
 */
public final class CoreContextImpl extends CoreContext implements CoreContext4Container {
    
    /** local service */
    private LocalServiceManager localServiceManager;
    
    /** remote service */
    private RemoteServiceManager remoteServiceManager;

    /** localͨ�Ź�����,������ȡservice,message */
    private CommunicateObject local;

    /** ���������в�����һЩkey-value������ */
    private ContainerPropertiesManager containerPropertiesManager; 

    private int rmiPort;

    MessageServiceManager messageServiceManager;
    
    private CoreContextImpl() {
        local=new LocalCommunicateObjectImpl(this);
        messageServiceManager=MessageServiceManagerFactory.getMessageServiceManager();
        localServiceManager=ServiceManagerFactory.getLocalServiceManager();
        remoteServiceManager=ServiceManagerFactory.getRemoteServiceManager();
        containerPropertiesManager=new ContainerPropertiesManagerImpl();
    };
    
    /**
     * ��ʼ��
     */
    public static void init(){
        instance = new CoreContextImpl();
    }

    @Override
    public synchronized void setLocalService(String serviceName, Class<?> interfaceClass, Object service) {
        localServiceManager.setLocalService(serviceName, interfaceClass, service);
    }

    /**
     * �õ�rmi�˿ں�
     * @return
     */
    synchronized int getRmiPort(){
        if(rmiPort<=0){
            String port = ModuleContext.getInstance().getSystemParam(ContainerPropertiesManager.KEY_RMI_PORT);
            rmiPort=SfStringUtils.convert2T(port,Integer.class,-1);
        }
        return rmiPort;
    }
    
    @Override
    public void setRemoteService(String serviceName, Class<?> interfaceClass,Object service) {
        try{ 
            int port=getRmiPort();
            remoteServiceManager.exportRemoteService(port,serviceName, interfaceClass, service);
        }catch(Exception ex){
            throw new CoreException(CoreException.REGISTER_REMOTE_SERVICE_FAILED,ex,ExceptionUtils.getCommonExceptionInfo(ex)); 
        }
    }

    synchronized <T> T getLocalService(String serviceName,Class<T> serviceItf) {
        return localServiceManager.getLocalService(serviceName,serviceItf);
    }
    
    public RemoteServiceManager getRemoteServiceManager() {
        return remoteServiceManager;
    }
    
    @Override
    public CommunicateObject local() {
        return local;
    }

    @Override
    public RemoteCommunicateObject remote(String ip, int port) {
        RemoteCommunicateObject remote= new RemoteCommunicateObjectImpl(ip, port, this);
        return remote;
    }

    /**
     * ������Ϣ
     * @param messageName ��Ϣ���ƣ�����topo.msg��sm.msg��system.msg...
     * @param messageInfo ��Ϣ��������
     */
    @Override
    public void publish(String messageName,Serializable messageInfo){
        messageServiceManager.publish(messageName, messageInfo);
    }
    
    @Override
    public ContainerPropertiesManager getPropertiesManager() {
        return containerPropertiesManager;
    }
    
    /**
     * ��ȡ���ط�������֣�ͨ������������
     * @param serviceInterfaceClassName
     * @return
     */
    public String getLocalServiceNameByClassName(Class<?> serviceInterfaceClassName){
        return localServiceManager.getLocalServiceNameByClassName(serviceInterfaceClassName);
    }
    
    @Override
    public void setSelfMessageMetadata(MessageMetadata messageMetadata) {
        messageServiceManager.setSelfMessageMetadata(messageMetadata);
    }

    @Override
    public MessageMetadata getSelfMessageMetadata() {
        return messageServiceManager.getSelfMessageMetadata();
    }

    @Override
    public void unregisterLocalService(String serviceName, Class<?> serviceItf) {
        localServiceManager.unregisterService(serviceName, serviceItf);
    }

    @Override
    public void unregisterRemoteService(String serviceName, Class<?> serviceItf) {
        remoteServiceManager.unregisterService(serviceName, serviceItf);
    }
    
}
