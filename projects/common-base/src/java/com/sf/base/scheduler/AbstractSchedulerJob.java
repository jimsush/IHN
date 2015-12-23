/*
 * $Id: AbstratSchedulerJob.java, 2015-2-18 ����03:00:59  Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

import org.quartz.Scheduler;

/**
 * <p>
 * Title: AbstratSchedulerJob
 * </p>
 * <p>
 * Description:
 * �������������
 * </p>
 * 
 * @author 
 * created 2015-2-18 ����03:00:59
 * modified [who date description]
 * check [who date description]
 */
public abstract class AbstractSchedulerJob implements SchedulerJob {
    
    private final static String DEFAULT_GROUP = Scheduler.DEFAULT_GROUP;
    
    /** �������� */
    private String jobName;
    
    /** ���������� */
    private String jobGroupName;

    /**
     * 
     * ����ϵͳ�Զ����ɵ��������ơ����������ƵĹ��캯��
     * 
     */
    public AbstractSchedulerJob() {
        this(DEFAULT_GROUP,SchedulerJobNamingEngine.createSchedulerJobName());
    }
    /**
     * �Զ����������ơ����������ƵĹ��캯����
     * ��һ����������������У����������Ʊ�����Ψһ�ģ�������������������Ҳ������Ψһ��
     * @param jobName
     *        ����������������
     * @param groupName
     *        �����������ڵ�������
     */
    public AbstractSchedulerJob(String jobName,String groupName) {
        super();
        if(jobName==null||groupName==null)
            throw new NullPointerException("jobName or jobGroupName is null");
        this.jobGroupName=groupName;
        this.jobName=jobName;
    }
    /**
     * 
     * @see com.sf.base.scheduler.SchedulerJob#init()
     */
    public void init() {

    }
    /**
     * 
     * @see com.sf.base.scheduler.SchedulerJob#destory()
     */
    public void destory(){
        
    }
    /**
     * 
     * @see com.sf.base.scheduler.SchedulerJob#getJobGroupName()
     */
    @Override
    public String getJobGroupName() {
        return jobGroupName;
    }
    /**
     * 
     * @see com.sf.base.scheduler.SchedulerJob#getJobName()
     */
    @Override
    public String getJobName() {
        return jobName;
    }
    /**
     * 
     * @see com.sf.base.scheduler.SchedulerJob#needFinished()
     */
    @Override
    public boolean needFinished(){
        return false;
    }
    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString(){
        return jobGroupName+":"+jobName;
    }
    
   

}
