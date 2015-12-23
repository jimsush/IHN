/*
 * $Id: FixedIntevalSchedulerJob.java, 2015-2-18 下午03:45:01  Exp $
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
 * Description: 执行固定周期调度任务
 * </p>
 * 
 * @author 
 * created 2015-2-18 下午03:45:01
 * modified [who date description]
 * check [who date description]
 */
public abstract class FixedIntervalSchedulerJob extends AbstractSchedulerJob {
    /** 任务开始时间 */
    protected final Date starttime;
    /** 任务结束时间 */
    protected final Date endtime;
    /** 任务执行间隔，单位秒 */
    protected volatile int interval;

    /**
     * 
     * 
     * @param starttime
     *            开始时间
     * @param endtime
     *            结束时间
     * @param interval
     *            任务执行间隔,单位秒
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
     *        任务名称
     * @param groupName
     *        任务组名称
     * @param starttime
     *        开始时间
     * @param endtime
     *        结束时间
     * @param interval
     *        时间间隔,单位秒
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
     * 设置调度任务时间间隔
     * 该方法必须在启动任务之前调用,任务启动之后,如果需要该参数有效,必须重新启动调度任务
     * @param interval
     *         时间间隔 单位秒
     */
    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * 获取调度任务执行间隔
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
