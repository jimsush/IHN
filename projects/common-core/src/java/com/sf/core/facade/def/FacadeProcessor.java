/*
 * $Id: FacadeProcessor.java, 2015-1-30 ����04:32:28 sufeng Exp $
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
 * Description: Facade�Ĵ����������Ա��滻
 * </p>
 * 
 * @author sufeng
 * created 2015-1-30 ����04:32:28
 * modified [who date description]
 * check [who date description]
 */
public interface FacadeProcessor {

    /**
     * Facade Processor������
     * @return
     */
    public String getName();
    
    /**
     * ����ǰ����
     * @param serviceClz ���õķ���ӿ�����
     * @param methodName ������
     * @param paramType  ���������б�
     * @param args       �����б�
     * @return FacadeResponse before����Ľ��,����Ϊnull���������reponse���ж��Ƿ������һ���ĵ���
     */
    public FacadeResponse beforeInvoke(Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args);
    
    /**
     * ���ú���
     * @param response   ǰһ�����õĽ��
     * @param serviceClz ���õķ���ӿ�����
     * @param methodName ������
     * @param paramType  ���������б�
     * @param args       �����б�
     * @return ���ؽ��
     */
    public FacadeResponse afterInvoke(FacadeResponse lastResponse, Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args);
    
    
}
