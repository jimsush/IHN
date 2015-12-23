/*
 * $Id: OneTimeSchedulerJob.java, 2015-3-3 ����12:49:41  Exp $
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

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

/**
 * <p>
 * Title: OneTimeSchedulerJob
 * </p>
 * <p>
 * Description: ִ��һ�εĵ�������
 * </p>
 * 
 * @author 
 * created 2015-3-3 ����12:49:41
 * modified [who date description]
 * check [who date description]
 */
public abstract class OneTimeSchedulerJob extends AbstractSchedulerJob {

    private Date startTime;

    /**
     * @param startTime
     *            ����ʼʱ�䣬���startTimeΪnull���ӵ�ǰʱ�俪ʼִ��
     */
    public OneTimeSchedulerJob(Date startTime) {
        super();
        this.startTime = new Date(startTime.getTime());
    }
    /**
     * 
     * @param jobName
     *         ��������
     * @param groupName
     *         ��������������
     * @param startTime
     *         ����ʼʱ�䣬���startTimeΪnull���ӵ�ǰʱ�俪ʼִ��
     */
    public OneTimeSchedulerJob(String jobName,String groupName,Date startTime) {
        super(jobName,groupName);
        this.startTime = new Date(startTime.getTime());
    }

    /**
     * 
     * @see com.sf.base.scheduler.SchedulerJob#getTrigger()
     */
    @Override
    public Trigger getTrigger() {
        Trigger trigger = new SimpleTrigger();
        if (startTime == null)
            trigger.setStartTime(new Date());
        else
            trigger.setStartTime(startTime);
        trigger.setName(this.getJobName());
        trigger.setJobName(this.getJobName());
        trigger.setJobGroup(this.getJobGroupName());
        return trigger;
    }

}
