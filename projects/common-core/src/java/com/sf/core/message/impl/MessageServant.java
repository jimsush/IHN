/*
 * $Id: MessageServant.java, 2015-9-27 下午05:56:37 aaron Exp $
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
 * Title: MessageServant
 * </p>
 * <p>
 * Description: 发送远端消息的管理接口(仅仅在平台中使用,上层网管不能调用该类)
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 下午05:56:37
 * modified [who date description]
 * check [who date description]
 */
public interface MessageServant {
	
	/**
	 * 注册到容器中的service名称
	 */
	public static final String SERVICE_NAME="messageServant";
	
	/**
	 * 消息端口
	 * @return
	 */
    public int getMsgPort();
    
    /**
     * 订阅消息
     * @param sessionId 订阅者的会话id
     * @param name 消息类别名
     */
    public void subscribe(long sessionId, String name);
    
    /**
     * 订阅消息
     * @param sessionId 订阅者的会话id
     * @param name 消息类别名
     */
    public void unsubscribe(long sessionId, String name);
    
}
