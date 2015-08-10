package com.ihn.server.util.schedule;

import org.quartz.Trigger;


public interface SchedulerJob {

    void init();

    void execute();

    Trigger getTrigger();

    public void setJobName(String jobName);
    
    public void setJobGroupName(String jobGroupName);
    
    String getJobName();

    String getJobGroupName();
    
    boolean isPaused();
    
    void pause();
    
    void resume();

}
