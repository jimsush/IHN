/*
 * $Id: RemoteInvocationFacadeImpl.java, 2015-1-30 ����04:37:55 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.facade.impl;

import java.lang.reflect.Method;
import java.rmi.server.RemoteServer;
import java.util.List;

import com.sf.base.exception.ExceptionUtils;
import com.sf.base.exception.SfException;
import com.sf.base.log.def.Logger;
import com.sf.base.log.def.SfLog;
import com.sf.core.container.def.CommunicateObject;
import com.sf.core.container.def.CoreContext;
import com.sf.core.container.def.CoreException;
import com.sf.core.container.impl.CoreContextImpl;
import com.sf.core.facade.def.FacadeManager;
import com.sf.core.facade.def.FacadeProcessor;
import com.sf.core.facade.def.FacadeResponse;
import com.sf.core.facade.def.RemoteInvocationFacade;
import com.sf.core.proxy.impl.ProxyFacadeRemoteServiceManager;
import com.sf.core.service.impl.ServiceManagerFactory;
import com.sf.core.service.impl.remote.exporter.RemoteConnector;
import com.sf.core.session.def.Session;
import com.sf.core.session.def.SessionService;

/**
 * <p>
 * Title: RemoteInvocationFacadeImpl
 * </p>
 * <p>
 * Description:RemoteInvocationFacadeʵ���࣬���ͨ�õļ�飬facadeprocessor�Ĵ���,��ת����������local service��
 * </p>
 * 
 * @author sufeng
 * created 2015-1-30 ����04:37:55
 * modified [who date description]
 * check [who date description]
 */
public class RemoteInvocationFacadeImpl implements RemoteInvocationFacade{

    /**
     * �Ự����
     */
    private SessionService sessionService;
    /**
     * ���������
     */
    private CoreContextImpl coreContext;
    /**
     * facade������
     */
    private FacadeManager facadeManager;
    private Logger logger;
    /**
     * Զ�̷��������
     */
    private ProxyFacadeRemoteServiceManager proxyFacadeRemoteServiceManager;
    
    public void init(){
        coreContext=(CoreContextImpl)CoreContext.getInstance();
        CommunicateObject local = coreContext.local();
        sessionService=local.getService("sessionService", SessionService.class);
        facadeManager=local.getService("facadeManager", FacadeManager.class);
        logger=SfLog.getDefaultLogger();
        proxyFacadeRemoteServiceManager=(ProxyFacadeRemoteServiceManager)ServiceManagerFactory.getRemoteServiceManager();
    }
    
    @Override
    public Object remoteInvoke(Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args, Long sessionId) throws Throwable {
        // Ԥ����
        prepare(serviceClz, methodName, paramType, args, sessionId);

        // before����
        List<FacadeProcessor> befores=facadeManager.getFacadeProcessorByBeforeOrder();
        for(FacadeProcessor before : befores){
            FacadeResponse beforeResp = before.beforeInvoke(serviceClz, methodName, paramType, args);
            // before�������Ҳ�ǿ��Ա�ǿ�Ʒ��ص�
            if(beforeResp!=null && beforeResp.isForceReturn()){
                if(beforeResp.getThrowedException()!=null)
                    throw beforeResp.getThrowedException();
                else
                    return beforeResp.getReturnValue();
            }
        }
        
        // �����Ĵ���
        FacadeResponse afterResp = actualInvoke(serviceClz, methodName, paramType, args);
        
        // after����
        List<FacadeProcessor> afters=facadeManager.getFacadeProcessorByAfterOrder();
        for(FacadeProcessor after : afters){
            afterResp=after.afterInvoke(afterResp, serviceClz, methodName, paramType, args);
            // after�������Ҳ�ǿ��Ա�ǿ�Ʒ��ص�
            if(afterResp!=null && afterResp.isForceReturn()){
                if(afterResp.getThrowedException()!=null)
                    throw afterResp.getThrowedException();
                else
                    return afterResp.getReturnValue();
            }
        }
        
        // �����յĽ�����д���
        Throwable th = afterResp.getThrowedException();
        if(th==null)
            return afterResp.getReturnValue();
        else
            throw th;
    }
    
    /**
     * Ԥ����
     * @param serviceClz
     * @param methodName
     * @param paramType
     * @param args
     * @param sessionId
     */
    private Long prepare(Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args, Long sessionId){
        Session session =null;
        // ��¼session 
        if(sessionId==null){
            // ��һ��connectʱ��û��session��
            if(serviceClz.equals(RemoteConnector.class) && "connect".equals(methodName)){
                session=createSession(args);
                sessionId=session.getSessionId();
            }
        }else{
            session = sessionService.getSessionById(sessionId);
            if(session!=null){
                session.setLastActivetime(System.currentTimeMillis());
            }else{
                // �����ǵ�һ�����ӵģ���Ҫ����һ��Session id
                // Ҳ�����ǷǷ�����
                if(serviceClz.equals(RemoteConnector.class) && "connect".equals(methodName)){
                    session=createSession(args);
                    sessionId=session.getSessionId();
                }
            }
        }
        
        if(session==null)
            throw new CoreException(CoreException.ILLEGAL_SESSION,serviceClz.getSimpleName()+"."+methodName);
        else
            sessionService.setSessionInCurrentThread(sessionId);
        return sessionId;
    }
    
    /**
     * �����Ự
     * @param args
     * @return
     */
    private Session createSession(Object[] args){
        Session session=null;
        String clientHost =null;
        String serverIp=null;
        try {
            if(args!=null && args[0]!=null)
                serverIp=args[0].toString();
            clientHost = RemoteServer.getClientHost();
        } catch (Exception ex) {
            throw new SfException("getClientHost failed," + ExceptionUtils.getCommonExceptionInfo(ex));
        }
        session = sessionService.connect(clientHost, serverIp);
        return session;
    }
    
    /**
     * �����ĵ���
     * @param serviceClz
     * @param methodName
     * @param paramType
     * @param args
     * @return
     */
    private FacadeResponse actualInvoke(Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args) {
        // ��ʼ����
        //String serviceName=coreContext.getLocalServiceNameByClassName(serviceClz);
        String serviceName=proxyFacadeRemoteServiceManager.getLocalServiceNameByClassName(serviceClz);
        if(serviceName==null)
            throw new NullPointerException("serviceName=null,class="+serviceClz);

        //Object seviceRef = coreContext.local().getService(serviceName, serviceClz);
        Object seviceRef =proxyFacadeRemoteServiceManager.getLocalService(serviceName, serviceClz);
        Method method = null;
        FacadeResponse resp;
        Object obj=null;
        try {
            method = serviceClz.getMethod(methodName, paramType);
            obj = method.invoke(seviceRef, args);
            resp=new FacadeResponse(obj);
        }catch(Exception ex){
            Throwable th=ExceptionUtils.getRawThrowable(ex);
            resp=new FacadeResponse(th);
            logger.info("",th);
        }
        return resp;
    }

}
