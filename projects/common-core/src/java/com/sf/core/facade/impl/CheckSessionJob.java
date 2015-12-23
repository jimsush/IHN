/*
 * $Id: CheckSessionJob.java, 2015-1-31 上午09:38:10 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.facade.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sf.base.scheduler.FixedIntervalSchedulerJob;
import com.sf.core.container.def.CoreContext;
import com.sf.core.session.def.Session;
import com.sf.core.session.def.SessionService;

/**
 * <p>
 * Title: CheckSessionJob
 * </p>
 * <p>
 * Description: 检查对端子系统（比如client）的会话（比如server检查与client的连接状态，sbi检查与server的连接状态, server检查与上级网管的连接状态）
 * </p>
 * 
 * @author sufeng
 * created 2015-1-31 上午09:38:10
 * modified [who date description]
 * check [who date description]
 */
public class CheckSessionJob extends FixedIntervalSchedulerJob {

    private SessionService sessionService;
    private long timeout=60000L; //1分钟
    
    /**
     * 
     * @param interval 秒
     */
    public CheckSessionJob(int interval) {
        super(null, null, interval);
        sessionService=CoreContext.getInstance().local().getService("sessionService", SessionService.class);
    }

    @Override
    public void execute() {
        Collection<Session> rawSessions = sessionService.getAllSessions();
        List<Session> sessions=new ArrayList<Session>();
        sessions.addAll(rawSessions);
        long now=System.currentTimeMillis();
        for(Session session : sessions){
            // 如果超时了，认为session已死
            long distance=now-session.getLastActivetime();
            if(distance>timeout){
                sessionService.cleanup(session.getSessionId());
            }
        }
    }

}
