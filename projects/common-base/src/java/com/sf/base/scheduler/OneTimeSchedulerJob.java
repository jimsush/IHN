/*
 * $Id: OneTimeSchedulerJob.java, 2015-3-3 下午12:49:41  Exp $
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
 * Description: 执行一次的调度任务
 * </p>
 * 
 * @author 
 * created 2015-3-3 下午12:49:41
 * modified [who date description]
 * check [who date description]
 */
public abstract class OneTimeSchedulerJob extends AbstractSchedulerJob {

    private Date startTime;

    /**
     * @param startTime
     *            任务开始时间，如果startTime为null，从当前时间开始执行
     */
    public OneTimeSchedulerJob(Date startTime) {
        super();
        this.startTime = new Date(startTime.getTime());
    }
    /**
     * 
     * @param jobName
     *         任务名称
     * @param groupName
     *         任务所在任务组
     * @param startTime
     *         任务开始时间，如果startTime为null，从当前时间开始执行
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
