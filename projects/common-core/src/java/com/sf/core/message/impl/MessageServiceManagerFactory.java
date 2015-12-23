/*
 * $Id: MessageServiceManagerFactory.java, 2015-2-25 上午11:47:52 sufeng Exp $
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
 * Description:消息服务的工厂类，用来获取消息服务manager
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 上午11:47:52
 * modified [who date description]
 * check [who date description]
 */
public class MessageServiceManagerFactory {

    private static MessageServiceManager messageServiceManager=new MessageServiceManagerImpl();
    
    /**
     * 获取消息进行管理的服务接口
     * @return
     */
    public static MessageServiceManager getMessageServiceManager(){
        return messageServiceManager;
    }
    
}
