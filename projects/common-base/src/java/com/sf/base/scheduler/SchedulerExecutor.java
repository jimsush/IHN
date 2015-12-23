/*
 * $Id: SchedulerExecutor.java, 2015-11-9 下午02:00:54 Administrator Exp $
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
 *      调度任务管理器接口
 * </p>
 * 
 * @author 
 * created 2015-11-9 下午02:00:54
 * modified [who date description]
 * check [who date description]
 */
public interface SchedulerExecutor {
    /**
     * 启动调度任务管理器
     */
    public void start();
    
    /**
     * 关闭调度任务管理器
     */
    public void shutdown();
    
    /**
     * 调度任务管理器是否已经启动
     * @return
     *       true:调度任务管理器已经启动
     *       false:调度任务管理器被关闭
     */
    public boolean isStarted();
    
    /**
     * 调度任务管理器是否已经关闭
     * @return
     *       true:调度任务管理器被关闭
     *       false:调度任务管理器在运行
     */
    public boolean isShutdown();
    
    /**
     * 启动调度任务
     * @param 调度任务
     */
    public void startSchedulerJob(SchedulerJob schedulerJob);
    /**
     * 关闭调度任务
     * @param jobName
     *        任务名称
     * @param groupName
     *        任务组名称
     */
    public void shutdownSchedulerJob(String jobName, String groupName);
    
    /**
     * 暂停调度任务
     * @param jobName
     *        任务名称
     * @param groupName
     *        任务组名称
     */
    public void pauseSchedulerJob(String jobName, String groupName);
    
    
    /**
     * 重新设置调度任务
     * @param 调度任务
     */
    public void resetSchedulerJob(SchedulerJob schedulerJob);
    
    /**
     * 恢复调度任务
     * @param jobName
     *          任务名称
     * @param jobGroupName
     *          任务组名称
     */
    public void resumeSchedulerJob(String jobName, String jobGroupName);
    /**
     * 调度任务是否被关闭
     * @param jobName
     *          任务名称
     * @param jobGroupName
     *          任务组名称
     * @return
     *         true:调度任务被关闭
     *         false:调度任务正在运行
     *        
     */
    public boolean isSchedulerJobShutdown(String jobName, String jobGroupName);
    
    /**
     * 调度任务是否暂停
     * @param jobName
     *        任务名称
     * @param jobGroupName
     *        任务组名称
     * @return
     */
    public boolean isSchedulerJobPaused(String jobName, String jobGroupName);
    /**
     * 查询调度任务
     * @param jobName
     *        调度任务名称
     * @param groupName
     *        任务组名称
     * @return  
     *        调度任务
     */
    public SchedulerJob getSchedulerJob(String jobName, String groupName);
    
    /**
     * 查询所有调度任务
     * @return
     *       调度任务
     */
    public List<SchedulerJob> getAllSchedulerJobs();
    /**
     * 根据任务组名称查询调度任务
     * @param groupName
     *        任务组名称
     * @return
     *        调度任务
     */
    public List<SchedulerJob> getSchedulerJobs(String groupName);
 
}
