package com.ihn.server.util.schedule;

import org.quartz.Scheduler;

public abstract class AbstractSchedulerJob implements SchedulerJob {
    public final static String DEFAULT_GROUP = Scheduler.DEFAULT_GROUP;
    private String jobName;
    private String jobGroupName;

    private volatile boolean paused;

    public AbstractSchedulerJob() {
        super();
    }


    public void init() {

    }

    @Override
    public String getJobGroupName() {
        return jobGroupName;
    }

    @Override
    public String getJobName() {
        return jobName;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }
    
    public void setJobName(String jobName){
    	this.jobName=jobName;
    }
    
    public void setJobGroupName(String jobGroupName){
    	this.jobGroupName=jobGroupName;
    }

}
