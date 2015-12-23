/*
 * $Id: RemoteCommunicate4Container.java, 2015-2-12 ����11:08:27 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;

import com.sf.base.scheduler.SchedulerJob;



/**
 * <p>
 * Title: RemoteCommunicate4Container
 * </p>
 * <p>
 * Description: �����ڲ�ʹ�õ�Զ����ϵͳ��ͨ�Ŷ���Ĺ���ӿ�
 * </p>
 * 
 * @author sufeng
 * created 2015-2-12 ����11:08:27
 * modified [who date description]
 * check [who date description]
 */
public interface RemoteCommunicate4Container {

    /**
     * ����(ip,port��������������)
     */
    public void reconnect();
    
    /**
     * ��ǰ��ϵͳ�ĻỰid
     * @return
     */
    public Long getSessionId();
    
    /**
     * ����session id
     * @param sessionId
     */
    public void setSessionId(Long sessionId);
    
    /**
     * ������Զ����ϵͳ�����ӹ�ϵ
     * @param linkStatus 0 linkdown 1 linkup
     */
    public void setLinkStatus(int linkStatus);
    
    /**
     * ����ping
     * @param pingJob
     */
    public void startPing(SchedulerJob pingJob);
    
    /**
     * ֹͣping
     */
    public void stopPing();
    
    /**
     * ��ȡping������
     * @return ��
     */
    public int getPingInterval();
    
    /**
     * ����ping���
     * @param interval����
     */
    public void setPingInterval(int interval);
    
    /**
     * �õ�������Զ�̷���ӿ�����
     * @param serviceInterface
     * @return
     */
    public Class<?> getRealRemoteServiceClass(Class<?> serviceInterface);
    
}
