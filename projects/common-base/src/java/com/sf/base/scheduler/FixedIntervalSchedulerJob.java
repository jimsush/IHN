/*
 * $Id: FixedIntevalSchedulerJob.java, 2015-2-18 ����03:45:01  Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

import java.util.Date;

import org.quartz.Trigger;
import org.quartz.TriggerUtils;

/**
 * <p>
 * Title: FixedIntevalSchedulerJob
 * </p>
 * <p>
 * Description: ִ�й̶����ڵ�������
 * </p>
 * 
 * @author 
 * created 2015-2-18 ����03:45:01
 * modified [who date description]
 * check [who date description]
 */
public abstract class FixedIntervalSchedulerJob extends AbstractSchedulerJob {
    /** ����ʼʱ�� */
    protected final Date starttime;
    /** �������ʱ�� */
    protected final Date endtime;
    /** ����ִ�м������λ�� */
    protected volatile int interval;

    /**
     * 
     * 
     * @param starttime
     *            ��ʼʱ��
     * @param endtime
     *            ����ʱ��
     * @param interval
     *            ����ִ�м��,��λ��
     */
    public FixedIntervalSchedulerJob(Date starttime, Date endtime, int interval) {
        super();
        if(starttime!=null)
            this.starttime = new Date(starttime.getTime());
        else
            this.starttime=null;
        if(endtime!=null)
            this.endtime = new Date(endtime.getTime());
        else
            this.endtime=null;
        this.interval = interval;
    }
    /**
     * 
     * @param jobName
     *        ��������
     * @param groupName
     *        ����������
     * @param starttime
     *        ��ʼʱ��
     * @param endtime
     *        ����ʱ��
     * @param interval
     *        ʱ����,��λ��
     */
    public FixedIntervalSchedulerJob(String jobName,String groupName,Date starttime, Date endtime, int interval) {
        super(jobName,groupName);
        if(starttime!=null)
            this.starttime = new Date(starttime.getTime());
        else
            this.starttime=null;
        if(endtime!=null)
            this.endtime = new Date(endtime.getTime());
        else
            this.endtime=null;
        this.interval = interval;
    }
    /**
     * ���õ�������ʱ����
     * �÷�����������������֮ǰ����,��������֮��,�����Ҫ�ò�����Ч,��������������������
     * @param interval
     *         ʱ���� ��λ��
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * ��ȡ��������ִ�м��
     * 
     * @return
     */
    public Integer getInterval() {
        return interval;
    }

    /**
     * 
     * @see com.sf.base.scheduler.SchedulerJob#getTrigger()
     */
    @Override
    public Trigger getTrigger() {
        Trigger trigger = TriggerUtils.makeSecondlyTrigger(interval);
        if (starttime == null)
            trigger.setStartTime(new Date());
        else
            trigger.setStartTime(starttime);
        if (endtime != null)
            trigger.setEndTime(endtime);
        trigger.setName(this.getJobName());
        trigger.setJobName(this.getJobName());
        trigger.setJobGroup(this.getJobGroupName());
        trigger.setGroup(this.getJobGroupName());
        return trigger;
    }

}
