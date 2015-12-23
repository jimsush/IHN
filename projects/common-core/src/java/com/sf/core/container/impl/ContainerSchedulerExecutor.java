/*
 * $Id: ContainerSchedulerExecutor.java, 2015-2-12 下午02:50:06 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.container.impl;

import com.sf.base.scheduler.SchedulerExecutor;
import com.sf.base.scheduler.SchedulerExecutorFactoryImpl;

/**
 * <p>
 * Title: ContainerSchedulerExecutor
 * </p>
 * <p>
 * Description: 容器自己使用的调度执行器
 * </p>
 * 
 * @author sufeng
 * created 2015-2-12 下午02:50:06
 * modified [who date description]
 * check [who date description]
 */
public class ContainerSchedulerExecutor {

    private static SchedulerExecutor schedulerExecutor;
    static{
        SchedulerExecutorFactoryImpl factory=new SchedulerExecutorFactoryImpl();
        schedulerExecutor=factory.createSchedulerExecutor();
    }
    
    public static SchedulerExecutor getSchedulerExecutor(){
        return schedulerExecutor;
    }
}
