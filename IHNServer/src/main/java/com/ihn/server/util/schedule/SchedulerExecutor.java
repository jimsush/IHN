package com.ihn.server.util.schedule;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.ihn.server.util.exception.IHNException;


final public class SchedulerExecutor {

    private static SchedulerExecutor schedulerService;

    public static Scheduler scheduler;

    public synchronized static SchedulerExecutor getInstance() {
        if (schedulerService == null) {
            schedulerService = new SchedulerExecutor();
            SchedulerFactory schedFact = new StdSchedulerFactory();
            try {
                scheduler = schedFact.getScheduler();
                scheduler.start();
            } catch (SchedulerException e) {
                IHNException ihnEx=new IHNException(IHNException.CODE_SCHEDULE,e.getMessage());
                throw ihnEx;
            }

        }
        return schedulerService;
    }

    public void startScheduleJob(final SchedulerJob schedulerJob) {
        if (schedulerJob == null)
            throw new NullPointerException();
        
        String jobGroupName = AbstractSchedulerJob.DEFAULT_GROUP;
        schedulerJob.setJobGroupName(jobGroupName);
        String jobName = SchedulerJobNamingEngine.createSchedulerJobName();
        schedulerJob.setJobName(jobName);
        
        JobDetail jobDetail = new JobDetail(schedulerJob.getJobName(),
                schedulerJob.getJobGroupName(), ProxyJob.class);
        jobDetail.getJobDataMap().put(ProxyJob.SCHEDULER_JOB, schedulerJob);
        try {
            schedulerJob.init();// ��ʼ��������Դ
            final Trigger trigger=schedulerJob.getTrigger();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
        	IHNException ex = new IHNException(
            		IHNException.CODE_SCHEDULE, schedulerJob
                            .getJobGroupName()+":"+schedulerJob.getJobName());
            throw ex;
        }

    }

    public SchedulerJob getSchedulerJob(String jobName, String groupName) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobName, groupName);
            if (jobDetail == null)
                return null;
            SchedulerJob schedulerJob = (SchedulerJob) jobDetail.getJobDataMap().get(ProxyJob.SCHEDULER_JOB);
            return schedulerJob;
        } catch (SchedulerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void shutdownSchedulerJob(String jobName, String groupName) {
        if (jobName == null)
            throw new NullPointerException();
        try {
            scheduler.deleteJob(jobName, groupName);
        } catch (SchedulerException e) {
        	IHNException ex = new IHNException(IHNException.CODE_SCHEDULE,groupName+":"+jobName+" "+e.getMessage());
            throw ex;
        }
    }

    public void pauseSchedulerJob(String jobName, String groupName) {
        if (jobName == null)
            throw new NullPointerException();
        try {
            SchedulerJob schedulerJob = getSchedulerJob(jobName, groupName);
            if (schedulerJob == null)
                return;
            schedulerJob.pause();
        } catch (Exception e) {
        	IHNException ex = new IHNException(IHNException.CODE_SCHEDULE,groupName+":"+jobName+" "+e.getMessage());
            throw ex;
        }
    }

	public void resetSchedulerJob(final SchedulerJob schedulerJob) {
        if (schedulerJob == null)
            throw new NullPointerException();
        
        try {
        	Trigger trigger=scheduler.getTrigger(schedulerJob.getJobName(),schedulerJob.getJobGroupName());
        	scheduler.unscheduleJob(trigger.getJobName(), trigger.getJobGroup());
        	startScheduleJob(schedulerJob);
        } catch (Exception e) {
        	IHNException ex = new IHNException(IHNException.CODE_SCHEDULE, schedulerJob.getJobGroupName()+":"+schedulerJob.getJobName()+" "+e.getMessage());
            throw ex;
        }
    }

    
    public void resumeSchedulerJob(String jobName, String jobGroupName) {
        if (jobName == null)
            throw new NullPointerException();
        
        try {
            SchedulerJob schedulerJob = getSchedulerJob(jobName, jobGroupName);
            if (schedulerJob == null)
                return;
            schedulerJob.resume();
        } catch (Exception e) {
        	IHNException ex = new IHNException(IHNException.CODE_SCHEDULE, jobGroupName+":"+jobName+" "+e.getMessage());
        	throw ex;
        }

    }

    public boolean isShutdown(String jobName, String jobGroupName) {
        try {
            return scheduler.getJobDetail(jobName, jobGroupName) == null;
        } catch (SchedulerException e) {
        	IHNException ex = new IHNException(IHNException.CODE_SCHEDULE, jobGroupName+":"+jobName+" "+e.getMessage());
        	throw ex;
        }

    }

}
