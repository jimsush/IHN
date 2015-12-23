/*
 * $Id: CommunicateObject.java, 2015-9-27 下午03:53:37 aaron Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;

import com.sf.core.message.def.MessageReceiver;


/**
 * <p>
 * Title: CommunicateObject
 * </p>
 * <p>
 * Description: 通信对象(包括local通信对象),用来获取服务、订阅消息
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 下午03:53:37
 * modified [who date description]
 * check [who date description]
 */
public interface CommunicateObject {
    
	/**
	 * 获取一个本地或远程接口
	 * @param <T>
	 * @param serviceName
	 * @param serviceItf
	 * @return
	 */
    public <T> T getService(String serviceName, Class<T> serviceItf);

    /**
     * 注册消息订阅者
     * @param name 消息名
     * @param receiver 消息订阅者的回调处理
     */
    public void subscribe(String name, MessageReceiver receiver);
    
    /**
     * 反注册消息订阅
     * @param name 消息名
     * @param receiver 消息订阅者的回调处理
     */
    public void unsubscribe(String name, MessageReceiver receiver);
    
}

