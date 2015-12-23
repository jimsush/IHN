/*
 * $Id: RemoteInvocationFacade.java, 2015-11-5 ����10:41:30  Exp $
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
 * Description: Զ�̵���ͳһ��ת������
 * </p>
 * 
 * @author 
 * created 2015-11-5 ����10:41:30
 * modified [who date description]
 * check [who date description]
 */
public interface RemoteInvocationFacade {
    
    /**
     * ת�����õĽӿ�
     * @param clz Զ�̽ӿ���
     * @param methodName ������
     * @param paramType �����������
     * @param args      �����б�
     * @param sessionId �Ựid
     * @return
     * @throws
     */
    public Object remoteInvoke(Class<?> clz, String methodName, Class<?>[] paramType,
            Object[] args, Long sessionId) throws Throwable ;
    
}
