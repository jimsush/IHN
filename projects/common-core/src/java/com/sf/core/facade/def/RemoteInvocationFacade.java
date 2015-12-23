/*
 * $Id: RemoteInvocationFacade.java, 2015-11-5 上午10:41:30  Exp $
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
 * Title: RemoteInvocationFacade
 * </p>
 * <p>
 * Description: 远程调用统一的转发服务
 * </p>
 * 
 * @author 
 * created 2015-11-5 上午10:41:30
 * modified [who date description]
 * check [who date description]
 */
public interface RemoteInvocationFacade {
    
    /**
     * 转发调用的接口
     * @param clz 远程接口类
     * @param methodName 方法名
     * @param paramType 参数类型类表
     * @param args      参数列表
     * @param sessionId 会话id
     * @return
     * @throws
     */
    public Object remoteInvoke(Class<?> clz, String methodName, Class<?>[] paramType,
            Object[] args, Long sessionId) throws Throwable ;
    
}
