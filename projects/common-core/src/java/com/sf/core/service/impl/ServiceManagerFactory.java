/*
 * $Id: ServiceManagerFactory.java, 2015-2-25 下午12:43:40 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.service.impl;

import com.sf.core.proxy.impl.ProxyFacadeRemoteServiceManager;
import com.sf.core.service.impl.local.LocalServiceManager;
import com.sf.core.service.impl.remote.RemoteServiceManager;

/**
 * <p>
 * Title: ServiceManagerFactory
 * </p>
 * <p>
 * Description: 服务管理器工厂，用来获取local,remote的服务管理器
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 下午12:43:40
 * modified [who date description]
 * check [who date description]
 */
public class ServiceManagerFactory {

    private static LocalServiceManager localServiceManager=new LocalServiceManager();
    
    /** remote service */
    private static RemoteServiceManager remoteServiceManager=new ProxyFacadeRemoteServiceManager();
    
    /**
     * 获取本地服务管理器
     * @return
     */
    public static LocalServiceManager getLocalServiceManager(){
        return localServiceManager;
    }
    
    /**
     * 获取远程服务管理器
     * @return
     */
    public static RemoteServiceManager getRemoteServiceManager(){
        return remoteServiceManager;
    }
    
    /**
     * 设置远程服务管理器
     * @param remoteServiceManager
     */
    public static void setRemoteServiceManager(RemoteServiceManager remoteServiceManager) {
        ServiceManagerFactory.remoteServiceManager = remoteServiceManager;
    }

}
