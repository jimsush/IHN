/*
 * $Id: MessageServiceManagerFactory.java, 2015-2-25 ����11:47:52 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.message.impl;

/**
 * <p>
 * Title: MessageServiceManagerFactory
 * </p>
 * <p>
 * Description:��Ϣ����Ĺ����࣬������ȡ��Ϣ����manager
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 ����11:47:52
 * modified [who date description]
 * check [who date description]
 */
public class MessageServiceManagerFactory {

    private static MessageServiceManager messageServiceManager=new MessageServiceManagerImpl();
    
    /**
     * ��ȡ��Ϣ���й���ķ���ӿ�
     * @return
     */
    public static MessageServiceManager getMessageServiceManager(){
        return messageServiceManager;
    }
    
}
