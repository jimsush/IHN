/*
 * $Id: RemoteComunicateObject.java, 2015-9-27 ����04:02:57 aaron Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.def;


/**
 * <p>
 * Title: RemoteComunicateObject
 * </p>
 * <p>
 * Description: Զ��ͨ�Ŷ���
 * </p>
 * 
 * @author aaron
 * created 2015-9-27 ����04:02:57
 * modified [who date description]
 * check [who date description]
 * @see CommunicateObject
 * @see RemoteCommunicate4Container
 */
public interface RemoteCommunicateObject extends CommunicateObject {

    /**
     * ������Զ����ϵͳ�Ľ���
     */
    public void cleanup();
    
    /**
     * Զ�˷�����IP
     * @return ip��ַ
     */
    public String getRemoteIp();
    
    /**
     * Զ�̷�������port
     * @return
     */
    public int getRemoteServerPort();
    
    /**
     * ����Զ����ϵͳ��ip,port
     * @param ip
     * @param port
     */
    public void resetRemoteServer(String ip,int port);
    
    /**
     * ��Զ����ϵͳ������״̬
     * @return
     */
    public int getLinkStatus();
    
}
