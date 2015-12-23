/*
 * $Id: SessionServiceImpl.java, 2015-10-11 下午05:45:56 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.session.impl;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.sf.base.log.def.Logger;
import com.sf.base.log.def.SimpleLogger;
import com.sf.base.log.def.SfLog;
import com.sf.core.container.def.CoreContext;
import com.sf.core.session.def.Session;
import com.sf.core.session.def.SessionMessage;
import com.sf.core.session.def.SessionService;
import com.sf.core.session.def.SessionState;

/**
 * <p>
 * Title: SessionServiceImpl
 * </p>
 * <p>
 * Description: 会话管理服务的实现类
 * </p>
 * 
 * @author sufeng
 * created 2015-10-11 下午05:45:56
 * modified [who date description]
 * check [who date description]
 */
public class SessionServiceImpl implements SessionService {

    /** sessionid存入threadlocal中,主线程消亡,存储的值自动会消亡 */
    private final ThreadLocal<Long> sessions = new InheritableThreadLocal<Long>();
    
    /** 存储Session */
    private final Map<Long, Session> sessionMap = new ConcurrentHashMap<Long, Session>();

    /** session id */
    private final AtomicLong sessionKey = new AtomicLong(2L);
    
    private Logger logger;

    public SessionServiceImpl(){
        try{
            logger=SfLog.getDefaultLogger();
        }catch(Exception ex){
            logger=new SimpleLogger("simple");
        }
    }
    
    @Override
    public Collection<Session> getAllSessions() {
        return sessionMap.values();
    }

    @Override
    public Session getSessionById(Long sessionId) {
        return sessionMap.get(sessionId);
    }
    
    @Override
    public void setSessionInCurrentThread(Long sessionId) {
        sessions.set(sessionId);
    }

    @Override
    public Session getSession() {
        Long sessionId = getSessionId();
        if (sessionId == null) {
            Thread th = Thread.currentThread();
            logger.warn("local thread have no sessionId, th=" + th.getName());
            return null;
        }

        Session session = sessionMap.get(sessionId);
        if (session == null) {
            logger.warn("sessionMap have no session, sessionId=" + sessionId);
        }
        return session;
    }

    @Override
    public Long getSessionId() {
        return sessions.get();
    }

    @Override
    public Session connect(String clientIp, String serverIp) {
        Session session = new Session(sessionKey.getAndIncrement(), clientIp);
        session.setServerIp(serverIp);
        session.setSessionState(SessionState.Linkup);
        session.setLastActivetime(System.currentTimeMillis());
        sessions.set(session.getSessionId());
        sessionMap.put(Long.valueOf(session.getSessionId().longValue()), session);
 
        logger.info(clientIp + " connect current system, session Id="+session.getSessionId());
        
        // send message
        CoreContext.getInstance().publish(SessionMessage.NAME, new SessionMessage(session));
        return session;
    }

    @Override
    public void cleanup(Long sessionId) {
        Session session = sessionMap.remove(sessionId);
        if(session!=null){
            logger.info(session.getIp() + " disconnect current system, will cleanup, session Id="+session.getSessionId());
            session.setSessionState(SessionState.Linkdown);
        
            // send message
            CoreContext.getInstance().publish(SessionMessage.NAME, new SessionMessage(session));
        }
    }

}
