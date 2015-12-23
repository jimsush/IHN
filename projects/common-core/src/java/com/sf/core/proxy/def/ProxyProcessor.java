/*
 * $Id: ProxyProcessor.java, 2015-1-31 上午11:08:34 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.proxy.def;

/**
 * <p>
 * Title: ProxyProcessor
 * </p>
 * <p>
 * Description: proxy的调用处理器，可以被替换（client proxy, sbi proxy, lct proxy）
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 上午11:08:34
 * modified [who date description]
 * check [who date description]
 */
public interface ProxyProcessor {
    
    /**
     * processor的名字
     * @return
     */
    public String getName();
    
    /**
     * proxy调用到facade的处理
     * @param serviceClz 调用的服务接口类名
     * @param methodName 方法名
     * @param paramType  参数类型列表
     * @param args       参数列表
     * @param sessionId  会话ID
     * @return
     * @throws
     */
    public Object invoke(Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args,Long sessionId) throws Throwable;

}
