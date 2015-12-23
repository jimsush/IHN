/*
 * $Id: ImmediateSchedulerJob.java, 2015-2-18 下午02:56:31  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

import org.quartz.Trigger;
import org.quartz.TriggerUtils;

/**
 * <p>
 * Title: ImmediateSchedulerJob
 * </p>
 * <p>
 * Description: 调度管理器立即以一定周期执行固定次数的任务;
 * </p>
 * 
 * @author 
 * created 2015-2-18 下午02:56:31
 * modified [who date description]
 * check [who date description]
 */
public abstract class ImmediateSchedulerJob extends AbstractSchedulerJob {
    /** 轮询次数 */
    private int repeatCount;
    /** 轮询间隔 单位毫秒 */
    private long repeatInterval;

    /**
     * 
     * @param repearCount
     *            任务执行次数
     * @param repeatInterval
     *            任务执行周期,单位毫秒
     */
    public ImmediateSchedulerJob(int repeatCount, long repeatInterval) {
        super();
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;

    }
  
    /**
     * 
     * @param jobName
     *         任务名称
     * @param groupName
     *         任务所在的组的名称
     * @param repeatCount
     *         任务重复执行次数
     * @param repeatInterval
     *         任务执行周期,单位毫秒
     */
    public ImmediateSchedulerJob(String jobName,String groupName,int repeatCount, long repeatInterval) {
        super(jobName,groupName);
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;

    }
    /**
     * 
     * @see com.sf.base.scheduler.SchedulerJob#getTrigger()
     */
    @Override
    public Trigger getTrigger() {
        Trigger trigger = TriggerUtils.makeImmediateTrigger(repeatCount - 1,// 采用这个触发器，会多执行一次(在重复次数上国外和国内可能不一样)，所以需要减1
                repeatInterval);
        trigger.setName(this.getJobName());
        trigger.setJobName(this.getJobName());
        trigger.setJobGroup(this.getJobGroupName());
        return trigger;
    }

}
