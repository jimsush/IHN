/*
 * $Id: RemoteCommunicateObjectImpl.java, 2015-9-27 ����04:08:32 aaron Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.impl;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import com.sf.base.exception.ExceptionUtils;
import com.sf.base.log.def.Logger;
import com.sf.base.log.def.SfLog;
import com.sf.base.scheduler.SchedulerJob;
import com.sf.core.bootstrap.def.ModuleContext;
import com.sf.core.container.def.RemoteCommunicate4Container;
import com.sf.core.container.def.RemoteCommunicateObject;
import com.sf.core.message.def.BaseMessage;
import com.sf.core.message.def.MessageMetadata;
import com.sf.core.message.def.MessageReceiver;
import com.sf.core.message.impl.MessageServiceManager;
import com.sf.core.service.impl.remote.RemoteServiceManager;
import com.sf.core.service.impl.remote.exporter.RemoteConnector;

/**
 * <p>
 * Title: RemoteCommunicateObjectImpl
 * </p>
 * <p>
 * Description:Զ��ͨ�Ŷ���ʵ����Զ�˷������Ľ���������Զ�̷���Ļ�ȡ��Զ����Ϣ�Ķ���
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 ����04:08:32
 * modified [who date description]
 * check [who date description]��
 */
public class RemoteCommunicateObjectImpl implements RemoteCommunicateObject,RemoteCommunicate4Container,Serializable {

    private static final long serialVersionUID = 3222699236972197854L;

    /** Զ�˷�����IP */
    private String remoteIp;

    /** Զ�˷�����rmi port */
    private int remoteServicePort;

    /** ��ǰ��ϵͳ�ĻỰ��ʶ */
    private Long sessionId;
    
    /** Զ����Ϣ��������������Ϣ */
    private MessageMetadata serverMessageMetadata=null;
    
    private transient CoreContextImpl coreContext=null;

    /** �����Զ�˽ӿ� */
    private Map<String, Object> services = new ConcurrentHashMap<String, Object>();
    
    /** �����Զ�̽ӿ��� */
    private Set<Class<?>> serviceClasses=new CopyOnWriteArraySet<Class<?>>();

    /** ������Ϣ�������б�*/
    private Map<String, ArrayList<MessageReceiver>> receiverMap = new ConcurrentHashMap<String, ArrayList<MessageReceiver>>();

    /** ��Զ����ϵͳ������״̬ 0 linkdown 1 linkup */
    private int linkStatus=1;
    
    /**
     * ��Ϣ���������
     */
    private transient MessageServiceManager messageServiceManager=null;
    
    /** ��ϵͳ������ping�ļ�� */
    private int pingInterval=30;
    
    /**
     * ��Զ����ϵͳ��������ping������
     */
    private transient SchedulerJob pingJob=null;
    
    private transient Logger logger=null;
    
    RemoteCommunicateObjectImpl(String ip, int port,CoreContextImpl coreContext) {
        this.coreContext=coreContext;
        this.messageServiceManager=this.coreContext.messageServiceManager;
        
        this.remoteIp = ip;
        this.remoteServicePort = port;
        
        initLogger();
        // ���ӵ�Զ�˷�������
        reconnect();
    }
    
    @Override
    public void resetRemoteServer(String ip, int port) {
        cleanup();
        this.remoteIp=ip;
        this.remoteServicePort=port;
        initConnect();
    }
    
    @Override
    public void reconnect() {
        // ������
        cleanup();
        // ���ӵ�Զ�˲���ȡSessionID
        initConnect();
    }
    
    private void initConnect(){
        // ���ӵ�Զ�˲���ȡSessionID
        RemoteConnector remoteConnector = this.getService("remoteConnector", RemoteConnector.class);
        sessionId= remoteConnector.connect(this.remoteIp);
        
        // ���ڲ���Receiver��ע�ᣬ�������е�Զ��JMS��Ϣ
        serverMessageMetadata=remoteConnector.getMessageMetadata(sessionId);
        try{
            messageServiceManager.initMessageReceive(this,serverMessageMetadata, new Receiver());
        }catch(RuntimeException ex){
            // �������jms��Ϣʧ��,����Ҫ����Ѿ�������session�����쳣
            try{
                cleanup();
            }catch(Exception ex2){
                logger.warn("cleanup exception:"+ExceptionUtils.getCommonExceptionInfo(ex2));
            }
            throw ex; // �����쳣
        }
    }
    
    /**
     * ��ȡ��־����
     */
    private void initLogger(){
        logger=SfLog.getDefaultLogger();
    }

    @Override
    public void cleanup() {
        // ֹͣping�������
        this.stopPing();
        
        // ��Զ����ϵͳ�ж�����
        if(sessionId!=null){
            RemoteConnector remoteConnector = this.getService("remoteConnector", RemoteConnector.class);
            try{
                remoteConnector.shutdown(sessionId);
            }catch(Exception ex){
                logger.info(ExceptionUtils.getCommonExceptionInfo(ex));
            }
            sessionId=null;
        }
        
        services.clear();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T> T getService(String serviceName, Class<T> serviceItf) {
        Object object = services.get(serviceName);
        if (object != null)
            return (T) object;

        // ��ȡԶ�̷���,�����뵽cache
        RemoteServiceManager remoteServiceManager = coreContext.getRemoteServiceManager();
        T obj = remoteServiceManager.getRemoteService(this,serviceName,serviceItf);
        services.put(serviceName, obj);
        serviceClasses.add(serviceItf); //cacheһ��,�ڶ�ӿڼ̳�ʱ��Ҫ��������,����aop�ᶪʧ����
        return obj;
    }
    
    @Override
    public Class<?> getRealRemoteServiceClass(Class<?> serviceInterface) {
        if(serviceClasses.contains(serviceInterface))
            return serviceInterface;
        for(Class<?> classInCache : serviceClasses){
            // ����з���ӿڼ̳��˸��ӽӿڣ�����Ϊ�÷���ӿھ����ӽӿڶ�Ӧ�ķ���ӿ�
            if(serviceInterface.isAssignableFrom(classInCache)){
                return classInCache;
            }
        }
        return null;
    }

    /**
     * ����Զ��JMS��Ϣ�Ĵ���,���е�Զ��JMS��Ϣ����ת����Receiver��
     */
    class Receiver implements MessageListener {

        private ClassLoader oldClassLoader;
        private ClassLoader coreLoader;
        
        public Receiver(){
            oldClassLoader=Thread.currentThread().getContextClassLoader();
            coreLoader=ModuleContext.getInstance().getCoreModule().getClass().getClassLoader();
        }
        
        @Override
        public void onMessage(Message msg) {
            Thread.currentThread().setContextClassLoader(coreLoader);
            try {
                if (msg instanceof ObjectMessage) {
                    Serializable mo = ((ObjectMessage) msg).getObject();
                    String msgName=getMessageName(mo);
                    if(msgName!=null) {
                        ArrayList<MessageReceiver> curReceivers = null;
                        synchronized (receiverMap) {
                            ArrayList<MessageReceiver> receivers = receiverMap.get(msgName);
                            if (null == receivers)
                                return;
                            curReceivers = new ArrayList<MessageReceiver>();
                            for (int i = 0; i < receivers.size(); i++) {
                                curReceivers.add(receivers.get(i));
                            }
                        }
                        // ��ת����local�Ķ�����
                        for (int i = 0; i < curReceivers.size(); i++) {
                            curReceivers.get(i).receive(RemoteCommunicateObjectImpl.this, msgName,  mo);
                        }
                    }else{
                        // ��Ϣ��δ��������Ϣ��
                        logger.info("[warn] ignore non-BaseMessage,msg.class="+mo.getClass().getSimpleName());
                    }
                }
            } catch (JMSException jE) {
                logger.info("Exception in listener: " + jE);
            }finally{
                Thread.currentThread().setContextClassLoader(oldClassLoader);
            }
        }
    }
    
    /**
     * �õ���Ϣ���е���Ϣ���ƣ�Ҫô��BaseMessage��Ҫô�ṩ��getName������
     * @param mo
     * @return
     */
    private String getMessageName(Serializable mo){
        if(mo instanceof BaseMessage){
            return ((BaseMessage)mo).getName();
        }else{
            try{
                // �����Ϣ���ṩ��getName�ӿ�,������ΪҲ���ԴӸ÷����н�����message name
                Method method = mo.getClass().getMethod("getName");
                Object obj=method.invoke(mo);
                if(obj instanceof String)
                    return (String)obj;
            }catch(Exception ex){
                logger.info("reflect message getName failed,class="+mo.getClass().getSimpleName()+",ex="+ex.getClass().getSimpleName());
            } 
        }
        return null;
    }

    /**
     * @see com.sf.core.container.def.CommunicateObject#subscribe(java.lang.String,
     *      com.sf.core.message.def.MessageReceiver)
     */
    @Override
    public void subscribe(String name, MessageReceiver receiver) {
        if ((null == name) || (null == receiver) || (name.equals("")))
            throw new IllegalArgumentException();
        boolean todo = false;
        //synchronized (receiverMap) {
        ArrayList<MessageReceiver> receivers = receiverMap.get(name);
        if (null == receivers) {
            receivers = new ArrayList<MessageReceiver>();
            receivers.add(receiver);
            receiverMap.put(name, receivers);
            todo = true;
        } else {
            for (int i = 0; i < receivers.size(); i++) {
                if (receivers.get(i) == receiver)
                    throw new RuntimeException("Message receiver '" + receiver + "' repeat.");
            }
            receivers.add(receiver);
        }
        //}
        // �����ڱ��ر��涩������Ϣ���⣬����Ҫ����Զ����ϵͳ����Ҫ������Ϣ
        if (todo) {
            coreContext.messageServiceManager.subscribeRemoteMessage(this,sessionId, name);
        }
    }

    /**
     * @see com.sf.core.container.def.CommunicateObject#unsubscribe(java.lang.String,
     *      com.sf.core.message.def.MessageReceiver)
     */
    @Override
    public void unsubscribe(String name, MessageReceiver receiver) {
        if ((null == name) || (null == receiver) || (name.equals("")))
            throw new IllegalArgumentException();
        boolean todo = false;
        //synchronized (receiverMap) {
        ArrayList<MessageReceiver> receivers = receiverMap.get(name);
        if (null != receivers) {
            for (int i = 0; i < receivers.size(); i++) {
                if (receivers.get(i) == receiver) {
                    receivers.remove(i);
                    if (0 == receivers.size())
                        receiverMap.remove(name);
                    todo = true;
                    break;
                }
            }
        }
        //}
        
        // ֪ͨԶ����ϵͳ�˶���Ϣ
        if (todo) 
            coreContext.messageServiceManager.unsubscribeRemoteMessage(this,sessionId, name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((remoteIp == null) ? 0 : remoteIp.hashCode());
        result = prime * result + remoteServicePort;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RemoteCommunicateObjectImpl other = (RemoteCommunicateObjectImpl) obj;
        if (remoteIp == null) {
            if (other.remoteIp != null)
                return false;
        } else if (!remoteIp.equals(other.remoteIp))
            return false;
        if (remoteServicePort != other.remoteServicePort)
            return false;
        if (sessionId == null) {
            if (other.sessionId != null)
                return false;
        } else if (!sessionId.equals(other.sessionId))
            return false;
        return true;
    }

    @Override
    public Long getSessionId() {
        return sessionId;
    }
    
    @Override
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
    
    @Override
    public int getPingInterval() {
        return pingInterval;
    }

    @Override
    public void setPingInterval(int interval) {
        this.pingInterval=interval;
        stopPing();
        startPing(this.pingJob);
    }
    
    @Override
    public void startPing(SchedulerJob pingJob) {
        this.pingJob=pingJob;
        
        // ��ʼ��Զ����ϵͳ���ж���ping
        if(pingJob!=null)
            ContainerSchedulerExecutor.getSchedulerExecutor().startSchedulerJob(pingJob);
    }

    @Override
    public void stopPing() {
        if(pingJob!=null)
            ContainerSchedulerExecutor.getSchedulerExecutor().shutdownSchedulerJob(pingJob.getJobName(), pingJob.getJobGroupName());
    }

    /**
     * Զ�˷�����IP
     * @return ip��ַ
     */
    @Override
    public String getRemoteIp() {
        return this.remoteIp;
    }
    
    /**
     * Զ�˷�����RMI�˿�
     * @return �˿ں�
     */
    @Override
    public int getRemoteServerPort() {
        return this.remoteServicePort;
    }
    
    @Override
    public synchronized int getLinkStatus() {
        return this.linkStatus;
    }
    
    @Override
    public synchronized void setLinkStatus(int linkStatus) {
        this.linkStatus=linkStatus;
    }

}
