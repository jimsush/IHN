/*
 * $Id: BaseMessage.java, 2015-9-20 ����11:32:51 sufeng Exp $
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
 * Description: ��Ϣ����
 * </p>
 * 
 * @author sufeng
 * created 2015-9-20 ����01:07:17
 * modified [who date description]
 * check [who date description]
 */
public class BaseMessage implements Serializable {
    
    private static final long serialVersionUID = -5163088215909807762L;
    
    /**
     * ��Ϣ�����������࣬���ͻ��ı�����name������sm.msg,topo.msg,system.msg
     */
    private String name;

    /** ��Ϣ����ʱ�� */
    private long occuredTime;
    
    /** ��Ϣ���Ķ��� */
    private Serializable messageSource;
    
    /**
     * @param messageName   ��Ϣ��������sm.msg,topo.msg,system.msg
     * @param messageSource ������Ϣ�Ķ���id
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
     * ��Ϣ������
     * <br>�ϲ�Ӧ�ö������Ϣ���Բ��̳���BaseMessage�������������л��ģ�ͬʱ�ṩgetName����
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
