package com.ihn.server.util.schedule;

import org.quartz.Trigger;
import org.quartz.TriggerUtils;


public abstract class ImmediateSchedulerJob extends AbstractSchedulerJob {
    private int repeatCount;
    private long repeatInterval;

    public ImmediateSchedulerJob(int repeatCount, long repeatInterval) {
        super();
        this.repeatCount = repeatCount;
        this.repeatInterval = repeatInterval;

    }

    @Override
    public Trigger getTrigger() {
        Trigger trigger = TriggerUtils.makeImmediateTrigger(repeatCount - 1,// ������������������ִ��һ��(���ظ������Ϲ���͹��ڿ��ܲ�һ��)��������Ҫ��1
                repeatInterval);
        trigger.setName(this.getJobName());
        trigger.setGroup(this.getJobGroupName());
        trigger.setJobName(this.getJobName());
        trigger.setJobGroup(this.getJobGroupName());
        return trigger;
    }

}
