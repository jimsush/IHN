/*
 * $Id: ProxyFacadeRemoteServiceManager.java, 2015-1-31 下午12:47:42 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.proxy.impl;

import com.sf.base.exception.ExceptionUtils;
import com.sf.base.log.def.Logger;
import com.sf.base.log.def.SfLog;
import com.sf.core.container.def.CoreContext;
import com.sf.core.container.def.RemoteCommunicateObject;
import com.sf.core.facade.def.RemoteInvocationFacade;
import com.sf.core.proxy.def.ProxyManager;
import com.sf.core.service.impl.local.LocalServiceManager;
import com.sf.core.service.impl.remote.DefaultRemoteServiceManagerImpl;
import com.sf.core.service.impl.remote.importer.RemoteServiceProxyFactory;

/**
 * <p>
 * Title: ProxyFacadeRemoteServiceManager
 * </p>
 * <p>
 * Description: invoke seq: request module->proxy-->|-->facade->response module
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 下午12:47:42
 * modified [who date description]
 * check [who date description]
 */
public class ProxyFacadeRemoteServiceManager extends DefaultRemoteServiceManagerImpl {

    private ProxyManagerImpl proxyManager;
    private Logger logger; 
    
    @Override
    public void init(){
        ProxyManager service = CoreContext.getInstance().local().getService("proxyManager", ProxyManager.class);
        proxyManager=(ProxyManagerImpl)service;
        
        initLogger();
    }
    
    private void initLogger(){
        logger=SfLog.getDefaultLogger();
    }
    
    /**
     * 就服务导出为RMI服务
     * @param rmiPort
     * @param serviceName
     * @param interfaceClass
     * @param service
     */
    public void exportRemoteService(int rmiPort,String serviceName, Class<?> interfaceClass,Object service) {
        if(interfaceClass.equals(RemoteInvocationFacade.class)){
            super.exportRemoteService(rmiPort, serviceName, interfaceClass, service);
        }else{
            if(logger==null)
                initLogger();
            logger.info("register proxy Remote service:"+serviceName);
            try{
                localServiceManager.setLocalService(serviceName, interfaceClass, service);
                //CoreContext.getInstance().setLocalService(serviceName, interfaceClass, service);
            }catch(Exception ex){
                // 当某个远程接口已经作为本地接口注册后，此时会报注册重复，应该忽略这个异常
                logger.info(ExceptionUtils.getCommonExceptionInfo(ex));
            }
        }
    }
    
    /**
     * 远程服务，自行维护一套local service，避免与真正的local service冲突
     */
    private LocalServiceManager localServiceManager=new LocalServiceManager();

    /**
     * 获取代理过的本地接口服务名
     * @param serviceInterfaceClassName
     * @return
     */
    public String getLocalServiceNameByClassName(Class<?> serviceInterfaceClassName){
        return localServiceManager.getLocalServiceNameByClassName(serviceInterfaceClassName);
    }
    
    /**
     * 获取代理过的本地接口服务
     * @param <T>
     * @param serviceName
     * @param serviceItf
     * @return
     */
    public <T> T getLocalService(String serviceName, Class<T> serviceItf){
        return localServiceManager.getLocalService(serviceName,serviceItf);
    }
    
    /**
     * 获取远程服务(此处不缓存，完全由上层来cache
     * @param remoteIp
     * @param remoteServerIp
     * @param serviceName
     * @param serviceItf 服务接口类名
     * 
     */
    public synchronized <T> T getRemoteService(RemoteCommunicateObject remote,String serviceName, Class<T> serviceItf) {
        if(serviceItf.equals(RemoteInvocationFacade.class)){
            return super.getRemoteService(remote, serviceName, serviceItf);
        }else{
            proxyManager.createProxy(remote);
            RemoteProxyAdvice proxyAdvice = proxyManager.getProxyAdvisor(remote);
            T obj = RemoteServiceProxyFactory.getRemoteServiceByAdvice(serviceItf,proxyAdvice);
            return obj;
        }
    }
    
}
