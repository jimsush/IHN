/*
 * $Id: CommunicateObject.java, 2015-9-27 ����03:53:37 aaron Exp $
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
 * Description: ͨ�Ŷ���(����localͨ�Ŷ���),������ȡ���񡢶�����Ϣ
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 ����03:53:37
 * modified [who date description]
 * check [who date description]
 */
public interface CommunicateObject {
    
	/**
	 * ��ȡһ�����ػ�Զ�̽ӿ�
	 * @param <T>
	 * @param serviceName
	 * @param serviceItf
	 * @return
	 */
    public <T> T getService(String serviceName, Class<T> serviceItf);

    /**
     * ע����Ϣ������
     * @param name ��Ϣ��
     * @param receiver ��Ϣ�����ߵĻص�����
     */
    public void subscribe(String name, MessageReceiver receiver);
    
    /**
     * ��ע����Ϣ����
     * @param name ��Ϣ��
     * @param receiver ��Ϣ�����ߵĻص�����
     */
    public void unsubscribe(String name, MessageReceiver receiver);
    
}

