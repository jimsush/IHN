/*
 * $Id: BaseMessage.java, 2015-9-20 上午11:32:51 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.message.def;

import java.io.Serializable;

/**
 * <p>
 * Title: BaseMessage
 * </p>
 * <p>
 * Description: 消息基类
 * </p>
 * 
 * @author sufeng
 * created 2015-9-20 下午01:07:17
 * modified [who date description]
 * check [who date description]
 */
public class BaseMessage implements Serializable {
    
    private static final long serialVersionUID = -5163088215909807762L;
    
    /**
     * 消息名，用来分类，发送或订阅必须有name，比如sm.msg,topo.msg,system.msg
     */
    private String name;

    /** 消息产生时间 */
    private long occuredTime;
    
    /** 消息正文对象 */
    private Serializable messageSource;
    
    /**
     * @param messageName   消息名，比如sm.msg,topo.msg,system.msg
     * @param messageSource 触发消息的对象id
     */
    public BaseMessage(String messageName,Serializable messageSource){
        this.name=messageName;
        this.messageSource=messageSource;
        this.setOccuredTime(System.currentTimeMillis());   
    }
    
    public long getOccuredTime() {
        return occuredTime;
    }
    
    public void setOccuredTime(long occuredTime) {
        this.occuredTime = occuredTime;
    }

    public Serializable getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(Serializable messageSource) {
        this.messageSource = messageSource;
    }
    
    /**
     * 消息的名称
     * <br>上层应用定义的消息可以不继承于BaseMessage，但必须是序列化的，同时提供getName方法
     * @return
     */
    public String getName() {
		return name;
	}
    
    public void setName(String name) {
		this.name = name;
	}
    
    @Override
    public String toString() {
    	return "name="+name+",messageSource="+messageSource;
    }

}
