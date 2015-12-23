/*
 * $Id: SessionService.java, 2015-10-11 上午11:30:08 sufeng Exp $
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
 * Description: 会话服务接口
 * </p>
 * 
 * @author sufeng
 * created 2015-10-11 上午11:30:08
 * modified [who date description]
 * check [who date description]
 */
public interface SessionService {
    
    /**
     * 注册session
     * @param clientIp    当前登录用户所在客户端的ip地址
     * @param serverIp    服务子系统的IP（为了规避一机器配置多IP的情况，此处增加该参数）
     * @return Session
     */
    public Session connect(String clientIp, String serverIp);
    
    /**
     * 注销session
     * 
     * @param sessionId
     */
    public void cleanup(Long sessionId);
    
    /**
     * 根据SessionId获取Session
     * @param sessionId
     * @return
     */
    public Session getSessionById(Long sessionId);
    
    /**
     * session id存储在本地线程中
     * @param sessionId
     */
    public void setSessionInCurrentThread(Long sessionId);
    
    /**
     * 获取当前线程的会话Session
     * 
     * @return
     */
    public Session getSession();
    
    /**
     * 获取当前线程的Session Id
     * @return
     */
    public Long getSessionId();
    
    /**
     * 获取当前所有会话Session
     * 
     * @return
     */
    public Collection<Session> getAllSessions();

}
