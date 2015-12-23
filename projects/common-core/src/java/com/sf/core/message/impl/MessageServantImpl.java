/*
 * $Id: MessageServantImpl.java, 2015-9-27 下午05:57:50 aaron Exp $
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;

import javax.jms.ConnectionFactory;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jndi.JndiTemplate;

import com.sf.core.container.def.CommunicateObject;
import com.sf.core.container.def.CoreContext;
import com.sf.core.message.def.MessageConst;
import com.sf.core.message.def.MessageMetadata;
import com.sf.core.message.def.MessageReceiver;
import com.sf.core.message.impl.send.JmsMessageSendServiceImpl;
import com.sf.core.session.def.Session;
import com.sf.core.session.def.SessionMessage;
import com.sf.core.session.def.SessionState;

/**
 * <p>
 * Title: MessageServantImpl
 * </p>
 * <p>
 * Description: 为远端提供消息订阅的服务,发送
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 下午05:57:50
 * modified [who date description]
 * check [who date description]
 */
public class MessageServantImpl implements MessageServant {

    private HashMap<String, ArrayList<Long>> map = new HashMap<String, ArrayList<Long>>();
    private ArrayList<Long> sessions = new ArrayList<Long>();
    
    /**
     * 接收本子系统的消息，并转发到JMS消息服务器中
     */
    private Receiver receiveForwarder = new Receiver();
    private int msgPort=16010;
    
    private MessageServiceManagerImpl messageServiceManager;
    
    public MessageServantImpl(MessageServiceManagerImpl messageServiceManager){
        this.messageServiceManager=messageServiceManager;
        
        // 订阅本地子系统session变化的消息
        CoreContext.getInstance().local().subscribe(SessionMessage.NAME,new MessageReceiver(){
            @Override
            public void receive(CommunicateObject co, String name, Serializable msg) {
                if(msg instanceof SessionMessage){
                    SessionMessage sessionMsg=(SessionMessage)msg;
                    Session session = sessionMsg.getSession();
                    if(session!=null){
                        // 断连时需要清理session
                        if(SessionState.Linkdown.equals(session.getSessionState()))
                            removeSession(session.getSessionId());
                    }
                }
            }
        });
    }
    
    /**
     * 接收其他模块发送过来的消息，然后通过JMS转发出去,
     * jms消息的接收在RemoteCommunicateObjectImpl处
     */
    static class Receiver implements MessageReceiver {
        
        private JmsMessageSendServiceImpl jmsMessageSendService;
        
        public void init(){
            MessageMetadata messageMetadata = CoreContext.getInstance().getSelfMessageMetadata();
            Properties env =new Properties();
            if(messageMetadata.getServerIp()!=null)
                env.put("java.naming.factory.host", messageMetadata.getServerIp());
            else
                env.put("java.naming.factory.host", "localhost");
            env.put("java.naming.factory.port", messageMetadata.getNamingPort()+"");
            env.put("java.naming.factory.initial", messageMetadata.getInitial());
            JndiTemplate jndiTemplate=new JndiTemplate(env);
            
            // 发送jms消息的服务
            jmsMessageSendService=new JmsMessageSendServiceImpl();
            jmsMessageSendService.setJndiTemplate(jndiTemplate);
            
            ConnectionFactory connectionFactory = JmsHandleUtils.getConnectionFactory("tcf", jndiTemplate);
            JmsTemplate jmsTemplate=new JmsTemplate();
            jmsTemplate.setConnectionFactory(connectionFactory);
            jmsTemplate.setReceiveTimeout(30000);
            jmsMessageSendService.setJmsTemplate(jmsTemplate);
            
            jmsMessageSendService.init(messageMetadata);
        }
        
        @Override
        public void receive(CommunicateObject co, String msgName, Serializable msg) {
            // 转发消息给JMS消息服务器
            jmsMessageSendService.sendMessage(msgName, msg);
        }
    }
    
    public  void init(){
        MessageMetadata msgMetadata = CoreContext.getInstance().getSelfMessageMetadata();
        this.msgPort=msgMetadata.getPort();
        
        // 当消息服务器启动完毕后，就启动JMS消息转发器
        CoreContext.getInstance().local().subscribe(MessageConst.MSG_JMS_SERVER_INIT_COMPLETED, new MessageReceiver(){
            @Override
            public void receive(CommunicateObject co, String name, Serializable msg) {
                receiveForwarder.init();
            }
        });
        
    }
    
    /**
     * @see com.sf.core.container.impl.MessageServant#getMsgPort()
     */
    @Override
    public int getMsgPort() {
        return msgPort;
    }

    void addSession(long sessionid) {
        synchronized (this) {
            for (int i = 0; i < sessions.size(); i++) {
                if (sessionid == sessions.get(i)) {
                    throw new RuntimeException("SessionId '" + sessionid + "' repeat!");
                }
            }
            sessions.add(sessionid);
        }
    }

    void removeSession(long sessionid) {
        synchronized (this) {
            int i = 0;
            for (i = 0; i < sessions.size(); i++) {
                if (sessionid == sessions.get(i)) {
                    sessions.remove(i);
                    Set<String> keySet = map.keySet();
                    Iterator<String> iterator = keySet.iterator();
                    ArrayList<String> keys = new ArrayList<String>();
                    try {
                        String key = iterator.next();
                        while (null != key) {
                            ArrayList<Long> receivers = map.get(key);
                            for (i = 0; i < receivers.size(); i++) {
                                if (receivers.get(i) == sessionid) {
                                    receivers.remove(i);
                                    if (0 == receivers.size()) {
                                        keys.add(key);
                                    }
                                    break;
                                }
                            }
                            key = iterator.next();
                        }
                    } catch (NoSuchElementException e) {
                    }
                    for (i = 0; i < keys.size(); i++) {
                        map.remove(keys.get(i));
                        messageServiceManager.unsubscribe(keys.get(i),receiveForwarder);
                    }
                    return;
                }
            }
        }
    }

    /**
     * @see com.sf.core.container.impl.MessageServant#subscribe(long,
     *      java.lang.String)
     */
    @Override
    public void subscribe(long sessionId, String name) {
        if ((null == name) || (name.equals("")))
            throw new IllegalArgumentException();
        
        // 这里需要判断是否需要启动jms消息服务器
        // 
        
        synchronized (this) {
            int i = 0;
            for (i = 0; i < sessions.size(); i++) {
                if (sessionId == sessions.get(i)) {
                    break;
                }
            }

            ArrayList<Long> receivers = map.get(name);
            if (null == receivers) {
                receivers = new ArrayList<Long>();
                receivers.add(sessionId);
                map.put(name, receivers);
                messageServiceManager.subscribe(name, receiveForwarder);
            } else {
                for (i = 0; i < receivers.size(); i++) {
                    if (receivers.get(i) == sessionId)
                        throw new RuntimeException("Message receiver '" + receivers.get(i) + "' repeat.");
                }
                receivers.add(sessionId);
            }
        }
    }

    /**
     * @see com.sf.core.container.impl.MessageServant#unsubscribe(long,
     *      java.lang.String)
     */
    @Override
    public void unsubscribe(long sessionId, String name) {
        if ((null == name) || (name.equals("")))
            throw new IllegalArgumentException();
        synchronized (this) {
            int i = 0;
            for (i = 0; i < sessions.size(); i++) {
                if (sessionId == sessions.get(i)) {
                    break;
                }
            }

            ArrayList<Long> receivers = map.get(name);
            if (null != receivers) {
                for (i = 0; i < receivers.size(); i++) {
                    if (receivers.get(i) == sessionId) {
                        receivers.remove(i);
                        if (0 == receivers.size()) {
                            map.remove(name);
                            messageServiceManager.unsubscribe(name,receiveForwarder);
                        }
                        return;
                    }
                }
            }
        }
    }
    
}
