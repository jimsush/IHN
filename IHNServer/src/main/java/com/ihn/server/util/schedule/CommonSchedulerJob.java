package com.ihn.server.util.schedule;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.Trigger;

public abstract class CommonSchedulerJob extends AbstractSchedulerJob {

    private String cronTrigger = null;
    private Date startTime = null;
    private Date endTime = null;

    /**
     * 
     * @param cronTrigger
     */
    public CommonSchedulerJob(String cronTrigger) {
        super();
        if (cronTrigger == null)
            throw new NullPointerException();
        this.cronTrigger = cronTrigger;
    }

    /**
     * 
     * @param startTime
     *            
     * @param endTime
     *           
     * @param cronTrigger
     */
    public CommonSchedulerJob(Date startTime, Date endTime, String cronTrigger) {
        super();
        if (startTime == null || endTime == null || cronTrigger == null)
            throw new NullPointerException();
        this.startTime = startTime;
        this.endTime = endTime;
        this.cronTrigger = cronTrigger;

    }

    @Override
    public Trigger getTrigger() {
        CronTrigger trigger;
        try {
            trigger = new CronTrigger(this.getJobName(), this.getJobGroupName(), cronTrigger);
            if (startTime != null)
                trigger.setStartTime(startTime);
            if (endTime != null)
                trigger.setEndTime(endTime);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return trigger;
    }

}
