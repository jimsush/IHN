/*
 * $Id: FacadeProcessor.java, 2015-1-30 下午04:32:28 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.facade.def;

/**
 * <p>
 * Title: FacadeProcessor
 * </p>
 * <p>
 * Description: Facade的处理器，可以被替换
 * </p>
 * 
 * @author sufeng
 * created 2015-1-30 下午04:32:28
 * modified [who date description]
 * check [who date description]
 */
public interface FacadeProcessor {

    /**
     * Facade Processor的名字
     * @return
     */
    public String getName();
    
    /**
     * 调用前处理
     * @param serviceClz 调用的服务接口类名
     * @param methodName 方法名
     * @param paramType  参数类型列表
     * @param args       参数列表
     * @return FacadeResponse before处理的结果,可以为null，根据这个reponse来判断是否进行下一步的调用
     */
    public FacadeResponse beforeInvoke(Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args);
    
    /**
     * 调用后处理
     * @param response   前一个调用的结果
     * @param serviceClz 调用的服务接口类名
     * @param methodName 方法名
     * @param paramType  参数类型列表
     * @param args       参数列表
     * @return 返回结果
     */
    public FacadeResponse afterInvoke(FacadeResponse lastResponse, Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args);
    
    
}
