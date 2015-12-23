/*
 * $Id: ProxyManager.java, 2015-1-31 ����01:26:14 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.proxy.def;

import com.sf.core.container.def.RemoteCommunicateObject;

/**
 * <p>
 * Title: ProxyManager
 * </p>
 * <p>
 * Description: ����Զ����ϵͳ�ڱ�����ϵͳ�Ĵ���
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 ����01:26:14
 * modified [who date description]
 * check [who date description]
 */
public interface ProxyManager {

    /**
     * ����proxy���������ϲ�Ӧ�ÿ�ͨ���÷����滻Ĭ�ϵ�ProxyProcessor��
     * @param comm
     * @param proxyProcessor
     */
    public void setProxyProcessor(RemoteCommunicateObject comm,ProxyProcessor proxyProcessor);
    
    /**
     * �õ���ǰ��ProxyProcessor
     * @param comm Զ��ͨ�Ŷ���
     * @return ProxyProcessor
     */
    public ProxyProcessor getProxyProcessor(RemoteCommunicateObject comm);
    
    /**
     * �ر�proxy
     * @param comm Զ��ͨ�Ŷ���
     */
    public void closeProxy(RemoteCommunicateObject comm);
    
}
