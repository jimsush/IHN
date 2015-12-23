/*
 * $Id: ProxyJob.java, 2015-2-18 下午12:47:24  Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 * <p>
 * Title: ProxyJob
 * </p>
 * <p>
 * Description:任务
 * </p>
 * 
 * @author 
 * created 2015-2-18 下午12:47:24
 * modified [who date description]
 * check [who date description]
 */
final public class ProxyJob implements StatefulJob {

    public static final String SCHEDULER_JOB = "SCHEDULER_JOB";
    public static final String SCHEDULER_EXECUTOR="SCHEDULER_EXECUTOR";
    /**
     * 
     * (non-Javadoc)
     * 
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerJob schedulerJob = (SchedulerJob) context.getJobDetail().getJobDataMap().get(SCHEDULER_JOB);
        SchedulerExecutor schedulerExecutor=(SchedulerExecutor)context.getJobDetail().getJobDataMap().get(SCHEDULER_EXECUTOR);
        try{
        if (schedulerJob != null&&!schedulerJob.needFinished())
            schedulerJob.execute();
        }finally{
            if(schedulerJob!=null && schedulerJob.needFinished()){//如果调度任务结束，直接关闭调度任务
                schedulerJob.destory();
                schedulerExecutor.shutdownSchedulerJob(schedulerJob.getJobName(), schedulerJob.getJobGroupName());
            }
              
        }
    }

}
