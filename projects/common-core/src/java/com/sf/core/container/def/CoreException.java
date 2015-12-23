/*
 * $Id: CoreException.java, 2015-2-24 ����03:46:34 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;

import com.sf.base.exception.SfException;

/**
 * <p>
 * Title: CoreException
 * </p>
 * <p>
 * Description: ��������ʹ�õ��쳣��
 * </p>
 * 
 * @author sufeng
 * created 2015-2-24 ����03:46:34
 * modified [who date description]
 * check [who date description]
 */
public class CoreException extends SfException {

    private static final long serialVersionUID = 2298659607995553625L;
    
    /**
     * ģ�����ʧ�� {0}
     */
    public static final int MODULE_LOAD_FAILED=1;
    
    /**
     * coreģ��initʧ�� {0}
     */
    public static final int CORE_MODULE_INIT_FAILED=2;
    
    /**
     * �ӿ���û��match����Ӧ����service name,{0}{1}
     */
    public static final int INTERFACE_NOT_MATCH_SERVICENAME=3;
    
    /**
     * ע��Զ�̷���ʧ��,{0}
     */
    public static final int REGISTER_REMOTE_SERVICE_FAILED=4;
    
    /**
     * ����˿ڱ�ռ��,{0}
     */
    public static final int PORT_USED=5;
    
    /** 
     * spring����ʧ��,{0}
     */
    public static final int SPRING_LOAD_FAILED=6;
    
    /** 
     * JMS��Ϣ��������ʼ��ʧ��,{0}
     */
    public static final int JMS_MANAGER_INIT_FAILED=7;
    
    /** 
     * ��ȡJMS���ӹ���ʧ��,{0}
     */
    public static final int JMS_CONNECTION_FACTORY_LOOKUP_FAILED=8;
    
    /** 
     * ��ȡJMS topicʧ��,{0}
     */
    public static final int JMS_TOPIC_LOOKUP_FAILED=9;
    
    /** 
     * ����JMS topicʧ��,{0}
     */
    public static final int JMS_TOPIC_INIT_FAILED=10;
    
    /**
     * ��Ч��session
     */
    public static final int ILLEGAL_SESSION=11;
    
    public CoreException(int errorCode, String... source) {
        super(errorCode, source);
    }
    
    public CoreException(int errorCode, Throwable th, String... source) {
        super(errorCode, th, source);
    }

    

}
