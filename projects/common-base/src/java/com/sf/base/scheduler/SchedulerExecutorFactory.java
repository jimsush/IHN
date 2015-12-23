/*
 * $Id: SchedulerExecutorFactory.java, 2015-11-10 上午09:33:52 Administrator Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

/**
 * <p>
 * Title: SchedulerExecutorFactory
 * </p>
 * <p>
 * Description:
 * <br>调度管理器工厂接口
 * </p>
 * 
 * @author 
 * created 2015-11-10 上午09:33:52
 * modified [who date description]
 * check [who date description]
 */
public interface SchedulerExecutorFactory {
    
    /**
     * 创建调度任务管理
     * @return
     *        调度任务管理器
     */
    public SchedulerExecutor createSchedulerExecutor();


}
