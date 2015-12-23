/*
 * $Id: ServiceManagerFactory.java, 2015-2-25 ����12:43:40 sufeng Exp $
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
 * Description: ���������������������ȡlocal,remote�ķ��������
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 ����12:43:40
 * modified [who date description]
 * check [who date description]
 */
public class ServiceManagerFactory {

    private static LocalServiceManager localServiceManager=new LocalServiceManager();
    
    /** remote service */
    private static RemoteServiceManager remoteServiceManager=new ProxyFacadeRemoteServiceManager();
    
    /**
     * ��ȡ���ط��������
     * @return
     */
    public static LocalServiceManager getLocalServiceManager(){
        return localServiceManager;
    }
    
    /**
     * ��ȡԶ�̷��������
     * @return
     */
    public static RemoteServiceManager getRemoteServiceManager(){
        return remoteServiceManager;
    }
    
    /**
     * ����Զ�̷��������
     * @param remoteServiceManager
     */
    public static void setRemoteServiceManager(RemoteServiceManager remoteServiceManager) {
        ServiceManagerFactory.remoteServiceManager = remoteServiceManager;
    }

}
