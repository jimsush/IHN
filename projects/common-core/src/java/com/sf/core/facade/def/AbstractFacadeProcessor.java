/*
 * $Id: AbstractFacadeProcessor.java, 2015-3-7 下午01:03:01 sufeng Exp $
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
 * Title: AbstractFacadeProcessor
 * </p>
 * <p>
 * Description: 对FacadeProcessor进行扩展的抽象基类，提供了最简单的默认实现
 * </p>
 * 
 * @author sufeng
 * created 2015-3-7 下午01:03:01
 * modified [who date description]
 * check [who date description]
 */
public abstract class AbstractFacadeProcessor implements FacadeProcessor{

    @Override
    public FacadeResponse afterInvoke(FacadeResponse lastResponse, Class<?> serviceClz, String methodName,
            Class<?>[] paramType, Object[] args) {
        // 返回原值
        return lastResponse;
    }

    @Override
    public FacadeResponse beforeInvoke(Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args) {
        return null;
    }
    
    @Override
    public String getName() {
        // 用类名作为facade processor name
        return getClass().getSimpleName();
    }

}
