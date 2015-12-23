/*
 * $Id: AbstractFacadeProcessor.java, 2015-3-7 ����01:03:01 sufeng Exp $
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
 * Description: ��FacadeProcessor������չ�ĳ�����࣬�ṩ����򵥵�Ĭ��ʵ��
 * </p>
 * 
 * @author sufeng
 * created 2015-3-7 ����01:03:01
 * modified [who date description]
 * check [who date description]
 */
public abstract class AbstractFacadeProcessor implements FacadeProcessor{

    @Override
    public FacadeResponse afterInvoke(FacadeResponse lastResponse, Class<?> serviceClz, String methodName,
            Class<?>[] paramType, Object[] args) {
        // ����ԭֵ
        return lastResponse;
    }

    @Override
    public FacadeResponse beforeInvoke(Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args) {
        return null;
    }
    
    @Override
    public String getName() {
        // ��������Ϊfacade processor name
        return getClass().getSimpleName();
    }

}
