/*
 * $Id: CoreContext.java, 2015-10-11 ����09:38:59 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;

import java.io.Serializable;

import com.sf.core.message.def.MessageMetadata;


/**
 * <p>
 * Title: CoreContext
 * </p>
 * <p>
 * Description: ģ��������ӿ�
 * </p>
 * 
 * @author sufeng
 * created 2015-10-11 ����09:38:59
 * modified [who date description]
 * check [who date description]
 */
public abstract class CoreContext {
    
    /**
     * ��ȡ����ͨ�Ŷ���
     */
    public abstract CommunicateObject local();
    
    /**
     * ע��local����
     * @param serviceName     ������
     * @param interfaceClass  �ӿ���
     * @param serviceInstance ����ʵ��
     */
    public abstract void setLocalService(String serviceName, Class<?> interfaceClass, Object serviceInstance);
    
    /**
     * ��������Ϊremote����
     * @param serviceName     ������
     * @param interfaceClass  Զ�̷���Ľӿ���
     * @param serviceInstance ����ʵ��
     */
    public abstract void setRemoteService(String serviceName, Class<?> interfaceClass, Object serviceInstance);
    
    /**
     * ��ע��local����
     * @param serviceName
     * @param serviceItf
     */
    public abstract void unregisterLocalService(String serviceName, Class<?> serviceItf);
    
    /**
     * ��ע��remote����
     * @param serviceName
     * @param serviceItf
     */
    public abstract void unregisterRemoteService(String serviceName, Class<?> serviceItf);
    
    /**
     * ���ӵ�һ��Զ����ϵͳ
     * @param ip
     * @param port
     */
    public abstract RemoteCommunicateObject remote(String ip, int port);
    
    /**
     * ������Ϣ
     * @param messageName
     * @param messageInfo
     */
    public abstract void publish(String messageName,Serializable messageInfo);
    
    /**
     * ��ȡ���Թ�����
     * @return
     */
    public abstract ContainerPropertiesManager getPropertiesManager();

    /**
     * ��ȡ����ϵͳ�����ṩԶ����Ϣ�������Ϣ��������Ϣ
     * @return ��Ϣ�������Ļ���������Ϣ
     */
    public abstract MessageMetadata getSelfMessageMetadata();
    
    /**
     * ��ȡģ�������
     * @return
     */
    public static CoreContext getInstance() {return instance;}
    protected static CoreContext instance = null;
    
}
