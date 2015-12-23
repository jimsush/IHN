/*
 * $Id: ScheduleJob.ja+va, 2015-2-18 上午09:40:46  Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

import org.quartz.Trigger;

/**
 * <p>
 * Title: ScheduleJob
 * </p>
 * <p>
 * Description: 
 * <br>调度任务接口
 * </p>
 * 
 * @author 
 * created 2015-2-18 上午09:40:46
 * modified [who date description]
 * check [who date description]
 */
public interface SchedulerJob {

    /**
     * 初始化调度任务资源
     */
    void init();

    /**
     * 销毁调度任务资源
     */
    void destory();
    /**
     * 任务执行方法体,根据具体需要实现
     */
    void execute();

    /**
     * 获取该任务执行的Trigger
     * 
     * @return 任务Trigger
     */
    Trigger getTrigger();

    /**
     * 获取调度任务名称
     * 
     * @return
     *       调度任务名称
     */
    String getJobName();

    /**
     * 获取调度任务所在组的名称
     * 
     * @return
     *     调度任务所在的组的名称
     */
    String getJobGroupName();
    
    /**
     * 调度任务是否结束，通过该方法实现判断任务结束的业务逻辑
     * @return
     *     true 调度任务管理器会关闭当前调度任务
     *     false:调度任务继续执行
     *      
     */
    boolean needFinished();
    
}
