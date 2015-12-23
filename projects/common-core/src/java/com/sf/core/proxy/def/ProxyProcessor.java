/*
 * $Id: ProxyProcessor.java, 2015-1-31 ����11:08:34 sufeng Exp $
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
 * Description: proxy�ĵ��ô����������Ա��滻��client proxy, sbi proxy, lct proxy��
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 ����11:08:34
 * modified [who date description]
 * check [who date description]
 */
public interface ProxyProcessor {
    
    /**
     * processor������
     * @return
     */
    public String getName();
    
    /**
     * proxy���õ�facade�Ĵ���
     * @param serviceClz ���õķ���ӿ�����
     * @param methodName ������
     * @param paramType  ���������б�
     * @param args       �����б�
     * @param sessionId  �ỰID
     * @return
     * @throws
     */
    public Object invoke(Class<?> serviceClz, String methodName, Class<?>[] paramType, Object[] args,Long sessionId) throws Throwable;

}
