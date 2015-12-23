/*
 * $Id: RemoteConnector.java, 2015-12-2 ����11:45:48 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.service.impl.remote.exporter;

import com.sf.core.message.def.MessageMetadata;

/**
 * <p>
 * Title: RemoteConnector
 * </p>
 * <p>
 * Description: �������õģ��û���ϵͳ�以���Ľӿڣ�����������ϵͳ�����������
 * ,��Ҫͨ��RMIԶ�̱�¶��ȥ
 * </p>
 * 
 * @author sufeng
 * created 2015-12-2 ����11:45:48
 * modified [who date description]
 * check [who date description]
 */
public interface RemoteConnector {

    /**
     * ��Զ�˽�������
     * @param server ip     ������ϵͳ��ip
     * @return Session ID
     */
    public Long connect(String serverIp);
    
    /**
     * Զ�˷�������jms��Ϣ������������Ϣ
     * @param sessionId
     * @return
     */
    public MessageMetadata getMessageMetadata(Long sessionId);
    
    /**
     * ��Զ����ϵͳ��������
     * @param sessionId
     */
    public void shutdown(Long sessionId);
    
    /**
     * pingԶ����ϵͳ���������������Ƿ�link up
     * @param sessionId
     */
    public void ping(Long sessionId);
    
}
