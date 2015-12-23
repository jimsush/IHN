/*
 * $Id: XRmiProxyFactoryBean.java, 2015-2-21 下午04:39:35  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.service.impl.remote.importer;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;


/**
 * <p>
 * Title: XRmiProxyFactoryBean
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author 
 * created 2015-2-21 下午04:39:35
 * modified [who date description]
 * check [who date description]
 */
public class XRmiProxyFactoryBean extends RmiProxyFactoryBean {

    /**
     * 记录配置文件中配置的serviceUrl名称
     */
    private String serviceUrl;
    private String ip;
    private int port;

    public XRmiProxyFactoryBean(String ip,int port) {
        this.ip=ip;
        this.port=port;
    }

    @Override
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
        super.setServiceUrl(initServerUrl());
    }

    /**
     * 格式：rmiserver = rmi://127.0.0.1:8888
     * 
     * @return
     */
    private String initServerUrl() {
        String result = "rmi://" + ip + ":" + port + "/" + serviceUrl;
        return result;
    }

    /**
     * 重新初始化, 方法是调用Bean的lookupStub方法, 前提条件是ServerIP地址已经被更新
     * 
     */
    void reInit() {
        // 使用之前保存的url进行重新初始化
        super.setServiceUrl(initServerUrl());
        // 强制要求bean再找一次Stub, 此时会用新的rmi的url
        super.lookupStub();
    }
    
}