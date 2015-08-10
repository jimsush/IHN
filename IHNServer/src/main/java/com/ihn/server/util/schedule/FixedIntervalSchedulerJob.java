package com.ihn.server.util.schedule;

import java.util.Date;

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerUtils;


public abstract class FixedIntervalSchedulerJob extends AbstractSchedulerJob {
    protected Date starttime;
    protected Date endtime;
    protected volatile int interval;
    
    /**
     * 
     * 
     * @param starttime
     *            
     * @param endtime
     *            
     * @param interval
     *           
     */
    public FixedIntervalSchedulerJob(Date starttime, Date endtime, int interval) {
        super();
        this.starttime = starttime;
        this.endtime = endtime;
        this.interval = interval;
    }
    
    
    public void setInterval(int interval) {
    	if(this.interval==interval)
    		return;
        this.interval = interval;
    }

   public Integer getInterval() {
        return interval;
    }

    @Override
    public Trigger getTrigger() {
        SimpleTrigger trigger = (SimpleTrigger)TriggerUtils.makeSecondlyTrigger(interval);
        trigger.setMisfireInstruction(Trigger.INSTRUCTION_SET_TRIGGER_COMPLETE);
        if (starttime == null)
            trigger.setStartTime(new Date());
        else
            trigger.setStartTime(starttime);
        if (endtime != null)
            trigger.setEndTime(endtime);
        trigger.setName(this.getJobName());
        trigger.setJobName(this.getJobName());
        trigger.setJobGroup(this.getJobGroupName());
        return trigger;
    }

}
