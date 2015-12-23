/*
 * $Id: CommonSchedulerJob.java, 2015-2-23 ����10:34:56  Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronTrigger;
import org.quartz.Trigger;

/**
 * <p>
 * Title: CommonSchedulerJob
 * </p>
 * <p>
 * Description: ͨ�õ�������;��Ҫ���ݵ���������Ҫ����cronTrigger�ַ���; <br>
 * ���ʽ���Բο�http://airdream.javaeye.com/blog/53472 <br>
 * "s m h d w y" <br>
 * �� 0-59,�� 0-59,Сʱ 0-23,���� 1-31,�·� 1-12,���� 1-7 �꣨��ѡ������,1970-2099 <br>
 * 0/n��ʾÿn��ʱ����ִ��һ�Σ�1,3,5,7,10��ʾ��1,3,5,7,10ʱִ��һ��
 * </p>
 * 
 * @author 
 * created 2015-2-23 ����10:34:56
 * modified [who date description]
 * check [who date description]
 */
public abstract class CommonSchedulerJob extends AbstractSchedulerJob {

    private final String cronTrigger;
    private final Date startTime;
    private final Date endTime;

    /**
     * 
     * @param cronTrigger
     *        ������ȴ������ַ���
     */
    public CommonSchedulerJob(String cronTrigger) {
        super();
        if (cronTrigger == null)
            throw new NullPointerException("cronTrigger is null");
        this.cronTrigger = cronTrigger;
        this.startTime=null;
        this.endTime=null;
    }
    /**
     * 
     * @param jobName
     *        ��������
     * @param groupName
     *        �������ڵ�����������
     * @param cronTrigger
     *         ������ȴ������ַ���
     */
    public CommonSchedulerJob(String jobName,String groupName,String cronTrigger) {
        super(jobName,groupName);
        if (cronTrigger == null)
            throw new NullPointerException("cronTrigger is null");
        this.cronTrigger = cronTrigger;
        this.startTime=null;
        this.endTime=null;
    }
    
    /**
     * 
     * @param startTime
     *            ��ʼʱ��
     * @param endTime
     *            ����ʱ��
     * @param cronTrigger
     *           ������ȴ������ַ���
     */
    public CommonSchedulerJob(Date startTime, Date endTime, String cronTrigger) {
        super();
        if (cronTrigger == null)
            throw new NullPointerException("cronTrigger is null");
        this.startTime = new Date(startTime.getTime());
        this.endTime = new Date(endTime.getTime());
        this.cronTrigger = cronTrigger;

    }
    
    
    /**
     * 
     * @param startTime
     *            ��ʼʱ��
     * @param endTime
     *            ����ʱ��
     * @param jobName
     *            ��������
     * @param groupName
     *            ����������
     * @param cronTrigger
     *           ������ȴ������ַ���
     */
    public CommonSchedulerJob(String jobName,String groupName,Date startTime, Date endTime, String cronTrigger) {
        super(jobName,groupName);
        if (cronTrigger == null)
            throw new NullPointerException("cronTrigger is null");
        this.startTime = new Date(startTime.getTime());
        this.endTime = new Date(endTime.getTime());
        this.cronTrigger = cronTrigger;

    }

    /**
     * 
     * @see com.sf.base.scheduler.SchedulerJob#getTrigger()
     */
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
