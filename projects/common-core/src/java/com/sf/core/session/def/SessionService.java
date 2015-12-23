/*
 * $Id: SessionService.java, 2015-10-11 ����11:30:08 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.session.def;

import java.util.Collection;

/**
 * <p>
 * Title: SessionService
 * </p>
 * <p>
 * Description: �Ự����ӿ�
 * </p>
 * 
 * @author sufeng
 * created 2015-10-11 ����11:30:08
 * modified [who date description]
 * check [who date description]
 */
public interface SessionService {
    
    /**
     * ע��session
     * @param clientIp    ��ǰ��¼�û����ڿͻ��˵�ip��ַ
     * @param serverIp    ������ϵͳ��IP��Ϊ�˹��һ�������ö�IP��������˴����Ӹò�����
     * @return Session
     */
    public Session connect(String clientIp, String serverIp);
    
    /**
     * ע��session
     * 
     * @param sessionId
     */
    public void cleanup(Long sessionId);
    
    /**
     * ����SessionId��ȡSession
     * @param sessionId
     * @return
     */
    public Session getSessionById(Long sessionId);
    
    /**
     * session id�洢�ڱ����߳���
     * @param sessionId
     */
    public void setSessionInCurrentThread(Long sessionId);
    
    /**
     * ��ȡ��ǰ�̵߳ĻỰSession
     * 
     * @return
     */
    public Session getSession();
    
    /**
     * ��ȡ��ǰ�̵߳�Session Id
     * @return
     */
    public Long getSessionId();
    
    /**
     * ��ȡ��ǰ���лỰSession
     * 
     * @return
     */
    public Collection<Session> getAllSessions();

}
