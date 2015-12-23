/*
 * $Id: RemoteConnector.java, 2015-12-2 上午11:45:48 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.service.impl.remote.exporter;

import com.sf.core.message.def.MessageMetadata;

/**
 * <p>
 * Title: RemoteConnector
 * </p>
 * <p>
 * Description: 容器内置的，用户子系统间互连的接口，用来管理子系统间的生命周期
 * ,需要通过RMI远程暴露出去
 * </p>
 * 
 * @author sufeng
 * created 2015-12-2 上午11:45:48
 * modified [who date description]
 * check [who date description]
 */
public interface RemoteConnector {

    /**
     * 与远端建立连接
     * @param server ip     服务子系统的ip
     * @return Session ID
     */
    public Long connect(String serverIp);
    
    /**
     * 远端服务器的jms消息服务器配置信息
     * @param sessionId
     * @return
     */
    public MessageMetadata getMessageMetadata(Long sessionId);
    
    /**
     * 与远端子系统结束连接
     * @param sessionId
     */
    public void shutdown(Long sessionId);
    
    /**
     * ping远端子系统，用来测试连接是否link up
     * @param sessionId
     */
    public void ping(Long sessionId);
    
}
