/*
 * $Id: MessageServiceManager.java, 2015-2-25 上午11:46:37 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.message.impl;

import java.io.Serializable;

import javax.jms.MessageListener;

import com.sf.core.container.def.RemoteCommunicateObject;
import com.sf.core.message.def.MessageMetadata;
import com.sf.core.message.def.MessageReceiver;

/**
 * <p>
 * Title: MessageServiceManager
 * </p>
 * <p>
 * Description:对消息进行管理的服务接口
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 上午11:46:37
 * modified [who date description]
 * check [who date description]
 */
public interface MessageServiceManager {
    
    /**
     * 初始化接收JMS消息的处理
     * @param remote
     * @param msgMetadata
     * @param msgListener
     */
    public void initMessageReceive(RemoteCommunicateObject remote,MessageMetadata msgMetadata,MessageListener msgListener);
    
    /**
     * 替换MessageCenter
     * @param name
     * @param msg
     */
    public void publish(String name, Serializable msg);
    
    /**
     * 订阅本地消息
     * @param name
     * @param receiver
     */
    public void subscribe(String name, MessageReceiver receiver);
    
    /**
     * 退订本地消息
     * @param name
     * @param receiver
     */
    public void unsubscribe(String name, MessageReceiver receiver);
    
    /**
     * 设置消息服务器的基本配置信息
     * @param messageMetadata
     */
    public void setSelfMessageMetadata(MessageMetadata messageMetadata);
    
    /**
     * 获取消息服务器的基本配置信息
     * @return
     */
    public MessageMetadata getSelfMessageMetadata();
    
    /**
     * 订阅远程消息
     * @param remote
     * @param sessionId
     * @param name
     */
    public void subscribeRemoteMessage(RemoteCommunicateObject remote,long sessionId, String name);
    
    /**
     * 退订远程消息
     * @param remote
     * @param sessionId
     * @param name
     */
    public void unsubscribeRemoteMessage(RemoteCommunicateObject remote,long sessionId, String name);
    
    
}
