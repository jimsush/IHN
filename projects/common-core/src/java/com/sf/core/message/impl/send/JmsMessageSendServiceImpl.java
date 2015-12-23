/*
 * $Id: JmsMessageSendServiceImpl.java, 2015-9-20 ����11:41:07 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.message.impl.send;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jndi.JndiTemplate;

import com.sf.core.message.def.MessageMetadata;
import com.sf.core.message.impl.JmsHandleUtils;

/**
 * <p>
 * Title: JmsMessageSendServiceImpl
 * </p>
 * <p>
 * Description: ����jms��Ϣ��topic
 * </p>
 * 
 * @author sufeng
 * created 2015-9-20 ����11:41:07
 * modified [who date description]
 * check [who date description]
 */
public class JmsMessageSendServiceImpl {
    
    private JmsTemplate jmsTemplate;
    
    private JndiTemplate jndiTemplate;
    
    /**
     * ����ϵͳ������Ϣ��topic
     */
    private Destination destination;

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }
    public void setJndiTemplate(JndiTemplate jndiTemplate) {
		this.jndiTemplate = jndiTemplate;
	}
    
    /**
     * ��ʼ��jndi template��destination
     * @param messageMetadata ��Ϣ��������������Ϣ
     */
    public void init(MessageMetadata messageMetadata){
        Properties env = jndiTemplate.getEnvironment();
        if(messageMetadata.getServerIp()!=null)
            env.put("java.naming.factory.host", messageMetadata.getServerIp());
        else
            env.put("java.naming.factory.host", "localhost");
        env.put("java.naming.factory.port", messageMetadata.getNamingPort()+"");
        jndiTemplate.setEnvironment(env);
        
        destination=JmsHandleUtils.getDestination(messageMetadata.getTopicName(), jndiTemplate);
    }

    /**
     * ������Ϣ
     * @param msgName ��Ϣ����,����topo.msg,system.msg,trap.msg...
     * @param message
     */
    public void sendMessage(String msgName, Serializable message) {
        if(message==null)
            return;
        
        sendMessage(message, destination);
    }

    
    private void sendMessage(final Serializable msg, Destination destination) {
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(msg);
            }
        });
    }

}
