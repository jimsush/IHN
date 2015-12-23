/*
 * $Id: RemoteServiceExporter.java, 2015-12-2 上午11:11:35 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.service.impl.remote.exporter;

import org.springframework.remoting.rmi.RmiServiceExporter;

import com.sf.base.exception.SfException;
import com.sf.base.log.def.SfLog;

/**
 * <p>
 * Title: RemoteServiceExporter
 * </p>
 * <p>
 * Description: 暴露远程RMI服务
 * </p>
 * 
 * @author sufeng
 * created 2015-12-2 上午11:11:35
 * modified [who date description]
 * check [who date description]
 */
public class RemoteServiceExporter {
    
    /**
     * 将某个接口注册为RMI远程服务
     * @param serviceInterface 接口类名
     * @param service   服务实现体对象
     * @param rmiPort   端口号
     */
    public static void register(Class<?> serviceInterface,Object service,int rmiPort){
        // 为了避免本地bean和远程bean重合,此处的bean不需要注册到spring容器中,需要容器自己维护cache
        RmiServiceExporter rmiExporter=new RmiServiceExporter();
        rmiExporter.setServiceName(serviceInterface.getSimpleName());
        rmiExporter.setService(service);
        rmiExporter.setServiceInterface(serviceInterface);
        rmiExporter.setRegistryPort(rmiPort);
        try{
            rmiExporter.afterPropertiesSet();
        }catch(Exception ex){
            SfLog.getDefaultLogger().error("",ex);
            throw new SfException(ex);
        }
    }

}
