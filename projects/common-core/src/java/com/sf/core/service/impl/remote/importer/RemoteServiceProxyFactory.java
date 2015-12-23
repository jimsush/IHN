/*
 * 
 * $Id: RemoteServiceProxyFactory.java, 2015-2-21 下午03:03:09 Victor Exp $
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.service.impl.remote.importer;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;

import com.sf.base.exception.ExceptionUtils;
import com.sf.base.log.def.Logger;
import com.sf.base.log.def.SfLog;
import com.sf.core.proxy.impl.RemoteProxyAdvice;

/**
 * <p>
 * Title: RemoteServiceProxyFactory
 * </p>
 * <p>
 * Description: 客户端获取服务端远程接口的工厂
 * </p>
 * 
 * @author Victor
 * created 2015-2-21 下午03:03:09
 * modified [who date description]
 * check [who date description]
 */
public class RemoteServiceProxyFactory {
    
    /**
     * 获取服务端远程facade接口
     * 
     * @param <T>
     * @param ip
     * @param port
     * @param serviceInterface
     *            给定的接口类名
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getRemoteService(String ip,int port,Class<T> serviceInterface) {
        Logger logger =getLogger();
        logger.debug("Aware RemoteService:" + serviceInterface);
        XRmiProxyFactoryBean proxy = new XRmiProxyFactoryBean(ip,port);
        proxy.setServiceInterface(serviceInterface);
        proxy.setServiceUrl(serviceInterface.getSimpleName());
        proxy.setLookupStubOnStartup(false);
        proxy.setRefreshStubOnConnectFailure(true);
        proxy.afterPropertiesSet();
        logger.info("Success to proxy facade:" + serviceInterface.getName());
        return (T) proxy.getObject();
    }
    
    /**
     * 通过AOP代理模式来获取远程接口
     * @param <T>
     * @param serviceInterface 需要被代理的接口类名
     * @param advice           拦截处理器
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getRemoteServiceByAdvice(Class<T> serviceInterface,RemoteProxyAdvice advice) {
        try{
            ProxyFactory factory = new ProxyFactory();
            factory.setInterfaces(new Class[]{serviceInterface});
            RegexpMethodPointcutAdvisor advisor=new RegexpMethodPointcutAdvisor();
            advisor.setAdvice(advice);
            advisor.setPattern(".*");
            factory.addAdvisor(advisor);
            return (T)factory.getProxy();
        }catch(Exception ex){
            getLogger().warn(ExceptionUtils.getCommonExceptionInfo(ex));
        }
        return null;
    }
    
    private static Logger getLogger(){
        return SfLog.getDefaultLogger();
    }

}
