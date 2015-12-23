/*
 * $Id: SchedulerExecutor.java, 2015-11-9 ����02:00:54 Administrator Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

import java.util.List;

/**
 * <p>
 * Title: SchedulerExecutor
 * </p>
 * <p>
 * Description:
 *      ��������������ӿ�
 * </p>
 * 
 * @author 
 * created 2015-11-9 ����02:00:54
 * modified [who date description]
 * check [who date description]
 */
public interface SchedulerExecutor {
    /**
     * �����������������
     */
    public void start();
    
    /**
     * �رյ������������
     */
    public void shutdown();
    
    /**
     * ��������������Ƿ��Ѿ�����
     * @return
     *       true:��������������Ѿ�����
     *       false:����������������ر�
     */
    public boolean isStarted();
    
    /**
     * ��������������Ƿ��Ѿ��ر�
     * @return
     *       true:����������������ر�
     *       false:�������������������
     */
    public boolean isShutdown();
    
    /**
     * ������������
     * @param ��������
     */
    public void startSchedulerJob(SchedulerJob schedulerJob);
    /**
     * �رյ�������
     * @param jobName
     *        ��������
     * @param groupName
     *        ����������
     */
    public void shutdownSchedulerJob(String jobName, String groupName);
    
    /**
     * ��ͣ��������
     * @param jobName
     *        ��������
     * @param groupName
     *        ����������
     */
    public void pauseSchedulerJob(String jobName, String groupName);
    
    
    /**
     * �������õ�������
     * @param ��������
     */
    public void resetSchedulerJob(SchedulerJob schedulerJob);
    
    /**
     * �ָ���������
     * @param jobName
     *          ��������
     * @param jobGroupName
     *          ����������
     */
    public void resumeSchedulerJob(String jobName, String jobGroupName);
    /**
     * ���������Ƿ񱻹ر�
     * @param jobName
     *          ��������
     * @param jobGroupName
     *          ����������
     * @return
     *         true:�������񱻹ر�
     *         false:����������������
     *        
     */
    public boolean isSchedulerJobShutdown(String jobName, String jobGroupName);
    
    /**
     * ���������Ƿ���ͣ
     * @param jobName
     *        ��������
     * @param jobGroupName
     *        ����������
     * @return
     */
    public boolean isSchedulerJobPaused(String jobName, String jobGroupName);
    /**
     * ��ѯ��������
     * @param jobName
     *        ������������
     * @param groupName
     *        ����������
     * @return  
     *        ��������
     */
    public SchedulerJob getSchedulerJob(String jobName, String groupName);
    
    /**
     * ��ѯ���е�������
     * @return
     *       ��������
     */
    public List<SchedulerJob> getAllSchedulerJobs();
    /**
     * �������������Ʋ�ѯ��������
     * @param groupName
     *        ����������
     * @return
     *        ��������
     */
    public List<SchedulerJob> getSchedulerJobs(String groupName);
 
}
