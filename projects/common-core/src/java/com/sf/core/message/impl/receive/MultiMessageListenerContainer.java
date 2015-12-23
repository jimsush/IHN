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
 * ��spring����jms�����İ�װ,�ɽ����topic/queue����Ϣ���յ�ͬһ��message Listener�н���ͳһ����
 * @author sufeng
 *
 */
public class MultiMessageListenerContainer {

    /**
     * remoteͨ�Ŷ���
     */
    private RemoteCommunicateObject remote;
    
    /**
     * ���ӹ���
     */
	private ConnectionFactory connectionFactory;
	
	/**
	 * jndiģ��
	 */
	private JndiTemplate jndiTemplate;
	
	/**
	 * ͳһ����Ϣ������
	 */
	private MessageListener messageListener;
	
	/**
	 * spring��װ����Ϣ��������
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
                       // ��JMS��Ϣʧ����
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
		container.shutdown(); // TODO spring 2.5�п��ܻ�hang
		container.destroy(); 
	}
	
}
