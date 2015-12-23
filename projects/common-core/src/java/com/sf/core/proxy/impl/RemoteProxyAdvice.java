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
 * Description:AOP拦截器，所有到远端子系统的调用都会经过该类的invoke方法
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 下午13:23:02
 * modified [who date description]
 * check [who date description]
 */
public class RemoteProxyAdvice implements MethodInterceptor {
    
    private RemoteCommunicate4Container remoteCommunicate4Container;
    private RemoteCommunicateObject remoteCommunicateObject;
    
    /**
     * proxy proccessor，目前只有DefaultProxyProcessor
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
        // 拦截到的是接口名,如果接口有继承（服务接口继承多个子接口）,必须得到真正的服务接口类
        Class<?> declaringClz = method.getDeclaringClass();
        Class<?> clz=getRealRemoteServiceClass(declaringClz);
        Object ret=null;
        // 调用到远端子系统的facade
        try{
            if(clz.equals(RemoteInvocationFacade.class)){
                ret=invocation.proceed();
            }else{
                ret=proxyProcessor.invoke(clz, methodName, method.getParameterTypes(), arguments, remoteCommunicate4Container.getSessionId());
                
                // 此处来处理启动ping 
                if(clz.equals(RemoteConnector.class)){
                    if(methodName.equals("connect")){
                        // 启动心跳检测
                        remoteCommunicate4Container.setSessionId((Long)ret);
                        PingRemoteServerJob pingJob = new PingRemoteServerJob(remoteCommunicateObject,30);
                        remoteCommunicate4Container.startPing(pingJob);
                    }else if(methodName.equals("shutdown")){
                        // 停止心跳检测
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
     * 为了避免每次去查询,此处cache了调用接口对应的服务接口map
     */
    private Map<Class<?>,Class<?>> serviceClassCache=new ConcurrentHashMap<Class<?>,Class<?>>();
    
    /**
     * 从cache中获取当前方法class的真正的服务类名(当接口继承接口的时候存在该问题)
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
