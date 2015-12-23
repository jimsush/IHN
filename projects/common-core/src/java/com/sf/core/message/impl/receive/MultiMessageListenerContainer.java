/**
 * 
 */
package com.sf.core.message.impl.receive;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageListener;

import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jndi.JndiTemplate;

import com.sf.core.container.def.CoreContext;
import com.sf.core.container.def.RemoteCommunicate4Container;
import com.sf.core.container.def.RemoteCommunicateObject;
import com.sf.core.message.def.MessageMetadata;
import com.sf.core.message.impl.JmsHandleUtils;
import com.sf.core.session.def.SessionMessage;

/**
 * 对spring接收jms容器的包装,可将多个topic/queue的消息接收到同一个message Listener中进行统一处理
 * @author sufeng
 *
 */
public class MultiMessageListenerContainer {

    /**
     * remote通信对象
     */
    private RemoteCommunicateObject remote;
    
    /**
     * 连接工厂
     */
	private ConnectionFactory connectionFactory;
	
	/**
	 * jndi模板
	 */
	private JndiTemplate jndiTemplate;
	
	/**
	 * 统一的消息接收者
	 */
	private MessageListener messageListener;
	
	/**
	 * spring包装的消息接收容器
	 */
	private DefaultMessageListenerContainer container;

	public void setMessageListener(MessageListener messageListener) {
		this.messageListener = messageListener;
	}

	public void init(MessageMetadata messageMetadata,RemoteCommunicateObject remote){  
	    this.remote=remote;
	    
	    Properties env =new Properties();
	    if(messageMetadata.getServerIp()!=null)
            env.put("java.naming.factory.host", messageMetadata.getServerIp());
        else
            env.put("java.naming.factory.host", "localhost");
        env.put("java.naming.factory.port", messageMetadata.getNamingPort()+"");
        env.put("java.naming.factory.initial", messageMetadata.getInitial());
        jndiTemplate=new JndiTemplate(env);
        
        connectionFactory=JmsHandleUtils.getConnectionFactory("tcf", jndiTemplate);

	    createAndStartContainer(messageMetadata.getTopicName());
	}
	
	private void createAndStartContainer(String topicName){
	    Destination destination=JmsHandleUtils.getDestination(topicName, jndiTemplate);
	    container=new MessageListenerContainerWithoutAutoConnect();
        container.setConcurrentConsumers(1);
        container.setConnectionFactory(connectionFactory);
        container.setDestination(destination);
        container.setMessageListener(messageListener);
        container.afterPropertiesSet();
        container.start();
        
        container.setExceptionListener(new ExceptionListener(){
           @Override
            public void onException(JMSException jmsexception) {
               if(jmsexception.getClass().equals(javax.jms.IllegalStateException.class)){
                   if("Connection closed".equals(jmsexception.getMessage())){
                       // 与JMS消息失联了
                       System.out.println("[warn] JMS connection closed");
                       
                       // update status and send local message
                       if(remote instanceof RemoteCommunicate4Container){
                           RemoteCommunicate4Container remoteObj=(RemoteCommunicate4Container)remote;
                           remoteObj.setLinkStatus(0);
                           SessionMessage message=new SessionMessage(remote);
                           message.setAdditionalInfo("JMS connection closed");
                           CoreContext.getInstance().publish(message.getName(),message);
                       }
                   }
               }
            } 
        });
        
	}
	
	public void destroy(){
		container.stop();
		container.shutdown(); // TODO spring 2.5有可能会hang
		container.destroy(); 
	}
	
}
