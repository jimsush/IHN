/*
 * $Id: CoreContextImpl.java, 2015-9-17 下午05:03:49 aaron Exp $
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
 * Description: 模块、服务管理中心
 * </p>
 * 
 * @author aaron
 * created 2015-9-17 下午05:03:49
 * modified [who date description]
 * check [who date description]
 */
public final class CoreContextImpl extends CoreContext implements CoreContext4Container {
    
    /** local service */
    private LocalServiceManager localServiceManager;
    
    /** remote service */
    private RemoteServiceManager remoteServiceManager;

    /** local通信管理器,用来获取service,message */
    private CommunicateObject local;

    /** 管理容器中产生的一些key-value对属性 */
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
     * 初始化
     */
    public static void init(){
        instance = new CoreContextImpl();
    }

    @Override
    public synchronized void setLocalService(String serviceName, Class<?> interfaceClass, Object service) {
        localServiceManager.setLocalService(serviceName, interfaceClass, service);
    }

    /**
     * 得到rmi端口号
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
     * 发送消息
     * @param messageName 消息名称，比如topo.msg，sm.msg，system.msg...
     * @param messageInfo 消息正文内容
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
     * 获取本地服务的名字（通过服务类名）
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
