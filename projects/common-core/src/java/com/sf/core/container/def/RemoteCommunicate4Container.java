/*
 * $Id: RemoteCommunicate4Container.java, 2015-2-12 上午11:08:27 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;

import com.sf.base.scheduler.SchedulerJob;



/**
 * <p>
 * Title: RemoteCommunicate4Container
 * </p>
 * <p>
 * Description: 容器内部使用的远端子系统的通信对象的管理接口
 * </p>
 * 
 * @author sufeng
 * created 2015-2-12 上午11:08:27
 * modified [who date description]
 * check [who date description]
 */
public interface RemoteCommunicate4Container {

    /**
     * 重连(ip,port不变的情况下重连)
     */
    public void reconnect();
    
    /**
     * 当前子系统的会话id
     * @return
     */
    public Long getSessionId();
    
    /**
     * 设置session id
     * @param sessionId
     */
    public void setSessionId(Long sessionId);
    
    /**
     * 设置与远端子系统的连接关系
     * @param linkStatus 0 linkdown 1 linkup
     */
    public void setLinkStatus(int linkStatus);
    
    /**
     * 开启ping
     * @param pingJob
     */
    public void startPing(SchedulerJob pingJob);
    
    /**
     * 停止ping
     */
    public void stopPing();
    
    /**
     * 获取ping的周期
     * @return 秒
     */
    public int getPingInterval();
    
    /**
     * 设置ping间隔
     * @param interval，秒
     */
    public void setPingInterval(int interval);
    
    /**
     * 得到真正的远程服务接口类名
     * @param serviceInterface
     * @return
     */
    public Class<?> getRealRemoteServiceClass(Class<?> serviceInterface);
    
}
