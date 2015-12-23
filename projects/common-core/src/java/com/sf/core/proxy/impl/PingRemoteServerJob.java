/*
 * $Id: PingRemoteServerJob.java, 2015-2-11 ����05:06:39 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.proxy.impl;

import com.sf.base.scheduler.FixedIntervalSchedulerJob;
import com.sf.core.container.def.CoreContext;
import com.sf.core.container.def.RemoteCommunicate4Container;
import com.sf.core.container.def.RemoteCommunicateObject;
import com.sf.core.service.impl.remote.exporter.RemoteConnector;
import com.sf.core.session.def.SessionMessage;

/**
 * <p>
 * Title: PingRemoteServerJob
 * </p>
 * <p>
 * Description: �ͻ�����ϵͳpingԶ����ϵͳ���Դ������Ự����״̬������client ping server, server ping sbi, server ping sub ems��
 * </p>
 * 
 * @author sufeng
 * created 2015-2-11 ����05:06:39
 * modified [who date description]
 * check [who date description]
 */
public class PingRemoteServerJob extends FixedIntervalSchedulerJob {

    private RemoteCommunicateObject remoteCommunicateObject;
    private RemoteCommunicate4Container remoteCommunicate4Container;
    private RemoteConnector remoteConnector;
    private long lastActiveTime;
    private boolean activeStatus=true;
    
    /**
     * 
     * @param remoteCommunicateObject
     * @param interval ��
     */
    public PingRemoteServerJob(RemoteCommunicateObject remoteCommunicateObject,int interval) {
        super(null, null, interval);
        
        this.remoteCommunicateObject=remoteCommunicateObject;
        if(remoteCommunicateObject instanceof RemoteCommunicate4Container)
            remoteCommunicate4Container=(RemoteCommunicate4Container)remoteCommunicateObject;
        
        remoteConnector = this.remoteCommunicateObject.getService("remoteConnector", RemoteConnector.class);
    }

    @Override
    public void execute() {
        if(this.remoteCommunicateObject==null)
            return;
        
        // ping remote communication object
        try{
            remoteConnector.ping(remoteCommunicate4Container.getSessionId());
        }catch(Exception ex){
            if(activeStatus)
                activeStatus=false;
            
            // ����ܾ�ûpingͨ�ˣ�����һ��lost communication����Ϣ
            long now=System.currentTimeMillis();
            long distance=now-lastActiveTime; 
            if(distance>=120000){
                // 2����ûping������Ϊ������
                remoteCommunicate4Container.setLinkStatus(0);
                // ���ͱ�����Ϣ
                SessionMessage message=new SessionMessage(remoteCommunicateObject);
                CoreContext.getInstance().publish(message.getName(),message);
            }
            return ;
        }

        lastActiveTime=System.currentTimeMillis();
        if(!activeStatus){
            activeStatus=true;
            if(this.remoteCommunicateObject.getLinkStatus()!=1){
                remoteCommunicate4Container.setLinkStatus(1);
                // ���ͻָ����ӵı�����Ϣ
                SessionMessage message=new SessionMessage(remoteCommunicateObject);
                CoreContext.getInstance().publish(message.getName(),message);
            }
        }
    }

}
