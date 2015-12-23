/*
 * $Id: RemoteCommunicateObjectImpl.java, 2015-9-27 下午04:08:32 aaron Exp $
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
 * Description:远程通信对象，实现与远端服务器的交互，包括远程服务的获取，远程消息的订阅
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 下午04:08:32
 * modified [who date description]
 * check [who date description]·
 */
public class RemoteCommunicateObjectImpl implements RemoteCommunicateObject,RemoteCommunicate4Container,Serializable {

    private static final long serialVersionUID = 3222699236972197854L;

    /** 远端服务器IP */
    private String remoteIp;

    /** 远端服务器rmi port */
    private int remoteServicePort;

    /** 当前子系统的会话标识 */
    private Long sessionId;
    
    /** 远端消息服务器的配置信息 */
    private MessageMetadata serverMessageMetadata=null;
    
    private transient CoreContextImpl coreContext=null;

    /** 缓存的远端接口 */
    private Map<String, Object> services = new ConcurrentHashMap<String, Object>();
    
    /** 缓存的远程接口类 */
    private Set<Class<?>> serviceClasses=new CopyOnWriteArraySet<Class<?>>();

    /** 本地消息接收者列表*/
    private Map<String, ArrayList<MessageReceiver>> receiverMap = new ConcurrentHashMap<String, ArrayList<MessageReceiver>>();

    /** 与远端子系统的连接状态 0 linkdown 1 linkup */
    private int linkStatus=1;
    
    /**
     * 消息服务管理器
     */
    private transient MessageServiceManager messageServiceManager=null;
    
    /** 子系统间心跳ping的间隔 */
    private int pingInterval=30;
    
    /**
     * 与远端子系统进行心跳ping的任务
     */
    private transient SchedulerJob pingJob=null;
    
    private transient Logger logger=null;
    
    RemoteCommunicateObjectImpl(String ip, int port,CoreContextImpl coreContext) {
        this.coreContext=coreContext;
        this.messageServiceManager=this.coreContext.messageServiceManager;
        
        this.remoteIp = ip;
        this.remoteServicePort = port;
        
        initLogger();
        // 连接到远端服务器上
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
        // 先清理
        cleanup();
        // 连接到远端并获取SessionID
        initConnect();
    }
    
    private void initConnect(){
        // 连接到远端并获取SessionID
        RemoteConnector remoteConnector = this.getService("remoteConnector", RemoteConnector.class);
        sessionId= remoteConnector.connect(this.remoteIp);
        
        // 将内部类Receiver类注册，接收所有的远端JMS消息
        serverMessageMetadata=remoteConnector.getMessageMetadata(sessionId);
        try{
            messageServiceManager.initMessageReceive(this,serverMessageMetadata, new Receiver());
        }catch(RuntimeException ex){
            // 如果连接jms消息失败,则需要清掉已经创建的session并抛异常
            try{
                cleanup();
            }catch(Exception ex2){
                logger.warn("cleanup exception:"+ExceptionUtils.getCommonExceptionInfo(ex2));
            }
            throw ex; // 再抛异常
        }
    }
    
    /**
     * 获取日志服务
     */
    private void initLogger(){
        logger=SfLog.getDefaultLogger();
    }

    @Override
    public void cleanup() {
        // 停止ping心跳检测
        this.stopPing();
        
        // 与远端子系统切断连接
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

        // 获取远程服务,并存入到cache
        RemoteServiceManager remoteServiceManager = coreContext.getRemoteServiceManager();
        T obj = remoteServiceManager.getRemoteService(this,serviceName,serviceItf);
        services.put(serviceName, obj);
        serviceClasses.add(serviceItf); //cache一下,在多接口继承时需要这样处理,否则aop会丢失类型
        return obj;
    }
    
    @Override
    public Class<?> getRealRemoteServiceClass(Class<?> serviceInterface) {
        if(serviceClasses.contains(serviceInterface))
            return serviceInterface;
        for(Class<?> classInCache : serviceClasses){
            // 如果有服务接口继承了该子接口，则认为该服务接口就是子接口对应的服务接口
            if(serviceInterface.isAssignableFrom(classInCache)){
                return classInCache;
            }
        }
        return null;
    }

    /**
     * 接收远端JMS消息的代理,所有的远端JMS消息都将转发到Receiver上
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
                        // 再转发给local的订阅者
                        for (int i = 0; i < curReceivers.size(); i++) {
                            curReceivers.get(i).receive(RemoteCommunicateObjectImpl.this, msgName,  mo);
                        }
                    }else{
                        // 消息体未解析出消息名
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
     * 得到消息体中的消息名称（要么是BaseMessage，要么提供了getName方法）
     * @param mo
     * @return
     */
    private String getMessageName(Serializable mo){
        if(mo instanceof BaseMessage){
            return ((BaseMessage)mo).getName();
        }else{
            try{
                // 如果消息体提供了getName接口,我们认为也可以从该方法中解析出message name
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
        // 除了在本地保存订阅者信息以外，还需要告诉远端子系统有人要订阅消息
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
        
        // 通知远端子系统退订消息
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
        
        // 开始与远端子系统进行定期ping
        if(pingJob!=null)
            ContainerSchedulerExecutor.getSchedulerExecutor().startSchedulerJob(pingJob);
    }

    @Override
    public void stopPing() {
        if(pingJob!=null)
            ContainerSchedulerExecutor.getSchedulerExecutor().shutdownSchedulerJob(pingJob.getJobName(), pingJob.getJobGroupName());
    }

    /**
     * 远端服务器IP
     * @return ip地址
     */
    @Override
    public String getRemoteIp() {
        return this.remoteIp;
    }
    
    /**
     * 远端服务器RMI端口
     * @return 端口号
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
