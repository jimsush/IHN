/*
 * $Id: XRmiProxyFactoryBean.java, 2015-2-21 ����04:39:35  Exp $
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
 * created 2015-2-21 ����04:39:35
 * modified [who date description]
 * check [who date description]
 */
public class XRmiProxyFactoryBean extends RmiProxyFactoryBean {

    /**
     * ��¼�����ļ������õ�serviceUrl����
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
     * ��ʽ��rmiserver = rmi://127.0.0.1:8888
     * 
     * @return
     */
    private String initServerUrl() {
        String result = "rmi://" + ip + ":" + port + "/" + serviceUrl;
        return result;
    }

    /**
     * ���³�ʼ��, �����ǵ���Bean��lookupStub����, ǰ��������ServerIP��ַ�Ѿ�������
     * 
     */
    void reInit() {
        // ʹ��֮ǰ�����url�������³�ʼ��
        super.setServiceUrl(initServerUrl());
        // ǿ��Ҫ��bean����һ��Stub, ��ʱ�����µ�rmi��url
        super.lookupStub();
    }
    
}