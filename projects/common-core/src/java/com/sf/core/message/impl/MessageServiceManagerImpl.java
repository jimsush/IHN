/*
 * $Id: MessageServiceManagerImpl.java, 2015-2-25 上午11:48:45 sufeng Exp $
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
import com.sf.core.message.impl.receive.MultiMessageListenerContainer;

/**
 * <p>
 * Title: MessageServiceManagerImpl
 * </p>
 * <p>
 * Description: 消息服务管理器实现类
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 上午11:48:45
 * modified [who date description]
 * check [who date description]
 */
public class MessageServiceManagerImpl implements MessageServiceManager{

    MessageCeneter messageCeneter=new MessageCeneter();
    
    private MessageMetadata messageMetadata;
    
    @Override
    public void initMessageReceive(RemoteCommunicateObject remote,MessageMetadata msgMetadata,MessageListener msgListener) {
        // 连接完毕后,需要启动jms消息接收线程
        MultiMessageListenerContainer jmsListenerContainer =new MultiMessageListenerContainer();
        jmsListenerContainer.setMessageListener(msgListener);
        jmsListenerContainer.init(msgMetadata,remote);
    }

    @Override
    public void publish(String name, Serializable msg) {
        messageCeneter.publish(name, msg);
    }
    
    @Override
    public void unsubscribe(String name, MessageReceiver receiver){
        messageCeneter.unsubscribe(name, receiver);
    }
    
    @Override
    public void subscribe(String name, MessageReceiver receiver){
        messageCeneter.subscribe(name, receiver);
    }

    @Override
    public MessageMetadata getSelfMessageMetadata() {
        return this.messageMetadata;
    }

    @Override
    public void setSelfMessageMetadata(MessageMetadata messageMetadata) {
        this.messageMetadata=messageMetadata;
    }

    @Override
    public void subscribeRemoteMessage(RemoteCommunicateObject remote, long sessionId, String name) {
        MessageServant ms =  remote.getService(MessageServant.SERVICE_NAME, MessageServant.class);
        ms.subscribe(sessionId, name);
    }

    @Override
    public void unsubscribeRemoteMessage(RemoteCommunicateObject remote, long sessionId, String name) {
        MessageServant ms =  remote.getService(MessageServant.SERVICE_NAME, MessageServant.class);
        ms.unsubscribe(sessionId, name);
    }

}
