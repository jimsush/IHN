/*
 * $Id: ProxyManager.java, 2015-1-31 下午01:26:14 sufeng Exp $
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
 * Description: 管理远端子系统在本地子系统的代理
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 下午01:26:14
 * modified [who date description]
 * check [who date description]
 */
public interface ProxyManager {

    /**
     * 设置proxy处理器（上层应用可通过该方法替换默认的ProxyProcessor）
     * @param comm
     * @param proxyProcessor
     */
    public void setProxyProcessor(RemoteCommunicateObject comm,ProxyProcessor proxyProcessor);
    
    /**
     * 得到当前的ProxyProcessor
     * @param comm 远程通信对象
     * @return ProxyProcessor
     */
    public ProxyProcessor getProxyProcessor(RemoteCommunicateObject comm);
    
    /**
     * 关闭proxy
     * @param comm 远程通信对象
     */
    public void closeProxy(RemoteCommunicateObject comm);
    
}
