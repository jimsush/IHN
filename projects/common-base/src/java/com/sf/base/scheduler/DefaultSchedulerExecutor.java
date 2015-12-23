/*
 * $Id: DefaultSchedulerExecutor.java, 2015-11-9 下午02:11:38 Administrator Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * <p>
 * Title: DefaultSchedulerExecutor
 * </p>
 * <p>
 * Description: 默认的任务调度管理器实现
 * </p>
 * 
 * @author 
 * created 2015-11-9 下午02:11:38
 * modified [who date description]
 * check [who date description]
 */
class DefaultSchedulerExecutor implements SchedulerExecutor {
    
    /**Quartz 调度器*/
    private Scheduler scheduler;
    
    /**缓存用于被暂停的任务 */
    private Map<String,Map<String,SchedulerJob>> pausedSchedulerJobs=new ConcurrentHashMap<String,Map<String,SchedulerJob>>();
    
    /**
     * @see com.sf.base.scheduler.SchedulerExecutor#start()
     */
    @Override
    public void start() {
        SchedulerFactory schedFact = new StdSchedulerFactory();
        try {
            scheduler = schedFact.getScheduler();
            scheduler.start();
        } catch (SchedulerException e) {
            SchException ex = new SchException(SchException.INIT_SCHEDULER_JOB, e);
            throw ex;
        }
    }

    /**
     * @see com.sf.base.scheduler.SchedulerExecutor#shutdown()
     */
    @Override
    public void shutdown() {
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            SchException ex = new SchException(SchException.SHUTDOWN_SCHEDULER_JOB, e);
            throw ex;
        }
    }

    @Override
    public boolean isStarted() {
        try {
            return scheduler.isStarted();
        } catch (SchedulerException e) {
            throw new SchException(e);
        }
    }

    @Override
    public void startSchedulerJob(SchedulerJob schedulerJob) {
        if (schedulerJob == null)
            throw new NullPointerException();
        SchedulerJob job=this.getSchedulerJob(schedulerJob.getJobName(), schedulerJob.getJobGroupName());
        if(job!=null)
            throw new IllegalArgumentException("jobGroup:"+schedulerJob.getJobGroupName()+" jobName:"+schedulerJob.getJobName()+" has been existed");
        JobDetail jobDetail = new JobDetail(schedulerJob.getJobName(), schedulerJob.getJobGroupName(), ProxyJob.class);
        jobDetail.getJobDataMap().put(ProxyJob.SCHEDULER_JOB, schedulerJob);
        jobDetail.getJobDataMap().put(ProxyJob.SCHEDULER_EXECUTOR, this);
        try {
            schedulerJob.init();// 初始化调度资源
            scheduler.scheduleJob(jobDetail, schedulerJob.getTrigger());
        } catch (SchedulerException e) {
            SchException ex = new SchException(SchException.START_SCHEDULER_JOB, e, schedulerJob.getJobGroupName(),
                    schedulerJob.getJobName());
            throw ex;
        }

    }

    @Override
    public boolean isShutdown() {
        try {
            return scheduler.isShutdown();
        } catch (SchedulerException e) {
            throw new SchException(e);
        }
    }

    @Override
    public void shutdownSchedulerJob(String jobName, String groupName) {
        if (jobName == null)
            throw new NullPointerException("jobName is null");
        try {
            scheduler.deleteJob(jobName, groupName);
        } catch (SchedulerException e) {
            SchException ex = new SchException(SchException.SHUTDOWN_SCHEDULER_JOB, e, groupName, jobName);
            throw ex;
        }
    }

    @Override
    public List<SchedulerJob> getAllSchedulerJobs() {
        List<SchedulerJob> schedulerJobs=new ArrayList<SchedulerJob>();
        try {
            String[] groupNames=scheduler.getJobGroupNames();
            for(String groupName:groupNames){
                schedulerJobs.addAll(getSchedulerJobs(groupName));
            }

        } catch (SchedulerException ex) {
            throw new RuntimeException(ex);
        }
        return schedulerJobs;
    }

    @Override
    public SchedulerJob getSchedulerJob(String jobName, String groupName) {
        try {
            JobDetail jobDetail = scheduler.getJobDetail(jobName, groupName);
            if (jobDetail == null)
                return null;
            SchedulerJob schedulerJob = (SchedulerJob) jobDetail.getJobDataMap().get(ProxyJob.SCHEDULER_JOB);
            return schedulerJob;
        } catch (SchedulerException ex) {
           throw new SchException(ex);
        }
    }

    @Override
    public List<SchedulerJob> getSchedulerJobs(String groupName) {
        List<SchedulerJob> schedulerJobs=new ArrayList<SchedulerJob>();
        try {
            String[] jobNames=scheduler.getJobNames(groupName);
            for(String jobName:jobNames){
                SchedulerJob schedulerJob=getSchedulerJob(jobName, groupName);
                schedulerJobs.add(schedulerJob);
            }
        } catch (SchedulerException ex) {
            throw new SchException(ex);
        }
        return schedulerJobs;
    }

    @Override
    public boolean isSchedulerJobPaused(String jobName, String jobGroupName) {
        if(pausedSchedulerJobs.get(jobGroupName)==null)
            return false;
        if(pausedSchedulerJobs.get(jobGroupName).get(jobName)==null)
            return false;
        return true;
    }

    @Override
    public boolean isSchedulerJobShutdown(String jobName, String jobGroupName) {
        if(isSchedulerJobPaused(jobName,jobGroupName))
            return false;
        try {
            return scheduler.getJobDetail(jobName, jobGroupName) == null;
        } catch (SchedulerException e) {
            SchException ex = new SchException(SchException.COMMON_SCHEDULER_JOB, e, jobGroupName, jobName);
            throw ex;
        }

    }

    @Override
    public void pauseSchedulerJob(String jobName, String jobGroupName) {
        if (jobName == null|| jobGroupName==null)
            throw new NullPointerException();
        try {
            SchedulerJob schedulerJob= this.getSchedulerJob(jobName, jobGroupName);
            boolean flag=scheduler.deleteJob(jobName, jobGroupName);
            if(flag){
                if(!pausedSchedulerJobs.containsKey(jobGroupName)){
                    pausedSchedulerJobs.put(jobGroupName, new HashMap<String,SchedulerJob>());
                }
                
                pausedSchedulerJobs.get(jobGroupName).put(jobName, schedulerJob);
            }else{
                SchException ex = new SchException(SchException.PAUSE_SCHEDULER_JOB, jobGroupName, jobName);
                throw ex;
            }
        } catch (Exception e) {
            SchException ex = new SchException(SchException.PAUSE_SCHEDULER_JOB, e, jobGroupName, jobName);
            throw ex;
        }
    }

    @Override
    public void resumeSchedulerJob(String jobName, String jobGroupName) {
        if (jobName == null)
            throw new NullPointerException();
        try {
            SchedulerJob schedulerJob= pausedSchedulerJobs.get(jobGroupName).get(jobName);
            if(schedulerJob==null){
                SchException ex = new SchException(SchException.RESUME_SCHEDULER_JOB, jobGroupName, jobName);
                throw ex;
            }
            this.startSchedulerJob(schedulerJob);
            pausedSchedulerJobs.get(jobGroupName).remove(jobName);
            if(pausedSchedulerJobs.get(jobGroupName).size()==0)
                pausedSchedulerJobs.remove(jobGroupName);
        } catch (Exception e) {
            SchException ex = new SchException(SchException.RESUME_SCHEDULER_JOB, e, jobGroupName, jobName);
            throw ex;
        }

    }

    /**
     * 当前实现有些问题！重设之后之前的quartz触发器依然有可能会运行几次
     * @see com.sf.base.scheduler.SchedulerExecutor#resetSchedulerJob(com.sf.base.scheduler.SchedulerJob)
     */
    @Override
    public void resetSchedulerJob(SchedulerJob schedulerJob) {
        if (schedulerJob == null)
            throw new NullPointerException();
        try {
            scheduler.rescheduleJob(schedulerJob.getJobName(), schedulerJob.getJobGroupName(), schedulerJob.getTrigger());
        } catch (Exception e) {
            SchException ex = new SchException(SchException.RESET_SCHEDULER_JOB, e, schedulerJob.getJobGroupName(),
                    schedulerJob.getJobName());
            throw ex;
        }
    }

}
