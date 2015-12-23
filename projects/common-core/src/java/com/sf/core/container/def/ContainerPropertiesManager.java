/*
 * $Id: ContainerPropertiesManager.java, 2015-10-11 ����09:36:10 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;

import java.util.Properties;

/**
 * <p>
 * Title: ContainerPropertiesManager
 * </p>
 * <p>
 * Description: �������Թ���,������дһЩkey-value�����Զ�
 * </p>
 * 
 * @author sufeng
 * created 2015-10-11 ����09:36:10
 * modified [who date description]
 * check [who date description]
 */
public interface ContainerPropertiesManager {

    /**
     * ��ǰ��ϵͳ���еĵ�ǰĿ¼
     */
    public static final String KEY_HOME_DIR="homeDir";
    
    /**
     * �ṩԶ�̷����IP��ַ
     */
    public static final String KEY_SERVER_IP="server.ip";
    
    /**
     * �ṩԶ�̷���Ķ˿ں�
     */
    public static final String KEY_RMI_PORT="rmi.port";
    
    /**
     * �ṩԶ����Ϣ��������ip,��ѡ
     */
    public static final String KEY_JMS_IP="jms.ip";
    
    /**
     * �ṩԶ����Ϣ����Ķ˿ں�
     */
    public static final String KEY_JMS_PORT="jms.port";
    
    /**
     * �ṩԶ����Ϣ���ַ���˿ں�
     */
    public static final String KEY_JMS_NAMING_PORT="jms.naming.port";
    
    /**
     * �Ƿ���Ҫ����JMS��Ϣ������:ture/false
     */
    public static final String KEY_JMS_LAUNCH="jms.server.launch";
    
    /**
     * JMS SERVER manager��ʵ������
     */
    public static final String KEY_JMS_MANAGER_CLASS="jms.manager.class";
    
    /**
     * java.naming.factory.initial
     */
    public static final String KEY_JMS_FACTORY_CLASS="java.naming.factory.initial";
    
    /**
     * DB SERVER manager��ʵ������
     */
    public static final String KEY_DB_MANAGER_CLASS="db.manager.class";
    
    /**
     * ��ǰ��ϵͳ���Է�������Ϣ���б�
     */
    public static final String KEY_SUBSYSTEM_MESSAGE_NAMES="subsystem.message";
    
    /**
     * ȱʡ�����IP��ַ
     */
    public static final String VALUE_DEFAULT_SERVER_IP="127.0.0.1";
    
    /**
     * ȱʡԶ�̷���˿ں�
     */
    public static final String VALUE_DEFAULT_RMI_PORT="8888";
    
    /**
     * ȱʡԶ����Ϣ����˿�
     */
    public static final String VALUE_DEFAULT_JMS_PORT="16010";
    
    /**
     * ȱʡԶ����Ϣ���ַ���˿ں�
     */
    public static final String VALUE_DEFAULT_JMS_NAMING_PORT="16400";
    
    /**
     * ��������(����Module��before��put)
     * @param key
     * @param value
     */
    public void put(String key,String value);
    
    /**
     * ��ȡ����
     * @param key
     * @return
     */
    public String get(String key);
    
    /**
     * �õ���ǰ�������Զ�
     * @return
     */
    public Properties getProps();

}
