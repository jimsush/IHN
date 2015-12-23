/*
 * $Id: OneTimeSchedulerJob.java, 2015-3-3 下午12:49:41  Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 
 * <p>
 * Title: SchedulerJobNamingEngine
 * </p>
 * <p>
 * Description:
 * <br>调度任务名称产生器，用于产生不重复的调度任务名称
 * </p>
 * 
 * @author 
 * created 2015-11-10 下午01:03:26
 * modified [who date description]
 * check [who date description]
 */
final public class SchedulerJobNamingEngine {

    private static final String DEFAULT_SCHEDULER_JOB = "DEFAULT_SCHEDULER_JOB";

    private static AtomicLong count = new AtomicLong(0);

    /**
     * 生成一个唯一的调度任务名称
     * @return
     *      调度任务名称
     */
    public static String createSchedulerJobName() {
        return DEFAULT_SCHEDULER_JOB + count.getAndIncrement();
    }

}
