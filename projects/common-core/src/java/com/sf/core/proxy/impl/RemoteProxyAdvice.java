package com.sf.core.proxy.impl;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.sf.base.exception.ExceptionUtils;
import com.sf.core.container.def.RemoteCommunicate4Container;
import com.sf.core.container.def.RemoteCommunicateObject;
import com.sf.core.facade.def.RemoteInvocationFacade;
import com.sf.core.proxy.def.ProxyProcessor;
import com.sf.core.service.impl.remote.exporter.RemoteConnector;

/**
 * <p>
 * Title: RemoteProxyAdvice
 * </p>
 * <p>
 * Description:AOP�����������е�Զ����ϵͳ�ĵ��ö��ᾭ�������invoke����
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 ����13:23:02
 * modified [who date description]
 * check [who date description]
 */
public class RemoteProxyAdvice implements MethodInterceptor {
    
    private RemoteCommunicate4Container remoteCommunicate4Container;
    private RemoteCommunicateObject remoteCommunicateObject;
    
    /**
     * proxy proccessor��Ŀǰֻ��DefaultProxyProcessor
     */
    private ProxyProcessor proxyProcessor;
    
    public RemoteProxyAdvice(RemoteCommunicateObject remoteCommunicateObject){
        this.remoteCommunicateObject=remoteCommunicateObject;
        
        if(remoteCommunicateObject instanceof RemoteCommunicate4Container)
            remoteCommunicate4Container=(RemoteCommunicate4Container)remoteCommunicateObject;
        
        DefaultProxyProcessor defaultProxyProcessor=new DefaultProxyProcessor(this.remoteCommunicateObject);
        setProxyProcessor(defaultProxyProcessor);
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object[] arguments = invocation.getArguments();
        Method method = invocation.getMethod();
        String methodName = method.getName();
        // ���ص����ǽӿ���,����ӿ��м̳У�����ӿڼ̳ж���ӽӿڣ�,����õ������ķ���ӿ���
        Class<?> declaringClz = method.getDeclaringClass();
        Class<?> clz=getRealRemoteServiceClass(declaringClz);
        Object ret=null;
        // ���õ�Զ����ϵͳ��facade
        try{
            if(clz.equals(RemoteInvocationFacade.class)){
                ret=invocation.proceed();
            }else{
                ret=proxyProcessor.invoke(clz, methodName, method.getParameterTypes(), arguments, remoteCommunicate4Container.getSessionId());
                
                // �˴�����������ping 
                if(clz.equals(RemoteConnector.class)){
                    if(methodName.equals("connect")){
                        // �����������
                        remoteCommunicate4Container.setSessionId((Long)ret);
                        PingRemoteServerJob pingJob = new PingRemoteServerJob(remoteCommunicateObject,30);
                        remoteCommunicate4Container.startPing(pingJob);
                    }else if(methodName.equals("shutdown")){
                        // ֹͣ�������
                        remoteCommunicate4Container.stopPing();
                    }
                }
            }
        }catch(Throwable ex){
            Throwable actualEx = ExceptionUtils.getRawThrowable(ex);
            throw actualEx;
        }
        return ret;
    }
    
    /**
     * Ϊ�˱���ÿ��ȥ��ѯ,�˴�cache�˵��ýӿڶ�Ӧ�ķ���ӿ�map
     */
    private Map<Class<?>,Class<?>> serviceClassCache=new ConcurrentHashMap<Class<?>,Class<?>>();
    
    /**
     * ��cache�л�ȡ��ǰ����class�������ķ�������(���ӿڼ̳нӿڵ�ʱ����ڸ�����)
     * @param serviceInterface
     * @return
     */
    private synchronized Class<?> getRealRemoteServiceClass(Class<?> serviceInterface){
        Class<?> realClass=serviceClassCache.get(serviceInterface);//from cache
        if(realClass!=null)
            return realClass;
        
        // lookup from container
        Class<?> clz=remoteCommunicate4Container.getRealRemoteServiceClass(serviceInterface);
        if(clz!=null){
            serviceClassCache.put(serviceInterface, clz);//save to cache
            realClass=clz;
        }
        return realClass;
    }
    
    ProxyProcessor getProxyProcessor() {
        return proxyProcessor;
    }
    
    void setProxyProcessor(ProxyProcessor proxyInvoker) {
        this.proxyProcessor = proxyInvoker;
    }
    
    public RemoteCommunicateObject getRemoteCommunicateObject(){
        return remoteCommunicateObject;
    }
    
}
