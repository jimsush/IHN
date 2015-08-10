package com.ihn.server.util.schedule;

import java.util.Date;

import org.quartz.SimpleTrigger;
import org.quartz.Trigger;


public abstract class OneTimeSchedulerJob extends AbstractSchedulerJob {

    private Date startTime;

    /**
     * @param startTime
     *          
     */
    public OneTimeSchedulerJob(Date startTime) {
        this.startTime = startTime;
    }

    @Override
    public Trigger getTrigger() {
        Trigger trigger = new SimpleTrigger();
        if (startTime == null)
            trigger.setStartTime(new Date());
        else
            trigger.setStartTime(startTime);
        trigger.setName(this.getJobName());
        trigger.setGroup(getJobGroupName());
        trigger.setJobName(this.getJobName());
        trigger.setJobGroup(this.getJobGroupName());
        return trigger;
    }

}
