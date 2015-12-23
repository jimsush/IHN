/*
 * $Id: MessageReceiver.java, 2015-9-27 ����04:30:50 aaron Exp $
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

import com.sf.core.container.def.CommunicateObject;

/**
 * <p>
 * Title: MessageReceiver
 * </p>
 * <p>
 * Description: ��Ϣ���ջص�
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 ����04:30:50
 * modified [who date description]
 * check [who date description]
 * @see com.sf.core.container.def.CommunicateObject
 */
public interface MessageReceiver {
    
    /**
     * ��Ϣ���յ��Ļص�����
     * @param co  ͨ�Ŷ���
     * @param name ��Ϣ��
     * @param msg  ��Ϣ��
     */
    public void receive(CommunicateObject co, String name, Serializable msg);
    
}
