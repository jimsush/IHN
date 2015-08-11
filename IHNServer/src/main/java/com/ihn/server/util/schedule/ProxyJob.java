package com.ihn.server.util.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;


final public class ProxyJob implements StatefulJob {

    public static final String SCHEDULER_JOB = "SCHEDULER_JOB";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        SchedulerJob schedulerJob = (SchedulerJob) context.getJobDetail().getJobDataMap().get(SCHEDULER_JOB);
        if (schedulerJob.isPaused())
            return;
        if (schedulerJob != null)
            schedulerJob.execute();
    }

}
