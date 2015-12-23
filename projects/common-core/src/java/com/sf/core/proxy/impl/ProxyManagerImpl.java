/*
 * $Id: ProxyManagerImpl.java, 2015-1-31 下午01:27:40 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.proxy.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sf.core.container.def.RemoteCommunicateObject;
import com.sf.core.proxy.def.ProxyManager;
import com.sf.core.proxy.def.ProxyProcessor;

/**
 * <p>
 * Title: ProxyManagerImpl
 * </p>
 * <p>
 * Description: 代理管理器的实现
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 下午01:27:40
 * modified [who date description]
 * check [who date description]
 */
public class ProxyManagerImpl implements ProxyManager{
    
    private Map<RemoteCommunicateObject,RemoteProxyAdvice> remoteMap=new ConcurrentHashMap<RemoteCommunicateObject,RemoteProxyAdvice>();
    
    /**
     * 创建一个到远端子系统的Proxy，比如ClientProxy,SbiProxy
     * @param comm
     */
    public void createProxy(RemoteCommunicateObject comm) {
        if(remoteMap.containsKey(comm))
            return;
        
        RemoteProxyAdvice remoteProxyAdvisor = new RemoteProxyAdvice(comm);
        remoteMap.put(comm, remoteProxyAdvisor);
    }
    
    /**
     * 得到advisor
     * @param comm
     * @return
     */
    public RemoteProxyAdvice getProxyAdvisor(RemoteCommunicateObject comm) {
        return remoteMap.get(comm);
    }

    @Override
    public void closeProxy(RemoteCommunicateObject comm) {
        remoteMap.remove(comm);
    }

    @Override
    public synchronized ProxyProcessor getProxyProcessor(RemoteCommunicateObject comm) {
        RemoteProxyAdvice proxyAdvisor = getProxyAdvisor(comm);
        if(proxyAdvisor==null)
            return null;
        return proxyAdvisor.getProxyProcessor();
    }

    @Override
    public synchronized void setProxyProcessor(RemoteCommunicateObject comm,ProxyProcessor proxyInvoker) {
        RemoteProxyAdvice proxyAdvisor = getProxyAdvisor(comm);
        proxyAdvisor.setProxyProcessor(proxyInvoker);
    }
    
}
